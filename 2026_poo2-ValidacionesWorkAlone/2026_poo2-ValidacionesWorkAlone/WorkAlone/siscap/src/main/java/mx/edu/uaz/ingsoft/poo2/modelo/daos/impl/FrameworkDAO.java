package mx.edu.uaz.ingsoft.poo2.modelo.daos.impl;

import jakarta.persistence.EntityManager;

import mx.edu.uaz.ingsoft.poo2.modelo.daos.interf.IFrameworkDAO;
import mx.edu.uaz.ingsoft.poo2.modelo.entidades.Framework;

public class FrameworkDAO extends DAOBase<Framework> implements IFrameworkDAO {
    public FrameworkDAO(EntityManager em) {
        super(Framework.class, em);
    }
}
