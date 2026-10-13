package mx.edu.uaz.ingsoft.poo2.programas.cruds;

import jakarta.persistence.EntityManager;

import mx.edu.uaz.ingsoft.poo2.modelo.daos.impl.PersonaDAO;
import mx.edu.uaz.ingsoft.poo2.modelo.entidades.Persona;
import mx.edu.uaz.ingsoft.poo2.modelo.utils.JPAUtil;
import mx.edu.uaz.ingsoft.poo2.utils.inout.impl.LectorDatosTexto;
import mx.edu.uaz.ingsoft.poo2.utils.inout.interfaces.ILectorDatos;
import mx.edu.uaz.ingsoft.poo2.utils.menu.impl.MenuTexto;
import mx.edu.uaz.ingsoft.poo2.utils.menu.interfaces.IMenu;

import java.util.List;

import static mx.edu.uaz.ingsoft.poo2.utils.Constantes.*;

/**
 * CRUD de personas en modo texto. El menú se construye con la biblioteca de
 * menús (IMenu/MenuTexto): el programa registra opciones con VALOR y TEXTO, y
 * decide qué hacer según el valor devuelto por solicitarOpcion().
 *
 * La entrada de datos se delega en la biblioteca utils.inout (ILectorDatos): el
 * menú y las lecturas de campos comparten el mismo lector, y por tanto la misma
 * fuente de entrada.
 */
public class CrudPersonas {
    private static final ILectorDatos LECTOR = new LectorDatosTexto();
    private static final EntityManager EM = JPAUtil.crearEntityManager();
    private static final PersonaDAO PERSONA_DAO = new PersonaDAO(EM);

    public static void main(String[] args) {
        LECTOR.mostrarTexto(MSG_MENU_CRUD_PERSONAS);
        IMenu menu = construirMenu();
        String opcion;
        do {
            opcion = menu.solicitarOpcion();
            switch (opcion) {
                case VALOR_OPCION_MOSTRAR_TODOS -> mostrarTodasLasPersonas();
                case VALOR_OPCION_AGREGAR -> agregarPersona();
                case VALOR_OPCION_ACTUALIZAR -> actualizarPersona();
                case VALOR_OPCION_ELIMINAR -> borrarPersona();
                case IMenu.VALOR_SALIR -> LECTOR.mostrarTexto(MSG_SALIENDO_PROGRAMA);
            }
        } while (!opcion.equals(IMenu.VALOR_SALIR));
        JPAUtil.cerrar();
    }

    private static IMenu construirMenu() {
        IMenu menu = new MenuTexto(LECTOR);
        menu.setConCancelar(false);
        menu.setConSalir(true);
        menu.agregarOpcion(VALOR_OPCION_MOSTRAR_TODOS, TEXTO_MENU_MOSTRAR_TODAS_PERSONAS);
        menu.agregarOpcion(VALOR_OPCION_AGREGAR, TEXTO_MENU_AGREGAR_PERSONA);
        menu.agregarOpcion(VALOR_OPCION_ACTUALIZAR, TEXTO_MENU_ACTUALIZAR_PERSONA);
        menu.agregarOpcion(VALOR_OPCION_ELIMINAR, TEXTO_MENU_ELIMINAR_PERSONA);
        return menu;
    }

    private static void borrarPersona() {
        Integer id = LECTOR.leerEntero(MSG_INGRESE_ID_PERSONA);
        Persona persona = PERSONA_DAO.findById(id);
        if (persona == null) {
            LECTOR.mostrarError(MSG_PERSONA_NO_ENCONTRADA);
            return;
        }
        PERSONA_DAO.delete(persona);
        LECTOR.mostrarTexto(MSG_PERSONA_BORRADA);
    }

    private static void actualizarPersona() {
        Integer id = LECTOR.leerEntero(MSG_INGRESE_ID_PERSONA);
        Persona persona = PERSONA_DAO.findById(id);
        if (persona == null) {
            LECTOR.mostrarError(MSG_PERSONA_NO_ENCONTRADA);
            return;
        }
        persona.setNombre(LECTOR.leerTexto(mensajeActualizable(MSG_INGRESE_NOMBRE_PERSONA, persona.getNombre()), persona.getNombre()));
        persona.setPrimerApellido(LECTOR.leerTexto(mensajeActualizable(MSG_INGRESE_PRIMER_APELLIDO_PERSONA, persona.getPrimerApellido()), persona.getPrimerApellido()));
        persona.setSegundoApellido(LECTOR.leerTexto(mensajeActualizable(MSG_INGRESE_SEGUNDO_APELLIDO_PERSONA, persona.getSegundoApellido()), persona.getSegundoApellido()));
        persona.setNumeroEmpleado(LECTOR.leerTexto(mensajeActualizable(MSG_INGRESE_NUMERO_EMPLEADO_PERSONA, persona.getNumeroEmpleado()), persona.getNumeroEmpleado()));
        persona.setCorreoElectronico(LECTOR.leerTexto(mensajeActualizable(MSG_INGRESE_CORREO_PERSONA, persona.getCorreoElectronico()), persona.getCorreoElectronico()));
        persona.setActivo(LECTOR.leerBoolean(mensajeActualizable(MSG_INGRESE_ACTIVO_PERSONA, persona.isActivo()), persona.isActivo()));
        PERSONA_DAO.update(persona);
        LECTOR.mostrarTexto(MSG_PERSONA_ACTUALIZADA);
    }

    private static void agregarPersona() {
        String nombre = LECTOR.leerTexto(MSG_INGRESE_NOMBRE_PERSONA);
        String primerApellido = LECTOR.leerTexto(MSG_INGRESE_PRIMER_APELLIDO_PERSONA);
        String segundoApellido = LECTOR.leerTexto(MSG_INGRESE_SEGUNDO_APELLIDO_PERSONA);
        String numeroEmpleado = LECTOR.leerTexto(MSG_INGRESE_NUMERO_EMPLEADO_PERSONA);
        String correo = LECTOR.leerTexto(MSG_INGRESE_CORREO_PERSONA);
        PERSONA_DAO.save(new Persona(null, ACTIVO, nombre, primerApellido, segundoApellido, numeroEmpleado, correo));
        LECTOR.mostrarTexto(MSG_PERSONA_AGREGADA);
    }

    private static void mostrarTodasLasPersonas() {
        List<Persona> personas = PERSONA_DAO.findAll();
        LECTOR.mostrarTexto(MSG_ENCABEZADO_PERSONAS);
        if (personas.isEmpty()) {
            LECTOR.mostrarTexto(MSG_SIN_PERSONAS);
            return;
        }
        for (Persona persona : personas) {
            LECTOR.mostrarTexto(MSG_DETALLE_ID + persona.getId()
                    + MSG_DETALLE_ACTIVO + persona.isActivo()
                    + MSG_DETALLE_NOMBRE + persona.getNombre()
                    + MSG_DETALLE_PRIMER_APELLIDO + persona.getPrimerApellido()
                    + MSG_DETALLE_SEGUNDO_APELLIDO + persona.getSegundoApellido()
                    + MSG_DETALLE_NUMERO_EMPLEADO + persona.getNumeroEmpleado()
                    + MSG_DETALLE_CORREO_ELECTRONICO + persona.getCorreoElectronico());
        }
    }
}
