package mx.edu.uaz.ingsoft.poo2.modelo.daos.impl;

import jakarta.persistence.EntityManager;

import mx.edu.uaz.ingsoft.poo2.modelo.daos.interf.ICursoDAO;
import mx.edu.uaz.ingsoft.poo2.modelo.entidades.Curso;
import mx.edu.uaz.ingsoft.poo2.modelo.entidades.Persona;
import mx.edu.uaz.ingsoft.poo2.modelo.entidades.Tema;

import java.util.List;

public class CursoDAO extends DAOBase<Curso> implements ICursoDAO {

    private static final String CONSULTA_CON_INSCRIPCIONES =
            "SELECT DISTINCT c FROM Curso c LEFT JOIN FETCH c.inscripciones WHERE c.id = :id";

    public CursoDAO(EntityManager em) {
        super(Curso.class, em);
    }

    @Override
    public List<Curso> findByInstructor(Persona instructor) {
        return findByCampo("instructor", instructor);
    }

    @Override
    public List<Curso> findByTema(Tema tema) {
        return findByCampo("tema", tema);
    }

    @Override
    public Curso findConInscripciones(Curso curso) {
        if (curso == null || curso.getId() == null) {
            return curso;
        }
        var resultados = getEm()
                .createQuery(CONSULTA_CON_INSCRIPCIONES, Curso.class)
                .setParameter("id", curso.getId())
                .getResultList();
        if (resultados.isEmpty()) {
            return null;
        }
        var cargado = resultados.get(0);
        cargado.setInscripcionesCargadas(true);
        return cargado;
    }
}