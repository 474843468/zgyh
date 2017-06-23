package com.boc.bocsoft.mobile.bocmobile;

import java.util.ArrayList;
import java.util.List;

/**
 * APP常量类
 * APP中用到的常量定义在该类中
 * Created by lxw on 2016/5/21.
 */
public class ApplicationConst {
    //     public static final String APP_KEY = "AF0047D2B0CFB034E3DFFED8F2AFFE09";// 本地测试版
//     public static final String APP_KEY = "baf8969c10384d08f5da004551-OBP";// 正式版
    public static final String APP_KEY = "e48e149515fa811527b4004551-OBP";// 测试版(提交到SVN上时确保是这个key!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!)
    //微信分享ID
    public static final String APP_ID = "wxb1214b15f1e07e06";
    /**
     * 页面显示记录条数
     */
    public static final int PAGE_SIZE = 50;
    /**
     * 中银理财页面请求记录条数
     */
    public static final int WEALTH_PAGE_SIZE = 30;
    /**
     * 基金页面请求记录条数
     */
    public static final int FUND_PAGE_SIZE = 50;

    /**
     * CFCA设置参数常量
     */
    /**
     * 输入密码时的最短长度
     */
    public static final int MIN_LENGTH = 6;
    /**
     * 修改密码时的最短长度
     */
    public static final int MIN_LENGTH_MODIFY = 8;
    /**
     * 输入密码时的最长长度
     */
    public static final int MAX_LENGTH = 20;
    /**
     * CFCA密码控件 密码算法标识，0代表国密算法SM2，1代表国际算法RSA，默认为0
     */
    public static int CIPHERTYPE = 0;
    /**
     * CFCA密码键盘 数字键盘
     */
    public static final int KEYBOARDTYPE_NUMBOER = 1;
    /**
     * CFCA密码键盘全键盘
     */
    public static final int KEYBOARDTYPE_ALL = 0;
    /**
     * CFCA 除了登录修改密码之外的类型 （ 动态口令、手机交易码、电话银行密码、ATM取款密码） 均采用该类型
     */
    public static final int OUT_PUT_VALUE_TYPE_OTP = 2;
    /**
     * CFCA密码类型
     */
    public static final int OUT_PUT_VALUE_TYPE = 1;

    /**
     * 登录参数常量
     */
    /**
     * 正常
     */
    public static final String LOGIN_STATUS_0 = "0";
    /**
     * 柜台首次登录
     */
    public static final String LOGIN_STATUS_1 = "1";
    /**
     * 柜台重置密码
     */
    public static final String LOGIN_STATUS_2 = "2";

    /**
     * 设备绑定，本地设备信息
     */
    public static final String SHARED_PREF_LOCAL_BIND_INFO = "local_bind_info";
    public static final String SHARED_PREF_LOCAL_BIND_INFO_MAC = "local_bind_info_mac";


    /** 银行卡号类型 */

    /**
     * 普通活期
     */
    public static final String ACC_TYPE_ORD = "101";
    /**
     * 中银系列信用卡
     */
    public static final String ACC_TYPE_ZHONGYIN = "103";
    /**
     * 长城信用卡
     */
    public static final String ACC_TYPE_GRE = "104";
    /**
     * 单外币信用卡
     */
    public static final String ACC_TYPE_SINGLEWAIBI = "107";
    /**
     * 虚拟信用卡
     */
    public static final String ACC_TYPE_XNCRCD1 = "108";//虚拟信用卡贷记
    public static final String ACC_TYPE_XNCRCD2 = "109";//虚拟信用卡准贷记
    /**
     * 虚拟借记卡
     */
    public static final String ACC_TYPE_JIEJIXN = "110";
    /**
     * 借记卡
     */
    public static final String ACC_TYPE_BRO = "119";
    /**
     * 存本取息
     */
    public static final String ACC_TYPE_CBQX = "140";
    /**
     * 零存整取
     */
    public static final String ACC_TYPE_ZOR = "150";
    /**
     * 教育储蓄
     */
    public static final String ACC_TYPE_EDU = "152";
    /**
     * 定期一本通
     */
    public static final String ACC_TYPE_REG = "170";
    /**
     * 活期一本通
     */
    public static final String ACC_TYPE_RAN = "188";
    /**
     * 网上专属理财
     */
    public static final String ACC_TYPE_BOCINVT = "190";
    /**
     * 优汇通专户
     */

