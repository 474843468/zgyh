package com.boc.bocma.global;

/**
 * 开放平台服务器URL
 */
public class OPURL {
	private static final int SIT_MODE = 1;
	private static final int PRODUCT_MODE = 2;
	private static final int APP_MODE = PRODUCT_MODE;
//	private static final int APP_MODE = SIT_MODE;//打版修改，2016.3.9，lgw

	public static String APPKEY;
	public static String APPSECRET;

	private static final String SIT_APPKEY = "146";
	private static final String SIT_APPSECRET = "1661d2fdd1017960efc65db279d0c322110a1bd4538d0a6ff8";

	private static final String PRODUCT_APPKEY = "146";
	private static final String PRODUCT_APPSECRET = "1661d2fdd1017960efc65db279d0c322110a1bd4538d0a6ff8";

	/**
	 * 登录url和端口
	 */
	public static String LOGIN_IP;
	public static int LOGIN_PORT;
	/**
	 * 注册地址
	 */
	public static String REGISTER_URL;

	// private static final String SIT_LOGIN_IP = "http://22.188.12.156";
	private static final String SIT_LOGIN_IP = "http://22.188.12.160";
	// private static final int SIT_LOGIN_PORT = 9080;
	private static final int SIT_LOGIN_PORT = 8080;
	private static final String SIT_REGISTER_URL = "http://22.188.146.47/wap/register.php";

	private static final String PRODUCT_LOGIN_IP = "https://openapi.boc.cn";
	private static final int PRODUCT_LOGIN_PORT = 443;
	private static final String PRODUCT_REGISTER_URL = "http://open.boc.cn/wap/register.php";

	static {
		switch (APP_MODE) {
		case SIT_MODE:
			APPKEY = SIT_APPKEY;
			APPSECRET = SIT_APPSECRET;

			LOGIN_IP = SIT_LOGIN_IP;
			LOGIN_PORT = SIT_LOGIN_PORT;

			REGISTER_URL = SIT_REGISTER_URL;
			break;
		case PRODUCT_MODE:
			APPKEY = PRODUCT_APPKEY;
			APPSECRET = PRODUCT_APPSECRET;

			LOGIN_IP = PRODUCT_LOGIN_IP;
			LOGIN_PORT = PRODUCT_LOGIN_PORT;

			REGISTER_URL = PRODUCT_REGISTER_URL;
			break;
		default:
			throw new IllegalArgumentException();
		}

	}

	/**
	 * SIT测试FAA url
	 */
	// private static final String FAA_BASE_URL_IP =
	// "http://22.188.36.194:8080/";
	// private static final String FAA_BASE_URL_IP =
	// "http://22.188.37.137:8080/";//打版修改地址，2016.3.9，lgw 旧接口地址
	
	// D6 打版修改地址，2016.3.9，lgw 新接口地址
	//private static final String FAA_BASE_URL_IP = "http://22.188.14.39:8080/";
//	private static final String FAA_BASE_URL_IP = "http://22.188.36.194:8080/";// T2
//	private static final String FAA_BASE_URL_IP = "http://22.188.37.137:8080/";// T5
	private static final String FAA_BASE_URL_IP = "http://22.188.36.180:8080/";
	/**
	 * 生产环境FAA url
	 */
	private static final String PRODUCT_FAA_BASE_URL = "https://openapi.boc.cn/";

	public static final String getFAABaseTransUrl() {
		String faaUrl;
		switch (APP_MODE) {
		case SIT_MODE:
			faaUrl = FAA_BASE_URL_IP + "base/unlogin/";
			break;
		case PRODUCT_MODE:
			faaUrl = PRODUCT_FAA_BASE_URL + "base/unlogin/";
			break;
		default:
			throw new IllegalArgumentException();
		}
		return faaUrl;
	}

	public static final String remoteOpenAccountUrl() {
		String faaUrl;
		switch (APP_MODE) {
		case SIT_MODE:
			faaUrl = FAA_BASE_URL_IP + "remote_open_account/";
			break;
		case PRODUCT_MODE:
			faaUrl = PRODUCT_FAA_BASE_URL + "remote_open_account/";
			break;
		default:
			throw new IllegalArgumentException();
		}
		return faaUrl;
	}

}
