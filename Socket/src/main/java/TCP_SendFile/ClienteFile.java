package TCP_SendFile;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.DecimalFormat;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClienteFile {

	String direccion = "130.10.1.54";
	int port = 6767;

	public ClienteFile() {
		Socket sock = null;
		DataOutputStream dos = null;
		DataInputStream dis = null;
		BufferedOutputStream buffOut = null;
		FileInputStream fis = null;
		BufferedInputStream bis = null;

		PrintStream ps = new PrintStream(System.out);
		try {
			InetAddress IP = InetAddress.getByName(direccion);
			ps.println("Conectado...");

			File archivo = new File(FileChoiser.SelectFile(null));

			if (archivo.exists()) {

				DecimalFormat df = new DecimalFormat("#.00");
				ps.println("Se prepara el fichero:" + archivo.getName() + " / " + df.format(archivo.length()) + "Kb");

				sock = new Socket(IP, port);
				sock.setKeepAlive(true);
				sock.setSoTimeout(30000);
				sock.setSoLinger(true, 10);

				ps.println("Cliente conectado: " + sock.getInetAddress().getHostName());

				dos = new DataOutputStream(sock.getOutputStream());
				dis = new DataInputStream(sock.getInputStream());
				buffOut = new BufferedOutputStream(sock.getOutputStream());

				fis = new FileInputStream(archivo);
				bis = new BufferedInputStream(fis);

				dos.writeFloat(archivo.length());
				Thread.sleep(200);
				dos.writeUTF(archivo.getName());
				Thread.sleep(200);

				byte buff[] = new byte[(int) archivo.length()];

				bis.read(buff);
				for (int i = 0; i < buff.length; i++) {
					dos.write(buff[i]);
				}
				Thread.sleep(500);
				ps.println("El archivo:" + archivo.getName() + " se ah enviado");
			} else {
				ps.println("No se seleeciono un archivo");
			}
		} catch (UnknownHostException ex) {
			Logger.getLogger(ClienteFile.class.getName()).log(Level.SEVERE, null, ex);
		} catch (IOException ex) {
			Logger.getLogger(ClienteFile.class.getName()).log(Level.SEVERE, null, ex);
		} catch (InterruptedException ex) {
			Logger.getLogger(ClienteFile.class.getName()).log(Level.SEVERE, null, ex);
		} finally {
			try {
				buffOut.close();
				dos.close();
				dis.close();
				fis.close();
				bis.close();
				sock.close();
			} catch (IOException ex) {
				Logger.getLogger(ClienteFile.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
	}

}
