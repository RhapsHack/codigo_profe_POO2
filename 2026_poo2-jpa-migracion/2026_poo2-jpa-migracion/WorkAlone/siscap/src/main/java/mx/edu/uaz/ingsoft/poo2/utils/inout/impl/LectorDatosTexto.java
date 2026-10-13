package mx.edu.uaz.ingsoft.poo2.utils.inout.impl;

import java.io.InputStream;
import java.util.Scanner;

/**
 * Implementación de {@link AbstractLectorDatos} que lee y muestra en la
 * CONSOLA: la lectura usa un {@code Scanner}, los mensajes van a
 * {@code System.out} y los errores a {@code System.err}.
 *
 * <p>Como toda la lógica de validación vive en la clase base (Template
 * Method), esta clase solo responde las tres operaciones primitivas:
 * <ul>
 *   <li>{@code solicitarLinea} escribe el mensaje sin salto de línea y lee con
 *       {@code Scanner.nextLine()}.</li>
 *   <li>{@code mostrarMensaje} escribe una línea informativa con
 *       {@code System.out.println}.</li>
 *   <li>{@code mostrarError} escribe una línea de error con
 *       {@code System.err.println}.</li>
 * </ul>
 */
public class LectorDatosTexto extends AbstractLectorDatos {

    private final Scanner scanner;

    /** Lee de la entrada estándar ({@code System.in}) con su propio Scanner. */
    public LectorDatosTexto() {
        this(System.in);
    }

    /**
     * Lee de la fuente de entrada recibida. Permite inyectar un
     * {@code ByteArrayInputStream} en las pruebas unitarias, sin depender de
     * la entrada estándar real.
     */
    public LectorDatosTexto(InputStream entrada) {
        this.scanner = new Scanner(entrada);
    }

    @Override
    protected String solicitarLinea(String mensaje) {
        System.out.print(mensaje);
        return scanner.nextLine();
    }

    @Override
    protected void mostrarMensaje(String mensaje) {
        System.out.println(mensaje);
    }

    @Override
    public void mostrarError(String mensaje) {
        System.err.println(mensaje);
    }
}