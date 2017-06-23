package com.boc.bocsoft.mobile.bocmobile.buss.qrcodepay.recordquery.ui;

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
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.model.AccountBean;
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
import com.boc.bocsoft.mobile.bocmobile.base.widget.singlemoreselect.Content;
import com.boc.bocsoft.mobile.bocmobile.base.widget.singlemoreselect.SelectGridView;
import com.boc.bocsoft.mobile.bocmobile.buss.qrcodepay.recordquery.model.QRPayPaymentRecordViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.qrcodepay.recordquery.presenter.QRPayPaymentRecordPresenter;
import com.boc.bocsoft.mobile.bocmobile.buss.qrcodepay.setting.ui.QRPayMainFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.qrcodepay.utils.QrCodeStatusUtils;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.utils.PublicUtils;
import com.boc.bocsoft.mobile.common.utils.NumberUtils;
import com.boc.bocsoft.mobile.common.utils.date.DateFormatters;

import org.threeten.bp.LocalDate;
import org.threeten.bp.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.List;


/**
 * 二维码支付 - 支付记录
 * Created by wangf on 2016/8/27.
 */
public class QRPayPaymentRecordFragment extends BussFragment implements QRPayPaymentRecordContract.PaymentRecordView {

    /**
     * 根布局
     */
    private View mRootView;

    /**
     * fragment的页面
     */
    //详情的ListView
    PinnedSectionListView paymentRecordListView;
    ShowListAdapter mAdapter;
    //详情的上拉刷新
    PullToRefreshLayout paymentRecordRefresh;
    //侧滑菜单
    SelectTimeRangeNew rightDrawer;
    //无数据的TextView
    private TextView tvNoData;

    /**
     * 侧滑菜单
     */
    private SlipDrawerLayout mDrawerLayout;

    /**
     * listView的Head
     */
    private View headView;
    //左侧返回
    private ImageView ivPaymentRecordLeftIcon;
    //标题
    private TextView tvPaymentRecordTitleName;
    //账户选择
    private SelectAccountButton selectAccountButton;
    //筛选的LinearLayout
    private LinearLayout llPaymentRecordSelect;
    //筛选
    private TextView tvPaymentRecordSelect;
    //筛选的imageView
    private ImageView ivPaymentRecordSelect;

    /**
     * 筛选的 账户 交易状态
     */
    //筛选处额外添加的layout
    private LinearLayout llAddToTopLayout;
    //账户
    private SelectGridView selectRecordAccountView;
    //交易状态
    private SelectGridView selectRecordStatusView;
    //账户筛选的数据
    private List<Content> selectAccountList;
    //交易状态筛选的数据
    private List<Content> selectStatusList;
    //筛选出的当前账户信息
    private String currentRecordAccount;
    private String lastRecordAccount;
    //筛选出的当前交易状态信息
    private String currentRecordStatus;
    private String lastRecordStatus;

    /**
     * 加载相关
     */
    //当前加载页码
    private int pageCurrentIndex;
    //每页大小
    private static int pageSize;
    private String _refresh = "false";

    /**
     * 查询范围相关
     */
    //最大查询范围为3个月
    private final static int MAX_QUERY_RANGE = 3;
    //最大查询起始日期为一年
    private final static int MAX_QUERY_DATE = 12;

    //ListView所需要的List
    private List<ShowListBean> listViewBeanList;

    //判断是否是筛选
    private boolean isSelectData;
    //判断是否是上拉加载
    private boolean isPullToRefresh;

    /**
     * 页面跳转相关
     */
    public static final String QRPAY_RECORD_INFO = "RecordInfo";
    public static final String QRPAY_RECORD_ACCOUNT = "RecordAccount";

    /**
     * 相关数据
     */
    //起始时间
    private LocalDate startLocalDate;
    //结束时间
    private LocalDate endLocalDate;

    //交易记录service通信处理类
    private QRPayPaymentRecordPresenter mPaymentRecordPresenter;
    //交易记录UI层model
    private QRPayPaymentRecordViewModel mPaymentRecordViewModel;
    //账户列表
    private List<AccountBean> accountBeanList;
    //用户选择的账户
    private AccountBean selectAccountBean;


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
        paymentRecordListView = (PinnedSectionListView) mRootView.findViewById(R.id.lv_transfer_query);
        paymentRecordRefresh = (PullToRefreshLayout) mRootView.findViewById(R.id.refresh_transfer_query);
        tvNoData = (TextView) mRootView.findViewById(R.id.no_data_transfer_query);

