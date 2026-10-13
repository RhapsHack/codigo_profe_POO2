# Guía de uso de la capa DAO

Guía práctica para usar la capa de acceso a datos tal como quedó después del
refactor: cómo se arma el objeto (*wiring*), cómo se pide un CRUD, qué hacer
con `DAOException`, y cómo paginar y buscar por campo.

Complementa a `01-RefactorCapaDAOs.md`, que explica *por qué* está así el
código; aquí nos concentramos en *cómo se usa*.

## 1. Wiring: de `JPAUtil` al controlador

La cadena de objetos se arma a mano (no hay framework de inyección):

```
JPAUtil.crearEntityManager()          una fábrica, muchos EntityManager
        │
        ▼
new PersonaDAO(em)                    DAO concreto: clase de entidad + EM
        │
        ▼
new PersonaService(personaDAO)        servicio (ServicioBase<T, U>)
        │
        ▼
new PersonaController(...)            controlador Swing / CRUD de consola
```

Un DAO concreto solo dos líneas:

```java
public class PersonaDAO extends DAOBase<Persona> implements IPersonaDAO {
    public PersonaDAO(EntityManager em) {
        super(Persona.class, em);
    }
}
```

Si no quieres armar la cadena a mano, `DaoServiceFactory` la arma por catálogo:

```java
Optional<IServicioBase> servicio = DaoServiceFactory.getServicio(Constantes.CAT_PEOPLE);
servicio.ifPresent(s -> s.guardar(persona));   // los métodos de escritura lanzan DAOException
```

## 2. El contrato `IBaseDAO<T>` de un vistazo

| Grupo | Métodos | ¿Declara `throws DAOException`? |
| --- | --- | --- |
| Lectura | `findById(Integer)`, `findAll()`, `findAll(int, int)`, `findByCampo(...)`, `findByCampoUnico(...)` | No (no cambian la base) |
| Escritura | `save`, `saveAll`, `update`, `updateAll`, `delete`, `deleteById`, `deleteAll` | **Sí** (los 7) |

```java
List<Persona> todas = dao.findAll();                 // sin throws
Persona ana = dao.findById(1);

dao.save(persona);                                    // compila solo si el caller
                                                      // maneja DAOException
```

Regla sencilla: **si el método puede modificar la base, declara
`throws DAOException`**. Las lecturas conservan la firma sin cambios.

## 3. Errores con `DAOException`

### De dónde sale

`DAOBase` envuelve cada escritura en dos *wrappers* transaccionales. Ante
cualquier fallo (violación de restricción, tabla inexistente, error de
conexión) hacen tres cosas, en este orden:

```java
private T executeTransactionWithResult(IDBExecute<T> comando) throws DAOException {
    try {
        em.getTransaction().begin();
        T salida = comando.executeDBCommand();
        em.getTransaction().commit();
        return salida;
    } catch (Exception e) {
        revertirTransaccion(e);                         // rollback; si falla, solo se loguea
        throw new DAOException(MSG_ERROR_PERSISTENCIA + ": " + e.getMessage(), e);
    }
}
```

1. **Revierte** la transacción: nada del lote queda a medias.
2. **Traduce** el error a `DAOException` con un mensaje entendible
   (`"No se pudo completar la operación en la base de datos: ..."`).
3. **Conserva la causa original** en `getCause()` para el log.

`DAOException` es *checked* (`extends Exception`) a propósito: el compilador
obliga a cada eslabón a decidir qué hacer; ningún fallo se pierde en un
`return null`.

### Cómo se propaga

```
DAOBase ──DAOException──▶ ServicioBase ──DAOException──▶ controlador / CRUD / ingestas
```

Los servicios **no capturan**: solo añaden `throws` (la firma de
`IServicioBase.guardar/actualizar/borrarRegistro` ya la incluye). Quien decide
qué ve el usuario es el nivel que toca la interfaz:

**Interfaz gráfica** (`AbstractControllerBase`): un único método privado de
presentación y captura solo en los puntos donde se toca la base:

```java
try {
    servicio.guardar(registroActual);
} catch (DAOException e) {
    mostrarErrorPersistencia(e);      // JOptionPane.showMessageDialog(null, e.getMessage())
    return;                           // la ventana NO se cierra ni se limpia
}
```

**CRUD de consola** (`AbstractCrudEntidad`): se muestra el mensaje con el
lector de texto y el menú sigue vivo:

