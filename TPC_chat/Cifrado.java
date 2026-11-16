package cliente;

import java.security.SecureRandom;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class Cifrado {

    public static String RESET = "\u001B[0m";
    public static String VERDE = "\u001B[32m";
    public static String CIAN = "\u001B[36m";
    public static String MORADO = "\u001B[35m"; 
    public static String ROJO = "\u001B[31m";
    public static String AMARILLO = "\u001B[33m";
    
    private static final SecureRandom sr = new SecureRandom();

    public static String encriptar(String clave, String texto) {
        try {
            byte[] iv = new byte[16]; 
            sr.nextBytes(iv); 
            
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            SecretKeySpec sks = new SecretKeySpec(clave.getBytes("UTF-8"), "AES");
            cipher.init(Cipher.ENCRYPT_MODE, sks, new IvParameterSpec(iv));

            byte[] encriptado = cipher.doFinal(texto.getBytes("UTF-8"));
            
            String ivB64 = Base64.getEncoder().encodeToString(iv);
            String msgB64 = Base64.getEncoder().encodeToString(encriptado);
            
            return ivB64 + ":" + msgB64; 
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String decriptar(String clave, String paquete) {
        try {
            if (paquete == null || !paquete.contains(":")) {
                return null; 
            }
            
            String[] partes = paquete.split(":");
            byte[] iv = Base64.getDecoder().decode(partes[0]);
            String encriptado = partes[1];

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            SecretKeySpec sks = new SecretKeySpec(clave.getBytes("UTF-8"), "AES");
            cipher.init(Cipher.DECRYPT_MODE, sks, new IvParameterSpec(iv));

            byte[] dec = cipher.doFinal(Base64.getDecoder().decode(encriptado)); 
            return new String(dec, "UTF-8"); 
        } catch (Exception ex) {
            // Silenciado para no ensuciar la consola si llega texto plano
        }
        return null;
    }
}
