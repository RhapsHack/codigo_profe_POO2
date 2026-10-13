package mx.edu.uaz.ingsoft.poo2.vista.ventanas.actualizacion;



import mx.edu.uaz.ingsoft.poo2.modelo.entidades.IEntidad;
import mx.edu.uaz.ingsoft.poo2.vista.paneles.PanelListEntidades;
import mx.edu.uaz.ingsoft.poo2.vista.ventanas.captura.AbstractVentanaCapturaEntidad;

import java.awt.BorderLayout;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

public abstract class AbstractVentanaUpdateEntidad<T extends IEntidad>
        extends AbstractVentanaCapturaEntidad<T> {
    private final Logger log = Logger.getLogger("AbsVenUpdate");
    private PanelListEntidades<T> panelListEntidades;

    public AbstractVentanaUpdateEntidad(String title, String nombreCatalogo) {
        super(title, nombreCatalogo);
        panelListEntidades = new PanelListEntidades<>();
        agregarPanelListadoRegistros();
    }
    private void agregarPanelListadoRegistros(){
        add(panelListEntidades, BorderLayout.LINE_START);
        panelListEntidades.establecerListListener(
                e -> {
                    var seleccionado = panelListEntidades.getItemSelected();
                    if (seleccionado.isPresent()){
                        setEntidad(seleccionado.get());
                        log.info(seleccionado.toString());
                    }
                }
        );
    }
    public void establecerListadoRegistros(List<T> registros){
        panelListEntidades.establecerListaEntidades(registros);
    }

    public Optional<T> getEntidad(){
        var entidadOp = super.getEntidadActual();
        var entidadFormOp = getEntidadFormulario();
        if(entidadOp.isPresent() && entidadFormOp.isPresent()){
            var entidadUpdate = entidadOp.get();
            var entidadFormulario = entidadFormOp.get();
            actualizarCamposEntidad(entidadUpdate,entidadFormulario);
            return Optional.of(entidadUpdate);
        }
        return Optional.empty();
    }
    public abstract void actualizarCamposEntidad(T original, T nuevo);
}
