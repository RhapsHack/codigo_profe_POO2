package mx.edu.uaz.ingsoft.poo2.controlador.interfaces;


import mx.edu.uaz.ingsoft.poo2.modelo.entidades.IEntidad;
import mx.edu.uaz.ingsoft.poo2.vista.ventanas.captura.AbstractVentanaCapturaEntidad;

public interface IControllerBase<T extends IEntidad> {
    void agregarRegistro();
    void actualizarRegistro();
    void borrarRegistro();
    void mostrarVentanaListado();
    void mostrarVentanaNuevoRegistro();
    void mostrarVentanaBorrado();
    void mostrarVentanaEdicion();
    void aceptarListado();
    void aceptarCaptura();
    void aceptarUpdate();
    void cancelarCaptura(AbstractVentanaCapturaEntidad<T> ventanaCapturaRegistro);
}
