package mx.edu.uaz.ingsoft.poo2.modelo.utils;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

/**
 * Punto único de acceso a JPA. Construye (una sola vez) la
 * {@link EntityManagerFactory} de la unidad de persistencia {@value #UNIDAD_PERSISTENCIA}
 * y entrega {@link EntityManager} nuevos a quien los pida.
 */
public final class JPAUtil {
    // Este nombre de unidad de persistencia es usado en persistence.xml
    public static final String UNIDAD_PERSISTENCIA = "siscapPU";

    private static final EntityManagerFactory FABRICA;

    static {
        // InicializadorEsquema.crearEsquema();
        FABRICA = Persistence.createEntityManagerFactory(UNIDAD_PERSISTENCIA);
    }

    private JPAUtil() {
    }

    public static EntityManager crearEntityManager() {
        return FABRICA.createEntityManager();
    }

    public static void cerrar() {
        if (FABRICA.isOpen()) {
            FABRICA.close();
        }
    }
}
