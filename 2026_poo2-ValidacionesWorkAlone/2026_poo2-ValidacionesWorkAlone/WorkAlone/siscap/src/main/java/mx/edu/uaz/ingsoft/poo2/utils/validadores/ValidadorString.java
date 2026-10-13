package mx.edu.uaz.ingsoft.poo2.utils.validadores;


import mx.edu.uaz.ingsoft.poo2.utils.validadores.interfaces.IStringValidador;

public class ValidadorString extends AbstractValidador<String,Integer>
        implements IStringValidador {

    public static final String RX_ONLY_ALPHABET = "[a-zA-Z]+";
    public static final String RX_ONLY_ALPHABET_AND_WHITE = "[a-zA-Z ]+";
    public static final String RX_ALFANUMERIC = "[a-zA-Z0-9]+";
    public static final String RX_ONLY_NUMBERS = "[0-9]+";
    public static final String RX_ONLY_NUMBERS_AND_WHITE = "[0-9 ]+";

    public ValidadorString(){
        super();
    }

    @Override
    public ValidadorString noVacia(){
        addCondicion( cadena -> ! cadena.isBlank(),"La cadena está vacía");
        return this;
    }

    @Override
    public ValidadorString min(Integer valorMinimo) {
        addCondicion( cadena -> cadena.length() >= valorMinimo, "La longitud debe ser mayor a "+valorMinimo);
        return this;
    }

    @Override
    public ValidadorString max(Integer valorMaximo) {
        addCondicion( cadena -> cadena.length() <= valorMaximo,"La longitud debe ser menor a "+valorMaximo);
        return this;
    }

    @Override
    public ValidadorString esVacia(){
        addCondicion( cadena -> cadena.isBlank(),"La cadena no está vacía");
        return this;
    }

    @Override
    public ValidadorString soloAlfabeticas(){
        addCondicion( cadena -> cadena.matches(RX_ONLY_ALPHABET), "El texto debe ser solo alfabéticas");
        return this;
    }

    @Override
    public ValidadorString soloAlfabeticasYBlanco(){
        addCondicion( cadena -> cadena.matches(RX_ONLY_ALPHABET_AND_WHITE),"La cadena debe cumplir con la RegExp "+RX_ONLY_ALPHABET_AND_WHITE);
        return this;
    }

    @Override
    public ValidadorString soloAlfaNumericas(){
        addCondicion( cadena -> cadena.matches(RX_ALFANUMERIC),"La cadena debe cumplir con la RegExp "+RX_ALFANUMERIC);
        return this;
    }

    @Override
    public ValidadorString cumpleRegExp(String re){
        addCondicion( cadena -> cadena.matches(re),"La cadena debe cumplir con la RegExp "+re);
        return this;
    }
}
