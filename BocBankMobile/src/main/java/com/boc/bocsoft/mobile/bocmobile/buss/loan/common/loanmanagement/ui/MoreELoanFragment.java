package com.boc.bocsoft.mobile.bocmobile.buss.loan.common.loanmanagement.ui;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.boc.bocsoft.mobile.bocmobile.ApplicationContext;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.common.loanmanagement.LoanCosnt;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.common.loanmanagement.model.LoanAccountListModel;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.common.loanmanagement.model.LoanQuoteViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.query.model.EloanQuoteViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.query.ui.EloanDrawFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.reloan.query.ui.ReloanStatusFragment;
import com.boc.bocsoft.mobile.common.utils.date.DateFormatters;

import java.util.List;

import static com.boc.bocsoft.mobile.bocmobile.R.id.lv_header_bank;

/**
 * Created by huixiaobo on 2016/9/23.
 * 特殊页面
 */
public class MoreELoanFragment extends BussFragment {
    /**view*/
    protected View rootView;
    /**头条view*/
    private View mheaderView;
    /**头部view*/
    private ListView mHeaderLv;
    /**列表*/
    protected ListView eloanAccount;
    /**列表适配器*/
    private LoanHeaderAdapter mAdapter;
    /**循环贷款适配器*/
    private UserLoanAdapter mUserAdapter;
    /**中银E贷返回值*/
    private List<EloanQuoteViewModel> mEloanQuote;

    /**1046 和1047 循环贷款*/
    private List<LoanAccountListModel.PsnLOANListEQueryBean> mLoanAccountList;
    /**跳转指定贷款页面*/
    private boolean isLoanPager;
    /**逾期信息*/
    private String mOverdue;

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        rootView = mInflater.inflate(R.layout.boc_eloanspecial_view, null);
        return rootView;
    }

    @Override
    public void initView() {
        super.initView();
        eloanAccount = (ListView) rootView.findViewById(R.id.eloanAccount);
        mheaderView = View.inflate(getContext(), R.layout.boc_loanheard_listview, null);
        mHeaderLv = (ListView) mheaderView.findViewById(lv_header_bank);
    }

    @Override
    public void initData() {
        super.initData();
        getPagerData();
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
    protected String getTitleValue() {
        return getResources().getString(R.string.boc_loan_selectDraw);
    }

    /**
     * 获得上页面传递的参数
     * @param eloanQuote 中银E贷
     * @param loanAccountList 1046 或1047 循环贷款
     */
    public void putEloanData(List<EloanQuoteViewModel> eloanQuote,
                             List<LoanAccountListModel.PsnLOANListEQueryBean> loanAccountList, String overdue) {
        mEloanQuote = eloanQuote;
        mLoanAccountList = loanAccountList;
        mOverdue = overdue;
    }

    /**
     * 页面加载数据
     */
    private void getPagerData() {
        //中银E贷 和 1046、1047 贷款
        if (mEloanQuote != null && mEloanQuote.size() > 0
                && mLoanAccountList != null && mLoanAccountList.size() > 0) {

            mAdapter = new LoanHeaderAdapter(getContext());
            mAdapter.setLoanQuote(mEloanQuote);
            mHeaderLv.setAdapter(mAdapter);

            int count = mAdapter.getCount();
            DisplayMetrics metrics = new DisplayMetrics();
            Activity activity = (Activity) mActivity;
            activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
            float density = metrics.density;
            int itemHeight = 0;
            if (count == 1) {
                itemHeight = (int) activity.getResources().getDimension(R.dimen.boc_space_between_220px);
            } else {
                itemHeight = (int) activity.getResources().getDimension(R.dimen.boc_space_between_224px);
            }
            LinearLayout.LayoutParams latyoutParams = (LinearLayout.LayoutParams) mHeaderLv.getLayoutParams();
            latyoutParams.height = itemHeight * count;
            mHeaderLv.setLayoutParams(latyoutParams);

            eloanAccount.addHeaderView(mheaderView);
            mUserAdapter = new UserLoanAdapter(getContext());
            mUserAdapter.setLoanAccountList(mLoanAccountList);
            eloanAccount.setAdapter(mUserAdapter);

        } //中银E贷
        else if (mEloanQuote != null && mEloanQuote.size() > 0) {
            mAdapter = new LoanHeaderAdapter(getContext());
            mAdapter.setLoanQuote(mEloanQuote);
            eloanAccount.setAdapter(mAdapter);
            isLoanPager = true;
        } // 1046、1047贷款
        else {
            mUserAdapter = new UserLoanAdapter(getContext());
            mUserAdapter.setLoanAccountList(mLoanAccountList);
            eloanAccount.setAdapter(mUserAdapter);
        }
    }

    @Override
    public void setListener() {
        super.setListener();

        // 中银E贷点击事件
        mHeaderLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                start(EloanDrawFragment.newInstance(mEloanQuote.get(position),mOverdue));
            }
        });

        //中银E 或1046 或 1047 点击事件
        eloanAccount.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mEloanQuote != null && mEloanQuote.size() > 0
                        && mLoanAccountList != null && mLoanAccountList.size() > 0) {
                    ReloanStatusFragment statusFragment = new ReloanStatusFragment();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(LoanCosnt.LOAN_DATA, mLoanAccountList.get(position -1));
                    bundle.putString(LoanCosnt.LOAN_ENDDATE, ApplicationContext.getInstance().getCurrentSystemDate().
                            format(DateFormatters.dateFormatter1));
                    bundle.putString(LoanCosnt.LOAN_OVERDUE, mOverdue);
                    statusFragment.setArguments(bundle);
                    start(statusFragment);
                } else {
                    if (isLoanPager) {
                        start(EloanDrawFragment.newInstance(mEloanQuote.get(position),mOverdue));
                    } else {
                        ReloanStatusFragment statusFragment = new ReloanStatusFragment();
                        Bundle bundle = new Bundle();
                        bundle.putSerializable(LoanCosnt.LOAN_DATA, mLoanAccountList.get(position));
                        bundle.putString(LoanCosnt.LOAN_ENDDATE,
                                String.valueOf(ApplicationContext.getInstance().getCurrentSystemDate()));
                        bundle.putString(LoanCosnt.LOAN_OVERDUE, mOverdue);
                        statusFragment.setArguments(bundle);
                        start(statusFragment);
                    }
                }

            }
        });
    }
}
