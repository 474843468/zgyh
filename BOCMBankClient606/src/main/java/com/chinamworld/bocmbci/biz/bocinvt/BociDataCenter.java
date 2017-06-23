package com.chinamworld.bocmbci.biz.bocinvt;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.chinamworld.bocmbci.biz.bocinvt_p603.BocInvestControl;
import com.chinamworld.bocmbci.utils.StringUtil;

public class BociDataCenter {
	private static BociDataCenter dataCenter;
	/** 登记账户信息 */
	private List<Map<String, Object>> investBindingInfo;
	/** 产品赎回预交易 */
	private Map<String, Object> redeemVerify;
	private Map<String, Object> redeemSuccessMap;
	/** 组合查询点击列表信息 */
	private Map<String, Object> choosemap;
	/** 登记账户信息 */
	private List<Map<String, Object>> newinvestBindingInfo;
	/** 登记账户余额 */
	private List<Map<String, Object>> balanceList;
	/** 组合查询产品详情 */
	private Map<String, Object> detailMap;

	/** 投资协议申请填写信息 */
	private Map<String, String> agreeInputMap;
	/** 等级是否匹配 */
	private Map<String, Object> riskMatchMap;
	/** 投资协议申请结果信息 */
	private Map<String, Object> agreeResultMap;
	/** 周期性产品续约协议详情信息 */
	private Map<String, Object> periodDetailMap;
	/** 查询是否是周期性连续产品 */
	private Map<String, Object> periodModifyMap;
	/** 修改信息提交内容 */
	private Map<String, Object> inputResultMap;
	/** 修改成功信息 */
	private Map<String, Object> editResultMap;
	/** 维护信息 */
	private Map<String, Object> autoResultMap;
	
	/** 投资明细查询信息 */
	private Map<String, Object> inverstDetailMap;
	
	//pwe
	/** 收益累计  */
	private List<Map<String, Object>> progressionList;
	/** 理财账户信息  **/
	private List<Map<String, Object>> bocinvtAcctList;
	/** 所有已关联账户    **/
	private List<Map<String, Object>> allAcctList;
	/** 未登记账户   **/
	private List<Map<String, Object>> unSetAcctList;
	/** 网上专属理财账户   **/
	private Map<String, Object> acctOFmap;
	/** 币种信息  **/
	private List<Map<String, Object>> currencyList;
	/** 账户列表数据   **/
	private List<Map<String, Object>> acctList;
	/** 指令交易ConversationId **/
	private String ocrmConversationId;
	/** 指令交易数据集   **/
	private Map<String, Object> ocrmMap;
	/** 服务器时间*/
	private Map<String, String> dateTime = new HashMap<String, String>();
	/** 赎回方式 是否为指定日期赎回   **/
	public static boolean isRedeem = false;
	
	public List<Map<String, Object>> getNewinvestBindingInfo() {
		return newinvestBindingInfo;
	}

	public void setNewinvestBindingInfo(List<Map<String, Object>> newinvestBindingInfo) {
		this.newinvestBindingInfo = newinvestBindingInfo;
	}

	private int i = 0;

	public int getI() {
		return i;
	}

	public void setI(int i) {
		this.i = i;
	}

	protected BociDataCenter() {
	}

	public static BociDataCenter getInstance() {
		if (dataCenter == null) {
//			dataCenter = new BociDataCenter();
			dataCenter = new BocInvestControl();
		}
		return dataCenter;
	}

	public List<Map<String, Object>> getInvestBindingInfo() {
		return investBindingInfo;
	}

	public void setInvestBindingInfo(List<Map<String, Object>> investBindingInfo) {
		this.investBindingInfo = investBindingInfo;
	}

	public Map<String, Object> getRedeemVerify() {
		return redeemVerify;
	}

	public void setRedeemVerify(Map<String, Object> redeemVerify) {
		this.redeemVerify = redeemVerify;
	}

	public Map<String, Object> getRedeemSuccessMap() {
		return redeemSuccessMap;
	}

	public void setRedeemSuccessMap(Map<String, Object> redeemSuccessMap) {
		this.redeemSuccessMap = redeemSuccessMap;
	}

	public Map<String, Object> getChoosemap() {
		return choosemap;
	}

	public void setChoosemap(Map<String, Object> choosemap) {
		this.choosemap = choosemap;
	}

