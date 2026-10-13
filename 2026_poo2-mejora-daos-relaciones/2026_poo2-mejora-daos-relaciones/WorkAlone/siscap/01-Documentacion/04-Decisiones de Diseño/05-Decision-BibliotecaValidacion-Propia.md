# Decisión: validación propia con binding por `Function` y mensajes etiquetados por campo

> Cambio: `validadores-errores-visibles` (archivado 2026-10-07).
> Explicación en `../03-Explicaciones de Código/validadores/`.

## Contexto

**1. El formulario no explicaba nada.** `aceptarCaptura`/`aceptarUpdate`
mostraban `"Error en la validacion "` sin decir qué campo falló ni por qué;
`getErrores()` existía pero nadie lo invocaba.

**2. La máquina de validación tenía fallas de diseño.**

- Dos registros por campo: el constructor llamaba `agregarValidador(...)` y
  un segundo método manual `asignarValoresAValidadores()` debía leer la
  entidad campo por campo. Olvidar una línea evaluaba ese campo como nulo
  **sin causa visible** (el bug que motivó el cambio).
- Se detenía en la primera condición fallida: el usuario corregía, volvía a
  fallar otro lado, y otra vez…
- `getErrores()` devolvía `subList` (vista viva de la lista interna),
  `addCondicion(null, ...)` se descartaba en silencio, `min`/`max`
  formateaban con `%d` (crash con `ValidadorNumerico<Double>`) y el logger
  estaba en la clase abstracta (mostraba `AbstractValidador`, no la subclase).

## Decisión

### 1. Binding en una sola llamada, con `Function`

```java
<V> void agregarValidador(String nombreCampo,
                          IValidador<V,?> validador,
                          Function<T,V> getter);
```

Un `record ValidadorCampo<T,V>(String, IValidador<V,?>, Function<T,V>)`
sustituye las dos listas paralelas y el `Map` de llaves string;
`validar(entidad)` hace `validador.setValor(getter.apply(entidad))` y
`asignarValoresAValidadores()` desaparece de la interfaz y de los 3
validadores (quedan solo constructores).

**Por qué `Function`/method reference**: el compilador verifica que el
getter lea el tipo que el validador espera (`Persona::getNombre` vs
`IValidador<String,?>`); una desincronización no compila, no falla en
tiempo de ejecución.

### 2. Acumulación total con una sola cortacosa

`validar()` recorre todas las condiciones de cada campo y todos los campos;
acumula todos los mensajes. Única excepción: valor nulo → mensaje de nulo y
no se evalúa predicado (antes había una condición "no nulo" implícita además
del chequeo explícito: doble mecanismo).

### 3. Cada mensaje identifica su campo

`ValidadorEntidad.getErrores()` antepone `etiqueta + ": "` resolviendo la
llave contra `mensajes.properties` con `containsKey` y **respaldo a la llave
cruda** (nunca `MissingResourceException`):

```
Se encontraron errores de validación:
Primer apellido: La cadena está vacía
Correo electrónico: La longitud debe ser mayor a 2
```

Es la misma etiqueta que el formulario muestra junto al campo
(`PanelShowPeople` usa las mismas llaves). Corrigió de paso la llave
`PeopleValidador.CORREO = "correo"` → `"correo_electronico"` (no existía en
el bundle).

### 4. Robustez y presentación

- `getErrores()` → `List.copyOf` (copia inmutable).
- `addCondicion` → `Objects.requireNonNull(condicion, "...")` (fail-fast en
  quien llama).
- `min`/`max` → `%s` (funciona para `Integer`, `Long` y `Double`).
- Logger → `LoggerFactory.getLogger(getClass())`.
- Diálogo → método privado único `mostrarErroresValidacion()` con el prefijo
  `MSG_ERROR_VALIDACION` de `Constantes` (regla del proyecto: los textos
  viven en `Constantes`).

## Alternativas descartadas

- **Reflexión (`getNombre()` por nombre de string)**: magia, typos en
  tiempo de ejecución, opaca el aprendizaje; `Function` da verificación
  estática sin costo.
- **Mantener `asignarValoresAValidadores()`**: era justamente el origen del
  bug (olvido silencioso).
- **Que la entidad exponga `Map<String,Object>`**: contamina las entidades
  con presentación/validación.
- **Prefijo con la llave cruda** (`primer_apellido:`): legible, pero no
  coincide con la etiqueta visible del formulario.
- **Que el controlador resuelva las etiquetas**: allá la asociación
  campo↔mensaje ya se perdió (recibe la lista aplanada).
- **Que cada validador hoja conozca su campo**: acopla componentes
  reutilizables a la UI; los validadores siguen siendo genéricos.
- **Jakarta Bean Validation (Hibernate Validator)**: fuera de alcance
  didáctico — el proyecto quiere mostrar los patrones, no consumirlos.

## Consecuencias

**A favor**

- Una sola operación por campo; el olvido imposible por diseño.
- El usuario corrige **todos** los campos de una vez y sabe cuál falló y
  por qué: el diálogo es autoexplicativo.
- Las pruebas fijan el comportamiento (10 pruebas de validación; la suite
  completa en verde, 71 pruebas).
- Los validadores quedan listos para fase 2 (validaciones de negocio:
  email, unicidad contra BD) sin tocar la UI.

**En contra / pendiente**

- Cambia la firma de `agregarValidador` y elimina un método de la interfaz
  (BREAKING): solo 3 implementaciones, el compilador lista los puntos.
- Resolver etiquetas desde el bundle acopla `ValidadorEntidad` a
  `MensajesResourceBundle` (misma capa `modelo.*` que ya usaba
  `IEntidad`); si el bundle fallara al cargar, el proyecto entero sale con
  `System.exit` (comportamiento preexistente del enum).
- Los mensajes de texto son literales en las clases de validación (no van
  a `Constantes`): cambiar el idioma exige tocar el código. Decisión
  pendiente si algún día hay i18n real.
