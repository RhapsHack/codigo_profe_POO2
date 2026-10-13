package mx.edu.uaz.ingsoft.poo2.servicios;

/**
 * Excepción para violaciones de una regla de negocio (no de persistencia).
 *
 * <p>{@code DAOException} señala fallos al hablar con la base; esta señala que
 * la operación es inválida por las reglas del dominio, por ejemplo emitir un
 * certificado sobre una inscripción que no está aprobada. Se separa para que
 * la vista pueda distinguir "la base falló" de "así no se puede hacer".
 */
public class ReglaNegocioException extends Exception {
    public ReglaNegocioException(String mensaje) {
        super(mensaje);
    }
}