	public List<Map<String, Object>> getProgressionList() {
		return progressionList;
	}

	public void setProgressionList(List<Map<String, Object>> progressionList) {
		this.progressionList = progressionList;
	}

	public List<Map<String, Object>> getBocinvtAcctList() {
		return bocinvtAcctList;
	}

	public void setBocinvtAcctList(List<Map<String, Object>> bocinvtAcctList) {
		this.bocinvtAcctList = bocinvtAcctList;
	}

	public List<Map<String, Object>> getAllAcctList() {
		return allAcctList;
	}

	public void setAllAcctList(List<Map<String, Object>> allAcctList) {
		this.allAcctList = allAcctList;
	}

	public List<Map<String, Object>> getUnSetAcctList() {
		return unSetAcctList;
	}

	public void setUnSetAcctList(List<Map<String, Object>> unSetAcctList) {
		this.unSetAcctList = unSetAcctList;
	}

	public Map<String, Object> getAcctOFmap() {
		return acctOFmap;
	}

	public void setAcctOFmap(Map<String, Object> acctOFmap) {
		this.acctOFmap = acctOFmap;
	}

	public List<Map<String, Object>> getCurrencyList() {
		return currencyList;
	}

	public void setCurrencyList(List<Map<String, Object>> currencyList) {
		this.currencyList = currencyList;
	}
	
	public List<Map<String, Object>> getAcctList() {
		return acctList;
	}

	public void setAcctList(List<Map<String, Object>> acctList) {
		this.acctList = acctList;
	}

	public String getOcrmConversationId() {
		return ocrmConversationId;
	}

	public void setOcrmConversationId(String ocrmConversationId) {
		this.ocrmConversationId = ocrmConversationId;
	}

	public Map<String, Object> getOcrmMap() {
		return ocrmMap;
	}

	public void setOcrmMap(Map<String, Object> ocrmMap) {
		this.ocrmMap = ocrmMap;
	}

	/** 风险类别   **/
	public static final Map<String, String> prodRiskType = new HashMap<String, String>() {
		private static final long serialVersionUID = 1L;

		{
			put("1", "保本固定收益");
			put("2", "保本浮动收益");
			put("3", "非保本浮动收益");
		}
	};
	
	/** 认购撤单规则   **/
	public static final Map<String, String> isCanCancle = new HashMap<String, String>() {
		private static final long serialVersionUID = 1L;
		
		{
			put("0", "不允许撤单");
			put("1", "认购当日允许撤单");
			put("2", "认购期允许撤单");
		}
	};
	
	/** 排序条件   */
	public static final List<String> sortType = new ArrayList<String>() {
		private static final long serialVersionUID = 1L;
		{
			add("产品销售期");
			add("产品期限");
			add("收益率");
			add("购买起点金额");
		}
	};
	
	/** 非交易时间挂单  */
	public static final Map<String, String> outTimeOrder = new HashMap<String, String>() {
		private static final long serialVersionUID = 1L;
		
		{
			put("0", "不允许");
			put("1", "允许");
		}
	};
	
	/** 渠道   */
	public static final List<String> channle = new ArrayList<String>() {
		private static final long serialVersionUID = 1L;
		{
			add("柜台");
			add("短信");
			add("网上银行");
			add("手机银行");
			add("家居银行");
			add("自助终端");
			add("电话银行自助");
			add("电话银行人工");
			add("微信银行");
			add("、");
		}
	};
	
	/** 钞汇标志 */
	public static final Map<String, String> cashMapValue = new HashMap<String, String>() {
		private static final long serialVersionUID = 1L;

		{
			put("null", "-");
			put("0", "人民币");
			put("1", "现钞");
			put("2", "现汇");
		}
	};
	
	/** 钞汇标志 */
	public static final Map<String, String> cashRemitMapValue = new HashMap<String, String>() {
		private static final long serialVersionUID = 1L;

		{
			put("null", "-");
			put("00", "人民币钞汇");
			put("01", "钞");
			put("02", "汇");
		}
	};
	
	/** 钞汇标志 */
	public static final Map<String, String> cashRemitMapValue2 = new HashMap<String, String>() {
		private static final long serialVersionUID = 1L;

		{
			put("null", "-");
			put("00", "人民币");
			put("01", "现钞");
			put("02", "现汇");
		}
	};
	
