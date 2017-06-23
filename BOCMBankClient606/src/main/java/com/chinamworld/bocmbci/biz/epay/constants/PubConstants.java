package com.chinamworld.bocmbci.biz.epay.constants;

public class PubConstants {

	/** 菜单字段名称  */
	public static final String MAIN_FIELD_MENU_NAME = "menu_name";
	public static final String MAIN_FIELD_MENU_CLASS = "menu_class";
	public static final String MAIN_FIELD_MENU_ITEM_ONCLICK_LISTENER = "menu_item_onclickListener";
	
	/** 公共参数字段名 */
	public static final String PUB_FIELD_TOKEN_ID = "token";
	public static final String PUB_FIELD_CONVERSATION_ID = "conversationId";
	public static final String PUB_FIELD_SERVICE_ID = "serviceId";
	public static final String PUB_FIELD_ISSELECTED = "isSelected";
	public static final String PUB_FIELD_FACTORLIST = "factorList";
	public static final String PUB_FIELD_OTP = "Otp";
	public static final String PUB_FIELD_SMC = "Smc";
	public static final String PUB_FIELD_USBKEY = "";
	public static final String PUB_FIELD_OTP_RC = "Otp_RC";
	public static final String PUB_FIELD_SMC_RC = "Smc_RC";
	public static final String PUB_FIELD_COMBIN_ID = "_combinId";
	public static final String PUB_FILED_ALL_ACCOUNT_LIST = "allAccountList";
	public static final String PUB_FILED_SMC_AVALIABLE_TIME = "smcTrigerInterval";
	
	public static final String PUB_QUERY_FEILD_IS_REFRESH = "_refresh";
	public static final String PUB_QUERY_FEILD_CURRENT_INDEX = "currentIndex";
	public static final String PUB_QUERY_FEILD_PAGE_SIZE = "pageSize";
	public static final String PUB_QUERY_PAY_METHODS = "payMethods";
	public static final String PUB_QUERY_ACCOUNT_IDS = "accountIds";
	
	/** 左侧菜单名称 */
	public static final String MENU_LEFT_ONE = "我的支付服务";
	public static final String MENU_LEFT_TWO = "支付交易查询";
	
	/** 标题名称 */
	public static final String TITLE_MAIN = "我的支付服务";
	public static final String TITLE_BOANK_OF_MOBILE = "电子支付";
	public static final String TITLE_WITHOUT_CARD = "银联跨行无卡支付";
	public static final String TITLE_TREATY = "协议支付";
	public static final String TITLE_TRANS_QUERY = "支付交易查询";
	
	
	public static final String TITLE_SECOND_MAIN = "银联跨行支付业务开通关闭";
	public static final String TITLE_SECOND_PAY = "银联在线支付";
	public static final String TITLE_SECOND_RECEVICE = "银联跨行代扣";
	/**接口：支付开通-查询所有账户列表*/
	public static final String METHOD_QUERY_ALL_ACCOUNT = "PsnCommonQueryAllChinaBankAccount";
	public static final String METHOD_QUERY_ALL_ACCOUNT_FIELD_ACCOUNT_TYPE = "accountType";
	public static final String METHOD_QUERY_ALL_ACCOUNT_FIELD_NICKNAME = "nickName";
	public static final String METHOD_QUERY_ALL_ACCOUNT_FIELD_ACCOUNT_NUMBER = "accountNumber";
	public static final String METHOD_QUERY_ALL_ACCOUNT_FIELD_ACCOUNT_ID = "accountId";
	public static final String METHOD_QUERY_ALL_ACCOUNT_FIELD_ACCOUNT_NAME = "accountName";
	/**接口：支付开通-查询账户详情*/
	public static final String METHOD_QUERY_ACCOUNT_DETAIL = "PsnAccountQueryAccountDetail";
	public static final String METHOD_QUERY_ACCOUNT_DETAIL_FIELD_CURRENCY_CODE = "currencyCode";
	public static final String METHOD_QUERY_ACCOUNT_DETAIL_FIELD_ACCOUNT_DETAIL_LIST = "accountDetaiList";
	public static final String METHOD_QUERY_ACCOUNT_DETAIL_FIELD_BOOK_BALANCE = "bookBalance";
	public static final String METHOD_QUERY_ACCOUNT_DETAIL_FIELD_AVAILABLE_BALANCE = "availableBalance";
	
	/**接口：查询用户自设交易限额*/
	public static final String METHOD_QUERY_CUST_MAX_QUOTA = "PsnSVRLimitQuery";
	public static final String METHOD_QUERY_CUST_MAX_QUOTA_FIELD_SERVICE_NAME = "serviceName";
	public static final String METHOD_QUERY_CUST_MAX_QUOTA_FIELD_SECURITY = "security";
	public static final String METHOD_QUERY_CUST_MAX_QUOTA_FIELD_MAX_AMOUNT = "maxAmount";
	public static final String METHOD_QUERY_CUST_MAX_QUOTA_FIELD_AMOUNT = "amount";
	public static final String METHOD_QUERY_CUST_MAX_QUOTA_FIELD_DAYMAX = "dayMax";
	public static final String METHOD_QUERY_CUST_MAX_QUOTA_FIELD_PERMAX = "perMax";
	public static final String METHOD_QUERY_CUST_MAX_QUOTA_FIELD_UPDATE_FLAG = "updateFlag";
	
	/**接口-获取安全因子*/
	public static final String METHOD_GET_SECURITY_FACTOR = "PsnGetSecurityFactor";
	public static final String METHOD_GET_SECURITY_FACTOR_FIELD = "field";
	public static final String METHOD_GET_SECURITY_FACTOR_FIELD_NAME = "name";
	public static final String METHOD_GET_SECURITY_FACTOR_FIELD_TYPE = "type";
	
	/**接口-获取安全因子*/
	public static final String METHOD_GET_SYSTEM_TIME = "PsnCommonQuerySystemDateTime";
	public static final String METHOD_GET_SYSTEM_TIME_FIELD_DATE_TIME = "dateTme";
	
	public static final String METHOD_GET_RANDOM_KEY = "PSNGetRandom";

	public static final String CONTEXT_WITHOUT_CARD = "withoutCardPaymentService";
	public static final String CONTEXT_TQ = "transQueryService";
	public static final String CONTEXT_BOM = "bomPaymentService";
	public static final String CONTEXT_TREATY = "treatyService";
	
	public static final String CONTEXT_FIELD_DREDGED_LIST = "contextDredgedList";
	public static final String CONTEXT_FIELD_DELETE_ACCOUNT_ID = "contextDeleteAccountId";
	public static final String CONTEXT_FIELD_ALL_ACCLIST = "contextAllAccountList";
	public static final String CONTEXT_FIELD_SELECTED_ACCLIST = "contextSelectedAccountList";
	public static final String CONTEXT_FIELD_UN_DREDGED_ACCOUNT_LIST = "contextNotDredgedAccountLIst";
	public static final String CONTEXT_FIELD_SELECTED_ACC = "contextSelecteAccount";
	public static final String CONTEXT_FIELD_SELECTED_MERCHANT = "contextSelecteMerchant";
	
}
