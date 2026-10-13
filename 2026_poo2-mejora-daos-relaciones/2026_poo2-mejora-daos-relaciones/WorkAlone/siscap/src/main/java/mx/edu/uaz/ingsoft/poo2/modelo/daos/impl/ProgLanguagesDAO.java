package mx.edu.uaz.ingsoft.poo2.modelo.daos.impl;

import jakarta.persistence.EntityManager;

import mx.edu.uaz.ingsoft.poo2.modelo.daos.interf.IProgLanguagesDAO;
import mx.edu.uaz.ingsoft.poo2.modelo.entidades.ProgLanguages;

public class ProgLanguagesDAO extends DAOBase<ProgLanguages> implements IProgLanguagesDAO {
    public ProgLanguagesDAO(EntityManager em) {
        super(ProgLanguages.class, em);
    }
}
