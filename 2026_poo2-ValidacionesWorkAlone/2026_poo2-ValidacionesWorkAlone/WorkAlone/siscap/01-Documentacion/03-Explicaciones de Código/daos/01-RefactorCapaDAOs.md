# Refactor: capa DAO genérica sobre JPA

## ¿Qué cambia en esta versión?

La capa de datos conserva su forma (una implementación genérica reutilizada por los
8 catálogos) pero cambió de tecnología: de **JDBC con SQL escrito a mano** a
**JPA (EclipseLink)**. Desaparecen el `Connection`, el `PreparedStatement`, el
`ResultSet` y los *hooks* de mapeo; en su lugar hay un `EntityManager` que se
entrega por constructor.

Antes cada DAO abría su conexión y escribía su SQL (`obtenerTecnologiaPorId`,
`agregarFramework(...)`, ...). Ahora **ningún DAO escribe SQL**: todos delegan en
`EntityManager.persist/merge/remove/find` y en la Criteria API.

La refactorización se apoya en 4 piezas del paquete `modelo`:

| Pieza | Archivo | Rol |
| --- | --- | --- |
| Contrato de entidad | `entidades/IEntidad` | Toda entidad guardable debe saber su `id` y su `activo`. |
| Contrato de acceso | `daos/interf/IBaseDAO<T>` | Las operaciones CRUD que cualquier catálogo debe ofrecer. |
| Implementación común | `daos/impl/DAOBase<T>` | La lógica de persistencia y de transacción vive UNA sola vez. |
| Fábrica de EM | `utils/JPAUtil` | Construye una vez la fábrica y entrega `EntityManager` nuevos. |

Las 8 entidades usan `Integer id` e implementan `IEntidad`; las interfaces de DAO
heredan de `IBaseDAO<T>` y las 8 implementaciones se reducen a dos líneas
(ver §3).

## Vista general del mapa de clases

| Tabla | Entidad | Interfaz | Implementación | Programa CRUD | Ingesta |
| --- | --- | --- | --- | --- | --- |
| `tecnologias` | `Tecnologia` | `ITecnologiaDAO` | `TecnologiaDAO` | `CrudTecnologias` | `IngestaTecnologias` |
| `personas` | `Persona` | `IPersonaDAO` | `PersonaDAO` | `CrudPersonas` | `IngestaPersonas` |
| `prog_languages` | `ProgLanguages` | `IProgLanguagesDAO` | `ProgLanguagesDAO` | `CrudLenguajesProgramacion` | `IngestaLenguajesProgramacion` |
| `sistemas_operativos` | `SistemaOperativo` | `ISistemaOperativoDAO` | `SistemaOperativoDAO` | `CrudSistemasOperativos` | `IngestaSistemasOperativos` |
| `herramientas` | `Herramienta` | `IHerramientaDAO` | `HerramientaDAO` | `CrudHerramientas` | `IngestaHerramientas` |
| `frameworks` | `Framework` | `IFrameworkDAO` | `FrameworkDAO` | `CrudFrameworks` | `IngestaFrameworks` |
| `bases_de_datos` | `BaseDeDatos` | `IBaseDeDatosDAO` | `BaseDeDatosDAO` | `CrudBasesDeDatos` | `IngestaBasesDeDatos` |
| `habilidades_blandas` | `HabilidadBlanda` | `IHabilidadBlandaDAO` | `HabilidadBlandaDAO` | `CrudHabilidadesBlandas` | `IngestaHabilidadesBlandas` |

## 1. `IEntidad` — el contrato mínimo de una entidad

Toda entidad que se guarda en una tabla de estos catálogos se compromete a
cuatro operaciones: leer y escribir su `id` y su bandera `activo`:

```java
public interface IEntidad {
    Integer getId();
    void setId(Integer id);
    boolean isActivo();
    void setActivo(boolean activo);
}
```

Tener **un solo tipo de id en todo el modelo** simplifica la capa genérica: el
`DAOBase` puede pedir `Integer id` sin preguntar quién es. El tipo importa: ver
la trampa de §5.

