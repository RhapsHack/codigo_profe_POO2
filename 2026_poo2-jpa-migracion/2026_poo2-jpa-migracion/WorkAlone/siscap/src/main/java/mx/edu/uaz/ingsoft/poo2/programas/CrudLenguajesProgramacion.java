package mx.edu.uaz.ingsoft.poo2.programas;

import jakarta.persistence.EntityManager;

import mx.edu.uaz.ingsoft.poo2.modelo.daos.impl.ProgLanguagesDAO;
import mx.edu.uaz.ingsoft.poo2.modelo.entidades.ProgLanguages;
import mx.edu.uaz.ingsoft.poo2.modelo.utils.JPAUtil;
import mx.edu.uaz.ingsoft.poo2.utils.inout.impl.LectorDatosTexto;
import mx.edu.uaz.ingsoft.poo2.utils.inout.interfaces.ILectorDatos;
import mx.edu.uaz.ingsoft.poo2.utils.menu.impl.MenuTexto;
import mx.edu.uaz.ingsoft.poo2.utils.menu.interfaces.IMenu;

import java.util.List;

import static mx.edu.uaz.ingsoft.poo2.utils.Constantes.*;

/**
 * CRUD de lenguajes de programación en modo texto. El menú se construye con la
 * biblioteca de menús (IMenu/MenuTexto): el programa registra opciones con
 * VALOR y TEXTO, y decide qué hacer según el valor devuelto por
 * solicitarOpcion().
 *
 * La entrada de datos se delega en la biblioteca utils.inout (ILectorDatos): el
 * menú y las lecturas de campos comparten el mismo lector, y por tanto la misma
 * fuente de entrada.
 */
public class CrudLenguajesProgramacion {
    private static final ILectorDatos LECTOR = new LectorDatosTexto();
    private static final EntityManager EM = JPAUtil.crearEntityManager();
    private static final ProgLanguagesDAO PROG_LANGUAGES_DAO = new ProgLanguagesDAO(EM);

    public static void main(String[] args) {
        LECTOR.mostrarTexto(MSG_MENU_CRUD_LENGUAJES);
        IMenu menu = construirMenu();
        String opcion;
        do {
            opcion = menu.solicitarOpcion();
            switch (opcion) {
                case VALOR_OPCION_MOSTRAR_TODOS -> mostrarTodosLosLenguajesProgramacion();
                case VALOR_OPCION_AGREGAR -> agregarLenguajeProgramacion();
                case VALOR_OPCION_ACTUALIZAR -> actualizarLenguajeProgramacion();
                case VALOR_OPCION_ELIMINAR -> borrarLenguajeProgramacion();
                case IMenu.VALOR_SALIR -> LECTOR.mostrarTexto(MSG_SALIENDO_PROGRAMA);
            }
        } while (!opcion.equals(IMenu.VALOR_SALIR));

        JPAUtil.cerrar();
    }

    private static IMenu construirMenu() {
        IMenu menu = new MenuTexto(LECTOR);
        menu.setConCancelar(false);
        menu.setConSalir(true);
        menu.agregarOpcion(VALOR_OPCION_MOSTRAR_TODOS, TEXTO_MENU_MOSTRAR_TODOS_LENGUAJES);
        menu.agregarOpcion(VALOR_OPCION_AGREGAR, TEXTO_MENU_AGREGAR_LENGUAJE);
        menu.agregarOpcion(VALOR_OPCION_ACTUALIZAR, TEXTO_MENU_ACTUALIZAR_LENGUAJE);
        menu.agregarOpcion(VALOR_OPCION_ELIMINAR, TEXTO_MENU_ELIMINAR_LENGUAJE);
        return menu;
    }

