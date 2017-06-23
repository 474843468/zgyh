package com.chinamworld.bocmbci.biz.remittance.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.util.EncodingUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.bii.constant.Safety;
import com.chinamworld.bocmbci.biz.remittance.RemittanceContent;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 跨境汇款工具类
 * 
 * @author Zhi
 */
public class RemittanceUtils {

	/**
	 * 构造spinner数据，支持将卡账号格式化为464格式
	 * 
	 * @param list
	 *            原始数据
	 * @param key
	 *            从原始数据中取数据的字段
	 * @param zeroDefalt
	 *            下拉框第0个选项显示的提示，为空则不添加
	 * @return 构造后的数据列表
	 */
//	public static List<String> initSpinnerData(List<Map<String, Object>> list, String key, String zeroDefalt) {
//		if (StringUtil.isNullOrEmpty(list)) {
//			return null;
//		}
//		ArrayList<String> mList = new ArrayList<String>();
//		if (!StringUtil.isNull(zeroDefalt)) {
//			mList.add(zeroDefalt);
//		}
//
//		if (!StringUtil.isNullOrEmpty(list)) {
//			for (int i = 0; i < list.size(); i++) {
//				if (key.equals(Comm.ACCOUNTNUMBER)) {
//					mList.add(StringUtil.getForSixForString((String) list.get(i).get(key)));
//				} else {
//					mList.add((String) list.get(i).get(key));
//				}
//			}
//		}
//		return mList;
//	}

	/**
	 * 初始化下拉框
	 * 
	 * @param c
	 * @param sp
	 * @param list
	 */
	public static void initSpinnerView(Context c, Spinner sp, List<String> list) {
		boolean isdefault = true;
		if (StringUtil.isNullOrEmpty(list)) {
			list = new ArrayList<String>();
			list.add("");
			isdefault = false;
		}
		if (list.size() == 1) {
			isdefault = false;
		}
		setSpinnerBackground(sp, isdefault);
		@SuppressWarnings({ "rawtypes", "unchecked" })
		ArrayAdapter<ArrayList<String>> mAdapter = new ArrayAdapter(c, R.layout.spinner_item, list);
		mAdapter.setDropDownViewResource(R.layout.simple_spinner_item_not_single_line);
		sp.setAdapter(mAdapter);
	}
	/**
	 * 初始化下拉框    付费币种
	 */
	public static void initPayeeMoneySpinnerView(Context c,Spinner sp,List<String> list
			){
		boolean isdefault=true;
		if(StringUtil.isNullOrEmpty(list)){
			list.add("");
			isdefault=false;
		}
		setSpinnerBackground(sp, isdefault);
		ArrayAdapter<ArrayList<String>> mAdapter=new ArrayAdapter(c,R.layout.spinner_item,list);
		mAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		sp.setAdapter(mAdapter);
	}
	
	/**
	 * 设置sipnner背景
	 * 
	 * @param v
	 * @param isdefault
	 */
	public static void setSpinnerBackground(View v, boolean isdefault) {
		if (v != null) {
			if (isdefault) {
				v.setClickable(true);
				v.setBackgroundResource(R.drawable.bg_spinner);
			} else {
				v.setClickable(false);
				v.setBackgroundResource(R.drawable.bg_spinner_default);
			}
		}
	}

	/** 把钞/汇代码转为中文钞/汇 */
	public static String getCashRimit(String cashRimit) {
		if (cashRimit.equals("01")) {
			return "现钞";
		} else if (cashRimit.equals("02")) {
			return "现汇";
		} else {
			return "-";
		}
	}
	/**
	 * 读取国家
	 * 
	 * @param c
	 * @return
	 */
	public static List<Map<String, String>> getCountry(Context c) {
		ArrayList<Map<String, String>> list = new ArrayList<Map<String, String>>();
		try {
			JSONObject jb = new JSONObject(readAssetsFile(c, RemittanceContent.COUNTRYFILENAME));
			JSONArray ja = (JSONArray) jb.getJSONArray(Safety.COUNTRY);
			for (int i = 0; i < ja.length(); i++) {
				HashMap<String, String> map = new HashMap<String, String>();
				JSONObject object = (JSONObject) ja.opt(i);
				map.put(Safety.NAME, object.getString(Safety.NAME));
				map.put(Safety.CODE, object.getString(Safety.CODE));
				list.add(map);
			}
		} catch (Exception e) {
			LogGloble.exceptionPrint(e);
		}
		return list;
	}

	/**
	 * 读取assets目录文件
	 * 
	 * @param c
	 * @param fileName
	 * @return
	 */
	public static String readAssetsFile(Context c, String fileName) {
		String result = null;
		InputStream in;
		try {
			in = c.getAssets().open(fileName);
			int length = in.available();
			byte[] buffer = new byte[length];
			in.read(buffer);
			result = EncodingUtils.getString(buffer, "UTF-8");
		} catch (IOException e) {
			LogGloble.exceptionPrint(e);
		}
		return result;
	}

	/**
	 * 输入的字符是否是汉字
	 * 
	 * @param a
	 *            char
	 * @return boolean
	 */
	public static boolean isChinese(char a) {
		int v = (int) a;
		return (v >= 19968 && v <= 171941);
	}

	/**
	 * 判断字符串中是否有汉字
	 * 
	 * @param s
	 * @return
	 */
	public static boolean containsChinese(String s) {
		if (null == s || "".equals(s.trim()))
			return false;
		for (int i = 0; i < s.length(); i++) {
			if (isChinese(s.charAt(i)))
				return true;
		}
		return false;
	}
}
