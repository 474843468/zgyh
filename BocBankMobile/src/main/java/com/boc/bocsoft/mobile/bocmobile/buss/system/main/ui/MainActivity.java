package com.boc.bocsoft.mobile.bocmobile.buss.system.main.ui;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.Toast;

import com.boc.bocsoft.mobile.bocmobile.ApplicationContext;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BroadcastConst;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussActivity;
import com.boc.bocsoft.mobile.bocmobile.base.utils.LogUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.ErrorDialog;
import com.boc.bocsoft.mobile.bocmobile.buss.common.eventbus.BocEventBus;
import com.boc.bocsoft.mobile.bocmobile.buss.login.ui.LoginBaseActivity;
import com.boc.bocsoft.mobile.bocmobile.buss.system.main.eventbus.event.GoHomeEvent;
import com.boc.bocsoft.mobile.bocmobile.buss.system.main.eventbus.event.GoInvestEvent;
import com.boc.bocsoft.mobile.bocmobile.buss.system.main.eventbus.event.SessionOutEvent;
import com.boc.bocsoft.mobile.framework.ui.ActivityManager;

import java.util.Timer;
import java.util.TimerTask;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 手机银行主页面
 * Created by Administrator on 2016/5/23.
 */
public class MainActivity extends BussActivity {

  /** Intent Action 跳转首页面 */
  public static final String ACTION_HOME_TAB = "com.boc.tohome";
  /** Intent Action 跳转投资 */
  public static final String ACTION_INVEST_TAB = "com.boc.toinvest";
  /** Intent Action 跳转生活 */
  public static final String ACTION_LIFE_TAB = "com.boc.tolife";
  /** Intent Action 跳转沟通页面 */
  public static final String ACTION_MESSAGE_TAB = "com.boc.tomessage";
  /** Intent Action 跳转我的页面 */
  public static final String ACTION_MINE_TAB = "com.boc.tomine";

  /**
   * 首页切登录
   */
  public static final String ACTION_HOMELOGIN_TAB = "com.boc.tohomeforLogin";

  private static final String LOG_TAG = MainActivity.class.getSimpleName();

  private InterfaceFaultReceiver faultReceiver;
  private ErrorDialog timeOutDialog;
  private MainFragment mainFragment;

  private int targetTab = -1;
  private static boolean isExit = false;

  @Override protected void onCreate(Bundle savedInstanceState) {
    LogUtils.i(LOG_TAG, "onCreate start...");
    super.onCreate(savedInstanceState);
    if (savedInstanceState != null) {
      boolean isToHome = savedInstanceState.getBoolean("EVENT_TO_HOME", false);
      targetTab = isToHome ? 0 : -1;
    }
    IntentFilter filter = new IntentFilter();
    filter.addAction(BroadcastConst.INTENT_ACTION_SERVICE_FAULT);//故障
    filter.addAction(BroadcastConst.INTENT_ACTION_SERVICE_TIMEOUT);//超时
    faultReceiver = new InterfaceFaultReceiver();
    registerReceiver(faultReceiver, filter);
    ApplicationContext.getInstance().initTimer();

    goToMessageTab(getIntent());
  }

  @Override protected void onNewIntent(Intent intent) {
    super.onNewIntent(intent);
    goToMessageTab(intent);
  }

  private void goToMessageTab(Intent intent) {
    if (intent == null || intent.getAction() == null || !intent.getAction().startsWith("com.boc")) {
      return;
    }
    MainFragment fragment = findFragment(MainFragment.class);
    if (fragment != null) {
      popTo(MainFragment.class, false);
      setMainTab(getIndexByAction(intent.getAction()));
    } else {
      targetTab = getIndexByAction(intent.getAction());
    }
    if (ACTION_HOMELOGIN_TAB.equals(intent.getAction())) {
      Intent loginIntent = new Intent();
      intent.setClass(MainActivity.this, LoginBaseActivity.class);
      startActivity(loginIntent);
    }
  }

  @Override public void initView() {
    LogUtils.i(LOG_TAG, "initView start...");
    mainFragment = new MainFragment();
    if (findFragment(MainFragment.class) == null) {
      start(mainFragment);
    }

    BocEventBus.getInstance()
        .getBusObservable()
        .subscribeOn(Schedulers.newThread())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(mainBusSubscriber);
  }

  @Override protected boolean isHaveTitleBarView() {
    return false;
  }

  /**
   * 全局的监听
   */
  private final class InterfaceFaultReceiver extends BroadcastReceiver {

    @Override public void onReceive(Context context, Intent intent) {
      String action = intent.getAction();
      String message = intent.getStringExtra(INTENT_MESSAGE);

      if (action.equals(BroadcastConst.INTENT_ACTION_SERVICE_TIMEOUT)) {
        onServiceTimeout(message, ActivityManager.getAppManager().currentActivity());
      }
    }
  }

