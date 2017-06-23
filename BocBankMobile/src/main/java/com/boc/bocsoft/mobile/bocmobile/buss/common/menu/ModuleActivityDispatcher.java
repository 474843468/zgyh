package com.boc.bocsoft.mobile.bocmobile.buss.common.menu;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.boc.bocsoft.mobile.bocmobile.ApplicationContext;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussActivity;
import com.boc.bocsoft.mobile.bocmobile.base.utils.LogUtils;
import com.boc.bocsoft.mobile.bocmobile.buss.common.LoginCallback;
import com.boc.bocsoft.mobile.bocmobile.buss.common.eventbus.BocEventBus;
import com.boc.bocsoft.mobile.bocmobile.buss.common.lianlong.ModuleDispatcher;
import com.boc.bocsoft.mobile.bocmobile.buss.login.ui.LoginBaseActivity;
import com.boc.bocsoft.mobile.bocmobile.buss.login.ui.LoginContext;
import com.boc.bocsoft.mobile.bocmobile.buss.system.main.eventbus.event.GoHomeEvent;
import com.boc.bocsoft.mobile.bocmobile.buss.system.main.ui.MainActivity;
import com.boc.bocsoft.mobile.common.utils.StringUtils;
import com.boc.bocsoft.mobile.framework.ui.ActivityManager;
import com.chinamworld.boc.commonlib.BaseCommonTools;
import com.chinamworld.boc.commonlib.ModuleManager;

import java.util.Map;

/**
 * 模块activity分发器
 * Created by lxw on 2016/8/12 0012.
 */
public class ModuleActivityDispatcher {

    private static final String LOG_TAG = ModuleActivityDispatcher.class.getSimpleName();

    public static void dispatch(BussActivity activity, String moduleId){
        dispatch(activity, moduleId, null);
    }

  /**
   * 跳转未在菜单配置的连龙功能
   */
  private static void dispatch2LianLongNoCheck(BussActivity activity, String moduleId,Map<String, Object> params){

      if(StringUtils.isEmptyOrNull(moduleId)){
          LogUtils.d(LOG_TAG,"---模块跳转 目标不存在");
          return;
      }

      String id = ModuleDispatcher.convertLianLongId(moduleId);
      if(StringUtils.isEmpty(id)){
          LogUtils.d(LOG_TAG,"---模块跳转 目标不存在");
          return;
      }
      if (ApplicationContext.getInstance().isLogin()){
          LoginContext.instance.saveCookiesToLianLong();
      } else {
          BaseCommonTools.getInstance().SetCookie(null, null);
      }
      ModuleManager.instance.gotoModule(activity, id, params);
    }


