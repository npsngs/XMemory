/**---------------------------------------------------------------------
 * Utility: EncodeUtils
 * Description: 字符串工具，提供各种字符串操作相关的方法
 * 
 * Author: DuRuixue
 * Date: 2013-8-19
------------------------------------------------------------------------ */
package com.forthe.xmemory;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

public class EncodeUtils {
	/**
	 * MD5加密
	 * @param string
	 */
	public static String md5(String string) {
		byte[] hash;
		try {
			hash = MessageDigest.getInstance("MD5").digest(string.getBytes("UTF-8"));
		} catch(NoSuchAlgorithmException e) {
			throw new RuntimeException("MD5 should be supported?", e);
		} catch(UnsupportedEncodingException e) {
			throw new RuntimeException("UTF-8 should be supported?", e);
		}
		
		StringBuilder hex = new StringBuilder(hash.length * 2);
		for(byte b : hash) {
			if((b & 0xFF) < 0x10)
				hex.append("0");
			hex.append(Integer.toHexString(b & 0xFF));
		}
		return hex.toString();
	}
	
	/**
	 * 使用Base64算法对字符串加密
	 * @param 	originalStr		初始字符串
	 * @param	encodingType	编码方式
	 */
	@SuppressLint("NewApi")
	public static String encodeWithBase64(String originalStr, String encodingType) throws Exception {
		if(originalStr == null)
			return null;
		
		return new String(Base64.encode(originalStr.getBytes(encodingType), Base64.DEFAULT));
	}

	private static String encodeWithBase64(byte[] byteArray) {
		if(byteArray == null)
			return null;
		return new String(Base64.encode(byteArray, Base64.DEFAULT));
	}
	
	
	/**
     * Description 根据键值进行解密
     */
    public static String DESDecrypt(String data, String key) throws Exception {
        if (data == null)
            return null;
        byte[] buf =  Base64.decode(data, Base64.DEFAULT);
        byte[] bt = DESDecrypt(buf, key.getBytes());
        return new String(bt);
    }
	
    
    /**
     * DES 根据键值进行解密
     */
    private static byte[] DESDecrypt(byte[] data, byte[] key) throws Exception {
        // 生成一个可信任的随机数源
        SecureRandom sr = new SecureRandom();

        // 从原始密钥数据创建DESKeySpec对象
        DESKeySpec dks = new DESKeySpec(key);

        // 创建一个密钥工厂，然后用它把DESKeySpec转换成SecretKey对象
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        SecretKey securekey = keyFactory.generateSecret(dks);

        // Cipher对象实际完成解密操作
        @SuppressLint("GetInstance") Cipher cipher = Cipher.getInstance("DES");

        // 用密钥初始化Cipher对象
        cipher.init(Cipher.DECRYPT_MODE, securekey, sr);
        return cipher.doFinal(data);
    }
	
	
    /**
     * DES 根据键值进行加密
     */
    public static byte[] encrypt(byte[] data, byte[] key) throws Exception {
        // 生成一个可信任的随机数源
        SecureRandom sr = new SecureRandom();

        // 从原始密钥数据创建DESKeySpec对象
        DESKeySpec dks = new DESKeySpec(key);

        // 创建一个密钥工厂，然后用它把DESKeySpec转换成SecretKey对象
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        SecretKey securekey = keyFactory.generateSecret(dks);

        // Cipher对象实际完成加密操作
        @SuppressLint("GetInstance") Cipher cipher = Cipher.getInstance("DES");
        // 用密钥初始化Cipher对象
        cipher.init(Cipher.ENCRYPT_MODE, securekey, sr);

        return cipher.doFinal(data);
    }
    
	/**
	 * 使用Base64算法对字符串进行解密
	 * @param 	encodedStr		加密后的字符串
	 * @param	encodingType	编码方式
	 */
	@SuppressLint({ "NewApi", "InlinedApi" })
	public static String decodeWithBase64(String encodedStr, String encodingType) throws Exception {
		if(encodedStr == null)
			return null;
		byte[] result = Base64.decode(encodedStr, Base64.DEFAULT);
//		Log.d(LibConst.LOG_TEST, "EncodeUtils -- EncodedBytes:: " + result);
		return new String(result, encodingType);
	}
	
