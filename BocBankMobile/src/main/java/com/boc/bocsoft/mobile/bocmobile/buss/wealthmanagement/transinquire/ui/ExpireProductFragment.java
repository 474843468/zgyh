package com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.transinquire.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.ApplicationConst;
import com.boc.bocsoft.mobile.bocmobile.ApplicationContext;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.widget.refreshliseview.PullToRefreshLayout;
import com.boc.bocsoft.mobile.bocmobile.base.widget.refreshliseview.PullableListView;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.transinquire.adapter.ExpireProductAdapter;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.transinquire.model.SelectParams;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.transinquire.model.XpadAccountQueryViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.transinquire.model.XpadDueProductProfitQueryViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.transinquire.model.XpadRecentAccountQueryViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.transinquire.presenter.ExpireProductPresenter;
import com.boc.bocsoft.mobile.common.utils.date.DateFormatters;

import java.util.ArrayList;
import java.util.List;

/**
 * Fragment：中银理财-交易查询-已到期产品页面
 * Created by zhx on 2016/9/7
 */
public class ExpireProductFragment extends BussFragment implements ExpireProductContact.View {
    public static final String PAGE_SIZE = String.valueOf(ApplicationConst.WEALTH_PAGE_SIZE); // 每页的条目数
    private static final int PAGE_INDEX = 2; // 页面索引
    private View mRootView;
    private PullToRefreshLayout refreshLayout;
    private PullableListView plv_pull;
    private ExpireProductPresenter expireProductPresenter;

    private XpadDueProductProfitQueryViewModel xpadDueProductProfitQueryViewModel;

    private XpadAccountQueryViewModel.XPadAccountEntity mRecentQueryAccount; // 最近查询账户
    private XpadAccountQueryViewModel.XPadAccountEntity mCurrentQueryAccount; // 当前查询的账户（列表中的数据所对应的账户）
    private XpadAccountQueryViewModel.XPadAccountEntity mChangedQueryAccount; // 从账户列表中选择后改变的账户

    private List<XpadAccountQueryViewModel.XPadAccountEntity> mAccountList; // 理财账户列表
    private List<XpadDueProductProfitQueryViewModel.DueProductEntity> mDueProductList = new ArrayList<XpadDueProductProfitQueryViewModel.DueProductEntity>(); // 过期产品列表

    private MyReceiver myReceiver;
    private SelectParams mSelectParams; // 当前选择条件

