package com.boc.bocsoft.mobile.bocmobile.buss.buyandsellexchange.TradeQuery.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bii.bus.fess.model.PsnFessQueryHibsExchangeTrans.PsnFessQueryHibsExchangeTransParams;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.ApplicationContext;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.MvpBussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.utils.DateTimePicker;
import com.boc.bocsoft.mobile.bocmobile.base.utils.LogUtils;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;
import com.boc.bocsoft.mobile.bocmobile.base.utils.PublicCodeUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.SlipMenu.SlipDrawerLayout;
import com.boc.bocsoft.mobile.bocmobile.base.widget.refreshliseview.PullToRefreshLayout;
import com.boc.bocsoft.mobile.bocmobile.base.widget.selectview.selecttimerangenew.SelectTimeRangeNew;
import com.boc.bocsoft.mobile.bocmobile.base.widget.showlistview.PinnedSectionListView;
import com.boc.bocsoft.mobile.bocmobile.base.widget.showlistview.ShowListConst;
import com.boc.bocsoft.mobile.bocmobile.base.widget.showlistview.adapter.ShowListAdapter;
import com.boc.bocsoft.mobile.bocmobile.base.widget.showlistview.bean.ShowListBean;
import com.boc.bocsoft.mobile.bocmobile.buss.buyandsellexchange.TradeQuery.model.TradeQueryListModel;
import com.boc.bocsoft.mobile.bocmobile.buss.buyandsellexchange.TradeQuery.presenter.TradeQueryListPresenter;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.utils.PublicUtils;
import com.boc.bocsoft.mobile.common.utils.date.DateFormatters;
import com.boc.bocsoft.mobile.framework.widget.TitleBarView;

import org.threeten.bp.LocalDate;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * Created by wzn7074 on 2016/12/1.
 */
public class TradeQueryListFragment extends MvpBussFragment<TradeQueryListPresenter> implements TradeQueryListContract.View {

    //MVP结构
    private TradeQueryListModel mModel;
    private View mView;
    private TradeQueryListContract.Presenter mTradeQueryListPresenter;

    /**
     *  视图里面用到的组件
     * */
    private SlipDrawerLayout mDrawerLayout; // boc_fragment_buyandsellexchange_list; //根布局-侧滑组件
    private TitleBarView mTitleBar; //tv_buyandsellexchange_select; //标题栏
    private TextView exchangeRange; // tv_buyandsellexchange_list_range; //近三个月交易
    private TextView excQuerySelect; //tv_buyandsellexchange_list_select; //筛选
    private PullToRefreshLayout refreshLayout; //refresh_query; //上拉刷新
    private PinnedSectionListView pinnedSectionListView; //lv_history_query; //分组展示列表组件
    private ShowListAdapter mAdapter;
    private List<ShowListBean> mShowList;
    private LinearLayout noDataQuery; // no_data_query; //无结果页面
    private SelectTimeRangeNew mSelectTimeRangeNew; //right_drawer_exchange_select_time; //时间选择组件
    private LinearLayout mSelectBar; //l_buyandsellexchange_select_bar //筛选条
    //
//    // 上拉刷新
//    private PullToRefreshLayout refreshLayout;
//
//    //查询无数据
//    private LinearLayout ll_no_data_query;
//    private TextView tv_no_data;
//
//    // 查询列表组件
//    private PinnedSectionListView pinnedSectionListView;

//
//    //侧滑
//    private SlipDrawerLayout mAttCardDrawerLayout;
//    //时间筛选
//    private SelectTimeRangeNew attCardSelectTimeRangeNew;
//    private TextView excQuerySelect; //”筛选“按钮
//    private TextView exchangeRange;//"近三个月查询结果"按钮
//    private LinearLayout ll_atcard_choose_select;
//

