package mx.edu.uaz.ingsoft.poo2.controlador;

import mx.edu.uaz.ingsoft.poo2.controlador.interfaces.IControllerBase;
import mx.edu.uaz.ingsoft.poo2.modelo.daos.DAOException;
import mx.edu.uaz.ingsoft.poo2.modelo.entidades.IEntidad;
import mx.edu.uaz.ingsoft.poo2.servicios.interfaces.IServicioBase;
import mx.edu.uaz.ingsoft.poo2.utils.validadores.interfaces.IValidadorEntidad;
import mx.edu.uaz.ingsoft.poo2.vista.ventanas.actualizacion.AbstractVentanaUpdateEntidad;
import mx.edu.uaz.ingsoft.poo2.vista.ventanas.borrado.AbstractVentanaBorradoEntidad;
import mx.edu.uaz.ingsoft.poo2.vista.ventanas.captura.AbstractVentanaCapturaEntidad;
import mx.edu.uaz.ingsoft.poo2.vista.ventanas.listado.AbstractVentanaListadoEntidades;

import javax.swing.*;
import java.util.List;

import static mx.edu.uaz.ingsoft.poo2.utils.Constantes.MSG_ERROR_VALIDACION;
import static mx.edu.uaz.ingsoft.poo2.utils.FunctionUtils.crearInstancia;


public abstract class AbstractControllerBase<T extends IEntidad,U extends IServicioBase<T>> implements IControllerBase<T> {
    protected final U servicio;
    protected final AbstractVentanaCapturaEntidad<T> ventanaCaptura;
    protected final AbstractVentanaListadoEntidades<T> ventanaListado;
    protected final AbstractVentanaBorradoEntidad<T> ventanaBorrado;
    protected final AbstractVentanaUpdateEntidad<T> ventanaUpdate;
    protected final IValidadorEntidad<T> validadorEntidad;

    protected List<T> registros;
    protected T registroActual;
    protected Class<T> clazz;


    public AbstractControllerBase(Class<T> clazz, 
                                  U servicio,
                                  AbstractVentanaCapturaEntidad<T> ventanaCaptura,
                                  AbstractVentanaListadoEntidades<T> ventanaListado,
                                  AbstractVentanaBorradoEntidad<T> ventanaBorrado,
                                  AbstractVentanaUpdateEntidad<T> ventanaUpdateEntidad,
                                  IValidadorEntidad<T> validadorEntidad){
        this.clazz = clazz;
        this.validadorEntidad = validadorEntidad;
        this.servicio = servicio;
             
        registros = servicio.recuperarTodosRegistros();
        registroActual = null;
        
        
        this.ventanaCaptura = ventanaCaptura;
        this.ventanaCaptura.setAccionAceptar(e->aceptarCaptura());
        this.ventanaCaptura.setAccionCancelar(e->cancelarCaptura(ventanaCaptura));
        
        this.ventanaListado = ventanaListado;
        this.ventanaListado.setAccionAceptar(e->aceptarListado());
        this.ventanaListado.setAccionRegresar(e->ventanaListado.setVisible(false));
        
        this.ventanaBorrado = ventanaBorrado;
        this.ventanaBorrado.setAccionRegresar(e->ventanaBorrado.setVisible(false));
        this.ventanaBorrado.setAccionAceptar(e->borrarRegistro());
        
        this.ventanaUpdate = ventanaUpdateEntidad;
        this.ventanaUpdate.setAccionAceptar(e->aceptarUpdate());
        this.ventanaUpdate.setAccionCancelar(e->cancelarCaptura(ventanaUpdate));

    }

    @Override
    public void agregarRegistro() {
        try {
            servicio.guardar(registroActual);
        } catch (DAOException e) {
            mostrarErrorPersistencia(e);
            return;
        }
        registros = servicio.recuperarTodosRegistros();
    }

