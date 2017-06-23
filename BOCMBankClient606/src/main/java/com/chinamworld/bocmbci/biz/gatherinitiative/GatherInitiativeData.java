package com.chinamworld.bocmbci.biz.gatherinitiative;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * @ClassName: GatherInitiativeData
 * @Description: 主动收款数据存储类
 * @author JiangWei
 * @date 2013-8-21上午11:13:54
 */
public class GatherInitiativeData {
	private static GatherInitiativeData dataCenter;
	/** 查询“收款账户”列表 */
	private List<Map<String, Object>> queryAcountCallBackList;
	/** 查询“付款账户”列表 */
	private List<Map<String, Object>> payAcountCallBackList;
	/** 查询详情数据 */
	private Map<String, Object> detailMap;
	/** 付款人详情 */
	private Map<String, Object> payerInfoMap;
	/** 发起新收款流程的随机数 */
	private String randomNumber;

	private GatherInitiativeData() {

	}

	public static GatherInitiativeData getInstance() {
		if (dataCenter == null) {
			dataCenter = new GatherInitiativeData();
		}
		return dataCenter;
	}

	public List<Map<String, Object>> getQueryAcountCallBackList() {
		if (queryAcountCallBackList == null) {
			queryAcountCallBackList = new ArrayList<Map<String, Object>>();
		}
		return queryAcountCallBackList;
	}

	public void setQueryAcountCallBackList(List<Map<String, Object>> queryCallBackList) {
		this.queryAcountCallBackList = queryCallBackList;
	}

	public List<Map<String, Object>> getPayAcountCallBackList() {
		if (payAcountCallBackList == null) {
			payAcountCallBackList = new ArrayList<Map<String, Object>>();
		}
		return payAcountCallBackList;
	}

	public void setPayAcountCallBackList(List<Map<String, Object>> payAcountCallBackList) {
		this.payAcountCallBackList = payAcountCallBackList;
	}

	public Map<String, Object> getDetailInfo() {
		if(detailMap == null){
			detailMap = new HashMap<String, Object>();
		}
		return detailMap;
	}

	public void setDetailInfo(Map<String, Object> accountInfoMap) {
		this.detailMap = accountInfoMap;
	}

	public Map<String, Object> getPayerInfo() {
		if(payerInfoMap == null){
			payerInfoMap = new HashMap<String, Object>();
		}
		return payerInfoMap;
	}

	public void setPayerInfo(Map<String, Object> payerInfoMap) {
		this.payerInfoMap = payerInfoMap;
	}

	public void setRandomNumber(String randomNumber) {
		this.randomNumber = randomNumber;
	}

	public String getRandomNumber() {
		return randomNumber;
	}

	/**
	 * @Title: clearDrawMoneyData
	 * @Description: 清理"主动收款模块的缓存"
	 * @param
	 * @return void
	 * @throws
	 */
	public void clearGatherData() {
		if (!StringUtil.isNullOrEmpty(queryAcountCallBackList)) {
			queryAcountCallBackList.clear();
		}
		if (!StringUtil.isNullOrEmpty(detailMap)) {
			detailMap.clear();
		}
		if (!StringUtil.isNullOrEmpty(payAcountCallBackList)) {
			payAcountCallBackList.clear();
		}
		if (!StringUtil.isNullOrEmpty(payerInfoMap)) {
			payerInfoMap.clear();
		}
	}
}