## 2. `IBaseDAO<T>` — el contrato de acceso a datos

Interfaz genérica con limitador de tipo: `T` debe ser una `IEntidad`
(`<T extends IEntidad>`). Agrupa las operaciones de cualquier catálogo:

- **Alta**: `save(T)`, `saveAll(List<T>)`.
- **Consulta**: `findById(Integer)`, `findAll()`.
- **Modificación**: `update(T)`, `updateAll(List<T>)`.
- **Baja**: `delete(T)`, `deleteById(Integer)`, `deleteAll(List<T>)`.

Los nombres son los de la API de JPA a propósito: quien conoce JPA ya conoce
esta interfaz. Observa que **no hay borrado lógico**: `delete(...)` es un
`DELETE` físico. El campo `activo` sigue existiendo en las entidades, pero hoy
lo usa el usuario como un dato más, no como una política de borrado.

## 3. `DAOBase<T>` — la implementación común

`DAOBase<T>` guarda solo dos cosas: la clase de la entidad y el
`EntityManager` con el que trabaja. No hay *hooks*: el tipo concreto se recibe
en el constructor.

```java
public class DAOBase<T extends IEntidad> implements IBaseDAO<T>, Serializable {
    private EntityManager em;
    private final Class<T> claseEntidad;

    public DAOBase(Class<T> claseEntidad, EntityManager em) { ... }

    @Override public T findById(Integer id)   { return em.find(claseEntidad, id); }

    @Override public List<T> findAll() {
        CriteriaQuery<T> q = em.getCriteriaBuilder().createQuery(claseEntidad);
        q.select(q.from(claseEntidad));
        return em.createQuery(q).getResultList();
    }

    @Override public T save(T entidad)    { return executeTransactionWithResult(() -> { em.persist(entidad);  return entidad; }); }
    @Override public T update(T entidad)  { return executeTransactionWithResult(() -> em.merge(entidad)); }
    @Override public void delete(T entidad) { executeTransaction(() -> em.remove(entidad)); }
}
```

Un DAO concreto queda así de corto:

```java
public class BaseDeDatosDAO extends DAOBase<BaseDeDatos> implements IBaseDeDatosDAO {
    public BaseDeDatosDAO(EntityManager em) {
        super(BaseDeDatos.class, em);
    }
}
```

Y su interfaz no declara nada:

```java
public interface IBaseDeDatosDAO extends IBaseDAO<BaseDeDatos> {}
```

**Las transacciones** están encapsuladas en dos métodos privados que son el
*Template Method* de esta capa:

```java
private T executeTransactionWithResult(IDBExecute<T> comando) {
    try {
        em.getTransaction().begin();
        T salida = comando.executeDBCommand();
        em.getTransaction().commit();
        return salida;
    } catch (Exception e) {
        log.severe(e.getMessage());
        em.getTransaction().rollback();
        return null;
    }
}
```

Fíjate en la política: la excepción **no se propaga**; se registra, se revierte
la transacción y se devuelve `null`/`false`. Es una decisión discutible (el
programa no puede distinguir "no se guardó" de "no había nada"), pero mantiene
simples los CRUD. `IDBExecute` / `IDBJustExecute` son interfaces funcionales
usadas como *callbacks*.

## 4. Configuración: `persistence.xml` y SQLite

La unidad de persistencia es `siscapPU` (`RESOURCE_LOCAL`, EclipseLink). Tres
detalles no obvios:

- **El driver es de SQLite** (`org.sqlite.JDBC`, `jdbc:sqlite:capacitaciones.db`)
  aunque `eclipselink.target-database` diga `MySQLPlatform`. No es un descuido:
  SQLite no soporta la tabla `SEQUENCE` que EclipseLink usa para
  `GenerationType.SEQUENCE`, así que se lo engaña para que use `IDENTITY`.
