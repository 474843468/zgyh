package com.boc.bocsoft.mobile.bocmobile.base.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;

import com.boc.bocsoft.mobile.bocmobile.ApplicationContext;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.ErrorDialog;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.loadingDialog.GlobalLoadingDialog;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.loadingDialog.LoadingDialogControl;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.loadingDialog.LoadingDialogHelper;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.EloanContext;
import com.boc.bocsoft.mobile.bocmobile.buss.login.ui.LoginContext;
import com.boc.bocsoft.mobile.framework.ui.ActivityManager;
import com.boc.bocsoft.mobile.framework.ui.TitleBarActivity;

/**
 * 手机银行通用基类
 * Created by feibin on 2016/5/28.
 */
public class BaseMobileActivity extends TitleBarActivity  implements LoadingDialogControl {

    public final static String INTENT_MESSAGE = "message";

    private Boolean _isVisible;
    protected GlobalLoadingDialog mLoadingDialog;
    private ErrorDialog errorDialog;
    //private InterfaceFaultReceiver faultReceiver;
   // private ErrorDialog timeOutDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _isVisible = true;
    }
    @Override
    public void beforeInitView() {
        super.beforeInitView();
    }

    @Override
    public GlobalLoadingDialog showLoadingDialog() {
        return showLoadingDialog("加载中...");
    }

    @Override
    public GlobalLoadingDialog showLoadingDialog(boolean flag) {
        return showLoadingDialog("加载中...", flag);
    }

    @Override
    public GlobalLoadingDialog showLoadingDialog(String message) {
        return showLoadingDialog(message, true);
    }

    @Override
    public GlobalLoadingDialog showLoadingDialog(String message, boolean flag) {
        if (_isVisible) {
            if (mLoadingDialog == null) {
                mLoadingDialog = LoadingDialogHelper.getLoadingDialog(this,message, flag);
            } else if(mLoadingDialog != null){
                mLoadingDialog.setText(message);
                mLoadingDialog.setCancelable(flag);

            }
            mLoadingDialog.show();
            return mLoadingDialog;
        }
        return null;
    }

    /**
     * 关闭loading框
     */
    @Override
    public void closeProgressDialog() {
        try{
            if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
                mLoadingDialog.cancel();
            }
        } catch(Exception ex){
            ex.printStackTrace();
        }


    }

    /**
     * 错误提示框
     * @param errorMessage
     */
    public void showErrorDialog(String errorMessage){
        synchronized (this){
            if(errorDialog==null){
                errorDialog = new ErrorDialog(mContext);
                errorDialog.setBtnText("确认");
            }
            errorDialog.setErrorData(errorMessage);
            closeProgressDialog();
            if (!errorDialog.isShowing()) {
                errorDialog.show();
            }

            errorDialog.setOnBottomViewClickListener(new ErrorDialog.OnBottomViewClickListener() {
                @Override
                public void onBottomViewClick() {
                    if(null!=mErrorDialogClickCallBack){
                        mErrorDialogClickCallBack.onEnterBtnClick();
                    }
                }
            });
        }
    }


    @Override
    public void initView() {

    }

    @Override
    public void initData() {

    }

    @Override
    public void setListener() {

    }

    private ErrorDialogClickCallBack mErrorDialogClickCallBack;

    /**
     * 设置按钮点击监听
     *
     * @param callBack
     */
    public void setErrorDialogClickListener(ErrorDialogClickCallBack callBack) {
        mErrorDialogClickCallBack = callBack;
    }

    public interface ErrorDialogClickCallBack{
        abstract void onEnterBtnClick();
    }

//    public void onServiceTimeout(String message) {
//
//        // 关闭对话框
//        closeProgressDialog();
//
//        ErrorDialog timeOutDialog = new ErrorDialog(this);
//        timeOutDialog = new ErrorDialog(this);
//        timeOutDialog.setOnBottomViewClickListener(new ErrorDialog.OnBottomViewClickListener() {
//
//            @Override
//            public void onBottomViewClick() {
//                //timeOutDialog.dismiss();
////					mActivity.jumpToFragment(MEBFragmentManager
////							.getFragmet(MEBMainPageFragment.class));
//                //mActivity.onLogout(0);
//                // TODO
//                onSessionTimeout();
//            }
//        });
//        timeOutDialog.setBtnText("确认");
//        timeOutDialog.setErrorData(message);
//        timeOutDialog.show();
//
//    }

//    /**
//     * session超期时处理
//     */
//    protected void onSessionTimeout(){
//
//    }

    @Override
    protected void onResume() {
        super.onResume();
        LoginContext.instance.setCurrentActivity(this);
//        // 关闭后台的闹钟
//        ApplicationContext.getInstance().stopAlarmBack();
//        // 开启前台的闹钟
//        ApplicationContext.getInstance().reSetalarmPre();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        boolean isDispatch = super.dispatchTouchEvent(ev);
        Activity activity = ActivityManager.getAppManager()
                .currentActivity() ;
        if (activity == null){
            return false;

        } else {

            if (MotionEvent.ACTION_DOWN == ev.getAction()) {
                ApplicationContext.getInstance().reSetalarmPre();
            }

            return true;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        return super.onTouchEvent(event);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        super.dispatchKeyEvent(event);
        return true;
    }


    @Override
    protected void onPause() {
        super.onPause();
//        // 开启后台的闹钟
//        ApplicationContext.getInstance().reSetalarmBack();
//        // 关闭前台的闹钟
//        ApplicationContext.getInstance().stopAlarmPre();
    }

    @Override
    protected void onDestroy() {
//        // 开启后台的闹钟
//        ApplicationContext.getInstance().stopAlarmBack();
//        // 关闭前台的闹钟
//        ApplicationContext.getInstance().stopAlarmPre();
        super.onDestroy();
    }
}
