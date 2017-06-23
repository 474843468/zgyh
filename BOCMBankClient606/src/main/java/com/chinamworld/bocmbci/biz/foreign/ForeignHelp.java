package com.chinamworld.bocmbci.biz.foreign;

import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;

/**
 * 3.0 外汇
 * @author luqp 2016/10/14
 */
public class ForeignHelp extends ForeignBaseActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView(); //设置布局
    }

    /** 设置布局*/
    public void initView() {
        setContentView(R.layout.foreign_help);
        getBackGroundLayout().setTitleText(R.string.help); // 设置标题
        getBackGroundLayout().setRightButtonVisibility(View.GONE); //设置右按钮显示
        getBackGroundLayout().setShareButtonVisibility(View.GONE); //设置分享图标隐藏

        TextView helpTv = (TextView) findViewById(R.id.foreign_help_tv);
        helpTv.setText(Html.fromHtml( this.getResources().getString(R.string.foregin_help_message)));
    }
}
