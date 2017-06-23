package com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.repealorder.ui;

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
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.repealorder.presenter.TransNormalPresenter;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.transinquire.model.SelectParams;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.transinquire.model.XpadAccountQueryViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.transinquire.model.XpadAutTradStatusViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.transinquire.model.XpadRecentAccountQueryViewModel;
import com.boc.bocsoft.mobile.common.utils.date.DateFormatters;

import org.threeten.bp.LocalDate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Fragment：中银理财-撤单-常规交易
 * Created by zhx on 2016/9/19
 */
public class TransNormalFragment1 extends BussFragment implements TransNormalContact.View {
    public static final String PAGE_SIZE = String.valueOf(ApplicationConst.WEALTH_PAGE_SIZE);
    private static final int PAGE_INDEX = 0; // 页面索引
    private View rootView;
    // 上拉刷新
    private PullToRefreshLayout refreshLayout;
    // 查询列表组件
    private PinnedSectionListView transactionView;
    private boolean isPullToLoadMore; // 是否是上拉加载更多
    private SelectParams mSelectParams;
    private TransNormalPresenter transNormalPresenter;
    // 查询列表组件的数据集合
    private List<ShowListBean> mTransactionList;
    private XpadAccountQueryViewModel xpadAccountQueryViewModel;
    private List<XpadAccountQueryViewModel.XPadAccountEntity> mAccountList;
    private XpadAccountQueryViewModel.XPadAccountEntity mRecentQueryAccount; // 最近查询账户
    private XpadAccountQueryViewModel.XPadAccountEntity mCurrentQueryAccount; // 当前查询的账户（列表中的数据所对应的账户）
    private XpadAccountQueryViewModel.XPadAccountEntity mChangedQueryAccount; // 从账户列表中选择后改变的账户
    private XpadAutTradStatusViewModel xpadAutTradStatusViewModel;
    private List<XpadAutTradStatusViewModel.AutTradEntitiy> mAutTradList = new ArrayList<XpadAutTradStatusViewModel.AutTradEntitiy>(); // 常规交易数据集合
    private List<XpadAutTradStatusViewModel.AutTradEntitiy> mAutTradList2 = new ArrayList<XpadAutTradStatusViewModel.AutTradEntitiy>(); // 常规交易数据集合
    private MyReceiver myReceiver;
    private SelectParams mChangedSelectParams;
    private TextView no_data_transfer_query;//查询无数据
    private LinearLayout ll_no_data_query;
    private boolean isFisrtTimeEnter = true; // 是否首次进入
    private boolean isNeedScrollToTop = false;

