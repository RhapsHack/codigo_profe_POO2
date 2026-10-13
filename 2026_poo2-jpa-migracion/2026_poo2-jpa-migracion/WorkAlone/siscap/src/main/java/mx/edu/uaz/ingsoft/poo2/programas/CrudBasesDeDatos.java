package mx.edu.uaz.ingsoft.poo2.programas;

import jakarta.persistence.EntityManager;
import mx.edu.uaz.ingsoft.poo2.modelo.daos.impl.BaseDeDatosDAO;
import mx.edu.uaz.ingsoft.poo2.modelo.entidades.BaseDeDatos;
import mx.edu.uaz.ingsoft.poo2.modelo.utils.JPAUtil;
import mx.edu.uaz.ingsoft.poo2.utils.inout.impl.LectorDatosTexto;
import mx.edu.uaz.ingsoft.poo2.utils.inout.interfaces.ILectorDatos;
import mx.edu.uaz.ingsoft.poo2.utils.menu.impl.MenuTexto;
import mx.edu.uaz.ingsoft.poo2.utils.menu.interfaces.IMenu;

import java.util.List;

import static mx.edu.uaz.ingsoft.poo2.utils.Constantes.*;

/**
 * CRUD de bases de datos en modo texto. El menú se construye con la biblioteca
 * de menús (IMenu/MenuTexto): el programa registra opciones con VALOR y TEXTO,
 * y decide qué hacer según el valor devuelto por solicitarOpcion().
 *
 * La entrada de datos se delega en la biblioteca utils.inout (ILectorDatos): el
 * menú y las lecturas de campos comparten el mismo lector, y por tanto la misma
 * fuente de entrada.
 */
public class CrudBasesDeDatos {
    private static final EntityManager EM = JPAUtil.crearEntityManager();
    private static final ILectorDatos LECTOR = new LectorDatosTexto();
    private static final BaseDeDatosDAO BASE_DE_DATOS_DAO = new BaseDeDatosDAO(EM);

    public static void main(String[] args) {

        LECTOR.mostrarTexto(MSG_MENU_CRUD_BASES_DE_DATOS);
        IMenu menu = construirMenu();
        String opcion;
        do {
            opcion = menu.solicitarOpcion();
            switch (opcion) {
                case VALOR_OPCION_MOSTRAR_TODOS -> mostrarTodasLasBasesDeDatos();
                case VALOR_OPCION_AGREGAR -> agregarBaseDeDatos();
                case VALOR_OPCION_ACTUALIZAR -> actualizarBaseDeDatos();
                case VALOR_OPCION_ELIMINAR -> borrarBaseDeDatos();
                case IMenu.VALOR_SALIR -> LECTOR.mostrarTexto(MSG_SALIENDO_PROGRAMA);
            }
        } while (!opcion.equals(IMenu.VALOR_SALIR));
        JPAUtil.cerrar();
    }

    private static IMenu construirMenu() {
        IMenu menu = new MenuTexto(LECTOR);
        menu.setConCancelar(false);
        menu.setConSalir(true);
        menu.agregarOpcion(VALOR_OPCION_MOSTRAR_TODOS, TEXTO_MENU_MOSTRAR_TODAS_BASES_DE_DATOS);
        menu.agregarOpcion(VALOR_OPCION_AGREGAR, TEXTO_MENU_AGREGAR_BASE_DE_DATOS);
        menu.agregarOpcion(VALOR_OPCION_ACTUALIZAR, TEXTO_MENU_ACTUALIZAR_BASE_DE_DATOS);
        menu.agregarOpcion(VALOR_OPCION_ELIMINAR, TEXTO_MENU_ELIMINAR_BASE_DE_DATOS);
        return menu;
    }

