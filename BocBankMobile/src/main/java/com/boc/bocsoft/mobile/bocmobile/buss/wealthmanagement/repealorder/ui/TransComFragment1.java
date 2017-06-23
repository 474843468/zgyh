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
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.repealorder.model.XpadAutComTradStatusViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.repealorder.presenter.TransComPresenter;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.transinquire.model.SelectParams;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.transinquire.model.XpadAccountQueryViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.transinquire.model.XpadRecentAccountQueryViewModel;
import com.boc.bocsoft.mobile.common.utils.date.DateFormatters;

import org.threeten.bp.LocalDate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Fragment：中银理财-撤单-组合购买
 * Created by zhx on 2016/9/19
 */
public class TransComFragment1 extends BussFragment implements TransComContact.View {
    public static final String PAGE_SIZE = String.valueOf(ApplicationConst.WEALTH_PAGE_SIZE);
    private static final int PAGE_INDEX = 1; // 页面索引
    private View rootView;
    // 上拉刷新
    private PullToRefreshLayout refreshLayout;
    // 查询列表组件
    private PinnedSectionListView transactionView;
    private boolean isPullToLoadMore; // 是否是上拉加载更多
    private SelectParams mSelectParams;
    private TransComPresenter transComPresenter;
    private XpadAccountQueryViewModel xpadAccountQueryViewModel;
    private List<XpadAccountQueryViewModel.XPadAccountEntity> mAccountList;
    private XpadAccountQueryViewModel.XPadAccountEntity mRecentQueryAccount; // 最近查询账户
    private XpadAccountQueryViewModel.XPadAccountEntity mCurrentQueryAccount; // 当前查询的账户（列表中的数据所对应的账户）
    private XpadAccountQueryViewModel.XPadAccountEntity mChangedQueryAccount; // 从账户列表中选择后改变的账户
    private XpadAutComTradStatusViewModel xpadAutComTradStatusViewModel;
    private MyReceiver myReceiver;
    // 查询列表组件的数据集合
    private List<ShowListBean> mTransactionList;
    private SelectParams mChangedSelectParams;
    private List<XpadAutComTradStatusViewModel.AutComTradEntity> mAutComTradList = new ArrayList<XpadAutComTradStatusViewModel.AutComTradEntity>(); // 组合交易数据集合
    private List<XpadAutComTradStatusViewModel.AutComTradEntity> mAutComTradList2 = new ArrayList<XpadAutComTradStatusViewModel.AutComTradEntity>(); // 组合交易数据集合

    private int mCurrentSelectedPage = 0; // 当前选中的页面索引
    private TextView no_data_transfer_query;//查询无数据
    private LinearLayout ll_no_data_query; // 显示"查询无结果"提示信息的布局
    private boolean isFisrtTimeEnter = true; // 是否首次进入
    private boolean isNeedScrollToTop = false;
    private ShowListAdapter mAdapter;

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        rootView = mInflater.inflate(R.layout.fragment_trans_com1, null);
        Log.e("LJLJLJ", "TransComFragment1 --------------------111111111111111111111");
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

