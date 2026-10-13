package mx.edu.uaz.ingsoft.poo2.modelo.daos.impl;

import jakarta.persistence.EntityManager;

import mx.edu.uaz.ingsoft.poo2.modelo.daos.interf.IBaseDeDatosDAO;
import mx.edu.uaz.ingsoft.poo2.modelo.entidades.BaseDeDatos;

public class BaseDeDatosDAO extends DAOBase<BaseDeDatos> implements IBaseDeDatosDAO {
    public BaseDeDatosDAO(EntityManager em) {
        super(BaseDeDatos.class, em);
    }
}
