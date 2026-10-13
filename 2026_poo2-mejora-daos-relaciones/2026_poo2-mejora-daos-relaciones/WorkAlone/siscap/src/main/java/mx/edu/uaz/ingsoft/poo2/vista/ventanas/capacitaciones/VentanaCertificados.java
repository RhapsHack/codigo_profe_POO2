package mx.edu.uaz.ingsoft.poo2.vista.ventanas.capacitaciones;

import mx.edu.uaz.ingsoft.poo2.controlador.capacitaciones.CertificadosController;
import mx.edu.uaz.ingsoft.poo2.modelo.daos.DAOException;
import mx.edu.uaz.ingsoft.poo2.modelo.entidades.Certificado;
import mx.edu.uaz.ingsoft.poo2.modelo.entidades.Inscripcion;
import mx.edu.uaz.ingsoft.poo2.modelo.utils.MensajesResourceBundle;
import mx.edu.uaz.ingsoft.poo2.servicios.ReglaNegocioException;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ResourceBundle;

/**
 * Pantalla de gestión de certificados: emite el certificado de una inscripción
 * (solo si está aprobada y aún no tiene uno) y permite consultarlo. Los errores
 * se informan en diálogos.
 */
public class VentanaCertificados extends JFrame {

    private final ResourceBundle resBundle;
    private final CertificadosController controlador;

    private final JComboBox<Inscripcion> comboInscripcion = new JComboBox<>();
    private final JTextField txtFolio = new JTextField();
    private final JTextField txtFecha = new JTextField();
    private final DefaultListModel<Certificado> modeloLista = new DefaultListModel<>();
    private final JList<Certificado> lista = new JList<>(modeloLista);

    public VentanaCertificados() {
        this(new CertificadosController());
    }

    public VentanaCertificados(CertificadosController controlador) {
        super("Gestión de Certificados");
        this.controlador = controlador;
        resBundle = MensajesResourceBundle.MENSAJES.getResourceBundle();
        setTitle(resBundle.getString("ventana.title.gestion.certificados"));
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        construirUI();
        recargarCombos();
        recargarLista();
        pack();
        setLocationRelativeTo(null);
    }

    private void construirUI() {
        var pnlFormulario = new JPanel(new GridLayout(0, 2, 5, 5));
        pnlFormulario.add(new JLabel(resBundle.getString("inscripcion")));
        pnlFormulario.add(comboInscripcion);
        pnlFormulario.add(new JLabel(resBundle.getString("folio")));
        pnlFormulario.add(txtFolio);
        pnlFormulario.add(new JLabel(resBundle.getString("fecha.emision")));
        pnlFormulario.add(txtFecha);

        var botEmitir = new JButton(resBundle.getString("emitir"));
        var botConsultar = new JButton(resBundle.getString("consultar"));
        var botSalir = new JButton(resBundle.getString("salir"));
        var botonera = new JPanel();
        botonera.add(botEmitir);
        botonera.add(botConsultar);
        botonera.add(botSalir);

        botEmitir.addActionListener(e -> emitir());
        botConsultar.addActionListener(e -> consultar());
        botSalir.addActionListener(e -> dispose());

        add(pnlFormulario, BorderLayout.PAGE_START);
        add(new JScrollPane(lista), BorderLayout.CENTER);
        add(botonera, BorderLayout.PAGE_END);
    }

    private void recargarCombos() {
        comboInscripcion.removeAllItems();
        for (Inscripcion inscripcion : controlador.inscripciones()) {
            comboInscripcion.addItem(inscripcion);
        }
    }

    private void recargarLista() {
        modeloLista.removeAllElements();
        for (Certificado certificado : controlador.certificados()) {
            modeloLista.addElement(certificado);
        }
    }

    private void emitir() {
        var inscripcion = (Inscripcion) comboInscripcion.getSelectedItem();
        if (inscripcion == null) {
            mostrarError("Seleccione una inscripción.");
            return;
        }
        LocalDate fecha = leerFecha();
        if (fecha == null) {
            mostrarError("Fecha inválida. Use el formato aaaa-mm-dd.");
            return;
        }
        try {
            controlador.emitir(inscripcion, txtFolio.getText(), fecha);
            recargarLista();
            mostrar("Certificado emitido.");
        } catch (ReglaNegocioException e) {
            mostrarError("Regla de negocio: " + e.getMessage());
        } catch (DAOException e) {
            mostrarError("Error de persistencia: " + e.getMessage());
        }
    }

    private void consultar() {
        var inscripcion = (Inscripcion) comboInscripcion.getSelectedItem();
        if (inscripcion == null) {
            mostrarError("Seleccione una inscripción.");
            return;
        }
        controlador.consultar(inscripcion).ifPresentOrElse(
                certificado -> mostrar("Certificado: " + certificado),
                () -> mostrar("Esa inscripción no tiene certificado."));
    }

    private LocalDate leerFecha() {
        try {
            return LocalDate.parse(txtFecha.getText().trim());
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    private void mostrar(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje);
    }

    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }
}