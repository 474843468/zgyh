package com.boc.bocsoft.mobile.bocmobile.buss.common.lianlong;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import com.boc.bocsoft.mobile.bocmobile.ApplicationContext;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussActivity;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.activity.ModuleFactory;
import com.boc.bocsoft.mobile.bocmobile.base.model.User;
import com.boc.bocsoft.mobile.bocmobile.base.utils.LogUtils;
import com.boc.bocsoft.mobile.bocmobile.base.utils.SpUtils;
import com.boc.bocsoft.mobile.bocmobile.buss.account.base.BaseAccountFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.common.eventbus.BocEventBus;
import com.boc.bocsoft.mobile.bocmobile.buss.common.lianlong.event.BadgeChangeEvent;
import com.boc.bocsoft.mobile.bocmobile.buss.common.menu.ModuleCode;
import com.boc.bocsoft.mobile.bocmobile.buss.common.menu.ModuleFactoryImpl;
import com.boc.bocsoft.mobile.bocmobile.buss.system.life.event.LifeAddCommUseEvent;
import com.boc.bocsoft.mobile.bocmobile.buss.system.main.ui.MainActivity;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.wealthproduct.ui.WealthProductFragment;
import com.boc.bocsoft.mobile.bocmobile.yun.BocCloudCenter;
import com.boc.bocsoft.mobile.cr.bus.product.model.CRgetProductList.CRgetProductListParams;
import com.boc.bocsoft.mobile.cr.bus.product.model.CRgetProductList.CRgetProductListResult;
import com.boc.bocsoft.mobile.cr.bus.product.service.CRProductService;
import com.boc.bocsoft.mobile.framework.ui.ActivityManager;
import java.util.HashMap;
import java.util.Map;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 中行给联龙提供的接口
 * Created by lxw on 2016/10/9 0009.
 */
public class BocCommonTools {

    /**
     * 跳转功能页面code
     */
    public interface PageCode {

        /**
         * 跳转转账汇款页面
         * TransRemitBlankFragment
         */
        String PAGE_TRANSREMITBLANK = ModuleCode.MODULE_TRANSER_0000;

        /**
         * 账户管理
         */
        String PAGE_ACCOUNTMANGER = ModuleCode.MODULE_ACCOUNT_0000;

        /**
         * 账户管理-申请定期账户
         */
        String PAGE_ACCOUNT_REGULAR_APPLY = ModuleCode.MODULE_ACCOUNT_0300;

        /**
         * 活期账户详情
         */
        String PAGE_MODULE_ACCOUNT_DETAIL = ModuleCode.MODULE_ACCOUNT_DETAIL;
    }

    /**
     * 返回主页面
     */
    public void toHomePage() {
        ActivityManager.getAppManager().popToActivity(MainActivity.class);
        //ModuleActivityDispatcher.popToHomePage();
    }

     /**
     * 跳转消息频道
     */
    public void toMessagePage(int tabIndex, String tag) {
        LogUtils.d("dding", "----跳转 频道页面:" + tabIndex + " :" + tag);
    }

    public Intent getToMessageIntent(Context context){
      Intent intent = new Intent();
      intent.setClass(context,MainActivity.class);
      intent.setAction(MainActivity.ACTION_MESSAGE_TAB);
      intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
      intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
      return intent;
    }

  /**
   * 设置 沟通小红点
   * @param isShow 是否显示
   * @param count 消息数
   */
    public void setBadgeNumber(boolean isShow,int count){
        BocEventBus.getInstance().post(new BadgeChangeEvent(isShow,count));
    }

    /**
     * 解绑设备成功
     */
    public void onUnBindDeviceSuccess() {
      LogUtils.d("dding", "--- 设备解绑成功 ----");
      //SpUtils.remove();
      String loginName =
          SpUtils.getLNhoneSpString(ApplicationContext.getInstance(), SpUtils.SPKeys.KEY_LOGINNAME, "");
      //清除用户数据
      SpUtils.removeLNhoneSpString(ApplicationContext.getInstance(), SpUtils.SPKeys.KEY_LOGINNAME);
      SpUtils.remove(ApplicationContext.getInstance(),SpUtils.SPKeys.KEY_LOGINIDENTITYNUMBER);
        SpUtils.remove(ApplicationContext.getInstance(),SpUtils.SPKeys.KEY_LOGINIDENTITYTYPE);
        BocCloudCenter.getInstance().clear(loginName);
      ApplicationContext.getInstance().logout();
      ActivityManager.getAppManager().popToActivity(MainActivity.class);
    }

