package com.boc.bocsoft.mobile.framework.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * 排序的properies
 * 
 * @author gwluo
 * 
 */
public class OrderProperties {
	/** Keys */
	private List<String> keys = new ArrayList<String>();

	/** ValueMap */
	private Map<String, String> valueMap = new HashMap<String, String>();

	public String getValue(String key) {
		return valueMap.get(key);
	}

	public Map<String, String> getKeyValue() {
		return valueMap;
	}

	public List<String> getKeys(String keyPattern) {
		Pattern pat = Pattern.compile(keyPattern);
		List<String> kl = new ArrayList<String>();
		for (String k : keys) {
			if (pat.matcher(k).matches()) {
				kl.add(k);
			}
		}
		return kl;
	}

	/**
	 * 加载Properties文件
	 * 
	 * @param istream
	 * @throws Exception
	 */
	public synchronized void load(InputStream istream) throws Exception {
		List<String> lines = toLines(istream, "utf-8");

		// parse key-value
		for (String l : lines) {
			if (l.trim().startsWith("#")) {
				keys.add(l);
			} else {
				if (l.indexOf("=") > -1) {
					String k = l.substring(0, l.indexOf("=")).trim();
					String v = l.substring(l.indexOf("=") + 1).trim();
					keys.add(k);
					valueMap.put(k, v);
				} else {
					keys.add(l);
				}
			}
		}
	}

	String line = "";

	/**
	 * 以指定编码格式将输入流按行置入一个List<String>
	 * 
	 * @param input
	 * @return
	 * @throws Exception
	 */
	public List<String> toLines(InputStream input, String encoding)
			throws Exception {
		InputStreamReader insreader = new InputStreamReader(input, encoding);
		BufferedReader bin = new BufferedReader(insreader);
		List<String> lines = new ArrayList<String>();
		while (read(bin) != null) {
			lines.add(line);
		}
		bin.close();
		insreader.close();
		return lines;
	}

	public String read(BufferedReader bin) throws IOException {
		line = bin.readLine();
		return line;
	}

	public List<String> getKeys() {
		return keys;
	}

	@Override
	public String toString() {
		return valueMap.toString();
	}
}
