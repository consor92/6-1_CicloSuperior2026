package GenericFiles;

import java.io.PrintStream;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;

import javax.crypto.SecretKey;

public class main {

	public static void main(String[] args) {
		PrintStream ps = new PrintStream(System.out);
		managerFile mF = new managerFile("PROBANDO.TXT");

		// mF.crearFileConPrintStream( mF.getFile() );
		// mF.crearFileConPrinter(mF.getFile(), "lalalalalal", false);
		// ps.println( mF.leerFileCaracterCaracter(mF.getFile()) );
		// ps.println( mF.LeerFileConBuffer(mF.getFile()) );
		// mF.modificarArchivoTemporalLinea(mF.getFile(), "Priner", "Primer");
		/*
		 * ArrayList<dtoProductos> lista = mF.archivoConLinkedList(mF.getFile());
		 * 
		 * for (dtoProductos l : lista) { ps.println("Nuevo Producto");
		 * ps.println(String.format("%s %.2f %d", l.getProducto(), l.getPrecio(),
		 * l.getCantidad() )); ps.println(String.join(" ", l.getProducto(),
		 * String.valueOf(l.getPrecio()) , String.valueOf(l.getCantidad()) )); }
		 */

		//Encriptado RSA priv-publ
		String datoEncriptado = EncriptUtil.cifrarConClavePublica("Hola", (PublicKey)EncriptUtil.keys.get("RSAp"));
		ps.printf("Texto a cifrar:%s \n ", "Hola");
		ps.printf("Texto a Encriptado:%s \n Desencriptado:%s \n", datoEncriptado,
				EncriptUtil.desCifrarConClavePublica(datoEncriptado, (PrivateKey)EncriptUtil.keys.get("RRSApr")));

		//Encriptado AES  publico
		SecretKey pKey = (SecretKey)EncriptUtil.keys.get("AES");
		String dato = EncriptUtil.cifrarAES("Hola", pKey);
		ps.println( dato );
		ps.println( EncriptUtil.desCifrarAES(dato, pKey) );
		
		//HASH  SHA-256
		EncriptUtil.hashSha("password");
		
	}

}
