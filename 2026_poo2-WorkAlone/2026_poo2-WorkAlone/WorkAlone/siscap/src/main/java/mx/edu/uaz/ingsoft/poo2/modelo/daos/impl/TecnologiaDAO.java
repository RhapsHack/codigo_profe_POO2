package mx.edu.uaz.ingsoft.poo2.modelo.daos.impl;

import jakarta.persistence.EntityManager;

import mx.edu.uaz.ingsoft.poo2.modelo.daos.interf.ITecnologiaDAO;
import mx.edu.uaz.ingsoft.poo2.modelo.entidades.Tecnologia;

public class TecnologiaDAO extends DAOBase<Tecnologia> implements ITecnologiaDAO {
    public TecnologiaDAO(EntityManager em) {
        super(Tecnologia.class, em);
    }
}
