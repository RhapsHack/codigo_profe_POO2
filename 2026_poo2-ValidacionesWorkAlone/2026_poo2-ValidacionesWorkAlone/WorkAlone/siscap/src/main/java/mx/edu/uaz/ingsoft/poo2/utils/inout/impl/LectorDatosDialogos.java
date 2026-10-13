package mx.edu.uaz.ingsoft.poo2.utils.inout.impl;

import javax.swing.JOptionPane;

/**
 * Implementación de {@link AbstractLectorDatos} que lee y muestra mediante
 * CUADROS DE DIÁLOGO ({@code JOptionPane}).
 *
 * <p>Esta es la demostración del beneficio del Template Method: para cambiar
 * el medio de entrada de consola a diálogos NO se toca el algoritmo de
 * validación (vive en {@link AbstractLectorDatos}); solo se responden las tres
 * operaciones primitivas:
 * <ul>
 *   <li>{@code solicitarLinea} usa {@code JOptionPane.showInputDialog} (el
 *       propio diálogo muestra el mensaje).</li>
 *   <li>{@code mostrarMensaje} usa {@code JOptionPane.showMessageDialog}.</li>
 *   <li>{@code mostrarError} usa {@code JOptionPane.showMessageDialog} con
 *       tipo de mensaje de ERROR.</li>
 * </ul>
 *
 * <p>Nota: en esta etapa del proyecto esta implementación se compila pero los
 * programas todavía interactúan en modo texto ({@link LectorDatosTexto}). Su
 * uso real llegará en la etapa de cuadros de diálogo.
 */
public class LectorDatosDialogos extends AbstractLectorDatos {

    @Override
    protected String solicitarLinea(String mensaje) {
        return JOptionPane.showInputDialog(mensaje);
    }

    @Override
    protected void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(null, mensaje);
    }

    @Override
    public void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(null, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }
}