package TCP_Chat;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ServerBot {

	private static String CLAVE = "ClaveSecreta1234";

	public static void main(String[] args) {
		
		PrintStream ps = new PrintStream(System.out);
		int puerto = 6767;
		
		List<String> historial = new ArrayList<>();
		
		ps.println("Iniciando Servidor Bot Seguro con puerto: " +
					puerto +
					"...");
		
		try(ServerSocket server = new ServerSocket( puerto );
			Socket sockerCli = server.accept();	
			DataInputStream entrada = new DataInputStream(sockerCli.getInputStream());
			DataOutputStream salida = new DataOutputStream(sockerCli.getOutputStream());
		   ){
			ps.println(Cifrado.VERDE +
					   "\n[ CONEXION ENTRANTE ESTABLECIDA ]" +
					   Cifrado.RESET
					);
			ps.println("-> IP:" + 
					  sockerCli.getInetAddress().getHostAddress() +
					  " | Puero: " +
					  sockerCli.getPort()
					);
			
			String nickName = recibirMensajeCifrado(entrada);
			ps.println(Cifrado.CIAN +
					   "Usuario autenticado como: " +
					   Cifrado.RESET +
					   nickName
					);
			
			enviarMensajeCifrado( "Bienvenido " +
								  nickName +
								  " conexion cifrada activa"
					             , salida); 
			
			while( true ) {
				String msg = recibirMensajeCifrado(entrada).trim();
				
				if( msg.equalsIgnoreCase( "/salir" )) {
					ps.println( Cifrado.VERDE +
							    "\nEl usuario " +
								nickName +
								" solicito desconexion seura (/salir)" +
								Cifrado.RESET
								);
					enviarMensajeCifrado("Desconexion aprobada. ¡Adios!", salida);
					break;
				}
				
				ps.println( "\t\t" +
						Cifrado.CIAN +
						nickName +
						"Recibo: " +
						Cifrado.RESET + 
						msg
						);
				
				
				if( msg.startsWith("/")) {
					historial.add( msg );
					switch( msg ) {
						case "/ping":
							enviarMensajeCifrado("pong", salida);
						break;
						case "/historial":
							StringBuilder sb = new StringBuilder();
							sb.append("\n--- HISTORIAL ---\n");
							for(String linea : historial) {
								sb.append( linea ).append("\n");
							}
							
							enviarMensajeCifrado( sb.toString() , salida);
						break;
					}
					
				}else {
					//texto libre de cliente
					historial.add( msg );
					enviarMensajeCifrado("Mensaje recibido/guarado. " +
										"(Eco:" + 
										msg + 
										")"
										, salida);
				}

			}
			
		}catch (EOFException ex) {
			ps.println(Cifrado.ROJO + "\n[ALERTA] El cliente cerro la conexion abrutamente" + Cifrado.RESET);
		}catch (Exception ex) {
			ps.println( Cifrado.ROJO + "\nError en red: " + ex.getLocalizedMessage() + Cifrado.RESET );
		}
		
		ps.println("Servidor apagado correctamente.");
	}

	private static void enviarMensajeCifrado(String msg, DataOutputStream salida) throws IOException {
		byte[] iv = Cifrado.generarIV();
		
		salida.write(iv);
		salida.writeUTF( Cifrado.encriptar( CLAVE , iv, msg) );
		salida.flush();
	}

	private static String recibirMensajeCifrado(DataInputStream entrada) throws IOException {
		byte[] iv = new byte[16];
		entrada.readFully(iv);
		
		return Cifrado.decriptar(CLAVE, iv, entrada.readUTF() );
	}

}
