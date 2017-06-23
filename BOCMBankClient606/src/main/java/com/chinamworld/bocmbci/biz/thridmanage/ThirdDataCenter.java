package com.chinamworld.bocmbci.biz.thridmanage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ThirdDataCenter {

	private static ThirdDataCenter instance;

	/** 所有账户列表 */
	private List<Map<String, Object>> bankAccountList;
	/** 保证金账户  **/
	private List<Map<String, Object>> cecAccountList;
	/** 用户列表  **/
	private Map<String, Object> customInfo;
	/** 省份代码 */
	private List<String> provinceCode;
	/** 营业部列表  **/
	private List<Map<String, Object>> stockBranList;

	public static ThirdDataCenter getInstance() {
		if (instance == null) {
			instance = new ThirdDataCenter();
		}
		return instance;
	}

	public List<Map<String, Object>> getBankAccountList() {
		return bankAccountList;
	}

	public void setBankAccountList(List<Map<String, Object>> bankAccountList) {
		this.bankAccountList = bankAccountList;
	}

	public List<Map<String, Object>> getCecAccountList() {
		return cecAccountList;
	}

	public void setCecAccountList(List<Map<String, Object>> cecAccountList) {
		this.cecAccountList = cecAccountList;
	}

	public Map<String, Object> getCustomInfo() {
		return customInfo;
	}

	public void setCustomInfo(Map<String, Object> customInfo) {
		this.customInfo = customInfo;
	}

	public List<String> getProvinceCode() {
		return provinceCode;
	}

	public void setProvinceCode(List<String> provinceCode) {
		this.provinceCode = provinceCode;
	}

	public List<Map<String, Object>> getStockBranList() {
		return stockBranList;
	}

	public void setStockBranList(List<Map<String, Object>> stockBranList) {
		this.stockBranList = stockBranList;
	}

//	public ArrayList<String> setEncy() {
//		ArrayList<String> ency = new ArrayList<String>();
//		ency.add("人民币");
//		return ency;
//	}

	public List<String> spInitData() {
		ArrayList<String> init = new ArrayList<String>();
		init.add("请选择");
		return init;
	}

	/**
	 * 清空所有数据 
	 */
	public void clear() {
		if (bankAccountList != null) {
			bankAccountList.clear();
		}
		if (cecAccountList != null) {
			cecAccountList.clear();
		}
		if (customInfo != null) {
			customInfo.clear();
		}
		if (provinceCode != null) {
			provinceCode.clear();
		}
		if (stockBranList != null) {
			stockBranList.clear();
		}
	}
}
