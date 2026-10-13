package mx.edu.uaz.ingsoft.poo2.modelo.daos;


import jakarta.persistence.EntityManager;
import mx.edu.uaz.ingsoft.poo2.modelo.daos.impl.DAOBase;
import mx.edu.uaz.ingsoft.poo2.modelo.daos.impl.FrameworkDAO;
import mx.edu.uaz.ingsoft.poo2.modelo.daos.impl.PersonaDAO;
import mx.edu.uaz.ingsoft.poo2.modelo.daos.impl.ProgLanguagesDAO;
import mx.edu.uaz.ingsoft.poo2.modelo.daos.interf.IFrameworkDAO;
import mx.edu.uaz.ingsoft.poo2.modelo.daos.interf.IPersonaDAO;
import mx.edu.uaz.ingsoft.poo2.modelo.daos.interf.IProgLanguagesDAO;
import mx.edu.uaz.ingsoft.poo2.modelo.utils.JPAUtil;
import mx.edu.uaz.ingsoft.poo2.servicios.*;
import mx.edu.uaz.ingsoft.poo2.servicios.interfaces.IServicioBase;
import mx.edu.uaz.ingsoft.poo2.servicios.interfaces.IServicioCarga;

import java.util.Optional;
import static mx.edu.uaz.ingsoft.poo2.utils.Constantes.*;


public abstract class DaoServiceFactory {

    protected static final EntityManager EM = JPAUtil.crearEntityManager();
    private DaoServiceFactory(){}

    public static Optional<DAOBase> getDAO(String nombre){
        return Optional.ofNullable(
          switch (nombre){
              case CAT_FRAMEWORK -> new FrameworkDAO(EM);
              case CAT_PEOPLE -> new PersonaDAO(EM);
              case CAT_LANG_PROG -> new ProgLanguagesDAO(EM);

              default -> null;
          }
        );
    }

    public static Optional<IServicioBase> getServicio(String nombre){
        var daoOptional = getDAO(nombre);
        if (daoOptional.isPresent()){
            return Optional.ofNullable(
                    switch (nombre){
                        case CAT_FRAMEWORK -> new FrameworkService((IFrameworkDAO) daoOptional.get());
                        case CAT_PEOPLE -> new PersonaService((IPersonaDAO) daoOptional.get());
                        case CAT_LANG_PROG -> new ProgLangService((IProgLanguagesDAO) daoOptional.get());
                        default -> null;
                    }
            );
        }
        return Optional.empty();
    }


}
