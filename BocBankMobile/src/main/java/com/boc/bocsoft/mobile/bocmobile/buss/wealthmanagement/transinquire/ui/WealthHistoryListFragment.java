package com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.transinquire.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.ApplicationConst;
import com.boc.bocsoft.mobile.bocmobile.ApplicationContext;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;
import com.boc.bocsoft.mobile.bocmobile.base.utils.PublicCodeUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.refreshliseview.PullToRefreshLayout;
import com.boc.bocsoft.mobile.bocmobile.base.widget.showlistview.PinnedSectionListView;
import com.boc.bocsoft.mobile.bocmobile.base.widget.showlistview.ShowListConst;
import com.boc.bocsoft.mobile.bocmobile.base.widget.showlistview.adapter.ShowListAdapter;
import com.boc.bocsoft.mobile.bocmobile.base.widget.showlistview.bean.ShowListBean;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.transinquire.model.SelectParams;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.transinquire.model.XpadAccountQueryViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.transinquire.model.XpadHisTradStatusViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.transinquire.model.XpadQueryGuarantyProductListViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.transinquire.model.XpadRecentAccountQueryViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.transinquire.presenter.WealthHistoryListPresenter;
import com.boc.bocsoft.mobile.common.utils.date.DateFormatters;

import org.threeten.bp.LocalDate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Fragment：中银理财-历史交易-列表页面
 * Created by zc on 2016/9/14
 */
public class WealthHistoryListFragment extends BussFragment implements WealthHistoryListContract.View {

    public static final String PAGE_SIZE = String.valueOf(ApplicationConst.WEALTH_PAGE_SIZE); // 每页的条目数
    private View rootView;
    private WealthHistoryListPresenter wealthHistoryListPresenter;
    // 上拉刷新
    private PullToRefreshLayout refreshLayout;
    // 查询列表组件
    private PinnedSectionListView pinnedSectionListView;
    private ShowListAdapter mAdapter;
    private List<ShowListBean> mShowListBean;

    // 最大查询范围为3个月
    private final static int MAX_QUERY_RANGE = 3;
    //--------------
    private XpadAccountQueryViewModel xpadAccountQueryViewModel;
    private XpadHisTradStatusViewModel xpadHisTradStatusViewModel;//常规交易

    private static final int PAGE_INDEX = 0; // 页面索引
    //组合购买
    private XpadQueryGuarantyProductListViewModel xpadQueryGuarantyProductListViewModel;

    private XpadAccountQueryViewModel.XPadAccountEntity currentQueryAccount; // 当前查询的账户（列表中的数据所对应的账户）
    private List<XpadAccountQueryViewModel.XPadAccountEntity> mAccountList;

    private XpadAccountQueryViewModel.XPadAccountEntity recentQueryAccount; // 最近查询账户
    private XpadAccountQueryViewModel.XPadAccountEntity mChangedQueryAccount; // 改变的查询账户

    List<XpadHisTradStatusViewModel.XpadHisTradStatusResultEntity> mHisTradList = new ArrayList<XpadHisTradStatusViewModel.XpadHisTradStatusResultEntity>(); // 常规交易数据集合

    //还没有这个
    List<XpadQueryGuarantyProductListViewModel.QueryGuarantyProductListEntity> mHisComTradList = new ArrayList<XpadQueryGuarantyProductListViewModel.QueryGuarantyProductListEntity>(); // 组合交易数据集合

    private boolean isPullToLoadMore; // 是否是上拉加载更多
    private boolean isTransTypeChange; // 交易类型是否改变

    private String mLastTransType = "0"; // 上次查询时的交易类型
    private LinearLayout ll_no_data;
    private TextView tv_no_history_data;
    private boolean isFisrtTimeEnter = true; // 是否首次进入当前页面的标志

    private int currentTransType = 0; // 当前交易类型(0表示常规，1表示组合)
    private SelectParams mSelectParams;
    private MyReceiver myReceiver;

    private String ibknum;//分行联行号
    private int mCurrentSelectedPage = 0;
    private boolean isNeedScrollToTop = false;


