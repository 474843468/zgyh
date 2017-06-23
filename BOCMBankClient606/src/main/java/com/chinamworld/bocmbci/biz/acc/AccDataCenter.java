package com.chinamworld.bocmbci.biz.acc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.chinamworld.bocmbci.utils.StringUtil;

public class AccDataCenter {

	private static AccDataCenter dataCenter;
	/** 所有账户列表 */
	private List<Map<String, Object>> bankAccountList;
	/** 账户详情信息 */
	private List<Map<String, Object>> accountDetailList;
	/** 临时挂失选择的账户 */
	private Map<String, Object> lossReportMap;
	/** 历史挂失预交易返回 */
	private Map<String, Object> confirmResult;
	/** 账户自助关联预交易返回 */
	private Map<String, Object> relevancePremap;
	/** 账户借记卡自助关联选择账户列表 */
	private List<Map<String, String>> choosefalseDebitList;
	/** 账户借记卡自助关联成功列表 */
	private Map<String, Object> debitlistSuccessMap;
	/** 新建签约选择的账户信息 */
	private Map<String, String> icsignmap;
	/** 请求回来的电子现金账户列表 */
	private List<Map<String, Object>> financeIcAccountList;
	/** 动态口令——新建签约 */
	private String otp;
	/** 手机交易码——新建签约 */
	private String smc;
	/** 加密控件——新建签约 */
	private String otp_password_RC;
	private String smc_password_RC;
	/** 待关联账号 */
	private String acc_relevance_actnum;
	/** 选择的账户详情 */
	private Map<String, Object> chooseBankAccount;
	/** 电子现金账户详情信息 */
	private Map<String, String> callbackmap;
	/** 信用卡 */
	private Map<String, Object> resultDetail;
	private List<Map<String, String>> serviceList;
	/** 币种 */
	public List<String> queryCurrencyList;
	public List<String> queryCodeList;
	public List<List<String>> queryCashRemitList;
	public List<List<String>> queryCashRemitCodeList;
	/** 存款存折册号与存单册号 */
	private ArrayList<Map<String, Object>> volumesAndCdnumbers;
	/** 普通可查询明细账户列表 */
	public List<Map<String, Object>> commCardList;
	/** 403请求回来的医保账户列表 */
	private List<Map<String, Object>> medicalAccountList;
	/** 医保账户详情信息 */
	private Map<String, Object> medicalbackmap;
	/** 是否显示工资单查询标识 */
	private Boolean isPayrollAccount;
	/** 安全工具上传参数 */
	private Map<String, Object> usbparams;
	/** 申请定期活期账户预交易返回结果 */
	private Map<String, Object> applyConfirmResult;
	/** 申请定期活期账户结果 */
	private Map<String, Object> applyResultMap;
	/** 申请定期活期账户:账户用途 */
	private List<String> purposeList;
	/** 申请定期活期账户:在中国开立账户原因 */
	private List<String> reasonList;
	/** 查询个人客户国籍信息 */
	private Map<String, Object> countryCode;
	/** 资产管理 */
	private boolean manageFlag;
	
	private AccDataCenter() {
	}

	public Map<String, Object> getUsbparams() {
		return usbparams;
	}

	public void setUsbparams(Map<String, Object> usbparams) {
		this.usbparams = usbparams;
	}

	public Boolean getIsPayrollAccount() {
		return isPayrollAccount;
	}

	public void setIsPayrollAccount(Boolean isPayrollAccount) {
		this.isPayrollAccount = isPayrollAccount;
	}

	public static AccDataCenter getInstance() {
		if (dataCenter == null) {
			dataCenter = new AccDataCenter();
		}
		return dataCenter;
	}

	public List<Map<String, Object>> getBankAccountList() {
		return bankAccountList;
	}

	public void setBankAccountList(List<Map<String, Object>> bankAccountList) {
		this.bankAccountList = bankAccountList;
	}

