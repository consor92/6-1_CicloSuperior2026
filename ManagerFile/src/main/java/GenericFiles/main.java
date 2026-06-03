package GenericFiles;

import java.io.PrintStream;
import java.util.ArrayList;

public class main {

	public static void main(String[] args) {
		PrintStream ps = new PrintStream(System.out);
		managerFile mF = new managerFile("PROBANDO.TXT");

		// mF.crearFileConPrintStream( mF.getFile() );
		// mF.crearFileConPrinter(mF.getFile(), "lalalalalal", false);
		// ps.println( mF.leerFileCaracterCaracter(mF.getFile()) );
		// ps.println( mF.LeerFileConBuffer(mF.getFile()) );
		// mF.modificarArchivoTemporalLinea(mF.getFile(), "Priner", "Primer");

		ArrayList<dtoProductos> lista = mF.archivoConLinkedList(mF.getFile());

		for (dtoProductos l : lista) {
			ps.println("Nuevo Producto");
			ps.println(String.format("%s %.2f %d", l.getProducto(), l.getPrecio(), l.getCantidad() ));
			ps.println(String.join(" ", l.getProducto(), String.valueOf(l.getPrecio()) , String.valueOf(l.getCantidad()) ));
		}

	}

}
