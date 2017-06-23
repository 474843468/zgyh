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
import com.boc.bocsoft.mobile.bocmobile.buss.fund.trademanagement.model.PsnFundQueryTransOntranModel;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.utils.DataUtils;
import com.boc.bocsoft.mobile.common.utils.date.DateFormatters;

import org.threeten.bp.LocalDate;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wy7105 on 2016/12/9.
 * 在途交易列表view
 */
public class TransitTradeListView extends FrameLayout implements PinnedSectionListView.ClickListener {

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
    private List<PsnFundQueryTransOntranModel.ListBean> openTradingList = new ArrayList<PsnFundQueryTransOntranModel.ListBean>();//查询列表
    private PsnFundQueryTransOntranModel model; //保存接口请求回来的数据

    public TransitTradeListView(Context context) {
        this(context, null, 0);
    }

    public TransitTradeListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TransitTradeListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    protected void initView(Context context) {
        mContext = context;
        rootView = LayoutInflater.from(mContext).inflate(R.layout.boc_fragment_transit_trade_list2, this);
        model = new PsnFundQueryTransOntranModel();
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

    public void onLoadSuccess(PsnFundQueryTransOntranModel viewModel) {
        mBussFragment.closeProgressDialog();
        boolean isNodate = (viewModel == null || viewModel.getRecordNumber() <= 0 || viewModel.getList() == null
                || viewModel.getList().size() <= 0);
        if (isNodate) {
            if (isPullToLoadMore) {
                refreshLayout.loadmoreCompleted(PullToRefreshLayout.FAIL);
            } else {
                showNodata();
            }
        } else {
            if (isPullToLoadMore) {
                refreshLayout.loadmoreCompleted(PullToRefreshLayout.SUCCEED);
            }

            refleshListData(viewModel);
            refleshListView();
            isPullToLoadMore = false;
            ll_no_data.setVisibility(GONE);
            refreshLayout.setVisibility(VISIBLE);
        }
    }

    //刷新数据
    private void refleshListData(PsnFundQueryTransOntranModel resultModel) {

        if (model.getList() == null) {
            List<PsnFundQueryTransOntranModel.ListBean> listBeens = new ArrayList<>();
            model.setList(listBeens);
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
        List<ShowListBean> transactionList = new ArrayList<ShowListBean>(); //按规定的格式显示列表
        openTradingList = model.getList();
        for (int i = 0; i < openTradingList.size(); i++) {
            PsnFundQueryTransOntranModel.ListBean transbean = openTradingList.get(i);
            ShowListBean transactionBean = new ShowListBean();
            transactionBean.setChangeColor(true);
            String nameAndCode = getLeftAbove(openTradingList.get(i)); //基金名称
            String rightDes = getRightDes(openTradingList.get(i)); //交易金额或份额
            String tranTypeDes = getLeftBelow(openTradingList.get(i)); //交易类型
            LocalDate localDate = transbean.getPaymentDate();
            String formatTime = "";// 当前时间 MM月/yyyy
            String tempTime = "";// 上一次时间

            if (localDate != null) {
                formatTime = localDate.format(DateFormatters.monthFormatter1);
            }
            if (i > 0) {
                tempTime = openTradingList.get(i - 1).getPaymentDate().format(DateFormatters.monthFormatter1);
            }
            if (tempTime.equals(formatTime)) {// child
                ShowListBean item = new ShowListBean();
                item.type = ShowListBean.CHILD;
                item.setTitleID(ShowListConst.TITLE_WEALTH);
                item.setTime(localDate);
                item.setContentLeftAbove(nameAndCode);
                item.setContentLeftBelow(tranTypeDes);
                item.setContentRightBelow(rightDes);
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
                        itemFirst.setContentLeftAbove(nameAndCode);
                        itemFirst.setContentLeftBelow(tranTypeDes);
                        itemFirst.setContentRightBelow(rightDes);
                    }
                    transactionList.add(itemFirst);
                }
            }
        }
        mAdapter.setData(transactionList);
    }

    //名称代码显示
    private String getLeftAbove(PsnFundQueryTransOntranModel.ListBean bean) {
        String fundName = bean.getFundName();
        String fundCode = bean.getFundCode();
        String content = mContext.getString(R.string.boc_fund_record_name_code, fundName, fundCode);
        String transType = DataUtils.getTransTypeDes(mContext, bean.getFundTranType());
        if (transType.equals(mContext.getString(R.string.boc_fund_trans_type_fund_account_management)))
            content = mContext.getString(R.string.boc_fund_TA_account) + bean.getTaAccountNo();
        return content;
    }

