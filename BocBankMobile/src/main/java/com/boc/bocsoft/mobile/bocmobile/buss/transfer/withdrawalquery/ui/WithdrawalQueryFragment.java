package com.boc.bocsoft.mobile.bocmobile.buss.transfer.withdrawalquery.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.ApplicationConst;
import com.boc.bocsoft.mobile.bocmobile.ApplicationContext;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.MvpBussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.utils.DateTimePicker;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.SlipMenu.SlipDrawerLayout;
import com.boc.bocsoft.mobile.bocmobile.base.widget.refreshliseview.PullToRefreshLayout;
import com.boc.bocsoft.mobile.bocmobile.base.widget.selectaccoutview.SelectAccountButton;
import com.boc.bocsoft.mobile.bocmobile.base.widget.selectview.selecttimerangenew.SelectTimeRangeNew;
import com.boc.bocsoft.mobile.bocmobile.base.widget.showlistview.PinnedSectionListView;
import com.boc.bocsoft.mobile.bocmobile.base.widget.showlistview.ShowListConst;
import com.boc.bocsoft.mobile.bocmobile.base.widget.showlistview.adapter.ShowListAdapter;
import com.boc.bocsoft.mobile.bocmobile.base.widget.showlistview.bean.ShowListBean;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.utils.PublicUtils;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.utils.ResultStatusUtils;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.withdraw.ui.MobileWithdrawFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.withdrawalquery.model.WithdrawalQueryDetailInfoViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.withdrawalquery.model.WithdrawalQueryViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.withdrawalquery.presenter.WithdrawalQueryPresenter;
import com.boc.bocsoft.mobile.common.utils.NumberUtils;
import com.boc.bocsoft.mobile.common.utils.date.DateFormatters;
import com.boc.bocsoft.mobile.framework.widget.TitleBarView;

import org.threeten.bp.LocalDate;

import java.util.ArrayList;
import java.util.List;


/**
 * 手机取款-取款查询
 * Created by wangf on 2016/6/21.
 */
public class WithdrawalQueryFragment extends MvpBussFragment<WithdrawalQueryContract.Presenter> implements WithdrawalQueryContract.View {

    /**
     * 根布局
     */
    private View mRootView;

    /**
     * fragment的页面
     */
    //详情的ListView
    PinnedSectionListView withDrawalListView;
    ShowListAdapter mAdapter;
    //详情的上拉刷新
    PullToRefreshLayout withDrawalRefresh;
    //侧滑菜单
    SelectTimeRangeNew rightDrawer;
    //无数据的TextView
    private TextView tvNoData;


    /**
     * listView的Head
     */
    private View headView;
    private TitleBarView queryTitleView;
    //账户选择
    private SelectAccountButton selectAccountButton;
    //筛选的LinearLayout
    private LinearLayout llWithDrawalSelect;
    //筛选处的范围
    private TextView tvWithDrawalRange;
    //筛选
    private TextView tvWithDrawalSelect;
    //筛选的imageView
    private ImageView ivWithDrawalSelect;


    /**
     * 侧滑菜单
     */
    private SlipDrawerLayout mDrawerLayout;

    //起始时间
    private LocalDate startLocalDate;
    //结束时间
    private LocalDate endLocalDate;

//    //取款查询service通信处理类
//    private WithdrawalQueryPresenter mWithdrawalQueryPresenter;
    //取款查询UI层model
    private WithdrawalQueryViewModel mWithdrawalQueryViewModel;

    /**
     * 加载相关
     */
    //当前加载页码
    private int pageCurrentIndex;
    //每页大小
    private static int pageSize;

    /**
     * 查询范围相关
     */
    //最大查询范围为3个月
    private final static int MAX_QUERY_RANGE = 3;
    //最大查询起始日期为一年
    private final static int MAX_QUERY_DATE = 12;

    //ListView所需要的List
    private List<ShowListBean> listViewBeanList;

    //用户点击的itemposition,用于向详情页面传数据
    private int itemPosition;

    //判断是否是筛选
    private boolean isSelectData;
    //判断是否是上拉加载
    private boolean isPullToRefresh;

