package com.boc.bocsoft.mobile.bocmobile.buss.fund.trademanagement.ui;


import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.boc.bocma.tools.LogUtil;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.refreshliseview.PullToRefreshLayout;
import com.boc.bocsoft.mobile.bocmobile.base.widget.showlistview.PinnedSectionListView;
import com.boc.bocsoft.mobile.bocmobile.base.widget.showlistview.ShowListConst;
import com.boc.bocsoft.mobile.bocmobile.base.widget.showlistview.adapter.ShowListAdapter;
import com.boc.bocsoft.mobile.bocmobile.base.widget.showlistview.bean.ShowListBean;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.trademanagement.model.PsnFundQueryHistoryDetailModel;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.utils.DataUtils;
import com.boc.bocsoft.mobile.common.utils.date.DateFormatters;

import org.threeten.bp.LocalDate;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wy7105 on 2016/12/9.
 * 历史列表view
 */
public class HistoryTradeListView extends FrameLayout implements PinnedSectionListView.ClickListener {

    protected BussFragment mBussFragment;
    private View rootView;
    private Context mContext;
    private int currentIndex = 0;
    private boolean isPullToLoadMore = false;
    public PullToRefreshLayout refreshLayout; // 上拉刷新
    private PinnedSectionListView pinnedSectionListView; //查询列表组件
    private ShowListAdapter mAdapter;
    private LinearLayout ll_no_data;//查询无数据
    private TextView tv_no_data;
    private List<PsnFundQueryHistoryDetailModel.ListEntity> openTradingList = new ArrayList<PsnFundQueryHistoryDetailModel.ListEntity>();//查询列表
    private PsnFundQueryHistoryDetailModel model;

    public HistoryTradeListView(Context context) {
        this(context, null, 0);
    }

