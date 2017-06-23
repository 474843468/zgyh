package com.chinamworld.bocmbci.biz.epay.constants;

import java.util.HashMap;

public class TreatyConstants {
	/** 已签约商户 */
	public static final String PUB_FEILD_TREATY_MERCHANTS = "treatyRelationsMechants";
	public static final String PUB_FEILD_SELECTED_MERCHANT = "selectedMerchant";
	public static final String PUB_FEILD_TREATY_UN_MERCHANTS = "treatyUnRelationsMechants";

	/** 接口：查询已签约商户 */
	public static final String METHOD_QUERY_TREATY_RELATIONS = "PsnEpayQueryAgreementPaySignRelation";
	public static final String METHOD_QUERY_TREATY_RELATIONS_FIELD_LIST = "List";
	public static final String METHOD_QUERY_TREATY_RELATIONS_FIELD_IDENTITY_NUMBER = "identityNumber";
	public static final String METHOD_QUERY_TREATY_RELATIONS_FIELD_IDENTITY_TYPE = "identityType";
	public static final String METHOD_QUERY_TREATY_RELATIONS_FIELD_MERCHANT_NAME = "merchantName";
	public static final String METHOD_QUERY_TREATY_RELATIONS_FIELD_CARD_TYPE = "cardType";
	public static final String METHOD_QUERY_TREATY_RELATIONS_FIELD_BOC_NO = "bocNo";
	public static final String METHOD_QUERY_TREATY_RELATIONS_FIELD_HOLDER_MERID = "holderMerId";
	public static final String METHOD_QUERY_TREATY_RELATIONS_FIELD_CARD_NO = "cardNo";
	public static final String METHOD_QUERY_TREATY_RELATIONS_FIELD_AGREEMENT_ID = "agreementId";
	public static final String METHOD_QUERY_TREATY_RELATIONS_FIELD_HOLDER_NAME = "holderName";
	public static final String METHOD_QUERY_TREATY_RELATIONS_FIELD_SIGN_CHANNEL = "signChannel";
	public static final String METHOD_QUERY_TREATY_RELATIONS_FIELD_DAILY_QUOTA = "dailyQuota";
	public static final String METHOD_QUERY_TREATY_RELATIONS_FIELD_SIGN_DATE = "signDate";
	public static final String METHOD_QUERY_TREATY_RELATIONS_FIELD_STATUS = "status";
	public static final String METHOD_QUERY_TREATY_RELATIONS_FIELD_RECORD_NUMBER = "recordNumber";
	public static final String METHOD_QUERY_TREATY_RELATIONS_FIELD_MOBILE_NUMBER = "mobileNumber";
	public static final String METHOD_QUERY_TREATY_RELATIONS_FIELD_SIGN_VERIFY_TYPE = "signVerifyType";
	public static final String METHOD_QUERY_TREATY_RELATIONS_FIELD_VERIFY_MOBILE = "verifyMobile";
	// public static final String
	// METHOD_QUERY_TREATY_RELATIONS_FIELD_VERIFY_MODTERMINAL_FLAG =
	// "modTerminalFlag";
	public static final String METHOD_QUERY_TREATY_RELATIONS_FIELD_VERIFY_MODTERMINAL_FLAG = "signTerminalFlag";
	// /** * 查询签约关系详情*/
	// public static final String METHOD_QUERY_TREATY_RELATION_DETAIL =
	// "PsnEpayQuerySignRelationDetail";
	// public static final String
	// METHOD_QUERY_TREATY_RELATION_DETAIL_FIELD_AGREEMENT_SEQ = "agreementSeq";
	// public static final String
	// METHOD_QUERY_TREATY_RELATION_DETAIL_FIELD_IDENTITY_NUMBER =
	// "identityNumber";
	// public static final String
	// METHOD_QUERY_TREATY_RELATION_DETAIL_FIELD_IDENTITY_TYPE = "identityType";
	// public static final String
	// METHOD_QUERY_TREATY_RELATION_DETAIL_FIELD_MERCHANT_NAME = "merchantName";
	// public static final String
	// METHOD_QUERY_TREATY_RELATION_DETAIL_FIELD_CARD_TYPE = "cardType";
	// public static final String
	// METHOD_QUERY_TREATY_RELATION_DETAIL_FIELD_CARD_NO = "cardNo";
	// public static final String
	// METHOD_QUERY_TREATY_RELATION_DETAIL_FIELD_BOC_NO = "bocNo";
	// public static final String
	// METHOD_QUERY_TREATY_RELATION_DETAIL_FIELD_HOLDER_MERID = "holderMerId";
	// public static final String
	// METHOD_QUERY_TREATY_RELATION_DETAIL_FIELD_AGREEMENT_ID = "agreementId";
	// public static final String
	// METHOD_QUERY_TREATY_RELATION_DETAIL_FIELD_HOLDER_NAME = "holderName";
	// public static final String
	// METHOD_QUERY_TREATY_RELATION_DETAIL_FIELD_SIGN_CHANNEL = "signChannel";
	// public static final String
	// METHOD_QUERY_TREATY_RELATION_DETAIL_FIELD_DAILY_QUOTA = "dailyQuota";
	// public static final String
	// METHOD_QUERY_TREATY_RELATION_DETAIL_FIELD_SIGN_DATE = "signDate";
	// public static final String
	// METHOD_QUERY_TREATY_RELATION_DETAIL_FIELD_STATUS = "status";
	// public static final String
	// METHOD_QUERY_TREATY_RELATION_DETAIL_FIELD_MOBILE_NUMBER = "mobileNumber";
	// public static final String
	// METHOD_QUERY_TREATY_RELATION_DETAIL_FIELD_SIGN_VERIFY_TYPE =
	// "signVerifyType";
	// public static final String
	// METHOD_QUERY_TREATY_RELATION_DETAIL_FIELD_VERIFY_MOBILE = "verifyMobile";
	// public static final String
	// METHOD_QUERY_TREATY_RELATION_DETAIL_FIELD_VERIFY_MODTERMINAL_FLAG =
	// "modTerminalFlag";

