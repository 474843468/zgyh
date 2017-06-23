package com.chinamworld.bocmbci.biz.dept;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.chinamworld.bocmbci.utils.StringUtil;

public class DeptDataCenter {

	private static DeptDataCenter dataCenter;
	/** 所有账户列表 */
	private List<Map<String, Object>> accountList;
	/** 我要存定期 转出列表 */
	private List<Map<String, Object>> accountOutList;
	/** 我要存定期 转人列表 */
	private List<Map<String, Object>> accountInList;
	/** 当前选择的账户 */
	private Map<String, Object> accountContent;
	/** 所有账户详情 */
	private List<Map<String, Object>> accountDetailList;
	/** 当前选择的账户详情 */
	private Map<String, Object> curDetailContent;
	/** 转出账户详情 */
	private Map<String, Object> accOutInfoMap;
	/** 转入账户详情 */
	private Map<String, Object> accInInfoMap;
	/** 新增定期存款 和 新增定期存款 返回数据 */
	private Map<String, Object> requestCallBackMap;
	/** 新增通知存款 返回数据 */
	private Map<String, Object> requestNotifySaveCallBackMap;
	/** 查看定期详情 返回数据 */
	private Map<String, Object> requestMyRegDetailCallBackMap;
	/** 账户详细返回数据 */
	private Map<String, Object> accountDetailCallBackMap;
	/** 支取返回 数据 */
	private Map<String, Object> checkoutCallBackMap;
	/** 查询通知返回数据 */
	private List<Map<String, Object>> queryNotifyCallBackList;
	/** 定期一本通账户列表 */
	private List<Map<String, Object>> wholesaveAccountList;
	/** 教育储蓄账户列表 */
	private List<Map<String, Object>> educationAccountList;
	/** 零存整取账户列表 */
	private List<Map<String, Object>> zerosaveAccountList;
	/** 我要存定期 转出账户列表 */
	private List<Map<String, Object>> tranoutAccountList;
	/** 我要存定期 列表 */
	private List<Map<String, Object>> myRegAccountList;
	/** 通知编号列表 */
	private List<String> noticeIdList;
	/** 查询通知详情 选择通知编号后 当前条目详情 */
	private Map<String, Object> curNotifyDetail;
	/** 大额存单签约账号 */
	private Map<String, Object> signedAcc;
	/** 可购买存单详情 */
	private Map<String, Object> availableDetial;
	/** 已购买存单详情 */
	private Map<String, Object> purchasedDetail;
	/** 已购买存单交易明细 */
	private Map<String, Object> purchasedTransDetail;
	/** 整存整取手费用试算 */
	private Map<String, Object> costCalculationMap;
	/** 定活两便手费用试算*/
	private Map<String, Object> regulaRandomMap;
	/** 七天&一天通知存款*/
	private Map<String, Object> notificationMap;
	/** 支取整存整取费用试算*/
	private Map<String, Object> drawMoneyMap;
	/** 支取定活两便费用试算*/
	private Map<String, Object> drawFixedMoneyMap;
	/** 零存整取*/
	private Map<String, Object> zeroDepositMap;
	/** 教育储存*/
	private Map<String, Object> educationStockpileMap;
	/** 通知存款*/
	private Map<String, Object> informMoneyMap;
		/**步步高*/
	private Map<String, Object> bbkMap;
	/**聚财通*/
	private Map<String, Object> enrichmentMap;
	/** 我要存定期 转人账户*/
	private Map<String, Object> accInInfoNewMap;
	/** 大额存单 列表 */
	private List<Map<String, Object>> largeSignAccountList;
	/** 大额存单 选中项列表*/
	private Map<String, Object> largeSignSelectListMap;
	/** 大额存单 签约成功*/
	private Map<String, Object> largeSignSuccessMap;
	/** 整存整取手费用试算  利率上浮1.3倍*/
	private Map<String, Object> interestRateFloatingMap;
	/** 整存整取手费用试算  利率上浮1.3倍*/
	private List<Map<String, Object>> largeSelectAccountMap;
	/** 当前选择的账户 用来储存外来模块用户选择的账户 */
	private Map<String, Object> accountContentMap;

