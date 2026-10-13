package mx.edu.uaz.ingsoft.poo2.servicios;


import mx.edu.uaz.ingsoft.poo2.modelo.daos.interf.IProgLanguagesDAO;
import mx.edu.uaz.ingsoft.poo2.modelo.entidades.ProgLanguages;
import mx.edu.uaz.ingsoft.poo2.servicios.interfaces.ILangProgService;

public class ProgLangService extends ServicioBase<ProgLanguages, IProgLanguagesDAO> implements ILangProgService {
    public ProgLangService(IProgLanguagesDAO dao) {
        super(dao);
    }

}