    /**
     * 修改欢迎信息成功
     *
     * @param newMsg 新的欢迎信息
     */
    public void onModifyWelcomeInfoSuccess(String newMsg) {
        LogUtils.d("dding", "---- 修改欢迎信息成功 -- >:" + newMsg);
        User user = ApplicationContext.getInstance().getUser();
        if (user == null) return;
        user.setLoginHint(newMsg);
    }

    /**
     * 生活缴费 - 添加常用缴费成功
     */
    public void onAddCommonUsePaymentSuccess() {
        LogUtils.d("dding", "添加常用菜单成功:,请求新的个人常用菜单");
        BocEventBus.getInstance().post(new LifeAddCommUseEvent());
    }

    /**
     * 连龙跳转功能页面接口
     *
     * @param pageCode 跳转目标功能 {@link PageCode}
     * @param params   携带的参数
     */
    public void toBocPage(Activity activity, String pageCode, HashMap<String, String> params) {
        LogUtils.d("dding", "--- 跳转请求:" + pageCode);

        if(PageCode.PAGE_ACCOUNTMANGER.equals(pageCode)){
          //账户列表
          ActivityManager.getAppManager().finishActivity(BussActivity.class);
        }

        ModuleFactory factory = new ModuleFactoryImpl();
        BussFragment bussFragment = factory.getModuleFragmentInstance(pageCode);
        if (bussFragment == null) {
            LogUtils.e("dding", "----请求模块不正确:" + pageCode);
            return;
        }
        Intent intent = new Intent();
        intent.setClass(activity, BussActivity.class);
        intent.putExtra(BussActivity.MODULE_ID, pageCode);
        if (params != null) {
            Bundle bundle = new Bundle();
            for (Map.Entry<String, String> entry : params.entrySet()) {
                bundle.putString(entry.getKey(), entry.getValue());
            }
            intent.putExtras(bundle);
        }
        try {
            activity.startActivity(intent);
        } catch (Exception e) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            activity.startActivity(intent);
        }
    }

    /**
     * 连龙跳转功能页面接口
     *
     * @param pageCode 跳转目标功能 {@link PageCode}
     * @param params   携带的参数
     */
    public void toBocPage(Activity activity, String pageCode, HashMap<String, String> params, IBocCallBack callBack) {
        LogUtils.d("dding", "--- 跳转请求:" + pageCode);
        ModuleFactory factory = new ModuleFactoryImpl();
        BussFragment bussFragment = factory.getModuleFragmentInstance(pageCode);
        if (bussFragment == null) {
            LogUtils.e("dding", "----请求模块不正确:" + pageCode);
            return;
        }

        if (pageCode == ModuleCode.MODULE_ACCOUNT_0300)
            ((BaseAccountFragment) bussFragment).setCallBack(callBack);

        Intent intent = new Intent();
        intent.setClass(activity, BussActivity.class);
        intent.putExtra(BussActivity.MODULE_ID, pageCode);
        if (params != null) {
            Bundle bundle = new Bundle();
            for (Map.Entry<String, String> entry : params.entrySet()) {
                bundle.putString(entry.getKey(), entry.getValue());
            }
            intent.putExtras(bundle);
        }
        try {
            activity.startActivity(intent);
        } catch (Exception e) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            activity.startActivity(intent);
        }
    }


    /**
     * 获取产品推荐列表
     *
     * @return Subscription
     * 在退出页面时应该判断 并调用 (  if(subscription.isUnsubscribed())subscription.unsubscribe(); )
     */
    public Subscription getProductList(final IBocCallBack callBack) {
        if (callBack == null) return null;

        CRgetProductListParams params = new CRgetProductListParams();
        return new CRProductService().cRgetProductList(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<CRgetProductListResult>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable throwable) {
                        callBack.callBack(throwable, false);
                    }

                    @Override
                    public void onNext(CRgetProductListResult result) {
                        callBack.callBack(result, true);
                    }
                });
    }

    /**
     * 回调接口
     */
    public interface IBocCallBack {
        /**
         * @param isSuccess 标示成功||失败
         */
        void callBack(Object obj, boolean isSuccess);
    }

    /**
     * 更新投资理财服务状态
     *
     * @param isOpen
     */
    public void updateOpenWealthStatus(boolean isOpen){
        boolean[] openStatus = WealthProductFragment.getInstance().isOpenWealth();
        openStatus[0] = isOpen;
    }
}
