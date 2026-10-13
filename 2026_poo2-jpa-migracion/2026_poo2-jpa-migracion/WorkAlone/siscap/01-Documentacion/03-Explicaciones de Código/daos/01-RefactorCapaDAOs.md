# Refactor: capa DAO genérica sobre JDBC

## ¿Qué cambia en esta versión?

Antes, cada una de las 8 tablas tenía su DAO *a mano*: cada uno abría su
propia conexión, repetía su `SELECT`, su `INSERT`, su `UPDATE` y su borrado, y
expresaba lo mismo con nombres distintos (`obtenerTecnologiaPorId`,
`obtenerPersonaPorId`, `agregarFramework(...)`, ...).

Ahora existe **una sola implementación de acceso a datos** que se reutiliza 8
veces. Cada DAO ya no escribe SQL propio de operación: solo declara qué tabla,
qué columnas y cómo mapear una fila.

La refactorización se apoya en 4 piezas nuevas del paquete `modelo`:

| Pieza | Archivo | Rol |
| --- | --- | --- |
| Contrato de entidad | `entidades/IEntidad` | Toda entidad guardable debe saber su `id` y su `activo`. |
| Contrato de acceso | `daos/interf/IBaseDAO<T>` | Las operaciones CRUD que cualquier catálogo debe ofrecer. |
| Implementación común | `daos/impl/DAOBase<T>` | Template Method: la lógica JDBC vive UNA sola vez. |
| Constructor de SQL | `utils/SQLQueryBuilder` | Genera las consultas parametrizadas a partir de la lista de campos. |

Junto con estas piezas: las 8 entidades ahora usan `Long id` e implementan
`IEntidad`, las interfaces de DAO heredan de `IBaseDAO<T>` y los 8 DAOs se
acotan a implementar unos pocos puntos de extensión (`hooks`).

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
cuatro operaciones: leer y escribir su `id` (ahora `Long`, no `int`/`Integer`)
y su bandera `activo`:

```java
public interface IEntidad {
    Long getId();
    void setId(Long id);
    boolean isActivo();
    void setActivo(boolean activo);
}
```

Tener **un solo tipo de id en todo el modelo** simplifica la capa genérica: el
`DAOBase` puede pedir `Long id` sin preguntar quién es.

## 2. `IBaseDAO<T>` — el contrato de acceso a datos

Interfaz genérica con limitador de tipo: `T` debe ser una `IEntidad`
(`<T extends IEntidad>`). Agrupa las operaciones de cualquier catálogo:

- **Conexión**: `conectar()`, `desconectar()`, `estaConectado()`.
- **Consultas**: `obtenerPorId(Long)`, `obtenerTodas()`, `obtenerPorCampo(String, Object)`.
- **Existencia**: `existeConId(Long)`, `existeConCampo(String, Object)`.
- **Mutación**: `agregar(T)`, `actualizar(T)`, `actualizarTodos(List<T>)`.
- **Borrado lógico**: `borrar(Long)`, `borrar(T)`, `borrarTodos(List<T>)`.
- **Borrado físico**: `borradoFisico(Long)`, `borradoFisico(T)`, `borradoFisico(List<T>)`, `borradoFisicoPorIds(List<Long>)`.

Observa que hay **dos borrados**:

- `borrar(...)` es un **borrado lógico** (`UPDATE ... SET activo = false`);
- `borradoFisico(...)` elimina la fila de verdad (`DELETE ...`).

## 3. `DAOBase<T>` — el Template Method

`DAOBase<T>` implementa TODA la lógica JDBC que antes estaba duplicada:
abrir/cerrar conexión, `PreparedStatement` con try-with-resources, recorrer el
`ResultSet`, lanzar la excepción de negocio. De lo único que se entera cada
DAO concreto es de **su** tabla, y lo declara en 6 *hooks*:

```java
protected abstract String getNombreTabla();          // "tecnologias"
protected abstract List<String> getCampos();         // [nombre, categoriaGeneral, ...]
protected abstract String getSqlCrearTabla();        // el DDL CREATE TABLE IF NOT EXISTS
protected abstract T mapearEntidad(ResultSet rs);    // fila -> objeto
protected abstract void setParametrosInsert(PreparedStatement ps, T e);
protected abstract void setParametrosUpdate(PreparedStatement ps, T e);
```

El flujo ya está escrito en la superclase. Por ejemplo, `agregar(T)`:
`obtenerPorId(Long)` etc. son métodos **concretos** del `DAOBase` que solo usan
los hooks. Un `TecnologiaDAO` concreto queda así de corto:

