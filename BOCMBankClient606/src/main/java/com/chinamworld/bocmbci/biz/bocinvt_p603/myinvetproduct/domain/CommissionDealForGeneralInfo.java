package com.chinamworld.bocmbci.biz.bocinvt_p603.myinvetproduct.domain;

import java.math.BigDecimal;
import java.util.Map;

import android.text.TextUtils;

//import com.chinamworld.bocmbci.biz.bocinvt_p603.myinvetproduct.ui.fragment.ItemThreePartitionHaveArrowListFragment.IItem;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 常规交易信息
 * 
 * @author HVZHUNG
 *
 */
public class CommissionDealForGeneralInfo extends ICommissionDealInfo {
	/** 交易日期/预计交易日期 */
	public String paymentDate;
	/**
	 * 产品代码 String
	 */
	public String prodCode;
	/**
	 * 产品名称 String
	 * 
	 */
	public String prodName;
	/**
	 * 交易类型 String 00：认购 01：申购 02：赎回 03：红利再投 04：红利发放 05：（经过）利息返还 06：本金返还
	 * 07：起息前赎回 08：利息折份额 09:赎回亏损 10:赎回盈利 11:产品转让
	 */
	public String trfType;
	/**
	 * 交易币种 001：人民币元 014：美元 012：英镑 013：港币 028: 加拿大元 029：澳元 038：欧元 027：日元
	 * 
	 */
	public String currencyCode;

	/**
	 * 钞汇标识 String 01：钞 02：汇 00：人民币钞汇
	 * */
	public String cashRemit;

	/**
	 * 交易价格
	 */
	public BigDecimal trfPrice;
	/** 交易份额 BigDecimal */
	public BigDecimal trfAmount;
	/** 交易金额 BigDecimal */
	public BigDecimal amount;
	/**
	 * 渠道 String 00：理财系统交易 01：（核心系统 OFP）柜面 02：网银 03：电话银行自助 04：电话银行人工 05：手机银行
	 * 06：家居银行 07：微信银行 08：自助终端 09：OCRM
	 */
	public String channel;
	/***
	 * 状态 String 0：委托待处理 1：成功 2：失败 3：已撤销 4：已冲正 5：已赎回
	 */
	public String status;

	/**
	 * 交易属性 Integer 0-常规交易 1-自动续约交易 2-预约交易 3-组合购买 4-自动投资交易 5-委托交易 6-短信委托 7-产品转入
	 * 8-产品转出 9-组合购买 10-委托交易 11-产品转让 12-周期投资 13-本金摊还
	 */
	public int tranAtrr;

	/**
	 * 是否可撤单 String 0：是 1：否
	 */
	public String canBeCanceled;
	/**
	 * 是否到账 String 0：否 1：是
	 */
	public String isReciveMoney;
	/** 交易账号 String 加星显示 */
	public String accountNumber;
	/** 交易账号缓存ID String */
	public String accountKey;
	/** 委托日期 */
	public String futureDate;
	/**
	 * 委托业务类型 String 委托交易撤单时上送 0：系统交易 1：认购委托 2：挂单委托 3：预约额度委托 4：类基金申请委托 5：份额转换委托
	 * 6：指定日期赎回委托 7：申购申请委托 8：赎回申请委托 9：预约赎回委托 10提前赎回委托
	 */
	public int entrustType;

	/** 交易流水号（后台），撤单时使用 String */
	public String tranSeq;
	/**
	 * 产品种类 String 0：结构理财产品 1：类基金理财产品
	 */
	public String productKind;
	
	/**
	 * 委托撤单成功交易序号
	 */
	public String transactionId;

	public CommissionDealForGeneralInfo() {
		super();
	}

	public CommissionDealForGeneralInfo(Map<String, Object> map) {
		super();
		this.paymentDate = (String) map.get("paymentDate");
		this.prodCode = (String) map.get("prodCode");
		this.prodName = (String) map.get("prodName");
		this.trfType = (String) map.get("trfType");
		this.currencyCode = (String) map.get("currencyCode");
		this.cashRemit = (String) map.get("cashRemit");
		String trfPriceStr = (String) map.get("trfPrice");
		if (!TextUtils.isEmpty(trfPriceStr))
			this.trfPrice = new BigDecimal(trfPriceStr);
		String trfAmountStr = (String) map.get("trfAmount");
		if (!TextUtils.isEmpty(trfAmountStr))
			this.trfAmount = new BigDecimal(trfAmountStr);
		String amountStr = (String) map.get("amount");
		if (!TextUtils.isEmpty(amountStr))
			this.amount = new BigDecimal(amountStr);
		this.channel = (String) map.get("channel");
		this.status = (String) map.get("status");
		this.tranAtrr = Integer.valueOf((String) map.get("tranAtrr"));
		this.canBeCanceled = (String) map.get("canBeCanceled");
		this.isReciveMoney = (String) map.get("isReciveMoney");
		this.accountNumber = (String) map.get("accountNumber");
		this.accountKey = (String) map.get("accountKey");
		this.futureDate = (String) map.get("futureDate");
		String entrustTypeStr = (String) map.get("entrustType");
		if (!TextUtils.isEmpty(entrustTypeStr))
			this.entrustType = Integer.valueOf(entrustTypeStr);
		this.tranSeq = (String) map.get("tranSeq");
		this.productKind = (String) map.get("productKind");
	}

	public String getFormatTradeAmount() {
		String amountTemp = "-";
		if(Double.parseDouble(amount.toString())>0){
			 amountTemp = StringUtil.parseStringCodePattern(currencyCode,amount.toString(), 2);
		}
		return amountTemp;
	}

	public String getTradeTypeName() {

		return getTradeTypeName(entrustType);

	}

	/**
	 * 是否可撤单
	 * 
	 * @return
	 */
	public boolean enableCanceled() {

		return "0".equals(canBeCanceled);
	}

}
