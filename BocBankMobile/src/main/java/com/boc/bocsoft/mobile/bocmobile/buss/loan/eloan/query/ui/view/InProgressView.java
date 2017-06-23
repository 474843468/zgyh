package com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.query.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.widget.waterwaveballview.WaterWaveBallView;

/**
 * Created by huixiaobo on 2016/6/21.
 * 申请中页面
 */
public class InProgressView extends LinearLayout {
    /**view*/
    protected View rootView;
    /**额度显示图*/
    protected WaterWaveBallView waveBllView;
    /**审批中显示组件*/
    protected TextView sumTv;
    /**提示语*/
    protected TextView reachdateTv;

    private Context mContext;

    public InProgressView(Context context) {
        this(context, null, 0);
    }

    public InProgressView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public InProgressView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initView();
        initData();
    }

    private void initView() {
        rootView = LayoutInflater.from(mContext).inflate(R.layout.boc_eloandraw_fragment, this);
        waveBllView = (WaterWaveBallView) rootView.findViewById(R.id.waveBllView);
        sumTv = (TextView) rootView.findViewById(R.id.sumTv);
        reachdateTv = (TextView) rootView.findViewById(R.id.reachdateTv);
        waveBllView.startWave();
    }

    private void initData() {
        sumTv.setText(getResources().getString(R.string.boc_eapply_inprogresstTv));
        waveBllView.setFinallyWaterHeight(0.3f);
        reachdateTv.setText(getResources().getString(R.string.boc_eapply_nextreachdateTv));
    }

}
