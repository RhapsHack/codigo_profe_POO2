package mx.edu.uaz.ingsoft.poo2.utils.validadores.entidades;


import mx.edu.uaz.ingsoft.poo2.modelo.entidades.Tema;
import mx.edu.uaz.ingsoft.poo2.utils.validadores.ValidadorString;

public class TemaValidador extends ValidadorEntidad<Tema> {

    private static final String NOMBRE = "nombre";

    public TemaValidador() {
        super();
        agregarValidador(NOMBRE, new ValidadorString()
                .noVacia()
                .between(2, 100), Tema::getNombre);
    }

}