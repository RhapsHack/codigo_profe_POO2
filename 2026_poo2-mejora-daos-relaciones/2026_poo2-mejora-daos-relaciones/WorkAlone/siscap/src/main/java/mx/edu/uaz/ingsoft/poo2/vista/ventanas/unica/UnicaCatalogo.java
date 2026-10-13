package mx.edu.uaz.ingsoft.poo2.vista.ventanas.unica;

import mx.edu.uaz.ingsoft.poo2.controlador.CatalogoUnicoController;
import mx.edu.uaz.ingsoft.poo2.modelo.daos.DaoServiceFactory;
import mx.edu.uaz.ingsoft.poo2.modelo.utils.MensajesResourceBundle;
import mx.edu.uaz.ingsoft.poo2.utils.config.CamposBusquedaConfig;
import mx.edu.uaz.ingsoft.poo2.utils.validadores.ValidadorEntidadFactory;
import mx.edu.uaz.ingsoft.poo2.vista.paneles.PanelListEntidades;
import mx.edu.uaz.ingsoft.poo2.vista.paneles.PanelShowEntity;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.ResourceBundle;

import static mx.edu.uaz.ingsoft.poo2.utils.Constantes.*;
import static mx.edu.uaz.ingsoft.poo2.vista.utils.SwingComponentUtils.agregarComponentes;
import static mx.edu.uaz.ingsoft.poo2.vista.ventanas.utils.EntityPanelFactory.createPanelShowEntity;
import static mx.edu.uaz.ingsoft.poo2.vista.ventanas.utils.PanelsFactory.createComponentsPanel;
import static mx.edu.uaz.ingsoft.poo2.vista.ventanas.utils.PanelsFactory.createVerticalBoxPanel;

/**
 * Ventana única de CRUD de catálogos.
 *
 * <p>En una sola pantalla se selecciona el catálogo, se listan sus registros
 * paginados, se busca por campo, se ven los detalles y se hacen altas,
 * ediciones y borrados sobre el mismo formulario. Compone las piezas
 * existentes del CRUD clásico ({@code EntityPanelFactory},
 * {@code DaoServiceFactory}, {@code ValidadorEntidadFactory} y los paneles)
 * y delega las decisiones en {@code CatalogoUnicoController}.
 */
public class UnicaCatalogo extends JFrame {

    private final ResourceBundle resBundle;

    private final JComboBox<String> cmbCatalogos = new JComboBox<>();
    private final JComboBox<String> cmbCampos = new JComboBox<>();
    private final JComboBox<String> cmbTamano = new JComboBox<>();
    private final JTextField txtValor = new JTextField(NUM_COLS_TEXTFIELD);
    private final JLabel lblPagina = new JLabel();

    private final JButton botBuscar;
    private final JButton botLimpiar;
    private final JButton botNuevo;
    private final JButton botEditar;
    private final JButton botBorrar;
    private final JButton botAceptar;
    private final JButton botCancelar;
    private final JButton botAnterior;
    private final JButton botSiguiente;
    private final JButton botSalir;

    private final JPanel pnlCentroLista = new JPanel();
    private final JScrollPane scrollFormulario = new JScrollPane();

    private final List<String> catalogosDisponibles = List.of(CAT_PEOPLE, CAT_FRAMEWORK, CAT_LANG_PROG, CAT_TEMA);
    private final List<String> tamanosPagina = List.of("3", "5", "10");

    private CatalogoUnicoController<?> controlador;
    private List<String> clavesCamposBusqueda = List.of();
    private String catalogoActual;

