package TPC_Socker_Thread;


import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class Servidor implements AutoCloseable {

    private final ServerSocket server;
    private volatile boolean ejecutando = true;

    private final ConcurrentMap<String, Cliente> clientes =
            new ConcurrentHashMap<>();

    public Servidor(int puerto) throws IOException {
        server = new ServerSocket(puerto);
    }

    public void iniciar() {
        System.out.println(
                Colores.GREEN
                + "Servidor escuchando en puerto "
                + server.getLocalPort()
                + Colores.RESET
        );

        Thread consola = new Thread(
                this::consolaAdministracion,
                "CONSOLA-SERVIDOR"
        );
        consola.setDaemon(true);
        consola.start();

        while (ejecutando) {
            try {
                Socket socket = server.accept();

                System.out.println(
                		Colores.AZUL
                        + "[CONEXIÓN] "
                        + socket.getInetAddress().getHostAddress()
                        + Colores.RESET
                );

                new Thread(
                        () -> registrarCliente(socket),
                        "REGISTRO-" + socket.getPort()
                ).start();

            } catch (IOException e) {
                if (ejecutando) {
                    System.err.println(
                           Colores.ROJO
                            + "[ERROR] " + e.getMessage()
                            + Colores.RESET
                    );
                }
                break;
            }
        }
    
	}

    private void registrarCliente(Socket socket) {
        Cliente cliente = null;

        try {
            DataInputStream entrada = new DataInputStream(
                    socket.getInputStream()
            );
            DataOutputStream salida = new DataOutputStream(
                    socket.getOutputStream()
            );

            // El nickname también llega cifrado.
            String nickname =
                    CifradoAES.descifrar(entrada.readUTF()).trim();

            if (!nicknameValido(nickname)) {
                enviarDirecto(salida, "ERROR|Nickname inválido.");
                socket.close();
                return;
            }

            cliente = new Cliente(
                    nickname, socket, entrada, salida, this
            );

            String clave = normalizar(nickname);

            if (clientes.putIfAbsent(clave, cliente) != null) {
                enviarDirecto(
                        salida,
                        "ERROR|Ese nickname ya está en uso."
                );
                socket.close();
                return;
            }

            if (!cliente.enviar(
                    "SYS|Bienvenido " + nickname
                    + ". Escribí /ayuda para ver los comandos."
            )) {
                return;
            }

            System.out.println(
            		Colores.VERDE
                    + "[CONECTADO] " + nickname
                    + Colores.RESET
            );

            cliente.iniciar();

            notificarTodos(
                    "SYS|" + nickname + " se ha unido al chat.",
                    cliente
            );

        } catch (IOException e) {
            if (cliente != null) {
                cliente.cerrar();
            } else {
                try {
                    socket.close();
                } catch (IOException ignored) {
                    // La conexión ya se cerró.
                }
            }

            System.err.println(
                   Colores.AMARILLO
                    + "[AVISO] No se pudo registrar un cliente."
                    + Colores.RESET
            );
        }
    
	}

    private void enviarDirecto(
            DataOutputStream salida,
            String mensaje) throws IOException {

        salida.writeUTF(CifradoAES.cifrar(mensaje));
        salida.flush();
    }

    private boolean nicknameValido(String nickname) {
        return nickname != null
                && !nickname.isBlank()
                && nickname.length() <= 20
                && !nickname.startsWith("/")
                && !nickname.contains("#")
                && !nickname.contains("|")
                && !nickname.contains(",")
                && nickname.chars().noneMatch(
                        Character::isISOControl
                );
    }

    private String normalizar(String nickname) {
        return nickname.toLowerCase(Locale.ROOT);
    }

    public void difundir(Cliente emisor, String mensaje) {
        String respuesta = "CHAT|"
                + emisor.getNickName() + "|" + mensaje;

        for (Cliente cliente : clientes.values()) {
            if (cliente != emisor) {
                cliente.enviar(respuesta);
            }
        }

        System.out.println(
               Colores.CYAN
                + "[CHAT] " + emisor.getNickName()
                + ": " + mensaje
                + Colores.RESET
        );
    }

    public void enviarPrivado(
            Cliente emisor,
            String destinatario,
            String mensaje) {

        Cliente receptor = clientes.get(normalizar(destinatario));

        if (receptor == null) {
            emisor.enviar(
                    "ERROR|El usuario " + destinatario
                    + " no está conectado."
            );
            return;
        }

        if (receptor == emisor) {
            emisor.enviar(
                    "ERROR|No podés enviarte mensajes a vos mismo."
            );
            return;
        }

        receptor.enviar(
                "PRIV|" + emisor.getNickName() + "|" + mensaje
        );

        emisor.enviar(
                "SYS|Mensaje privado enviado a "
                + receptor.getNickName() + "."
        );

        System.out.println(
                Colores.VIOLETA
                + "[PRIVADO] " + emisor.getNickName()
                + " -> " + receptor.getNickName()
                + Colores.RESET
        );
    }

    public void enviarListaUsuarios(Cliente solicitante) {
        List<String> usuarios = new ArrayList<>();

        for (Cliente cliente : clientes.values()) {
            usuarios.add(cliente.getNickName());
        }

        usuarios.sort(String.CASE_INSENSITIVE_ORDER);

        String lista = usuarios.isEmpty()
                ? "No hay usuarios conectados."
                : String.join(", ", usuarios);

        solicitante.enviar("USERS|" + lista);
    }

    public void notificarTodos(String mensaje, Cliente excluir) {
        for (Cliente cliente : clientes.values()) {
            if (cliente != excluir) {
                cliente.enviar(mensaje);
            }
        }
    }

    public void desconectar(Cliente cliente) {
        String clave = normalizar(cliente.getNickName());

        if (clientes.remove(clave, cliente)) {
            System.out.println(
                   Colores.AMARILLO
                    + "[DESCONECTADO] " + cliente.getNickName()
                    + Colores.RESET
            );

            if (ejecutando) {
                notificarTodos(
                        "SYS|" + cliente.getNickName()
                        + " se ha desconectado.",
                        cliente
                );
            }
        }
    }

    private void consolaAdministracion() {
        BufferedReader teclado = new BufferedReader(
                new InputStreamReader(System.in)
        );

        try {
            String linea;

            while (ejecutando && (linea = teclado.readLine()) != null) {
                linea = linea.trim();

                if (linea.equalsIgnoreCase("/ayuda")) {
                    System.out.println(
                           Colores.AMARILLO
                            + "/usuarios - lista usuarios\n"
                            + "/mensaje texto - aviso general\n"
                            + "/kick nickname - expulsa usuario\n"
                            + "/ayuda - muestra comandos\n"
                            + "/salir - detiene el servidor"
                            + Colores.RESET
                    );

                } else if (linea.equalsIgnoreCase("/usuarios")) {
                    String lista = String.join(
                            ", ",
                            obtenerUsuarios()
                    );

                    System.out.println(
                           Colores.CYAN
                            + "[USUARIOS] "
                            + (lista.isEmpty() ? "Ninguno" : lista)
                            + Colores.RESET
                    );

                } else if (linea.regionMatches(
                        true, 0, "/mensaje ", 0, 9)) {

                    String texto = linea.substring(9).trim();

                    if (!texto.isEmpty()) {
                        notificarTodos(
                                "SYS|[SERVIDOR] " + texto,
                                null
                        );
                    }

                } else if (linea.regionMatches(
                        true, 0, "/kick ", 0, 6)) {

                    String nickname = linea.substring(6).trim();
                    Cliente cliente = clientes.get(normalizar(nickname));

                    if (cliente == null) {
                        System.out.println("Usuario no encontrado.");
                    } else {
                        cliente.enviar(
                                "ERROR|El administrador te desconectó."
                        );
                        cliente.cerrar();
                    }

                } else if (linea.equalsIgnoreCase("/salir")) {
                    close();
                    break;

                } else if (!linea.isEmpty()) {
                    System.out.println(
                            "Comando desconocido. Escribí /ayuda."
                    );
                }
            }

        } catch (IOException e) {
            if (ejecutando) {
                System.err.println(
                        "Error leyendo la consola del servidor."
                );
            }
        }
    }

    private List<String> obtenerUsuarios() {
        List<String> usuarios = new ArrayList<>();

        for (Cliente cliente : clientes.values()) {
            usuarios.add(cliente.getNickName());
        }

        usuarios.sort(String.CASE_INSENSITIVE_ORDER);
        return usuarios;
    }

    @Override
    public void close() {
        ejecutando = false;

        try {
            if (!server.isClosed()) {
                server.close();
            }
        } catch (IOException e) {
            System.err.println("Error cerrando el servidor.");
        }

        for (Cliente cliente : clientes.values()) {
            cliente.enviar("SYS|El servidor se está cerrando.");
            cliente.cerrar();
        }

        clientes.clear();

        System.out.println(
               Colores.ROJO
                + "Servidor detenido."
                + Colores.RESET
        );
    }
}
