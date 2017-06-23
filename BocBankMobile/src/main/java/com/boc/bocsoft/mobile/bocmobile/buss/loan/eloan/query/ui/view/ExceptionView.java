package com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.query.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bocmobile.R;

/**
 * Created by huixiaobo on 2016/6/21.
 * 异常页面
 */
public class ExceptionView extends LinearLayout {
    /**view*/
    protected View rootView;
    /**显示内容组件*/
    protected TextView eloanExcptionTv;
    /**上下文*/
    private Context mContext;

    public ExceptionView(Context context) {
        this(context, null, 0);
    }

    public ExceptionView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ExceptionView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initView();
    }

    private void initView() {
        rootView = LayoutInflater.from(mContext).inflate(R.layout.boc_eloan_excptionview, this);
        eloanExcptionTv = (TextView) rootView.findViewById(R.id.eloanExcptionTv);
    }

    /**
     *获取页面传递的值
     *
     */
    public void putData(String exceptionMsg) {
        if (exceptionMsg != null) {
            eloanExcptionTv.setText(exceptionMsg);
        }

    }

}
