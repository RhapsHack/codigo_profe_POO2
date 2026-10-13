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
import static mx.edu.uaz.ingsoft.poo2.utils.Constantes.MSG_ERROR_PERSISTENCIA;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Pruebas de error y de éxito de la escritura en la capa DAO:
 * una escritura que viola una restricción de la base de datos debe
 * levantar {@link DAOException} (con mensaje y causa) y revertir la
 * transacción completa; una escritura válida no debe lanzar nada.
 *
 * <p>La provocación del fallo es intencional y didáctica: la tabla
 * {@code personas} no trae restricciones en su DDL, así que cada prueba
 * crea un índice único temporal sobre {@code correo_electronico} y lo
 * elimina al terminar. Insertar dos correos repetidos en el mismo lote es
 * entonces un error de SQLite que el DAO debe traducir a
 * {@code DAOException}.
 */
public class PersonaDAOTest {

    private static final String CREAR_INDICE_UNICO =
            "CREATE UNIQUE INDEX IF NOT EXISTS idx_personas_correo_unico ON personas(correo_electronico)";
    private static final String ELIMINAR_INDICE_UNICO =
            "DROP INDEX IF EXISTS idx_personas_correo_unico";

    private EntityManager em;
    private PersonaDAO dao;

    @BeforeEach
    public void preparar() throws DAOException {
        BaseDePruebas.sembrarPersonas();
        em = JPAUtil.crearEntityManager();
        dao = new PersonaDAO(em);
        ejecutarDdl(CREAR_INDICE_UNICO);
    }

    @AfterEach
    public void restaurar() throws DAOException {
        try {
            ejecutarDdl(ELIMINAR_INDICE_UNICO);
            BaseDePruebas.sembrarPersonas();
        } finally {
            em.close();
        }
    }

    /** Una consulta nativa (aunque sea DDL) exige transacción activa en JPA. */
    private void ejecutarDdl(String sql) {
        em.getTransaction().begin();
        try {
            em.createNativeQuery(sql).executeUpdate();
            em.getTransaction().commit();
        } catch (RuntimeException e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        }
    }

    @Test
    @DisplayName("Escritura fallida: DAOException con mensaje y causa, y nada del lote queda persistido")
    public void escrituraFallidaLanzaDAOExceptionYNoPersiste() throws DAOException {
        int personasAntes = dao.findAll().size();

        Persona nueva = new Persona(null, ACTIVO, "Nuevo", "Registro", "Prueba",
                "EMP901", "nuevo@empresa.com");
        // Repite el correo de Ana: viola el índice único creado en preparar()
        Persona repetida = new Persona(null, ACTIVO, "Ana", "Martinez", "Lopez",
                "EMP001", "ana.martinez@empresa.com");

        DAOException error = assertThrows(DAOException.class,
                () -> dao.saveAll(List.of(nueva, repetida)));

        assertTrue(error.getMessage().startsWith(MSG_ERROR_PERSISTENCIA),
                "El mensaje debe empezar con el texto base: " + error.getMessage());
        assertNotNull(error.getCause(), "La causa original (el fallo de SQLite) debe conservarse");
        assertEquals(personasAntes, dao.findAll().size(),
                "El lote completo debe revertirse: ni la fila válida debe quedar");
    }

    @Test
    @DisplayName("Escritura exitosa: no lanza excepción y el registro queda consultable")
    public void escrituraExitosaNoLanzaExcepcion() throws DAOException {
        int personasAntes = dao.findAll().size();

        Persona guardada = dao.save(new Persona(null, ACTIVO, "Prueba", "Siembra", "Exitosa",
                "EMP903", "prueba.exitosa@empresa.com"));

        assertNotNull(guardada.getId(), "La escritura exitosa devuelve la entidad con su id");
        assertEquals(personasAntes + 1, dao.findAll().size());
        assertNotNull(dao.findById(guardada.getId()));
    }
}
