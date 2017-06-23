package com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.transinquire.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
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
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.repealorder.model.XpadAutComTradStatusViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.repealorder.ui.TransComDetailFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.repealorder.ui.TransDetailFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.transinquire.model.SelectParams;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.transinquire.model.XpadAccountQueryViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.transinquire.model.XpadAutTradStatusViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.transinquire.model.XpadRecentAccountQueryViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.transinquire.presenter.EntrustTransPresenter;
import com.boc.bocsoft.mobile.common.utils.date.DateFormatters;

import org.threeten.bp.LocalDate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Fragment：中银理财-交易查询-委托交易页面(带悬浮效果)
 * Created by zhx on 2016/9/7
 */
public class EntrustTransFragment1 extends BussFragment implements EntrustTransContact.View {
    public static final String PAGE_SIZE = String.valueOf(ApplicationConst.WEALTH_PAGE_SIZE); // 每页的条目数
    private static final int PAGE_INDEX = 1; // 页面索引
    private View rootView;
    // 上拉刷新
    private PullToRefreshLayout refreshLayout;
    // 查询列表组件
    private PinnedSectionListView transactionView;
    // 查询列表组件的数据集合(界面显示的集合)
    private List<ShowListBean> mTransactionList;

    private EntrustTransPresenter entrustTransPresenter;
    private XpadAccountQueryViewModel xpadAccountQueryViewModel;
    private XpadAutTradStatusViewModel xpadAutTradStatusViewModel;
    private XpadAutComTradStatusViewModel xpadAutComTradStatusViewModel;

    private XpadAccountQueryViewModel.XPadAccountEntity mRecentQueryAccount; // 最近查询账户
    private XpadAccountQueryViewModel.XPadAccountEntity mCurrentQueryAccount; // 当前查询的账户（列表中的数据所对应的账户）
    private XpadAccountQueryViewModel.XPadAccountEntity mChangedQueryAccount; // 改变的查询账户

    private List<XpadAccountQueryViewModel.XPadAccountEntity> mAccountList; // 理财账户列表
    List<XpadAutTradStatusViewModel.AutTradEntitiy> mAutTradList = new ArrayList<XpadAutTradStatusViewModel.AutTradEntitiy>(); // 常规交易数据集合
    List<XpadAutComTradStatusViewModel.AutComTradEntity> mAutComTradList = new ArrayList<XpadAutComTradStatusViewModel.AutComTradEntity>(); // 组合交易数据集合
    private boolean isPullToLoadMore; // 是否是上拉加载更多
    private boolean isTransTypeChange = false; // 交易类型是否改变

    private int currentTransType = 0; // 当前交易类型(0表示常规，1表示组合)
    private String mLastTransType = "0"; // 上次查询时的交易类型

    private SelectParams mSelectParams; // 当前选择条件
    private MyReceiver myReceiver; // 广播接收者
    private int mCurrentSelectedPage = 0;
    private TextView no_data_transfer_query; // 筛选后如果无数据，提示信息TextView
    private LinearLayout ll_no_data_query;//筛选后无结果数据
    private boolean isFisrtTimeEnter = true; // 是否首次进入当前页面的标志
    private ShowListAdapter mAdapter;