```java
try {
    dao.deleteById(id);
} catch (DAOException error) {
    LECTOR.mostrarError(error.getMessage());
    return;                           // no se imprime "entidad borrada"
}
LECTOR.mostrarTexto(MSG_ENTIDAD_BORRADA);
```

**Carga de CSV** (`AbstractServicioCargaCSV`): se registra en el log junto con
los errores de lectura, sin cambiar la firma de `IServicioCarga`:

```java
} catch (IOException | DAOException e) {
    log.severe(e.getMessage());
}
```

**Ingestas**: cada `ejecutarIngesta()` declara `throws DAOException`; los
`main` individuales la propagan (aparece el *stack trace* completo) y
`IngestaInicial.main` captura e imprime `System.err` antes de detenerse, para
que no se reporte una siembra que no ocurrió.

### Qué NO hacer

- No envolver en `RuntimeException` ni en `Optional`: se pierde el tipo y el
  mensaje.
- No mostrar solo `e.getCause().getMessage()`: el usuario necesita el mensaje
  de contexto; la causa es para el log.
- No capturar "por si acaso" en lecturas: si `findAll()` falla, eso sí debe
  romper con el *stack trace* (no hay transacción que revertir).

## 4. Paginación: `findAll(int pagina, int tamano)`

`pagina` es el **índice de página base 0** (no el desplazamiento) y `tamano`
es cuántos registros caben en una página. El desplazamiento lo calcula el DAO:

```java
consulta.setFirstResult(pagina * tamano).setMaxResults(tamano);
```

```java
// Si la tabla tiene 5 personas:
dao.findAll(0, 2);   // [1, 2]
dao.findAll(1, 2);   // [3, 4]
dao.findAll(2, 2);   // [5]        ← la última página queda con lo que resta
dao.findAll(3, 2);   // []         ← más allá del final: lista vacía, sin error
dao.findAll();       // las 5      ← el listado completo sigue siendo findAll()
```

Argumentos inválidos lanzan `IllegalArgumentException` (no `DAOException`):
páginas negativas o tamaños no positivos son error de programación, no un
fallo de la base:

```java
dao.findAll(-1, 2);  // IllegalArgumentException
dao.findAll(0, 0);   // IllegalArgumentException
```

`findAll()` sin argumentos comparte la misma implementación privada que el
paginado (sin límites), así que el comportamiento no puede divergir entre los
dos.

## 5. Búsqueda por campo

```java
List<Persona> repetidas = dao.findByCampo("nombre", "Coincidencia");   // 0..n
Persona ana = dao.findByCampoUnico("correoElectronico", "ana@empresa.com"); // 0..1, o null
```

Detalles que importan:

- El primer argumento es un **atributo de la entidad** (`correoElectronico`),
  **no** el nombre de la columna (`correo_electronico`). El `CriteriaBuilder`
  traduce el atributo a columna solo cuando arma el SQL.
- Se arma con `cb.equal(root.get(campo), valor)`: el valor se pasa como
  parámetro, nunca se concatena; inyección SQL imposible.
- `findByCampoUnico` aplica `setMaxResults(1)` y devuelve `null` si no hay
  coincidencias; es la forma corta cuando el campo debería ser único.

## 6. Pruebas de esta capa

```bash
mvn test                                              # toda la suite
mvn test "-Dtest=PersonaDAOTest,PaginacionDAOTest,BusquedaDAOTest"
```

Las pruebas corren contra `capacitaciones.db` real (la misma que usa la
aplicación). Por eso cada prueba empieza y termina con la siembra canónica de
`BaseDePruebas` (borra y vuelve a insertar las 5 personas), de modo que son
independientes del orden en que Surefire las ejecute y dejan la base en su
estado normal.

Casos cubiertos:

- **Error y rollback** (`PersonaDAOTest`): la tabla no trae restricciones en
  su DDL, así que la prueba crea un índice único temporal sobre
  `correo_electronico` e intenta guardar dos correos repetidos en un mismo
  lote; verifica que llega `DAOException` (con mensaje y causa) y que **ni la
  fila válida del lote** quedó guardada.
- **Paginación** (`PaginacionDAOTest`): páginas dentro del rango, más allá del
  final, listado completo y argumentos inválidos.
- **Búsqueda** (`BusquedaDAOTest`): varias coincidencias, ninguna, variante
  única y su caso sin resultados.
