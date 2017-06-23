package com.chinamworld.bocmbci.biz.bocinvt_p603.myinvetproduct.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Map;

import android.text.TextUtils;

import com.chinamworld.bocmbci.base.application.BaseDroidApp;


/**
 * 过去产品信息
 * 
 * @author HVZHUNG
 *
 */
public class BocProductForDeprecateInfo implements Serializable {
	/** 产品代码 String */
	public String proId;
	/** 产品到期日 String Yyyy/MM/dd */
	public String eDate;
	/** 产品名称 String */
	public String proname;
	/**
	 * 币种 String 000：全部、 001：人民币元 014：美元 012：英镑 013：港币 028: 加拿大元 029：澳元 038：欧元
	 * 027：日元
	 */
	public BOCCurrency procur;

	/**
	 * 钞汇标识 String 1：钞 2：汇 0：人民币
	 */
	public String buyMode;

	/** 产品期限 String 以天数表示的产品到期日期限，等于产品到日期减去产品起息日。 */
	public String proterm;

	/** 本金 BigDecimal */
	public BigDecimal amount;
	/** 实际收益 BigDecimal */
	public BigDecimal payProfit;
	/** 收益率 BigDecimal */
	public BigDecimal payRate;
	/**
	 * 产品性质 String 0:结构理财产品 1:类基金理财产品
	 */
	public String kind;

	/** 支付标识 String 0：未付1：已付 */
	public String payFlag;

	/** 账户 String */
	public String accno;
	/** 保留字段 String */
	public String extFiled;
	/** 币种 String */
	public String currency;
	
	public BocProductForDeprecateInfo(Map<String, Object> mapInfo) {
		this.proId = (String) mapInfo.get("proId");
		this.eDate = (String) mapInfo.get("eDate");
		this.proname = (String) mapInfo.get("proname");
		this.currency = (String) mapInfo.get("procur");
		this.procur = BOCCurrency.getInstanceByNumberCode(BaseDroidApp.context,
				(String) mapInfo.get("procur"));
		if (this.procur == null) {
			this.procur = BOCCurrency.getInstanceByNumberCode(
					BaseDroidApp.context, "000");
		}
		this.buyMode = (String) mapInfo.get("buyMode");
		this.proterm = (String) mapInfo.get("proterm");
		String amountStr = (String) mapInfo.get("amount");
		if (!TextUtils.isEmpty(amountStr))
			this.amount = new BigDecimal(amountStr);
		String payProfitStr = (String) mapInfo.get("payProfit");
		if (!TextUtils.isEmpty(payProfitStr))
			this.payProfit = new BigDecimal(payProfitStr);
		String payRateStr = (String) mapInfo.get("payRate");
		if (!TextUtils.isEmpty(payRateStr))
			this.payRate = new BigDecimal(payRateStr);
		this.kind = (String) mapInfo.get("kind");
		this.payFlag = (String) mapInfo.get("payFlag");
		this.accno = (String) mapInfo.get("accno");
		this.extFiled = (String) mapInfo.get("extFiled");
	}

	public BocProductForDeprecateInfo() {
		// TODO Auto-generated constructor stub
	}

}