    public static final String ACC_TYPE_YOUHUITONG = "199";
    /**
     * 单电子现金卡
     */
    public static final String ACC_TYPE_ECASH = "300";
    /**币种代码*/
    /**
     * 人民币
     */
    public static final String CURRENCY_CNY = "001";
    /**
     * 英镑
     */
    public static final String CURRENCY_GBP = "012";
    /**
     * 港币元
     */
    public static final String CURRENCY_HKD = "013";
    /**
     * 美元
     */
    public static final String CURRENCY_USD = "014";
    /**
     * 瑞士法郎
     */
    public static final String CURRENCY_CHF = "015";
    /**
     * 新加坡元
     */
    public static final String CURRENCY_SGD = "018";
    /**
     * 日元
     */
    public static final String CURRENCY_JPY = "027";
    /**
     * 加拿大元
     */
    public static final String CURRENCY_CAD = "028";
    /**
     * 澳大利亚元
     */
    public static final String CURRENCY_AUD = "029";
    /**
     * 欧元
     */
    public static final String CURRENCY_EUR = "038";
    /**
     * 澳门元
     */
    public static final String CURRENCY_MOP = "081";
    /**
     * 阿联酋迪拉姆
     */
    public static final String CURRENCY_AED = "096";
    /**
     * 巴西里亚尔
     */
    public static final String CURRENCY_BRL = "134";
    /**
     *丹麦克朗
     */
    public static final String CURRENCY_DKK = "022";
    /**
     *俄罗斯贸易卢布
     */
    public static final String CURRENCY_E_LUO_SI = "072";
    // 服务码因子
    /**
     * 电子现金账户--充值上送服务码
     */
    public static final String SERVICE_ID_FINANCE_TRANSFER = "PB012";
    /**
     * 电子现金账户--新建签约上送服务码
     */
    public static final String SERVICE_ID_FINANCE_SIGN = "PB013";
    /**
     * 申请定期/活期
     */
    public static final String SERVICE_ID_APPLY_ACCOUNT = "PB014";
    /**
     * 虚拟银行卡
     */
    public static final String SERVICE_ID_VIRTUAL_CARD = "PB059";
    /**
     * 借记卡限额设置
     */
    public static final String SERVICE_ID_ACCOUNT_LIMIT_SET = "PB016";

    public static final String SERVICE_ID_ACCOUNT_LIMIT = "PB203";

    /**
     * 基金的服务码
     */
    public static final String FINC_SERVICE = "PB063C";
    public static final String ISFOREX_SERVICEID = "PB081";
    /**
     * 关联账户转账
     */
    public static final String PB021 = "PB021";
    /**
     * 中行内转账
     */
    public static final String PB031 = "PB031";// 行内
    public static final String PB032 = "PB032";// 跨行
    public static final String PB035 = "PB035";// 手机号转账
    public static final String PB033 = "PB033";// 行内定向
    public static final String PB034 = "PB034";// 跨行定向
    public static final String PB113 = "PB113";// 跨行实时付款
    public static final String PB118 = "PB118";// 跨行实时定向

    public static final String PB999 = "PB999";// 全部转账类型查询

    public static final String LOAN_PB = "PB092";
    public static final String LOAN_PB1 = "PB091";
    public static final String LOAN_PB5 = "PB095";
    /**
     * 解除自动预约预交易服务码
     */
    public static final String TRAN_SERVICEID = "PB039";

    /**
     * 转账交易 立即执行
     */
    public static final String NOWEXE = "0";
    /**
     * 转账交易 预约日期执行
     */
    public static final String PREDATEEXE = "1";
    /**
     * 转账交易 预约周期执行
     */
    public static final String PREPERIODEXE = "2";

    /**
     * 查询类型----1：单一额度
     */
    public static final String LOAN_QUERYTYPE = "1";
    /**
     * 操作类型
     */
    public static final String LOAN_ACTTYPE = "B";
    public static final String LOAN_PAYCYCLE = "98";
    public static final String LOAN_ACTTYPE1 = "B1";
    public static final String LOAN_QUARY_RESULT = "result";

    /**
     * 金额格式化时没有小数点的币种
     */
    public static List<String> currencyCodeNoPoint = new ArrayList<String>() {
        {
            add("027");// 日元
            add("JPY");
            add("088");// 韩元
            add("KRW");
            add("064");// 越南盾
            add("VND");
            // add("068");//人民币银
            // add("845");//人民币铂金
            // add("844");//人民币钯金
            // add("036");//美元银

        }
    };
    public static final int MAX_INVEST_NUM = 5;//优选投资编辑页面选择投资的最大数
    public static final int MAX_MENU_NUM = 7;//菜单的最大数
    public static final String SELECTED_INVEST_GOLD = "selectedGolds";//选中的贵金属
    public static final String SELECTED_INVEST_FUND = "selectedFunds";//选中的基金
    public static final String SELECTED_INVEST_FESS = "selectedFesses";//选中的结购汇

    public static final String QUERY_HIS_NAME = "query_his_name";//从Map中存放和取出历史记录用的key
    public static final String PRODUCT_QUERY_HIS = "product_query_his";//产品搜索历史记录保存到sp的key
    public static final String FUND_QUERY_HIS_NAME = "fund_query_his_name";//基金产品从Map中存放和取出历史记录用的key
    public static final String FUND_PRODUCT_QUERY_HIS = "fund_product_query_his";//基金产品搜索历史记录保存到sp的key

    // 中行支持的结购汇币
    public static final String[] FESS_BOC_ARRAY = {"USD", "AUD", "CAD", "HKD", "GBP", "EUR", "JPY"
            , "NZD", "SGD", "THB", "KRW", "TWD", "CHF"
            , "SEK", "DKK", "RUB", "NOK", "PHP", "MOP",
            "IDR", "BRL", "AED", "INR", "ZAR"};
}