        headView = LayoutInflater.from(mContext).inflate(R.layout.boc_head_qrpay_record, null);

        //页面title初始化
        initQueryTitleView();

        //侧滑菜单
        mDrawerLayout = (SlipDrawerLayout) mRootView.findViewById(R.id.drawer_layout_transfer_query);
        rightDrawer = (SelectTimeRangeNew) mRootView.findViewById(R.id.right_drawer_transfer_query);

        //筛选中的 账户 交易状态
        View selectRecordLayout = View.inflate(mContext, R.layout.boc_view_qrpay_record_select, null);
        selectRecordAccountView = (SelectGridView) selectRecordLayout.findViewById(R.id.qrpay_record_select_single_account);
        selectRecordStatusView = (SelectGridView) selectRecordLayout.findViewById(R.id.qrpay_record_select_single_status);
        llAddToTopLayout = rightDrawer.getAddToTopLayout();
        llAddToTopLayout.setVisibility(View.VISIBLE);
        llAddToTopLayout.addView(selectRecordLayout);
   //     initSelectView();

        paymentRecordListView.addHeaderView(headView, null, false);
        mAdapter = new ShowListAdapter(mContext, -1);
        paymentRecordListView.saveAdapter(mAdapter);
        paymentRecordListView.setShadowVisible(false);
        paymentRecordListView.setAdapter(mAdapter);
//        paymentRecordListView.addHeaderView(headView, null, false);
//        paymentRecordListView.setAdapter();

    }


    /**
     * 初始化Title
     */
    private void initQueryTitleView() {
        ivPaymentRecordLeftIcon = (ImageView) headView.findViewById(R.id.iv_qrpay_left_icon);
        tvPaymentRecordTitleName = (TextView) headView.findViewById(R.id.tv_qrpay_title_name);
        llPaymentRecordSelect = (LinearLayout) headView.findViewById(R.id.ll_qrpay_select);
        tvPaymentRecordSelect = (TextView) headView.findViewById(R.id.tv_qrpay_select_name);
        ivPaymentRecordSelect = (ImageView) headView.findViewById(R.id.iv_qrpay_select_icon);
        selectAccountButton = (SelectAccountButton) headView.findViewById(R.id.qrpay_select_account_button);
        selectAccountButton.setArrowVisible(false);
        selectAccountButton.setEnabled(false);

        ivPaymentRecordLeftIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                titleLeftIconClick();
            }
        });
        llPaymentRecordSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            	selectStatustChange();
                rightDrawer.setClickSelectDefaultData();
                mDrawerLayout.toggle();
            }
        });
    }


   @Override
    public void initData() {
        pageSize = ApplicationConst.PAGE_SIZE;
        pageCurrentIndex = 0;

        startLocalDate = ApplicationContext.getInstance().getCurrentSystemDate().toLocalDate().plusWeeks(-1).plusDays(1);
        endLocalDate = ApplicationContext.getInstance().getCurrentSystemDate().toLocalDate();
        rightDrawer.setDefaultDate(startLocalDate.format(DateFormatters.dateFormatter1), endLocalDate.format(DateFormatters.dateFormatter1));
        rightDrawer.setDefaultSelect(0);

        mPaymentRecordPresenter = new QRPayPaymentRecordPresenter(this);
        mPaymentRecordViewModel = new QRPayPaymentRecordViewModel();
        listViewBeanList = new ArrayList<ShowListBean>();


        initSelectView();
 //     queryPaymentRecordList();
    }





    /**
     * 初始化 筛选数据
     */
    private void initSelectView() {
        //账户列表
        ArrayList<String> accountTypeList = new ArrayList<String>();
        accountTypeList.add(ApplicationConst.ACC_TYPE_BRO);//借记卡
        accountTypeList.add(ApplicationConst.ACC_TYPE_ZHONGYIN);//中银系列信用卡
        accountTypeList.add(ApplicationConst.ACC_TYPE_GRE);//长城信用卡
        accountBeanList = QRPayMainFragment.getRelativeBankAccountList(accountTypeList);
//        accountBeanList = ApplicationContext.getInstance().getChinaBankAccountList(accountTypeList);

        if (accountBeanList.size() > 0) {
            judgeNoAccountHeadVisible(true);
        } else {
            judgeNoAccountHeadVisible(false);
            return;
        }

        selectAccountList = new ArrayList<Content>();
        selectStatusList = new ArrayList<Content>();

        String[] selectStatusNameData = {"全部", "成功", "失败", "银行处理中"};
        String[] selectStatusIdData = {"4", "0", "1", "2"};

        //账户的筛选初始化
        for (int i = 0; i < accountBeanList.size(); i++) {
            Content item = new Content();
            item.setName(NumberUtils.formatCardNumberStrong(accountBeanList.get(i).getAccountNumber()));
            item.setContentNameID(accountBeanList.get(i).getAccountNumber());

            if (QRPayMainFragment.isSetDefaultCard){
                if (QRPayMainFragment.mDefaultAccountBean.getAccountId().equals(accountBeanList.get(i).getAccountId())){
                    currentRecordAccount = accountBeanList.get(i).getAccountNumber();
                    lastRecordAccount = currentRecordAccount;
                    item.setSelected(true);
                }
            }else{
                if (i == 0) {
                    currentRecordAccount = accountBeanList.get(i).getAccountNumber();
                    lastRecordAccount = currentRecordAccount;
                    item.setSelected(true);
                }
            }


            selectAccountList.add(item);
        }

        //交易状态的筛选初始化
        for (int i = 0; i < selectStatusNameData.length; i++) {
            Content item = new Content();
            item.setName(selectStatusNameData[i]);
            item.setContentNameID(selectStatusIdData[i]);
            if (i == 0) {
                currentRecordStatus = selectStatusIdData[i];
                lastRecordStatus = currentRecordStatus;
                item.setSelected(true);
            }
            selectStatusList.add(item);
        }

        selectRecordAccountView.setData(selectAccountList);
        selectRecordStatusView.setData(selectStatusList);

        selectAccountButton.setData(getAccountBeanByNumber(currentRecordAccount));

        queryPaymentRecordList();
    }


    @Override
    public void setListener() {
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
                String startTime = rightDrawer.getStartDate();
                String endTime = rightDrawer.getEndDate();
                startLocalDate = LocalDate.parse(startTime, DateFormatters.dateFormatter1);
                endLocalDate = LocalDate.parse(endTime, DateFormatters.dateFormatter1);
                if (PublicUtils.judgeChoiceDateRange(startLocalDate, endLocalDate, MAX_QUERY_RANGE, QRPayPaymentRecordFragment.this)) {
                    mDrawerLayout.toggle();

                    judgeSelectRecordAccount();
                    judgeSelectRecordStatus();
                    lastRecordAccount = currentRecordAccount;
                    lastRecordStatus = currentRecordStatus;
                    selectAccountButton.setData(getAccountBeanByNumber(currentRecordAccount));

                    listViewBeanList.clear();
                    if (mPaymentRecordViewModel.getList() != null) {
                        mPaymentRecordViewModel.getList().clear();
                    }
                    pageCurrentIndex = 0;
                    _refresh = "true";
                    isSelectData = true;
                    isPullToRefresh = false;
                    queryPaymentRecordList();
                }
            }
        });

        //筛选 -- 重置按钮的监听
        rightDrawer.setResetListener(new SelectTimeRangeNew.ResetClickListener() {
            @Override
            public void resetClick() {
                rightDrawer.setClickSelectDefaultData();
                resetAccountAndStatusSelected();
            }
        });

        //listView的item点击事件
        paymentRecordListView.setListener(new PinnedSectionListView.ClickListener() {
            @Override
            public void onItemClickListener(int position) {
                Bundle bundle = new Bundle();
                bundle.putParcelable(QRPAY_RECORD_INFO, mPaymentRecordViewModel.getList().get(position));
                bundle.putParcelable(QRPAY_RECORD_ACCOUNT, selectAccountBean);
                QRPayPaymentRecordInfoFragment recordInfoFragment = new QRPayPaymentRecordInfoFragment();
                recordInfoFragment.setArguments(bundle);
                start(recordInfoFragment);
            }
        });

        //上拉加载
        paymentRecordRefresh.setOnLoadListener(new PullToRefreshLayout.OnLoadListener() {
            @Override
            public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
                if (mPaymentRecordViewModel.getList() != null) {
                    if (mPaymentRecordViewModel.getList().size() < mPaymentRecordViewModel.getRecordNumber()) {
                        isPullToRefresh = true;
                        queryPaymentRecordList();
                    } else {
                        paymentRecordRefresh.loadmoreCompleted(PullToRefreshLayout.NO_MORE_DATA);
                    }
                } else {
                    listViewBeanList.clear();
                    pageCurrentIndex = 0;
                    _refresh = "true";
                    isPullToRefresh = true;
                    queryPaymentRecordList();
                }
            }
        });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    /**
     * 没有关联账号时页面显示规则
     *
     * @param isVisible
     */
    private void judgeNoAccountHeadVisible(boolean isVisible) {
        if (isVisible) {
            llPaymentRecordSelect.setVisibility(View.VISIBLE);
            selectAccountButton.setVisibility(View.VISIBLE);
            tvNoData.setVisibility(View.GONE);
        } else {
            llPaymentRecordSelect.setVisibility(View.GONE);
            selectAccountButton.setVisibility(View.GONE);
            paymentRecordRefresh.setMove(false);
            tvNoData.setVisibility(View.VISIBLE);
            tvNoData.setText(getResources().getString(R.string.boc_account_transdetail_no_account));
        }
    }


   /**
     * 根据账号获取accountBean
     *
     * @param accountNumber
     */
    private AccountBean getAccountBeanByNumber(String accountNumber) {
        if (null != accountBeanList) {
            for (AccountBean accountItem : accountBeanList) {
                if (accountNumber.equals(accountItem.getAccountNumber())) {
                    selectAccountBean = accountItem;
                    return accountItem;
                }
            }
        }
        return null;
    }


    /**
     * 判断 账户 的筛选
     *
     * @return
     */
    private boolean judgeSelectRecordAccount() {
        boolean haveSelected = false;
        for (Content item : selectAccountList) {
            if (item.getSelected()) {
                haveSelected = true;
                currentRecordAccount = item.getContentNameID();
                break;
            }
        }
        return haveSelected;
    }

    /**
     * 判断 交易状态 的筛选
     *
     * @return
     */
    private boolean judgeSelectRecordStatus() {
        boolean haveSelected = false;
        for (Content item : selectStatusList) {
            if (item.getSelected()) {
                haveSelected = true;
                currentRecordStatus = item.getContentNameID();
                break;
            }
        }
        return haveSelected;
    }


    /**
     * 重置为上次筛选的结果
     */
    private void resetAccountAndStatusSelected() {
        for (int i = 0; i < selectAccountList.size(); i++) {
            Content item = selectAccountList.get(i);
            if (item.getContentNameID().equals(lastRecordAccount)) {
                item.setSelected(true);
            } else {
                item.setSelected(false);
            }
        }
        selectRecordAccountView.getAdapter().notifyDataSetChanged();

        for (int i = 0; i < selectStatusList.size(); i++) {
            Content item = selectStatusList.get(i);
            if (item.getContentNameID().equals(lastRecordStatus)) {
                item.setSelected(true);
            } else {
                item.setSelected(false);
            }
        }
        selectRecordStatusView.getAdapter().notifyDataSetChanged();
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
    	tvPaymentRecordSelect.setTextColor(getResources().getColor(R.color.boc_text_color_red));
    	ivPaymentRecordSelect.setImageResource(R.drawable.boc_select_red);
    }

    /**
     * 调用接口，查询交易记录
     */
    private void queryPaymentRecordList() {
        if (pageCurrentIndex == 0) {
            //开启加载对话框
            showLoadingDialog();
        }
        mPaymentRecordPresenter.queryPaymentRecordList(buildWithdrawalQueryViewModel());
    }


    /**
     * 封装页面数据--查询交易记录
     *
     * @return
     */
    private QRPayPaymentRecordViewModel buildWithdrawalQueryViewModel() {
        mPaymentRecordViewModel.setType("00");
        mPaymentRecordViewModel.setActSeq(selectAccountBean.getAccountId());
        mPaymentRecordViewModel.setStartDate(startLocalDate.format(DateFormatters.dateFormatter1));//起始日期
        mPaymentRecordViewModel.setEndDate(endLocalDate.format(DateFormatters.dateFormatter1));//结束日期
        mPaymentRecordViewModel.set_refresh(_refresh);
        mPaymentRecordViewModel.setPageSize(pageSize);//页面大小
        mPaymentRecordViewModel.setCurrentIndex(pageCurrentIndex);//当前页索引
        mPaymentRecordViewModel.setTranStatus(currentRecordStatus);
        return mPaymentRecordViewModel;
    }


    /**
     * 将接口返回数据封装成ListView所需要的model
     *
     * @param paymentRecordViewModel
     */
    private void copyResult2TransBean(QRPayPaymentRecordViewModel paymentRecordViewModel) {
        listViewBeanList.clear();

        for (int i = 0; i < paymentRecordViewModel.getList().size(); i++) {
            LocalDate localDate = paymentRecordViewModel.getList().get(i).getTranTime().toLocalDate();
            String formatTime = "";//当前时间 MM月/yyyy
            String tempTime = "";//上一次时间
            if (localDate != null) {
                formatTime = localDate.format(DateFormatters.monthFormatter1);
            }
            if (i > 0) {
                tempTime = paymentRecordViewModel.getList().get(i - 1).getTranTime().toLocalDate().format(DateFormatters.monthFormatter1);
            }
            if (tempTime.equals(formatTime)) {
                ShowListBean item = new ShowListBean();
                item.type = ShowListBean.CHILD;

                item.setTitleID(ShowListConst.TITLE_QRPAY_TYPE);
                item.setTime(localDate);
                item.setChangeColor(true);
//                item.setContentLeftAbove(paymentRecordViewModel.getList().get(i).getMerchantName());
                item.setContentLeftBelow(paymentRecordViewModel.getList().get(i).getTranTime().format(DateTimeFormatter.ofPattern("HH:mm:ss")));
                item.setContentRightAbove(QrCodeStatusUtils.getResultStatusList(paymentRecordViewModel.getList().get(i).getTranStatus()));//状态
                String content = "";
                if ("01".equals(paymentRecordViewModel.getList().get(i).getType())) {//支付
                    item.setContentLeftAbove(paymentRecordViewModel.getList().get(i).getMerchantName());
                    if ("01".equals(paymentRecordViewModel.getList().get(i).getTransferType())) {//正扫
                        content = "-";
                    } else if ("02".equals(paymentRecordViewModel.getList().get(i).getTransferType())) {//反扫
                        if ("1".equals(paymentRecordViewModel.getList().get(i).getTranRemark()) || "2".equals(paymentRecordViewModel.getList().get(i).getTranRemark())) {
                            //若为已退货 已撤销 则金额为正
                            content = "";
                        } else {
                            content = "-";
                        }
                    }
                } else if ("02".equals(paymentRecordViewModel.getList().get(i).getType())) {//转账
                    if ("01".equals(paymentRecordViewModel.getList().get(i).getTransferType())) {//转入
                        item.setContentLeftAbove(paymentRecordViewModel.getList().get(i).getPayerName());//付款方姓名
                        content = "";
                    } else if ("02".equals(paymentRecordViewModel.getList().get(i).getTransferType())) {//转出
                        item.setContentLeftAbove(paymentRecordViewModel.getList().get(i).getPayeeName());//收款方姓名
                        content = "-";
                    }
                }
                item.setContentRightBelow(content + MoneyUtils.transMoneyFormat(paymentRecordViewModel.getList().get(i).getTranAmount(), ApplicationConst.CURRENCY_CNY));
                listViewBeanList.add(item);
            } else {
                for (int j = 0; j < 2; j++) {
                    ShowListBean itemFirst = new ShowListBean();
                    if (j == 0) {
                        itemFirst.type = ShowListBean.GROUP;
                        itemFirst.setGroupName(formatTime);
                        itemFirst.setTime(localDate);
                    } else {
                        itemFirst.type = ShowListBean.CHILD;

                        itemFirst.setTitleID(ShowListConst.TITLE_QRPAY_TYPE);
                        itemFirst.setTime(localDate);
                        itemFirst.setChangeColor(true);
//                        itemFirst.setContentLeftAbove(paymentRecordViewModel.getList().get(i).getMerchantName());
                        itemFirst.setContentLeftBelow(paymentRecordViewModel.getList().get(i).getTranTime().format(DateTimeFormatter.ofPattern("HH:mm:ss")));
                        itemFirst.setContentRightAbove(QrCodeStatusUtils.getResultStatusList(paymentRecordViewModel.getList().get(i).getTranStatus()));//状态
                        String content = "";
                        if ("01".equals(paymentRecordViewModel.getList().get(i).getType())) {//支付
                            itemFirst.setContentLeftAbove(paymentRecordViewModel.getList().get(i).getMerchantName());
                            if ("1".equals(paymentRecordViewModel.getList().get(i).getTranRemark()) || "2".equals(paymentRecordViewModel.getList().get(i).getTranRemark())) {
                                //若为已退货 已撤销 则金额为正
                                content = "";
                            } else {
                                content = "-";
                            }
                        } else if ("02".equals(paymentRecordViewModel.getList().get(i).getType())) {//转账
                            if ("01".equals(paymentRecordViewModel.getList().get(i).getTransferType())) {//转入
                                itemFirst.setContentLeftAbove(paymentRecordViewModel.getList().get(i).getPayerName());//付款方姓名
                                content = "";
                            } else if ("02".equals(paymentRecordViewModel.getList().get(i).getTransferType())) {//转出
                                itemFirst.setContentLeftAbove(paymentRecordViewModel.getList().get(i).getPayeeName());//收款方姓名
                                content = "-";
                            }
                        }
                        itemFirst.setContentRightBelow(content + MoneyUtils.transMoneyFormat(paymentRecordViewModel.getList().get(i).getTranAmount(), ApplicationConst.CURRENCY_CNY));
                    }
                    listViewBeanList.add(itemFirst);
                }
            }
        }
    }


    /**
     * 处理请求成功后的数据
     *
     * @param recordViewModel
     */
    private void handleSuccessData(QRPayPaymentRecordViewModel recordViewModel) {
        mPaymentRecordViewModel.setRecordNumber(recordViewModel.getRecordNumber());
        if (recordViewModel.getList().size() != 0) {
            List<QRPayPaymentRecordViewModel.ListBean> listBeen = new ArrayList<QRPayPaymentRecordViewModel.ListBean>();
            if (mPaymentRecordViewModel.getList() != null) {
                listBeen.addAll(mPaymentRecordViewModel.getList());
            }
            listBeen.addAll(recordViewModel.getList());

            mPaymentRecordViewModel.setList(sortRecordViewModelByDateTime(listBeen));
            copyResult2TransBean(mPaymentRecordViewModel);

//            mPaymentRecordViewModel.setList(listBeen);
//            copyResult2TransBean(recordViewModel);


            if (pageCurrentIndex == 0) {
                //关闭加载对话框
                closeProgressDialog();
            }
            if (isPullToRefresh) {
                paymentRecordRefresh.loadmoreCompleted(PullToRefreshLayout.SUCCEED);
            }
            PublicUtils.haveDataSelectText(true, tvNoData, isSelectData);

        } else {
            if (pageCurrentIndex == 0) {
                //关闭加载对话框
                closeProgressDialog();
                PublicUtils.haveDataSelectText(false, tvNoData, isSelectData);
            } else {
                PublicUtils.haveDataSelectText(true, tvNoData, isSelectData);
            }
            if (isPullToRefresh) {
                paymentRecordRefresh.loadmoreCompleted(PullToRefreshLayout.NO_MORE_DATA);
            }
        }
        //将所有数据处理完后为ListView设置数据
        mAdapter.setData(listViewBeanList);
        pageCurrentIndex += pageSize;//此处的索引是按记录的索引来走
        _refresh = "false";
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
            paymentRecordRefresh.loadmoreCompleted(PullToRefreshLayout.FAIL);
        }
        //将所有数据处理完后为ListView设置数据
        mAdapter.setData(listViewBeanList);
        showErrorDialog(biiResultErrorException.getErrorMessage());
    }


    /**
     * 交易记录查询成功
     *
     * @param paymentRecordViewModel
     */
    @Override
    public void queryPaymentRecordListSuccess(QRPayPaymentRecordViewModel paymentRecordViewModel) {
        handleSuccessData(paymentRecordViewModel);
    }

    /**
     * 交易记录查询失败
     *
     * @param biiResultErrorException
     */
    @Override
    public void queryPaymentRecordListFail(BiiResultErrorException biiResultErrorException) {
        handleFailData(biiResultErrorException);
    }


    /**
     * 对返回数据按时间进行排序
     * @param listBeen
     * @return
     */
    private List<QRPayPaymentRecordViewModel.ListBean> sortRecordViewModelByDateTime(List<QRPayPaymentRecordViewModel.ListBean> listBeen){
        int size = listBeen.size();
        for (int i = 0; i < size - 1; i++){
            for (int j = i + 1; j < size; j++){
                if(listBeen.get(i).getTranTime().isBefore(listBeen.get(j).getTranTime())){
                    QRPayPaymentRecordViewModel.ListBean listBeanTemp = listBeen.get(i);
                    QRPayPaymentRecordViewModel.ListBean listBeanTemp2 = listBeen.get(j);
                    listBeen.remove(i);
                    listBeen.add(i, listBeanTemp2);
                    listBeen.remove(j);
                    listBeen.add(j, listBeanTemp);
                }
            }
        }
        return listBeen;
    }


}
