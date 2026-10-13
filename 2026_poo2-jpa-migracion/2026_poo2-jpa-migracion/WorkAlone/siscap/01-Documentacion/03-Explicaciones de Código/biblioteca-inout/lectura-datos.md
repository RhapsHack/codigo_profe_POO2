# Biblioteca de entrada/salida de datos (utils.inout)

## Qué es

La capa `utils.inout` reemplaza el patrón duplicado de `Scanner`/`System.out` que tenían
los 8 programas CRUD por una biblioteca reutilizable y desacoplada del medio de entrada.

Cada CRUD actualmente hace `new Scanner(System.in)`, `System.out.print(...)`, `System.err.println(...)`
y parsea directamente. Ese código está repetido en cada archivo. Si un día cambiamos de consola
a ventanas (JOptionPane, Swing, web), tendríamos que reescribir ese código en cada CRUD.

La nueva biblioteca separa el **algoritmo** de lectura/validación del **medio** concreto.

---

## Template Method (Método Plantilla)

Es un patrón de diseño que vive en la clase abstracta `AbstractLectorDatos`.

**La idea:** toda la lógica de lectura, validación, rangos y mensajes de error está
escrita UNA sola vez en la clase abstracta. Las subclases concretas (`LectorDatosTexto`,
`LectorDatosDialogos`) solo definen TRES métodos básicos (las "primitivas") que dicen
*CÓMO* se lee y se muestra algo en el medio concreto:

| Primitiva | Texto (Scanner/consola) | Diálogo (JOptionPane) |
|---|---|---|
| `solicitarLinea(mensaje)` | `System.out.print(mensaje)` + `Scanner.nextLine()` | `JOptionPane.showInputDialog(mensaje)` |
| `mostrarMensaje(texto)` | `System.out.println(texto)` | `showMessageDialog(..., INFORMACION)` |
| `mostrarError(texto)` | `System.err.println(texto)` | `showMessageDialog(..., ERROR)` |

El algoritmo de `leerEntero()`, `leerDouble()`, `leerBoolean()`, rangos, mensajes
de error y política de vacíos está en la clase abstracta y **nunca se toca** al
cambiar de medio de entrada.

---

## Por qué se lee LÍNEA y después se parsea

Todos los métodos de lectura usan esta secuencia:

1. Leer una **línea completa** como `String` (por `solicitarLinea`).
2. Parsear la línea según el tipo (`Integer.parseInt`, `Double.parseDouble`, `V`/`F`).

**Nunca** se usa `Scanner.nextInt()` ni `Scanner.nextDouble()`.

### Razón técnica

`Scanner.nextInt()` lee un número entero pero **no consume** el salto de línea (`\n`)
que queda después. La siguiente llamada a `Scanner.nextLine()` recibe esa línea vacía
en vez de esperar la siguiente entrada del usuario. Es el bug más clásico en Java
principiante.

Al leer siempre la línea completa con `nextLine()` (o `showInputDialog`), el salto
de línea **siempre** queda consumido y no hay sorpresas.

### Razón de diseño

El mismo método de lectura funciona tanto para `Scanner` como para `JOptionPane`:
ambos leen una línea de texto. La clase abstracta no necesita saber qué tipo de
medio hay debajo; solo llama `solicitarLinea` y recibe un `String`.

### Separador decimal

`Double.parseDouble()` usa el separador decimal `.` (punto), no la coma `,`
que algunos usuarios esperan. Esto es intencional y consistente: funciona igual
en todos los sistemas operativos. Se documenta como política.

---

## Política de valores vacíos

El atributo `permitirValoresVacios` (default `true`) controla qué pasa cuando
el usuario escribe solo Enter (línea vacía) al leer texto:

- **true** (por defecto): se acepta la línea vacía y se devuelve `""`.
- **false**: se muestra `MSG_TEXTO_NO_VACIO` ("El valor no puede estar vacío.")
  y se repite la lectura.

**Los métodos "actualizables"** (como `leerTexto(mensaje, valorActual)`,
`leerBoolean(mensaje, valorActual)`, `leerEnteroActualizable(mensaje, valorActual)`)
**siempre aceptan el vacío** sin importar la política: Enter conserva el valor
actual del campo. Esto es intencional: en un formulario de actualización (un CRUD),
presionar Enter sin escribir nada significa "no quiero cambiar este campo".

### Por qué los numéricos actualizables tienen nombre propio

`leerEnteroActualizable` en lugar de una sobrecarga `leerEntero(mensaje, valorActual)`:
en Java, `leerEntero("Id: ", 10)` con `Integer valorActual` y `int valorMinimo` es
**ambigua**: el compilador resuelve la llamada con el parámetro `int valorMinimo`
(coincide exacto con `int`) en vez de con `Integer valorActual` (requiere
autoboxing). El resultado: el usuario cree que está pasando el valor actual 10,
pero el método que se invoca es el de mínimo. Para evitar este pozo silencioso,
las versiones numéricas actualizables usan un nombre distinto: `leerEnteroActualizable`
y `leerDoubleActualizable`. (`leerTexto` y `leerBoolean` sí pueden ser sobrecarga
porque no tienen conflicto de tipos.)

---

## Delegación de sobrecargas

Los métodos se construyen unos sobre otros. Por ejemplo:

```
leerEntero()                          → delega en leerEntero("")
leerEntero(mensaje)                   → parseInt(leerTexto(mensaje)) en bucle
leerEntero(mensaje, min)              → valida min sobre leerEntero(mensaje)
leerEntero(mensaje, min, max)         → valida min/max sobre leerEntero(mensaje)
leerEnteroActualizable(mensaje, actual) → Enter conserva; parseo en bucle
```

Esto significa que la lógica de parseo y errores se escribe **una sola vez**
en `leerEntero(mensaje)` y todas las demás variantes la reutilizan.

---

## Evolución a diálogos

`LectorDatosDialogos` implementa las tres primitivas con `JOptionPane`:

```java
protected String solicitarLinea(String mensaje) {
    return JOptionPane.showInputDialog(mensaje);
}

protected void mostrarMensaje(String mensaje) {
    JOptionPane.showMessageDialog(null, mensaje);
}

protected void mostrarError(String mensaje) {
    JOptionPane.showMessageDialog(null, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
}
```

Con esto, TODO el algoritmo de la clase abstracta funciona automáticamente
con ventanas de diálogo, sin tocar una sola línea de lógica. Es el poder del
Template Method: el medio de entrada es un detalle de implementación, no del
algoritmo.
