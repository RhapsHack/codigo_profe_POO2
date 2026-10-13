package mx.edu.uaz.ingsoft.poo2.utils.inout.interfaces;

import java.util.List;

/**
 * Abstracción de la lectura de datos y la presentación de mensajes.
 *
 * <p>Un {@code ILectorDatos} ofrece métodos para leer texto, números enteros,
 * decimales y booleanos desde un medio de entrada, así como métodos para
 * mostrar textos en un medio de salida. La interfaz NO dice CÓMO se lee o se
 * muestra: cada implementación concreta decide el medio (consola con
 * {@code Scanner}/{@code System.out}, cuadros de diálogo con
 * {@code JOptionPane}, etc.).
 *
 * <p>Gracias a que los métodos de lectura se construyen por delegación —todas
 * las variantes parten de la lectura de una línea de texto—, la validación es
 * una sola vez: si falla el parseo de un entero, de un decimal o de un
 * booleano, se muestra un mensaje de error por defecto y se repite la lectura.
 *
 * <p>Nota didáctica (política de vacíos): el atributo
 * {@code permitirValoresVacios} indica si se acepta una línea vacía (solo
 * Enter). Es útil en los formularios de ACTUALIZACIÓN de los CRUD: escribir
 * solo Enter conserva el valor actual del campo.
 */
public interface ILectorDatos {

    // ----- Configuración -----

    /** Indica si se aceptan cadenas vacías (solo Enter) al leer texto. */
    boolean isPermitirValoresVacios();

    /**
     * Establece si se aceptan cadenas vacías. Predeterminado: true.
     * Con false, se muestra el mensaje de error por defecto y se repite la
     * lectura hasta obtener un valor no vacío.
     */
    void setPermitirValoresVacios(boolean permitirValoresVacios);

    // ----- Lectura de texto -----

    /** Lee una línea de texto sin mostrar mensaje. */
    String leerTexto();

    /** Muestra el mensaje y lee una línea de texto. */
    String leerTexto(String mensaje);

    /**
     * Muestra el mensaje y lee una línea. Si la línea es vacía, devuelve
     * {@code valorActual} (útil al actualizar un campo: Enter conserva el
     * valor actual). Este método siempre acepta el vacío, con independencia de
     * la política {@code permitirValoresVacios}.
     */
    String leerTexto(String mensaje, String valorActual);

    /**
     * Lee texto sin aceptar líneas vacías: si la línea es vacía se muestra el
     * mensaje de error por defecto y se repite la lectura.
     */
    String leerTextoNoVacio(String mensaje);

    // ----- Lectura de entero -----

    /** Lee un número entero sin mostrar mensaje. */
    int leerEntero();

    /** Muestra el mensaje y lee un número entero. */
    int leerEntero(String mensaje);

    /**
     * Muestra el mensaje y lee un número entero. Si la línea es vacía,
     * devuelve {@code valorActual}. {@code valorActual} no debe ser nulo.
     *
     * <p>Nota didáctica: este método NO se llama {@code leerEntero(mensaje,
     * valorActual)}. Una sobrecarga con {@code Integer} y otra con {@code int}
     * serían ambiguas para el llamador: {@code leerEntero("Id: ", 10)} elegiría
     * la versión de {@code int} (mínimo) en lugar de la de {@code Integer}
     * (valor actual), porque en la resolución de sobrecarga {@code int}
     * coincide exacto y gana sobre el *autoboxing* a {@code Integer}. Por eso
     * las variantes "actualizables" numéricas usan un nombre propio.
     */
    int leerEnteroActualizable(String mensaje, Integer valorActual);

    /** Lee un entero mayor o igual a {@code valorMinimo}, sin mensaje. */
    int leerEntero(int valorMinimo);

    /** Lee un entero en el rango [mínimo, máximo], sin mensaje. */
    int leerEntero(int valorMinimo, int valorMaximo);

    /** Lee un entero mayor o igual a {@code valorMinimo}, con mensaje. */
    int leerEntero(String mensaje, int valorMinimo);

    /** Lee un entero en el rango [mínimo, máximo], con mensaje. */
    int leerEntero(String mensaje, int valorMinimo, int valorMaximo);

    // ----- Lectura de decimal (double) -----

    /** Lee un número decimal sin mostrar mensaje. */
    double leerDouble();

    /** Muestra el mensaje y lee un número decimal. */
    double leerDouble(String mensaje);

    /**
     * Muestra el mensaje y lee un número decimal. Si la línea es vacía,
     * devuelve {@code valorActual}. {@code valorActual} no debe ser nulo.
     *
     * <p>Mismo criterio de nombre que {@link #leerEnteroActualizable(String,
     * Integer)} para evitar la ambigüedad entre la variante de valor actual
     * ({@code Double}) y la de mínimo ({@code double}).
     */
    double leerDoubleActualizable(String mensaje, Double valorActual);

    /** Lee un decimal mayor o igual a {@code valorMinimo}, sin mensaje. */
    double leerDouble(double valorMinimo);

    /** Lee un decimal en el rango [mínimo, máximo], sin mensaje. */
    double leerDouble(double valorMinimo, double valorMaximo);

    /** Lee un decimal mayor o igual a {@code valorMinimo}, con mensaje. */
    double leerDouble(String mensaje, double valorMinimo);

    /** Lee un decimal en el rango [mínimo, máximo], con mensaje. */
    double leerDouble(String mensaje, double valorMinimo, double valorMaximo);

    // ----- Lectura de booleano -----

    /** Lee un booleano (V/F) sin mostrar mensaje. */
    boolean leerBoolean();

    /** Muestra el mensaje y lee un booleano (V/F). */
    boolean leerBoolean(String mensaje);

    /**
     * Muestra el mensaje y lee un booleano (V/F). Si la línea es vacía,
     * devuelve {@code valorActual}. {@code valorActual} no debe ser nulo.
     */
    boolean leerBoolean(String mensaje, Boolean valorActual);

    // ----- Presentación de textos -----

    /** Muestra cada texto del arreglo en el medio de salida de la implementación. */
    void mostrarTextos(String[] textos);

    /** Muestra cada texto de la lista en el medio de salida de la implementación. */
    void mostrarTextos(List<String> textos);
    /** Muestra una línea de texto */
    void mostrarTexto(String texto);

    /**
     * Muestra un mensaje de error en el medio de salida de errores de la
     * implementación (consola por {@code System.err}, cuadro de diálogo de
     * error, etc.).
     */
    void mostrarError(String mensaje);

}