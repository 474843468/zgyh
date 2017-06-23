package com.chinamworld.bocmbci.constant;

/**
 * @ClassName: SystemConfig
 * @Description: 系统配置文件
 * @author luql
 * @date 2013-12-18 上午10:31:27
 */
public class SystemConfig {
	/**
	 * 
	 * 当前环境:
	 * 
	 * DEV - 本地开发环境 </p> SIT - SIT环境 </p> UAT - UAT环境 </p> PPT - PPT环境 </p> PRD
	 * - 生产环境 </p>
	 * 
	 */
	public final static String ENV_DEV = "DEV";// 本地开发环境
	public final static String ENV_SIT = "SIT";// SIT
	public final static String ENV_UAT = "UAT";// UAT
	public final static String ENV_PPT = "PPT";// PPT
	public final static String ENV_PRD = "PRD";// 生产
	// ----------------------------------------------------------------------------------------------

	// 系统地址配置
	/** 配置模式 */
	public final static String ENV = ENV_UAT;// 默认选择生产环境
	public final static boolean ISCHECKVERSION = false; //是否检测版本更新。
	public final static boolean IS_SPLIDE_DEX =false; //是否分包。

//	/** BII地址 */
//	public static String BASE_HTTP_URL = "https://22.188.151.87/BII/"; //T2验密地址
//	public static String BASE_HTTP_URL = "http://22.188.130.126/BII/"; //T1地址
	public static String BASE_HTTP_URL = "https://22.188.135.115/BII/";  //新T1环境 - 20160823
//	public static String BASE_HTTP_URL = "https://22.188.151.167:8042/BII//";  //新T5环境 用于换肤积利金测试
//	public static String BASE_HTTP_URL = "http://22.18.61.188:9194/BIISimulate/";//广源
//	public static String BASE_HTTP_URL = "https://22.188.151.167:10002/"; //T5地址
//	public static String BASE_HTTP_URL = "https://22.188.135.115/BII/"; //T1加速器地址
//	public static String BASE_HTTP_URL = "http://22.188.20.26:9081/BII/"; //
//	public static String BASE_HTTP_URL = "https://22.188.135.115/BII/"; //ssl
//	public static String BASE_HTTP_URL = "http://180.168.146.75/BII/"; //T1外网地址
//	public static final String BASE_HTTP_URL = "https://ebsnew.boc.cn/BII/";	//投产地址
//	public static String BASE_HTTP_URL = "http://22.188.20.26:9083/BII/"; 
//	public static String BASE_HTTP_URL = "http://22.188.20.241:9081/BII/"; //UAT地址
//	public static final String BASE_HTTP_URL = "https://ebsnew.boc.cn/BII/";	//投产地址
//	public static String BASE_HTTP_URL = "http://22.188.178.31:9083/BII/"; //T3地址
//	public static String BASE_HTTP_URL = "http://22.188.137.21:9092/BII/"; //U2地址
//	public static String BASE_HTTP_URL = "http://22.188.137.21:8189/BII/"; //T2
//	public static final String BASE_HTTP_URL = "https://ebsnewppt.boc.cn:6443/BII/";	////投产演练地址(域名)
//	public static final String BASE_HTTP_URL = "125.35.4.77:6443/BII/";	//投产演练地址(IP)
	

