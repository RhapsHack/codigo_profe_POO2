package mx.edu.uaz.ingsoft.poo2.vista.ventanas.gestion;


import mx.edu.uaz.ingsoft.poo2.modelo.entidades.IEntidad;
import mx.edu.uaz.ingsoft.poo2.modelo.utils.MensajesResourceBundle;
import mx.edu.uaz.ingsoft.poo2.servicios.interfaces.IServicioBase;
import mx.edu.uaz.ingsoft.poo2.vista.ventanas.actualizacion.AbstractVentanaUpdateEntidad;
import mx.edu.uaz.ingsoft.poo2.vista.ventanas.borrado.AbstractVentanaBorradoEntidad;
import mx.edu.uaz.ingsoft.poo2.vista.ventanas.captura.AbstractVentanaCapturaEntidad;
import mx.edu.uaz.ingsoft.poo2.vista.ventanas.listado.AbstractVentanaListadoEntidades;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.ResourceBundle;

import static java.awt.BorderLayout.PAGE_END;
import static java.awt.BorderLayout.CENTER;
import static mx.edu.uaz.ingsoft.poo2.utils.Constantes.*;
import static mx.edu.uaz.ingsoft.poo2.utils.FunctionUtils.noEsNulo;
import static mx.edu.uaz.ingsoft.poo2.vista.utils.SwingComponentUtils.*;
import static mx.edu.uaz.ingsoft.poo2.vista.ventanas.utils.VentanasFactory.*;



public abstract class AbstractVentanaGestionCatalogo<T extends IEntidad> extends JFrame{

    protected final ResourceBundle resBundle;
    protected final IServicioBase<T> servicio;
    protected final String nombreCatalogo;

    private AbstractVentanaListadoEntidades<T> ventanaListado;
    private AbstractVentanaCapturaEntidad<T> ventanaCapturaEntidad;
    private AbstractVentanaBorradoEntidad<T> ventanaBorradoEntidad;

    public AbstractVentanaUpdateEntidad<T> getVentanaUpdateEntidad() {
        return ventanaUpdateEntidad;
    }

    public void setVentanaUpdateEntidad(AbstractVentanaUpdateEntidad<T> ventanaUpdateEntidad) {
        this.ventanaUpdateEntidad = ventanaUpdateEntidad;
    }

    private AbstractVentanaUpdateEntidad<T> ventanaUpdateEntidad;

    protected List<JButton> listaBotones;

    protected AbstractVentanaGestionCatalogo(String tituloVentana, IServicioBase<T> servicio, String nombreCatalogo) {
        super(tituloVentana);
        this.nombreCatalogo = nombreCatalogo;
        this.servicio = servicio;
        resBundle = MensajesResourceBundle.MENSAJES.getResourceBundle();
        setDefaultCloseOperation(HIDE_ON_CLOSE);

        crearVentanaCaptura(nombreCatalogo)
                .ifPresent(ventana -> {
                    setVentanaCapturaEntidad(ventana);
                });
        crearVentanaListado(nombreCatalogo)
                .ifPresent( ventana -> {
                    setVentanaListado(ventana);
                });

        crearVentanaBorrado(nombreCatalogo)
                .ifPresent( ventana -> {
                    setVentanaBorradoEntidad(ventana);
                });

        crearVentanaUpdate(nombreCatalogo)
                .ifPresent(ventana->{
                    setVentanaUpdateEntidad(ventana);
                });
        listaBotones = crearBotones(
                resBundle.getString(BOT_LISTAR),
                resBundle.getString(BOT_AGREGAR),
                resBundle.getString(BOT_EDITAR),
                resBundle.getString(BOT_BORRAR),
                resBundle.getString(BOT_REGRESAR)
        );

        var pnlAccionesGestion = new JPanel();
        agregarComponentes(pnlAccionesGestion, listaBotones);

        var pnlAccionesVentana = new JPanel();
        getButtonWithLabel(listaBotones, resBundle.getString(BOT_REGRESAR))
                .ifPresent( boton ->{
                    agregarComponentes(pnlAccionesVentana, boton);
                    boton.addActionListener(e->accionRegresar());
                } );

        this.add(pnlAccionesGestion, CENTER);
        this.add(pnlAccionesVentana, PAGE_END);
        pack();
        setLocationRelativeTo(null);
    }

    public void addBotonListener(String nombreBoton, ActionListener listener){
        getButtonWithLabel(listaBotones, nombreBoton).ifPresent( boton -> boton.addActionListener(listener));
    }

    public void addBotonListarListener(ActionListener listener){
        addBotonListener(resBundle.getString(BOT_LISTAR),listener);
    }

    public void addBotonAgregarListener(ActionListener listener){
        addBotonListener(resBundle.getString(BOT_AGREGAR),listener);
    }

    public void addBotonEditarListener(ActionListener listener){
        addBotonListener(resBundle.getString(BOT_EDITAR),listener);
    }

    public void addBotonBorrarListener(ActionListener listener){
        addBotonListener(resBundle.getString(BOT_BORRAR),listener);
    }

    protected void accionRegresar(){
        setVisible(false);
    }

    protected void ocultarVentana(JFrame ventana){
        if(noEsNulo(ventana)){
            ventana.setVisible(false);
        }
    }

    public AbstractVentanaListadoEntidades<T> getVentanaListado() {
        return ventanaListado;
    }

    public void setVentanaListado(AbstractVentanaListadoEntidades<T> ventanaListado) {
        this.ventanaListado = ventanaListado;
    }

    public AbstractVentanaCapturaEntidad<T> getVentanaCapturaEntidad() {
        return ventanaCapturaEntidad;
    }

    public void setVentanaCapturaEntidad(AbstractVentanaCapturaEntidad<T> ventanaCapturaEntidad) {
        this.ventanaCapturaEntidad = ventanaCapturaEntidad;
    }
    public AbstractVentanaBorradoEntidad<T> getVentanaBorradoEntidad(){
        return ventanaBorradoEntidad;
    }
    public void setVentanaBorradoEntidad(AbstractVentanaBorradoEntidad<T> ventanaBorrado) {
        this.ventanaBorradoEntidad = ventanaBorrado;
    }

}
