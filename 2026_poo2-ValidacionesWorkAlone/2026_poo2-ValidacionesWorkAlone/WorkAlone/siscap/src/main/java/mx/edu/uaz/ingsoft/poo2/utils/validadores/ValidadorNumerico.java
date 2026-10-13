package mx.edu.uaz.ingsoft.poo2.utils.validadores;


import mx.edu.uaz.ingsoft.poo2.utils.validadores.interfaces.INumberValidator;

public class ValidadorNumerico<T extends Number & Comparable>
        extends AbstractValidador<T,T>
        implements INumberValidator<T> {


    public ValidadorNumerico(){
        super();
    }

    @Override
    public INumberValidator<T> esPositivo() {
        addCondicion( valor -> valor.doubleValue() > 0.0, "El valor deberia ser positivo");
        return this;
    }

    @Override
    public INumberValidator<T> esNegativo() {
        addCondicion( valor -> valor.doubleValue() < 0.0,"El valor deberia ser negativo ");
        return this;
    }

}
