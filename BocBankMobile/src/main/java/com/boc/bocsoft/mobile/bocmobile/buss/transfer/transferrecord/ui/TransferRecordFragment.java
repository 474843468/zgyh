package com.boc.bocsoft.mobile.bocmobile.buss.transfer.transferrecord.ui;

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
import com.boc.bocsoft.mobile.bocmobile.base.widget.edittext.ClearEditText;
import com.boc.bocsoft.mobile.bocmobile.base.widget.moneyinputview.MoneyInputTextView;
import com.boc.bocsoft.mobile.bocmobile.base.widget.refreshliseview.PullToRefreshLayout;
import com.boc.bocsoft.mobile.bocmobile.base.widget.selectaccoutview.SelectAccountButton;
import com.boc.bocsoft.mobile.bocmobile.base.widget.selectview.selecttimerangenew.SelectTimeRangeNew;
import com.boc.bocsoft.mobile.bocmobile.base.widget.showlistview.PinnedSectionListView;
import com.boc.bocsoft.mobile.bocmobile.base.widget.showlistview.ShowListConst;
import com.boc.bocsoft.mobile.bocmobile.base.widget.showlistview.adapter.ShowListAdapter;
import com.boc.bocsoft.mobile.bocmobile.base.widget.showlistview.bean.ShowListBean;
import com.boc.bocsoft.mobile.bocmobile.base.widget.singlemoreselect.Content;
import com.boc.bocsoft.mobile.bocmobile.base.widget.singlemoreselect.SelectGridView;
import com.boc.bocsoft.mobile.bocmobile.buss.account.overview.ui.OverviewFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.phone.ui.PhoneEditPageFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.qrcode.ui.QrcodeTransFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.transferrecord.model.RemitReturnInfoViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.transferrecord.model.TransferRecordDetailInfoViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.transferrecord.model.TransferRecordViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.transferrecord.model.TransferRecordViewModelNew;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.transferrecord.presenter.TransferRecordPresenter;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.transremit.ui.TransRemitBlankFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.utils.PublicUtils;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.utils.ResultStatusUtils;
import com.boc.bocsoft.mobile.common.utils.StringUtils;
import com.boc.bocsoft.mobile.common.utils.date.DateFormatters;
import com.boc.bocsoft.mobile.framework.widget.TitleBarView;

import org.threeten.bp.LocalDate;

import java.util.ArrayList;
import java.util.List;


/**
 * 转账记录查询
 * Created by wangf on 2016/6/21.
 */
public class TransferRecordFragment extends MvpBussFragment<TransferRecordContract.Presenter> implements TransferRecordContract.View {

    /**
     * 根布局
     */
    private View mRootView;

    /**
     * fragment的页面
     */
    //详情的ListView
    PinnedSectionListView transferRecordListView;
    ShowListAdapter mAdapter;
    //详情的上拉刷新
    PullToRefreshLayout transferRecordRefresh;
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
    private LinearLayout llTransferRecordSelect;
    //筛选处的范围
    private TextView tvTransferRecordRange;
    //筛选
    private TextView tvTransferRecordSelect;
    //筛选的imageView
    private ImageView ivTransferRecordSelect;

    /**
     * 筛选的 金额 收款人 交易状态
     */
    //筛选处额外添加的layout
    private LinearLayout llAddToLayout;
    //起始金额
    private MoneyInputTextView selectTransStartAmountView;
    //终止金额
    private MoneyInputTextView selectTransEndAmountView;
    //收款人名称
    private ClearEditText selectTransPayeeAccountNameView;
    //交易状态
    private SelectGridView selectTransStatusView;

    //交易状态筛选的数据
    private List<Content> selectStatusList;
    //筛选出的当前 起始金额 信息
    private String currentTransStartAmount;
    private boolean isSelectStartAmount;
    //筛选出的当前 结束金额 信息
    private String currentTransEndAmount;
    private boolean isSelectEndAmount;
    //筛选出的当前 收款人 信息
    private String currentTransPayeeAccountName;
    //筛选出的当前 交易状态 信息
    private String currentTransStatus;
    private String lastTransStatus;


