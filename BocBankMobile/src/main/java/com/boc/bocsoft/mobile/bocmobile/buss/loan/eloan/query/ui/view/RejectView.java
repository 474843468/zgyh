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
 * 申请失败页
 */
public class RejectView extends LinearLayout {
    /**rootView*/
    protected View rootView;
    /**查看附件网店组件*/
    protected TextView nearTv;
    /**上下文*/
    private Context mContext;

    public RejectView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initView();
    }
    public RejectView(Context context) {
        this(context, null, 0);
    }

    public RejectView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    private void initView() {
        rootView = LayoutInflater.from(mContext)
                .inflate(R.layout.boc_eloanapply_rejectview, this);
        nearTv = (TextView) rootView.findViewById(R.id.nearTv);
        nearTv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO 跳转逻辑
            }
        });
    }

}
