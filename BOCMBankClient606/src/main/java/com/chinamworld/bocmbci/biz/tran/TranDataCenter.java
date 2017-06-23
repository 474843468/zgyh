package com.chinamworld.bocmbci.biz.tran;

import java.util.List;
import java.util.Map;

import android.widget.TextView;

import com.chinamworld.bocmbci.utils.Dictionary;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 转账管理数据中心
 * 
 * @author WJP
 * 
 */
public class TranDataCenter {
	private static TranDataCenter dataCenter;
	/** 转出账户列表 */
	private List<Map<String, Object>> accountOutList;
	/** 转入账户列表 */
	private List<Map<String, Object>> accountInList;
	/** 转入新添加信用卡列表 */
	private List<Map<String, Object>> crcdList;
	/** 转入新添加网上专属理财列表 */
	private List<Map<String, Object>> bocinvtList;
	/** 当前选择转出账户 */
	private Map<String, Object> accOutInfoMap;
	/** 当前选择转入账户 */
	private Map<String, Object> accInInfoMap;
	/** 当前转出账户详情 */
	private Map<String, Object> currOutDetail;
	/** 当前转入账户详情 */
	private Map<String, Object> currInDetail;
	/** 常用收款人列表 */
	private List<Map<String, Object>> commAccInList;
	/** 用户输入信息 */
	private Map<String, String> userInputMap;
	/** 手续费试算返回数据 */
	private Map<String, Object> CommissionChargeMap;
	/** 关联账户转账返回数据 */
	private Map<String, Object> relTranCallBackMap;
	/** 关联信用卡还款返回数据 */
	private Map<String, Object> relCrcdRepayCallBackMap;
	/** 信用卡查询购汇还款信息返回数据 */
	private Map<String, Object> relCrcdBuyCallBackMap;
	/** 信用卡购汇还款处理返回数据 */
	private Map<String, Object> relCrcdBuyDealCallBackMap;
	/** 非关联信用卡还款预交易返回数据 */
	private Map<String, Object> noRelCrcdRepayCallBackMap;
	/** 非关联信用卡还款提交返回数据 */
	private Map<String, Object> noRelCrcdRepaySubmitCallBackMap;
	/** 非关联行内转账预交易返回数据 */
	private Map<String, Object> noRelBankInCallBackMap;
	/** 非关联行内转账交易返回数据 */
	private Map<String, Object> noRelBankInDealCallBackMap;
	/** 非关联跨行预交易数据返回 */
	private Map<String, Object> noRelBankOtherCallBackMap;
	/** 非关联跨行交易数据返回 */
	private Map<String, Object> noRelBankOtherDealCallBackMap;
	/** 查询转账开户行数据返回 */
	private List<Map<String, String>> externalBankList;

	// 转账管理
	/** 预约交易详情查询数据返回 */
	private Map<String, Object> queryDetailCallBackMap;
	/** 查询类型 */
	private int queryType;
	/** 收款人列表 */
	private List<Map<String, Object>> QueryPayeeList;
	/** 当前选择收款人 */
	private Map<String, Object> curPayeeMap;

	// 手机号转账
	/** 手机号预交易返回 */
	private Map<String, Object> mobileTranCallBackMap;
	/** 手机号交易提交返回 */
	private Map<String, Object> mobileTranDealCallBackMap;
	/** 手机号转账记录单条详情 */
	private Map<String, Object> mobileTransDetailMap;
	/** 存储模块标识 */
	private int moduleType;
	/** 国内跨行转账非工作时间转预约 */
	private Map<String, Object> nationalChangeBookingMap;

	/** 401 */
	private Map<String, Object> atmChooseMap;
	private Map<String, Object> atmpremap;
	private List<Map<String, Object>> accountList;

	/** 402 */
	/** 汇款套餐共享账户*/
	private List<Map<String, String>> shareAccountList;
	/** 汇款套餐交易数据*/
	private Map<String, Object> shareInputMap;
	/*** 汇款套餐预交易数据 */
	private Map<String, Object> remitPreMap;
	private Map<String, Object> shareQueryMap;

