package com.chinamworld.bocmbci.biz.setting.safetytools.entity;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class ObjectValueSerializable implements Serializable {
	private Map<String , Object> map = null;
	private List<Map<String , Object>> list = null;
	public ObjectValueSerializable(List<Map<String , Object>> list){
		this.list = list ;
	}
	public List<Map<String, Object>> getList() {
		return list;
	}
	public void setList(List<Map<String, Object>> list) {
		this.list = list;
	}
	public ObjectValueSerializable(Map<String , Object> map){
		this.map = map;
	}
	
	public Map<String, Object> getMap() {
		return map;
	}

	public void setMap(Map<String, Object> map) {
		this.map = map;
	}
	
}
