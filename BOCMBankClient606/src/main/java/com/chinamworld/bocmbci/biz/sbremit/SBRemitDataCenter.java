package com.chinamworld.bocmbci.biz.sbremit;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.chinamworld.bocmbci.utils.StringUtil;

public class SBRemitDataCenter {
	
	private static SBRemitDataCenter dataCenter;
	/** 资金用途 */
	private Map<String, String> capitalUse;
	/** 币种 */
	private ArrayList<String> moneyType;
	/** 交易状态 */
	private Map<String, String> transStatus;
	/** 结汇/购汇 */
	private Map<String, String> sbRemit;
	/** 人民币 */
	private Map<String, String> rmbCode;
	/** 交易详情 */
	private Map<String, Object> infoDetail;
	/** 账户信息 */
	private Map<String, Object> accInfo;
	/** 交易历史列表 */
	private Map<String, Object> tradeListTotal;
	

	public Map<String, Object> getTradeListTotal() {
		return tradeListTotal;
	}

	public void setTradeListTotal(Map<String, Object> tradeListTotal) {
		this.tradeListTotal = tradeListTotal;
	}

	/** 金额格式化参数 */
	public final int CASH_PATTERN_PARAM = 2;
	/** 账户余额列表 */
	public List<Map<String, Object>> accRemainList = null;
	/** 外汇牌价 */
	public List<Map<String,Object>> exchangeOutlay;
	/** 最大金额 */
	public Map<String, Object> tryMap;
	/** 已用额度、剩余额度、随即值、表单 */
	public Map<String, Object> resultAmount;
	
	public Map<String, Object> getResultAmount() {
		return resultAmount;
	}

	public void setResultAmount(Map<String, Object> resultAmount) {
		this.resultAmount = resultAmount;
	}

	public Map<String, Object> getTryMap() {
		return tryMap;
	}

	public void setTryMap(Map<String, Object> tryMap) {
		this.tryMap = tryMap;
	}

	public SBRemitDataCenter(){}
	
	public static SBRemitDataCenter getInstance() {
		if (dataCenter == null) {
			dataCenter = new SBRemitDataCenter();
		}
		return dataCenter;
	}

	/**
	 * @Title: clearSBRemitDataCenter
	 * @Description: 清理结售汇数据
	 * @param
	 * @return void
	 */
	public void clearDrawMoneyData() {
		if (!StringUtil.isNullOrEmpty(capitalUse)) {
			capitalUse.clear();
		}
		if (!StringUtil.isNullOrEmpty(moneyType)) {
			moneyType.clear();
		}
		if (!StringUtil.isNullOrEmpty(transStatus)) {
			transStatus.clear();
		}
		if (!StringUtil.isNullOrEmpty(sbRemit)) {
			sbRemit.clear();
		}
		if (!StringUtil.isNullOrEmpty(rmbCode)) {
			rmbCode.clear();
		}
		if (!StringUtil.isNullOrEmpty(accRemainList)) {
			accRemainList.clear();
		}
		if (!StringUtil.isNullOrEmpty(infoDetail)) {
			infoDetail.clear();
		}
		if (!StringUtil.isNullOrEmpty(accInfo)) {
			accInfo.clear();
		}
		if (!StringUtil.isNullOrEmpty(exchangeOutlay)) {
			exchangeOutlay.clear();
		}
	}

	
	public Map<String, Object> getInfoDetail() {
		return infoDetail;
	}

	public void setInfoDetail(Map<String, Object> infoDetail) {
		this.infoDetail = infoDetail;
	}

	public Map<String, String> getCapitalUse() {
		return capitalUse;
	}

	public void setCapitalUse(Map<String, String> capitalUse) {
		this.capitalUse = capitalUse;
	}

	public ArrayList<String> getMoneyType() {
		return moneyType;
	}

	public void setMoneyType(ArrayList<String> moneyType) {
		this.moneyType = moneyType;
	}

	public Map<String, String> getTransStatus() {
		return transStatus;
	}

	public void setTransStatus(Map<String, String> transStatus) {
		this.transStatus = transStatus;
	}

	public Map<String, String> getSbRemit() {
		return sbRemit;
	}

	public void setSbRemit(Map<String, String> sbRemit) {
		this.sbRemit = sbRemit;
	}

	public Map<String, String> getRmbCode() {
		return rmbCode;
	}

	public void setRmbCode(Map<String, String> rmbCode) {
		this.rmbCode = rmbCode;
	}
	
	
	
	public List<Map<String, Object>> getAccRemainList() {
		return accRemainList;
	}

	public void setAccRemainList(List<Map<String, Object>> accRemainList) {
		this.accRemainList = accRemainList;
	}

	public Map<String, Object> getAccInfo() {
		return accInfo;
	}

	public void setAccInfo(Map<String, Object> accInfo) {
		this.accInfo = accInfo;
	}


	
	public List<Map<String, Object>> getExchangeOutlay() {
		return exchangeOutlay;
	}

	public void setExchangeOutlay(List<Map<String, Object>> exchangeOutlay) {
		this.exchangeOutlay = exchangeOutlay;
	}

	/** 账户列表需要屏蔽的账户 */
	public static List<String> dissMissAcc = new ArrayList<String>(){
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			add("199");//优汇通专户
		}
	};
}
