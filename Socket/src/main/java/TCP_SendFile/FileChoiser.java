package TCP_SendFile;

import java.awt.FileDialog;

public class FileChoiser {

	static public String SelectFile(java.awt.event.ActionEvent evt) {
		FileDialog dialog = new FileDialog( (java.awt.Frame)null, "Seleccione achivo a enviar", FileDialog.LOAD);
		dialog.setVisible(true);

		if (dialog.getFile() != null) {
			String ruta = dialog.getDirectory().concat(dialog.getFile());
			System.out.println("Archivo elegido:" + ruta);
			return ruta;
		} else {
			System.out.println("Sin achivo elegido");
		}
		return null;
	}

}
