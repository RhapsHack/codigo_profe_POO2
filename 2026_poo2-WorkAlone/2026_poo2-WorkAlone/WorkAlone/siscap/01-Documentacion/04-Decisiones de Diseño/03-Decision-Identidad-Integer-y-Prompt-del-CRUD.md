# Decisión: la identidad es `Integer` y el prompt de actualización lo compone la plantilla

## Contexto

Al migrar la capa de datos a JPA (EclipseLink) y al subir el CRUD repetido a una
plantilla abstracta (`AbstractCrudEntidad<T>`) aparecieron dos problemas concretos.

**1. La clave primaria no coincidía con la que recibía el DAO.** Las entidades
declaran `Integer id`, pero el contrato se había escrito para la versión JDBC,
donde el id se leía como `Long`:

```java
// IBaseDAO antes
T findById(Long id);
void deleteById(Long id);

// los 8 CRUD
Long id = (long) LECTOR.leerEntero(MSG_INGRESE_ID);
```

Con eso, las opciones *Actualizar* y *Eliminar* terminaban en excepción:

```
java.lang.IllegalArgumentException: You have provided an instance of an
incorrect PK class for this find operation.  Class expected : class
java.lang.Integer, Class received : class java.lang.Long.
```

**2. El texto del prompt de actualización se repetía.** Cada CRUD tenía que
inventar su propio formato para preguntar el valor de un campo mostrando el
actual, y ese formato no estaba escrito en un solo lugar: se iba a copiar en los
8 programas.

## Decisión

### 1. `Integer` en toda la cadena de la identidad

- `IEntidad.getId()/setId()` y `IBaseDAO.findById/deleteById` usan `Integer`.
- Los CRUD leen el id sin castear: `Integer id = LECTOR.leerEntero(...)`.
- `em.find` **no** convierte: exige la clase exacta de la clave primaria. El
  tipo de la clave es parte del contrato, no un detalle de la tabla.

**Por qué `Integer` y no `Long`:** es el tipo que declaran las 8 entidades (el
esquema de SQLite las declara `INTEGER`), y además coincide con lo que devuelve
el lector (`leerEntero()` devuelve `int`). Forzar `Long` obligaría a convertir en
cada frontera y fue exactamente el origen del error. Si algún día el esquema
migrara a `BIGINT`, la decisión se revisa completa (entidad + DAO + lecturas).

### 2. El prompt lo compone la plantilla, no cada CRUD

`AbstractCrudEntidad<T>` expone dos métodos `protected` que las subclases
heredan:

```java
protected String mensajeActualizable(String nombreCampo, Object valorActual);  // "Nombre(Oracle):"
protected String leerCampoActualizable(String nombreCampo, String valorActual);
```

El CRUD solo escribe una línea por campo:

```java
e.setNombre(leerCampoActualizable("Nombre", e.getNombre()));
```

Y el lector (`leerTexto(mensaje, valorActual)`) deja de añadir su propio
`", Valor actual:"`: **muestra exactamente el mensaje que recibe**. Así el
formato se escribe una sola vez, el "Enter conserva el valor" sigue siendo
responsabilidad de la biblioteca de entrada/salida, y el texto que ve el usuario
queda compacto:

```
Nombre(SQLite):Modelo(Relacional):Proveedor(SQLite):Descripción(Motor de base de datos embebido y ligero):
```

**Por qué en la plantilla y no en la biblioteca `inout`:** el *qué* se muestra
("el nombre del campo y su valor entre paréntesis") es una decisión de
presentación del CRUD, no del algoritmo de lectura. Si el formato viviera en
`ILectorDatos`, la biblioteca tendría que saber de campos, y los 7 CRUD
pendientes de migrar no podrían adoptarlo sin reescribir sus prompts. El formato
anterior (`Constantes.mensajeActualizable`, en varios renglones) queda
obsoleto cuando los 8 usen la plantilla.

## Consecuencias

**A favor**

- Un solo lugar decide el texto de los prompts: cambiar el formato es cambiar
  una línea en la plantilla.
- Actualizar y eliminar funcionan; el `id` deja de necesitar una conversión
  repartida por los 8 programas.
- Se conserva la regla del proyecto: los textos viven en código de
  presentación y los datos de entrada salen de `ILectorDatos`.

**En contra / pendiente**

- Cambiar el tipo del id en el futuro obliga a tocar entidad, DAO, lecturas y
  documento; no es un cambio local.
- `AbstractLectorDatos` tiene tres imports estáticos de `Constantes` que
  quedaron sin uso cuando se quitó el sufijo `", Valor actual:"`; conviene
  limpiarlos cuando se decida cerrar ese cambio.
- `pedirDatosEntidad` de `CrudBasesDeDatos` conserva un `dao.update(...)` y un
  mensaje de "actualizada" que la plantilla ya emite (el aviso sale duplicado).
  La plantilla debe ser la única que actualiza y avisa.
- `mostrarEntidad` concatena los campos sin separadores
  (`ID1Activo:trueNombre:...`); las constantes `MSG_DETALLE_*` de `Constantes`
  ya resuelven el formato.
