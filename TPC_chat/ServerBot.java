package TPC_chat;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ServerBot {

    private static final String CLAVE = "Secreta123456789"; 

    public static void main(String[] args) {
        PrintStream ps = new PrintStream(System.out);
        int puerto = 6789;
        

        List<String> historial = new ArrayList<>();

        ps.println("Iniciando Servidor Bot Seguro en puerto " + puerto + "...");

        try (
            ServerSocket serverSocket = new ServerSocket(puerto);
            Socket cliente = serverSocket.accept(); 
            DataInputStream entradaRed = new DataInputStream(cliente.getInputStream());
            DataOutputStream salidaRed = new DataOutputStream(cliente.getOutputStream())
        ) {
            ps.println(Cifrado.VERDE + "\n[ CONEXION ENTRANTE ESTABLECIDA ]" + Cifrado.RESET);
            ps.println("-> IP: " + cliente.getInetAddress().getHostAddress() + " | Puerto: " + cliente.getPort());
            
            String nick = recibirMensajeCifrado(entradaRed, ps, Cifrado.MORADO, Cifrado.RESET);
            ps.println(Cifrado.CIAN + "Usuario autenticado como: " + Cifrado.RESET + nick);
            
            enviarMensajeCifrado("Bienvenido " + nick + ", conexion cifrada AES activa.", salidaRed, ps, Cifrado.MORADO, Cifrado.RESET);

            // BUCLE PRINCIPAL
            while (true) {
                String peticion = recibirMensajeCifrado(entradaRed, ps, Cifrado.MORADO, Cifrado.RESET);
                
                // DESCONEXIÓN SEGURA
                if (peticion.equalsIgnoreCase("/salir")) {
                    ps.println(Cifrado.VERDE + "\nEl usuario " + nick + " solicito desconexion segura (/salir)." + Cifrado.RESET);
                    // Le enviamos un último mensaje al cliente para que sepa que puede cerrar
                    enviarMensajeCifrado("Desconexion aprobada. ¡Hasta luego!", salidaRed, ps, Cifrado.MORADO, Cifrado.RESET);
                    break; // Rompemos el bucle para que el try-with-resources cierre todo limpio
                }
                
                ps.println("\t\t" + Cifrado.CIAN + nick + " envia: " + Cifrado.RESET + peticion);
                String respuesta = "";
                
                // PROCESAMIENTO DE COMANDOS
                if (peticion.startsWith("/")) {
                	historial.add(peticion);
                    if (peticion.equalsIgnoreCase("/ping")) {
                        respuesta = "pong";  
                    } 
                    else if (peticion.equalsIgnoreCase("/historial")) {
                        StringBuilder sb = new StringBuilder();
                        sb.append("\n--- HISTORIAL ---\n");
                        for (String linea : historial) {
                            sb.append(linea).append("\n");
                        }
                        respuesta = sb.toString();
                    } 
                    else {
                        respuesta = "Comando desconocido. Usa /ping, /historial o /salir.";
                    }
                } 
                // PROCESAMIENTO DE TEXTO NORMAL (Chat)
                else {
                    String lineaChat = nick + " dijo: " + peticion;
                    historial.add(lineaChat); 
                    respuesta = "Mensaje guardado. (Eco: " + peticion + ")";
                }

                enviarMensajeCifrado(respuesta, salidaRed, ps, Cifrado.MORADO, Cifrado.RESET);
            }

        } 
        // CAPTURANDO EL "TIRÓN DE CABLE"
        catch (EOFException e) {
            ps.println(Cifrado.ROJO + "\n[ALERTA] El cliente cerro la conexion abruptamente (EOFException)." + Cifrado.RESET);
        } 
        catch (Exception e) {
            ps.println(Cifrado.ROJO + "\nError de red: " + e.getMessage() + Cifrado.RESET);
        }
        
        ps.println("Servidor apagado correctamente.");
    }

    // (Los métodos de red quedan igual, imprimiendo en consola)
    private static void enviarMensajeCifrado(String texto, DataOutputStream dos, PrintStream ps, String color, String reset) throws Exception {
        byte[] iv = Cifrado.generarIV();
        String encriptado = Cifrado.encriptar(CLAVE, iv, texto);
        ps.println(color + "[ENVIANDO] IV: " + Arrays.toString(iv).substring(0, 15) + "... | Base64: " + encriptado + reset);
        dos.write(iv);
        dos.writeUTF(encriptado);
        dos.flush();
    }

    private static String recibirMensajeCifrado(DataInputStream dis, PrintStream ps, String color, String reset) throws Exception {
        byte[] iv = new byte[16];
        dis.readFully(iv); 
        String encriptado = dis.readUTF(); 
        String desencriptado = Cifrado.decriptar(CLAVE, iv, encriptado);
        ps.println(color + "[RECIBIENDO] Base64: " + encriptado + " -> Descifrado: '" + desencriptado + "'" + reset);
        return desencriptado;
    }
}