    //起始时间和结束时间
    private LocalDate startLocalDate;
    private LocalDate endLocalDate;
    //时间选择器的开始时间和结束时间
    private String startTime;
    private String endTime;
    // 最大查询范围为3个月
    private final static int MAX_QUERY_RANGE = 3;
    //最大查询其实日期为一年
    private final static int MAX_QUERY_DATE = 12;
    // 页面索引
    private final static int PAGE_INDEX = 3;
    // 每页的条目数
    public static final String PAGE_SIZE = "5";
    List<TradeQueryListModel.TradeQueryResultEntity>
            mTradeQueryResultEntities = new ArrayList<TradeQueryListModel.TradeQueryResultEntity>(); //查询列表
    private boolean isPullToLoadMore; //是否上拉加载更多
    private boolean isFirstTimeEnter = true; //是否首次进入当前页面的标志
    private PsnFessQueryHibsExchangeTransParams mTransParams; //接口请求参数
    private int mCurrentSelectedPage = 0;
    private MyReceiver myReceiver;
    private String wznTag = "wzn7074-TradeQueryListFragment";  //test
    private String conversationId; //会话Id



    @Override
    protected View onCreateView(LayoutInflater mInfater) {
        LogUtils.d(wznTag, "in onCreateView");
        mView = mInfater.inflate(R.layout.boc_fragment_buyandsellexchange_list, null);
        return mView;
    }

    @Override
    public void initView() {
        //test
        LogUtils.d(wznTag, "in initView");

        //标题栏
        mTitleBar = (TitleBarView) mView.findViewById(R.id.tv_buyandsellexchange_select);
        //上拉刷新
        refreshLayout = (PullToRefreshLayout) mView.findViewById(R.id.refresh_query);  //todo：需要实现Pullable 接口
        //查询列表组件
        pinnedSectionListView = (PinnedSectionListView) mView.findViewById(R.id.lv_history_query);
        pinnedSectionListView.setShadowVisible(false);
        //无结果页面
        noDataQuery = (LinearLayout) mView.findViewById(R.id.no_data_query);
        //侧滑
        mDrawerLayout = (SlipDrawerLayout) mView.findViewById(R.id.boc_fragment_buyandsellexchange_list);
        //时间筛选组件
        mSelectTimeRangeNew = (SelectTimeRangeNew) mView.findViewById(R.id.right_drawer_exchange_select_time);
        //筛选按钮
        excQuerySelect = (TextView) mView.findViewById(R.id.tv_buyandsellexchange_list_select);
        //近3个月查询结果 显示
        exchangeRange = (TextView) mView.findViewById(R.id.tv_buyandsellexchange_list_range);
        //右侧更多按钮不显示
//        mTitleBarView.setRightImgBtnVisible(false);

        mSelectBar= (LinearLayout) mView.findViewById(R.id.ll_buyandsellexchange_select_bar);

        initTitleView();
    }

