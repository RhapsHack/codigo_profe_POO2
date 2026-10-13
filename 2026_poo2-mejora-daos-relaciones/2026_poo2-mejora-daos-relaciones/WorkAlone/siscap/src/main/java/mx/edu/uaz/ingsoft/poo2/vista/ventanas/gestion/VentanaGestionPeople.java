package mx.edu.uaz.ingsoft.poo2.vista.ventanas.gestion;

import mx.edu.uaz.ingsoft.poo2.modelo.entidades.Persona;
import mx.edu.uaz.ingsoft.poo2.servicios.interfaces.IServicioBase;
import static mx.edu.uaz.ingsoft.poo2.utils.Constantes.*;

public class VentanaGestionPeople extends AbstractVentanaGestionCatalogo<Persona> {
    public VentanaGestionPeople(String tituloVentana, IServicioBase<Persona> servicio) {
        super(tituloVentana, servicio, CAT_PEOPLE);
    }
}
