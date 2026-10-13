package mx.edu.uaz.ingsoft.poo2.servicios.interfaces;


import mx.edu.uaz.ingsoft.poo2.modelo.entidades.IEntidad;

import java.util.List;
import java.util.Optional;


public interface IServicioBase<T extends IEntidad> {
    void guardar(T unRegistro);
    void guardarTodos(List<T> losRegistros);
    void actualizar(T unRegistro);
    void actualizarTodos(List<T> losRegistros);
    Optional<T> buscarUnRegistro(Integer identificador);
    List<T> recuperarTodosRegistros();
    void borrarRegistro(Integer identificador);
    void borrarRegistro(T registro);
}
