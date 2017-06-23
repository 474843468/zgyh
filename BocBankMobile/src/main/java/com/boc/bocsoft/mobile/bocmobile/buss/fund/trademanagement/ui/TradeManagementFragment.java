package com.boc.bocsoft.mobile.bocmobile.buss.fund.trademanagement.ui;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.ApplicationConst;
import com.boc.bocsoft.mobile.bocmobile.ApplicationContext;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.MvpBussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.utils.DateTimePicker;
import com.boc.bocsoft.mobile.bocmobile.base.widget.SlipMenu.SlipDrawerLayout;
import com.boc.bocsoft.mobile.bocmobile.base.widget.refreshliseview.PullToRefreshLayout;
import com.boc.bocsoft.mobile.bocmobile.base.widget.selectview.selecttimerangenew.SelectTimeRangeNew;
import com.boc.bocsoft.mobile.bocmobile.base.widget.singlemoreselect.Content;
import com.boc.bocsoft.mobile.bocmobile.base.widget.singlemoreselect.SelectGridView;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.trademanagement.model.PsnFundQueryHistoryDetailModel;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.trademanagement.model.PsnFundQueryTransOntranModel;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.trademanagement.presenter.TradeManagementPresenter;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.utils.PublicUtils;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.transinquire.model.SelectParams;
import com.boc.bocsoft.mobile.common.utils.date.DateFormatters;
import com.boc.bocsoft.mobile.framework.widget.TitleBarView;
import com.boc.bocsoft.mobile.framework.widget.tabLayout.SlidingTabLayout;
import com.boc.bocsoft.mobile.framework.widget.tabLayout.TabLayoutPageAdapter;

import org.threeten.bp.LocalDate;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wy7105 on 2016/12/9.
 * 基金交易记录页面
 */
public class TradeManagementFragment extends MvpBussFragment<TradeManagementContract.Presenter> implements TradeManagementContract.View, ViewPager.OnPageChangeListener {

