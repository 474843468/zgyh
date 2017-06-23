package com.chinamworld.bocmbci.biz.epay.constants;

public class BomConstants {

	/**接口：判断是否已开通电子支付*/
	public static final String METHOD_IS_OPENONLINE_PAYMENT = "PsnEpayIsOpenOnlinePayment";
	
	/**接口：开通预交易*/
	public static final String METHOD_OPEN_SERVICE_PRE = "PsnEpayOpenOnlineServiceFullPre";
	public static final String METHOD_OPEN_SERVICE_PRE_FIELD_QUOTA = "quota";
	public static final String METHOD_OPEN_SERVICE_PRE_FIELD_COMBINID = "_combinId";
	/**接口：获取ConversationId*/
	public static final String METHOD_GET_CONVERSTATION_ID = "PSNCreatConversation";
	/**接口：获取TokenId*/
	public static final String METHOD_GET_TOKEN_ID = "PSNGetTokenId";
	/**接口：关闭支付服务*/
	public static final String METHOD_CLOSE_PAYMENT_SEVICE = "PsnEpayCloseOnlineService";
	/**接口：关闭支付账户*/
	public static final String METHOD_CLOSE_ACC_PAYMENT_SERVICE = "PsnEpayCancelOnlinePaymentAccount";
	public static final String METHOD_CLOSE_ACC_PAYMENT_SERVICE_FIELD_ACCOUNT_ID = "accountId";
	public static final String METHOD_CLOSE_ACC_PAYMENT_SERVICE_FIELD_TOKEN_ID = "token";
	/**接口：设置限额预交易*/
	public static final String METHOD_SET_MAX_QUOTA_PRE = "PsnEpaySetEpayQuotaPre";
	public static final String METHOD_SET_MAX_QUOTA_PRE_FIELD_QUOTA = "quota";
	public static final String METHOD_SET_MAX_QUOTA_PRE_FIELD_COMBIN_ID = "_combinId";
	
	/**接口：设置交易限额*/
	public static final String METHOD_SET_MAX_QUOTA = "PsnEpaySetEpayQuota";
	public static final String METHOD_SET_MAX_QUOTA_FIELD_QUOTA = "quota";
	
	/**接口：支付开通-查询系统设置限额*/
	public static final String METHOD_QUERY_MAX_QUOTA = "PsnEpayQueryEpayQuota";
	public static final String METHOD_QUERY_MAX_QUOTA_FIELD_DAY_MAX = "dayMax";
	public static final String METHOD_QUERY_MAX_QUOTA_FIELD_PER_MAX = "perMax";
	public static final String METHOD_QUERY_MAX_QUOTA_FIELD_OBLIGATE_MSG = "obligateMsg";
	/**接口：查询已开通支付账户列表*/
	public static final String METHOD_QUERY_DREDGED_ACC_LIST = "PsnEpayGetOpenOnlineServiceAccount";
	public static final String METHOD_QUERY_DREDGED_ACC_LIST_FIELD_ACC_NUMBER = "accountNumber";
	public static final String METHOD_QUERY_DREDGED_ACC_LIST_FIELD_ACC_ID = "accountId";
	public static final String METHOD_QUERY_DREDGED_ACC_LIST_FIELD_ACC_TYPE = "accountType";
	public static final String METHOD_QUERY_DREDGED_ACC_LIST_FIELD_ACC_NICKNAME = "nickname";
	public static final String METHOD_QUERY_DREDGED_ACC_LIST_FIELD_ACC_IBKNUM = "accountIbkNum";
	public static final String METHOD_QUERY_DREDGED_ACC_LIST_FIELD_TERMINAL_FLAG = "terminalflag";
	
	/**接口-开通手机支付服务*/
	public static final String METHOD_OPEN_PAYMENT_SERVICE = "PsnEpayOpenOnlineServiceFull";
	public static final String METHOD_OPEN_PAYMENT_SERVICE_FIELD_LOGIN_HINT = "loginHint";
	public static final String METHOD_OPEN_PAYMENT_SERVICE_FIELD_CHECK_ACCTS = "checkedAccts";
	public static final String METHOD_OPEN_PAYMENT_SERVICE_FIELD_QUOTA = "quota";
	
	/**接口-开通手机支付账户*/
	public static final String METHOD_SET_PAYMENT_SERVICE_ACC_PRE = "PsnEpaySetOnlinePaymentAccountPre";
	
	/**接口-开通手机支付账户*/
	public static final String METHOD_SET_PAYMENT_SERVICE_ACC = "PsnEpaySetOnlinePaymentAccount";
	public static final String METHOD_SET_PAYMENT_SERVICE_ACC_FIELD_CHECK_ACCTS = "checkedAccts";

}
