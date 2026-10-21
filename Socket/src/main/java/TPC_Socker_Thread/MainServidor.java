package TPC_Socker_Thread;


import java.io.IOException;

public class MainServidor {

    public static final int PUERTO = 7777;

    public static void main(String[] args) {
        System.out.println("INICIANDO SERVIDOR");

        try (Servidor servidor = new Servidor(PUERTO)) {

            Runtime.getRuntime().addShutdownHook(
                new Thread(() -> {
                    servidor.close();
                }, "CIERRE-SERVIDOR")
            );

            servidor.iniciar();

        } catch (IOException e) {
            System.err.println(
                "No se pudo iniciar el servidor: " + e.getMessage()
            );
        }
    }
}