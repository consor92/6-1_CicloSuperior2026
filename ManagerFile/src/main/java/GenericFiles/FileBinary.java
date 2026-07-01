package GenericFiles;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.io.Serializable;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class FileBinary implements Serializable {

	private static final long serialVersionUID = -123456L;
	final Logger LOG = Logger.getLogger( FileBinary.class.getName());
	
	public FileBinary() {
		FileHandler fileError;
		try {
			fileError = new FileHandler("FileError.log",true);
			LOG.addHandler(fileError);
			LOG.log(Level.ALL , "");
			
			SimpleFormatter format = new SimpleFormatter();
			fileError.setFormatter(format);
		} catch (SecurityException | IOException e) {
			e.printStackTrace();
		}
	}

	public void serializar(String ruta) throws IOException {
		File archivo = new File(ruta);

		ObjectOutputStream escritor;

		FileOutputStream fos = new FileOutputStream(archivo);
		escritor = new ObjectOutputStream(fos);
		escritor.writeObject(this);
		escritor.close();

	}

	public FileBinary deSerializar(String ruta) throws IOException, ClassNotFoundException {
		File archivo = new File(ruta);

		ObjectInputStream lector;

		FileInputStream fis = new FileInputStream(archivo);
		lector = new ObjectInputStream(fis);
		

		FileBinary f = (FileBinary) lector.readObject();
		lector.close();
		return f;
		
	}

	String numero;
	
	public void start() throws IOException {
		 PrintStream ps = new PrintStream(System.out);
		ps.println("Valor:" + numero);
		
		BufferedReader br = new BufferedReader( new InputStreamReader(System.in));
		numero = br.readLine();
	}

}