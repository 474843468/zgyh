package com.boc.bocsoft.mobile.framework.ui;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import com.boc.bocsoft.mobile.framework.R;
import com.boc.bocsoft.mobile.framework.widget.TitleBarView;


/**
 * 带有标题Activity类
 * 需要标题时，继承该Activity类
 * Created by lxw on 2016/5/28.
 */
public class TitleBarActivity extends BaseActivity implements BaseViewInterface{

    public final static int GOBACK_IN = R.anim.boc_infromleft;
    public final static int GOBACK_OUT = R.anim.boc_outtoright;
    public final static int JUMP_IN = R.anim.boc_infromright;
    public final static int JUMP_OUT = R.anim.boc_outtoleft;

    protected TitleBarView mTitleBarView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public void beforeInitView() {
        this.setContentView(R.layout.boc_activity_base);
        initTitleBarView();

        if (getContentView() != null) {
            ViewGroup vg = mViewFinder.find(R.id.baseContentView);
            vg.addView(getContentView());

        } else if (getLayoutId() != 0) {
            View view = View.inflate(this, getLayoutId(), null);
            ViewGroup vg = mViewFinder.find(R.id.baseContentView);
            vg.addView(view);

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
    protected int getLayoutId() {
        return 0;
    }

    protected View getContentView() {
        return null;
    }

    /**
     * 初始化标题栏
     */
    private void initTitleBarView() {
        ViewGroup titleBar =  mViewFinder.find(R.id.baseTitleView);
        if(isHaveTitleBarView()){
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
     * @return
     */
    protected boolean isHaveTitleBarView() {
        return true;
    }

    /**
     * 默认标题栏view
     * 子类可以重写此方法，改变标题栏样式
     * @return
     */
    protected View getTitleBarView(){
        mTitleBarView=new TitleBarView(mContext)
                .setStyle(getTitleBarRed()?R.style.titlebar_common_red :R.style.titlebar_common_white)
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
                .setRightImgBtnVisible(isDisplayRightIcon());
        return mTitleBarView;
        //View view = new TitleBarView().build(mContext, getTitleBarRed()
        //        ?R.layout.boc_view_titlebar_red :R.layout.boc_view_titlebar_white)
        //        //左侧图标点击事件
        //        .bindOnClick(R.id.leftIconIv, new View.OnClickListener() {
        //            @Override
        //            public void onClick(View v) {
        //                titleLeftIconClick();
        //            }
        //        })
        //        //右侧图标点击事件
        //        .bindOnClick(R.id.rightIconIv, new View.OnClickListener() {
        //            @Override
        //            public void onClick(View v) {
        //                titleRightIconClick();
        //            }
        //        })
        //        .setTextView(R.id.titleValueTv, getTitleValue())
        //        .setRightImgBtnVisible(R.id.rightIconIv,isDisplayRightIcon())
        //        .create();
        //return view;
    }

    protected String getTitleValue(){
        return "";
    }

    /**
     * 是否显示右侧标题按钮
     * @return
     */
    protected Boolean isDisplayRightIcon(){
        return true;
    }
    /**
     * 红色主题titleBar：0 ；
     * 白色主题titleBar：1 ；
     * @return
     */
    protected Boolean getTitleBarRed(){
        return true;
    }

    /**
     * 标题栏左侧图标点击事件
     */
    protected void titleLeftIconClick(){
        ActivityManager.getAppManager().finishActivity();
        overridePendingTransition(GOBACK_IN, GOBACK_OUT);
    }

    /**
     * 标题栏右侧图标点击事件
     * 重写此方法，处理右侧图标点击事件
     */
    protected void titleRightIconClick(){

    }

}
