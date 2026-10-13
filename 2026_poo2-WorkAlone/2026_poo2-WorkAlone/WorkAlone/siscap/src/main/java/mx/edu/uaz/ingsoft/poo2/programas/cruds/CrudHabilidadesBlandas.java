package mx.edu.uaz.ingsoft.poo2.programas.cruds;

import jakarta.persistence.EntityManager;
import mx.edu.uaz.ingsoft.poo2.modelo.daos.impl.HabilidadBlandaDAO;
import mx.edu.uaz.ingsoft.poo2.modelo.entidades.HabilidadBlanda;
import mx.edu.uaz.ingsoft.poo2.modelo.utils.JPAUtil;
import mx.edu.uaz.ingsoft.poo2.utils.inout.impl.LectorDatosTexto;
import mx.edu.uaz.ingsoft.poo2.utils.inout.interfaces.ILectorDatos;
import mx.edu.uaz.ingsoft.poo2.utils.menu.impl.MenuTexto;
import mx.edu.uaz.ingsoft.poo2.utils.menu.interfaces.IMenu;

import java.util.List;
import static mx.edu.uaz.ingsoft.poo2.utils.Constantes.*;

/**
 * CRUD de habilidades blandas en modo texto. El menú se construye con la
 * biblioteca de menús (IMenu/MenuTexto): el programa registra opciones con
 * VALOR y TEXTO, y decide qué hacer según el valor devuelto por
 * solicitarOpcion().
 */
public class CrudHabilidadesBlandas {
    private static final EntityManager EM = JPAUtil.crearEntityManager();
    private static final HabilidadBlandaDAO HABILIDAD_BLANDA_DAO = new HabilidadBlandaDAO(EM);
    protected static final ILectorDatos lectorDatos = new LectorDatosTexto();


    public static void main(String[] args) {

        lectorDatos.mostrarTexto(MSG_MENU_CRUD_HABILIDADES_BLANDAS);
        IMenu menu = construirMenu();
        String opcion;
        do {
            opcion = menu.solicitarOpcion();
            switch (opcion) {
                case VALOR_OPCION_MOSTRAR_TODOS -> mostrarTodasLasHabilidadesBlandas();
                case VALOR_OPCION_AGREGAR -> agregarHabilidadBlanda();
                case VALOR_OPCION_ACTUALIZAR -> actualizarHabilidadBlanda();
                case VALOR_OPCION_ELIMINAR -> borrarHabilidadBlanda();
                case IMenu.VALOR_SALIR -> lectorDatos.mostrarTexto(MSG_SALIENDO_PROGRAMA);
            }
        } while (!opcion.equals(IMenu.VALOR_SALIR));
        JPAUtil.cerrar();
    }

    private static IMenu construirMenu() {
        
        IMenu menu = new MenuTexto(lectorDatos);
        menu.setConCancelar(false);
        menu.setConSalir(true);
        menu.agregarOpcion(VALOR_OPCION_MOSTRAR_TODOS, TEXTO_MENU_MOSTRAR_TODAS_HABILIDADES_BLANDAS);
        menu.agregarOpcion(VALOR_OPCION_AGREGAR, TEXTO_MENU_AGREGAR_HABILIDAD_BLANDA);
        menu.agregarOpcion(VALOR_OPCION_ACTUALIZAR, TEXTO_MENU_ACTUALIZAR_HABILIDAD_BLANDA);
        menu.agregarOpcion(VALOR_OPCION_ELIMINAR, TEXTO_MENU_ELIMINAR_HABILIDAD_BLANDA);
        return menu;
    }

    private static void borrarHabilidadBlanda() {
        Integer id = lectorDatos.leerEntero(MSG_INGRESE_ID_HABILIDAD_BLANDA);
        HabilidadBlanda habilidadBlanda = HABILIDAD_BLANDA_DAO.findById(id);
        if (habilidadBlanda == null) {
            lectorDatos.mostrarError(MSG_HABILIDAD_BLANDA_NO_ENCONTRADA);
            return;
        }
        HABILIDAD_BLANDA_DAO.deleteById(id);
        lectorDatos.mostrarTexto(MSG_HABILIDAD_BLANDA_BORRADA);
    }

