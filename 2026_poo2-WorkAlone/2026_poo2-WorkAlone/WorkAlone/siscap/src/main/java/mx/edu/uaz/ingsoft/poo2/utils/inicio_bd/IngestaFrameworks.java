package mx.edu.uaz.ingsoft.poo2.utils.inicio_bd;

import jakarta.persistence.EntityManager;
import mx.edu.uaz.ingsoft.poo2.modelo.daos.impl.FrameworkDAO;
import mx.edu.uaz.ingsoft.poo2.modelo.entidades.Framework;
import mx.edu.uaz.ingsoft.poo2.modelo.utils.JPAUtil;

import static mx.edu.uaz.ingsoft.poo2.utils.Constantes.ACTIVO;
import static mx.edu.uaz.ingsoft.poo2.utils.Constantes.MSG_INGESTA_FRAMEWORKS_COMPLETADA;

public class IngestaFrameworks {

    private static final EntityManager EM = JPAUtil.crearEntityManager();
    public static void main(String[] args) {
        ejecutarIngesta();
        System.out.println(MSG_INGESTA_FRAMEWORKS_COMPLETADA);
    }

    public static void ejecutarIngesta() {
        FrameworkDAO frameworkDAO = new FrameworkDAO(EM);
        //frameworkDAO.conectar();
        frameworkDAO.deleteAll(frameworkDAO.findAll());

        frameworkDAO.save(new Framework(null, ACTIVO, "Spring Boot", "Backend", "Java", "Framework para aplicaciones Java empresariales"));
        frameworkDAO.save(new Framework(null, ACTIVO, "React", "Frontend", "JavaScript", "Biblioteca para interfaces de usuario"));
        frameworkDAO.save(new Framework(null, ACTIVO, "Angular", "Frontend", "TypeScript", "Framework para aplicaciones web"));
        frameworkDAO.save(new Framework(null, ACTIVO, "Laravel", "Backend", "PHP", "Framework para aplicaciones web en PHP"));
        frameworkDAO.save(new Framework(null, ACTIVO, "Django", "Backend", "Python", "Framework web de alto nivel"));
        frameworkDAO.save(new Framework(null, ACTIVO, "Flask", "Backend", "Python", "Framework ligero para aplicaciones web en Python"));
        //frameworkDAO.desconectar();
    }
}