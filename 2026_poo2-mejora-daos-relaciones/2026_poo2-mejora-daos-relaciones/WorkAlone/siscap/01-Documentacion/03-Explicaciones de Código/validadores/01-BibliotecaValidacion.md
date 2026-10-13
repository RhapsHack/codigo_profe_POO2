# Biblioteca de validación: errores específicos antes de guardar

> Cambio de origen: `validadores-errores-visibles` (archivado
> 2026-10-07). Decisión de diseño en
> `04-Decisiones de Diseño/05-Decision-BibliotecaValidacion-Propia.md`.

## ¿Por qué una biblioteca propia?

El proyecto no usa Jakarta Bean Validation: la validación es material
didáctico (mismos patrones que un framework, pero legibles en 4 clases).
Antes del cambio, los formularios solo mostraban `"Error en la validacion "`
y la máquina de validación tenía tres fallas de diseño: exigía dos registros
por campo (el olvido de `asignarValoresAValidadores()` evaluaba todo como
nulo sin causa visible), se detenía en la primera condición fallida y
`getErrores()` devolvía una vista viva de la lista interna.

## El contrato: `IValidador<T, U>`

```java
boolean validar();                       // evalúa todo y devuelve ¿sin errores?
void setValor(T valor);                  // el valor a evaluar
void addCondicion(Predicate<T> condicion, String textoError);
IValidador<T,U> min(U v); max(U v); between(U min, U max);
IValidador<T,U> or(...); and(...);       // combinar predicados
List<String> getErrores();               // copia inmutable de los mensajes
```

`T` es el tipo del valor evaluado y `U` el tipo de los límites numéricos:
`ValidadorString` es `IValidador<String,Integer>` (longitudes) y
`ValidadorNumerico<Double>` es `IValidador<Double,Double>` (rangos).

## `AbstractValidador`: acumular, no cortar

Dos listas paralelas (`Predicate<T>` y su texto de error) que se registran
juntos en `addCondicion`, que exige condición no nula con
`Objects.requireNonNull(condicion, ...)` — un error de programación falla
de inmediato y con traza, no se descarta en silencio.

`validar()` recorre **todas** las condiciones y acumula los mensajes
incumplidos. La única cortacosa es la nulidad: si el valor es nulo agrega
`"El valor a evaluar es nulo"` y no evalúa predicados (no se puede aplicar
un `Predicate` sobre `null`). Ese es también el único mecanismo de nulidad:
la condición "no nulo" implícita del constructor se eliminó (era un doble
mecanismo).

`min`/`max` formatean con `%s` (con `%d` un `ValidadorNumerico<Double>`
lanzaba `IllegalFormatConversionException` al registrarse). Cada error
también se registra en el `Logger` de la subclase real
(`LoggerFactory.getLogger(getClass())`).

## Los validadores concretos

`ValidadorString` (`noVacia`, `esVacia`, `min`, `max`, `soloAlfabeticas`,
`soloAlfabeticasYBlanco`, `soloAlfaNumericas`, `cumpleRegExp`) y
`ValidadorNumerico` (rangos con `Comparable`) construyen sobre
`addCondicion`. El API es fluido: `new ValidadorString().noVacia()
.between(2,100)` registra dos condiciones y devuelve `this`.

## Validar una entidad: `ValidadorEntidad` y el binding con `Function`

```java
agregarValidador(NOMBRE,
        new ValidadorString().noVacia().between(2,100),
        Persona::getNombre);          // una sola operación por campo
```

Internamente un `record` privado guarda las tres piezas:

```java
private record ValidadorCampo<T,V>(String nombreCampo,
                                   IValidador<V,?> validador,
                                   Function<T,V> getter) {
    void tomarValorDe(T entidad) {
        validador.setValor(getter.apply(entidad));
    }
}
```

`validar(entidad)` recorre los campos, les toma el valor con su getter y
evalúa; `getErrores()` aplana los mensajes **etiquetando cada uno con su
campo**, resolviendo la llave contra `mensajes.properties`
(`ResourceBundle.containsKey`, con respaldo a la llave cruda si no existe):

```
Nombre: La longitud debe ser mayor a 2
Correo electrónico: La cadena está vacía
```

La etiqueta es exactamente la que el formulario muestra junto al campo
(`PanelShowPeople` usa las mismas llaves), así que diálogo y formulario
hablan el mismo idioma.

Los tres validadores de entidad (`PeopleValidador`, `FrameworkValidador`,
`LangProgValidador`) ya son solo constructores. El
`ValidadorEntidadFactory.crearValidador(nombreCatalogo)` devuelve el
adecuado para cada ventana.

## El diálogo: de `getErrores()` a `JOptionPane`

`AbstractControllerBase` tiene un método privado único
(`mostrarErroresValidacion()`) usado por `aceptarCaptura` y `aceptarUpdate`:

```java
JOptionPane.showMessageDialog(null,
        MSG_ERROR_VALIDACION + "\n" + String.join("\n", validadorEntidad.getErrores()));
```

Si `validar(...)` devuelve `true` no se muestra ningún diálogo: el registro
se guarda, el formulario se limpia y la ventana se cierra.

## Recetas

**Añadir una condición a un campo existente** (en el validador de la
entidad): encadenar otro método o usar `addCondicion` con predicado y texto:

```java
agregarValidador(CORREO,
        new ValidadorString().noVacia().cumpleRegExp(".+@.+"),
        Persona::getCorreoElectronico);
```

**Añadir un catálogo nuevo al diálogo**: crear su `XxxValidador extends
ValidadorEntidad<T>`, registrarlo en `ValidadorEntidadFactory` con la llave
del catálogo y asegurar que las llaves de sus campos existan en
`mensajes.properties` (si no, el diálogo muestra la llave cruda).

**Probar sin ventana:**

```bash
mvn test "-Dtest=ValidadorStringTest,ValidadorNumericoTest,ValidadorEntidadTest"
```

## Conceptos que ilustra

1. **Lambdas y method references verificadas por el compilador**: el tipo
   `Function<T,V>` amarra el getter al tipo que espera el validador; una
   desincronización no compila, no falla en tiempo de ejecución.
2. **`record` como unidad inmutable**: las tres piezas de un campo viajan
   juntas; sustituyó a dos listas paralelas y a un `Map` de llaves string.
3. **Wildcards en genéricos**: `IValidador<V,?>` acepta cualquier segundo
   parámetro sin recurrir a tipos crudos.
4. **`List.copyOf`**: el estado interno no se expone; modificar la lista
   recibida no altera al validador.
5. **Fail-fast con `Objects.requireNonNull`**: el error de programación
   ocurre en quien llama, con la traza completa.
6. **`ResourceBundle` con respaldo**: presentación (etiquetas) resuelta
   desde el bundle sin romper si falta una llave.
