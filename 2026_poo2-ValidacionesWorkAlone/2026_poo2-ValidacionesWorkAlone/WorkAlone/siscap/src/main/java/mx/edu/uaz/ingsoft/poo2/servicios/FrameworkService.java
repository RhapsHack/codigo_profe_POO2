package mx.edu.uaz.ingsoft.poo2.servicios;


import mx.edu.uaz.ingsoft.poo2.modelo.daos.interf.IFrameworkDAO;
import mx.edu.uaz.ingsoft.poo2.modelo.entidades.Framework;
import mx.edu.uaz.ingsoft.poo2.servicios.interfaces.IFrameworkService;

public class FrameworkService extends ServicioBase<Framework, IFrameworkDAO> implements IFrameworkService {
    public FrameworkService(IFrameworkDAO dao) {
        super(dao);
    }
}
