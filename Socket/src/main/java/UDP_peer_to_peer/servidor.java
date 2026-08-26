package UDP_peer_to_peer;

import java.io.IOException;
import java.io.PrintStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;

public class servidor {

	static PrintStream ps = new PrintStream(System.out);

	// protocolo UDP
	public static void main(String[] args) {
		start();
	}

	public static void start() {
		try {
			DatagramSocket sock = 
					new DatagramSocket(8001, 
							InetAddress.getByName("localhost"));

			DatagramPacket pack = 
					new DatagramPacket(new byte[1024], 1024);
			
			ps.println("Esperando recepcion de datos: ");

			while (true) {
				sock.receive(pack);
				ps.println("\nRecibiendo datos de :" + pack.getAddress().getHostAddress() + " : ");

				// en el caso de ser un numero
				int datosInt = ByteBuffer.wrap(pack.getData()).getInt();

				// en el caso que sea un texto
				// pack.getData().toString();
				///String datoStr = new String(pack.getData(), 0, pack.getLength());

				// En el caso que sea un conjunto de datos: una Class
				///MisDatos datosObj = MisDatos.fromByArray(pack.getData());
				
				ps.println(pack.getData() );
				ps.println(datosInt);
				//ps.println(datosObj);
			}

		} catch (SocketException | UnknownHostException ex) {
			ex.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
