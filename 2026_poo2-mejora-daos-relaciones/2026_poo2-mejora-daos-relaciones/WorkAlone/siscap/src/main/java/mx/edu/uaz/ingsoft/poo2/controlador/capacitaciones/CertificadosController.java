package mx.edu.uaz.ingsoft.poo2.controlador.capacitaciones;

import mx.edu.uaz.ingsoft.poo2.modelo.daos.DAOException;
import mx.edu.uaz.ingsoft.poo2.modelo.daos.DaoServiceFactory;
import mx.edu.uaz.ingsoft.poo2.modelo.entidades.Certificado;
import mx.edu.uaz.ingsoft.poo2.modelo.entidades.Inscripcion;
import mx.edu.uaz.ingsoft.poo2.servicios.ReglaNegocioException;
import mx.edu.uaz.ingsoft.poo2.servicios.interfaces.ICertificadoService;
import mx.edu.uaz.ingsoft.poo2.servicios.interfaces.IInscripcionService;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static mx.edu.uaz.ingsoft.poo2.utils.Constantes.CAT_CERTIFICADO;
import static mx.edu.uaz.ingsoft.poo2.utils.Constantes.CAT_INSCRIPCION;

/**
 * Coordina la pantalla de certificados: emitir el certificado de una
 * inscripción aprobada (la regla vive en el servicio) y consultarlo.
 */
public class CertificadosController {

    private final ICertificadoService certificadoService;
    private final IInscripcionService inscripcionService;

    public CertificadosController() {
        this.certificadoService = (ICertificadoService) DaoServiceFactory.getServicio(CAT_CERTIFICADO).orElseThrow();
        this.inscripcionService = (IInscripcionService) DaoServiceFactory.getServicio(CAT_INSCRIPCION).orElseThrow();
    }

    public List<Inscripcion> inscripciones() {
        return inscripcionService.recuperarTodosRegistros();
    }

    public List<Certificado> certificados() {
        return certificadoService.recuperarTodosRegistros();
    }

    public Certificado emitir(Inscripcion inscripcion, String folio, LocalDate fechaEmision)
            throws DAOException, ReglaNegocioException {
        return certificadoService.emitir(inscripcion, folio, fechaEmision);
    }

    public Optional<Certificado> consultar(Inscripcion inscripcion) {
        return certificadoService.buscarPorInscripcion(inscripcion);
    }
}