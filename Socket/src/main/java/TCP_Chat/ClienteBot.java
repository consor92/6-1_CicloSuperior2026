package TCP_Chat;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

public class ClienteBot {

	private static String CLAVE = "ClaveSecreta1234";

	public static void main(String[] args) {

		PrintStream ps = new PrintStream(System.out);
		String direccion = "127.0.0.1";
		int puerto = 6767;

		ps.println("Iniciado cliente Seguro ");
		
		ps.print( "Ingrese IP server:" );
		
		try (InputStreamReader in = new InputStreamReader(System.in);
			 BufferedReader consola = new BufferedReader(in)){
			direccion = consola.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}

		try (Socket socket = new Socket(direccion, puerto);
			DataOutputStream salida = new DataOutputStream(socket.getOutputStream());
			DataInputStream entrada = new DataInputStream(socket.getInputStream());
			InputStreamReader in = new InputStreamReader(System.in);
			BufferedReader consola = new BufferedReader(in);
			) 
		{

			ps.println( Cifrado.VERDE + "Conectado al servidor Bot" + Cifrado.RESET );
			
			ps.println( "Ingrese su password de seguridad:" );
			CLAVE = consola.readLine();
			
			ps.println( "Ingrese su Nickname para unirse:" );
			String nick = consola.readLine();
			enviarMensajeCifrado( nick , salida );
			
			ps.println( Cifrado.CIAN +
						recibirMensaje( entrada ) +
						Cifrado.RESET					
					);
			ps.println(Cifrado.AMARILLO +
					   "Comando: '/ping', '/historial', o texto libre. Escriba /salir para Logout." +
					   Cifrado.RESET);
			
			String mensaje;
			//ciclo de vida del cliente
			while(true) {
				ps.println("\nTu peticion:");
				mensaje = consola.readLine();
				
				if( mensaje.equalsIgnoreCase("/salir") ) {
					enviarMensajeCifrado(mensaje, salida);
					
					ps.println(Cifrado.CIAN +
							   "Servidor " +
							   Cifrado.RESET +
							   recibirMensaje(entrada)
							);
					ps.println(Cifrado.AMARILLO + 
							"Cerrando recursos del red de forma segura. ¡Adios!" +
							Cifrado.RESET
							);
					break;
				}//   \salir
				
				if( mensaje.equalsIgnoreCase("/ping")  ) {
					long tiempoInicial = System.currentTimeMillis();      
					enviarMensajeCifrado("/ping", salida);
					String respuesta = recibirMensaje(entrada);
					long tiempoFinal = System.currentTimeMillis();
					
					ps.println( Cifrado.CIAN +
							"Ping respuesta: " +
							Cifrado.RESET +
							respuesta
							);
					ps.println( Cifrado.AMARILLO + 
							    "Latencia: " +
							    (tiempoFinal - tiempoInicial) +
							    " ms" +
                                Cifrado.RESET 
							  );
					continue;
				}//   \ping
				
				enviarMensajeCifrado( mensaje , salida);
				ps.println(Cifrado.CIAN +
						"Servidor responde: " +
						Cifrado.RESET +
						recibirMensaje(entrada)
						);
			}	
		} catch (Exception ex) {

		}

	}

	private static String recibirMensaje(DataInputStream entrada) throws IOException {
		byte[] iv = new byte[16];
		entrada.readFully(iv);
		String msg = entrada.readUTF();
		
		return  Cifrado.decriptar( CLAVE , iv , msg ) ;
	}

	private static void enviarMensajeCifrado(String msg, DataOutputStream salida) throws IOException {
		byte[] iv = Cifrado.generarIV();
		
		salida.write(iv);
		salida.writeUTF(  Cifrado.encriptar(CLAVE , iv , msg)   );
		salida.flush();
	}

}
