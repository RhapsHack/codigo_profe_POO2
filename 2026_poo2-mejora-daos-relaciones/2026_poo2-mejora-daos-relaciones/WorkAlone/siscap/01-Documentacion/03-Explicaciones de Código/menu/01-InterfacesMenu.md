# Interfaces para la biblioteca de menús (`IMenu` y `IOpcionMenu`)

> **HISTÓRICO:** la biblioteca de menús (`utils/menu`, `OpcionMenu` y
> `EstiloNumeracionMenu`) fue retirada del código en el cambio
> `retiro-consola-y-menues`. Este documento se conserva como material
> didáctico; los archivos que describe ya no existen en `src/`.

## ¿Qué cambia en esta versión?

Hasta ahora, cada programa CRUD de la carpeta `programas` escribía su menú a
mano: imprimía opciones con `System.out.println`, leía la respuesta con
`Scanner` y validaba "a pie" que la opción existiera. Ese código se repite una
y otra vez en las 8 clases de CRUD.

En este cambio **no se implementa todavía nada**: se definen las dos
interfaces que describirán a nuestros menús. Una interfaz en Java es un
**contrato**: declara QUÉ debe poder hacer un objeto, sin decir CÓMO se hace.
Las clases que implementen la interfaz se comprometen a cumplir ese contrato.

## Archivos nuevos

| Archivo | Papel |
| --- | --- |
| `utils/menu/interfaces/IOpcionMenu.java` | Contrato de una opción de menú. |
| `utils/menu/interfaces/IMenu.java` | Contrato de un menú completo. |
| `modelo/enumeraciones/EstiloNumeracionMenu.java` | Enum con los estilos de numeración (agregado en una evolución posterior). |
| `utils/menu/impl/MenuTexto.java` | Implementación en consola (ver 02). |
| `utils/menu/impl/OpcionMenu.java` | Clase que implementa la opción (ver 02). |

## Conceptos POO que este cambio ilustra

### 1. Abstracción
Un menú de verdad no es "un System.out.println": es un objeto que administra
**opciones** y **configuración**, sabe representarse como texto y sabe pedir al
usuario una opción válida. Abstraer es separar el QUÉ (contrato) del CÓMO
(implementación).

### 2. Interfaces como contrato y polimorfismo
`IMenu` promete métodos como `agregarOpcion`, `quitarOpcion`,
`obtenerMenuComoTexto` y `solicitarOpcion`. Cualquier clase que implemente
`IMenu` (pronto `MenuTexto` con consola, después `MenuDialog` con
`JOptionPane`) podrá usarse de la misma forma. El programa cliente escribirá:

```java
IMenu menu = new MenuTexto();      // mañana: new MenuDialog();
```

...y el resto del código no cambia: **el tipo declarado es la interfaz, el
objeto real es la implementación**. Ese es el corazón del polimorfismo y del
principio de sustitución de Liskov (LSP): cualquier `IMenu` puede sustituir a
otro sin romper al programa que lo usa.

### 3. Una opción de menú solo guarda información
`IOpcionMenu` declara `getValor`, `getTexto`, `setTexto`, `isHabilitada`,
`setHabilitada`. Fíjate en lo que NO declara: nada de ejecutar acciones. El
menú devuelve el **valor** de la opción elegida y es el programa cliente quien
decide qué hacer con ella.

Fíjate también en lo que NO está en la opción: la etiqueta `1)`, `a)`, `i)`.
La etiqueta es **posicional**: la calcula el menú al momento de mostrarse. Así,
si agregas o quitas opciones, la numeración se ajusta sola y el valor de cada
opción (su identidad) nunca cambia.

### 4. Sobrecarga de métodos (overloading)
`IMenu` declara tres formas de `agregarOpcion`: con un `IOpcionMenu`, con solo
un texto (`agregarOpcion("abrir")`) y con valor y texto distintos. Mismo
nombre, distintas firmas: eso es sobrecarga, y Java decide cuál llamar según
los argumentos.

### 5. Enumeraciones
El estilo de numeración es un enum propio del modelo:
`modelo/enumeraciones/EstiloNumeracionMenu` (`NUMEROS`, `LETRAS`, `ROMANOS`).
Un enum restringe los valores posibles a una lista cerrada: el compilador nos
impide pedir una numeración "inventada". Vive en el modelo —no dentro de
`IMenu`— porque es un concepto del dominio que comparten TODAS las
implementaciones de menú; así ninguna interfaz acopla a sus clientes con una
class concreta.

> Nota: en la primera versión este enum estaba anidado dentro de `IMenu` con
> el nombre `EstiloNumeracion`; se movió a `modelo/enumeraciones` y se renombró
> a `EstiloNumeracionMenu` en la actualización descrita en
> `04-EnumeracionEstiloNumeracionMenu.md`.

### 6. Constantes de interfaz
Los campos de una interfaz son `public static final` por omisión. `IMenu`
expone `VALOR_CANCELAR` y `VALOR_SALIR`. Observa que su valor se toma de la
clase `Constantes` del proyecto: la convención de este proyecto es que TODOS
los textos y valores viven en `Constantes`, para tener una única fuente de
verdad.

## El contrato completo, en corto

- Gestión de opciones: `agregarOpcion` (3 sobrecargas), `quitarOpcion`,
  `habilitarOpcion`, `inhabilitarOpcion`, `tieneOpcion`, `obtenerOpcion`,
  `obtenerTotalOpciones`.
- Configuración: `setEstiloNumeracion` (números desde 1, letras `a, b, c...`,
  romanos `i, ii, iii...`), `setSeparador` (`") "`, `" "`, `"- "`...),
  `setConCancelar`, `setConSalir` (la opción de Cancelar y la de Salir son
  automáticas pero opcionales; se muestran al final).
- Presentación e interacción: `obtenerMenuComoTexto()` (versión textual lista
  para un `System.out.println`), `mostrarMenu()` (la implementación decide el
  medio) y `solicitarOpcion()` (pide y valida hasta obtener una opción
  existente y habilitada; devuelve su valor, o `VALOR_CANCELAR` /
  `VALOR_SALIR`).

## Preguntas para discutir en clase

1. ¿Por qué la etiqueta `1)`, `a)`, `i)` no es un atributo de la opción?
2. Si mañana cambiamos `new MenuTexto()` por `new MenuDialog()`, ¿qué líneas
   del programa cliente deberían cambiar? ¿Por qué?
3. ¿Por qué `agregarOpcion` devuelve `boolean` en lugar de lanzar una
   excepción cuando el valor está duplicado? ¿Cuándo convendría una excepción?
4. ¿Qué gana el programa al recibir el valor (`"abrir"`) en lugar de un número
   de opción?

## Siguiente paso

Con las interfaces revisadas, la siguiente actualización implementará
`OpcionMenu` y `MenuTexto`, y después refactorizaremos los programas de la
carpeta `programas` para que usen el menú en lugar de su lógica escrita a mano.
