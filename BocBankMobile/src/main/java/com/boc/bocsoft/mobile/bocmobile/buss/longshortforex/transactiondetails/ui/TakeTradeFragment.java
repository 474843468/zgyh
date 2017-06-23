package com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.transactiondetails.ui;

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
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.transactiondetails.TransQueryUtils;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.transactiondetails.model.XpadPsnVFGGetRegCurrencyModel;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.transactiondetails.model.XpadVFGTradeInfoQueryModel;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.transactiondetails.presenter.TakeTradePresenter;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.transinquire.model.SelectParams;
import com.boc.bocsoft.mobile.common.utils.date.DateFormatters;

import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalDateTime;

import java.util.ArrayList;
import java.util.List;

/**
 * 双向宝交易查询--斩仓交易查询列表界面
 * Created by zc on 2016/11/17
 */
public class TakeTradeFragment extends BussFragment implements TakeTradeContract.View {

    private View rootView;
    private TakeTradeContract.Presenter mTakeTradePresenter;

    public static final String PAGE_SIZE = "5"; // 每页的条目数
    // 上拉刷新
    private PullToRefreshLayout refreshLayout;
    // 查询列表组件
    private PinnedSectionListView pinnedSectionListView;
    private ShowListAdapter mAdapter;
    private List<ShowListBean> mShowListBean;
    // 最大查询范围为3个月
    private final static int MAX_QUERY_RANGE = 3;
    private static final int PAGE_INDEX = 4; // 页面索引
    private XpadVFGTradeInfoQueryModel xpadVFGTradeInfoQueryModel;
    List<XpadVFGTradeInfoQueryModel.XpadPsnVFGTradeInfoQueryResultEntity> mInfoQueryResultEntities = new ArrayList<XpadVFGTradeInfoQueryModel.XpadPsnVFGTradeInfoQueryResultEntity>();//查询列表
    private boolean isPullToLoadMore; // 是否是上拉加载更多
    private boolean isFisrtTimeEnter = true; // 是否首次进入当前页面的标志
    private SelectParams mSelectParams;
    private int mCurrentSelectedPage = 0;
    private MyReceiver myReceiver;
    private String settleCurrency;
    private String conversationId;

    private LinearLayout ll_no_data_query;
    private TextView tv_no_data;

