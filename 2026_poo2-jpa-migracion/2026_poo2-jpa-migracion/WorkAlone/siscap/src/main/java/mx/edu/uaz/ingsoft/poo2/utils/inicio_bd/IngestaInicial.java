package mx.edu.uaz.ingsoft.poo2.utils.inicio_bd;

import static mx.edu.uaz.ingsoft.poo2.utils.Constantes.MSG_INGESTA_INICIAL_COMPLETADA;

public class IngestaInicial {
    public static void main(String[] args) {
        IngestaBasesDeDatos.ejecutarIngesta();
        IngestaPersonas.ejecutarIngesta();
        IngestaLenguajesProgramacion.ejecutarIngesta();
        IngestaTecnologias.ejecutarIngesta();
        IngestaSistemasOperativos.ejecutarIngesta();
        IngestaHerramientas.ejecutarIngesta();
        IngestaFrameworks.ejecutarIngesta();
        IngestaHabilidadesBlandas.ejecutarIngesta();
        System.out.println(MSG_INGESTA_INICIAL_COMPLETADA);
    }
}
