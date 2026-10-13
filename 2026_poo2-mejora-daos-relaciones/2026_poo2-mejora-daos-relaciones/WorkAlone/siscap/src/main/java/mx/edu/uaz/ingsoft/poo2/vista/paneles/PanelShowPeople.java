package mx.edu.uaz.ingsoft.poo2.vista.paneles;


import mx.edu.uaz.ingsoft.poo2.modelo.entidades.Persona;

import static mx.edu.uaz.ingsoft.poo2.utils.Constantes.TXT_VACIO;
import static mx.edu.uaz.ingsoft.poo2.utils.FunctionUtils.esNulo;
import static mx.edu.uaz.ingsoft.poo2.utils.FunctionUtils.stringValueFromOptional;

public class PanelShowPeople extends PanelShowEntity<Persona> {
    /*

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "primer_apellido")
    private String primerApellido;

    @Column(name = "segundo_apellido")
    private String segundoApellido;

    @Column(name = "numero_empleado")
    private String numeroEmpleado;

    @Column(name = "correo_electronico")
    private String correoElectronico;
     */

    public PanelShowPeople() {
        super();
    }

    @Override
    public void construirFormulario() {
        agregarCampoTexto(resBundle.getString("nombre"));
        agregarCampoTexto(resBundle.getString("primer_apellido"));
        agregarCampoTexto(resBundle.getString("segundo_apellido"));
        agregarCampoTexto(resBundle.getString("numero_empleado"));
        agregarCampoTexto(resBundle.getString("correo_electronico"));

    }

    @Override
    public void limpiarFormulario() {
        campos.values().forEach(unCampoTexto -> {unCampoTexto.setText(TXT_VACIO); });
    }

    @Override
    public Persona crearEntidadDesdeFormulario() {
        entidad = new Persona();
        actualizarEntidadDesdeFormulario(entidad);
        return entidad;
    }
    @Override
    public void actualizarEntidadDesdeFormulario(Persona entidad) {
        entidad.setNombre(stringValueFromOptional(getStringValue(resBundle.getString("nombre"))));
        entidad.setPrimerApellido(stringValueFromOptional(getStringValue(resBundle.getString("primer_apellido"))));
        entidad.setSegundoApellido(stringValueFromOptional(getStringValue(resBundle.getString("segundo_apellido"))));
        entidad.setNumeroEmpleado(stringValueFromOptional(getStringValue(resBundle.getString("numero_empleado"))));
        entidad.setCorreoElectronico(stringValueFromOptional(getStringValue(resBundle.getString("correo_electronico"))));

    }
    @Override
    public void llenarFormularioDesdeEntidad() {
        var entidadActual = getEntidad();
        if(esNulo(entidadActual)){
            entidadActual=new Persona();
            setEntidad(entidadActual);
        }
        setStringEnCampo(resBundle.getString("nombre"),entidadActual.getNombre());
        setStringEnCampo(resBundle.getString("primer_apellido"),entidadActual.getPrimerApellido());
        setStringEnCampo(resBundle.getString("segundo_apellido"),entidadActual.getSegundoApellido());
        setStringEnCampo(resBundle.getString("numero_empleado"),entidadActual.getNumeroEmpleado());
        setStringEnCampo(resBundle.getString("correo_electronico"),entidadActual.getCorreoElectronico());

    }


}