    private static void borrarLenguajeProgramacion() {
        Long idProgLanguage = (long) LECTOR.leerEntero(MSG_INGRESE_ID_LENGUAJE);
        ProgLanguages progLanguage = PROG_LANGUAGES_DAO.findById(idProgLanguage);
        if (progLanguage == null) {
            LECTOR.mostrarError(MSG_LENGUAJE_NO_ENCONTRADO);
            return;
        }
        PROG_LANGUAGES_DAO.delete(progLanguage);
        LECTOR.mostrarTexto(MSG_LENGUAJE_BORRADO);
    }

    private static void actualizarLenguajeProgramacion() {
        Long idProgLanguage = (long) LECTOR.leerEntero(MSG_INGRESE_ID_LENGUAJE);
        ProgLanguages progLanguage = PROG_LANGUAGES_DAO.findById(idProgLanguage);
        if (progLanguage == null) {
            LECTOR.mostrarError(MSG_LENGUAJE_NO_ENCONTRADO);
            return;
        }
        progLanguage.setNombre(LECTOR.leerTexto(mensajeActualizable(MSG_INGRESE_NOMBRE_LENGUAJE, progLanguage.getNombre()), progLanguage.getNombre()));
        progLanguage.setParadigma(LECTOR.leerTexto(mensajeActualizable(MSG_INGRESE_PARADIGMA_LENGUAJE, progLanguage.getParadigma()), progLanguage.getParadigma()));
        progLanguage.setVersionReferencia(LECTOR.leerTexto(mensajeActualizable(MSG_INGRESE_VERSION_REFERENCIA_LENGUAJE, progLanguage.getVersionReferencia()), progLanguage.getVersionReferencia()));
        progLanguage.setDescripcion(LECTOR.leerTexto(mensajeActualizable(MSG_INGRESE_DESCRIPCION_LENGUAJE, progLanguage.getDescripcion()), progLanguage.getDescripcion()));
        progLanguage.setActivo(LECTOR.leerBoolean(mensajeActualizable(MSG_INGRESE_ACTIVO_LENGUAJE, progLanguage.isActivo()), progLanguage.isActivo()));
        PROG_LANGUAGES_DAO.update(progLanguage);
        LECTOR.mostrarTexto(MSG_LENGUAJE_ACTUALIZADO);
    }

    private static void agregarLenguajeProgramacion() {
        String nombre = LECTOR.leerTexto(MSG_INGRESE_NOMBRE_LENGUAJE);
        String paradigma = LECTOR.leerTexto(MSG_INGRESE_PARADIGMA_LENGUAJE);
        String versionReferencia = LECTOR.leerTexto(MSG_INGRESE_VERSION_REFERENCIA_LENGUAJE);
        String descripcion = LECTOR.leerTexto(MSG_INGRESE_DESCRIPCION_LENGUAJE);
        PROG_LANGUAGES_DAO.save(new ProgLanguages(null, ACTIVO, nombre, paradigma, versionReferencia, descripcion));
        LECTOR.mostrarTexto(MSG_LENGUAJE_AGREGADO);
    }

    private static void mostrarTodosLosLenguajesProgramacion() {
        List<ProgLanguages> progLanguages = PROG_LANGUAGES_DAO.findAll();
        LECTOR.mostrarTexto(MSG_ENCABEZADO_LENGUAJES);
        if (progLanguages.isEmpty()) {
            LECTOR.mostrarTexto(MSG_SIN_LENGUAJES);
            return;
        }
        for (ProgLanguages progLanguage : progLanguages) {
            LECTOR.mostrarTexto(MSG_DETALLE_ID + progLanguage.getId()
                    + MSG_DETALLE_ACTIVO + progLanguage.isActivo()
                    + MSG_DETALLE_NOMBRE + progLanguage.getNombre()
                    + MSG_DETALLE_PARADIGMA + progLanguage.getParadigma()
                    + MSG_DETALLE_VERSION_REFERENCIA + progLanguage.getVersionReferencia()
                    + MSG_DETALLE_DESCRIPCION + progLanguage.getDescripcion());
        }
    }
}
