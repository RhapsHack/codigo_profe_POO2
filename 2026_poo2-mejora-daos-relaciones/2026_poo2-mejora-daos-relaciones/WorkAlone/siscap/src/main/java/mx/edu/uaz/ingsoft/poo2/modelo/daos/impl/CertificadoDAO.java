package mx.edu.uaz.ingsoft.poo2.modelo.daos.impl;

import jakarta.persistence.EntityManager;

import mx.edu.uaz.ingsoft.poo2.modelo.daos.interf.ICertificadoDAO;
import mx.edu.uaz.ingsoft.poo2.modelo.entidades.Certificado;
import mx.edu.uaz.ingsoft.poo2.modelo.entidades.Inscripcion;

public class CertificadoDAO extends DAOBase<Certificado> implements ICertificadoDAO {
    public CertificadoDAO(EntityManager em) {
        super(Certificado.class, em);
    }

    @Override
    public Certificado findByInscripcion(Inscripcion inscripcion) {
        return findByCampoUnico("inscripcion", inscripcion);
    }
}