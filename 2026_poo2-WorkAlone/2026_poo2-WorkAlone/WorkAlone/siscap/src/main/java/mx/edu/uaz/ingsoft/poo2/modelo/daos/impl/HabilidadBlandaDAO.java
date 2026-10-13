package mx.edu.uaz.ingsoft.poo2.modelo.daos.impl;

import jakarta.persistence.EntityManager;

import mx.edu.uaz.ingsoft.poo2.modelo.daos.interf.IHabilidadBlandaDAO;
import mx.edu.uaz.ingsoft.poo2.modelo.entidades.HabilidadBlanda;

public class HabilidadBlandaDAO extends DAOBase<HabilidadBlanda> implements IHabilidadBlandaDAO {
    public HabilidadBlandaDAO(EntityManager em) {
        super(HabilidadBlanda.class, em);
    }
}
