package com.boc.bocsoft.mobile.bocmobile.buss.fund.riskassessment.view;

import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

/**
 * Created by taoyongzhen on 2016/11/28.
 */

public class RiskEvaluationClickable extends ClickableSpan implements View.OnClickListener  {
    private final View.OnClickListener mListener;

    public RiskEvaluationClickable(View.OnClickListener l){
        this.mListener = l ;
    }

    @Override
    public void onClick(View widget) {
        mListener.onClick(widget);
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        super.updateDrawState(ds);
        ds.setUnderlineText(false);
    }
}
