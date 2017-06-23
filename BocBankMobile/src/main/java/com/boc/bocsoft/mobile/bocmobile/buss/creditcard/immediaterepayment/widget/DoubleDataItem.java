package com.boc.bocsoft.mobile.bocmobile.buss.creditcard.immediaterepayment.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bocmobile.R;

/**
 */
public class DoubleDataItem  extends LinearLayout{

    private Context context;
    private TextView tvLeft,tvRight;

    public DoubleDataItem(Context context) {
        super(context);
        this.context=context;
        initView();
    }

    public DoubleDataItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context=context;
        initView();
    }

    public DoubleDataItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
        this.context=context;
    }

    private void initView() {
        LinearLayout rootView = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.dialog_doubledata_item, this);
        tvLeft=(TextView)rootView.findViewById(R.id.tv_content_left);
        tvRight=(TextView)rootView.findViewById(R.id.tv_content_right);
    }

    public void setContent(String leftMsg,String rightMsg){
        tvLeft.setText(leftMsg);
        tvRight.setText(rightMsg);
    }
}
