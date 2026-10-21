
package TPC_Socker_Thread;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicBoolean;

public class MainCliente {

    private static final String HOST = "localhost";
    private static final int PUERTO = 7777;

    public static void main(String[] args) {
        BufferedReader teclado = new BufferedReader(
                new InputStreamReader(System.in)
        );

        System.out.println(
                Colores.CYAN
                + "====================================\n"
                + "          CLIENTE DE CHAT\n"
                + "===================================="
                + Colores.RESET
        );

        AtomicBoolean cerrando = new AtomicBoolean(false);

        try (Socket socket = new Socket(HOST, PUERTO)) {
            DataInputStream disServidor = new DataInputStream(
                    socket.getInputStream()
            );

            DataOutputStream dosServidor = new DataOutputStream(
                    socket.getOutputStream()
            );

            System.out.print(Colores.AMARILLO + "Ingresá tu nickname: "
                    + Colores.RESET);

            String nickname = teclado.readLine();

            if (nickname == null || nickname.trim().isEmpty()) {
                System.out.println(
                        Colores.ROJO + "Nickname inválido." + Colores.RESET
                );
                return;
            }

            // Enviar nickname cifrado.
            enviar(dosServidor, nickname.trim());

            // El servidor acepta o rechaza el nickname.
            String respuesta = recibir(disServidor);
            mostrarMensaje(respuesta);

            if (respuesta.startsWith("ERROR|")) {
                return;
            }

            Thread recibirMensajes = new Thread(() -> {
                try {
                    while (!socket.isClosed()) {
                        String mensaje = recibir(disServidor);
                        mostrarMensaje(mensaje);

                        System.out.print(
                                Colores.CYAN + "vos> " + Colores.RESET
                        );
                    }

                } catch (IOException e) {
                    if (!cerrando.get()) {
                        System.out.println(
                                "\n" + Colores.ROJO
                                + "Conexión cerrada por el servidor."
                                + Colores.RESET
                        );
                    }
                }
            }, "RECEPCION");

            recibirMensajes.start();

            System.out.println(
            		Colores.GREEN
                    + "Conectado. Escribí /ayuda para ver los comandos."
                    + Colores.RESET
            );

            System.out.print(Colores.CYAN + "vos> " + Colores.RESET);

            String mensaje;

            while ((mensaje = teclado.readLine()) != null) {
                mensaje = mensaje.trim();

                if (mensaje.isEmpty()) {
                    System.out.print(
                            Colores.CYAN + "vos> " + Colores.RESET
                    );
                    continue;
                }

                if (mensaje.equalsIgnoreCase("/ayuda")
                        || mensaje.equalsIgnoreCase("/help")) {

                    mostrarAyuda();
                    System.out.print(
                            Colores.CYAN + "vos> " + Colores.RESET
                    );
                    continue;
                }

                // Enviar texto cifrado; el servidor lo descifra.
                enviar(dosServidor, mensaje);

                if (mensaje.equalsIgnoreCase("/logout")
                        || mensaje.equalsIgnoreCase("/salir")
                        || mensaje.equalsIgnoreCase("/desconectar")) {

                    cerrando.set(true);
                    break;
                }

                System.out.print(
                        Colores.CYAN + "vos> " + Colores.RESET
                );
            }

            if (!cerrando.get()) {
                try {
                    enviar(dosServidor, "/logout");
                } catch (IOException ignored) {
                    // El servidor pudo cerrar la conexión antes.
                }
                cerrando.set(true);
            }

            // Cerrar el socket libera al hilo bloqueado en readUTF().
            socket.close();
            recibirMensajes.join();

            System.out.println(
                    Colores.AMARILLO + "Desconectado del chat."
                    + Colores.RESET
            );

        } catch (IOException e) {
            System.err.println(
                    Colores.ROJO + "Error de conexión: "
                    + e.getMessage() + Colores.RESET
            );

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("El cliente fue interrumpido.");
        }
    }

    private static void enviar(
            DataOutputStream salida,
            String mensaje) throws IOException {

        salida.writeUTF(CifradoAES.cifrar(mensaje));
        salida.flush();
    }

    private static String recibir(
            DataInputStream entrada) throws IOException {

        return CifradoAES.descifrar(entrada.readUTF());
    }

    private static void mostrarMensaje(String respuesta) {
        String[] partes = respuesta.split("\\|", 3);

        if (partes.length < 2) {
            System.out.println(respuesta);
            return;
        }

        String tipo = partes[0];
        String contenido = partes[1];

        switch (tipo) {
            case "SYS":
                if (partes.length > 2) {
                    contenido += "|" + partes[2];
                }
                System.out.println(
                		Colores.GREEN + "[SISTEMA] "
                        + contenido + Colores.RESET
                );
                break;

            case "ERROR":
                if (partes.length > 2) {
                    contenido += "|" + partes[2];
                }
                System.out.println(
                        Colores.ROJO + "[ERROR] "
                        + contenido + Colores.RESET
                );
                break;

            case "CHAT":
                if (partes.length > 2) {
                    System.out.println(
                            Colores.AZUL + "[CHAT] "
                            + contenido + ": "
                            + Colores.RESET + partes[2]
                    );
                }
                break;

            case "PRIV":
                if (partes.length > 2) {
                    System.out.println(
                            Colores.VIOLETA + "[PRIVADO] "
                            + contenido + ": "
                            + Colores.RESET + partes[2]
                    );
                }
                break;

            case "USERS":
                if (partes.length > 2) {
                    contenido += "|" + partes[2];
                }
                System.out.println(
                        Colores.AMARILLO + "[USUARIOS] "
                        + contenido + Colores.RESET
                );
                break;

            default:
                System.out.println(respuesta);
        }
    }

    private static void mostrarAyuda() {
        System.out.println(
                Colores.AMARILLO
                + "\n============= COMANDOS =============\n"
                + "/ayuda                 Mostrar ayuda\n"
                + "/usuarios              Listar conectados\n"
                + "/msg Juan Hola         Privado para Juan\n"
                + "Hola#Juan              Formato privado original\n"
                + "/logout                Desloguearse\n"
                + "/salir                 Desloguearse\n"
                + "Texto normal           Mensaje para todos\n"
                + "===================================="
                + Colores.RESET
        );
    }
}
