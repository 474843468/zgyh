package com.chinamworld.bocmbci.biz.drawmoney;

import java.util.List;
import java.util.Map;

import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * @ClassName: DrawMoneyData
 * @Description: 手机存款数据存储类
 * @author JiangWei
 * @date 2013-7-15 下午5:56:00
 */
public class DrawMoneyData {
	private static DrawMoneyData dataCenter;
	/** 所有账户列表 */
	private List<Map<String, Object>> accountList;
	/** 筛选后的列表 */
	private List<Map<String, Object>> accountScreenList;
	/** 查询结果列表 */
	private List<Map<String, Object>> queryCallBackList;
	/** 代理点结果列表 */
	private List<Map<String, Object>> agencyQueryList;
	/** 账户详情数据 */
	private Map<String, Object> accountInfoMap;
	/** 查询汇款条目的详情 */
	private Map<String, Object> queryResultMap;
	/** 取款详情数据 */
	private Map<String, Object> drawInfoMap;
	/** 随机数 */
	private String randomNumber;

	private DrawMoneyData() {

	}

	public static DrawMoneyData getInstance() {
		if (dataCenter == null) {
			dataCenter = new DrawMoneyData();
		}
		return dataCenter;
	}

	public List<Map<String, Object>> getAccountList() {
		return accountList;
	}

	public void setAccountList(List<Map<String, Object>> accountList) {
		this.accountList = accountList;
	}
	
	public List<Map<String, Object>> getScreenAccountList() {
		return accountScreenList;
	}

	public void setScreenAccountList(List<Map<String, Object>> accountScreenList) {
		this.accountScreenList = accountScreenList;
	}
	
	public List<Map<String, Object>> getQueryCallBackList() {
		return queryCallBackList;
	}
	
	public void setAgencyList(List<Map<String, Object>> agencyQueryList) {
		this.agencyQueryList = agencyQueryList;
	}
	
	public List<Map<String, Object>> getAgencyList() {
		return agencyQueryList;
	}
	
	public void setQueryCallBackList(List<Map<String, Object>> queryCallBackList) {
		this.queryCallBackList = queryCallBackList;
	}
	
	public Map<String, Object> getAccountInfo(){
		return accountInfoMap;
	}
	
	public void setAccountInfo(Map<String, Object> accountInfoMap){
		this.accountInfoMap = accountInfoMap;
	}
	
	public Map<String, Object> getQueryResultDetail(){
		return queryResultMap;
	}
	
	public void setQueryResultDetail(Map<String, Object> queryResultMap){
		this.queryResultMap = queryResultMap;
	}
	
	public Map<String, Object> getDrawInfo(){
		return drawInfoMap;
	}
	
	public void setDrawInfo(Map<String, Object> drawInfoMap){
		this.drawInfoMap = drawInfoMap;
	}
	
	public String getRandomNumber(){
		return randomNumber;
	}
	
	public void setRandomNumber(String randomNumber){
		this.randomNumber = randomNumber;
		
	}

	/**
	 * @Title: clearDrawMoneyData
	 * @Description: 清理手机取款模块的数据
	 * @param
	 * @return void
	 */
	public void clearDrawMoneyData() {
		if (!StringUtil.isNullOrEmpty(accountList)) {
			accountList.clear();
		}
		if (!StringUtil.isNullOrEmpty(accountScreenList)) {
			accountScreenList.clear();
		}
		if (!StringUtil.isNullOrEmpty(queryCallBackList)) {
			queryCallBackList.clear();
		}
		if (!StringUtil.isNullOrEmpty(agencyQueryList)) {
			agencyQueryList.clear();
		}
		if (!StringUtil.isNullOrEmpty(accountInfoMap)) {
			accountInfoMap.clear();
		}
		if (!StringUtil.isNullOrEmpty(queryResultMap)) {
			queryResultMap.clear();
		}
		if (!StringUtil.isNullOrEmpty(drawInfoMap)) {
			drawInfoMap.clear();
		}
	}
}
