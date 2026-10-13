package mx.edu.uaz.ingsoft.poo2.modelo.daos.impl;

import mx.edu.uaz.ingsoft.poo2.modelo.daos.interf.IBaseDAO;
import mx.edu.uaz.ingsoft.poo2.modelo.daos.interf.IDBExecute;
import mx.edu.uaz.ingsoft.poo2.modelo.daos.interf.IDBJustExecute;
import mx.edu.uaz.ingsoft.poo2.modelo.entidades.IEntidad;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaQuery;

import java.io.Serializable;
import java.util.List;
import java.util.logging.Logger;

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
    public T findById(Long id) {
        return em.find(claseEntidad, id);
    }

    @Override
    public List<T> findAll() {
        CriteriaQuery<T> criteriaQuery = em.getCriteriaBuilder().createQuery(claseEntidad);
        criteriaQuery.select(criteriaQuery.from(claseEntidad));
        return em.createQuery(criteriaQuery).getResultList();
    }

    @Override
    public T save(T entidad) {
        return executeTransactionWithResult(() -> {
            em.persist(entidad);
            return entidad;
        });
    }

    @Override
    public List<T> saveAll(List<T> listaEntidades) {
        executeTransaction(() -> {
            for (T unRegistro : listaEntidades) {
                em.persist(unRegistro);
            }
        });
        return listaEntidades;
    }

    @Override
    public T update(T entidad) {
        return executeTransactionWithResult(() -> em.merge(entidad));
    }

    @Override
    public List<T> updateAll(List<T> registros) {
        executeTransaction(() -> {
            for (T unRegistro : registros) {
                em.merge(unRegistro);
            }
        });
        return registros;
    }

    @Override
    public void delete(T entidad) {
        executeTransaction(() -> em.remove(entidad));
    }

    @Override
    public void deleteById(Long id) {
        T entidad = findById(id);
        if (entidad != null) {
            delete(entidad);
        }
    }

    @Override
    public void deleteAll(List<T> listaRegistros) {
        executeTransaction(() -> {
            for (T unRegistro : listaRegistros) {
                em.remove(unRegistro);
            }
        });
    }

    private T executeTransactionWithResult(IDBExecute<T> comando) {
        try {
            em.getTransaction().begin();
            T salida = comando.executeDBCommand();
            em.getTransaction().commit();
            return salida;
        } catch (Exception e) {
            log.severe(e.getMessage());
            em.getTransaction().rollback();
            return null;
        }
    }

    private Boolean executeTransaction(IDBJustExecute comando) {
        try {
            em.getTransaction().begin();
            comando.executeDBCommand();
            em.getTransaction().commit();
            return true;
        } catch (Exception e) {
            log.severe(e.getMessage());
            em.getTransaction().rollback();
            return false;
        }
    }

    public void setEm(EntityManager em) {
        if (noEsNulo(em)) {
            this.em = em;
        }
    }
}