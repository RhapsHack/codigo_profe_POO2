package mx.edu.uaz.ingsoft.poo2.modelo.daos;

/**
 * Excepción de la capa de acceso a datos.
 *
 * <p>Es checked (hereda de {@link Exception}) a propósito: el compilador
 * obliga a cada eslabón de la cadena (DAO → servicio → controlador o
 * programa) a decidir qué hacer con un fallo de persistencia, en lugar de
 * permitir que el error se pierda en un resultado {@code null}.
 *
 * <p>La causa original siempre se conserva con
 * {@link #getCause()}, de modo que el log y el mensaje mostrado al usuario
 * pueden decir qué falló de verdad.
 */
public class DAOException extends Exception {
    private static final long serialVersionUID = 1L;

    public DAOException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
