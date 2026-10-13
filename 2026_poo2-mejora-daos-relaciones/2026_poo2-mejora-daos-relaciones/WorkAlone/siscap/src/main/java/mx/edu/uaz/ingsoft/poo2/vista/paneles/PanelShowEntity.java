package mx.edu.uaz.ingsoft.poo2.vista.paneles;



import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.util.*;
import java.util.logging.Logger;

import mx.edu.uaz.ingsoft.poo2.modelo.entidades.IEntidad;
import mx.edu.uaz.ingsoft.poo2.modelo.utils.MensajesResourceBundle;
import mx.edu.uaz.ingsoft.poo2.utils.Constantes;

import static mx.edu.uaz.ingsoft.poo2.utils.FunctionUtils.*;
import static mx.edu.uaz.ingsoft.poo2.vista.ventanas.utils.PanelsFactory.createLabelComboPanel;
import static mx.edu.uaz.ingsoft.poo2.vista.ventanas.utils.PanelsFactory.createLabelTextPanel;


public abstract class PanelShowEntity <T extends IEntidad> extends JPanel {

    protected T entidad;
    protected final boolean isHorizontalLayout;
    protected final Map<String, JTextComponent> campos;
    protected final Map<String, JComboBox<String>> listas;
    private int colsCampoTexto;
    private int rensCampoTexto;
    protected final ResourceBundle resBundle;
    public PanelShowEntity() {
        this(true);
    }
    public PanelShowEntity(boolean isHorizontalLayout) {
        setLayout(new BoxLayout(this,BoxLayout.PAGE_AXIS));
        resBundle = MensajesResourceBundle.MENSAJES.getResourceBundle();
        campos = new HashMap<>();
        listas = new HashMap<>();
        colsCampoTexto = Constantes.NUM_COLS_TEXTFIELD;
        rensCampoTexto = Constantes.NUM_RENS_TEXTAREA;
        this.isHorizontalLayout = isHorizontalLayout;
        construirFormulario();
    }
    public void agregarListaSeleccion(String etiqueta, List<? extends IEntidad> listaDatos){
        String[] textos;
        if(esNulo(listaDatos)){
            textos = new String[0];
        }else{
            textos = listaToArray(listaDatos);
        }
        var comboLista = new JComboBox<String>(textos);
        var panelCombo = createLabelComboPanel(etiqueta,comboLista);
        listas.put(etiqueta,comboLista);
        add(panelCombo);
    }
    public List<String> getElementosSeleccionadosLista(String etiqueta){
        var componenteCombo = listas.get(etiqueta);
        var seleccionados = componenteCombo.getSelectedObjects();
        return Arrays.stream(seleccionados).map( elemento -> (String)elemento).toList();
    }
    public void agregarCampoTexto(String etiqueta){
        var campoTexto = new JTextField(Constantes.NUM_COLS_TEXTFIELD);
        var temporal = createLabelTextPanel(etiqueta,campoTexto);
        campos.put(etiqueta,campoTexto);
        add(temporal);
    }
    public void agregarCampoNumerico(String etiqueta){
        var campoNumerico = new JFormattedTextField();
        campoNumerico.setValue(Double.valueOf(0));
        campoNumerico.setColumns(Constantes.NUM_COLS_TEXTFIELD);
        var temporal = createLabelTextPanel(etiqueta,campoNumerico);
        campos.put(etiqueta,campoNumerico);
        add(temporal);
    }
    public void setStringEnCampo(String nombreCampo, String texto){
        var campo = campos.get(nombreCampo);
        if(noEsNulo(campo)){
            campo.setText(texto);
        }
    }
    public void setNumeroEnCampo(String nombreCampo, Number numero){
        var campo = campos.get(nombreCampo);
        if(noEsNulo(campo) && noEsNulo(numero)){
            campo.setText(numero.toString());
        }
    }
    public void seleccionarElementoEnLista(String nombreLista, String textoEntidad){
        var listaASeleccionar = listas.get(nombreLista);
        int indiceElemento;
        for(indiceElemento=0;indiceElemento<listaASeleccionar.getItemCount();indiceElemento++){
            if(listaASeleccionar.getItemAt(indiceElemento).equals(textoEntidad)){
                listaASeleccionar.setSelectedIndex(indiceElemento);
                return;
            }
        }
        listaASeleccionar.setSelectedItem(null);
    }
    public Optional<String> getStringValue(String nombreCampo){
        var campoTexto = campos.get(nombreCampo);
        if(esNulo(campoTexto)){
            return Optional.empty();
        }
        return Optional.of(campoTexto.getText());
    }
    public Optional<Double> getDouble(String nombreCampo){
        var textoCampo = getStringValue(nombreCampo);
        Double valorSalida = 0.0;
        if(textoCampo.isPresent()){
            var textoNumero = textoCampo.get().replace(",","");
            try {
                valorSalida = Double.valueOf(textoNumero);
            } catch (NumberFormatException exception) {
                Logger.getAnonymousLogger().severe(resBundle.getString("error.convertir.numero"));
                Logger.getAnonymousLogger().severe(exception.getMessage());
            }

        }
        return Optional.of(valorSalida);
    }
    public Optional<Integer> getInteger(String nombreCampo){
        var valorDoble = getDouble(nombreCampo);
        if(valorDoble.isPresent()){
            return Optional.of(valorDoble.get().intValue());
        }
        return Optional.empty();
    }
    public T getEntidad() {
        return entidad;
    }
    public T getEntidadFromFormulario(){
        crearEntidadDesdeFormulario();
        return entidad;
    }
    public void setEntidad(T entidad) {
        this.entidad = entidad;
        llenarFormularioDesdeEntidad();
    }
    public int getColsCampoTexto() {
        return colsCampoTexto;
    }
    public int getRensCampoTexto() {
        return rensCampoTexto;
    }
    public void setColumnasCampoTexto(int colsCampoTexto) {
        this.colsCampoTexto = colsCampoTexto;
    }
    public void setRenglonessCampoTexto(int rensCampoTexto) {
        this.rensCampoTexto = rensCampoTexto;
    }
    public void setEditable(boolean editable) {
        var camposTexto= campos.values();
        for(var unCampoTexto:camposTexto){
            unCampoTexto.setEditable(editable);
        }
    }
    public abstract void construirFormulario();
    public abstract void limpiarFormulario();
    public abstract T crearEntidadDesdeFormulario();
    public abstract void llenarFormularioDesdeEntidad();
    public abstract void actualizarEntidadDesdeFormulario (T entidad);
}
