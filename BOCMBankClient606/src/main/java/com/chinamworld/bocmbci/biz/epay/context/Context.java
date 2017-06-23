package com.chinamworld.bocmbci.biz.epay.context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.chinamworld.bocmbci.biz.epay.util.EpayUtil;

public class Context {

	private Map<String, Object> map = new HashMap<String, Object>();

	private boolean rightButtonClick = false;

	public Object getData(String key) {
		return map.get(key);
	}

	public String getString(String key, String defaultValue) {
		return EpayUtil.getString(map.get(key), defaultValue);
	}

	public boolean getBoolean(String key) {
		return EpayUtil.getBoolean(map.get(key));
	}

	@SuppressWarnings("unchecked")
	public Map<Object, Object> getMap(String key) {
		Map<Object, Object> mapTemp = null;
		if(!map.containsKey(key)) {
			mapTemp = new HashMap<Object, Object>();
			this.map.put(key, mapTemp);
		}
		return (Map<Object, Object>)map.get(key);
	}

	@SuppressWarnings("unchecked")
	public List<Object> getList(String key) {
		List<Object> list = null;
		if(!map.containsKey(key)) {
			list = new ArrayList<Object>();
			this.map.put(key, list);
		}
		return (List<Object>)map.get(key);
	}

	public void setData(String key, Object value) {
		map.put(key, value);
	}

	public void clear(String key) {
		if (key != null && !"".equals(key))
			map.remove(key);
		else {
			rightButtonClick = false;
			map.clear();
		}
	}

	public void setRightButtonClick(boolean rightButtonClick) {
		this.rightButtonClick = rightButtonClick;
	}

	public boolean isRightButtonClick() {
		return rightButtonClick;
	}
}
