package mx.edu.uaz.ingsoft.poo2.modelo.daos.impl;

import jakarta.persistence.EntityManager;

import mx.edu.uaz.ingsoft.poo2.modelo.daos.interf.IHerramientaDAO;
import mx.edu.uaz.ingsoft.poo2.modelo.entidades.Herramienta;

public class HerramientaDAO extends DAOBase<Herramienta> implements IHerramientaDAO {
    public HerramientaDAO(EntityManager em) {
        super(Herramienta.class, em);
    }
}
