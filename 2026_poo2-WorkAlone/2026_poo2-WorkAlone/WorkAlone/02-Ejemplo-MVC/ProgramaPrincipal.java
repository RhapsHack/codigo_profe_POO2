package prueba01;

public class ProgramaPrincipal {
	public static void main(String[] args) {
		IVentana unaVentana;
		IControladorVentana unControlador;
		
		unaVentana = new Ventana();
		unControlador = new ControladorVentana();
		
		unaVentana.setControlador(unControlador);
		unControlador.setVentana(unaVentana);
		
		
		unControlador.iniciarPrograma();
	}

}
