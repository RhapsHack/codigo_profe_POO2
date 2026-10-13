package mx.edu.uaz.ingsoft.poo2.controlador;


import mx.edu.uaz.ingsoft.poo2.modelo.entidades.Framework;
import mx.edu.uaz.ingsoft.poo2.servicios.interfaces.IFrameworkService;
import mx.edu.uaz.ingsoft.poo2.utils.validadores.interfaces.IValidadorEntidad;
import mx.edu.uaz.ingsoft.poo2.vista.ventanas.actualizacion.AbstractVentanaUpdateEntidad;
import mx.edu.uaz.ingsoft.poo2.vista.ventanas.borrado.AbstractVentanaBorradoEntidad;
import mx.edu.uaz.ingsoft.poo2.vista.ventanas.captura.AbstractVentanaCapturaEntidad;
import mx.edu.uaz.ingsoft.poo2.vista.ventanas.listado.AbstractVentanaListadoEntidades;

public class FrameworkController extends AbstractControllerBase<Framework, IFrameworkService> {
    public FrameworkController(IFrameworkService servicio,
                               AbstractVentanaCapturaEntidad<Framework> ventanaCaptura,
                               AbstractVentanaListadoEntidades<Framework> ventanaListado,
                               AbstractVentanaBorradoEntidad<Framework> ventanaBorrado,
                               AbstractVentanaUpdateEntidad<Framework> ventanaUpdate,
                               IValidadorEntidad<Framework> validador) {
        super(Framework.class,servicio,ventanaCaptura,ventanaListado,ventanaBorrado,ventanaUpdate,validador);
    }

}