    /**
     * 初始化布局
     */
    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        rootView = mInflater.inflate(R.layout.boc_fragment_wealth_history_list, null);
        return rootView;
    }

    @Override
    public void initView() {
        super.initView();

        refreshLayout = (PullToRefreshLayout) rootView.findViewById(R.id.refresh_history_query);
        pinnedSectionListView = (PinnedSectionListView) rootView.findViewById(R.id.lv_history_query);
        pinnedSectionListView.setShadowVisible(false);
        tv_no_history_data = (TextView) rootView.findViewById(R.id.no_data_history_query);
        ll_no_data = (LinearLayout) rootView.findViewById(R.id.no_data_query);

    }

    // 初始化选择的参数
    private void initSelectParams() {
        mSelectParams = new SelectParams();
        mSelectParams.setTransType("0");
        mSelectParams.setCurrency("000");
        mSelectParams.setStartDate(ApplicationContext.getInstance().getCurrentSystemDate().toLocalDate().minusMonths(3).plusDays(1).format(DateFormatters.dateFormatter1));
        mSelectParams.setEndDate(ApplicationContext.getInstance().getCurrentSystemDate().toLocalDate().format(DateFormatters.dateFormatter1));
    }

    @Override
    public void initData() {
        mShowListBean = new ArrayList<ShowListBean>();
        mAdapter = new ShowListAdapter(mContext, -1);
        pinnedSectionListView.saveAdapter(mAdapter);
        pinnedSectionListView.setAdapter(mAdapter);
        initSelectParams();
        // 请求所有账户
        // 请求所有账户
        if (mCurrentSelectedPage == PAGE_INDEX) {
            showLoadingDialog("请稍候...");
        }

        XpadAccountQueryViewModel viewModel = new XpadAccountQueryViewModel();
        viewModel.setQueryType("0");
        viewModel.setXpadAccountSatus("1");
        wealthHistoryListPresenter = new WealthHistoryListPresenter(this);
        wealthHistoryListPresenter.psnXpadAccountQuery(viewModel);
    }

    @Override
    public void setPresenter(WealthHistoryListContract.Presenter presenter) {

    }

    @Override
    public void setListener() {
        IntentFilter intentFilter = new IntentFilter("startQuery");
        myReceiver = new MyReceiver();
        mActivity.registerReceiver(myReceiver, intentFilter);

        pinnedSectionListView.setListener(new PinnedSectionListView.ClickListener() {
            @Override
            public void onItemClickListener(int position) {
                switch (currentTransType) {
                    //进入详情列表
                    case 0: // 常规交易
                        XpadHisTradStatusViewModel.XpadHisTradStatusResultEntity xpadHisTradStatusResultEntity = mHisTradList.get(position);
                        String amountKey = xpadHisTradStatusViewModel.getAccountKey();
                        FinanceHistoryDetailsFragment financeHistoryDetailsFragment = new FinanceHistoryDetailsFragment();
                        Bundle bundle = new Bundle();
                        bundle.putString("amountKey", amountKey);
                        bundle.putInt("selectPagePosition", PAGE_INDEX);
                        bundle.putParcelable("xpadHisTradStatusResultEntity", xpadHisTradStatusResultEntity);
                        bundle.putParcelable("currentQueryAccount", currentQueryAccount);
                        financeHistoryDetailsFragment.setArguments(bundle);
                        start(financeHistoryDetailsFragment);
                        break;
                    case 1: // 组合交易
                        XpadQueryGuarantyProductListViewModel.QueryGuarantyProductListEntity hisComTradEntity = mHisComTradList.get(position);
                        HistoryComPurchaseDetailFragment historyComPurchaseDetailFragment = new HistoryComPurchaseDetailFragment();
                        Bundle bundle1 = new Bundle();
                        bundle1.putParcelable("hisComTradEntity", hisComTradEntity);
                        bundle1.putParcelable("currentQueryAccount", currentQueryAccount);
                        bundle1.putInt("selectPagePosition", PAGE_INDEX);
                        historyComPurchaseDetailFragment.setArguments(bundle1);
                        start(historyComPurchaseDetailFragment);
                        break;
                }
            }
        });

        refreshLayout.setOnLoadListener(new PullToRefreshLayout.OnLoadListener() {
            //加载更多
            @Override
            public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
                isPullToLoadMore = true;
                switch (currentTransType) {
                    case 0: // 常规交易
                        if (xpadHisTradStatusViewModel != null) {
                            if (mHisTradList.size() < xpadHisTradStatusViewModel.getRecordNumber()) {
                                if (mCurrentSelectedPage == PAGE_INDEX) {
                                    showLoadingDialog("请稍候...");
                                }

                                XpadHisTradStatusViewModel viewModel = new XpadHisTradStatusViewModel();
                                viewModel.setAccountKey(currentQueryAccount.getAccountKey());
                                viewModel.setCurrentIndex(mShowListBean.size() + "");
                                viewModel.setPageSize(PAGE_SIZE);
                                viewModel.set_refresh("false");
                                viewModel.setStartDate(mSelectParams.getStartDate());
                                viewModel.setEndDate(mSelectParams.getEndDate());
                                viewModel.setAccountType("0");
                                viewModel.setXpadProductCurCode("000");
                                wealthHistoryListPresenter.psnXpadHisTradStatus(viewModel);
                                isNeedScrollToTop = false;
                            } else {
                                isPullToLoadMore = false;
                                refreshLayout.loadmoreCompleted(PullToRefreshLayout.NO_MORE_DATA);
                            }
                        } else {
                            // 暂时什么也不做
                        }
                        break;
                    case 1: // 组合交易
                        if (xpadQueryGuarantyProductListViewModel != null) {
                            if (mHisComTradList.size() < xpadQueryGuarantyProductListViewModel.getRecordNumber()) {
                                isPullToLoadMore = true;
                                if (mCurrentSelectedPage == PAGE_INDEX) {
                                    showLoadingDialog("请稍候...");
                                }

                                XpadQueryGuarantyProductListViewModel viewModel = new XpadQueryGuarantyProductListViewModel();
                                viewModel.setCurrentIndex(mShowListBean.size() + "");
                                viewModel.setPageSize(PAGE_SIZE);
                                viewModel.set_refresh("false");
                                viewModel.setAccountKey(currentQueryAccount.getAccountKey());
                                viewModel.setStartDate(mSelectParams.getStartDate());
                                viewModel.setEndDate(mSelectParams.getEndDate());
                                viewModel.setAccountType("0");
                                viewModel.setXpadProductCurCode("000");
                                wealthHistoryListPresenter.psnXpadHisComTradStatus(viewModel);
                                isNeedScrollToTop = false;
                            } else {
                                isPullToLoadMore = false;
                                refreshLayout.loadmoreCompleted(PullToRefreshLayout.NO_MORE_DATA);
                            }
                        } else {
                            // 暂时什么也不做
                            //                            if (mCurrentSelectedPage == PAGE_INDEX) {
                            //                                showLoadingDialog("请稍候...");
                            //                            }
                            //
                            //                            XpadQueryGuarantyProductListViewModel viewModel = new XpadQueryGuarantyProductListViewModel();
                            //                            viewModel.setCurrentIndex("0");
                            //                            viewModel.setPageSize(PAGE_SIZE);
                            //                            viewModel.set_refresh("true");
                            //                            viewModel.setAccountKey(currentQueryAccount.getAccountKey());
                            //                            viewModel.setStartDate(ApplicationContext.getInstance().getCurrentSystemDate().toLocalDate().plusMonths(-MAX_QUERY_RANGE).plusDays(1).format(DateFormatters.dateFormatter1));
                            //                            viewModel.setEndDate(ApplicationContext.getInstance().getCurrentSystemDate().toLocalDate().format(DateFormatters.dateFormatter1));
                            //                            viewModel.setAccountType("0");
                            //                            viewModel.setXpadProductCurCode("000");
                            //                            wealthHistoryListPresenter.psnXpadHisComTradStatus(viewModel);
                        }

                        break;
                }
            }
        });
    }

    // 广播接收者，接收TransInquireFragment发送的广播（共3种通知类型，页面选中通知，筛选条件选择通知，账户选择通知）
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
                case 4: // 4表示“撤单成功”通知
                    handleRepealOrderSuccessNotice(intent);
                    break;
                case 5: // 5表示刷新
                    isNeedScrollToTop = true;

                    if (mCurrentSelectedPage == PAGE_INDEX) {
                        showLoadingDialog("请稍候...");
                    }

                    XpadAccountQueryViewModel viewModel = new XpadAccountQueryViewModel();
                    viewModel.setQueryType("0");
                    viewModel.setXpadAccountSatus("1");
                    wealthHistoryListPresenter.psnXpadAccountQuery(viewModel);
            }
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
        boolean isStartQuery = false;
        if (mChangedQueryAccount != null && !mChangedQueryAccount.getAccountNo().equals(currentQueryAccount.getAccountNo())) {
            isStartQuery = true;
            currentQueryAccount = mChangedQueryAccount;
            mChangedQueryAccount = null;
        }

        if (isStartQuery) {
            isNeedScrollToTop = true;
            startQuery();
        }
    }

    // 处理“撤单成功”通知
    private void handleRepealOrderSuccessNotice(Intent intent) {
        isNeedScrollToTop = true;
        startQuery();
    }

    // 处理“账户选择”通知
    private void handleAccountSelectedNotice(Intent intent) {
        isFisrtTimeEnter = true;
        XpadAccountQueryViewModel.XPadAccountEntity account = intent.getParcelableExtra("account");
        int selectPagePosition = intent.getIntExtra("selectPagePosition", -1);
        if (selectPagePosition == PAGE_INDEX) { // 就是当前页面
            currentQueryAccount = account;
            isNeedScrollToTop = true;
            startQuery();
        } else { // 不是当前页面
            mChangedQueryAccount = account;
        }
    }

    // 处理“筛选条件选择”通知
    private void handleConditionSelectedNotice(Intent intent) {
        SelectParams selectParams = intent.getParcelableExtra("selectParams");
        int selectPagePosition = intent.getIntExtra("selectPagePosition", -1);
        if (selectPagePosition == PAGE_INDEX) { // 就是当前页面
            mSelectParams = selectParams;
            isNeedScrollToTop = true;
            startQuery();
        }
    }

    // 处理有无数据时的情况
    private void handleNoData() {
        // 处理“数据是否为空”的情况
        if (mShowListBean.size() > 0) {
            ll_no_data.setVisibility(View.GONE);
        } else {
            ll_no_data.setVisibility(View.VISIBLE);
            tv_no_history_data.setText(isFisrtTimeEnter ? getResources().getString(R.string.boc_transfer_query_empty) : getResources().getString(R.string.boc_transfer_select_empty));
        }
        isFisrtTimeEnter = false;
    }

    // 开始查询（此方法由TransInquireFragment发起广播触发）
    public void startQuery() {
        if (!mLastTransType.equals(mSelectParams.getTransType())) { // 判断交易类型条件是否改变
            isTransTypeChange = true;
        }
        mLastTransType = mSelectParams.getTransType();
        if ("0".equals(mSelectParams.getTransType())) { // 常规交易
            currentTransType = 0;
            if (currentQueryAccount != null) {
                if (mCurrentSelectedPage == PAGE_INDEX) {
                    showLoadingDialog("请稍候...");
                }
                XpadHisTradStatusViewModel viewModel = new XpadHisTradStatusViewModel();
                viewModel.setAccountKey(currentQueryAccount.getAccountKey());
                viewModel.setCurrentIndex("0");
                viewModel.setPageSize(PAGE_SIZE);
                viewModel.set_refresh("true");
                viewModel.setStartDate(mSelectParams.getStartDate());
                viewModel.setEndDate(mSelectParams.getEndDate());

                viewModel.setAccountType("0");
                viewModel.setXpadProductCurCode(mSelectParams.getCurrency());
                wealthHistoryListPresenter.psnXpadHisTradStatus(viewModel);
            }
        } else { // 组合购买
            if (mCurrentSelectedPage == PAGE_INDEX) {
                showLoadingDialog("请稍候...");
            }
            currentTransType = 1;
            if (currentQueryAccount != null) {
                XpadQueryGuarantyProductListViewModel viewModel = new XpadQueryGuarantyProductListViewModel();
                viewModel.setAccountKey(currentQueryAccount.getAccountKey());
                viewModel.setCurrentIndex("0");
                viewModel.setPageSize(PAGE_SIZE);
                viewModel.set_refresh("true");
                viewModel.setStartDate(mSelectParams.getStartDate());
                viewModel.setEndDate(mSelectParams.getEndDate());
                viewModel.setAccountType("1");
                viewModel.setXpadProductCurCode(mSelectParams.getCurrency());
                wealthHistoryListPresenter.psnXpadHisComTradStatus(viewModel);
            }
        }
    }

    @Override
    protected String getTitleValue() {
        return super.getTitleValue();
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
    protected boolean isHaveTitleBarView() {
        return false;
    }

    @Override
    public void onDestroy() {
        wealthHistoryListPresenter.unsubscribe();
        super.onDestroy();
        mActivity.unregisterReceiver(myReceiver);
    }

    @Override
    public void psnXpadHisTradStatusSuccess(XpadHisTradStatusViewModel viewModel) {
        if (mCurrentSelectedPage == PAGE_INDEX) {
            closeProgressDialog();
        }
        this.xpadHisTradStatusViewModel = viewModel;

        List<XpadHisTradStatusViewModel.XpadHisTradStatusResultEntity> hisTradList = xpadHisTradStatusViewModel.getList();
        //         2016-10-28 zhx 临时 日期校正的逻辑 start
        //                int u = 0;
        //                for (XpadHisTradStatusViewModel.XpadHisTradStatusResultEntity autTradEntitiy : hisTradList) {
        //                    if (u < 4) {
        //                        autTradEntitiy.setPaymentDate(autTradEntitiy.getPaymentDate().plusMonths(u));
        //                    }
        //                    u++;
        //                }
        //         2016-10-28 zhx 临时 日期校正的逻辑 end
        List<ShowListBean> transactionList = new ArrayList<ShowListBean>();
        // ----------start-----------
        if (!isPullToLoadMore) {
            mHisTradList.clear();
        }
        // 如果交易类型改变
        if (isTransTypeChange == true) {
            mHisTradList.clear();
        }

        isPullToLoadMore = false;
        isTransTypeChange = false;

        mHisTradList.addAll(hisTradList);

        // ----------end-----------
        // 进行排序的操作
        Collections.sort(mHisTradList);
        //        Collections.reverse(mHisTradList);

        // 组织界面上的显示
        if (mHisTradList != null) {
            for (int i = 0; i < mHisTradList.size(); i++) {

                XpadHisTradStatusViewModel.XpadHisTradStatusResultEntity hisTradEntitiy = mHisTradList.get(i);
                ShowListBean transactionBean = new ShowListBean();
                transactionBean.setChangeColor(true);

                LocalDate localDate = hisTradEntitiy.getPaymentDate();
                String formatTime = "";// 当前时间 MM月/yyyy
                String tempTime = "";// 上一次时间
                if (localDate != null) {
                    formatTime = localDate.format(DateFormatters.monthFormatter1);
                }
                if (i > 0) {
                    tempTime = mHisTradList.get(i - 1).getPaymentDate().format(DateFormatters.monthFormatter1);
                }

                if (tempTime.equals(formatTime)) {// child
                    ShowListBean item = new ShowListBean();
                    item.type = ShowListBean.CHILD;

                    item.setTitleID(ShowListConst.TITLE_WEALTH);
                    item.setTime(localDate);

                    item.setContentLeftAbove(hisTradEntitiy.getProdName());
                    //交易类型
                    item.setContentLeftBelow(getTranType(hisTradEntitiy.getTrfType()));
                    //交易状态
                    item.setContentRightAbove(getStatuss(hisTradEntitiy.getStatus()));

                    String moneyFormat = MoneyUtils.getLoanAmountShownRMB1(hisTradEntitiy.getAmount(), hisTradEntitiy.getCurrencyCode());
                    String transAmount = MoneyUtils.getLoanAmountShownRMB1(hisTradEntitiy.getTrfAmount(), hisTradEntitiy.getCurrencyCode());
                    String cashRemit = "";
                    if ("001".equals(hisTradEntitiy.getCurrencyCode())) {
                        moneyFormat = MoneyUtils.getLoanAmountShownRMB1(hisTradEntitiy.getAmount(), hisTradEntitiy.getCurrencyCode());
                        cashRemit = " 元";
                    } else {
                        cashRemit = " " + PublicCodeUtils.getCurrency(mActivity, hisTradEntitiy.getCurrencyCode());
                        if ("01".equals(hisTradEntitiy.getCashRemit())) {
                            cashRemit += "/钞";
                        } else if ("02".equals(hisTradEntitiy.getCashRemit())) {
                            cashRemit += "/汇";
                        }
                    }
                    if ("0.00".equals(moneyFormat) && (!"0.00".equals(transAmount))) {
                        item.setContentRightBelow(transAmount + " 份");
                    } else {
                        item.setContentRightBelow(moneyFormat + cashRemit);
                    }


                    transactionList.add(item);
                } else {// group
                    for (int j = 0; j < 2; j++) {
                        ShowListBean itemFirst = new ShowListBean();
                        if (j == 0) {
                            itemFirst.type = ShowListBean.GROUP;
                            itemFirst.setGroupName(formatTime);
                            itemFirst.setTime(localDate);
                        } else {
                            itemFirst.type = ShowListBean.CHILD;

                            itemFirst.setTitleID(ShowListConst.TITLE_WEALTH);
                            itemFirst.setTime(localDate);

                            itemFirst.setContentLeftAbove(hisTradEntitiy.getProdName());
                            //交易类型
                            itemFirst.setContentLeftBelow(getTranType(hisTradEntitiy.getTrfType()));
                            //交易状态
                            itemFirst.setContentRightAbove(getStatuss(hisTradEntitiy.getStatus()));
                            String moneyFormat = MoneyUtils.getLoanAmountShownRMB1(hisTradEntitiy.getAmount(), hisTradEntitiy.getCurrencyCode());
                            String transAmount = MoneyUtils.getLoanAmountShownRMB1(hisTradEntitiy.getTrfAmount(), hisTradEntitiy.getCurrencyCode());
                            String cashRemit = "";
                            if ("001".equals(hisTradEntitiy.getCurrencyCode())) {
                                moneyFormat = MoneyUtils.getLoanAmountShownRMB1(hisTradEntitiy.getAmount(), hisTradEntitiy.getCurrencyCode());
                                //                                moneyFormat = MoneyUtils.getLoanAmountShownRMB1("51000.00");

                                cashRemit = " 元";
                            } else {
                                cashRemit = " " + PublicCodeUtils.getCurrency(mActivity, hisTradEntitiy.getCurrencyCode());
                                if ("01".equals(hisTradEntitiy.getCashRemit())) {
                                    cashRemit += "/钞";
                                } else if ("02".equals(hisTradEntitiy.getCashRemit())) {
                                    cashRemit += "/汇";
                                }
                            }
                            if ("0.00".equals(moneyFormat) && (!"0.00".equals(transAmount))) {
                                itemFirst.setContentRightBelow(transAmount + " 份");
                            } else {
                                itemFirst.setContentRightBelow(moneyFormat + cashRemit);
                            }
                        }
                        transactionList.add(itemFirst);
                    }
                }
            }
        }

        mShowListBean.clear();
        mShowListBean.addAll(transactionList);
        mAdapter.setData(mShowListBean);
        // 回到顶部
        if (mShowListBean.size() > 0 && isNeedScrollToTop == true) {
            pinnedSectionListView.setSelection(0);
        }

        isNeedScrollToTop = false;

        refreshLayout.loadmoreCompleted(PullToRefreshLayout.SUCCEED);
        handleNoData();
    }

    @Override
    public void psnXpadHisTradStatusFail(BiiResultErrorException biiResultErrorException) {
        if (mCurrentSelectedPage == PAGE_INDEX) {
            closeProgressDialog();
        }
    }


    @Override
    public void psnXpadHisComTradStatusSuccess(XpadQueryGuarantyProductListViewModel viewModel) {
        if (mCurrentSelectedPage == PAGE_INDEX) {
            closeProgressDialog();
        }
        xpadQueryGuarantyProductListViewModel = viewModel;

        List<XpadQueryGuarantyProductListViewModel.QueryGuarantyProductListEntity> hisComTradList = xpadQueryGuarantyProductListViewModel.getList();
        List<ShowListBean> transactionList = new ArrayList<ShowListBean>();
        // ----------start-----------
        if (!isPullToLoadMore) { // 表示是加载更多
            mHisComTradList.clear();

        }
        // 如果交易类型改变
        if (isTransTypeChange == true) {
            mHisComTradList.clear();
        }

        isPullToLoadMore = false;
        isTransTypeChange = false;

        mHisComTradList.addAll(hisComTradList);

        // ----------end-----------

        // 进行排序的操作
        Collections.sort(mHisComTradList);
        //        Collections.reverse(mHisComTradList);

        // 组织界面的显示
        for (int i = 0; i < mHisComTradList.size(); i++) {

            XpadQueryGuarantyProductListViewModel.QueryGuarantyProductListEntity autComTradEntity = mHisComTradList.get(i);
            ShowListBean transactionBean = new ShowListBean();
            transactionBean.setChangeColor(true);

            LocalDate localDate = autComTradEntity.getReturnDate();
            String formatTime = "";// 当前时间 MM月/yyyy
            String tempTime = "";// 上一次时间
            if (localDate != null) {
                formatTime = localDate.format(DateFormatters.monthFormatter1);
            }
            if (i > 0) {
                tempTime = mHisComTradList.get(i - 1).getReturnDate().format(DateFormatters.monthFormatter1);
            }

            if (tempTime.equals(formatTime)) {// child
                ShowListBean item = new ShowListBean();
                item.type = ShowListBean.CHILD;

                item.setTitleID(ShowListConst.TITLE_WEALTH);

                item.setTime(localDate);
                item.setContentLeftAbove(autComTradEntity.getProdName());
                // 状态（0：正常 1：解除）
                if ("1".equals(autComTradEntity.getStatus())) {
                    item.setContentRightAbove("解除");
                }

                String moneyFormat = MoneyUtils.getLoanAmountShownRMB1(autComTradEntity.getBuyAmt(), autComTradEntity.getCurrency());
                String cashRemit = "";
                if ("001".equals(autComTradEntity.getCurrency())) {
                    moneyFormat = MoneyUtils.getLoanAmountShownRMB1(autComTradEntity.getBuyAmt(), autComTradEntity.getCurrency());
                    cashRemit = " 元";
                } else {
                    cashRemit = " " + PublicCodeUtils.getCurrency(mActivity, autComTradEntity.getCurrency());
                    if ("1".equals(autComTradEntity.getCashRemit())) {
                        cashRemit = cashRemit + "/钞";
                    } else if ("2".equals(autComTradEntity.getCashRemit())) {
                        cashRemit = cashRemit + "/汇";
                    }
                }
                item.setContentRightBelow(moneyFormat + cashRemit);
                transactionList.add(item);
            } else {// group
                for (int j = 0; j < 2; j++) {
                    ShowListBean itemFirst = new ShowListBean();
                    if (j == 0) {
                        itemFirst.type = ShowListBean.GROUP;
                        itemFirst.setGroupName(formatTime);
                        itemFirst.setTime(localDate);
                    } else {
                        itemFirst.type = ShowListBean.CHILD;

                        itemFirst.setTitleID(ShowListConst.TITLE_WEALTH);
                        itemFirst.setTime(localDate);
                        itemFirst.setContentLeftAbove(autComTradEntity.getProdName());
                        // 状态（0：正常 1：解除）
                        if ("1".equals(autComTradEntity.getStatus())) {
                            itemFirst.setContentRightAbove("解除");
                        }

                        String moneyFormat = MoneyUtils.getLoanAmountShownRMB1(autComTradEntity.getBuyAmt(), autComTradEntity.getCurrency());
                        String cashRemit = "";
                        if ("001".equals(autComTradEntity.getCurrency())) {
                            moneyFormat = MoneyUtils.getLoanAmountShownRMB1(autComTradEntity.getBuyAmt(), autComTradEntity.getCurrency());
                            cashRemit = " 元";
                        } else {
                            cashRemit = " " + PublicCodeUtils.getCurrency(mActivity, autComTradEntity.getCurrency());
                            if ("1".equals(autComTradEntity.getCashRemit())) {
                                cashRemit = cashRemit + "/钞";
                            } else if ("2".equals(autComTradEntity.getCashRemit())) {
                                cashRemit = cashRemit + "/汇";
                            }
                        }
                        itemFirst.setContentRightBelow(moneyFormat + cashRemit);
                    }
                    transactionList.add(itemFirst);
                }
            }
        }

        mShowListBean.clear();
        this.mShowListBean.addAll(transactionList);
        mAdapter.setData(mShowListBean);

        // 回到顶部
        if (mShowListBean.size() > 0 && isNeedScrollToTop == true) {
            pinnedSectionListView.setSelection(0);
        }
        isNeedScrollToTop = false;

        refreshLayout.loadmoreCompleted(PullToRefreshLayout.SUCCEED);
        handleNoData();
    }

    @Override
    public void psnXpadHisComTradStatusFail(BiiResultErrorException biiResultErrorException) {
        if (mCurrentSelectedPage == PAGE_INDEX) {
            closeProgressDialog();
        }
        isPullToLoadMore = false;
        isTransTypeChange = false;

        mShowListBean.clear();
        mAdapter.setData(mShowListBean);
        refreshLayout.loadmoreCompleted(PullToRefreshLayout.SUCCEED);
    }

    //查询客户理财账户
    @Override
    public void psnXpadAccountQuerySuccess(XpadAccountQueryViewModel xpadAccountQueryViewModel) {
        this.xpadAccountQueryViewModel = xpadAccountQueryViewModel;
        mAccountList = xpadAccountQueryViewModel.getList();
        //        XpadHisTradStatusViewModel viewModel = new XpadHisTradStatusViewModel();

        XpadRecentAccountQueryViewModel xpadRecentAccountQueryViewModel = new XpadRecentAccountQueryViewModel();
        wealthHistoryListPresenter.psnXpadRecentAccountQuery(xpadRecentAccountQueryViewModel);

    }

    @Override
    public void psnXpadAccountQueryFail(BiiResultErrorException biiResultErrorException) {
        if (mCurrentSelectedPage == PAGE_INDEX) {
            closeProgressDialog();
        }

    }

    //查询最近操作的账户成功
    @Override
    public void psnXpadRecentAccountQuerySuccess(XpadRecentAccountQueryViewModel xpadRecentAccountQueryViewModel) {
        if (mAccountList != null) {
            for (XpadAccountQueryViewModel.XPadAccountEntity accountEntity : mAccountList) {
                if (accountEntity.getAccountNo().equals(xpadRecentAccountQueryViewModel.getAccountNo())) {
                    currentQueryAccount = accountEntity;
                    recentQueryAccount = accountEntity;
                }
            }
        }

        // 历史常规交易状况查询
        if (currentQueryAccount == null) {
            currentQueryAccount = mAccountList.get(0);
            recentQueryAccount = mAccountList.get(0);
        }

        if (currentQueryAccount != null) {
            XpadHisTradStatusViewModel viewModel = new XpadHisTradStatusViewModel();
            ibknum = currentQueryAccount.getIbkNumber();
            viewModel.setAccountKey(currentQueryAccount.getAccountKey());
            viewModel.setCurrentIndex("0");
            viewModel.setPageSize(PAGE_SIZE);
            viewModel.set_refresh("true");
            viewModel.setStartDate(ApplicationContext.getInstance().getCurrentSystemDate().toLocalDate().plusMonths(-MAX_QUERY_RANGE).plusDays(1).format(DateFormatters.dateFormatter1));
            viewModel.setEndDate(ApplicationContext.getInstance().getCurrentSystemDate().toLocalDate().format(DateFormatters.dateFormatter1));
            viewModel.setAccountType("0");
            viewModel.setXpadProductCurCode("000");
            wealthHistoryListPresenter.psnXpadHisTradStatus(viewModel);
        }
    }

    @Override
    public void psnXpadRecentAccountQueryFail(BiiResultErrorException biiResultErrorException) {
        if (mCurrentSelectedPage == PAGE_INDEX) {
            closeProgressDialog();
        }

    }

    // 交易类型（00：认购 01：申购 02：赎回 03：红利再投 04：红利发放 05：（经过）利息返还 06：本金返还 07：起息前赎回 08：利息折份额 09:赎回亏损 10:赎回盈利 11:产品转让 12:份额转换）
    public String getTranType(String index) {
        String tranType = "";
        if (index.equals("00")) {
            tranType = "认购";
        } else if (index.equals("01")) {
            tranType = "申购";
        } else if (index.equals("02")) {
            tranType = "赎回";
        } else if (index.equals("03")) {
            tranType = "红利再投";
        } else if (index.equals("04")) {
            tranType = "红利发放";
        } else if (index.equals("05")) {
            tranType = "(经过)利息返还";
        } else if (index.equals("06")) {
            tranType = "本金返还";
        } else if (index.equals("07")) {
            tranType = "起息前赎回";
        } else if (index.equals("08")) {
            tranType = "利息折份额";
        } else if (index.equals("09")) {
            tranType = "赎回亏损";
        } else if (index.equals("10")) {
            tranType = "赎回盈利";
        } else if (index.equals("11")) {
            tranType = "产品转让";
        } else if (index.equals("12")) {
            tranType = "份额转换";
        }
        return tranType;
    }

    // 状态（0：委托待处理 1：成功 2：失败 3：已撤销 4：已冲正 5：已赎回）
    public String getStatuss(String string) {
        String statuss = "";
        if (string.equals("0")) {
            statuss = "委托待处理";
        } else if (string.equals("1")) {
            statuss = "";
        } else if (string.equals("2")) {
            statuss = "失败";
        } else if (string.equals("3")) {
            statuss = "已撤销";
        } else if (string.equals("4")) {
            statuss = "已冲正";
        } else if (string.equals("5")) {
            statuss = "已赎回";
        }
        return statuss;
    }

}
