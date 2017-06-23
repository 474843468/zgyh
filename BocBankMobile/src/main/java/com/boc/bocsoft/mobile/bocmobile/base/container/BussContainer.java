package com.boc.bocsoft.mobile.bocmobile.base.container;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.util.AttributeSet;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussActivity;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.loadingDialog.GlobalLoadingDialog;

/**
 * 作者：XieDu
 * 创建时间：2016/12/16 17:21
 * 描述：无presenter的容器
 */
public class BussContainer extends Container
        implements GlobalLoadingDialog.CloseLoadingDialogListener {

    protected BussFragment mBussFragment;
    protected BussActivity mBussActivity;

    public BussContainer(Context context) {
        this(context, null);
    }

    public BussContainer(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BussContainer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void attach(Activity activity) {
        if (activity != null && activity instanceof BussActivity) {
            mBussActivity = (BussActivity) activity;
        }
        super.attach(activity);
    }

    @Override
    public void attach(Fragment fragment) {
        if (fragment != null && fragment instanceof BussFragment) {
            mBussFragment = (BussFragment) fragment;
            mBussActivity = (BussActivity) mBussFragment.getActivity();
        }
        super.attach(fragment);
    }

    /**
     * 显示loading框,默认可关闭
     */
    public GlobalLoadingDialog showLoadingDialog() {
        return showLoadingDialog("加载中...");
    }

    /**
     * 显示loading框
     *
     * @param cancelable 是否有关闭按钮 true:有;false没有
     */
    public GlobalLoadingDialog showLoadingDialog(boolean cancelable) {
        return showLoadingDialog("加载中...", cancelable);
    }

    /**
     * 显示loading框，默认可关闭
     */
    public GlobalLoadingDialog showLoadingDialog(String message) {
        return showLoadingDialog(message, true);
    }

    /**
     * 显示loading框之前恢复presenter工作
     */
    public GlobalLoadingDialog showLoadingDialog(String message, boolean flag) {
        GlobalLoadingDialog globalLoadingDialog = mBussActivity.showLoadingDialog(message, flag);
        globalLoadingDialog.setCloseLoadingDialogListener(flag ? this : null);
        return globalLoadingDialog;
    }

    /**
     * 关闭loading框
     */
    public void closeProgressDialog() {
        mBussActivity.closeProgressDialog();
    }


    @Override
    public void onCloseLoadingDialog() {

    }
}
