package mx.edu.uaz.ingsoft.poo2.vista.utils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static mx.edu.uaz.ingsoft.poo2.utils.FunctionUtils.esNulo;

public abstract class SwingComponentUtils {
    
    public static void agregarComponentes(Container contenedor, JComponent ... componentes ){
        agregarComponentes(contenedor,Arrays.asList(componentes));
    }
    public static <T extends JComponent>  void  agregarComponentes(Container contenedor, List<T> componentes ){
        componentes.forEach(componente->{
        	if(componente != null) {
        		contenedor.add(componente);
        	}
        }
        );
    }
    public static List<JButton> crearBotones(List<String> etiquetas){
        if(esNulo(etiquetas)){
            return new ArrayList<>();
        }


        /*
        for(String etiqueta:etiquetas){
            JButton unBoton = new JButton(etiqueta);
            botones.add(unBoton);
        }
        return botones;

        etiquetas.forEach(etiqueta->botones.add(new JButton(etiqueta)));
        return botones;
         */
        return etiquetas.stream()
                .map(JButton::new)
                .toList();

    }
    public static List<JButton> crearBotones(String ... etiquetas){
        if(esNulo(etiquetas)){
            return new ArrayList<>();
        }
        return crearBotones( Arrays.asList(etiquetas));
    }
    public static List<JLabel> crearEtiquetas(List<String> etiquetas){
        if(esNulo(etiquetas)){
            return new ArrayList<>();
        }
        return etiquetas.stream().map(textoEtiqueta->new JLabel(textoEtiqueta)).toList();
    }
    public static List<JLabel> crearEtiquetas(String... etiquetas){
        if(esNulo(etiquetas)){
            return new ArrayList<>();
        }
        return crearEtiquetas(Arrays.asList(etiquetas));
    }
    public static void setActionListenerToButton(List<JButton> listaBotones, String etiqueta, ActionListener listener){
        var boton = getButtonWithLabel(listaBotones,etiqueta);
        if(boton.isPresent()){
            boton.get().addActionListener(listener);
        }
    }
    public static Optional<JButton> getButtonWithLabel(List<JButton> listaBotones,String etiqueta){
        if(esNulo(listaBotones)){
            return Optional.empty();
        }
        return listaBotones.stream()
                .filter(boton-> etiqueta.equalsIgnoreCase(boton.getText()))
                .findFirst();
    }

}
