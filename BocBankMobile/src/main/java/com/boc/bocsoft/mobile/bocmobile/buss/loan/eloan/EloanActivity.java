package com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan;

import android.app.Activity;
import android.view.KeyEvent;
import android.view.MotionEvent;

import com.boc.bocsoft.mobile.bocmobile.base.activity.BussActivity;
import com.boc.bocsoft.mobile.bocmobile.base.utils.LogUtils;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.apply.ui.ApplyFragment;
import com.boc.bocsoft.mobile.framework.ui.ActivityManager;

/**
 * 中银E贷主activity
 * Created by lxw4566 on 2016/6/21.
 */
public class EloanActivity extends BussActivity{

    private final static String LOG_TAG = EloanActivity.class.getSimpleName();
    @Override
    public void initView() {
//        start(new TransferFragment());
  //      start(new EloanStatusFragment());
      start(new ApplyFragment());
    }

    @Override
    public void initData() {

        EloanContext.instance.init(this.getApplication());
    }

    @Override
    public void setListener() {

    }
    /**
     * session超期时处理
     */
    protected void onSessionTimeout(){
        LogUtils.i(LOG_TAG, "后台网络超时，将跳转的登录页面...");
//        EloanContext.instance.unregisterReceiver();
        EloanContext.instance.getCommonTools().loginTimeOutHandler(this, "LogOut!!!");
        ActivityManager.getAppManager().finishActivity();
        LogUtils.i(LOG_TAG, "已结束当前Activity");
    }

//    @Override
//    public boolean dispatchTouchEvent(MotionEvent ev) {
//        boolean isDispatch = super.dispatchTouchEvent(ev);
//        Activity activity = ActivityManager.getAppManager()
//                .currentActivity() ;
//        if (activity == null){
//            return false;
//
//        } else {
//
//            if (MotionEvent.ACTION_DOWN == ev.getAction()) {
//                EloanContext.instance.reSetalarmPre();
//            }
//
//            return true;
//        }
//    }
//
//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//
//        return super.onTouchEvent(event);
//    }
//
//    @Override
//    public boolean dispatchKeyEvent(KeyEvent event) {
//        super.dispatchKeyEvent(event);
//        return true;
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        // 关闭后台的闹钟
//        EloanContext.instance.stopAlarmBack();
//        // 开启前台的闹钟
//        EloanContext.instance.reSetalarmPre();
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        // 开启后台的闹钟
//        EloanContext.instance.reSetalarmBack();
//        // 关闭前台的闹钟
//        EloanContext.instance.stopAlarmPre();
//    }

}
