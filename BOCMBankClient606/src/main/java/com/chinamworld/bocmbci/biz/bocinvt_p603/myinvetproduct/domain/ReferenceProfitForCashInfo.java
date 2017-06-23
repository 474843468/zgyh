package com.chinamworld.bocmbci.biz.bocinvt_p603.myinvetproduct.domain;

import java.util.Map;

/**
 * 现金管理类产品参考收益(修改为结构性理财产品的参考收益)
 * 
 * @author HVZHUNG
 *
 */
public class ReferenceProfitForCashInfo extends BaseReferProfitInfo {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 计息开始日期
	 */
	public String intsdate;
	/**
	 * 计息截止日期/收益日期
	 */
	public String intedate;
	/**
	 * 是否收益累计产品
	 */
	public String progressionflag;
	/**
	 * 产品性质
	 */
	public String kind;
	/**
	 * 产品期限特性
	 */
	public String termType;
	/** 当期收益/持仓收益 */
	public String profit;
	/** 历史累计收益 */
	public String totalprofit;
	/** 未到账收益 */
	public String unpayprofit;
	/** 已到账收益 */
	public String payprofit;
	/** 付息日 */
	public String paydate;
	/**
	 * 赎回规则
	 */
	public String redeemrule;
	/**
	 * 本金返还方式
	 */
	public String redpayamtmode;
	/** 本金返还T+N(天数) */
	public String redpayamountdate;
	/**
	 * 收益返还方式
	 */
	public String redpayprofitmode;
	/** 收益返还T+N(天数) */
	public String redpayprofitdate;
	
	/** 收益日期 */
	public String edate;
	
	
	public ReferenceProfitForCashInfo(Map<String, Object> map) {
		super(map);

		intsdate = (String) map.get("intsdate");
		intedate = (String) map.get("intedate");
		progressionflag = (String) map.get("progressionflag");
		kind = (String) map.get("kind");
		termType = (String) map.get("termType");
		profit = (String) map.get("profit");
		totalprofit = (String) map.get("totalprofit");
		unpayprofit = (String) map.get("unpayprofit");
		payprofit = (String) map.get("payprofit");
		paydate = (String) map.get("paydate");
		redeemrule = (String) map.get("redeemrule");
		redpayamtmode = (String) map.get("redpayamtmode");
		redpayamountdate = (String) map.get("redpayamountdate");
		redpayprofitmode = (String) map.get("redpayprofitmode");
		redpayprofitdate = (String) map.get("redpayprofitdate");
		prodName = (String) map.get("proname");
		edate = (String) map.get("edate");
		procur = (String) map.get("procur");
	}
}
