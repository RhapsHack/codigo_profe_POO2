package mx.edu.uaz.ingsoft.poo2.utils.validadores.interfaces;

public interface IStringValidador extends IValidador<String,Integer>{
    IStringValidador noVacia();
    IStringValidador esVacia();
    IStringValidador soloAlfabeticas();
    IStringValidador soloAlfabeticasYBlanco();
    IStringValidador soloAlfaNumericas();
    IStringValidador cumpleRegExp(String re);

}