- **`ddl-generation` es `none`**: el esquema no lo genera EclipseLink, tiene que
  existir en el archivo `.db` (si no, el error es `no such table: bases_de_datos`).
- **El log SQL está en `FINE`**: al ejecutar un CRUD se ven los `SELECT`,
  `INSERT` y `UPDATE` con sus parámetros. Es la forma más rápida de verificar
  qué se está guardando de verdad.

El `EntityManager` se pide siempre a la misma puerta:

```java
EntityManager em = JPAUtil.crearEntityManager();   // JPAUtil construye la fábrica una sola vez
...
JPAUtil.cerrar();                                  // cierra la fábrica al terminar el programa
```

## 5. La identidad es `Integer` (y por qué importa)

Las 8 entidades declaran la clave primaria con el generador de la base:

```java
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
@Column(name = "ID", columnDefinition = "INTEGER")
private Integer id;
```

Como el `id` **es** un `Integer`, el `DAOBase` debe recibir un `Integer`. Esta
trampa real ocupar una iteración: mientras `IBaseDAO` declaraba
`findById(Long id)` y los CRUD leían `Long id = (long) LECTOR.leerEntero(...)`,
las opciones *Actualizar* y *Eliminar* terminaban con

```
java.lang.IllegalArgumentException: You have provided an instance of an
incorrect PK class for this find operation.  Class expected : class
java.lang.Integer, Class received : class java.lang.Long.
```

`em.find` no hace conversión: exige exactamente la clase de la clave primaria.
La moraleja para la capa genérica es que **el tipo de la clave no es un detalle
de la tabla, es parte del contrato** (`IEntidad` y `IBaseDAO` deben hablar del
mismo tipo).

## 6. Cómo quedaron los programas que usan los DAOs

Los CRUD y las Ingestas ya no conocen nombres particulares:

```java
// Antes (JDBC, un método por tabla)          Ahora (JPA, la misma API para las 8)
tecnologiaDAO.obtenerTecnologiaPorId(id);     TECNOLOGIA_DAO.findById(id);
tecnologiaDAO.obtenerTodasLasTecnologias();   TECNOLOGIA_DAO.findAll();
tecnologiaDAO.agregar(t);                     TECNOLOGIA_DAO.save(t);
tecnologiaDAO.actualizar(t);                  TECNOLOGIA_DAO.update(t);
tecnologiaDAO.borrar(t);                      TECNOLOGIA_DAO.delete(t);
```

Las Ingestas limpian la tabla y la vuelven a sembrar:

```java
dao.deleteAll(dao.findAll());
dao.save(new BaseDeDatos(null, ACTIVO, "SQLite", "Relacional", "SQLite", "Motor de base de datos embebido y ligero"));
// ...
```

`IngestaInicial` es la que llama a las 8 ingestas; cada `IngestaXxx` expone
`ejecutarIngesta()` para poder invocarla sin pasar por su `main`.

## Conceptos que esta refactorización ilustra

### 1. El patrón DAO sobrevive al cambio de tecnología
`IBaseDAO<T>` no cambió de forma al pasar de JDBC a JPA: cambiaron los imports y
el cuerpo de los métodos. Eso demuestra que el patrón DAO es una frontera de
diseño útil: aísla la lógica de negocio del detalle de persistencia.

### 2. Generics con acotación de tipo
`DAOBase<T extends IEntidad>` permite que `findAll()` devuelva
`List<Tecnologia>` o `List<Persona>` con una sola implementación, y el
compilador garantiza que `T` siempre tenga `getId()`/`isActivo()`.

### 3. Template Method, no herencia de conveniencia
La superclase define el esqueleto de una operación transaccional
(begin → comando → commit, con rollback y log ante el error) y delega el paso
variable en un *callback*. Cada `save`/`update`/`delete` solo dice **qué**
operación de JPA ejecuta.

### 4. Programar contra interfaces
`IBaseDAO<T>` es el contrato; `DAOBase<T>` la implementación reutilizable; cada
`IXxxDAO` extiende el contrato sin añadir nada. Los CRUD dependen de
`IBaseDAO<T>`, no de `DAOBase`.

