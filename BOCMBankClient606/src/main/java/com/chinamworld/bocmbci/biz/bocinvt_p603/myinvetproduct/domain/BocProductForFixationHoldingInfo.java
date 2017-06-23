package com.chinamworld.bocmbci.biz.bocinvt_p603.myinvetproduct.domain;

/**
 * 固定期限型净值理财产品持仓信息
 * 
 * @author HVZHUNG
 *
 */
public class BocProductForFixationHoldingInfo extends BOCProductForHoldingInfo {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/** 是否显示详情 列表显示的时候adapter需要这个字段确定是否显示详情以及当前条目剪头的状态 */
	public boolean isShowDetails = false;

	public BocProductForFixationHoldingInfo() {

	}

	public BocProductForFixationHoldingInfo(BOCProductForHoldingInfo info) {
		this.prodCode = info.prodCode;
		this.bancAccount = info.bancAccount;
		this.xpadAccount = info.xpadAccount;
		this.prodName = info.prodName;
		this.curCode = info.curCode;
		this.yearlyRR = info.yearlyRR;
		this.prodBegin = info.prodBegin;
		this.prodEnd = info.prodEnd;
		this.holdingQuantity = info.holdingQuantity;
		this.availableQuantity = info.availableQuantity;
		this.canRedeem = info.canRedeem;
		this.canPartlyRedeem = info.canPartlyRedeem;
		this.canChangeBonusMode = info.canChangeBonusMode;
		this.currentBonusMode = info.currentBonusMode;
		this.lowestHoldQuantity = info.lowestHoldQuantity;
		this.redeemStartingAmount = info.redeemStartingAmount;
		this.cashRemit = info.cashRemit;
		this.progressionflag = info.progressionflag;
		this.sellPrice = info.sellPrice;
		this.issueType = info.issueType;
		this.productKind = info.productKind;
		this.expProfit = info.expProfit;
		this.price = info.price;
		this.priceDate = info.priceDate;
		this.expAmt = info.expAmt;
		this.termType = info.termType;
		this.canAddBuy = info.canAddBuy;
		this.standardPro = info.standardPro;
		this.canAgreementMange = info.canAgreementMange;
		this.productTerm = info.productTerm;
		this.allowAssignDate = info.allowAssignDate;
		this.accountKey = info.accountKey;
		this.shareValue = info.shareValue;
		this.cashProType = info.cashProType;

		this.bancAccountKey = info.bancAccountKey;
		this.currPeriod = info.currPeriod;
		this.totalPeriod = info.totalPeriod;
		this.yearlyRRMax = info.yearlyRRMax;
	}
}
