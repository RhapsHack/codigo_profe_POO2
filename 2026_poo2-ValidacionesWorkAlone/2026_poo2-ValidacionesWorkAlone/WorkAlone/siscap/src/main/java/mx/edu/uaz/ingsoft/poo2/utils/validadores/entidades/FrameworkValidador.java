package mx.edu.uaz.ingsoft.poo2.utils.validadores.entidades;


import mx.edu.uaz.ingsoft.poo2.modelo.entidades.Framework;
import mx.edu.uaz.ingsoft.poo2.utils.FunctionUtils;
import mx.edu.uaz.ingsoft.poo2.utils.validadores.ValidadorString;

public class FrameworkValidador extends  ValidadorEntidad<Framework> {
    private static final String NOMBRE = "nombre";
    private static final String TIPO = "tipo";
    private static final String LENGUAJE_BASE = "lenguaje_base";
    private static final String DESCRIPCION = "descripcion";

    public FrameworkValidador() {
        super();
        agregarValidador(NOMBRE,new ValidadorString()
                .min(4)
                .max(20)
                .soloAlfabeticasYBlanco());
        agregarValidador(LENGUAJE_BASE,new ValidadorString()
                .min(4)
                .max(20)
                .soloAlfabeticasYBlanco());
        agregarValidador(DESCRIPCION,new ValidadorString()
                .min(5)
                .soloAlfabeticasYBlanco());
        agregarValidador(TIPO,new ValidadorString()
                .min(2)
                .max(10)
                .soloAlfabeticasYBlanco());
    }


    @Override
    public void asignarValoresAValidadores() {
        if(FunctionUtils.noEsNulo(entidad)){
            mapaValidadores.get(NOMBRE).setValor(entidad.getNombre());
            mapaValidadores.get(DESCRIPCION).setValor(entidad.getDescripcion());
            mapaValidadores.get(LENGUAJE_BASE).setValor(entidad.getLenguajeBase());
            mapaValidadores.get(TIPO).setValor(entidad.getTipo());
        }
    }

}
