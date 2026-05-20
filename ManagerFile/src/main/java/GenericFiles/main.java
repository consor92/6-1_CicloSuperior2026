package GenericFiles;

import java.io.PrintStream;

public class main {

	public static void main(String[] args) {
		PrintStream ps = new PrintStream(System.out);	
		managerFile mF = new managerFile( "PROBANDO.TXT" );
		
		mF.crearFileConPrintStream( mF.getFile() );
		//mF.crearFileConPrinter(mF.getFile(), "lalalalalal", false);
		//ps.println( mF.leerFileCaracterCaracter(mF.getFile()) );
		//ps.println( mF.LeerFileConBuffer(mF.getFile()) );
		mF.modificarArchivoTemporalLinea(mF.getFile(), "Priner", "Primer");
		
	}

}
