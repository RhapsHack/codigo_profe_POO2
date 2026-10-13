package mx.edu.uaz.ingsoft.poo2.utils.validadores;

import mx.edu.uaz.ingsoft.poo2.modelo.entidades.IEntidad;
import mx.edu.uaz.ingsoft.poo2.utils.validadores.entidades.FrameworkValidador;
import mx.edu.uaz.ingsoft.poo2.utils.validadores.entidades.LangProgValidador;
import mx.edu.uaz.ingsoft.poo2.utils.validadores.entidades.PeopleValidador;
import mx.edu.uaz.ingsoft.poo2.utils.validadores.entidades.TemaValidador;
import mx.edu.uaz.ingsoft.poo2.utils.validadores.interfaces.IValidadorEntidad;
import static mx.edu.uaz.ingsoft.poo2.utils.Constantes.*;
import java.util.Optional;


public class ValidadorEntidadFactory<T extends IEntidad> {
    public static Optional<IValidadorEntidad> crearValidador(String nombreCatalogo){
        return Optional.ofNullable(switch (nombreCatalogo){
            case CAT_FRAMEWORK->new FrameworkValidador();
            case CAT_PEOPLE->new PeopleValidador();
            case CAT_LANG_PROG->new LangProgValidador();
            case CAT_TEMA->new TemaValidador();
            default -> null;
        });
    }
}
