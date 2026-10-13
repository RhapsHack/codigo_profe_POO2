package mx.edu.uaz.ingsoft.poo2.modelo.daos.impl;

import jakarta.persistence.EntityManager;

import mx.edu.uaz.ingsoft.poo2.modelo.daos.interf.IInscripcionDAO;
import mx.edu.uaz.ingsoft.poo2.modelo.entidades.Curso;
import mx.edu.uaz.ingsoft.poo2.modelo.entidades.Inscripcion;
import mx.edu.uaz.ingsoft.poo2.modelo.entidades.Persona;
import mx.edu.uaz.ingsoft.poo2.modelo.entidades.ResultadoInscripcion;

import java.util.List;

public class InscripcionDAO extends DAOBase<Inscripcion> implements IInscripcionDAO {

    public InscripcionDAO(EntityManager em) {
        super(Inscripcion.class, em);
    }

    @Override
    public List<Inscripcion> findByCurso(Curso curso) {
        return findByCampo("curso", curso);
    }

    @Override
    public List<Inscripcion> findByPersona(Persona persona) {
        return findByCampo("persona", persona);
    }

    @Override
    public List<Inscripcion> findAprobadasDeCurso(Curso curso) {
        var cb = getEm().getCriteriaBuilder();
        var consulta = cb.createQuery(Inscripcion.class);
        var raiz = consulta.from(Inscripcion.class);
        consulta.select(raiz).where(cb.and(
                cb.equal(raiz.get("curso"), curso),
                cb.equal(raiz.get("resultado"), ResultadoInscripcion.APROBADO)));
        return getEm().createQuery(consulta).getResultList();
    }
}