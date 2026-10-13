package mx.edu.uaz.ingsoft.poo2.servicios.interfaces;

import mx.edu.uaz.ingsoft.poo2.modelo.entidades.Curso;
import mx.edu.uaz.ingsoft.poo2.modelo.entidades.Persona;
import mx.edu.uaz.ingsoft.poo2.modelo.entidades.Tema;

import java.util.List;

public interface ICursoService extends IServicioBase<Curso> {

    List<Curso> buscarPorInstructor(Persona instructor);

    List<Curso> buscarPorTema(Tema tema);

    /**
     * Devuelve el curso con su colección de inscripciones ya poblada
     * (JOIN FETCH) y marcado como {@code inscripcionesCargadas}.
     */
    Curso cargarInscripciones(Curso curso);
}