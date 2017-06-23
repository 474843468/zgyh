package com.chinamworld.llbt.userwidget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

/**
 * 不带滚动条的ScrollView
 * Created by Administrator on 2016/11/5.
 */
public class LLBTScrollView extends ScrollView{

    public LLBTScrollView(Context context){
        super(context);
        initView(context);
    }
    public LLBTScrollView(Context context, AttributeSet attrs){
        super(context,attrs);
        initView(context);
    }

    public void initView(Context context){
        this.setVerticalScrollBarEnabled(false);
        this.setHorizontalScrollBarEnabled(false);
    }

}