	/** 接口：查询已签约商户 */
	public static final String METHOD_QUERY_MERCHANTS = "PsnEpayQueryAgreementPayMerchant";
	public static final String METHOD_QUERY_MERCHANTS_FIELD_MERCHANT_ID = "merchantId";
	public static final String METHOD_QUERY_MERCHANTS_FIELD_MERCHANT_NAME = "merchantName";
	public static final String METHOD_QUERY_MERCHANTS_FIELD_IS_DEBIT_CARD = "isDebitCard";
	public static final String METHOD_QUERY_MERCHANTS_FIELD_IS_QCCCARD = "isQccCard";
	public static final String METHOD_QUERY_MERCHANTS_FIELD_IS_CREDIT_CARD = "isCreditCard";
	public static final String METHOD_QUERY_MERCHANTS_FIELD_BOC_NO = "bocNo";
	/** 接口：查询银行端协议支付限额 */
	public static final String METHOD_QUERY_TREATY_QUOTA = "PsnEpayQueryAgreementQuota";
	public static final String METHOD_QUERY_TREATY_QUOTA_FIELD_SERVICE_ID = "serviceId";
	public static final String METHOD_QUERY_TREATY_QUOTA_FIELD_DAY_MAX_QUOTA = "dayMax";
	public static final String METHOD_QUERY_TREATY_QUOTA_FIELD_PER_MAX_QUOTA = "perMax";

	/** 接口：商户签约预交易 */
	public static final String METHOD_ADD_TREATY_MERCHANT_PRE = "PsnEpaySignAgreementPayConfirm";
	public static final String METHOD_ADD_TREATY_MERCHANT_PRE_FIELD_HOLDER_MERID = "holderMerId";
	public static final String METHOD_ADD_TREATY_MERCHANT_PRE_FIELD_INPUT_QUOTA = "inputQuota";
	public static final String METHOD_ADD_TREATY_MERCHANT_PRE_FIELD_SINGLE_QUOTA = "singleQuota";
	public static final String METHOD_ADD_TREATY_MERCHANT_PRE_FIELD_CN_NAME = "cnName";
	public static final String METHOD_ADD_TREATY_MERCHANT_PRE_FIELD_BOC_NO = "bocNo";
	public static final String METHOD_ADD_TREATY_MERCHANT_PRE_FIELD_ACCOUNT_NUMBER = "accountNumber";
	public static final String METHOD_ADD_TREATY_MERCHANT_PRE_FIELD_ACCOUNT_TYPE = "accountType";
	public static final String METHOD_ADD_TREATY_MERCHANT_PRE_FIELD_DAY_QUOTA = "dayQuota";
	public static final String METHOD_ADD_TREATY_MERCHANT_PRE_FIELD_USER_NAME = "userName";
	public static final String METHOD_ADD_TREATY_MERCHANT_PRE_FIELD_IDENTITY_TYPE = "identityType";
	public static final String METHOD_ADD_TREATY_MERCHANT_PRE_FIELD_IDENTITY_NUMBER = "identityNumber";
	public static final String METHOD_ADD_TREATY_MERCHANT_PRE_FIELD_MOBILE = "mobile";