	/** 汇款笔数套餐----套餐类型(显示内容|上送字段|服务器返回数据) */
	private Dictionary<String, String,  Map<String, Object>> mealTypeResDic;
	/** T43 */
	/**
	 * 定向收款人列表
	 */
	private List<Map<String, Object>> dirpayeeList;
	/** 查询实时转账所属银行数据返回 */
	private List<Map<String, String>> shishiBankList;
	/** 实时定向收款人列表 */
	private List<Map<String, Object>> ebpsDirList;
	/** 实时收款人列表 */
	private List<Map<String, Object>> ebpsList;

	/** bankCodeList*/
	private List<String> bankCodeList;
	
	
	/** 405网上专属理财账户绑定查询 */
	private Map<String, Object> accmap;

	/**倒计时通讯框显示文本*/
	private TextView textview;
	/**倒计总时间*/
	private int timer = 20;
	/**倒计变动时间*/
	private int timerChange = 20;
	
	
	/**信用卡综合信息*/
	private Map<String, Object> crcdGeneralInfo;
	
	/**退汇信息*/
	private Map<String, Object> RemitReturnInfo;
	
	
	public Map<String, Object> getRemitReturnInfo() {
		return RemitReturnInfo;
	}



	public void setRemitReturnInfo(Map<String, Object> remitReturnInfo) {
		RemitReturnInfo = remitReturnInfo;
	}

	public Map<String, Object> getCrcdGeneralInfo() {
		return crcdGeneralInfo;
	}

	public void setCrcdGeneralInfo(Map<String, Object> crcdGeneralInfo) {
		this.crcdGeneralInfo = crcdGeneralInfo;
	}

	public int getTimerChange() {
		return timerChange;
	}

	public void setTimerChange(int timerChange) {
		this.timerChange = timerChange;
	}

	/**倒计时间隔 时间*/
	private int timerInterval = 20;
	public int getTimerInterval() {
		return timerInterval;
	}

	public void setTimerInterval(int timerInterval) {
		this.timerInterval = timerInterval;
	}

	/**实时跨行转账 关闭dialogue 进入完成 界面*/
	private boolean colseDialog = false;
	public boolean getColseDialog() {
		return colseDialog;
	}

	public void setColseDialog(boolean colseDialog) {
		this.colseDialog = colseDialog;
	}

	public int getTimer() {
		return timer;
	}

	public void setTimer(int timer) {
		this.timer = timer;
	}

	public TextView getTextview() {
		return textview;
	}

	public void setTextview(TextView textview) {
		this.textview = textview;
	}


	private TranDataCenter() {

	}

	public static TranDataCenter getInstance() {
		if (dataCenter == null) {
			dataCenter = new TranDataCenter();
		}
		return dataCenter;
	}

	public List<Map<String, Object>> getAccountOutList() {
		return accountOutList;
	}

	public void setAccountOutList(List<Map<String, Object>> accountOutList) {
		this.accountOutList = accountOutList;
	}

	public Map<String, Object> getAccOutInfoMap() {
		return accOutInfoMap;
	}

	public void setAccOutInfoMap(Map<String, Object> accOutInfoMap) {
		this.accOutInfoMap = accOutInfoMap;
	}

	public Map<String, Object> getAccInInfoMap() {
		return accInInfoMap;
	}

	public void setAccInInfoMap(Map<String, Object> accInInfoMap) {
		this.accInInfoMap = accInInfoMap;
	}

	public Map<String, Object> getCurrOutDetail() {
		return currOutDetail;
	}

	public void setCurrOutDetail(Map<String, Object> currOutDetail) {
		this.currOutDetail = currOutDetail;
	}

	public Map<String, Object> getCurrInDetail() {
		return currInDetail;
	}

	public void setCurrInDetail(Map<String, Object> currInDetail) {
		this.currInDetail = currInDetail;
	}

