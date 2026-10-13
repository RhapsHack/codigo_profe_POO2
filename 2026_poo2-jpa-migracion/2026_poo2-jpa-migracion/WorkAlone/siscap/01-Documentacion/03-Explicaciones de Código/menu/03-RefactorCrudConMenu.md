# Refactor: los programas CRUD ahora usan la biblioteca de menús

## ¿Qué cambia en esta versión?

Los 8 programas de `programas` (`CrudLenguajesProgramacion`, `CrudPersonas`,
`CrudTecnologias`, `CrudSistemasOperativos`, `CrudHerramientas`, `CrudFrameworks`,
`CrudBasesDeDatos`, `CrudHabilidadesBlandas`) ya NO escriben su menú a mano:
lo construyen con `IMenu`/`MenuTexto` y solo deciden QUÉ hacer con el valor de
la opción elegida.

## El patrón en cada CRUD

```java
private static final Scanner SCANNER = new Scanner(System.in);

public static void main(String[] args) {
    // ... conectar el DAO ...
    System.out.println(MSG_MENU_CRUD_TECNOLOGIAS);   // título, una sola vez
    IMenu menu = construirMenu();
    String opcion;
    do {
        opcion = menu.solicitarOpcion();             // muestra, lee y valida
        switch (opcion) {
            case VALOR_OPCION_MOSTRAR_TODOS -> mostrarTodasLasTecnologias();
            case VALOR_OPCION_AGREGAR       -> agregarTecnologia();
            case VALOR_OPCION_ACTUALIZAR    -> actualizarTecnologia();
            case VALOR_OPCION_ELIMINAR      -> borrarTecnologia();
            case IMenu.VALOR_SALIR          -> System.out.println(MSG_SALIENDO_PROGRAMA);
        }
    } while (!opcion.equals(IMenu.VALOR_SALIR));
    // ... desconectar el DAO ...
}

private static IMenu construirMenu() {
    IMenu menu = new MenuTexto(SCANNER);              // comparte el Scanner
    menu.setConCancelar(false);
    menu.setConSalir(true);
    menu.agregarOpcion(VALOR_OPCION_MOSTRAR_TODOS, TEXTO_MENU_MOSTRAR_TODAS_TECNOLOGIAS);
    menu.agregarOpcion(VALOR_OPCION_AGREGAR,       TEXTO_MENU_AGREGAR_TECNOLOGIA);
    menu.agregarOpcion(VALOR_OPCION_ACTUALIZAR,    TEXTO_MENU_ACTUALIZAR_TECNOLOGIA);
    menu.agregarOpcion(VALOR_OPCION_ELIMINAR,      TEXTO_MENU_ELIMINAR_TECNOLOGIA);
    return menu;
}
```

## Conceptos que este refactor ilustra

### 1. Polimorfismo aplicado de verdad
El CRUD declara `IMenu menu`, no `MenuTexto menu`. El programa cliente depende
del CONTRATO, no de la implementación. El día que exista `MenuDialog`,
`construirMenu()` será la ÚNICA línea que cambie — y puede imaginarse ya una
versión que lea la implementación de una configuración: ni siquiera eso haría
falta recompilar.

### 2. Datos vs. comportamiento: el switch sobre el valor
El menú devuelve el **valor** de la opción (`"agregar"`, `"salir"`), no un
número. El `switch` sobre cadenas de Java (válido desde Java 7, con flechas
desde Java 14) queda legible y SIN casos imposibles: ya no existe el
`default -> "opción inválida"` porque el menú no devuelve opciones inválidas.
La validación se fue al menú: cada clase hace SU trabajo.

### 3. Ya no hay confirmación de salida redundante
Antes: opción "5. Salir" → pregunta "¿Desea salir? (S/N)". Ahora la selección
misma es la confirmación: si elegiste "Salir", sales. Menos código, menos
fricción. (Si algún día se quisiera confirmar, sería responsabilidad del
programa cliente, no del menú.)

### 4. Un solo Scanner para toda la aplicación
`MenuTexto` ganó un constructor que recibe un `Scanner`. ¿Por qué importa?
Dos `Scanner` sobre `System.in` compiten por la misma entrada: con teclado
"suele funcionar", pero con entrada redirigida se roban líneas entre sí. El
CRUD crea el `Scanner` y se lo presta al menú: una sola fuente de lectura.

### 5. Constantes como vocabulario del dominio
En `Constantes` se agregaron `VALOR_OPCION_MOSTRAR_TODOS/AGREGAR/ACTUALIZAR/ELIMINAR`
(los valores compartidos por todos los CRUDs) y, por cada tabla, las constantes
`TEXTO_MENU_*` (el texto visible de cada opción). Se retiraron las constantes
que ya no tienen razón de ser: las numéricas `OP_*` (1..5), los textos
`. Mostrar todas las...` con prefijo de numeración (`MSG_MENU_MOSTRAR_*`,
`MSG_MENU_SALIR`), `MSG_INGRESE_OPCION_MENU` y `MSG_OPCION_INVALIDA`. Fíjate
en la diferencia de diseño: el valor es GLOBAL (identifica la acción); el
texto es LOCAL a cada catálogo (lo que ve el usuario).

### 6. El menú es intercambiable, la lógica es estable
Compara un CRUD de antes y de ahora: los métodos `agregar*`, `actualizar*`,
`borrar*`, `mostrarTodos*` y los lectores auxiliares (`leerEntero`,
`leerCadenaActualizable`, `leerBooleanActualizable`) NO cambiaron. Solo
cambió la capa de interacción. Eso es separación de responsabilidades: la
lógica de negocio/datos respira por sí sola.

## Cómo responde el usuario al menú

`solicitarOpcion()` acepta dos formas de respuesta y devuelve SIEMPRE el valor:

- La **etiqueta mostrada**: `1`, `2`, ... (o `a`, `b`... si el menú está en
  estilo LETRAS, o `i`, `ii`... en ROMANOS).
- El **valor de la opción**: `agregar`, `mostrar-todos`, ... y `salir`/`cancelar`
  para las opciones automáticas.

Si la respuesta no corresponde a nada, el menú avisa y vuelve a preguntar.

## Para probar

```bash
mvn compile
mvn exec:java -Dexec.mainClass=mx.edu.uaz.ingsoft.poo2.programas.CrudTecnologias
```

## Preguntas para discutir en clase

1. En `construirMenu()` el tipo de la variable es `IMenu` pero el objeto es
   `MenuTexto`. ¿Qué líneas cambiarían para usar un futuro `MenuDialog`?
2. ¿Por qué el valor de la opción es una constante compartida
   (`VALOR_OPCION_AGREGAR`) mientras que el texto vive en una constante por
   tabla (`TEXTO_MENU_AGREGAR_TECNOLOGIA`)?
3. El método `elegirOpcionMenu()` desapareció de los 8 CRUDs. ¿Qué parte de su
   trabajo fue a parar a `obtenerMenuComoTexto()` y cuál a `solicitarOpcion()`?
4. Si quisieras agregar una opción "Buscar por nombre" a un CRUD, ¿qué deberías
   tocar? ¿El menú, el CRUD, o ambos?

## Siguiente paso (acordado con el docente)

Implementación `MenuDialog` (JOptionPane) para demostrar la intercambiabilidad
real: mismo programa, distinta implementación de menú, cero cambios de lógica.
