package com.boc.bocsoft.mobile.bocmobile.buss.creditcard.myinstallment.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.MvpBussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.model.AccountBean;
import com.boc.bocsoft.mobile.bocmobile.base.widget.refreshliseview.LoadMoreListHelper;
import com.boc.bocsoft.mobile.bocmobile.base.widget.refreshliseview.PullToRefreshLayout;
import com.boc.bocsoft.mobile.bocmobile.base.widget.refreshliseview.PullableListView;
import com.boc.bocsoft.mobile.bocmobile.buss.creditcard.myinstallment.adapter.InstallmentHistoryAdapter;
import com.boc.bocsoft.mobile.bocmobile.buss.creditcard.myinstallment.model.InstallmentHistoryViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.creditcard.myinstallment.model.InstallmentRecordModel;
import com.boc.bocsoft.mobile.bocmobile.buss.creditcard.myinstallment.presenter.InstallmentHistoryPresenter;


/**
 * Created by yangle on 2016/11/18.
 *  分期纪录 fragment
 */
public class InstallmentHistoryFragment extends MvpBussFragment<InstallmentHistoryPresenter> implements InstallmentHistoryContract.View, LoadMoreListHelper.LoadListListener<InstallmentHistoryViewModel> {

    public static final String ACCOUNT_BEAN = "account_bean";

    private View mRootView;
    private View mEmptyView;
    private PullToRefreshLayout mPullToRefreshLayout;
    private PullableListView mListView;
    private InstallmentHistoryAdapter mAdapter;
    private InstallmentHistoryViewModel mViewModel;
    private LoadMoreListHelper mLoadMoreListHelper;

    private AccountBean mAccountBean;

    public static InstallmentHistoryFragment newInstance(AccountBean accountBean) {
        InstallmentHistoryFragment fragment = new InstallmentHistoryFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(ACCOUNT_BEAN,accountBean);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAccountBean = getArguments().getParcelable(ACCOUNT_BEAN);
    }

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        mRootView = mInflater.inflate(R.layout.boc_fragment_creditcard_installmenthistory, null);
        return mRootView;
    }

    @Override
    public void reInit() {
        super.reInit();
        // 重新请求刷新数据
        mViewModel.reset();
        getPresenter().loadDividedPayRecords();
    }

    @Override
    public void initView() {
        super.initView();
        mEmptyView = mRootView.findViewById(R.id.empty_view);
        mPullToRefreshLayout = (PullToRefreshLayout) mRootView.findViewById(R.id.pull_to_refresh_view);
        mListView = (PullableListView) mRootView.findViewById(R.id.content_view);
    }

    @Override
    public void initData() {
        super.initData();
        mViewModel = new InstallmentHistoryViewModel(mAccountBean.getAccountId());

        mLoadMoreListHelper = new LoadMoreListHelper<>(mViewModel, mPullToRefreshLayout, this);
        mAdapter = new InstallmentHistoryAdapter(super.mContext);
        mListView.setAdapter(mAdapter);
        // 请求数据
        getPresenter().loadDividedPayRecords();
    }

    @Override
    public void setListener() {
        super.setListener();
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                goToDetailPage(position);
            }
        });
    }

    private void goToDetailPage(int position) {
        // 跳转祥情
        InstallmentRecordModel recordModel = mAdapter.getItem(position);
        recordModel.setAccountId(mAccountBean.getAccountId());
        recordModel.setCreditCardNum(mAccountBean.getAccountNumber());
        start(InstallmentDetailFragment.newInstance(recordModel));
    }

    @Override
    public void setPresenter(InstallmentHistoryContract.Presenter presenter) {
    }

    @Override
    protected InstallmentHistoryPresenter initPresenter() {
        return new InstallmentHistoryPresenter(this);
    }

    @Override
    public void showAndUpdateRecords() {
        showContentView();
    }

    @Override
    public void loadFailed() {
        mLoadMoreListHelper.onLoadFailed();
        //Toast.makeText(mContext,"加载失败,请检查网络",Toast.LENGTH_SHORT).show();
    }

    @Override
    public InstallmentHistoryViewModel getViewModel() {
        return mViewModel;
    }

    @Override
    public void showNoRecord() {
        showEmptyView();
    }

    @Override
    public void showLoading() {
        super.showLoadingDialog();
    }

    @Override
    public void closeLoading() {
        super.closeProgressDialog();
    }

    private void showEmptyView() {
        mEmptyView.setVisibility(View.VISIBLE);
        mPullToRefreshLayout.setVisibility(View.GONE);
    }

    private void showContentView() {
        mEmptyView.setVisibility(View.GONE);
        mPullToRefreshLayout.setVisibility(View.VISIBLE);

        mAdapter.setDatas(mViewModel.getList());
        mLoadMoreListHelper.onLoadSuccess();
    }

    @Override
    public void loadData(InstallmentHistoryViewModel viewModel) {
        // 上拉加载更多
        getPresenter().loadDividedPayRecords();
    }

    @Override
    protected String getTitleValue() {
        return getString(R.string.boc_crcd_myinstallment_instmt_record);
    }

    @Override
    protected boolean getTitleBarRed() {
        return false;
    }

    @Override
    protected boolean isDisplayRightIcon() {
        return false;
    }

}
