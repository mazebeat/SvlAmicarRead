package cl.intelidata.utils;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.KeySpec;

// import sun.misc.BASE64Encoder;

public class Cifrador {

	private static byte[] SALT_BYTES = {(byte)0xA9, (byte)0x9B, (byte)0xC8, (byte)0x32, (byte)0x56, (byte)0x35, (byte)0xE3, (byte)0x03};

	private static int ITERATION_COUNT = 19;

	public static String encriptar(String passPhrase, String str) {

		Cipher ecipher = null;
		try {
			// Crear la key
			KeySpec keySpec = new PBEKeySpec(passPhrase.toCharArray(), SALT_BYTES, ITERATION_COUNT);
			SecretKey key = SecretKeyFactory.getInstance("PBEWithMD5AndDES").generateSecret(keySpec);
			ecipher = Cipher.getInstance(key.getAlgorithm());

			// Preparar los parametros para los ciphers
			AlgorithmParameterSpec paramSpec = new PBEParameterSpec(SALT_BYTES, ITERATION_COUNT);

			// Crear los ciphers
			ecipher.init(Cipher.ENCRYPT_MODE, key, paramSpec);

			// Encodear la cadena a bytes usando utf-8
			byte[] utf8 = str.getBytes("UTF8");

			// Encriptar
			byte[] enc = ecipher.doFinal(utf8);

			// Encodear bytes a base64 para obtener cadena

			// return new BASE64Encoder().encode(enc);
			return new String(Base64.encodeBase64(enc));

		} catch (Exception e) {
			return null;
		}
	}

	public static String desencriptar(String passPhrase, String str) {
		Cipher dcipher = null;

		try {
			// Crear la key
			KeySpec keySpec = new PBEKeySpec(passPhrase.toCharArray(), SALT_BYTES, ITERATION_COUNT);
			SecretKey key = SecretKeyFactory.getInstance("PBEWithMD5AndDES").generateSecret(keySpec);
			dcipher = Cipher.getInstance(key.getAlgorithm());

			// Preparar los parametros para los ciphers
			AlgorithmParameterSpec paramSpec = new PBEParameterSpec(SALT_BYTES, ITERATION_COUNT);

			// Crear los ciphers
			dcipher.init(Cipher.DECRYPT_MODE, key, paramSpec);

			// Decodear base64 y obtener bytes
			// byte[] dec = new sun.misc.BASE64Decoder().decodeBuffer(str);
			byte[] dec = Base64.decodeBase64(str);

			// Desencriptar
			byte[] utf8 = dcipher.doFinal(dec);

			// Decodear usando utf-8
			return new String(utf8, "UTF8");

		} catch (Exception e) {
			return null;
		}
	}

	public static String cif(String text) throws UnsupportedEncodingException {
		String enc = Cifrador.encriptar("ct2013.*", text);
		// Se codifica para url
		enc = URLEncoder.encode(enc, "UTF-8");
		return enc;
	}

	public static void main(String args[]) throws IOException {

		// Se encripa cadena con datos delimitados por coma
		String id = "14220";
		System.out.println("id " + id + ": " + cif(id));

		Cifrador cif = new Cifrador();
		String res = cif.desencriptar(cl.intelidata.utils.Texto.LLAVE, "M+XZaISRuUo=");
		System.out.println(res);

	}

}