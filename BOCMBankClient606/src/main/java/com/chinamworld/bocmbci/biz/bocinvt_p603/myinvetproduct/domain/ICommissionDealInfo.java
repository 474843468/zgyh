package com.chinamworld.bocmbci.biz.bocinvt_p603.myinvetproduct.domain;

import java.io.Serializable;

public abstract class ICommissionDealInfo implements Serializable {

	/** 渠道-理财系统交易 */
	public final static int CHANNEL_FINANCING_SYSTEM = 0;
	/** 渠道-（核心系统 OFP）柜面 */
	public final static int CHANNEL_OFP = 1;
	/** 渠道- 网银 */
	public final static int CHANNEL_CHINABANK = 2;
	/** 渠道-电话银行自助 */
	public final static int CHANNEL_TELEPHONEBANK_SELF_SERVICE = 3;
	/** 渠道- 电话银行人工 */
	public final static int CHANNEL_TELEPHONEBANK_LABOR = 4;
	/** 渠道-手机银行 */
	public final static int CHANNEL_MOBILEPHONEBANK = 5;
	/** 渠道-家居银行 */
	public final static int CHANNEL_HOMEBANK = 6;
	/** 渠道-微信银行 */
	public final static int CHANNEL_WEIXINBANK = 7;
	/** 渠道-自助终端 */
	public final static int CHANNEL_SELF_SERVICE_TERMINALS = 8;
	/** 渠道-OCRM */
	public final static int CHANNEL_OCRM = 9;

	public String getChnanelName(int channel) {
		String channelName = "-";
		switch (channel) {
		case CHANNEL_FINANCING_SYSTEM:
			channelName = "理财系统";
			break;
		case CHANNEL_OFP:
			channelName = "柜面 ";
			break;
		case CHANNEL_CHINABANK:
			channelName = "网上银行 ";
			break;
		case CHANNEL_TELEPHONEBANK_SELF_SERVICE:
			channelName = "电话银行自助";
			break;
		case CHANNEL_TELEPHONEBANK_LABOR:
			channelName = "电话银行人工 ";
			break;
		case CHANNEL_MOBILEPHONEBANK:
			channelName = "手机银行 ";
			break;
		case CHANNEL_HOMEBANK:
			channelName = "家居银行 ";
			break;
		case CHANNEL_WEIXINBANK:
			channelName = "微信银行";
			break;
		case CHANNEL_SELF_SERVICE_TERMINALS:
			channelName = "自助终端";
			break;
		case CHANNEL_OCRM:
			channelName = "OCRM";
			break;

		default:
			break;
		}
		return channelName;
	}

	/** 交易状态-委托待处理 */
	public final static int STATUS_PENDING = 0;
	/** 交易状态-成功 */
	public final static int STATUS_SUCCESSFUL = 1;
	/** 交易状态-失败 */
	public final static int STATUS_UNSUCCESSFUL = 2;
	/** 交易状态-已撤销 */
	public final static int STATUS_UNDONE = 3;
	/** 交易状态-已冲正 */
	public final static int STATUS_REVERSAL = 4;
	/** 交易状态-已赎回 */
	public final static int STATUS_REDEMPTIVE = 5;

	/** 0-常规交易 */
	public final static int ATRR_REGULAR = 0;
	/** 1-自动续约交易 */
	public final static int ATRR_AUTO_RENEWAL = 1;
	/** 2-预约交易 */
	public final static int ATRR_SUBSCRIBE = 2;
	/** 3-组合购买 */
	public final static int ATRR_GROUP = 3;
	/** 4-自动投资交易 */
	public final static int ATRR_AUTO_INVEST = 4;
	/** 5-委托交易 */
	public final static int ATRR_COMMISSION = 5;
	/** 6-短信委托 */
	public final static int ATRR_MESSAGE = 6;
	/** 7-产品转入 */
	public final static int ATRR_PRODUCT_IN = 7;
	/** 8-产品转出 */
	public final static int ATRR_PRODUCT_OUT = 8;
	/** 9-组合购买 */
	public final static int ATRR_GROUP_9 = 9;
	/** 10-委托交易 */
	public final static int ATRR_COMMISSION_10 = 10;
	/** 11-产品转让 */
	public final static int ATRR_PRODUCT_ASSIGNMENT = 11;
	/** 12-周期投资 */
	public final static int ATRR_CYCLE_INVEST = 12;
	/** 13-本金摊还 */
	public final static int ATRR_PRINCIPAL = 13;

