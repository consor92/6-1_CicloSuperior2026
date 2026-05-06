package GenericFiles;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

public class managerFile {

	private String ruta = "";
	private File file;
	private PrintStream ps;

	public managerFile(String nombre) {
		file = new File(ruta.concat(nombre));

		ps = new PrintStream(System.out);
		try {
			ps.println("Name:" + file.getName());
			ps.println("Path:" + file.getPath());
			ps.println("PathAbs:" + file.getAbsolutePath());
			ps.println("PathCannon:" + file.getCanonicalPath());
			ps.println("Contenedor del archivo:" + file.getParentFile());
			ps.println("Parent:" + file.getParent());
			ps.println("Tama�o:" + file.getTotalSpace());
			ps.println("ejecutable?:" + file.canExecute());
			ps.println("acceso de lectura:" + file.canRead());
			ps.println("acceso de escrituta:" + file.canWrite());
			ps.println("esta oculto?:" + file.isHidden());
			// "Elimina:" archivo.delete();
			// "Elimna, cuando cierra el programa:" archivo.deleteOnExit();
			ps.println("existe?:" + file.exists());
			ps.println("Es archivo?:" + file.isFile());
			ps.println("Es carpeta?:" + file.isDirectory());
			// "Crea ARCHIVOS:" archivo.createNewFile();
			// "Crea CARPETAS:" archivo.mkdir();
			// "Renombrar:" archivo.renameTo("NuevoNombre.txt");
		} catch (IOException ex) {
			Logger.getLogger(managerFile.class.getName()).log(Level.WARNING, null, ex);
		}
	}

	public void crearFileConBuffered() {

	}

	/**
	 * Método para crear o escribir en un archivo usando un objeto Printer.
	 *
	 * @param f Archivo donde se realizará la operación.
	 */
	public void crearFileConPrinter(File f, String msg, boolean SobreEscribir) {
		FileWriter fw = null;
		PrintWriter pw = null;

		try {
			if (!f.exists()) {
				try {
					f.createNewFile();
				} catch (IOException ex) {
					Logger.getLogger(managerFile.class.getName()).log(Level.WARNING, null, ex);
				}
			}

			fw = new FileWriter(f); // <- canal de cominicacion / archivo
			pw = new PrintWriter(fw, !SobreEscribir);

			pw.println(msg);

			pw.flush();
		} catch (FileNotFoundException ex) {
			Logger.getLogger(managerFile.class.getName()).log(Level.WARNING, null, ex);
		} catch (IOException ex) {
			Logger.getLogger(managerFile.class.getName()).log(Level.WARNING, null, ex);
		} finally {
			try {
				if (pw == null)
					pw.close();
				if (fw == null)
					fw.close();
			} catch (IOException ex) {
				Logger.getLogger(managerFile.class.getName()).log(Level.WARNING, null, ex);
			}
		}
	}

	/**
	 * Escribe contenido en el archivo especificado usando un PrintStream. Este
	 * método crea un flujo de salida para el archivo, escribe varias líneas y
	 * caracteres, y asegura que los datos se escriban correctamente.
	 * 
	 * @param f Archivo donde se escribirá el contenido.
	 * @throws FileNotFoundException Si el archivo no puede ser abierto para
	 *                               escritura.
	 * @throws IOException           Si ocurre un error al cerrar los flujos de
	 *                               salida.
	 */
	public void crearFileConPrintStream(File f) {
		FileOutputStream fos = null;
		PrintStream fs = null;

		try {
			if (!f.exists()) {
				try {
					f.createNewFile();
				} catch (IOException ex) {
					Logger.getLogger(managerFile.class.getName()).log(Level.WARNING, null, ex);
				}
			}

			fos = new FileOutputStream(f, true); // no borra al original
			fs = new PrintStream(fos);

			fs.println("Manada enter.");
			fs.print("Priner renglon.");
			fs.append("escribi con append.");
			fs.write('n');
			fs.write(58);

			fs.flush();
		} catch (FileNotFoundException ex) {
			Logger.getLogger(managerFile.class.getName()).log(Level.WARNING, null, ex);
		} finally {
			try {
				if (fs == null)
					fs.close();
				if (fos == null)
					fos.close();
			} catch (IOException ex) {
				Logger.getLogger(managerFile.class.getName()).log(Level.WARNING, null, ex);
			}
		}

	}

	public void setFile(File f) {
		this.file = f;
	}

	/**
	 * Devuelve el archivo asociado a este objeto.
	 * 
	 * JavaDoc ESTE TEXTO NO TIENE NINGUNA ETIQUETA DE IDENTIFICACION. ESTO NO VA A
	 * APARECER :( También se pueden agregar referencias a clases, métodos o
	 * atributos con la instrucción: {@code <html></html>} o usar {@link String}.
	 * 
	 * @return El objeto File representando el archivo this.file.
	 * @since v1.0
	 */
	public File getFile() {
		return this.file;
	}
	
	/**
	 * Lee todo el contenido de un archivo usando BufferedReader y devuelve el texto completo.
	 *
	 * @param f un archivo al leer
	 * @return Todo el texto leído del archivo como una cadena.
	 * @throws FileNotFoundException si el archivo no existe.
	 * @throws IOException si ocurre un error durante la lectura.
	 */
	public String LeerFileConBuffer(File f) {
		return "";
	}
	
	/**
	 * Lee el contenido de un archivo carácter por carácter y devuelve el texto completo.
	 * Maneja saltos de línea adecuadamente y concatena los caracteres en una cadena.
	 *
	 * @param f Archivo desde donde se leerán los caracteres.
	 * @return El contenido completo del archivo como una cadena, o null si ocurre un error.
	 */	
	public String leerFileCaracterCaracter(File f) {
		return "";
	}
	
	/**
	 * Modifica un archivo original creando un archivo temporal donde reemplaza
	 * todas las ocurrencias de una cadena buscada por otra cadena dada.
	 * Luego elimina el archivo original y renombra el temporal con el nombre del archivo original.
	 *
	 * @param archivoOriginal El archivo que será modificado.
	 * @param buscar         La cadena que se busca en cada línea para ser reemplazada.
	 * @param reemplazar     La cadena con la cual se reemplazarán las ocurrencias encontradas.
	 */	
	public void modificarArchivoTemporalLinea(File archivoOriginal, String buscar, String reemplazar)  {
		
	}
	
	/**
	 * Modifica el contenido de un archivo leyendo todas sus líneas en una LinkedList,
	 * reemplazando en memoria las ocurrencias de una cadena dada, y luego escribiendo
	 * el contenido modificado de nuevo en el archivo original.
	 *
	 * @param archivoOriginal El archivo que será leído y modificado.
	 * @param buscar         La cadena que se desea buscar y reemplazar en el archivo.
	 * @param reemplazar     La cadena que reemplazará las ocurrencias encontradas.
	 */	
	public void modificarArchivoConLinkedList(File archivoOriginal, String buscar, String reemplazar) {
		
	}
}
