package mx.edu.uaz.ingsoft.poo2.servicios;

import mx.edu.uaz.ingsoft.poo2.modelo.daos.DAOException;
import mx.edu.uaz.ingsoft.poo2.modelo.daos.interf.IBaseDAO;
import mx.edu.uaz.ingsoft.poo2.modelo.entidades.IEntidad;

import java.util.List;

/**
 * Servicio base para las entidades del dominio de capacitaciones que usan
 * <b>borrado lógico</b>: "borrar" no elimina la fila, solo la marca como
 * inactiva, y los listados y búsquedas omiten las filas inactivas.
 *
 * <p>Se centraliza aquí para no repetir la misma lógica en los cuatro
 * servicios (temas, cursos, inscripciones y certificados) y para no alterar el
 * {@code ServicioBase} que usan los catálogos existentes.
 */
public abstract class ServicioConBorradoLogico<T extends IEntidad, U extends IBaseDAO<T>>
        extends ServicioBase<T, U> {

    protected ServicioConBorradoLogico(U dao) {
        super(dao);
    }

    @Override
    public List<T> recuperarTodosRegistros() {
        return soloActivos(dao.findAll());
    }

    @Override
    public List<T> recuperarPagina(int pagina, int tamano) {
        if (pagina < 0 || tamano <= 0) {
            return List.of();
        }
        return soloActivos(dao.findAll()).stream()
                .skip((long) pagina * tamano)
                .limit(tamano)
                .toList();
    }

    @Override
    public List<T> buscarPorCampo(String campo, Object valor) {
        return soloActivos(dao.findByCampo(campo, valor));
    }

    @Override
    public void borrarRegistro(Integer identificador) throws DAOException {
        var registro = dao.findById(identificador);
        if (registro != null) {
            marcarInactivo(registro);
        }
    }

    @Override
    public void borrarRegistro(T registro) throws DAOException {
        if (registro != null) {
            marcarInactivo(registro);
        }
    }

    private void marcarInactivo(T registro) throws DAOException {
        registro.setActivo(false);
        dao.update(registro);
    }

    private List<T> soloActivos(List<T> registros) {
        return registros.stream().filter(IEntidad::isActivo).toList();
    }
}