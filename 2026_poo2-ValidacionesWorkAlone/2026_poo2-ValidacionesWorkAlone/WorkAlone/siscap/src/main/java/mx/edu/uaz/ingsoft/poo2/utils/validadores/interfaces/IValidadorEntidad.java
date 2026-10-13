package mx.edu.uaz.ingsoft.poo2.utils.validadores.interfaces;

import mx.edu.uaz.ingsoft.poo2.modelo.entidades.IEntidad;

import java.util.List;


public interface IValidadorEntidad<T extends IEntidad> {
    boolean validar (T entidad);
    void agregarValidador(String nombreCampo,  IValidador validador);
    void asignarValoresAValidadores();
    List<String> getErrores();
}
