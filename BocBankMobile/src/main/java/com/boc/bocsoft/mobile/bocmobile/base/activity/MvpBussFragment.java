package com.boc.bocsoft.mobile.bocmobile.base.activity;

import android.os.Bundle;

import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.loadingDialog.GlobalLoadingDialog;
import com.boc.bocsoft.mobile.framework.ui.BasePresenter;
import com.boc.bocsoft.mobile.framework.ui.ViewWithPresenter;

/**
 * 作者：XieDu
 * 创建时间：2016/9/23 16:07
 * 描述：
 */
public abstract class MvpBussFragment<P extends BasePresenter> extends BussFragment
        implements ViewWithPresenter<P> {

    private P mPresenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onDestroy() {
        stopPresenter();
        super.onDestroy();
    }

    @Override
    public void onStart() {
        startPresenter();
        super.onStart();
    }

    @Override
    public final P getPresenter() {
        if (mPresenter == null) {
            mPresenter = initPresenter();
        }
        return mPresenter;
    }

    protected abstract P initPresenter();

    public void stopPresenter() {
        if (getPresenter() != null) {
            getPresenter().unsubscribe();
        }
    }

    public void startPresenter() {
        if (getPresenter() != null) {
            getPresenter().subscribe();
        }
    }

    /**
     * 显示loading框之前恢复presenter工作
     */
    public GlobalLoadingDialog showLoadingDialog(String message, boolean flag) {
        startPresenter();
        return super.showLoadingDialog(message, flag);
    }
    @Override
    public void onCloseLoadingDialog() {
        stopPresenter();
    }
}
