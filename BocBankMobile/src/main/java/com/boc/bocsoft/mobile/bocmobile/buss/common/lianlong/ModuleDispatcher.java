package com.boc.bocsoft.mobile.bocmobile.buss.common.lianlong;

import android.app.Activity;

import com.boc.bocsoft.mobile.bocmobile.ApplicationContext;
import com.boc.bocsoft.mobile.bocmobile.buss.common.menu.ModuleCode;
import com.boc.bocsoft.mobile.bocmobile.buss.login.ui.LoginContext;
import com.boc.bocsoft.mobile.common.utils.StringUtils;
import com.chinamworld.boc.commonlib.BaseCommonTools;
import com.chinamworld.boc.commonlib.ModuleManager;
import com.chinamworld.boc.commonlib.model.GoldDataModel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 联龙各个模块跳转
 * Created by lxw on 2016/8/11 0011.
 */
public class ModuleDispatcher {

    // 存款管理
    private final static String MAIN_DEPT_STORAGE_CASH = "deptStorageCash_main";
    //我要存定期
    private final static String MAIN_DEPTSTORAGECASH_1 = "deptStorageCash_1";
    //我的定期存款
    private final static String MAIN_DEPTSTORAGECASH_2 = "deptStorageCash_2";
    //定期约定转存
    private final static String MAIN_DEPTSTORAGECASH_3 = "deptStorageCash_3";
    //大额存单
    private final static String MAIN_DEPTSTORAGECASH_4 = "deptStorageCash_4";
    //通知管理
    private final static String MAIN_DEPTSTORAGECASH_5 = "deptStorageCash_5";
    //定期存款查询
    private final static String MAIN_DEPTSTORAGECASH_6 = "deptStorageCash_6";

    // 信用卡
    private final static String MAIN_MY_CRCD = "myCrcd_main";
    // 结汇购汇
    private final static String MAIN_SB_REMIT = "sbRemit_main";
    // 账单缴付
    private final static String MAIN_PAY = "pay_main";
    // 跨境汇款
    private final static String MAIN_CROSS_BORDER_REMIT = "crossBorderRemit_main";
    // 向境外他行
    private final static String MAIN_CROSS_BORDER_REMIT_1 = "crossBorderRemit_1";
    // 向境外中行
    private final static String MAIN_CROSS_BORDER_REMIT_4 = "crossBorderRemit_4";

    // 民生缴费
    private final static String MAIN_PLPS = "plps_main";
    // 手机取款
    private final static String MAIN_DRAW_MONEY = "DrawMoney_main";
    // 二维码转账
    private final static String MAIN_QR_TRANSER = "qr_transer_main";
    // 主动收款
    private final static String MAIN_GATHER_INITIATIVE = "GatherInitiative_main";
    // 主动收款
    private final static String MAIN_CROSS_RORDER_REMIT = "crossBorderRemit_main";
    // 移动支付
    private final static String MAIN_ZHIFU = "zhifu_main";

    // 服务设定
    private final static String MAIN_SETTING_MANAGER = "settingManager_main";
    // 账户管家
    private final static String MAIN_SETTING_MANAGER_1 = "settingManager_1";
    // 设置默认账户
    private final static String MAIN_SETTING_MANAGER_8 = "settingManager_8";
    // 开通投资理财
    private final static String MAIN_SETTING_MANAGER_9 = "settingManager_9";
    // 交易限额设置
    private final static String MAIN_SETTING_MANAGER_2 = "settingManager_2";
    // 修改登陆密码
    private final static String MAIN_SETTING_MANAGER_3 = "settingManager_3";
    // 预留信息
    private final static String MAIN_SETTING_MANAGER_4 = "settingManager_4";
    // 退出时间设定
    private final static String MAIN_SETTING_MANAGER_5 = "settingManager_5";
    // 我的支付服务
    private final static String MAIN_SETTING_MANAGER_6 = "settingManager_6";
    // 支付服务查询
    private final static String MAIN_SETTING_MANAGER_7 = "settingManager_7";

