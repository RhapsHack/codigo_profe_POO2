package mx.edu.uaz.ingsoft.poo2.modelo.daos.impl;

import jakarta.persistence.EntityManager;
import mx.edu.uaz.ingsoft.poo2.modelo.daos.DAOException;
import mx.edu.uaz.ingsoft.poo2.modelo.entidades.Curso;
import mx.edu.uaz.ingsoft.poo2.modelo.entidades.Persona;
import mx.edu.uaz.ingsoft.poo2.modelo.entidades.Tema;
import mx.edu.uaz.ingsoft.poo2.modelo.utils.JPAUtil;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Pruebas de las consultas propias de {@link CursoDAO}: filtrar por instructor
 * y por tema (N:1 con {@code findByCampo}) y traer el curso con sus
 * inscripciones en una sola consulta (JOIN FETCH).
 */
public class CursoDAOTest {

    private EntityManager em;
    private CursoDAO cursoDAO;

    @BeforeEach
    public void preparar() throws DAOException {
        CapacitacionBaseDePruebas.sembrar();
        em = JPAUtil.crearEntityManager();
        cursoDAO = new CursoDAO(em);
    }

    @AfterEach
    public void cerrar() {
        em.close();
    }

    @Test
    @DisplayName("findByInstructor devuelve solo los cursos del instructor dado")
    public void buscarPorInstructor() {
        Curso referencia = cursoDAO.findAll().get(0);
        Persona instructor = referencia.getInstructor();

        List<Curso> cursos = cursoDAO.findByInstructor(instructor);

        assertFalse(cursos.isEmpty());
        assertTrue(cursos.stream().allMatch(c -> instructor.getId().equals(c.getInstructor().getId())));
    }

    @Test
    @DisplayName("findByTema devuelve solo los cursos del tema dado")
    public void buscarPorTema() {
        Curso referencia = cursoDAO.findAll().get(0);
        Tema tema = referencia.getTema();

        List<Curso> cursos = cursoDAO.findByTema(tema);

        assertFalse(cursos.isEmpty());
        assertTrue(cursos.stream().allMatch(c -> tema.getId().equals(c.getTema().getId())));
    }

    @Test
    @DisplayName("findConInscripciones puebla la colección LAZY y la marca como cargada")
    public void buscarConInscripciones() {
        Curso javaBasico = cursoDAO.findByCampo("nombre", "Java Básico").get(0);

        Curso cargado = cursoDAO.findConInscripciones(javaBasico);

        assertNotNull(cargado);
        assertTrue(cargado.isInscripcionesCargadas(), "El indicador debe quedar en true");
        assertFalse(cargado.getInscripciones().isEmpty(), "La colección debe venir poblada");
    }
}