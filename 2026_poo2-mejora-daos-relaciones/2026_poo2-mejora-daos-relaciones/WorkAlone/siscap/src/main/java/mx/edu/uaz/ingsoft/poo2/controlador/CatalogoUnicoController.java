package mx.edu.uaz.ingsoft.poo2.controlador;

import mx.edu.uaz.ingsoft.poo2.modelo.daos.DAOException;
import mx.edu.uaz.ingsoft.poo2.modelo.entidades.IEntidad;
import mx.edu.uaz.ingsoft.poo2.modelo.utils.MensajesResourceBundle;
import mx.edu.uaz.ingsoft.poo2.servicios.interfaces.IServicioBase;
import mx.edu.uaz.ingsoft.poo2.utils.config.CampoBusqueda;
import mx.edu.uaz.ingsoft.poo2.utils.config.CamposBusquedaConfig;
import mx.edu.uaz.ingsoft.poo2.utils.validadores.interfaces.IValidadorEntidad;
import mx.edu.uaz.ingsoft.poo2.vista.paneles.PanelListEntidades;
import mx.edu.uaz.ingsoft.poo2.vista.paneles.PanelShowEntity;
import mx.edu.uaz.ingsoft.poo2.vista.ventanas.unica.UnicaCatalogo;

import java.util.List;
import java.util.ResourceBundle;

import static mx.edu.uaz.ingsoft.poo2.utils.Constantes.MSG_ERROR_VALIDACION;

/**
 * Controlador de la ventana única de CRUD de catálogos.
 *
 * <p>Reusa las mismas piezas que el CRUD clásico (servicio de
 * {@code DaoServiceFactory}, validador de {@code ValidadorEntidadFactory} y
 * los paneles {@code PanelShowEntity}/{@code PanelListEntidades}) pero en
 * lugar de repartir la operación en cuatro ventanas, gestiona una máquina de
 * estados ({@code ALTA}/{@code DETALLE}/{@code EDICION}) sobre un solo
 * formulario. El flujo de validar → guardar/actualizar/borrar con
 * {@code DAOException} y los diálogos de error se reaprovechan del patrón de
 * {@code AbstractControllerBase}.
 */
public class CatalogoUnicoController<T extends IEntidad> {

    public enum Modo {
        ALTA,
        DETALLE,
        EDICION
    }

    private final String nombreCatalogo;
    private final IServicioBase<T> servicio;
    private final IValidadorEntidad<T> validador;
    private final PanelShowEntity<T> panelFormulario;
    private final PanelListEntidades<T> panelListado;
    private final UnicaCatalogo ventana;
    private final ResourceBundle resBundle;

    private Modo modo = Modo.ALTA;
    private T registroActual;
    private int pagina = 0;
    private int tamanoPagina = 5;
    private boolean paginaCompleta = true;
    private boolean busquedaActiva = false;
    private CampoBusqueda campoBusquedaActivo;
    private String textoBusquedaActivo;

    public CatalogoUnicoController(String nombreCatalogo,
                                   IServicioBase<T> servicio,
                                   IValidadorEntidad<T> validador,
                                   PanelShowEntity<T> panelFormulario,
                                   PanelListEntidades<T> panelListado,
                                   UnicaCatalogo ventana) {
        this.nombreCatalogo = nombreCatalogo;
        this.servicio = servicio;
        this.validador = validador;
        this.panelFormulario = panelFormulario;
        this.panelListado = panelListado;
        this.ventana = ventana;
        resBundle = MensajesResourceBundle.MENSAJES.getResourceBundle();
        panelListado.establecerListListener(e -> {
            if (!e.getValueIsAdjusting()) {
                reaccionarSeleccion();
            }
        });
        cargarPagina(0);
        aplicarModo();
    }

    public void nuevo() {
        modo = Modo.ALTA;
        registroActual = null;
        panelFormulario.limpiarFormulario();
        panelFormulario.setEditable(true);
        panelListado.limpiarSeleccion();
        aplicarModo();
    }

    public void editar() {
        if (modo == Modo.DETALLE && registroActual != null) {
            modo = Modo.EDICION;
            panelListado.limpiarSeleccion();
            panelFormulario.setEditable(true);
            aplicarModo();
        }
    }

    public void borrar() {
        if (modo != Modo.DETALLE || registroActual == null) {
            return;
        }
        var mensajeConfirmacion = resBundle.getString("borrar.confirmacion") + "\n" + registroActual;
        if (!ventana.confirmarBorrado(mensajeConfirmacion)) {
            return;
        }
        try {
            servicio.borrarRegistro(registroActual);
        } catch (DAOException e) {
            ventana.mostrarMensaje(e.getMessage());
            return;
        }
        registroActual = null;
        refrescarVista();
        modo = Modo.ALTA;
        panelFormulario.limpiarFormulario();
        aplicarModo();
    }

    public void aceptar() {
        switch (modo) {
            case ALTA -> aceptarAlta();
            case EDICION -> aceptarEdicion();
            case DETALLE -> {
            }
        }
    }

    public void cancelar() {
        if (modo == Modo.EDICION && registroActual != null) {
            mostrarDetalle(registroActual);
        } else if (modo == Modo.ALTA) {
            registroActual = null;
            panelFormulario.limpiarFormulario();
            panelListado.limpiarSeleccion();
            aplicarModo();
        }
    }

