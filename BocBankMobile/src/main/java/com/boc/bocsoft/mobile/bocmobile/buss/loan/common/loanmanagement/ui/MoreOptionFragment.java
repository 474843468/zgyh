package com.boc.bocsoft.mobile.bocmobile.buss.loan.common.loanmanagement.ui;

import android.view.LayoutInflater;
import android.view.View;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.widget.edittext.EditChoiceWidget;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.common.facilityinquiry.ui.FacilityInquiryFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.common.otherloanappliedqry.ui.OtherLoanAppliedQryDetailFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.common.otherloanappliedqry.ui.OtherLoanAppliedQryInputFragment;

/**
 * Created by huixiaobo on 2016/9/4.
 * 更多
 */
public class MoreOptionFragment extends BussFragment implements View.OnClickListener{

    protected View rootView;
    protected EditChoiceWidget loanQuote;
    protected EditChoiceWidget loanpess;

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        rootView = mInflater.inflate(R.layout.boc_loan_moreoption, null);
        return rootView;
    }

    @Override
    public void initView() {
        super.initView();
        loanQuote = (EditChoiceWidget) rootView.findViewById(R.id.loanQuote);
        loanpess = (EditChoiceWidget) rootView.findViewById(R.id.loanpess);
        loanpess.setNameWidth();
        loanQuote.setBottomLineVisibility(true);
        loanpess.setBottomLineVisibility(true);
        loanQuote.setOnClickListener(this);
        loanpess.setOnClickListener(this);
    }

    @Override
    protected boolean getTitleBarRed() {
        return false;
    }

    @Override
    protected String getTitleValue() {
        return getString(R.string.boc_loan_mored);
    }

    @Override
    protected boolean isDisplayRightIcon() {
        return false;
    }

    @Override
    public void initData() {
        super.initData();
    }

    @Override
    public void onClick(View v) {
        int view = v.getId();
        if (view == R.id.loanQuote) {
            start(new FacilityInquiryFragment());
        } else if (view == R.id.loanpess) {
            start(new OtherLoanAppliedQryInputFragment());
        }
    }
}