    private static void borrarBaseDeDatos() {
        Long id = (long) LECTOR.leerEntero(MSG_INGRESE_ID_BASE_DE_DATOS);
        BaseDeDatos baseDeDatos = BASE_DE_DATOS_DAO.findById(id);
        if (baseDeDatos == null) {
            LECTOR.mostrarError(MSG_BASE_DE_DATOS_NO_ENCONTRADA);
            return;
        }
        BASE_DE_DATOS_DAO.deleteById(id);
        LECTOR.mostrarTexto(MSG_BASE_DE_DATOS_BORRADA);
    }

    private static void actualizarBaseDeDatos() {
        Long id = (long) LECTOR.leerEntero(MSG_INGRESE_ID_BASE_DE_DATOS);
        BaseDeDatos baseDeDatos = BASE_DE_DATOS_DAO.findById(id);
        if (baseDeDatos == null) {
            LECTOR.mostrarError(MSG_BASE_DE_DATOS_NO_ENCONTRADA);
            return;
        }
        baseDeDatos.setNombre(LECTOR.leerTexto(mensajeActualizable(MSG_INGRESE_NOMBRE_BASE_DE_DATOS, baseDeDatos.getNombre()), baseDeDatos.getNombre()));
        baseDeDatos.setModelo(LECTOR.leerTexto(mensajeActualizable(MSG_INGRESE_MODELO_BASE_DE_DATOS, baseDeDatos.getModelo()), baseDeDatos.getModelo()));
        baseDeDatos.setProveedor(LECTOR.leerTexto(mensajeActualizable(MSG_INGRESE_PROVEEDOR_BASE_DE_DATOS, baseDeDatos.getProveedor()), baseDeDatos.getProveedor()));
        baseDeDatos.setDescripcion(LECTOR.leerTexto(mensajeActualizable(MSG_INGRESE_DESCRIPCION_BASE_DE_DATOS, baseDeDatos.getDescripcion()), baseDeDatos.getDescripcion()));
        baseDeDatos.setActivo(LECTOR.leerBoolean(mensajeActualizable(MSG_INGRESE_ACTIVO_BASE_DE_DATOS, baseDeDatos.isActivo()), baseDeDatos.isActivo()));
        BASE_DE_DATOS_DAO.update(baseDeDatos);
        LECTOR.mostrarTexto(MSG_BASE_DE_DATOS_ACTUALIZADA);
    }

    private static void agregarBaseDeDatos() {
        String nombre = LECTOR.leerTexto(MSG_INGRESE_NOMBRE_BASE_DE_DATOS);
        String modelo = LECTOR.leerTexto(MSG_INGRESE_MODELO_BASE_DE_DATOS);
        String proveedor = LECTOR.leerTexto(MSG_INGRESE_PROVEEDOR_BASE_DE_DATOS);
        String descripcion = LECTOR.leerTexto(MSG_INGRESE_DESCRIPCION_BASE_DE_DATOS);
        BASE_DE_DATOS_DAO.save(new BaseDeDatos(null, ACTIVO, nombre, modelo, proveedor, descripcion));
        LECTOR.mostrarTexto(MSG_BASE_DE_DATOS_AGREGADA);
    }

    private static void mostrarTodasLasBasesDeDatos() {
        List<BaseDeDatos> basesDeDatos = BASE_DE_DATOS_DAO.findAll();
        LECTOR.mostrarTexto(MSG_ENCABEZADO_BASES_DE_DATOS);
        if (basesDeDatos.isEmpty()) {
            LECTOR.mostrarTexto(MSG_SIN_BASES_DE_DATOS);
            return;
        }
        for (BaseDeDatos baseDeDatos : basesDeDatos) {
            LECTOR.mostrarTexto(MSG_DETALLE_ID + baseDeDatos.getId()
                    + MSG_DETALLE_ACTIVO + baseDeDatos.isActivo()
                    + MSG_DETALLE_NOMBRE + baseDeDatos.getNombre()
                    + MSG_DETALLE_MODELO + baseDeDatos.getModelo()
                    + MSG_DETALLE_PROVEEDOR + baseDeDatos.getProveedor()
                    + MSG_DETALLE_DESCRIPCION + baseDeDatos.getDescripcion());
        }
    }
}
