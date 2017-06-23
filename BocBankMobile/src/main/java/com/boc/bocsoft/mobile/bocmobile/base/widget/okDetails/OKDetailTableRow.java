package com.boc.bocsoft.mobile.bocmobile.base.widget.okDetails;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bocmobile.R;

/**
 * 确认界面内容返显组件
 * Created by niuguobin on 2016/6/15.
 */
public class OKDetailTableRow extends LinearLayout {
    protected TextView tvName;
    protected TextView tvValue;
    protected LinearLayout layoutBody;

    private Context mContext;
    LinearLayout rootView;

    public OKDetailTableRow(Context context) {
        this(context, null, 0);
    }

    public OKDetailTableRow(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public OKDetailTableRow(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        initView();
    }

    private void initView() {
        rootView = (LinearLayout) LayoutInflater.from(mContext).inflate(R.layout.boc_view_ok_detail_table_row, this);
        tvName = (TextView) rootView.findViewById(R.id.tv_name);
        tvValue = (TextView) rootView.findViewById(R.id.tv_value);
        layoutBody = (LinearLayout) rootView.findViewById(R.id.layout_body);
    }

    /**
     * 改变内容
     *
     * @param name：左侧标题
     * @param value：右侧数据
     */
    public void updateData(String name, String value) {
        tvName.setText(name);
        tvValue.setText(value);
    }
}