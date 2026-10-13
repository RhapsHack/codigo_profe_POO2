package mx.edu.uaz.ingsoft.poo2.servicios;

import mx.edu.uaz.ingsoft.poo2.modelo.daos.DAOException;
import mx.edu.uaz.ingsoft.poo2.modelo.daos.interf.IInscripcionDAO;
import mx.edu.uaz.ingsoft.poo2.modelo.entidades.Curso;
import mx.edu.uaz.ingsoft.poo2.modelo.entidades.Inscripcion;
import mx.edu.uaz.ingsoft.poo2.modelo.entidades.Persona;
import mx.edu.uaz.ingsoft.poo2.modelo.entidades.ResultadoInscripcion;
import mx.edu.uaz.ingsoft.poo2.servicios.interfaces.IInscripcionService;

import java.time.LocalDate;
import java.util.List;

import static mx.edu.uaz.ingsoft.poo2.utils.Constantes.MSG_INSCRIPCION_DUPLICADA;

public class InscripcionService extends ServicioConBorradoLogico<Inscripcion, IInscripcionDAO>
        implements IInscripcionService {

    public InscripcionService(IInscripcionDAO dao) {
        super(dao);
    }

    @Override
    public Inscripcion inscribir(Persona persona, Curso curso, LocalDate fecha)
            throws DAOException, ReglaNegocioException {
        if (persona == null || curso == null) {
            throw new ReglaNegocioException(MSG_INSCRIPCION_DUPLICADA);
        }
        boolean yaInscrita = dao.findByCurso(curso).stream()
                .anyMatch(inscripcion -> inscripcion.getPersona() != null
                        && persona.getId() != null
                        && persona.getId().equals(inscripcion.getPersona().getId()));
        if (yaInscrita) {
            throw new ReglaNegocioException(MSG_INSCRIPCION_DUPLICADA);
        }
        var inscripcion = new Inscripcion(null, true, curso, persona, fecha, ResultadoInscripcion.PENDIENTE);
        curso.agregarInscripcion(inscripcion);
        dao.save(inscripcion);
        return inscripcion;
    }

    @Override
    public List<Inscripcion> buscarPorCurso(Curso curso) {
        return soloActivos(dao.findByCurso(curso));
    }

    @Override
    public List<Inscripcion> buscarPorPersona(Persona persona) {
        return soloActivos(dao.findByPersona(persona));
    }

    @Override
    public List<Inscripcion> aprobadasDeCurso(Curso curso) {
        return soloActivos(dao.findAprobadasDeCurso(curso));
    }

    @Override
    public void actualizarResultado(Inscripcion inscripcion, ResultadoInscripcion resultado) throws DAOException {
        inscripcion.setResultado(resultado);
        dao.update(inscripcion);
    }

    private List<Inscripcion> soloActivos(List<Inscripcion> inscripciones) {
        return inscripciones.stream().filter(Inscripcion::isActivo).toList();
    }
}