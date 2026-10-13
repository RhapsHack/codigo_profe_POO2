package mx.edu.uaz.ingsoft.poo2.modelo.daos.impl;

import jakarta.persistence.EntityManager;
import mx.edu.uaz.ingsoft.poo2.modelo.daos.DAOException;
import mx.edu.uaz.ingsoft.poo2.modelo.utils.JPAUtil;
import mx.edu.uaz.ingsoft.poo2.utils.inicio_bd.IngestaCapacitaciones;

/**
 * Punto de partida común para las pruebas del dominio de capacitaciones: deja
 * las tablas {@code personas}, {@code temas}, {@code cursos},
 * {@code inscripciones} y {@code certificados} en un estado conocido
 * (idempotente), de modo que cada prueba es independiente del orden.
 */
public final class CapacitacionBaseDePruebas {

    private CapacitacionBaseDePruebas() {
    }

    /**
     * Vuelve a sembrar todo el dominio de capacitaciones (incluidas las
     * personas) usando la ingesta, que borra en orden inverso de dependencias.
     */
    public static void sembrar() throws DAOException {
        IngestaCapacitaciones.ejecutarIngesta();
    }

    /**
     * Elimina las filas de las cuatro tablas del dominio que dependen de
     * {@code personas}, para poder reiniciar las personas sin violar las claves
     * foráneas. Se usa desde las pruebas de solo personas.
     */
    public static void limpiarCapacitaciones() throws DAOException {
        EntityManager em = JPAUtil.crearEntityManager();
        try {
            var certificadoDAO = new CertificadoDAO(em);
            var inscripcionDAO = new InscripcionDAO(em);
            var cursoDAO = new CursoDAO(em);
            var temaDAO = new TemaDAO(em);

            certificadoDAO.deleteAll(certificadoDAO.findAll());
            inscripcionDAO.deleteAll(inscripcionDAO.findAll());
            cursoDAO.deleteAll(cursoDAO.findAll());
            temaDAO.deleteAll(temaDAO.findAll());
        } finally {
            em.close();
        }
    }
}