package mx.edu.uaz.ingsoft.poo2.vista.ventanas.gestion;

import mx.edu.uaz.ingsoft.poo2.modelo.entidades.ProgLanguages;
import mx.edu.uaz.ingsoft.poo2.servicios.interfaces.IServicioBase;
import static mx.edu.uaz.ingsoft.poo2.utils.Constantes.*;

public class VentanaGestionProgLangs extends AbstractVentanaGestionCatalogo<ProgLanguages> {
    public VentanaGestionProgLangs(String tituloVentana, IServicioBase<ProgLanguages> servicio) {
        super(tituloVentana, servicio, CAT_LANG_PROG);
    }
}
