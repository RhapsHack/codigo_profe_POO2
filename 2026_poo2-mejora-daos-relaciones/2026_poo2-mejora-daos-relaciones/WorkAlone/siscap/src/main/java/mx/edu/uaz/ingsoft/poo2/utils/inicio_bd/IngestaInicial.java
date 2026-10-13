package mx.edu.uaz.ingsoft.poo2.utils.inicio_bd;

import mx.edu.uaz.ingsoft.poo2.modelo.daos.DAOException;

import static mx.edu.uaz.ingsoft.poo2.utils.Constantes.MSG_INGESTA_INICIAL_COMPLETADA;

public class IngestaInicial {
    public static void main(String[] args) {

        try {
            IngestaPersonas.ejecutarIngesta();
            IngestaLenguajesProgramacion.ejecutarIngesta();
            IngestaFrameworks.ejecutarIngesta();
        } catch (DAOException e) {
            System.err.println(e.getMessage());
            return;
        }

        System.out.println(MSG_INGESTA_INICIAL_COMPLETADA);
    }
}
