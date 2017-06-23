package com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.portfoliopurchase.ui;

import android.view.LayoutInflater;
import android.view.View;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;

/**
 * Created by wangtong on 2016/9/16.
 */
public class PortfolioHelpFragment extends BussFragment {

    private View rootView;

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        rootView = mInflater.inflate(R.layout.boc_fragment_portfolio_purchase, null);
        return rootView;
    }

    @Override
    protected boolean isDisplayRightIcon() {
        return false;
    }

    @Override
    protected void titleLeftIconClick() {
        pop();
    }

    @Override
    protected boolean getTitleBarRed() {
        return false;
    }

    @Override
    protected String getTitleValue() {
        return "帮助";
    }
}
