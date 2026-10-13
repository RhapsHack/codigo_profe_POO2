package mx.edu.uaz.ingsoft.poo2.modelo.daos.impl;

import jakarta.persistence.EntityManager;
import mx.edu.uaz.ingsoft.poo2.modelo.daos.DAOException;
import mx.edu.uaz.ingsoft.poo2.modelo.entidades.Tema;
import mx.edu.uaz.ingsoft.poo2.modelo.entidades.TipoTema;
import mx.edu.uaz.ingsoft.poo2.modelo.utils.JPAUtil;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static mx.edu.uaz.ingsoft.poo2.utils.Constantes.ACTIVO;
import static mx.edu.uaz.ingsoft.poo2.utils.Constantes.MSG_ERROR_PERSISTENCIA;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Pruebas de {@link TemaDAO}: alta respetando la unicidad del nombre, consulta
 * por campo sobre {@code findByCampo} y listado de los temas sembrados.
 */
public class TemaDAOTest {

    private EntityManager em;
    private TemaDAO temaDAO;

    @BeforeEach
    public void preparar() throws DAOException {
        CapacitacionBaseDePruebas.sembrar();
        em = JPAUtil.crearEntityManager();
        temaDAO = new TemaDAO(em);
    }

    @AfterEach
    public void cerrar() {
        em.close();
    }

    @Test
    @DisplayName("findAll devuelve los temas sembrados")
    public void listarTemas() throws DAOException {
        List<Tema> temas = temaDAO.findAll();

        assertFalse(temas.isEmpty());
        assertTrue(temas.stream().allMatch(tema -> tema.getNombre() != null));
    }

    @Test
    @DisplayName("findByCampo por nombre encuentra un tema con su tipo")
    public void buscarPorNombre() {
        List<Tema> temas = temaDAO.findByCampo("nombre", "Fundamentos de Java");

        assertEquals(1, temas.size());
        assertEquals(TipoTema.LENGUAJE, temas.get(0).getTipo());
    }

    @Test
    @DisplayName("Alta exitosa: el tema queda persistido y consultable")
    public void altaExitosa() throws DAOException {
        Tema guardado = temaDAO.save(
                new Tema(null, ACTIVO, "Tema de Prueba", TipoTema.HABILIDAD_BLANDA));

        assertNotNull(guardado.getId(), "La escritura exitosa asigna el id");
        assertNotNull(temaDAO.findById(guardado.getId()));
    }

    @Test
    @DisplayName("Nombre duplicado: DAOException por restricción de unicidad")
    public void nombreDuplicado() throws DAOException {
        Tema repetido = new Tema(null, ACTIVO, "Fundamentos de Java", TipoTema.LENGUAJE);

        DAOException error = assertThrows(DAOException.class, () -> temaDAO.save(repetido));

        assertTrue(error.getMessage().startsWith(MSG_ERROR_PERSISTENCIA),
                "El mensaje debe empezar con el texto base: " + error.getMessage());
    }
}