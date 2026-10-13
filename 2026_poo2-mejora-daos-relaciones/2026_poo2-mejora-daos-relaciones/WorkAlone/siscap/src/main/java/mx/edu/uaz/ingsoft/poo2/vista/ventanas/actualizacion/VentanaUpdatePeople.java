package mx.edu.uaz.ingsoft.poo2.vista.ventanas.actualizacion;

import mx.edu.uaz.ingsoft.poo2.modelo.entidades.IEntidad;
import mx.edu.uaz.ingsoft.poo2.modelo.entidades.Persona;

import static mx.edu.uaz.ingsoft.poo2.utils.Constantes.*;

public class VentanaUpdatePeople<T extends IEntidad> extends AbstractVentanaUpdateEntidad<Persona> {
    public VentanaUpdatePeople(String title) {
        super(title, CAT_PEOPLE);
    }

    @Override
    public void actualizarCamposEntidad(Persona original, Persona nuevo) {
        original.setNombre(nuevo.getNombre());
        original.setCorreoElectronico(nuevo.getCorreoElectronico());
        original.setNumeroEmpleado(nuevo.getNumeroEmpleado());
        original.setSegundoApellido(nuevo.getSegundoApellido());
        original.setPrimerApellido(nuevo.getPrimerApellido());
    }
}
