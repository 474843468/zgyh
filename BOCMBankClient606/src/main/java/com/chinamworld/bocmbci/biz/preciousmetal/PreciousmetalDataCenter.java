package com.chinamworld.bocmbci.biz.preciousmetal;

import com.chinamworld.bocmbci.utils.KeyAndValueItem;
import com.chinamworld.llbt.model.AccountItem;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 贵金属积存 数据类
 * Created by linyl on 2016/8/25.
 */
public class PreciousmetalDataCenter {

    public static PreciousmetalDataCenter instance = null;
    public synchronized static PreciousmetalDataCenter getInstance(){
        if(instance == null){
            instance = new PreciousmetalDataCenter();
        }
        return instance;
    }

    /**贵金属积存 交易查询接口返回 Map**/
    public Map<String,Object> TransQueryMap;
    /**贵金属积存 交易查询接口返回 列表选中项 Map**/
    public Map<String,Object> TransQueryDetailMap;
    /**贵金属积存 买卖交易接口返回 Map**/
    public Map<String,Object> TransMap;
    /**贵金属积存 提示网点 接口返回 Map**/
    public Map<String,Object> BranchQueryMap;
    /**贵金属积存 更多提货网点 接口返回 Map**/
    public List<Map<String,Object>> BranchListQueryMap;
    /**贵金属积利金 公共余额数据**/
    public String availbalance;
    /**#7 接口  StoreList 选择项**/
    public Map<String ,Object> StoreListItem;
    /**贵金属积利金 预交易返回*/
    public Map<String, Object> resultPsnGoldStore;





    /**************贵金属积存 接口字典信息*******************/
    /**
     * 贵金属积存 交易查询积存方式
     */
    public static List<KeyAndValueItem> transUnitList = new ArrayList<KeyAndValueItem>(){
        {
            add(new KeyAndValueItem("按金额","1"));
            add(new KeyAndValueItem("按克重","2"));
        }
    };
    /**
     * 贵金属积存 交易渠道
     */
    public static List<KeyAndValueItem> chennalList = new ArrayList<KeyAndValueItem>(){
        {
            add(new KeyAndValueItem("柜面","0"));
            add(new KeyAndValueItem("网上银行","1"));
            add(new KeyAndValueItem("手机银行","2"));
            add(new KeyAndValueItem("家居银行","3"));
            add(new KeyAndValueItem("账户金转实物金","4"));
            add(new KeyAndValueItem("BL前端","5"));
        }
    };

    /**
     * 贵金属积存 交易查询 交易状态
     */
    public static List<KeyAndValueItem> statusList = new ArrayList<KeyAndValueItem>(){
        {
            add(new KeyAndValueItem("成功","1"));
        }
    };

    /**
     * 积存品种
     */
    public static List<KeyAndValueItem> varietiesList = new ArrayList<KeyAndValueItem>(){
        {
            add(new KeyAndValueItem("吉祥金","BAU"));
        }
    };

    /**
     * 积存币种
     */
    public static List<KeyAndValueItem> currencyList = new ArrayList<KeyAndValueItem>(){
        {
            add(new KeyAndValueItem("人民币元","001"));
        }
    };

