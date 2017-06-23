package com.chinamworld.llbt.userwidget.dialogview;

import android.app.Dialog;
import android.content.Context;
import android.view.View;

import com.chinamworld.llbtwidget.R;


/**
 * 自定义弹出框基类
 * Created by Administrator on 2016/8/24.
 */
public abstract class BaseDialog extends Dialog {

    protected Context mContext;
    protected View curView = null;
    public BaseDialog(Context context){
        super(context, R.style.dialog_normal);
        mContext = context;

        setCancelable(false);
        curView = initView();
        if(curView != null)
          getWindow().setContentView(curView);
    }


    /**
     * 构造弹出框布局
     */
    protected abstract View initView();

}
