package TCP_SendFile;

import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.Socket;
import java.text.DecimalFormat;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClienteFile {

	String direccion = "127.0.0.1";
	int port = 6767;

	public ClienteFile() {
		Socket sock = null;
		DataOutputStream dos = null;
		FileInputStream fis = null;
		BufferedInputStream bis = null;

		PrintStream ps = new PrintStream(System.out);

		try {
			InetAddress IP = InetAddress.getByName(direccion);
			ps.println("Conectando al servidor...");

			// Obtenemos el archivo mediante la clase FileChoiser (previamente armada por
			// ti)
			String rutaArchivo = FileChoiser.selectFile();

			if (rutaArchivo != null) {
				File archivo = new File(rutaArchivo);

				if (archivo.exists() && archivo.isFile()) {
					DecimalFormat df = new DecimalFormat("#.00");
					ps.println("Se prepara el fichero: " + archivo.getName() + " / "
							+ df.format(archivo.length() / 1024.0) + " Kb");

					sock = new Socket(IP, port);

					// Mantiene la conexión "viva". El SO envía paquetes de prueba (probes)
					// periódicamente si no hay actividad, para detectar si el otro extremo se
					// desconectó.
					sock.setKeepAlive(true);

					// Tiempo máximo de bloqueo de lectura (30 segundos).
					// Si la red se cae, el 'read()' no se quedará colgado infinitamente; lanzará un
					// SocketTimeoutException.
					sock.setSoTimeout(30000);

					// Cierre elegante (Graceful close). Si se llama a close() pero aún hay bytes en
					// tránsito,
					// el socket esperará hasta 10 segundos intentando enviarlos antes de
					// destruirse.
					sock.setSoLinger(true, 10);

					// Por defecto, TCP agrupa paquetes pequeños antes de enviarlos para ahorrar
					// ancho de banda.
					// Al ponerlo en true, los datos se envían INMEDIATAMENTE (menor latencia).
					sock.setTcpNoDelay(true);

					// Tamaño de los Buffers del Sistema Operativo.
					// Aumentar esto (ej. a 64KB) le dice al SO que asigne más memoria para encolar
					// paquetes en la tarjeta de red,
					// lo que aumenta drásticamente el rendimiento en transferencias de archivos
					// grandes.
					sock.setSendBufferSize(64 * 1024);
					sock.setReceiveBufferSize(64 * 1024);

					System.out.println(FileChoiser.VERDE + "====================================================="
							+ FileChoiser.RESET);
					System.out.println(FileChoiser.VERDE + "       [ CLIENTE: CONECTADO AL SERVIDOR ]            "
							+ FileChoiser.RESET);
					System.out.println(FileChoiser.VERDE + "====================================================="
							+ FileChoiser.RESET);
					System.out.printf(FileChoiser.CIAN + "%-20s: " + FileChoiser.AMARILLO + "%s%n" + FileChoiser.RESET,
							"Servidor Remoto", sock.getInetAddress().getHostName());
					System.out.printf(FileChoiser.CIAN + "%-20s: " + FileChoiser.AMARILLO + "%s%n" + FileChoiser.RESET,
							"IP Servidor", sock.getInetAddress().getHostAddress());
					System.out.printf(FileChoiser.CIAN + "%-20s: " + FileChoiser.AMARILLO + "%d%n" + FileChoiser.RESET,
							"Puerto Servidor", sock.getPort());
					System.out.printf(FileChoiser.CIAN + "%-20s: " + FileChoiser.AMARILLO + "%s%n" + FileChoiser.RESET,
							"Mi IP Local", sock.getLocalAddress().getHostAddress());
					System.out.printf(FileChoiser.CIAN + "%-20s: " + FileChoiser.AMARILLO + "%d%n" + FileChoiser.RESET,
							"Mi Puerto Local", sock.getLocalPort());
					System.out.println(FileChoiser.VERDE + "====================================================="
							+ FileChoiser.RESET);

					// Utilizamos DataOutputStream para enviar los "metadatos" del archivo
					// fácilmente
					dos = new DataOutputStream(sock.getOutputStream());

					// MEJORA 1: Usamos 'long' en vez de 'float' para el tamaño del archivo.
					long tamañoArchivo = archivo.length();
					dos.writeLong(tamañoArchivo);
					dos.writeUTF(archivo.getName());

					// Forzamos el envío de los metadatos antes de enviar los bytes del archivo
					dos.flush();

					fis = new FileInputStream(archivo);
					bis = new BufferedInputStream(fis);

					// MEJORA 2: Buffer fijo de 8 KB (8192 bytes) para no saturar la memoria RAM.
					byte[] buffer = new byte[8192];
					int bytesLeidos;

					// MEJORA 3: Leemos bloques y los enviamos directamente, sin Thread.sleep().
					while ((bytesLeidos = bis.read(buffer)) != -1) {
						dos.write(buffer, 0, bytesLeidos);
					}

					// Aseguramos que se vacíe el conducto y llegue todo al servidor
					dos.flush();
					ps.println("El archivo: " + archivo.getName() + " se ha enviado exitosamente.");
				}
			} else {
				System.out.println("El cliente canceló el envío antes de conectarse.");
			}

		} catch (IOException ex) {
			Logger.getLogger(ClienteFile.class.getName()).log(Level.SEVERE, null, ex);
		} finally {
			// Cierre seguro y ordenado de los recursos.
			try {
				if (bis != null)
					bis.close();
				if (fis != null)
					fis.close();
				if (dos != null)
					dos.close();
				if (sock != null)
					sock.close();
			} catch (IOException ex) {
				Logger.getLogger(ClienteFile.class.getName()).log(Level.SEVERE, null, ex);
			}
			ps.println("----CLIENTE DESCONECTADO----");

		}
	}

}
