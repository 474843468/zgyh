package com.boc.bocsoft.mobile.bocmobile.base.widget.timeaxis;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bocmobile.R;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

/**
 * Created by taoyongzhen on 2016/12/3.
 */

public class TimeAxis extends LinearLayout {
    private Context mContext;
    private View rootView;
    private TextView titleView;
    private TextView valueView;
    private TextView despView;
    private TextView topAxis;
    private TextView bottomAxis;
    private TextView circle;

    public TimeAxis(Context context) {
        super(context);
        mContext = context;
        init();
    }

    public TimeAxis(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    private void init() {
        LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(LAYOUT_INFLATER_SERVICE);
        rootView =inflater.inflate(R.layout.boc_time_axis, this,true);
        titleView = (TextView) rootView.findViewById(R.id.tv_title);
        valueView = (TextView) rootView.findViewById(R.id.tv_amount);
        despView = (TextView) rootView.findViewById(R.id.tv_description);
        topAxis = (TextView) rootView.findViewById(R.id.top_line);
        bottomAxis = (TextView) rootView.findViewById(R.id.bottom_line);
        circle = (TextView) rootView.findViewById(R.id.circle);
    }

    public void setTitleView(String content) {
        titleView.setText(content);
    }

    public void setValueView(String content) {
        valueView.setText(content);
    }

    public void setDespView(String content){
        despView.setText(content);
    }

    public void setTitleTextColor(int color) {
        titleView.setTextColor(mContext.getResources().getColor(color));
    }

    public void setValueTextColor(int color) {
        valueView.setTextColor(mContext.getResources().getColor(color));
    }

    public void setDespTextColor(int color){
        despView.setTextColor(mContext.getResources().getColor(color));
    }

    public void setTopAxisColor(int color) {
        topAxis.setBackgroundColor(mContext.getResources().getColor(color));
    }

    public void setBottomAxisColor(int color) {
        bottomAxis.setBackgroundColor(mContext.getResources().getColor(color));
    }

    public void setShowCircle(boolean isShow) {
        if (isShow){
            circle.setVisibility(VISIBLE);
        }else{
            circle.setVisibility(GONE);
        }
    }
}
