package mx.edu.uaz.ingsoft.poo2.programas;

import jakarta.persistence.EntityManager;
import mx.edu.uaz.ingsoft.poo2.modelo.daos.impl.TecnologiaDAO;
import mx.edu.uaz.ingsoft.poo2.modelo.entidades.Tecnologia;
import mx.edu.uaz.ingsoft.poo2.modelo.utils.JPAUtil;
import mx.edu.uaz.ingsoft.poo2.utils.inout.impl.LectorDatosTexto;
import mx.edu.uaz.ingsoft.poo2.utils.inout.interfaces.ILectorDatos;
import mx.edu.uaz.ingsoft.poo2.utils.menu.impl.MenuTexto;
import mx.edu.uaz.ingsoft.poo2.utils.menu.interfaces.IMenu;

import java.util.List;

import static mx.edu.uaz.ingsoft.poo2.utils.Constantes.*;

/**
 * CRUD de tecnologías en modo texto. El menú se construye con la biblioteca de
 * menús (IMenu/MenuTexto): el programa registra opciones con VALOR y TEXTO, y
 * decide qué hacer según el valor devuelto por solicitarOpcion().
 *
 * La entrada de datos se delega en la biblioteca utils.inout (ILectorDatos): el
 * menú y las lecturas de campos comparten el mismo lector, y por tanto la misma
 * fuente de entrada.
 */
public class CrudTecnologias {
    private static final EntityManager EM = JPAUtil.crearEntityManager();
    private static final ILectorDatos LECTOR = new LectorDatosTexto();
    private static final TecnologiaDAO TECNOLOGIA_DAO = new TecnologiaDAO(EM);

    public static void main(String[] args) {

        LECTOR.mostrarTexto(MSG_MENU_CRUD_TECNOLOGIAS);
        IMenu menu = construirMenu();
        String opcion;
        do {
            opcion = menu.solicitarOpcion();
            switch (opcion) {
                case VALOR_OPCION_MOSTRAR_TODOS -> mostrarTodasLasTecnologias();
                case VALOR_OPCION_AGREGAR -> agregarTecnologia();
                case VALOR_OPCION_ACTUALIZAR -> actualizarTecnologia();
                case VALOR_OPCION_ELIMINAR -> borrarTecnologia();
                case IMenu.VALOR_SALIR -> LECTOR.mostrarTexto(MSG_SALIENDO_PROGRAMA);
            }
        } while (!opcion.equals(IMenu.VALOR_SALIR));
        JPAUtil.cerrar();
    }

    private static IMenu construirMenu() {
        IMenu menu = new MenuTexto(LECTOR);
        menu.setConCancelar(false);
        menu.setConSalir(true);
        menu.agregarOpcion(VALOR_OPCION_MOSTRAR_TODOS, TEXTO_MENU_MOSTRAR_TODAS_TECNOLOGIAS);
        menu.agregarOpcion(VALOR_OPCION_AGREGAR, TEXTO_MENU_AGREGAR_TECNOLOGIA);
        menu.agregarOpcion(VALOR_OPCION_ACTUALIZAR, TEXTO_MENU_ACTUALIZAR_TECNOLOGIA);
        menu.agregarOpcion(VALOR_OPCION_ELIMINAR, TEXTO_MENU_ELIMINAR_TECNOLOGIA);
        return menu;
    }

    private static void borrarTecnologia() {
        Long id = (long) LECTOR.leerEntero(MSG_INGRESE_ID_TECNOLOGIA);
        TECNOLOGIA_DAO.deleteById(id);
        Tecnologia tecnologia = TECNOLOGIA_DAO.findById(id);
        if (tecnologia == null) {
            LECTOR.mostrarError(MSG_TECNOLOGIA_NO_ENCONTRADA);
            return;
        }
        TECNOLOGIA_DAO.deleteById(id);
        LECTOR.mostrarTexto(MSG_TECNOLOGIA_BORRADA);
    }

    private static void actualizarTecnologia() {
        Long id = (long) LECTOR.leerEntero(MSG_INGRESE_ID_TECNOLOGIA);
        Tecnologia tecnologia = TECNOLOGIA_DAO.findById(id);
        if (tecnologia == null) {
            LECTOR.mostrarError(MSG_TECNOLOGIA_NO_ENCONTRADA);
            return;
        }
        tecnologia.setNombre(LECTOR.leerTexto(mensajeActualizable(MSG_INGRESE_NOMBRE_TECNOLOGIA, tecnologia.getNombre()), tecnologia.getNombre()));
        tecnologia.setCategoriaGeneral(LECTOR.leerTexto(mensajeActualizable(MSG_INGRESE_CATEGORIA_GENERAL_TECNOLOGIA, tecnologia.getCategoriaGeneral()), tecnologia.getCategoriaGeneral()));
        tecnologia.setFabricanteComunidad(LECTOR.leerTexto(mensajeActualizable(MSG_INGRESE_FABRICANTE_COMUNIDAD_TECNOLOGIA, tecnologia.getFabricanteComunidad()), tecnologia.getFabricanteComunidad()));
        tecnologia.setDescripcion(LECTOR.leerTexto(mensajeActualizable(MSG_INGRESE_DESCRIPCION_TECNOLOGIA, tecnologia.getDescripcion()), tecnologia.getDescripcion()));
        tecnologia.setActivo(LECTOR.leerBoolean(mensajeActualizable(MSG_INGRESE_ACTIVO_TECNOLOGIA, tecnologia.isActivo()), tecnologia.isActivo()));
        TECNOLOGIA_DAO.update(tecnologia);
        LECTOR.mostrarTexto(MSG_TECNOLOGIA_ACTUALIZADA);
    }

    private static void agregarTecnologia() {
        String nombre = LECTOR.leerTexto(MSG_INGRESE_NOMBRE_TECNOLOGIA);
        String categoriaGeneral = LECTOR.leerTexto(MSG_INGRESE_CATEGORIA_GENERAL_TECNOLOGIA);
        String fabricanteComunidad = LECTOR.leerTexto(MSG_INGRESE_FABRICANTE_COMUNIDAD_TECNOLOGIA);
        String descripcion = LECTOR.leerTexto(MSG_INGRESE_DESCRIPCION_TECNOLOGIA);
        TECNOLOGIA_DAO.save(new Tecnologia(null, ACTIVO, nombre, categoriaGeneral, fabricanteComunidad, descripcion));
        LECTOR.mostrarTexto(MSG_TECNOLOGIA_AGREGADA);
    }

    private static void mostrarTodasLasTecnologias() {
        List<Tecnologia> tecnologias = TECNOLOGIA_DAO.findAll();
        LECTOR.mostrarTexto(MSG_ENCABEZADO_TECNOLOGIAS);
        if (tecnologias.isEmpty()) {
            LECTOR.mostrarTexto(MSG_SIN_TECNOLOGIAS);
            return;
        }
        for (Tecnologia tecnologia : tecnologias) {
            LECTOR.mostrarTexto(MSG_DETALLE_ID + tecnologia.getId()
                    + MSG_DETALLE_ACTIVO + tecnologia.isActivo()
                    + MSG_DETALLE_NOMBRE + tecnologia.getNombre()
                    + MSG_DETALLE_CATEGORIA_GENERAL + tecnologia.getCategoriaGeneral()
                    + MSG_DETALLE_FABRICANTE_COMUNIDAD + tecnologia.getFabricanteComunidad()
                    + MSG_DETALLE_DESCRIPCION + tecnologia.getDescripcion());
        }
    }
}
