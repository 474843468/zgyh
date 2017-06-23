package com.boc.bocsoft.mobile.framework.ui;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import com.boc.bocsoft.mobile.framework.utils.ViewFinder;

/**
 * 基础Activity类
 * 所有的Activity继承该类
 * Created by lxw on 2016/5/28.
 */
public abstract class BaseActivity extends  FragmentActivity implements BaseViewInterface{

    // activity context.子类不要定义该对象
    protected Context mContext;
    // 画面元素查找类
    protected ViewFinder mViewFinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 设置状态栏
        //setStatusBarTransparent();

        mContext = this;
        ActivityManager.getAppManager().addActivity(this);
        mViewFinder = new ViewFinder(this);

        beforeInitView();
        initView();
        initData();
        setListener();

    }

    @Override protected void onDestroy() {
        super.onDestroy();
        ActivityManager.getAppManager().removeActivity(this);
    }

    protected Boolean getTitleBarRed(){
        return true;
    }

    /**
     * 设置全透明状态栏
     */
//    private void setStatusBarTransparent() {
//        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.LOLLIPOP){
//            Window window = getWindow();
//            //状态栏沉浸到应用中
//            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            //状态栏保留在应用上部
//            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//
//            window.setStatusBarColor(getTitleBarRed()?0xffca2b4f:0xfff9f9f9);
//        }
//    }



}
