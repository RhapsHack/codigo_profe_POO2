package mx.edu.uaz.ingsoft.poo2.servicios;

import mx.edu.uaz.ingsoft.poo2.modelo.daos.interf.ICursoDAO;
import mx.edu.uaz.ingsoft.poo2.modelo.entidades.Curso;
import mx.edu.uaz.ingsoft.poo2.modelo.entidades.Persona;
import mx.edu.uaz.ingsoft.poo2.modelo.entidades.Tema;
import mx.edu.uaz.ingsoft.poo2.servicios.interfaces.ICursoService;

import java.util.List;

public class CursoService extends ServicioConBorradoLogico<Curso, ICursoDAO> implements ICursoService {

    public CursoService(ICursoDAO dao) {
        super(dao);
    }

    @Override
    public List<Curso> buscarPorInstructor(Persona instructor) {
        return soloActivos(dao.findByInstructor(instructor));
    }

    @Override
    public List<Curso> buscarPorTema(Tema tema) {
        return soloActivos(dao.findByTema(tema));
    }

    @Override
    public Curso cargarInscripciones(Curso curso) {
        return dao.findConInscripciones(curso);
    }

    private List<Curso> soloActivos(List<Curso> cursos) {
        return cursos.stream().filter(Curso::isActivo).toList();
    }
}