    private ExpireProductAdapter mAdapter;
    private boolean isPullToLoadMore = false; // 是否是上拉加载更多
    private int mCurrentSelectedPage = 0; // 当前选中的页面
    private TextView no_data_transfer_query; // 筛选后如果无数据，提示信息TextView
    private LinearLayout ll_no_data_query;
    private boolean isFisrtTimeEnter = true; // 是否首次进入
    private boolean isNeedScrollToTop = false;

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        mRootView = mInflater.inflate(R.layout.fragment_expire_product, null);
        return mRootView;
    }

    @Override
    public void initView() {
        no_data_transfer_query = (TextView) mRootView.findViewById(R.id.no_data_transfer_query);
        ll_no_data_query = (LinearLayout) mRootView.findViewById(R.id.no_data_query);
        refreshLayout = (PullToRefreshLayout) mRootView.findViewById(R.id.prl_refresh_layout);
        plv_pull = (PullableListView) mRootView.findViewById(R.id.plv_pull);
    }

    @Override
    public void initData() {
        initSelectParams();

        if (mCurrentSelectedPage == PAGE_INDEX) {
            showLoadingDialog("加载中...");
        }
        XpadAccountQueryViewModel viewModel = new XpadAccountQueryViewModel();
        viewModel.setQueryType("0");
        viewModel.setXpadAccountSatus("1");

        expireProductPresenter = new ExpireProductPresenter(this);
        expireProductPresenter.psnXpadAccountQuery(viewModel);
    }

    // 初始化选择的参数
    private void initSelectParams() {
        mSelectParams = new SelectParams();
        mSelectParams.setTransType("0");
        mSelectParams.setCurrency("000");
        mSelectParams.setStartDate(ApplicationContext.getInstance().getCurrentSystemDate().toLocalDate().minusMonths(3).plusDays(1).format(
                DateFormatters.dateFormatter1));
        mSelectParams.setEndDate(ApplicationContext.getInstance().getCurrentSystemDate().toLocalDate().format(DateFormatters.dateFormatter1));
    }

    @Override
    public void setListener() {
        IntentFilter intentFilter = new IntentFilter("startQuery");
        myReceiver = new MyReceiver();
        mActivity.registerReceiver(myReceiver, intentFilter);

        refreshLayout.setOnLoadListener(new PullToRefreshLayout.OnLoadListener() {
            @Override
            public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
                isPullToLoadMore = true;

                if (xpadDueProductProfitQueryViewModel == null) {
                    if (mCurrentQueryAccount != null) {
                        XpadDueProductProfitQueryViewModel viewModel = new XpadDueProductProfitQueryViewModel();
                        viewModel.setAccountKey(mCurrentQueryAccount.getAccountKey());
                        if (mSelectParams == null) {
                            viewModel.setStartDate(ApplicationContext.getInstance().getCurrentSystemDate().toLocalDate().minusMonths(3).plusDays(1).format(DateFormatters.dateFormatter1));
                            viewModel.setEndDate(ApplicationContext.getInstance().getCurrentSystemDate().toLocalDate().format(DateFormatters.dateFormatter1));
                        } else {
                            viewModel.setStartDate(mSelectParams.getStartDate());
                            viewModel.setStartDate(mSelectParams.getEndDate());
                        }
                        viewModel.setPageSize(PAGE_SIZE);
                        viewModel.setCurrentIndex("0");
                        viewModel.set_refresh("true");
                        viewModel.setCurrency("000");
                        expireProductPresenter.psnXpadDueProductProfitQuery(viewModel);
                        isNeedScrollToTop = false;
                    } else {
                        isPullToLoadMore = false;
                    }
                    return;
                }
                // 下拉加载更多的逻辑
                if (mDueProductList.size() < xpadDueProductProfitQueryViewModel.getRecordNumber()) {
                    // 到期产品查询
                    if (mCurrentQueryAccount != null) {
                        XpadDueProductProfitQueryViewModel viewModel = new XpadDueProductProfitQueryViewModel();
                        viewModel.setAccountKey(mCurrentQueryAccount.getAccountKey());
                        if (mSelectParams == null) {
                            viewModel.setStartDate(ApplicationContext.getInstance().getCurrentSystemDate().toLocalDate().minusMonths(3).plusDays(1).format(DateFormatters.dateFormatter1));
                            viewModel.setEndDate(ApplicationContext.getInstance().getCurrentSystemDate().toLocalDate().format(DateFormatters.dateFormatter1));
                        } else {
                            viewModel.setStartDate(mSelectParams.getStartDate());
                            viewModel.setEndDate(mSelectParams.getEndDate());
                        }
                        viewModel.setPageSize(PAGE_SIZE);
                        viewModel.setCurrentIndex(mDueProductList.size() + "");
                        viewModel.set_refresh("false");
                        viewModel.setCurrency("000");
                        expireProductPresenter.psnXpadDueProductProfitQuery(viewModel);
                        isNeedScrollToTop = false;
                    }
                } else {
                    isPullToLoadMore = false;
                    refreshLayout.loadmoreCompleted(PullToRefreshLayout.NO_MORE_DATA);
                }
            }
        });

        // 设置条目的点击事件
        plv_pull.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                XpadDueProductProfitQueryViewModel.DueProductEntity dueProductEntity = mDueProductList.get(position);
                ExpireProductDetailFragment expireProductDetailFragment = new ExpireProductDetailFragment();
                Bundle bundle = new Bundle();
                bundle.putParcelable("dueProductEntity", dueProductEntity);
                bundle.putParcelable("currentQueryAccount", mCurrentQueryAccount);
                expireProductDetailFragment.setArguments(bundle);
                start(expireProductDetailFragment);
            }
        });
    }

    @Override
    protected boolean isHaveTitleBarView() {
        return false;
    }

    @Override
    public void psnXpadAccountQuerySuccess(XpadAccountQueryViewModel xpadAccountQueryViewModel) {
        mAccountList = xpadAccountQueryViewModel.getList();

        // 查询最近账户
        XpadRecentAccountQueryViewModel xpadRecentAccountQueryViewModel = new XpadRecentAccountQueryViewModel();
        expireProductPresenter.psnXpadRecentAccountQuery(xpadRecentAccountQueryViewModel);
    }

    // 广播接收者，接收TransInquireFragment发送的广播（共3种通知类型，页面选中通知，筛选条件选择通知，账户选择通知，这3种广播都有可能导致重新查询）
    class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            int noticeType = intent.getIntExtra("noticeType", -1);
            switch (noticeType) {
                case 0: // 0表示“页面选中”通知
                    handlePageSelectedNotice(intent);
                    break;
                case 1: // 1表示“账户选择”通知
                    handleAccountSelectedNotice(intent);
                    break;
                case 2: // 2表示“筛选条件选择”通知
                    handleConditionSelectedNotice(intent);
                    break;
                case 5: // 5表示“刷新”通知
                    isNeedScrollToTop = true;
                    if (mCurrentSelectedPage == PAGE_INDEX) {
                        showLoadingDialog("加载中...");
                    }
                    XpadAccountQueryViewModel viewModel = new XpadAccountQueryViewModel();
                    viewModel.setQueryType("0");
                    viewModel.setXpadAccountSatus("1");

                    expireProductPresenter.psnXpadAccountQuery(viewModel);
                    break;
            }
        }
    }

    // 处理“筛选条件选择”通知
    private void handleConditionSelectedNotice(Intent intent) {
        // 如果广播源不是来自TransInquireFragment，直接返回
        String broadcastSource = intent.getStringExtra("BroadcastSource");
        if (!"TransInquireFragment".equals(broadcastSource)) {
            return;
        }

        SelectParams selectParams = intent.getParcelableExtra("selectParams");
        int selectPagePosition = intent.getIntExtra("selectPagePosition", -1);
        mCurrentSelectedPage = selectPagePosition;
        if (selectPagePosition == PAGE_INDEX) { // 如果是当前页
            mSelectParams = selectParams;
            isNeedScrollToTop = true;
            startQuery();
        }
    }

    // 处理“账户选择”通知
    private void handleAccountSelectedNotice(Intent intent) {
        // 如果广播源不是来自TransInquireFragment，直接返回
        String broadcastSource = intent.getStringExtra("BroadcastSource");
        if (!"TransInquireFragment".equals(broadcastSource)) {
            return;
        }

        XpadAccountQueryViewModel.XPadAccountEntity account = intent.getParcelableExtra("account");
        int selectPagePosition = intent.getIntExtra("selectPagePosition", -1);
        mCurrentSelectedPage = selectPagePosition;
        if (selectPagePosition == PAGE_INDEX) { // 就是当前页面
            mCurrentQueryAccount = account;
            isNeedScrollToTop = true;
            startQuery();
        } else { // 不是当前页面
            mChangedQueryAccount = account;
        }
    }

    // 处理“页面选中”通知
    private void handlePageSelectedNotice(Intent intent) {
        // 如果广播源不是来自TransInquireFragment，直接返回
        String broadcastSource = intent.getStringExtra("BroadcastSource");
        if (!"TransInquireFragment".equals(broadcastSource)) {
            return;
        }

        int selectPagePosition = intent.getIntExtra("selectPagePosition", -1);
        mCurrentSelectedPage = selectPagePosition;
        if (selectPagePosition == PAGE_INDEX) {
            boolean isStartQuery = false;

            if (mChangedQueryAccount != null && !mChangedQueryAccount.getAccountNo().equals(mCurrentQueryAccount.getAccountNo())) {
                isStartQuery = false; // 因为过期产品与具体选择的账户无关，所以为false
                mCurrentQueryAccount = mChangedQueryAccount;
                mChangedQueryAccount = null;
            }

            if (isStartQuery) {
                isNeedScrollToTop = true;
                startQuery();
            }
        }
    }

    // 开始查询（此方法由TransInquireFragment发起广播触发）
    public void startQuery() {
        if (mCurrentSelectedPage == PAGE_INDEX) {
            showLoadingDialog("加载中...");
        }
        XpadDueProductProfitQueryViewModel viewModel = new XpadDueProductProfitQueryViewModel();
        viewModel.setAccountKey(mCurrentQueryAccount.getAccountKey());
        viewModel.setStartDate(mSelectParams.getStartDate());
        viewModel.setEndDate(mSelectParams.getEndDate());
        viewModel.setPageSize(PAGE_SIZE);
        viewModel.setCurrentIndex("0");
        viewModel.set_refresh("true");
        viewModel.setCurrency(mSelectParams.getCurrency());
        expireProductPresenter.psnXpadDueProductProfitQuery(viewModel);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mActivity.unregisterReceiver(myReceiver);
    }

    @Override
    public void psnXpadAccountQueryFail(BiiResultErrorException biiResultErrorException) {
        if (mCurrentSelectedPage == PAGE_INDEX) {
            closeProgressDialog();
        }
    }

    @Override
    public void psnXpadDueProductProfitQuerySuccess(XpadDueProductProfitQueryViewModel xpadDueProductProfitQueryViewModel) {
        if (mCurrentSelectedPage == PAGE_INDEX) {
            closeProgressDialog();
        }
        this.xpadDueProductProfitQueryViewModel = xpadDueProductProfitQueryViewModel;
        List<XpadDueProductProfitQueryViewModel.DueProductEntity> dueProductList = xpadDueProductProfitQueryViewModel.getList();

        if (!isPullToLoadMore) {  // 如果不是上拉加载更多，那么清空列表
            mDueProductList.clear();
        }
        mDueProductList.addAll(dueProductList);

        if (mAdapter == null) {
            mAdapter = new ExpireProductAdapter(mActivity, mDueProductList);
            plv_pull.setAdapter(mAdapter);
        } else {
            mAdapter.setDueProductList(mDueProductList);
            mAdapter.notifyDataSetChanged();
        }

        isPullToLoadMore = false;
        refreshLayout.loadmoreCompleted(PullToRefreshLayout.SUCCEED);

        // 回到顶部
        if(mDueProductList.size() > 0 && isNeedScrollToTop == true) {
            plv_pull.setSelection(0);
        }

        isNeedScrollToTop = false;
        handleNoData();
    }

    @Override
    public void psnXpadDueProductProfitQueryFail(BiiResultErrorException biiResultErrorException) {
        if (mCurrentSelectedPage == PAGE_INDEX) {
            closeProgressDialog();
        }
    }

    // 成功回调:查询最近账户
    @Override
    public void psnXpadRecentAccountQuerySuccess(XpadRecentAccountQueryViewModel xpadRecentAccountQueryViewModel) {
        if (mAccountList != null) {
            for (XpadAccountQueryViewModel.XPadAccountEntity accountEntity : mAccountList) {
                if (accountEntity.getAccountNo().equals(xpadRecentAccountQueryViewModel.getAccountNo())) {
                    mCurrentQueryAccount = accountEntity;
                    mRecentQueryAccount = accountEntity;
                }
            }
        }

        // 临时性的代码
        if (mCurrentQueryAccount == null) {
            mCurrentQueryAccount = mAccountList != null ? mAccountList.get(0) : null;
            mRecentQueryAccount = mAccountList != null ? mAccountList.get(0) : null;
        }

        // 到期产品查询
        if (mCurrentQueryAccount != null) {
            XpadDueProductProfitQueryViewModel viewModel = new XpadDueProductProfitQueryViewModel();
            viewModel.setAccountKey(null);
            viewModel.setStartDate(ApplicationContext.getInstance().getCurrentSystemDate().toLocalDate().minusMonths(3).plusDays(1).format(DateFormatters.dateFormatter1));
            viewModel.setEndDate(ApplicationContext.getInstance().getCurrentSystemDate().toLocalDate().format(DateFormatters.dateFormatter1));
            viewModel.setPageSize(PAGE_SIZE);
            viewModel.setCurrentIndex("0");
            viewModel.set_refresh("true");
            viewModel.setCurrency("000");
            expireProductPresenter.psnXpadDueProductProfitQuery(viewModel);
        }
    }

    @Override
    public void psnXpadRecentAccountQueryFail(BiiResultErrorException biiResultErrorException) {
        if (mCurrentSelectedPage == PAGE_INDEX) {
            closeProgressDialog();
        }
    }

    @Override
    public boolean onBack() {
        TransInquireFragment transInquireFragment = findFragment(TransInquireFragment.class);
        if (transInquireFragment != null){
            return transInquireFragment.onBack();
        } else {
            return true;
        }
    }

    // 处理有无数据时的情况
    private void handleNoData() {
        Log.e("ljljlj", "ExpireProductFragment handleNoData()");
        // 处理“数据是否为空”的情况
        if (mDueProductList != null && mDueProductList.size() > 0) {
            ll_no_data_query.setVisibility(View.GONE);
        } else {
            ll_no_data_query.setVisibility(View.VISIBLE);
            no_data_transfer_query.setText(isFisrtTimeEnter ? getResources().getString(R.string.boc_transfer_query_empty): getResources().getString(R.string.boc_transfer_select_empty));
//
//            }
        }
        isFisrtTimeEnter = false;
    }

    @Override
    public void setPresenter(ExpireProductContact.Presenter presenter) {
    }
}
