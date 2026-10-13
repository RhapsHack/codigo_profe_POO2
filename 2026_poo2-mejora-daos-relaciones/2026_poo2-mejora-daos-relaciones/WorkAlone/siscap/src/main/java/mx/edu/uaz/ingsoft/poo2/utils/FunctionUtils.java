package mx.edu.uaz.ingsoft.poo2.utils;

import mx.edu.uaz.ingsoft.poo2.modelo.entidades.IEntidad;

import javax.swing.*;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public class FunctionUtils {
    public static boolean noEsNulo(Object objeto) {
        return objeto != null;
    }

    public static boolean esNulo(Object objeto) {
        return objeto == null;
    }

    public static boolean esCadenaNoNulaNiVacia(String cadena){
        if(cadena == null){
            return false;
        }
        return ! cadena.isBlank();
    }

    public static String stringValueFromOptional(Optional<String> opcional){
        if(opcional.isPresent()){
            return opcional.get();
        }else{
            return Constantes.TXT_VACIO;
        }
    }
    public static int intValueFromOptional(Optional<Integer> opcional){
        if(opcional.isPresent()){
            return opcional.get();
        }else{
            return Constantes.ERR_VALOR_NO_VALIDO;
        }
    }
    public static boolean enteroDentroRango(int min, int max, int valor){
        return (min <= valor) && (valor <=max);
    }
    public static void mostrarMensajeConsola(String mensaje){
        System.out.println(mensaje);
    }
    public static void mostrarMensajeDialogo(String mensaje){
        JOptionPane.showMessageDialog(null, mensaje);
    }
    public static String[] listaToArray(List<? extends IEntidad> listaDatos) {
        var salida = new String[listaDatos.size()];
        int indice;
        for(indice = 0; indice < listaDatos.size(); indice++){
            salida[indice] = listaDatos.get(indice).toString();
        }
        return salida;
    }

    public static boolean todos(Predicate<Object> predicado, Object[] objetos ){
        return todos(predicado, Arrays.asList(objetos));
    }
    public static boolean alguno(Predicate<Object> predicado, Object[] objetos){
        return alguno(predicado,Arrays.asList(objetos));
    }
    public static boolean todos(Predicate<Object> predicado, List<Object> objetos ){
        return objetos.stream().allMatch(predicado);
    }
    public static boolean alguno(Predicate<Object> predicado, List<Object> objetos){
        return objetos.stream().anyMatch(predicado);
    }
    public static boolean ninguno(Predicate<Object> predicado, Object[] objetos){
        return ninguno(predicado,Arrays.asList(objetos));
    }
    public static boolean ninguno(Predicate<Object> predicado, List<Object> objetos){
        return objetos.stream().allMatch(predicado.negate());
    }
    public static <T extends IEntidad, U extends Class<T>> Optional<T> crearInstancia(U clazz){
        try {
            var salida = clazz.getDeclaredConstructor().newInstance();
            return Optional.of(salida);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }
}