package com.boc.bocsoft.mobile.bocmobile.buss.transfer.remitquery.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.ApplicationConst;
import com.boc.bocsoft.mobile.bocmobile.ApplicationContext;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.MvpBussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.model.AccountBean;
import com.boc.bocsoft.mobile.bocmobile.base.utils.DateTimePicker;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.SlipMenu.SlipDrawerLayout;
import com.boc.bocsoft.mobile.bocmobile.base.widget.refreshliseview.PullToRefreshLayout;
import com.boc.bocsoft.mobile.bocmobile.base.widget.selectaccoutview.SelectAccountButton;
import com.boc.bocsoft.mobile.bocmobile.base.widget.selectaccoutview.SelectAccoutFragment;
import com.boc.bocsoft.mobile.bocmobile.base.widget.selectview.selecttimerangenew.SelectTimeRangeNew;
import com.boc.bocsoft.mobile.bocmobile.base.widget.showlistview.PinnedSectionListView;
import com.boc.bocsoft.mobile.bocmobile.base.widget.showlistview.ShowListConst;
import com.boc.bocsoft.mobile.bocmobile.base.widget.showlistview.adapter.ShowListAdapter;
import com.boc.bocsoft.mobile.bocmobile.base.widget.showlistview.bean.ShowListBean;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.mobileremit.ui.MobileRemitFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.remitquery.model.RemitQueryDetailInfoViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.remitquery.model.RemitQueryViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.remitquery.presenter.RemitQueryPresenter;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.utils.PublicUtils;
import com.boc.bocsoft.mobile.bocmobile.yun.BocCloudCenter;
import com.boc.bocsoft.mobile.bocmobile.yun.other.AccountType;
import com.boc.bocsoft.mobile.common.utils.NumberUtils;
import com.boc.bocsoft.mobile.common.utils.StringUtils;
import com.boc.bocsoft.mobile.common.utils.date.DateFormatters;
import com.boc.bocsoft.mobile.framework.widget.TitleBarView;

import org.threeten.bp.LocalDate;

import java.util.ArrayList;
import java.util.List;


/**
 * 手机取款-汇出查询
 * Created by wangf on 2016/6/20.
 */
public class RemitQueryFragment extends MvpBussFragment<RemitQueryContract.Presenter> implements RemitQueryContract.View {

    /**
     * 根布局
     */
    private View mRootView;

    /**
     * fragment的页面
     */
    //详情的ListView
    PinnedSectionListView remitQueryListView;
    ShowListAdapter mAdapter;
    //详情的上拉刷新
    PullToRefreshLayout remitQueryRefresh;
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
    //头部的筛选Layout
    private RelativeLayout rlHeadSelectLayout;
    //筛选的LinearLayout
    private LinearLayout llRemitQuerySelect;
    //筛选处的范围
    private TextView tvRemitQueryRange;
    //筛选
    private TextView tvRemitQuerySelect;
    //筛选的imageView
    private ImageView ivRemitQuerySelect;


    /**
     * 侧滑菜单
     */
    private SlipDrawerLayout mDrawerLayout;

    //起始时间
    private LocalDate startLocalDate;
    //结束时间
    private LocalDate endLocalDate;
    //所选择的账户
    private AccountBean accountSelectBean;

//    //汇出查询service通信处理类
//    private RemitQueryPresenter mRemitQueryPresenter;
    //汇出查询UI层model
    private RemitQueryViewModel mRemitQueryViewModel;


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
    //账户类型列表
    private ArrayList<String> accountTypeList;

    //判断是否是筛选
    private boolean isSelectData;
    //判断是否是上拉加载
    private boolean isPullToRefresh;

    //用户点击的itemposition,用于向详情页面传数据
    private int itemPosition;

    //页面跳转
    private static final int REQUEST_CODE_DETAIL_INFO = 201;

    public static final String REMIT_QUERY_TYPE = "DetailQueryType";
    public static final String REMIT_ACCOUNT_BEAN = "DetailAccountBean";
    //全部汇出查询
    public static final int REMIT_ACCOUNT_TYPE_ALL = 0;
    //单账户汇出查询
    public static final int REMIT_ACCOUNT_TYPE_COMMON = 1;
    //当前所查询账户类型
    private int currentRemitAccountType;

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        mRootView = View.inflate(mContext, R.layout.boc_fragment_transfer_query, null);

