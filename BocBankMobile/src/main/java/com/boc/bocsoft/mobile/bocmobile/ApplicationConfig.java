package com.boc.bocsoft.mobile.bocmobile;

/**
 * 应用程序配置类：用于保存应用相关信息及设置
 *
 * @author xianwei
 */
public class ApplicationConfig {

    public final static String BII_CONTENT = "BII/_bfwajax.do";
    public static final String BMPS_CONTENT = "BMPS/_bfwajax.do";//
    public static final String WFSS_CONTENT = "mobileplatform/";


    //　BII接口地址
    //    public static final String BII_URL = "http://22.188.130.126/";  //T1验密、
    //    public static final String BII_URL = "http://22.18.61.194:9019/";  //理财测试平台
     public static final String BII_URL = "http://22.188.130.126:2468/";  //T1非验密
    //public static final String BII_URL = "https://22.188.135.115/";  //新T1环境 - 20160823
//        public static final String BII_URL = "https://22.11.147.54:8080/";  //HCE模块
//    public static final String BII_URL = "http://22.11.147.173:8080/";// HCE模块2
//    public static final String BII_URL = "http://22.11.65.195:8080/";// HCE模块2
    //    public static final String BII_URL = "https://22.188.151.167:8042/";   //T5验密（换肤积利金测试使用）
    //    public static final String BII_URL = "https://22.188.151.167:10002/";  //T5非验密
    //    public static final String BII_URL = "http://22.11.147.110:8080/"; //  test1
    //    public static final String BII_URL = "http://22.11.140.206:8080/"; // test1
    //    public static final String BII_URL = "http://22.188.137.21:8190/";  // T2
    //    public static final String BII_URL = "https://ebsnew.boc.cn/";	//生产环境
    //    public static final String BII_URL = "https://22.120.6.174/";	//生产ip (内网路由DNS可能无法解析地址,可以ip访问,正式生产请使用使用域名)
    //    public static final String BII_URL = "https://22.188.135.115/";	//测试环境是新的T1验密环境
    //    public static final String BII_URL = "http://22.11.65.171:9998/";	//二维码支付环境
    //     public static final String BII_URL = "http://22.11.147.36:8080/";    //中银E商测试
    //         public static final String BII_URL = "http://22.11.64.161:9998/";    //崩溃调试
    //    public static final String BII_URL = "https://22.188.151.87/";    //701测试

    /**
     * 纪念币接口
     */
    public static final String COIN_URL = "http://22.188.43.149/CoinSeller/_bfwajax.do";

    /**
     * WFSS地址
     */
    public static final String WFSS_URL = "http://22.188.43.167:9081/";//测试地址
    //public static final String WFSS_URL = "http://219.141.191.126/";//生产地址


    /**
     * 产品 与广告推荐URL
     */
    public static final String CR_URL = "http://22.188.64.68:8080/CustSliderApp/_bfwajax.do";//产品推荐 与广告
    //public static final String CR_URL = "https://mbasppt.mbs.boc.cn/AMSCustSliderApp/_bfwajax.do";//演练环境
    //public static final String CR_URL = "https://mbas.mbs.boc.cn/AMSCustSliderApp/_bfwajax.do"//生产环境

    /**
     * 云备份URL
     */
    //public static final String YUN_URL = "http://22.11.97.11:80/ubas-mgateway-webapp/";//内部测试地址
    public static final String YUN_URL = "http://22.188.64.80:80/ubas-mgateway-webapp/";//功能测试
    //public  static final String YUN_URL = "https://mbs.boc.cn/ubas-mgateway-webapp/";//生产环境

    /**
     * MBCG - 微信活动url
     */
    //  public static final String MBCG_URL = "https://mbs.boc.cn/BocMBCGate/activityInfo.action";//生产
    public static final String MBCG_URL = "http://22.188.196.65:9080/BocMBCGate/activityInfo.action";   //t1环境

    // 中银理财
    public static final String BMPS_URL = "http://22.188.132.27:9092/";  // T1
    //public static final String BMPS_URL = "http://22.188.34.110:9083/";     // T5
    //public static final String BMPS_URL = "https://ccsa.ebsnew.boc.cn/";//生产地址

    //　版本更新地址
    //public static final String URL_VERSION = "http://192.168.0.106:8080/BocMBCGate/update.action";//
    public static final String URL_VERSION =  "https://mbs.boc.cn/BocMBCGate/update.action";//生产

    //   验证码地址
    public static final String VERIFICATION_CODE_PATH = "BII/ImageValidationNew/validation.gif";

    public static final String EASY_BUSS_PATH =
            //"http://22.188.159.251/mobileHtml/html/userCenter/index.html?channel=android";
    "https://openapi.boc.cn/ezdb/mobileHtml/html/userCenter/index.html?channel=android";//生产
    //"http://22.188.177.9/mobileHtml/html/userCenter/index.html?sourceNo=000001&channel=android";

    //网申信用卡
    //  public static final String EAPPLY_CREDI_CARD_PATH = "https://apply.mcard.boc.cn/apply/FVJ3QZ";
    public static final String EAPPLY_CREDI_CARD_PATH = "https://apply.mcard.boc.cn/apply/feEfMf";//生产


    // 是否是DEMO
    public static final boolean BII_IS_DEMO = false;
    public static final boolean CR_IS_DEMO = BII_IS_DEMO;

    private static String biiURL = null;
    private static String verificationCodURl = null;

    /**
     * 获得验证地址
     */
    public static String getVerificationCodeUrl() {
        return verificationCodURl != null ? verificationCodURl : BII_URL + VERIFICATION_CODE_PATH;
    }

    /**
     * 设置BII地址
     */
    public static void setBiiURL(String biiUrl, String verificationCodUrl) {
        biiURL = biiUrl;
        verificationCodURl = verificationCodUrl;
    }

    /**
     * 获得BII 地址
     */
    public static String getBiiURL() {
        return biiURL != null ? biiURL : BII_URL + BII_CONTENT;
    }


    public static boolean isBigAmountNotice = true;//转账累计超过30万是否提示
}
