package mx.edu.uaz.ingsoft.poo2.servicios;

import mx.edu.uaz.ingsoft.poo2.modelo.daos.interf.ITemaDAO;
import mx.edu.uaz.ingsoft.poo2.modelo.entidades.Tema;
import mx.edu.uaz.ingsoft.poo2.servicios.interfaces.ITemaService;

import java.util.Optional;

public class TemaService extends ServicioConBorradoLogico<Tema, ITemaDAO> implements ITemaService {

    public TemaService(ITemaDAO dao) {
        super(dao);
    }

    @Override
    public Optional<Tema> buscarPorNombre(String nombre) {
        return dao.findByCampo("nombre", nombre).stream().findFirst();
    }
}