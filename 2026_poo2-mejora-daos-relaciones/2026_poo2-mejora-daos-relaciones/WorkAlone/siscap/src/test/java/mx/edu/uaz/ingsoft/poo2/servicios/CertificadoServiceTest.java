package mx.edu.uaz.ingsoft.poo2.servicios;

import jakarta.persistence.EntityManager;
import mx.edu.uaz.ingsoft.poo2.modelo.daos.DAOException;
import mx.edu.uaz.ingsoft.poo2.modelo.daos.impl.CapacitacionBaseDePruebas;
import mx.edu.uaz.ingsoft.poo2.modelo.daos.impl.CertificadoDAO;
import mx.edu.uaz.ingsoft.poo2.modelo.daos.impl.CursoDAO;
import mx.edu.uaz.ingsoft.poo2.modelo.daos.impl.InscripcionDAO;
import mx.edu.uaz.ingsoft.poo2.modelo.entidades.Curso;
import mx.edu.uaz.ingsoft.poo2.modelo.entidades.Inscripcion;
import mx.edu.uaz.ingsoft.poo2.modelo.entidades.ResultadoInscripcion;
import mx.edu.uaz.ingsoft.poo2.modelo.utils.JPAUtil;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Pruebas de la regla de negocio del certificado: solo se emite sobre una
 * inscripción APROBADA y como máximo uno por inscripción. La regla vive en
 * {@link CertificadoService} y se señala con {@link ReglaNegocioException},
 * separada de {@code DAOException}.
 */
public class CertificadoServiceTest {

    private EntityManager em;
    private CertificadoDAO certificadoDAO;
    private InscripcionDAO inscripcionDAO;
    private CursoDAO cursoDAO;
    private CertificadoService certificadoService;

    @BeforeEach
    public void preparar() throws DAOException {
        CapacitacionBaseDePruebas.sembrar();
        em = JPAUtil.crearEntityManager();
        certificadoDAO = new CertificadoDAO(em);
        inscripcionDAO = new InscripcionDAO(em);
        cursoDAO = new CursoDAO(em);
        certificadoService = new CertificadoService(certificadoDAO);
    }

    @AfterEach
    public void cerrar() {
        em.close();
    }

    @Test
    @DisplayName("Rechaza emitir certificado de una inscripción no aprobada")
    public void rechazaInscripcionNoAprobada() {
        Inscripcion pendiente = inscripcionPendiente();

        assertThrows(ReglaNegocioException.class,
                () -> certificadoService.emitir(pendiente, "CERT-X", LocalDate.now()));
    }

    @Test
    @DisplayName("Rechaza emitir un segundo certificado para la misma inscripción")
    public void rechazaCertificadoDuplicado() {
        Curso javaBasico = cursoDAO.findByCampo("nombre", "Java Básico").get(0);
        Inscripcion conCertificado = inscripcionDAO.findAprobadasDeCurso(javaBasico).get(0);

        assertThrows(ReglaNegocioException.class,
                () -> certificadoService.emitir(conCertificado, "CERT-X", LocalDate.now())
                        .getInscripcion());
    }

    @Test
    @DisplayName("Emite el certificado sobre una inscripción aprobada sin certificado")
    public void emiteCertificadoValido() throws DAOException, ReglaNegocioException {
        Inscripcion inscripcion = inscripcionPendiente();
        inscripcion.setResultado(ResultadoInscripcion.APROBADO);
        inscripcionDAO.update(inscripcion);

        var certificado = certificadoService.emitir(inscripcion, "CERT-TEST-01", LocalDate.now());

        assertNotNull(certificado.getId());
        assertNotNull(certificadoDAO.findByInscripcion(inscripcion));
    }

    private Inscripcion inscripcionPendiente() {
        Curso javaBasico = cursoDAO.findByCampo("nombre", "Java Básico").get(0);
        return inscripcionDAO.findByCurso(javaBasico).stream()
                .filter(inscripcion -> inscripcion.getResultado() != ResultadoInscripcion.APROBADO)
                .findFirst()
                .orElseThrow();
    }
}