        currentRemitAccountType = getArguments().getInt(REMIT_QUERY_TYPE, REMIT_ACCOUNT_TYPE_ALL);

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
        remitQueryListView = (PinnedSectionListView) mRootView.findViewById(R.id.lv_transfer_query);
        remitQueryRefresh = (PullToRefreshLayout) mRootView.findViewById(R.id.refresh_transfer_query);
        tvNoData = (TextView) mRootView.findViewById(R.id.no_data_transfer_query);

        headView = LayoutInflater.from(mContext).inflate(R.layout.boc_head_account_transdetail, null);

        //页面title初始化
        initQueryTitleView();

        //账户选择
        selectAccountButton = (SelectAccountButton) headView.findViewById(R.id.select_account_button);
        //头部的筛选Layout
        rlHeadSelectLayout = (RelativeLayout) headView.findViewById(R.id.select_head_select_rl);

        //筛选
        llRemitQuerySelect = (LinearLayout) headView.findViewById(R.id.ll_transdetail_select);
        tvRemitQuerySelect = (TextView) headView.findViewById(R.id.tv_transdetail_select);
        ivRemitQuerySelect = (ImageView) headView.findViewById(R.id.iv_transdetail_select);
        tvRemitQueryRange = (TextView) headView.findViewById(R.id.tv_transdetail_range);

        //侧滑菜单
        mDrawerLayout = (SlipDrawerLayout) mRootView.findViewById(R.id.drawer_layout_transfer_query);
        rightDrawer = (SelectTimeRangeNew) mRootView.findViewById(R.id.right_drawer_transfer_query);

