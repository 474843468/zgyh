package com.chinamworld.bocmbci.base.activity;

import android.os.Bundle;
import android.view.LayoutInflater;

import com.chinamworld.llbt.userwidget.NewBackGround.NewBackGroundLayout;

/**
 * 3.0新风格Activity基类
 * Created by Administrator on 2016/10/13.
 */
public abstract class NewStyleBaseActivity extends BaseActivity {

    private NewBackGroundLayout backGroundLayout;
    /** 获得背景控件*/
    protected NewBackGroundLayout getBackGroundLayout(){
        return backGroundLayout;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        backGroundLayout = new NewBackGroundLayout(this);
        super.setContentView(backGroundLayout);
    }

    /** 设置布局  */
    public void setTitle(String title){
        backGroundLayout.setTitleText(title);
    }


    @Override
    public void setContentView(int layoutResID) {
        backGroundLayout.addView(LayoutInflater.from(this).inflate(layoutResID, backGroundLayout, false));
    }

    public void setContentView(int layoutResID,boolean isNeedBackground) {
        if(isNeedBackground)
            backGroundLayout.addView(LayoutInflater.from(this).inflate(layoutResID, backGroundLayout, false));
        else
            super.setContentView(layoutResID);
    }
}