    public void buscar(String claveCampo, String texto) {
        if (texto == null || texto.isBlank()) {
            ventana.mostrarMensaje(resBundle.getString("busqueda.error.valor.vacio"));
            return;
        }
        var campo = CamposBusquedaConfig.campoPorEtiqueta(nombreCatalogo, claveCampo);
        if (campo.isEmpty()) {
            ventana.mostrarMensaje(resBundle.getString("busqueda.error.convertir.valor"));
            return;
        }
        buscar(campo.get(), texto);
    }

    public void limpiarBusqueda() {
        busquedaActiva = false;
        campoBusquedaActivo = null;
        textoBusquedaActivo = null;
        ventana.limpiarCampoValor();
        pagina = 0;
        cargarPagina(0);
    }

    public void irPaginaSiguiente() {
        if (busquedaActiva || !paginaCompleta) {
            return;
        }
        cargarPagina(pagina + 1);
    }

    public void irPaginaAnterior() {
        if (busquedaActiva || pagina <= 0) {
            return;
        }
        cargarPagina(pagina - 1);
    }

    public void cambiarTamanoPagina(int nuevoTamano) {
        if (nuevoTamano <= 0) {
            return;
        }
        tamanoPagina = nuevoTamano;
        if (!busquedaActiva) {
            pagina = 0;
            cargarPagina(0);
        }
    }

    private void aceptarAlta() {
        T nuevoRegistro = panelFormulario.getEntidadFromFormulario();
        if (!validador.validar(nuevoRegistro)) {
            mostrarErroresValidacion();
            return;
        }
        try {
            servicio.guardar(nuevoRegistro);
        } catch (DAOException e) {
            ventana.mostrarMensaje(e.getMessage());
            return;
        }
        registroActual = null;
        refrescarVista();
        modo = Modo.ALTA;
        aplicarModo();
    }

    private void aceptarEdicion() {
        T registroEditar = registroActual;
        panelFormulario.actualizarEntidadDesdeFormulario(registroEditar);
        if (!validador.validar(registroEditar)) {
            mostrarErroresValidacion();
            return;
        }
        try {
            servicio.actualizar(registroEditar);
        } catch (DAOException e) {
            ventana.mostrarMensaje(e.getMessage());
            return;
        }
        refrescarVista();
        mostrarDetalle(registroEditar);
    }

    private void buscar(CampoBusqueda campo, String texto) {
        var valor = CamposBusquedaConfig.convertirValor(campo, texto);
        if (valor.isEmpty()) {
            ventana.mostrarMensaje(resBundle.getString("busqueda.error.convertir.valor"));
            return;
        }
        campoBusquedaActivo = campo;
        textoBusquedaActivo = texto;
        busquedaActiva = true;
        List<T> resultados = servicio.buscarPorCampo(campo.atributoJpa(), valor.get());
        panelListado.establecerListaEntidades(resultados);
        ventana.actualizarPaginacion(resBundle.getString("busqueda.etiqueta"), false, false);
        reaccionarSeleccion();
    }

    private void reaccionarSeleccion() {
        var seleccion = panelListado.getItemSelected();
        if (seleccion.isPresent()) {
            mostrarDetalle(seleccion.get());
        } else if (modo != Modo.EDICION) {
            registroActual = null;
            panelFormulario.limpiarFormulario();
            panelFormulario.setEditable(true);
            modo = Modo.ALTA;
            aplicarModo();
        }
    }

    private void mostrarDetalle(T registro) {
        registroActual = registro;
        modo = Modo.DETALLE;
        panelFormulario.setEntidad(registro);
        panelFormulario.setEditable(false);
        aplicarModo();
    }

    private void refrescarVista() {
        if (busquedaActiva) {
            var campo = campoBusquedaActivo;
            var texto = textoBusquedaActivo;
            if (campo != null && texto != null) {
                var valor = CamposBusquedaConfig.convertirValor(campo, texto);
                if (valor.isPresent()) {
                    List<T> resultados = servicio.buscarPorCampo(campo.atributoJpa(), valor.get());
                    panelListado.establecerListaEntidades(resultados);
                    ventana.actualizarPaginacion(resBundle.getString("busqueda.etiqueta"), false, false);
                    return;
                }
            }
            busquedaActiva = false;
        }
        cargarPagina(pagina);
    }

    private void cargarPagina(int nuevaPagina) {
        if (nuevaPagina < 0) {
            nuevaPagina = 0;
        }
        pagina = nuevaPagina;
        List<T> datosPagina = servicio.recuperarPagina(pagina, tamanoPagina);
        paginaCompleta = datosPagina.size() >= tamanoPagina;
        panelListado.establecerListaEntidades(datosPagina);
        boolean hayAnterior = pagina > 0;
        boolean haySiguiente = paginaCompleta;
        ventana.actualizarPaginacion(resBundle.getString("pagina") + " " + (pagina + 1), hayAnterior, haySiguiente);
    }

    private void mostrarErroresValidacion() {
        ventana.mostrarMensaje(MSG_ERROR_VALIDACION + "\n" + String.join("\n", validador.getErrores()));
    }

    private void aplicarModo() {
        ventana.aplicarModo(modo);
    }
}