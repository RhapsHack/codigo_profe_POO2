package mx.edu.uaz.ingsoft.poo2.modelo.daos.impl;

import jakarta.persistence.EntityManager;
import mx.edu.uaz.ingsoft.poo2.modelo.daos.DAOException;
import mx.edu.uaz.ingsoft.poo2.modelo.entidades.Persona;
import mx.edu.uaz.ingsoft.poo2.modelo.utils.JPAUtil;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static mx.edu.uaz.ingsoft.poo2.utils.Constantes.ACTIVO;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Pruebas de búsqueda por campo con Criteria: varias coincidencias,
 * ninguna coincidencia (lista vacía) y la variante de un solo resultado.
 *
 * <p>Los nombres de campo son atributos de la entidad (por ejemplo
 * {@code correoElectronico}), no nombres de columna de la base de datos.
 */
public class BusquedaDAOTest {

    private EntityManager em;
    private PersonaDAO dao;

    @BeforeEach
    public void preparar() throws DAOException {
        BaseDePruebas.sembrarPersonas();
        em = JPAUtil.crearEntityManager();
        dao = new PersonaDAO(em);
        // Tres personas más con el mismo nombre, para forzar varias coincidencias
        for (int i = 1; i <= 3; i++) {
            dao.save(new Persona(null, ACTIVO, "Coincidencia", "Prueba", "Busqueda",
                    "EMP91" + i, "coincidencia" + i + "@empresa.com"));
        }
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
    @DisplayName("findByCampo con varias coincidencias devuelve todas las filas")
    public void variasCoincidenciasDevuelveTodas() {
        List<Persona> encontradas = dao.findByCampo("nombre", "Coincidencia");
        assertEquals(3, encontradas.size());
    }

    @Test
    @DisplayName("findByCampo sin coincidencias devuelve lista vacía")
    public void ningunaCoincidenciaDevuelveVacia() {
        assertTrue(dao.findByCampo("nombre", "NoExiste").isEmpty());
    }

    @Test
    @DisplayName("findByCampoUnico devuelve la única persona con ese correo")
    public void varianteUnicoDevuelveElRegistro() {
        Persona ana = dao.findByCampoUnico("correoElectronico", "ana.martinez@empresa.com");
        assertNotNull(ana);
        assertEquals("Ana", ana.getNombre());
    }

    @Test
    @DisplayName("findByCampoUnico sin coincidencias devuelve null")
    public void unicoSinCoincidenciaDevuelveNull() {
        assertNull(dao.findByCampoUnico("correoElectronico", "no.existe@empresa.com"));
    }
}
