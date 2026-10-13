package mx.edu.uaz.ingsoft.poo2.vista.ventanas.gestion;


import mx.edu.uaz.ingsoft.poo2.modelo.entidades.Framework;
import mx.edu.uaz.ingsoft.poo2.servicios.interfaces.IServicioBase;
import static mx.edu.uaz.ingsoft.poo2.utils.Constantes.*;

public class VentanaGestionFrameworks extends AbstractVentanaGestionCatalogo<Framework> {
    public VentanaGestionFrameworks(String tituloVentana, IServicioBase<Framework> servicio) {
        super(tituloVentana, servicio, CAT_FRAMEWORK);
    }
}
