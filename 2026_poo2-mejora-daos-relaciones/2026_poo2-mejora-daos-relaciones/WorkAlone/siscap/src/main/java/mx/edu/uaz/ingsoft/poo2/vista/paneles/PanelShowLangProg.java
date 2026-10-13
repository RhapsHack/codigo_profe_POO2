package mx.edu.uaz.ingsoft.poo2.vista.paneles;


import mx.edu.uaz.ingsoft.poo2.modelo.entidades.ProgLanguages;

import static mx.edu.uaz.ingsoft.poo2.utils.Constantes.TXT_VACIO;
import static mx.edu.uaz.ingsoft.poo2.utils.FunctionUtils.esNulo;
import static mx.edu.uaz.ingsoft.poo2.utils.FunctionUtils.stringValueFromOptional;

public class PanelShowLangProg extends PanelShowEntity<ProgLanguages> {

    /*
    @Column(name = "nombre")
    private String nombre;

    @Column(name = "paradigma")
    private String paradigma;

    @Column(name = "version_referencia")
    private String versionReferencia;

    @Column(name = "descripcion")
    private String descripcion;
     */

    public PanelShowLangProg() {
        super();
    }

    @Override
    public void construirFormulario() {
        agregarCampoTexto(resBundle.getString("nombre"));
        agregarCampoTexto(resBundle.getString("paradigma"));
        agregarCampoTexto(resBundle.getString("descripcion"));
        agregarCampoTexto(resBundle.getString("version_referencia"));
    }

    @Override
    public void limpiarFormulario() {
        campos.values().forEach(unCampoTexto -> {unCampoTexto.setText(TXT_VACIO); });
    }


    @Override
    public ProgLanguages crearEntidadDesdeFormulario() {
        entidad = new ProgLanguages();
        actualizarEntidadDesdeFormulario(entidad);
        return entidad;
    }

    @Override
    public void actualizarEntidadDesdeFormulario(ProgLanguages entidad) {
        entidad.setNombre(stringValueFromOptional(getStringValue(resBundle.getString("nombre"))));
        entidad.setParadigma(stringValueFromOptional(getStringValue(resBundle.getString("paradigma"))));
        entidad.setDescripcion(stringValueFromOptional(getStringValue(resBundle.getString("descripcion"))));
        entidad.setVersionReferencia(stringValueFromOptional(getStringValue(resBundle.getString("version_referencia"))));
    }

    @Override
    public void llenarFormularioDesdeEntidad() {
        var entidadActual = getEntidad();
        if(esNulo(entidadActual)){
            entidadActual=new ProgLanguages();
            setEntidad(entidadActual);
        }
        setStringEnCampo(resBundle.getString("nombre"),entidadActual.getNombre());
        setStringEnCampo(resBundle.getString("paradigma"),entidadActual.getParadigma());
        setStringEnCampo(resBundle.getString("descripcion"),entidadActual.getDescripcion());
        setStringEnCampo(resBundle.getString("version_referencia"),entidadActual.getVersionReferencia());
    }


}
