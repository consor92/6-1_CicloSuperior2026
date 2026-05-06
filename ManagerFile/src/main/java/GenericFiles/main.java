package GenericFiles;

public class main {

	public static void main(String[] args) {
			
		managerFile mF = new managerFile( "PROBANDO.TXT" );
		
		//mF.crearFileConPrintStream( mF.getFile() );
		mF.crearFileConPrinter(mF.getFile(), "lalalalalal", false);
	}

}