	public Map<String, Object> getAccountContentMap() {
		return accountContentMap;
	}

	/**是否来自于银行模块*/
	public boolean isFromBoc = false;

	public void setAccountContentMap(Map<String, Object> accountContentMap) {
		if (null == accountContentMap)
			this.accountContentMap = new HashMap<String, Object>();
		else
			this.accountContentMap = accountContentMap;
	}

	public List<Map<String, Object>> getLargeSelectAccountList() {
		return largeSelectAccountMap;
	}

	public void setLargeSelectAccountList( List<Map<String, Object>> largeSelectAccountMap) {
		if (null == largeSelectAccountMap)
			this.largeSelectAccountMap = new ArrayList<Map<String, Object>>();
		else
			this.largeSelectAccountMap = largeSelectAccountMap;
	}

	public Map<String, Object> getAccInInfoNewMap() {
		return accInInfoNewMap;
	}

	public void setAccInInfoNewMap(Map<String, Object> accInInfoNewMap) {
		if (null == accInInfoNewMap)
			this.accInInfoNewMap = new HashMap<String, Object>();
		else
			this.accInInfoNewMap = accInInfoNewMap;
	}

	public Map<String, Object> getBbkMap() {
		return bbkMap;
	}

	public void setBbkMap(Map<String, Object> bbkMap) {
		this.bbkMap = bbkMap;
	}

	public Map<String, Object> getEnrichmentMap() {
		return enrichmentMap;
	}

	public void setEnrichmentMap(Map<String, Object> enrichmentMap) {
		this.enrichmentMap = enrichmentMap;
	}
	public Map<String, Object> getInterestRateFloatingMap() {
		return interestRateFloatingMap;
	}

	public void setInterestRateFloatingMap(Map<String, Object> interestRateFloatingMap) {
		if (null == interestRateFloatingMap)
			this.interestRateFloatingMap = new HashMap<String, Object>();
		else
			this.interestRateFloatingMap = interestRateFloatingMap;
	}
	
	
	public Map<String, Object> getLargeSignSuccessMap() {
		return largeSignSuccessMap;
	}

	public void setLargeSignSuccessMap(Map<String, Object> largeSignSuccessMap) {
		if(null == largeSignSuccessMap){
			this.largeSignSuccessMap = new HashMap<String, Object>();
		}else{
			this.largeSignSuccessMap = largeSignSuccessMap;
		}
	}

	public List<Map<String, Object>> getLargeSignAccountList() {
		return largeSignAccountList;
	}

	public void setLargeSignAccountList(List<Map<String, Object>> largeSignAccountList) {
		if (null == largeSignAccountList)
			this.largeSignAccountList = new ArrayList<Map<String, Object>>();
		else
			this.largeSignAccountList = largeSignAccountList;
	}

	public Map<String, Object> getLargeSignSelectListMap() {
		return largeSignSelectListMap;
	}

	public void setLargeSignSelectListMap(Map<String, Object> largeSignSelectListMap) {
		if (null == largeSignSelectListMap)
			this.largeSignSelectListMap = new HashMap<String, Object>();
		else
			this.largeSignSelectListMap = largeSignSelectListMap;
	}

	public Map<String, Object> getInformMoneyMap() {
		return informMoneyMap;
	}

	public void setInformMoneyMap(Map<String, Object> informMoneyMap) {
		if (null == informMoneyMap)
			this.informMoneyMap = new HashMap<String, Object>();
		else
			this.informMoneyMap = informMoneyMap;
	}

	public Map<String, Object> getDrawFixedMoneyMap() {
		return drawFixedMoneyMap;
	}

	public void setDrawFixedMoneyMap(Map<String, Object> drawFixedMoneyMap) {
		if (null == drawFixedMoneyMap)
			this.drawFixedMoneyMap = new HashMap<String, Object>();
		else
			this.drawFixedMoneyMap = drawFixedMoneyMap;
	}

	public Map<String, Object> getEducationStockpileMap() {
		return educationStockpileMap;
	}

	public void setEducationStockpileMap(Map<String, Object> educationStockpileMap) {
		if (null == educationStockpileMap)
			this.educationStockpileMap = new HashMap<String, Object>();
		else
			this.educationStockpileMap = educationStockpileMap;
	}

