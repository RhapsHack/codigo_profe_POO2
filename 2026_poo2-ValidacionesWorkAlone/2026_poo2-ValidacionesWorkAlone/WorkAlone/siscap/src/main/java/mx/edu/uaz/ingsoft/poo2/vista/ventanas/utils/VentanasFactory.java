package mx.edu.uaz.ingsoft.poo2.vista.ventanas.utils;



import mx.edu.uaz.ingsoft.poo2.modelo.daos.DaoServiceFactory;
import mx.edu.uaz.ingsoft.poo2.modelo.utils.MensajesResourceBundle;
import mx.edu.uaz.ingsoft.poo2.vista.ventanas.actualizacion.AbstractVentanaUpdateEntidad;
import mx.edu.uaz.ingsoft.poo2.vista.ventanas.actualizacion.VentanaUpdateFramework;
import mx.edu.uaz.ingsoft.poo2.vista.ventanas.actualizacion.VentanaUpdateLangProg;
import mx.edu.uaz.ingsoft.poo2.vista.ventanas.actualizacion.VentanaUpdatePeople;
import mx.edu.uaz.ingsoft.poo2.vista.ventanas.borrado.AbstractVentanaBorradoEntidad;
import mx.edu.uaz.ingsoft.poo2.vista.ventanas.borrado.VentanaBorradoFramework;
import mx.edu.uaz.ingsoft.poo2.vista.ventanas.borrado.VentanaBorradoLangProg;
import mx.edu.uaz.ingsoft.poo2.vista.ventanas.borrado.VentanaBorradoPeople;
import mx.edu.uaz.ingsoft.poo2.vista.ventanas.captura.AbstractVentanaCapturaEntidad;
import mx.edu.uaz.ingsoft.poo2.vista.ventanas.captura.VentanaCapturaFramework;
import mx.edu.uaz.ingsoft.poo2.vista.ventanas.captura.VentanaCapturaPeople;
import mx.edu.uaz.ingsoft.poo2.vista.ventanas.captura.VentanaCapturaProgLang;
import mx.edu.uaz.ingsoft.poo2.vista.ventanas.gestion.AbstractVentanaGestionCatalogo;
import mx.edu.uaz.ingsoft.poo2.vista.ventanas.gestion.VentanaGestionFrameworks;
import mx.edu.uaz.ingsoft.poo2.vista.ventanas.gestion.VentanaGestionPeople;
import mx.edu.uaz.ingsoft.poo2.vista.ventanas.gestion.VentanaGestionProgLangs;
import mx.edu.uaz.ingsoft.poo2.vista.ventanas.listado.AbstractVentanaListadoEntidades;
import mx.edu.uaz.ingsoft.poo2.vista.ventanas.listado.VentanaListadoFrameworks;
import mx.edu.uaz.ingsoft.poo2.vista.ventanas.listado.VentanaListadoLenguajesProgramacion;
import mx.edu.uaz.ingsoft.poo2.vista.ventanas.listado.VentanaListadoPersonas;

import java.util.Optional;
import java.util.ResourceBundle;
import static mx.edu.uaz.ingsoft.poo2.utils.Constantes.*;


public abstract class VentanasFactory {


    static ResourceBundle resBundle;
    static{
        resBundle = MensajesResourceBundle.MENSAJES.getResourceBundle();
    }

    public static Optional<AbstractVentanaGestionCatalogo> crearVentanaGestionCatalogo(String nombreCatalogo){
        var servicioOptional = DaoServiceFactory.getServicio(nombreCatalogo);
        if(servicioOptional.isPresent()){
            return Optional.ofNullable(
                    switch (nombreCatalogo){
                        case CAT_FRAMEWORK ->new VentanaGestionFrameworks(resBundle.getString("ventana.title.gestion.frameworks"),servicioOptional.get());
                        case CAT_PEOPLE ->new VentanaGestionPeople(resBundle.getString("ventana.title.gestion.people"),servicioOptional.get());
                        case CAT_LANG_PROG ->new VentanaGestionProgLangs(resBundle.getString("ventana.title.gestion.prog_langs"),servicioOptional.get());
                        default -> null;
                    }
            );
        }
        return Optional.empty();
    }

    public static Optional<AbstractVentanaCapturaEntidad> crearVentanaCaptura(String nombreCatalogo){
        return Optional.ofNullable( switch (nombreCatalogo){
            case CAT_FRAMEWORK ->  new VentanaCapturaFramework(resBundle.getString("capturar.persona"));
            case CAT_PEOPLE -> new VentanaCapturaPeople(resBundle.getString("capturar.framework"));
            case CAT_LANG_PROG -> {
                var salida = new VentanaCapturaProgLang(resBundle.getString("capturar.lenguaje_programacion"));
                yield salida;
            }
            default -> null;
            }
        );
    }

    public static Optional<AbstractVentanaListadoEntidades> crearVentanaListado(String nombreCatalogo){
        return Optional.ofNullable( switch (nombreCatalogo){
                    case CAT_FRAMEWORK ->  new VentanaListadoFrameworks(resBundle.getString("lista.framework"));
                    case CAT_PEOPLE -> new VentanaListadoPersonas(resBundle.getString("lista.persona"));
                    case CAT_LANG_PROG -> new VentanaListadoLenguajesProgramacion(resBundle.getString("lista.lenguaje_programacion"));
                    default -> null;
                }
        );
    }
    public static Optional<AbstractVentanaBorradoEntidad> crearVentanaBorrado(String nombreCatalogo){
        return Optional.ofNullable(
                switch (nombreCatalogo){
                    case CAT_FRAMEWORK -> new VentanaBorradoFramework(resBundle.getString("ventana.title.borrado.framework"));
                    case CAT_PEOPLE -> new VentanaBorradoPeople(resBundle.getString("ventana.title.borrado.persona"));
                    case CAT_LANG_PROG -> new VentanaBorradoLangProg(resBundle.getString("ventana.title.borrado.lenguaje_programacion"));
                    default -> null;
                }
        );
    }
    public static Optional<AbstractVentanaUpdateEntidad> crearVentanaUpdate(String nombreCatalogo){
        return Optional.ofNullable(
                switch (nombreCatalogo){
                    case CAT_FRAMEWORK -> new VentanaUpdateFramework(resBundle.getString("ventana.title.update.framework"));
                    case CAT_PEOPLE -> new VentanaUpdatePeople(resBundle.getString("ventana.title.update.persona"));
                    case CAT_LANG_PROG -> new VentanaUpdateLangProg(resBundle.getString("ventana.title.update.lenguaje_programacion"));
                    default -> null;
                }
        );
    }
}