    /**
     * 初始化布局
     */
    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        rootView = mInflater.inflate(R.layout.boc_fragment_take_trade, null);
        Log.d("TakeTradeFragment  +","onCreate");
        return rootView;
    }

    @Override
    public void initView() {
        refreshLayout = (PullToRefreshLayout) rootView.findViewById(R.id.refresh_query);
        pinnedSectionListView = (PinnedSectionListView) rootView.findViewById(R.id.lv_history_query);
        pinnedSectionListView.setShadowVisible(false);
        ll_no_data_query = (LinearLayout) rootView.findViewById(R.id.no_data_query);
        tv_no_data = (TextView) rootView.findViewById(R.id.no_data);
    }

    @Override
    public void initData() {
        mTakeTradePresenter = new TakeTradePresenter(this);
        mShowListBean = new ArrayList<ShowListBean>();
        mAdapter = new ShowListAdapter(mContext, -1);
        pinnedSectionListView.saveAdapter(mAdapter);
        pinnedSectionListView.setAdapter(mAdapter);


        initSelectParams();
        if (mCurrentSelectedPage == PAGE_INDEX) {
            showLoadingDialog();
        }

        XpadPsnVFGGetRegCurrencyModel viewModel = new XpadPsnVFGGetRegCurrencyModel();
        mTakeTradePresenter.psnXpadGetRegCurrency(viewModel);
    }

    // 初始化选择的参数
    private void initSelectParams() {
        mSelectParams = new SelectParams();
        mSelectParams.setCurrency(settleCurrency);
        mSelectParams.setStartDate(ApplicationContext.getInstance().getCurrentSystemDate().toLocalDate().minusMonths(3).plusDays(1).format(DateFormatters.dateFormatter1));
        mSelectParams.setEndDate(ApplicationContext.getInstance().getCurrentSystemDate().toLocalDate().format(DateFormatters.dateFormatter1));
    }
    @Override
    public void setListener() {

        IntentFilter intentFilter = new IntentFilter("longshortQuery");
        myReceiver = new MyReceiver();
        mActivity.registerReceiver(myReceiver, intentFilter);
        //点击进入详情页
        pinnedSectionListView.setListener(new PinnedSectionListView.ClickListener() {
            @Override
            public void onItemClickListener(int position) {
                showLoadingDialog();
                XpadVFGTradeInfoQueryModel.XpadPsnVFGTradeInfoQueryResultEntity xpadTradeInfoQueryResultEntity = mInfoQueryResultEntities.get(position);
                Bundle bundle = new Bundle();
                bundle.putParcelable("xpadTradeInfoQueryResultEntity",xpadTradeInfoQueryResultEntity);
                bundle.putString("conversationId",conversationId);
                bundle.putInt("selectPagePosition", PAGE_INDEX);
                TakeTradeDetailFragment takeTradeDetailFragment = new TakeTradeDetailFragment();
                takeTradeDetailFragment.setArguments(bundle);
                start(takeTradeDetailFragment);
            }
        });
        //加载更多
        refreshLayout.setOnLoadListener(new  PullToRefreshLayout.OnLoadListener() {
            @Override
            public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
                isPullToLoadMore = true;
                if (xpadVFGTradeInfoQueryModel != null) {
                    if (mInfoQueryResultEntities.size() < xpadVFGTradeInfoQueryModel.getRecordNumber()) {
                        XpadVFGTradeInfoQueryModel viewModel = new XpadVFGTradeInfoQueryModel();
                        viewModel.setCurrentIndex(mShowListBean.size() + "");
                        viewModel.setPageSize(PAGE_SIZE);
                        viewModel.set_refresh("false");
                        viewModel.setCurrencyCode(settleCurrency);
                        viewModel.setQueryType("4");
                        viewModel.setStartDate(mSelectParams.getStartDate());
                        viewModel.setEndDate(mSelectParams.getEndDate());
                        mTakeTradePresenter.psnXpadTakeTradeQuery(viewModel);
                    } else {
                        isPullToLoadMore = false;
                        refreshLayout.loadmoreCompleted(PullToRefreshLayout.NO_MORE_DATA);
                    }
                }
            }
        });
    }
    //保证金账户查询成功
    @Override
    public void psnXpadGetRegCurrencySuccess(List<String> result) {
        settleCurrency = result.get(0);

        XpadVFGTradeInfoQueryModel xpadVFGTradeInfoQueryModel = new XpadVFGTradeInfoQueryModel();
        xpadVFGTradeInfoQueryModel.setCurrencyCode(settleCurrency);
        xpadVFGTradeInfoQueryModel.setQueryType("4");
        xpadVFGTradeInfoQueryModel.setStartDate(ApplicationContext.getInstance().getCurrentSystemDate().toLocalDate().plusMonths(-MAX_QUERY_RANGE).plusDays(1).format(DateFormatters.dateFormatter1));
        xpadVFGTradeInfoQueryModel.setEndDate(ApplicationContext.getInstance().getCurrentSystemDate().toLocalDate().format(DateFormatters.dateFormatter1));
        xpadVFGTradeInfoQueryModel.set_refresh("true");
        xpadVFGTradeInfoQueryModel.setCurrentIndex("0");
        xpadVFGTradeInfoQueryModel.setPageSize(PAGE_SIZE);
        mTakeTradePresenter.psnXpadTakeTradeQuery(xpadVFGTradeInfoQueryModel);
    }
    //保证金账户查询失败
    @Override
    public void psnXpadGetRegCurrencyFail(BiiResultErrorException biiResultErrorException) {
        closeProgressDialog();
    }
    //双向宝斩仓交易列表查询成功
    @Override
    public void psnXpadTakeTradeQuerySuccess(XpadVFGTradeInfoQueryModel viewModel) {
        if(mCurrentSelectedPage == PAGE_INDEX) {
            closeProgressDialog();
        }
        this.xpadVFGTradeInfoQueryModel = viewModel;
        System.out.println("zhangcheng "+xpadVFGTradeInfoQueryModel.getRecordNumber());

        List<XpadVFGTradeInfoQueryModel.XpadPsnVFGTradeInfoQueryResultEntity> effectiveEntrustLists = xpadVFGTradeInfoQueryModel.getList();
        List<ShowListBean> transactionList = new ArrayList<ShowListBean>();

        conversationId = xpadVFGTradeInfoQueryModel.getConversationId();

        ll_no_data_query.setVisibility(View.GONE);
        if (!isPullToLoadMore) {
            mInfoQueryResultEntities.clear();
        }

        isPullToLoadMore = false;
        mInfoQueryResultEntities.addAll(effectiveEntrustLists);

        if(mInfoQueryResultEntities != null){
            for(int i = 0; i < mInfoQueryResultEntities.size(); i++){
                XpadVFGTradeInfoQueryModel.XpadPsnVFGTradeInfoQueryResultEntity infoTradEntitiy = mInfoQueryResultEntities.get(i);
                ShowListBean transactionBean = new ShowListBean();
                transactionBean.setChangeColor(true);

                mInfoQueryResultEntities.get(i).setSettleCurrecny(settleCurrency);
                TransQueryUtils queryUtils = new TransQueryUtils();

                LocalDateTime localDate = infoTradEntitiy.getLiquidationDate();
                LocalDate date = LocalDate.of(localDate.getYear(), localDate.getMonth(), localDate.getDayOfMonth());

                String formatTime = "";// 当前时间 MM月/yyyy
                String tempTime = "";// 上一次时间
                if (localDate != null) {
                    formatTime = localDate.format(DateFormatters.monthFormatter1);
                }
                if (i > 0) {
                    tempTime = mInfoQueryResultEntities.get(i-1).getLiquidationDate().format(DateFormatters.monthFormatter1);
                }

                if (tempTime.equals(formatTime)) {// child
                    ShowListBean item = new ShowListBean();
                    item.type = ShowListBean.CHILD;

                    item.setTitleID(ShowListConst.TITLE_WEALTH);

                    item.setTime(date);

                    String currencyCoupe = PublicCodeUtils.getGoldCurrencyCode(mActivity,infoTradEntitiy.getCurrency1().getCode()) + "/" +PublicCodeUtils.getGoldCurrencyCode(mActivity,infoTradEntitiy.getCurrency2().getCode());
                    item.setContentLeftAbove(currencyCoupe);
                    item.setContentLeftBelow(queryUtils.getExchangeTranType(infoTradEntitiy.getExchangeTranType()));
                    item.setContentLeftBelowAgain("斩仓时间 "+infoTradEntitiy.getLiquidationDate().format(DateFormatters.timeFormatter));
                    String amount = MoneyUtils.transMoneyFormat(infoTradEntitiy.getLiquidationAmount(),infoTradEntitiy.getSettleCurrecny());
                    String danwei = queryUtils.getCurrencyUnit(infoTradEntitiy.getCurrency1().getCode(),infoTradEntitiy.getCurrency2().getCode());
                    if (danwei.equals(infoTradEntitiy.getCurrency1().getCode())){
                        danwei = PublicCodeUtils.getGoldCurrencyCode(mActivity,infoTradEntitiy.getCurrency1().getCode());
                    }
                    item.setContentRightBelow(amount + danwei);

                    transactionList.add(item);
                } else {// group
                    for (int j = 0; j < 2; j++) {
                        ShowListBean itemFirst = new ShowListBean();
                        if (j == 0) {
                            itemFirst.type = ShowListBean.GROUP;
                            itemFirst.setGroupName(formatTime);
                            itemFirst.setTime(date);
                        } else {
                            itemFirst.type = ShowListBean.CHILD;

                            itemFirst.setTitleID(ShowListConst.TITLE_WEALTH);
                            itemFirst.setTime(date);

                            String currencyCoupe = PublicCodeUtils.getGoldCurrencyCode(mActivity,infoTradEntitiy.getCurrency1().getCode()) + "/" +PublicCodeUtils.getGoldCurrencyCode(mActivity,infoTradEntitiy.getCurrency2().getCode());

                            itemFirst.setContentLeftAbove(currencyCoupe);

                            itemFirst.setContentLeftBelow(queryUtils.getExchangeTranType(infoTradEntitiy.getExchangeTranType()));
                            itemFirst.setContentLeftBelowAgain("斩仓时间 "+infoTradEntitiy.getLiquidationDate().format(DateFormatters.timeFormatter));
                            String amount = MoneyUtils.transMoneyFormat(infoTradEntitiy.getLiquidationAmount(),infoTradEntitiy.getSettleCurrecny());
                            String danwei = queryUtils.getCurrencyUnit(infoTradEntitiy.getCurrency1().getCode(),infoTradEntitiy.getCurrency2().getCode());
                            if (danwei.equals(infoTradEntitiy.getCurrency1().getCode())){
                                danwei = PublicCodeUtils.getGoldCurrencyCode(mActivity,infoTradEntitiy.getCurrency1().getCode());
                            }
                            itemFirst.setContentRightBelow(amount + danwei);
                        }
                        transactionList.add(itemFirst);
                    }
                }
            }
        }

        mShowListBean.clear();
        mShowListBean.addAll(transactionList);
        mAdapter.setData(mShowListBean);
        refreshLayout.loadmoreCompleted(PullToRefreshLayout.SUCCEED);
//        handleNoData();
    }

    //双向宝斩仓交易列表查询失败
    @Override
    public void psnXpadTakeTradeQueryFail(BiiResultErrorException biiResultErrorException) {
        if(mCurrentSelectedPage == PAGE_INDEX) {
            closeProgressDialog();
        }
        mShowListBean.clear();
        mAdapter.setData(mShowListBean);
        handleNoData();
    }

    @Override
    public void setPresenter(TakeTradeContract.Presenter presenter) {

    }

    // 广播接收者，接收TransQueryFragment发送的广播（共2种通知类型，页面选中通知，筛选条件选择通知）
    class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            int noticeType = intent.getIntExtra("noticeType", -1);
            if (0 == noticeType){// 0表示“页面选中”通知
                handlePageSelectedNotice(intent);
            }else if (1 == noticeType){// 1表示“筛选条件选择”通知
                handleConditionSelectedNotice(intent);
            }
        }
    }
    // 处理“页面选中”通知
    private void handlePageSelectedNotice(Intent intent) {
        // 如果广播源不是来自TransQueryFragment，直接返回
        String broadcastSource = intent.getStringExtra("BroadcastSource");
        if (!"TransQueryFragment".equals(broadcastSource)) {
            return;
        }
        int selectPagePosition = intent.getIntExtra("selectPagePosition", -1);
        mCurrentSelectedPage = selectPagePosition;

    }
    // 处理“筛选条件选择”通知
    private void handleConditionSelectedNotice(Intent intent) {
        SelectParams selectParams = intent.getParcelableExtra("selectParams");
        int selectPagePosition = intent.getIntExtra("selectPagePosition", -1);
        settleCurrency = intent.getStringExtra("settleCurrency");
        if(selectPagePosition == PAGE_INDEX) { // 就是当前页面
            mSelectParams = selectParams;
            startQuery();
        }
    }
    // 开始查询（此方法由TransQueryFragment发起广播触发）
    public void startQuery() {
        if (mCurrentSelectedPage == PAGE_INDEX) {
            showLoadingDialog();
        }
        XpadVFGTradeInfoQueryModel viewModel = new XpadVFGTradeInfoQueryModel();
        viewModel.setCurrentIndex("0");
        viewModel.setPageSize(PAGE_SIZE);
        viewModel.set_refresh("true");
        viewModel.setStartDate(mSelectParams.getStartDate());
        viewModel.setEndDate(mSelectParams.getEndDate());
        viewModel.setQueryType("4");
        viewModel.setCurrencyCode(settleCurrency);
        mTakeTradePresenter.psnXpadTakeTradeQuery(viewModel);

    }

    // 处理有无数据时的情况
    private void handleNoData() {
        // 处理“数据是否为空”的情况
        if(mShowListBean.size() > 0) {
            ll_no_data_query.setVisibility(View.GONE);
        } else {
            ll_no_data_query.setVisibility(View.VISIBLE);
            tv_no_data.setText(isFisrtTimeEnter ? getResources().getString(R.string.boc_transfer_query_empty): getResources().getString(R.string.boc_transfer_select_empty));
        }
        /**
         * 这里一定要改回去，变成false
         */
        isFisrtTimeEnter = false;
    }

    @Override
    protected String getTitleValue() {
        return "斩仓交易";
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
    public boolean onBack() {
        TransQueryFragment transQueryFragment = findFragment(TransQueryFragment.class);
        if (transQueryFragment != null){
            return transQueryFragment.onBack();
        } else {
            return true;
        }
    }
    @Override
    public void onDestroy() {
        mTakeTradePresenter.unsubscribe();
        super.onDestroy();
        Log.d("TakeTradeFragment  +","onDestory");
        mActivity.unregisterReceiver(myReceiver);
    }
    @Override
    public void onStart() {
        super.onStart();
        Log.d("TakeTradeFragment  +","onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("TakeTradeFragment  +","onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("TakeTradeFragment  +","onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d("TakeTradeFragment  +","onStop");
    }

}
