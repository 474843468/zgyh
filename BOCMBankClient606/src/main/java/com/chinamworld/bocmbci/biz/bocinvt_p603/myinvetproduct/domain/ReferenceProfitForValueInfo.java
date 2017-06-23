package com.chinamworld.bocmbci.biz.bocinvt_p603.myinvetproduct.domain;

import java.util.Map;

/**
 * 净值型产品参考收益
 * 
 * @author HVZHUNG
 *
 */
public class ReferenceProfitForValueInfo extends BaseReferProfitInfo {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/** 收益日期 */
	public String profitdate;
	/** 产品性质 */
	public String kind;
	/** 总盈亏 */
	public String totalamt;
	/** 已实现盈亏 */
	public String amt;
	/** 持仓盈亏 */
	public String balamt;

	public ReferenceProfitForValueInfo(Map<String, Object> map) {
		super(map);
		profitdate = (String) map.get("profitdate");
		kind = (String) map.get("kind");
		totalamt = (String) map.get("totalamt");
		amt = (String) map.get("amt");
		balamt = (String) map.get("balamt");
		prodName = (String) map.get("proname");
	}
}
