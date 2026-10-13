package mx.edu.uaz.ingsoft.poo2.utils.validadores.interfaces;

public interface INumberValidator<T extends Number> extends  IValidador<T,T>{
    INumberValidator<T> esPositivo();
    INumberValidator<T> esNegativo();
}