	/** 消息推送地址 */
//	T1:22.188.32.128:38080
//	T2:22.188.181.69:38080
//	T3:22.188.178.51:38080
//	T4:22.188.24.91:38080

//	public static String BASE_PUSH_RUL = "http://22.188.20.33:38080/PNSServletModule/PNSServerForApp";
//	public static String BASE_PUSH_RUL = "https://smsp.mbs.boc.cn/PNSServletModule/PNSServerForApp";	//投产地址
//	public static String BASE_PUSH_RUL = "http://22.188.32.128:38080/PNSServletModule/PNSServerForApp";	//603 消息推送平台地址
	public static String BASE_PUSH_RUL = "http://22.188.181.69:38080/PNSServletModule/PNSServerForApp";	//T2 消息推送平台地址
//	public static String BASE_PUSH_RUL = "https://smsppex.mbs.boc.cn/PNSServletModule/PNSServerForApp";	//投产演练地址orApp";
//	public static String BASE_PUSH_RUL = "http://22.18.61.188:9194/BIISimulate//_bfwajax.do";	//测试地址
	/** 基金地址 */
	public static String FINCADDRESS = "https://ebsnew.boc.cn/EBankingInfoSvc/";
	/** 功能外置地址 */
//	public static String Outlay_API_URL = "http://22.188.132.27:9081/BMPS/_bfwajax.do";// "http://22.11.97.246:9081/BMPS";// ;//
//	public static String Outlay_API_URL = "http://22.188.132.27:9092/BMPS/_bfwajax.do"; //测试地址

//	public static String Outlay_API_URL = "http://22.188.137.180:9081/BMPS/_bfwajax.do"; //601测试地址
//	public static String Outlay_API_URL = "http://22.188.132.27:9092/BMPS/_bfwajax.do"; //P603中银理财测试地址
//	public static String Outlay_API_URL = "http://22.11.147.90:8080/BMPS/_bfwajax.do"; //P605积利金联调外置地址
//	public static String Outlay_API_URL = "http://22.188.34.110:9083/BMPS/_bfwajax.do"; //P605外置地址
	public static String Outlay_API_URL = "http://22.188.132.27:9092/BMPS/_bfwajax.do"; //P606外置地址
//	public static String Outlay_API_URL = "https://ebsnew.boc.cn/BII/"; //投产地址
//	public static String Outlay_API_URL = "http://22.188.137.180:9081/BMPS/_bfwajax.do"; //601测试地址
//	public static String Outlay_API_URL = "http://22.188.132.27:9092/BMPS/_bfwajax.do"; //P603中银理财测试地址
//	"http://22.188.132.27:9092/BMPS/_bfwajax.do"
//	public static String Outlay_API_URL = "http://22.188.132.27:9092/BMPS/_bfwajax.do";

	/** 广源挡板 */
//	public static String Outlay_API_URL = "http://22.18.61.188:9194/BIISimulate//_bfwajax.do";
//	public static String Outlay_API_URL = "http://22.188.137.180:9081/BMPS/_bfwajax.do";
//	public static String Outlay_API_URL = "http://192.168.1.2:9194/BIISimulate/_bfwajax.do";
//	public static String Outlay_API_URL = "https://ccsa.ebsnewppt.boc.cn/BMPS/_bfwajax.do";//投产演练地址

//	public static String Outlay_API_URL = "https://ccsa.ebsnew.boc.cn/BMPS/_bfwajax.do";//投产演练地址
	
	/** 中银国际证券开户跳转web页面的url*/
//	public static String quickOpenJumpWebURL = "https://210.51.39.197:8083/m/testBankpage.jsp";
//	public static String quickOpenJumpWebURL = "https://210.51.39.197:8083/servlet/TokenAction?function=doTokenAuth";
//	public static String quickOpenJumpWebURL = "https://210.51.39.197:8083/m/bankpage.jsp";
	public static String quickOpenJumpWebURL = "https://wykh.bocichina.com/m/bankpage.jsp";
	
	/** 微信银行录入外部系统交易流水号 */
//	public static final String WEIBANKPUBLICMODULE_PATH ="http://22.188.33.152:8080/WeiBankPublicModule/_bfwajax.do";// 信用卡抽奖联调地址
//	public static final String WEIBANKPUBLICMODULE_PATH = "http://22.188.150.83:8080/WeiBankPublicModule/_bfwajax.do";//测试地址
//	public static final String WEIBANKPUBLICMODULE_PATH = "http://bocuat.javascriptchina.com/WeiBankPublicModule/_bfwajax.do";//投产演练
	public static final String WEIBANKPUBLICMODULE_PATH = "https://mbas.mbs.boc.cn/WeiBankPublicModule/_bfwajax.do";//投产地址

