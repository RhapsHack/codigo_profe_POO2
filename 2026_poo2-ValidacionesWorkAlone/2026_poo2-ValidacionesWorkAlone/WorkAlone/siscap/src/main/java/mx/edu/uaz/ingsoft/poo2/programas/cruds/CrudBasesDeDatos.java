package mx.edu.uaz.ingsoft.poo2.programas.cruds;


import mx.edu.uaz.ingsoft.poo2.modelo.daos.impl.BaseDeDatosDAO;
import mx.edu.uaz.ingsoft.poo2.modelo.entidades.BaseDeDatos;

import static mx.edu.uaz.ingsoft.poo2.utils.Constantes.*;


public class CrudBasesDeDatos  extends AbstractCrudEntidad<BaseDeDatos> {
    public CrudBasesDeDatos(){
        super(new BaseDeDatosDAO(EM));
    }

    @Override
    public void mostrarEntidad(BaseDeDatos entidad) {
        LECTOR.mostrarTexto("ID" + entidad.getId()
        + "Activo:" + entidad.isActivo()
        + "Nombre:" + entidad.getNombre()
        + "Modelo:" + entidad.getModelo()
        + "Proveedor:" + entidad.getProveedor()
        + "Descripción" + entidad.getDescripcion());
    }

    @Override
    public void pedirDatosEntidad(BaseDeDatos e) {

        LECTOR.mostrarTexto("Ingresa el nuevo valor de los campos (solo presiona Enter para mantener el valor actual)");

        e.setNombre(leerCampoActualizable("Nombre", e.getNombre()));
        e.setModelo(leerCampoActualizable("Modelo", e.getModelo()));
        e.setProveedor(leerCampoActualizable("Proveedor", e.getProveedor()));
        e.setDescripcion(leerCampoActualizable("Descripción", e.getDescripcion()));
        //e.setActivo(LECTOR.leerBoolean(mensajeActualizable("Activo", e.isActivo()), e.isActivo()));
        dao.update(e);
        LECTOR.mostrarTexto(MSG_BASE_DE_DATOS_ACTUALIZADA);
    }

    @Override
    public BaseDeDatos pedirDatosEntidad() {
        BaseDeDatos registro = new BaseDeDatos();
        String nombre = LECTOR.leerTexto(MSG_INGRESE_NOMBRE_BASE_DE_DATOS);
        String modelo = LECTOR.leerTexto(MSG_INGRESE_MODELO_BASE_DE_DATOS);
        String proveedor = LECTOR.leerTexto(MSG_INGRESE_PROVEEDOR_BASE_DE_DATOS);
        String descripcion = LECTOR.leerTexto(MSG_INGRESE_DESCRIPCION_BASE_DE_DATOS);
        return new BaseDeDatos(null, ACTIVO, nombre, modelo, proveedor, descripcion);
    }
}
