package TPC_Socker_Thread;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicBoolean;

//Thrad  - Runnable
public class Cliente implements Runnable {
	private String nickName;
	private Socket sock;
	private DataInputStream disCli;
	private DataOutputStream dosCli;
	private AtomicBoolean conectado = new AtomicBoolean(true);
	private Servidor server;
	private Thread hilo;
	
	public Cliente(String nick, Socket soc, DataInputStream dis, DataOutputStream dos, Servidor ser) {
		this.nickName = nick;
		this.sock = soc;
		this.disCli = dis;
		this.dosCli = dos;
		this.server = ser;
	}

	@Override
	public void run() {

		try {
			while (conectado.get()) {

				String msg = CifradoAES.descifrar(disCli.readUTF()).trim();
				if (msg.isEmpty()) {
					continue;
				} else if (msg.startsWith("/")) {
					if (!procesarComando(msg))
						break;
				} else {
					procesarMensaje(msg);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			cerrar();
		}

	}

    public void iniciar() {
        hilo = new Thread(this, "CLIENTE-" + nickName);
        hilo.start();
    }	
	private void procesarMensaje(String msg) {
		// MP: mensahe#usuario
		int separador = msg.lastIndexOf('#');

		if (separador == -1) {
			server.difundir(this, msg);
			return;
		}

		String texto = msg.substring(0, separador).trim();
		String usuario = msg.substring(separador + 1).trim();

		if (texto.isEmpty() || usuario.isEmpty()) {
			enviar("ERROR | Formato privado incorrecto. Usa /msg nickname mensaje  o Mensaje#Nickname");
			return;
		}

		server.enviarPrivado(this, texto, usuario);
	}

	public synchronized boolean enviar(String msg) {
		if( !conectado.get() )
			return false;
		
		try {
			dosCli.writeUTF( CifradoAES.cifrar(msg) );
			dosCli.flush();
			return true;
		}catch(IOException ex) {
			cerrar();
			return false;
		}
	}

	private boolean procesarComando(String msg) {
		String comando = msg.trim();
        String minusculas = comando.toLowerCase(Locale.ROOT);

        if (minusculas.equals("/logout")
                || minusculas.equals("/salir")
                || minusculas.equals("/desconectar")) {

            enviar("SYS|Desconectando. ¡Hasta luego!");
            return false;
        }

        if (minusculas.equals("/usuarios")
                || minusculas.equals("/who")) {

            server.enviarListaUsuarios(this);
            return true;
        }

        if (minusculas.equals("/ayuda")
                || minusculas.equals("/help")) {

            enviar(
                    "SYS|Comandos: /ayuda, /usuarios, "
                    + "/msg nickname mensaje, /logout."
            );
            return true;
        }

        if (minusculas.startsWith("/msg ")) {
            String[] partes = comando.split("\\s+", 3);

            if (partes.length < 3) {
                enviar("ERROR|Usá /msg nickname mensaje.");
                return true;
            }

            server.enviarPrivado(this, partes[1], partes[2]);
            return true;
        }

        enviar("ERROR|Comando desconocido: " + comando);
        return true;
	}

	public void cerrar() {
		if (!conectado.compareAndSet(true, false)) {
			return;
		}

		try {
			sock.close();
		} catch (IOException e) {
			System.err.println("Error cerrando el socket de " + nickName);
		}

		server.desconectar(this);
	}

	public String getNickName() {
		return nickName;
	}
	public Socket getSock() {
		return sock;
	}
	public DataInputStream getDisCli() {
		return disCli;
	}
	public DataOutputStream getDosCli() {
		return dosCli;
	}
	public boolean isConectado() {
		return conectado.get();
	}
	public Servidor getServer() {
		return server;
	}
}