    public static void dispatch(BussActivity activity, String moduleId, Map<String, Object> params){

        LogUtils.i(LOG_TAG, "dispatch start...");
        Menu menu = ApplicationContext.getInstance().getMenu();
        Item item = menu.findItemById(moduleId);

        if(item == null){
            //未在菜单配置
            dispatch2LianLongNoCheck(activity,moduleId,params);
            return;
        }

        if ("1".equals(item.getOld())){
            LogUtils.i(LOG_TAG, "跳转联龙博通模块...");
            String id = ModuleDispatcher.convertLianLongId(moduleId);
            LogUtils.i(LOG_TAG, "模块ID:" + id);
            String login = item.getLogin();
            if ("1".equals(login)){
//                User user = ApplicationContext.getInstance().getUser();
                if(ApplicationContext.getInstance().isLogin()){
                    LogUtils.i(LOG_TAG, "开始跳转联龙博通模块，模块id为:" + id);
                    LoginContext.instance.saveCookiesToLianLong();
                    ModuleManager.instance.gotoModule(activity, id, params);

                } else {
                    LogUtils.i(LOG_TAG, "当前应用未登录，需要登录，跳转登录页面");
                    BaseCommonTools.getInstance().SetCookie(null, null);
                    startToLogin(activity, new LoginCallbackImpl(moduleId));
                }
            } else {
                LogUtils.i(LOG_TAG, "开始跳转联龙博通模块，模块id为:" + id);
                if (ApplicationContext.getInstance().isLogin()){
                    LoginContext.instance.saveCookiesToLianLong();
                } else {
                    BaseCommonTools.getInstance().SetCookie(null, null);
                }
                ModuleManager.instance.gotoModule(activity, id, params);
            }

        } else {
            LogUtils.i(LOG_TAG, "跳转中行开发模块...");
            String login = item.getLogin();
            if ("1".equals(login)){
//                User user = ApplicationContext.getInstance().getUser();
                if(ApplicationContext.getInstance().isLogin()){
                    LogUtils.i(LOG_TAG, "开始跳转中行开发模块，模块id为:" + moduleId);
                    Intent intent = new Intent();
                    if (params != null) {
                        Bundle bundle = new Bundle();
                        for(Map.Entry<String, Object> entry : params.entrySet()){
                            bundle.putString(entry.getKey(), (String) entry.getValue());
                        }
                        intent.putExtras(bundle);
                    }
                    intent.setClass(activity, BussActivity.class);
                    intent.putExtra(BussActivity.MODULE_ID, moduleId);
                    activity.startActivity(intent);
                    activity.overridePendingTransition( R.anim.boc_infromright, R.anim.boc_outtoleft);
                } else {
                    LogUtils.i(LOG_TAG, "当前应用未登录，需要登录，跳转登录页面");
                    startToLogin(activity, new LoginCallbackImpl(moduleId));
                }
            } else {
                LogUtils.i(LOG_TAG, "开始跳转中行开发模块，模块id为:" + moduleId);
                Intent intent = new Intent();
                if (params != null) {
                    Bundle bundle = new Bundle();
                    for(Map.Entry<String, Object> entry : params.entrySet()){
                        bundle.putString(entry.getKey(), (String) entry.getValue());
                    }
                    intent.putExtras(bundle);
                }
                intent.setClass(activity, BussActivity.class);
                intent.putExtra(BussActivity.MODULE_ID, moduleId);
                activity.startActivity(intent);
                activity.overridePendingTransition( R.anim.boc_infromright, R.anim.boc_outtoleft);
            }

        }
        LogUtils.i(LOG_TAG, "dispatch end...");
    }

    /**
     * 跳转到登录页面
     */
    public static void startToLogin(Activity activity, LoginCallback callback){
        Intent intent = new Intent();
        intent.setClass(activity, LoginBaseActivity.class);
        if (callback != null) {
            LoginContext.instance.setCallback(callback);
        }

        activity.startActivity(intent);
        activity.overridePendingTransition( R.anim.boc_infromright, R.anim.boc_outtoleft);
    }

    /**
     * 跳转到登录页面
     */
    public static void startToLoginForResult(Activity activity, LoginCallback callback, int resultCode){
        Intent intent = new Intent();
        intent.setClass(activity, LoginBaseActivity.class);
        if (callback != null) {
            LoginContext.instance.setCallback(callback);
        }

        activity.startActivityForResult(intent, resultCode);
        //activity.startActivity(intent);
        activity.overridePendingTransition( R.anim.boc_infromright, R.anim.boc_outtoleft);
    }

    /**
     * 跳转到登录页面
     */
    public static void startToLogin(Activity activity){
        startToLogin(activity, null);
    }

    /***
     * 登录成功后回调
     */
    static class LoginCallbackImpl implements LoginCallback {
        private String id;
        public LoginCallbackImpl(String id){
            this.id = id;
        }
        @Override
        public void success() {

            ModuleActivityDispatcher.dispatch((BussActivity) ActivityManager.getAppManager().currentActivity(), id);
        }
    }

    /**
     * 返回到频道页
     */
    public static void popToHomePage(){
        final MainActivity mainActivity = (MainActivity) ActivityManager.getAppManager().getActivity(MainActivity.class);
        //mainActivity.setIsToHome(true);
        //BocEventBus.getInstance().post(new GoHomeEvent());
        ActivityManager.getAppManager().popToActivity(MainActivity.class);

    }

    public static void popToMainPage(int pageIndex){
        //
        ActivityManager.getAppManager().popToActivity(MainActivity.class);

    }
}
