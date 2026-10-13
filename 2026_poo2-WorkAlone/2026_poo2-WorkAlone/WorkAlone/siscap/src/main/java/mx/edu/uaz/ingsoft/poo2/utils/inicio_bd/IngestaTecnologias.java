package mx.edu.uaz.ingsoft.poo2.utils.inicio_bd;

import jakarta.persistence.EntityManager;
import mx.edu.uaz.ingsoft.poo2.modelo.daos.impl.TecnologiaDAO;
import mx.edu.uaz.ingsoft.poo2.modelo.entidades.Tecnologia;
import mx.edu.uaz.ingsoft.poo2.modelo.utils.JPAUtil;

import static mx.edu.uaz.ingsoft.poo2.utils.Constantes.ACTIVO;
import static mx.edu.uaz.ingsoft.poo2.utils.Constantes.MSG_INGESTA_TECNOLOGIAS_COMPLETADA;

public class IngestaTecnologias {
    private static final EntityManager EM = JPAUtil.crearEntityManager();
    public static void main(String[] args) {

        ejecutarIngesta();
        System.out.println(MSG_INGESTA_TECNOLOGIAS_COMPLETADA);
    }

    public static void ejecutarIngesta() {
        TecnologiaDAO tecnologiaDAO = new TecnologiaDAO(EM);
        //tecnologiaDAO.conectar();
        tecnologiaDAO.deleteAll(tecnologiaDAO.findAll());

        tecnologiaDAO.save(new Tecnologia(null, ACTIVO, "Git", "Control de versiones", "Comunidad", "Sistema de control de versiones distribuido"));
        tecnologiaDAO.save(new Tecnologia(null, ACTIVO, "Docker", "Contenedores", "Docker Inc.", "Plataforma para crear y ejecutar contenedores"));
        tecnologiaDAO.save(new Tecnologia(null, ACTIVO, "Linux", "Sistema operativo", "Comunidad", "Familia de sistemas operativos tipo Unix"));
        tecnologiaDAO.save(new Tecnologia(null, ACTIVO, "Excel", "Ofimática", "Microsoft", "Hoja de cálculo para análisis y reportes"));
        tecnologiaDAO.save(new Tecnologia(null, ACTIVO, "REST", "Arquitectura", "Comunidad", "Estilo arquitectónico para servicios web"));
        //tecnologiaDAO.desconectar();
    }
}