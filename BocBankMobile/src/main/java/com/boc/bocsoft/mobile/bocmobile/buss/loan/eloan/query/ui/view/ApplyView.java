package com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.query.ui.view;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.waterwaveballview.WaterWaveBallView;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.query.model.EloanApplyModel;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.query.model.EloanCreditViewModel;

/**
 * Created by huixiaobo on 2016/6/21.
 * 申请页面
 */
public class ApplyView extends LinearLayout {
    /**view*/
    protected View rootView;
    /**抬头显示文字组件*/
    protected TextView titleValueTv;
    /**显示额度比例公共组件*/
    protected WaterWaveBallView waveBllView;
    /**大约额度显示组件*/
    protected TextView explainTv;
    /**具体额度金额组件*/
    protected TextView sumTv;
    /**申请提交显示组件*/
    protected TextView activatedTv;
    /**提示文字*/
    protected TextView reminderHead;
    /**提示文字*/
    protected TextView reminderTv;
    /**上下文*/
    private Context mContet;


    public ApplyView(Context context) {
        this(context, null, 0);
    }

    public ApplyView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ApplyView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContet = context;
        initView();
        initData();
    }

    private void initView() {
        rootView = LayoutInflater.from(mContet).inflate(R.layout.boc_eapply_fragment, this);
        titleValueTv = (TextView) rootView.findViewById(R.id.titleValueTv);
        waveBllView = (WaterWaveBallView) rootView.findViewById(R.id.waveBllView);
        explainTv = (TextView) rootView.findViewById(R.id.explainTv);
        sumTv = (TextView) rootView.findViewById(R.id.sumTv);
        activatedTv = (TextView) rootView.findViewById(R.id.activatedTv);
        reminderHead = (TextView) rootView.findViewById(R.id.reminderHead);
        reminderTv = (TextView) rootView.findViewById(R.id.reminderTv);
        titleValueTv.setVisibility(View.VISIBLE);
        waveBllView.setVisibility(View.VISIBLE);
        waveBllView.startWave();

    }


    private void initData() {
        explainTv.setText(R.string.boc_eapply_explainTv);
        activatedTv.setText(R.string.boc_eapply_submitEloanTv);
        //activatedTv.setBackgroundResource(R.drawable.boc_bg_round_blue_text);
        activatedTv.setBackgroundResource(R.drawable.boc_eloan_but);
        //activatedTv.setAlpha(0.7f);
       // reminderHead.setText(R.string.boc_eapply_reminderHead);
        reminderTv.setText(R.string.boc_eapply_reminderTv);
    }

    /**
     * 传递申请页面的显示modle;
     */
    public void putApplyData(EloanApplyModel ecreditResult) {
        if (ecreditResult == null) {
            return;
        }
        sumTv.setText(MoneyUtils.getLoanAmountShownRMB(ecreditResult.getQuote()));
        waveBllView.setFinallyWaterHeight(Float.parseFloat(ecreditResult.getQuote())/300000.0f);
    }

}
