package mx.edu.uaz.ingsoft.poo2.vista.ventanas.borrado;


import mx.edu.uaz.ingsoft.poo2.modelo.entidades.IEntidad;
import mx.edu.uaz.ingsoft.poo2.vista.ventanas.listado.AbstractVentanaListadoEntidades;
import static mx.edu.uaz.ingsoft.poo2.vista.utils.SwingComponentUtils.setActionListenerToButton;

public abstract class AbstractVentanaBorradoEntidad<T extends IEntidad> extends AbstractVentanaListadoEntidades<T> {

    public AbstractVentanaBorradoEntidad(String title) {
        super(title,"Borrar","Cancelar");
        setActionListenerToButton(listaBotones,"Cancelar",e->this.setVisible(false));
    }

}
