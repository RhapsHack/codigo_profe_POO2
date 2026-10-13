package mx.edu.uaz.ingsoft.poo2.programas;

import jakarta.persistence.EntityManager;
import mx.edu.uaz.ingsoft.poo2.modelo.daos.impl.SistemaOperativoDAO;
import mx.edu.uaz.ingsoft.poo2.modelo.entidades.SistemaOperativo;
import mx.edu.uaz.ingsoft.poo2.modelo.utils.JPAUtil;
import mx.edu.uaz.ingsoft.poo2.utils.inout.impl.LectorDatosTexto;
import mx.edu.uaz.ingsoft.poo2.utils.inout.interfaces.ILectorDatos;
import mx.edu.uaz.ingsoft.poo2.utils.menu.impl.MenuTexto;
import mx.edu.uaz.ingsoft.poo2.utils.menu.interfaces.IMenu;

import java.util.List;

import static mx.edu.uaz.ingsoft.poo2.utils.Constantes.*;

/**
 * CRUD de sistemas operativos en modo texto. El menú se construye con la
 * biblioteca de menús (IMenu/MenuTexto): el programa registra opciones con
 * VALOR y TEXTO, y decide qué hacer según el valor devuelto por
 * solicitarOpcion().
 *
 * La entrada de datos se delega en la biblioteca utils.inout (ILectorDatos): el
 * menú y las lecturas de campos comparten el mismo lector, y por tanto la misma
 * fuente de entrada.
 */
public class CrudSistemasOperativos {
    private static final EntityManager EM = JPAUtil.crearEntityManager();
    private static final ILectorDatos LECTOR = new LectorDatosTexto();
    private static final SistemaOperativoDAO SISTEMA_OPERATIVO_DAO = new SistemaOperativoDAO(EM);

    public static void main(String[] args) {

        LECTOR.mostrarTexto(MSG_MENU_CRUD_SISTEMAS_OPERATIVOS);
        IMenu menu = construirMenu();
        String opcion;
        do {
            opcion = menu.solicitarOpcion();
            switch (opcion) {
                case VALOR_OPCION_MOSTRAR_TODOS -> mostrarTodosLosSistemasOperativos();
                case VALOR_OPCION_AGREGAR -> agregarSistemaOperativo();
                case VALOR_OPCION_ACTUALIZAR -> actualizarSistemaOperativo();
                case VALOR_OPCION_ELIMINAR -> borrarSistemaOperativo();
                case IMenu.VALOR_SALIR -> LECTOR.mostrarTexto(MSG_SALIENDO_PROGRAMA);
            }
        } while (!opcion.equals(IMenu.VALOR_SALIR));
        JPAUtil.cerrar();
    }

    private static IMenu construirMenu() {
        IMenu menu = new MenuTexto(LECTOR);
        menu.setConCancelar(false);
        menu.setConSalir(true);
        menu.agregarOpcion(VALOR_OPCION_MOSTRAR_TODOS, TEXTO_MENU_MOSTRAR_TODOS_SISTEMAS_OPERATIVOS);
        menu.agregarOpcion(VALOR_OPCION_AGREGAR, TEXTO_MENU_AGREGAR_SISTEMA_OPERATIVO);
        menu.agregarOpcion(VALOR_OPCION_ACTUALIZAR, TEXTO_MENU_ACTUALIZAR_SISTEMA_OPERATIVO);
        menu.agregarOpcion(VALOR_OPCION_ELIMINAR, TEXTO_MENU_ELIMINAR_SISTEMA_OPERATIVO);
        return menu;
    }

    private static void borrarSistemaOperativo() {
        Long id = (long) LECTOR.leerEntero(MSG_INGRESE_ID_SISTEMA_OPERATIVO);
        SistemaOperativo sistemaOperativo = SISTEMA_OPERATIVO_DAO.findById(id);
        if (sistemaOperativo == null) {
            LECTOR.mostrarError(MSG_SISTEMA_OPERATIVO_NO_ENCONTRADO);
            return;
        }
        SISTEMA_OPERATIVO_DAO.deleteById(id);
        LECTOR.mostrarTexto(MSG_SISTEMA_OPERATIVO_BORRADO);
    }