	/**
	 * 从加密的验证码图片中获取验证码图片Bitmap对象
	 * 1. 首先将加密字符串解密，获取到图片的Base64码。
	 * 2. 从Base64码中还原验证码图片。
	 * @param encodedStr	验证码图片加密字符串
	 * @return	验证码图片Bitmap对象
	 */
	public static Bitmap decodeGiftBitmapFromEncodedCode(String encodedStr, String encodingGiftKey) {
		String decodedStr;
		try {
			decodedStr = decodeGiftImageCode(encodedStr, encodingGiftKey);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		byte[] decodedBytes = Base64.decode(decodedStr, Base64.DEFAULT);
		return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
	}
	public static Bitmap decodeGiftBitmapFromCode(String encodedStr) {
		byte[] decodedBytes = Base64.decode(encodedStr, Base64.DEFAULT);
		return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
	}
	
	/**
	 * 应用于礼包验证吗图片解密：将加密后的验证码图片码流解密
	 * 1. 将源码Base64解密
	 * 2. 生成32位偏移字符串
	 * 3. 各个位置先减33，再减偏移量，最后恢复到（33,127）范围内
	 * 3. 根据各个位置的字符偏移量还原验证码源码
	 */
	private static String decodeGiftImageCode(String encodedStr, String encodingGiftKey) throws Exception {
		// 1
//		Log.d(LibConst.LOG_TEST, "EncodeUtils -- 开始解密验证码图片:: " + encodedStr);
		String encodedOriginStr = decodeWithBase64(encodedStr, "utf-8");
//		Log.d(LibConst.LOG_TEST, "EncodeUtils -- EncodedOriginStr:: " + encodedOriginStr);
		
		// 2
		StringBuilder buffer = new StringBuilder(encodingGiftKey);
		String offsetStr = md5(buffer.reverse().toString());
		int offsetLength = offsetStr.length();
//		Log.d(LibConst.LOG_TEST, "EncodeUtils -- offsetStr:: " + encodingGiftKey + ", " + offsetStr);

		// 3
		String decodedStr = "";
		int encodedStrLength = encodedOriginStr.length();
		for(int i = 0; i < encodedStrLength; i++) {
			int encodeCharInt = (int)encodedOriginStr.charAt(i);
			int offsetCharInt = (int)offsetStr.charAt(i % offsetLength);
			int charInt = encodeCharInt - offsetCharInt - 33;
			while(charInt < 33)
				charInt += 94;
//			Log.d(LibConst.LOG_TEST, "EncodeUtils -- charInt:: " + encodeCharInt + ", " + offsetCharInt + ", " + charInt);
			decodedStr += (char) (charInt);
		}
//		Log.d(LibConst.LOG_TEST, "EncodeUtils -- DecodedStr:: " + decodedStr);
		return decodedStr;
	}
	
	/**
	 * 对Bitmap对象使用Base64进行加密
	 * @param bitmap	待加密的Bitmap对象
	 * @throws Exception
	 */
	public static String encodeBitmapWithBase64(Bitmap bitmap) throws Exception {
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		bitmap.compress(CompressFormat.PNG, 100, stream);
		byte[] bytes = stream.toByteArray();
//		return new String(bytes);
		return encodeWithBase64(bytes);
	}
	
	/**
	 * 从Bitmap图片对象获取其码流
	 * @param bitmap	图片对象
	 */
	public static byte[] getByteArrayFromBitmap(Bitmap bitmap) {
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		bitmap.compress(CompressFormat.JPEG, 50, stream);
		return stream.toByteArray();
	}
	
	/**
	 * 将String字符串转换成Unicode
	 * @param str	原始字符串
	 */
	public static String StringToUnicode(String str) {
		StringBuilder builder = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
			builder.append("\\u").append(Integer.toHexString(str.charAt(i)));
        }
        return builder.toString();
	}
	
	/**
	 * 将Unicode转换成String字符串
	 * @param str	Unicode String
	 */
	public static String UnicodeToString(String str) {
        Pattern pattern = Pattern.compile("(\\\\u(\\p{XDigit}{4}))");
        Matcher matcher = pattern.matcher(str);
        char ch;
        while (matcher.find()) {
            ch = (char) Integer.parseInt(matcher.group(2), 16);
            str = str.replace(matcher.group(1), ch + "");
        }
        return str;
	}


	public static String byteToHexString(byte[] src) {
        if(null == src || src.length < 1){
            return "";
        }

        StringBuilder sb = new StringBuilder();
        for(byte b:src){
            int i = b&0xff;
            if(i < 16){
                sb.append("0").append(Integer.toHexString(i));
            }else{
                sb.append(Integer.toHexString(i));
            }
        }
		return sb.toString();
	}


    public static byte[] hexStringToByte(String src) {
        if(null == src || ""== src || src.length()%2!=0){
            return null;
        }
        byte[] ret = new byte[src.length()/2];
        for(int i=0;i<ret.length;i++){
            int b = Integer.parseInt(src.substring(i*2, i*2+2), 16);
            ret[i] = (byte) b;
        }
        return ret;
    }


	/**
	 * 获取设备唯一识别码
	 *
	 * @return
	 */
	private static String DEVICE_UNIQUECODE = null;

	public static String getUniqueId(Context context) {
		if (TextUtils.isEmpty(DEVICE_UNIQUECODE)) {
			if (null != context) {
				try {
					TelephonyManager tm = (TelephonyManager) context
							.getSystemService(Context.TELEPHONY_SERVICE);
					String tmDevice = tm.getDeviceId();
					if (TextUtils.isEmpty(tmDevice)) {
						DEVICE_UNIQUECODE = getMacAddress(context);
					} else {
						DEVICE_UNIQUECODE = tmDevice;
					}
				} catch (Exception e) {
					DEVICE_UNIQUECODE = getMacAddress(context);
				}
			}

			if (TextUtils.isEmpty(DEVICE_UNIQUECODE)) {
				DEVICE_UNIQUECODE = "errorID";
			}
			//DEVICE_UNIQUECODE = EncodeUtils.md5(DEVICE_UNIQUECODE);
		}

		return DEVICE_UNIQUECODE;
	}


	/**
	 * 获取设备的Mac地址
	 */
	public static String getMacAddress(Context context) {
		String macSerial;
		String str;
		try {
			Process pp = Runtime.getRuntime().exec("cat /sys/class/net/wlan0/address");
			InputStreamReader ir = new InputStreamReader(pp.getInputStream());
			LineNumberReader input = new LineNumberReader(ir);

			str = input.readLine();
			if (str != null) {
				macSerial = str.trim();// 去空格
			} else {
				// 赋予默认值
				WifiManager manager = (WifiManager) context.getSystemService(
						Context.WIFI_SERVICE);
				macSerial = manager.getConnectionInfo().getMacAddress();
			}
		} catch (IOException ex) {
			// 赋予默认值
			WifiManager manager = (WifiManager) context.getSystemService(
					Context.WIFI_SERVICE);
			macSerial = manager.getConnectionInfo().getMacAddress();
		}

		return macSerial;
	}

}
