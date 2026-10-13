package mx.edu.uaz.ingsoft.poo2.servicios.interfaces;

import mx.edu.uaz.ingsoft.poo2.modelo.daos.DAOException;
import mx.edu.uaz.ingsoft.poo2.servicios.ReglaNegocioException;
import mx.edu.uaz.ingsoft.poo2.modelo.entidades.Curso;
import mx.edu.uaz.ingsoft.poo2.modelo.entidades.Inscripcion;
import mx.edu.uaz.ingsoft.poo2.modelo.entidades.Persona;
import mx.edu.uaz.ingsoft.poo2.modelo.entidades.ResultadoInscripcion;

import java.time.LocalDate;
import java.util.List;

public interface IInscripcionService extends IServicioBase<Inscripcion> {

    /**
     * Inscribe a una persona en un curso con resultado inicial PENDIENTE y
     * mantiene ambos lados de la relación {@code Curso 1:N Inscripcion}.
     */
    Inscripcion inscribir(Persona persona, Curso curso, LocalDate fecha)
            throws DAOException, ReglaNegocioException;

    List<Inscripcion> buscarPorCurso(Curso curso);

    List<Inscripcion> buscarPorPersona(Persona persona);

    List<Inscripcion> aprobadasDeCurso(Curso curso);

    void actualizarResultado(Inscripcion inscripcion, ResultadoInscripcion resultado) throws DAOException;
}