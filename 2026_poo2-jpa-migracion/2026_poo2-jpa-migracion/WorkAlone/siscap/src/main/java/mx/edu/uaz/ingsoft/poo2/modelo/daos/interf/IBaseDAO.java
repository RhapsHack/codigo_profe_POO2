package mx.edu.uaz.ingsoft.poo2.modelo.daos.interf;

import mx.edu.uaz.ingsoft.poo2.modelo.entidades.IEntidad;

import java.util.List;

public interface IBaseDAO<T extends IEntidad> {
    T save(T registro);
    List<T> saveAll(List<T> listaEntidades);

    T findById(Long id);
    List<T> findAll();

    T update(T registro);
    List<T> updateAll(List<T> registros);

    void delete(T registro);
    void deleteById(Long id);
    void deleteAll(List<T> listaRegistros);
}