    private static void actualizarHabilidadBlanda() {
        Integer id = lectorDatos.leerEntero(MSG_INGRESE_ID_HABILIDAD_BLANDA);
        HabilidadBlanda habilidadBlanda = HABILIDAD_BLANDA_DAO.findById(id);
        if (habilidadBlanda == null) {
            lectorDatos.mostrarError(MSG_HABILIDAD_BLANDA_NO_ENCONTRADA);
            return;
        }
        habilidadBlanda.setNombre(lectorDatos.leerTexto(MSG_INGRESE_NOMBRE_HABILIDAD_BLANDA, habilidadBlanda.getNombre()));
        habilidadBlanda.setCategoria(lectorDatos.leerTexto(MSG_INGRESE_CATEGORIA_HABILIDAD_BLANDA, habilidadBlanda.getCategoria()));
        habilidadBlanda.setNivelEsperado(lectorDatos.leerTexto(MSG_INGRESE_NIVEL_ESPERADO_HABILIDAD_BLANDA, habilidadBlanda.getNivelEsperado()));
        habilidadBlanda.setDescripcion(lectorDatos.leerTexto(MSG_INGRESE_DESCRIPCION_HABILIDAD_BLANDA, habilidadBlanda.getDescripcion()));
        habilidadBlanda.setActivo(lectorDatos.leerBoolean(MSG_INGRESE_ACTIVO_HABILIDAD_BLANDA, habilidadBlanda.isActivo()));
        HABILIDAD_BLANDA_DAO.update(habilidadBlanda);
        lectorDatos.mostrarTexto(MSG_HABILIDAD_BLANDA_ACTUALIZADA);
    }

    private static void agregarHabilidadBlanda() {
        String nombre = lectorDatos.leerTexto(MSG_INGRESE_NOMBRE_HABILIDAD_BLANDA);
        String categoria = lectorDatos.leerTexto(MSG_INGRESE_CATEGORIA_HABILIDAD_BLANDA);
        String nivelEsperado = lectorDatos.leerTexto(MSG_INGRESE_NIVEL_ESPERADO_HABILIDAD_BLANDA);
        String descripcion = lectorDatos.leerTexto(MSG_INGRESE_DESCRIPCION_HABILIDAD_BLANDA);
        HABILIDAD_BLANDA_DAO.save(new HabilidadBlanda(null, ACTIVO, nombre, categoria, nivelEsperado, descripcion));
        lectorDatos.mostrarTexto(MSG_HABILIDAD_BLANDA_AGREGADA);
    }

    private static void mostrarTodasLasHabilidadesBlandas() {
        List<HabilidadBlanda> habilidadesBlandas = HABILIDAD_BLANDA_DAO.findAll();
        lectorDatos.mostrarTexto(MSG_ENCABEZADO_HABILIDADES_BLANDAS);
        if (habilidadesBlandas.isEmpty()) {
            lectorDatos.mostrarTexto(MSG_SIN_HABILIDADES_BLANDAS);
            return;
        }
        for (HabilidadBlanda habilidadBlanda : habilidadesBlandas) {
            lectorDatos.mostrarTexto(MSG_DETALLE_ID + habilidadBlanda.getId()
                    + MSG_DETALLE_ACTIVO + habilidadBlanda.isActivo()
                    + MSG_DETALLE_NOMBRE + habilidadBlanda.getNombre()
                    + MSG_DETALLE_CATEGORIA + habilidadBlanda.getCategoria()
                    + MSG_DETALLE_NIVEL_ESPERADO + habilidadBlanda.getNivelEsperado()
                    + MSG_DETALLE_DESCRIPCION + habilidadBlanda.getDescripcion());
        }
    }

}
