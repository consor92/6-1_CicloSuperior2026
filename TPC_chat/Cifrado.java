package TPC_chat;

import java.security.SecureRandom;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class Cifrado {

    static String RESET = "\u001B[0m";
    static String VERDE = "\u001B[32m";
    static String CIAN = "\u001B[36m";
    static String MORADO = "\u001B[35m"; 
    static String ROJO = "\u001B[31m";
    static String AMARILLO = "\u001B[33m";
    
    private static final SecureRandom sr = new SecureRandom();

    public static String encriptar(String clave, byte[] iv, String texto) {
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            SecretKeySpec sks = new SecretKeySpec(clave.getBytes("UTF-8"), "AES");
            cipher.init(Cipher.ENCRYPT_MODE, sks, new IvParameterSpec(iv));

            byte[] encriptado = cipher.doFinal(texto.getBytes("UTF-8"));
            return Base64.getEncoder().encodeToString(encriptado); 
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String decriptar(String clave, byte[] iv, String encriptado) {
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            SecretKeySpec sks = new SecretKeySpec(clave.getBytes("UTF-8"), "AES");
            cipher.init(Cipher.DECRYPT_MODE, sks, new IvParameterSpec(iv));

            byte[] dec = cipher.doFinal(Base64.getDecoder().decode(encriptado)); 
            return new String(dec, "UTF-8"); 
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static byte[] generarIV() {
        byte[] iv = new byte[16]; 
        sr.nextBytes(iv); 
        return iv;
    }
}