    @Override
    public void initData() {

        mTradeQueryListPresenter = new TradeQueryListPresenter(this);
        mModel = new TradeQueryListModel();

//        mTradeQueryListPresenter.psnTradeQueryListQuery(mModel); //// TODO: 2016/12/28  20:33:52 

        mShowList = new ArrayList<ShowListBean>();
        mAdapter = new ShowListAdapter(mContext, -1);
        pinnedSectionListView.saveAdapter(mAdapter);
        pinnedSectionListView.setAdapter(mAdapter);

        initTimePicker();//初始化时间选择器


//        initSelectParams();
        if (mCurrentSelectedPage == PAGE_INDEX) {
            showLoadingDialog("请稍后");
        }

        TradeQueryListModel mModel = new TradeQueryListModel();
        

  /*              testListView();

            "startDate": "2021/10/25",
                    "endDate": "2021/12/31",
                    "fessFlag": "00",
                    "currentIndex": "0",
                    "pageSize": "15",
                    "_refresh": "true"
    */


        mModel.setStartDate(mSelectTimeRangeNew.getStartDate());
        mModel.setEndDate(mSelectTimeRangeNew.getEndDate());
        mModel.setFessFlag("00");
        mModel.setCurrentIndex("0");
        mModel.setPageSize("15");
        mModel.set_refresh("true");

//        //test
//        LogUtils.d(wznTag, " 写死数据：  \"startDate\": \"2021/12/15\",\n" +
//                "                    \"endDate\": \"2021/12/21\",\n" +
//                "                    \"fessFlag\": \"00\",\n" +
//                "                    \"currentIndex\": \"0\",\n" +
//                "                    \"pageSize\": \"15\",\n" +
//                "                    \"_refresh\": \"true\"");

//        mModel.setCurrentIndex(mShowList.size()+ "");
//        mModel.setPageSize(PAGE_SIZE);
//        mModel.set_refresh("false");
//        mModel.setStartDate(mTransParams.getStartDate());
//        mModel.setEndDate(mTransParams.getEndDate());
//        mModel.setFessFlag(mTransParams.getFessFlag());


//        mAdapter.setData(mShowList);

        LogUtils.d(wznTag, "psn接口调用开始");
        mTradeQueryListPresenter.psnTradeQueryListQuery(mModel);

//        LogUtils.d(wznTag, "mShowListBean name is" + mShowList.toString());
//        LogUtils.d(wznTag, "mShowListBean 0 1 is " + mShowList.get(0).getContentLeftAbove() + mShowList.get(1).getContentLeftAbove());

    }

//    //初始化选择的参数
//    private void initSelectParams() {
//        mTransParams = new PsnFessQueryHibsExchangeTransParams();
//        mTransParams.setCurrentIndex(settleCurrency);
//
//        mTransParams.setStartDate(ApplicationContext
//                .getInstance()
//                .getInstance()
//                .getCurrentSystemDate()
//                .toLocalDate()
//                .minusMonths(3)
//                .plusDays(1)
//                .format(DateFormatters.dateFormatter1));
//
//        mTransParams.setEndDate(ApplicationContext
//        .getInstance()
//        .getCurrentSystemDate()
//        .toLocalDate()
//        .format(DateFormatters.dateFormatter1));
//    }


    @Override
    public void setListener() {
        IntentFilter intentFilter = new IntentFilter("exchangeTransQuery");
        myReceiver = new MyReceiver();
        mActivity.registerReceiver(myReceiver, intentFilter);

        //点击进入详情
        pinnedSectionListView.setListener(new PinnedSectionListView.ClickListener() {
            @Override
            public void onItemClickListener(int position) {
                TradeQueryListModel.TradeQueryResultEntity tradeQueryResultEntity = mTradeQueryResultEntities.get(position);
                Bundle bundle = new Bundle();
                bundle.putParcelable("tradeQueryResultEntity", tradeQueryResultEntity);
                bundle.putString("conversationId", conversationId);
                bundle.putInt("selectPagePosition", PAGE_INDEX);
                TradeQueryDetailFragment mTradeQueryDetailFragment = new TradeQueryDetailFragment();
                mTradeQueryDetailFragment.setArguments(bundle);
                start(mTradeQueryDetailFragment);
            }
        });

        //上拉加载更多
        refreshLayout.setOnLoadListener(new PullToRefreshLayout.OnLoadListener() {
            @Override
            public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
                isPullToLoadMore = true;
                if (mModel != null) {
                    if (mTradeQueryResultEntities.size() < mModel.getRecordNumber()) {
                        TradeQueryListModel viewModel = new TradeQueryListModel();
                        LogUtils.d(wznTag, "mShowList size is" + mShowList.size());
                        viewModel.setCurrentIndex(mShowList.size() + "");
                        viewModel.setPageSize(PAGE_SIZE);
                        viewModel.set_refresh("false");
                        viewModel.setStartDate(mTransParams.getStartDate());
                        viewModel.setEndDate(mTransParams.getEndDate());
                        mTradeQueryListPresenter.psnTradeQueryListQuery(viewModel);
                    } else {
                        isPullToLoadMore = false;
                        refreshLayout.loadmoreCompleted(PullToRefreshLayout.NO_MORE_DATA);
                    }
                }
            }

        });