```java
public class TecnologiaDAO extends DAOBase<Tecnologia> implements ITecnologiaDAO {
    @Override protected String getNombreTabla() { return TABLA_TECNOLOGIAS; }

    @Override protected List<String> getCampos() {
        return Arrays.asList(FLD_NOMBRE, FLD_CATEGORIA_GENERAL, FLD_FABRICANTE_COMUNIDAD, FLD_DESCRIPCION);
    }

    @Override protected String getSqlCrearTabla() { /* DDL portado de la versión anterior */ }

    @Override protected Tecnologia mapearEntidad(ResultSet rs) throws SQLException {
        return new Tecnologia(rs.getLong(FLD_ID), rs.getBoolean(FLD_ACTIVO),
                rs.getString(FLD_NOMBRE), rs.getString(FLD_CATEGORIA_GENERAL),
                rs.getString(FLD_FABRICANTE_COMUNIDAD), rs.getString(FLD_DESCRIPCION));
    }

    @Override protected void setParametrosInsert(PreparedStatement ps, Tecnologia t) throws SQLException {
        ps.setString(1, t.getNombre());
        ps.setString(2, t.getCategoriaGeneral());
        ps.setString(3, t.getFabricanteComunidad());
        ps.setString(4, t.getDescripcion());
    }

    @Override protected void setParametrosUpdate(PreparedStatement ps, Tecnologia t) throws SQLException {
        setParametrosInsert(ps, t);   // aquí los campos de update son los mismos que insert
    }
}
```

Detalles que el `DAOBase` ya resuelve por ti:

- `conectar()` también crea la tabla si no existe (ejecuta `getSqlCrearTabla()`),
  así la base de datos se auto-construye la primera vez.
- En `agregar`, el parámetro `activo` se escribe en la posición
  `getCampos().size() + 1` (el INSERT no lleva `id`: es AUTOINCREMENT).
- En `actualizar`, `activo` va en `size() + 1` y `id` (WHERE) en `size() + 2`.

## 4. `SQLQueryBuilder` — el SQL se genera, no se escribe

El builder conoce tres invariantes de este esquema: columna `id`, columna
`activo` y el hecho de que los repositorios se ordenan por `nombre`. Con la
lista de campos registrada con `addField(...)` genera, por ejemplo, para la
tabla `tecnologias`:

```sql
-- INSERT (sin id: autoincrement, activo al final)
INSERT INTO tecnologias (nombre, categoriaGeneral, fabricanteComunidad, descripcion, activo)
VALUES (?, ?, ?, ?, ?)

-- SELECT por id (solo filas activas)
SELECT id, nombre, categoriaGeneral, fabricanteComunidad, descripcion, activo
FROM tecnologias WHERE id = ? AND activo = true

-- SELECT por campo
SELECT ... FROM tecnologias WHERE nombre = ? AND activo = true

-- SELECT todas (solo activas, ordenadas)
SELECT ... FROM tecnologias WHERE activo = true ORDER BY nombre ASC

-- UPDATE y borrado lógico
UPDATE tecnologias SET nombre = ?, ..., activo = ? WHERE id = ?
UPDATE tecnologias SET activo = false WHERE id = ?

-- borrado físico
DELETE FROM tecnologias WHERE id = ?
```

Fíjate que **todas** las consultas son parametrizadas (`?`): no se concatenan
valores dentro del SQL, lo que además elimina la inyección SQL.

## 5. Cómo quedaron los programas que usan los DAOs

Con la capa genérica, los CRUDs y las Ingestas ya no conocen nombres
particulares:

```java
// CRUD: antes vs. ahora
Tecnologia t = TECNOLOGIA_DAO.obtenerTecnologiaPorId(id);  // antes
Tecnologia t = TECNOLOGIA_DAO.obtenerPorId(id);            // ahora

List<Tecnologia> ts = TECNOLOGIA_DAO.obtenerTodasLasTecnologias(); // antes
List<Tecnologia> ts = TECNOLOGIA_DAO.obtenerTodas();               // ahora

// Agregar: hoy se construye la entidad y se entrega el objeto completo
TECNOLOGIA_DAO.agregar(new Tecnologia(null, ACTIVO, nombre, catGeneral, fabricante, descripcion));
```

Las Ingestas aprovechan el borrado físico para limpiar la tabla antes de
sembrar datos:

