package UDP_Brodcast;

import java.io.IOException;
import java.io.PrintStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;

public class Emisor_milticast {
	public static void main(String args[]) {
		PrintStream ps = new PrintStream(System.out);
		try {
			MulticastSocket sock = new MulticastSocket();
			
			sock.setSoTimeout(1000);
			InetAddress group = 
					InetAddress.getByName("230.0.0.0");//230.0.0.1
			
			ps.println("Enviando mensaje masivo");
			byte[] msg = ("Holaa").getBytes();
			
			DatagramPacket pack = new DatagramPacket(
					msg, 
					msg.length);
			pack.setPort(6790);
			pack.setAddress(group);
			
			sock.send(pack);
			ps.println("Enviando.");
			Thread.sleep(1000);
			sock.close();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
