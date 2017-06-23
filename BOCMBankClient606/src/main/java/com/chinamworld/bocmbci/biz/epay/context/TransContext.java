package com.chinamworld.bocmbci.biz.epay.context;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.chinamworld.bocmbci.biz.epay.constants.PubConstants;
import com.chinamworld.bocmbci.biz.epay.util.EpayUtil;
import com.chinamworld.bocmbci.utils.StringUtil;

public class TransContext {

	private static Map<String, Object> map = new HashMap<String, Object>();
	
	public static void setData(String key, Object value) {
		map.put(key, value);
	}
	
	public static Object getData(String key) {
		return map.get(key);
	}
	
	public static String getString(String key, String defaultValue) {
		return EpayUtil.getString(map.get(key), defaultValue);
	}
	
	public static boolean getBoolean(String key) {
		Object obj = map.get(key);
		return EpayUtil.getBoolean(obj);
	}
	
	@SuppressWarnings("unchecked")
	public static Map<Object, Object> getMap(String key) {
		return EpayUtil.getMap(map.get(key));
	}
	
	@SuppressWarnings("unchecked")
	public static List<Object> getList(String key) {
		return EpayUtil.getList(map.get(key));
	}
	
	public static void clearData(String key) {
		if(StringUtil.isNullOrEmpty(key)) {
			map.clear();
		} else {
			if(map.containsKey(key))
				map.remove(key);
		}
	}
	
	public static Context getContext(String key) {
		return (Context)TransContext.getData(key);
	}
	
	public static Context getBomContext() {
		if(!map.containsKey(PubConstants.CONTEXT_BOM)) {
			map.put(PubConstants.CONTEXT_BOM, new Context());
		}
		return getContext(PubConstants.CONTEXT_BOM);
	}
	
	public static Context getWithoutCardContext() {
		if(!map.containsKey(PubConstants.CONTEXT_WITHOUT_CARD)) {
			map.put(PubConstants.CONTEXT_WITHOUT_CARD, new Context());
		}
		return getContext(PubConstants.CONTEXT_WITHOUT_CARD);
	}
	
	public static Context getTQTransContext() {
		if(!map.containsKey(PubConstants.CONTEXT_TQ)) {
			map.put(PubConstants.CONTEXT_TQ, new Context());
		}
		return getContext(PubConstants.CONTEXT_TQ);
	}
	
	public static Context getTreatyTransContext() {
		if(!map.containsKey(PubConstants.CONTEXT_TREATY)) {
			map.put(PubConstants.CONTEXT_TREATY, new Context());
		}
		return getContext(PubConstants.CONTEXT_TREATY);
	}
	
}
