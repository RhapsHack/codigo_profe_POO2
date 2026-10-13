package mx.edu.uaz.ingsoft.poo2.utils.inout.impl;

import mx.edu.uaz.ingsoft.poo2.utils.inout.interfaces.ILectorDatos;

import static mx.edu.uaz.ingsoft.poo2.utils.Constantes.MSG_INGRESE_NUEVO_VALOR;
import static mx.edu.uaz.ingsoft.poo2.utils.Constantes.MSG_MANTENER_VALOR_ACTUAL;
import static mx.edu.uaz.ingsoft.poo2.utils.Constantes.MSG_VALOR_ACTUAL;

import java.util.List;

/**
 * Implementación base de {@link ILectorDatos} que aplica el patrón de diseño
 * <strong>Template Method</strong> (Método Plantilla).
 *
 * <p>Aquí vive TODO el algoritmo de lectura, validación, rangos, política de
 * valores vacíos y mensajes de error. La clase NO conoce el medio de entrada o
 * salida: delega en tres operaciones primitivas abstractas que implementa cada
 * subclase concreta:
 * <ul>
 *   <li>{@link #solicitarLinea(String)}: muestra el mensaje y lee una línea.</li>
 *   <li>{@link #mostrarMensaje(String)}: muestra un mensaje informativo.</li>
 *   <li>{@link #mostrarError(String)}: muestra un mensaje de error.</li>
 * </ul>
 *
 * <p>Nota didáctica (leer y parsear): TODAS las lecturas se hacen como LÍNEA
 * de texto y después se parsean ({@code Integer.parseInt},
 * {@code Double.parseDouble}, comparación de V/F). Esto evita el clásico error
 * de mezclar {@code Scanner.nextInt()} con {@code Scanner.nextLine()} (queda
 * sin consumir el salto de línea entre ambos) y, sobre todo, permite que este
 * mismo algoritmo funcione con cualquier medio de entrada: en modo diálogo,
 * {@code JOptionPane.showInputDialog} devuelve una {@code String} exactamente
 * como la línea de {@code Scanner}.
 *
 * <p>Nota didáctica (delegación de sobrecargas): los métodos de lectura se
 * construyen unos sobre otros. Por ejemplo, {@code leerEntero(mensaje)} parsea
 * la línea leída con {@code leerTexto(mensaje)}, y
 * {@code leerEntero(mensaje, min, max)} valida el resultado de
 * {@code leerEntero(mensaje)}. Así la validación se implementa una sola vez.
 *
 * <p>Los mensajes de error por defecto son constantes protegidas: cada método,
 * al detectar un error, usa SIEMPRE el mensaje correspondiente, con lo que los
 * textos de error son consistentes en todo el programa.
 */
public abstract class AbstractLectorDatos implements ILectorDatos {

    // ----- Mensajes de error por defecto (consistencia entre métodos) -----
    protected static final String MSG_ENTERO_INVALIDO =
            "Valor inválido. Debe ingresar un número entero.";
    protected static final String MSG_DOUBLE_INVALIDO =
            "Valor inválido. Debe ingresar un número decimal.";
    protected static final String MSG_BOOLEANO_INVALIDO =
            "Valor inválido. Debe ingresar V o F.";
    protected static final String MSG_FUERA_RANGO =
            "Valor inválido. El número está fuera del rango permitido.";
    protected static final String MSG_TEXTO_NO_VACIO =
            "El valor no puede estar vacío.";

    private boolean permitirValoresVacios = true;

    // ----- Operaciones primitivas (las implementan las subclases) -----

    /** Muestra {@code mensaje} y lee una línea de la fuente de entrada. */
    protected abstract String solicitarLinea(String mensaje);

    /** Muestra {@code mensaje} en la fuente de salida informativa. */
    protected abstract void mostrarMensaje(String mensaje);

    /** Muestra {@code mensaje} en la fuente de salida de errores. */
    public abstract void mostrarError(String mensaje);

    // ----- Configuración -----

    @Override
    public boolean isPermitirValoresVacios() {
        return permitirValoresVacios;
    }

    @Override
    public void setPermitirValoresVacios(boolean permitirValoresVacios) {
        this.permitirValoresVacios = permitirValoresVacios;
    }

    // ----- Lectura de texto -----

    @Override
    public String leerTexto() {
        return leerTexto("");
    }

    @Override
    public String leerTexto(String mensaje) {
        while (true) {
            String valor = solicitarLinea(mensaje);
            if (permitirValoresVacios || !valor.isEmpty()) {
                return valor;
            }
            mostrarError(MSG_TEXTO_NO_VACIO);
        }
    }

    @Override
    public String leerTexto(String mensaje, String valorActual) {
        String valor = solicitarLinea(mensaje);
        return valor.isEmpty() ? valorActual : valor;
    }

    @Override
    public String leerTextoNoVacio(String mensaje) {
        while (true) {
            String valor = solicitarLinea(mensaje);
            if (!valor.isEmpty()) {
                return valor;
            }
            mostrarError(MSG_TEXTO_NO_VACIO);
        }
    }

