package mx.edu.uaz.ingsoft.poo2.modelo.daos.impl;

import jakarta.persistence.EntityManager;

import mx.edu.uaz.ingsoft.poo2.modelo.daos.interf.ISistemaOperativoDAO;
import mx.edu.uaz.ingsoft.poo2.modelo.entidades.SistemaOperativo;

public class SistemaOperativoDAO extends DAOBase<SistemaOperativo> implements ISistemaOperativoDAO {
    public SistemaOperativoDAO(EntityManager em) {
        super(SistemaOperativo.class, em);
    }
}
