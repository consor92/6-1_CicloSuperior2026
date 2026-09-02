package UDP_Brodcast;

import java.io.IOException;
import java.io.PrintStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Receptot_Brodcast {
	
	@SuppressWarnings("deprecation")
	public static void main(String args[])
	{
		PrintStream ps = new PrintStream(System.out);
		
		try {
			InetAddress group = InetAddress.getByName("230.0.0.0");
			MulticastSocket sock = new MulticastSocket(6790);
			
			sock.joinGroup(group);
			
			byte[] buf = new byte[1024];
			String texto="";
			
			ps.println("Esperando mesanje");
			while(true) {
				DatagramPacket datos = new DatagramPacket(
						buf, buf.length);
				sock.receive(datos);
				
			   texto = new String(
					   datos.getData(), 
					   datos.getOffset(),
					   datos.getLength()
					   );	
			   
			   ps.printf("Recibimos: %s \n" , texto);
			   
			   if(texto.equals("/salir") )	
				   break;
			}
			sock.close();
			sock.leaveGroup(group);
			
		} catch (UnknownHostException e) {
			Logger.getLogger(Receptot_Brodcast.class.getName()).log(Level.SEVERE, null, e);
		} catch (IOException e) {
			Logger.getLogger(Receptot_Brodcast.class.getName()).log(Level.SEVERE, null, e);
		}
		
	}
}