    @Override
    public void actualizarRegistro() {
        try {
            servicio.actualizar(registroActual);
        } catch (DAOException e) {
            mostrarErrorPersistencia(e);
            return;
        }
        registros = servicio.recuperarTodosRegistros();
    }

    @Override
    public void borrarRegistro() {
        var seleccionado = ventanaBorrado.obtenerEntidadSeleccionada();
        if(seleccionado.isPresent()){
            registroActual = seleccionado.get();
            var respuestaConfirmacion = JOptionPane.showConfirmDialog(null,"En verdad quieres borrar el registro\n"+registroActual.toString());
            if(JOptionPane.YES_NO_OPTION == respuestaConfirmacion){
                try {
                    servicio.borrarRegistro(registroActual);
                } catch (DAOException e) {
                    mostrarErrorPersistencia(e);
                    ventanaBorrado.setVisible(false);
                    return;
                }
                registros = servicio.recuperarTodosRegistros();
                registroActual = null;

            }
        }else{
            JOptionPane.showMessageDialog(null,"No se seleccionó ningún registro");
        }
        ventanaBorrado.setVisible(false);
    }

    @Override
    public void aceptarListado() {
        registroActual = ventanaListado.obtenerEntidadSeleccionada().orElse(crearNuevoRegistro());
    }

    @Override
    public void mostrarVentanaListado() {
        ventanaListado.establecerListaEntidades(registros);
        ventanaListado.setVisible(true);
    }

    public void mostrarVentanaBorrado(){
        ventanaBorrado.establecerListaEntidades(registros);
        ventanaBorrado.setVisible(true);
    }

    @Override
    public void mostrarVentanaNuevoRegistro() {
        ventanaCaptura.setEntidad(crearNuevoRegistro());
        ventanaCaptura.setVisible(true);
    }

    public void mostrarVentanaEdicion(){
        ventanaUpdate.establecerListadoRegistros(registros);
        ventanaUpdate.setVisible(true);
    }

    @Override
    public void aceptarCaptura() {
        var registro = ventanaCaptura.getEntidadFormulario();
        if (registro.isPresent()) {
            registroActual = registro.get();
            if(validadorEntidad.validar(registroActual)){
                try {
                    servicio.guardar(registroActual);
                } catch (DAOException e) {
                    mostrarErrorPersistencia(e);
                    return;
                }
                registros = servicio.recuperarTodosRegistros();
                ventanaCaptura.limpiarFormulario();
                ventanaCaptura.setVisible(false);
            }else{
                mostrarErroresValidacion();
            }
        }
    }
    @Override
    public void aceptarUpdate() {
        var registro = ventanaUpdate.getEntidad();
        if (registro.isPresent()) {
            registroActual = registro.get();
            if(validadorEntidad.validar(registroActual)){
                try {
                    servicio.actualizar(registroActual);
                } catch (DAOException e) {
                    mostrarErrorPersistencia(e);
                    return;
                }
                registros = servicio.recuperarTodosRegistros();
                ventanaUpdate.limpiarFormulario();
                ventanaUpdate.setVisible(false);
            }else{
                mostrarErroresValidacion();
            }
        }
    }

    private void mostrarErroresValidacion() {
        JOptionPane.showMessageDialog(null,
                MSG_ERROR_VALIDACION + "\n" + String.join("\n", validadorEntidad.getErrores()));
    }

    private void mostrarErrorPersistencia(DAOException e) {
        JOptionPane.showMessageDialog(null, e.getMessage());
    }

    @Override
    public  void cancelarCaptura(AbstractVentanaCapturaEntidad<T> ventanaCapturaRegistro) {
        ventanaCapturaRegistro.limpiarFormulario();
        ventanaCapturaRegistro.setVisible(false);
    }

    public T crearNuevoRegistro() throws ExceptionInInitializerError{
        var instanciaCreada = crearInstancia(clazz);
        if(instanciaCreada.isPresent()){
            return instanciaCreada.get();
        }else{
            return null;
        }
    }

}
