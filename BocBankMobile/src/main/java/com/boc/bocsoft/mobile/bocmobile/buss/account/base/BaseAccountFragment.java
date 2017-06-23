package com.boc.bocsoft.mobile.bocmobile.buss.account.base;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v4.app.FragmentTransactionBugFixHack;
import android.view.View;
import android.widget.Toast;

import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.activity.MvpBussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.utils.GlobalParams;
import com.boc.bocsoft.mobile.bocmobile.base.utils.PublicCodeUtils;
import com.boc.bocsoft.mobile.bocmobile.buss.common.lianlong.BocCommonTools;
import com.boc.bocsoft.mobile.framework.ui.BasePresenter;
import com.boc.bocsoft.mobile.framework.ui.BaseView;
import com.boc.bocsoft.mobile.framework.ui.RxPresenter;

/**
 * @author wangyang
 *         16/7/16 14:09
 *         账户Fragment基类
 */
public abstract class BaseAccountFragment<T extends BasePresenter> extends MvpBussFragment<T> implements BaseView<BasePresenter> {


    protected final String KEY_BEAN = "infoBean";

    protected BocCommonTools.IBocCallBack callBack;
    /**
     * handler
     */
    private Handler handler;

    /**
     * 是否可以关闭对话框
     */
    protected boolean isCanCloseLoadingDialog = true;
    /**
     * Fragment动画时间
     */
    public static final long BUFFER_TIME = 300L;

    private Handler getHandler() {
        if (handler == null)
            handler = new Handler();
        return handler;
    }

    @Override
    protected boolean getTitleBarRed() {
        return false;
    }

    @Override
    protected boolean isDisplayRightIcon() {
        return false;
    }


    @Override
    public void setPresenter(BasePresenter presenter) {

    }

    public void requestFocus(View view) {
        view.setFocusable(true);
        view.setFocusableInTouchMode(true);
        view.requestFocus();
    }

    public void setVisibility(int visibility, View... views) {
        if (views == null || views.length == 0)
            return;
        for (View view : views)
            view.setVisibility(visibility);
    }

    public boolean isCurrentFragment() {
        return isVisible();
    }

    /**
     * 重新排序Fragment顺序
     */
    protected final void reorderIndices() {
        getHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                FragmentTransactionBugFixHack.reorderIndices(mActivity.getSupportFragmentManager());
                afterReorderIndices();
            }
        }, BUFFER_TIME);
    }

    /**
     * 重新排序Fragment后回调
     */
    protected void afterReorderIndices() {
        throw new RuntimeException("请在子类实现该方法");
    }

    @Override
    public void closeProgressDialog() {
        //如果请求后,为false说明还有一个请求,设置为true
        if (!isCanCloseLoadingDialog) {
            isCanCloseLoadingDialog = true;
            return;
        }

        //第二次请求,关闭对话框
        if (isCanCloseLoadingDialog) {
            super.closeProgressDialog();
        }
    }

    public void setCallBack(BocCommonTools.IBocCallBack callBack) {
        this.callBack = callBack;
    }

    /**
     * 保存至Sp
     *
     * @param key
     * @param value
     */
    protected void put(String key, Object value) {
        SharedPreferences.Editor editor = mActivity.getSharedPreferences(GlobalParams.CONFIG_NAME, Context.MODE_PRIVATE).edit();
        if (value instanceof Integer)
            editor.putInt(key, Integer.parseInt(value.toString())).commit();

        if (value instanceof Boolean)
            editor.putBoolean(key, Boolean.parseBoolean(value.toString())).commit();

        if (value instanceof String)
            editor.putString(key, value.toString()).commit();

        if (value instanceof Float)
            editor.putFloat(key, Float.parseFloat(value.toString())).commit();
    }

    public SharedPreferences getSharedPreferences() {
        return mActivity.getSharedPreferences(GlobalParams.CONFIG_NAME, Context.MODE_PRIVATE);
    }

    protected void popFragments(Class<? extends BussFragment>... classList) {
        if (classList == null || classList.length == 0)
            return;

        for (Class clazz : classList)
            findFragment(clazz).pop();
    }

    protected void showWait() {
        Toast.makeText(mContext, "(*^__^*) 测试同事们耐心等待下,这个功能正在开发中,过几天开发好了会加上。", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected T initPresenter() {
        return null;
    }

    protected String getCurrency(String currencyCode) {
        return PublicCodeUtils.getCurrency(mContext, currencyCode);
    }

    public void destroyBgTask() {
        if (getPresenter() != null && getPresenter() instanceof RxPresenter) {
            ((RxPresenter) getPresenter()).destroyBgTask();
        }
    }

    @Override
    public void onDestroy() {
        destroyBgTask();//super里会把普通任务取消，这里取消一下后台任务就行。
        super.onDestroy();
    }
}
