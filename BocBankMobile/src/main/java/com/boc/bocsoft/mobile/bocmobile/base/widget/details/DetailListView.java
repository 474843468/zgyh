package com.boc.bocsoft.mobile.bocmobile.base.widget.details;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bocmobile.R;

/**
 * Created by huixiaobo on 2016/6/8.
 * 三个TextView item 组件
 */
public class DetailListView extends LinearLayout {

    protected View rootView;
    protected TextView tvLeft;
    protected TextView tvMiddle;
    protected TextView tvRight;
    protected LinearLayout layoutBody;
    protected View vBody;
    private Context mContext;

    public DetailListView(Context context) {
        super(context);
        mContext = context;
        init();
    }

    public DetailListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    public DetailListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init();
    }

    private void init() {
        rootView = LayoutInflater.from(mContext).inflate(R.layout.boc_view_detail_list, this);
        tvLeft = (TextView) rootView.findViewById(R.id.tv_left);
        tvMiddle = (TextView) rootView.findViewById(R.id.tv_middle);
        tvRight = (TextView) rootView.findViewById(R.id.tv_right);
        layoutBody = (LinearLayout) rootView.findViewById(R.id.layout_body);
        vBody = (View) rootView.findViewById(R.id.v_body);
    }

    /**
     * 组件赋值
     *
     * @param left   左侧
     * @param middle 中间
     * @param right  右侧
     */
    public void setDataValue(String left, String middle, String right) {
        tvLeft.setText(left);
        tvMiddle.setText(middle);
        tvRight.setText(right);
    }
}
