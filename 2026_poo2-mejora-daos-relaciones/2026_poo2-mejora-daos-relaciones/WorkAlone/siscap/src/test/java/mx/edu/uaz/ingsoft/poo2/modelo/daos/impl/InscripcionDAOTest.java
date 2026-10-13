package mx.edu.uaz.ingsoft.poo2.modelo.daos.impl;

import jakarta.persistence.EntityManager;
import mx.edu.uaz.ingsoft.poo2.modelo.daos.DAOException;
import mx.edu.uaz.ingsoft.poo2.modelo.entidades.Curso;
import mx.edu.uaz.ingsoft.poo2.modelo.entidades.Inscripcion;
import mx.edu.uaz.ingsoft.poo2.modelo.entidades.Persona;
import mx.edu.uaz.ingsoft.poo2.modelo.entidades.ResultadoInscripcion;
import mx.edu.uaz.ingsoft.poo2.modelo.utils.JPAUtil;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Pruebas de {@link InscripcionDAO}: consultas por curso y por persona (N:1) y
 * la consulta con Criteria que filtra por curso y resultado APROBADO.
 */
public class InscripcionDAOTest {

    private EntityManager em;
    private InscripcionDAO inscripcionDAO;
    private CursoDAO cursoDAO;

    @BeforeEach
    public void preparar() throws DAOException {
        CapacitacionBaseDePruebas.sembrar();
        em = JPAUtil.crearEntityManager();
        inscripcionDAO = new InscripcionDAO(em);
        cursoDAO = new CursoDAO(em);
    }

    @AfterEach
    public void cerrar() {
        em.close();
    }

    @Test
    @DisplayName("findByCurso devuelve las inscripciones de ese curso")
    public void buscarPorCurso() {
        Curso javaBasico = cursoDAO.findByCampo("nombre", "Java Básico").get(0);

        var inscripciones = inscripcionDAO.findByCurso(javaBasico);

        assertFalse(inscripciones.isEmpty());
        assertTrue(inscripciones.stream()
                .allMatch(inscripcion -> inscripcion.getCurso().getId().equals(javaBasico.getId())));
    }

    @Test
    @DisplayName("findByPersona devuelve las inscripciones de esa persona")
    public void buscarPorPersona() {
        Curso javaBasico = cursoDAO.findByCampo("nombre", "Java Básico").get(0);
        var persona = inscripcionDAO.findByCurso(javaBasico).get(0).getPersona();

        var inscripciones = inscripcionDAO.findByPersona(persona);

        assertFalse(inscripciones.isEmpty());
        assertTrue(inscripciones.stream()
                .allMatch(inscripcion -> inscripcion.getPersona().getId().equals(persona.getId())));
    }

    @Test
    @DisplayName("findAprobadasDeCurso solo devuelve inscripciones con resultado APROBADO")
    public void buscarAprobadasDeCurso() {
        Curso javaBasico = cursoDAO.findByCampo("nombre", "Java Básico").get(0);

        var aprobadas = inscripcionDAO.findAprobadasDeCurso(javaBasico);

        assertFalse(aprobadas.isEmpty());
        assertTrue(aprobadas.stream()
                .allMatch(inscripcion -> inscripcion.getResultado() == mx.edu.uaz.ingsoft.poo2.modelo.entidades.ResultadoInscripcion.APROBADO));
    }
}