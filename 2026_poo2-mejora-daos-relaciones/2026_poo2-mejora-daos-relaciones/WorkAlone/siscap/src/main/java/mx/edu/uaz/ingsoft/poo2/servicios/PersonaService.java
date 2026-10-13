package mx.edu.uaz.ingsoft.poo2.servicios;



import mx.edu.uaz.ingsoft.poo2.modelo.daos.interf.IPersonaDAO;

import mx.edu.uaz.ingsoft.poo2.modelo.entidades.Persona;

import mx.edu.uaz.ingsoft.poo2.servicios.interfaces.IPeopleService;

public class PersonaService extends ServicioBase<Persona, IPersonaDAO> implements IPeopleService {
    public PersonaService(IPersonaDAO dao) {
        super(dao);
    }
}
