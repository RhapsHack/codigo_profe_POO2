package prueba01;

import javax.swing.JOptionPane;

public class ControladorVentana implements IControladorVentana{
	
	IVentana laVentana;
	public void setVentana(IVentana v) {
		laVentana = v;
	}
	public void iniciarPrograma() {
		// Inicializa la base de datos, etc
		laVentana.mostrar();
		
	}
	
	
	public void procesarDatos(InfoVentanaDTO info) {
		// Lógica de todo lo que hay que hacer 
		// cuando se da clic al boton
		JOptionPane.showMessageDialog(null, "El nombre es "+info.nombre);
		InfoVentanaDTO ivd;
		ivd = new InfoVentanaDTO();
		ivd.nombre="Juan";
		ivd.precio = 20;
		//ivd.tieneDescuento = true;
		laVentana.mostrarInfo(ivd);
	}

}
