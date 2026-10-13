package mx.edu.uaz.ingsoft.poo2.modelo.dtos;


import mx.edu.uaz.ingsoft.poo2.controlador.AbstractControllerBase;
import mx.edu.uaz.ingsoft.poo2.vista.ventanas.gestion.AbstractVentanaGestionCatalogo;

import javax.swing.*;


public class VentanaGestionDTO {
    private AbstractVentanaGestionCatalogo ventanaGestionCatalogo;
    private AbstractControllerBase controllerBase;
    private JButton botonCatalogo;

    public VentanaGestionDTO(AbstractVentanaGestionCatalogo ventana, AbstractControllerBase controller, JButton boton){
        ventanaGestionCatalogo = ventana;
        controllerBase = controller;
        botonCatalogo = boton;
    }
    public AbstractVentanaGestionCatalogo getVentanaGestionCatalogo() {
        return ventanaGestionCatalogo;
    }

    public void setVentanaGestionCatalogo(AbstractVentanaGestionCatalogo ventanaGestionCatalogo) {
        this.ventanaGestionCatalogo = ventanaGestionCatalogo;
    }

    public AbstractControllerBase getControllerBase() {
        return controllerBase;
    }

    public void setControllerBase(AbstractControllerBase controllerBase) {
        this.controllerBase = controllerBase;
    }


    public JButton getBotonCatalogo() {
        return botonCatalogo;
    }

    public void setBotonCatalogo(JButton botonCatalogo) {
        this.botonCatalogo = botonCatalogo;
    }
}