	public Map<String, Object> getConfirmResult() {
		return confirmResult;
	}

	public void setConfirmResult(Map<String, Object> confirmResult) {
		this.confirmResult = confirmResult;
	}

	public Map<String, Object> getLossReportMap() {
		return lossReportMap;
	}

	public void setLossReportMap(Map<String, Object> lossReportMap) {
		this.lossReportMap = lossReportMap;
	}

	public Map<String, Object> getRelevancePremap() {
		return relevancePremap;
	}

	public void setRelevancePremap(Map<String, Object> relevancePremap) {
		this.relevancePremap = relevancePremap;
	}

	public List<Map<String, String>> getChoosefalseDebitList() {
		return choosefalseDebitList;
	}

	public void setChoosefalseDebitList(
			List<Map<String, String>> choosefalseDebitList) {
		this.choosefalseDebitList = choosefalseDebitList;
	}

	public Map<String, Object> getDebitlistSuccessMap() {
		return debitlistSuccessMap;
	}

	public void setDebitlistSuccessMap(Map<String, Object> debitlistSuccessMap) {
		this.debitlistSuccessMap = debitlistSuccessMap;
	}

	public String getOtp() {
		return otp;
	}

	public void setOtp(String otp) {
		this.otp = otp;
	}

	public String getSmc() {
		return smc;
	}

	public void setSmc(String smc) {
		this.smc = smc;
	}

	public String getOtp_password_RC() {
		return otp_password_RC;
	}

	public void setOtp_password_RC(String otp_password_RC) {
		this.otp_password_RC = otp_password_RC;
	}

	public String getSmc_password_RC() {
		return smc_password_RC;
	}

	public void setSmc_password_RC(String smc_password_RC) {
		this.smc_password_RC = smc_password_RC;
	}

	public List<Map<String, Object>> getAccountDetailList() {
		return accountDetailList;
	}

	public void setAccountDetailList(List<Map<String, Object>> accountDetailList) {
		this.accountDetailList = accountDetailList;
	}

	public Map<String, String> getIcsignmap() {
		return icsignmap;
	}

	public void setIcsignmap(Map<String, String> icsignmap) {
		this.icsignmap = icsignmap;
	}

	public List<Map<String, Object>> getFinanceIcAccountList() {
		return financeIcAccountList;
	}

	public void setFinanceIcAccountList(
			List<Map<String, Object>> financeIcAccountList) {
		this.financeIcAccountList = financeIcAccountList;
	}

	public String getAcc_relevance_actnum() {
		return acc_relevance_actnum;
	}

	public void setAcc_relevance_actnum(String acc_relevance_actnum) {
		this.acc_relevance_actnum = acc_relevance_actnum;
	}

	public List<Map<String, String>> getServiceList() {
		return serviceList;
	}

	public void setServiceList(List<Map<String, String>> serviceList) {
		this.serviceList = serviceList;
	}

	/** 业务摘要 */
	public final Map<String, String> transferTypeList = new HashMap<String, String>() {
		{
			put("201", "圈存");
			put("404", "圈存");
			put("502", "换卡");
			put("506", "换卡");
			put("514", "补卡");
			put("518", "补卡");
			put("528", "转卡");
			put("530", "转卡");
			put("531", "转卡");
			put("711", "圈存");
			put("721", "圈存");
			put("731", "圈存");
			put("751", "圈存");
			put("760", "圈存");
			put("770", "圈存");
			put("781", "圈提");
			put("782", "圈提");
			put("784", "圈提");
			put("785", "圈提");
			put("790", "消费");
			put("791", "圈存");
			put("803", "圈提");
			put("805", "圈提");
			put("807", "圈提");
			put("809", "圈提");
			put("811", "回收");
			put("812", "调账");
			put("813", "调账");
			put("BTI", "转卡");
			put("BTO", "转卡");
			put("PCS", "消费");
			put("741", "补登");
			put("820", "退货");
			put("821", "退货");
			put("002", "退货");
			put("552", "换卡");
			put("556", "换卡");
			put("560", "换卡");
			put("568", "补卡");
			put("564", "补卡");
			put("572", "补卡");
			put("738", "跨行指定账户圈存");
			put("729", "跨行签约账户圈存");
			put("FEE", "手续费");
			put("799", "其他");
			put("798", "其他");
		}
	};
	