	public List<Map<String, Object>> getCommAccInList() {
		return commAccInList;
	}

	public void setCommAccInList(List<Map<String, Object>> commAccInList) {
		this.commAccInList = commAccInList;
	}

	public List<Map<String, Object>> getAccountInList() {
		return accountInList;
	}

	public void setAccountInList(List<Map<String, Object>> accountInList) {
		this.accountInList = accountInList;
	}

	public Map<String, String> getUserInputMap() {
		return userInputMap;
	}

	public void setUserInputMap(Map<String, String> userInputMap) {
		this.userInputMap = userInputMap;
	}

	public Map<String, Object> getCommissionChargeMap() {
		return CommissionChargeMap;
	}

	public void setCommissionChargeMap(Map<String, Object> commissionChargeMap) {
		CommissionChargeMap = commissionChargeMap;
	}

	public Map<String, Object> getRelTranCallBackMap() {
		return relTranCallBackMap;
	}

	public void setRelTranCallBackMap(Map<String, Object> relTranCallBackMap) {
		this.relTranCallBackMap = relTranCallBackMap;
	}

	public Map<String, Object> getRelCrcdRepayCallBackMap() {
		return relCrcdRepayCallBackMap;
	}

	public void setRelCrcdRepayCallBackMap(
			Map<String, Object> relCrcdRepayCallBackMap) {
		this.relCrcdRepayCallBackMap = relCrcdRepayCallBackMap;
	}

	public Map<String, Object> getRelCrcdBuyCallBackMap() {
		return relCrcdBuyCallBackMap;
	}

	public void setRelCrcdBuyCallBackMap(
			Map<String, Object> relCrcdBuyCallBackMap) {
		this.relCrcdBuyCallBackMap = relCrcdBuyCallBackMap;
	}

	public Map<String, Object> getRelCrcdBuyDealCallBackMap() {
		return relCrcdBuyDealCallBackMap;
	}

	public void setRelCrcdBuyDealCallBackMap(
			Map<String, Object> relCrcdBuyDealCallBackMap) {
		this.relCrcdBuyDealCallBackMap = relCrcdBuyDealCallBackMap;
	}

	public Map<String, Object> getNoRelCrcdRepayCallBackMap() {
		return noRelCrcdRepayCallBackMap;
	}

	public Map<String, Object> getNoRelBankInDealCallBackMap() {
		return noRelBankInDealCallBackMap;
	}

	public void setNoRelBankInDealCallBackMap(
			Map<String, Object> noRelBankInDealCallBackMap) {
		this.noRelBankInDealCallBackMap = noRelBankInDealCallBackMap;
	}

	public void setNoRelCrcdRepayCallBackMap(
			Map<String, Object> noRelCrcdRepayCallBackMap) {
		this.noRelCrcdRepayCallBackMap = noRelCrcdRepayCallBackMap;
	}

	public Map<String, Object> getNoRelCrcdRepaySubmitCallBackMap() {
		return noRelCrcdRepaySubmitCallBackMap;
	}

	public void setNoRelCrcdRepaySubmitCallBackMap(
			Map<String, Object> noRelCrcdRepaySubmitCallBackMap) {
		this.noRelCrcdRepaySubmitCallBackMap = noRelCrcdRepaySubmitCallBackMap;
	}

	public Map<String, Object> getNoRelBankInCallBackMap() {
		return noRelBankInCallBackMap;
	}

	public void setNoRelBankInCallBackMap(
			Map<String, Object> noRelBankInCallBackMap) {
		this.noRelBankInCallBackMap = noRelBankInCallBackMap;
	}

	public Map<String, Object> getNoRelBankOtherCallBackMap() {
		return noRelBankOtherCallBackMap;
	}

	public void setNoRelBankOtherCallBackMap(
			Map<String, Object> noRelBankOtherCallBackMap) {
		this.noRelBankOtherCallBackMap = noRelBankOtherCallBackMap;
	}

