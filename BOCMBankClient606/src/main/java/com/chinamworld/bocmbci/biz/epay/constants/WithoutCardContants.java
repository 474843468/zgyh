package com.chinamworld.bocmbci.biz.epay.constants;

public class WithoutCardContants {
	
	/**接口：判断是否开通无卡支付*/
	public static final String METHOD_IS_OPEN_NC_PAY = "PsnNcpayServiceChoose";
	public static final String METHOD_IS_OPEN_NC_PAY_FIELD_ACCOUNT_ID = "accountId";
	public static final String METHOD_IS_OPEN_NC_PAY_FIELD_STATUS = "status";//银联无卡自助消费服务状态 N：未开通,  Y：开通
//	public static final String METHOD_IS_OPEN_NC_PAY_FIELD_IDENTITYNUM = "identityNum";//证件号
//	public static final String METHOD_IS_OPEN_NC_PAY_FIELD_IDENTITYTYPE = "identityType";//证件类型
	public static final String METHOD_IS_OPEN_NC_PAY_FIELD_QUOTA = "quota";//银联无卡自助消费核心限额
	public static final String METHOD_IS_OPEN_NC_PAY_FIELD_ACCNUM = "acctNum";//卡号
	public static final String METHOD_IS_OPEN_NC_PAY_FIELD_CUSTNAM = "custNam";//客户姓名
//	public static final String METHOD_IS_OPEN_NC_PAY_FIELD_PHONE = "phone";//核心预留手机号
//	public static final String METHOD_IS_OPEN_NC_PAY_FIELD_CIF = "cif";//核心客户号
	public static final String METHOD_IS_OPEN_NC_PAY_TRUE = "1";
	public static final String METHOD_IS_OPEN_NC_PAY_FALSE = "0";
	
	public static final String METHOD_IS_OPEN_NC_PAY_FIELD_CARDNAME = "cardName";//卡产品名称
	public static final String METHOD_IS_OPEN_NC_PAY_FIELD_COLLECTSTATUS = "collectStatus";//银联代收服务状态
	public static final String METHOD_IS_OPEN_NC_PAY_FIELD_COLLECTQUOTA = "collectQuota";//银联代收核心限额
	//N：未开通,  Y：开通
	public static final String METHOD_IS_OPEN_NC_PAY_YES = "Y";
	public static final String METHOD_IS_OPEN_NC_PAY_NO = "N";
	
	/**接口：开通无卡支付预交易*/
	public static final String METHOD_OPEN_NC_PAY_PRE = "PsnNcpayOpenPre";
	public static final String METHOD_OPEN_NC_PAY_PRE_FIELD_ACCOUNT_ID = "accountId";//账户流水号
	public static final String METHOD_OPEN_NC_PAY_PRE_FIELD_ACCOUNT_NUMBER = "accountNumber";//支付卡号
	public static final String METHOD_OPEN_NC_PAY_PRE_FIELD_TRANDATE = "tranDate";//交易日期
	public static final String METHOD_OPEN_NC_PAY_PRE_FIELD_OPERATE_TYPE = "operateType";//交易类型
	public static final String METHOD_OPEN_NC_PAY_PRE_FIELD_CUSTOMER_NAME = "customerName";//客户姓名
	public static final String METHOD_OPEN_NC_PAY_PRE_FIELD_CURRENT_QUOTA = "currentQuota";//当前交易限额
	public static final String METHOD_OPEN_NC_PAY_PRE_FIELD_SERVICE_TYPE = "serviceType";//服务类别1：银联无卡自助消费2：银联代收

	/**接口：开通无卡支付提交交易*/
	public static final String METHOD_OPEN_NC_PAY = "PsnNcpayOpenSubmit";
	public static final String METHOD_OPEN_NC_PAY_FIELD_ACCOUNT_ID = "accountId";
	public static final String METHOD_OPEN_NC_PAY_FIELD_ACCOUNT_NUMBER = "accountNumber";
	public static final String METHOD_OPEN_NC_PAY_FIELD_TRANDATE = "tranDate";
	public static final String METHOD_OPEN_NC_PAY_FIELD_OPERATE_TYPE = "operateType";
	public static final String METHOD_OPEN_NC_PAY_FIELD_CUSTOMER_NAME = "customerName";
	public static final String METHOD_OPEN_NC_PAY_FIELD_CLIENT_NAME = "clientName";
	public static final String METHOD_OPEN_NC_PAY_FIELD_CURRENT_QUOTA = "currentQuota";
	public static final String METHOD_OPEN_NC_PAY_FIELD_TELPHONE = "telphone";
	public static final String METHOD_OPEN_NC_PAY_FIELD_IDENTITY_TYPE = "identityType";
	public static final String METHOD_OPEN_NC_PAY_FIELD_IDENTITY_NUMBER = "identityNumber";
	public static final String METHOD_OPEN_NC_PAY_FIELD_CUST_CIF = "custCif";
	public static final String METHOD_OPEN_NC_PAY_FIELD_STATUS = "status";
	
