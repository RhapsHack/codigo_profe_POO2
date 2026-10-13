package mx.edu.uaz.ingsoft.poo2.modelo.daos.interf;

import mx.edu.uaz.ingsoft.poo2.modelo.daos.DAOException;
import mx.edu.uaz.ingsoft.poo2.modelo.entidades.IEntidad;

import java.util.List;

public interface IBaseDAO<T extends IEntidad> {
    T save(T registro) throws DAOException;
    List<T> saveAll(List<T> listaEntidades) throws DAOException;

    T findById(Integer id);
    List<T> findAll();
    List<T> findAll(int pagina, int tamano);
    List<T> findByCampo(String campo, Object valor);
    T findByCampoUnico(String campo, Object valor);

    T update(T registro) throws DAOException;
    List<T> updateAll(List<T> registros) throws DAOException;

    void delete(T registro) throws DAOException;
    void deleteById(Integer id) throws DAOException;
    void deleteAll(List<T> listaRegistros) throws DAOException;
}
