package TCP_SendFile;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.DecimalFormat;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServerFile {

	ServerSocket server = null;
	int port = 6767;
	Socket sockCli = null;
	DataInputStream dis = null;
	PrintStream ps = null;

	public ServerFile() {
		try {
			ps = new PrintStream(System.out);

			server = new ServerSocket(port);
			ps.println("Esperando conexion de Cliente ....");
			sockCli = server.accept();
			sockCli.setKeepAlive(true);
			sockCli.setSoTimeout(30000);
			sockCli.setSoLinger(true, 10);

			ps.println("Se ha conectado un Cliete: " + sockCli.getInetAddress().getHostAddress());

			dis = new DataInputStream(sockCli.getInputStream());
			float peso = dis.readFloat();
			ps.println("Recibiendo archivo PESO: " + peso);
			Thread.sleep(200);

			String name = dis.readUTF();
			ps.println("Recibiendo archivo " + name);
			Thread.sleep(200);

			File archivo = new File("RECIBIDOS/" + name);
			if (archivo.exists()) {
				archivo.delete();
			}

			FileOutputStream fos = new FileOutputStream(archivo, true);
			BufferedOutputStream out = new BufferedOutputStream(fos);

			BufferedInputStream bis = new BufferedInputStream(sockCli.getInputStream());
			byte[] buff = new byte[(int) peso];

			int in = 0;
			while ((in = bis.read(buff)) != -1) {
				out.write(buff, 0, in);
			}

			out.close();
			fos.close();
			dis.close();
			sockCli.close();
			server.close();

			if (archivo.length() == peso) {
				ps.println("Completo");
			} else {
				DecimalFormat dc = new DecimalFormat("#.00");
				ps.println("CORRUPTO:" + dc.format(archivo.length()));
			}
		} catch (IOException ex) {
			Logger.getLogger(ServerFile.class.getName()).log(Level.SEVERE, null, ex);

		} catch (InterruptedException ex) {
			Logger.getLogger(ServerFile.class.getName()).log(Level.SEVERE, null, ex);
		}finally {
	        ps.println("----SERVIDOR DESCONECTADO----");
		}
	}

}