	/**提示信息*/
	public final Map<String,String> messageInfo=new HashMap<String, String>(){
		{
			put("1","一");
			put("2","两");
			put("3","三");
			put("6","六");
		}
	};
	public Map<String, Object> getChooseBankAccount() {
		return chooseBankAccount;
	}

	public void setChooseBankAccount(Map<String, Object> chooseBankAccount) {
		this.chooseBankAccount = chooseBankAccount;
	}

	public Map<String, String> getCallbackmap() {
		return callbackmap;
	}

	public void setCallbackmap(Map<String, String> callbackmap) {
		this.callbackmap = callbackmap;
	}

	public Map<String, Object> getResultDetail() {
		return resultDetail;
	}

	public void setResultDetail(Map<String, Object> resultDetail) {
		this.resultDetail = resultDetail;
	}

	/**
	 * 清除datacenter所有数据
	 */
	public void clearAccData() {
		if (!StringUtil.isNullOrEmpty(accountDetailList)) {
			accountDetailList.clear();
		}
		if (!StringUtil.isNullOrEmpty(bankAccountList)) {
			bankAccountList.clear();
		}
		if (!StringUtil.isNullOrEmpty(lossReportMap)) {
			lossReportMap.clear();
		}
		if (!StringUtil.isNullOrEmpty(confirmResult)) {
			confirmResult.clear();
		}
		if (!StringUtil.isNullOrEmpty(relevancePremap)) {
			relevancePremap.clear();
		}
		if (!StringUtil.isNullOrEmpty(choosefalseDebitList)) {
			choosefalseDebitList.clear();
		}
		if (!StringUtil.isNullOrEmpty(debitlistSuccessMap)) {
			debitlistSuccessMap.clear();
		}
		if (!StringUtil.isNullOrEmpty(icsignmap)) {
			icsignmap.clear();
		}
		if (!StringUtil.isNullOrEmpty(financeIcAccountList)) {
			financeIcAccountList.clear();
		}
		if (!StringUtil.isNull(otp)) {
			otp = "";
		}
		if (!StringUtil.isNull(smc)) {
			smc = "";
		}
		if (!StringUtil.isNull(otp_password_RC)) {
			otp_password_RC = "";
		}
		if (!StringUtil.isNull(smc_password_RC)) {
			smc_password_RC = "";
		}
		if (!StringUtil.isNull(acc_relevance_actnum)) {
			acc_relevance_actnum = "";
		}
		if (!StringUtil.isNullOrEmpty(chooseBankAccount)) {
			chooseBankAccount.clear();
		}
		if (!StringUtil.isNullOrEmpty(callbackmap)) {
			callbackmap.clear();
		}
		if (!StringUtil.isNullOrEmpty(resultDetail)) {
			resultDetail.clear();
		}
		if (!StringUtil.isNullOrEmpty(serviceList)) {
			serviceList.clear();
		}
		if (!StringUtil.isNullOrEmpty(queryCurrencyList)) {
			queryCurrencyList.clear();
		}
		if (!StringUtil.isNullOrEmpty(queryCodeList)) {
			queryCodeList.clear();
		}
		if (!StringUtil.isNullOrEmpty(queryCashRemitList)) {
			queryCashRemitList.clear();
		}
		if (!StringUtil.isNullOrEmpty(queryCashRemitCodeList)) {
			queryCashRemitCodeList.clear();
		}
		if (!StringUtil.isNullOrEmpty(volumesAndCdnumbers)) {
			volumesAndCdnumbers.clear();
		}
		if (!StringUtil.isNullOrEmpty(commCardList)) {
			commCardList.clear();
		}
		if (!StringUtil.isNullOrEmpty(medicalAccountList)) {
			medicalAccountList.clear();
		}
		if (!StringUtil.isNullOrEmpty(medicalbackmap)) {
			medicalbackmap.clear();
		}
		if (!StringUtil.isNullOrEmpty(applyConfirmResult)) {
			applyConfirmResult.clear();
		}
		if (!StringUtil.isNullOrEmpty(applyResultMap)) {
			applyResultMap.clear();
		}
		if (!StringUtil.isNullOrEmpty(purposeList)) {
			purposeList.clear();
		}
		if (!StringUtil.isNullOrEmpty(reasonList)) {
			reasonList.clear();
		}
	}