    private static void actualizarSistemaOperativo() {
        Long id = (long) LECTOR.leerEntero(MSG_INGRESE_ID_SISTEMA_OPERATIVO);
        SistemaOperativo sistemaOperativo = SISTEMA_OPERATIVO_DAO.findById(id);
        if (sistemaOperativo == null) {
            LECTOR.mostrarError(MSG_SISTEMA_OPERATIVO_NO_ENCONTRADO);
            return;
        }
        sistemaOperativo.setNombre(LECTOR.leerTexto(mensajeActualizable(MSG_INGRESE_NOMBRE_SISTEMA_OPERATIVO, sistemaOperativo.getNombre()), sistemaOperativo.getNombre()));
        sistemaOperativo.setFamilia(LECTOR.leerTexto(mensajeActualizable(MSG_INGRESE_FAMILIA_SISTEMA_OPERATIVO, sistemaOperativo.getFamilia()), sistemaOperativo.getFamilia()));
        sistemaOperativo.setProveedor(LECTOR.leerTexto(mensajeActualizable(MSG_INGRESE_PROVEEDOR_SISTEMA_OPERATIVO, sistemaOperativo.getProveedor()), sistemaOperativo.getProveedor()));
        sistemaOperativo.setDescripcion(LECTOR.leerTexto(mensajeActualizable(MSG_INGRESE_DESCRIPCION_SISTEMA_OPERATIVO, sistemaOperativo.getDescripcion()), sistemaOperativo.getDescripcion()));
        sistemaOperativo.setActivo(LECTOR.leerBoolean(mensajeActualizable(MSG_INGRESE_ACTIVO_SISTEMA_OPERATIVO, sistemaOperativo.isActivo()), sistemaOperativo.isActivo()));
        SISTEMA_OPERATIVO_DAO.update(sistemaOperativo);
        LECTOR.mostrarTexto(MSG_SISTEMA_OPERATIVO_ACTUALIZADO);
    }

    private static void agregarSistemaOperativo() {
        String nombre = LECTOR.leerTexto(MSG_INGRESE_NOMBRE_SISTEMA_OPERATIVO);
        String familia = LECTOR.leerTexto(MSG_INGRESE_FAMILIA_SISTEMA_OPERATIVO);
        String proveedor = LECTOR.leerTexto(MSG_INGRESE_PROVEEDOR_SISTEMA_OPERATIVO);
        String descripcion = LECTOR.leerTexto(MSG_INGRESE_DESCRIPCION_SISTEMA_OPERATIVO);
        SISTEMA_OPERATIVO_DAO.save(new SistemaOperativo(null, ACTIVO, nombre, familia, proveedor, descripcion));
        LECTOR.mostrarTexto(MSG_SISTEMA_OPERATIVO_AGREGADO);
    }

    private static void mostrarTodosLosSistemasOperativos() {
        List<SistemaOperativo> sistemasOperativos = SISTEMA_OPERATIVO_DAO.findAll();
        LECTOR.mostrarTexto(MSG_ENCABEZADO_SISTEMAS_OPERATIVOS);
        if (sistemasOperativos.isEmpty()) {
            LECTOR.mostrarTexto(MSG_SIN_SISTEMAS_OPERATIVOS);
            return;
        }
        for (SistemaOperativo sistemaOperativo : sistemasOperativos) {
            LECTOR.mostrarTexto(MSG_DETALLE_ID + sistemaOperativo.getId()
                    + MSG_DETALLE_ACTIVO + sistemaOperativo.isActivo()
                    + MSG_DETALLE_NOMBRE + sistemaOperativo.getNombre()
                    + MSG_DETALLE_FAMILIA + sistemaOperativo.getFamilia()
                    + MSG_DETALLE_PROVEEDOR + sistemaOperativo.getProveedor()
                    + MSG_DETALLE_DESCRIPCION + sistemaOperativo.getDescripcion());
        }
    }
}
