package mx.edu.uaz.ingsoft.poo2.utils.inicio_bd;

import jakarta.persistence.EntityManager;
import mx.edu.uaz.ingsoft.poo2.modelo.daos.impl.SistemaOperativoDAO;
import mx.edu.uaz.ingsoft.poo2.modelo.entidades.SistemaOperativo;
import mx.edu.uaz.ingsoft.poo2.modelo.utils.JPAUtil;

import static mx.edu.uaz.ingsoft.poo2.utils.Constantes.ACTIVO;
import static mx.edu.uaz.ingsoft.poo2.utils.Constantes.MSG_INGESTA_SISTEMAS_OPERATIVOS_COMPLETADA;

public class IngestaSistemasOperativos {
    private static final EntityManager EM = JPAUtil.crearEntityManager();
    public static void main(String[] args) {
        ejecutarIngesta();
        System.out.println(MSG_INGESTA_SISTEMAS_OPERATIVOS_COMPLETADA);
    }

    public static void ejecutarIngesta() {
        SistemaOperativoDAO sistemaOperativoDAO = new SistemaOperativoDAO(EM);
        //sistemaOperativoDAO.conectar();
        sistemaOperativoDAO.deleteAll(sistemaOperativoDAO.findAll());

        sistemaOperativoDAO.save(new SistemaOperativo(null, ACTIVO, "Windows 11", "Windows", "Microsoft", "Sistema operativo de escritorio"));
        sistemaOperativoDAO.save(new SistemaOperativo(null, ACTIVO, "Ubuntu", "Linux", "Canonical", "Distribución Linux popular para desarrollo"));
        sistemaOperativoDAO.save(new SistemaOperativo(null, ACTIVO, "macOS", "Unix", "Apple", "Sistema operativo para computadoras Mac"));
        sistemaOperativoDAO.save(new SistemaOperativo(null, ACTIVO, "Debian", "Linux", "Comunidad", "Distribución Linux estable"));
        sistemaOperativoDAO.save(new SistemaOperativo(null, ACTIVO, "Android", "Linux", "Google", "Sistema operativo móvil basado en Linux"));
        //sistemaOperativoDAO.desconectar();
    }
}