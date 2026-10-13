package mx.edu.uaz.ingsoft.poo2.modelo.daos.impl;

import jakarta.persistence.EntityManager;
import mx.edu.uaz.ingsoft.poo2.modelo.daos.DAOException;
import mx.edu.uaz.ingsoft.poo2.modelo.utils.JPAUtil;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import mx.edu.uaz.ingsoft.poo2.modelo.entidades.Persona;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Pruebas de {@code findAll(pagina, tamano)} contra la siembra canónica de
 * 5 personas: páginas dentro del rango, página más allá del final,
 * listado completo sin argumentos y rechazo de argumentos inválidos.
 */
public class PaginacionDAOTest {

    private static final int TOTAL_SIEMBRA = 5;

    private EntityManager em;
    private PersonaDAO dao;

    @BeforeEach
    public void preparar() throws DAOException {
        BaseDePruebas.sembrarPersonas();
        em = JPAUtil.crearEntityManager();
        dao = new PersonaDAO(em);
    }

    @AfterEach
    public void restaurar() throws DAOException {
        try {
            BaseDePruebas.sembrarPersonas();
        } finally {
            em.close();
        }
    }

    @Test
    @DisplayName("Página dentro del rango: devuelve exactamente el tamaño pedido (la última, lo que resta)")
    public void paginaDentroDelRangoDevuelveElTamanoPedido() {
        assertEquals(2, dao.findAll(0, 2).size());
        assertEquals(2, dao.findAll(1, 2).size());
        assertEquals(1, dao.findAll(2, 2).size(), "La última página queda con el resto");
    }

    @Test
    @DisplayName("Página más allá del final: devuelve lista vacía, sin error")
    public void paginaMasAllaDelFinalDevuelveVacia() {
        List<Persona> lejos = dao.findAll(10, 2);
        assertTrue(lejos.isEmpty());
    }

    @Test
    @DisplayName("findAll sin argumentos devuelve el listado completo")
    public void listadoSinArgumentosDevuelveTodo() {
        assertEquals(TOTAL_SIEMBRA, dao.findAll().size());
    }

    @Test
    @DisplayName("Argumentos inválidos (página negativa o tamaño no positivo) lanzan IllegalArgumentException")
    public void argumentosInvalidosLanzanIllegalArgument() {
        assertThrows(IllegalArgumentException.class, () -> dao.findAll(-1, 2));
        assertThrows(IllegalArgumentException.class, () -> dao.findAll(0, 0));
        assertThrows(IllegalArgumentException.class, () -> dao.findAll(0, -3));
    }
}
