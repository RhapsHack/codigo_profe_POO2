package mx.edu.uaz.ingsoft.poo2.utils.inicio_bd;

import jakarta.persistence.EntityManager;
import mx.edu.uaz.ingsoft.poo2.modelo.daos.impl.ProgLanguagesDAO;
import mx.edu.uaz.ingsoft.poo2.modelo.entidades.ProgLanguages;
import mx.edu.uaz.ingsoft.poo2.modelo.utils.JPAUtil;

import static mx.edu.uaz.ingsoft.poo2.utils.Constantes.ACTIVO;
import static mx.edu.uaz.ingsoft.poo2.utils.Constantes.MSG_INGESTA_LENGUAJES_COMPLETADA;

public class IngestaLenguajesProgramacion {
    //En este programa se ingesta la información de los lenguajes de programación a la base de datos, si es que no existen previamente.
    // con los lenguajes de programación más populares, como Java, Python, C++, JavaScript, C#, Ruby, PHP, Swift, Kotlin y Go.
    private static final EntityManager EM = JPAUtil.crearEntityManager();
    public static void main(String[] args) {
        ejecutarIngesta();
        System.out.println(MSG_INGESTA_LENGUAJES_COMPLETADA);
    }

    public static void ejecutarIngesta() {
        ProgLanguagesDAO progLanguagesDAO = new ProgLanguagesDAO(EM);
        //progLanguagesDAO.conectar();
        progLanguagesDAO.deleteAll(progLanguagesDAO.findAll());

        progLanguagesDAO.save(new ProgLanguages(null, ACTIVO, "Java", "Orientado a objetos", "21",
                "Lenguaje ampliamente usado en aplicaciones empresariales"));
        progLanguagesDAO.save(new ProgLanguages(null, ACTIVO, "Python", "Multiparadigma", "3.12",
                "Lenguaje popular en automatización, ciencia de datos y desarrollo web"));
        progLanguagesDAO.save(new ProgLanguages(null, ACTIVO, "C++", "Multiparadigma", "C++20",
                "Lenguaje de alto rendimiento usado en sistemas y aplicaciones especializadas"));
        progLanguagesDAO.save(new ProgLanguages(null, ACTIVO, "JavaScript", "Multiparadigma", "ES2023",
                "Lenguaje base para desarrollo web del lado del cliente y servidor"));
        progLanguagesDAO.save(new ProgLanguages(null, ACTIVO, "C#", "Orientado a objetos", ".NET 8",
                "Lenguaje principal del ecosistema .NET"));
        progLanguagesDAO.save(new ProgLanguages(null, ACTIVO, "Ruby", "Orientado a objetos", "3.3",
                "Lenguaje conocido por su simplicidad y uso en aplicaciones web"));
        progLanguagesDAO.save(new ProgLanguages(null, ACTIVO, "PHP", "Imperativo", "8.3",
                "Lenguaje ampliamente usado para desarrollo web"));
        progLanguagesDAO.save(new ProgLanguages(null, ACTIVO, "Swift", "Multiparadigma", "5.10",
                "Lenguaje usado principalmente para desarrollo en ecosistema Apple"));
        progLanguagesDAO.save(new ProgLanguages(null, ACTIVO, "Kotlin", "Multiparadigma", "2.0",
                "Lenguaje moderno usado en Android y backend"));
        progLanguagesDAO.save(new ProgLanguages(null, ACTIVO, "Go", "Concurrente", "1.23",
                "Lenguaje enfocado en simplicidad, concurrencia y servicios backend"));
        //progLanguagesDAO.desconectar();
    }
}