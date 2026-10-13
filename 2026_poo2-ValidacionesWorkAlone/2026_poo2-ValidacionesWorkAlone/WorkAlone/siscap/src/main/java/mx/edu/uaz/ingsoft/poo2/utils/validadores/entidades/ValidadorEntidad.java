package mx.edu.uaz.ingsoft.poo2.utils.validadores.entidades;




import mx.edu.uaz.ingsoft.poo2.modelo.entidades.IEntidad;
import mx.edu.uaz.ingsoft.poo2.utils.validadores.interfaces.IValidador;
import mx.edu.uaz.ingsoft.poo2.utils.validadores.interfaces.IValidadorEntidad;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public abstract class ValidadorEntidad<T extends IEntidad> implements IValidadorEntidad<T> {

    protected T entidad;
    protected List<IValidador> validadoresCampos;
    protected Map<String, IValidador> mapaValidadores;

    public ValidadorEntidad() {
        this.entidad = null;
        validadoresCampos = new ArrayList<>();
        mapaValidadores = new HashMap<>();

    }

    @Override
    public boolean validar(T entidad){
        boolean resultadoValidacion = true;
        this.entidad = entidad;
        asignarValoresAValidadores();
        for(var validador:validadoresCampos){
            if(! validador.validar()){
                resultadoValidacion = false;
            }
        }
        return resultadoValidacion;
    }

    @Override
    public void agregarValidador(String nombreCampo, IValidador validador){
        mapaValidadores.put(nombreCampo,validador);
        validadoresCampos.add(validador);
    }

    @Override
    public List<String> getErrores(){
        var listaErrores = new ArrayList<String>();
        validadoresCampos.forEach(unValidador->{
            listaErrores.addAll(unValidador.getErrores());
        });
        return listaErrores;
    }
    
    @Override
    public abstract void asignarValoresAValidadores();

}
