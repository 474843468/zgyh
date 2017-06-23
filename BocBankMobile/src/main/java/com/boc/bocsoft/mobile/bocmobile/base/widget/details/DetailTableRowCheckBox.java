package com.boc.bocsoft.mobile.bocmobile.base.widget.details;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.boc.bocsoft.mobile.bocmobile.R;

/**
 * Created by XieDu on 2016/7/27.
 */
public class DetailTableRowCheckBox extends RelativeLayout {
    protected View rootView;
    protected TextView tvTitle;
    protected CheckBox btnCheckbox;

    public DetailTableRowCheckBox(Context context) {
        this(context, null);
    }

    public DetailTableRowCheckBox(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DetailTableRowCheckBox(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        View rootView = inflate(getContext(), R.layout.boc_view_detail_table_row_checkbox, this);
        tvTitle = (TextView) rootView.findViewById(R.id.tv_title);
        btnCheckbox = (CheckBox) rootView.findViewById(R.id.btn_checkbox);
    }

    public void setTitle(String title) {
        tvTitle.setText(title);
    }

    public boolean isChecked() {
        return btnCheckbox.isChecked();
    }

    public void setChecked(boolean checked) {
        btnCheckbox.setChecked(checked);
    }

    public void setOnCheckedChangeListener(
            CompoundButton.OnCheckedChangeListener onCheckedChangeListener) {
        btnCheckbox.setOnCheckedChangeListener(onCheckedChangeListener);
    }

    public void setTitleBold(boolean isBold) {
        tvTitle.getPaint().setFakeBoldText(isBold);
    }
}
