package mx.edu.uaz.ingsoft.poo2.modelo.daos.impl;

import mx.edu.uaz.ingsoft.poo2.modelo.daos.DAOException;
import mx.edu.uaz.ingsoft.poo2.utils.inicio_bd.IngestaPersonas;

/**
 * Utilidad para las pruebas de la capa DAO (referencia: la
 * {@code BaseDePruebas} del historial git en {@code 51e6c90^}).
 *
 * <p>Cada prueba empieza y termina con la tabla {@code personas} en un
 * estado conocido: la siembra borra todo y vuelve a insertar las 5 personas
 * fijas, de modo que las pruebas son independientes entre sí y del orden en
 * que Surefire las ejecute.
 */
public final class BaseDePruebas {

    private BaseDePruebas() {
    }

    /**
     * Deja la tabla {@code personas} vacía y con la siembra canónica de
     * {@link IngestaPersonas} (idempotente: puede llamarse antes y después
     * de cada prueba).
     */
    public static void sembrarPersonas() throws DAOException {
        CapacitacionBaseDePruebas.limpiarCapacitaciones();
        mx.edu.uaz.ingsoft.poo2.utils.inicio_bd.IngestaPersonas.ejecutarIngesta();
    }
}
