package mx.edu.uaz.ingsoft.poo2.utils.validadores.interfaces;

import java.util.List;
import java.util.function.Predicate;

public interface IValidador<T,U> {
    
    boolean validar();
    void setValor(T valor);
    T getValor();
    void addCondicion(Predicate<T> condicion, String textoError);
    IValidador<T,U> min(U valorMinimo);
    IValidador<T,U> max(U valorMaximo);
    IValidador<T,U> between(U valorMinimo, U valorMaximo);
    IValidador<T,U> or(Predicate<T> condicion1, Predicate<T> condicion2, String textoError);
    IValidador<T,U> and(Predicate<T> condicion1, Predicate<T> condicion2, String textoError);
    List<String> getErrores();  
    void limpiarErrores();
}
