package mx.edu.uaz.ingsoft.poo2.utils.inicio_bd;

import jakarta.persistence.EntityManager;
import mx.edu.uaz.ingsoft.poo2.modelo.daos.DAOException;
import mx.edu.uaz.ingsoft.poo2.modelo.daos.impl.CertificadoDAO;
import mx.edu.uaz.ingsoft.poo2.modelo.daos.impl.CursoDAO;
import mx.edu.uaz.ingsoft.poo2.modelo.daos.impl.InscripcionDAO;
import mx.edu.uaz.ingsoft.poo2.modelo.daos.impl.PersonaDAO;
import mx.edu.uaz.ingsoft.poo2.modelo.daos.impl.TemaDAO;
import mx.edu.uaz.ingsoft.poo2.modelo.entidades.Certificado;
import mx.edu.uaz.ingsoft.poo2.modelo.entidades.Curso;
import mx.edu.uaz.ingsoft.poo2.modelo.entidades.Inscripcion;
import mx.edu.uaz.ingsoft.poo2.modelo.entidades.Persona;
import mx.edu.uaz.ingsoft.poo2.modelo.entidades.ResultadoInscripcion;
import mx.edu.uaz.ingsoft.poo2.modelo.entidades.Tema;
import mx.edu.uaz.ingsoft.poo2.modelo.entidades.TipoTema;
import mx.edu.uaz.ingsoft.poo2.modelo.utils.JPAUtil;

import java.time.LocalDate;
import java.util.List;

import static mx.edu.uaz.ingsoft.poo2.utils.Constantes.ACTIVO;
import static mx.edu.uaz.ingsoft.poo2.utils.Constantes.MSG_INGESTA_CAPACITACIONES_COMPLETADA;

/**
 * Ingesta idempotente del dominio de capacitaciones: vacía las cuatro tablas
 * en orden inverso de dependencias (certificados, inscripciones, cursos, temas)
 * y vuelve a sembrar datos de ejemplo con claves foráneas válidas.
 *
 * <p>Reutiliza las personas ya sembradas por {@code IngestaPersonas}; si no
 * existen, siembra unas mínimas para que los cursos tengan instructor.
 */
public class IngestaCapacitaciones {

    private static final EntityManager EM = JPAUtil.crearEntityManager();

    public static void main(String[] args) throws DAOException {
        ejecutarIngesta();
        System.out.println(MSG_INGESTA_CAPACITACIONES_COMPLETADA);
    }

    public static void ejecutarIngesta() throws DAOException {
        CertificadoDAO certificadoDAO = new CertificadoDAO(EM);
        InscripcionDAO inscripcionDAO = new InscripcionDAO(EM);
        CursoDAO cursoDAO = new CursoDAO(EM);
        TemaDAO temaDAO = new TemaDAO(EM);
        PersonaDAO personaDAO = new PersonaDAO(EM);

        certificadoDAO.deleteAll(certificadoDAO.findAll());
        inscripcionDAO.deleteAll(inscripcionDAO.findAll());
        cursoDAO.deleteAll(cursoDAO.findAll());
        temaDAO.deleteAll(temaDAO.findAll());
        personaDAO.deleteAll(personaDAO.findAll());

        personaDAO.save(new Persona(null, ACTIVO, "Ana", "Martinez", "Lopez", "EMP001", "ana.martinez@empresa.com"));
        personaDAO.save(new Persona(null, ACTIVO, "Luis", "Hernandez", "Garcia", "EMP002", "luis.hernandez@empresa.com"));
        personaDAO.save(new Persona(null, ACTIVO, "Carla", "Ramirez", "Torres", "EMP003", "carla.ramirez@empresa.com"));
        personaDAO.save(new Persona(null, ACTIVO, "Jorge", "Sanchez", "Ruiz", "EMP004", "jorge.sanchez@empresa.com"));
        personaDAO.save(new Persona(null, ACTIVO, "Miriam", "Flores", "Diaz", "EMP005", "miriam.flores@empresa.com"));
        List<Persona> personas = personaDAO.findAll();

        Tema java = temaDAO.save(new Tema(null, ACTIVO, "Fundamentos de Java", TipoTema.LENGUAJE));
        Tema spring = temaDAO.save(new Tema(null, ACTIVO, "Desarrollo Web con Spring", TipoTema.FRAMEWORK));
        Tema sql = temaDAO.save(new Tema(null, ACTIVO, "Bases de Datos Relacionales", TipoTema.BASE_DATOS));
        Tema comunicacion = temaDAO.save(new Tema(null, ACTIVO, "Comunicación Efectiva", TipoTema.HABILIDAD_BLANDA));

        Curso javaBasico = cursoDAO.save(new Curso(null, ACTIVO,
                "Java Básico", "Introducir la sintaxis y la programación orientada a objetos en Java",
                java, personas.get(0)));
        Curso springIntermedio = cursoDAO.save(new Curso(null, ACTIVO,
                "Spring Boot Intermedio", "Construir servicios REST con Spring Boot",
                spring, personas.get(1)));
        Curso sqlAnalistas = cursoDAO.save(new Curso(null, ACTIVO,
                "SQL para Analistas", "Consultar y modelar datos relacionales",
                sql, personas.get(2)));
        cursoDAO.save(new Curso(null, ACTIVO,
                "Comunicación en Equipos", "Mejorar la comunicación y el trabajo en equipo",
                comunicacion, personas.get(1)));

        Inscripcion aprobadaJava = inscripcionDAO.save(new Inscripcion(null, ACTIVO,
                javaBasico, personas.get(0), LocalDate.of(2026, 1, 15), ResultadoInscripcion.APROBADO));
        inscripcionDAO.save(new Inscripcion(null, ACTIVO,
                javaBasico, personas.get(1), LocalDate.of(2026, 1, 20), ResultadoInscripcion.PENDIENTE));
        Inscripcion aprobadaSql = inscripcionDAO.save(new Inscripcion(null, ACTIVO,
                sqlAnalistas, personas.get(2), LocalDate.of(2026, 2, 03), ResultadoInscripcion.APROBADO));
        inscripcionDAO.save(new Inscripcion(null, ACTIVO,
                springIntermedio, personas.get(3), LocalDate.of(2026, 2, 10), ResultadoInscripcion.PENDIENTE));
        inscripcionDAO.save(new Inscripcion(null, ACTIVO,
                sqlAnalistas, personas.get(1), LocalDate.of(2026, 2, 12), ResultadoInscripcion.NO_APROBADO));

        certificadoDAO.save(new Certificado(null, ACTIVO, aprobadaJava, "CERT-0001", LocalDate.of(2026, 1, 30)));
        certificadoDAO.save(new Certificado(null, ACTIVO, aprobadaSql, "CERT-0002", LocalDate.of(2026, 2, 15)));
    }
}