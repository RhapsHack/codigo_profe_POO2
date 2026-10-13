package mx.edu.uaz.ingsoft.poo2.vista.ventanas.actualizacion;


import mx.edu.uaz.ingsoft.poo2.modelo.entidades.IEntidad;
import mx.edu.uaz.ingsoft.poo2.modelo.entidades.ProgLanguages;
import static mx.edu.uaz.ingsoft.poo2.utils.Constantes.CAT_LANG_PROG;

public class VentanaUpdateLangProg<T extends IEntidad> extends AbstractVentanaUpdateEntidad<ProgLanguages> {
    public VentanaUpdateLangProg(String title) {
        super(title, CAT_LANG_PROG);
    }

    @Override
    public void actualizarCamposEntidad(ProgLanguages original, ProgLanguages nuevo) {
        original.setNombre(nuevo.getNombre());
        original.setDescripcion(nuevo.getDescripcion());
        original.setParadigma(nuevo.getParadigma());
        original.setVersionReferencia(nuevo.getVersionReferencia());
    }
}