	/** 委托业务类型- 0：系统交易 */
	public final static int COMMISSION_TYPE_SYSTEM = 0;
	/** 委托业务类型-1：认购委托 */
	public final static int COMMISSION_TYPE_SUBSCRIBE = 1;
	/** 委托业务类型-2：挂单委托 */
	public final static int COMMISSION_TYPE_RESTING = 2;
	/** 委托业务类型-3：预约额度委托 */
	public final static int COMMISSION_TYPE_RESERVATION = 3;
	/** 委托业务类型-4：类基金申请委托 */
	public final static int COMMISSION_TYPE_REQUEST = 4;
	/** 委托业务类型-5：份额转换委托 */
	public final static int COMMISSION_TYPE_TRANSITION = 5;
	/** 委托业务类型-6：指定日期赎回委托 */
	public final static int COMMISSION_TYPE_ASSIGN_DATE_REDEEM = 6;
	/** 委托业务类型-7：申购申请委托 */
	public final static int COMMISSION_TYPE_SUBSCRIBE_REQUEST = 7;
	/** 委托业务类型-8：赎回申请委托 */
	public final static int COMMISSION_TYPE_REDEEM_REQUEST = 8;
	/** 委托业务类型-9：预约赎回委托 */
	public final static int COMMISSION_TYPE_RESERVATION_REQUEST = 9;
	/** 委托业务类型- 10提前赎回委托 */
	public final static int COMMISSION_TYPE_AHEAD_REDEEM = 10;

	/**
	 * 交易类型 String
	 * 
	 */
	/** 00：认购 */
	public final static int TRADE_TYPE_SUBSCRIPTION = 0;
	/** 01：申购 */
	public final static int TRADE_TYPE_SUBSCRIBE = 1;
	/** 02：赎回 */
	public final static int TRADE_TYPE_REDEEM = 2;
	/** 03：红利再投 */
	public final static int TRADE_TYPE_BONUS_AGAIN = 3;
	/** 04：红利发放 */
	public final static int TRADE_TYPE_BONUS_GRANT = 4;
	/** 05：（经过）利息返还 */
	public final static int TRADE_TYPE_INTEREST_BACK = 5;
	/** 06：本金返还 */
	public final static int TRADE_TYPE_PRINCIPAL_BACK = 6;
	/** 07：起息前赎回 */
	public final static int TRADE_TYPE_REDEEM_BEFORE_INTEREST = 7;
	/** 08：利息折份额 */
	public final static int TRADE_TYPE_INTEREST_TRANSITION = 8;
	/** 09:赎回亏损 */
	public final static int TRADE_TYPE_REDEEM_DEFICIT = 9;
	/** 10:赎回盈利 */
	public final static int TRADE_TYPE_REDEEM_EARNINGS = 10;
	/** 11:产品转让 */
	public final static int TRADE_TYPE_PRODUCT_MAKE_OVER = 11;

	public String getTradeTypeName(int tradeType) {
		String result = "-";
		switch (tradeType) {
		case TRADE_TYPE_SUBSCRIPTION:
			result = "认购 ";
			break;
		case TRADE_TYPE_SUBSCRIBE:
			result = "申购 ";
			break;
		case TRADE_TYPE_REDEEM:
			result = "赎回 ";
			break;
		case TRADE_TYPE_BONUS_AGAIN:
			result = "红利再投 ";
			break;
		case TRADE_TYPE_BONUS_GRANT:
			result = "红利发放 ";
			break;
		case TRADE_TYPE_INTEREST_BACK:
			result = "利息返还 ";
			break;
		case TRADE_TYPE_PRINCIPAL_BACK:
			result = "本金返还 ";
			break;
		case TRADE_TYPE_REDEEM_BEFORE_INTEREST:
			result = "起息前赎回 ";
			break;
		case TRADE_TYPE_INTEREST_TRANSITION:
			result = "利息折份额 ";
			break;
		case TRADE_TYPE_REDEEM_DEFICIT:
			result = "赎回亏损 ";
			break;
		case TRADE_TYPE_REDEEM_EARNINGS:
			result = "赎回盈利 ";
			break;
		case TRADE_TYPE_PRODUCT_MAKE_OVER:
			result = "产品转让 ";
			break;

		default:
			break;
		}
		return result;
	}
}