	public Map<String, Object> getNoRelBankOtherDealCallBackMap() {
		return noRelBankOtherDealCallBackMap;
	}

	public void setNoRelBankOtherDealCallBackMap(
			Map<String, Object> noRelBankOtherDealCallBackMap) {
		this.noRelBankOtherDealCallBackMap = noRelBankOtherDealCallBackMap;
	}

	public List<Map<String, String>> getExternalBankList() {
		return externalBankList;
	}

	public void setExternalBankList(List<Map<String, String>> externalBankList) {
		this.externalBankList = externalBankList;
	}

	public Map<String, Object> getQueryDetailCallBackMap() {
		return queryDetailCallBackMap;
	}

	public void setQueryDetailCallBackMap(
			Map<String, Object> queryDetailCallBackMap) {
		this.queryDetailCallBackMap = queryDetailCallBackMap;
	}

	public int getQueryType() {
		return queryType;
	}

	public void setQueryType(int queryType) {
		this.queryType = queryType;
	}

	public List<Map<String, Object>> getQueryPayeeList() {
		return QueryPayeeList;
	}

	public void setQueryPayeeList(List<Map<String, Object>> queryPayeeList) {
		QueryPayeeList = queryPayeeList;
	}

	public Map<String, Object> getCurPayeeMap() {
		return curPayeeMap;
	}

	public void setCurPayeeMap(Map<String, Object> curPayeeMap) {
		this.curPayeeMap = curPayeeMap;
	}

	public Map<String, Object> getMobileTranCallBackMap() {
		return mobileTranCallBackMap;
	}

	public void setMobileTranCallBackMap(
			Map<String, Object> mobileTranCallBackMap) {
		this.mobileTranCallBackMap = mobileTranCallBackMap;
	}

	public Map<String, Object> getMobileTranDealCallBackMap() {
		return mobileTranDealCallBackMap;
	}

	public void setMobileTranDealCallBackMap(
			Map<String, Object> mobileTranDealCallBackMap) {
		this.mobileTranDealCallBackMap = mobileTranDealCallBackMap;
	}

	public int getModuleType() {
		return moduleType;
	}

	public void setModuleType(int moduleType) {
		this.moduleType = moduleType;
	}

	
	
	public List< String> getBankCodeList() {
		return bankCodeList;
	}

	public void setBankCodeList(List<String> bankCodeList) {
		this.bankCodeList = bankCodeList;
	}

