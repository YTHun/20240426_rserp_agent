package apps.framework.utils;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.Mac;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.logging.log4j.LogManager;

public class CmSecretUtil {

	private static final Log logger = LogFactory.getLog(CmSecretUtil.class);

	/**
	 * hex to byte[] : 16진수 문자열을 바이트 배열로 변환한다.
	 *
	 * @param hex
	 * @return
	 */
	public static byte[] hexToByteArray(String hex) {
		if (hex == null || hex.length() == 0) {
			return null;
		}

		byte[] ba = new byte[hex.length() / 2];

		for (int i = 0; i < ba.length; i++) {
			ba[i] = (byte) Integer.parseInt(hex.substring(2 * i, 2 * i + 2), 16);
		}
		return ba;
	}

	/**
	 * byte[] to hex : unsigned byte(바이트) 배열을 16진수 문자열로 바꾼다.
	 *
	 * @param ba
	 * @return
	 */
	public static String byteArrayToHex(byte[] ba) {
		if (ba == null || ba.length == 0) {
			return null;
		}

		StringBuffer sb = new StringBuffer(ba.length * 2);
		String hexNumber;
		for (int x = 0; x < ba.length; x++) {
			hexNumber = "0" + Integer.toHexString(0xff & ba[x]);

			sb.append(hexNumber.substring(hexNumber.length() - 2));
		}
		return sb.toString();
	}

	/**
	 * AES 방식의 암호화
	 *
	 * @param message
	 * @return
	 * @throws NoSuchPaddingException
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeyException
	 * @throws UnsupportedEncodingException
	 * @throws BadPaddingException
	 * @throws IllegalBlockSizeException
	 * @throws Exception
	 */
	public static String encodeAES(String message, String key) throws RuntimeException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {

		if (message == null || message.equals("")) {
			return "";
		}

		SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes(), "AES");

		Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
		cipher.init(Cipher.ENCRYPT_MODE, skeySpec);

		byte[] encrypted = cipher.doFinal(message.getBytes("UTF-8"));
		return byteArrayToHex(encrypted);
	}

	/**
	 * AES 방식의 복호화
	 *
	 * @param encrypted
	 * @return
	 * @throws NoSuchPaddingException
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeyException
	 * @throws BadPaddingException
	 * @throws IllegalBlockSizeException
	 * @throws UnsupportedEncodingException
	 * @throws Exception
	 */
	public static String decodeAES(String encrypted, String key) throws RuntimeException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {
		if (encrypted == null || encrypted.equals("")) {
			return "";
		}
		SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes(), "AES");

		Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
		cipher.init(Cipher.DECRYPT_MODE, skeySpec);

		byte[] original = cipher.doFinal(hexToByteArray(encrypted));
		String originalString = new String(original, "UTF-8");
		return originalString;
	}

	/**
	 * @param str
	 * @return
	 */
	public static String encodeSha512(String str) {
		MessageDigest md;
		String rtn = "";
		try {
			md = MessageDigest.getInstance("SHA-512");

			md.update(str.getBytes());
			byte[] mb = md.digest();
			for (int i = 0; i < mb.length; i++) {
				byte temp = mb[i];
				String s = Integer.toHexString(new Byte(temp));
				while (s.length() < 2) {
					s = "0" + s;
				}
				s = s.substring(s.length() - 2);
				rtn += s;
			}
		} catch (RuntimeException | NoSuchAlgorithmException e) {
			logger.error(e); //e.printStackTrace();
		}
		return rtn;
	}

	/**
	 * @param str
	 * @return
	 */
	public static String encodeHmacSha512(String str, String key, String nonce) {
		String result = "";
		String plainText = str + nonce;
		try {
			Mac sha512_HMAC = Mac.getInstance("HmacSHA512");
			SecretKeySpec secret_key = new SecretKeySpec(key.getBytes(), "HmacSHA512");

			// Mac 객체 초기화
			sha512_HMAC.init(secret_key);

			// 해시값 생성 및 Base64 인코딩
			String encoded = Base64.encodeBase64String(sha512_HMAC.doFinal(plainText.getBytes()));
			// Url 인코딩
			result = encoded.replace("=", "").replace("+", "-").replace("/", "_");

		} catch (RuntimeException | NoSuchAlgorithmException | InvalidKeyException e) {
			logger.error(e);
		}

		return result;
	}


	public static String encodeSha256(String str) {
		String rtn = "";
		try {
			byte[] plainText = null;
			byte[] hashValue = null;
			plainText = str.getBytes();

			MessageDigest md = MessageDigest.getInstance("SHA-256");
			hashValue = md.digest(plainText);
			return new String(Base64.encodeBase64(hashValue));

		} catch (RuntimeException | NoSuchAlgorithmException e) {
			logger.error(e); //e.printStackTrace();
		}
		return rtn;
	}


	/**
	 * AES 방식의 암호화
	 *
	 * @param message
	 * @return
	 * @throws Exception
	 */
	public static String encodeAES_Base64(String message, String key) throws Exception {

		if (message == null || message.equals("") || message.equals("null")) {
			return "";
		}
		try {
			SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes(), "AES");

			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
			cipher.init(Cipher.ENCRYPT_MODE, skeySpec);

			byte[] encrypted = cipher.doFinal(message.getBytes("UTF-8"));
			return new String(Base64.encodeBase64(encrypted));

		} catch (RuntimeException ex) {
			logger.error(ex);
			return message;
		}
	}

	/**
	 * AES 방식의 복호화
	 *
	 * @param encrypted
	 * @return
	 * @throws Exception
	 */
	public static String decodeAES_Base64(String encrypted, String key) throws Exception {
		if (encrypted == null || encrypted.equals("") || encrypted.equals("null")) {
			return "";
		}

		try {
			SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes(), "AES");

			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
			cipher.init(Cipher.DECRYPT_MODE, skeySpec);

			byte[] original = cipher.doFinal(Base64.decodeBase64(encrypted));
			String originalString = new String(original, "UTF-8");
			return originalString;
		} catch (RuntimeException ex) {
			logger.error(ex);
			return encrypted;

		}
	}
}
