package com.chinamworld.bocmbci.biz.bocinvt_p603.myinvetproduct.domain;

import java.io.Serializable;
import java.util.Map;

/**
 * 中银理财的赎回确认信息
 * 
 * @author HVZHUNG
 *
 */
public class RedeemVerifyInfo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/** 产品代码 */
	public String prodCode;
	/** 产品名称 */
	public String prodName;
	/** 币种 */
	public String currencyCode;
	/** 交易日期 */
	public String paymentDate;
	/** 赎回价格 */
	public String sellPrice;
	/** 赎回份额 */
	public String redeemQuantity;
	/** 赎回金额 */
	public String redeemAmount;
	/** 成交类型 */
	public String tranflag;
	/** 交易流水号 */
	public String tranSeq;
	/** 指定赎回日期/确认赎回日期 */
	public String redeemDate;
	/** 钞汇标识 */
	public String cashRemit;

	public RedeemVerifyInfo() {

	}

	public RedeemVerifyInfo(Map<String, Object> map) {
		this.prodCode = (String) map.get("prodCode");
		this.prodName = (String) map.get("prodName");
		this.currencyCode = (String) map.get("currencyCode");
		this.paymentDate = (String) map.get("paymentDate");
		this.sellPrice = (String) map.get("sellPrice");
		this.redeemQuantity = (String) map.get("redeemQuantity");
		this.redeemAmount = (String) map.get("redeemAmount");
		this.tranflag = (String) map.get("tranflag");
		this.tranSeq = (String) map.get("tranSeq");
		this.redeemDate = (String) map.get("redeemDate");
		this.cashRemit = (String) map.get("cashRemit");

	}
}
