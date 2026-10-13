package mx.edu.uaz.ingsoft.poo2.utils.menu.impl;

import mx.edu.uaz.ingsoft.poo2.utils.Constantes;
import mx.edu.uaz.ingsoft.poo2.utils.menu.interfaces.IOpcionMenu;

/**
 * Implementación de {@link IOpcionMenu}: una opción de menú que SOLO guarda
 * información (valor, texto y estado habilitada/inhabilitada). No ejecuta
 * ninguna acción.
 *
 * Nota didáctica (inmutabilidad del valor): el valor identifica a la opción
 * dentro de su menú, así que no se permite cambiarlo después de construida;
 * solo se fija en el constructor. Esta decisión se apoya en la palabra
 * reservada {@code final}.
 */
public class OpcionMenu implements IOpcionMenu {
    private final String valor;
    private String texto;
    private boolean habilitada;

    /**
     * Constructor mínimo: el texto es el valor con la primera letra en
     * mayúscula (p. ej. el valor "abrir" se mostraría "Abrir") y la opción
     * queda habilitada.
     */
    public OpcionMenu(String valor) {
        this(valor, capitalizarPrimeraLetra(valor), true);
    }

    /** Constructor completo: valor y texto distintos, estado inicial configurable. */
    public OpcionMenu(String valor, String texto, boolean habilitada) {
        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException(Constantes.ERR_OPCION_VALOR_REQUERIDO);
        }
        if (texto == null || texto.isBlank()) {
            throw new IllegalArgumentException(Constantes.ERR_OPCION_TEXTO_REQUERIDO);
        }
        this.valor = valor;
        this.texto = texto;
        this.habilitada = habilitada;
    }

    @Override
    public String getValor() {
        return valor;
    }

    @Override
    public String getTexto() {
        return texto;
    }

    @Override
    public void setTexto(String texto) {
        if (texto == null || texto.isBlank()) {
            throw new IllegalArgumentException(Constantes.ERR_OPCION_TEXTO_REQUERIDO);
        }
        this.texto = texto;
    }

    @Override
    public void setHabilitada(boolean habilitada) {
        this.habilitada = habilitada;
    }

    @Override
    public boolean isHabilitada() {
        return habilitada;
    }

    @Override
    public String toString() {
        return valor + " - " + texto;
    }

    /**
     * Devuelve la cadena con su primera letra en mayúscula (soporta acentos:
     * "árbol" se convierte en "Árbol"). Si la cadena es nula o vacía se
     * devuelve tal cual; la validación corre por cuenta del constructor.
     */
    static String capitalizarPrimeraLetra(String cadena) {
        if (cadena == null || cadena.isEmpty()) {
            return cadena;
        }
        char primera = Character.toUpperCase(cadena.charAt(0));
        return primera + cadena.substring(1);
    }
}
