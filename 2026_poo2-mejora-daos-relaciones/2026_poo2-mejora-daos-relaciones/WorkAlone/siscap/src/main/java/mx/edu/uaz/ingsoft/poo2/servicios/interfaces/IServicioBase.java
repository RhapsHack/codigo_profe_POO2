package mx.edu.uaz.ingsoft.poo2.servicios.interfaces;


import mx.edu.uaz.ingsoft.poo2.modelo.daos.DAOException;
import mx.edu.uaz.ingsoft.poo2.modelo.entidades.IEntidad;

import java.util.List;
import java.util.Optional;


public interface IServicioBase<T extends IEntidad> {
    void guardar(T unRegistro) throws DAOException;
    void guardarTodos(List<T> losRegistros) throws DAOException;
    void actualizar(T unRegistro) throws DAOException;
    void actualizarTodos(List<T> losRegistros) throws DAOException;
    Optional<T> buscarUnRegistro(Integer identificador);
    List<T> recuperarTodosRegistros();
    List<T> recuperarPagina(int pagina, int tamano);
    List<T> buscarPorCampo(String campo, Object valor);
    void borrarRegistro(Integer identificador) throws DAOException;
    void borrarRegistro(T registro) throws DAOException;
}