    public UnicaCatalogo() {
        resBundle = MensajesResourceBundle.MENSAJES.getResourceBundle();
        botBuscar = new JButton(resBundle.getString("buscar"));
        botLimpiar = new JButton(resBundle.getString(BOT_LIMPIAR));
        botNuevo = new JButton(resBundle.getString("nuevo"));
        botEditar = new JButton(resBundle.getString(BOT_EDITAR));
        botBorrar = new JButton(resBundle.getString(BOT_BORRAR));
        botAceptar = new JButton(resBundle.getString(BOT_ACEPTAR));
        botCancelar = new JButton(resBundle.getString(BOT_CANCELAR));
        botAnterior = new JButton(resBundle.getString("anterior"));
        botSiguiente = new JButton(resBundle.getString("siguiente"));
        botSalir = new JButton(resBundle.getString("salir"));

        setTitle(resBundle.getString("ventana.title.catalogo.unico"));
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        add(construirPanelSuperior(), BorderLayout.PAGE_START);
        add(construirPanelCentral(), BorderLayout.CENTER);
        add(construirBotonera(), BorderLayout.PAGE_END);

        configurarAcciones();
        cargarCatalogos();
        catalogoActual = catalogosDisponibles.get(0);
        reconstruirCatalogo(catalogoActual);

        pack();
        setLocationRelativeTo(null);
    }

    private JPanel construirPanelSuperior() {
        var pnlCatalogo = createComponentsPanel(
                new JLabel(resBundle.getString("catalogo")), cmbCatalogos);
        var pnlBusqueda = createComponentsPanel(
                new JLabel(resBundle.getString("campo.busqueda")), cmbCampos,
                new JLabel(resBundle.getString("valor.busqueda")), txtValor,
                botBuscar, botLimpiar);
        var salida = createVerticalBoxPanel();
        agregarComponentes(salida, pnlCatalogo, pnlBusqueda);
        return salida;
    }

    private JPanel construirPanelCentral() {
        pnlCentroLista.setLayout(new BorderLayout());
        var pnlPaginacion = createComponentsPanel(
                botAnterior, lblPagina, botSiguiente,
                new JLabel(resBundle.getString("tamano_pagina")), cmbTamano);

        var pnlListado = new JPanel(new BorderLayout());
        pnlListado.add(pnlCentroLista, BorderLayout.CENTER);
        pnlListado.add(pnlPaginacion, BorderLayout.PAGE_END);

        var pnlFormulario = new JPanel(new BorderLayout());
        pnlFormulario.add(scrollFormulario, BorderLayout.CENTER);

        var separador = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, pnlListado, pnlFormulario);
        separador.setResizeWeight(0.4);

