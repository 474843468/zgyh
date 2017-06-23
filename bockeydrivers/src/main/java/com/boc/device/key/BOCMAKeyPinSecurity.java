package com.boc.device.key;

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;


public class BOCMAKeyPinSecurity {

	private BOCMAKeyPinSecurity() {

	}

	/**
	 * 音频Key的PIN码加密
	 * 
	 * @param pin
	 *            PIN码明文
	 * @param randomID
	 *            随机数（用于生成解密PIN码的密钥的种子）
	 * @param sessionID
	 *            会话ID（用于生成解密PIN码的密钥的种子）
	 * 
	 * @return 加密后的PIN码数据
	 */
	public static byte[] encryptWithKeyPin(String pin, String randomID,
			String sessionID) {
		String seed = createSeed(randomID, sessionID);
		String result = "";
		if (seed != null) {
			try {
				result = AES.encrypt(pin, seed);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return result.getBytes();
	}

	/**
	 * 音频Key的PIN码解密
	 * 
	 * @param encrytoData
	 *            加密后的PIN码数据
	 * @param randomID
	 *            随机数（用于生成解密PIN码的密钥的种子）
	 * @param sessionID
	 *            会话ID（用于生成解密PIN码的密钥的种子）
	 * 
	 * @return PIN码明文
	 */
	public static String decryptKeyPinFromData(byte[] encrytoData,
			String randomID, String sessionID) {

		String seed = createSeed(randomID, sessionID);
		String result = "";
		try {
			result = AES.decrypt(new String(encrytoData), seed);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 生成秘钥
	 * 
	 * @param randomID
	 * @param sessionID
	 * @return
	 */
	private static final String createSeed(String randomID, String sessionID) {
		if (randomID == null || sessionID == null) {
			return null;
		}
		String key = randomID + sessionID;
		key = MD5.toMD5(key);

		// 长度大于256
		if (key.length() > 256) {
			key = key.substring(0, 256);
		}

		return key;
	}

	/**
	 * MD5加密算法
	 * 
	 * @author lxw
	 * 
	 */
	public static final class MD5 {
		public static final String toMD5(String message) {
			String cacheKey;
			try {
				final MessageDigest mDigest = MessageDigest.getInstance("MD5");
				mDigest.update(message.getBytes());
				cacheKey = bytesToHexString(mDigest.digest());
			} catch (NoSuchAlgorithmException e) {
				cacheKey = String.valueOf(message.hashCode());
			}
			return cacheKey;
		}

		private static String bytesToHexString(byte[] bytes) {
			// http://stackoverflow.com/questions/332079
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < bytes.length; i++) {
				String hex = Integer.toHexString(0xFF & bytes[i]);
				if (hex.length() == 1) {
					sb.append('0');
				}
				sb.append(hex);
			}
			return sb.toString();
		}
	}

	/**
	 * AES加解密算法
	 * 
	 * @author lxw
	 * 
	 */
	public static final class AES {
		public static final String encrypt(String unencrypted, String seed)
				throws Exception {
			byte[] rawKey = generateEncodedKey(seed);

			SecretKeySpec key = new SecretKeySpec(rawKey, "AES");
			Cipher cipher = Cipher.getInstance("AES");
			cipher.init(Cipher.ENCRYPT_MODE, key);
			byte[] encrypted = cipher.doFinal(unencrypted.getBytes());
			return bytesToHex(encrypted);
		}

		private static byte[] generateEncodedKey(String seed)
				throws NoSuchAlgorithmException, NoSuchProviderException {
			KeyGenerator generator = KeyGenerator.getInstance("AES");
			SecureRandom secureRandom = null;
			if (android.os.Build.VERSION.SDK_INT >= 17) {
			    secureRandom = SecureRandom.getInstance("SHA1PRNG", "Crypto");
			} else {
			    secureRandom = SecureRandom.getInstance("SHA1PRNG");
			}
			
			secureRandom.setSeed(seed.getBytes());
			generator.init(128, secureRandom);

			SecretKey secretKey = generator.generateKey();
			byte[] rawKey = secretKey.getEncoded();
			return rawKey;
		}

		private static final String HEX = "0123456789ABCDEF";

		private static String bytesToHex(byte[] bytes) {
			StringBuilder to = new StringBuilder(2 * bytes.length);
			for (int i = 0; i < bytes.length; i++) {
				byte b = bytes[i];
				to.append(HEX.charAt((b >> 4) & 0x0f)).append(
						HEX.charAt(b & 0x0f));
			}
			return to.toString();
		}

		public static final String decrypt(String encrypted, String seed)
				throws Exception {
			byte[] rawKey = generateEncodedKey(seed);

			SecretKeySpec key = new SecretKeySpec(rawKey, "AES");
			Cipher cipher = Cipher.getInstance("AES");
			cipher.init(Cipher.DECRYPT_MODE, key);
			byte[] decrypted = cipher.doFinal(hexToBytes(encrypted));
			return new String(decrypted, Charset.forName("UTF-8"));
		}

		private static byte[] hexToBytes(String hexString) {
			int len = hexString.length() / 2;
			byte[] to = new byte[len];
			for (int i = 0; i < len; i++) {
				to[i] = Integer.valueOf(hexString.substring(2 * i, 2 * i + 2),
						16).byteValue();
			}
			return to;
		}

	}

}
