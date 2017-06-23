package com.boc.bocsoft.mobile.bocmobile.base.widget.SpringPressageView;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;

;

/**
 * 中银理财-持仓详情-预期年华收益率
 * Created by zn on 2016/11/3.
 */
public class SpringRateDetail extends LinearLayout {
    private Context mContext;
    protected View rootView;
    private SpringProgressView spring_progress_view;
    private TextView text_days;
    private TextView text_pecen;
    private  String liftText;
    private String rightText;

    public SpringRateDetail(Context context) {
        super(context);
        this.mContext = context;
        init();
    }

    public SpringRateDetail(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        init();
    }

    public SpringRateDetail(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        init();
    }

    private void init() {
        rootView = LayoutInflater.from(mContext).inflate(R.layout.boc_financial_type_progress_query, this);
        text_days = (TextView)rootView.findViewById(R.id.text_days);
        text_pecen = (TextView)rootView.findViewById(R.id.text_percent);
        spring_progress_view = (SpringProgressView) rootView.findViewById(R.id.spring_progress_view);
    }
    private void setTextContent(String days,String pecent){
        text_days.setText(days);
        text_pecen.setText(pecent);
    }
    /**
     *
     * @param Max 最大值
     * @param Min 当前值
     * @param progress_incom 百分比
     * @param days 左边标题
     */
    public void addSpringProgressView(float Max, float Min,float progress_incom, String days) {
//        setTextContent(days,Min+"%");
        setTextContent(days, MoneyUtils.transMoneyFormat(String.valueOf(Min), "001")+"%");

        spring_progress_view.setContext(Max,Min,progress_incom);
    }
}