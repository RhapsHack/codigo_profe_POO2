package mx.edu.uaz.ingsoft.poo2.controlador.capacitaciones;

import mx.edu.uaz.ingsoft.poo2.modelo.daos.DAOException;
import mx.edu.uaz.ingsoft.poo2.modelo.daos.DaoServiceFactory;
import mx.edu.uaz.ingsoft.poo2.modelo.entidades.Curso;
import mx.edu.uaz.ingsoft.poo2.modelo.entidades.Inscripcion;
import mx.edu.uaz.ingsoft.poo2.modelo.entidades.Persona;
import mx.edu.uaz.ingsoft.poo2.modelo.entidades.ResultadoInscripcion;
import mx.edu.uaz.ingsoft.poo2.servicios.ReglaNegocioException;
import mx.edu.uaz.ingsoft.poo2.servicios.interfaces.ICursoService;
import mx.edu.uaz.ingsoft.poo2.servicios.interfaces.IInscripcionService;
import mx.edu.uaz.ingsoft.poo2.servicios.interfaces.IPeopleService;

import java.time.LocalDate;
import java.util.List;

import static mx.edu.uaz.ingsoft.poo2.utils.Constantes.CAT_CURSO;
import static mx.edu.uaz.ingsoft.poo2.utils.Constantes.CAT_INSCRIPCION;
import static mx.edu.uaz.ingsoft.poo2.utils.Constantes.CAT_PEOPLE;

/**
 * Coordina la pantalla de inscripciones: inscribir a una persona en un curso
 * (impidiendo duplicados) y actualizar el resultado de la evaluación.
 */
public class InscripcionesController {

    private final IInscripcionService inscripcionService;
    private final IPeopleService personaService;
    private final ICursoService cursoService;

    public InscripcionesController() {
        this.inscripcionService = (IInscripcionService) DaoServiceFactory.getServicio(CAT_INSCRIPCION).orElseThrow();
        this.personaService = (IPeopleService) DaoServiceFactory.getServicio(CAT_PEOPLE).orElseThrow();
        this.cursoService = (ICursoService) DaoServiceFactory.getServicio(CAT_CURSO).orElseThrow();
    }

    public List<Persona> personas() {
        return personaService.recuperarTodosRegistros();
    }

    public List<Curso> cursos() {
        return cursoService.recuperarTodosRegistros();
    }

    public List<Inscripcion> inscripciones() {
        return inscripcionService.recuperarTodosRegistros();
    }

    public Inscripcion inscribir(Persona persona, Curso curso, LocalDate fecha)
            throws DAOException, ReglaNegocioException {
        return inscripcionService.inscribir(persona, curso, fecha);
    }

    public void cambiarResultado(Inscripcion inscripcion, ResultadoInscripcion resultado) throws DAOException {
        inscripcionService.actualizarResultado(inscripcion, resultado);
    }
}