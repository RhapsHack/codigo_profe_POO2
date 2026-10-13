package mx.edu.uaz.ingsoft.poo2.utils.validadores;



import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

import mx.edu.uaz.ingsoft.poo2.utils.FunctionUtils;
import mx.edu.uaz.ingsoft.poo2.utils.validadores.interfaces.IValidador;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractValidador<T extends Comparable,U extends Comparable>
        implements IValidador<T,U> {

    private static final String VALOR_EVALUAR_NULO = "El valor a evaluar es nulo";
    private static final String NO_ES_NULO = "El valor a evaluar no es nulo";
    
    private static final String ERR_VALOR_NULO = "Se envió un valor NULO";
    
    private final List<Predicate<T>> validaciones;
    private final List<String> textosErroresValidacion;
    
    private T valorAEvaluar;

    private final Logger logger;
    
    private final List<String> listaErrores;

    protected AbstractValidador(){
        logger = LoggerFactory.getLogger(AbstractValidador.class);
        valorAEvaluar = null;
        validaciones = new ArrayList<>();
        textosErroresValidacion = new ArrayList<>();
        listaErrores = new ArrayList<>();
        addCondicion(FunctionUtils::noEsNulo,NO_ES_NULO);
    }

    @Override
    public void setValor(T nuevoValor){
        this.valorAEvaluar = nuevoValor;
    }

    @Override
    public T getValor(){
        return valorAEvaluar;
    }

    @Override
    public boolean validar(){  
        limpiarErrores();
        if(validaciones.isEmpty())
            return true;
        if(FunctionUtils.esNulo(valorAEvaluar)){
            agregarErrorValidacion(VALOR_EVALUAR_NULO,null);
            return false;
        }
        for(var condicion:validaciones){
            if( ! condicion.test(valorAEvaluar) ) {
                var indice = validaciones.indexOf(condicion);                
                agregarErrorValidacion(textosErroresValidacion.get(indice),valorAEvaluar);                
                return false;
            }
        }
        return true;
    }
    
    private void agregarErrorValidacion(String mensajeErrorValidacion, T value){
        listaErrores.add(mensajeErrorValidacion);
        if(Objects.isNull(value)){
            logger.error(ERR_VALOR_NULO);
        }else{
            logger.error("Valor =>'{}', error =>'{}'",value,mensajeErrorValidacion);
        }   
    }

    @Override
    public final void addCondicion(Predicate<T> condicion, String textoError){
        if( FunctionUtils.noEsNulo(condicion)){
            validaciones.add(condicion);
            textosErroresValidacion.add(textoError);
        }
    }
    
    @Override
    public IValidador<T,U> between(U valorMinimo, U valorMaximo){
        min(valorMinimo);
        max(valorMaximo);
        return this;
    }

    @Override
    public IValidador<T,U> min(U valorMinimo){
        addCondicion(valor -> valor.compareTo(valorMinimo) >= 0, String.format("El valor minimo debe ser %d",valorMinimo));
        return this;
    }
    @Override
    public  IValidador<T,U> max(U valorMaximo){
        addCondicion(valor -> valor.compareTo(valorMaximo) <= 0,String.format("El valor maximo debe ser %d",valorMaximo));
        return this;
    }

    @Override
    public void limpiarErrores(){
        listaErrores.clear();
    }

    @Override
    public IValidador<T,U> or(Predicate<T> condicion1, Predicate<T> condicion2, String textoError){
        addCondicion( condicion1.or(condicion2),textoError);
        return this;
    }
    @Override
    public IValidador<T,U> and(Predicate<T> condicion1, Predicate<T> condicion2, String textoError){
        addCondicion( condicion1.and(condicion2),textoError);
        return this;
    }
    @Override
    public List<String> getErrores(){
        return listaErrores.subList(0, listaErrores.size());
    }
}
