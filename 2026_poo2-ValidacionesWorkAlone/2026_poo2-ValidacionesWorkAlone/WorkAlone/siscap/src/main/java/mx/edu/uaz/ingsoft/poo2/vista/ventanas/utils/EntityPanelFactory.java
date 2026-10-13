package mx.edu.uaz.ingsoft.poo2.vista.ventanas.utils;


import static mx.edu.uaz.ingsoft.poo2.utils.Constantes.*;
import mx.edu.uaz.ingsoft.poo2.vista.paneles.PanelShowEntity;
import mx.edu.uaz.ingsoft.poo2.vista.paneles.PanelShowFramework;
import mx.edu.uaz.ingsoft.poo2.vista.paneles.PanelShowLangProg;
import mx.edu.uaz.ingsoft.poo2.vista.paneles.PanelShowPeople;

import java.util.Optional;




public abstract class EntityPanelFactory {
    public static Optional<PanelShowEntity> createPanelShowEntity(String nombreCatalogo){
        return Optional.ofNullable(
                switch (nombreCatalogo){
                    case CAT_FRAMEWORK -> new PanelShowFramework();
                    case CAT_PEOPLE -> new PanelShowPeople();
                    case CAT_LANG_PROG -> new PanelShowLangProg();
                    default -> null;
                }
        );
    }
}
