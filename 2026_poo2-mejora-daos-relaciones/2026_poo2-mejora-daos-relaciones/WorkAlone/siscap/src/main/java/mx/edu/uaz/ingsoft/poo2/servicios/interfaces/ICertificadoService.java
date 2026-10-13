package mx.edu.uaz.ingsoft.poo2.servicios.interfaces;

import mx.edu.uaz.ingsoft.poo2.modelo.daos.DAOException;
import mx.edu.uaz.ingsoft.poo2.modelo.entidades.Certificado;
import mx.edu.uaz.ingsoft.poo2.modelo.entidades.Inscripcion;
import mx.edu.uaz.ingsoft.poo2.servicios.ReglaNegocioException;

import java.time.LocalDate;
import java.util.Optional;

public interface ICertificadoService extends IServicioBase<Certificado> {

    /**
     * Emite un certificado para una inscripción <b>aprobada</b> que todavía no
     * tenga uno, con folio único y fecha de emisión.
     *
     * @throws DAOException          si falla la persistencia (folio duplicado)
     * @throws ReglaNegocioException si la inscripción no está aprobada o ya tiene certificado
     */
    Certificado emitir(Inscripcion inscripcion, String folio, LocalDate fechaEmision)
            throws DAOException, ReglaNegocioException;

    Optional<Certificado> buscarPorInscripcion(Inscripcion inscripcion);
}