  @Override protected void onResumeFragments() {
    LogUtils.i(LOG_TAG, "onResumeFragments start..." + targetTab);
    super.onResumeFragments();
    if (targetTab >= 0) {
      MainActivity.this.popTo(MainFragment.class, false);
      mainFragment.setCurrentTab(targetTab);
      targetTab = -1;
    }
  }

  /**
   * 跳转页面
   */
  public void jumpPage(int page) {
    if (mainFragment != null) {
      mainFragment.setCurrentTab(page);
    }
  }

  @Override protected void onDestroy() {
    LogUtils.i(LOG_TAG, "onDestroy start...");
    unregisterReceiver(faultReceiver);
    mainBusSubscriber.unsubscribe();
    super.onDestroy();
  }

  /**
   * 会话超时错误，返回到登陆页面
   */
  public void onServiceTimeout(String message, Activity activity) {

    // 关闭对话框

    BussActivity currentActivy = (BussActivity) activity;
    currentActivy.closeProgressDialog();
    if (timeOutDialog == null) {
      timeOutDialog = new ErrorDialog(currentActivy);

      timeOutDialog.setOnBottomViewClickListener(new ErrorDialog.OnBottomViewClickListener() {

        @Override public void onBottomViewClick() {
          timeOutDialog.dismiss();
          BocEventBus.getInstance().post(new SessionOutEvent());
          //onSessionTimeout();
        }
      });
    }

    if (!timeOutDialog.isShowing()) {
      timeOutDialog.setErrorData(message);
      timeOutDialog.setBtnText("确定");
      timeOutDialog.show();
    }
  }

  /**
   * session超期时处理
   */
  protected void onSessionTimeout() {
    ApplicationContext.getInstance().logout();
    Intent intent = new Intent();
    intent.setClass(mContext, LoginBaseActivity.class);
    startActivity(intent);
  }

 /*   public void setIsToHome(boolean isToHome){
        this.isToHome = isToHome;
    }*/

  private void setMainTab(final int index) {
    targetTab = index;
    if (mainFragment == null) return;
    if (mainFragment.isAdded()) {
      mainFragment.setCurrentTab(index);
      targetTab = -1;
    }
  }

  /********************
   * EventBus  *
   *********************/

  private Subscriber<? super Object> mainBusSubscriber = new Subscriber<Object>() {
    @Override public void onCompleted() {
    }

    @Override public void onError(Throwable e) {
    }

    @Override public void onNext(Object event) {
      if (event instanceof GoHomeEvent) {
        setMainTab(0);
        ActivityManager.getAppManager().popToActivity(MainActivity.class);
      } else if (event instanceof GoInvestEvent) {
        setMainTab(1);
        ActivityManager.getAppManager().popToActivity(MainActivity.class);
      } else if (event instanceof SessionOutEvent) {
        ApplicationContext.getInstance().logout();

        if (MainActivity.this != ActivityManager.getAppManager().currentActivity()) {
          ActivityManager.getAppManager().popToActivity(MainActivity.class);
        }
        Intent intent = new Intent();
        intent.setClass(mContext, LoginBaseActivity.class);
        startActivity(intent);
      }
    }
  };

  @Override public void onBackPressed() {

    // 这里是防止动画过程中，按返回键取消加载Fragment
    setFragmentClickable(true);

    if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
      mFragmentation.back(getSupportFragmentManager());
    } else {
      exitBy2Click();
      //ActivityManager.getAppManager().finishActivity();
    }
  }

  public void exitBy2Click() {
    Timer tExit = null;
    if (isExit == false) {
      isExit = true;
      Toast.makeText(mContext, "再按一次退出应用", Toast.LENGTH_SHORT).show();
      tExit = new Timer();
      tExit.schedule(new TimerTask() {
        @Override public void run() {
          isExit = false;
        }
      }, 2000);
    } else {
      ActivityManager.getAppManager().AppExit(mContext);
    }
  }

  /**
   * 重新启动主页面
   */
  public static void startActivity2Tab(Context context, String action) {
    Intent intent = new Intent();
    intent.setClass(context, MainActivity.class);
    intent.setAction(action);
    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    try {
      context.startActivity(intent);
    } catch (Exception e) {
      intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
      context.startActivity(intent);
    }
  }

  public static int getIndexByAction(String action) {
    if (action == null) return 0;
    switch (action) {
      case ACTION_HOME_TAB:
        return 0;
      case ACTION_INVEST_TAB:
        return 1;
      case ACTION_LIFE_TAB:
        return 2;
      case ACTION_MESSAGE_TAB:
        return 3;
      case ACTION_MINE_TAB:
        return 4;
    }
    return 0;
  }
}