```java
tecnologiaDAO.conectar();
tecnologiaDAO.borradoFisico(tecnologiaDAO.obtenerTodas());
tecnologiaDAO.agregar(new Tecnologia(null, ACTIVO, "Git", "...", "Comunidad", "..."));
// ...
tecnologiaDAO.desconectar();
```

## Conceptos que esta refactorización ilustra

### 1. Template Method, no herencia de conveniencia
La superclase define el *esqueleto* del algoritmo (JDBC completo) y delega los
pasos variables en hooks. El detalle de cada tabla no se repite, se *cuelga*
de un punto de extensión.

### 2. Generics con acotación de tipo
`DAOBase<T extends IEntidad>` permite que `obtenerTodas()` devuelva
`List<Tecnologia>` o `List<Persona>` con una sola implementación, y el
compilador garantiza que `T` siempre tenga `getId()`/`isActivo()`.

### 3. Programar contra interfaces
`IBaseDAO<T>` es el contrato; `DAOBase<T>` la implementación reutilizable; cada
`IXxxDAO` extiende el contrato. El dia que cambie la forma de conectar (otra BD,
JPA), los 8 CRUDs no se tocan.

### 4. `PreparedStatement` y SQL parametrizado
Un valor se liga con `ps.setX(pos, valor)`. El `?` nunca se interpola: ni bugs
de comillas, ni inyección SQL.

### 5. Borrado lógico vs. físico
`borrar()` solo apaga `activo`. Es la versión "no romper la historia": los
registros se ocultan pero no se pierden. `borradoFisico()` es la tijera real,
apropiada para ingestas que re-siembran datos.

### 6. `Long` como identidad uniforme
Se eliminó la mezcla `int`/`Integer` por entidad: un solo tipo de id en todo el
modelo simplifica la firma genérica.

### 7. El id se genera en la base
El `INSERT` no lleva `id` (AUTOINCREMENT): la entidad llega con
`id = null` y la base lo asigna. `mapearEntidad` lo recupera con
`rs.getLong(FLD_ID)` al leer.

## Qué se eliminó en esta versión

- `ConexionSqlite` y `EntidadCatalogoDAOUtils` (clases de apoyo de la versión
  anterior).
- Las consultas SQL embebidas como constantes `SQL_*` en `Constantes` y los
  mensajes de error `ERR_*`: ahora el `DAOBase` centraliza el manejo de
  `SQLException`.
- Los métodos particulares de cada DAO (`obtenerXPorId`, `agregarX(String...)`,
  `existeXConNombre`, ...), reemplazados por el CRUD genérico.

## Para probar

```bash
mvn test                          # 49 pruebas de las bibliotecas de menú y lectura
mvn compile exec:java@ingesta-inicial        # siembra/limpia las 8 tablas
mvn compile exec:java@crud-tecnologias       # y los demás @crud-* del pom.xml
```

La lista de programas ejecutables (con sus `id`) está en el `pom.xml`,
sección del `exec-maven-plugin`.

## Preguntas para discutir en clase

1. ¿Qué pasaría si un catálogo nuevo necesitara un campo que no aparece en
   `getCampos()`? ¿Dónde se toca: en `getCampos()`, en `mapearEntidad()`, en
   `setParametrosInsert()`... o en varios? ¿Por qué son varias piezas?
2. Agregar un nuevo catálogo (por ejemplo `Categoria`) implica: entidad +
   interfaz + DAO + CRUD + Ingesta. ¿Cuántas líneas por pieza, ahora que la
   lógica vive en `DAOBase`? Compáralo con la versión anterior.
3. ¿Por qué `borrar(id)` no hace `DELETE`? ¿Qué perdería la aplicación si
   borrara físicamente una persona que aparece en reportes históricos?
4. ¿Por qué el `INSERT` no incluye la columna `id`? ¿Qué pasaría si
   `agregar()` recibiera una entidad con `id` ya asignado?
5. En `actualizar`, el `activo` se liga en la posición `size() + 1` y el `id`
   en `size() + 2`. ¿De dónde sale ese orden? Dibuja el SQL que genera el
   builder para `UPDATE`.
6. `getQueryDeleteFisicoTodo()` ya existe en el builder pero `IBaseDAO` no lo
   expone. ¿Para qué caso lo usarías? ¿Debe subir al contrato?

## Siguiente paso (sugerido)

Agregar un catálogo nuevo completo (entidad → interfaz → DAO → CRUD → Ingesta
→ execution en el `pom.xml`) usando SOLO los hooks, para demostrar en clase
que la capa genérica hace que un catálogo quepa en ~5 archivos pequeños y cero
lógica repetida.