        remitQueryListView.addHeaderView(headView, null, false);
        mAdapter = new ShowListAdapter(mContext, -1);
        remitQueryListView.saveAdapter(mAdapter);
        remitQueryListView.setShadowVisible(false);
        remitQueryListView.setAdapter(mAdapter);
//        remitQueryListView.addHeaderView(headView, null, false);
//        remitQueryListView.setAdapter();

    }


    /**
     * 初始化Title
     */
    private void initQueryTitleView() {
        queryTitleView = (TitleBarView) headView.findViewById(R.id.select_title_view);
        queryTitleView.setStyle(R.style.titlebar_common_white);
        queryTitleView.setTitle(getResources().getString(R.string.boc_transfer_remit_query_title));
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
        tvRemitQueryRange.setText(getResources().getString(R.string.boc_common_query_month_3));

//        mRemitQueryPresenter = new RemitQueryPresenter(this);
        mRemitQueryViewModel = new RemitQueryViewModel();
        accountSelectBean = new AccountBean();

        listViewBeanList = new ArrayList<ShowListBean>();

        accountTypeList = new ArrayList<String>();
        accountTypeList.add(ApplicationConst.ACC_TYPE_ORD);//普通活期
        accountTypeList.add(ApplicationConst.ACC_TYPE_BRO);//借记卡
        accountTypeList.add(ApplicationConst.ACC_TYPE_RAN);//活一本

        List<AccountBean> accountBeanList = ApplicationContext.getInstance().getChinaBankAccountList(accountTypeList);

        switch (currentRemitAccountType) {
            case REMIT_ACCOUNT_TYPE_ALL:
                // 首先获取本地保存的账户信息，如果本地没有保存，则取账户列表中的第一个账户数据 modify by wangf on 2016-10-28 15:50:15
                String cloudAccountId = BocCloudCenter.getInstance().getAccountId(AccountType.ACC_TYPE_PHONE_DRAW);
                if (StringUtils.isEmpty(cloudAccountId)){
                    if (accountBeanList.size() > 0) {
                        accountSelectBean = accountBeanList.get(0);
                        selectAccountButton.setData(accountSelectBean);
                    } else {
                        judgeNoAccountHeadVisible(false);
                        return;
                    }
                }else{
                    List<AccountBean> accountBeanListAll = ApplicationContext.getInstance().getChinaBankAccountList(null);
                    for (int i = 0; i < accountBeanListAll.size(); i++) {
                        String sha256String = BocCloudCenter.getSha256String(accountBeanListAll.get(i).getAccountId());
                        if (cloudAccountId.equals(sha256String)){
                            accountSelectBean = accountBeanListAll.get(i);
                            selectAccountButton.setData(accountSelectBean);
                        }
                    }
                }
                break;
            case REMIT_ACCOUNT_TYPE_COMMON:
                accountSelectBean = getArguments().getParcelable(REMIT_ACCOUNT_BEAN);
                selectAccountButton.setData(accountSelectBean);
                break;
        }


        queryRemitQueryList();
    }


    @Override
    public void setListener() {
        //选择账户layout
        selectAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //进入选择账户页面
//                Bundle bundle = new Bundle();
//                bundle.putStringArrayList(SelectAccoutFragment.ACCOUNT_TYPE_LIST, accountTypeList);
//                SelectAccoutFragment selectAccoutFragment = SelectAccoutFragment.newInstance(accountTypeList);
//                selectAccoutFragment.setArguments(bundle);
                startForResult(SelectAccoutFragment.newInstance(accountTypeList), SelectAccoutFragment.REQUEST_CODE_SELECT_ACCOUNT);
            }
        });

        //筛选layout的点击事件
        llRemitQuerySelect.setOnClickListener(new View.OnClickListener() {
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
                        tvRemitQueryRange.setText(content.replace("月", "个月") + "查询结果");
                    }else{
                        tvRemitQueryRange.setText(content + "查询结果");
                    }
                } else {
                    tvRemitQueryRange.setText("");
                }

                String startTime = rightDrawer.getStartDate();
                String endTime = rightDrawer.getEndDate();
                startLocalDate = LocalDate.parse(startTime, DateFormatters.dateFormatter1);
                endLocalDate = LocalDate.parse(endTime, DateFormatters.dateFormatter1);
                if (PublicUtils.judgeChoiceDateRange(startLocalDate, endLocalDate, MAX_QUERY_RANGE, RemitQueryFragment.this)) {
                    mDrawerLayout.toggle();

                    listViewBeanList.clear();
                    if (mRemitQueryViewModel.getList() != null) {
                        mRemitQueryViewModel.getList().clear();
                    }
                    pageCurrentIndex = 0;
                    queryRemitQueryList();
                    isSelectData = true;
                    isPullToRefresh = false;
                }
            }
        });

        //重置按钮的监听
        rightDrawer.setResetListener(new SelectTimeRangeNew.ResetClickListener() {
            @Override
            public void resetClick() {
                // modify by wangf on 2016-12-4 10:39:24 组件控制重置的状态
//                rightDrawer.setClickSelectDefaultData();
            }
        });

        //listView的item点击事件
        remitQueryListView.setListener(new PinnedSectionListView.ClickListener() {
            @Override
            public void onItemClickListener(int position) {
                itemPosition = position;
                queryRemitDetailInfoQueryList(position);
            }
        });

        //上拉加载
        remitQueryRefresh.setOnLoadListener(new PullToRefreshLayout.OnLoadListener() {
            @Override
            public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
                startPresenter();
                if (mRemitQueryViewModel.getList() != null) {
                    if (mRemitQueryViewModel.getList().size() < mRemitQueryViewModel.getRecordNumber()) {
                        isPullToRefresh = true;
                        queryRemitQueryList();
                    } else {
                        remitQueryRefresh.loadmoreCompleted(PullToRefreshLayout.NO_MORE_DATA);
                    }
                } else {
                    listViewBeanList.clear();
                    pageCurrentIndex = 0;
                    isPullToRefresh = true;
                    queryRemitQueryList();
                }
            }
        });
    }

    /**
     * 没有关联账号时页面显示规则
     *
     * @param isVisible
     */
    private void judgeNoAccountHeadVisible(boolean isVisible) {
        if (isVisible) {
            rlHeadSelectLayout.setVisibility(View.VISIBLE);
            selectAccountButton.setVisibility(View.VISIBLE);
            tvNoData.setVisibility(View.GONE);
        } else {
            rlHeadSelectLayout.setVisibility(View.GONE);
            selectAccountButton.setVisibility(View.GONE);
            remitQueryRefresh.setMove(false);
            tvNoData.setVisibility(View.VISIBLE);
            tvNoData.setText(getResources().getString(R.string.boc_account_transdetail_no_account));
        }
    }


    @Override
    protected void onFragmentResult(int requestCode, int resultCode, Bundle data) {
        super.onFragmentResult(requestCode, resultCode, data);
        if (SelectAccoutFragment.REQUEST_CODE_SELECT_ACCOUNT == requestCode) {
            if (resultCode == SelectAccoutFragment.RESULT_CODE_SELECT_ACCOUNT) {
                accountSelectBean = data.getParcelable(SelectAccoutFragment.ACCOUNT_SELECT);
                selectAccountButton.setData(accountSelectBean);

                listViewBeanList.clear();
                if (mRemitQueryViewModel.getList() != null) {
                    mRemitQueryViewModel.getList().clear();
                }
                pageCurrentIndex = 0;
                isSelectData = false;
                isPullToRefresh = false;
                queryRemitQueryList();
            }
        } else if (REQUEST_CODE_DETAIL_INFO == requestCode) {
            if (resultCode == RemitQueryDetailInfoFragment.RESULT_CODE_DETAIL_INFO_SUCCESS) {
                listViewBeanList.clear();
                if (mRemitQueryViewModel.getList() != null) {
                    mRemitQueryViewModel.getList().clear();
                }
                pageCurrentIndex = 0;
                isSelectData = false;
                isPullToRefresh = false;
                queryRemitQueryList();
            }
        }
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
                    showErrorDialog(getResources().getString(R.string.boc_account_transdetail_start_year_change, PublicUtils.changeNumberToUpper(MAX_QUERY_DATE / 12)));
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
                    showErrorDialog(getResources().getString(R.string.boc_account_transdetail_end_year_change, PublicUtils.changeNumberToUpper(MAX_QUERY_DATE / 12)));
                    return;
                }
                rightDrawer.setEndDate(strChoiceTime);
            }
        });
    }