    // 查看中银e盾信息
    private final static String MAIN_SETTING_MANAGER_10 = "settingManager_10";
    // 修改中银e盾密码
    private final static String MAIN_SETTING_MANAGER_11 = "settingManager_11";
    // 管理绑定设备
    private final static String MAIN_SETTING_MANAGER_12 = "settingManager_12";
    // 安全工具设置
    private final static String MAIN_SETTING_MANAGER_13 = "settingManager_13";

    //资产管理
    private final static String MAIN_ASSSETMANAGER_1 = "asssetManager_1";

    // 资产管理
    private final static String MAIN_ASSSET_MANAGER = "asssetManager_main";
    // 中银理财
    private final static String MAIN_BOCINVT_MANAGER = "bocinvtManager_main";
    // 基金
    private final static String MAIN_FINC = "finc_main";
    //我的基金
    private final static String FINC_2 = "finc_2";
    // 账户贵金属
    private final static String MAIN_PRMS_MANAGE = "prmsManage_main";
    // 双向宝
    private final static String MAIN_IS_FOREX_STORAGE_CASH = "isForexStorageCash_main";
    //我的双向宝	isForexStorageCash_3
    private final static String ISFOREXSTORAGECASH_3 = "isForexStorageCash_3";

    // 外汇投资
    private final static String MAIN_FOREX_STORAGE_CASH = "forexStorageCash_main";
    // 第三方存管
    private final static String MAIN_THIRD_MANANGER = "thirdMananger_main";
    // 债券
    private final static String MAIN_BOND = "bond_main";
    //我的债券
    private final static String BOND_2 = "bond_2";
    // 保险
    private final static String MAIN_SAFETY = "safety_main";
    //持有保单查询
    private final static String SAFETY_3 = "safety_3";

    // 贵金属积利
    private final static String MAIN_GOLDBONUS_MANAGER = "goldbonusManager_main";
    // 贵金属积利
    private final static String MAIN_GOLD_STORE = "goldstore_main";

    private static HashMap<String, String> moduleMap = new HashMap<>();

