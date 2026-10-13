package mx.edu.uaz.ingsoft.poo2.vista;


import mx.edu.uaz.ingsoft.poo2.vista.interfaces.IVistaCarga;

import javax.swing.*;
import java.io.File;
import java.nio.file.Path;
import java.util.Optional;
import java.util.Scanner;

public class VistaCargaGrafico implements IVistaCarga {

    @Override
    public Optional<Path> pedirRutaArchivo() {
        var selectorArchivos = new JFileChooser();
        var eleccionUsuario = selectorArchivos.showOpenDialog(null);
        if(eleccionUsuario == JFileChooser.APPROVE_OPTION){
            return Optional.of( selectorArchivos.getSelectedFile().toPath());
        }else{
            return Optional.empty();
        }
    }
}