    public HistoryTradeListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HistoryTradeListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        mContext = context;
        rootView = LayoutInflater.from(mContext).inflate(R.layout.boc_fragment_history_trade_list, this);
        model = new PsnFundQueryHistoryDetailModel();
        refreshLayout = (PullToRefreshLayout) rootView.findViewById(R.id.refresh_transfer_query); //刷新
        ll_no_data = (LinearLayout) rootView.findViewById(R.id.no_data_query);//没有数据的布局
        tv_no_data = (TextView) rootView.findViewById(R.id.no_data_transfer_query);//没有数据的文字显示
        mAdapter = new ShowListAdapter(mContext, -1);
        pinnedSectionListView = (PinnedSectionListView) rootView.findViewById(R.id.lv_transfer_query); //列表
        pinnedSectionListView.saveAdapter(mAdapter);
        pinnedSectionListView.setShadowVisible(false);
        pinnedSectionListView.setAdapter(mAdapter);
        pinnedSectionListView.setListener(this);
    }

    public void setFragment(BussFragment bussFragment) {
        mBussFragment = bussFragment;
    }

    public void onLoadSuccess(PsnFundQueryHistoryDetailModel viewModel) {
        mBussFragment.closeProgressDialog();
        boolean isNodate = (viewModel == null || viewModel.getRecordNumber() <= 0 || viewModel.getList() == null
                || viewModel.getList().size() <= 0);
        if (isNodate) {
            if (isPullToLoadMore) {
                refreshLayout.loadmoreCompleted(PullToRefreshLayout.FAIL); //没有更多数据
            } else {
                showNodata();
            }

        } else {
            if (isPullToLoadMore) {
                refreshLayout.loadmoreCompleted(PullToRefreshLayout.SUCCEED); //加载成功
            }

            refleshListData(viewModel); //刷新数据
            refleshListView(); //刷新视图
            isPullToLoadMore = false;
            ll_no_data.setVisibility(GONE);
            refreshLayout.setVisibility(VISIBLE);
        }
    }

    //刷新数据
    private void refleshListData(PsnFundQueryHistoryDetailModel resultModel) {

        if (model.getList() == null) {
            List<PsnFundQueryHistoryDetailModel.ListEntity> listEntities = new ArrayList<>();
            model.setList(listEntities);
        }

        if (!isPullToLoadMore) {  // 如果不是上拉加载更多，那么清空列表
            model.getList().clear();
        }
        model.setRecordNumber(resultModel.getRecordNumber());
        model.getList().addAll(resultModel.getList());
        currentIndex = model.getList().size();
    }

    //刷新视图
    private void refleshListView() {
        List<ShowListBean> transactionList = new ArrayList<ShowListBean>();
        openTradingList = model.getList();
        for (int i = 0; i < openTradingList.size(); i++) {
            PsnFundQueryHistoryDetailModel.ListEntity transbean = openTradingList.get(i);
            ShowListBean transactionBean = new ShowListBean();
            transactionBean.setChangeColor(true);
//            String fundName = transbean.getFundName();
//            String fundCode = transbean.getFundCode();
//            String nameAndCode = mContext.getString(R.string.boc_fund_record_name_code, fundName, fundCode);
            String nameAndCode = getLeftAbove(openTradingList.get(i));//名称代码
            String tranTypeDes = getLeftBelow(openTradingList.get(i));//交易类型
            String rightDes = getRightBelow(openTradingList.get(i));//交易金额
            String transState = getRightAbove(openTradingList.get(i));//交易状态
            LocalDate localDate = transbean.getTransDate();
            String formatTime = "";// 当前时间 MM月/yyyy
            String tempTime = "";// 上一次时间
            if (localDate != null) {
                formatTime = localDate.format(DateFormatters.monthFormatter1);
            }
            if (i > 0) {
                tempTime = openTradingList.get(i - 1).getTransDate().format(DateFormatters.monthFormatter1);
            }
            if (tempTime.equals(formatTime)) {// child
                ShowListBean item = new ShowListBean();
                item.type = ShowListBean.CHILD;
                item.setTitleID(ShowListConst.TITLE_WEALTH);
                item.setTime(localDate);
                item.setContentLeftAbove(nameAndCode);//基金名称
                item.setContentLeftBelow(tranTypeDes); //交易类型
                item.setContentRightBelow(rightDes);//交易金额或份额
                item.setContentRightAbove(transState); //交易状态
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
                        itemFirst.setContentLeftAbove(nameAndCode); //基金名称
                        itemFirst.setContentLeftBelow(tranTypeDes); //交易类型
                        itemFirst.setContentRightBelow(rightDes);//交易金额或份额
                        itemFirst.setContentRightAbove(transState);//交易状态
                    }
                    transactionList.add(itemFirst);
                }
            }
        }
        mAdapter.setData(transactionList);
    }

    //名称代码显示
    private String getLeftAbove(PsnFundQueryHistoryDetailModel.ListEntity listEntity) {
        String fundName = listEntity.getFundName();
        String fundCode = listEntity.getFundCode();
        String content = mContext.getString(R.string.boc_fund_record_name_code, fundName, fundCode);
        String transType = DataUtils.getTransTypeDes(mContext, listEntity.getTransCode());
        if (transType.equals(mContext.getString(R.string.boc_fund_trans_type_fund_account_management)))
            content = mContext.getString(R.string.boc_fund_TA_account) + listEntity.getTaAccount();
        return content;
    }

    //交易类型显示
    private String getLeftBelow(PsnFundQueryHistoryDetailModel.ListEntity listEntity) {
        String content = DataUtils.getTransTypeDes(mContext, listEntity.getTransCode());
        if (content.equals(mContext.getString(R.string.boc_fund_trans_type_purchase))) {
            if ("1".equals(listEntity.getSpecialTransFlag()))
                content = mContext.getString(R.string.boc_fund_trans_type_limit_purchase);
            if ("2".equals(listEntity.getSpecialTransFlag()))
                content = mContext.getString(R.string.boc_fund_trans_type_appoint_purchase);
        }  //购买
        else if (content.equals(mContext.getString(R.string.boc_fund_trans_type_reddem))) {
            if ("1".equals(listEntity.getSpecialTransFlag()))
                content = mContext.getString(R.string.boc_fund_trans_type_limit_reddem);
            if ("2".equals(listEntity.getSpecialTransFlag()))
                content = mContext.getString(R.string.boc_fund_trans_type_appoint_reddem);
        }  //赎回
        else if (content.equals(mContext.getString(R.string.boc_fund_trans_type_fundscheduledbuy))) {
            if ("3".equals(listEntity.getSpecialTransFlag()))
                content = mContext.getString(R.string.boc_fund_trans_type_fundscheduled_buy);
            if ("4".equals(listEntity.getSpecialTransFlag()))
                content = mContext.getString(R.string.boc_fund_trans_type_fundscheduled_sell);
        }  //定投
        else if (content.equals(mContext.getString(R.string.boc_fund_trans_type_set_bonus))) {
            content = mContext.getString(R.string.boc_fund_trans_type_setbonus, DataUtils.getBonusTypeDes(mContext, listEntity.getBonusType()));
        } //设置分红方式
        else if (content.equals(mContext.getString(R.string.boc_fund_trans_type_fund_account_management))) {
            if ("008".equals(listEntity.getTransCode()) || "108".equals(listEntity.getTransCode()))
                content = mContext.getString(R.string.boc_fund_trans_type_TAaccount_register);
            if ("002".equals(listEntity.getTransCode()) || "102".equals(listEntity.getTransCode()))
                content = mContext.getString(R.string.boc_fund_trans_type_TAaccount_cancel);
            if ("009".equals(listEntity.getTransCode()) || "109".equals(listEntity.getTransCode()))
                content = mContext.getString(R.string.boc_fund_trans_type_TAaccount_disassociate);
        } //账户管理
        return content;
    }

    //右下金额/份额显示
    private String getRightBelow(PsnFundQueryHistoryDetailModel.ListEntity listEntity) {
        String content = "";
        String transType = DataUtils.getTransTypeDes(mContext, listEntity.getTransCode());
        if (transType.equals(mContext.getString(R.string.boc_fund_trans_type_fund_account_management)) || transType.equals(mContext.getString(R.string.boc_fund_trans_type_set_bonus))) {
            content = "";
            return content;
        }
        String unit = DataUtils.getFundUnit(mContext, listEntity.getTransCode(), listEntity.getBonusType(), listEntity.getCurrencyCode());
        String flag = DataUtils.getAmountOrCount(listEntity.getTransCode(), listEntity.getBonusType());
        if (DataUtils.FLAG_AMOUNT.equals(flag)) {
            if (!listEntity.getSpecialTransFlag().equals("0") || listEntity.getIsCancle().equals("1")) {
                content = MoneyUtils.transMoneyFormat(listEntity.getApplyAmount(), listEntity.getCurrencyCode());
                content = content + " " + unit;
            } else {
                content = MoneyUtils.transMoneyFormat(listEntity.getConfirmAmount(), listEntity.getCurrencyCode());
                content = content + " " + unit;
            }
        } else if (DataUtils.FLAG_COUNT.equals(flag)) {
            if (!listEntity.getSpecialTransFlag().equals("0") || listEntity.getIsCancle().equals("1")) {
                content = MoneyUtils.transMoneyFormat(listEntity.getApplyCount(), listEntity.getCurrencyCode());
                content = content + " " + unit;
            } else {
                content = MoneyUtils.transMoneyFormat(listEntity.getConfirmCount(), listEntity.getCurrencyCode());
                content = content + " " + unit;
            }
        }
        return content;
    }

    //交易状态
    private String getRightAbove(PsnFundQueryHistoryDetailModel.ListEntity listEntity) {
        String content = DataUtils.getTransStatusDes(mContext, listEntity.getTransStatus());
        if (content.equals(mContext.getString(R.string.boc_fund_trans_status_success)))
            content = "";
        return content;
    }

    public int getCurrentIndex() {
        return currentIndex;
    }

    // 处理“数据是否为空”的情况
    private void showNodata() {
        ll_no_data.setVisibility(View.VISIBLE);
        tv_no_data.setText("暂无信息");
        tv_no_data.setVisibility(View.VISIBLE);
        refreshLayout.setVisibility(View.GONE);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        LogUtil.e("" + this.getClass().getName() + "onDetachedFromWindow");
    }

    public void setIsPullToLoadMore(boolean isPullToLoadMore) {
        this.isPullToLoadMore = isPullToLoadMore;
    }

    public void onLoadFailed() {
        mBussFragment.closeProgressDialog();
        if (isPullToLoadMore) {
            refreshLayout.loadmoreCompleted(PullToRefreshLayout.NO_MORE_DATA);
        } else {
            showNodata();
        }
    }

    @Override
    public void onItemClickListener(int position) {
        PsnFundQueryHistoryDetailModel.ListEntity listEntity = openTradingList.get(position);
        HistoryTradeDetailFragment historyTradeDetailFragment = new HistoryTradeDetailFragment();
        //传值给历史交易记录详情页
        Bundle bundle = new Bundle();
        bundle.putSerializable("listEntity", listEntity);
        historyTradeDetailFragment.setArguments(bundle);
        mBussFragment.start(historyTradeDetailFragment);
    }
}