	public List<String> getQueryCurrencyList() {
		return queryCurrencyList;
	}

	public void setQueryCurrencyList(List<String> queryCurrencyList) {
		this.queryCurrencyList = queryCurrencyList;
	}

	public List<String> getQueryCodeList() {
		return queryCodeList;
	}

	public void setQueryCodeList(List<String> queryCodeList) {
		this.queryCodeList = queryCodeList;
	}

	public List<List<String>> getQueryCashRemitList() {
		return queryCashRemitList;
	}

	public void setQueryCashRemitList(List<List<String>> queryCashRemitList) {
		this.queryCashRemitList = queryCashRemitList;
	}

	public List<List<String>> getQueryCashRemitCodeList() {
		return queryCashRemitCodeList;
	}

	public void setQueryCashRemitCodeList(
			List<List<String>> queryCashRemitCodeList) {
		this.queryCashRemitCodeList = queryCashRemitCodeList;
	}

	public ArrayList<Map<String, Object>> getVolumesAndCdnumbers() {
		return volumesAndCdnumbers;
	}

	public void setVolumesAndCdnumbers(
			ArrayList<Map<String, Object>> volumesAndCdnumbers) {
		this.volumesAndCdnumbers = volumesAndCdnumbers;
	}

	public List<Map<String, Object>> getCommCardList() {
		return commCardList;
	}

	public void setCommCardList(List<Map<String, Object>> commCardList) {
		this.commCardList = commCardList;
	}

	public List<Map<String, Object>> getMedicalAccountList() {
		return medicalAccountList;
	}

	public void setMedicalAccountList(
			List<Map<String, Object>> medicalAccountList) {
		this.medicalAccountList = medicalAccountList;
	}

	public Map<String, Object> getMedicalbackmap() {
		return medicalbackmap;
	}

	public void setMedicalbackmap(Map<String, Object> medicalbackmap) {
		this.medicalbackmap = medicalbackmap;
	}

	public Map<String, Object> getApplyConfirmResult() {
		return applyConfirmResult;
	}

	public void setApplyConfirmResult(Map<String, Object> applyConfirmResult) {
		this.applyConfirmResult = applyConfirmResult;
	}

	public Map<String, Object> getApplyResultMap() {
		return applyResultMap;
	}

	public void setApplyResultMap(Map<String, Object> applyResultMap) {
		this.applyResultMap = applyResultMap;
	}

	public List<String> getPurposeList() {
		return purposeList;
	}

	public void setPurposeList(List<String> purposeList) {
		this.purposeList = purposeList;
	}

	public List<String> getReasonList() {
		return reasonList;
	}

	public void setReasonList(List<String> reasonList) {
		this.reasonList = reasonList;
	}

	public Map<String, Object> getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(Map<String, Object> countryCode) {
		this.countryCode = countryCode;
	}

	public boolean isManageFlag() {
		return manageFlag;
	}

	public void setManageFlag(boolean manageFlag) {
		this.manageFlag = manageFlag;
	}
	
}
