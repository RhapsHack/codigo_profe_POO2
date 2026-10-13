package mx.edu.uaz.ingsoft.poo2.vista.paneles;


import mx.edu.uaz.ingsoft.poo2.modelo.entidades.Framework;

import static mx.edu.uaz.ingsoft.poo2.utils.Constantes.TXT_VACIO;
import static mx.edu.uaz.ingsoft.poo2.utils.FunctionUtils.esNulo;
import static mx.edu.uaz.ingsoft.poo2.utils.FunctionUtils.stringValueFromOptional;

public class PanelShowFramework extends PanelShowEntity<Framework> {
    /*

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "tipo")
    private String tipo;

    @Column(name = "lenguaje_base")
    private String lenguajeBase;

    @Column(name = "descripcion")
    private String descripcion;
     */

    public PanelShowFramework() {
        super();
    }

    @Override
    public void construirFormulario() {
        agregarCampoTexto(resBundle.getString("nombre"));
        agregarCampoTexto(resBundle.getString("descripcion"));
        agregarCampoTexto(resBundle.getString("lenguaje_base"));
        agregarCampoTexto(resBundle.getString("tipo"));
    }

    @Override
    public void limpiarFormulario() {
        campos.values().forEach(unCampoTexto -> {unCampoTexto.setText(TXT_VACIO); });
    }

    @Override
    public Framework crearEntidadDesdeFormulario() {
        entidad = new Framework();
        actualizarEntidadDesdeFormulario(entidad);
        return entidad;
    }
    @Override
    public void actualizarEntidadDesdeFormulario(Framework entidad) {
        entidad.setNombre(stringValueFromOptional(getStringValue(resBundle.getString("nombre"))));
        entidad.setDescripcion(stringValueFromOptional(getStringValue(resBundle.getString("descripcion"))));
        entidad.setTipo(stringValueFromOptional(getStringValue(resBundle.getString("tipo"))));
        entidad.setLenguajeBase(stringValueFromOptional(getStringValue(resBundle.getString("lenguaje_base"))));
    }
    @Override
    public void llenarFormularioDesdeEntidad() {
        var entidadActual = getEntidad();
        if(esNulo(entidadActual)){
            entidadActual=new Framework();
            setEntidad(entidadActual);
        }
        setStringEnCampo(resBundle.getString("nombre"),entidadActual.getNombre());
        setStringEnCampo(resBundle.getString("descripcion"),entidadActual.getDescripcion());
        setStringEnCampo(resBundle.getString("tipo"),entidadActual.getTipo());
        setStringEnCampo(resBundle.getString("lenguaje_base"),entidadActual.getLenguajeBase());
    }


}
