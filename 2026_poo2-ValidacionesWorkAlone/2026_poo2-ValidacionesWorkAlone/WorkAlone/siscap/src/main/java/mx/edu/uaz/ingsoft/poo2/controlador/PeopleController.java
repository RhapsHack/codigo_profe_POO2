package mx.edu.uaz.ingsoft.poo2.controlador;


import mx.edu.uaz.ingsoft.poo2.modelo.entidades.Persona;
import mx.edu.uaz.ingsoft.poo2.servicios.interfaces.IPeopleService;
import mx.edu.uaz.ingsoft.poo2.utils.validadores.interfaces.IValidadorEntidad;
import mx.edu.uaz.ingsoft.poo2.vista.ventanas.actualizacion.AbstractVentanaUpdateEntidad;
import mx.edu.uaz.ingsoft.poo2.vista.ventanas.borrado.AbstractVentanaBorradoEntidad;
import mx.edu.uaz.ingsoft.poo2.vista.ventanas.captura.AbstractVentanaCapturaEntidad;
import mx.edu.uaz.ingsoft.poo2.vista.ventanas.listado.AbstractVentanaListadoEntidades;

public class PeopleController extends AbstractControllerBase<Persona, IPeopleService> {
    public PeopleController(IPeopleService servicio,
                            AbstractVentanaCapturaEntidad<Persona> ventanaCaptura,
                            AbstractVentanaListadoEntidades<Persona> ventanaListado,
                            AbstractVentanaBorradoEntidad<Persona> ventanaBorrado,
                            AbstractVentanaUpdateEntidad<Persona> ventanaUpdate,
                            IValidadorEntidad<Persona> validador) {
        super(Persona.class,servicio,ventanaCaptura,ventanaListado,ventanaBorrado,ventanaUpdate,validador);
    }

}
