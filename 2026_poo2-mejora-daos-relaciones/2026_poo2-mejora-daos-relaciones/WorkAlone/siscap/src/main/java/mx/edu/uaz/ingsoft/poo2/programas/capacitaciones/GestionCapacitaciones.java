package mx.edu.uaz.ingsoft.poo2.programas.capacitaciones;

import mx.edu.uaz.ingsoft.poo2.modelo.utils.MensajesResourceBundle;
import mx.edu.uaz.ingsoft.poo2.vista.ventanas.capacitaciones.VentanaCertificados;
import mx.edu.uaz.ingsoft.poo2.vista.ventanas.capacitaciones.VentanaCursos;
import mx.edu.uaz.ingsoft.poo2.vista.ventanas.capacitaciones.VentanaInscripciones;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.GridLayout;

/**
 * Programa de escritorio que integra y abre las tres pantallas del sistema de
 * capacitaciones: cursos, inscripciones y certificados.
 */
public class GestionCapacitaciones extends JFrame {

    public GestionCapacitaciones() {
        super("Sistema de Capacitaciones");
        setTitle(MensajesResourceBundle.MENSAJES.getResourceBundle()
                .getString("ventana.title.gestion.capacitaciones"));
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        var botCursos = new JButton("Cursos");
        var botInscripciones = new JButton("Inscripciones");
        var botCertificados = new JButton("Certificados");
        var botSalir = new JButton("Salir");

        botCursos.addActionListener(e -> new VentanaCursos().setVisible(true));
        botInscripciones.addActionListener(e -> new VentanaInscripciones().setVisible(true));
        botCertificados.addActionListener(e -> new VentanaCertificados().setVisible(true));
        botSalir.addActionListener(e -> System.exit(0));

        var pnlBotones = new JPanel(new GridLayout(0, 1, 5, 5));
        pnlBotones.add(botCursos);
        pnlBotones.add(botInscripciones);
        pnlBotones.add(botCertificados);

        add(pnlBotones, BorderLayout.CENTER);
        add(botSalir, BorderLayout.PAGE_END);

        pack();
        setLocationRelativeTo(null);
    }

    public static void main(String[] args) {
        new GestionCapacitaciones().setVisible(true);
    }
}