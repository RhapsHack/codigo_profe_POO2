package mx.edu.uaz.ingsoft.poo2.servicios;

import mx.edu.uaz.ingsoft.poo2.modelo.daos.DAOException;
import mx.edu.uaz.ingsoft.poo2.modelo.daos.interf.ICertificadoDAO;
import mx.edu.uaz.ingsoft.poo2.modelo.entidades.Certificado;
import mx.edu.uaz.ingsoft.poo2.modelo.entidades.Inscripcion;
import mx.edu.uaz.ingsoft.poo2.modelo.entidades.ResultadoInscripcion;
import mx.edu.uaz.ingsoft.poo2.servicios.interfaces.ICertificadoService;

import java.time.LocalDate;
import java.util.Optional;

import static mx.edu.uaz.ingsoft.poo2.utils.Constantes.MSG_CERTIFICADO_DUPLICADO;
import static mx.edu.uaz.ingsoft.poo2.utils.Constantes.MSG_INSCRIPCION_NO_APROBADA;

public class CertificadoService extends ServicioConBorradoLogico<Certificado, ICertificadoDAO>
        implements ICertificadoService {

    public CertificadoService(ICertificadoDAO dao) {
        super(dao);
    }

    @Override
    public Certificado emitir(Inscripcion inscripcion, String folio, LocalDate fechaEmision)
            throws DAOException, ReglaNegocioException {
        if (inscripcion == null || inscripcion.getId() == null) {
            throw new ReglaNegocioException("La inscripcion no existe.");
        }
        if (inscripcion.getResultado() != ResultadoInscripcion.APROBADO) {
            throw new ReglaNegocioException(MSG_INSCRIPCION_NO_APROBADA);
        }
        if (dao.findByInscripcion(inscripcion) != null) {
            throw new ReglaNegocioException(MSG_CERTIFICADO_DUPLICADO);
        }
        var certificado = new Certificado(null, true, inscripcion, folio, fechaEmision);
        dao.save(certificado);
        return certificado;
    }

    @Override
    public Optional<Certificado> buscarPorInscripcion(Inscripcion inscripcion) {
        return Optional.ofNullable(dao.findByInscripcion(inscripcion));
    }
}