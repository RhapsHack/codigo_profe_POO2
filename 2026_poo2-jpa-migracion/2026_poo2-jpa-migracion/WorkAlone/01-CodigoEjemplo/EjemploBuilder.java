public class EjemploBuilder{
	public static void main(String[] args){
		StringBuilder sb;
		sb = new StringBuilder();
		sb.append("hola")
		.append("adios")
		.append("ya me canse");
		System.out.println("La cadena es"+
		sb.toString());
	}
}