	/** 产品类型*/
	public static final Map<String, String> productType = new HashMap<String, String>() {
		private static final long serialVersionUID = 1L;

		{
			put("1", "现金管理类产品");
			put("2", "净值开放类产品");
			put("3", "固定期限产品");
		}
	};
	
	/** 付息状态*/
	public static final Map<String, String> payflagMap = new HashMap<String, String>() {
		private static final long serialVersionUID = 1L;

		{
			put("0", "未付");
			put("1", "现金分红");
			put("2", "红利再投");
		}
	};
	
	/** 赎回规则*/
	public static final Map<String, String> redeemrule = new HashMap<String, String>() {
		private static final long serialVersionUID = 1L;

		{
			put("0", "否");
			put("1", "先进先出");
			put("2", "后进先出");
		}
	};
	
	/** 本金返还方式*/
	public static final Map<String, String> redpayamtmode = new HashMap<String, String>() {
		private static final long serialVersionUID = 1L;

		{
			put("0", "实时返还");
			put("1", "T+N返还");
			put("2", "期末返还");
		}
	};
	
	/** 收益返还方式*/
	public static final Map<String, String> redpayprofitmode = new HashMap<String, String>() {
		private static final long serialVersionUID = 1L;

		{
			put("1", "T+N返还");
			put("2", "期末返还");
		}
	};
	
	/** 交易属性 */
	public static final Map<String, String> tranAtrrMapValue = new HashMap<String, String>() {
		private static final long serialVersionUID = 1L;

		{
			put("0", "常规交易");
			put("1", "自动续约交易");
			put("2", "预约交易");
			put("3", "组合购买");
			put("4", "自动投资交易");
			put("5", "委托交易");
			put("6", "短信委托");
			put("7", "产品转入");
			put("8", "产品转出");
			put("9", "组合购买");
			put("10", "委托交易");
			put("11", "产品转让");
			put("12", "周期投资");
			put("13", "本金摊还");
		}
	};
	
	/** 委托交易属性 */
	public static final Map<String, String> entrustTypeMapValue = new HashMap<String, String>() {
		private static final long serialVersionUID = 1L;

		{
			put("0", "系统交易");
			put("1", "认购委托");
			put("2", "挂单委托");
			put("3", "预约额度委托");
			put("4", "类基金申请委托");
			put("5", "份额转换委托");
			put("6", "指定日期赎回委托");
			put("7", "申购申请委托");
			put("8", "赎回申请委托");
			put("9", "预约赎回委托");
			put("10", "提前赎回委托");
			put("11", "申购委托");
			put("12", "投资期数赎回委托");
			put("13", "赎回委托");
		}
	};
	
	/** 网上专属理财状态   */
	public static final Map<String, String> openStatus = new HashMap<String, String>() {
		private static final long serialVersionUID = 1L;
		
		{
			put("S", "已开通");
			put("B", "账户未绑定");
			put("R", "账户未关联网银");
			put("N", "未开通");
		}
	};