	/** 接口：商户签约提交交易 */
	public static final String METHOD_ADD_TREATY_MERCHANT = "PsnEpaySignAgreementPay";
	public static final String METHOD_ADD_TREATY_MERCHANT_FIELD_HOLDER_MERID = "holderMerId";
	public static final String METHOD_ADD_TREATY_MERCHANT_FIELD_INPUT_QUOTA = "inputQuota";
	public static final String METHOD_ADD_TREATY_MERCHANT_FIELD_SINGLE_QUOTA = "singleQuota";
	public static final String METHOD_ADD_TREATY_MERCHANT_FIELD_CN_NAME = "cnName";
	public static final String METHOD_ADD_TREATY_MERCHANT_FIELD_BOC_NO = "bocNo";
	public static final String METHOD_ADD_TREATY_MERCHANT_FIELD_ACCOUNT_ID = "accountId";
	public static final String METHOD_ADD_TREATY_MERCHANT_FIELD_DAY_QUOTA = "dayQuota";
	public static final String METHOD_ADD_TREATY_MERCHANT_FIELD_USERNAME = "userName";
	public static final String METHOD_ADD_TREATY_MERCHANT_FIELD_IDENTITY_TYPE = "identityType";
	public static final String METHOD_ADD_TREATY_MERCHANT_FIELD_IDENTITY_NUMBER = "identityNumber";
	public static final String METHOD_ADD_TREATY_MERCHANT_FIELD_MOBILE = "mobile";

	/** 接口：修改协议支付交易限额预交易 */
	public static final String METHOD_MODIFY_MAX_QUOTA_PRE = "PsnEpayModifyAgreementPayConfirm";
	public static final String METHOD_MODIFY_MAX_QUOTA_PRE_FIELD_MERCHANT_NO = "merchantNo";
	public static final String METHOD_MODIFY_MAX_QUOTA_PRE_FIELD_AGREEMENT_ID = "agreementId";
	public static final String METHOD_MODIFY_MAX_QUOTA_PRE_FIELD_HOLDER_MER_ID = "holderMerId";
	public static final String METHOD_MODIFY_MAX_QUOTA_PRE_FIELD_MERCHANT_NAME = "merchantName";
	public static final String METHOD_MODIFY_MAX_QUOTA_PRE_FIELD_DAILY_QUOTA = "dailyQuota";
	public static final String METHOD_MODIFY_MAX_QUOTA_PRE_FIELD_NEW_DAILY_QUOTA = "newDailyQuota";
	public static final String METHOD_MODIFY_MAX_QUOTA_PRE_FIELD_ACCOUNT_NUMBER = "cardNo";
	public static final String METHOD_MODIFY_MAX_QUOTA_PRE_FIELD_ACCOUNT_TYPE = "cardType";
	public static final String METHOD_MODIFY_MAX_QUOTA_PRE_FIELD_BANK_DAILY_QUOTA = "bankDailyQuota";
	public static final String METHOD_MODIFY_MAX_QUOTA_PRE_FIELD_BANK_SINGLE_QUOTA = "bankSingleQuota";
	public static final String METHOD_MODIFY_MAX_QUOTA_PRE_FIELD_CURRENCY = "currency";

