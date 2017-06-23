package com.boc.bocsoft.mobile.bocmobile.base.widget.AccountCheckBox;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bocmobile.R;

/**
 * @author wangyang
 *         2016/10/17 10:24
 *         选择框
 */
public class AccountCheckBox extends LinearLayout {

    private CheckBox cbChoice;

    private TextView tvContent, tvCurrency, tvAmount;

    public AccountCheckBox(Context context) {
        super(context);
        init();
    }

    public AccountCheckBox(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AccountCheckBox(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public AccountCheckBox(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        View view = View.inflate(getContext(), R.layout.boc_view_check_box, this);
        cbChoice = (CheckBox) view.findViewById(R.id.cb_choice);
        tvContent = (TextView) view.findViewById(R.id.tv_content);
        tvCurrency = (TextView) view.findViewById(R.id.tv_currency);
        tvAmount = (TextView) view.findViewById(R.id.tv_amount);
    }

    public void setData(String... datas) {
        if (datas == null || datas.length == 0)
            return;

        if (datas.length > 0)
            tvContent.setText(datas[0]);

        if (datas.length > 1)
            tvCurrency.setText(datas[1]);

        if (datas.length > 2)
            tvAmount.setText(datas[2]);
    }

    public boolean isChecked() {
        return cbChoice.isChecked();
    }

    public void checked(boolean isChecked){
        cbChoice.setChecked(isChecked);
    }

    public void setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener listener) {
        cbChoice.setOnCheckedChangeListener(listener);
    }
}