	/**
	 * 清除datacenter所有数据
	 */
	public void clearBociData() {
		if (!StringUtil.isNullOrEmpty(investBindingInfo)) {
			investBindingInfo.clear();
		}
		if (!StringUtil.isNullOrEmpty(choosemap)) {
			choosemap.clear();
		}
		if (!StringUtil.isNullOrEmpty(redeemVerify)) {
			redeemVerify.clear();
		}
		if (!StringUtil.isNullOrEmpty(redeemSuccessMap)) {
			redeemSuccessMap.clear();
		}
		if (!StringUtil.isNullOrEmpty(newinvestBindingInfo)) {
			newinvestBindingInfo.clear();
		}
		if (!StringUtil.isNullOrEmpty(balanceList)) {
			balanceList.clear();
		}
		if (!StringUtil.isNullOrEmpty(detailMap)) {
			detailMap.clear();
		}
		i = 0;
		if (!StringUtil.isNullOrEmpty(agreeInputMap)) {
			agreeInputMap.clear();
		}
		if (!StringUtil.isNullOrEmpty(riskMatchMap)) {
			riskMatchMap.clear();
		}
		if (!StringUtil.isNullOrEmpty(agreeResultMap)) {
			agreeResultMap.clear();
		}
		if (!StringUtil.isNullOrEmpty(periodDetailMap)) {
			periodDetailMap.clear();
		}
		if (!StringUtil.isNullOrEmpty(periodModifyMap)) {
			periodModifyMap.clear();
		}
		if (!StringUtil.isNullOrEmpty(inputResultMap)) {
			inputResultMap.clear();
		}
		if (!StringUtil.isNullOrEmpty(editResultMap)) {
			editResultMap.clear();
		}
		if (!StringUtil.isNullOrEmpty(autoResultMap)) {
			autoResultMap.clear();
		}
		if (!StringUtil.isNullOrEmpty(progressionList)) {
			progressionList.clear();
		}
		if (!StringUtil.isNullOrEmpty(bocinvtAcctList)) {
			bocinvtAcctList.clear();
		}
		if (!StringUtil.isNullOrEmpty(unSetAcctList)) {
			unSetAcctList.clear();
		}
		if (!StringUtil.isNullOrEmpty(acctOFmap)) {
			acctOFmap.clear();
		}
		if (!StringUtil.isNullOrEmpty(currencyList)) {
			currencyList.clear();
		}
		if (!StringUtil.isNullOrEmpty(acctList)) {
			acctList.clear();
		}
		if (!StringUtil.isNullOrEmpty(ocrmMap)) {
			ocrmMap.clear();
		}
		if (!StringUtil.isNullOrEmpty(inverstDetailMap)) {
			inverstDetailMap.clear();
		}
		if (!StringUtil.isNullOrEmpty(dateTime)) {
			dateTime.clear();
		}
		if(isRedeem){
			isRedeem = false;
		}
	}

	public List<Map<String, Object>> getBalanceList() {
		return balanceList;
	}

	public void setBalanceList(List<Map<String, Object>> balanceList) {
		this.balanceList = balanceList;
	}

	public Map<String, Object> getDetailMap() {
		return detailMap;
	}

	public void setDetailMap(Map<String, Object> detailMap) {
		this.detailMap = detailMap;
	}

	public Map<String, String> getAgreeInputMap() {
		return agreeInputMap;
	}

	public void setAgreeInputMap(Map<String, String> agreeInputMap) {
		this.agreeInputMap = agreeInputMap;
	}

	public Map<String, Object> getRiskMatchMap() {
		return riskMatchMap;
	}

	public void setRiskMatchMap(Map<String, Object> riskMatchMap) {
		this.riskMatchMap = riskMatchMap;
	}

	public Map<String, Object> getAgreeResultMap() {
		return agreeResultMap;
	}

	public void setAgreeResultMap(Map<String, Object> agreeResultMap) {
		this.agreeResultMap = agreeResultMap;
	}

	public Map<String, Object> getPeriodDetailMap() {
		return periodDetailMap;
	}

	public void setPeriodDetailMap(Map<String, Object> periodDetailMap) {
		this.periodDetailMap = periodDetailMap;
	}

	public Map<String, Object> getPeriodModifyMap() {
		return periodModifyMap;
	}

	public void setPeriodModifyMap(Map<String, Object> periodModifyMap) {
		this.periodModifyMap = periodModifyMap;
	}

	public Map<String, Object> getInputResultMap() {
		return inputResultMap;
	}

	public void setInputResultMap(Map<String, Object> inputResultMap) {
		this.inputResultMap = inputResultMap;
	}

	public Map<String, Object> getEditResultMap() {
		return editResultMap;
	}

	public void setEditResultMap(Map<String, Object> editResultMap) {
		this.editResultMap = editResultMap;
	}

	public Map<String, Object> getAutoResultMap() {
		return autoResultMap;
	}

	public void setAutoResultMap(Map<String, Object> autoResultMap) {
		this.autoResultMap = autoResultMap;
	}

	public Map<String, Object> getInverstDetailMap() {
		return inverstDetailMap;
	}

	public void setInverstDetailMap(Map<String, Object> inverstDetailMap) {
		this.inverstDetailMap = inverstDetailMap;
	}

	public String getDateTime(String key) {
		if(dateTime == null || StringUtil.isNull(key))
			return "-";
		return dateTime.get(key);
	}

	public void setDateTime(String key, String value) {
		if(dateTime == null)
			dateTime = new HashMap<String, String>();
		dateTime.put(key, value);
	}
	
}
