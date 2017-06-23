package com.boc.bocsoft.mobile.bocmobile.buss.creditcard.automaticrepayment.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bocmobile.R;

/**
 * Name: liukai
 * Time：2016/11/25 16:17.
 * Created by lk7066 on 2016/11/25.
 * It's used to 已开通页面，此处一个页面两个按钮颜色不同，所以单独写了一个Button
 */

public class AutoCrcdPaymentButton extends LinearLayout {

    private View layout_root;
    private Context mContext;
    private TextView txt;

    public AutoCrcdPaymentButton(Context context) {
        super(context);
        this.mContext = context;
        init();
    }

    public AutoCrcdPaymentButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        init();
    }

    private void init() {

        layout_root = LayoutInflater.from(mContext).inflate(R.layout.boc_button_autocrcdpayment_opened, this);
        txt = (TextView) layout_root.findViewById(R.id.txt_autopay);

    }

    public void updateText(String text) {
        txt.setText(text);
    }

    public void updateTextColor(int color) {
        txt.setTextColor(getResources().getColor(color));
    }

    /**
     * 设置背景颜色
     */
    public void updateTextStyle() {
        txt.setTextColor(getResources().getColor(R.color.boc_text_color_red));
        txt.setBackgroundColor(getResources().getColor(R.color.boc_main_btn_bg_color));
    }


}
