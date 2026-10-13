package mx.edu.uaz.ingsoft.poo2.utils.inicio_bd;

import jakarta.persistence.EntityManager;
import mx.edu.uaz.ingsoft.poo2.modelo.daos.DAOException;
import mx.edu.uaz.ingsoft.poo2.modelo.daos.impl.PersonaDAO;
import mx.edu.uaz.ingsoft.poo2.modelo.entidades.Persona;
import mx.edu.uaz.ingsoft.poo2.modelo.utils.JPAUtil;

import static mx.edu.uaz.ingsoft.poo2.utils.Constantes.ACTIVO;
import static mx.edu.uaz.ingsoft.poo2.utils.Constantes.MSG_INGESTA_PERSONAS_COMPLETADA;

public class IngestaPersonas {
    private static final EntityManager EM = JPAUtil.crearEntityManager();
    public static void main(String[] args) throws DAOException {
        ejecutarIngesta();
        System.out.println(MSG_INGESTA_PERSONAS_COMPLETADA);
    }

    public static void ejecutarIngesta() throws DAOException {
        PersonaDAO personaDAO = new PersonaDAO(EM);
        //personaDAO.conectar();
        personaDAO.deleteAll(personaDAO.findAll());

        personaDAO.save(new Persona(null, ACTIVO, "Ana", "Martinez", "Lopez", "EMP001", "ana.martinez@empresa.com"));
        personaDAO.save(new Persona(null, ACTIVO, "Luis", "Hernandez", "Garcia", "EMP002", "luis.hernandez@empresa.com"));
        personaDAO.save(new Persona(null, ACTIVO, "Carla", "Ramirez", "Torres", "EMP003", "carla.ramirez@empresa.com"));
        personaDAO.save(new Persona(null, ACTIVO, "Jorge", "Sanchez", "Ruiz", "EMP004", "jorge.sanchez@empresa.com"));
        personaDAO.save(new Persona(null, ACTIVO, "Miriam", "Flores", "Diaz", "EMP005", "miriam.flores@empresa.com"));
        //personaDAO.desconectar();
    }
}