	/**接口：开通无卡支付预交易*/
	public static final String METHOD_MODIFY_QUOTA_PRE = "PsnNcpayQuotaModifyPre";
	public static final String METHOD_MODIFY_QUOTA_PRE_FIELD_ACCOUNT_ID = "accountId";
	public static final String METHOD_MODIFY_QUOTA_PRE_FIELD_ACCOUNT_NUMBER = "accountNumber";
	public static final String METHOD_MODIFY_QUOTA_PRE_FIELD_TRANDATE = "tranDate";
	public static final String METHOD_MODIFY_QUOTA_PRE_FIELD_OPERATE_TYPE = "operateType";
	public static final String METHOD_MODIFY_QUOTA_PRE_FIELD_CUSTOMER_NAME = "customerName";
	public static final String METHOD_MODIFY_QUOTA_PRE_FIELD_CURRENT_QUOTA = "currentQuota";
	
	/**接口：开通无卡支付提交交易*/
	public static final String METHOD_MODIFY_QUOTA = "PsnNcpayQuotaModifySubmit";
	public static final String METHOD_MODIFY_QUOTA_FIELD_ACCOUNT_ID = "accountId";
	public static final String METHOD_MODIFY_QUOTA_FIELD_ACCOUNT_NUMBER = "accountNumber";
	public static final String METHOD_MODIFY_QUOTA_FIELD_TRANDATE = "tranDate";
	public static final String METHOD_MODIFY_QUOTA_FIELD_OPERATE_TYPE = "operateType";
	public static final String METHOD_MODIFY_QUOTA_FIELD_CUSTOMER_NAME = "customerName";
	public static final String METHOD_MODIFY_QUOTA_FIELD_CLIENT_NAME = "clientName";
	public static final String METHOD_MODIFY_QUOTA_FIELD_CURRENT_QUOTA = "currentQuota";
	public static final String METHOD_MODIFY_QUOTA_FIELD_TELPHONE = "telphone";
	public static final String METHOD_MODIFY_QUOTA_FIELD_IDENTITY_TYPE = "identityType";
	public static final String METHOD_MODIFY_QUOTA_FIELD_IDENTITY_NUMBER = "identityNumber";
	public static final String METHOD_MODIFY_QUOTA_FIELD_CUST_CIF = "custCif";
	public static final String METHOD_MODIFY_QUOTA_FIELD_STATUS = "status";
	
	/**接口：关闭无卡支付账户预交易*/
	public static final String METHOD_CLOSE_WC_ACC_PRE = "PsnNcpayClosePre";
	public static final String METHOD_CLOSE_WC_ACC_PRE_FIELD_ACCOUNT_ID = "accountId";
	
	/**接口：开通无卡支付提交交易*/
	public static final String METHOD_CLOSE_WC_ACC = "PsnNcpayCloseSubmit";
	public static final String METHOD_CLOSE_WC_ACC_FIELD_ACCOUNT_ID = "accountId";
	public static final String METHOD_CLOSE_WC_ACC_FIELD_CLIENT_NAME = "clientName";
	public static final String METHOD_CLOSE_WC_ACC_FIELD_CURRENT_QUOTA = "currentQuota";
	public static final String METHOD_CLOSE_WC_ACC_FIELD_TELPHONE = "telphone";
	public static final String METHOD_CLOSE_WC_ACC_FIELD_IDENTITY_TYPE = "identityType";
	public static final String METHOD_CLOSE_WC_ACC_FIELD_IDENTITY_NUMBER = "identityNumber";
	public static final String METHOD_CLOSE_WC_ACC_FIELD_CUST_CIF = "custCif";
	public static final String METHOD_CLOSE_WC_ACC_FIELD_STATUS = "status";
	
	public static final String METHOD_QUERY_WITHOUTCARD_QUOTA = "PsnQueryBnmsQuota";
	
	//客户手机号 
	public static final String GIF_MOBILE = "cifMobile";
	
}
