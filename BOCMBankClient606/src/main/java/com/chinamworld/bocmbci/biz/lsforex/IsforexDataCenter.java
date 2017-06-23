package com.chinamworld.bocmbci.biz.lsforex;

import com.chinamworld.bocmbci.utils.StringUtil;

import java.util.Map;

/**
 * 双向宝 数据中心
 * @author luqipeng
 * 2015年7月6日23:16:44
 */
public class IsforexDataCenter {
	private static IsforexDataCenter dataCenter;
	
	/** 成交状况查询*/
	private  Map<String, Object> lradeDetailMap;
	
	private IsforexDataCenter() {
	}

	public static IsforexDataCenter getInstance() {
		if (dataCenter == null) {
			dataCenter = new IsforexDataCenter();
		}
		return dataCenter;
	}

	public Map<String, Object> getLradeDetailMap() {
		return lradeDetailMap;
	}

	public void setLradeDetailMap(Map<String, Object> lradeDetailMap) {
		this.lradeDetailMap = lradeDetailMap;
	}
	
	
	/**
	 * 清除datacenter所有数据
	 */
	public void clearAccData() {
		if (!StringUtil.isNullOrEmpty(lradeDetailMap)) {
			lradeDetailMap.clear();
		}
	}
	/**贵金属-币种排序
	 * 顺序如下：
	 * 人民币金 --- 黄金（克）
	 人民币银 --- 白银（克）
	 人民币铂 --- 铂金（克）
	 人民币钯 --- 钯金（克）
	 美元金--- 美元（盎司）
	 美元银--  白银（盎司）
	 美元铂---  铂金（盎司）
	 美元钯---  钯金（盎司）*/
//	public static List<String> code_List = new ArrayList() {
//		{
//			this.add("035001");
//			this.add("068001");
//			this.add("845001");
//			this.add("844001");
//			this.add("034014");
//			this.add("036014");
//			this.add("045014");
//			this.add("841014");
//		}
//	};
}