    // ----- Lectura de entero -----

    @Override
    public int leerEntero() {
        return leerEntero("");
    }

    @Override
    public int leerEntero(String mensaje) {
        while (true) {
            try {
                return Integer.parseInt(leerTexto(mensaje).trim());
            } catch (NumberFormatException e) {
                mostrarError(MSG_ENTERO_INVALIDO);
            }
        }
    }

    @Override
    public int leerEnteroActualizable(String mensaje, Integer valorActual) {
        validarValorActualNoNulo(valorActual);
        while (true) {
            String valor = solicitarLinea(mensaje);
            if (valor.isEmpty()) {
                return valorActual;
            }
            try {
                return Integer.parseInt(valor.trim());
            } catch (NumberFormatException e) {
                mostrarError(MSG_ENTERO_INVALIDO);
            }
        }
    }

    @Override
    public int leerEntero(int valorMinimo) {
        return leerEntero("", valorMinimo);
    }

    @Override
    public int leerEntero(int valorMinimo, int valorMaximo) {
        return leerEntero("", valorMinimo, valorMaximo);
    }

    @Override
    public int leerEntero(String mensaje, int valorMinimo) {
        while (true) {
            int valor = leerEntero(mensaje);
            if (valor >= valorMinimo) {
                return valor;
            }
            mostrarError(MSG_FUERA_RANGO);
        }
    }

    @Override
    public int leerEntero(String mensaje, int valorMinimo, int valorMaximo) {
        while (true) {
            int valor = leerEntero(mensaje);
            if (valor >= valorMinimo && valor <= valorMaximo) {
                return valor;
            }
            mostrarError(MSG_FUERA_RANGO);
        }
    }

    // ----- Lectura de decimal (double) -----

    @Override
    public double leerDouble() {
        return leerDouble("");
    }

    @Override
    public double leerDouble(String mensaje) {
        while (true) {
            try {
                return Double.parseDouble(leerTexto(mensaje).trim());
            } catch (NumberFormatException e) {
                mostrarError(MSG_DOUBLE_INVALIDO);
            }
        }
    }

    @Override
    public double leerDoubleActualizable(String mensaje, Double valorActual) {
        validarValorActualNoNulo(valorActual);
        while (true) {
            String valor = solicitarLinea(mensaje);
            if (valor.isEmpty()) {
                return valorActual;
            }
            try {
                return Double.parseDouble(valor.trim());
            } catch (NumberFormatException e) {
                mostrarError(MSG_DOUBLE_INVALIDO);
            }
        }
    }

    @Override
    public double leerDouble(double valorMinimo) {
        return leerDouble("", valorMinimo);
    }

    @Override
    public double leerDouble(double valorMinimo, double valorMaximo) {
        return leerDouble("", valorMinimo, valorMaximo);
    }

    @Override
    public double leerDouble(String mensaje, double valorMinimo) {
        while (true) {
            double valor = leerDouble(mensaje);
            if (valor >= valorMinimo) {
                return valor;
            }
            mostrarError(MSG_FUERA_RANGO);
        }
    }

    @Override
    public double leerDouble(String mensaje, double valorMinimo, double valorMaximo) {
        while (true) {
            double valor = leerDouble(mensaje);
            if (valor >= valorMinimo && valor <= valorMaximo) {
                return valor;
            }
            mostrarError(MSG_FUERA_RANGO);
        }
    }

    // ----- Lectura de booleano -----

    @Override
    public boolean leerBoolean() {
        return leerBoolean("");
    }

    @Override
    public boolean leerBoolean(String mensaje) {
        while (true) {
            String valor = leerTexto(mensaje);
            if (valor.equalsIgnoreCase("V")) {
                return true;
            }
            if (valor.equalsIgnoreCase("F")) {
                return false;
            }
            mostrarError(MSG_BOOLEANO_INVALIDO);
        }
    }

    @Override
    public boolean leerBoolean(String mensaje, Boolean valorActual) {
        validarValorActualNoNulo(valorActual);
        while (true) {
            String valor = solicitarLinea(mensaje);
            if (valor.isEmpty()) {
                return valorActual;
            }
            if (valor.equalsIgnoreCase("V")) {
                return true;
            }
            if (valor.equalsIgnoreCase("F")) {
                return false;
            }
            mostrarError(MSG_BOOLEANO_INVALIDO);
        }
    }

    // ----- Presentación de textos -----

    @Override
    public void mostrarTextos(String[] textos) {
        for (String texto : textos) {
            mostrarMensaje(texto);
        }
    }

    @Override
    public void mostrarTextos(List<String> textos) {
        for (String texto : textos) {
            mostrarMensaje(texto);
        }
    }


    @Override 
    public void mostrarTexto(String unTexto){
        mostrarMensaje(unTexto);
    }
    private void validarValorActualNoNulo(Object valorActual) {
        if (valorActual == null) {
            throw new IllegalArgumentException("El valor actual no puede ser nulo.");
        }
    }



}