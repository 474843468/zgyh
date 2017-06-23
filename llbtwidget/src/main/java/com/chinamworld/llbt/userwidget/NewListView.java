package com.chinamworld.llbt.userwidget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * 不带滚动条的ListView
 * Created by Administrator on 2016/11/5.
 */
public class NewListView extends ListView {

    public NewListView(Context context){
        super(context);
        initView(context);
    }

    public NewListView(Context context, AttributeSet attr){
        super(context,attr);
        initView(context);
    }

    private void initView(Context context){
        this.setVerticalScrollBarEnabled(false);
        this.setFastScrollEnabled(false);
    }
}
