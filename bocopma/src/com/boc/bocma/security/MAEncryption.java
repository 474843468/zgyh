package com.boc.bocma.security;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import android.util.Base64;

public class MAEncryption {
	/**
	 * TODO: parameters validity uncheck
	 *
	 */
	public static final class DES {
		private static byte[] bytes = new byte[8];
		public static final String encrypt(String unencrypted, String password) throws Exception {
			IvParameterSpec ivp = new IvParameterSpec(bytes);
			SecretKeySpec key = new SecretKeySpec(password.getBytes(), "DES");
			Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, key, ivp);
			byte[] encrypted = cipher.doFinal(unencrypted.getBytes());
			return Base64.encodeToString(encrypted, Base64.DEFAULT);
		}
		
		public static final String decrypt(String encrypted, String password) throws Exception {
			byte[] bytesForDecrypt = Base64.decode(encrypted, Base64.DEFAULT);
			IvParameterSpec ivp = new IvParameterSpec(bytes);
			SecretKeySpec key = new SecretKeySpec(password.getBytes(), "DES");
			Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
			cipher.init(Cipher.DECRYPT_MODE, key, ivp);
			Base64.decode(encrypted, Base64.DEFAULT);
			byte[] data = cipher.doFinal(bytesForDecrypt);
			return new String(data);
		}
	}
	
	/**
	 * TODO: parameters validity uncheck
	 */
	public static final class AES {
		public static final String encrypt(String unencrypted, String seed) throws Exception {
			byte[] rawKey = generateEncodedKey(seed);
			
			SecretKeySpec key = new SecretKeySpec(rawKey, "AES");
			Cipher cipher = Cipher.getInstance("AES");
			cipher.init(Cipher.ENCRYPT_MODE, key);
			byte[] encrypted = cipher.doFinal(unencrypted.getBytes());
			return bytesToHex(encrypted);
		}

		private static byte[] generateEncodedKey(String seed)
				throws NoSuchAlgorithmException {
			KeyGenerator generator = KeyGenerator.getInstance("AES");
			SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
			secureRandom.setSeed(seed.getBytes());
			generator.init(128, secureRandom);
			
			SecretKey secretKey = generator.generateKey();
			byte[] rawKey = secretKey.getEncoded();
			return rawKey;
		}
		
		private static final String HEX = "0123456789ABCDEF";
		private static String bytesToHex(byte[] bytes) {
			StringBuilder to = new StringBuilder(2 * bytes.length);
			for (int i=0; i < bytes.length; i++) {
				byte b = bytes[i];
				to.append(HEX.charAt((b>>4) & 0x0f)).append(HEX.charAt(b & 0x0f));
			}
			return to.toString();
		}
		
		public static final String decrypt(String encrypted, String seed) throws Exception {
			byte[] rawKey = generateEncodedKey(seed);
			
			SecretKeySpec key = new SecretKeySpec(rawKey, "AES");
			Cipher cipher = Cipher.getInstance("AES");
			cipher.init(Cipher.DECRYPT_MODE, key);
			byte[] decrypted = cipher.doFinal(hexToBytes(encrypted));
			return new String(decrypted);
		}
		
		private static byte[] hexToBytes(String hexString) {
			int len = hexString.length()/2;
			byte[] to = new byte[len];
			for (int i=0; i < len; i++) {
				to[i] = Integer.valueOf(hexString.substring(2*i, 2*i + 2), 16).byteValue();
			}
			return to;
		}
		
	}
	
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
}
