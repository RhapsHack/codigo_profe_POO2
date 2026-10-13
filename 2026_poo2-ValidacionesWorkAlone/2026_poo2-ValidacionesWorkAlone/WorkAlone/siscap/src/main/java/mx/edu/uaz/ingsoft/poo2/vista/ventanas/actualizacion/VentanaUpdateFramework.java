package mx.edu.uaz.ingsoft.poo2.vista.ventanas.actualizacion;


import mx.edu.uaz.ingsoft.poo2.modelo.entidades.Framework;
import mx.edu.uaz.ingsoft.poo2.modelo.entidades.IEntidad;
import static mx.edu.uaz.ingsoft.poo2.utils.Constantes.*;

public class VentanaUpdateFramework<T extends IEntidad> extends AbstractVentanaUpdateEntidad<Framework> {
    public VentanaUpdateFramework(String title) {
        super(title, CAT_FRAMEWORK);
    }

    @Override
    public void actualizarCamposEntidad(Framework original, Framework nuevo) {
        original.setNombre(nuevo.getNombre());
        original.setDescripcion(nuevo.getDescripcion());
        original.setLenguajeBase(nuevo.getLenguajeBase());
        original.setTipo(nuevo.getTipo());
    }
}
