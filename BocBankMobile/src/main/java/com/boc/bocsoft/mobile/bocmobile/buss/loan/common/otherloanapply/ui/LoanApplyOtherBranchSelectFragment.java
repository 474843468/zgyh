package com.boc.bocsoft.mobile.bocmobile.buss.loan.common.otherloanapply.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.widget.refreshliseview.LoadMoreListHelper;
import com.boc.bocsoft.mobile.bocmobile.base.widget.refreshliseview.PullToRefreshLayout;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.common.otherloanapply.model.BranchSelectViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.common.otherloanapply.model.OnLineLoanBranchBean;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.common.otherloanapply.presenter.LoanApplyOtherBranchSelectPresenter;
import com.boc.bocsoft.mobile.framework.utils.PublicConst;

/**
 * Use the {@link LoanApplyOtherBranchSelectFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoanApplyOtherBranchSelectFragment extends BussFragment
        implements LoanApplyOtherBranchSelectContract.View,
        LoadMoreListHelper.LoadListListener<BranchSelectViewModel>,
        AdapterView.OnItemClickListener {

    private static final String PARAM_CITY = "param_city";
    private static final String PARAM_PRODUCT = "param_product";
    private static final String PARAM_REFRESH= "param_refresh";
    protected ListView lvBranch;
    protected PullToRefreshLayout prlRefreshLayout;
    private View rootView;
    private LoanApplyOtherBranchSelectPresenter mLoanApplyOtherBranchSelectPresenter;
    private String mCityCode, mProductCode,mRefresh;
    private BranchSelectViewModel mViewModel;
    private LoadMoreListHelper<BranchSelectViewModel> mLoadMoreListHelper;
    private BranchSelectAdapter mAdapter;
    private TextView tvNoData;

    public LoanApplyOtherBranchSelectFragment() {
    }

    public static LoanApplyOtherBranchSelectFragment newInstance(String cityCode,
            String productCode,String refresh) {
        LoanApplyOtherBranchSelectFragment fragment = new LoanApplyOtherBranchSelectFragment();
        Bundle args = new Bundle();
        args.putString(PARAM_CITY, cityCode);
        args.putString(PARAM_PRODUCT, productCode);
        args.putString(PARAM_REFRESH,refresh);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * 初始化布局
     */
    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        rootView = mInflater.inflate(R.layout.boc_fragment_loan_apply_other_branch_select, null);
        return rootView;
    }

    @Override
    public void initView() {
        lvBranch = (ListView) rootView.findViewById(R.id.lv_branch);
        prlRefreshLayout = (PullToRefreshLayout) rootView.findViewById(R.id.prl_refresh_layout);
        tvNoData = (TextView) rootView.findViewById(R.id.tvNoData);
        lvBranch.setOnItemClickListener(this);
    }

    @Override
    public void initData() {
        if (getArguments() != null) {
            mCityCode = getArguments().getString(PARAM_CITY);
            mProductCode = getArguments().getString(PARAM_PRODUCT);
            mRefresh=getArguments().getString(PARAM_REFRESH);
        }
        mViewModel = new BranchSelectViewModel();
        mViewModel.setCityCode(mCityCode);
        mViewModel.setProductCode(mProductCode);
        mViewModel.setPageSize(50);
        mViewModel.set_refresh(mRefresh);
        mLoanApplyOtherBranchSelectPresenter = new LoanApplyOtherBranchSelectPresenter(this);
        mLoadMoreListHelper = new LoadMoreListHelper<>(mViewModel, prlRefreshLayout, this);
        showLoadingDialog();
        mLoanApplyOtherBranchSelectPresenter.getOnLineLoanBranch(mViewModel);
    }


    @Override
    protected String getTitleValue() {
        return getResources().getString(R.string.boc_loan_branch_select);
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
    public void onDestroy() {
        mLoanApplyOtherBranchSelectPresenter.unsubscribe();
        super.onDestroy();
    }

    @Override
    public void onLoadSuccess(BranchSelectViewModel viewModel) {
        closeProgressDialog();
        if(mAdapter == null && viewModel.getRecordNumber() <= 0) {//首次进入,且获取记录数目为零
            refleshLayout(true);
            return;
        } else {
            refleshLayout(false);
        }
        if (mAdapter==null) {
            mAdapter = new BranchSelectAdapter(mContext);
            lvBranch.setAdapter(mAdapter);
            mViewModel.set_refresh("false");
        }
        mLoadMoreListHelper.onLoadSuccess();
        mAdapter.setDatas(viewModel.getList());
    }

    @Override
    public void onLoadFailed() {
        if (mAdapter == null) {//首次进入,获取数据失败
            refleshLayout(true);
        }
        mLoadMoreListHelper.onLoadFailed();
    }

    @Override
    public void loadData(BranchSelectViewModel viewModel) {
        showLoadingDialog();
        mLoanApplyOtherBranchSelectPresenter.getOnLineLoanBranch(viewModel);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        OnLineLoanBranchBean onLineLoanBranchBean = mAdapter.getItem(position);
        Bundle bundle = new Bundle();
        bundle.putParcelable(PublicConst.RESULT_DATA, onLineLoanBranchBean);
        setFramgentResult(Activity.RESULT_OK, bundle);
        pop();
    }

    private void refleshLayout(boolean isShowNoData) {
        if (isShowNoData == true) {
            tvNoData.setText(getString(R.string.boc_loan_bank_branch_no));
            if(tvNoData.getVisibility() == View.GONE || tvNoData.getVisibility() == View.INVISIBLE){
                tvNoData.setVisibility(View.VISIBLE);
            }
            if (prlRefreshLayout.getVisibility() == View.VISIBLE) {
                prlRefreshLayout.setVisibility(View.GONE);
            }
        } else {
            if(prlRefreshLayout.getVisibility() == View.GONE || prlRefreshLayout.getVisibility() == View.INVISIBLE){
                prlRefreshLayout.setVisibility(View.VISIBLE);
            }
            if (tvNoData.getVisibility() == View.VISIBLE) {
                tvNoData.setVisibility(View.GONE);
            }
        }
    }
}