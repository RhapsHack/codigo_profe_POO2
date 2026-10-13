package mx.edu.uaz.ingsoft.poo2.modelo.daos.impl;

import jakarta.persistence.EntityManager;

import mx.edu.uaz.ingsoft.poo2.modelo.daos.interf.ITemaDAO;
import mx.edu.uaz.ingsoft.poo2.modelo.entidades.Tema;

public class TemaDAO extends DAOBase<Tema> implements ITemaDAO {
    public TemaDAO(EntityManager em) {
        super(Tema.class, em);
    }
}