package com.boc.bocsoft.mobile.bocmobile.buss.loan.pledgeloan.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;

/**
 * 作者：XieDu
 * 创建时间：2016/8/10 14:52
 * 描述：
 */
public abstract class PledgeLoanTypeView extends LinearLayout implements View.OnClickListener {

    protected TextView tvTitle;
    protected TextView tvDescription;
    protected TextView tvCount;
    protected Button btnApply;
    protected LinearLayout lytApply;
    protected TextView tvEmpty;
    private View rootView;

    protected BussFragment mBussFragment;

    public PledgeLoanTypeView(Context context) {
        this(context, null);
    }

    public PledgeLoanTypeView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PledgeLoanTypeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    protected void initView() {
        rootView = inflate(getContext(), R.layout.boc_fragment_pledge_loan_type, this);
        tvTitle = (TextView) rootView.findViewById(R.id.tv_title);
        tvDescription = (TextView) rootView.findViewById(R.id.tv_description);
        tvCount = (TextView) rootView.findViewById(R.id.tv_count);
        btnApply = (Button) rootView.findViewById(R.id.btn_apply);
        lytApply = (LinearLayout) rootView.findViewById(R.id.lyt_apply);
        tvEmpty = (TextView) rootView.findViewById(R.id.tv_empty);
        btnApply.setOnClickListener(this);
    }

    public void setFragment(BussFragment bussFragment) {
        mBussFragment = bussFragment;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_apply) {
            //TODO 进入申请页面
            applyLoan();
        }
    }

    protected abstract void applyLoan();

    protected void showData(boolean hasData) {
        if (hasData) {
            lytApply.setVisibility(View.VISIBLE);
            tvEmpty.setVisibility(View.GONE);
        } else {
            lytApply.setVisibility(View.GONE);
            tvEmpty.setVisibility(View.VISIBLE);
        }
    }

    public void clear(){
        lytApply.setVisibility(View.GONE);
        tvEmpty.setVisibility(View.GONE);
    }
}
