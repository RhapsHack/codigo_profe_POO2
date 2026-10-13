package mx.edu.uaz.ingsoft.poo2.utils.menu.interfaces;

import mx.edu.uaz.ingsoft.poo2.modelo.enumeraciones.EstiloNumeracionMenu;

import static mx.edu.uaz.ingsoft.poo2.utils.Constantes.VALOR_OPCION_CANCELAR;
import static mx.edu.uaz.ingsoft.poo2.utils.Constantes.VALOR_OPCION_SALIR;

/**
 * Abstracción de un menú.
 *
 * Un menú administra opciones y su configuración, pero NO ejecuta acciones:
 * únicamente presenta sus opciones y devuelve el valor de la opción que el
 * usuario seleccionó. Cada implementación decide CÓMO mostrar y capturar:
 * MenuTexto usará la consola (System.out / Scanner) y MenuDialog usará
 * diálogos gráficos (JOptionPane.showXXXDialog). Gracias al polimorfismo,
 * ambas implementaciones son intercambiables para el programa que las usa
 * (principio de sustitución de Liskov): el programa trabaja siempre con IMenu.
 *
 * Nota didáctica: las constantes de una interfaz son públicas y estáticas por
 * omisión. Aquí se exponen los valores de las opciones automáticas, tomando
 * las cadenas desde la clase Constantes (única fuente de textos del proyecto).
 */
public interface IMenu {

    /** Valor con el que el menú identifica a su opción automática "Cancelar". */
    String VALOR_CANCELAR = VALOR_OPCION_CANCELAR;

    /** Valor con el que el menú identifica a su opción automática "Salir". */
    String VALOR_SALIR = VALOR_OPCION_SALIR;

    // ----- Gestión de opciones -----

    /**
     * Agrega al menú una opción ya construida.
     *
     * @return true si se agregó; false si el valor de la opción ya existe en el menú.
     */
    boolean agregarOpcion(IOpcionMenu opcion);

    /**
     * Comodidad: agrega una opción a partir de una sola cadena. El valor de la
     * opción es la cadena recibida y el texto es la misma cadena con la primera
     * letra en mayúscula (p. ej. agregarOpcion("abrir") se mostraría "Abrir").
     *
     * @return true si se agregó; false si ya existía una opción con ese valor.
     */
    boolean agregarOpcion(String texto);

    /**
     * Agrega una opción con valor y texto distintos.
     *
     * @return true si se agregó; false si ya existía una opción con ese valor.
     */
    boolean agregarOpcion(String valor, String texto);

    /**
     * Quita del menú la opción identificada por el valor indicado.
     *
     * @return true si existía y se quitó; false en caso contrario.
     */
    boolean quitarOpcion(String valor);

    /**
     * Habilita la opción identificada por el valor indicado.
     *
     * @return true si existía; false en caso contrario.
     */
    boolean habilitarOpcion(String valor);

    /**
     * Inhabilita la opción identificada por el valor indicado. Una opción
     * inhabilitada no se muestra y no puede ser seleccionada.
     *
     * @return true si existía; false en caso contrario.
     */
    boolean inhabilitarOpcion(String valor);

    /**
     * Indica si el menú contiene una opción con el valor indicado, sin importar
     * si está habilitada o inhabilitada.
     */
    boolean tieneOpcion(String valor);

    /**
     * Devuelve la opción identificada por el valor indicado, o null si no existe.
     */
    IOpcionMenu obtenerOpcion(String valor);

    /**
     * Devuelve cuántas opciones han sido agregadas al menú (sin contar a
     * Cancelar ni Salir, que son automáticas).
     */
    int obtenerTotalOpciones();

    // ----- Configuración del menú -----

    /** Establece el estilo de etiquetado de las opciones. Predeterminado: NUMEROS. */
    void setEstiloNumeracion(EstiloNumeracionMenu estilo);

    /**
     * Establece la cadena separadora entre la etiqueta y el texto de cada
     * opción, por ejemplo: ") ", " " o "- ". Predeterminado: ") ".
     */
    void setSeparador(String separador);

    /** Activa o desactiva la opción automática "Cancelar" (se muestra al final). */
    void setConCancelar(boolean conCancelar);

    /** Activa o desactiva la opción automática "Salir" (se muestra al final). */
    void setConSalir(boolean conSalir);

    /** Indica si la opción automática "Cancelar" está activada. */
    boolean tieneCancelar();

    /** Indica si la opción automática "Salir" está activada. */
    boolean tieneSalir();

    // ----- Presentación e interacción -----

    /**
     * Devuelve la versión textual del menú: una línea por cada opción
     * habilitada, con el formato "etiqueta + separador + texto", y al final las
     * opciones automáticas Cancelar y/o Salir si están activadas. Las opciones
     * inhabilitadas se omiten.
     */
    String obtenerMenuComoTexto();

    /**
     * Muestra el menú en el medio propio de la implementación (consola,
     * diálogo gráfico, ...). No captura ninguna respuesta.
     */
    void mostrarMenu();

    /**
     * Solicita al usuario que elija una opción: muestra el menú, lee su
     * respuesta y valida que corresponda a una opción existente y habilitada,
     * repitiendo la solicitud hasta obtener una respuesta válida. El usuario
     * responde con la etiqueta mostrada (1, a, i, ...) o con el valor de la
     * opción; en ambos casos el método devuelve el VALOR de la opción elegida.
     *
     * @return el valor de la opción elegida, o bien VALOR_CANCELAR o
     *         VALOR_SALIR si el usuario eligió alguna de esas opciones
     *         automáticas. El menú NO ejecuta ninguna acción con el valor
     *         devuelto: eso corresponde al programa que usa el menú.
     */
    String solicitarOpcion();
}
