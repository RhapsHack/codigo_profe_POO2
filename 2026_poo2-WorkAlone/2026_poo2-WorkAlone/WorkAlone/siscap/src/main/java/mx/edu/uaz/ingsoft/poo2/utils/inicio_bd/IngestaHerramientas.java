package mx.edu.uaz.ingsoft.poo2.utils.inicio_bd;

import jakarta.persistence.EntityManager;
import mx.edu.uaz.ingsoft.poo2.modelo.daos.impl.HerramientaDAO;
import mx.edu.uaz.ingsoft.poo2.modelo.entidades.Herramienta;
import mx.edu.uaz.ingsoft.poo2.modelo.utils.JPAUtil;

import static mx.edu.uaz.ingsoft.poo2.utils.Constantes.ACTIVO;
import static mx.edu.uaz.ingsoft.poo2.utils.Constantes.MSG_INGESTA_HERRAMIENTAS_COMPLETADA;

public class IngestaHerramientas {
    private static final EntityManager EM = JPAUtil.crearEntityManager();
    public static void main(String[] args) {
        ejecutarIngesta();
        System.out.println(MSG_INGESTA_HERRAMIENTAS_COMPLETADA);
    }

    public static void ejecutarIngesta() {
        HerramientaDAO herramientaDAO = new HerramientaDAO(EM);
        //herramientaDAO.conectar();
        herramientaDAO.deleteAll(herramientaDAO.findAll());

        herramientaDAO.save(new Herramienta(null, ACTIVO, "IntelliJ IDEA", "IDE", "JetBrains", "Entorno de desarrollo integrado para Java y otros lenguajes"));
        herramientaDAO.save(new Herramienta(null, ACTIVO, "Visual Studio Code", "Editor", "Microsoft", "Editor de código extensible"));
        herramientaDAO.save(new Herramienta(null, ACTIVO, "Postman", "API", "Postman", "Herramienta para probar APIs"));
        herramientaDAO.save(new Herramienta(null, ACTIVO, "Maven", "Build", "Apache", "Herramienta de construcción y gestión de dependencias"));
        herramientaDAO.save(new Herramienta(null, ACTIVO, "Jira", "Gestión", "Atlassian", "Herramienta para seguimiento de tareas"));
        //herramientaDAO.desconectar();
    }
}