//    /**
//     * 判断选择时间范围是否合法
//     *
//     * @return
//     */
//    private boolean judgeChoiceDateRange() {
//        if (startLocalDate == null || endLocalDate == null) {
//            showErrorDialog(getResources().getString(R.string.boc_account_transdetail_date_range_null));
//            return false;
//        }
//        if (endLocalDate.isBefore(startLocalDate)) {
//            showErrorDialog(getResources().getString(R.string.boc_account_transdetail_start_end));
//            return false;
//        }
//        if (isCompareDateRange(startLocalDate, endLocalDate, MAX_QUERY_RANGE)) {
//            showErrorDialog(getResources().getString(R.string.boc_account_transdetail_date_range_change, MAX_QUERY_RANGE));
//            return false;
//        }
//        return true;
//    }


//    /**
//     * 比较两个日期间隔是否超过mouthRange个月，startDate < endDate
//     * true为超过mouthRange个月，false为没有超过。
//     *
//     * @return
//     */
//    public static boolean isCompareDateRange(LocalDate startDate, LocalDate endDate, int mouthRange) {
//        if (startDate == null || endDate == null) {
//            return false;
//        } else {
//            LocalDate newDate = endDate.plusMonths(-mouthRange);
//            return !newDate.isBefore(startDate);
//        }
//    }


