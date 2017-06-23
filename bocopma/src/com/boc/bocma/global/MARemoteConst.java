package com.boc.bocma.global;

/**
 *	封装  网络访问的 url 
 */
public class MARemoteConst {

	/**
	 * 接口基础地址
	 */
//	public  static String URL_BASIC = "http://22.188.20.98:9081/BII/test.do";//307
	 public  static String URL_BASIC = "http://22.188.16.32:9083/BII/test.do";//（验密)
	
//	public static String URL_BASIC = "http://22.188.20.118:9088/BII/test.do";//401
//	 public final static String URL_BASIC = "http://22.188.20.117:9086/BII/test.do";//（验密)
//	public static String URL_BASIC = "http://22.188.130.126/BII/test.do";//
	
	public static void setBasicUrl(String url){
		URL_BASIC = url;
	}
	/**
	 * 企业Logo地址
	 */
	public final static String URL_LOGO = "http://22.188.20.118:9088/BII/EntCustLogoQuery.do";
	
	/**
	 * 版本更新
	 */
	public final static String URL_VERSION = "http://22.11.65.203:8080/BocMBCGate/update.action";

	/**
	 * 自助填单
	 */                                        
	public final static String URL_AUTOFILLBILL = "https://ebs.boc.cn/BocnetClient/autoFillBill.do";

	/**
	 * 回单验证
	 */
	public final static String URL_ENTHUIDANCHECK = "https://ebs.boc.cn/BocnetClient/entHuiDanCheck.do";
	
	/**
	 * 安全须知
	 */
	public final static String URL_SECURITY = "http://www.boc.cn/ebs/cn/security.html";
	
	/**
	 * 在线客服
	 */
	public final static String URL_ONLINESERVICE = "https://95566.boc.cn/";
	
	/**
	 * 电子银行章程
	 */
	public final static String URL_CONSTITUTION = "http://www.boc.cn/ebanking/service/cs1/200810/t20081022_989535.html";
	
	/**
	 * 网银业务规则
	 */
	public final static String URL_BUSINESSRULES = "http://www.boc.cn/ebanking/service/cs1/200810/t20081022_989533.html";
	
	public final class ErrorCode {
		public final static String SESSION_INVALID = "validation.session_invalid";
		public final static String ROLE_INVALID = "role.invalid_user";
		public final static String PRODUCTEXCEPTION = "ProductException";
	}

}
