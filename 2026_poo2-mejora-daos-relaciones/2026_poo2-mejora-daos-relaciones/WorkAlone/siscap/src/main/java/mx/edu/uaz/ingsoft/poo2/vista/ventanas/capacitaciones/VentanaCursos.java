package mx.edu.uaz.ingsoft.poo2.vista.ventanas.capacitaciones;

import mx.edu.uaz.ingsoft.poo2.controlador.capacitaciones.CursosController;
import mx.edu.uaz.ingsoft.poo2.modelo.daos.DAOException;
import mx.edu.uaz.ingsoft.poo2.modelo.entidades.Curso;
import mx.edu.uaz.ingsoft.poo2.modelo.entidades.Persona;
import mx.edu.uaz.ingsoft.poo2.modelo.entidades.Tema;
import mx.edu.uaz.ingsoft.poo2.modelo.utils.MensajesResourceBundle;

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
import java.util.ResourceBundle;

/**
 * Pantalla de gestión de cursos: lista los cursos activos, registra un curso
 * nuevo y edita el seleccionado, eligiendo instructor entre las personas y tema
 * entre los temas. Los errores se informan en diálogos.
 */
public class VentanaCursos extends JFrame {

    private final ResourceBundle resBundle;
    private final CursosController controlador;

    private final JComboBox<Persona> comboInstructor = new JComboBox<>();
    private final JComboBox<Tema> comboTema = new JComboBox<>();
    private final JTextField txtNombre = new JTextField();
    private final JTextField txtObjetivo = new JTextField();
    private final DefaultListModel<Curso> modeloLista = new DefaultListModel<>();
    private final JList<Curso> lista = new JList<>(modeloLista);

    public VentanaCursos() {
        this(new CursosController());
    }

    public VentanaCursos(CursosController controlador) {
        super("Gestión de Cursos");
        this.controlador = controlador;
        resBundle = MensajesResourceBundle.MENSAJES.getResourceBundle();
        setTitle(resBundle.getString("ventana.title.gestion.cursos"));
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        construirUI();
        recargarCombos();
        recargarLista();
        pack();
        setLocationRelativeTo(null);
    }

    private void construirUI() {
        var pnlFormulario = new JPanel(new GridLayout(0, 2, 5, 5));
        pnlFormulario.add(new JLabel(resBundle.getString("instructor")));
        pnlFormulario.add(comboInstructor);
        pnlFormulario.add(new JLabel(resBundle.getString("tema")));
        pnlFormulario.add(comboTema);
        pnlFormulario.add(new JLabel(resBundle.getString("nombre")));
        pnlFormulario.add(txtNombre);
        pnlFormulario.add(new JLabel(resBundle.getString("objetivo")));
        pnlFormulario.add(txtObjetivo);

        var botGuardar = new JButton(resBundle.getString("guardar"));
        var botActualizar = new JButton(resBundle.getString("actualizar"));
        var botBorrar = new JButton(resBundle.getString("borrar"));
        var botSalir = new JButton(resBundle.getString("salir"));
        var botonera = new JPanel();
        botonera.add(botGuardar);
        botonera.add(botActualizar);
        botonera.add(botBorrar);
        botonera.add(botSalir);

        botGuardar.addActionListener(e -> guardar());
        botActualizar.addActionListener(e -> actualizar());
        botBorrar.addActionListener(e -> borrar());
        botSalir.addActionListener(e -> dispose());

        lista.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && lista.getSelectedValue() != null) {
                cargarEnFormulario(lista.getSelectedValue());
            }
        });

        add(pnlFormulario, BorderLayout.PAGE_START);
        add(new JScrollPane(lista), BorderLayout.CENTER);
        add(botonera, BorderLayout.PAGE_END);
    }

    private void recargarCombos() {
        comboInstructor.removeAllItems();
        controlador.personas().forEach(comboInstructor::addItem);
        comboTema.removeAllItems();
        for (Tema tema : controlador.temas()) {
            comboTema.addItem(tema);
        }
    }

    private void recargarLista() {
        modeloLista.removeAllElements();
        for (Curso curso : controlador.cursos()) {
            modeloLista.addElement(curso);
        }
    }

    private void guardar() {
        var instructor = (Persona) comboInstructor.getSelectedItem();
        var tema = (Tema) comboTema.getSelectedItem();
        if (instructor == null || tema == null) {
            mostrarError(resBundle.getString("curso.error.instructor_tema_inexistente"));
            return;
        }
        try {
            controlador.guardar(txtNombre.getText(), txtObjetivo.getText(),
                    instructor, tema);
            recargarLista();
            limpiarFormulario();
            mostrar("Curso guardado.");
        } catch (DAOException e) {
            mostrarError("No se pudo guardar (¿nombre duplicado o catálogo inexistente?): "
                    + e.getMessage());
        }
    }

    private void actualizar() {
        var curso = lista.getSelectedValue();
        if (curso == null) {
            mostrarError("Seleccione un curso de la lista.");
            return;
        }
        var instructor = (Persona) comboInstructor.getSelectedItem();
        var tema = (Tema) comboTema.getSelectedItem();
        if (instructor == null || tema == null) {
            mostrarError(resBundle.getString("curso.error.instructor_tema_inexistente"));
            return;
        }
        try {
            controlador.actualizar(curso, txtNombre.getText(), txtObjetivo.getText(),
                    instructor, tema);
            recargarLista();
        } catch (DAOException e) {
            mostrarError("Error de persistencia: " + e.getMessage());
        }
    }

    private void borrar() {
        var curso = lista.getSelectedValue();
        if (curso == null) {
            mostrarError("Seleccione un curso de la lista.");
            return;
        }
        try {
            controlador.borrar(curso);
            recargarLista();
        } catch (DAOException e) {
            mostrarError("Error de persistencia: " + e.getMessage());
        }
    }

    private void cargarEnFormulario(Curso curso) {
        txtNombre.setText(curso.getNombre());
        txtObjetivo.setText(curso.getObjetivo());
        comboInstructor.setSelectedItem(curso.getInstructor());
        comboTema.setSelectedItem(curso.getTema());
    }

    private void limpiarFormulario() {
        txtNombre.setText("");
        txtObjetivo.setText("");
        lista.clearSelection();
    }

    private void mostrar(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje);
    }

    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }
}