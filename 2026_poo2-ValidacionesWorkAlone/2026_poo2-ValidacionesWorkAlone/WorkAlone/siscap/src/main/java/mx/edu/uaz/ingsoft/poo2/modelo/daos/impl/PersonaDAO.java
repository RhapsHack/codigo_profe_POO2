package mx.edu.uaz.ingsoft.poo2.modelo.daos.impl;

import jakarta.persistence.EntityManager;

import mx.edu.uaz.ingsoft.poo2.modelo.daos.interf.IPersonaDAO;
import mx.edu.uaz.ingsoft.poo2.modelo.entidades.Persona;

public class PersonaDAO extends DAOBase<Persona> implements IPersonaDAO {
    public PersonaDAO(EntityManager em) {
        super(Persona.class, em);
    }
}
