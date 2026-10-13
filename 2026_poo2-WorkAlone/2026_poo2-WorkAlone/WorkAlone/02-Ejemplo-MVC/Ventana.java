package prueba01;

import java.awt.Container;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class Ventana extends JFrame implements IVentana{
	private static final long serialVersionUID = 1L;

	JTextField txtNombre;
	JTextField txtPrecio;
	JLabel tieneDescuento;
	JButton boton;
	
	IControladorVentana controlador;
	
	public void setControlador(IControladorVentana c) {
		controlador = c;
	}
	public void mostrar() {
		setVisible(true);
	}
	
	public Ventana( ){
		setSize(300,300);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// Mas acciones
		txtNombre = new JTextField(25);
		 tieneDescuento = new JLabel("No lo se");
		 boton = new JButton("Apachurra aqui");
		// Mas acciones
		Container cristal;
		cristal = getContentPane();
		cristal.setLayout(new FlowLayout());
		add(txtNombre);
		add(tieneDescuento);
		add(boton);
		boton.addActionListener(e -> ejecutarAccionBoton());
		
		setVisible(false);
	}
	
	private void ejecutarAccionBoton() {
		// hacer muchas cosas
		InfoVentanaDTO ivd;
		ivd = new InfoVentanaDTO();
		ivd.nombre=txtNombre.getText();
		ivd.precio = 50;
		controlador.procesarDatos(ivd);
		// muchas mas cosas
		
	}
	
	
	public void mostrarInfo(InfoVentanaDTO  datos) {
		txtNombre.setText(datos.nombre);
		
	}
}