    private int currentPagePosition = 0; // 当前页面位置
    private final static int pageSize = ApplicationConst.PAGE_SIZE;
    private final static int MAX_QUERY_DATE = 12; // 最大查询起始日期为一年
    private final static int MAX_QUERY_RANGE = 3;// 最大时间跨度为三个月
    private boolean isSelectButtonRed = false; // 筛选按钮0是否是红色
    public boolean isTransPullToLoadMore = false;
    public boolean isHistoryPullToLoadMore = false;
    private boolean leftLoaded = false; //左滑加载
    private boolean rightLoaded = false; //右滑加载
    private int currentIndex;
    private String currentTransType; // 筛选出的当前的交易类型
    private List<Content> selectTransTypeList; // 交易类型筛选的数据
    private SelectParams mSelectParams; //页面的筛选参数
    private LocalDate startLocalDate; // 起始时间
    private LocalDate endLocalDate; // 结束时间
    private TextView tv_select; // “筛选”按钮
    private TitleBarView select_title_view; // 标题
    private TextView tv_result; //筛选行左侧文字
    private SlipDrawerLayout mDrawerLayout; //筛选侧滑组件
    private SelectTimeRangeNew selectTimeRangeNew; //选择时间范围组件(rightDrawer)
    private SlidingTabLayout lytTab; //tab切换
    private LinearLayout ll_choose_select;
    private LinearLayout ll_trans_type;
    private SelectGridView sgv_trans_type; //交易类型
    private LinearLayout llAddToLayout;
    private ViewPager vpTradeManagement; //viewpager
    private View rootView;
    private TabLayoutPageAdapter mPageAdapter;
    private TransitTradeListView mTransitTradeListView;
    private HistoryTradeListView mHistoryTradeListView;
    private PsnFundQueryTransOntranModel transOntranModel;
    private PsnFundQueryHistoryDetailModel historyDetailModel;
    private TradeManagementContract.Presenter mTradeManagementPresenter;

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        rootView = mInflater.inflate(R.layout.boc_fragment_trade_management, null);
        return rootView;
    }

    @Override
    public void initView() {
        ll_choose_select = (LinearLayout) rootView.findViewById(R.id.ll_choose_select); //筛选按钮行
        tv_result = (TextView) rootView.findViewById(R.id.tv_result);
        ll_choose_select.setVisibility(View.VISIBLE);
        mDrawerLayout = (SlipDrawerLayout) rootView.findViewById(R.id.drawer_layout_trade_management); //筛选侧滑布局
        selectTimeRangeNew = (SelectTimeRangeNew) rootView.findViewById(R.id.right_drawer_trade_management); //时间选择组件
        selectTimeRangeNew.setSingleSelect(false); //去掉时间单选框
        tv_select = (TextView) rootView.findViewById(R.id.tv_select); //筛选按钮
        select_title_view = (TitleBarView) rootView.findViewById(R.id.select_title_view);
        initTitleView();
        View selectTransLayout = View.inflate(mContext, R.layout.boc_view_fund_trade_inquire_select, null); //交易类型布局
        sgv_trans_type = (SelectGridView) selectTransLayout.findViewById(R.id.sgv_trans_type);//交易类型内容布局组件
        ll_trans_type = (LinearLayout) selectTransLayout.findViewById(R.id.ll_trans_type);//交易类型布局
        llAddToLayout = selectTimeRangeNew.getAddToTopLayout(); //在日期上面添加父布局
        llAddToLayout.setVisibility(View.VISIBLE);
        llAddToLayout.addView(selectTransLayout); //添加交易类型
        initTransTypeSelectView();  // 初始化交易类型选择
        lytTab = (SlidingTabLayout) rootView.findViewById(R.id.lyt_tab);
        vpTradeManagement = (ViewPager) rootView.findViewById(R.id.view_pager);
    }

    @Override
    public void initData() {
        mTradeManagementPresenter = new TradeManagementPresenter(this);
        mPageAdapter = new TabLayoutPageAdapter();
        mHistoryTradeListView = new HistoryTradeListView(mContext);
        mHistoryTradeListView.setFragment(this);
        mTransitTradeListView = new TransitTradeListView(mContext);
        mTransitTradeListView.setFragment(this);
        mPageAdapter.addPage(mHistoryTradeListView, getString(R.string.boc_fund_history_trade));
        mPageAdapter.addPage(mTransitTradeListView, getString(R.string.boc_fund_transit_trade));
        vpTradeManagement.setAdapter(mPageAdapter);
        lytTab.setViewPager(vpTradeManagement);
        lytTab.setOnPageChangeListener(this);
        initQueryDate();//初始化查询时间
        initSelectParams();//初始化选择参数
        historyDetailModel = new PsnFundQueryHistoryDetailModel();
        historyDetailModel = initFirstQueryHistoryparams(); //第一次展示历史列表
        transOntranModel = new PsnFundQueryTransOntranModel();
        transOntranModel = initFirstQueryTransparams();
        Bundle bundle = getArguments();
        currentIndex = bundle.getInt("currentIndex");
        lytTab.setCurrentTab(currentIndex);
        resetPageSelectConditionShow(currentIndex); //根据位置控制筛选栏的显示
        showLoadingDialog();
        if (currentIndex == 0)
            mTradeManagementPresenter.psnFundQueryHistoryDetail(historyDetailModel);
        if (currentIndex == 1) mTradeManagementPresenter.psnFundQueryTransOntran(transOntranModel);
    }

    //初始化第一次查询的历史交易上送参数
    private PsnFundQueryHistoryDetailModel initFirstQueryHistoryparams() {
        PsnFundQueryHistoryDetailModel model = new PsnFundQueryHistoryDetailModel();
        model.set_refresh("true");
        model.setCurrentIndex("0");
        model.setPageSize(String.valueOf(pageSize));
        model.setStartDate(mSelectParams.getStartDate());
        model.setEndDate(mSelectParams.getEndDate());
        model.setTransType(mSelectParams.getTransType());
        return model;
    }

    //初始化第一次查询的在途交易上送参数
    private PsnFundQueryTransOntranModel initFirstQueryTransparams() {
        PsnFundQueryTransOntranModel model = new PsnFundQueryTransOntranModel();
        model.set_refresh("true");
        model.setCurrentIndex("0");
        model.setPageSize(String.valueOf(pageSize));
        return model;
    }

    //tab滑动请求不同接口
    @Override
    public void onPageSelected(int position) {
        if (position == 0 && leftLoaded == false) {
            showLoadingDialog();
            mTradeManagementPresenter.psnFundQueryHistoryDetail(historyDetailModel); //调历史查询接口
        } else if (position == 1 && rightLoaded == false) {
            showLoadingDialog();
            mTradeManagementPresenter.psnFundQueryTransOntran(transOntranModel); //调在途查询接口
        }
    }

    //设置监听
    @Override
    public void setListener() {

        //筛选按钮监听
        tv_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.toggle(); //开启/关闭侧边栏
                resetPageSelectConditionShow(currentPagePosition); //页面筛选条件的显示
            }
        });

        //viewpager监听
        vpTradeManagement.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                currentPagePosition = position;
                resetPageSelectConditionShow(currentPagePosition); //控制筛选部分的显示
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        //历史页面的下拉刷新
        mHistoryTradeListView.refreshLayout.setOnLoadListener(new PullToRefreshLayout.OnLoadListener() {
            @Override
            public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
                if (historyDetailModel == null) {
                    PsnFundQueryHistoryDetailModel model = new PsnFundQueryHistoryDetailModel();
                    if (mSelectParams == null) {
                        model.setStartDate(ApplicationContext.getInstance().getCurrentSystemDate().toLocalDate().minusMonths(3).format(DateFormatters.dateFormatter1));
                        model.setEndDate(ApplicationContext.getInstance().getCurrentSystemDate().toLocalDate().format(DateFormatters.dateFormatter1));
                        model.setTransType("0");

                    } else {
                        model.setStartDate(mSelectParams.getStartDate());
                        model.setStartDate(mSelectParams.getEndDate());
                        model.setTransType(mSelectParams.getTransType());
                    }
                    model.set_refresh("true");
                    model.setCurrentIndex("0");
                    model.setPageSize(String.valueOf(pageSize));
                    mTradeManagementPresenter.psnFundQueryHistoryDetail(model);
                }
                //下拉加载更多
                if (mHistoryTradeListView.getCurrentIndex() < historyDetailModel.getRecordNumber()) {
                    isHistoryPullToLoadMore = true;
                    PsnFundQueryHistoryDetailModel model = new PsnFundQueryHistoryDetailModel();
                    if (mSelectParams == null) {
                        model.setStartDate(ApplicationContext.getInstance().getCurrentSystemDate().toLocalDate().minusMonths(3).format(DateFormatters.dateFormatter1));
                        model.setEndDate(ApplicationContext.getInstance().getCurrentSystemDate().toLocalDate().format(DateFormatters.dateFormatter1));
                        model.setTransType("0");
                    } else {
                        model.setStartDate(mSelectParams.getStartDate());
                        model.setStartDate(mSelectParams.getEndDate());
                        model.setTransType(mSelectParams.getTransType());
                    }
                    model.set_refresh("false");
                    model.setCurrentIndex(mHistoryTradeListView.getCurrentIndex() + "");
                    model.setPageSize(String.valueOf(pageSize));
                    mTradeManagementPresenter.psnFundQueryHistoryDetail(model);
                } else {
                    isHistoryPullToLoadMore = false;
                    mHistoryTradeListView.refreshLayout.loadmoreCompleted(PullToRefreshLayout.NO_MORE_DATA);
                }
                mHistoryTradeListView.setIsPullToLoadMore(isHistoryPullToLoadMore);
            }
        });

        //在途页面的下拉刷新
        mTransitTradeListView.refreshLayout.setOnLoadListener(new PullToRefreshLayout.OnLoadListener() {
            @Override
            public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
                if (transOntranModel == null) {
                    PsnFundQueryTransOntranModel model = new PsnFundQueryTransOntranModel();
                    model.set_refresh("true");
                    model.setCurrentIndex("0");
                    model.setPageSize(String.valueOf(pageSize));
                    mTradeManagementPresenter.psnFundQueryTransOntran(model);
                }
                if (mTransitTradeListView.getCurrentIndex() < transOntranModel.getRecordNumber()) {
                    isTransPullToLoadMore = true;
                    PsnFundQueryTransOntranModel model = new PsnFundQueryTransOntranModel();
                    model.set_refresh("false");
                    model.setCurrentIndex(mTransitTradeListView.getCurrentIndex() + "");
                    model.setPageSize(String.valueOf(pageSize));
                    mTradeManagementPresenter.psnFundQueryTransOntran(model);
                } else {
                    isTransPullToLoadMore = false;
                    mTransitTradeListView.refreshLayout.loadmoreCompleted(PullToRefreshLayout.NO_MORE_DATA);
                }
                mTransitTradeListView.setIsPullToLoadMore(isTransPullToLoadMore);
            }
        });

        // 交易类型筛选
        sgv_trans_type.setListener(new SelectGridView.ClickListener() {
            @Override
            public void setItemClick(AdapterView<?> parent, View view, int position, long id) {
                Content selectTransType = (Content) parent.getAdapter().getItem(position);
                currentTransType = selectTransType.getContentNameID();
            }
        });

        // 筛选重置按钮的监听
        selectTimeRangeNew.setResetListener(new SelectTimeRangeNew.ResetClickListener() {
            @Override
            public void resetClick() {
                resetTransTypeSelected();
            }
        });
        selectTimeRangeNew.setListener(new SelectTimeRangeNew.ClickListener() {
            @Override
            public void startClick() {
                judgeStartTimeAndSet(LocalDate.parse(selectTimeRangeNew.getStartDate(), DateFormatters.dateFormatter1));
            }

            @Override
            public void endClick() {
                judgeEndTimeAndSet(LocalDate.parse(selectTimeRangeNew.getEndDate(), DateFormatters.dateFormatter1));
            }

            @Override
            public void cancelClick() {
                mDrawerLayout.toggle();
            }

            //确认按钮监听
            @Override
            public void rightClick(boolean haveSelected, String content) {
                String startTime = selectTimeRangeNew.getStartDate();
                String endTime = selectTimeRangeNew.getEndDate();
                LocalDate start = LocalDate.parse(startTime, DateFormatters.dateFormatter1);
                LocalDate end = LocalDate.parse(endTime, DateFormatters.dateFormatter1);
                if (PublicUtils.judgeChoiceDateRange(start, end, MAX_QUERY_RANGE, TradeManagementFragment.this)) {
                    mDrawerLayout.toggle();
                    isHistoryPullToLoadMore = false;
                    startLocalDate = start;
                    endLocalDate = end;
                    // 如果是历史交易，则保存当前的筛选条件
                    if (currentPagePosition == 0) {
                        mSelectParams.setStartDate(startTime);
                        mSelectParams.setEndDate(endTime);
                        mSelectParams.setTransType(currentTransType);
                        isSelectButtonRed = true;
                    }
                    showLoadingDialog();
                    //筛选确认后请求数据
                    PsnFundQueryHistoryDetailModel model = new PsnFundQueryHistoryDetailModel();
                    model.set_refresh("false");
                    model.setCurrentIndex("0");
                    model.setPageSize(String.valueOf(pageSize));
                    model.setStartDate(mSelectParams.getStartDate());
                    model.setEndDate(mSelectParams.getEndDate());
                    model.setTransType(mSelectParams.getTransType());
                    mTradeManagementPresenter.psnFundQueryHistoryDetail(model);

                    // 设置筛选按钮的颜色为红色
                    tv_select.setTextColor(getResources().getColor(R.color.boc_text_color_red));
                    tv_select.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.boc_select_red), null);
                    tv_result.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    //撤单成功后返回在途页面并刷新数据
    @Override
    public void reInit() {
        super.reInit();
        if (vpTradeManagement.getCurrentItem() == 1) {
            mTradeManagementPresenter.psnFundQueryTransOntran(transOntranModel); //调在途查询接口
        }
    }

    //初始化交易类型
    private void initTransTypeSelectView() {
        String[] selectTransTypeData = {"全部", "购买", "赎回", "基金定投", "设置分红方式", "基金转换", "基金分红", "基金账户管理"};
        String[] selectTransTypeIdData = {"0", "1", "2", "3", "4", "5", "6", "7"};
        //单选条目赋值
        selectTransTypeList = new ArrayList<Content>();
        for (int i = 0; i < selectTransTypeData.length; i++) {
            Content item = new Content();
            item.setName(selectTransTypeData[i]);
            item.setContentNameID(selectTransTypeIdData[i]);
            if (i == 0) {
                currentTransType = selectTransTypeIdData[i];
                item.setSelected(true);
            }
            selectTransTypeList.add(item);
        }
        sgv_trans_type.setData(selectTransTypeList);
    }

    //初始化历史交易页面的筛选查询参数
    private void initSelectParams() {
        mSelectParams = new SelectParams();
        mSelectParams.setTransType("0");
        mSelectParams.setStartDate(ApplicationContext.getInstance().getCurrentSystemDate().toLocalDate().minusMonths(3).plusDays(1).format(
                DateFormatters.dateFormatter1));
        mSelectParams.setEndDate(ApplicationContext.getInstance().getCurrentSystemDate().toLocalDate().format(DateFormatters.dateFormatter1));
    }

    //初始化时间组件
    private void initQueryDate() {
        startLocalDate = ApplicationContext.getInstance().getCurrentSystemDate().toLocalDate().plusMonths(-MAX_QUERY_RANGE).plusDays(1);
        endLocalDate = ApplicationContext.getInstance().getCurrentSystemDate().toLocalDate();
        selectTimeRangeNew.setDefaultDate(startLocalDate.format(DateFormatters.dateFormatter1), endLocalDate.format(DateFormatters.dateFormatter1));
    }

    //控制交易类型的显示
    private void resetPageSelectTransTypeSelected(int position) {
        SelectParams selectParams = null;
        if (position == 0) selectParams = mSelectParams;
        // 重置为当前页面的交易类型
        for (int i = 0; i < selectTransTypeList.size(); i++) {
            Content item = selectTransTypeList.get(i);
            if (item.getContentNameID().equals(selectParams.getTransType())) {
                item.setSelected(true);
            } else {
                item.setSelected(false);
            }
        }
        sgv_trans_type.getAdapter().notifyDataSetChanged(); //更新交易记录列表内容
        currentTransType = selectParams.getTransType(); //获取当前交易类型
    }

    //页面筛选条件的显示
    private void resetPageSelectConditionShow(int position) {
        switch (position) {
            case 0: // 历史交易
                llAddToLayout.setVisibility(View.VISIBLE);
                selectTimeRangeNew.getTimeLayout().setVisibility(View.VISIBLE); //时间组件显示
                ll_trans_type.setVisibility(View.VISIBLE); //交易类型显示
                ll_choose_select.setVisibility(View.VISIBLE); //筛选布局显示
                resetPageSelectTransTypeSelected(position); // 控制交易类型显示为上一次的设置
                selectTimeRangeNew.setDefaultDate(mSelectParams.getStartDate(), mSelectParams.getEndDate());  // 控制时间一栏的显示
                // 控制筛选按钮是否红色
                if (isSelectButtonRed) {
                    tv_select.setTextColor(getResources().getColor(R.color.boc_text_color_red)); //设置文字颜色
                    tv_select.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.boc_select_red), null); //设置图片颜色
                } else {
                    tv_select.setTextColor(getResources().getColor(R.color.boc_text_color_common_gray));
                    tv_select.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.boc_select_gray), null);
                }
                break;
            case 1: // 在途交易
                llAddToLayout.setVisibility(View.GONE);
                ll_choose_select.setVisibility(View.GONE);
                selectTimeRangeNew.getTimeLayout().setVisibility(View.GONE);
                ll_trans_type.setVisibility(View.GONE);
                break;
        }
    }

    //交易类型重置
    private void resetTransTypeSelected() {
        for (int i = 0; i < selectTransTypeList.size(); i++) {
            Content item = selectTransTypeList.get(i);
            if (item.getContentNameID().equals("0")) { // 重置为"全部"
                item.setSelected(true);
            } else {
                item.setSelected(false);
            }
        }
        sgv_trans_type.getAdapter().notifyDataSetChanged();
        currentTransType = "0";
    }

    //起始日期的选择
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
                selectTimeRangeNew.setStartDate(strChoiceTime);
            }
        });
    }

    // 结束日期的选择
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
                selectTimeRangeNew.setEndDate(strChoiceTime);
            }
        });
    }

    private void initTitleView() {
        select_title_view.setStyle(R.style.titlebar_common_white);
        select_title_view.setTitle("交易记录");
        select_title_view.setRightImgBtnVisible(false);
        select_title_view.setLeftButtonOnClickLinster(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                titleLeftIconClick();
            }
        });
    }

    @Override
    protected boolean isHaveTitleBarView() {
        return false;
    }

    @Override
    protected String getTitleValue() {
        return getString(R.string.boc_purchase_result_transaction_record);
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
    protected TradeManagementContract.Presenter initPresenter() {
        return null;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    @Override
    public void psnFundQueryHistoryDetailSuccess(PsnFundQueryHistoryDetailModel viewModel) {
        closeProgressDialog();
        leftLoaded = true;
        mHistoryTradeListView.onLoadSuccess(viewModel);
    }

    @Override
    public void psnFundQueryHistoryDetailFail(BiiResultErrorException biiResultErrorException) {
        closeProgressDialog();
        mHistoryTradeListView.onLoadFailed();
    }

    @Override
    public void psnFundQueryTransOntranSuccess(PsnFundQueryTransOntranModel viewModel) {
        closeProgressDialog();
        rightLoaded = true;
        mTransitTradeListView.onLoadSuccess(viewModel);
    }

    @Override
    public void psnFundQueryTransOntranFail(BiiResultErrorException biiResultErrorException) {
        closeProgressDialog();
        mTransitTradeListView.onLoadFailed();
    }

    @Override
    public void setPresenter(TradeManagementPresenter tradeManagementPresenter) {

    }
}
