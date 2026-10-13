package mx.edu.uaz.ingsoft.poo2.vista.ventanas.capacitaciones;

import mx.edu.uaz.ingsoft.poo2.controlador.capacitaciones.InscripcionesController;
import mx.edu.uaz.ingsoft.poo2.modelo.daos.DAOException;
import mx.edu.uaz.ingsoft.poo2.modelo.entidades.Curso;
import mx.edu.uaz.ingsoft.poo2.modelo.entidades.Inscripcion;
import mx.edu.uaz.ingsoft.poo2.modelo.entidades.Persona;
import mx.edu.uaz.ingsoft.poo2.modelo.entidades.ResultadoInscripcion;
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
 * Pantalla de gestión de inscripciones: inscribe una persona en un curso con
 * fecha (resultado inicial PENDIENTE), impide duplicados y permite cambiar el
 * resultado de la evaluación. Los errores se informan en diálogos.
 */
public class VentanaInscripciones extends JFrame {

    private final ResourceBundle resBundle;
    private final InscripcionesController controlador;

    private final JComboBox<Persona> comboPersona = new JComboBox<>();
    private final JComboBox<Curso> comboCurso = new JComboBox<>();
    private final JTextField txtFecha = new JTextField("2026-01-01");
    private final JComboBox<ResultadoInscripcion> comboResultado =
            new JComboBox<>(ResultadoInscripcion.values());
    private final DefaultListModel<Inscripcion> modeloLista = new DefaultListModel<>();
    private final JList<Inscripcion> lista = new JList<>(modeloLista);

    public VentanaInscripciones() {
        this(new InscripcionesController());
    }

    public VentanaInscripciones(InscripcionesController controlador) {
        super("Gestión de Inscripciones");
        this.controlador = controlador;
        resBundle = MensajesResourceBundle.MENSAJES.getResourceBundle();
        setTitle(resBundle.getString("ventana.title.gestion.inscripciones"));
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        construirUI();
        recargarCombos();
        recargarLista();
        pack();
        setLocationRelativeTo(null);
    }

    private void construirUI() {
        var pnlFormulario = new JPanel(new GridLayout(0, 2, 5, 5));
        pnlFormulario.add(new JLabel(resBundle.getString("persona")));
        pnlFormulario.add(comboPersona);
        pnlFormulario.add(new JLabel(resBundle.getString("curso")));
        pnlFormulario.add(comboCurso);
        pnlFormulario.add(new JLabel(resBundle.getString("fecha")));
        pnlFormulario.add(txtFecha);
        pnlFormulario.add(new JLabel(resBundle.getString("resultado")));
        pnlFormulario.add(comboResultado);

        var botInscribir = new JButton(resBundle.getString("inscribir"));
        var botCambiar = new JButton(resBundle.getString("cambiar.resultado"));
        var botSalir = new JButton(resBundle.getString("salir"));
        var botonera = new JPanel();
        botonera.add(botInscribir);
        botonera.add(botCambiar);
        botonera.add(botSalir);

        botInscribir.addActionListener(e -> inscribir());
        botCambiar.addActionListener(e -> cambiarResultado());
        botSalir.addActionListener(e -> dispose());

        lista.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && lista.getSelectedValue() != null) {
                comboResultado.setSelectedItem(lista.getSelectedValue().getResultado());
            }
        });

        add(pnlFormulario, BorderLayout.PAGE_START);
        add(new JScrollPane(lista), BorderLayout.CENTER);
        add(botonera, BorderLayout.PAGE_END);
    }

    private void recargarCombos() {
        comboPersona.removeAllItems();
        for (Persona persona : controlador.personas()) {
            comboPersona.addItem(persona);
        }
        comboCurso.removeAllItems();
        for (Curso curso : controlador.cursos()) {
            comboCurso.addItem(curso);
        }
    }

    private void recargarLista() {
        modeloLista.removeAllElements();
        for (Inscripcion inscripcion : controlador.inscripciones()) {
            modeloLista.addElement(inscripcion);
        }
    }

    private void inscribir() {
        var persona = (Persona) comboPersona.getSelectedItem();
        var curso = (Curso) comboCurso.getSelectedItem();
        if (persona == null || curso == null) {
            mostrarError(resBundle.getString("inscripcion.error.persona_curso_inexistentes"));
            return;
        }
        LocalDate fecha = leerFecha();
        if (fecha == null) {
            mostrarError("Fecha inválida. Use el formato aaaa-mm-dd.");
            return;
        }
        try {
            controlador.inscribir(persona, curso, fecha);
            recargarLista();
            mostrar("Inscripción registrada con resultado PENDIENTE.");
        } catch (ReglaNegocioException e) {
            mostrarError("Regla de negocio: " + e.getMessage());
        } catch (DAOException e) {
            mostrarError("Error de persistencia: " + e.getMessage());
        }
    }

    private void cambiarResultado() {
        var inscripcion = lista.getSelectedValue();
        if (inscripcion == null) {
            mostrarError("Seleccione una inscripción de la lista.");
            return;
        }
        try {
            controlador.cambiarResultado(inscripcion,
                    (ResultadoInscripcion) comboResultado.getSelectedItem());
            recargarLista();
            mostrar("Resultado actualizado.");
        } catch (DAOException e) {
            mostrarError("Error de persistencia: " + e.getMessage());
        }
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