	/**
	 * 清楚datacenter所有数据
	 */
	public void clearTranData() {
		if (!StringUtil.isNullOrEmpty(accountOutList)) {
			accountOutList.clear();
		}
		if (!StringUtil.isNullOrEmpty(accountInList)) {
			accountInList.clear();
		}
		if (!StringUtil.isNullOrEmpty(accOutInfoMap)) {
			accOutInfoMap.clear();
		}
		if (!StringUtil.isNullOrEmpty(accInInfoMap)) {
			accInInfoMap.clear();
		}
		if (!StringUtil.isNullOrEmpty(currOutDetail)) {
			currOutDetail.clear();
		}
		if (!StringUtil.isNullOrEmpty(currInDetail)) {
			currInDetail.clear();
		}
		if (!StringUtil.isNullOrEmpty(commAccInList)) {
			commAccInList.clear();
		}
		if (!StringUtil.isNullOrEmpty(userInputMap)) {
			userInputMap.clear();
		}
		if (!StringUtil.isNullOrEmpty(CommissionChargeMap)) {
			CommissionChargeMap.clear();
		}
		if (!StringUtil.isNullOrEmpty(relTranCallBackMap)) {
			relTranCallBackMap.clear();
		}
		if (!StringUtil.isNullOrEmpty(relCrcdRepayCallBackMap)) {
			relCrcdRepayCallBackMap.clear();
		}
		if (!StringUtil.isNullOrEmpty(relCrcdBuyCallBackMap)) {
			relCrcdBuyCallBackMap.clear();
		}
		if (!StringUtil.isNullOrEmpty(relCrcdBuyDealCallBackMap)) {
			relCrcdBuyDealCallBackMap.clear();
		}
		if (!StringUtil.isNullOrEmpty(noRelCrcdRepayCallBackMap)) {
			noRelCrcdRepayCallBackMap.clear();
		}
		if (!StringUtil.isNullOrEmpty(noRelCrcdRepaySubmitCallBackMap)) {
			noRelCrcdRepaySubmitCallBackMap.clear();
		}
		if (!StringUtil.isNullOrEmpty(noRelBankInCallBackMap)) {
			noRelBankInCallBackMap.clear();
		}
		if (!StringUtil.isNullOrEmpty(noRelBankInDealCallBackMap)) {
			noRelBankInDealCallBackMap.clear();
		}
		if (!StringUtil.isNullOrEmpty(noRelBankOtherCallBackMap)) {
			noRelBankOtherCallBackMap.clear();
		}
		if (!StringUtil.isNullOrEmpty(noRelBankOtherDealCallBackMap)) {
			noRelBankOtherDealCallBackMap.clear();
		}
		if (!StringUtil.isNullOrEmpty(externalBankList)) {
			externalBankList.clear();
		}
		if (!StringUtil.isNullOrEmpty(queryDetailCallBackMap)) {
			queryDetailCallBackMap.clear();
		}
		if (!StringUtil.isNullOrEmpty(QueryPayeeList)) {
			QueryPayeeList.clear();
		}
		if (!StringUtil.isNullOrEmpty(curPayeeMap)) {
			curPayeeMap.clear();
		}
		if (!StringUtil.isNullOrEmpty(mobileTranCallBackMap)) {
			mobileTranCallBackMap.clear();
		}
		if (!StringUtil.isNullOrEmpty(mobileTranDealCallBackMap)) {
			mobileTranDealCallBackMap.clear();
		}
		if (!StringUtil.isNullOrEmpty(mobileTransDetailMap)) {
			mobileTransDetailMap.clear();
		}
		if (!StringUtil.isNullOrEmpty(crcdList)) {
			crcdList.clear();
		}
		if (!StringUtil.isNullOrEmpty(nationalChangeBookingMap)) {
			nationalChangeBookingMap.clear();
		}
		if (!StringUtil.isNullOrEmpty(atmChooseMap)) {
			atmChooseMap.clear();
		}
		if (!StringUtil.isNullOrEmpty(atmpremap)) {
			atmpremap.clear();
		}
		if (!StringUtil.isNullOrEmpty(accountList)) {
			accountList.clear();
		}
		if (!StringUtil.isNullOrEmpty(shareAccountList)) {
			shareAccountList.clear();
		}
		if (!StringUtil.isNullOrEmpty(shareInputMap)) {
			shareInputMap.clear();
		}
		if (!StringUtil.isNullOrEmpty(shareQueryMap)) {
			shareQueryMap.clear();
		}
		if (!StringUtil.isNullOrEmpty(dirpayeeList)) {
			dirpayeeList.clear();
		}
		if (!StringUtil.isNullOrEmpty(shishiBankList)) {
			shishiBankList.clear();
		}
		if (!StringUtil.isNullOrEmpty(ebpsDirList)) {
			ebpsDirList.clear();
		}
		if (!StringUtil.isNullOrEmpty(ebpsList)) {
			ebpsList.clear();
		}
		if (!StringUtil.isNullOrEmpty(accmap)) {
			accmap.clear();
		}
		if (!StringUtil.isNullOrEmpty(remitPreMap)) {
			remitPreMap.clear();
		}

		if (!StringUtil.isNullOrEmpty(bankCodeList)) {
			bankCodeList.clear();
		}
		if (!StringUtil.isNullOrEmpty(crcdGeneralInfo)) {
			crcdGeneralInfo.clear();
		}
		if (!StringUtil.isNullOrEmpty(RemitReturnInfo)) {
			RemitReturnInfo.clear();
		}
		

		setModuleType(0);
	}

