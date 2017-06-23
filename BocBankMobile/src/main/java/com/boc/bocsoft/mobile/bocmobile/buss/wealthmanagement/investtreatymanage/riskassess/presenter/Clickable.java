package com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.investtreatymanage.riskassess.presenter;

import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

/**
 * Created by guokai on 2016/10/20.
 */
public class Clickable extends ClickableSpan implements View.OnClickListener {

    private final View.OnClickListener mListener;

    public Clickable(View.OnClickListener l){
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
