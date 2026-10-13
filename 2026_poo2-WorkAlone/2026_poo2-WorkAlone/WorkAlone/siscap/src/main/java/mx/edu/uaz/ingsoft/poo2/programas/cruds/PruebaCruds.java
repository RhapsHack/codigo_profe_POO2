package mx.edu.uaz.ingsoft.poo2.programas.cruds;

import mx.edu.uaz.ingsoft.poo2.modelo.entidades.BaseDeDatos;

public class PruebaCruds {
    public static void main(String[] args) {
        AbstractCrudEntidad<BaseDeDatos> programaCRUD;
        programaCRUD = new CrudBasesDeDatos();
        programaCRUD.ejecutar();
    }
}
