package mx.edu.uaz.ingsoft.poo2.utils.inicio_bd;

import jakarta.persistence.EntityManager;
import mx.edu.uaz.ingsoft.poo2.modelo.daos.impl.HabilidadBlandaDAO;
import mx.edu.uaz.ingsoft.poo2.modelo.entidades.HabilidadBlanda;
import mx.edu.uaz.ingsoft.poo2.modelo.utils.JPAUtil;

import static mx.edu.uaz.ingsoft.poo2.utils.Constantes.ACTIVO;
import static mx.edu.uaz.ingsoft.poo2.utils.Constantes.MSG_INGESTA_HABILIDADES_BLANDAS_COMPLETADA;

public class IngestaHabilidadesBlandas {
    private static final EntityManager EM = JPAUtil.crearEntityManager();
    public static void main(String[] args) {
        ejecutarIngesta();
        System.out.println(MSG_INGESTA_HABILIDADES_BLANDAS_COMPLETADA);
    }

    public static void ejecutarIngesta() {
        HabilidadBlandaDAO habilidadBlandaDAO = new HabilidadBlandaDAO(EM);
       // habilidadBlandaDAO.conectar();
        habilidadBlandaDAO.deleteAll(habilidadBlandaDAO.findAll());

        habilidadBlandaDAO.save(new HabilidadBlanda(null, ACTIVO, "Comunicación", "Interpersonal", "Intermedio", "Capacidad para expresar ideas con claridad"));
        habilidadBlandaDAO.save(new HabilidadBlanda(null, ACTIVO, "Trabajo en equipo", "Colaboración", "Intermedio", "Capacidad para colaborar con otras personas"));
        habilidadBlandaDAO.save(new HabilidadBlanda(null, ACTIVO, "Resolución de problemas", "Pensamiento", "Avanzado", "Capacidad para analizar y resolver situaciones"));
        habilidadBlandaDAO.save(new HabilidadBlanda(null, ACTIVO, "Adaptabilidad", "Actitud", "Intermedio", "Capacidad para ajustarse a cambios"));
        habilidadBlandaDAO.save(new HabilidadBlanda(null, ACTIVO, "Liderazgo", "Gestión", "Básico", "Capacidad para guiar equipos de trabajo"));
        //habilidadBlandaDAO.desconectar();
    }
}