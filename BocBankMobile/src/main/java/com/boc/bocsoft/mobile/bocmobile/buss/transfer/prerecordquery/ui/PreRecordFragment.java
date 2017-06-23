package com.boc.bocsoft.mobile.bocmobile.buss.transfer.prerecordquery.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
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
import com.boc.bocsoft.mobile.bocmobile.base.utils.PublicCodeUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.SlipMenu.SlipDrawerLayout;
import com.boc.bocsoft.mobile.bocmobile.base.widget.refreshliseview.PullToRefreshLayout;
import com.boc.bocsoft.mobile.bocmobile.base.widget.selectaccoutview.SelectAccountButton;
import com.boc.bocsoft.mobile.bocmobile.base.widget.selectview.selecttimerangenew.SelectTimeRangeNew;
import com.boc.bocsoft.mobile.bocmobile.base.widget.showlistview.PinnedSectionListView;
import com.boc.bocsoft.mobile.bocmobile.base.widget.showlistview.ShowListConst;
import com.boc.bocsoft.mobile.bocmobile.base.widget.showlistview.adapter.ShowListAdapter;
import com.boc.bocsoft.mobile.bocmobile.base.widget.showlistview.bean.ShowListBean;
import com.boc.bocsoft.mobile.bocmobile.base.widget.singlemoreselect.Content;
import com.boc.bocsoft.mobile.bocmobile.base.widget.singlemoreselect.SelectGridView;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.prerecordquery.model.PreRecordDetailInfoViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.prerecordquery.model.PreRecordViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.prerecordquery.presenter.PreRecordPresenter;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.utils.PublicUtils;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.utils.ResultStatusUtils;
import com.boc.bocsoft.mobile.common.utils.date.DateFormatters;
import com.boc.bocsoft.mobile.framework.widget.TitleBarView;

import org.threeten.bp.LocalDate;

import java.util.ArrayList;
import java.util.List;

/**
 * 预约管理 -- 预约交易查询
 * Created by wangf on 2016/7/22.
 */
public class PreRecordFragment extends MvpBussFragment<PreRecordContract.Presenter> implements PreRecordContract.View {

    /**
     * 根布局
     */
    private View mRootView;

    /**
     * fragment的页面
     */
    //详情的ListView
    PinnedSectionListView preRecordListView;
    ShowListAdapter mAdapter;
    //详情的上拉刷新
    PullToRefreshLayout preRecordRefresh;
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
    //筛选处额外添加的layout
    private LinearLayout llAddToTopLayout;
    //筛选的LinearLayout
    private LinearLayout llPreRecordSelect;
    //筛选处的范围
    private TextView tvPreRecordRange;
    //筛选
    private TextView tvPreRecordSelect;
    //筛选的imageView
    private ImageView ivPreRecordSelect;

    /**
     * 预约类型的筛选
     */
    //类型的筛选
    private SelectGridView selectSinglePreView;
    //预约类型筛选的数据
    private List<Content> selectPreRecordList;

    /**
     * 侧滑菜单
     */
    private SlipDrawerLayout mDrawerLayout;

    /**
     * 加载相关
     */
    //当前加载页码
    private int pageCurrentIndex;
    //每页大小
    private static int pageSize;
    private String _refresh = "false";

//    //预约管理service通信处理类
//    private PreRecordPresenter mPreRecordPresenter;
    //预约管理UI层model
    private PreRecordViewModel mPreRecordViewModel;

    /**
     * 查询范围相关
     */
    //最大查询范围为3个月
    private final static int MAX_QUERY_RANGE = 3;
    //最大查询起始日期为一年
    private final static int MAX_QUERY_DATE = 12;

    //起始时间
    private LocalDate startLocalDate;
    //结束时间
    private LocalDate endLocalDate;

    //日期查询类型 0：按执行日期查询 1：按预约日期查询
    private static final String DATE_QUERY_TYPE_PAYMENT = "0";
    private static final String DATE_QUERY_TYPE_FIRST = "1";
    private String currentDateQueryType = DATE_QUERY_TYPE_PAYMENT;
    private String lastDateQueryType = currentDateQueryType;

    //ListView所需要的List
    private List<ShowListBean> listViewBeanList;