    private int mCurrentSelectedPage = 0;
    private ShowListAdapter mAdapter;

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        rootView = mInflater.inflate(R.layout.fragment_trans_normal1, null);
        Log.e("LJLJLJ", "TransNormalFragment1 --------------------111111111111111111111");
        return rootView;
    }

    @Override
    public void initView() {
        no_data_transfer_query = (TextView) rootView.findViewById(R.id.no_data_transfer_query);
        ll_no_data_query = (LinearLayout) rootView.findViewById(R.id.no_data_query);
        refreshLayout = (PullToRefreshLayout) rootView.findViewById(R.id.refresh_transfer_query);
        transactionView = (PinnedSectionListView) rootView.findViewById(R.id.lv_transfer_query);
    }

    @Override
    public void initData() {
        initSelectParams();

        mTransactionList = new ArrayList<ShowListBean>();
        mAdapter = new ShowListAdapter(mContext, -1);
        transactionView.saveAdapter(mAdapter);
        transactionView.setShadowVisible(false);
        transactionView.setAdapter(mAdapter);

        if (mCurrentSelectedPage == PAGE_INDEX) {
            showLoadingDialog("加载中...");
        }
        // 请求所有账户
        transNormalPresenter = new TransNormalPresenter(this);
        XpadAccountQueryViewModel xpadAccountQueryViewModel = new XpadAccountQueryViewModel();
        xpadAccountQueryViewModel.setQueryType("0");
        xpadAccountQueryViewModel.setXpadAccountSatus("1");
        transNormalPresenter.psnXpadAccountQuery(xpadAccountQueryViewModel);
    }

    // 初始化选择的参数
    private void initSelectParams() {
        mSelectParams = new SelectParams();
        mSelectParams.setStartDate(ApplicationContext.getInstance().getCurrentSystemDate().toLocalDate().minusMonths(3).plusDays(1).format(
                DateFormatters.dateFormatter1));
        mSelectParams.setEndDate(ApplicationContext.getInstance().getCurrentSystemDate().toLocalDate().format(DateFormatters.dateFormatter1));
    }

    @Override
    public void setListener() {
        IntentFilter intentFilter = new IntentFilter("startQuery");
        myReceiver = new MyReceiver();
        mActivity.registerReceiver(myReceiver, intentFilter);

        transactionView.setListener(new PinnedSectionListView.ClickListener() {
            @Override
            public void onItemClickListener(int position) {
                XpadAutTradStatusViewModel.AutTradEntitiy autTradEntitiy = mAutTradList2.get(position);
                TransDetailFragment transDetailFragment = new TransDetailFragment();
                Bundle bundle = new Bundle();
                bundle.putParcelable("autTradEntitiy", autTradEntitiy);
                bundle.putParcelable("currentQueryAccount", mCurrentQueryAccount);
                bundle.putInt("selectPagePosition", PAGE_INDEX);
                bundle.putString("noticeType", "3"); // 区分页面源
                transDetailFragment.setArguments(bundle);
                start(transDetailFragment);
            }
        });

        refreshLayout.setOnLoadListener(new PullToRefreshLayout.OnLoadListener() {
            @Override
            public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
                // 判断mCurrentQueryAccount是否为空，如果为null，那么什么也不做
                if (mCurrentQueryAccount == null) {
                    return;
                }

                XpadAutTradStatusViewModel viewModel = new XpadAutTradStatusViewModel();
                viewModel.setAccountKey(mCurrentQueryAccount.getAccountKey()); // 此处会崩溃，注意
                viewModel.setCurrentIndex(mTransactionList.size() + "");
                viewModel.setPageSize(PAGE_SIZE);

                isPullToLoadMore = true;

                if (xpadAutTradStatusViewModel != null) {
                    if (mAutTradList.size() < xpadAutTradStatusViewModel.getRecordNumber()) {
                        if (mCurrentSelectedPage == PAGE_INDEX) {
                            showLoadingDialog("加载中...");
                        }
                        transNormalPresenter.psnXpadAutTradStatus(viewModel);
                        isNeedScrollToTop = false;
                    } else {
                        isPullToLoadMore = false;
                        refreshLayout.loadmoreCompleted(PullToRefreshLayout.NO_MORE_DATA);
                    }
                } else {
                    // 暂时啥也不做
                }
            }
        });
    }

    @Override
    protected boolean isHaveTitleBarView() {
        return false;
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.e("ljljlj", "TransNormalFragment onPause()");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("ljljlj", "TransNormalFragment onDestroy()");
        mActivity.unregisterReceiver(myReceiver);
    }

    // 广播接收者，接收RepealOrderFragment发送的广播（共3种通知类型，页面选中通知，筛选条件选择通知，账户选择通知）
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
                case 3: // 3表示“撤单成功”通知（分为交易查询撤单和“撤单”页面撤单）
                    handleRepealOrderSuccessNotice(intent);
                    break;
            }
        }
    }

    // 处理“撤单成功”通知
    private void handleRepealOrderSuccessNotice(Intent intent) {
        int selectPagePosition = intent.getIntExtra("selectPagePosition", -1);
        if (selectPagePosition == PAGE_INDEX) {
            isNeedScrollToTop = true;
            startQuery();
        }
    }

    // 处理“筛选条件选择”通知
    private void handleConditionSelectedNotice(Intent intent) {
        // 如果广播源不是来自RepealOrderFragment，直接返回
        String broadcastSource = intent.getStringExtra("BroadcastSource");
        if (!"RepealOrderFragment".equals(broadcastSource)) {
            return;
        }

        SelectParams selectParams = intent.getParcelableExtra("selectParams");
        int selectPagePosition = intent.getIntExtra("selectPagePosition", -1);
        mCurrentSelectedPage = selectPagePosition;
        if (selectPagePosition == PAGE_INDEX) { // 如果是当前页
            mSelectParams = selectParams;
            //            startQuery(); // 因为筛选条件并不影响查询结果，所以不用重新查询
        } else {
            mChangedSelectParams = selectParams;
        }
    }

    // 处理“账户选择”通知
    private void handleAccountSelectedNotice(Intent intent) {
        // 如果广播源不是来自RepealOrderFragment，直接返回
        String broadcastSource = intent.getStringExtra("BroadcastSource");
        if (!"RepealOrderFragment".equals(broadcastSource)) {
            return;
        }

        XpadAccountQueryViewModel.XPadAccountEntity account = intent.getParcelableExtra("account");
        int selectPagePosition = intent.getIntExtra("selectPagePosition", -1);
        mCurrentSelectedPage = selectPagePosition;
        if (selectPagePosition == PAGE_INDEX) { // 就是当前页面
            mCurrentQueryAccount = account;
            isNeedScrollToTop = false;
            startQuery();
        } else { // 不是当前页面
            mChangedQueryAccount = account;
        }
    }

    // 处理“页面选中”通知
    private void handlePageSelectedNotice(Intent intent) {
        // 如果广播源不是来自RepealOrderFragment，直接返回
        String broadcastSource = intent.getStringExtra("BroadcastSource");
        if (!"RepealOrderFragment".equals(broadcastSource)) {
            return;
        }

        int selectPagePosition = intent.getIntExtra("selectPagePosition", -1);
        mCurrentSelectedPage = selectPagePosition;
        if (selectPagePosition == PAGE_INDEX) {
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
    }

    // 开始查询（此方法由TransInquireFragment发起广播触发）
    private void startQuery() {
        if (mCurrentQueryAccount != null) {
            if (mCurrentSelectedPage == PAGE_INDEX) {
                showLoadingDialog("加载中...");
            }
            XpadAutTradStatusViewModel viewModel = new XpadAutTradStatusViewModel();
            viewModel.setAccountKey(mCurrentQueryAccount.getAccountKey());
            viewModel.setCurrentIndex("0");
            viewModel.setPageSize(PAGE_SIZE);
            transNormalPresenter.psnXpadAutTradStatus(viewModel);
        }
    }

    // 成功回调：查询客户理财账户信息
    @Override
    public void psnXpadAccountQuerySuccess(XpadAccountQueryViewModel xpadAccountQueryViewModel) {
        this.xpadAccountQueryViewModel = xpadAccountQueryViewModel;
        mAccountList = xpadAccountQueryViewModel.getList();

        // 查询最近账户
        XpadRecentAccountQueryViewModel xpadRecentAccountQueryViewModel = new XpadRecentAccountQueryViewModel();
        transNormalPresenter.psnXpadRecentAccountQuery(xpadRecentAccountQueryViewModel);
    }

    @Override
    public void psnXpadAccountQueryFail(BiiResultErrorException biiResultErrorException) {
        if (mCurrentSelectedPage == PAGE_INDEX) {
            closeProgressDialog();
        }
    }

    //
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

        // （临时，防止没有当前查询账户）以后删掉
        if (mCurrentQueryAccount == null) {
            mCurrentQueryAccount = mAccountList != null ? mAccountList.get(0) : null;
            mRecentQueryAccount = mAccountList != null ? mAccountList.get(0) : null;
        }

        // 开始查询“常规交易列表”
        startQuery();
    }

    // 失败回调：查询客户最近操作的理财账号
    @Override
    public void psnXpadRecentAccountQueryFail(BiiResultErrorException biiResultErrorException) {
        if (mCurrentSelectedPage == PAGE_INDEX) {
            closeProgressDialog();
        }
    }

    // 成功回调：委托常规交易状况查询
    @Override
    public void psnXpadAutTradStatusSuccess(XpadAutTradStatusViewModel viewModel) {
        if (mCurrentSelectedPage == PAGE_INDEX) {
            closeProgressDialog();
        }
        this.xpadAutTradStatusViewModel = viewModel;

        List<XpadAutTradStatusViewModel.AutTradEntitiy> autTradList = xpadAutTradStatusViewModel.getAutTradList();
        List<XpadAutTradStatusViewModel.AutTradEntitiy> autTradList2 = new ArrayList<XpadAutTradStatusViewModel.AutTradEntitiy>();

        // ----------start-----------
        if (!isPullToLoadMore) {
            mAutTradList.clear();
            mAutTradList2.clear();
        }

        isPullToLoadMore = false;

        // ----------end-----------

        // 循环遍历，（在撤单那个菜单里面，只把status＝＝0&&canbecanceled＝＝0的数据显示出来，然后可以支持撤单）
        if (autTradList != null) {
            for (XpadAutTradStatusViewModel.AutTradEntitiy autTradEntitiy : autTradList) {
                if ("0".equals(autTradEntitiy.getStatus()) && "0".equals(autTradEntitiy.getCanBeCanceled())) {
                    autTradList2.add(autTradEntitiy);
                }
            }
        }

        mAutTradList.addAll(autTradList);
        mAutTradList2.addAll(autTradList2);

        // 进行排序的操作
        Collections.sort(mAutTradList2);
        //        Collections.reverse(mAutTradList2);

        List<ShowListBean> transactionList = new ArrayList<ShowListBean>();
        for (int i = 0; i < mAutTradList2.size(); i++) {
            LocalDate localDate = mAutTradList2.get(i).getFutureDate();
            String formatTime = "";// 当前时间 MM月/yyyy
            String tempTime = "";// 上一次时间
            if (localDate != null) {
                formatTime = localDate.format(DateFormatters.monthFormatter1);
            }
            if (i > 0) {
                tempTime = mAutTradList2.get(i - 1).getFutureDate().format(DateFormatters.monthFormatter1);
            }
            if (tempTime.equals(formatTime)) { // child
                ShowListBean item = new ShowListBean();
                item.type = ShowListBean.CHILD;

                item.setChangeColor(true);
                item.setTitleID(ShowListConst.TITLE_WEALTH);
                item.setTime(localDate);
                item.setContentLeftAbove(mAutTradList2.get(i).getProdName());
                item.setContentLeftBelow(PublicCodeUtils.getTransferType(mActivity, mAutTradList2.get(i).getTrfType())); // 交易类型
                if ("0".equals(mAutTradList2.get(i).getStatus()) && "0".equals(mAutTradList2.get(i).getCanBeCanceled())) {
                    item.setContentRightAbove("");
                } else {
                    item.setContentRightAbove("不可撤单");
                }

                String moneyFormat = MoneyUtils.getLoanAmountShownRMB1(mAutTradList2.get(i).getAmount(), mAutTradList2.get(i).getCurrencyCode());
                String transAmout = MoneyUtils.getLoanAmountShownRMB1(mAutTradList2.get(i).getTrfAmount(), mAutTradList2.get(i).getCurrencyCode());
                String cashRemit = "";
                if ("001".equals(mAutTradList2.get(i).getCurrencyCode())) {
                    cashRemit = " 元";
                } else {
                    cashRemit = " " + PublicCodeUtils.getCurrency(mActivity, mAutTradList2.get(i).getCurrencyCode());
                    if ("01".equals(mAutTradList2.get(i).getCashRemit())) {
                        cashRemit += "/钞";
                    } else if ("02".equals(mAutTradList2.get(i).getCashRemit())) {
                        cashRemit += "/汇";
                    }
                }
                if ("0.00".equals(moneyFormat) && (!"0.00".equals(transAmout))) { // TODO: 2016/11/1  这块待讨论，是不是有问题
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
                        itemFirst.setContentLeftAbove(mAutTradList2.get(i).getProdName());
                        itemFirst.setContentLeftBelow(PublicCodeUtils.getTransferType(mActivity, mAutTradList2.get(i).getTrfType())); // 交易类型
                        if ("0".equals(mAutTradList2.get(i).getStatus()) && "0".equals(mAutTradList2.get(i).getCanBeCanceled())) {
                            itemFirst.setContentRightAbove("");
                        } else {
                            itemFirst.setContentRightAbove("不可撤单");
                        }

                        String moneyFormat = MoneyUtils.getLoanAmountShownRMB1(mAutTradList2.get(i).getAmount(), mAutTradList2.get(i).getCurrencyCode());
                        String transAmout = MoneyUtils.getLoanAmountShownRMB1(mAutTradList2.get(i).getTrfAmount(), mAutTradList2.get(i).getCurrencyCode());
                        String cashRemit = "";
                        if ("001".equals(mAutTradList2.get(i).getCurrencyCode())) {
                            cashRemit = " 元";
                        } else {
                            cashRemit = " " + PublicCodeUtils.getCurrency(mActivity, mAutTradList2.get(i).getCurrencyCode());
                            if ("01".equals(mAutTradList2.get(i).getCashRemit())) {
                                cashRemit += "/钞";
                            } else if ("2".equals(mAutTradList2.get(i).getCashRemit())) {
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

    // 处理有无无数据时的情况
    private void handleNoData() {
        Log.e("ljljlj", "TransNormalFragment handleNoData()");

        // 处理“数据是否为空”的情况
        if (mTransactionList.size() > 0) {
            ll_no_data_query.setVisibility(View.GONE);
        } else {
            ll_no_data_query.setVisibility(View.VISIBLE);
            no_data_transfer_query.setText("查询结果为空，请稍后再试");

        }
        isFisrtTimeEnter = false;
    }

    // 失败回调：委托常规交易状况查询
    @Override
    public void psnXpadAutTradStatusFail(BiiResultErrorException biiResultErrorException) {
        if (mCurrentSelectedPage == PAGE_INDEX) {
            closeProgressDialog();
        }
    }

    @Override
    public void setPresenter(TransNormalContact.Presenter presenter) {
    }
}