//    /**
//     * 根据是否有数据显示筛选按钮的颜色
//     * 若查询数据为空，则查询按钮需要变成红色
//     *
//     * @param haveDate
//     */
//    private void haveDataSelectStatust(boolean haveDate) {
//        if (haveDate) {
//            tvRemitQuerySelect.setTextColor(getResources().getColor(R.color.boc_text_color_common_gray));
//            ivRemitQuerySelect.setImageResource(R.drawable.boc_select_gray);
//        } else {
//            tvRemitQuerySelect.setTextColor(getResources().getColor(R.color.boc_text_color_red));
//            ivRemitQuerySelect.setImageResource(R.drawable.boc_select_red);
//        }
//    }

    /**
     * 筛选按钮的颜色改变
     */
    private void selectStatustChange() {
        tvRemitQuerySelect.setTextColor(getResources().getColor(R.color.boc_text_color_red));
        ivRemitQuerySelect.setImageResource(R.drawable.boc_select_red);
    }

    /**
     * 调用接口，汇出查询数据
     */
    private void queryRemitQueryList() {
        if (pageCurrentIndex == 0) {
            //开启加载对话框
            showLoadingDialog();
        }
        getPresenter().queryRemitQueryList(buildRemitQueryViewModel());
    }

    /**
     * 调用接口，汇出详情查询
     */
    private void queryRemitDetailInfoQueryList(int position) {
        //开启加载对话框
        showLoadingDialog();
        getPresenter().queryRemitDetailInfo(buildRemitDetailInfoQueryViewModel(position));
    }

    /**
     * 封装页面数据--列表
     *
     * @return
     */
    private RemitQueryViewModel buildRemitQueryViewModel() {
        mRemitQueryViewModel.setAccountId(accountSelectBean.getAccountId());
//        mRemitQueryViewModel.setAccountId("125787679");
        mRemitQueryViewModel.setStartDate(startLocalDate.format(DateFormatters.dateFormatter1));//起始日期
        mRemitQueryViewModel.setEndDate(endLocalDate.format(DateFormatters.dateFormatter1));//结束日期
        mRemitQueryViewModel.setPageSize(pageSize);//页面大小
        mRemitQueryViewModel.setCurrentIndex(pageCurrentIndex);//当前页索引
        return mRemitQueryViewModel;
    }

    /**
     * 封装页面数据--详情
     *
     * @return
     */
    private RemitQueryDetailInfoViewModel buildRemitDetailInfoQueryViewModel(int position) {
        RemitQueryDetailInfoViewModel infoViewModel = new RemitQueryDetailInfoViewModel();
        infoViewModel.setTransactionId(mRemitQueryViewModel.getList().get(position).getTransactionId());
        return infoViewModel;
    }


    /**
     * 将接口返回数据封装成ListView所需要的model
     *
     * @param remitQueryViewModel
     */
    private void copyResult2TransBean(RemitQueryViewModel remitQueryViewModel) {
        listViewBeanList.clear();
        for (int i = 0; i < remitQueryViewModel.getList().size(); i++){
            LocalDate localDate = remitQueryViewModel.getList().get(i).getTranDate().toLocalDate();
            String formatTime = "";//当前时间 MM月/yyyy
            String tempTime = "";//上一次时间
            if (localDate != null){
                formatTime = localDate.format(DateFormatters.monthFormatter1);
            }
            if (i > 0){
                tempTime = remitQueryViewModel.getList().get(i - 1).getTranDate().toLocalDate().format(DateFormatters.monthFormatter1);
            }
            if (tempTime.equals(formatTime)){
                ShowListBean item = new ShowListBean();
                item.type = ShowListBean.CHILD;

                item.setTitleID(ShowListConst.TITLE_MOBILE_TYPE);
                item.setTime(localDate);
                item.setContentLeftAbove(remitQueryViewModel.getList().get(i).getPayeeName());//收款人姓名
                item.setContentLeftBelow(NumberUtils.formatMobileNumber(remitQueryViewModel.getList().get(i).getPayeeMobile()));//收款人手机号
                item.setContentRightAbove(MoneyUtils.transMoneyFormat(remitQueryViewModel.getList().get(i).getRemitAmount(), ApplicationConst.CURRENCY_CNY));//汇款金额
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

                        itemFirst.setTitleID(ShowListConst.TITLE_MOBILE_TYPE);
                        itemFirst.setTime(localDate);
                        itemFirst.setContentLeftAbove(remitQueryViewModel.getList().get(i).getPayeeName());//收款人姓名
                        itemFirst.setContentLeftBelow(NumberUtils.formatMobileNumber(remitQueryViewModel.getList().get(i).getPayeeMobile()));//收款人手机号
                        itemFirst.setContentRightAbove(MoneyUtils.transMoneyFormat(remitQueryViewModel.getList().get(i).getRemitAmount(), ApplicationConst.CURRENCY_CNY));//汇款金额
                    }
                    listViewBeanList.add(itemFirst);
                }
            }
        }

