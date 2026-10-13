package mx.edu.uaz.ingsoft.poo2.modelo.daos.impl;

import mx.edu.uaz.ingsoft.poo2.modelo.daos.DAOException;
import mx.edu.uaz.ingsoft.poo2.modelo.daos.interf.IBaseDAO;
import mx.edu.uaz.ingsoft.poo2.modelo.daos.interf.IDBExecute;
import mx.edu.uaz.ingsoft.poo2.modelo.daos.interf.IDBJustExecute;
import mx.edu.uaz.ingsoft.poo2.modelo.entidades.IEntidad;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

import java.io.Serializable;
import java.util.List;
import java.util.logging.Logger;

import static mx.edu.uaz.ingsoft.poo2.utils.Constantes.MSG_ERROR_PERSISTENCIA;
import static mx.edu.uaz.ingsoft.poo2.utils.FunctionUtils.noEsNulo;

public class DAOBase<T extends IEntidad> implements IBaseDAO<T>, Serializable {
    private static final long serialVersionUID = 1L;

    private EntityManager em = null;
    private final Class<T> claseEntidad;

    public static final String LOGGER_NAME = "DAOBase";
    static final Logger log = Logger.getLogger(LOGGER_NAME);

    public DAOBase(Class<T> claseEntidad, EntityManager em) {
        this.claseEntidad = claseEntidad;
        setEm(em);
    }

    public DAOBase(Class<T> claseEntidad) {
        this(claseEntidad, null);
    }

    @Override
    public T findById(Integer id) {
        return em.find(claseEntidad, id);
    }

    @Override
    public List<T> findAll() {
        return ejecutarConsulta(null, null);
    }

    @Override
    public List<T> findAll(int pagina, int tamano) {
        if (pagina < 0 || tamano <= 0) {
            throw new IllegalArgumentException(
                    "Página y tamaño inválidos: la página debe ser >= 0 y el tamaño > 0 (página="
                            + pagina + ", tamaño=" + tamano + ")");
        }
        return ejecutarConsulta(pagina, tamano);
    }

    @Override
    public List<T> findByCampo(String campo, Object valor) {
        return em.createQuery(consultaPorCampo(campo, valor)).getResultList();
    }

    @Override
    public T findByCampoUnico(String campo, Object valor) {
        List<T> resultados = em.createQuery(consultaPorCampo(campo, valor))
                .setMaxResults(1)
                .getResultList();
        return resultados.isEmpty() ? null : resultados.get(0);
    }

    private CriteriaQuery<T> consultaPorCampo(String campo, Object valor) {
        CriteriaQuery<T> criteriaQuery = em.getCriteriaBuilder().createQuery(claseEntidad);
        Root<T> raiz = criteriaQuery.from(claseEntidad);
        criteriaQuery.select(raiz)
                .where(em.getCriteriaBuilder().equal(raiz.get(campo), valor));
        return criteriaQuery;
    }

    /**
     * Única implementación de las lecturas por Criteria: con página y
     * tamaño limita el resultado; sin ellos devuelve el listado completo.
     *
     * @param pagina null cuando no se pagina
     * @param tamano null cuando no se pagina
     */
    private List<T> ejecutarConsulta(Integer pagina, Integer tamano) {
        CriteriaQuery<T> criteriaQuery = em.getCriteriaBuilder().createQuery(claseEntidad);
        criteriaQuery.select(criteriaQuery.from(claseEntidad));
        var consulta = em.createQuery(criteriaQuery);
        if (pagina != null && tamano != null) {
            consulta.setFirstResult(pagina * tamano).setMaxResults(tamano);
        }
        return consulta.getResultList();
    }

    @Override
    public T save(T entidad) throws DAOException {
        return executeTransactionWithResult(() -> {
            em.persist(entidad);
            return entidad;
        });
    }

    @Override
    public List<T> saveAll(List<T> listaEntidades) throws DAOException {
        executeTransaction(() -> {
            for (T unRegistro : listaEntidades) {
                em.persist(unRegistro);
            }
        });
        return listaEntidades;
    }

    @Override
    public T update(T entidad) throws DAOException {
        return executeTransactionWithResult(() -> em.merge(entidad));
    }

    @Override
    public List<T> updateAll(List<T> registros) throws DAOException {
        executeTransaction(() -> {
            for (T unRegistro : registros) {
                em.merge(unRegistro);
            }
        });
        return registros;
    }

    @Override
    public void delete(T entidad) throws DAOException {
        executeTransaction(() -> em.remove(entidad));
    }

    @Override
    public void deleteById(Integer id) throws DAOException {
        T entidad = findById(id);
        if (entidad != null) {
            delete(entidad);
        }
    }

    @Override
    public void deleteAll(List<T> listaRegistros) throws DAOException {
        executeTransaction(() -> {
            for (T unRegistro : listaRegistros) {
                em.remove(unRegistro);
            }
        });
    }

    private T executeTransactionWithResult(IDBExecute<T> comando) throws DAOException {
        try {
            em.getTransaction().begin();
            T salida = comando.executeDBCommand();
            em.getTransaction().commit();
            return salida;
        } catch (Exception e) {
            revertirTransaccion(e);
            throw new DAOException(MSG_ERROR_PERSISTENCIA + ": " + e.getMessage(), e);
        }
    }

    private void executeTransaction(IDBJustExecute comando) throws DAOException {
        try {
            em.getTransaction().begin();
            comando.executeDBCommand();
            em.getTransaction().commit();
        } catch (Exception e) {
            revertirTransaccion(e);
            throw new DAOException(MSG_ERROR_PERSISTENCIA + ": " + e.getMessage(), e);
        }
    }

    /**
     * Revierte la transacción en curso sin tapar el error original: si el
     * rollback también falla, solo se registra en el log y el error que se
     * comunica sigue siendo la causa real de la operación.
     */
    private void revertirTransaccion(Exception causa) {
        log.severe(causa.getMessage());
        try {
            em.getTransaction().rollback();
        } catch (Exception falloRollback) {
            log.severe("El rollback también falló: " + falloRollback.getMessage());
        }
    }

    public void setEm(EntityManager em) {
        if (noEsNulo(em)) {
            this.em = em;
        }
    }

    /**
     * Expone el {@link EntityManager} de la sesión para que los DAOs concretos
     * puedan escribir consultas propias (p. ej. Criteria con JOIN FETCH) sin
     * duplicar la gestión de transacciones del genérico.
     */
    protected EntityManager getEm() {
        return em;
    }
}