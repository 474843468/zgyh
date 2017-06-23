package com.chinamworld.bocmbci.biz.loan;

import java.util.List;
import java.util.Map;

import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 贷款管理数据
 * 
 * @author wangmengmeng
 * 
 */
public class LoanDataCenter {
	private static LoanDataCenter dataCenter;
	/**选中的贷款账号信息*/
	private Map<String, Object> loanmap;
	/**第一次还款测算条件*/
	private Map<String, String> loanRepayCount;
	/**第一次还款测算结果*/
	private Map<String, Object> countResultmap;
	/**用款信息*/
	private Map<String, Object> loanUsemap;
	/**用款预交易信息*/
	private Map<String, Object> loanUsePremap;
	/**用款预交易结果信息*/
	private Map<String, Object> loanUsePreResultmap;
	/**变更还款账户信息*/
	private Map<String, String> loanChangeRepayAccmap;
	/**变更还款账户预交易请求信息*/
	private Map<String, Object> loanChangeRepayAccPremap;
	/**变更还款账户预交易结果信息*/
	private Map<String, Object> loanChangeRepayAccPreResultmap;
	private Map<String, String> LoanProtocolmap;
	/**进度查询单笔贷款记录详情*/
	private Map<String, Object> loanApplyResultmap;
	/**存储省份的集合*/
	private List<Map<String, Object>> resApplyprovinceList;
	private LoanDataCenter() {
	}

	public static LoanDataCenter getInstance() {
		if (dataCenter == null) {
			dataCenter = new LoanDataCenter();
		}
		return dataCenter;
	}

	public Map<String, Object> getLoanUsemap() {
		return loanUsemap;
	}

	public void setLoanUsemap(Map<String, Object> loanUsemap) {
		this.loanUsemap = loanUsemap;
	}

	public Map<String, String> getLoanChangeRepayAccmap() {
		return loanChangeRepayAccmap;
	}

	public void setLoanChangeRepayAccmap(Map<String, String> loanChangeRepayAccmap) {
		this.loanChangeRepayAccmap = loanChangeRepayAccmap;
	}

	public Map<String, Object> getLoanChangeRepayAccPremap() {
		return loanChangeRepayAccPremap;
	}

	public void setLoanChangeRepayAccPremap(
			Map<String, Object> loanChangeRepayAccPremap) {
		this.loanChangeRepayAccPremap = loanChangeRepayAccPremap;
	}

	public Map<String, Object> getLoanChangeRepayAccPreResultmap() {
		return loanChangeRepayAccPreResultmap;
	}

	public void setLoanChangeRepayAccPreResultmap(
			Map<String, Object> loanChangeRepayAccPreResultmap) {
		this.loanChangeRepayAccPreResultmap = loanChangeRepayAccPreResultmap;
	}

	public Map<String, Object> getLoanUsePreResultmap() {
		return loanUsePreResultmap;
	}

	public void setLoanUsePreResultmap(Map<String, Object> loanUsePreResultmap) {
		this.loanUsePreResultmap = loanUsePreResultmap;
	}

	public Map<String, Object> getLoanmap() {
		return loanmap;
	}

	public void setLoanmap(Map<String, Object> loanmap) {
		this.loanmap = loanmap;
	}

	public Map<String, String> getLoanRepayCount() {
		return loanRepayCount;
	}

	public void setLoanRepayCount(Map<String, String> loanRepayCount) {
		this.loanRepayCount = loanRepayCount;
	}

	public Map<String, Object> getCountResultmap() {
		return countResultmap;
	}

	public void setCountResultmap(Map<String, Object> countResultmap) {
		this.countResultmap = countResultmap;
	}
	public Map<String, Object> getLoanUsePremap() {
		return loanUsePremap;
	}

	public void setLoanUsePremap(Map<String, Object> loanUsePremap) {
		this.loanUsePremap = loanUsePremap;
	}

	public Map<String, String> getLoanProtocolmap() {
		return LoanProtocolmap;
	}
	public void setLoanProtocolmap(Map<String, String> LoanProtocolmap) {
		this.LoanProtocolmap = LoanProtocolmap;
	}
	public Map<String, Object> getLoanApplymap() {
		return loanApplyResultmap;
	}
	public void setLoanApplymap(Map<String,Object > loanApplyResultmap) {
		this.loanApplyResultmap = loanApplyResultmap;
	}
	public void setResApplyprovinceList(List<Map<String, Object>> resApplyprovinceList) {
		this.resApplyprovinceList = resApplyprovinceList;
	}

	public List<Map<String, Object>> getResApplyprovinceList() {
		return resApplyprovinceList;
	}
	/**
	 * 清除datacenter所有数据
	 */
	public void clearLoanData() {
		if(!StringUtil.isNullOrEmpty(loanmap)){
			loanmap.clear();
		}
		if(!StringUtil.isNullOrEmpty(loanRepayCount)){
			loanRepayCount.clear();
		}
		if(!StringUtil.isNullOrEmpty(countResultmap)){
			countResultmap.clear();
		}
	}
}
