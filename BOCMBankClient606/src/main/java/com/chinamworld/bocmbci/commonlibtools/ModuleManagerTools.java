package com.chinamworld.bocmbci.commonlibtools;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.chinamworld.boc.commonlib.ModuleManager;
import com.chinamworld.boc.commonlib.model.GoldDataModel;
import com.chinamworld.boc.commonlib.model.IActionCallBack;
import com.chinamworld.bocmbci.base.activity.ActivitySwitcher;
import com.chinamworld.bocmbci.base.activity.BaseActivity;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.base.application.CommonApplication;
import com.chinamworld.bocmbci.biz.bocinvt.productlist.QueryProductActivity;
import com.chinamworld.bocmbci.biz.bocinvt.productlist.QueryProductOutlayActivity;
import com.chinamworld.bocmbci.biz.branchorder.BranchOrderMainActivity;
import com.chinamworld.bocmbci.biz.crcd.CrcdModuleTools;
import com.chinamworld.bocmbci.biz.dept.DeptTools;
import com.chinamworld.bocmbci.biz.finc.FincIntentNew;
import com.chinamworld.bocmbci.biz.finc.finc_p606.FincProductDetailActivity;
import com.chinamworld.bocmbci.biz.foreign.ForeignTools;
import com.chinamworld.bocmbci.biz.infoserve.InfoServeMainFragment;
import com.chinamworld.bocmbci.biz.login.LoginModuleTools;
import com.chinamworld.bocmbci.biz.lsforex.IsforexTools;
import com.chinamworld.bocmbci.biz.main.Item;
import com.chinamworld.bocmbci.biz.main.MainSetting;
import com.chinamworld.bocmbci.biz.plps.PlpsTools;
import com.chinamworld.bocmbci.biz.prms.PrmsTools;
import com.chinamworld.bocmbci.biz.prms.price.PrmsNewPricesActivity;
import com.chinamworld.bocmbci.biz.prms.price.PrmsNewPricesDetailActivity;
import com.chinamworld.bocmbci.biz.sbremit.mysbremit.ChooseAccountActivity;
import com.chinamworld.bocmbci.biz.sbremit.rate.SBRemitRateInfoOutlayActivity;
import com.chinamworld.bocmbci.biz.servicerecord.MoreApp;
import com.chinamworld.bocmbci.biz.servicerecord.ServiceRecordQueryActivity;
import com.chinamworld.bocmbci.server.BocMBCManager;
import com.chinamworld.bocmbci.utils.ActivityIntentTools;
import com.chinamworld.bocmbci.utils.LoginTask;
import com.chinamworld.bocmbci.widget.entity.ImageAndText;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ModuleManagerTools extends ModuleManager {



    public ModuleManagerTools() {
        instance = this;
    }

    @Override
    public void gotoModule(Activity context, String moduleId,
                           Map<String, Object> data) {
        List<Item> firstMenuItem = MainSetting.initItem(context);
        List<Item> secordMenuItem = MainSetting.initsecondItem(context);
        List<Item> listMenuItem = new ArrayList<Item>();
        for (Item item : firstMenuItem) {
            listMenuItem.add(item);
        }
        for (Item item : secordMenuItem) {
            listMenuItem.add(item);
        }
        Item mainItem = null;
        ImageAndText menuItem = null;
        for (Item item : listMenuItem) {
            if (item.MenuID.equals(moduleId)) {
                // 九宫格菜单
                mainItem = item;
                break;
            }
            if (item.getImageAndText() == null)
                continue;
            for (ImageAndText it : item.getImageAndText()) {
                if (it.MenuID.equals(moduleId)) {
                    mainItem = item;
                    menuItem = it;
                    break;
                }
            }
            if (menuItem != null)
                break;
        }
        ActivitySwitcher as = new ActivitySwitcher(context, false);
        if (menuItem != null) {
            // 进入某个二级菜单
            as.openFast(menuItem, -1);
            return;
        }

        if (mainItem != null) {
            // 需要进入的九宫格菜单
            as.openModule(mainItem.getName(), false);
            return;
        }
    }

    @Override
    public void gotoFincModule(Activity arg0, String fincCode) {
        // 判断finccode进入基金详情界面，登陆后需要判断开通投资理财
//        FincIntent.getIntent().fincIntent(arg0, fincCode);
       // FincIntentNew.getIntent().fincIntent(arg0, fincCode);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("fromOtherFundCode", fincCode);
        map.put("isFromOther", "1");
        ActivityIntentTools.intentToActivityWithData(arg0, FincProductDetailActivity.class, map);
    }

    @Override
    public void gotoGoldModule(Activity arg0, GoldDataModel arg1) {
        // 跳入贵金属
        Intent intent = new Intent();
        PrmsNewPricesActivity.prmsFlagGoWay = 1;
        intent.setClass(arg0, PrmsNewPricesDetailActivity.class);
//        intent.putExtra("GoldDataModel", (Serializable) arg1);
        BaseDroidApp.getInstance().getBizDataMap().put("sourceCurrencyCodeStr",arg1.sourceCurrencyCode);
        BaseDroidApp.getInstance().getBizDataMap().put("targetCurrencyStr",arg1.targetCurrencyCode);
        intent.putExtra("sourceCurrencyCodeStr",arg1.sourceCurrencyCode);
        intent.putExtra("targetCurrencyStr",arg1.targetCurrencyCode);
        intent.putExtra("enters",1);//1是中行跳进去,2 内部跳进去,3是主页面扫二维码进去
        arg0.startActivity(intent);
    }

    @Override
    public void gotoInvestModule(Activity activity, String productCode) {
        //进入中银理财
        Map<String, Object> paramsmap = new HashMap<String, Object>();
        paramsmap.put("productCode", productCode);
        paramsmap.put("isEnterDetail", true);
        if (BaseDroidApp.getInstanse().isLogin()) {
            ActivityIntentTools.intentToActivityWithData(activity, QueryProductActivity.class, paramsmap);
        } else {
            ActivityIntentTools.intentToActivityWithData(activity, QueryProductOutlayActivity.class, paramsmap);
        }
    }

    /** 进入结售汇功能模块
     * context:当前activity
     * data：上送数据项，结售汇货币对。目前暂时不使用
     *  */
    @Override
    public void gotoSBRemitModule(Activity arg0, Map<String, Object> arg1) {
        // 登陆后跳入结汇购汇
        if (BaseDroidApp.getInstanse().isLogin()) { // 进入我的结汇购汇
                Intent intent = new Intent();
                intent.setClass(arg0, ChooseAccountActivity.class);
                arg0.startActivity(intent);

        } else { // 进入未登录外汇牌价页面
            Intent intent = new Intent();
            intent.setClass(arg0, SBRemitRateInfoOutlayActivity.class);
            arg0.startActivity(intent);

        }

    }


    @Override
    public void gotoForgetPasswordModule(Activity activity) {
        new LoginModuleTools(activity).gotoForgetPasswordModule(activity);
    }

    @Override
    public void gotoRegistModule(Activity activity) {
        new LoginModuleTools(activity).gotoRegistModule(activity);
    }


    @Override
    public void sendFunctionUsedAction(Activity activity, String cifNumber) {
        if (BaseDroidApp.getInstanse().getSflag()) {
            // 上送设备信息
            new BocMBCManager().sendFunctionUsedAction(activity, cifNumber);
        }
    }


    /**
     * 持卡登录成功，进入持卡登录成功页面
     * @param loginInfo: 持卡登陆返回信息； isDebit : true:借记卡；false:信用卡
     */
    @Override
    public void gotoBocnetLoginModule(Activity activity, Map<String, Object> loginInfo,String accountSeq, boolean isDebit) {
        CommonApplication.getInstance().setCurrentAct(activity);
        new LoginModuleTools(activity).gotoBocnetLoginModule(activity,loginInfo, accountSeq, isDebit);
    }

    @Override
    public void BindingDeviceForPushService(Activity activity, IActionCallBack callBack) {
        new LoginModuleTools(activity).BindingDeviceForPushService(activity,callBack);
    }

    @Override
    public Fragment GotoServeMainFragment() {
        return new InfoServeMainFragment();
    }


    /**
     * 常用缴费项目 小类 点击入口
     * prvcShortName 小类省简称
     * flowFileIdt 缴费商户名称id 即商户id
     * catName 小类名称
     * menuName小类对应的服务大类名
     * merchantName 商户名称
     * conversationId 会话id需要跟PsnPlpsQueryHistoryRecords接口会话保持一致
     */
    @Override
    public void gotoComonService(Activity activity, String prvcShortName,String flowFileId, String catName, String menuName,String merchantName,String conversationId) {
        new PlpsTools(activity).gotoComonService(activity,prvcShortName,flowFileId,catName,menuName,merchantName,conversationId);
    }

    /**
     * 全部缴费项目 小类 点击入口
     *List<Map<String, Object>> commPaymentList 常用缴费项目列表
     * catId 小类id
     * catName 小类名
     * prvcDispName 省名称
     * cityDispName 城市名
     * cityDispNo市多语言代码
     * menuName小类对应的服务大类名
     * prvcShortName 省简称
     **/
    @Override
    public void gotoAllPayment(Activity activity, List<Map<String, Object>> commPaymentList,String catId, String catName,String isAvalible, String prvcDispName, String cityDispName,String cityDispNo,String menuName, String prvcShortName) {
        new PlpsTools(activity).gotoAllPayment(activity,commPaymentList,catId,catName, isAvalible,prvcDispName,cityDispName,cityDispNo,menuName,prvcShortName);
    }

    /**添加常用入口
     * List<Map<String, Object>> commPaymentList 常用缴费项目列表
     * List<Map<String, Object>> allPaymentList 所有缴费项目列表
     * prvcShortName 省简称
     * cityDispNo 市多语言代码
     * prvcDispName 省名称
     * cityDispName 城市名
     * */
    @Override
    public void addCommService(Activity activity, List<Map<String, Object>> commPaymentList,List<Map<String, Object>> allPaymentList, String prvcShortName, String cityDispNo,String prvcDispName, String cityDispName,String conversationId) {
        new PlpsTools(activity).addCommService(activity,commPaymentList,allPaymentList,prvcShortName,cityDispNo,prvcDispName,cityDispName,conversationId);
    }

    /**
     * 跳转到服务记录查询页面
     * @param activity
     */
    @Override
    public void gotoServiceRecordQueryActivity(final Activity activity) {
        BaseActivity.getLoginUtils(activity).exe(new LoginTask.LoginCallback() {

            @Override
            public void loginStatua(boolean isLogin) {
                // TODO Auto-generated method stub
                Intent intent=new Intent(activity, ServiceRecordQueryActivity.class);
                activity.startActivity(intent);
            }
        });
    }

    /**
     * 跳转到更多中行应用页面
     * */
    @Override
    public void gotoMoreApplicationActivity(final Activity activity) {
        Intent intent=new Intent(activity, MoreApp.class);
        activity.startActivity(intent);
    }




    /* 跳转到信用卡详情页面
        * @param
        * activity ： activity
        * 请求回来的账户列表信息 (我的信用卡)  List<Map<String, Object>> bankAccountList
        * 账户id  accountSeq
        * 选择的项  position
        */
    @Override
    public void gotoCardmessageAndSetActivity(Activity activity,List<Map<String, Object>> bankAccountList ,String accountSeq) {
        new CrcdModuleTools(activity).gotoCardmessageAndSetActivity(activity, bankAccountList ,accountSeq);
    }


    /**本地小类缴费入口
     * localId 本地缴费标识
     *localId=-1 养老金服务
     *  localId=-2签约代缴服务
     *  localId=-3 预付卡
     *  localId=-4 交通异地罚款*/
    @Override
    public void gotoLocalService(Activity activity, int localId, String prvcShortName) {
        new PlpsTools(activity).gotoLocalService(activity,localId,prvcShortName);
    }

    /**缴费记录查询跳转
     * 我的民生二级菜单 liveMenus
     * */
    @Override
    public void gotoCommService(Activity activity) {
        new PlpsTools(activity).gotoCommService(activity);
    }

    /**
     * 支取 入口
     * @param activity
     * @param accountMap 用戶选择账户
     * @param selectAccountMap 选择账户信息 -- PsnCommonQueryAllChinaBankAccount查询中行所有帐户列表
     * @oaram notifyMgFlag 是否是通知存款 标识
     */
    @Override
    public void gotoWithdrawDeposits(Activity activity, Map<String, Object> accountMap,Map<String, Object> selectAccountMap,String depositsType) {
        new DeptTools(activity).gotoWithdrawDeposits(activity,accountMap,selectAccountMap,depositsType);
    }


    /**
     * 跳转到网点预约排队页面
     * @param activity
     */
    @Override
    public void gotoBranchOrderActivity(Activity activity) {
        activity.startActivity(new Intent(activity, BranchOrderMainActivity.class));
    }


    @Override
    public boolean dispatchQRModule(Activity activity, String qrInfo) {
        // 理财产品type为1，基金type为2，外汇type为3，双向宝为4，账户贵金属为5
        // boc://bocphone?type=1&productCode=afsadf
        if(new IsforexTools().isForexQRHandler(activity,qrInfo))
            return true;
        if(new PrmsTools().isPrmsQRHandler(activity,qrInfo))
            return true;
        if(new ForeignTools(activity).gotoDetailsOfForeignExchange(activity,qrInfo))
            return true;
        if(new FincIntentNew(activity).fundQRHandler(activity, qrInfo)){//跳转到基金详情页面
            return true;
        }

        return false;
    }


}