	/**  登录信息，用户行为统计地址 */
//	public static final String Function_User_Url = "http://22.11.147.74:8080/BocMBCGate/functionUsed.action";
//	public static final String Function_User_Url = "http://22.188.131.21:9700/BocMBCGate/functionUsed.action";
	public static final String Function_User_Url = "https://mbs.boc.cn/BocMBCGate/functionUsed.action";
//	public static final String Function_User_Url = "http://22.188.196.65:9081/BocMBCGate/functionUsed.action";// 测试地址
//	public static final String Function_User_Url = "http://22.188.196.65:9080/BocMBCGate/functionUsed.action";// 测试地址
	
	
	/**  版本更新地址 */
	public final static String UpdateUrl = "https://mbs.boc.cn/BocMBCGate/MBCExchangeVersionInfo.do";//生产
//	public final static String UpdateUrl = "http://22.188.196.65:9080/BocMBCGate/MBCExchangeVersionInfo.do";// 测试
	/** 微信抽奖地址 */
	public final static String ActivityInfo_Url = "https://mbs.boc.cn/BocMBCGate/activityInfo.action";// 生产
//	public final static String ActivityInfo_Url = "http://22.188.46.227/BocMBCGate/activityInfo.action";// 测试

//	public final static String OnLineService = "https://22.188.159.17/BOC_MOBILE/"; //在线开户测试地址
	public final static String OnLineService = "https://95566.boc.cn/BOC_MOBILE/"; //在线开户生产地址
//	public final static String OnLineService = "https://95566ppt.boc.cn/BOC_MOBILE/"; //在线演练测试地址
	//当前版本versionCode

	//四方测试地址
	public final static String SF_URL ="http://22.188.43.167:9081/mobileplatform/"; // 测试地址
//	public final static String SF_URL ="http://219.141.191.126/mobileplatform/"; // 投产地址

//	public final static String SF_URL ="http://boc.lanjianhui.cn:8099/BIISimulate/_bfwajax.do";
//	http://boc.lanjianhui.cn:8099/BIISimulate/_bfwajax.do
//	public final static String SF_URL ="http://22.7.17.76:9000/mobileplatform/";  //四方开发联调地址


	public final static	int currentVersionCode = 5;
		
	// ;//
	/** CFCA密码控件 密码算法标识，0代表国密算法SM2，1代表国际算法RSA，默认为0 */
	public static int CIPHERTYPE = 0;

	// -------------------------------------------------------------------------------------------------
	// 系统开关参数
	/**
	 * 将log信息写到文件开关，不需要时或生产版本时改为false，不要轻易打开这个开关！！<b>
	 * 对应的API是SDcardLogUtil.logWriteToFile，详情请参见该API注释<b>
	 * 网络报文统一放在手机目录的BIILog文件夹内，请求返回报文文件分别为request+接口名、response+接口名，默认文件是续写的<b>
	 * 如果不需要续写文件，请使用SDcardLogUtil.logWriteToFile四个参数的API，第四个参数代表是否续写
	 */
	public static final boolean WRITETOFILE = false;
	/** 打印日志的开关,生产版本时改为false */
	public static final boolean DEBUG = false;
	/** 是否打开选择地址 */	
	public static final boolean IS_SELECT_INTER = false;

	
	// -------------------------------------------------------------------------------------------------
	// 系统参数配置
	/** app的版本号 和string.xml中 app_versionName 保持同步 */
	public static final String APP_VERSION = "1.5.35";
	// 默认轮询时间 （T51修改为1小时）
	public static final int PUSH_INTERVAL = 60 * 1000 * 60;
	/** 退出时间间隔 */
	public static final int EXIT_BETWEEN_TIME = 3000;
}