    static {

        // 资产管理
        moduleMap.put(ModuleCode.MODULE_LOAN_0000, MAIN_ASSSET_MANAGER);

        // 信用卡
        moduleMap.put(ModuleCode.MODULE_CREDIT_CARD_0000, MAIN_MY_CRCD);

        // 存款管理
        moduleMap.put(ModuleCode.MODULE_DEPOSIT_0000, MAIN_DEPT_STORAGE_CASH);

        // 民生缴费
        moduleMap.put(ModuleCode.MODULE_PLPS_0000, MAIN_PLPS);
        // 结售汇
        moduleMap.put(ModuleCode.MODULE_SB_REMIT_0000, MAIN_SB_REMIT);
        // 跨境汇款
        moduleMap.put(ModuleCode.MODULE_CROSS_BORDER_REMIT_0000, MAIN_CROSS_BORDER_REMIT);

        // 向境外他行
        moduleMap.put(ModuleCode.MODULE_CROSS_BORDER_REMIT_0100, MAIN_CROSS_BORDER_REMIT_1);
        // 向境外中行
        moduleMap.put(ModuleCode.MODULE_CROSS_BORDER_REMIT_0400, MAIN_CROSS_BORDER_REMIT_4);

        // 账单支付
        moduleMap.put(ModuleCode.MODULE_BILL_PAY_0000, MAIN_PAY);
        // 移动支付
        moduleMap.put(ModuleCode.MODULE_MOBILE_PAY_0000, MAIN_ZHIFU);

        //--- 投资
        moduleMap.put(ModuleCode.MODULE_ASSET_MANAGER_0000, MAIN_ASSSET_MANAGER);

        // --  投资 menu --
        //基金
        moduleMap.put(ModuleCode.MODULE_AFINC_0000, MAIN_FINC);
        //我的基金
        moduleMap.put(ModuleCode.MODEUL_MINE_FUNC, FINC_2);

        //理财
        //moduleMap.put(ModuleCode.MODULE_BOCINVT_0000, MAIN_BOCINVT_MANAGER);

        // 账户贵金属
        moduleMap.put(ModuleCode.MODULE_GOLDACCOUNT_0000, MAIN_PRMS_MANAGE);

        // 双向宝
        moduleMap.put(ModuleCode.MODULE_ISFOREXSTORAGECASH_0000, MAIN_IS_FOREX_STORAGE_CASH);
        ////我的双向宝	isForexStorageCash_3
        moduleMap.put(ModuleCode.MODULE_ISFOREXSTORAGECASH_3, ISFOREXSTORAGECASH_3);


        // 余额理财
        moduleMap.put(ModuleCode.MODULE_BALANCE_0000, MAIN_ASSSET_MANAGER);
        // 外汇买卖
        moduleMap.put(ModuleCode.MODULE_FOREX_STORAGE_CASH_0000, MAIN_FOREX_STORAGE_CASH);

        // 第三方存管
        moduleMap.put(ModuleCode.MODULE_THIRD_MANANGER_0000, MAIN_THIRD_MANANGER);

        // 贵金属积存
        moduleMap.put(ModuleCode.MODULE_GOLD_STORE_0000, MAIN_GOLD_STORE);

        // 贵金属积利
        moduleMap.put(ModuleCode.MODULE_GOLD_BONUS_MANAGER_0000, MAIN_GOLDBONUS_MANAGER);

        // 债券
        moduleMap.put(ModuleCode.MODULE_BOND_0000, MAIN_BOND);
        //我的债券
        moduleMap.put(ModuleCode.MODULE_BOND_0002, BOND_2);

        // 保险
        moduleMap.put(ModuleCode.MODULE_SAFETY_0000, MAIN_SAFETY);
        //持有保单查询	safety_3
        moduleMap.put(ModuleCode.MODULE_SAFETY_0003, SAFETY_3);

        // 服务设定

        // 设置默认账户
        moduleMap.put(ModuleCode.MODULE_TRANSER_0700, MAIN_SETTING_MANAGER_8);

        // 开通投资理财
        moduleMap.put(ModuleCode.MODULE_SETTING_MANAGER_0100, MAIN_SETTING_MANAGER_9);
        // 限额设置
        moduleMap.put(ModuleCode.MODULE_SETTING_MANAGER_0200, MAIN_SETTING_MANAGER_2);
        // 退出时间设定
        moduleMap.put(ModuleCode.MODULE_SETTING_MANAGER_0301, MAIN_SETTING_MANAGER_5);
        // 预留信息
        moduleMap.put(ModuleCode.MODULE_SETTING_MANAGER_0302, MAIN_SETTING_MANAGER_4);
        // 修改登录密码
        moduleMap.put(ModuleCode.MODULE_SETTING_MANAGER_0303, MAIN_SETTING_MANAGER_3);
        // 中银E盾 - 查看信息
        moduleMap.put(ModuleCode.MODULE_SETTING_MANAGER_0304, MAIN_SETTING_MANAGER_10);
        //修改密码
        moduleMap.put(ModuleCode.MODULE_SETTING_MANAGER_0307, MAIN_SETTING_MANAGER_11);

        // 管理绑定设备
        moduleMap.put(ModuleCode.MODULE_SETTING_MANAGER_0305, MAIN_SETTING_MANAGER_12);
        // 安全工具设置
        moduleMap.put(ModuleCode.MODULE_SETTING_MANAGER_0306, MAIN_SETTING_MANAGER_13);
        // 我的支付 - 移动支付
        moduleMap.put(ModuleCode.MODULE_SETTINGMANAGER_0401, MAIN_ZHIFU);
        // 我的支付 - 支付交易查询
        moduleMap.put(ModuleCode.MODULE_SETTINGMANAGER_0402, MAIN_SETTING_MANAGER_7);

        // 服务记录
        moduleMap.put(ModuleCode.MODULE_SETTING_MANAGER_0500, MAIN_SETTING_MANAGER_7);
        //我的资产
        moduleMap.put(ModuleCode.MODULE_SETTING_MANAGER_0700, MAIN_ASSSETMANAGER_1);
         //跨境金融
        //moduleMap.put(ModuleCode.MODULE_CROSS_BORDER_FINANCE_0000, MAIN_CROSS_BORDER_REMIT);

        //其他

        //我要存定期
        moduleMap.put(ModuleCode.MODEUL_DEPTSTORAGE, MAIN_DEPTSTORAGECASH_1);
        //通知管理
        moduleMap.put(ModuleCode.MODEUL_DEPTSTORAGECASH_5, MAIN_DEPTSTORAGECASH_5);

    }

