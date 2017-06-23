package com.boc.bocsoft.mobile.bocmobile.buss.loan.pledgeloan.ui;

import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.MvpBussFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.pledgeloan.model.deposit.PledgeAvaAndPersonalTimeAccount;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.pledgeloan.model.finance.ProductsData;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.pledgeloan.presenter.PledgeLoanTypeSelectPresenter;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.pledgeloan.ui.deposit.PledgeLoanTypeDepositView;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.pledgeloan.ui.finance.PledgeLoanTypeFinanceView;
import com.boc.bocsoft.mobile.framework.widget.tabLayout.SlidingTabLayout;
import com.boc.bocsoft.mobile.framework.widget.tabLayout.TabLayoutPageAdapter;
import java.util.List;

/**
 *
 */
public class PledgeLoanTypeSelectFragment
        extends MvpBussFragment<PledgeLoanTypeSelectContract.Presenter>
        implements PledgeLoanTypeSelectContract.View, ViewPager.OnPageChangeListener {

    protected SlidingTabLayout lytTab;
    protected ViewPager vpPledgeLoan;
    private View rootView;

    private TabLayoutPageAdapter mPageAdapter;

    private PledgeLoanTypeDepositView mPledgeLoanTypeDepositView;
    private PledgeLoanTypeFinanceView mPledgeLoanTypeFinanceView;

    private boolean[] isInitted;

    /**
     * 初始化布局
     */
    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        rootView = mInflater.inflate(R.layout.boc_fragment_pledge_loan_type_select, null);
        return rootView;
    }

    @Override
    public void initView() {
        lytTab = (SlidingTabLayout) rootView.findViewById(R.id.lyt_tab);
        vpPledgeLoan = (ViewPager) rootView.findViewById(R.id.vp_pledge_loan);
    }

    @Override
    public void initData() {
        isInitted = new boolean[2];
        mPageAdapter = new TabLayoutPageAdapter();
        mPledgeLoanTypeDepositView = new PledgeLoanTypeDepositView(mContext);
        mPledgeLoanTypeDepositView.setFragment(this);
        mPledgeLoanTypeFinanceView = new PledgeLoanTypeFinanceView(mContext);
        mPledgeLoanTypeFinanceView.setFragment(this);
        mPageAdapter.addPage(mPledgeLoanTypeDepositView, getString(R.string.boc_pledge_deposit));
        mPageAdapter.addPage(mPledgeLoanTypeFinanceView, getString(R.string.boc_pledge_finance));
        vpPledgeLoan.setAdapter(mPageAdapter);
        lytTab.setViewPager(vpPledgeLoan);
        lytTab.setOnPageChangeListener(this);
        showLoadingDialog();
        getPresenter().loadDepositData();
    }

    @Override
    protected String getTitleValue() {
        return getString(R.string.boc_loan_pledge_online);
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
    protected PledgeLoanTypeSelectContract.Presenter initPresenter() {
        return new PledgeLoanTypeSelectPresenter(this);
    }

    @Override
    public void onLoadDepositDataSuccess(
            List<PledgeAvaAndPersonalTimeAccount> pledgeAvaAndPersonalTimeAccountList) {
        closeProgressDialog();
        isInitted[0] = true;
        mPledgeLoanTypeDepositView.onLoadSuccess(pledgeAvaAndPersonalTimeAccountList);
    }

    @Override
    public void onLoadDepositDataFailed() {
        closeProgressDialog();
        mPledgeLoanTypeDepositView.onLoadFailed();
    }

    @Override
    public void onLoadProductDataSuccess(ProductsData productsData) {
        closeProgressDialog();
        isInitted[1] = true;
        mPledgeLoanTypeFinanceView.onLoadSuccess(productsData);
    }

    @Override
    public void onLoadProductDataFailed() {
        closeProgressDialog();
        mPledgeLoanTypeFinanceView.onLoadFailed();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        if (!isInitted[position]) {
            showLoadingDialog();
            if (position == 0) {
                getPresenter().loadDepositData();
            } else if (position == 1) {
                getPresenter().loadProductData();
            }
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}