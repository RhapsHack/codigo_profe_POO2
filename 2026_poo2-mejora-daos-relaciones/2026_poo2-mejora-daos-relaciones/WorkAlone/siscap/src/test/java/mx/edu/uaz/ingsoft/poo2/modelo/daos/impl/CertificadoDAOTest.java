package mx.edu.uaz.ingsoft.poo2.modelo.daos.impl;

import jakarta.persistence.EntityManager;
import mx.edu.uaz.ingsoft.poo2.modelo.daos.DAOException;
import mx.edu.uaz.ingsoft.poo2.modelo.entidades.Curso;
import mx.edu.uaz.ingsoft.poo2.modelo.entidades.Inscripcion;
import mx.edu.uaz.ingsoft.poo2.modelo.entidades.ResultadoInscripcion;
import mx.edu.uaz.ingsoft.poo2.modelo.utils.JPAUtil;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * Pruebas de {@link CertificadoDAO.findByInscripcion}: relación 1:1, así que una
 * inscripción aprobada devuelve su certificado y una sin certificado devuelve
 * {@code null}.
 */
public class CertificadoDAOTest {

    private EntityManager em;
    private CertificadoDAO certificadoDAO;
    private InscripcionDAO inscripcionDAO;
    private CursoDAO cursoDAO;

    @BeforeEach
    public void preparar() throws DAOException {
        CapacitacionBaseDePruebas.sembrar();
        em = JPAUtil.crearEntityManager();
        certificadoDAO = new CertificadoDAO(em);
        inscripcionDAO = new InscripcionDAO(em);
        cursoDAO = new CursoDAO(em);
    }

    @AfterEach
    public void cerrar() {
        em.close();
    }

    @Test
    @DisplayName("Una inscripción aprobada con certificado lo devuelve")
    public void certificadoExistente() {
        Curso javaBasico = cursoDAO.findByCampo("nombre", "Java Básico").get(0);
        Inscripcion aprobada = inscripcionDAO.findAprobadasDeCurso(javaBasico).get(0);

        var certificado = certificadoDAO.findByInscripcion(aprobada);

        assertNotNull(certificado);
        assertEquals("CERT-0001", certificado.getFolio());
    }

    @Test
    @DisplayName("Una inscripción sin certificado devuelve null")
    public void certificadoInexistente() {
        Curso javaBasico = cursoDAO.findByCampo("nombre", "Java Básico").get(0);
        Inscripcion pendiente = inscripcionDAO.findByCurso(javaBasico).stream()
                .filter(inscripcion -> inscripcion.getResultado() != ResultadoInscripcion.APROBADO)
                .findFirst()
                .orElseThrow();

        assertNull(certificadoDAO.findByInscripcion(pendiente));
    }
}