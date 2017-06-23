package com.chinamworld.bocmbci.biz.epay.constants;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class TQConstants {
	// /** 电子支付 */
	// public static final int PAYMENT_TYPE_BOM = 1;
	// /** 协议支付 */
	// public static final int PAYMENT_TYPE_TREATY_QUCIK = 2;
	// /** 中银快付 */
	// public static final int PAYMENT_TYPE_ZY_QUCIK = 3;
	/** 查询全部支付方式 */
	public static final String PAYMENT_WAY_ALL = "0";
	/** 查询一次性还清 */
	public static final String PAYMENT_WAY_CLEAR = "1";
	/** 查询分期还款 */
	public static final String PAYMENT_WAY_BY_STAGES = "2";

	public static final String PUB_QUERY_FEILD_START_DATE = "startDate";
	public static final String PUB_QUERY_FEILD_END_DATE = "endDate";
	public static final String PUB_QUERY_FEILD_INSTALMENT_PLAN = "instalmentPlan";
	public static final String PUB_QUERY_FEILD_ACCOUNT_ID = "accountId";

	public static final String PUB_SELECTED_RESULT = "selectedResult";
	public static final String PUB_PAYMENT_TYPE = "tqPaymentType";
	/** 接口：查询电子支付记录 */
	public static final String METHOD_QUERY_BOM_TRANS_RECORD = "PsnEpayQueryOnlinePaymentRecord";
	public static final String METHOD_QUERY_BOM_TRANS_RECORD_FEILD_RECORD_NUMBER = "recordnumber";
	public static final String METHOD_QUERY_BOM_TRANS_RECORD_FEILD_LIST = "list";
	public static final String METHOD_QUERY_BOM_TRANS_RECORD_FEILD_AMOUNT = "amount";
	public static final String METHOD_QUERY_BOM_TRANS_RECORD_FEILD_MERCHANT_NO = "merchantNo";
	public static final String METHOD_QUERY_BOM_TRANS_RECORD_FEILD_MERCHANT_NAME = "merchantName";
	public static final String METHOD_QUERY_BOM_TRANS_RECORD_FEILD_AGREEMENT_NO = "agreementNo";
	public static final String METHOD_QUERY_BOM_TRANS_RECORD_FEILD_ORDER_SEQ = "orderSeq";
	public static final String METHOD_QUERY_BOM_TRANS_RECORD_FEILD_EPAY_TIME = "epayTime";
	public static final String METHOD_QUERY_BOM_TRANS_RECORD_FEILD_CHANNEL = "channl";
	public static final String METHOD_QUERY_BOM_TRANS_RECORD_FEILD_STATUS = "status";
	public static final String METHOD_QUERY_BOM_TRANS_RECORD_FEILD_CURRENCY_CODE = "currencyCode";
	public static final String METHOD_QUERY_BOM_TRANS_RECORD_FEILD_ORDER_NO = "orderNo";
	public static final String METHOD_QUERY_BOM_TRANS_RECORD_FEILD_PLAN_CODE = "planCode";
	public static final String METHOD_QUERY_BOM_TRANS_RECORD_FEILD_PLAN_NUMBER = "planNumber";
	public static final String METHOD_QUERY_BOM_TRANS_RECORD_FEILD_PLAN_FREE = "planFee";
	public static final String METHOD_QUERY_BOM_TRANS_RECORD_FEILD_PLAN_FIRST_AMOUNT = "planFirstAmount";
	public static final String METHOD_QUERY_BOM_TRANS_RECORD_FEILD_PLAN_EACH_AMOUNT = "planEachAmount";
	public static final String METHOD_QUERY_BOM_TRANS_RECORD_FEILD_TRANSACTION_ID = "transactionId";
	/** 接口：查询协议支付记录 */
	public static final String METHOD_QUERY_TREATY_TRANS_RECORD = "PsnEpayQueryAgreePaymentRecord";
	public static final String METHOD_QUERY_TREATY_TRANS_ALL_RECORD = "PsnEpayQueryAllTypeRecord";
	public static final String METHOD_QUERY_TREATY_TRANS_RECORD_FEILD_RECORD_NUMBER = "recordNumber";
	public static final String METHOD_QUERY_TREATY_TRANS_RECORD_FEILD_LIST = "List";
	public static final String METHOD_QUERY_TREATY_TRANS_RECORD_FEILD_PAY_TIME = "payTime";
	public static final String METHOD_QUERY_TREATY_TRANS_RECORD_FEILD_MERCHANT_NAME = "merchantName";
	public static final String METHOD_QUERY_TREATY_TRANS_RECORD_FEILD_AGREEMENT_SEQ = "agreementSeq";
	public static final String METHOD_QUERY_TREATY_TRANS_RECORD_FEILD_ORDER_AMOUNT = "orderAmount";
	public static final String METHOD_QUERY_TREATY_TRANS_RECORD_FEILD_CUR_CODE = "curCode";
	public static final String METHOD_QUERY_TREATY_TRANS_RECORD_FEILD_ORDER_STATUS = "orderStatus";
	public static final String METHOD_QUERY_TREATY_TRANS_RECORD_FEILD_ORDER_NOTE = "orderNote";
	public static final String METHOD_QUERY_TREATY_TRANS_RECORD_FEILD_HOLDER_MERID = "holderMerId";
	public static final String METHOD_QUERY_TREATY_TRANS_RECORD_FEILD_ORDER_NO = "orderNo";
	public static final String METHOD_QUERY_TREATY_TRANS_RECORD_FEILD_PAY_CARD_NO = "payCardNo";
	public static final String METHOD_QUERY_TREATY_TRANS_RECORD_FEILD_PAY_CARD_TYPE = "payCardType";
	public static final String METHOD_QUERY_TREATY_TRANS_RECORD_FEILD_ORDER_TIME = "orderTime";
	public static final String METHOD_QUERY_TREATY_TRANS_RECORD_FEILD_PAY_INFO_LIST = "orderPayInfoList";// 支付/退货信息
	public static final String METHOD_QUERY_TREATY_TRANS_RECORD_FEILD_BACK_FLAG = "backFlag";
	public static final String METHOD_QUERY_TREATY_TRANS_RECORD_FEILD_TRANSACTION_ID = "transactionId";
	public static final String METHOD_QUERY_TREATY_TRANS_RECORD_FEILD_RETURN_TIME = "returnTime";
	public static final String METHOD_QUERY_TREATY_TRANS_RECORD_FEILD_TRANA_MOUNT = "tranAmount";
	public static final String METHOD_QUERY_TREATY_TRANS_RECORD_FEILD_CURRENCY = "currency";
	public static final String METHOD_QUERY_TREATY_TRANS_RECORD_FEILD_TRAN_STATUS = "tranStatus";
	/** 接口：查询中银快付记录 */
	public static final String METHOD_QUERY_ZY_TRANS_RECORD = "PsnBocExpressPaymentRecordQuery";
	public static final String METHOD_QUERY_ZY_TRANS_RECORD_FEILD_RECORD_NUMBER = "recordNumber";
	public static final String METHOD_QUERY_ZY_TRANS_RECORD_FEILD_LIST = "List";
	public static final String METHOD_QUERY_ZY_TRANS_RECORD_FEILD_PAY_TIME = "payTime";
	public static final String METHOD_QUERY_ZY_TRANS_RECORD_FEILD_ORDER_NO = "orderNo";
	public static final String METHOD_QUERY_ZY_TRANS_RECORD_FEILD_MERCHANT_NAME = "merchantName";
	public static final String METHOD_QUERY_ZY_TRANS_RECORD_FEILD_ORDER_AMOUNT = "orderAmount";
	public static final String METHOD_QUERY_ZY_TRANS_RECORD_FEILD_CUR_CODE = "curCode";
	public static final String METHOD_QUERY_ZY_TRANS_RECORD_FEILD_ORDER_STATUS = "orderStatus";
	public static final String METHOD_QUERY_ZY_TRANS_RECORD_FEILD_ORDER_NOTE = "orderNote";
	public static final String METHOD_QUERY_ZY_TRANS_RECORD_FEILD_HOLDER_MERID = "holderMerId";
	public static final String METHOD_QUERY_ZY_TRANS_RECORD_FEILD_PAY_CARD_NO = "payCardNo";
	public static final String METHOD_QUERY_ZY_TRANS_RECORD_FEILD_PAY_CARD_TYPE = "payCardType";
	public static final String METHOD_QUERY_ZY_TRANS_RECORD_FEILD_ORDER_TIME = "orderTime";
	public static final String METHOD_QUERY_ZY_TRANS_RECORD_FEILD_PLAN_NUMBER = "planNumber";
	public static final String METHOD_QUERY_ZY_TRANS_RECORD_FEILD_PLAN_FEE = "planFee";
	public static final String METHOD_QUERY_ZY_TRANS_RECORD_FEILD_PLAN_FIRST_AMOUNT = "planFirstAmount";
	public static final String METHOD_QUERY_ZY_TRANS_RECORD_FEILD_CHANNEL_TYPE = "channelType";
	public static final String METHOD_QUERY_ZY_TRANS_RECORD_FEILD_PLAN_EACH_AMOUNT = "planEachAmount";
	public static final String METHOD_QUERY_ZY_TRANS_RECORD_FEILD_ORDER_PAY_INFO_LIST = "orderPayInfoList";
	public static final String METHOD_QUERY_ZY_TRANS_RECORD_FEILD_BACK_FLAG = "backFlag";
	public static final String METHOD_QUERY_ZY_TRANS_RECORD_FEILD_TRANSACTION_ID = "transactionId";
	public static final String METHOD_QUERY_ZY_TRANS_RECORD_FEILD_RETURN_TIME = "returnTime";
	public static final String METHOD_QUERY_ZY_TRANS_RECORD_FEILD_TRAN_AMOUNT = "tranAmount";
	public static final String METHOD_QUERY_ZY_TRANS_RECORD_FEILD_CURRENCY = "currency";
	public static final String METHOD_QUERY_ZY_TRANS_RECORD_FEILD_TRAN_STATUS = "tranStatus";
	public static final String METHOD_QUERY_ZY_TRANS_RECORD_FEILD_TRAN_REMARK = "remark";
	/** 接口：查询电子支付交易详情 */
	public static final String METHOD_QUERY_BOM_TRANS_DETAIL = "PsnEpayQueryPaymentDetail";
	public static final String METHOD_QUERY_BOM_TRANS_DETAIL_FEILD_TRANSACTION_ID = "transactionId";
	public static final String METHOD_QUERY_BOM_TRANS_DETAIL_FEILD_ACCOUNT_NUMBER = "accountNumber";
	public static final String METHOD_QUERY_BOM_TRANS_DETAIL_FEILD_ACCOUNT_NAME = "accountName";
	public static final String METHOD_QUERY_BOM_TRANS_DETAIL_FEILD_AMOUNT = "amount";
	public static final String METHOD_QUERY_BOM_TRANS_DETAIL_FEILD_MERCHANT_NO = "merchantNo";
	public static final String METHOD_QUERY_BOM_TRANS_DETAIL_FEILD_MERCHANT_NAME = "merchantName";
	public static final String METHOD_QUERY_BOM_TRANS_DETAIL_FEILD_AGREEMENT_NO = "agreementNo";
	public static final String METHOD_QUERY_BOM_TRANS_DETAIL_FEILD_ORDER_SEQ = "orderSeq";
	public static final String METHOD_QUERY_BOM_TRANS_DETAIL_FEILD_ORDER_NOTE = "orderNote";
	public static final String METHOD_QUERY_BOM_TRANS_DETAIL_FEILD_HOLDER_MERID = "holderMerId";
	public static final String METHOD_QUERY_BOM_TRANS_DETAIL_FEILD_EPAY_TIME = "epayTime";
	public static final String METHOD_QUERY_BOM_TRANS_DETAIL_FEILD_CHANNEL = "channl";
	public static final String METHOD_QUERY_BOM_TRANS_DETAIL_FEILD_STATUS = "status";
	public static final String METHOD_QUERY_BOM_TRANS_DETAIL_FEILD_REMARK = "remark";
	public static final String METHOD_QUERY_BOM_TRANS_DETAIL_FEILD_CURRENCY_CODE = "currencyCode";
	public static final String METHOD_QUERY_BOM_TRANS_DETAIL_FEILD_ACCOUNT_TYPE = "accountType";

	public static final Map<String, String> BOM_ORDER_STATUS = new HashMap<String, String>() {
		{
			put("A", "成功");
			put("B", "失败");
			put("K", "未明");
		}
	};

	public static final Map<String, String> OTHER_ORDER_STATUS = new HashMap<String, String>() {
		{
			put("0", "未支付");
			put("1", "支付成功");
			put("2", "已撤销");
			put("3", "已退货");
			put("4", "支付未明");
			put("5", "支付失败");
		}
	};

	public static final Map<String, String> OTHER_PAY_STATUS = new HashMap<String, String>() {
		{
			put("0", "未处理");
			put("1", "成功");
			put("2", "失败");
			put("3", "未明");
			put("4", "异常退款");
		}
	};
	public static final Map<String, String> ORDER_TYPES = new HashMap<String, String>() {
		{
			put("01", "支付");
			put("03", "退货");
			put("06", "退货");
		}
	};

	public static final Map<String, String> PAY_TYPES = new HashMap<String, String>() {
		{
			put("1", "网银支付");
			put("2", "中银快付");
			put("3", "理财直付");
			put("4", "协议支付");
		}
	};
	
	public static final String PUB_ZY_ACCOUNTS = "zyQueryAccountList";

	/**
	 * @ClassName: PaymentType
	 * @Description: 支付方式
	 * @author luql
	 * @date 2013-9-15 下午07:36:33
	 * 
	 */
	public static class PaymentType {
		/** 全部 */
		public static final String PAYMENT_ALL = "全部";
		/** 电子支付 */
		public static final String PAYMENT_TYPE_BOM = "电子支付";
		/** 中银快付 */
		public static final String PAYMENT_TYPE_ZY_QUCIK = "中银快付";
		/** 协议支付 */
		public static final String PAYMENT_TYPE_TREATY_QUCIK = "协议支付";

		public static LinkedHashMap<String, String> PAYMENT_TYPE__MAP = new LinkedHashMap<String, String>();
		static {
			PAYMENT_TYPE__MAP.put("0", PAYMENT_ALL);
			PAYMENT_TYPE__MAP.put("1", PAYMENT_TYPE_BOM);
			PAYMENT_TYPE__MAP.put("2", PAYMENT_TYPE_ZY_QUCIK);
			PAYMENT_TYPE__MAP.put("4", PAYMENT_TYPE_TREATY_QUCIK);
		}

		public static String[] getPaymentTypeList() {
			Collection<String> values = PAYMENT_TYPE__MAP.values();
			String[] sort = new String[values.size()];
			String[] result = values.toArray(sort);
			return result;
		}

	}

}
