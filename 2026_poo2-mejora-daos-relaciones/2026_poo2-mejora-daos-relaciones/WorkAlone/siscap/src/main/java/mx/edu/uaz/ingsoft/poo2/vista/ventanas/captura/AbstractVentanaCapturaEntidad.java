package mx.edu.uaz.ingsoft.poo2.vista.ventanas.captura;



import mx.edu.uaz.ingsoft.poo2.modelo.entidades.IEntidad;
import mx.edu.uaz.ingsoft.poo2.modelo.utils.MensajesResourceBundle;
import mx.edu.uaz.ingsoft.poo2.vista.paneles.PanelShowEntity;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.Optional;
import java.util.ResourceBundle;

import static mx.edu.uaz.ingsoft.poo2.utils.Constantes.*;
import static mx.edu.uaz.ingsoft.poo2.utils.FunctionUtils.esNulo;
import static mx.edu.uaz.ingsoft.poo2.utils.FunctionUtils.noEsNulo;
import static mx.edu.uaz.ingsoft.poo2.vista.utils.SwingComponentUtils.*;
import static mx.edu.uaz.ingsoft.poo2.vista.ventanas.utils.EntityPanelFactory.createPanelShowEntity;


public abstract class AbstractVentanaCapturaEntidad<T extends IEntidad> extends JFrame {

    protected ResourceBundle resBundle;

    private JButton botLimpiar,botAceptar,botCancelar;
    private PanelShowEntity<T> pnlEntidad;

    public AbstractVentanaCapturaEntidad(String title, String nombreCatalogo) {
        super(title);
        resBundle = MensajesResourceBundle.MENSAJES.getResourceBundle();
        botLimpiar = new JButton(resBundle.getString(BOT_LIMPIAR));
        botCancelar = new JButton(resBundle.getString(BOT_CANCELAR));
        botAceptar = new JButton(resBundle.getString(BOT_ACEPTAR));
        botLimpiar.addActionListener(e-> limpiarFormulario());

        var pnlBotones = new JPanel();
        agregarComponentes(pnlBotones,botLimpiar,botAceptar,botCancelar);
        add(pnlBotones,BorderLayout.PAGE_END);

        createPanelShowEntity(nombreCatalogo).ifPresent( nuevoPanel->{
            pnlEntidad=nuevoPanel;
            add(pnlEntidad,BorderLayout.CENTER);
        });

        pack();
        setLocationRelativeTo(null);
    }
    public void setEntidad(T entidad){
        if(noEsNulo(pnlEntidad)){
            pnlEntidad.setEntidad(entidad);
        }
    }
    public Optional<T> getEntidadActual(){
        if(esNulo(pnlEntidad)){
            return Optional.empty();
        }
        return Optional.ofNullable(pnlEntidad.getEntidad());
    }

    public Optional<T> getEntidadFormulario(){
        if(esNulo(pnlEntidad)){
            return Optional.empty();
        }
        return Optional.ofNullable(pnlEntidad.getEntidadFromFormulario());
    }
    protected Optional<PanelShowEntity<T>> getPanelMostrarEntidad(){
        return Optional.ofNullable(pnlEntidad);
    }
    public void limpiarFormulario(){
        pnlEntidad.limpiarFormulario();
    }
    public void setAccionCancelar(ActionListener accionCancelar){
        botCancelar.addActionListener(accionCancelar);
    }
    public void setAccionAceptar(ActionListener accionAceptar){
        botAceptar.addActionListener(accionAceptar);
    }

}