	public Map<String, Object> getZeroDepositMap() {
		return zeroDepositMap;
	}

	public void setZeroDepositMap(Map<String, Object> zeroDepositMap) {
		if (null == zeroDepositMap)
			this.zeroDepositMap = new HashMap<String, Object>();
		else
			this.zeroDepositMap = zeroDepositMap;
	}

	public Map<String, Object> getDrawMoneyMap() {
		return drawMoneyMap;
	}

	public void setDrawMoneyMap(Map<String, Object> drawMoneyMap) {
		if (null == drawMoneyMap)
			this.drawMoneyMap = new HashMap<String, Object>();
		else
			this.drawMoneyMap = drawMoneyMap;
	}

	public Map<String, Object> getNotificationMap() {
		return notificationMap;
	}

	public void setNotificationMap(Map<String, Object> notificationMap) {
		if (null == notificationMap)
			this.notificationMap = new HashMap<String, Object>();
		else
			this.notificationMap = notificationMap;
	}

	private Map<String, Object> fixedMap;
	
	
	
	public Map<String, Object> getRegulaRandomMap() {
		return regulaRandomMap;
	}

	public void setRegulaRandomMap(Map<String, Object> regulaRandomMap) {
		if (null == regulaRandomMap)
			this.regulaRandomMap = new HashMap<String, Object>();
		else
			this.regulaRandomMap = regulaRandomMap;
	}

	public Map<String, Object> getFixedMap() {
		return fixedMap;
	}

	public void setFixedMap(Map<String, Object> fixedMap) {
		if (null == fixedMap)
			this.fixedMap = new HashMap<String, Object>();
		else
			this.fixedMap = fixedMap;
	}

	public Map<String, Object> getCostCalculationMap() {
		return costCalculationMap;
	}

	public void setCostCalculationMap(Map<String, Object> costCalculationMap) {
		if (null == costCalculationMap)
			this.costCalculationMap = new HashMap<String, Object>();
		else
			this.costCalculationMap = costCalculationMap;
	}

	private DeptDataCenter() {

	}

	public static DeptDataCenter getInstance() {
		if (dataCenter == null) {
			dataCenter = new DeptDataCenter();
		}
		return dataCenter;
	}

	public Map<String, Object> getAccOutInfoMap() {
		return accOutInfoMap;
	}

	public void setAccOutInfoMap(Map<String, Object> accOutInfoMap) {

		if (null == accOutInfoMap)
			this.accOutInfoMap = new HashMap<String, Object>();
		else
			this.accOutInfoMap = accOutInfoMap;
	}

	public Map<String, Object> getAccInInfoMap() {
		return accInInfoMap;
	}

	public void setAccInInfoMap(Map<String, Object> accInInfoMap) {
		if (null == accInInfoMap)
			this.accInInfoMap = new HashMap<String, Object>();
		else
			this.accInInfoMap = accInInfoMap;
	}

	public Map<String, Object> getRequestCallBackMap() {
		return requestCallBackMap;
	}

	public void setRequestCallBackMap(Map<String, Object> requestCallBackMap) {
		if (null == requestCallBackMap)
			this.requestCallBackMap = new HashMap<String, Object>();
		else
			this.requestCallBackMap = requestCallBackMap;
	}

	public Map<String, Object> getRequestNotifySaveCallBackMap() {
		return requestNotifySaveCallBackMap;
	}

	public void setRequestNotifySaveCallBackMap(Map<String, Object> requestNotifySaveCallBackMap) {
		if (null == requestNotifySaveCallBackMap)
			this.requestNotifySaveCallBackMap = new HashMap<String, Object>();
		else
			this.requestNotifySaveCallBackMap = requestNotifySaveCallBackMap;
	}

	public Map<String, Object> getRequestMyRegDetailCallBackMap() {
		return requestMyRegDetailCallBackMap;
	}

