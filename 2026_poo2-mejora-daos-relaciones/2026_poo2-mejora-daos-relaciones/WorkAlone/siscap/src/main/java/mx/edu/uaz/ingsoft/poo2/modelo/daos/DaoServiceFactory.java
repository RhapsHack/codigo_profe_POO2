package mx.edu.uaz.ingsoft.poo2.modelo.daos;


import jakarta.persistence.EntityManager;
import mx.edu.uaz.ingsoft.poo2.modelo.daos.impl.CertificadoDAO;
import mx.edu.uaz.ingsoft.poo2.modelo.daos.impl.CursoDAO;
import mx.edu.uaz.ingsoft.poo2.modelo.daos.impl.DAOBase;
import mx.edu.uaz.ingsoft.poo2.modelo.daos.impl.FrameworkDAO;
import mx.edu.uaz.ingsoft.poo2.modelo.daos.impl.InscripcionDAO;
import mx.edu.uaz.ingsoft.poo2.modelo.daos.impl.PersonaDAO;
import mx.edu.uaz.ingsoft.poo2.modelo.daos.impl.ProgLanguagesDAO;
import mx.edu.uaz.ingsoft.poo2.modelo.daos.impl.TemaDAO;
import mx.edu.uaz.ingsoft.poo2.modelo.daos.interf.ICertificadoDAO;
import mx.edu.uaz.ingsoft.poo2.modelo.daos.interf.ICursoDAO;
import mx.edu.uaz.ingsoft.poo2.modelo.daos.interf.IFrameworkDAO;
import mx.edu.uaz.ingsoft.poo2.modelo.daos.interf.IInscripcionDAO;
import mx.edu.uaz.ingsoft.poo2.modelo.daos.interf.IPersonaDAO;
import mx.edu.uaz.ingsoft.poo2.modelo.daos.interf.IProgLanguagesDAO;
import mx.edu.uaz.ingsoft.poo2.modelo.daos.interf.ITemaDAO;
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
              case CAT_TEMA -> new TemaDAO(EM);
              case CAT_CURSO -> new CursoDAO(EM);
              case CAT_INSCRIPCION -> new InscripcionDAO(EM);
              case CAT_CERTIFICADO -> new CertificadoDAO(EM);

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
                        case CAT_TEMA -> new TemaService((ITemaDAO) daoOptional.get());
                        case CAT_CURSO -> new CursoService((ICursoDAO) daoOptional.get());
                        case CAT_INSCRIPCION -> new InscripcionService((IInscripcionDAO) daoOptional.get());
                        case CAT_CERTIFICADO -> new CertificadoService((ICertificadoDAO) daoOptional.get());
                        default -> null;
                    }
            );
        }
        return Optional.empty();
    }


}
