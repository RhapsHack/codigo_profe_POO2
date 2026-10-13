package mx.edu.uaz.ingsoft.poo2.controlador.utils;



import mx.edu.uaz.ingsoft.poo2.controlador.AbstractControllerBase;
import mx.edu.uaz.ingsoft.poo2.controlador.FrameworkController;
import mx.edu.uaz.ingsoft.poo2.controlador.LangProgController;
import mx.edu.uaz.ingsoft.poo2.controlador.PeopleController;
import mx.edu.uaz.ingsoft.poo2.modelo.daos.DaoServiceFactory;

import mx.edu.uaz.ingsoft.poo2.servicios.interfaces.IFrameworkService;
import mx.edu.uaz.ingsoft.poo2.servicios.interfaces.ILangProgService;
import mx.edu.uaz.ingsoft.poo2.servicios.interfaces.IPeopleService;
import mx.edu.uaz.ingsoft.poo2.utils.validadores.ValidadorEntidadFactory;
import mx.edu.uaz.ingsoft.poo2.vista.ventanas.actualizacion.AbstractVentanaUpdateEntidad;
import mx.edu.uaz.ingsoft.poo2.vista.ventanas.borrado.AbstractVentanaBorradoEntidad;
import mx.edu.uaz.ingsoft.poo2.vista.ventanas.captura.AbstractVentanaCapturaEntidad;
import mx.edu.uaz.ingsoft.poo2.vista.ventanas.listado.AbstractVentanaListadoEntidades;

import java.util.Optional;

import static  mx.edu.uaz.ingsoft.poo2.utils.Constantes.*;


public abstract class ControllerFactory {


    public static Optional<AbstractControllerBase> createEntityController(String nombreCatalogo,
                                                                          AbstractVentanaCapturaEntidad ventanaCaptura,
                                                                          AbstractVentanaListadoEntidades ventanaListado,
                                                                          AbstractVentanaBorradoEntidad ventanaBorrado,
                                                                          AbstractVentanaUpdateEntidad ventanaUpdate){
        var servicio = DaoServiceFactory.getServicio(nombreCatalogo);
        var validador = ValidadorEntidadFactory.crearValidador(nombreCatalogo);
        if(servicio.isPresent()&&validador.isPresent()){
            var controlador = switch (nombreCatalogo){
                case CAT_FRAMEWORK -> new FrameworkController((IFrameworkService) servicio.get(),ventanaCaptura,ventanaListado, ventanaBorrado,ventanaUpdate,validador.get() );
                case CAT_PEOPLE ->  new PeopleController((IPeopleService) servicio.get(),ventanaCaptura,ventanaListado, ventanaBorrado,ventanaUpdate,validador.get());
                case CAT_LANG_PROG ->  new LangProgController((ILangProgService) servicio.get(),ventanaCaptura,ventanaListado,ventanaBorrado,ventanaUpdate, validador.get());
                default -> null;
            };
            return Optional.ofNullable(controlador);
        }else{
            return Optional.empty();
        }
    }
}
