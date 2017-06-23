package com.boc.bocsoft.mobile.framework.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.boc.bocsoft.mobile.framework.R;
import com.boc.bocsoft.mobile.framework.utils.ViewFinder;
import com.boc.bocsoft.mobile.framework.widget.TitleBarView;

/**
 * 基础Fragment
 * 所有的Fragment继承该类
 * Created by Administrator on 2016/5/17.
 */
public abstract class BaseFragment extends Fragment implements BaseViewInterface {

    protected Context mContext;

    protected View mFragmentView;

    protected ViewFinder mViewFinder;

    protected FrameLayout mContentView;

    protected TitleBarView mTitleBarView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        if (mFragmentView == null) {
            mFragmentView = inflater.inflate(R.layout.boc_fragment_base, null);

            mViewFinder = new ViewFinder(mFragmentView);
            initTitleBarView();
            mContentView = mViewFinder.find(R.id.baseContentView);
            mContentView.removeAllViews();
            View contentView = onCreateView(inflater);
            if (contentView != null) {
                mContentView.addView(contentView);
            }
            beforeInitView();
            initView();
            initData();
            //initTitleBarView();
            setListener();
        }

        return mFragmentView;
    }

    /**
     * 具体fragment实现此方法，
     */
    protected abstract View onCreateView(LayoutInflater mInflater);

    /**
     * 初始化标题栏
     */
    private void initTitleBarView() {
        ViewGroup titleBar = mViewFinder.find(R.id.baseTitleView);
        if (isHaveTitleBarView()) {
            titleBar.setVisibility(View.VISIBLE);
            titleBar.removeAllViews();
            titleBar.addView(getTitleBarView());
        } else {
            titleBar.setVisibility(View.GONE);
        }
    }

    /**
     * 是否显示标题栏，默认显示
     * 子类可以重写此方法，控制是否显示标题栏
     */
    protected boolean isHaveTitleBarView() {
        return true;
    }

    /**
     * 默认标题栏view
     * 子类可以重写此方法，改变标题栏样式
     */
    protected View getTitleBarView() {
        //setStatusBarTransparent();
        mTitleBarView = new TitleBarView(mContext)
                .setStyle(getTitleBarRed() ? R.style.titlebar_common_red : R.style.titlebar_common_white)
                .setLeftButtonOnClickLinster(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        titleLeftIconClick();
                    }
                })
                .setRightButtonOnClickLinster(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        titleRightIconClick();
                    }
                })
                .setTitle(getTitleValue())
                .setLeftBtnVisible(isDisplayLeftIcon())
                .setRightImgBtnVisible(isDisplayRightIcon());
        return mTitleBarView;
    }

    protected String getTitleValue() {
        return "";
    }

    /**
     * 是否显示右侧标题按钮
     */
    protected boolean isDisplayRightIcon() {
        return true;
    }

    /**
     * 是否显示左侧标题按钮
     */
    protected boolean isDisplayLeftIcon() {
        return true;
    }

    /**
     * 红色主题titleBar：true ；
     * 白色主题titleBar：false ；
     */
    protected boolean getTitleBarRed() {
        return true;
    }

    /**
     * 标题栏左侧图标点击事件
     */
    protected void titleLeftIconClick() {

    }

    /**
     * 标题栏右侧图标点击事件
     * 重写此方法，处理右侧图标点击事件
     */
    protected void titleRightIconClick() {

    }

    /**
     * 设置全透明状态栏
     */
    //    private void setStatusBarTransparent() {
    //        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.LOLLIPOP){
    //            Window window = this.getActivity().getWindow();
    //            //状态栏沉浸到应用中
    //            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
    //            //状态栏保留在应用上部
    //            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
    //            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
    //
    //            window.setStatusBarColor(getTitleBarRed()?0xffca2b4f:0xfff9f9f9);
    //
    //        }
    //    }
    private Toast toast;

    public void showToast(String msg) {
        if (toast != null) {
            toast.cancel();
        }
        toast = Toast.makeText(mContext, msg, Toast.LENGTH_SHORT);
        toast.show();
    }

    public void updateTitleValue(String value){
        if(mTitleBarView != null && value != null){
            mTitleBarView.setTitle(value);
        }
    }
}
