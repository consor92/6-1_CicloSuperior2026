package TCP_SendFile;

import java.awt.FileDialog;
import java.awt.Frame;

public class FileChoiser {

	static String RESET = "\u001B[0m";
	static String VERDE = "\u001B[32m";
	static String CIAN = "\u001B[36m";
	static String AMARILLO = "\u001B[33m";
	
	// Eliminamos el parámetro ActionEvent ya que no se utiliza en el método
	public static String selectFile() {
        // 1. Creamos un Frame explícito (aunque no lo mostremos) para tener el control
        Frame parentFrame = new Frame();
        W
        // 2. Le pasamos nuestro Frame al FileDialog en lugar de 'null'
        FileDialog dialog = new FileDialog(parentFrame, "Seleccione archivo a enviar", FileDialog.LOAD);
        dialog.setVisible(true);

        String rutaFinal = null;

        // 3. Obtenemos el archivo
        if (dialog.getFile() != null) {
            rutaFinal = dialog.getDirectory().concat(dialog.getFile());
            System.out.println("Archivo elegido: " + rutaFinal);
        } else {
            System.out.println("No se eligió ningún archivo. Operación cancelada.");
        }

        // =================================================================
        // 4. AQUÍ MATAMOS EL GRÁFICO:
        // Liberamos los recursos nativos y destruimos los hilos de la ventana
        // =================================================================
        dialog.dispose();
        parentFrame.dispose(); 

        return rutaFinal;
    }

}
