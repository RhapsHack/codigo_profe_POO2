package mx.edu.uaz.ingsoft.poo2.programas.cruds;

import jakarta.persistence.EntityManager;
import mx.edu.uaz.ingsoft.poo2.modelo.daos.impl.FrameworkDAO;
import mx.edu.uaz.ingsoft.poo2.modelo.entidades.Framework;
import mx.edu.uaz.ingsoft.poo2.modelo.utils.JPAUtil;
import mx.edu.uaz.ingsoft.poo2.utils.inout.impl.LectorDatosTexto;
import mx.edu.uaz.ingsoft.poo2.utils.inout.interfaces.ILectorDatos;
import mx.edu.uaz.ingsoft.poo2.utils.menu.impl.MenuTexto;
import mx.edu.uaz.ingsoft.poo2.utils.menu.interfaces.IMenu;

import java.util.List;

import static mx.edu.uaz.ingsoft.poo2.utils.Constantes.*;

/**
 * CRUD de frameworks en modo texto. El menú se construye con la biblioteca de
 * menús (IMenu/MenuTexto): el programa registra opciones con VALOR y TEXTO, y
 * decide qué hacer según el valor devuelto por solicitarOpcion().
 *
 * La entrada de datos se delega en la biblioteca utils.inout (ILectorDatos): el
 * menú y las lecturas de campos comparten el mismo lector, y por tanto la misma
 * fuente de entrada.
 */
public class CrudFrameworks {
    private static final EntityManager EM = JPAUtil.crearEntityManager();
    private static final ILectorDatos LECTOR = new LectorDatosTexto();
    private static final FrameworkDAO FRAMEWORK_DAO = new FrameworkDAO(EM);

    public static void main(String[] args) {

        LECTOR.mostrarTexto(MSG_MENU_CRUD_FRAMEWORKS);
        IMenu menu = construirMenu();
        String opcion;
        do {
            opcion = menu.solicitarOpcion();
            switch (opcion) {
                case VALOR_OPCION_MOSTRAR_TODOS -> mostrarTodosLosFrameworks();
                case VALOR_OPCION_AGREGAR -> agregarFramework();
                case VALOR_OPCION_ACTUALIZAR -> actualizarFramework();
                case VALOR_OPCION_ELIMINAR -> borrarFramework();
                case IMenu.VALOR_SALIR -> LECTOR.mostrarTexto(MSG_SALIENDO_PROGRAMA);
            }
        } while (!opcion.equals(IMenu.VALOR_SALIR));

        JPAUtil.cerrar();
    }

    private static IMenu construirMenu() {
        IMenu menu = new MenuTexto(LECTOR);
        menu.setConCancelar(false);
        menu.setConSalir(true);
        menu.agregarOpcion(VALOR_OPCION_MOSTRAR_TODOS, TEXTO_MENU_MOSTRAR_TODOS_FRAMEWORKS);
        menu.agregarOpcion(VALOR_OPCION_AGREGAR, TEXTO_MENU_AGREGAR_FRAMEWORK);
        menu.agregarOpcion(VALOR_OPCION_ACTUALIZAR, TEXTO_MENU_ACTUALIZAR_FRAMEWORK);
        menu.agregarOpcion(VALOR_OPCION_ELIMINAR, TEXTO_MENU_ELIMINAR_FRAMEWORK);
        return menu;
    }

    private static void borrarFramework() {
        Integer id = LECTOR.leerEntero(MSG_INGRESE_ID_FRAMEWORK);
        Framework framework = FRAMEWORK_DAO.findById(id);
        if (framework == null) {
            LECTOR.mostrarError(MSG_FRAMEWORK_NO_ENCONTRADO);
            return;
        }
        FRAMEWORK_DAO.deleteById(id);
        LECTOR.mostrarTexto(MSG_FRAMEWORK_BORRADO);
    }

    private static void actualizarFramework() {
        Integer id = LECTOR.leerEntero(MSG_INGRESE_ID_FRAMEWORK);
        Framework framework = FRAMEWORK_DAO.findById(id);
        if (framework == null) {
            LECTOR.mostrarError(MSG_FRAMEWORK_NO_ENCONTRADO);
            return;
        }
        framework.setNombre(LECTOR.leerTexto(mensajeActualizable(MSG_INGRESE_NOMBRE_FRAMEWORK, framework.getNombre()), framework.getNombre()));
        framework.setTipo(LECTOR.leerTexto(mensajeActualizable(MSG_INGRESE_TIPO_FRAMEWORK, framework.getTipo()), framework.getTipo()));
        framework.setLenguajeBase(LECTOR.leerTexto(mensajeActualizable(MSG_INGRESE_LENGUAJE_BASE_FRAMEWORK, framework.getLenguajeBase()), framework.getLenguajeBase()));
        framework.setDescripcion(LECTOR.leerTexto(mensajeActualizable(MSG_INGRESE_DESCRIPCION_FRAMEWORK, framework.getDescripcion()), framework.getDescripcion()));
        framework.setActivo(LECTOR.leerBoolean(mensajeActualizable(MSG_INGRESE_ACTIVO_FRAMEWORK, framework.isActivo()), framework.isActivo()));
        FRAMEWORK_DAO.update(framework);
        LECTOR.mostrarTexto(MSG_FRAMEWORK_ACTUALIZADO);
    }

    private static void agregarFramework() {
        String nombre = LECTOR.leerTexto(MSG_INGRESE_NOMBRE_FRAMEWORK);
        String tipo = LECTOR.leerTexto(MSG_INGRESE_TIPO_FRAMEWORK);
        String lenguajeBase = LECTOR.leerTexto(MSG_INGRESE_LENGUAJE_BASE_FRAMEWORK);
        String descripcion = LECTOR.leerTexto(MSG_INGRESE_DESCRIPCION_FRAMEWORK);
        FRAMEWORK_DAO.save(new Framework(null, ACTIVO, nombre, tipo, lenguajeBase, descripcion));
        LECTOR.mostrarTexto(MSG_FRAMEWORK_AGREGADO);
    }

    private static void mostrarTodosLosFrameworks() {
        List<Framework> frameworks = FRAMEWORK_DAO.findAll();
        LECTOR.mostrarTexto(MSG_ENCABEZADO_FRAMEWORKS);
        if (frameworks.isEmpty()) {
            LECTOR.mostrarTexto(MSG_SIN_FRAMEWORKS);
            return;
        }
        for (Framework framework : frameworks) {
            LECTOR.mostrarTexto(MSG_DETALLE_ID + framework.getId()
                    + MSG_DETALLE_ACTIVO + framework.isActivo()
                    + MSG_DETALLE_NOMBRE + framework.getNombre()
                    + MSG_DETALLE_TIPO + framework.getTipo()
                    + MSG_DETALLE_LENGUAJE_BASE + framework.getLenguajeBase()
                    + MSG_DETALLE_DESCRIPCION + framework.getDescripcion());
        }
    }
}
