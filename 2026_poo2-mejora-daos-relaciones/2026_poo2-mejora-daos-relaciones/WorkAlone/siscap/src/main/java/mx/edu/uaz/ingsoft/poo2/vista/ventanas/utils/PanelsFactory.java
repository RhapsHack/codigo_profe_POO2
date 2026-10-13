package mx.edu.uaz.ingsoft.poo2.vista.ventanas.utils;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static mx.edu.uaz.ingsoft.poo2.vista.utils.SwingComponentUtils.agregarComponentes;


public abstract class PanelsFactory {
    public static JPanel createVerticalBoxPanel(){
        var salida = new JPanel();
        salida.setLayout(new BoxLayout(salida,BoxLayout.PAGE_AXIS));
        return salida;
    }
    public static JPanel createLabelComboPanel(String etiqueta, JComboBox<String> comboListaDatos, boolean isHorizontal){
        JPanel panel;
        panel = (isHorizontal)? new JPanel() : createVerticalBoxPanel();
        panel.add(new JLabel(etiqueta));
        panel.add(comboListaDatos);
        return panel;
    }
    public static JPanel createLabelComboPanel(String etiqueta, JComboBox<String> comboListaDatos){
        return createLabelComboPanel(etiqueta,comboListaDatos,true);
    }
    public static JPanel createLabelTextPanel(String etiqueta, JTextComponent campoTexto, boolean isHorizontal){
        JPanel panel;
        panel = (isHorizontal)? new JPanel() : createVerticalBoxPanel();
        panel.add(new JLabel(etiqueta));
        panel.add(campoTexto);
        return panel;
    }
    public static JPanel createComponentsPanel(boolean isHorizontal, JComponent ...components){
        return createComponentsPanel(isHorizontal, Arrays.stream(components).toList());
    }
    public static JPanel createComponentsPanel(JComponent... components){
        return createComponentsPanel(true, Arrays.stream(components).toList());
    }
    public static JPanel createComponentsPanel(boolean isHorizontal, List<? extends JComponent> componentes){
        JPanel panel;
        panel = (isHorizontal)? new JPanel() : createVerticalBoxPanel();
        agregarComponentes(panel,  componentes);
        return panel;
    }
    public static JPanel createComponentsPanel(List<? extends JComponent> componentes){
        return createComponentsPanel(true,componentes);
    }
    public static JPanel createGridComponentsPanel(int numeroColumnas, List<? extends JComponent> componentes ){
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(0,numeroColumnas>0?numeroColumnas:1));
        agregarComponentes(panel,componentes);
        return panel;
    }
    public static JPanel createGridComponentsPanel(int numeroColumnas, JComponent ...componentes ){
        return createGridComponentsPanel(numeroColumnas, Arrays.stream(componentes).collect(Collectors.toList()));
    }
    public static JPanel createLabelTextPanel(String etiqueta, JTextComponent campoTexto){
        return createLabelTextPanel(etiqueta,campoTexto,true);
    }

}
