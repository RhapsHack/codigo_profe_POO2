package mx.edu.uaz.ingsoft.poo2.vista.ventanas.listado;


import mx.edu.uaz.ingsoft.poo2.modelo.entidades.IEntidad;
import mx.edu.uaz.ingsoft.poo2.modelo.utils.MensajesResourceBundle;
import mx.edu.uaz.ingsoft.poo2.vista.paneles.PanelListEntidades;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import static mx.edu.uaz.ingsoft.poo2.vista.utils.SwingComponentUtils.*;

public abstract class AbstractVentanaListadoEntidades<T extends IEntidad> extends JFrame {

    private List<T> entidades;
    protected List<JButton> listaBotones;
    protected ResourceBundle resBoundle;
    protected PanelListEntidades<T> panelListEntidades;
    protected JPanel panelBotones;

    public AbstractVentanaListadoEntidades(String title, String... etiquetasBotones) {
        super(title);
        configurarValoresPorDefectoVentana();
        inicializarCampos();
        crearSeccionListadoEntidades();
        crearBotonesListado(etiquetasBotones);
        agregarComponentes(panelBotones, listaBotones);
        add(panelBotones, BorderLayout.PAGE_END);
        setLocationRelativeTo(null);
    }
    public AbstractVentanaListadoEntidades(String title) {
        this(title,"Regresar");
        setActionListenerToButton(listaBotones,"Regresar",e->this.setVisible(false));
    }
    private void inicializarCampos(){
        resBoundle = MensajesResourceBundle.MENSAJES.getResourceBundle();
        entidades = new ArrayList<>();
        panelBotones = new JPanel();
    }
    
    private void configurarValoresPorDefectoVentana(){
        setSize(300,200);
        setResizable(true);
    }
    
    private void crearSeccionListadoEntidades(){
        panelListEntidades = new PanelListEntidades<>(entidades);
        add(panelListEntidades,BorderLayout.CENTER);
    }

    private void crearBotonesListado(String[] etiquetas){
        listaBotones = crearBotones(etiquetas);
    }

    public void establecerListaEntidades(List<T> nuevaLista){
        panelListEntidades.establecerListaEntidades(nuevaLista);
    }
    public Optional<T> obtenerEntidadSeleccionada() {
    	return  panelListEntidades.getItemSelected();
    	
    }
    public List<T> obtenerEntidadesSeleccionadas(){
    	return panelListEntidades.getItemsSelected();
    }

    /**
     * Establece la acción a realizar cuando se presiona el botón con funcioanlidad
     * equivalente a "Cancelar" o "Regresar".
     * De la lista de botones, debe ser el último.

     * @param listener
     */
    public void setAccionRegresar(ActionListener listener) {
        if(listaBotones.size()>=1) {
            var botonRegresar = listaBotones.get(listaBotones.size() - 1);
            botonRegresar.addActionListener(listener);
        }
    }
    /**
     * Establece la acción a realizar cuando se presiona el botón con funcioanlidad
     * equivalente a "Aceptar".
     * De la lista de botones, debe ser el penúltimo.

     * @param listener
     */
    public void setAccionAceptar(ActionListener listener) {
        if( listaBotones.size() >= 2 ) {
            var botonAceptar = listaBotones.get(listaBotones.size() - 2);
            botonAceptar.addActionListener(listener);
        }
    }
}