	public void setRequestMyRegDetailCallBackMap(Map<String, Object> requestMyRegDetailCallBackMap) {
		if (null == requestMyRegDetailCallBackMap)
			this.requestMyRegDetailCallBackMap = new HashMap<String, Object>();
		else
			this.requestMyRegDetailCallBackMap = requestMyRegDetailCallBackMap;
	}

	public List<Map<String, Object>> getAccountList() {
		return accountList;
	}

	public void setAccountList(List<Map<String, Object>> accountList) {
		if (null == accountList)
			this.accountList = new ArrayList<Map<String, Object>>();
		else
			this.accountList = accountList;
	}

	public Map<String, Object> getAccountDetailCallBackMap() {
		return accountDetailCallBackMap;
	}

	public void setAccountDetailCallBackMap(Map<String, Object> accountDetailCallBackMap) {
		if (null == accountDetailCallBackMap)
			this.accountDetailCallBackMap = new HashMap<String, Object>();
		else
			this.accountDetailCallBackMap = accountDetailCallBackMap;
	}

	public List<Map<String, Object>> getAccountDetailList() {
		return accountDetailList;
	}

	public void setAccountDetailList(List<Map<String, Object>> accountDetailList) {

		if (null == accountDetailList)
			this.accountDetailList = new ArrayList<Map<String, Object>>();
		else
			this.accountDetailList = accountDetailList;
	}

	public Map<String, Object> getCurDetailContent() {
		return curDetailContent;
	}

	public void setCurDetailContent(Map<String, Object> curDetailContent) {
		if (null == curDetailContent)
			this.curDetailContent = new HashMap<String, Object>();
		else
			this.curDetailContent = curDetailContent;
	}

	public Map<String, Object> getAccountContent() {
		return accountContent;
	}

	public void setAccountContent(Map<String, Object> accountContent) {
		if (null == accountContent)
			this.accountContent = new HashMap<String, Object>();
		else
			this.accountContent = accountContent;
	}

	public Map<String, Object> getCheckoutCallBackMap() {
		return checkoutCallBackMap;
	}

	public void setCheckoutCallBackMap(Map<String, Object> checkoutCallBackMap) {

		if (null == checkoutCallBackMap)
			this.checkoutCallBackMap = new HashMap<String, Object>();
		else
			this.checkoutCallBackMap = checkoutCallBackMap;
	}

	public List<Map<String, Object>> getQueryNotifyCallBackList() {
		return queryNotifyCallBackList;
	}

	public void setQueryNotifyCallBackList(List<Map<String, Object>> queryNotifyCallBackMap) {
		if (null == queryNotifyCallBackMap)
			this.queryNotifyCallBackList = new ArrayList<Map<String, Object>>();
		else
			this.queryNotifyCallBackList = queryNotifyCallBackMap;
	}

	public List<Map<String, Object>> getWholesaveAccountList() {
		return wholesaveAccountList;
	}

	public void setWholesaveAccountList(List<Map<String, Object>> wholesaveAccountList) {

		if (null == wholesaveAccountList)
			this.wholesaveAccountList = new ArrayList<Map<String, Object>>();
		else
			this.wholesaveAccountList = wholesaveAccountList;
	}

	public List<Map<String, Object>> getTranoutAccountList() {
		return tranoutAccountList;
	}

	public void setTranoutAccountList(List<Map<String, Object>> tranoutAccountList) {
		if (null == tranoutAccountList)
			this.tranoutAccountList = new ArrayList<Map<String, Object>>();
		else
			this.tranoutAccountList = tranoutAccountList;
	}

	public List<Map<String, Object>> getEducationAccountList() {
		return educationAccountList;
	}

	public void setEducationAccountList(List<Map<String, Object>> educationAccountList) {
		if (null == educationAccountList)
			this.educationAccountList = new ArrayList<Map<String, Object>>();
		else
			this.educationAccountList = educationAccountList;
	}

	public List<Map<String, Object>> getZerosaveAccountList() {
		return zerosaveAccountList;
	}

	public void setZerosaveAccountList(List<Map<String, Object>> zerosaveAccountList) {
		if (null == zerosaveAccountList)
			this.zerosaveAccountList = new ArrayList<Map<String, Object>>();
		else
			this.zerosaveAccountList = zerosaveAccountList;
	}

