package mx.edu.uaz.ingsoft.poo2.utils.inicio_bd;

import jakarta.persistence.EntityManager;
import mx.edu.uaz.ingsoft.poo2.modelo.daos.impl.BaseDeDatosDAO;
import mx.edu.uaz.ingsoft.poo2.modelo.entidades.BaseDeDatos;
import mx.edu.uaz.ingsoft.poo2.modelo.utils.JPAUtil;

import static mx.edu.uaz.ingsoft.poo2.utils.Constantes.ACTIVO;
import static mx.edu.uaz.ingsoft.poo2.utils.Constantes.MSG_INGESTA_BASES_DE_DATOS_COMPLETADA;

public class IngestaBasesDeDatos {

    private static final EntityManager EM = JPAUtil.crearEntityManager();
    public static void main(String[] args) {
        ejecutarIngesta();
        System.out.println(MSG_INGESTA_BASES_DE_DATOS_COMPLETADA);
    }

    public static void ejecutarIngesta() {
        BaseDeDatosDAO dao = new BaseDeDatosDAO(EM);
        //dao.conectar();
        dao.deleteAll(dao.findAll());
        //dao.borradoFisico(dao.obtenerTodas());

        dao.save(new BaseDeDatos(null, ACTIVO, "SQLite", "Relacional", "SQLite", "Motor de base de datos embebido y ligero"));
        dao.save(new BaseDeDatos(null, ACTIVO, "PostgreSQL", "Relacional", "PostgreSQL Global Development Group", "Base de datos relacional avanzada y de código abierto"));
        dao.save(new BaseDeDatos(null, ACTIVO, "MySQL", "Relacional", "Oracle", "Base de datos relacional ampliamente usada en aplicaciones web"));
        dao.save(new BaseDeDatos(null, ACTIVO, "MariaDB", "Relacional", "MariaDB Foundation", "Alternativa abierta derivada de MySQL"));
        dao.save(new BaseDeDatos(null, ACTIVO, "MongoDB", "No relacional", "MongoDB Inc.", "Base de datos orientada a documentos"));
        //dao.desconectar();
    }
}