        // 请求所有账户
        if (mCurrentSelectedPage == PAGE_INDEX) {
            showLoadingDialog("加载中...");
        }
        transComPresenter = new TransComPresenter(this);
        XpadAccountQueryViewModel xpadAccountQueryViewModel = new XpadAccountQueryViewModel();
        xpadAccountQueryViewModel.setQueryType("0");
        xpadAccountQueryViewModel.setXpadAccountSatus("1");
        transComPresenter.psnXpadAccountQuery(xpadAccountQueryViewModel);
    }

    // 初始化选择的参数
    private void initSelectParams() {
        mSelectParams = new SelectParams();
        mSelectParams.setStartDate(ApplicationContext.getInstance().getCurrentSystemDate().toLocalDate().minusMonths(3).plusDays(1).format(
                DateFormatters.dateFormatter1));
        mSelectParams.setEndDate(ApplicationContext.getInstance().getCurrentSystemDate().toLocalDate().format(DateFormatters.dateFormatter1));
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
                case 3: // 3表示“撤单成功”通知
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
            isNeedScrollToTop = true;
            startQuery();
        } else {
            mChangedSelectParams = selectParams;
        }
    }

    // 处理“账户选择”通知
    private void handleAccountSelectedNotice(Intent intent) {
        isFisrtTimeEnter = true;
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
            isNeedScrollToTop = true;
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

            if (mChangedSelectParams != null && !mChangedSelectParams.equals(mSelectParams)) {
                isStartQuery = true;
                mSelectParams = mChangedSelectParams;
                mChangedSelectParams = null;
            }

            if (isStartQuery) {
                isNeedScrollToTop = true;
                startQuery();
            }
        }
    }

    @Override
    public void setListener() {
        IntentFilter intentFilter = new IntentFilter("startQuery");
        myReceiver = new MyReceiver();
        mActivity.registerReceiver(myReceiver, intentFilter);

        transactionView.setListener(new PinnedSectionListView.ClickListener() {
            @Override
            public void onItemClickListener(int position) {
                XpadAutComTradStatusViewModel.AutComTradEntity autComTradEntity = mAutComTradList2.get(position);
                TransComDetailFragment transComDetailFragment = new TransComDetailFragment();
                Bundle bundle1 = new Bundle();
                bundle1.putParcelable("autComTradEntity", autComTradEntity);
                bundle1.putParcelable("currentQueryAccount", mCurrentQueryAccount);
                bundle1.putInt("selectPagePosition", PAGE_INDEX);
                bundle1.putString("noticeType", "3"); // 区分页面源
                transComDetailFragment.setArguments(bundle1);
                start(transComDetailFragment);
            }
        });
        refreshLayout.setOnLoadListener(new PullToRefreshLayout.OnLoadListener() {
            @Override
            public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
                // 判断mCurrentQueryAccount是否为空，如果为null，那么什么也不做
                if (mCurrentQueryAccount == null) {
                    return;
                }

                XpadAutComTradStatusViewModel viewModel = new XpadAutComTradStatusViewModel();
                viewModel.setAccountKey(mCurrentQueryAccount.getAccountKey());
                if (mSelectParams != null) {
                    viewModel.setStartDate(mSelectParams.getStartDate());
                    viewModel.setEndDate(mSelectParams.getEndDate());
                } else {
                    viewModel.setStartDate(ApplicationContext.getInstance().getCurrentSystemDate().toLocalDate().minusMonths(3).plusDays(1).format(DateFormatters.dateFormatter1));
                    viewModel.setEndDate(ApplicationContext.getInstance().getCurrentSystemDate().toLocalDate().format(DateFormatters.dateFormatter1));
                }

                isPullToLoadMore = true;

                if (xpadAutComTradStatusViewModel != null) {
                    if (mAutComTradList.size() < xpadAutComTradStatusViewModel.getRecordNumber()) {
                        if (mCurrentSelectedPage == PAGE_INDEX) {
                            showLoadingDialog("加载中...");
                        }
                        transComPresenter.psnXpadAutComTradStatus(viewModel);
                        isNeedScrollToTop = false;
                    } else {
                        isPullToLoadMore = false;
                        refreshLayout.loadmoreCompleted(PullToRefreshLayout.NO_MORE_DATA);
                    }
                }
            }
        });
    }

    @Override
    protected boolean isHaveTitleBarView() {
        return false;
    }

    // 开始查询
    private void startQuery() {
        // 组合交易状况查询
        if (mCurrentQueryAccount != null) {
            if (mCurrentSelectedPage == PAGE_INDEX) {
                showLoadingDialog("加载中...");
            }
            XpadAutComTradStatusViewModel viewModel = new XpadAutComTradStatusViewModel();
            viewModel.setAccountKey(mCurrentQueryAccount.getAccountKey());
            if (mSelectParams != null) {
                viewModel.setStartDate(mSelectParams.getStartDate());
                viewModel.setEndDate(mSelectParams.getEndDate());
            } else {
                viewModel.setStartDate(ApplicationContext.getInstance().getCurrentSystemDate().toLocalDate().minusMonths(3).plusDays(1).format(DateFormatters.dateFormatter1));
                viewModel.setEndDate(ApplicationContext.getInstance().getCurrentSystemDate().toLocalDate().format(DateFormatters.dateFormatter1));
            }

            transComPresenter.psnXpadAutComTradStatus(viewModel);
        }
    }

    // 成功：中银理财-撤单-组合购买（列表查询成功）
    @Override
    public void psnXpadAutComTradStatusSuccess(XpadAutComTradStatusViewModel viewModel) {
        if (mCurrentSelectedPage == PAGE_INDEX) {
            closeProgressDialog();
        }
        xpadAutComTradStatusViewModel = viewModel;

        List<XpadAutComTradStatusViewModel.AutComTradEntity> autComTradList = xpadAutComTradStatusViewModel.getList();
        List<XpadAutComTradStatusViewModel.AutComTradEntity> listBean = new ArrayList<XpadAutComTradStatusViewModel.AutComTradEntity>();
        List<ShowListBean> transactionList = new ArrayList<ShowListBean>();

        // ----------start-----------
        if (!isPullToLoadMore) {
            mAutComTradList.clear();
            mAutComTradList2.clear();
            listBean.clear();
        }

        mAutComTradList.clear();
        mAutComTradList2.clear();
        listBean.clear();

        isPullToLoadMore = false;

        // ----------end-----------
        // 循环遍历，（在撤单那个菜单里面，只把status＝＝0&&canbecanceled＝＝0的数据显示出来，然后可以支持撤单）
        if (autComTradList != null) {
            for (XpadAutComTradStatusViewModel.AutComTradEntity autComTradEntity : autComTradList) {
                if ("0".equals(autComTradEntity.getStatus()) && "0".equals(autComTradEntity.getCanBeCanceled())) {
                    listBean.add(autComTradEntity);
                }
            }
        }

        mAutComTradList.addAll(autComTradList);
        mAutComTradList2.addAll(listBean);

        // 日期的校正 2016-11-01 start
        //        int u = 0;
        //        for (XpadAutComTradStatusViewModel.AutComTradEntity entity :  mAutComTradList2) {
        //            u++;
        //            entity.setReturnDate(entity.getReturnDate().plusMonths(u));
        //        }
        // 日期的校正 2016-11-01 end

        // 进行排序的操作
        Collections.sort(mAutComTradList2);
//        Collections.reverse(mAutComTradList2);


        for (int i = 0; i < mAutComTradList2.size(); i++) {
            LocalDate localDate = mAutComTradList2.get(i).getReturnDate();
            String formatTime = "";// 当前时间 MM月/yyyy
            String tempTime = "";// 上一次时间
            if (localDate != null) {
                formatTime = localDate.format(DateFormatters.monthFormatter1);
            }
            if (i > 0) {
                tempTime = mAutComTradList2.get(i - 1).getReturnDate().format(DateFormatters.monthFormatter1);
            }
            if (tempTime.equals(formatTime)) { // child
                ShowListBean item = new ShowListBean();
                item.type = ShowListBean.CHILD;

                item.setChangeColor(true);
                item.setTitleID(ShowListConst.TITLE_WEALTH);
                item.setContentLeftAbove(mAutComTradList2.get(i).getProdName());
                // 状态（0：正常 1：解除）
//                String status = "";
//                if (mAutComTradList2.get(i).getStatus().equals("0")) {
//                    status = "正常";
//                } else if (mAutComTradList2.get(i).getStatus().equals("1")) {
//                    status = "解除";
//                }
//                item.setContentLeftBelow(status);
//
//                item.setContentLeftBelow(PublicCodeUtils.getTransferChannel(mActivity, mAutComTradList2.get(i).getStatus())); // 渠道

                if ("0".equals(mAutComTradList2.get(i).getStatus()) && "0".equals(mAutComTradList2.get(i).getCanBeCanceled())) {
                    item.setContentRightAbove("");
                } else {
                    item.setContentRightAbove("不可撤单");
                }
                String moneyFormat = MoneyUtils.getLoanAmountShownRMB1(mAutComTradList2.get(i).getBuyAmt(), mAutComTradList2.get(i).getCurrency());
                String cashRemit = "";
                if ("001".equals(mAutComTradList2.get(i).getCurrency())) {
                    cashRemit = " 元";
                } else {
                    cashRemit = " " + PublicCodeUtils.getCurrency(mActivity, mAutComTradList2.get(i).getCurrency());
                    if ("1".equals(mAutComTradList2.get(i).getCashRemit())) {
                        cashRemit += "/钞";
                    } else if ("2".equals(mAutComTradList2.get(i).getCashRemit())) {
                        cashRemit += "/汇";
                    }
                }
                item.setContentRightBelow(moneyFormat + cashRemit);

                item.setTime(mAutComTradList2.get(i).getReturnDate());

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
                        itemFirst.setContentLeftAbove(mAutComTradList2.get(i).getProdName());
//                        String status = "";
//                        if (mAutComTradList2.get(i).getStatus().equals("0")) {
//                            status = "正常";
//                        } else if (mAutComTradList2.get(i).getStatus().equals("1")) {
//                            status = "解除";
//                        }
//                        itemFirst.setContentLeftBelow(status);
//
//                        itemFirst.setContentLeftBelow(PublicCodeUtils.getTransferChannel(mActivity, mAutComTradList2.get(i).getChannel())); // 渠道
                        if ("0".equals(mAutComTradList2.get(i).getStatus()) && "0".equals(mAutComTradList2.get(i).getCanBeCanceled())) {
                            itemFirst.setContentRightAbove("");
                        } else {
                            itemFirst.setContentRightAbove("不可撤单");
                        }
                        String moneyFormat = MoneyUtils.getLoanAmountShownRMB1(mAutComTradList2.get(i).getBuyAmt(), mAutComTradList2.get(i).getCurrency());
                        String cashRemit = "";
                        if ("001".equals(mAutComTradList2.get(i).getCurrency())) {
                            cashRemit = " 元";
                        } else {
                            cashRemit = " " + PublicCodeUtils.getCurrency(mActivity, mAutComTradList2.get(i).getCurrency());
                            if ("1".equals(mAutComTradList2.get(i).getCashRemit())) {
                                cashRemit += "/钞";
                            } else if ("2".equals(mAutComTradList2.get(i).getCashRemit())) {
                                cashRemit += "/汇";
                            }
                        }
                        itemFirst.setContentRightBelow(moneyFormat + cashRemit);

                        itemFirst.setTime(mAutComTradList2.get(i).getReturnDate());


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
        Log.e("ljljlj", "TransComFragment handleNoData()");
        // 处理“数据是否为空”的情况
        if (mTransactionList.size() > 0) {
            ll_no_data_query.setVisibility(View.GONE);
        } else {
            ll_no_data_query.setVisibility(View.VISIBLE);
            no_data_transfer_query.setText(isFisrtTimeEnter ? getResources().getString(R.string.boc_transfer_query_empty): getResources().getString(R.string.boc_transfer_select_empty));
        }
        isFisrtTimeEnter = false;
    }

    @Override
    public boolean onBack() {
        RepealOrderFragment repealOrderFragment = findFragment(RepealOrderFragment.class);
        if (repealOrderFragment != null) {
            return repealOrderFragment.onBack();
        } else {
            return true;
        }
    }

    @Override
    public void psnXpadAutComTradStatusFail(BiiResultErrorException biiResultErrorException) {
        if (mCurrentSelectedPage == PAGE_INDEX) {
            closeProgressDialog();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mActivity.unregisterReceiver(myReceiver);
    }

    // 成功回调：查询客户理财账户信息
    @Override
    public void psnXpadAccountQuerySuccess(XpadAccountQueryViewModel xpadAccountQueryViewModel) {
        this.xpadAccountQueryViewModel = xpadAccountQueryViewModel;
        mAccountList = xpadAccountQueryViewModel.getList();

        // 查询最近账户
        XpadRecentAccountQueryViewModel xpadRecentAccountQueryViewModel = new XpadRecentAccountQueryViewModel();
        transComPresenter.psnXpadRecentAccountQuery(xpadRecentAccountQueryViewModel);
    }

    @Override
    public void psnXpadAccountQueryFail(BiiResultErrorException biiResultErrorException) {
        if (mCurrentSelectedPage == PAGE_INDEX) {
            closeProgressDialog();
        }
    }

    // 查询最近账户成功
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
            mCurrentQueryAccount = mAccountList.get(0);
            mRecentQueryAccount = mAccountList.get(0);
        }

        // 开始进行“组合交易状况查询”
        startQuery();
    }

    @Override
    public void psnXpadRecentAccountQueryFail(BiiResultErrorException biiResultErrorException) {
        if (mCurrentSelectedPage == PAGE_INDEX) {
            closeProgressDialog();
        }
    }

    @Override
    public void setPresenter(TransComContact.Presenter presenter) {
    }
}
