package mx.edu.uaz.ingsoft.poo2.controlador.capacitaciones;

import mx.edu.uaz.ingsoft.poo2.modelo.daos.DAOException;
import mx.edu.uaz.ingsoft.poo2.modelo.daos.DaoServiceFactory;
import mx.edu.uaz.ingsoft.poo2.modelo.entidades.Curso;
import mx.edu.uaz.ingsoft.poo2.modelo.entidades.Persona;
import mx.edu.uaz.ingsoft.poo2.modelo.entidades.Tema;
import mx.edu.uaz.ingsoft.poo2.servicios.interfaces.ICursoService;
import mx.edu.uaz.ingsoft.poo2.servicios.interfaces.IPeopleService;
import mx.edu.uaz.ingsoft.poo2.servicios.interfaces.ITemaService;

import java.util.List;

import static mx.edu.uaz.ingsoft.poo2.utils.Constantes.CAT_CURSO;
import static mx.edu.uaz.ingsoft.poo2.utils.Constantes.CAT_PEOPLE;
import static mx.edu.uaz.ingsoft.poo2.utils.Constantes.CAT_TEMA;

/**
 * Coordina la pantalla de cursos con los servicios: listar personas y temas
 * para los combos, listar cursos y ejecutar altas, cambios y borrado lógico.
 * La vista solo captura y muestra; la lógica de persistencia queda aquí.
 */
public class CursosController {

    private final ICursoService cursoService;
    private final IPeopleService personaService;
    private final ITemaService temaService;

    public CursosController() {
        this.cursoService = (ICursoService) DaoServiceFactory.getServicio(CAT_CURSO).orElseThrow();
        this.personaService = (IPeopleService) DaoServiceFactory.getServicio(CAT_PEOPLE).orElseThrow();
        this.temaService = (ITemaService) DaoServiceFactory.getServicio(CAT_TEMA).orElseThrow();
    }

    public List<Persona> personas() {
        return personaService.recuperarTodosRegistros();
    }

    public List<Tema> temas() {
        return temaService.recuperarTodosRegistros();
    }

    public List<Curso> cursos() {
        return cursoService.recuperarTodosRegistros();
    }

    public Curso guardar(String nombre, String objetivo, Persona instructor, Tema tema) throws DAOException {
        var curso = new Curso(null, true, nombre, objetivo, tema, instructor);
        cursoService.guardar(curso);
        return curso;
    }

    public void actualizar(Curso curso, String nombre, String objetivo, Persona instructor, Tema tema)
            throws DAOException {
        curso.setNombre(nombre);
        curso.setObjetivo(objetivo);
        curso.setInstructor(instructor);
        curso.setTema(tema);
        cursoService.actualizar(curso);
    }

    public void borrar(Curso curso) throws DAOException {
        cursoService.borrarRegistro(curso);
    }
}