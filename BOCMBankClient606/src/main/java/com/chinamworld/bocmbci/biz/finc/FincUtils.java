package com.chinamworld.bocmbci.biz.finc;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import android.content.Context;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.bii.constant.Finc;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

public class FincUtils {
	
	/**
	 * 处理返回数据
	 * 
	 * @param result
	 * @return
	 */
//	public static Object httpResponseDeal(Object result) {
//		BiiResponse biiResponse = (BiiResponse) result;
//		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
//		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
//		return biiResponseBody.getResult();
//	}
	
	/**
	 * 循环遍历设置浮动框
	 * @param textViews控件列表
	 */
	public static void setOnShowAllTextListener(Context context, TextView... textViews){
		if(textViews == null || textViews.length <= 0)
			return;
		for(TextView tv : textViews){
			if(tv != null)
				PopupWindowUtils.getInstance().setOnShowAllTextListener(context,
						tv);
		}
	}
	
	/**
	 * 初始化下拉框
	 * @param c
	 * @param sp
	 * @param list
	 */
	public static List<String> initSpinnerView(Context c,Spinner sp,Map<String, Object> map) {
		if(StringUtil.isNullOrEmpty(map))
			return null;
		List<String> list = getMapValueList(map, false);
		boolean isdefault = true;
		if (StringUtil.isNullOrEmpty(list)) {
			list = new ArrayList<String>();
			list.add("");
			isdefault = false;
		}
		setSpinnerBackground(sp, isdefault);
		initSpinnerView(c, sp, list);
		return list;
	}
	
	
	public static List<String> initSpinnerView(Context c, Spinner sp, List<String> list){
		ArrayAdapter<ArrayList<String>> mAdapter = new ArrayAdapter(c,
				 R.layout.dept_spinner,list);
		sp.setPadding(
				c.getResources().getDimensionPixelSize(R.dimen.dp_seven), 0,
				c.getResources().getDimensionPixelSize(R.dimen.dp_two_zero), 0);
		mAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		sp.setAdapter(mAdapter);
		return list;
	}
	
	/**
	 * 设置sipnner背景
	 * @param v
	 * @param isdefault
	 */
	public static void setSpinnerBackground(View v,boolean isdefault){
		if (v != null) {
			if (isdefault) {
				v.setClickable(true);
				v.setBackgroundResource(R.drawable.bg_spinner);
			}else{
				v.setClickable(false);
				v.setBackgroundResource(R.drawable.bg_spinner_default);
			}
		}
	}
	
	/**
	 * 获取Map字典的List的值列表或Key列表
	 * @param map原字典
	 * @param isKey 是否是获取key值列表
	 * @return
	 */
	public static List<String> getMapValueList(Map<String, Object> map, boolean isKey){
		List<String> list = new ArrayList<String>();
		Iterator<Entry<String, Object>> iter = map.entrySet().iterator();
		Entry<String, Object> entry;
		while(iter.hasNext()){
			entry = iter.next();
			if(isKey){
				list.add(entry.getKey());
			}else{
				list.add((String)entry.getValue());
			}
		}
		return list;
	}
	
	public static String getKeyByValue(Map<String, Object> map, String value){
		if(StringUtil.isNullOrEmpty(map))
			return "";
		Set set = map.entrySet();
		Iterator iterator = set.iterator();
		while(iterator.hasNext()){
			Map.Entry<String, Object> enter = (Map.Entry<String, Object>)iterator.next();
			if(isStrEquals(value, (String)enter.getValue()))
				return enter.getKey();
		}
		return "";
	}
	
	/**
	 * 判斷兩個字符串是否一样
	 * @param orig 要判断的字符串
	 * @param newStr 目的串
	 * @return true:一样，false:不一样
	 */
	public static boolean isStrEquals(String orig, String newStr){
		if(orig == null || newStr == null || orig.equals("") || newStr.equals(""))
			return false;
		if(!orig.equals(newStr))
			return false;
		return true;
	}
	
	/**
	 * 获取不带冒号的字符串
	 * @param str
	 * @return
	 */
	public static String getNoColonStr(String str){
		if (StringUtil.isNull(str))
			return ConstantGloble.BOCINVT_DATE_ADD;
		if (str.contains("：") || str.contains(":"))
			return str.substring(0, str.length() - 1);
		return str;
	}
	
	public static boolean isListContainsStr(List<Map<String, String>> list, String... string){
		if(StringUtil.isNullOrEmpty(list) || StringUtil.isNullOrEmpty(string))
			return false;
		for(Map<String, String> map : list){
			for(String origStr :string){
				if(isStrEquals(origStr, map.get(Finc.BRANCH)))
					return true;
			}
		}
		return false;
	}
	
	/**
	 * 日元去掉小数部分
	 * @param transSum
	 * @return
	 */
	public static String getYenIntegerStr(String transSum){
		if(transSum.contains(".")){
			transSum = transSum.substring(0, transSum.indexOf("."));
		}
		return transSum;
	}
	
	/**
	 * 如果是0,0.0,0.00中的一个则返回true，否则返回false
	 * @param str
	 * @return
	 */
	public static boolean isZero(String str){
		if("0".equals(str)){
			return true;
		}
		if("0.0".equals(str)){
			return true;
		}
		if("0.00".equals(str)){
			return true;
		}
		return false;
	}
	
}