    /**外置接口即14号接口*/
    public static final String  PSNGOLDSTOREPRICELISTOUTLAY="PsnGoldStorePriceListOutlay";
    /**登陆后牌价即04号接口*/
    public static final String  PSNGOLDSTOREPRICELIST="PsnGoldStorePriceList";
    /**登陆后积存量即07号接口*/
    public static final String  PSNGOLDSTOREACCOUNTQUERY="PsnGoldStoreAccountQuery";
    /**登陆后积存量即06号接口*/
    public static final String  PSNGOLDSTOREGOODSLIST="PsnGoldStoreGoodsList";
    /**登陆后牌价即04号接口返回值*/
    public List<Map<String, Object>>  LOGINPRICEMAP;
    /**登陆后积存量的集合*/
    public List<Map<String, Object>> AMOUNTLIST;
    /**登陆后牌价即04号接口返回值 选中项 Item*/
    public Map<String, Object>  LOGINPRICEMAPITEM;
    /**登陆后积存量即07号接口返回值*/
    public Map<String,Object> ACCOUNTQUERYMAP;
    /**外置买入价*/
    public static  final String BUYPRICE="buyPrice";
    /**w外置卖出价*/
    public static final String SALEPRICE="salePrice";
    /**w外置更新时间*/
    public static  final String PRICETIME="priceTime";
    /**积存列表*/
    public static  final String STORELIST="storeList";
    /**积存量*/
    public static  final String AMOUNT="amount";
    /**7号接口的*/
    public static  final String CUSINFO="cusInfo";
    /**是否已设定积存账户*/
    public static  final String SETGOLDSTOREACCOUNT="setGoldStoreAccount";
    /**是否已关联网银*/
    public static  final String LINKEDACCOUNT="linkedAccount";
    /**m买入标志*/
    public static  final String REQUEST_LOGIN_CODE_BUY="3";
    /**积存种类金属中文名*/
    public static  final String CURRCODENAME="currCodeName";
    /**积存种类金属code*/
    public static  final String CURRCODE="currCode";
    /**accountNum*/
    public static  final String ACCOUNTMUN="accountNum";
   /** custName*/
    public static  final String CUSTNAME="custName";
    /**买入价*/
    public String BUYPRICE_LONGIN;
    /**卖出价*/
    public  String SALEPRICE_LONGIN;
    //登陆后牌价是否暂停或无牌价标志
    public Boolean loginIsHave=true;
    //未登陆牌价是否暂停或无牌价标志
    public Boolean NologinIsHave=true;

    /** 首页当前积存类型详细数据 */
    public Map<String, Object> curDetail;
    //主页种类的位置
    public int Mainposition=0;
    /**选择种类的code*/
    public  String CODE="BAU/CNY";
    /**未登陆牌价即外置接口返回值*/
    public List<Map<String, Object>>  PRICEMAPLAYOUT;
    /**6号接口返回值*/
    public List<Map<String, Object>>  GoodsListT;
    /**账户重设接口*/
    public static final String  PSNGOLDSTOREMODIFYACCOUNT="PsnGoldStoreModifyAccount";
    /**账户签约预交易接口*/
    public static final String  PSNGOLDSTORECREATEACCOUNTCONFIRM="PsnGoldStoreCreateAccountConfirm";
    /**账户设定接口*/
    public static final String  PSNGOLDSTORECREATEACCOUNT="PsnGoldStoreCreateAccount";
    /**12号接口*/
    public static final String  PSNGOLDSTOREBRANCJQUERY="PsnGoldStoreBranchQuery";
    /**老帐户账号*/
    public String AccountNumberOld="";
    /**老帐户账号ID*/
    public String AccountIDOld="";
    /**老帐户姓名*/
    public String AccountName="";
    /**帐户类型*/
    public String AccountType="";
    /**帐户类型*/
    public String AccNikeName="";
    /**老帐户电话号码*/
    public String AccountPhoneMunber="";
    /**老帐户地址*/
    public String AccountAddress="";
    /**老帐户邮编*/
    public String AccountMail="";
    /**新帐户账号*/
    public String AccountNumberNEW="";
    /**新帐户账号ID*/
    public String AccountIdNEW="";
    /**从购买进入标志*/
    public String BUYFLAG_CHANGE="1";
    /**从外置购买进入标志*/
    public String BUYFLAG="1";
    /**从外置赎回进入标志*/
    public String BACKFLAG="1";
    /**从外置查询进入标志*/
    public String CHECKFLAG="1";
    /**从外置账户管理进入标志*/
    public String ACCOUNTMANAGERFLAG="1";
    /**积存量*/
    public String JiCunAmount="";

    /** 重设账户时，选中的账户 */
    public AccountItem curAccountItem;
    /***********************************************正则校验提示中的字段名***************************************************/
    public static final String GOLDSTORENUMBER = "电话";
    public static final String GOLDSTOREADDRESS = "地址";
    public static final String GOLDSTOREMAIL = "邮编";

    public static final String CUSTPHONENUM = "custPhoneNum";
    public static final String CUSTADDRESS = "custAddress";
    public static final String POSTCODE = "postCode";


    public static Map<String, String> Accountstyle = new LinkedHashMap<String, String>() {

        private static final long serialVersionUID = 1L;

        {//
            put("188", "活期一本通");
            put("119", "借记卡");
        }
    };
    //重设账户的新正则
    public static final String newRege = "(?=(\\S*[0-9]\\S*[a-zA-Z]|\\S*[a-zA-Z]\\S*[0-9]))^\\S{8,20}$";
}
