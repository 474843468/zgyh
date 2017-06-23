package com.chinamworld.bocmbci.biz.plps.order;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.util.EncodingUtils;

import android.annotation.SuppressLint;
import android.content.Context;

import com.chinamworld.bocmbci.log.LogGloble;

/**
 * Java汉字转换为拼音 
 * 
 */
public class CharacterParser {
	
	public static final String CHINESEPINYIN = "chinesepinyin.txt";
	
	private  String[] pyvalue = new String[] {};
	private   String[] pystr = new String[] {};
	private StringBuilder buffer;
	private String resource;
	private static CharacterParser characterParser = new CharacterParser();
	public static CharacterParser getInstance() {
		return characterParser;
	}

	public String getResource() {
		return resource;
	}

	public void setResource(String resource) {
		this.resource = resource;
	}

	 /** * 汉字转成ASCII码 * * @param chs * @return */  
	@SuppressLint("DefaultLocale")
	private String getChsAscii(String chs) {
		String asc = null;
		try {
			byte[] bytes = chs.getBytes("Unicode");
			if (bytes == null || bytes.length > 4 || bytes.length <= 0) {
				throw new RuntimeException("illegal resource string");
			}
//			if (bytes.length == 1) {
//				asc = bytes[0];
//			}
			if (bytes.length == 4) {
				int bytes2 = bytes[2];
				int bytes3 = bytes[3];
				if(bytes[2]>=0){
					bytes2 = bytes[2];
				}else {
					bytes2 = 256+bytes[2];
				}
				if(bytes[3]>=0){
					bytes3 = bytes[3];
				}else {
					bytes3 = 256+bytes[3];
				}
				int code = bytes3 * 256 + bytes2;
				asc = Integer.toHexString(code).toUpperCase();
			}
		} catch (Exception e) {
			System.out.println("ERROR:ChineseSpelling.class-getChsAscii(String chs)" + e);
		}
		return asc;
	}

	 /** * 单字解析 * * @param str * @return */  
	public String convert(String str) {
		String result = null;
		String ascii = getChsAscii(str);
//		if (ascii > 0 && ascii < 160) {
//			result = String.valueOf((char) ascii);
//		} else {
//			for (int i = (pyvalue.length - 1); i >= 0; i--) {
//				if (pyvalue[i] <= ascii) {
//					result = pystr[i];
//					break;
//				}
//			}
//		}
		if(chinesePinYinMap.containsKey(ascii)) {
			result = chinesePinYinMap.get(ascii);
		}
//		for(int i=0; i <pyvalue.length; i++){
//			if(ascii.equals(pyvalue[i])){
//				result = pystr[i];
//				break;
//			}
//		}
		return result;
	}

	  /** * 词组解析 * * @param chs * @return */  
	public String getSelling(String chs) {
		String key, value;
		buffer = new StringBuilder();
		for (int i = 0; i < chs.length(); i++) {
			key = chs.substring(i, i + 1);
			if (key.getBytes().length >= 2) {
				value = (String) convert(key);
				if (value == null) {
					value = "unknown";
				}
			} else {
				value = key;
			}
			buffer.append(value);
		}
		return buffer.toString();
	}

	public String getSpelling() {
		return this.getSelling(this.getResource());
	}

	
	
	/**
	 * 读取assets目录文件
	 * @param c
	 * @param fileName
	 * @return
	 */
	public static String readAssetsFile(Context c,String fileName) {
		String result = null;
		InputStream in;
		try {
			in = c.getAssets().open(fileName);
			int length = in.available();
			byte[]buffer = new byte[length];
			in.read(buffer);
			result =  EncodingUtils.getString(buffer, "UTF-8");
		} catch (IOException e) {
			LogGloble.exceptionPrint(e);
		}
		return result;
	}
	
	public static Map<String,String> chinesePinYinMap = new HashMap<String,String>();
	public static Map<String,String> initChinesePinYin(Context c){
		if(chinesePinYinMap.size() > 10000)
			return chinesePinYinMap;
		String tmp = readAssetsFile(c,CHINESEPINYIN);
		String [] chinesePinyins = tmp.split("\r\n");
		String [] tmps = null,values = null;
		for(String str : chinesePinyins){
			tmps = str.split(" ");
			values = tmps[1].replace("(", "").replace(")", "").split(",");
			chinesePinYinMap.put(tmps[0],values[0].replace("1", "").replace("2", "").replace("3", "").replace("4", ""));
		}
		return chinesePinYinMap;
	}
	
}
