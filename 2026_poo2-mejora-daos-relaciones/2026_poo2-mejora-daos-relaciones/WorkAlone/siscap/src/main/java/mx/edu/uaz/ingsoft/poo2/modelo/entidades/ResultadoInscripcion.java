package mx.edu.uaz.ingsoft.poo2.modelo.entidades;

/**
 * Resultado de la evaluación de una inscripción a un curso.
 *
 * <p>Se persiste como texto ({@code @Enumerated(EnumType.STRING)}) para que el
 * valor guardado sea legible en la base y no dependa del orden de las
 * constantes.
 */
public enum ResultadoInscripcion {
    PENDIENTE,
    APROBADO,
    NO_APROBADO
}