### 5. La Criteria API para las consultas
`findAll()` no escribe SQL a mano ni usa un `String` con JPQL: pide la consulta
al `CriteriaBuilder` tipada con `claseEntidad`. No hay concatenación de valores
en ningún punto: la inyección SQL deja de ser posible porque ya no hay SQL
escrito por nosotros.

### 6. El id se genera en la base
La entidad llega a `save(...)` con `id = null` y `IDENTITY` hace que la base le
asigne el valor; `merge` es lo que permite actualizar una entidad que ya tiene id. Por
eso `save` y `update` son operaciones distintas aunque ambas "escriban".

## Qué quedó sin uso

- `modelo/utils/SQLQueryBuilder` e `ISQLQueryBuilder`: el constructor de SQL
  escrito a mano de la versión JDBC. Ya ningún DAO los usa; son candidatos a
  borrarse.
- Las constantes de `Constantes` de la era JDBC: `FLD_*` (nombres de columna),
  `TIPO_*`, `RESTRICCION_*` y las `TABLA_*` (el `@Table` de la entidad ya
  declara la tabla).
- El borrado lógico: `activo` ya no es una política de borrado.

## Para probar

```bash
mvn test                                     # 51 pruebas (fallan solo las de Calculadora)
mvn compile exec:java@ingesta-inicial         # limpia y siembra las 8 tablas
mvn compile exec:java@crud-personas           # único CRUD con id de ejecución en el pom
mvn compile exec:java -Dexec.mainClass=mx.edu.uaz.ingsoft.poo2.programas.cruds.PruebaCruds
```

`PruebaCruds` es el `main` que instancia `CrudBasesDeDatos`; todavía no tiene un
`id` propio en el `exec-maven-plugin`, de ahí la forma de invocarlo.

Para comprobar qué se guardó de verdad, mira el log SQL (`FINE`) o abre
`capacitaciones.db` con cualquier cliente SQLite.

## Pendientes

- Migrar los 7 CRUD restantes a la plantilla `AbstractCrudEntidad<T>` (hoy solo
  `CrudBasesDeDatos` lo usa). Ver
  `03-Explicaciones de Código/menu/03-RefactorCrudConMenu.md`.
- Decidir si `executeTransaction*` debe propagar la excepción en lugar de
  devolver `null`, para que el CRUD pueda avisar al usuario.
- Registrar en el `pom.xml` un `id` de ejecución por cada CRUD migrado.

## Preguntas para discutir en clase

1. `DAOBase` implementa `Serializable` y es usada por varias clases. ¿Qué
   consecuencias tiene serializar un objeto que guarda un `EntityManager`?
2. Los callbacks transaccionales devuelven `null` ante un error y `DAOBase`
   solo lo registra. ¿Cómo le avisaría al usuario que su cambio no se guardó?
   ¿Qué cambios harían falta?
3. Se usa `MySQLPlatform` en un proyecto con SQLite. ¿Qué otros "trucos de
   compatibilidad" aparecen al elegir una base que el proveedor no soporta
   nativamente, y qué pasa si el proveedor cambia de versión?
4. `findById` es una consulta, pero no abre transacción. ¿Es correcto leer sin
   transacción? ¿Qué pasaría si el `SELECT` se hiciera dentro de una?
5. El `EntityManager` se crea una vez y se comparte (campo `static` en
   `AbstractCrudEntidad`). ¿Es seguro usarlo desde varios hilos? ¿Qué cambiarías?
6. `ddl-generation=none` obliga a que el esquema exista aparte. ¿Qué ventajas y
   qué riesgos tiene generar el esquema desde el código?

## Siguiente paso (sugerido)

Terminar la migración de los 7 CRUD que quedan a la plantilla
`AbstractCrudEntidad<T>` y borrar `SQLQueryBuilder` junto con las constantes
`FLD_*` de la era JDBC.