    public static final String WITHDRAWAL_QUERY_TYPE = "WithdrawalQueryType";
    //直接查询所有数据
    public static final int WITHDRAWAL_TYPE_ALL = 0;
    //从取款结果页进入查询
    public static final int WITHDRAWAL_TYPE_COMMON = 1;
    //当前所查询类型
    private int currentWithdrawalType;


    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        mRootView = View.inflate(mContext, R.layout.boc_fragment_transfer_query, null);

        currentWithdrawalType = getArguments().getInt(WITHDRAWAL_QUERY_TYPE, WITHDRAWAL_TYPE_ALL);

        return mRootView;
    }

    @Override
    public void beforeInitView() {

    }

    @Override
    protected boolean isHaveTitleBarView() {
        return false;
    }


    @Override
    public void initView() {
        //fragment页面
        withDrawalListView = (PinnedSectionListView) mRootView.findViewById(R.id.lv_transfer_query);
        withDrawalRefresh = (PullToRefreshLayout) mRootView.findViewById(R.id.refresh_transfer_query);
        tvNoData = (TextView) mRootView.findViewById(R.id.no_data_transfer_query);

        headView = LayoutInflater.from(mContext).inflate(R.layout.boc_head_account_transdetail, null);

        //页面title初始化
        initQueryTitleView();

        //账户选择
        selectAccountButton = (SelectAccountButton) headView.findViewById(R.id.select_account_button);
        selectAccountButton.setVisibility(View.GONE);

        //筛选
        llWithDrawalSelect = (LinearLayout) headView.findViewById(R.id.ll_transdetail_select);
        tvWithDrawalSelect = (TextView) headView.findViewById(R.id.tv_transdetail_select);
        ivWithDrawalSelect = (ImageView) headView.findViewById(R.id.iv_transdetail_select);
        tvWithDrawalRange = (TextView) headView.findViewById(R.id.tv_transdetail_range);

        //侧滑菜单
        mDrawerLayout = (SlipDrawerLayout) mRootView.findViewById(R.id.drawer_layout_transfer_query);
        rightDrawer = (SelectTimeRangeNew) mRootView.findViewById(R.id.right_drawer_transfer_query);

        withDrawalListView.addHeaderView(headView, null, false);
        mAdapter = new ShowListAdapter(mContext, -1);
        withDrawalListView.saveAdapter(mAdapter);
        withDrawalListView.setShadowVisible(false);
        withDrawalListView.setAdapter(mAdapter);
//        withDrawalListView.addHeaderView(headView, null, false);
//        withDrawalListView.setAdapter();

    }


    /**
     * 初始化Title
     */
    private void initQueryTitleView(){
        queryTitleView = (TitleBarView)headView.findViewById(R.id.select_title_view);
        queryTitleView.setStyle(R.style.titlebar_common_white);
        queryTitleView.setTitle(getResources().getString(R.string.boc_transfer_withdrawal_query_title));
        queryTitleView.setRightImgBtnVisible(false);

        queryTitleView.setLeftButtonOnClickLinster(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                titleLeftIconClick();
            }
        });
    }


    @Override
    public void initData() {
        pageSize = ApplicationConst.PAGE_SIZE;
        pageCurrentIndex = 0;

        startLocalDate = ApplicationContext.getInstance().getCurrentSystemDate().toLocalDate().plusMonths(-MAX_QUERY_RANGE).plusDays(1);
        endLocalDate = ApplicationContext.getInstance().getCurrentSystemDate().toLocalDate();
        rightDrawer.setDefaultDate(startLocalDate.format(DateFormatters.dateFormatter1),
                endLocalDate.format(DateFormatters.dateFormatter1));
        tvWithDrawalRange.setText(getResources().getString(R.string.boc_common_query_month_3));


//        mWithdrawalQueryPresenter = new WithdrawalQueryPresenter(this);
        mWithdrawalQueryViewModel = new WithdrawalQueryViewModel();
        listViewBeanList = new ArrayList<ShowListBean>();

        queryWithdrawalQueryList();
    }


    @Override
    public void setListener() {
        //筛选layout的点击事件
        llWithDrawalSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectStatustChange();
                rightDrawer.setClickSelectDefaultData();
                mDrawerLayout.toggle();
            }
        });

        //筛选--侧滑菜单
        rightDrawer.setListener(new SelectTimeRangeNew.ClickListener() {
            @Override
            public void startClick() {
                judgeStartTimeAndSet(LocalDate.parse(rightDrawer.getStartDate(), DateFormatters.dateFormatter1));
            }

            @Override
            public void endClick() {
                judgeEndTimeAndSet(LocalDate.parse(rightDrawer.getEndDate(), DateFormatters.dateFormatter1));
            }

            @Override
            public void cancelClick() {
                mDrawerLayout.toggle();
            }

            @Override
            public void rightClick(boolean haveSelected, String content) {
                if (haveSelected) {
                    if (content.contains("月")){
                        tvWithDrawalRange.setText(content.replace("月", "个月") + "查询结果");
                    }else{
                        tvWithDrawalRange.setText(content + "查询结果");
                    }
                } else {
                    tvWithDrawalRange.setText("");
                }

                String startTime = rightDrawer.getStartDate();
                String endTime = rightDrawer.getEndDate();
                startLocalDate = LocalDate.parse(startTime, DateFormatters.dateFormatter1);
                endLocalDate = LocalDate.parse(endTime, DateFormatters.dateFormatter1);
                if (PublicUtils.judgeChoiceDateRange(startLocalDate, endLocalDate, MAX_QUERY_RANGE, WithdrawalQueryFragment.this)) {
                    mDrawerLayout.toggle();

                    listViewBeanList.clear();
                    if (mWithdrawalQueryViewModel.getList() != null) {
                        mWithdrawalQueryViewModel.getList().clear();
                    }
                    pageCurrentIndex = 0;
                    queryWithdrawalQueryList();
                    isSelectData = true;
                    isPullToRefresh = false;
                }
            }
        });

        rightDrawer.setResetListener(new SelectTimeRangeNew.ResetClickListener() {
            @Override
            public void resetClick() {
                // modify by wangf on 2016-12-4 10:39:24 组件控制重置的状态
//                rightDrawer.setClickSelectDefaultData();
            }
        });

        //listView的item点击事件
        withDrawalListView.setListener(new PinnedSectionListView.ClickListener() {
            @Override
            public void onItemClickListener(int position) {
                itemPosition = position;
                queryWithdrawalQueryDetailInfo(position);
            }
        });

        //上拉加载
        withDrawalRefresh.setOnLoadListener(new PullToRefreshLayout.OnLoadListener() {
            @Override
            public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
                startPresenter();
                if (mWithdrawalQueryViewModel.getList() != null) {
                    if (mWithdrawalQueryViewModel.getList().size() < mWithdrawalQueryViewModel.getRecordNumber()) {
                        isPullToRefresh = true;
                        queryWithdrawalQueryList();
                    } else {
                        withDrawalRefresh.loadmoreCompleted(PullToRefreshLayout.NO_MORE_DATA);
                    }
                } else {
                    listViewBeanList.clear();
                    pageCurrentIndex = 0;
                    isPullToRefresh = true;
                    queryWithdrawalQueryList();
                }
            }
        });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
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
                    showErrorDialog(getResources().getString(R.string.boc_account_transdetail_start_year_change, PublicUtils.changeNumberToUpper(MAX_QUERY_DATE/12)));
                    return;
                }
                rightDrawer.setStartDate(strChoiceTime);
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
                    showErrorDialog(getResources().getString(R.string.boc_account_transdetail_end_year_change, PublicUtils.changeNumberToUpper(MAX_QUERY_DATE/12)));
                    return;
                }
                rightDrawer.setEndDate(strChoiceTime);
            }
        });
    }

