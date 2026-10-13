package mx.edu.uaz.ingsoft.poo2.vista.paneles;


import mx.edu.uaz.ingsoft.poo2.modelo.entidades.Tema;
import mx.edu.uaz.ingsoft.poo2.modelo.entidades.TipoTema;

import java.util.Locale;
import java.util.Optional;

import static mx.edu.uaz.ingsoft.poo2.utils.Constantes.TXT_VACIO;
import static mx.edu.uaz.ingsoft.poo2.utils.FunctionUtils.esNulo;
import static mx.edu.uaz.ingsoft.poo2.utils.FunctionUtils.stringValueFromOptional;

public class PanelShowTema extends PanelShowEntity<Tema> {

    public PanelShowTema() {
        super();
    }

    @Override
    public void construirFormulario() {
        agregarCampoTexto(resBundle.getString("nombre"));
        agregarCampoTexto(resBundle.getString("tipo"));
    }

    @Override
    public void limpiarFormulario() {
        campos.values().forEach(unCampoTexto -> unCampoTexto.setText(TXT_VACIO));
    }

    @Override
    public Tema crearEntidadDesdeFormulario() {
        entidad = new Tema();
        actualizarEntidadDesdeFormulario(entidad);
        return entidad;
    }

    @Override
    public void actualizarEntidadDesdeFormulario(Tema entidad) {
        entidad.setNombre(stringValueFromOptional(getStringValue(resBundle.getString("nombre"))));
        entidad.setTipo(parsearTipo(getStringValue(resBundle.getString("tipo"))));
    }

    @Override
    public void llenarFormularioDesdeEntidad() {
        var entidadActual = getEntidad();
        if (esNulo(entidadActual)) {
            entidadActual = new Tema();
            setEntidad(entidadActual);
        }
        setStringEnCampo(resBundle.getString("nombre"), entidadActual.getNombre());
        setStringEnCampo(resBundle.getString("tipo"),
                entidadActual.getTipo() == null ? TXT_VACIO : entidadActual.getTipo().name());
    }

    private TipoTema parsearTipo(Optional<String> texto) {
        if (texto.isEmpty() || texto.get().isBlank()) {
            return null;
        }
        try {
            return TipoTema.valueOf(texto.get().trim().toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}