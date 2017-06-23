package com.boc.bocsoft.mobile.bocmobile.base.container;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

/**
 * 作者：XieDu
 * 创建时间：2016/12/16 16:16
 * 描述：
 */
public class Container extends FrameLayout implements IContainer {

    protected LayoutParams mLayoutParams;
    protected Activity mActivity;
    protected Fragment mFragment;

    public Container(Context context) {
        this(context, null);
    }

    public Container(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Container(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        createView();
    }

    private void createView() {
        mLayoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        View contentView = createContentView();
        if (contentView != null) {
            setContentView(contentView);
        }
    }

    /**
     * 创建contentview
     */
    protected View createContentView() {
        return null;
    }

    public void setContentView(int layoutResID) {
        View view = inflate(getContext(), layoutResID, null);
        addView(view, mLayoutParams);
    }

    public void setContentView(View view) {
        addView(view, mLayoutParams);
    }

    public void attach(Activity activity) {
        mActivity = activity;
        onAttach();
    }

    /**
     * 创建容器之后要绑定创建者
     * @param fragment 创建容器对象和viewpager的fragment
     */
    public void attach(Fragment fragment) {
        mFragment = fragment;
        if (mFragment != null) {
            mActivity = mFragment.getActivity();
        }
        onAttach();
    }

    private void onAttach() {
        initView();
        initData();
        setListener();
    }

    /**
     * 初始化布局中各个view的地方
     */
    protected void initView() {

    }

    /**
     * 初始化数据，因为是在attach里调用的，此时界面尚未切换到该容器。
     * 因此如果要在viewpager场景实现懒加载，应该调用setUserVisibleHint
     */
    protected void initData() {

    }

    /**
     * 设置监听器
     */
    protected void setListener() {

    }

    /**
     * 容器做销毁处理的地方，需要创建者去控制调用
     */
    @Override
    public void onDestroy() {

    }

    @Override
    public View getView() {
        return this;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {

    }
}
