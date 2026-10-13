package mx.edu.uaz.ingsoft.poo2.utils.validadores.entidades;


import mx.edu.uaz.ingsoft.poo2.modelo.entidades.ProgLanguages;
import mx.edu.uaz.ingsoft.poo2.utils.validadores.ValidadorString;

import static mx.edu.uaz.ingsoft.poo2.utils.FunctionUtils.noEsNulo;

public class LangProgValidador extends  ValidadorEntidad<ProgLanguages> {

    /*
    @Column(name = "nombre")
    private String nombre;

    @Column(name = "paradigma")
    private String paradigma;

    @Column(name = "version_referencia")
    private String versionReferencia;

    @Column(name = "descripcion")
    private String descripcion;
     */
    private static final String NOMBRE = "nombre";
    private static final String PARADIGMA = "paradigma";
    private static final String VERSION_REFERENCIA = "version_referencia";
    private static final String DESCRIPCION = "descripcion";

    public LangProgValidador() {
        super();
        agregarValidador(NOMBRE,new ValidadorString()
                .noVacia()
                .between(2,100));
        agregarValidador(PARADIGMA,new ValidadorString()
                .noVacia()
                .between(2,100));
        agregarValidador(VERSION_REFERENCIA,new ValidadorString()
                .noVacia()
                .between(2,100));
        agregarValidador(DESCRIPCION,new ValidadorString()
                .noVacia()
                .between(2,100));
    }

    @Override
    public void asignarValoresAValidadores() {
        if(noEsNulo(entidad)){
            mapaValidadores.get(NOMBRE).setValor(entidad.getNombre());
            mapaValidadores.get(PARADIGMA).setValor(entidad.getParadigma());
            mapaValidadores.get(VERSION_REFERENCIA).setValor(entidad.getVersionReferencia());
            mapaValidadores.get(DESCRIPCION).setValor(entidad.getDescripcion());
        }
    }

}
