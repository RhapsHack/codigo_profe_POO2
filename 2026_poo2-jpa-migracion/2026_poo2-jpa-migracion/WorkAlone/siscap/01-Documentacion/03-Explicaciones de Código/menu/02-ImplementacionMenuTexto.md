# Implementación de la biblioteca de menús: `OpcionMenu` y `MenuTexto`

## ¿Qué cambia en esta versión?

En la actualización anterior definimos los contratos (`IMenu` y
`IOpcionMenu`); ahora se cumplen. Nuevos archivos:

| Archivo | Papel |
| --- | --- |
| `utils/menu/impl/OpcionMenu.java` | La opción: guarda valor, texto y estado. |
| `utils/menu/impl/MenuTexto.java` | El menú de consola: muestra, lee y valida. |

Además se agregaron a `Constantes` los mensajes de la biblioteca
(`MSG_MENU_SOLICITAR_OPCION`, `MSG_MENU_OPCION_NO_VALIDA` y los errores de
construcción), siguiendo la convención del proyecto: **ningún texto suelto en
el código**.

## Conceptos POO y de Java que esta implementación ilustra

### 1. Encapsulamiento con validación (invariantes)
`OpcionMenu` mantiene sus campos `private` y valida en el constructor que el
valor y el texto no sean nulos ni vacíos. Un objeto se construye **válido y
así se queda**: `valor` es `final` (su identidad nunca cambia) y `setTexto`
también rechaza vacíos. La clase es imposible de "romper" desde fuera: eso es
el encapsulamiento bien entendido, no solo campos privados con getters.

### 2. `final` para inmutabilidad
`private final String valor` solo se asigna una vez, en el constructor. Es la
forma de Java de decir "esto no cambia nunca", y el compilador lo verifica.

### 3. Sobrecarga de constructores y encadenamiento
`OpcionMenu` tiene dos constructores: el mínimo
(`new OpcionMenu("abrir")`) delega en el completo con `this(...)`, que es el
que valida y asigna. `MenuTexto` no necesita constructor con parámetros: su
constructor sin argumentos fija la configuración predeterminada.

### 4. Implementar una interfaz (polimorfismo)
Ambas clases usan `implements` y cada método lleva `@Override`. La promesa de
`IMenu` ahora tiene un cuerpo real. Nada en `MenuTexto` conoce a los programas
CRUD, y nada en los CRUD conocerá los detalles de `MenuTexto`: solo compartirán
la interfaz. Cuando exista `MenuDialog`, será intercambiable sin tocar la
lógica de los programas (sustitución de Liskov, ya en marcha).

### 5. El contrato devuelve, no grita
Observa la decisión de diseño: `agregarOpcion` devuelve `false` si el valor ya
existe; `quitarOpcion`, `habilitarOpcion` e `inhabilitarOpcion` devuelven
`false` si la opción no existe; `obtenerOpcion` devuelve `null`. Las
excepciones se reservan para errores de programación (configurar un estilo
nulo, construir una opción sin valor). Compara esto con los DAOs, que sí
lanzan `RuntimeException` ante fallas de base de datos: cada capa elige su
estrategia de errores.

### 6. Etiquetas posicionales: el algoritmo
La etiqueta NO se guarda en la opción: `obtenerMenuComoTexto()` recorre las
opciones habilitadas y numera por posición. Fíjate en dos algoritmos dignos de
estudio:

- **Letras**: conversión a base 26 con letras (`a..z`, luego `aa, ab...`);
  el truco `numero--` antes de dividir alinea el rango 0-25 con las letras.
- **Romanos**: tabla de valores {1000, 900, 500, ...} con sus símbolos,
  restando de mayor a menor; al final, `toLowerCase()`.

Y la validación de `solicitarOpcion()` compara la respuesta del usuario con el
**valor** de cada opción (nunca con la etiqueta): 1, a o i son solo disfraces
de presentación.

### 7. Colecciones y construcción de texto
El menú guarda sus opciones en un `ArrayList<IOpcionMenu>` —de nuevo el tipo
declarado es la interfaz (`List`/`IOpcionMenu`) y no la clase concreta— y arma
su texto con `StringBuilder`, evitando la concatenación ineficiente de
cadenas dentro de ciclos.

### 8. Salida estándar vs. salida de errores
La implementación separa flujos: el menú y los mensajes normales van a
`System.out`; los avisos de opción inválida van a `System.err`. En consola se
mezclan, pero son flujos distintos que un programa puede redirigir por
separado.

### 9. `static` y utilidades de clase
El método `capitalizarPrimeraLetra` de `OpcionMenu` es `static`: no depende del
estado de ningún objeto, solo de su parámetro. Usa `Character.toUpperCase`,
que respeta acentos ("árbol" → "Árbol").

## Configuración predeterminada de `MenuTexto`

- Estilo `NUMEROS` desde 1 y separador `") "`.
- Opción **Salir** activada, opción **Cancelar** desactivada (recuerda:
  `setConCancelar(true)` / `setConSalir(false)` las cambia; se listan al final,
  después de las opciones del usuario).

## ¿Cómo se usará? (anticipando el refactor)

```java
IMenu menu = new MenuTexto();
menu.agregarOpcion("mostrar", "Mostrar todos");
menu.agregarOpcion("agregar", "Agregar una tecnología");
String elegido;
do {
    elegido = menu.solicitarOpcion();   // muestra, lee y valida
    switch (elegido) {
        case "mostrar" -> /* ... */;
        case "agregar" -> /* ... */;
    }
} while (!elegido.equals(IMenu.VALOR_SALIR));
```

El menú no sabe qué es "mostrar"; el programa no sabe qué es un `Scanner`.
Esa frontera es el punto del ejercicio.

## Preguntas para discutir en clase

1. ¿Por qué `valor` es `final` pero `texto` y `habilitada` no?
2. `solicitarOpcion()` compara contra `VALOR_OPCION_CANCELAR` y
   `VALOR_OPCION_SALIR`, no contra `"cancelar"`/`"salir"` escritos a mano.
   ¿Qué problema evita escribir literales a mano?
3. Si mañana agregamos `MenuDialog`, ¿cuáles métodos tendrán implementación
   distinta y cuáles podrían reutilzarse tal cual?
4. ¿Qué pasaría si `obtenerMenuComoTexto()` numerara TODAS las opciones, incluso
   las inhabilitadas? ¿Qué comportamiento del menú cambiaría?

## Siguiente paso

Refactor de los programas de `programas`: reemplazar los menús escritos a mano
de los 8 CRUDs por `IMenu`/`MenuTexto`.
