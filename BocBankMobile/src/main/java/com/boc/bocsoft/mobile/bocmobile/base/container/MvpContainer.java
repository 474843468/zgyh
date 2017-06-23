package com.boc.bocsoft.mobile.bocmobile.base.container;

import android.content.Context;
import android.util.AttributeSet;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.loadingDialog.GlobalLoadingDialog;
import com.boc.bocsoft.mobile.framework.ui.BasePresenter;
import com.boc.bocsoft.mobile.framework.ui.BaseView;
import com.boc.bocsoft.mobile.framework.ui.ViewWithPresenter;

/**
 * 作者：XieDu
 * 创建时间：2016/12/16 16:10
 * 描述：viewpager承载多个页面时，可以用这个来代替fragment(Mvp可以用来和presenter联动请求接口数据)
 */
public abstract class MvpContainer<P extends BasePresenter> extends BussContainer
        implements ViewWithPresenter<P>, BaseView<P> {

    private P mPresenter;

    public MvpContainer(Context context) {
        this(context, null);
    }

    public MvpContainer(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MvpContainer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void onDestroy() {
        stopPresenter();
    }

    @Override
    public void setPresenter(P presenter) {
        mPresenter = presenter;
    }

    @Override
    public final P getPresenter() {
        if (mPresenter == null) {
            mPresenter = initPresenter();
        }
        return mPresenter;
    }

    protected abstract P initPresenter();

    @Override
    public void startPresenter() {
        if (getPresenter() != null) {
            getPresenter().subscribe();
        }
    }

    @Override
    public void stopPresenter() {
        if (getPresenter() != null) {
            getPresenter().unsubscribe();
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
