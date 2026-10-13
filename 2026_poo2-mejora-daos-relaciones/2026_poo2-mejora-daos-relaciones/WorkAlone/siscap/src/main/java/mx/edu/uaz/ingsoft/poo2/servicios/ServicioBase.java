package mx.edu.uaz.ingsoft.poo2.servicios;


import mx.edu.uaz.ingsoft.poo2.modelo.daos.DAOException;
import mx.edu.uaz.ingsoft.poo2.modelo.daos.interf.IBaseDAO;
import mx.edu.uaz.ingsoft.poo2.modelo.entidades.IEntidad;
import mx.edu.uaz.ingsoft.poo2.servicios.interfaces.IServicioBase;

import java.util.List;
import java.util.Optional;



public abstract class ServicioBase<T extends IEntidad, U extends IBaseDAO<T>> implements IServicioBase<T> {
    protected final U dao;

    public ServicioBase(U dao) {
        this.dao = dao;
    }

    @Override
    public void guardar(T unRegistro) throws DAOException {
        dao.save(unRegistro);
    }
    @Override
    public void guardarTodos(List<T> losRegistros) throws DAOException {
        for (T unRegistro : losRegistros) {
            dao.save(unRegistro);
        }
    }
    @Override
    public void actualizar(T unRegistro) throws DAOException {
        dao.update(unRegistro);
    }
    @Override
    public void actualizarTodos(List<T> losRegistros) throws DAOException {
        for (T unRegistro : losRegistros) {
            dao.update(unRegistro);
        }
    }
    @Override
    public Optional<T> buscarUnRegistro(Integer identificador) {
        return Optional.ofNullable(dao.findById(identificador));
    }
    @Override
    public List<T> recuperarTodosRegistros() {
        return dao.findAll();
    }
    @Override
    public List<T> recuperarPagina(int pagina, int tamano) {
        return dao.findAll(pagina, tamano);
    }
    @Override
    public List<T> buscarPorCampo(String campo, Object valor) {
        return dao.findByCampo(campo, valor);
    }
    @Override
    public void borrarRegistro(Integer identificador) throws DAOException {
        dao.deleteById(identificador);
    }
    @Override
    public void borrarRegistro(T registro) throws DAOException {
        dao.delete(registro);
    }

}
