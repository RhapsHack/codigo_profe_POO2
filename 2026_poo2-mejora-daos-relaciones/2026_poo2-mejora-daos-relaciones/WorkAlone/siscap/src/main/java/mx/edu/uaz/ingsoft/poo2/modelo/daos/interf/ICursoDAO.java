package mx.edu.uaz.ingsoft.poo2.modelo.daos.interf;

import mx.edu.uaz.ingsoft.poo2.modelo.entidades.Curso;
import mx.edu.uaz.ingsoft.poo2.modelo.entidades.Persona;
import mx.edu.uaz.ingsoft.poo2.modelo.entidades.Tema;

import java.util.List;

public interface ICursoDAO extends IBaseDAO<Curso> {

    /**
     * Cursos que dicta una persona instructora (relación 1:N desde Persona).
     * Se resuelve con el {@code findByCampo} genérico porque el filtro es una
     * igualdad simple sobre la referencia.
     */
    List<Curso> findByInstructor(Persona instructor);

    /**
     * Cursos que cubren un tema (relación 1:N desde Tema).
     */
    List<Curso> findByTema(Tema tema);

    /**
     * Carga un curso con su colección de inscripciones poblada en una sola
     * consulta (JOIN FETCH), evitando el problema N+1 de recorrer la colección
     * LAZY. Deja marcado el curso como "inscripciones cargadas".
     */
    Curso findConInscripciones(Curso curso);
}