    //判断是否是筛选
    private boolean isSelectData;
    //判断是否是上拉加载
    private boolean isPullToRefresh;


    //页面跳转
    private static final int REQUEST_CODE_DETAIL_INFO = 201;

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        mRootView = View.inflate(mContext, R.layout.boc_fragment_transfer_query, null);
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
        preRecordListView = (PinnedSectionListView) mRootView.findViewById(R.id.lv_transfer_query);
        preRecordRefresh = (PullToRefreshLayout) mRootView.findViewById(R.id.refresh_transfer_query);
        tvNoData = (TextView) mRootView.findViewById(R.id.no_data_transfer_query);

        headView = LayoutInflater.from(mContext).inflate(R.layout.boc_head_account_transdetail, null);

        //页面title
        initQueryTitleView();

        //账户选择
        selectAccountButton = (SelectAccountButton) headView.findViewById(R.id.select_account_button);
        selectAccountButton.setVisibility(View.GONE);

        //筛选
        llPreRecordSelect = (LinearLayout) headView.findViewById(R.id.ll_transdetail_select);
        tvPreRecordSelect = (TextView) headView.findViewById(R.id.tv_transdetail_select);
        ivPreRecordSelect = (ImageView) headView.findViewById(R.id.iv_transdetail_select);
        tvPreRecordRange = (TextView) headView.findViewById(R.id.tv_transdetail_range);

        //侧滑菜单
        mDrawerLayout = (SlipDrawerLayout) mRootView.findViewById(R.id.drawer_layout_transfer_query);
        rightDrawer = (SelectTimeRangeNew) mRootView.findViewById(R.id.right_drawer_transfer_query);

        //筛选中的预约类型
        View selectTransLayout = View.inflate(mContext, R.layout.boc_view_trans_select_pre_record, null);
        selectSinglePreView = (SelectGridView) selectTransLayout.findViewById(R.id.select_single_pre);
        llAddToTopLayout = rightDrawer.getAddToTopLayout();
        llAddToTopLayout.setVisibility(View.VISIBLE);
        llAddToTopLayout.addView(selectTransLayout);