    /**
     * 侧滑菜单
     */
    private SlipDrawerLayout mDrawerLayout;

    //起始时间
    private LocalDate startLocalDate;
    //结束时间
    private LocalDate endLocalDate;


//    //转账记录service通信处理类
//    private TransferRecordPresenter mTransferRecordPresenter;
    //转账记录UI层model
//    private TransferRecordViewModel mTransferRecordViewModel;
    //转账记录UI层model - 新接口
    private TransferRecordViewModelNew mTransferRecordViewModelNew;

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

    /**
     * 退汇交易状态
     * 1表示已经退汇
     */
    private final static String REMIT_RETURN_TRUE = "1";

    //ListView所需要的List
    private List<ShowListBean> listViewBeanList;

    //判断是否是筛选
    private boolean isSelectData;
    //判断是否是上拉加载
    private boolean isPullToRefresh;

    public static final String TRANS_FROM_KEY = "TransFromKey";
    public static final int TRANS_FROM_LIST = 1;
    public static final int TRANS_FROM_RESULT = 2;
    public static final int TRANS_FROM_QRCODE = 3;
    public static final int TRANS_FROM_PHONE = 4;
    public static final int TRANS_FROM_TRANS_OVERVIEW = 5;
    private int transRecordFrom;


    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        mRootView = View.inflate(mContext, R.layout.boc_fragment_transfer_query, null);
        transRecordFrom = getArguments().getInt(TRANS_FROM_KEY, 0);
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
        transferRecordListView = (PinnedSectionListView) mRootView.findViewById(R.id.lv_transfer_query);
        transferRecordRefresh = (PullToRefreshLayout) mRootView.findViewById(R.id.refresh_transfer_query);
        tvNoData = (TextView) mRootView.findViewById(R.id.no_data_transfer_query);

        headView = LayoutInflater.from(mContext).inflate(R.layout.boc_head_account_transdetail, null);

        //页面title初始化
        initQueryTitleView();

        //账户选择
        selectAccountButton = (SelectAccountButton) headView.findViewById(R.id.select_account_button);
        selectAccountButton.setVisibility(View.GONE);

        //筛选
        llTransferRecordSelect = (LinearLayout) headView.findViewById(R.id.ll_transdetail_select);
        tvTransferRecordSelect = (TextView) headView.findViewById(R.id.tv_transdetail_select);
        ivTransferRecordSelect = (ImageView) headView.findViewById(R.id.iv_transdetail_select);
        tvTransferRecordRange = (TextView) headView.findViewById(R.id.tv_transdetail_range);

        //侧滑菜单
        mDrawerLayout = (SlipDrawerLayout) mRootView.findViewById(R.id.drawer_layout_transfer_query);
        rightDrawer = (SelectTimeRangeNew) mRootView.findViewById(R.id.right_drawer_transfer_query);

        //筛选中的 金额 收款人 交易状态
        View selectTransLayout = View.inflate(mContext, R.layout.boc_view_trans_select_transferrecord, null);
        selectTransStartAmountView = (MoneyInputTextView) selectTransLayout.findViewById(R.id.trans_select_start_amount);
        selectTransEndAmountView = (MoneyInputTextView) selectTransLayout.findViewById(R.id.trans_select_end_amount);
        selectTransPayeeAccountNameView = (ClearEditText) selectTransLayout.findViewById(R.id.trans_select_payee_account_name);
        selectTransStatusView = (SelectGridView) selectTransLayout.findViewById(R.id.trans_select_single_trans_status);
        llAddToLayout = rightDrawer.getAddToLayout();
        llAddToLayout.addView(selectTransLayout);
        initSelectView();