    /**
     * 联龙模块跳转
     * @param moduleId
     */
    public static String convertLianLongId(String moduleId){
        if (StringUtils.isEmptyOrNull(moduleId)) {
            return "";
        }

        String value = moduleMap.get(moduleId);
        return value;
    }

    /**
     * 根据基金产品id跳转到联龙功能
     * @param activity
     * @param productID
     */
    public static void gotoFincMudule(Activity activity, String productID){
        settingCookiesToLianlong();
        ModuleManager.instance.gotoFincModule(activity, productID);
    }


    /**
     * 根据理财产品id跳转到联龙功能
     * @param activity
     * @param investID
     */
    public static void gotoInvestMudule(Activity activity, String investID){
        settingCookiesToLianlong();
        ModuleManager.instance.gotoInvestModule(activity, investID);
    }

    /**
     * 跳转到联龙贵金属页面
     * @param activity
     * @param buyRate
     * @param sellRate
     * @param sourceCurrencyCode
     * @param targetCurrencyCode
     */
    public static void gotoGoldMudule(Activity activity, String buyRate, String sellRate, String sourceCurrencyCode, String targetCurrencyCode){
        settingCookiesToLianlong();
        GoldDataModel model = new GoldDataModel(buyRate, sellRate, sourceCurrencyCode, targetCurrencyCode);
        ModuleManager.instance.gotoGoldModule(activity, model);
    }

    /**
     * 跳转到联龙结购汇页面
     * @param activity
     * @param params
     */
    public static void gotoSBRemitMudule(Activity activity, Map<String, Object> params){
        settingCookiesToLianlong();
        ModuleManager.instance.gotoSBRemitModule(activity, params);
    }

    /**
     * 跳转到联龙  支取 入口
     *
     * @param activity
     * @param accountMap 用戶选择账户
     * @oaram notifyMgFlag 1 通知 2 定期一本通
     */
    public static void gotoWithdrawDeposits(Activity activity, Map<String, Object> accountMap, Map<String, Object> accountMap1,String depositsType) {
        settingCookiesToLianlong();
        ModuleManager.instance.gotoWithdrawDeposits(activity,accountMap,accountMap1,depositsType);
    }

    /* 跳转到信用卡详情页面
   * @param
   * activity ： activity
   * 请求回来的账户列表信息 (我的信用卡)  List<Map<String, Object>> bankAccountList
   * 账户id  accountSeq
   * 选择的项  position
   */
    public static void gotoCardmessageAndSetActivity(Activity activity,List<Map<String, Object>> bankAccountList ,String accountSeq) {
        settingCookiesToLianlong();
        ModuleManager.instance.gotoCardmessageAndSetActivity(activity,bankAccountList,accountSeq);
    }


    private static void settingCookiesToLianlong(){
        if (ApplicationContext.getInstance().isLogin()){
            LoginContext.instance.saveCookiesToLianLong();
        } else {
            BaseCommonTools.getInstance().SetCookie(null, null);
        }
    }
}
