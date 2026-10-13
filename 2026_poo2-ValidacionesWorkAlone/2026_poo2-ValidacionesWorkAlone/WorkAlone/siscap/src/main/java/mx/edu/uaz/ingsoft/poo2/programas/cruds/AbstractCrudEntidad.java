package mx.edu.uaz.ingsoft.poo2.programas.cruds;

import jakarta.persistence.EntityManager;
import mx.edu.uaz.ingsoft.poo2.modelo.daos.interf.IBaseDAO;
import mx.edu.uaz.ingsoft.poo2.modelo.entidades.IEntidad;
import mx.edu.uaz.ingsoft.poo2.modelo.utils.JPAUtil;
import mx.edu.uaz.ingsoft.poo2.utils.inout.impl.LectorDatosTexto;
import mx.edu.uaz.ingsoft.poo2.utils.inout.interfaces.ILectorDatos;
import mx.edu.uaz.ingsoft.poo2.utils.menu.impl.MenuTexto;
import mx.edu.uaz.ingsoft.poo2.utils.menu.interfaces.IMenu;

import java.util.List;

import static mx.edu.uaz.ingsoft.poo2.utils.Constantes.*;


public abstract class AbstractCrudEntidad<T extends IEntidad> {
    protected static final EntityManager EM = JPAUtil.crearEntityManager();
    protected static final ILectorDatos LECTOR = new LectorDatosTexto();
    protected IMenu menu;

    protected final IBaseDAO<T> dao;

    public AbstractCrudEntidad(IBaseDAO<T> dao) {
        this.dao = dao;
        this.menu = new MenuTexto(LECTOR);
        construirMenu();
    }

    public void ejecutar() {
        LECTOR.mostrarTexto(MSG_MENU_CRUD);
        String opcion;
        do {
            opcion = menu.solicitarOpcion();
            switch (opcion) {
                case VALOR_OPCION_MOSTRAR_TODOS -> mostrarTodosLosRegistros();
                case VALOR_OPCION_AGREGAR -> agregarBaseDeDatos();
                case VALOR_OPCION_ACTUALIZAR -> actualizarRegistro();
                case VALOR_OPCION_ELIMINAR -> borrarRegistro();
                case IMenu.VALOR_SALIR -> LECTOR.mostrarTexto(MSG_SALIENDO_PROGRAMA);
            }
        } while (!opcion.equals(IMenu.VALOR_SALIR));
        JPAUtil.cerrar();
    }

    public void construirMenu(){

        menu.setConCancelar(false);
        menu.setConSalir(true);
        menu.agregarOpcion(VALOR_OPCION_MOSTRAR_TODOS, TEXTO_MENU_MOSTRAR_TODOS_LOS_REGISTROS);
        menu.agregarOpcion(VALOR_OPCION_AGREGAR, TEXTO_MENU_AGREGAR_REGISTRO);
        menu.agregarOpcion(VALOR_OPCION_ACTUALIZAR, TEXTO_MENU_ACTUALIZAR_REGISTRO);
        // menu.agregarOpcion(VALOR_OPCION_ELIMINAR, TEXTO_MENU_ELIMINAR_REGISTRO);
    }
    public abstract   void  mostrarEntidad(T entidad);

    private void borrarRegistro() {
        Integer id = LECTOR.leerEntero(MSG_INGRESAR_ID);
        T entidad = dao.findById(id);
        if (entidad == null) {
            LECTOR.mostrarError(MSG_REGISTRO_NO_ENCONTRADO);
            return;
        }
        dao.deleteById(id);
        LECTOR.mostrarTexto(MSG_ENTIDAD_BORRADA);
    }

    public abstract void pedirDatosEntidad(T e);
    public abstract T pedirDatosEntidad();

    /**
     * Compone el texto que se muestra al pedir el nuevo valor de un campo con
     * el formato "Nombre_del_campo(ValorActual):".
     *
     * <p>Es un método {@code protected} de la clase abstracta a propósito: así
     * cada CRUD concreto (por ejemplo {@code CrudBasesDeDatos}) solo tiene que
     * indicar el nombre de su campo y el valor actual, y el formato queda
     * escrito una sola vez para todos los programas CRUD.
     *
     * <p>Sustituye a {@code Constantes.mensajeActualizable}, que componía el
     * mismo tipo de texto en varios renglones; cuando los demás CRUD se
     * refactoricen a esta plantilla, aquella función queda sin uso.
     *
     * @param nombreCampo nombre del campo que se va a actualizar.
     * @param valorActual valor que tiene el campo en este momento; si es un
     *                   {@code Boolean} se muestra como V/F, que es como lo
     *                   entiende {@code ILectorDatos.leerBoolean}.
     */
    protected String mensajeActualizable(String nombreCampo, Object valorActual) {
        String valor = (valorActual instanceof Boolean activo)
                ? (activo ? "V" : "F")
                : String.valueOf(valorActual);
        return nombreCampo + "(" + valor + "):";
    }

    /**
     * Pide el valor de un campo de texto mostrando su valor actual. Si el
     * usuario solo presiona Enter, se conserva el valor actual.
     */
    protected String leerCampoActualizable(String nombreCampo, String valorActual) {
        return LECTOR.leerTexto(mensajeActualizable(nombreCampo, valorActual), valorActual);
    }

    private void actualizarRegistro() {
        Integer id = LECTOR.leerEntero(MSG_INGRESAR_ID);
        T entidad = dao.findById(id);
        if (entidad == null) {
            LECTOR.mostrarError(MSG_REGISTRO_NO_ENCONTRADO);
            return;
        }
        pedirDatosEntidad(entidad);
        dao.update(entidad);
        LECTOR.mostrarTexto(MSG_ENTIDAD_ACTUALIZADA);
    }

    private void agregarBaseDeDatos() {
        T entidad = pedirDatosEntidad();
        dao.save(entidad);
        LECTOR.mostrarTexto(MSG_ENTIDAD_AGREGADA);
    }

    private  void mostrarTodosLosRegistros() {
        List<T> entidades = dao.findAll();
        LECTOR.mostrarTexto(MSG_ENCABEZADO);
        if (entidades.isEmpty()) {
            LECTOR.mostrarTexto(MSG_SIN_REGISTROS);
            return;
        }
        entidades.forEach( e -> mostrarEntidad(e));

    }
}