        //筛选按钮
        excQuerySelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.toggle();
                exchangeRange.setVisibility(View.GONE);
            }
        });

        //筛选重置按钮
        mSelectTimeRangeNew.setResetListener(new SelectTimeRangeNew.ResetClickListener() {

            @Override
            public void resetClick() {

                LogUtils.d(wznTag, "筛选重置"); //test
            }

        });

        //时间选择器
        mSelectTimeRangeNew.setListener(new SelectTimeRangeNew.ClickListener() {
            @Override
            public void startClick() {
                judgeStartTimeAndSet(LocalDate.parse(mSelectTimeRangeNew.getStartDate(), DateFormatters.dateFormatter1));
            }

            @Override
            public void endClick() {
                judgeEndTimeAndSet(LocalDate.parse(mSelectTimeRangeNew.getEndDate(), DateFormatters.dateFormatter1));
            }

            @Override
            public void cancelClick() {
                mDrawerLayout.toggle();
            }

            @Override
            public void rightClick(boolean haveSelected, String content) {
                startTime = mSelectTimeRangeNew.getStartDate();
                endTime = mSelectTimeRangeNew.getEndDate();

                mModel.setStartDate(startTime);
                mModel.setEndDate(endTime);
                mTradeQueryListPresenter.psnTradeQueryListQuery(mModel);

                excQuerySelect.setTextColor(getResources().getColor(R.color.boc_text_color_red));
                excQuerySelect.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.boc_select_red), null);
                mDrawerLayout.toggle();

            }
        });

    }


    @Override
    public void setPresenter(TradeQueryListContract.Presenter presenter) {

    }

    /**
     * 查询成功
     *
     * @param model TradeQueryListModel
     */
    @Override
    public void psnFessQueryHibsExchangeTransSuccess(TradeQueryListModel model) {
        LogUtils.d(wznTag, "get PsnFessQueryHibsExchangeTransSuccess to do something "); //test

        if (mCurrentSelectedPage == PAGE_INDEX) {
            closeProgressDialog();
        }
        this.mModel = model;
        LogUtils.d(wznTag, "mModel.getRecordNumber() is " + mModel.getRecordNumber()); //test

        List<TradeQueryListModel.TradeQueryResultEntity> queryLists = mModel.getList();
        List<ShowListBean> HistoryList = new ArrayList<ShowListBean>();

        conversationId = mModel.getConversationId();

        if (!isPullToLoadMore) {
            mTradeQueryResultEntities.clear();
        }

        isPullToLoadMore = false;
        mTradeQueryResultEntities.addAll(queryLists);

        if (mTradeQueryResultEntities != null) {
            for (int i = 0; i < mTradeQueryResultEntities.size(); i++) {

                String formatTime = "";// 当前时间 MM月/yyyy
                String tempTime = "";// 上一次时间

                TradeQueryListModel.TradeQueryResultEntity mResultEntity = mTradeQueryResultEntities.get(i);
                ShowListBean transactionBean = new ShowListBean();
                //改变？？颜色
                transactionBean.setChangeColor(true);

                LocalDate localDate = LocalDate.parse(mResultEntity.getPaymentDate(), DateFormatters.dateFormatter1);


                /**
                 * 根据返回的列表中数据 格式化为所需显示的格式
                 * */
                String mCurrencyCode = PublicCodeUtils.getCurrency(mContext, mModel.getList().get(i).getCurrencyCode());//币种
                String mAmount = MoneyUtils.transMoneyFormat(mResultEntity.getAmount(), mModel.getList().get(i).getCurrencyCode());//外币交易金额 将传入金额保留小数点后两位，四舍五入，没有小数的加.00.如果是日元则没有小数
                String mCashRemit = PublicCodeUtils.getCashSpot(mContext, mModel.getList().get(i).getCashRemit());
                String mTransType = PublicCodeUtils.getFessTransType(mContext, mModel.getList().get(i).getTransType());
                String mRawStatus = mModel.getList().get(i).getStatus();
                String mStatus = "";
                    if (mRawStatus.equals("00")) {
                        mStatus = "";
                    } else if (mRawStatus.equals("01")) {
                        mStatus = "失败";
                    } else if (mRawStatus.equals("02")) {
                        mStatus = "未明";
                    }//交易状态


                if (localDate != null) {
                    formatTime = localDate.format(DateFormatters.monthFormatter1);
                }
                if (i > 0) {
                    LocalDate lastDate = localDate.parse(mTradeQueryResultEntities.get(i - 1).getPaymentDate(), DateFormatters.dateFormatter1);
                    tempTime = lastDate.format(DateFormatters.monthFormatter1);
                }

                if (tempTime.equals(formatTime)) {//child
                    ShowListBean item = new ShowListBean();
                    item.type = ShowListBean.CHILD;
                    item.setTitleID(ShowListConst.TITLE_WEALTH); //todo：ListView的布局需要考察
                    item.setTime(localDate);
//                    //test
//                    LogUtils.d(wznTag, "交易类型 转换前 " + mModel.getList().get(i).getTransType() + "  转换后 " + mTransType);
//                    LogUtils.d(wznTag, "结汇 " + PublicCodeUtils.getFessTransType(mContext, "01"));
//                    LogUtils.d(wznTag, "购汇 " + PublicCodeUtils.getFessTransType(mContext, "11"));
                    /**
                     * 填充数据至对应位置
                     * */
                    item.setContentLeftAbove(mTransType);
                    item.setContentRightBelow(mCurrencyCode + " " + mCashRemit + " " + mAmount);
                    item.setContentRightAbove(mStatus);

                    HistoryList.add(item);

                    LogUtils.d(wznTag, i +"child");//test

                } else {//group 相同日期合并之后的头
                    for (int j = 0; j < 2; j++) {
                        ShowListBean itemFirst = new ShowListBean();
                        if (j == 0) {
                            itemFirst.type = ShowListBean.GROUP;
                            //test
                            LogUtils.d(wznTag, i +"group" + j);

                            itemFirst.setGroupName(formatTime);
                            itemFirst.setTime(localDate);
                            //test
                            LogUtils.d(wznTag, "group itemFirst.setGroupName(formatTime) " + itemFirst.getGroupName());
                            LogUtils.d(wznTag, "group itemFirst.setTime(localDate) " + itemFirst.getTime());
                            LogUtils.d(wznTag, i +"child");
                        } else {
                            itemFirst.type = ShowListBean.CHILD;
                            itemFirst.setTitleID(ShowListConst.TITLE_WEALTH);
                            itemFirst.setTime(localDate);

                            /**
                             * 填充数据至对应位置
                             * */
                            itemFirst.setContentLeftAbove(mTransType);
                            itemFirst.setContentRightBelow(mCurrencyCode + " " + mCashRemit + " " + mAmount);
                            itemFirst.setContentRightAbove(mStatus);
                            //test
//                            LogUtils.d(wznTag, "child itemFirst.setGroupName(formatTime) " + itemFirst.getGroupName());
//                            LogUtils.d(wznTag, "child itemFirst.setTime(localDate) " + itemFirst.getTime());
                        }
                        HistoryList.add(itemFirst);
                    }
                    mAdapter.setData(mShowList);

                }
            }

        }
        mShowList.clear();
        mShowList.addAll(HistoryList);
        mAdapter.setData(mShowList);
        refreshLayout.loadmoreCompleted(PullToRefreshLayout.SUCCEED);
    }

    @Override
    public void psnFessQueryHibsExchangeTransFail(BiiResultErrorException biiResultErrorException) {
        //test
        LogUtils.d(wznTag, "psnFessQueryHibsExchangeTransFail is running!");

        pinnedSectionListView.setVisibility(View.GONE);
        noDataQuery.setVisibility(View.VISIBLE );

    }

    @Override
    protected boolean getTitleBarRed() {
        return false;
    }

    @Override
    protected boolean isHaveTitleBarView() {
        return false;
    } //不显示标题栏

    class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            //// TODO: 2016/12/14
        }
    }

    @Override
    protected TradeQueryListPresenter initPresenter() {
        return null;
    }


    /**
     * 初始化时间选择器
     */
    private void initTimePicker() {
        startLocalDate = ApplicationContext.getInstance().getCurrentSystemDate().toLocalDate().plusMonths(-MAX_QUERY_RANGE).plusDays(1);
        endLocalDate = ApplicationContext.getInstance().getCurrentSystemDate().toLocalDate();
        mSelectTimeRangeNew.setDefaultDate(startLocalDate.format(DateFormatters.dateFormatter1), endLocalDate.format(DateFormatters.dateFormatter1));
    }

    /**
     * 起始日期的选择
     */
    private void judgeStartTimeAndSet(LocalDate currentDate) {
        DateTimePicker.showDatePick(mContext, currentDate, DateFormatters.dateFormatter1, new DateTimePicker.DatePickCallBack() {
            @Override
            public void onChoiceDateSet(String strChoiceTime, LocalDate choiceDate) {
                if (ApplicationContext.getInstance().getCurrentSystemDate().toLocalDate().isBefore(choiceDate)) {
                    showErrorDialog(getResources().getString(R.string.boc_account_transdetail_start_before));
                    return;
                }
                if (PublicUtils.isCompareDateRange(choiceDate, ApplicationContext.getInstance().getCurrentSystemDate().toLocalDate(), MAX_QUERY_DATE)) {
                    showErrorDialog(getResources().getString(R.string.boc_account_transdetail_start_year_change, PublicUtils.changeNumberToUpper(MAX_QUERY_DATE / 12)));
                    return;
                }
                mSelectTimeRangeNew.setStartDate(strChoiceTime);
            }
        });
    }

    /**
     * 结束日期的选择
     */
    private void judgeEndTimeAndSet(LocalDate currentDate) {
        DateTimePicker.showDatePick(mContext, currentDate, DateFormatters.dateFormatter1, new DateTimePicker.DatePickCallBack() {
            @Override
            public void onChoiceDateSet(String strChoiceTime, LocalDate choiceDate) {
                if (ApplicationContext.getInstance().getCurrentSystemDate().toLocalDate().isBefore(choiceDate)) {
                    showErrorDialog(getResources().getString(R.string.boc_account_transdetail_end_before));
                    return;
                }
                if (PublicUtils.isCompareDateRange(choiceDate, ApplicationContext.getInstance().getCurrentSystemDate().toLocalDate(), MAX_QUERY_DATE)) {
                    showErrorDialog(getResources().getString(R.string.boc_account_transdetail_end_year_change, PublicUtils.changeNumberToUpper(MAX_QUERY_DATE / 12)));
                    return;
                }
                mSelectTimeRangeNew.setEndDate(strChoiceTime);
            }
        });
    }


    /**
     * 初始化Title
     */
    private void initTitleView() {
        mTitleBar.setStyle(R.style.titlebar_common_white);
        mTitleBar.setTitle("交易明细");
        mTitleBar.setRightImgBtnVisible(false);
        mTitleBar.setLeftButtonOnClickLinster(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                titleLeftIconClick();
            }
        });
    }

    //test:测试listview  向其填充数据
    private void testListView() {
        for (int i = 0; i < 3; i++) {
            ShowListBean mShowListBean = new ShowListBean();
            mShowListBean.type = ShowListBean.CHILD;
            mShowListBean.setTitleID(10);
            mShowListBean.setTime(LocalDate.now());
            mShowListBean.setContentLeftAbove("左上" + i);
            mShowListBean.setContentLeftBelow("左下");
            mShowListBean.setContentRightAbove("右上");
            mShowListBean.setContentRightBelow("右下");

            mShowList.add(mShowListBean);
        }

        for (int i = 0; i < 2; i++) {
            ShowListBean mShowListBean = new ShowListBean();
            mShowListBean.type = ShowListBean.CHILD;
            mShowListBean.setTitleID(3);
            mShowListBean.setTime(LocalDate.now());
            mShowListBean.setContentLeftAbove("左上1" + i);
            mShowListBean.setContentLeftBelow("左下1");
            mShowListBean.setContentRightAbove("右上1");
            mShowListBean.setContentRightBelow("右下1");

            mShowList.add(mShowListBean);
        }

    }
}
