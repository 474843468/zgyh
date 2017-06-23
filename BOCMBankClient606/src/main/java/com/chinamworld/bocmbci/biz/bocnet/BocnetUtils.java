package com.chinamworld.bocmbci.biz.bocnet;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
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
import com.chinamworld.bocmbci.bii.constant.Bocnet;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

public class BocnetUtils {
	
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
	 * @param isKeyOrValue true:KeyList  false: valueList
	 */
	public static List<String> initSpinnerView(Context c,Spinner sp,Map<String, Object> map, boolean isKeyOrValue) {
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
	
	public static List<String> initSpinnerView(Context c, Spinner sp, List<String> list){
		ArrayAdapter<ArrayList<String>> mAdapter = new ArrayAdapter(c,
				R.layout.spinner_item, list);
		mAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		sp.setAdapter(mAdapter);
		return list;
	}
	
	/**
	 * 根据字典列表得到相应的字符串列表
	 * @param listMap 原字典列表
	 * @param key 目标字段
	 * @param isFilterDupl 是否需要根据某字段去重
	 * @param DuplKey 去重字段名
	 * @return
	 */
	public static List<String> getListFromListMapByKey(List<Map<String, Object>> listMap, String key, 
			boolean isFilterDupl){
			if(StringUtil.isNullOrEmpty(listMap) || StringUtil.isNullOrEmpty(key))
				return new ArrayList<String>();
			else
				return getListFromListMap(listMap, key, isFilterDupl);
	}
	
	/**
	 * 从ListMap中获取对应字段的list
	 * @param listMap
	 * @param key
	 * @param isFilterDupl
	 * @return
	 */
	public static List<String> getListFromListMap(List<Map<String, Object>> listMap, String key, 
			boolean isFilterDupl){
		List<String> list = new ArrayList<String>();
		for(Map<String, Object> map : listMap){
			String str = (String)map.get(key);
			if(isFilterDupl){
				if(!StringUtil.isNull(str) && !list.contains(str))
					list.add(str);
			}else{
				if(!StringUtil.isNull(str))
					list.add(str);
			}
		}
		return list;

	}
	
	/**
	 * 根据币种获取钞汇
	 * @param listMap
	 * @param key
	 * @param isFilterDupl
	 * @return
	 */
	public static List<String> getCashRemitListByCurrency(List<Map<String, Object>> listMap, String currency){
		List<String> list = new ArrayList<String>();
		for(Map<String, Object> map : listMap){
			String cur = (String)map.get(Bocnet.CURRENCYCODE);
			if(isStrEquals(cur, currency)){
				String cr = (String)map.get(Bocnet.CASHREMIT);
				String crStr = LocalData.cashRemitBackMap.get(cr);
				if(!StringUtil.isNull(crStr) && !list.contains(crStr) && !isStrEquals(crStr, "-"))
					list.add(crStr);
			}
		}
		return list;
		
	}
	
	/**
	 * 从List列表中组建Map
	 * @param listMap
	 * @param key
	 * @param valueKey
	 * @return
	 */
	public static Map<String, Object> getMapFromMapList(List<Map<String, Object>> listMap, 
			String key, String valueKey){
		Map<String, Object> resultMap= new LinkedHashMap<String, Object>();
		if(StringUtil.isNullOrEmpty(listMap) || StringUtil.isNull(key) || StringUtil.isNull(valueKey))
			return resultMap;
		for(Map<String, Object> map : listMap){
			if(map != null && map.containsKey(key) && map.containsKey(valueKey)){
				resultMap.put((String)map.get(key), map.get(valueKey));
			}
		}
		return resultMap;
	}
	
	/**
	 * 获取币种的key值
	 * @param map
	 * @param value
	 * @return
	 */
	public static String getKeyByValue(Map<String, String> map, String value){
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
}
