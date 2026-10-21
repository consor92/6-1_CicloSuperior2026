package TPC_Socker_Thread;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public final class CifradoAES {

    // Clave educativa AES-128. Debe coincidir en ambos extremos.
    private static final byte[] CLAVE =
            "FooBar1234567890".getBytes(StandardCharsets.UTF_8);

    private static final SecureRandom RANDOM = new SecureRandom();
    private static final int TAMANO_IV = 12;
    private static final int TAMANO_TAG = 128;

    private CifradoAES() {
    }

    public static String cifrar(String texto) throws IOException {
        try {
            byte[] iv = new byte[TAMANO_IV];
            RANDOM.nextBytes(iv);

            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            cipher.init(
                    Cipher.ENCRYPT_MODE,
                    new SecretKeySpec(CLAVE, "AES"),
                    new GCMParameterSpec(TAMANO_TAG, iv)
            );

            byte[] cifrado = cipher.doFinal(
                    texto.getBytes(StandardCharsets.UTF_8)
            );

            byte[] resultado = ByteBuffer
                    .allocate(iv.length + cifrado.length)
                    .put(iv)
                    .put(cifrado)
                    .array();

            return Base64.getEncoder().encodeToString(resultado);

        } catch (GeneralSecurityException e) {
            throw new IOException("Error al cifrar.", e);
        }
    }

    public static String descifrar(String texto) throws IOException {
        try {
            byte[] datos = Base64.getDecoder().decode(texto);

            if (datos.length < TAMANO_IV + 16) {
                throw new IOException("Mensaje cifrado incompleto.");
            }

            ByteBuffer buffer = ByteBuffer.wrap(datos);
            byte[] iv = new byte[TAMANO_IV];
            buffer.get(iv);

            byte[] cifrado = new byte[buffer.remaining()];
            buffer.get(cifrado);

            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            cipher.init(
                    Cipher.DECRYPT_MODE,
                    new SecretKeySpec(CLAVE, "AES"),
                    new GCMParameterSpec(TAMANO_TAG, iv)
            );

            byte[] original = cipher.doFinal(cifrado);

            return new String(original, StandardCharsets.UTF_8);

        } catch (GeneralSecurityException | IllegalArgumentException e) {
            throw new IOException(
                    "No se pudo descifrar el mensaje.", e
            );
        }
    }
}
