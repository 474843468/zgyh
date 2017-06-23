package com.chinamworld.bocmbci.biz.peopleservice.entity;

import java.util.Map;

public class BTCCommbean {

	/**请求方法post/get*/
	private String method;//请求方法post/get
	/**请求地址*/
//	private String url;//请求地址
	/**请求参数*/
	private Map<String,Object> params;//请求参数
	private Map<String,String> response;//返回参数
	private String name;// comm name
//	/**需要setvar变量列表*/
//	private Map<String, String> varMap;//需要setvar变量列表
//	/**除了具体值时需要的数据库变量*/
//	private Map<String, String> dbMap;//除了具体值时需要的数据库变量
//	/**content_type中的数据*/
//	private Map<String, String> contentType;
//	
	/**
	 * get/set方法对
	 * @return
	 */
//	public Map<String, String> getContentType() {
//		return contentType;
//	}
//
//	public void setContentType(Map<String, String> contentType) {
//		this.contentType = contentType;
//	}
//	
//	public Map<String, String> getDbMap() {
//		return dbMap;
//	}
//	
//	public void setDbMap(Map<String, String> dbMap) {
//		this.dbMap = dbMap;
//	}
//
//	public Map<String, String> getVarMap() {
//		return varMap;
//	}
//
//	public void setVarMap(Map<String, String> varMap) {
//		this.varMap = varMap;
//	}

	public Map<String, Object> getParams() {
		return params;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Map<String, String> getResponse() {
		return response;
	}

	public void setResponse(Map<String, String> response) {
		this.response = response;
	}

	public void setParams(Map<String, Object> params) {
		this.params = params;
	}
	
	public String getMethod() {
		return method;
	}
	
	public void setMethod(String method) {
		this.method = method;
	}
	
//	public String getUrl() {
//		return url;
//	}
//	
//	public void setUrl(String url) {
//		this.url = url;
//	}
//	


	
	/**
	 * 无参构造函数
	 */
	public BTCCommbean() {
		super();
	}
	
	public BTCCommbean(String method, String url, Map<String, Object> params,
			Map<String, String> response, String name) {
		super();
		this.method = method;
//		this.url = url;
		this.params = params;
		this.response = response;
		this.name = name;
	}

	/**
	 * 返回封装的信息
	 */
	@Override
	public String toString() {
		return "BTCCommbean [  name=" + name
				+ ",\n method=" + method
				+ ",\n params=" + params +",\n url=" +  ",\n response=" + response +
				 "]";
	}

	


}