	public Map<String, Object> getMobileTransDetailMap() {
		return mobileTransDetailMap;
	}

	public void setMobileTransDetailMap(Map<String, Object> mobileTransDetailMap) {
		this.mobileTransDetailMap = mobileTransDetailMap;
	}

	public List<Map<String, Object>> getCrcdList() {
		return crcdList;
	}

	public void setCrcdList(List<Map<String, Object>> crcdList) {
		this.crcdList = crcdList;
	}

	public Map<String, Object> getNationalChangeBookingMap() {
		return nationalChangeBookingMap;
	}

	public void setNationalChangeBookingMap(
			Map<String, Object> nationalChangeBookingMap) {
		this.nationalChangeBookingMap = nationalChangeBookingMap;
	}

	public Map<String, Object> getAtmChooseMap() {
		return atmChooseMap;
	}

	public void setAtmChooseMap(Map<String, Object> atmChooseMap) {
		this.atmChooseMap = atmChooseMap;
	}

	public Map<String, Object> getAtmpremap() {
		return atmpremap;
	}

	public void setAtmpremap(Map<String, Object> atmpremap) {
		this.atmpremap = atmpremap;
	}

	public List<Map<String, Object>> getAccountList() {
		return accountList;
	}

	public void setAccountList(List<Map<String, Object>> accountList) {
		this.accountList = accountList;
	}

	public List<Map<String, String>> getShareAccountList() {
		return shareAccountList;
	}

	public void setShareAccountList(List<Map<String, String>> shareAccountList) {
		this.shareAccountList = shareAccountList;
	}

	public Map<String, Object> getShareInputMap() {
		return shareInputMap;
	}

	public void setShareInputMap(Map<String, Object> shareInputMap) {
		this.shareInputMap = shareInputMap;
	}

	public Map<String, Object> getShareQueryMap() {
		return shareQueryMap;
	}

	public void setShareQueryMap(Map<String, Object> shareQueryMap) {
		this.shareQueryMap = shareQueryMap;
	}

	public List<Map<String, Object>> getDirpayeeList() {
		return dirpayeeList;
	}

	public void setDirpayeeList(List<Map<String, Object>> dirpayeeList) {
		this.dirpayeeList = dirpayeeList;
	}

	public List<Map<String, String>> getShishiBankList() {
		return shishiBankList;
	}

	public void setShishiBankList(List<Map<String, String>> shishiBankList) {
		this.shishiBankList = shishiBankList;
	}

	public List<Map<String, Object>> getEbpsDirList() {
		return ebpsDirList;
	}

	public void setEbpsDirList(List<Map<String, Object>> ebpsDirList) {
		this.ebpsDirList = ebpsDirList;
	}

	public List<Map<String, Object>> getEbpsList() {
		return ebpsList;
	}

	public void setEbpsList(List<Map<String, Object>> ebpsList) {
		this.ebpsList = ebpsList;
	}

	public List<Map<String, Object>> getBocinvtList() {
		return bocinvtList;
	}

	public void setBocinvtList(List<Map<String, Object>> bocinvtList) {
		this.bocinvtList = bocinvtList;
	}

	public Map<String, Object> getAccmap() {
		return accmap;
	}

	public void setAccmap(Map<String, Object> accmap) {
		this.accmap = accmap;
	}

	public Dictionary<String, String,  Map<String, Object>> getMealTypeResDic() {
		return mealTypeResDic;
	}

	public void setMealTypeResDic(Dictionary<String, String,  Map<String, Object>> mealTypeResDic) {
		this.mealTypeResDic = mealTypeResDic;
	}
	
	/**
	 * @Description: 汇款套餐预交易数据
	 */
	public Map<String, Object> getRemitPreMap() {
		return remitPreMap;
	}
	
	/**
	 * @Description: 汇款套餐预交易数据
	 */
	public void setRemitPreMap(Map<String, Object> remitPreMap) {
		this.remitPreMap = remitPreMap;
	}
}