    private boolean isNeedScrollToTop = false;

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        rootView = mInflater.inflate(R.layout.fragment_entrust_trans1, null);
        return rootView;
    }

    @Override
    public void initView() {
        no_data_transfer_query = (TextView) rootView.findViewById(R.id.no_data_transfer_query);
        ll_no_data_query = (LinearLayout) rootView.findViewById(R.id.no_data_query);
        refreshLayout = (PullToRefreshLayout) rootView.findViewById(R.id.refresh_transfer_query);
        transactionView = (PinnedSectionListView) rootView.findViewById(R.id.lv_transfer_query);
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
    public void initData() {

        initSelectParams();
        mTransactionList = new ArrayList<ShowListBean>();
        mAdapter = new ShowListAdapter(mContext, -1);
        transactionView.saveAdapter(mAdapter);
        transactionView.setShadowVisible(false);
        transactionView.setAdapter(mAdapter);

        // 请求所有账户
        if (mCurrentSelectedPage == PAGE_INDEX) {
            showLoadingDialog("加载中...");
        }
        XpadAccountQueryViewModel viewModel = new XpadAccountQueryViewModel();
        viewModel.setQueryType("0");
        viewModel.setXpadAccountSatus("1");
        entrustTransPresenter = new EntrustTransPresenter(this);
        entrustTransPresenter.psnXpadAccountQuery(viewModel);
    }

    @Override
    public void setListener() {
        IntentFilter intentFilter = new IntentFilter("startQuery");
        myReceiver = new MyReceiver();
        mActivity.registerReceiver(myReceiver, intentFilter);

        transactionView.setListener(new PinnedSectionListView.ClickListener() {
            @Override
            public void onItemClickListener(int position) {
                switch (currentTransType) {
                    case 0: // 常规交易
                        XpadAutTradStatusViewModel.AutTradEntitiy autTradEntitiy = mAutTradList.get(position);
                        TransDetailFragment transDetailFragment = new TransDetailFragment();
                        Bundle bundle = new Bundle();
                        bundle.putParcelable("autTradEntitiy", autTradEntitiy);
                        bundle.putParcelable("currentQueryAccount", mCurrentQueryAccount);
                        bundle.putInt("selectPagePosition", PAGE_INDEX);
                        bundle.putString("noticeType", "4"); // 区分页面源（是来自TransInquireFragment还是RepealOrderFragment）
                        transDetailFragment.setArguments(bundle);
                        start(transDetailFragment);
                        break;
                    case 1: // 组合交易
                        XpadAutComTradStatusViewModel.AutComTradEntity autComTradEntity = mAutComTradList.get(position);
                        TransComDetailFragment transComDetailFragment = new TransComDetailFragment();
                        Bundle bundle1 = new Bundle();
                        bundle1.putParcelable("autComTradEntity", autComTradEntity);
                        bundle1.putParcelable("currentQueryAccount", mCurrentQueryAccount);
                        bundle1.putInt("selectPagePosition", PAGE_INDEX);
                        bundle1.putString("noticeType", "4"); // 区分页面源
                        transComDetailFragment.setArguments(bundle1);
                        start(transComDetailFragment);
                        break;
                }
            }
        });

        refreshLayout.setOnLoadListener(new PullToRefreshLayout.OnLoadListener() {
            @Override
            public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
                isPullToLoadMore = true;
                switch (currentTransType) {
                    case 0: // 常规交易
                        if (xpadAutTradStatusViewModel != null) {
                            if (mAutTradList.size() < xpadAutTradStatusViewModel.getRecordNumber()) {
                                if (mCurrentSelectedPage == PAGE_INDEX) {
                                    showLoadingDialog("加载中...");
                                }

                                XpadAutTradStatusViewModel viewModel = new XpadAutTradStatusViewModel();
                                viewModel.setAccountKey(mCurrentQueryAccount.getAccountKey());
                                viewModel.setCurrentIndex(mTransactionList.size() + "");
                                viewModel.setPageSize(PAGE_SIZE);

                                entrustTransPresenter.psnXpadAutTradStatus(viewModel);
                                isNeedScrollToTop = false;

                            } else {
                                isPullToLoadMore = false;
                                refreshLayout.loadmoreCompleted(PullToRefreshLayout.NO_MORE_DATA);
                            }
                        } else { // xpadAutTradStatusViewModel为空的情况
/*                            if (mCurrentSelectedPage == PAGE_INDEX) {
                                showLoadingDialog("加载中...");
                            }

                            XpadAutTradStatusViewModel viewModel = new XpadAutTradStatusViewModel();
                            viewModel.setAccountKey(mCurrentQueryAccount.getAccountKey());
                            viewModel.setCurrentIndex("0");
                            viewModel.setPageSize(PAGE_SIZE);

                            entrustTransPresenter.psnXpadAutTradStatus(viewModel);*/
                        }
                        break;
                    case 1: // 组合交易
                        if (xpadAutComTradStatusViewModel != null) {

                            if (mAutComTradList.size() < xpadAutComTradStatusViewModel.getRecordNumber()) {
                                isPullToLoadMore = true;
                                if (mCurrentSelectedPage == PAGE_INDEX) {
                                    showLoadingDialog("加载中...");
                                }
                                XpadAutComTradStatusViewModel viewModel = new XpadAutComTradStatusViewModel();
                                viewModel.setAccountKey(mCurrentQueryAccount.getAccountKey());
                                viewModel.setStartDate(mSelectParams.getStartDate());
                                viewModel.setEndDate(mSelectParams.getEndDate());

                                entrustTransPresenter.psnXpadAutComTradStatus(viewModel);
                                isNeedScrollToTop = false;

                            } else {
                                isPullToLoadMore = false;
                                refreshLayout.loadmoreCompleted(PullToRefreshLayout.NO_MORE_DATA);
                            }
                        } else { // xpadAutComTradStatusViewModel为空的情况
/*                            if (mCurrentSelectedPage == PAGE_INDEX) {
                                showLoadingDialog("加载中...");
                            }
                            XpadAutComTradStatusViewModel viewModel = new XpadAutComTradStatusViewModel();
                            viewModel.setAccountKey(mCurrentQueryAccount.getAccountKey());
                            viewModel.setStartDate(mSelectParams.getStartDate());
                            viewModel.setEndDate(mSelectParams.getEndDate());

                            entrustTransPresenter.psnXpadAutComTradStatus(viewModel);*/
                        }
                        break;
                }
            }
        });
    }

    // 广播接收者，接收TransInquireFragment发送的广播（共3种通知类型，页面选中通知，筛选条件选择通知，账户选择通知，这3种广播都有可能导致重新查询）
    // 广播接收者，接收交易详情页面发送的广播（共1种通知类型，撤单成功通知，会导致重新查询）
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
                case 5: // 5表示“刷新”通知
                    isNeedScrollToTop = true;
                    if (mCurrentSelectedPage == PAGE_INDEX) {
                        showLoadingDialog("加载中...");
                    }
                    XpadAccountQueryViewModel viewModel = new XpadAccountQueryViewModel();
                    viewModel.setQueryType("0");
                    viewModel.setXpadAccountSatus("1");
                    entrustTransPresenter.psnXpadAccountQuery(viewModel);
                    break;
            }
        }
    }

    // 处理“撤单成功”通知
    private void handleRepealOrderSuccessNotice(Intent intent) {
        isNeedScrollToTop = true;
        startQuery();
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
        if (selectPagePosition == PAGE_INDEX) { // 就是当前页面
            isNeedScrollToTop = true;
            mSelectParams = selectParams;
            startQuery();
        }
    }

    // 处理“账户选择”通知
    private void handleAccountSelectedNotice(Intent intent) {
        isFisrtTimeEnter = true;
        // 如果广播源不是来自TransInquireFragment，直接返回
        String broadcastSource = intent.getStringExtra("BroadcastSource");
        if (!"TransInquireFragment".equals(broadcastSource)) {
            return;
        }

        XpadAccountQueryViewModel.XPadAccountEntity account = intent.getParcelableExtra("account");
        int selectPagePosition = intent.getIntExtra("selectPagePosition", -1);
        mCurrentSelectedPage = selectPagePosition;
        if (selectPagePosition == PAGE_INDEX) { // 就是当前页面
            isNeedScrollToTop = true;
            mCurrentQueryAccount = account;
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
        boolean isStartQuery = false;

        if (mChangedQueryAccount != null && !mChangedQueryAccount.getAccountNo().equals(mCurrentQueryAccount.getAccountNo())) {
            isStartQuery = true;
            mCurrentQueryAccount = mChangedQueryAccount;
            mChangedQueryAccount = null;
        }

        if (isStartQuery) {
            isNeedScrollToTop = true;
            startQuery();
        }
    }

    // 处理有无数据时的情况
    private void handleNoData() {
        Log.e("ljljlj", "EntrustTransFragment handleNoData()");
        // 处理“数据是否为空”的情况
        if (mTransactionList.size() > 0) {
            ll_no_data_query.setVisibility(View.GONE);
        } else {
            ll_no_data_query.setVisibility(View.VISIBLE);
            no_data_transfer_query.setText(isFisrtTimeEnter ? getResources().getString(R.string.boc_transfer_query_empty): getResources().getString(R.string.boc_transfer_select_empty));
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
            if (mCurrentQueryAccount != null) {
                if (mCurrentSelectedPage == PAGE_INDEX) {
                    showLoadingDialog("加载中...");
                }
                XpadAutTradStatusViewModel viewModel = new XpadAutTradStatusViewModel();
                viewModel.setAccountKey(mCurrentQueryAccount.getAccountKey());
                viewModel.setCurrentIndex("0");
                viewModel.setPageSize(PAGE_SIZE);
                entrustTransPresenter.psnXpadAutTradStatus(viewModel);
            }
        } else { // 组合购买
            currentTransType = 1;
            if (mCurrentQueryAccount != null) {
                if (mCurrentSelectedPage == PAGE_INDEX) {
                    showLoadingDialog("加载中...");
                }
                XpadAutComTradStatusViewModel viewModel = new XpadAutComTradStatusViewModel();
                viewModel.setAccountKey(mCurrentQueryAccount.getAccountKey());
                viewModel.setStartDate(mSelectParams.getStartDate());
                viewModel.setEndDate(mSelectParams.getEndDate());

                entrustTransPresenter.psnXpadAutComTradStatus(viewModel);
            }
        }
    }

    @Override
    protected boolean isHaveTitleBarView() {
        return false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mActivity.unregisterReceiver(myReceiver);
    }

    @Override
    public void psnXpadAutTradStatusSuccess(XpadAutTradStatusViewModel viewModel) {
        if (mCurrentSelectedPage == PAGE_INDEX) {
            closeProgressDialog();
        }
        this.xpadAutTradStatusViewModel = viewModel;

        List<XpadAutTradStatusViewModel.AutTradEntitiy> autTradList = xpadAutTradStatusViewModel.getAutTradList();
        // 2016-10-28 zhx 临时 日期校正的逻辑 start
//                int u = 0;
//                for (XpadAutTradStatusViewModel.AutTradEntitiy autTradEntitiy : autTradList) {
//                    if(u == 1) {
//                        autTradEntitiy.setFutureDate(autTradEntitiy.getFutureDate().plusDays(u));
//                    }
//                    if(u == 2) {
//                        autTradEntitiy.setFutureDate(autTradEntitiy.getFutureDate().plusMonths(1));
//                    }
//                    if(u == 3) {
//                        autTradEntitiy.setFutureDate(autTradEntitiy.getFutureDate().plusYears(-1));
//                    }
//                    u++;
//                }
        // 2016-10-28 zhx 临时 日期校正的逻辑 end

        List<ShowListBean> transactionList = new ArrayList<ShowListBean>();

        // ----------start-----------
        if (!isPullToLoadMore) { // 如果不是上拉加载更多
            mAutTradList.clear();
        }

        if (isTransTypeChange) { // 如果交易类型改变
            mAutTradList.clear();
        }

        isPullToLoadMore = false;
        isTransTypeChange = false;
        mAutTradList.addAll(autTradList);
        // ----------end-----------

        // 此处进行排序的操作
        Collections.sort(mAutTradList);
//        Collections.reverse(mAutTradList);

        // 此处组织界面显示的数据
        for (int i = 0; i < mAutTradList.size(); i++) {
            LocalDate localDate = mAutTradList.get(i).getFutureDate();
            String formatTime = "";// 当前时间 MM月/yyyy
            String tempTime = "";// 上一次时间
            if (localDate != null) {
                formatTime = localDate.format(DateFormatters.monthFormatter1);
            }
            if (i > 0) {
                tempTime = mAutTradList.get(i - 1).getFutureDate().format(DateFormatters.monthFormatter1);
            }
            if (tempTime.equals(formatTime)) { // child
                ShowListBean item = new ShowListBean();
                item.type = ShowListBean.CHILD;

                item.setChangeColor(true);
                item.setTitleID(ShowListConst.TITLE_WEALTH);
                item.setTime(localDate);
                item.setContentLeftAbove(mAutTradList.get(i).getProdName());
                item.setContentLeftBelow(PublicCodeUtils.getTransferType(mActivity, mAutTradList.get(i).getTrfType())); // 交易类型
                if ("0".equals(mAutTradList.get(i).getStatus()) && "0".equals(mAutTradList.get(i).getCanBeCanceled())) {
                    item.setContentRightAbove("");
                } else {
                    item.setContentRightAbove("不可撤单");
                }

                String moneyFormat = MoneyUtils.getLoanAmountShownRMB1(mAutTradList.get(i).getAmount(), mAutTradList.get(i).getCurrencyCode());
                String transAmout = MoneyUtils.getLoanAmountShownRMB1(mAutTradList.get(i).getTrfAmount(), mAutTradList.get(i).getCurrencyCode());
                String cashRemit = "";
                if ("001".equals(mAutTradList.get(i).getCurrencyCode())) {
                    //                    moneyFormat = MoneyUtils.getLoanAmountShownRMB1("30000");
                    cashRemit = " 元";
                } else {
                    cashRemit = " " + PublicCodeUtils.getCurrency(mActivity, mAutTradList.get(i).getCurrencyCode());
                    if ("01".equals(mAutTradList.get(i).getCashRemit())) {
                        cashRemit += "/钞";
                    } else if ("02".equals(mAutTradList.get(i).getCashRemit())) {
                        cashRemit += "/汇";
                    }
                }
                if ("0.00".equals(moneyFormat) && (!"0.00".equals(transAmout))||"0".equals(moneyFormat) && (!"0".equals(transAmout))) {
                    item.setContentRightBelow(transAmout + " 份");
                } else {
                    item.setContentRightBelow(moneyFormat + cashRemit);
                }

                //                item.setContentRightBelow(moneyFormat + cashRemit);

                transactionList.add(item);
            } else { // group
                for (int j = 0; j < 2; j++) {
                    ShowListBean itemFirst = new ShowListBean();
                    if (j == 0) {
                        itemFirst.type = ShowListBean.GROUP;
                        itemFirst.setGroupName(formatTime);
                        itemFirst.setTime(localDate);
                    } else {
                        itemFirst.type = ShowListBean.CHILD;

                        itemFirst.setChangeColor(true);
                        itemFirst.setTitleID(ShowListConst.TITLE_WEALTH);
                        itemFirst.setTime(localDate);
                        itemFirst.setContentLeftAbove(mAutTradList.get(i).getProdName());
                        itemFirst.setContentLeftBelow(PublicCodeUtils.getTransferType(mActivity, mAutTradList.get(i).getTrfType())); // 交易类型
                        if ("0".equals(mAutTradList.get(i).getStatus()) && "0".equals(mAutTradList.get(i).getCanBeCanceled())) {
                            itemFirst.setContentRightAbove("");
                        } else {
                            itemFirst.setContentRightAbove("不可撤单");
                        }

                        String moneyFormat = MoneyUtils.getLoanAmountShownRMB1(mAutTradList.get(i).getAmount(), mAutTradList.get(i).getCurrencyCode());
                        String transAmout = MoneyUtils.getLoanAmountShownRMB1(mAutTradList.get(i).getTrfAmount(), mAutTradList.get(i).getCurrencyCode());
                        String cashRemit = "";
                        if ("001".equals(mAutTradList.get(i).getCurrencyCode())) {
                            //                            moneyFormat = MoneyUtils.getLoanAmountShownRMB1("30000");
                            cashRemit = " 元";
                        } else {
                            cashRemit = " " + PublicCodeUtils.getCurrency(mActivity, mAutTradList.get(i).getCurrencyCode());
                            if ("01".equals(mAutTradList.get(i).getCashRemit())) {
                                cashRemit += "/钞";
                            } else if ("02".equals(mAutTradList.get(i).getCashRemit())) {
                                cashRemit += "/汇";
                            }
                        }
                        if ("0.00".equals(moneyFormat) && (!"0.00".equals(transAmout))) {
                            itemFirst.setContentRightBelow(transAmout + " 份");
                        } else {
                            itemFirst.setContentRightBelow(moneyFormat + cashRemit);
                        }

                        //                        itemFirst.setContentRightBelow(moneyFormat + cashRemit);

                    }
                    transactionList.add(itemFirst);
                }
            }
        }


        mTransactionList.clear();
        mTransactionList.addAll(transactionList);

        mAdapter.setData(mTransactionList);
        refreshLayout.loadmoreCompleted(PullToRefreshLayout.SUCCEED);

        // 回到顶部
        if(mTransactionList.size() > 0 && isNeedScrollToTop == true) {
            transactionView.setSelection(0);
        }
        isNeedScrollToTop = false;
        handleNoData();
    }

    // 失败回调：委托常规交易状况查询
    @Override
    public void psnXpadAutTradStatusFail(BiiResultErrorException biiResultErrorException) {
        if (mCurrentSelectedPage == PAGE_INDEX) {
            closeProgressDialog();
        }
    }

    // 成功回调：委托组合交易状况查询
    @Override
    public void psnXpadAutComTradStatusSuccess(XpadAutComTradStatusViewModel viewModel) {
        if (mCurrentSelectedPage == PAGE_INDEX) {
            closeProgressDialog();
        }
        xpadAutComTradStatusViewModel = viewModel;
        List<XpadAutComTradStatusViewModel.AutComTradEntity> listBean = xpadAutComTradStatusViewModel.getList();
        List<ShowListBean> transactionList = new ArrayList<ShowListBean>();

        // ----------start-----------
        if (!isPullToLoadMore) {// 如果不是上拉加载
            mAutComTradList.clear();
        }
        // 如果交易类型改变
        if (isTransTypeChange == true) {
            mAutComTradList.clear();
        }

        isPullToLoadMore = false;
        isTransTypeChange = false;

        mAutComTradList.addAll(listBean);

        // 日期的校正 2016-11-01 start
        //        int u = 0;
        //        for (XpadAutComTradStatusViewModel.AutComTradEntity entity :  mAutComTradList) {
        //            u++;
        //            entity.setReturnDate(entity.getReturnDate().plusMonths(u));
        //        }
        // 日期的校正 2016-11-01 end
        // ----------end-----------
        Collections.sort(mAutComTradList);
//        Collections.reverse(mAutComTradList);

        for (int i = 0; i < mAutComTradList.size(); i++) {
            LocalDate localDate = mAutComTradList.get(i).getReturnDate();
            String formatTime = "";// 当前时间 MM月/yyyy
            String tempTime = "";// 上一次时间
            if (localDate != null) {
                formatTime = localDate.format(DateFormatters.monthFormatter1);
            }
            if (i > 0) {
                tempTime = mAutComTradList.get(i - 1).getReturnDate().format(DateFormatters.monthFormatter1);
            }
            if (tempTime.equals(formatTime)) { // child
                ShowListBean item = new ShowListBean();
                item.type = ShowListBean.CHILD;

                item.setChangeColor(true);
                item.setTitleID(ShowListConst.TITLE_WEALTH);
                item.setContentLeftAbove(mAutComTradList.get(i).getProdName());

                // 状态（0：正常 1：解除）
//                String status = "";
//                if (mAutComTradList.get(i).getStatus().equals("0")) {
//                    status = "正常";
//                } else if (mAutComTradList.get(i).getStatus().equals("1")) {
//                    status = "解除";
//                }
//                item.setContentLeftBelow(status);

                if ("0".equals(mAutComTradList.get(i).getStatus()) && "0".equals(mAutComTradList.get(i).getCanBeCanceled())) {
                    item.setContentRightAbove("");
                } else {
                    item.setContentRightAbove("不可撤单");
                }
                String moneyFormat = MoneyUtils.getLoanAmountShownRMB1(mAutComTradList.get(i).getBuyAmt(), mAutComTradList.get(i).getCurrency());
                String cashRemit = "";
                if ("001".equals(mAutComTradList.get(i).getCurrency())) {
                    //                    moneyFormat = MoneyUtils.getLoanAmountShownRMB1("25700");
                    cashRemit = " 元";
                } else {
                    cashRemit = " " + PublicCodeUtils.getCurrency(mActivity, mAutComTradList.get(i).getCurrency());
                    if ("1".equals(mAutComTradList.get(i).getCashRemit())) {
                        cashRemit += "/钞";
                    } else if ("2".equals(mAutComTradList.get(i).getCashRemit())) {
                        cashRemit += "/汇";
                    }
                }
                item.setContentRightBelow(moneyFormat + cashRemit);

                item.setTime(mAutComTradList.get(i).getReturnDate());

                transactionList.add(item);
            } else { // group
                for (int j = 0; j < 2; j++) {
                    ShowListBean itemFirst = new ShowListBean();
                    if (j == 0) {
                        itemFirst.type = ShowListBean.GROUP;
                        itemFirst.setGroupName(formatTime);
                        itemFirst.setTime(localDate);
                    } else {
                        itemFirst.type = ShowListBean.CHILD;

                        itemFirst.setChangeColor(true);
                        itemFirst.setTitleID(ShowListConst.TITLE_WEALTH);
                        itemFirst.setContentLeftAbove(mAutComTradList.get(i).getProdName());
                        // 状态（0：正常 1：解除）
//                        String status = "";
//                        if (mAutComTradList.get(i).getStatus().equals("0")) {
//                            status = "正常";
//                        } else if (mAutComTradList.get(i).getStatus().equals("1")) {
//                            status = "解除";
//                        }
//                        itemFirst.setContentLeftBelow(status);

                        if ("0".equals(mAutComTradList.get(i).getStatus()) && "0".equals(mAutComTradList.get(i).getCanBeCanceled())) {
                            itemFirst.setContentRightAbove("");
                        } else {
                            itemFirst.setContentRightAbove("不可撤单");
                        }
                        String moneyFormat = MoneyUtils.getLoanAmountShownRMB1(mAutComTradList.get(i).getBuyAmt(), mAutComTradList.get(i).getCurrency());
                        String cashRemit = "";
                        if ("001".equals(mAutComTradList.get(i).getCurrency())) {
                            //                            moneyFormat = MoneyUtils.getLoanAmountShownRMB1("25700");
                            cashRemit = " 元";
                        } else {
                            cashRemit = " " + PublicCodeUtils.getCurrency(mActivity, mAutComTradList.get(i).getCurrency());
                            if ("1".equals(mAutComTradList.get(i).getCashRemit())) {
                                cashRemit += "/钞";
                            } else if ("2".equals(mAutComTradList.get(i).getCashRemit())) {
                                cashRemit += "/汇";
                            }
                        }
                        itemFirst.setContentRightBelow(moneyFormat + cashRemit);

                        itemFirst.setTime(mAutComTradList.get(i).getReturnDate());


                    }
                    transactionList.add(itemFirst);
                }
            }
        }

        mTransactionList.clear();
        mTransactionList.addAll(transactionList);

        mAdapter.setData(mTransactionList);
        refreshLayout.loadmoreCompleted(PullToRefreshLayout.SUCCEED);
        // 回到顶部
        if(mTransactionList.size() > 0 && isNeedScrollToTop == true) {
            transactionView.setSelection(0);
        }
        isNeedScrollToTop = false;
        handleNoData();

    }

    // 失败回调：委托组合交易状况查询
    @Override
    public void psnXpadAutComTradStatusFail(BiiResultErrorException biiResultErrorException) {
        if (mCurrentSelectedPage == PAGE_INDEX) {
            closeProgressDialog();
        }
        isPullToLoadMore = false;
        isTransTypeChange = false;

        mTransactionList.clear();
        refreshLayout.loadmoreCompleted(PullToRefreshLayout.SUCCEED);
    }

    // 成功回调：查询客户理财账户信息
    @Override
    public void psnXpadAccountQuerySuccess(XpadAccountQueryViewModel xpadAccountQueryViewModel) {
        this.xpadAccountQueryViewModel = xpadAccountQueryViewModel;
        mAccountList = xpadAccountQueryViewModel.getList();

        // 查询最近账户
        XpadRecentAccountQueryViewModel xpadRecentAccountQueryViewModel = new XpadRecentAccountQueryViewModel();
        entrustTransPresenter.psnXpadRecentAccountQuery(xpadRecentAccountQueryViewModel);
    }

    // 失败回调：查询客户理财账户信息
    @Override
    public void psnXpadAccountQueryFail(BiiResultErrorException biiResultErrorException) {
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

        // 委托常规交易状况查询
        if (mCurrentQueryAccount != null) {
            XpadAutTradStatusViewModel viewModel = new XpadAutTradStatusViewModel();
            viewModel.setAccountKey(mCurrentQueryAccount.getAccountKey());
            viewModel.setCurrentIndex("0");
            viewModel.setPageSize(PAGE_SIZE);
            entrustTransPresenter.psnXpadAutTradStatus(viewModel);
        }
    }

    // 失败回调:查询最近账户
    @Override
    public void psnXpadRecentAccountQueryFail(BiiResultErrorException biiResultErrorException) {
        if (mCurrentSelectedPage == PAGE_INDEX) {
            closeProgressDialog();
        }
    }

    @Override
    public void setPresenter(EntrustTransContact.Presenter presenter) {
    }
}