        transferRecordListView.addHeaderView(headView, null, false);
        mAdapter = new ShowListAdapter(mContext, -1);
        transferRecordListView.saveAdapter(mAdapter);
        transferRecordListView.setShadowVisible(false);
        transferRecordListView.setAdapter(mAdapter);
//        transferRecordListView.addHeaderView(headView, null, false);
//        transferRecordListView.setAdapter();

    }


    /**
     * 初始化Title
     */
    private void initQueryTitleView() {
        queryTitleView = (TitleBarView) headView.findViewById(R.id.select_title_view);
        queryTitleView.setStyle(R.style.titlebar_common_white);
        queryTitleView.setTitle(getResources().getString(R.string.boc_transfer_trans_record_query_title));
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

        currentTransStartAmount = "0";
        currentTransEndAmount = "99999999999.99";

        startLocalDate = ApplicationContext.getInstance().getCurrentSystemDate().toLocalDate().plusMonths(-MAX_QUERY_RANGE).plusDays(1);
        endLocalDate = ApplicationContext.getInstance().getCurrentSystemDate().toLocalDate();
        rightDrawer.setDefaultDate(startLocalDate.format(DateFormatters.dateFormatter1),
                endLocalDate.format(DateFormatters.dateFormatter1));
        tvTransferRecordRange.setText(getResources().getString(R.string.boc_common_query_month_3));

//        mTransferRecordPresenter = new TransferRecordPresenter(this);
//        mTransferRecordViewModel = new TransferRecordViewModel();
        mTransferRecordViewModelNew = new TransferRecordViewModelNew();

        listViewBeanList = new ArrayList<>();

//        queryTransferRecordList();
        queryTransferRecordListNew();
    }

    @Override
    public void reInit() {
        super.reInit();
        listViewBeanList.clear();
        if (mTransferRecordViewModelNew.getList() != null) {
            mTransferRecordViewModelNew.getList().clear();
        }
        pageCurrentIndex = 0;
        queryTransferRecordListNew();
    }

    /**
     * 初始化 交易状态 筛选数据
     */
    private void initSelectView() {
        String[] selectStatusNameData = {"全部", "交易成功", "交易失败", "银行处理中"};
        String[] selectStatusIdData = {"0", "1", "2", "3"};

        selectStatusList = new ArrayList<Content>();

        for (int i = 0; i < selectStatusNameData.length; i++) {
            Content item = new Content();
            item.setName(selectStatusNameData[i]);
            item.setContentNameID(selectStatusIdData[i]);
            if (i == 0) {
                currentTransStatus = selectStatusIdData[i];
                lastTransStatus = currentTransStatus;
                item.setSelected(true);
            }
            selectStatusList.add(item);
        }
        selectTransStatusView.setData(selectStatusList);
    }

    /**
     * 重置交易状态的选中
     */
    private void cancelTransStatusSelected() {
        for (int i = 0; i < selectStatusList.size(); i++) {
            Content item = selectStatusList.get(i);
            if (item.getContentNameID().equals(lastTransStatus)) {
                item.setSelected(true);
            } else {
                item.setSelected(false);
            }
        }
        selectTransStatusView.getAdapter().notifyDataSetChanged();
    }

    /**
     * 获取交易状态的筛选
     * @return
     */
    private boolean getSelectTransStatus() {
        boolean haveSelected = false;
        for (Content item : selectStatusList) {
            if (item.getSelected()) {
                haveSelected = true;
                currentTransStatus = item.getContentNameID();
                break;
            }
        }
        return haveSelected;
    }



    @Override
    public void setListener() {
        //筛选layout的点击事件
        llTransferRecordSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectStatustChange();
                setAmountAndPayeeDefault();
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
                // modify by wangf on 2016-12-4 10:38:29 取消时未将交易状态的筛选重置
                cancelTransStatusSelected();
                mDrawerLayout.toggle();
            }

            @Override
            public void rightClick(boolean haveSelected, String content) {
                if (haveSelected) {
                    if (content.contains("月")){
                        tvTransferRecordRange.setText(content.replace("月", "个月") + "查询结果");
                    }else{
                        tvTransferRecordRange.setText(content + "查询结果");
                    }
                } else {
                    tvTransferRecordRange.setText("");
                }

                //金额的获取
                String startAmount = selectTransStartAmountView.getInputMoney();
                String endAmount = selectTransEndAmountView.getInputMoney();
                if (StringUtils.isEmpty(startAmount)) {
                    isSelectStartAmount = false;
                    currentTransStartAmount = "0";
                } else {
                    isSelectStartAmount = true;
                    currentTransStartAmount = startAmount;
                }
                if (StringUtils.isEmpty(endAmount)) {
                    isSelectEndAmount = false;
                    currentTransEndAmount = "99999999999.99";
                } else {
                    isSelectEndAmount = true;
                    currentTransEndAmount = endAmount;
                }
                //收款人的获取
                currentTransPayeeAccountName = selectTransPayeeAccountNameView.getText().toString();

                getSelectTransStatus();
                lastTransStatus = currentTransStatus;

                String startTime = rightDrawer.getStartDate();
                String endTime = rightDrawer.getEndDate();
                startLocalDate = LocalDate.parse(startTime, DateFormatters.dateFormatter1);
                endLocalDate = LocalDate.parse(endTime, DateFormatters.dateFormatter1);
                if (PublicUtils.judgeChoiceDateRange(startLocalDate, endLocalDate, MAX_QUERY_RANGE, TransferRecordFragment.this)) {
                    mDrawerLayout.toggle();
                    listViewBeanList.clear();
                    if (mTransferRecordViewModelNew.getList() != null) {
                        mTransferRecordViewModelNew.getList().clear();
                    }
                    pageCurrentIndex = 0;
//                    queryTransferRecordList();
                    queryTransferRecordListNew();
                    isSelectData = true;
                    isPullToRefresh = false;
                }
            }
        });

        //筛选重置按钮的监听
        rightDrawer.setResetListener(new SelectTimeRangeNew.ResetClickListener() {
            @Override
            public void resetClick() {
                setAmountAndPayeeDefault();
                cancelTransStatusSelected();
                // modify by wangf on 2016-12-4 10:39:24 组件控制重置的状态
//                rightDrawer.setClickSelectDefaultData();
            }
        });

        //listView的item点击事件
        transferRecordListView.setListener(new PinnedSectionListView.ClickListener() {
            @Override
            public void onItemClickListener(int position) {
//                if (REMIT_RETURN_TRUE.equals(mTransferRecordViewModel.getList().get(position).getReexchangeStatus())) {
//                    //查询退汇信息--当退汇交易状态为1时，需要查询退汇信息
//                    queryDetailAndRemitReturnInfo(position);
//                } else {
//                    //查询转账记录详情
//                    queryTransferRecordDetailInfo(position);
//                }
                if (REMIT_RETURN_TRUE.equals(mTransferRecordViewModelNew.getList().get(position).getReexchangeStatus())) {
                    //查询退汇信息--当退汇交易状态为1时，需要查询退汇信息
                    queryDetailAndRemitReturnInfo(position);
                } else {
                    //查询转账记录详情
                    queryTransferRecordDetailInfo(position);
                }
            }
        });

        //上拉加载
        transferRecordRefresh.setOnLoadListener(new PullToRefreshLayout.OnLoadListener() {
            @Override
            public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
                startPresenter();
                if (mTransferRecordViewModelNew.getList() != null) {
                    if (mTransferRecordViewModelNew.getList().size() < mTransferRecordViewModelNew.getRecordnumber()) {
                        isPullToRefresh = true;
//                        queryTransferRecordList();
                        queryTransferRecordListNew();
                    } else {
                        transferRecordRefresh.loadmoreCompleted(PullToRefreshLayout.NO_MORE_DATA);
                    }
                } else {
                    listViewBeanList.clear();
                    pageCurrentIndex = 0;
                    isPullToRefresh = true;
//                    queryTransferRecordList();
                    queryTransferRecordListNew();
                }
            }
        });

        //交易状态筛选
        selectTransStatusView.setListener(new SelectGridView.ClickListener() {
            @Override
            public void setItemClick(AdapterView<?> parent, View view, int position, long id) {
                Content selectCurrency = (Content) parent.getAdapter().getItem(position);
                currentTransStatus = selectCurrency.getContentNameID();
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


    /**
     * 筛选按钮的颜色改变
     */
    private void selectStatustChange() {
        tvTransferRecordSelect.setTextColor(getResources().getColor(R.color.boc_text_color_red));
        ivTransferRecordSelect.setImageResource(R.drawable.boc_select_red);
    }


    /**
     * 设置金额范围和收款人为默认状态
     */
    private void setAmountAndPayeeDefault() {
        if (isSelectStartAmount){
            selectTransStartAmountView.setInputMoney(currentTransStartAmount);
        }else{
            selectTransStartAmountView.clearText();
        }
        if (isSelectEndAmount){
            selectTransEndAmountView.setInputMoney(currentTransEndAmount);
        }else{
            selectTransEndAmountView.clearText();
        }
        selectTransPayeeAccountNameView.setText("");
    }


//    /**
//     * 调用接口，查询转账记录数据
//     */
//    private void queryTransferRecordList() {
//        if (pageCurrentIndex == 0) {
//            //开启加载对话框
//            showLoadingDialog();
//        }
//        mTransferRecordPresenter.queryTransferRecordList(buildTransferRecordViewModel());
//    }

    /**
     * 调用接口，查询转账记录数据 - 新接口
     */
    private void queryTransferRecordListNew() {
        if (pageCurrentIndex == 0) {
            //开启加载对话框
            showLoadingDialog();
        }
        getPresenter().queryTransferRecordListNew(buildTransferRecordViewModelNew());
    }

    /**
     * 调用接口，查询转账记录详情数据
     */
    private void queryTransferRecordDetailInfo(int position) {
        //开启加载对话框
        showLoadingDialog();
        getPresenter().queryTransferRecordDetailInfo(mTransferRecordViewModelNew.getList().get(position).getTransactionId());
    }

    /**
     * 调用接口，查询 详情和退汇信息
     */
    private void queryDetailAndRemitReturnInfo(int position) {
        //开启加载对话框
        showLoadingDialog();
        getPresenter().queryDetailAndRemitReturnInfo(mTransferRecordViewModelNew.getList().get(position).getTransactionId());
    }


//    /**
//     * 封装页面数据--转账记录列表
//     *
//     * @return
//     */
//    private TransferRecordViewModel buildTransferRecordViewModel() {
//        mTransferRecordViewModel.setTransType(ApplicationConst.PB999);//转账类型此处传 全部类型
//        mTransferRecordViewModel.setStartDate(startLocalDate.format(DateFormatters.dateFormatter1));//起始日期
//        mTransferRecordViewModel.setEndDate(endLocalDate.format(DateFormatters.dateFormatter1));//结束日期
//        mTransferRecordViewModel.setPageSize(pageSize);//页面大小
//        mTransferRecordViewModel.setCurrentIndex(pageCurrentIndex);//当前页索引
//        return mTransferRecordViewModel;
//    }

    /**
     * 封装页面数据--转账记录列表 - 新接口
     *
     * @return
     */
    private TransferRecordViewModelNew buildTransferRecordViewModelNew() {
        mTransferRecordViewModelNew.setTransType(ApplicationConst.PB999);//转账类型此处传 全部类型
        mTransferRecordViewModelNew.setStartDate(startLocalDate.format(DateFormatters.dateFormatter1));//起始日期
        mTransferRecordViewModelNew.setEndDate(endLocalDate.format(DateFormatters.dateFormatter1));//结束日期
        mTransferRecordViewModelNew.setStartAmount(currentTransStartAmount);
        mTransferRecordViewModelNew.setEndAmount(currentTransEndAmount);
        mTransferRecordViewModelNew.setPayeeAccountName(currentTransPayeeAccountName);
        mTransferRecordViewModelNew.setStatus(currentTransStatus);
        mTransferRecordViewModelNew.setPageSize(pageSize);//页面大小
        mTransferRecordViewModelNew.setCurrentIndex(pageCurrentIndex);//当前页索引
        return mTransferRecordViewModelNew;
    }

//    /**
//     * 将接口返回数据封装成ListView所需要的model
//     *
//     * @param transferRecordViewModel
//     */
//    private void copyResult2TransBean(TransferRecordViewModel transferRecordViewModel) {
//        for (int i = 0; i < transferRecordViewModel.getList().size(); i++) {
//            TransactionBean transactionBean = new TransactionBean();
//
//            LocalDate paymentDate = transferRecordViewModel.getList().get(i).getPaymentDate();
//
//            transactionBean.setTitleID(TransactionView.TITLE_DETAIL_TYPE);
//            transactionBean.setTime(paymentDate);//日期
//            transactionBean.setContentLeftAbove(transferRecordViewModel.getList().get(i).getPayeeAccountName());//收款人名称
//            transactionBean.setContentRightBelow(MoneyUtils.transMoneyFormat(transferRecordViewModel.getList().get(i).getAmount(), transferRecordViewModel.getList().get(i).getFeeCur()));//金额
//            transactionBean.setContentRightAboveL(PublicCodeUtils.getCurrency(mContext, transferRecordViewModel.getList().get(i).getFeeCur()));//币种
//
//            listViewBeanList.add(transactionBean);
//        }
//    }

    /**
     * 将接口返回数据封装成ListView所需要的model - 新接口
     *
     * @param transferRecordViewModel
     */
    private void copyResult2TransBeanNew(TransferRecordViewModelNew transferRecordViewModel) {
        listViewBeanList.clear();
        for (int i = 0; i < transferRecordViewModel.getList().size(); i++){
            LocalDate localDate = transferRecordViewModel.getList().get(i).getPaymentDate();
            String formatTime = "";//当前时间 MM月/yyyy
            String tempTime = "";//上一次时间
            if (localDate != null){
                formatTime = localDate.format(DateFormatters.monthFormatter1);
            }
            if (i > 0){
                tempTime = transferRecordViewModel.getList().get(i - 1).getPaymentDate().format(DateFormatters.monthFormatter1);
            }
            if (tempTime.equals(formatTime)){
                ShowListBean item = new ShowListBean();
                item.type = ShowListBean.CHILD;

                item.setTitleID(ShowListConst.TITLE_ORDER_MANAGE);
                item.setTime(localDate);
                item.setContentLeftAbove(transferRecordViewModel.getList().get(i).getPayeeAccountName());//收款人名称
                item.setContentLeftBelow(ResultStatusUtils.getTransferStatus(mContext, true, transferRecordViewModel.getList().get(i).getStatus()));//状态
                item.setContentRightAbove(PublicCodeUtils.getCurrency(mContext, transferRecordViewModel.getList().get(i).getFeeCur()));//币种
                item.setContentRightBelow(MoneyUtils.transMoneyFormat(transferRecordViewModel.getList().get(i).getAmount(), transferRecordViewModel.getList().get(i).getFeeCur()));//金额
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
                        itemFirst.setContentLeftAbove(transferRecordViewModel.getList().get(i).getPayeeAccountName());//收款人名称
                        itemFirst.setContentLeftBelow(ResultStatusUtils.getTransferStatus(mContext, true, transferRecordViewModel.getList().get(i).getStatus()));//状态
                        itemFirst.setContentRightAbove(PublicCodeUtils.getCurrency(mContext, transferRecordViewModel.getList().get(i).getFeeCur()));//币种
                        itemFirst.setContentRightBelow(MoneyUtils.transMoneyFormat(transferRecordViewModel.getList().get(i).getAmount(), transferRecordViewModel.getList().get(i).getFeeCur()));//金额
                    }
                    listViewBeanList.add(itemFirst);
                }
            }
        }


//        for (int i = 0; i < transferRecordViewModel.getList().size(); i++) {
//            TransactionBean transactionBean = new TransactionBean();
//
//            LocalDate paymentDate = transferRecordViewModel.getList().get(i).getPaymentDate();
//
//            transactionBean.setTitleID(TransactionView.TITLE_ORDER_MANAGE);
//            transactionBean.setTime(paymentDate);//日期
//            transactionBean.setContentLeftAbove(transferRecordViewModel.getList().get(i).getPayeeAccountName());//收款人名称
//            transactionBean.setContentLeftBelowAgain(ResultStatusUtils.getTransferStatus(mContext, transferRecordViewModel.getList().get(i).getStatus()));//状态
//            transactionBean.setContentRightBelow(MoneyUtils.transMoneyFormat(transferRecordViewModel.getList().get(i).getAmount(), transferRecordViewModel.getList().get(i).getFeeCur()));//金额
//            transactionBean.setContentRightAboveL(PublicCodeUtils.getCurrency(mContext, transferRecordViewModel.getList().get(i).getFeeCur()));//币种
//
//            listViewBeanList.add(transactionBean);
//        }
    }


//    /**
//     * 处理请求成功后的数据
//     *
//     * @param transferRecordViewModel
//     */
//    private void handleSuccessData(TransferRecordViewModel transferRecordViewModel) {
//        mTransferRecordViewModel.setRecordnumber(transferRecordViewModel.getRecordnumber());
//        if (transferRecordViewModel.getList().size() != 0) {
//            List<TransferRecordViewModel.ListBean> listBeen = new ArrayList<TransferRecordViewModel.ListBean>();
//            if (mTransferRecordViewModel.getList() != null) {
//                listBeen.addAll(mTransferRecordViewModel.getList());
//            }
//            listBeen.addAll(transferRecordViewModel.getList());
//            mTransferRecordViewModel.setList(listBeen);
//
//            copyResult2TransBean(transferRecordViewModel);
//
//            if (pageCurrentIndex == 0) {
//                //关闭加载对话框
//                closeProgressDialog();
//            }
//            if (isPullToRefresh) {
//                transferRecordRefresh.loadmoreCompleted(PullToRefreshLayout.SUCCEED);
//            }
////            haveDataSelectStatust(true);
////            haveDataSelectText(true);
//            PublicUtils.haveDataSelectText(true, tvNoData, isSelectData);
//
//        } else {
//            if (pageCurrentIndex == 0) {
//                //关闭加载对话框
//                closeProgressDialog();
////                haveDataSelectStatust(false);
////                haveDataSelectText(false);
//                PublicUtils.haveDataSelectText(false, tvNoData, isSelectData);
//            } else {
////                haveDataSelectStatust(true);
////                haveDataSelectText(true);
//                PublicUtils.haveDataSelectText(true, tvNoData, isSelectData);
//            }
//            if (isPullToRefresh) {
//                transferRecordRefresh.loadmoreCompleted(PullToRefreshLayout.NO_MORE_DATA);
//            }
//        }
//        //将所有数据处理完后为ListView设置数据
//        transferRecordListView.setData(listViewBeanList);
//        pageCurrentIndex += pageSize;//此处的索引是按记录的索引来走
//    }

    /**
     * 处理请求成功后的数据 - 新接口
     *
     * @param transferRecordViewModel
     */
    private void handleSuccessDataNew(TransferRecordViewModelNew transferRecordViewModel) {
        mTransferRecordViewModelNew.setRecordnumber(transferRecordViewModel.getRecordnumber());
        if (transferRecordViewModel.getList().size() != 0) {
            List<TransferRecordViewModelNew.ListBean> listBeen = new ArrayList<TransferRecordViewModelNew.ListBean>();
            if (mTransferRecordViewModelNew.getList() != null) {
                listBeen.addAll(mTransferRecordViewModelNew.getList());
            }
            listBeen.addAll(transferRecordViewModel.getList());
            mTransferRecordViewModelNew.setList(listBeen);

            copyResult2TransBeanNew(mTransferRecordViewModelNew);

            if (pageCurrentIndex == 0) {
                //关闭加载对话框
                closeProgressDialog();
            }
            if (isPullToRefresh) {
                transferRecordRefresh.loadmoreCompleted(PullToRefreshLayout.SUCCEED);
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
                transferRecordRefresh.loadmoreCompleted(PullToRefreshLayout.NO_MORE_DATA);
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
            transferRecordRefresh.loadmoreCompleted(PullToRefreshLayout.FAIL);
        }
        //将所有数据处理完后为ListView设置数据
        mAdapter.setData(listViewBeanList);
        showErrorDialog(biiResultErrorException.getErrorMessage());
    }


    /**
     * 查询转账记录列表成功
     *
     * @param transferRecordViewModel
     */
    @Override
    public void queryTransferRecordListSuccess(TransferRecordViewModel transferRecordViewModel) {
//        handleSuccessData(transferRecordViewModel);
    }

    /**
     * 查询转账记录列表成功 - 新接口
     *
     * @param transferRecordViewModel
     */
    @Override
    public void queryTransferRecordListSuccessNew(TransferRecordViewModelNew transferRecordViewModel) {
        handleSuccessDataNew(transferRecordViewModel);
    }

    /**
     * 查询转账记录列表失败
     *
     * @param biiResultErrorException
     */
    @Override
    public void queryTransferRecordListFail(BiiResultErrorException biiResultErrorException) {
        handleFailData(biiResultErrorException);
    }

    /**
     * 查询转账记录 详细信息 成功
     *
     * @param infoViewModel
     */
    @Override
    public void queryTransferRecordDetailInfoSuccess(TransferRecordDetailInfoViewModel infoViewModel) {
        closeProgressDialog();
        // 跳转到详情页
        Bundle bundle = new Bundle();
        bundle.putBoolean("HaveReturn", false);
        bundle.putParcelable("DetailsInfo", infoViewModel);
        TransferRecordDetailInfoFragment queryDetailInfoFragment = new TransferRecordDetailInfoFragment();
        queryDetailInfoFragment.setArguments(bundle);
        start(queryDetailInfoFragment);
    }

    /**
     * 查询转账记录 详细信息 失败
     *
     * @param biiResultErrorException
     */
    @Override
    public void queryTransferRecordDetailInfoFail(BiiResultErrorException biiResultErrorException) {
        closeProgressDialog();
        showErrorDialog(biiResultErrorException.getErrorMessage());
    }

    /**
     * 查询退汇交易信息成功
     *
     * @param infoViewModel
     */
    @Override
    public void queryRemitReturnInfoSuccess(RemitReturnInfoViewModel infoViewModel) {
        closeProgressDialog();
        // 跳转到详情页
        Bundle bundle = new Bundle();
        bundle.putBoolean("HaveReturn", true);
        bundle.putParcelable("DetailsInfo", infoViewModel);
        TransferRecordDetailInfoFragment queryDetailInfoFragment = new TransferRecordDetailInfoFragment();
        queryDetailInfoFragment.setArguments(bundle);
        start(queryDetailInfoFragment);
    }

    /**
     * 查询退汇交易信息失败
     *
     * @param biiResultErrorException
     */
    @Override
    public void queryRemitReturnInfoFail(BiiResultErrorException biiResultErrorException) {
        closeProgressDialog();
        showErrorDialog(biiResultErrorException.getErrorMessage());
    }


//    @Override
//    protected void titleLeftIconClick() {
//        switch (transRecordFrom){
//            case TRANS_FROM_LIST:
//                super.titleLeftIconClick();
//                break;
//            case TRANS_FROM_RESULT:
//                popToAndReInit(TransRemitBlankFragment.class);
//                break;
//            default:
//                super.titleLeftIconClick();
//                break;
//        }
//    }
//
//
    @Override
    public boolean onBack() {
        switch (transRecordFrom){
            case TRANS_FROM_LIST:
                pop();
                break;
            case TRANS_FROM_RESULT:
                popToAndReInit(TransRemitBlankFragment.class);
                break;
            case TRANS_FROM_QRCODE:
                popToAndReInit(QrcodeTransFragment.class);
                break;
            case TRANS_FROM_PHONE:
                popToAndReInit(PhoneEditPageFragment.class);
                break;
            case TRANS_FROM_TRANS_OVERVIEW:
                popToAndReInit(OverviewFragment.class);
                break;
            default:
                pop();
                break;
        }
        return false;
    }

    @Override
    protected TransferRecordContract.Presenter initPresenter() {
        return new TransferRecordPresenter(this);
    }
}