        preRecordListView.addHeaderView(headView, null, false);
        mAdapter = new ShowListAdapter(mContext, -1);
        preRecordListView.saveAdapter(mAdapter);
        preRecordListView.setShadowVisible(false);
        preRecordListView.setAdapter(mAdapter);
//        preRecordListView.addHeaderView(headView, null, false);
//        preRecordListView.setAdapter();

    }

    @Override
    public void initData() {
        pageSize = ApplicationConst.PAGE_SIZE;
        initSelectView();

        pageCurrentIndex = 0;

//        mPreRecordPresenter = new PreRecordPresenter(this);
        mPreRecordViewModel = new PreRecordViewModel();

        listViewBeanList = new ArrayList<ShowListBean>();


        queryPreRecordList();
    }


    /**
     * 初始化Title
     */
    private void initQueryTitleView(){
        queryTitleView = (TitleBarView)headView.findViewById(R.id.select_title_view);
        queryTitleView.setStyle(R.style.titlebar_common_white);
        queryTitleView.setTitle("预约管理");
        queryTitleView.setRightImgBtnVisible(false);

        queryTitleView.setLeftButtonOnClickLinster(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                titleLeftIconClick();
            }
        });
    }

    /**
     * 初始化按执行日期查询的时间
     */
    private void initPaymentQueryDate(){
        startLocalDate = ApplicationContext.getInstance().getCurrentSystemDate().toLocalDate();
        endLocalDate = ApplicationContext.getInstance().getCurrentSystemDate().toLocalDate().plusMonths(MAX_QUERY_RANGE);
        rightDrawer.setDefaultDate(startLocalDate.format(DateFormatters.dateFormatter1), endLocalDate.format(DateFormatters.dateFormatter1));
        tvPreRecordRange.setText("");
//        tvPreRecordRange.setText(getResources().getString(R.string.boc_common_query_month_3));
    }

    /**
     * 初始化按预约日期查询的时间
     */
    private void initFirstQueryDate(){
        startLocalDate = ApplicationContext.getInstance().getCurrentSystemDate().toLocalDate().plusMonths(-MAX_QUERY_RANGE).plusDays(1);
        endLocalDate = ApplicationContext.getInstance().getCurrentSystemDate().toLocalDate();
        rightDrawer.setDefaultDate(startLocalDate.format(DateFormatters.dateFormatter1), endLocalDate.format(DateFormatters.dateFormatter1));
        tvPreRecordRange.setText("");
//        tvPreRecordRange.setText(getResources().getString(R.string.boc_common_query_month_3));
    }


    @Override
    public void setListener() {
        //筛选layout的点击事件
        llPreRecordSelect.setOnClickListener(new View.OnClickListener() {
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
                        tvPreRecordRange.setText(content.replace("月", "个月") + "查询结果");
                    }else{
                        tvPreRecordRange.setText(content + "查询结果");
                    }
                } else {
                    tvPreRecordRange.setText("");
                }

                //获取预约类型的筛选
                getSelectTransType();
                lastDateQueryType = currentDateQueryType;

                String startTime = rightDrawer.getStartDate();
                String endTime = rightDrawer.getEndDate();
                startLocalDate = LocalDate.parse(startTime, DateFormatters.dateFormatter1);
                endLocalDate = LocalDate.parse(endTime, DateFormatters.dateFormatter1);
                if (currentDateQueryType.equals(DATE_QUERY_TYPE_PAYMENT)){
                    if (PublicUtils.judgeChoiceDateRange(startLocalDate, endLocalDate.plusDays(-1), MAX_QUERY_RANGE, PreRecordFragment.this)) {
                        mDrawerLayout.toggle();
                        listViewBeanList.clear();
                        if (mPreRecordViewModel.getList() != null) {
                            mPreRecordViewModel.getList().clear();
                        }
                        pageCurrentIndex = 0;
                        queryPreRecordList();
                        isSelectData = true;
                        isPullToRefresh = false;
                    }
                }else if (currentDateQueryType.equals(DATE_QUERY_TYPE_FIRST)){
                    if (PublicUtils.judgeChoiceDateRange(startLocalDate, endLocalDate, MAX_QUERY_RANGE, PreRecordFragment.this)) {
                        mDrawerLayout.toggle();
                        listViewBeanList.clear();
                        if (mPreRecordViewModel.getList() != null) {
                            mPreRecordViewModel.getList().clear();
                        }
                        pageCurrentIndex = 0;
                        queryPreRecordList();
                        isSelectData = true;
                        isPullToRefresh = false;
                    }
                }


            }
        });

        //重置按钮的监听
        rightDrawer.setResetListener(new SelectTimeRangeNew.ResetClickListener() {
            @Override
            public void resetClick() {
                // modify by wangf on 2016-12-4 10:39:24 组件控制重置的状态
//                rightDrawer.setClickSelectDefaultData();
//                cancelTransTypeSelected();
            }
        });

        //listView的item点击事件
        preRecordListView.setListener(new PinnedSectionListView.ClickListener() {
            @Override
            public void onItemClickListener(int position) {
                queryPreRecordInfo(position);
            }
        });

        //上拉加载
        preRecordRefresh.setOnLoadListener(new PullToRefreshLayout.OnLoadListener() {
            @Override
            public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {

                startPresenter();
                if (mPreRecordViewModel.getList() != null) {
                    if (mPreRecordViewModel.getList().size() < mPreRecordViewModel.getRecordCount()) {
                        isPullToRefresh = true;
                        queryPreRecordList();
                    } else {
                        preRecordRefresh.loadmoreCompleted(PullToRefreshLayout.NO_MORE_DATA);
                    }
                } else {
                    listViewBeanList.clear();
                    pageCurrentIndex = 0;
                    isPullToRefresh = true;
                    queryPreRecordList();
                }
            }
        });

        // 预约类型的筛选
        selectSinglePreView.setListener(new SelectGridView.ClickListener() {
            @Override
            public void setItemClick(AdapterView<?> parent, View view, int position, long id) {
                Content content = (Content)parent.getAdapter().getItem(position);
                currentDateQueryType = content.getContentNameID();
                if (DATE_QUERY_TYPE_PAYMENT.equals(currentDateQueryType)){
                    initPaymentQueryDate();
                    rightDrawer.setTitleValue("执行日期查询");
                    rightDrawer.setDefaultSelectStatus();
                    rightDrawer.setDefaultDateStatus();
//                    rightDrawer.setDateShowWay(SelectTimeRangeNew.START_DATE_BY_CURDATE);
                }else if (DATE_QUERY_TYPE_FIRST.equals(currentDateQueryType)){
                    initFirstQueryDate();
                    rightDrawer.setTitleValue("预约日期查询");
                    rightDrawer.setDefaultSelectStatus();
                    rightDrawer.setDefaultDateStatus();
//                    rightDrawer.setDateShowWay(SelectTimeRangeNew.END_DATE_BY_CURDATE);
                }
            }
        });
    }


    /**
     * 初始化预约类型的筛选数据
     */
    private void initSelectView() {
        String[] selectPreNameData = {"执行日期查询", "预约日期查询"};
        String[] selectPreIdData = {DATE_QUERY_TYPE_PAYMENT, DATE_QUERY_TYPE_FIRST};

        selectPreRecordList = new ArrayList<Content>();

        for (int i = 0; i < selectPreNameData.length; i++) {
            Content item = new Content();
            item.setName(selectPreNameData[i]);
            item.setContentNameID(selectPreIdData[i]);
            if (i == 0) {
                currentDateQueryType = DATE_QUERY_TYPE_PAYMENT;
                lastDateQueryType = currentDateQueryType;
                initPaymentQueryDate();
                rightDrawer.setTitleValue("执行日期查询");
                rightDrawer.setDefaultSelectStatus();
                rightDrawer.setDefaultDateStatus();
//                rightDrawer.setDateShowWay(SelectTimeRangeNew.START_DATE_BY_CURDATE);
                item.setSelected(true);
            }
            selectPreRecordList.add(item);
        }
        selectSinglePreView.setData(selectPreRecordList);
    }

    /**
     * 重置预约类型的选中
     */
    private void cancelTransTypeSelected() {
        for (int i = 0; i < selectPreRecordList.size(); i++) {
            Content item = selectPreRecordList.get(i);
            if (item.getContentNameID().equals(lastDateQueryType)) {
                item.setSelected(true);
            } else {
                item.setSelected(false);
            }
        }
        selectSinglePreView.getAdapter().notifyDataSetChanged();
    }

    /**
     * 获取预约类型的筛选
     * @return
     */
    private boolean getSelectTransType() {
        boolean haveSelected = false;
        for (Content item : selectPreRecordList) {
            if (item.getSelected()) {
                haveSelected = true;
                currentDateQueryType = item.getContentNameID();
                break;
            }
        }
        return haveSelected;
    }


    /**
     * 起始日期的选择与判断
     */
    private void judgeStartTimeAndSet(LocalDate currentDate){
        DateTimePicker.showDatePick(mContext, currentDate, DateFormatters.dateFormatter1, new DateTimePicker.DatePickCallBack() {
            @Override
            public void onChoiceDateSet(String strChoiceTime, LocalDate choiceDate) {
                if (currentDateQueryType.equals(DATE_QUERY_TYPE_PAYMENT)){
                    //起始日期不能早于系统当前日期一年前
                    if (PublicUtils.isCompareDateRange(choiceDate, ApplicationContext.getInstance().getCurrentSystemDate().toLocalDate(), MAX_QUERY_DATE)) {
                        showErrorDialog(getResources().getString(R.string.boc_account_transdetail_start_year_change, PublicUtils.changeNumberToUpper(MAX_QUERY_DATE/12)));
                        return;
                    }
                    rightDrawer.setStartDate(strChoiceTime);

                }else if(currentDateQueryType.equals(DATE_QUERY_TYPE_FIRST)){
                    //起始日期不能晚于系统当前日期
                    if (ApplicationContext.getInstance().getCurrentSystemDate().toLocalDate().isBefore(choiceDate)) {
                        showErrorDialog(getResources().getString(R.string.boc_account_transdetail_start_before));
                        return;
                    }
                    //起始日期不能早于系统当前日期一年前
                    if (PublicUtils.isCompareDateRange(choiceDate, ApplicationContext.getInstance().getCurrentSystemDate().toLocalDate(), MAX_QUERY_DATE)) {
                        showErrorDialog(getResources().getString(R.string.boc_account_transdetail_start_year_change, PublicUtils.changeNumberToUpper(MAX_QUERY_DATE/12)));
                        return;
                    }
                    rightDrawer.setStartDate(strChoiceTime);
                }
            }
        });
    }


    /**
     * 结束日期的选择与判断
     */
    private void judgeEndTimeAndSet(LocalDate currentDate){
        DateTimePicker.showDatePick(mContext, currentDate, DateFormatters.dateFormatter1, new DateTimePicker.DatePickCallBack() {
            @Override
            public void onChoiceDateSet(String strChoiceTime, LocalDate choiceDate) {
                if (currentDateQueryType.equals(DATE_QUERY_TYPE_PAYMENT)){
                    //结束日期不能早于系统当前日期一年前
                    if (PublicUtils.isCompareDateRange(choiceDate, ApplicationContext.getInstance().getCurrentSystemDate().toLocalDate(), MAX_QUERY_DATE)) {
                        showErrorDialog(getResources().getString(R.string.boc_account_transdetail_end_year_change, PublicUtils.changeNumberToUpper(MAX_QUERY_DATE / 12)));
                        return;
                    }

                    //结束日期不能晚于系统当前日期+6个月
                    if (ApplicationContext.getInstance().getCurrentSystemDate().toLocalDate().plusMonths(6).isBefore(choiceDate)) {
                        showErrorDialog("结束日期不能晚于系统当前日期+6个月");
                        return;
                    }

                    rightDrawer.setEndDate(strChoiceTime);
                }else if(currentDateQueryType.equals(DATE_QUERY_TYPE_FIRST)){
                    //结束日期不能晚于系统当前日期
                    if (ApplicationContext.getInstance().getCurrentSystemDate().toLocalDate().isBefore(choiceDate)) {
                        showErrorDialog(getResources().getString(R.string.boc_account_transdetail_end_before));
                        return;
                    }
                    //结束日期不能早于系统当前日期一年前
                    if (PublicUtils.isCompareDateRange(choiceDate, ApplicationContext.getInstance().getCurrentSystemDate().toLocalDate(), MAX_QUERY_DATE)) {
                        showErrorDialog(getResources().getString(R.string.boc_account_transdetail_end_year_change, PublicUtils.changeNumberToUpper(MAX_QUERY_DATE / 12)));
                        return;
                    }
                    rightDrawer.setEndDate(strChoiceTime);
                }
            }
        });
    }


    /**
     * 筛选按钮的颜色改变
     */
    private void selectStatustChange() {
        tvPreRecordSelect.setTextColor(getResources().getColor(R.color.boc_text_color_red));
        ivPreRecordSelect.setImageResource(R.drawable.boc_select_red);
    }


    @Override
    protected void onFragmentResult(int requestCode, int resultCode, Bundle data) {
        super.onFragmentResult(requestCode, resultCode, data);
        if (REQUEST_CODE_DETAIL_INFO == requestCode){
            if (resultCode == PreRecordDetailInfoFragment.RESULT_CODE_DETAIL_INFO_SUCCESS){
                listViewBeanList.clear();
                if (mPreRecordViewModel.getList() != null) {
                    mPreRecordViewModel.getList().clear();
                }
                pageCurrentIndex = 0;
                isSelectData = false;
                isPullToRefresh = false;
                queryPreRecordList();
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    /**
     * 调用接口，预约管理列表查询
     */
    private void queryPreRecordList() {
        if (pageCurrentIndex == 0) {
            //开启加载对话框
            showLoadingDialog();
        }
        getPresenter().queryPreRecordList(buildPreRecordViewModel());
    }

    /**
     * 调用接口，预约管理详情查询
     */
    private void queryPreRecordInfo(int position) {
        //开启加载对话框
        showLoadingDialog();
        PreRecordViewModel.ListBean listBean = mPreRecordViewModel.getList().get(position);
        getPresenter().queryPreRecordInfo(currentDateQueryType, listBean.getTransactionId(), listBean.getBatSeq());
    }



    /**
     * 封装页面数据--列表
     *
     * @return
     */
    private PreRecordViewModel buildPreRecordViewModel() {
        mPreRecordViewModel.setDateType(currentDateQueryType);// 日期查询类型
        mPreRecordViewModel.setStartDate(startLocalDate.format(DateFormatters.dateFormatter1));// 起始日期
        mPreRecordViewModel.setEndDate(endLocalDate.format(DateFormatters.dateFormatter1));// 结束日期
        mPreRecordViewModel.setCurrentIndex(pageCurrentIndex);// 当前页
        mPreRecordViewModel.setPageSize(pageSize);// 每页条数
        mPreRecordViewModel.set_refresh(_refresh);
        return mPreRecordViewModel;
    }

    /**
     * 将接口返回数据封装成ListView所需要的model
     *
     * @param preRecordViewModel
     */
    private void copyResult2TransBean(PreRecordViewModel preRecordViewModel) {
        listViewBeanList.clear();
        for (int i = 0; i < preRecordViewModel.getList().size(); i++){

            LocalDate paymentDate = ApplicationContext.getInstance().getCurrentSystemDate().toLocalDate();
            //dateType为0时，执行日期；为1时，预约日期
            if ("0".equals(currentDateQueryType)){
                paymentDate  = preRecordViewModel.getList().get(i).getPaymentDate();
            }else if("1".equals(currentDateQueryType)){
                paymentDate  = preRecordViewModel.getList().get(i).getFirstSubmitDate();
            }

            LocalDate localDate = paymentDate;
            String formatTime = "";//当前时间 MM月/yyyy
            String tempTime = "";//上一次时间
            if (localDate != null){
                formatTime = localDate.format(DateFormatters.monthFormatter1);
            }
            if (i > 0){
                LocalDate paymentDateTemp = ApplicationContext.getInstance().getCurrentSystemDate().toLocalDate();
                //dateType为0时，执行日期；为1时，预约日期
                if ("0".equals(currentDateQueryType)){
                    paymentDateTemp  = preRecordViewModel.getList().get(i - 1).getPaymentDate();
                }else if("1".equals(currentDateQueryType)){
                    paymentDateTemp  = preRecordViewModel.getList().get(i - 1).getFirstSubmitDate();
                }
                tempTime = paymentDateTemp.format(DateFormatters.monthFormatter1);
            }
            if (tempTime.equals(formatTime)){
                ShowListBean item = new ShowListBean();
                item.type = ShowListBean.CHILD;

                item.setTitleID(ShowListConst.TITLE_ORDER_MANAGE);
                item.setTime(localDate);
                item.setContentLeftAbove(mPreRecordViewModel.getList().get(i).getPayeeAccountName());//收款人姓名
                item.setContentLeftBelow(ResultStatusUtils.getPreRecordStatus(mContext, true, mPreRecordViewModel.getList().get(i).getStatus()));//状态
                item.setContentRightBelow(MoneyUtils.transMoneyFormat(mPreRecordViewModel.getList().get(i).getAmount(), mPreRecordViewModel.getList().get(i).getCurrency()));//金额
                item.setContentRightAbove(PublicCodeUtils.getCurrency(mContext, mPreRecordViewModel.getList().get(i).getCurrency()));//币种
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

                        itemFirst.setTitleID(ShowListConst.TITLE_ORDER_MANAGE);
                        itemFirst.setTime(localDate);
                        itemFirst.setContentLeftAbove(mPreRecordViewModel.getList().get(i).getPayeeAccountName());//收款人姓名
                        itemFirst.setContentLeftBelow(ResultStatusUtils.getPreRecordStatus(mContext, true, mPreRecordViewModel.getList().get(i).getStatus()));//状态
                        itemFirst.setContentRightBelow(MoneyUtils.transMoneyFormat(mPreRecordViewModel.getList().get(i).getAmount(), mPreRecordViewModel.getList().get(i).getCurrency()));//金额
                        itemFirst.setContentRightAbove(PublicCodeUtils.getCurrency(mContext, mPreRecordViewModel.getList().get(i).getCurrency()));//币种
                    }
                    listViewBeanList.add(itemFirst);
                }
            }
        }

//        for (int i = 0; i < preRecordViewModel.getList().size(); i++) {
//            TransactionBean transactionBean = new TransactionBean();
//
//            LocalDate paymentDate = ApplicationContext.getInstance().getCurrentSystemDate().toLocalDate();
//            //dateType为0时，执行日期；为1时，预约日期
//            if ("0".equals(currentDateQueryType)){
//                paymentDate  = preRecordViewModel.getList().get(i).getPaymentDate();
//            }else if("1".equals(currentDateQueryType)){
//                paymentDate  = preRecordViewModel.getList().get(i).getFirstSubmitDate();
//            }
//
//            transactionBean.setTitleID(TransactionView.TITLE_ORDER_MANAGE);
//            transactionBean.setTime(paymentDate);//日期
//            transactionBean.setContentLeftAbove(mPreRecordViewModel.getList().get(i).getPayeeAccountName());//收款人姓名
//            transactionBean.setContentLeftBelowAgain(ResultStatusUtils.getPreRecordStatus(mContext, mPreRecordViewModel.getList().get(i).getStatus()));//状态
//            transactionBean.setContentRightBelow(MoneyUtils.transMoneyFormat(mPreRecordViewModel.getList().get(i).getAmount(), mPreRecordViewModel.getList().get(i).getCurrency()));//金额
//            transactionBean.setContentRightAboveL(PublicCodeUtils.getCurrency(mContext, mPreRecordViewModel.getList().get(i).getCurrency()));//币种
//
//            listViewBeanList.add(transactionBean);
//        }
    }


    /**
     * 处理请求成功后的数据
     *
     * @param preRecordViewModel
     */
    private void handleSuccessData(PreRecordViewModel preRecordViewModel) {
        mPreRecordViewModel.setRecordCount(preRecordViewModel.getRecordCount());
        if (preRecordViewModel.getList().size() != 0) {
            List<PreRecordViewModel.ListBean> listBeen = new ArrayList<PreRecordViewModel.ListBean>();
            if (mPreRecordViewModel.getList() != null) {
                listBeen.addAll(mPreRecordViewModel.getList());
            }
            listBeen.addAll(preRecordViewModel.getList());
            mPreRecordViewModel.setList(listBeen);

            copyResult2TransBean(mPreRecordViewModel);

            if (pageCurrentIndex == 0) {
                //关闭加载对话框
                closeProgressDialog();
            }
            if (isPullToRefresh) {
                preRecordRefresh.loadmoreCompleted(PullToRefreshLayout.SUCCEED);
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
                preRecordRefresh.loadmoreCompleted(PullToRefreshLayout.NO_MORE_DATA);
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
            preRecordRefresh.loadmoreCompleted(PullToRefreshLayout.FAIL);
        }
        //将所有数据处理完后为ListView设置数据
        mAdapter.setData(listViewBeanList);
        showErrorDialog(biiResultErrorException.getErrorMessage());
    }




    /**
     * 预约管理查询 列表 成功
     *
     * @param preRecordViewModel
     */
    @Override
    public void queryPreRecordListSuccess(PreRecordViewModel preRecordViewModel) {
        handleSuccessData(preRecordViewModel);
    }

    /**
     * 预约管理查询 列表 失败
     *
     * @param biiResultErrorException
     */
    @Override
    public void queryPreRecordListFail(BiiResultErrorException biiResultErrorException) {
        handleFailData(biiResultErrorException);
    }

    /**
     * 预约管理查询 详情 成功
     *
     * @param infoViewModel
     */
    @Override
    public void queryPreRecordInfoSuccess(PreRecordDetailInfoViewModel infoViewModel) {
        closeProgressDialog();
        // 跳转到详情页
        Bundle bundle = new Bundle();
        bundle.putParcelable("DetailsInfo", infoViewModel);
        bundle.putString("DateType", currentDateQueryType);
        PreRecordDetailInfoFragment preRecordDetailInfoFragment = new PreRecordDetailInfoFragment();
        preRecordDetailInfoFragment.setArguments(bundle);
        startForResult(preRecordDetailInfoFragment, REQUEST_CODE_DETAIL_INFO);
    }

    /**
     * 预约管理查询 详情 失败
     *
     * @param biiResultErrorException
     */
    @Override
    public void queryPreRecordInfoFail(BiiResultErrorException biiResultErrorException) {
        closeProgressDialog();
        showErrorDialog(biiResultErrorException.getErrorMessage());
    }


    @Override
    protected PreRecordContract.Presenter initPresenter() {
        return new PreRecordPresenter(this);
    }
}
