package mx.edu.uaz.ingsoft.poo2.vista.paneles;


import mx.edu.uaz.ingsoft.poo2.modelo.entidades.IEntidad;

import javax.swing.*;
import javax.swing.event.ListSelectionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import static mx.edu.uaz.ingsoft.poo2.utils.Constantes.NO_SELECTED;
import static mx.edu.uaz.ingsoft.poo2.utils.FunctionUtils.esNulo;



public class PanelListEntidades<T extends IEntidad> extends JPanel {

    protected JList<String> listaDesplegada;
    protected List<T> listaEntidades;
    public PanelListEntidades(List<T> lista) {
        listaDesplegada = new JList<>();
        setSingleSelection();
        establecerListaEntidades(lista);
        JScrollPane contenedorBarras = new JScrollPane(listaDesplegada);
        add(contenedorBarras);
    }
    public PanelListEntidades(){
        this(new ArrayList<>());
    }
    public void setSingleSelection(){
        listaDesplegada.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }
    public void setMultipleSelection(){
        listaDesplegada.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
    }
    public Optional<T> getItemSelected(){
        int indiceSelected = listaDesplegada.getSelectedIndex();
        if( indiceSelected == NO_SELECTED){
            return Optional.empty();
        }
        return Optional.of(listaEntidades.get(indiceSelected));
    }
    public List<T> getItemsSelected(){
        var seleccionados = listaDesplegada.getSelectedIndices();
        List<T> salida = new ArrayList<>();
        for(var indice:seleccionados){
            salida.add(listaEntidades.get(indice));
        }
        return salida;
    }
    public void establecerListaEntidades(List<T> nuevaLista, Function<T,String> stringConverter){
        listaEntidades = esNulo(nuevaLista)?new ArrayList<>():nuevaLista;
        String[] arregloDatosLista = new String[nuevaLista.size()];
        int indice = 0;
        for(var entidad:nuevaLista){
            arregloDatosLista[indice++]=stringConverter.apply(entidad);
        }
        listaDesplegada.setListData(arregloDatosLista);
    }
    public void establecerListaEntidades(List<T> nuevaLista){
        establecerListaEntidades(nuevaLista,T::toString);
    }
    public void establecerListListener(ListSelectionListener listener){
        listaDesplegada.addListSelectionListener(listener);
    }
}
