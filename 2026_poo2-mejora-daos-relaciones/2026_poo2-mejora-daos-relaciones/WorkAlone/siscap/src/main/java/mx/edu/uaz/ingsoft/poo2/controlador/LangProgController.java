package mx.edu.uaz.ingsoft.poo2.controlador;


import mx.edu.uaz.ingsoft.poo2.modelo.entidades.ProgLanguages;
import mx.edu.uaz.ingsoft.poo2.servicios.interfaces.ILangProgService;
import mx.edu.uaz.ingsoft.poo2.utils.validadores.interfaces.IValidadorEntidad;
import mx.edu.uaz.ingsoft.poo2.vista.ventanas.actualizacion.AbstractVentanaUpdateEntidad;
import mx.edu.uaz.ingsoft.poo2.vista.ventanas.borrado.AbstractVentanaBorradoEntidad;
import mx.edu.uaz.ingsoft.poo2.vista.ventanas.captura.AbstractVentanaCapturaEntidad;
import mx.edu.uaz.ingsoft.poo2.vista.ventanas.listado.AbstractVentanaListadoEntidades;

public class LangProgController extends AbstractControllerBase<ProgLanguages, ILangProgService> {
    public LangProgController(ILangProgService servicio,
                              AbstractVentanaCapturaEntidad<ProgLanguages> ventanaCaptura,
                              AbstractVentanaListadoEntidades<ProgLanguages> ventanaListado,
                              AbstractVentanaBorradoEntidad<ProgLanguages> ventanaBorrado,
                              AbstractVentanaUpdateEntidad<ProgLanguages> ventanaUpdate,
                              IValidadorEntidad<ProgLanguages> validador) {
        super(ProgLanguages.class,servicio,ventanaCaptura,ventanaListado,ventanaBorrado,ventanaUpdate,validador);
    }

}