    //交易类型显示
    private String getLeftBelow(PsnFundQueryTransOntranModel.ListBean bean) {
        String content = DataUtils.getTransTypeDes(mContext, bean.getFundTranType());
        if (content.equals(mContext.getString(R.string.boc_fund_trans_type_purchase))) {
            if ("1".equals(bean.getSpecialTransFlag()))
                content = mContext.getString(R.string.boc_fund_trans_type_limit_purchase);
            if ("2".equals(bean.getSpecialTransFlag()))
                content = mContext.getString(R.string.boc_fund_trans_type_appoint_purchase);
        }  //购买
        else if (content.equals(mContext.getString(R.string.boc_fund_trans_type_reddem))) {
            if ("1".equals(bean.getSpecialTransFlag()))
                content = mContext.getString(R.string.boc_fund_trans_type_limit_reddem);
            if ("2".equals(bean.getSpecialTransFlag()))
                content = mContext.getString(R.string.boc_fund_trans_type_appoint_reddem);
        }  //赎回
        else if (content.equals(mContext.getString(R.string.boc_fund_trans_type_fundscheduledbuy))) {
            if ("3".equals(bean.getSpecialTransFlag()))
                content = mContext.getString(R.string.boc_fund_trans_type_fundscheduled_buy);
            if ("4".equals(bean.getSpecialTransFlag()))
                content = mContext.getString(R.string.boc_fund_trans_type_fundscheduled_sell);
        }  //定投
        else if (content.equals(mContext.getString(R.string.boc_fund_trans_type_set_bonus))) {
            content = mContext.getString(R.string.boc_fund_trans_type_setbonus, DataUtils.getBonusTypeDes(mContext, bean.getBonusType()));
        } //设置分红方式
        else if (content.equals(mContext.getString(R.string.boc_fund_trans_type_fund_account_management))) {
            if ("008".equals(bean.getFundTranType()) || "108".equals(bean.getFundTranType()))
                content = mContext.getString(R.string.boc_fund_trans_type_TAaccount_register);
            if ("002".equals(bean.getFundTranType()) || "102".equals(bean.getFundTranType()))
                content = mContext.getString(R.string.boc_fund_trans_type_TAaccount_cancel);
            if ("009".equals(bean.getFundTranType()) || "109".equals(bean.getFundTranType()))
                content = mContext.getString(R.string.boc_fund_trans_type_TAaccount_disassociate);
        } //账户管理
        return content;
    }

    //交易金额/份额
    private String getRightDes(PsnFundQueryTransOntranModel.ListBean bean) {
        String content = "";
        String transType = DataUtils.getTransTypeDes(mContext, bean.getFundTranType());
        if (transType.equals(mContext.getString(R.string.boc_fund_trans_type_fund_account_management)) || transType.equals(mContext.getString(R.string.boc_fund_trans_type_set_bonus))) {
            content = "";
            return content;
        }
        String unit = DataUtils.getFundUnit(mContext, bean.getFundTranType(), bean.getBonusType(), bean.getCurrencyCode());
        String flag = DataUtils.getAmountOrCount(bean.getFundTranType(), bean.getBonusType());
        if (DataUtils.FLAG_AMOUNT.equals(flag)) {
            content = MoneyUtils.transMoneyFormat(bean.getTransAmount(), bean.getCurrencyCode());
            content = content + " " + unit;
        } else if (DataUtils.FLAG_COUNT.equals(flag)) {
            content = MoneyUtils.transMoneyFormat(bean.getTransCount(), bean.getCurrencyCode());
            content = content + " " + unit;
        }
        return content;
    }

    public void onLoadFailed() {
        mBussFragment.closeProgressDialog();
        if (isPullToLoadMore) {
            refreshLayout.loadmoreCompleted(PullToRefreshLayout.NO_MORE_DATA);
        } else {
            showNodata();
        }
    }

    public int getCurrentIndex() {
        return currentIndex;
    }

    public void setIsPullToLoadMore(boolean isPullToLoadMore) {
        this.isPullToLoadMore = isPullToLoadMore;
    }

    // 处理无数据时的情况
    private void showNodata() {
        ll_no_data.setVisibility(VISIBLE);
        tv_no_data.setText("暂无信息");
        tv_no_data.setVisibility(View.VISIBLE);
        refreshLayout.setVisibility(View.GONE);
    }


    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        LogUtil.e("" + this.getClass().getName() + "onDetachedFromWindow");
    }

    @Override
    public void onItemClickListener(int position) {
        PsnFundQueryTransOntranModel.ListBean listBean = openTradingList.get(position);
        TransitTradeDetailFragment transitTradeDetailFragment = new TransitTradeDetailFragment();
        //传值给在途交易详情页面
        Bundle bundle = new Bundle();
        bundle.putSerializable("listBean", listBean);
        transitTradeDetailFragment.setArguments(bundle);
        mBussFragment.start(transitTradeDetailFragment);
    }
}