	public List<String> getNoticeIdList() {
		return noticeIdList;
	}

	public void setNoticeIdList(List<String> noticeIdList) {
		if (null == noticeIdList)
			this.noticeIdList = new ArrayList<String>();
		else
			this.noticeIdList = noticeIdList;
	}

	public Map<String, Object> getCurNotifyDetail() {
		return curNotifyDetail;
	}

	public void setCurNotifyDetail(Map<String, Object> curNotifyDetail) {
		if (null == curNotifyDetail)
			this.curNotifyDetail = new HashMap<String, Object>();
		else
			this.curNotifyDetail = curNotifyDetail;
	}

	public List<Map<String, Object>> getAccountOutList() {
		return accountOutList;
	}

	public void setAccountOutList(List<Map<String, Object>> accountOutList) {
		if (null == accountOutList)
			this.accountOutList = new ArrayList<Map<String, Object>>();
		else
			this.accountOutList = accountOutList;
	}

	public List<Map<String, Object>> getAccountInList() {
		return accountInList;
	}

	public void setAccountInList(List<Map<String, Object>> accountInList) {
		if (null == accountInList)
			this.accountInList = new ArrayList<Map<String, Object>>();
		else
			this.accountInList = accountInList;
	}

	public List<Map<String, Object>> getMyRegAccountList() {
		return myRegAccountList;
	}

	public void setMyRegAccountList(List<Map<String, Object>> myRegAccountList) {
		if (null == myRegAccountList)
			this.myRegAccountList = new ArrayList<Map<String, Object>>();
		else
			this.myRegAccountList = myRegAccountList;
	}

	public Map<String, Object> getSignedAcc() {
		return signedAcc;
	}

	public void setSignedAcc(Map<String, Object> signedAcc) {
		if (null == signedAcc) {
			this.signedAcc = new HashMap<String, Object>();
		} else {
			this.signedAcc = signedAcc;
		}
	}

	public Map<String, Object> getAvailableDetial() {
		return availableDetial;
	}

	public void setAvailableDetial(Map<String, Object> availableDetial) {
		if (null == availableDetial) {
			this.availableDetial = new HashMap<String, Object>();
		} else {
			this.availableDetial = availableDetial;
		}
	}

	public Map<String, Object> getPurchasedDetail() {
		return purchasedDetail;
	}

	public void setPurchasedDetail(Map<String, Object> purchasedDetail) {
		if (null == purchasedDetail) {
			this.purchasedDetail = new HashMap<String, Object>();
		} else {
			this.purchasedDetail = purchasedDetail;
		}
	}

	public Map<String, Object> getPurchasedTransDetail() {
		return purchasedTransDetail;
	}

	public void setPurchasedTransDetail(Map<String, Object> purchasedTransDetail) {
		if (null == purchasedTransDetail) {
			this.purchasedTransDetail = new HashMap<String, Object>();
		} else {
			this.purchasedTransDetail = purchasedTransDetail;
		}
	}

