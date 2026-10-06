package TPC_chat;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

public class ClienteBot {

    private static final String CLAVE = "Secreta123456789"; 

    public static void main(String[] args) {
        PrintStream ps = new PrintStream(System.out);
        String direccion = "127.0.0.1";
        int puerto = 6789;

        ps.println("Iniciando Cliente (Modo Cifrado)...");

        try (
            Socket socket = new Socket(direccion, puerto);
            DataOutputStream salidaRed = new DataOutputStream(socket.getOutputStream());
            DataInputStream entradaRed = new DataInputStream(socket.getInputStream());
            BufferedReader teclado = new BufferedReader(new InputStreamReader(System.in))
        ) {
            ps.println(Cifrado.VERDE + "Conectado al Servidor Bot." + Cifrado.RESET);
            
            // --- HANDSHAKE ---
            ps.print("Ingresa tu Nickname para unirte: ");
            String nick = teclado.readLine();
            enviarMensajeCifrado(nick, salidaRed);
            
            String bienvenida = recibirMensajeCifrado(entradaRed);
            ps.println(Cifrado.CIAN + "Servidor: " + Cifrado.RESET + bienvenida);

            // Instrucciones actualizadas con '/'
            ps.println(Cifrado.AMARILLO + "Comandos: '/ping', '/historial', o texto libre. Escribe '/salir' para terminar." + Cifrado.RESET);

            // --- BUCLE PRINCIPAL ---
            String mensaje;
            while (true) {
                ps.print("\nTu peticion: ");
                mensaje = teclado.readLine();
                
                // DESCONEXIÓN SEGURA
                if (mensaje.equalsIgnoreCase("/salir")) {
                    enviarMensajeCifrado("/salir", salidaRed);
                    
                    // Esperamos la confirmación del servidor antes de cerrar
                    String confirmacion = recibirMensajeCifrado(entradaRed);
                    ps.println(Cifrado.CIAN + "Servidor: " + Cifrado.RESET + confirmacion);
                    ps.println(Cifrado.AMARILLO + "Cerrando recursos de red de forma segura. ¡Adios!" + Cifrado.RESET);
                    break; 
                    // Al hacer break, el bloque try-with-resources cierra el Socket y los Streams automáticamente
                }

                // COMANDO PING
                if (mensaje.equalsIgnoreCase("/ping")) {
                    long tiempoInicio = System.currentTimeMillis();
                    enviarMensajeCifrado("/ping", salidaRed);
                    String respuesta = recibirMensajeCifrado(entradaRed);
                    long tiempoFin = System.currentTimeMillis();
                    
                    ps.println(Cifrado.CIAN + "Ping respuesta: " + Cifrado.RESET + respuesta);
                    ps.println(Cifrado.AMARILLO + "Latencia: " + (tiempoFin - tiempoInicio) + " ms" + Cifrado.RESET);
                    continue;
                }

                // MENSAJE NORMAL O CUALQUIER OTRO COMANDO
                enviarMensajeCifrado(mensaje, salidaRed);
                String respuesta = recibirMensajeCifrado(entradaRed);
                ps.println(Cifrado.CIAN + "Servidor responde: " + Cifrado.RESET + respuesta);
            }

        } catch (Exception e) {
            ps.println("Error en cliente o desconexion abrupta: " + e.getMessage());
        }
    }

    // (Los métodos enviarMensajeCifrado y recibirMensajeCifrado quedan exactamente igual que antes)
    private static void enviarMensajeCifrado(String texto, DataOutputStream dos) throws Exception {
        byte[] iv = Cifrado.generarIV();
        String encriptado = Cifrado.encriptar(CLAVE, iv, texto);
        dos.write(iv);
        dos.writeUTF(encriptado);
        dos.flush();
    }

    private static String recibirMensajeCifrado(DataInputStream dis) throws Exception {
        byte[] iv = new byte[16];
        dis.readFully(iv);
        String encriptado = dis.readUTF();
        return Cifrado.decriptar(CLAVE, iv, encriptado);
    }
}