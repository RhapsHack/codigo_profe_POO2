package mx.edu.uaz.ingsoft.poo2.vista.ventanas;



import mx.edu.uaz.ingsoft.poo2.controlador.utils.ControllerFactory;
import mx.edu.uaz.ingsoft.poo2.modelo.dtos.VentanaGestionDTO;
import mx.edu.uaz.ingsoft.poo2.modelo.utils.MensajesResourceBundle;
import mx.edu.uaz.ingsoft.poo2.vista.ventanas.utils.VentanasFactory;

import javax.swing.*;
import java.awt.*;
import java.util.ResourceBundle;
import java.util.logging.Logger;
import static mx.edu.uaz.ingsoft.poo2.utils.Constantes.*;
import static mx.edu.uaz.ingsoft.poo2.vista.utils.SwingComponentUtils.agregarComponentes;


/*
Esta clase tiene el listado de los catálogos que se pueden administrar.
Muestra un botón por cada uno de los catálogos disponibles
 */
public class PrincipalCatalogos extends JFrame {
    private final ResourceBundle resBundle;



    public PrincipalCatalogos() {
        super();
        resBundle = MensajesResourceBundle.MENSAJES.getResourceBundle();
        setTitle(resBundle.getString("gestion.catalogos"));
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel pnlBotonesCatalogos = new JPanel();
        JPanel pnlBotAcciones = new JPanel();

        var gestionFramework = inicializarVentanaGestion(CAT_FRAMEWORK);
        var gestionPersonas = inicializarVentanaGestion(CAT_PEOPLE);
        var gestionLenguajesProgramacion = inicializarVentanaGestion(CAT_LANG_PROG);

        agregarComponentes(
                pnlBotonesCatalogos,
                gestionFramework.getBotonCatalogo(),
                gestionPersonas.getBotonCatalogo(),
                gestionLenguajesProgramacion.getBotonCatalogo()
        );

        var botSalir = new JButton(resBundle.getString("salir"));
        botSalir.addActionListener(e->System.exit(ERR_CODE_NO_ERROR));

        pnlBotAcciones.add(botSalir);

        add(pnlBotonesCatalogos, BorderLayout.CENTER);
        add(pnlBotAcciones, BorderLayout.PAGE_END);

        pack();
        setLocationRelativeTo(null);
    }


    private VentanaGestionDTO inicializarVentanaGestion(String nombreCatalogo){
        var ventanaGestionOptional = VentanasFactory.crearVentanaGestionCatalogo(nombreCatalogo);
        if(ventanaGestionOptional.isEmpty()){
            Logger.getAnonymousLogger().severe(resBundle.getString("error.ventana.no.creada"));
            System.exit(ERR_CODE_VENTANA_NO_CREADA);
        }
        var ventanaGestionCatalogo = ventanaGestionOptional.get();

        var ventanaCaptura = ventanaGestionCatalogo.getVentanaCapturaEntidad();
        var ventanaListado = ventanaGestionCatalogo.getVentanaListado();
        var ventanaBorrado = ventanaGestionCatalogo.getVentanaBorradoEntidad();
        var ventanaUpdate = ventanaGestionCatalogo.getVentanaUpdateEntidad();

        var nuevoController = ControllerFactory.createEntityController(
                nombreCatalogo,
                ventanaCaptura,
                ventanaListado,
                ventanaBorrado,
                ventanaUpdate
        );
        if(nuevoController.isEmpty()){
            Logger.getAnonymousLogger().severe(ERR_CONTROLLER_NO_CREADO);
            System.exit(ERR_CODE_CONTROLLER_NO_CREADO);
        }
        var controladorCatalogo = nuevoController.get();

        ventanaGestionCatalogo.addBotonAgregarListener(e->controladorCatalogo.mostrarVentanaNuevoRegistro());
        ventanaGestionCatalogo.addBotonListarListener(e->controladorCatalogo.mostrarVentanaListado());
        ventanaGestionCatalogo.addBotonBorrarListener(e->controladorCatalogo.mostrarVentanaBorrado());
        ventanaGestionCatalogo.addBotonEditarListener(e->controladorCatalogo.mostrarVentanaEdicion());
        ventanaListado.setAccionRegresar(evento->ventanaListado.setVisible(false));
        ventanaCaptura.setAccionCancelar(evento->ventanaCaptura.setVisible(false));
        ventanaBorrado.setAccionRegresar(e->ventanaBorrado.setVisible(false));

        var botCatalogo = new JButton(resBundle.getString(nombreCatalogo));
        botCatalogo.addActionListener(e->ventanaGestionCatalogo.setVisible(true));

        return new VentanaGestionDTO(ventanaGestionCatalogo,controladorCatalogo,botCatalogo);
    }

}
