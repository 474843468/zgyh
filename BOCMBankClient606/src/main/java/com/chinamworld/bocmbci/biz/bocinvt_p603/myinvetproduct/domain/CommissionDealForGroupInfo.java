package com.chinamworld.bocmbci.biz.bocinvt_p603.myinvetproduct.domain;

import java.math.BigDecimal;
import java.util.Map;

import android.text.TextUtils;
//import com.chinamworld.bocmbci.biz.bocinvt_p603.myinvetproduct.ui.fragment.ItemThreePartitionHaveArrowListFragment.IItem;

/**
 * 组合交易信息
 * 
 * @author HVZHUNG
 *
 */
public class CommissionDealForGroupInfo extends ICommissionDealInfo {
	/** 组合交易流水号 String */
	public String tranSeq;
	/** 银行账号 String */
	public String accountNo;
	/** 银行账号缓存ID String */
	public String accountKey;
	/** 产品代码 String */
	public String prodCode;
	/** 产品名称 String */
	public String prodName;
	/**
	 * 币种 String 001：人民币元 014：美元 012：英镑 013：港币 028: 加拿大元 029：澳元 038：欧元 027：日元
	 */
	public String currency;

	/**
	 * 钞汇标识 String 01：钞 02：汇 00：人民币
	 */
	public String cashRemit;
	/** 购买金额 BigDecimal */
	public BigDecimal buyAmt;
	/** 购买价格 BigDecimal */
	public BigDecimal trfPrice;
	/**
	 * 状态 String 0：正常 1：解除
	 */
	public String status;

	/** 交易日期 String 即质押日期 */
	public String returnDate;
	/**
	 * 渠道 String 交易渠道 0：理财系统交易 1：（核心系统 OFP）柜面 2：网银 3：电话银行自助 4：电话银行人工 5:手机银行
	 * 6:家居银行 7:微信银行 8:自助终端 9：OCRM
	 */
	public String channel;

	/**
	 * 是否可解除 String 0：是 1：否 (状态已为解除的返回‘否’)
	 */
	public String canBeCanceled;
	
	/**
	 * 组合交易撤单 交易序号 
	 */
	public String transactionId;
	/** 成交金额 */
	public BigDecimal amount;

	public CommissionDealForGroupInfo(Map<String, Object> mapInfo) {
		super();
		this.tranSeq = (String) mapInfo.get("tranSeq");
		this.accountNo = (String) mapInfo.get("accountNo");
		this.accountKey = (String) mapInfo.get("accountKey");
		this.prodCode = (String) mapInfo.get("prodCode");
		this.prodName = (String) mapInfo.get("prodName");
		this.currency = (String) mapInfo.get("currency");
		this.cashRemit = (String) mapInfo.get("cashRemit");
		String buyAmtString = (String) mapInfo.get("buyAmt");
		if (!TextUtils.isEmpty(buyAmtString))
			this.buyAmt = new BigDecimal(buyAmtString);
		String trfPriceStr = (String) mapInfo.get("trfPrice");
		if (!TextUtils.isEmpty(trfPriceStr))
			this.trfPrice = new BigDecimal(trfPriceStr);
		this.status = (String) mapInfo.get("status");
		this.returnDate = (String) mapInfo.get("returnDate");
		this.channel = (String) mapInfo.get("channel");
		this.canBeCanceled = (String) mapInfo.get("canBeCanceled");
		String amount = (String) mapInfo.get("amount");
		if (!TextUtils.isEmpty(amount))
			this.amount = new BigDecimal(amount);
	}

	public CommissionDealForGroupInfo() {
	}

	public String getStatusText() {
		String text = "-";
		if ("0".equals(status)) {
			text = "正常";
		} else if ("1".equals(status)) {
			text = "解除";
		}
		return text;
	}

	public String getChnanelName() {
		int channelInt = -1;
		try {
			channelInt = Integer.valueOf(channel);
		} catch (Exception e) {
		}
		return super.getChnanelName(channelInt);
	}

}