//        for (int i = 0; i < remitQueryViewModel.getList().size(); i++) {
//            TransactionBean transactionBean = new TransactionBean();
//
//            LocalDate paymentDate = remitQueryViewModel.getList().get(i).getTranDate().toLocalDate();
//
//            transactionBean.setTitleID(TransactionView.TITLE_MOBILE_TYPE);
//            transactionBean.setTime(paymentDate);//日期
//            transactionBean.setContentLeftAbove(remitQueryViewModel.getList().get(i).getPayeeName());//收款人姓名
//            transactionBean.setContentLeftBelow(NumberUtils.formatMobileNumber(remitQueryViewModel.getList().get(i).getPayeeMobile()));//收款人手机号
//            transactionBean.setContentRightBelow(MoneyUtils.transMoneyFormat(remitQueryViewModel.getList().get(i).getRemitAmount(), ApplicationConst.CURRENCY_CNY));//汇款金额
//
//            listViewBeanList.add(transactionBean);
//        }
    }


    /**
     * 处理请求成功后的数据
     *
     * @param remitQueryViewModel
     */
    private void handleSuccessData(RemitQueryViewModel remitQueryViewModel) {
        mRemitQueryViewModel.setFromAcctNumber(remitQueryViewModel.getFromAcctNumber());
        mRemitQueryViewModel.setRecordNumber(remitQueryViewModel.getRecordNumber());
        mRemitQueryViewModel.setNickName(remitQueryViewModel.getNickName());
        if (remitQueryViewModel.getList().size() != 0) {
            List<RemitQueryViewModel.ListBean> listBeen = new ArrayList<RemitQueryViewModel.ListBean>();
            if (mRemitQueryViewModel.getList() != null) {
                listBeen.addAll(mRemitQueryViewModel.getList());
            }
            listBeen.addAll(remitQueryViewModel.getList());
            mRemitQueryViewModel.setList(listBeen);

            copyResult2TransBean(mRemitQueryViewModel);

            if (pageCurrentIndex == 0) {
                //关闭加载对话框
                closeProgressDialog();
            }
            if (isPullToRefresh) {
                remitQueryRefresh.loadmoreCompleted(PullToRefreshLayout.SUCCEED);
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
                remitQueryRefresh.loadmoreCompleted(PullToRefreshLayout.NO_MORE_DATA);
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
            remitQueryRefresh.loadmoreCompleted(PullToRefreshLayout.FAIL);
        }
        //将所有数据处理完后为ListView设置数据
        mAdapter.setData(listViewBeanList);
        showErrorDialog(biiResultErrorException.getErrorMessage());
    }


    @Override
    public void queryRemitQueryListSuccess(RemitQueryViewModel remitQueryViewModel) {
        handleSuccessData(remitQueryViewModel);
    }

    @Override
    public void queryRemitQueryListFail(BiiResultErrorException biiResultErrorException) {
        handleFailData(biiResultErrorException);
    }

    @Override
    public void queryRemitDetailInfoSuccess(RemitQueryDetailInfoViewModel infoViewModel) {
        closeProgressDialog();
        // 跳转到详情页
        Bundle bundle = new Bundle();
        bundle.putParcelable("DetailsInfo", infoViewModel);
        bundle.putString("Channel", mRemitQueryViewModel.getList().get(itemPosition).getChannel());
        RemitQueryDetailInfoFragment queryDetailInfoFragment = new RemitQueryDetailInfoFragment();
        queryDetailInfoFragment.setArguments(bundle);
        startForResult(queryDetailInfoFragment, REQUEST_CODE_DETAIL_INFO);
    }

    @Override
    public void queryRemitDetailInfoFail(BiiResultErrorException biiResultErrorException) {
        closeProgressDialog();
        showErrorDialog(biiResultErrorException.getErrorMessage());
    }


//    @Override
//    protected void titleLeftIconClick() {
//        if (currentRemitAccountType == REMIT_ACCOUNT_TYPE_COMMON) {
//            startAndPopOther(MobileRemitFragment.class);
//        } else {
//            super.titleLeftIconClick();
//        }
//    }

    @Override
    public boolean onBack() {
        if (currentRemitAccountType == REMIT_ACCOUNT_TYPE_COMMON) {
            startAndPopOther(MobileRemitFragment.class);
        }else{
            pop();
        }
        return false;
    }

    private void startAndPopOther(Class<? extends MobileRemitFragment> clazz) {
        popTo(clazz, false);
        findFragment(clazz).initData();
    }

    @Override
    protected RemitQueryContract.Presenter initPresenter() {
        return new RemitQueryPresenter(this);
    }
}