	/**
	 * 清楚datacenter所有数据
	 */
	public void clearDeptData() {
		if (!StringUtil.isNullOrEmpty(accountList)) {
			accountList.clear();
		}
		if (!StringUtil.isNullOrEmpty(accountOutList)) {
			accountOutList.clear();
		}
		if (!StringUtil.isNullOrEmpty(accountInList)) {
			accountInList.clear();
		}
		if (!StringUtil.isNullOrEmpty(accountContent)) {
			accountContent.clear();
		}
		if (!StringUtil.isNullOrEmpty(accountDetailList)) {
			accountDetailList.clear();
		}
		if (!StringUtil.isNullOrEmpty(curDetailContent)) {
			curDetailContent.clear();
		}
		if (!StringUtil.isNullOrEmpty(accOutInfoMap)) {
			accOutInfoMap.clear();
		}
		if (!StringUtil.isNullOrEmpty(accInInfoMap)) {
			accInInfoMap.clear();
		}
		if (!StringUtil.isNullOrEmpty(requestCallBackMap)) {
			requestCallBackMap.clear();
		}
		if (!StringUtil.isNullOrEmpty(requestNotifySaveCallBackMap)) {
			requestNotifySaveCallBackMap.clear();
		}
		if (!StringUtil.isNullOrEmpty(requestMyRegDetailCallBackMap)) {
			requestMyRegDetailCallBackMap.clear();
		}
		if (!StringUtil.isNullOrEmpty(accountDetailCallBackMap)) {
			accountDetailCallBackMap.clear();
		}
		if (!StringUtil.isNullOrEmpty(checkoutCallBackMap)) {
			checkoutCallBackMap.clear();
		}
		if (!StringUtil.isNullOrEmpty(queryNotifyCallBackList)) {
			queryNotifyCallBackList.clear();
		}
		if (!StringUtil.isNullOrEmpty(wholesaveAccountList)) {
			wholesaveAccountList.clear();
		}
		if (!StringUtil.isNullOrEmpty(educationAccountList)) {
			educationAccountList.clear();
		}
		if (!StringUtil.isNullOrEmpty(zerosaveAccountList)) {
			zerosaveAccountList.clear();
		}
		if (!StringUtil.isNullOrEmpty(tranoutAccountList)) {
			tranoutAccountList.clear();
		}
		if (!StringUtil.isNullOrEmpty(myRegAccountList)) {
			myRegAccountList.clear();
		}
		if (!StringUtil.isNullOrEmpty(noticeIdList)) {
			noticeIdList.clear();
		}
		if (!StringUtil.isNullOrEmpty(curNotifyDetail)) {
			curNotifyDetail.clear();
		}
		if (!StringUtil.isNullOrEmpty(signedAcc)) {
			signedAcc.clear();
		}
		if (!StringUtil.isNullOrEmpty(availableDetial)) {
			availableDetial.clear();
		}
		if (!StringUtil.isNullOrEmpty(purchasedDetail)) {
			purchasedDetail.clear();
		}
		if (!StringUtil.isNullOrEmpty(purchasedTransDetail)) {
			purchasedTransDetail.clear();
		}
		if (!StringUtil.isNullOrEmpty(costCalculationMap)) {
			costCalculationMap.clear();
		}
		if (!StringUtil.isNullOrEmpty(fixedMap)) {
			fixedMap.clear();
		}
		if (!StringUtil.isNullOrEmpty(regulaRandomMap)) {
			regulaRandomMap.clear();
		}
		if (!StringUtil.isNullOrEmpty(notificationMap)) {
			notificationMap.clear();
		}
		if (!StringUtil.isNullOrEmpty(drawMoneyMap)) {
			drawMoneyMap.clear();
		}
		if (!StringUtil.isNullOrEmpty(zeroDepositMap)) {
			zeroDepositMap.clear();
		}
		if (!StringUtil.isNullOrEmpty(educationStockpileMap)) {
			educationStockpileMap.clear();
		}
		if (!StringUtil.isNullOrEmpty(drawFixedMoneyMap)) {
			drawFixedMoneyMap.clear();
		}
		if (!StringUtil.isNullOrEmpty(informMoneyMap)) {
			informMoneyMap.clear();
		}
		if (!StringUtil.isNullOrEmpty(bbkMap)) {
			bbkMap.clear();
		}
		if (!StringUtil.isNullOrEmpty(enrichmentMap)) {
			enrichmentMap.clear();
		}
		if (!StringUtil.isNullOrEmpty(accInInfoNewMap)) {
			accInInfoNewMap.clear();
		}
		if (!StringUtil.isNullOrEmpty(largeSignAccountList)) {
			largeSignAccountList.clear();
		}
		if (!StringUtil.isNullOrEmpty(largeSignSelectListMap)) {
			largeSignSelectListMap.clear();
		}
		if (!StringUtil.isNullOrEmpty(largeSignSuccessMap)) {
			largeSignSuccessMap.clear();
		}
		if (!StringUtil.isNullOrEmpty(interestRateFloatingMap)) {
			interestRateFloatingMap.clear();
		}
		if (!StringUtil.isNullOrEmpty(largeSelectAccountMap)) {
			largeSelectAccountMap.clear();
		}
		if (!StringUtil.isNullOrEmpty(accountContentMap)){
			accountContentMap.clear();
		}
	}
}
