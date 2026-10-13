package mx.edu.uaz.ingsoft.poo2.vista.ventanas.captura;



import mx.edu.uaz.ingsoft.poo2.modelo.entidades.Persona;

import static mx.edu.uaz.ingsoft.poo2.utils.Constantes.*;

public class VentanaCapturaPeople extends AbstractVentanaCapturaEntidad<Persona> {
    public VentanaCapturaPeople(String title) {
        super(title, CAT_PEOPLE);
    }
}
