package mx.edu.uaz.ingsoft.poo2.modelo.daos.interf;

import mx.edu.uaz.ingsoft.poo2.modelo.entidades.Curso;
import mx.edu.uaz.ingsoft.poo2.modelo.entidades.Inscripcion;
import mx.edu.uaz.ingsoft.poo2.modelo.entidades.Persona;

import java.util.List;

public interface IInscripcionDAO extends IBaseDAO<Inscripcion> {

    /**
     * Inscripciones de un curso, con la persona poblada (relación EAGER).
     */
    List<Inscripcion> findByCurso(Curso curso);

    /**
     * Inscripciones de una persona, con el curso poblado (relación EAGER).
     */
    List<Inscripcion> findByPersona(Persona persona);

    /**
     * Inscripciones aprobadas de un curso. Se resuelve con Criteria porque
     * combina dos condiciones (curso y resultado) sobre la misma tabla.
     */
    List<Inscripcion> findAprobadasDeCurso(Curso curso);
}