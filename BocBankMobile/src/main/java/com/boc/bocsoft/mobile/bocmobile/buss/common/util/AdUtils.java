package com.boc.bocsoft.mobile.bocmobile.buss.common.util;

import android.app.Activity;
import android.content.Intent;
import android.widget.Toast;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.WebViewActivity;
import com.boc.bocsoft.mobile.bocmobile.buss.common.LoginCallback;
import com.boc.bocsoft.mobile.bocmobile.buss.common.eventbus.BocEventBus;
import com.boc.bocsoft.mobile.bocmobile.buss.common.menu.ModuleActivityDispatcher;
import com.boc.bocsoft.mobile.bocmobile.buss.system.home.model.AdvertisementModel;
import com.boc.bocsoft.mobile.bocmobile.buss.system.main.eventbus.event.LoginedEvent;
import com.boc.bocsoft.mobile.common.utils.StringUtils;
import com.boc.bocsoft.mobile.framework.ui.ActivityManager;

/**
 * Created by dingeryue on 2016年10月25.
 * 广告跳转工具类
 */

public class AdUtils {

  private final static String ACTION = "action";

  public static void onAdClick(AdvertisementModel item){
    if(item == null)return;

    String type = item.getPosterType();
    Activity activity = ActivityManager.getAppManager().currentActivity();
    if ("0".equals(type)) {
      //活动地址
      String url = item.getPosterUrl();
      String title = item.getPosterName();


      if (StringUtils.isEmptyOrNull(url)){

        //Toast.makeText(activity, "广告后台配置的地址为空", Toast.LENGTH_SHORT).show();
        return;

      }
      if(url.startsWith(ACTION)){
        ModuleActivityDispatcher.startToLogin(ActivityManager.getAppManager().currentActivity());
        return;
      }


      Intent intent = new Intent();
      intent.putExtra(WebViewActivity.URL, url);
      intent.putExtra(WebViewActivity.TITLE, title);
      intent.setClass(activity, WebViewActivity.class);
      activity.overridePendingTransition( R.anim.boc_infromright, R.anim.boc_outtoleft);
      activity.startActivity(intent);


    }

    // 更业务确认目前都是活动的方式，产品码并未使用
    else if ("1".equals(type)) {
      // 活动地址

      String url = item.getPosterUrl();
      String title = item.getPosterName();
      if (StringUtils.isEmptyOrNull(url)){

        //Toast.makeText(activity, "广告后台配置的地址为空", Toast.LENGTH_SHORT).show();
        return;

      }

      if(url.startsWith(ACTION)){
        ModuleActivityDispatcher.startToLogin(ActivityManager.getAppManager().currentActivity(), new LoginCallback() {
          @Override
          public void success() {
            BocEventBus.getInstance().post(new LoginedEvent());
          }
        });
        return;
      }

      Intent intent = new Intent();
      intent.putExtra(WebViewActivity.URL, url);
      intent.putExtra(WebViewActivity.TITLE, title);
      intent.setClass(activity, WebViewActivity.class);
      activity.overridePendingTransition( R.anim.boc_infromright, R.anim.boc_outtoleft);
      activity.startActivity(intent);
    }


  }
}
