package mx.edu.uaz.ingsoft.poo2.programas.cruds;

import jakarta.persistence.EntityManager;
import mx.edu.uaz.ingsoft.poo2.modelo.daos.impl.HerramientaDAO;
import mx.edu.uaz.ingsoft.poo2.modelo.entidades.Herramienta;
import mx.edu.uaz.ingsoft.poo2.modelo.utils.JPAUtil;
import mx.edu.uaz.ingsoft.poo2.utils.inout.impl.LectorDatosTexto;
import mx.edu.uaz.ingsoft.poo2.utils.inout.interfaces.ILectorDatos;
import mx.edu.uaz.ingsoft.poo2.utils.menu.impl.MenuTexto;
import mx.edu.uaz.ingsoft.poo2.utils.menu.interfaces.IMenu;

import java.util.List;

import static mx.edu.uaz.ingsoft.poo2.utils.Constantes.*;

/**
 * CRUD de herramientas en modo texto. El menú se construye con la biblioteca
 * de menús (IMenu/MenuTexto): el programa registra opciones con VALOR y TEXTO,
 * y decide qué hacer según el valor devuelto por solicitarOpcion().
 *
 * La entrada de datos se delega en la biblioteca utils.inout (ILectorDatos): el
 * menú y las lecturas de campos comparten el mismo lector, y por tanto la misma
 * fuente de entrada.
 */
public class CrudHerramientas {
    private static final EntityManager EM = JPAUtil.crearEntityManager();
    private static final ILectorDatos LECTOR = new LectorDatosTexto();
    private static final HerramientaDAO HERRAMIENTA_DAO = new HerramientaDAO(EM);

    public static void main(String[] args) {

        LECTOR.mostrarTexto(MSG_MENU_CRUD_HERRAMIENTAS);
        IMenu menu = construirMenu();
        String opcion;
        do {
            opcion = menu.solicitarOpcion();
            switch (opcion) {
                case VALOR_OPCION_MOSTRAR_TODOS -> mostrarTodasLasHerramientas();
                case VALOR_OPCION_AGREGAR -> agregarHerramienta();
                case VALOR_OPCION_ACTUALIZAR -> actualizarHerramienta();
                case VALOR_OPCION_ELIMINAR -> borrarHerramienta();
                case IMenu.VALOR_SALIR -> LECTOR.mostrarTexto(MSG_SALIENDO_PROGRAMA);
            }
        } while (!opcion.equals(IMenu.VALOR_SALIR));
        JPAUtil.cerrar();
    }

    private static IMenu construirMenu() {
        IMenu menu = new MenuTexto(LECTOR);
        menu.setConCancelar(false);
        menu.setConSalir(true);
        menu.agregarOpcion(VALOR_OPCION_MOSTRAR_TODOS, TEXTO_MENU_MOSTRAR_TODAS_HERRAMIENTAS);
        menu.agregarOpcion(VALOR_OPCION_AGREGAR, TEXTO_MENU_AGREGAR_HERRAMIENTA);
        menu.agregarOpcion(VALOR_OPCION_ACTUALIZAR, TEXTO_MENU_ACTUALIZAR_HERRAMIENTA);
        menu.agregarOpcion(VALOR_OPCION_ELIMINAR, TEXTO_MENU_ELIMINAR_HERRAMIENTA);
        return menu;
    }

    private static void borrarHerramienta() {
        Integer id = LECTOR.leerEntero(MSG_INGRESE_ID_HERRAMIENTA);
        Herramienta herramienta = HERRAMIENTA_DAO.findById(id);
        if (herramienta == null) {
            LECTOR.mostrarError(MSG_HERRAMIENTA_NO_ENCONTRADA);
            return;
        }
        HERRAMIENTA_DAO.deleteById(id);
        LECTOR.mostrarTexto(MSG_HERRAMIENTA_BORRADA);
    }

    private static void actualizarHerramienta() {
        Integer id = LECTOR.leerEntero(MSG_INGRESE_ID_HERRAMIENTA);
        Herramienta herramienta = HERRAMIENTA_DAO.findById(id);
        if (herramienta == null) {
            LECTOR.mostrarError(MSG_HERRAMIENTA_NO_ENCONTRADA);
            return;
        }
        herramienta.setNombre(LECTOR.leerTexto(mensajeActualizable(MSG_INGRESE_NOMBRE_HERRAMIENTA, herramienta.getNombre()), herramienta.getNombre()));
        herramienta.setTipo(LECTOR.leerTexto(mensajeActualizable(MSG_INGRESE_TIPO_HERRAMIENTA, herramienta.getTipo()), herramienta.getTipo()));
        herramienta.setProveedor(LECTOR.leerTexto(mensajeActualizable(MSG_INGRESE_PROVEEDOR_HERRAMIENTA, herramienta.getProveedor()), herramienta.getProveedor()));
        herramienta.setDescripcion(LECTOR.leerTexto(mensajeActualizable(MSG_INGRESE_DESCRIPCION_HERRAMIENTA, herramienta.getDescripcion()), herramienta.getDescripcion()));
        herramienta.setActivo(LECTOR.leerBoolean(mensajeActualizable(MSG_INGRESE_ACTIVO_HERRAMIENTA, herramienta.isActivo()), herramienta.isActivo()));
        HERRAMIENTA_DAO.update(herramienta);
        LECTOR.mostrarTexto(MSG_HERRAMIENTA_ACTUALIZADA);
    }

    private static void agregarHerramienta() {
        String nombre = LECTOR.leerTexto(MSG_INGRESE_NOMBRE_HERRAMIENTA);
        String tipo = LECTOR.leerTexto(MSG_INGRESE_TIPO_HERRAMIENTA);
        String proveedor = LECTOR.leerTexto(MSG_INGRESE_PROVEEDOR_HERRAMIENTA);
        String descripcion = LECTOR.leerTexto(MSG_INGRESE_DESCRIPCION_HERRAMIENTA);
        HERRAMIENTA_DAO.save(new Herramienta(null, ACTIVO, nombre, tipo, proveedor, descripcion));
        LECTOR.mostrarTexto(MSG_HERRAMIENTA_AGREGADA);
    }

    private static void mostrarTodasLasHerramientas() {
        List<Herramienta> herramientas = HERRAMIENTA_DAO.findAll();
        LECTOR.mostrarTexto(MSG_ENCABEZADO_HERRAMIENTAS);
        if (herramientas.isEmpty()) {
            LECTOR.mostrarTexto(MSG_SIN_HERRAMIENTAS);
            return;
        }
        for (Herramienta herramienta : herramientas) {
            LECTOR.mostrarTexto(MSG_DETALLE_ID + herramienta.getId()
                    + MSG_DETALLE_ACTIVO + herramienta.isActivo()
                    + MSG_DETALLE_NOMBRE + herramienta.getNombre()
                    + MSG_DETALLE_TIPO + herramienta.getTipo()
                    + MSG_DETALLE_PROVEEDOR + herramienta.getProveedor()
                    + MSG_DETALLE_DESCRIPCION + herramienta.getDescripcion());
        }
    }
}