	/** 接口：修改协议支付交易限额 */
	public static final String METHOD_MODIFY_MAX_QUOTA = "PsnEpayModifyAgreementPay";
	public static final String METHOD_MODIFY_MAX_QUOTA_FIELD_MERCHANT_NO = "merchantNo";
	public static final String METHOD_MODIFY_MAX_QUOTA_FIELD_AGREEMENT_ID = "agreementId";
	public static final String METHOD_MODIFY_MAX_QUOTA_FIELD_HOLDER_MER_ID = "holderMerId";
	public static final String METHOD_MODIFY_MAX_QUOTA_FIELD_MERCHANT_NAME = "merchantName";
	public static final String METHOD_MODIFY_MAX_QUOTA_FIELD_DAILY_QUOTA = "dailyQuota";
	public static final String METHOD_MODIFY_MAX_QUOTA_FIELD_NEW_DAILY_QUOTA = "newDailyQuota";
	public static final String METHOD_MODIFY_MAX_QUOTA_FIELD_ACCOUNT_NUMBER = "cardNo";
	public static final String METHOD_MODIFY_MAX_QUOTA_FIELD_ACCOUNT_TYPE = "cardType";
	public static final String METHOD_MODIFY_MAX_QUOTA_FIELD_BANK_DAILY_QUOTA = "bankDailyQuota";
	public static final String METHOD_MODIFY_MAX_QUOTA_FIELD_BANK_SINGLE_QUOTA = "bankSingleQuota";
	public static final String METHOD_MODIFY_MAX_QUOTA_FIELD_CURRENCY = "currency";
	/** 接口：解约商户预交易 */
	public static final String METHOD_DELETE_RELATIONS_PRE = "PsnEpayRemoveAgreementPayConfirm";
	public static final String METHOD_DELETE_RELATIONS_PRE_FIELD_MERCHANT_NO = "merchantNo";
	public static final String METHOD_DELETE_RELATIONS_PRE_FIELD_AGREEMENT_ID = "agreementId";
	public static final String METHOD_DELETE_RELATIONS_PRE_FIELD_HOLDER_MER_ID = "holderMerId";
	public static final String METHOD_DELETE_RELATIONS_PRE_FIELD_MERCHANT_NAME = "merchantName";
	public static final String METHOD_DELETE_RELATIONS_PRE_FIELD_CARD_NO = "cardNo";
	public static final String METHOD_DELETE_RELATIONS_PRE_FIELD_CARD_TYPE = "cardType";
	public static final String METHOD_DELETE_RELATIONS_PRE_FIELD_DAILY_QUOTA = "dailyQuota";
	public static final String METHOD_DELETE_RELATIONS_PRE_FIELD_SIGN_DATE = "signDate";
	/** 接口：解约商户 */
	public static final String METHOD_DELETE_RELATIONS = "PsnEpayRemoveAgreementPay";
	public static final String METHOD_DELETE_RELATIONS_FIELD_MERCHANT_NO = "merchantNo";
	public static final String METHOD_DELETE_RELATIONS_FIELD_AGREEMENT_ID = "agreementId";
	public static final String METHOD_DELETE_RELATIONS_FIELD_HOLDER_MER_ID = "holderMerId";
	public static final String METHOD_DELETE_RELATIONS_FIELD_MERCHANT_NAME = "merchantName";
	public static final String METHOD_DELETE_RELATIONS_FIELD_CARD_NO = "cardNo";
	public static final String METHOD_DELETE_RELATIONS_FIELD_CARD_TYPE = "cardType";
	public static final String METHOD_DELETE_RELATIONS_FIELD_DAILY_QUOTA = "dailyQuota";
	public static final String METHOD_DELETE_RELATIONS_FIELD_SIGN_DATE = "signDate";

	/**
	 * 签约状态
	 */
	public static final HashMap<String, String> TREATY_STATUS = new HashMap<String, String>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = -1177233894053123818L;

		{
			put("V", "有效");
			put("D", "已解约");
			put("0", "初始状态");
		}
	};

	/**
	 * 签约途径
	 */
	public static final HashMap<String, String> TREATY_CHANNEL = new HashMap<String, String>() {

		/**
		 * 
		 */
		private static final long serialVersionUID = 6607011439270388232L;

		{
			put("1", "电子银行");
			put("2", "柜台端");
			put("4", "商户端");
			put("8", "批量端");

		}
	};

	/**
	 * 签约途径
	 */
	public static final HashMap<String, String> TREATY_AGENT = new HashMap<String, String>() {

		/**
		 * 
		 */
		private static final long serialVersionUID = 6607011439270388232L;

		{
			put("WEB15", "经典风格网银");
			put("Q-IPAD", "IPAD支付客户端");
			put("Q-APAD", "Android PAD银行客户端");
			put("Q-IOS", "Iphone支付客户端");
			put("Q-ANDR", "Android支付客户端");
			put("Q-WPAD", "win8 PAD支付客户端");
			put("Q-WIN8", "win8支付客户端");
			put("IPAD", "IPAD银行客户端");
			put("M-WAP", "移动WAP网页");
			put("M-ANDR", "手机银行Android客户端");
			put("M-IOS", "手机银行Iphone客户端");
			put("M-WP7", "手机银行win7客户端");
			put("WEB20", "个性风格网银");
			put("H-TV", "家居电视银行");
			put("APAD", "Android PAD银行客户端");
			put("X-IOS", "手机银行Iphone客户端");
			put("X-ANDR", "手机银行Android客户端");
			put("X-WIN8", "手机银行wp8客户端");
			put("B-IOS", "移动浏览器IOS手机");
			put("B-ANDR", "移动浏览器安卓手机");
			put("B-WIN", "移动浏览器WINDOWS手机");

		}
	};
	/**
	 * 终端
	 */
	public static final HashMap<String, String> TREATY_CHANNL = new HashMap<String, String>() {

		/**
		 * 
		 */
		private static final long serialVersionUID = 6607011439270388232L;

		{
			put("1", "网银银行");
			put("2", "手机银行");
			put("4", "家居银行");
			put("6", "银企对接");

		}
	};

}
