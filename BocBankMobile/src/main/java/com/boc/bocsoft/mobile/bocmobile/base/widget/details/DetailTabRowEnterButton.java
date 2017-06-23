package com.boc.bocsoft.mobile.bocmobile.base.widget.details;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bocmobile.R;

/**
 * Created by niuguobin on 2016/6/22.
 */
public class DetailTabRowEnterButton extends LinearLayout {

    protected TextView tvName;
    protected TextView tvValue;
    protected TextView btnValue;
    private View root_view;
    private Context mContext;

    public DetailTabRowEnterButton(Context context) {
        this(context, null, 0);
    }

    public DetailTabRowEnterButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DetailTabRowEnterButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        initView();
    }

    private void initView() {
        root_view = LayoutInflater.from(mContext).inflate(R.layout.boc_view_detail_table_row_enter_btn, this);
        tvName = (TextView) root_view.findViewById(R.id.tv_name);
        tvValue = (TextView) root_view.findViewById(R.id.tv_value);
        btnValue = (TextView) root_view.findViewById(R.id.btn_value);
    }

    /**
     * 添加一行
     * @param name
     * @param value
     * @param btnText
     */
    public void addRow(String name, String value,String btnText) {
        tvName.setText(name);
        tvValue.setText(value);
        btnValue.setText(btnText);
    }
}
