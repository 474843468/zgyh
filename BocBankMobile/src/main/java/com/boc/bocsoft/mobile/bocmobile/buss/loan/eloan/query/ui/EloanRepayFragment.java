package com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.query.ui;

import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.EloanConst;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.query.model.EloanStatusListModel;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.query.ui.adapter.RepaymentAdapter;
import com.boc.bocsoft.mobile.framework.widget.tabLayout.SlidingTabLayout;

/**
 * Created by louis.hui on 2016/10/24.
 * 用款记录列表
 */
public class EloanRepayFragment extends BussFragment {
    protected View rootView;
    protected SlidingTabLayout lytTab;
    protected ViewPager vpContent;
    /**试配器*/
    private RepaymentAdapter repaymentAdapter;
    /**已结清*/
    private EloanStatusListModel mRepayRecord;
    /**未结清*/
    private EloanStatusListModel mSettledRecord;
    /**已结清*/
    private EloanRepayrcdFragment mRepayrscdFragment;
    /**未结清*/
    private EloanRepayrcdFragment mRepayrcdFragment;
    /**还款账号*/
    private String mPayAccount;
    /**账户类型*/
    private String quoteType;
    /**结清标识*/
    private String repayFlag;
    /**额度编号*/
    private String mQuoteNo;

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        rootView = mInflater.inflate(R.layout.boc_drawingrecord_fragment, null);
        return rootView;
    }

    @Override
    protected boolean isHaveTitleBarView() {
        return true;
    }

    @Override
    protected String getTitleValue() {
        return getResources().getString(R.string.boc_repayment);
    }

    @Override
    protected boolean getTitleBarRed() {
        return false;
    }

    @Override
    protected boolean isDisplayRightIcon() {
        return false;
    }

    @Override
    protected void titleLeftIconClick() {
        super.titleLeftIconClick();
        //removeFragments();
    }

    @Override
    public void initView() {
        super.initView();
        lytTab = (SlidingTabLayout) rootView.findViewById(R.id.lyt_tab);
        vpContent = (ViewPager) rootView.findViewById(R.id.vpContent);

    }

    /**
     * 上页传值，已结清，未结清
     * @param repayRecord  未结清
     * @param settledRecord 已结清
     */
    public void setData(EloanStatusListModel repayRecord, EloanStatusListModel settledRecord) {
        //未结清列表
        mRepayRecord = repayRecord;
        //已结清列表
        mSettledRecord = settledRecord;
    }

    @Override
    public void initData() {
        super.initData();
        //账户类型
        quoteType = getArguments().getString(EloanConst.ELOAN_QUOTETYPE);
        //还款标识
        repayFlag = getArguments().getString(EloanConst.ELOAN_REPAYFLAG);
        //还款账户
        mPayAccount = getArguments().getString(EloanConst.PEPAY_ACCOUNT);
        // 额度编码
        mQuoteNo = getArguments().getString(EloanConst.LOAN_QUOTENO);

        //已结清列表

        EloanRepayrcdFragment repayrcdFragment = new EloanRepayrcdFragment();
        mRepayrcdFragment = repayrcdFragment;
        repayrcdFragment.setAccountLoanList(mRepayRecord, mSettledRecord);
        repayrcdFragment.setLoanFlag("N", quoteType);
        repayrcdFragment.setLoanData(mPayAccount, mQuoteNo);

        //已结清清列表

        EloanRepayrcdFragment repayrscdFragment = new EloanRepayrcdFragment();
        mRepayrscdFragment = repayrscdFragment;
        repayrscdFragment.setAccountLoanList(mRepayRecord, mSettledRecord);
        repayrscdFragment.setLoanFlag("Y", quoteType);
        repayrscdFragment.setLoanData(mPayAccount, mQuoteNo);

        repaymentAdapter = new RepaymentAdapter(getActivity().getSupportFragmentManager());

        repaymentAdapter .pageTitle(repayrcdFragment, getString(R.string.boc_repayment_rcd));
        repaymentAdapter.pageTitle(repayrscdFragment, getString(R.string.boc_repayment_settled));
        vpContent.setAdapter(repaymentAdapter);
        vpContent.setOffscreenPageLimit(2);
        lytTab.setViewPager(vpContent);
        //首次显示已结清、未结清
        if ("Y".equals(repayFlag)) {
            lytTab.setCurrentTab(1);
        } else {
            lytTab.setCurrentTab(0);
        }
    }
    /**
     * 释放现有的fragment
     */
    private void removeFragments(){
        FragmentTransaction mTransaction = getFragmentManager()
                .beginTransaction();
        mTransaction.remove(mRepayrcdFragment);
        mTransaction.remove(mRepayrscdFragment);
        mTransaction.commit();
    }

    @Override
    public void onDestroy() {
        if(!getActivity().isFinishing()) {
            removeFragments();
        }
        super.onDestroy();
    }
}
