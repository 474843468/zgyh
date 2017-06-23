package com.chinamworld.bocmbci.biz.bocinvt_p603.myinvetproduct.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Map;

import android.text.TextUtils;

import com.chinamworld.bocmbci.base.application.BaseDroidApp;

/**
 * 赎回信息
 * 
 * @author HVZHUNG
 *
 */
public class RedeemInfo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/** 网银交易流水号 */
	public String transactionId;
	/** 产品代码 */
	public String prodCode;
	/** 产品名称 */
	public String prodName;
	/** 币种 */
	public String currencyCode;
	public BOCCurrency currencyInfo;
	/** 交易价格 */
	public BigDecimal trfPrice;
	/** 交易份额 */
	public BigDecimal trfAmount;
	/** 付款日期 */
	public String paymentDate;
	/** 赎回金额 */
	public String redeemAmount;
	/** 成交类型 0:正常1:挂单 */
	public String tranflag;
	/** 交易日期 */
	public String transDate;

	public RedeemInfo() {

	}

	public RedeemInfo(Map<String, Object> map) {
		/** 网银交易流水号 */
		transactionId = (String) map.get("transactionId");
		/** 产品代码 */
		prodCode = (String) map.get("prodCode");
		/** 产品名称 */
		prodName = (String) map.get("prodName");
		/** 币种 */
		currencyCode = (String) map.get("currencyCode");
		currencyInfo = BOCCurrency.getInstanceByNumberCode(
				BaseDroidApp.context, currencyCode);
		/** 交易价格 */
		String priceString = (String) map.get("trfPrice");
		if (TextUtils.isEmpty(priceString)) {
			trfPrice = new BigDecimal(0);
		} else {
			trfPrice = new BigDecimal(priceString);
		}

		/** 交易份额 */
		String amountString = (String) map.get("trfAmount");
		if (TextUtils.isEmpty(amountString)) {
			trfAmount = new BigDecimal(0);
		} else {
			trfAmount = new BigDecimal(amountString);
		}

		/** 付款日期 */
		paymentDate = (String) map.get("paymentDate");
		/** 赎回金额 */
		redeemAmount = (String) map.get("redeemAmount");
		/** 成交类型 0:正常1:挂单 */
		tranflag = (String) map.get("tranflag");
		/** 交易日期 */
		transDate = (String) map.get("transDate");
	}
}
