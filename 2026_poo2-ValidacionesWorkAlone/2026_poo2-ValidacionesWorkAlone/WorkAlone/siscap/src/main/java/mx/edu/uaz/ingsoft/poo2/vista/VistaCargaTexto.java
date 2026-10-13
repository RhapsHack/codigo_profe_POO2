package mx.edu.uaz.ingsoft.poo2.vista;



import mx.edu.uaz.ingsoft.poo2.vista.interfaces.IVistaCarga;

import java.io.File;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Path;
import java.util.Optional;
import java.util.Scanner;

public class VistaCargaTexto implements IVistaCarga {

    @Override
    public Optional<Path> pedirRutaArchivo() {
        Scanner teclado;
        teclado = new Scanner(System.in);
        System.out.println("Dame la ruta y archivo a cargar");
        var direccion = teclado.nextLine();
        var archivoIndicado = new File(direccion);
        if(archivoIndicado.exists()){
            return Optional.of(archivoIndicado.toPath());
        }else{
            return Optional.empty();
        }
    }
}
