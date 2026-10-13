package mx.edu.uaz.ingsoft.poo2.modelo.utils;

import mx.edu.uaz.ingsoft.poo2.utils.Constantes;

import java.util.ResourceBundle;
import java.util.logging.Logger;

public enum MensajesResourceBundle {
    MENSAJES;
    private ResourceBundle rb;
    private MensajesResourceBundle(){
        try{
            rb = ResourceBundle.getBundle(Constantes.RB_FULL_NAME);
        }catch (Exception e){
            Logger.getAnonymousLogger().severe(Constantes.ERR_RESOURCE_BUNDLE);
            System.exit(Constantes.ERR_CODE_RESOURCE_BUNDLE_NOT_LOADED);
        }
    }
    public ResourceBundle getResourceBundle(){
        return rb;
    }
}
