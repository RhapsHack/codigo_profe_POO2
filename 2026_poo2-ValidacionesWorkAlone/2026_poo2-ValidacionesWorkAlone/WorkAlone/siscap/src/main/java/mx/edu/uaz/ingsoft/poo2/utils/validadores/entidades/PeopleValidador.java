package mx.edu.uaz.ingsoft.poo2.utils.validadores.entidades;


import mx.edu.uaz.ingsoft.poo2.modelo.entidades.Persona;
import mx.edu.uaz.ingsoft.poo2.utils.FunctionUtils;
import mx.edu.uaz.ingsoft.poo2.utils.validadores.ValidadorString;

public class PeopleValidador extends  ValidadorEntidad<Persona> {
    /*

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "primer_apellido")
    private String primerApellido;

    @Column(name = "segundo_apellido")
    private String segundoApellido;

    @Column(name = "numero_empleado")
    private String numeroEmpleado;

    @Column(name = "correo_electronico")
    private String correoElectronico;
     */

    private static final String NOMBRE = "nombre";
    private static final String PRIMER_APELLIDO = "primer_apellido";
    private static final String SEGUNDO_APELLIDO = "segundo_apellido";
    private static final String NUMERO_EMPLEADO = "numero_empleado";
    private static final String CORREO = "correo";


    public PeopleValidador() {
        super();

        agregarValidador(NOMBRE,new ValidadorString()
                .noVacia()
                .between(2,100));
        agregarValidador(PRIMER_APELLIDO,new ValidadorString()
                .noVacia()
                .between(2,100));
        agregarValidador(SEGUNDO_APELLIDO,new ValidadorString()
                .noVacia()
                .between(2,100));
        agregarValidador(NUMERO_EMPLEADO,new ValidadorString()
                .noVacia()
                .between(2,100));
        agregarValidador(CORREO,new ValidadorString()
                .noVacia()
                .between(2,100));
    }


    @Override
    public void asignarValoresAValidadores() {
        if(FunctionUtils.noEsNulo(entidad)){
            mapaValidadores.get(NOMBRE).setValor(entidad.getNombre());
            mapaValidadores.get(PRIMER_APELLIDO).setValor(entidad.getPrimerApellido());
            mapaValidadores.get(SEGUNDO_APELLIDO).setValor(entidad.getSegundoApellido());
            mapaValidadores.get(NUMERO_EMPLEADO).setValor(entidad.getNumeroEmpleado());
            mapaValidadores.get(CORREO).setValor(entidad.getCorreoElectronico());
        }
    }

}
