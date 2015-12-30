package cn.goodjobs.common.util.http;

import java.io.UnsupportedEncodingException;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.EncodedKeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

/**
 * 加密解密工具包
 * 
 * @author Winter Lau
 * @date 2011-12-26
 */
public class CryptUtils {
	public final static String PUBLIC_KEY_BEGIN = "-----BEGIN PUBLIC KEY-----";
	public final static String PUBLIC_KEY_END = "-----END PUBLIC KEY-----";
	public final static String PRIVATE_KEY_BEGIN = "-----BEGIN PRIVATE KEY-----";
	public final static String PRIVATE_KEY_END = "-----END PRIVATE KEY-----";
	public final static String RSA = "RSA/ECB/PKCS1Padding";
	private static PublicKey RSA_PUBKEY;
	private static PrivateKey SGN_PRIKEY;

	public static void loadRSAPubKey(String key) {
		try {
			key = key.replace(PUBLIC_KEY_BEGIN, "").replace(PUBLIC_KEY_END, "").trim().replaceAll("\\s+", "");
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(
					Base64.decode(key, Base64.DEFAULT));
			RSA_PUBKEY = keyFactory.generatePublic(publicKeySpec);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void loadSGNPriKey(String key) {
		try {
			key = key.replace(PRIVATE_KEY_BEGIN, "").replace(PRIVATE_KEY_END, "").trim().replaceAll("\\s+", "");
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(
					Base64.decode(key, Base64.DEFAULT));
			SGN_PRIKEY = keyFactory.generatePrivate(privateKeySpec);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static byte[] RSASignature(String text) {
		try {
			Signature sig = Signature.getInstance("SHA1WithRSA");
			if(SGN_PRIKEY==null){
				CryptUtils.loadSGNPriKey(MetaDataUtil.getInstanse().getMetaJson().getString("sgn_prikey"));
			}
			sig.initSign(SGN_PRIKEY);
			sig.update(text.getBytes("utf-8"));
			return sig.sign();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static byte[] RSAEncode(String text) {
		try {
			if (RSA_PUBKEY == null) {
				loadRSAPubKey(MetaDataUtil.getInstanse().getMetaJson().getString("rsa_pubkey"));
			}
			Cipher cipher = Cipher.getInstance(RSA);
			cipher.init(Cipher.ENCRYPT_MODE, RSA_PUBKEY);
			return cipher.doFinal(text.getBytes("utf-8"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public static String Base64Encode(byte[] data) {
		data = Base64.encode(data, Base64.NO_WRAP);
		String str = null;
		str = new String(data);
		return str;
	}
	
	public static String Base64Decode(byte[] data) {
		data = Base64.decode(data, Base64.DEFAULT);
		String str = null;
		try {

			str = new String(data, "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return str;
	}

	/**
	 * MD5加密
	 */
	public static String MD5Encode32(String str) {
		MessageDigest messageDigest = null;

		try {
			messageDigest = MessageDigest.getInstance("MD5");

			messageDigest.reset();

			messageDigest.update(str.getBytes("utf-8"));
		} catch (NoSuchAlgorithmException e) {
			System.out.println("NoSuchAlgorithmException caught!");
			System.exit(-1);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		byte[] byteArray = messageDigest.digest();

		StringBuffer md5StrBuff = new StringBuffer();

		for (int i = 0; i < byteArray.length; i++) {
			if (Integer.toHexString(0xFF & byteArray[i]).length() == 1)
				md5StrBuff.append("0").append(
						Integer.toHexString(0xFF & byteArray[i]));
			else
				md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
		}
		return md5StrBuff.toString().toUpperCase();
	}

	/**
	 * MD5加密
	 */
	public static String MD5Encode16(String str) {
		return MD5Encode32(str).substring(8, 24);
	}

	/**
	 * 二行制转字符串
	 * 
	 * @param b
	 * @return
	 */
	private static String byte2hex(byte[] b) {
		StringBuilder hs = new StringBuilder();
		String stmp;
		for (int n = 0; b != null && n < b.length; n++) {
			stmp = Integer.toHexString(b[n] & 0XFF);
			if (stmp.length() == 1)
				hs.append('0');
			hs.append(stmp);
		}
		return hs.toString().toUpperCase();
	}

	private static byte[] hex2byte(byte[] b) {
		if ((b.length % 2) != 0)
			throw new IllegalArgumentException();
		byte[] b2 = new byte[b.length / 2];
		for (int n = 0; n < b.length; n += 2) {
			String item = new String(b, n, 2);
			b2[n / 2] = (byte) Integer.parseInt(item, 16);
		}
		return b2;
	}

}
