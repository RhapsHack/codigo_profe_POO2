package mx.edu.uaz.ingsoft.poo2.modelo.daos.interf;

import mx.edu.uaz.ingsoft.poo2.modelo.entidades.Certificado;
import mx.edu.uaz.ingsoft.poo2.modelo.entidades.Inscripcion;

public interface ICertificadoDAO extends IBaseDAO<Certificado> {

    /**
     * Certificado de una inscripción (relación 1:1). Se resuelve con
     * {@code findByCampoUnico} porque el campo {@code inscripcion} es único.
     */
    Certificado findByInscripcion(Inscripcion inscripcion);
}