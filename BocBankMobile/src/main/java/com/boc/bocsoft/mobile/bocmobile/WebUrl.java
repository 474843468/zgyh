package com.boc.bocsoft.mobile.bocmobile;

/**
 * Created by dingeryue on 2016年11月28.
 */

public class WebUrl {

    /**
     * 生活页面 精彩特惠
     */
    //生产
    public static final String LIFE_JINGCAITEHUI = "http://mp.weixin.qq.com/mp/homepage?__biz=MjM5MDcxNzI3NA==&hid=3&sn=06116ab9045936d87a32ed8f4aad8707#wechat_redirect";

    /**
     * 生活页面 汇聚天下
     */
    //生产
    //public static final String LIFE_HUIJUTIANXIA = "https://card.bank-of-china.com/CouponsMall/indexBocForPB.do";
    //内网地址
    public static final String LIFE_HUIJUTIANXIA = "http://22.188.63.96:9080/CouponsMall/indexBocForPB.do";

    /**
     * 生活页面 纪念币预约
     */

    //生产
    //public static final String LIFE_SOUVENIRCOIN = "file:///android_asset/SouvenirCoin_release/Framework/index.html?entrance=SouvenirCoin_reserve";
    //测试
    public static final String LIFE_SOUVENIRCOIN = "file:///android_asset/SouvenirCoin/Framework/index.html?entrance=SouvenirCoin_reserve";

    /**
     * 说明书
     */
    public static final String URL_INSTRUCTION = "http://srh.bankofchina.com/search/finprod/getProdPage.jsp?keyword=";

    /**
     * 分享详情URL
     */
    private static final String URL_SHARE =
        "http://boc.javascriptchina.com/shareFinace/Framework/index.html?entrance=shareFinace_financeDetail&productCode=%s&productKind=%s&v=%s";
    //生产
    //public static final  String URL_SHARE = "https://ccsa.ebsnew.boc.cn/shareFinace/Framework/index.html?entrance=shareFinace_financeDetail&productCode=%s&productKind=%s&v=%s";

    public static String getDetailShareUrl(String code,String kind){
        return String.format(URL_SHARE,code,kind,ApplicationContext.getInstance().getVersion());
    }

    /**
     * 分享产品列表URL
     */
    private static final String URL_SHARE_PRODUCT = "http://boc.javascriptchina.com/shareFinace/Framework/index.html?entrance=shareFinace_financeList&v=%s";
    //生产
    //public static final String URL_SHARE_PRODUCT = "https://ccsa.ebsnew.boc.cn/shareFinace/Framework/index.html?entrance=shareFinace_financeList&v=%s";
    public static String getListShareUrl(){
        return String.format(URL_SHARE_PRODUCT,ApplicationContext.getInstance().getVersion());
    }

    /*-------------------------start 信用卡 start---------------------------*/
    // 激活信用卡
    public static final String CRCD_ACTIVE = "https://22.188.135.115/bocphone/Framework/index.html?entrance=BocPhone_cardActivate";
    // 查询密码设置
    public static final String CRCD_SET_QUERY_PASSWORD = "https://22.188.135.115/bocphone/Framework/index.html?entrance=BocPhone_psnCrcdSetQuery";
    // 挂失/补卡
    public static final String CRCD_LOSS = "https://22.188.135.115/bocphone/Framework/index.html?entrance=BocPhone_reportLoss";
    // 全球交易人民币记账
    public static final String CRCD_RMB_RECORD = "https://22.188.135.115/bocphone/Framework/index.html?entrance=BocPhone_rmbRecord";
    // 外币账单自动购汇
    public static final String CRCD_BILL_AUTO_PURCHASE = "https://22.188.135.115/bocphone/Framework/index.html?entrance=BocPhone_foreignAuto";
    // 对账单设置
    public static final String CRCD_SET_BILL = "https://22.188.135.115/bocphone/Framework/index.html?entrance=BocPhone_checkBil";
    // 交易密码设置
    public static final String CRCD_SET_TRANSACTION_PASSWORD = "https://22.188.135.115/bocphone/Framework/index.html?entrance=BocPhone_psnCrcdSetTransaction";
    // POS消费功能设置
    public static final String CRCD_SET_POS_MSG = "https://22.188.135.115/bocphone/Framework/index.html?entrance=BocPhone_posMsgTrigger";
    // 3D安全认证
    public static final String CRCD_3D_SECURITY = "https://22.188.135.115/bocphone/Framework/index.html?entrance=BocPhone_identification3D";
    /*-------------------------end 信用卡 end---------------------------*/
}
