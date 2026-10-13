package mx.edu.uaz.ingsoft.poo2.utils.menu.interfaces;

/**
 * Abstracción de una opción de menú.
 *
 * Una opción de menú SOLO guarda información: el valor que la identifica, el
 * texto que se muestra al usuario y su estado (habilitada/inhabilitada). No
 * ejecuta ninguna acción: quien usa el menú decide qué hacer con el valor de
 * la opción elegida.
 *
 * Nota didáctica: la etiqueta con la que se presenta cada opción (1, 2, ...,
 * a, b, ..., i, ii, ...) NO forma parte de la opción. El menú la calcula según
 * la posición de la opción al momento de generar el menú. La identidad de la
 * opción es su valor, y este nunca cambia.
 */
public interface IOpcionMenu {

    /** Devuelve el valor que identifica a la opción dentro de su menú (p. ej. "abrir"). */
    String getValor();

    /** Devuelve el texto que se muestra al usuario (p. ej. "Abrir archivo"). */
    String getTexto();

    /** Establece el texto que se muestra al usuario. El valor jamás cambia. */
    void setTexto(String texto);

    /** Indica si la opción está habilitada. */
    boolean isHabilitada();

    /** Habilita (true) o inhabilita (false) la opción. */
    void setHabilitada(boolean habilitada);
}