        return crearContenedorCentral(separador);
    }

    private JPanel crearContenedorCentral(JSplitPane separador) {
        var salida = new JPanel(new BorderLayout());
        salida.add(separador, BorderLayout.CENTER);
        return salida;
    }

    private JPanel construirBotonera() {
        return createComponentsPanel(
                botNuevo, botEditar, botBorrar, botAceptar, botCancelar, botSalir);
    }

    private void configurarAcciones() {
        cmbCatalogos.addActionListener(e -> cambiarCatalogo());
        for (String tamano : tamanosPagina) {
            cmbTamano.addItem(tamano);
        }
        cmbTamano.setSelectedItem("5");
        cmbTamano.addActionListener(e -> notificarTamanoPagina());

        botBuscar.addActionListener(e -> buscarConCampos());
        botLimpiar.addActionListener(e -> {
            if (controlador != null) {
                controlador.limpiarBusqueda();
            }
        });
        botNuevo.addActionListener(e -> {
            if (controlador != null) {
                controlador.nuevo();
            }
        });
        botEditar.addActionListener(e -> {
            if (controlador != null) {
                controlador.editar();
            }
        });
        botBorrar.addActionListener(e -> {
            if (controlador != null) {
                controlador.borrar();
            }
        });
        botAceptar.addActionListener(e -> {
            if (controlador != null) {
                controlador.aceptar();
            }
        });
        botCancelar.addActionListener(e -> {
            if (controlador != null) {
                controlador.cancelar();
            }
        });
        botAnterior.addActionListener(e -> {
            if (controlador != null) {
                controlador.irPaginaAnterior();
            }
        });
        botSiguiente.addActionListener(e -> {
            if (controlador != null) {
                controlador.irPaginaSiguiente();
            }
        });
        botSalir.addActionListener(e -> System.exit(ERR_CODE_NO_ERROR));
    }

    private void cargarCatalogos() {
        for (String nombreCatalogo : catalogosDisponibles) {
            cmbCatalogos.addItem(resBundle.getString(nombreCatalogo));
        }
    }

    private void cambiarCatalogo() {
        int indice = cmbCatalogos.getSelectedIndex();
        if (indice < 0) {
            return;
        }
        String nuevoCatalogo = catalogosDisponibles.get(indice);
        if (nuevoCatalogo.equals(catalogoActual)) {
            return;
        }
        if (reconstruirCatalogo(nuevoCatalogo)) {
            catalogoActual = nuevoCatalogo;
        } else {
            cmbCatalogos.setSelectedItem(resBundle.getString(catalogoActual));
            JOptionPane.showMessageDialog(this, resBundle.getString("catalogo.desconocido"));
        }
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private boolean reconstruirCatalogo(String nombreCatalogo) {
        var servicio = DaoServiceFactory.getServicio(nombreCatalogo);
        var validador = ValidadorEntidadFactory.crearValidador(nombreCatalogo);
        var panelOptional = createPanelShowEntity(nombreCatalogo);
        var configCampos = CamposBusquedaConfig.campos(nombreCatalogo);
        if (servicio.isEmpty() || validador.isEmpty() || panelOptional.isEmpty() || configCampos.isEmpty()) {
            return false;
        }

        PanelShowEntity panelFormulario = panelOptional.get();
        PanelListEntidades nuevoListado = new PanelListEntidades();
        nuevoListado.setPreferredSize(new Dimension(280, 320));

        pnlCentroLista.removeAll();
        pnlCentroLista.add(nuevoListado, BorderLayout.CENTER);
        scrollFormulario.setViewportView(panelFormulario);
        pnlCentroLista.revalidate();
        repaint();

        clavesCamposBusqueda = List.copyOf(configCampos.keySet());
        cmbCampos.removeAllItems();
        for (String clave : clavesCamposBusqueda) {
            cmbCampos.addItem(resBundle.getString(clave));
        }
        txtValor.setText("");

        CatalogoUnicoController nuevoControlador = new CatalogoUnicoController(
                nombreCatalogo, servicio.get(), validador.get(), panelFormulario, nuevoListado, this);
        controlador = nuevoControlador;
        return true;
    }

    private void buscarConCampos() {
        if (controlador == null) {
            return;
        }
        int indice = cmbCampos.getSelectedIndex();
        if (indice < 0) {
            return;
        }
        String claveCampo = clavesCamposBusqueda.get(indice);
        controlador.buscar(claveCampo, txtValor.getText());
    }

    private void notificarTamanoPagina() {
        if (controlador == null) {
            return;
        }
        Object seleccion = cmbTamano.getSelectedItem();
        if (seleccion != null) {
            controlador.cambiarTamanoPagina(Integer.parseInt(seleccion.toString()));
        }
    }

    public void aplicarModo(CatalogoUnicoController.Modo modo) {
        switch (modo) {
            case ALTA -> {
                botNuevo.setEnabled(true);
                botEditar.setEnabled(false);
                botBorrar.setEnabled(false);
                botAceptar.setEnabled(true);
                botCancelar.setEnabled(true);
            }
            case DETALLE -> {
                botNuevo.setEnabled(true);
                botEditar.setEnabled(true);
                botBorrar.setEnabled(true);
                botAceptar.setEnabled(false);
                botCancelar.setEnabled(false);
            }
            case EDICION -> {
                botNuevo.setEnabled(false);
                botEditar.setEnabled(false);
                botBorrar.setEnabled(false);
                botAceptar.setEnabled(true);
                botCancelar.setEnabled(true);
            }
        }
    }

    public void actualizarPaginacion(String etiquetaPagina, boolean habilitadoAnterior, boolean habilitadoSiguiente) {
        lblPagina.setText(etiquetaPagina);
        botAnterior.setEnabled(habilitadoAnterior);
        botSiguiente.setEnabled(habilitadoSiguiente);
    }

    public void limpiarCampoValor() {
        txtValor.setText(TXT_VACIO);
    }

    public void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje);
    }

    public boolean confirmarBorrado(String mensaje) {
        int respuesta = JOptionPane.showConfirmDialog(
                this, mensaje, resBundle.getString(BOT_BORRAR), JOptionPane.YES_NO_OPTION);
        return respuesta == JOptionPane.YES_OPTION;
    }
}