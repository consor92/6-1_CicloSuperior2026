package GenericFiles;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

public class EncriptUtil {
	/**
	 * Mapa que almacena las claves criptográficas utilizadas en el sistema.
	 * <p>
	 * Este Map se utiliza como contenedor en memoria para las claves generadas
	 * mediante el método {@code generateKeys()}, las cuales son necesarias para
	 * realizar operaciones de cifrado y descifrado.
	 * </p>
	 * <p>
	 * Claves almacenadas:
	 * </p>
	 * <ul>
	 *   <li><b>RSApr</b>: Clave privada RSA utilizada para descifrado o firma digital.</li>
	 *   <li><b>RSAp</b>: Clave pública RSA utilizada para cifrado o verificación de firma.</li>
	 *   <li><b>AES</b>: Clave simétrica AES utilizada para cifrado y descifrado de datos.</li>
	 *   <li><b>prAES</b>: Clave AES cifrada o protegida mediante RSA para su intercambio seguro.</li>
	 * </ul>
	 *
	 * <p>
	 * Este Map es inicializado y cargado por el método {@code generateKeys()}.
	 * @see #generateKeys()
	 * </p>
	 */
	public static Map<String, Object> keys;
    static {
        generateKeys();
    }

	/**
	 * Cifra un texto utilizando la clave pública RSA proporcionada.
	 *
	 * Este método emplea el cifrado RSA con el esquema OAEP y SHA-256 para cifrar
	 * el texto dado. El texto se codifica en UTF-8 antes de ser cifrado, y el
	 * resultado cifrado se devuelve como una cadena codificada en Base64.
	 *
	 * @param texto        El texto plano que se desea cifrar.
	 * @param clavePublica La clave pública RSA que se utilizará para el cifrado.
	 * @return Una cadena codificada en Base64 que representa el texto cifrado, o
	 *         {@code null} si ocurre algún error durante el proceso.
	 */
	private static SecretKey generarClaveAES() {
		try {
			KeyGenerator gen = KeyGenerator.getInstance("AES");
			gen.init(256);
			return gen.generateKey();
		} catch (Exception ex) {
			Logger.getLogger(EncriptUtil.class.getName()).log(Level.WARNING, null, ex);
		}
		return null;
	}

    /**
     * Genera un par de claves RSA (pública y privada) con tamaño de 2048 bits.
     *
     * Este método utiliza el generador de claves RSA de Java para crear un
     * par de claves que pueden ser usadas para cifrado y firma digital.
     *
     * @return Un objeto {@link KeyPair} que contiene la clave pública y privada generadas,
     *         o {@code null} si ocurre un error durante la generación.
     */ 	
	private static KeyPair generarParRSA() {
		try {
			KeyPairGenerator gen = KeyPairGenerator.getInstance("RSA");
			gen.initialize(2048);

			return gen.generateKeyPair();
		} catch (NoSuchAlgorithmException ex) {
			ex.printStackTrace();
		}
		return null;
	}