//    /**
//     * 根据是否有数据显示筛选按钮的颜色
//     * 若查询数据为空，则查询按钮需要变成红色
//     *
//     * @param haveDate
//     */
//    private void haveDataSelectStatust(boolean haveDate) {
//        if (haveDate) {
//            tvWithDrawalSelect.setTextColor(getResources().getColor(R.color.boc_text_color_common_gray));
//            ivWithDrawalSelect.setImageResource(R.drawable.boc_select_gray);
//        } else {
//            tvWithDrawalSelect.setTextColor(getResources().getColor(R.color.boc_text_color_red));
//            ivWithDrawalSelect.setImageResource(R.drawable.boc_select_red);
//        }
//    }

    /**
     * 筛选按钮的颜色改变
     */
    private void selectStatustChange() {
        tvWithDrawalSelect.setTextColor(getResources().getColor(R.color.boc_text_color_red));
        ivWithDrawalSelect.setImageResource(R.drawable.boc_select_red);
    }

    /**
     * 调用接口，查询取款列表数据
     */
    private void queryWithdrawalQueryList() {
        if (pageCurrentIndex == 0) {
            //开启加载对话框
            showLoadingDialog();
        }
        getPresenter().queryWithdrawalQueryList(buildWithdrawalQueryViewModel());
    }

    /**
     * 调用接口，查询取款详情数据
     */
    private void queryWithdrawalQueryDetailInfo(int position) {
        //开启加载对话框
        showLoadingDialog();
        getPresenter().queryWithdrawalDetailInfo(buildWithdrawalQueryDetailInfoViewModel(position));
    }


    /**
     * 封装页面数据--取款查询
     *
     * @return
     */
    private WithdrawalQueryViewModel buildWithdrawalQueryViewModel() {
        mWithdrawalQueryViewModel.setStartDate(startLocalDate.format(DateFormatters.dateFormatter1));//起始日期
        mWithdrawalQueryViewModel.setEndDate(endLocalDate.format(DateFormatters.dateFormatter1));//结束日期
        mWithdrawalQueryViewModel.setPageSize(pageSize);//页面大小
        mWithdrawalQueryViewModel.setCurrentIndex(pageCurrentIndex);//当前页索引
        return mWithdrawalQueryViewModel;
    }

    /**
     * 封装页面数据--详情
     *
     * @return
     */
    private WithdrawalQueryDetailInfoViewModel buildWithdrawalQueryDetailInfoViewModel(int position) {
        WithdrawalQueryDetailInfoViewModel infoViewModel = new WithdrawalQueryDetailInfoViewModel();
        infoViewModel.setReceiptAmount(mWithdrawalQueryViewModel.getList().get(position).getRemitAmount());//交易金额
        infoViewModel.setTransactionId(mWithdrawalQueryViewModel.getList().get(position).getTransactionId());//网银交易序号
        infoViewModel.setCurrencyCode(mWithdrawalQueryViewModel.getList().get(position).getCurrencyCode());//交易币种
        infoViewModel.setPayeeMobile(mWithdrawalQueryViewModel.getList().get(position).getPayeeMobile());//收款人手机号
        infoViewModel.setPayeeName(mWithdrawalQueryViewModel.getList().get(position).getPayeeName());//收款人姓名
        return infoViewModel;
    }


    /**
     * 将接口返回数据封装成ListView所需要的model
     *
     * @param withdrawalQueryViewModel
     */
    private void copyResult2TransBean(WithdrawalQueryViewModel withdrawalQueryViewModel) {
        listViewBeanList.clear();
        for (int i = 0; i < withdrawalQueryViewModel.getList().size(); i++){
            LocalDate localDate = withdrawalQueryViewModel.getList().get(i).getTranDate().toLocalDate();
            String formatTime = "";//当前时间 MM月/yyyy
            String tempTime = "";//上一次时间
            if (localDate != null){
                formatTime = localDate.format(DateFormatters.monthFormatter1);
            }
            if (i > 0){
                tempTime = withdrawalQueryViewModel.getList().get(i -1).getTranDate().toLocalDate().format(DateFormatters.monthFormatter1);
            }
            if (tempTime.equals(formatTime)){
                ShowListBean item = new ShowListBean();
                item.type = ShowListBean.CHILD;

                item.setTitleID(ShowListConst.TITLE_SEND_TYPE);
                item.setTime(localDate);
                item.setContentLeftAbove(withdrawalQueryViewModel.getList().get(i).getPayeeName());//收款人姓名
                item.setContentLeftBelow(NumberUtils.formatMobileNumber(withdrawalQueryViewModel.getList().get(i).getPayeeMobile()));//收款人手机号
                item.setContentLeftBelowAgain(ResultStatusUtils.getWithdrawalStatus(mContext, withdrawalQueryViewModel.getList().get(i).getRemitStatus()));//状态
                item.setContentRightBelow(MoneyUtils.transMoneyFormat(withdrawalQueryViewModel.getList().get(i).getRemitAmount(), ApplicationConst.CURRENCY_CNY));//取款金额
                listViewBeanList.add(item);
            } else{
                for (int j = 0; j < 2; j++){
                    ShowListBean itemFirst = new ShowListBean();
                    if (j == 0){
                        itemFirst.type = ShowListBean.GROUP;
                        itemFirst.setGroupName(formatTime);
                        itemFirst.setTime(localDate);
                    }else {
                        itemFirst.type = ShowListBean.CHILD;

                        itemFirst.setTitleID(ShowListConst.TITLE_SEND_TYPE);
                        itemFirst.setTime(localDate);
                        itemFirst.setContentLeftAbove(withdrawalQueryViewModel.getList().get(i).getPayeeName());//收款人姓名
                        itemFirst.setContentLeftBelow(NumberUtils.formatMobileNumber(withdrawalQueryViewModel.getList().get(i).getPayeeMobile()));//收款人手机号
                        itemFirst.setContentLeftBelowAgain(ResultStatusUtils.getWithdrawalStatus(mContext, withdrawalQueryViewModel.getList().get(i).getRemitStatus()));//状态
                        itemFirst.setContentRightBelow(MoneyUtils.transMoneyFormat(withdrawalQueryViewModel.getList().get(i).getRemitAmount(), ApplicationConst.CURRENCY_CNY));//取款金额
                    }
                    listViewBeanList.add(itemFirst);
                }
            }
        }

//
//        for (int i = 0; i < withdrawalQueryViewModel.getList().size(); i++) {
//            TransactionBean transactionBean = new TransactionBean();
//
//            LocalDate paymentDate = withdrawalQueryViewModel.getList().get(i).getTranDate().toLocalDate();
//
//            transactionBean.setTitleID(TransactionView.TITLE_SEND_TYPE);
//            transactionBean.setTime(paymentDate);//日期
//            transactionBean.setContentLeftAbove(withdrawalQueryViewModel.getList().get(i).getPayeeName());//收款人姓名
//            transactionBean.setContentLeftBelow(NumberUtils.formatMobileNumber(withdrawalQueryViewModel.getList().get(i).getPayeeMobile()));//收款人手机号
//            transactionBean.setContentLeftBelowAgain(ResultStatusUtils.getWithdrawalStatus(mContext, withdrawalQueryViewModel.getList().get(i).getRemitStatus()));//状态
//            transactionBean.setContentRightBelow(MoneyUtils.transMoneyFormat(withdrawalQueryViewModel.getList().get(i).getRemitAmount(), ApplicationConst.CURRENCY_CNY));//取款金额
//
//            listViewBeanList.add(transactionBean);
//        }
    }


    /**
     * 处理请求成功后的数据
     *
     * @param withdrawalQueryViewModel
     */
    private void handleSuccessData(WithdrawalQueryViewModel withdrawalQueryViewModel) {
        mWithdrawalQueryViewModel.setAgentAcctNumber(withdrawalQueryViewModel.getAgentAcctNumber());
        mWithdrawalQueryViewModel.setNickName(withdrawalQueryViewModel.getNickName());
        mWithdrawalQueryViewModel.setRecordNumber(withdrawalQueryViewModel.getRecordNumber());
        if (withdrawalQueryViewModel.getList().size() != 0) {
            List<WithdrawalQueryViewModel.ListBean> listBeen = new ArrayList<WithdrawalQueryViewModel.ListBean>();
            if (mWithdrawalQueryViewModel.getList() != null) {
                listBeen.addAll(mWithdrawalQueryViewModel.getList());
            }
            listBeen.addAll(withdrawalQueryViewModel.getList());
            mWithdrawalQueryViewModel.setList(listBeen);

            copyResult2TransBean(mWithdrawalQueryViewModel);

            if (pageCurrentIndex == 0) {
                //关闭加载对话框
                closeProgressDialog();
            }
            if (isPullToRefresh) {
                withDrawalRefresh.loadmoreCompleted(PullToRefreshLayout.SUCCEED);
            }
//            haveDataSelectStatust(true);
//            haveDataSelectText(true);
            PublicUtils.haveDataSelectText(true, tvNoData, isSelectData);
        } else {
            if (pageCurrentIndex == 0) {
                //关闭加载对话框
                closeProgressDialog();
//                haveDataSelectStatust(false);
//                haveDataSelectText(false);
                PublicUtils.haveDataSelectText(false, tvNoData, isSelectData);
            } else {
//                haveDataSelectStatust(true);
//                haveDataSelectText(true);
                PublicUtils.haveDataSelectText(true, tvNoData, isSelectData);
            }
            if (isPullToRefresh) {
                withDrawalRefresh.loadmoreCompleted(PullToRefreshLayout.NO_MORE_DATA);
            }
        }
        //将所有数据处理完后为ListView设置数据
        mAdapter.setData(listViewBeanList);
        pageCurrentIndex += pageSize;//此处的索引是按记录的索引来走
    }

    /**
     * 处理请求失败后的数据
     *
     * @param biiResultErrorException
     */
    private void handleFailData(BiiResultErrorException biiResultErrorException) {
        if (pageCurrentIndex == 0) {
            //关闭加载对话框
            closeProgressDialog();
        } else {
            withDrawalRefresh.loadmoreCompleted(PullToRefreshLayout.FAIL);
        }
        //将所有数据处理完后为ListView设置数据
        mAdapter.setData(listViewBeanList);
        showErrorDialog(biiResultErrorException.getErrorMessage());
    }


    @Override
    public void queryWithdrawalQueryListSuccess(WithdrawalQueryViewModel withdrawalQueryViewModel) {
        handleSuccessData(withdrawalQueryViewModel);
    }

    @Override
    public void queryWithdrawalQueryListFail(BiiResultErrorException biiResultErrorException) {
        handleFailData(biiResultErrorException);
    }

    @Override
    public void queryWithdrawalDetailInfoSuccess(String queryResult) {
        closeProgressDialog();
        // 跳转到详情页
        Bundle bundle = new Bundle();
        bundle.putParcelable("DetailsInfo", mWithdrawalQueryViewModel.getList().get(itemPosition));//此处使用列表返回的数据，不使用详情数据
        bundle.putString("AgentAcctNumber", mWithdrawalQueryViewModel.getAgentAcctNumber());//此处使用列表返回的数据，不使用详情数据
        WithdrawalQueryDetailInfoFragment queryDetailInfoFragment = new WithdrawalQueryDetailInfoFragment();
        queryDetailInfoFragment.setArguments(bundle);
        start(queryDetailInfoFragment);
    }

    @Override
    public void queryWithdrawalDetailInfoFail(BiiResultErrorException biiResultErrorException) {
        closeProgressDialog();
        showErrorDialog(biiResultErrorException.getErrorMessage());
    }

    @Override
    protected void titleLeftIconClick() {
        if (currentWithdrawalType == WITHDRAWAL_TYPE_COMMON) {
            startAndPopOther(MobileWithdrawFragment.class);
        } else {
            super.titleLeftIconClick();
        }
    }

    @Override
    public boolean onBack() {
        if (currentWithdrawalType == WITHDRAWAL_TYPE_COMMON) {
            startAndPopOther(MobileWithdrawFragment.class);
        }
        return true;
    }

    private void startAndPopOther(Class<? extends MobileWithdrawFragment> clazz) {
        popTo(clazz, false);
        findFragment(clazz).initData();
    }

    @Override
    protected WithdrawalQueryContract.Presenter initPresenter() {
        return new WithdrawalQueryPresenter(this);
    }
}
