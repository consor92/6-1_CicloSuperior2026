package TCP_SendFile;

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

	public ServerFile() {
		Socket sockCli = null;
		DataInputStream dis = null;
		FileOutputStream fos = null;
		BufferedOutputStream out = null;
		PrintStream ps = new PrintStream(System.out);

		try {
			server = new ServerSocket(port);

			ps.println("Esperando conexion de Cliente en el puerto " + port + "....");

			sockCli = server.accept();

			// Mantiene la conexión "viva". El SO envía paquetes de prueba (probes)
			// periódicamente si no hay actividad, para detectar si el otro extremo se
			// desconectó.
			sockCli.setKeepAlive(true);

			// Tiempo máximo de bloqueo de lectura (30 segundos).
			// Si la red se cae, el 'read()' no se quedará colgado infinitamente; lanzará un
			// SocketTimeoutException.
			sockCli.setSoTimeout(30000);

			// Cierre elegante (Graceful close). Si se llama a close() pero aún hay bytes en
			// tránsito,
			// el socket esperará hasta 10 segundos intentando enviarlos antes de
			// destruirse.
			sockCli.setSoLinger(true, 10);

			// Por defecto, TCP agrupa paquetes pequeños antes de enviarlos para ahorrar
			// ancho de banda.
			// Al ponerlo en true, los datos se envían INMEDIATAMENTE (menor latencia).
			sockCli.setTcpNoDelay(true);

			// Tamaño de los Buffers del Sistema Operativo.
			// Aumentar esto (ej. a 64KB) le dice al SO que asigne más memoria para encolar
			// paquetes en la tarjeta de red,
			// lo que aumenta drásticamente el rendimiento en transferencias de archivos
			// grandes.
			sockCli.setSendBufferSize(64 * 1024);
			sockCli.setReceiveBufferSize(64 * 1024);

			ps.println(FileChoiser.VERDE + "=====================================================" + FileChoiser.RESET);
			ps.println(FileChoiser.VERDE + "             [ CONEXIÓN ESTABLECIDA ]                " + FileChoiser.RESET);
			ps.println(FileChoiser.VERDE + "=====================================================" + FileChoiser.RESET);

			ps.printf(FileChoiser.CIAN + "%-20s: " + FileChoiser.AMARILLO + "%s%n" + FileChoiser.RESET, "Host Remoto (Name)", sockCli.getInetAddress().getHostName());
			ps.printf(FileChoiser.CIAN + "%-20s: " + FileChoiser.AMARILLO + "%s%n" + FileChoiser.RESET, "Host Remoto (IP)", sockCli.getInetAddress().getHostAddress());
			ps.printf(FileChoiser.CIAN + "%-20s: " + FileChoiser.AMARILLO + "%d%n" + FileChoiser.RESET, "Puerto Remoto", sockCli.getPort());
			ps.printf(FileChoiser.CIAN + "%-20s: " + FileChoiser.AMARILLO + "%s%n" + FileChoiser.RESET, "Host Local (IP)", sockCli.getLocalAddress().getHostAddress());
			ps.printf(FileChoiser.CIAN + "%-20s: " + FileChoiser.AMARILLO + "%d%n" + FileChoiser.RESET, "Puerto Local", sockCli.getLocalPort());

			ps.println(FileChoiser.VERDE + "=====================================================" + FileChoiser.RESET);			
			dis = new DataInputStream(sockCli.getInputStream());

			// Recibimos los metadatos (Notar que usamos readLong porque el cliente envió
			// writeLong)
			long pesoEsperado = dis.readLong();
			String name = dis.readUTF();

			ps.println("Recibiendo archivo: " + name + " | Tamaño esperado: " + pesoEsperado + " bytes");

			// MEJORA 5: Buena práctica: asegurarnos de que la carpeta de destino exista.
			File directorio = new File("RECIBIDOS");
			if (!directorio.exists()) {
				directorio.mkdirs();
			}

			File archivo = new File(directorio, name);
			if (archivo.exists()) {
				archivo.delete(); // Si el archivo ya existía, lo borramos para sobrescribirlo.
			}

			fos = new FileOutputStream(archivo);
			out = new BufferedOutputStream(fos);

			// Buffer de 8 KB para procesar el flujo entrante
			byte[] buffer = new byte[8192];
			int bytesLeidos;
			long totalRecibido = 0;

			// MEJORA 6: Leemos el socket asegurándonos de NO leer más allá del
			// "pesoEsperado".
			// Esto es crucial por si el cliente envía otros datos después del archivo.
			while (totalRecibido < pesoEsperado) {
				// Calculamos cuánto falta leer, para no pasarnos
				int bytesRestantes = (int) Math.min(buffer.length, pesoEsperado - totalRecibido);
				bytesLeidos = dis.read(buffer, 0, bytesRestantes);

				if (bytesLeidos == -1) {
					break; // El cliente cerró la conexión abruptamente
				}

				out.write(buffer, 0, bytesLeidos);
				totalRecibido += bytesLeidos;
			}

			// Obligamos a que cualquier dato en el buffer se escriba en el disco duro
			out.flush();

			// Verificamos la integridad
			if (totalRecibido == pesoEsperado) {
				ps.println("Descarga completa y exitosa.");
			} else {
				DecimalFormat dc = new DecimalFormat("#.00");
				ps.println("ARCHIVO CORRUPTO: Se esperaban " + pesoEsperado + " bytes pero se recibieron "
						+ totalRecibido);
			}

		} catch (IOException ex) {
			Logger.getLogger(ServerFile.class.getName()).log(Level.SEVERE, null, ex);
		} finally {
			// Cierre ordenado de los recursos en orden inverso a su apertura
			try {
				if (out != null)
					out.close();
				if (fos != null)
					fos.close();
				if (dis != null)
					dis.close();
				if (sockCli != null)
					sockCli.close();
				if (server != null)
					server.close();
			} catch (IOException ex) {
				Logger.getLogger(ServerFile.class.getName()).log(Level.SEVERE, null, ex);
			}
			ps.println("----SERVIDOR DESCONECTADO----");
		}
	}
}