    /**
     * Cifra un texto utilizando la clave pública RSA proporcionada.
     *
     * Este método emplea el cifrado RSA con el esquema OAEP y SHA-256 para
     * cifrar el texto dado. El texto se codifica en UTF-8 antes de ser cifrado,
     * y el resultado cifrado se devuelve como una cadena codificada en Base64.
     *
     * @param texto El texto plano que se desea cifrar.
     * @param clavePublica La clave pública RSA que se utilizará para el cifrado.
     * @return Una cadena codificada en Base64 que representa el texto cifrado,
     *         o {@code null} si ocurre algún error durante el proceso.
     */ 	
	public static String cifrarConClavePublica(String texto, PublicKey keyPublic) {
		String tranform = "RSA/ECB/OAEPWithSHA-256AndMGF1Padding";
		try {
			Cipher encriptar = Cipher.getInstance(tranform);
			encriptar.init(Cipher.ENCRYPT_MODE, keyPublic);

			byte[] byteCifrados = encriptar.doFinal(texto.getBytes("UTF-8"));
			return Base64.getEncoder().encodeToString(byteCifrados);

		} catch (NoSuchAlgorithmException | NoSuchPaddingException ex) {
			Logger.getLogger(EncriptUtil.class.getName()).log(Level.WARNING, null, ex);
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Descifra un texto cifrado en Base64 utilizando la clave privada RSA proporcionada.
	 *
	 * Este método decodifica el texto cifrado desde Base64 y utiliza el esquema
	 * RSA con OAEP y SHA-256 para descifrar el contenido. El resultado se devuelve
	 * como una cadena en formato UTF-8.
	 *
	 * @param textoCifrado El texto cifrado codificado en Base64 que se desea descifrar.
	 * @param clavePrivada La clave privada RSA que se utilizará para el descifrado.
	 * @return El texto descifrado en formato de cadena UTF-8,
	 *         o {@code null} si ocurre algún error durante el proceso.
	 */	
	public static String desCifrarConClavePublica(String texto, PrivateKey keyPrivate) {
		try {
			String tranform = "RSA/ECB/OAEPWithSHA-256AndMGF1Padding";
			Cipher desEncriptar = Cipher.getInstance(tranform);
			desEncriptar.init(Cipher.DECRYPT_MODE, keyPrivate);

			byte[] byteDeCifrados = desEncriptar.doFinal(Base64.getDecoder().decode(texto));
			return new String(byteDeCifrados, "UTF-8");

		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Cifra un texto utilizando AES en modo CBC con padding PKCS5.
	 *
	 * Este método genera un vector de inicialización (IV) aleatorio de 16 bytes,
	 * cifra el texto con la clave AES proporcionada y concatena el IV con el texto cifrado.
	 * El resultado se codifica en Base64 para facilitar su almacenamiento o transmisión.
	 *
	 * @param texto El texto plano que se desea cifrar.
	 * @param clave La clave secreta AES utilizada para el cifrado.
	 * @return Una cadena codificada en Base64 que contiene el IV concatenado con el texto cifrado,
	 *         o {@code null} si ocurre algún error durante el proceso.
	 */	
	public static String cifrarAES(String text, SecretKey key) {
		try {
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			byte[] IV = new byte[16];
			SecureRandom random = new SecureRandom();
			random.nextBytes(IV);
			IvParameterSpec idParam = new IvParameterSpec(IV);

			cipher.init(Cipher.ENCRYPT_MODE, key, idParam);
			byte[] Byte = cipher.doFinal(text.getBytes("UTF-8"));

			byte[] cifrado = new byte[IV.length + Byte.length];
			System.arraycopy(IV, 0, cifrado, 0, IV.length);
			System.arraycopy(Byte, 0, cifrado, 0, Byte.length);

			return Base64.getEncoder().encodeToString(cifrado);
		} catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (InvalidAlgorithmParameterException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Descifra un texto cifrado en Base64 utilizando AES en modo CBC con padding PKCS5.
	 *
	 * Este método recibe una cadena codificada en Base64 que contiene concatenados
	 * el vector de inicialización (IV) y el texto cifrado. Extrae el IV, realiza el
	 * descifrado con la clave AES proporcionada y devuelve el texto plano en UTF-8.
	 *
	 * @param textoCifradoBase64 Cadena Base64 que contiene el IV concatenado con el texto cifrado.
	 * @param clave La clave secreta AES que se utilizará para el descifrado.
	 * @return El texto descifrado en formato UTF-8,
	 *         o {@code null} si ocurre algún error durante el proceso.
	 */	
	public static String desCifrarAES(String text, SecretKey key) {
		try {
			byte[] ivCifrado = Base64.getDecoder().decode(text);
			byte[] IV = new byte[16];
			byte[] textoCifrado = new byte[ivCifrado.length - 16];

			System.arraycopy(ivCifrado, 0, IV, 0, 16);
			System.arraycopy(ivCifrado, 16, textoCifrado, 0, textoCifrado.length);

			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(IV));

			byte[] decifrados = cipher.doFinal(textoCifrado);

			return new String(decifrados, "UTF-8");
		} catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (InvalidAlgorithmParameterException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Genera el hash SHA-256 de un texto dado.
	 * <p>
	 * Este método utiliza el algoritmo SHA-256 para calcular un resumen criptográfico
	 * (hash) del texto proporcionado. El resultado se devuelve en formato hexadecimal.
	 * </p>
	 *
	 * @param text Texto de entrada que se desea hashear.
	 * @return Cadena en formato hexadecimal que representa el hash SHA-256 del texto,
	 *         o {@code null} si ocurre un error al obtener el algoritmo de hash.
	 *
	 * @throws RuntimeException en caso de error inesperado del entorno criptográfico
	 *                          (actualmente capturado internamente).
	 */
	public static String hashSha(String text) {
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");

			byte[] hash = md.digest(text.getBytes(StandardCharsets.UTF_8));

			StringBuilder sb = new StringBuilder();
			for (byte b : hash) {
				sb.append(String.format("%02x", b));
			}

			return sb.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Genera un par de claves RSA y una clave simétrica AES, y las almacena
	 * en el mapa global {@code keys}.
	 * <p>
	 * Este método inicializa el sistema criptográfico generando:
	 * </p>
	 * <ul>
	 *   <li>Un par de claves RSA (pública y privada).</li>
	 *   <li>Una clave simétrica AES para cifrado de datos.</li>
	 * </ul>
	 *
	 * <p>
	 * Las claves generadas se almacenan en el mapa {@code keys} con las siguientes entradas:
	 * </p>
	 * <ul>
	 *   <li><b>RSAp</b>: clave pública RSA.</li>
	 *   <li><b>RSApr</b>: clave privada RSA.</li>
	 *   <li><b>AES</b>: clave simétrica AES en formato original.</li>
	 *   <li><b>prAES</b>: clave AES cifrada con la clave pública RSA.</li>
	 * </ul>
	 *
	 * <p>
	 * La clave AES se codifica en Base64 antes de ser cifrada con RSA para su almacenamiento seguro.
	 * </p>
	 *
	 * @see keys
	 */
	private static void generateKeys() {
		KeyPair k = generarParRSA();
		SecretKey sk = generarClaveAES();

		keys.put("RSAp", k.getPublic());
		keys.put("RSApr", k.getPrivate());
		keys.put("AES", sk);

		keys.put("prAES", cifrarConClavePublica(Base64.getEncoder().encodeToString(sk.getEncoded()), k.getPublic()));
	}

}
