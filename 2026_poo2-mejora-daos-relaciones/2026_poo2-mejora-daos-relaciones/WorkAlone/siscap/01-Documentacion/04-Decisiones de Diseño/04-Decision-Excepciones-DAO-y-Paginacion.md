# Decisión: los errores de persistencia se propagan con `DAOException` y la paginación es por índice de página

> Cambio: `daos-errores-paginacion` (archivado 2026-10-07). Explicación en
> `../03-Explicaciones de Código/daos/01-RefactorCapaDAOs.md` y guía de uso
> en `../03-Explicaciones de Código/daos/02-GuiaUsoDAOs.md`.

## Contexto

**1. `DAOBase` tragaba los errores.** Las transacciones de escritura
registraban el problema en el `Logger` y devolvían `null` (o `false`): el
CRUD no podía avisar al usuario de que su cambio **no** se guardó. El
roadmap lo tenía listado como "calidad pendiente". Con la ventana gráfica
el síntoma era peor: `aceptarCaptura` cerraba el formulario y limpiaba los
campos **aunque el `guardar` hubiera fallado**, dando por hecho un éxito
que no ocurrió.

**2. No existía paginación.** `findAll()` cargaba la tabla completa; con
datos reales la lista crecía sin límite.

## Decisión

### 1. `DAOException` checked, construida en un solo punto

```java
public class DAOException extends Exception {          // checked
    public DAOException(String mensaje, Throwable causa) { ... }
}
```

`DAOBase` es el único que la construye: toma el mensaje de
`Constantes.MSG_ERROR_PERSISTENCIA` (la misma constante que hoy ve el
usuario) y guarda la excepción original como causa (`initCause`), para que
el logger muestre la traza real de EclipseLink/SQLite. Antes de relanzar,
`revertirTransaccion()` deshace la transacción para no dejar la BD a medias.

### 2. La cadena de escrituras declara `throws`

`IBaseDAO` (7 métodos: `guardar`, `guardarTodos`, `actualizar`, `borrar`,
`borrarPorId`, `borrarFisico`, `borrarFisicoPorId`), `DAOBase`,
`IServicioBase` y `ServicioBase` propagan la excepción. Los helpers privados
de `DAOBase` que ejecutan transacciones también. El compilador obliga a
decidir en cada frontera qué hacer con el error.

### 3. La interfaz decide qué ve el usuario

`AbstractControllerBase.mostrarErrorPersistencia(e)` muestra
`e.getMessage()` y los métodos de captura/edición hacen **`return` sin cerrar
la ventana**: si el guardado falló, el formulario conserva los datos y el
usuario puede reintentar. Las ingestas (`ejecutarIngesta`/`main`) declaran
`throws`; `IngestaInicial` captura y escribe a `System.err`;
`AbstractServicioCargaCSV` captura `IOException | DAOException` y hace
`log.severe`.

### 4. Paginación por índice de página, no por offset en SQL

```java
List<T> findAll(int pagina, int tamano);   // página base 0
// setFirstResult(pagina * tamano); setMaxResults(tamano);
// IllegalArgumentException si pagina < 0 o tamano <= 0
```

Las búsquedas (`findByCampo`, `findByCampoUnico`) comparten
`consultaPorCampo`, que recibe el **atributo** de la entidad (no el nombre
de columna), de modo que siguen funcionando si cambia el `@Column`.

## Alternativas descartadas

- **Unchecked (`RuntimeException`)**: no obliga a manejar el error; el
  problema vuelve al punto de partida (nadie avisa al usuario). La cadena
  JDBC→JPA ya había impuesto la misma disciplina.
- **`Optional`/`boolean` como resultado**: pierde el motivo del fallo y
  obliga a mirar el `Logger`; el usuario no lee logs.
- **Paginar con `LIMIT/OFFSET` escritos a mano en SQL**: reintroduce SQL
  frágil cuando Criteria ya lo expone; además la validación de argumentos
  viviría en cada DAO.
- **Página base 1**: confuso junto a las APIs de JPA/Criteria y con las
  colecciones de Java; base 0 es la convención de la plataforma.

## Consecuencias

**A favor**

- El usuario ve el error real en el diálogo y el formulario no se cierra
  en falso: el bug de "éxito silencioso" desaparece.
- Un solo punto (`DAOBase`) compone el mensaje y revierte; los DAOs
  concretos siguen en dos líneas.
- La paginación protege a la UI de tablas grandes sin cambiar el contrato
  de `findAll()`.
- Las pruebas provocan el fallo real (índice `UNIQUE` temporal) y verifican
  mensaje, causa y transacción revertida.

**En contra / pendiente**

- Cambiar un método de unchecked a checked toca toda la cadena
  (DAO→servicio→controlador→ingesta): el compilador lista los puntos, pero
  es ruido de compilación inicial.
- `guardarTodos` es un bucle con una transacción por elemento: no es
  atómico (decisión didáctica, documentada en la guía de DAOs).
- Si algún día hay manejo de reintentos/compensación, `DAOException` será
  el tipo sobre el que cuelga esa política.
