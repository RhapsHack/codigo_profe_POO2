package mx.edu.uaz.ingsoft.poo2.servicios.interfaces;


import mx.edu.uaz.ingsoft.poo2.modelo.entidades.Bitacora;

public interface IBitacoraService extends IServicioBase<Bitacora>{
    void setNombreTabla(String nombreTabla);
    void guardarAccesoBD(String nombreAccion);
}
