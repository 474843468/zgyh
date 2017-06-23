package com.boc.bocsoft.mobile.bocmobile.buss.transfer.atmwithdraw.ui;

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
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.atmwithdraw.model.ATMWithDrawDetailsViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.atmwithdraw.model.ATMWithDrawQueryViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.atmwithdraw.presenter.ATMWithDrawPresenter;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.utils.PublicUtils;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.utils.ResultStatusUtils;
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
 * ATM无卡取款查询/撤销
 * Created by liuweidong on 2016/6/22.
 */
public class ATMWithDrawQueryFragment extends MvpBussFragment<ATMWithDrawContract.Presenter> implements View.OnClickListener, ATMWithDrawContract.View {
    private View rootView;
    private SlipDrawerLayout slipDrawerLayout;// 侧滑
    private PullToRefreshLayout refreshLayout;// 上拉刷新
    private PinnedSectionListView transactionView;// 查询列表组件
    private ShowListAdapter mAdapter;
    private SelectTimeRangeNew selectTimeRangeNew;// 选择时间范围组件
    private TextView txtNoData;
    private View headView;// listView的Head
    private TitleBarView queryTitleView;
    private LinearLayout selectParentLayout;// 筛选父布局
    private TextView selectTxt;// 筛选文本
    private ImageView selectImg;// 筛选图片
    private TextView tvTransferRecordRange;// 筛选处的范围
    private SelectAccountButton selectAccountButton;// 账户选择
    private RelativeLayout rlHeadSelectLayout;// 头部的筛选Layout

    private ATMWithDrawQueryViewModel mViewModel;
    private LocalDate startLocalDate;// 起始时间
    private LocalDate endLocalDate;// 结束时间
    private int currentIndex = 0;// 当前页起始索引
    private List<ShowListBean> mList;// 查询列表组件的数据集合
    private ArrayList<String> accountTypeList;// 过滤的账户类型
    private AccountBean curAccountBean;// 当前选中的账户数据
    private boolean isSelectData;// 判断是否是筛选
    private boolean isStartFragment;// 从取款页跳转过来
    private boolean isPullToRefresh;// 是否上拉加载

    //交易类型 O:汇款 I：收款
    private static final String TRANSACTION_TYPE = "O";
    //查询详情交易类型
    private static final String TRANSACTION_TYPE_DETAILS = "QU";

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        rootView = View.inflate(mContext, R.layout.boc_fragment_transfer_query, null);
        return rootView;
    }

    @Override
    public void initView() {
        slipDrawerLayout = (SlipDrawerLayout) rootView.findViewById(R.id.drawer_layout_transfer_query);
        refreshLayout = (PullToRefreshLayout) rootView.findViewById(R.id.refresh_transfer_query);
        transactionView = (PinnedSectionListView) rootView.findViewById(R.id.lv_transfer_query);
        selectTimeRangeNew = (SelectTimeRangeNew) rootView.findViewById(R.id.right_drawer_transfer_query);
        txtNoData = (TextView) rootView.findViewById(R.id.no_data_transfer_query);

        headView = View.inflate(mContext, R.layout.boc_head_account_transdetail, null);
        selectParentLayout = (LinearLayout) headView.findViewById(R.id.ll_transdetail_select);
        selectTxt = (TextView) headView.findViewById(R.id.tv_transdetail_select);
        selectImg = (ImageView) headView.findViewById(R.id.iv_transdetail_select);
        tvTransferRecordRange = (TextView) headView.findViewById(R.id.tv_transdetail_range);
        selectAccountButton = (SelectAccountButton) headView.findViewById(R.id.select_account_button);
        rlHeadSelectLayout = (RelativeLayout) headView.findViewById(R.id.select_head_select_rl);
        transactionView.addHeaderView(headView, null, false);
        mAdapter = new ShowListAdapter(mContext, -1);
        transactionView.saveAdapter(mAdapter);
        transactionView.setShadowVisible(false);
        transactionView.setAdapter(mAdapter);
        initQueryTitleView();
    }

    @Override
    public void initData() {
        mList = new ArrayList<ShowListBean>();
        mViewModel = new ATMWithDrawQueryViewModel();
        curAccountBean = new AccountBean();
        tvTransferRecordRange.setText(getResources().getString(R.string.boc_common_query_month_3));
        filterAccountType();
        List<AccountBean> accountList = ApplicationContext.getInstance().getChinaBankAccountList(accountTypeList);
        if (accountList.size() > 0) {// 设置默认账户
            if (isStartFragment) {
                AccountBean accountBean = getArguments().getParcelable("AccountBean");
                selectAccountButton.setData(accountBean);
                saveCurAccountInfo(accountBean);
            } else {
                String accountId = BocCloudCenter.getInstance().getAccountId(AccountType.ACC_TYPE_NOCARD_DRAW);
                if (!StringUtils.isEmptyOrNull(accountId)) {
                    boolean haveAccount = false;
                    for (int i = 0; i < accountList.size(); i++) {
                        String sha256String = BocCloudCenter.getSha256String(accountList.get(i).getAccountId());
                        if (accountId.equals(sha256String)) {
                            haveAccount = true;
                            selectAccountButton.setData(accountList.get(i));
                            saveCurAccountInfo(accountList.get(i));
                            break;
                        }
                    }
                    if (!haveAccount) {
                        saveCurAccountInfo(accountList.get(0));
                    }
                } else {
                    selectAccountButton.setData(accountList.get(0));
                    saveCurAccountInfo(accountList.get(0));
                }
            }
            /*设置默认日期*/
            startLocalDate = ApplicationContext.getInstance().getCurrentSystemDate().toLocalDate().plusMonths(-3).plusDays(1);
            endLocalDate = ApplicationContext.getInstance().getCurrentSystemDate().toLocalDate();
            selectTimeRangeNew.setDefaultDate(startLocalDate.format(DateFormatters.dateFormatter1),
                    endLocalDate.format(DateFormatters.dateFormatter1));
            showLoadingDialog();
            queryATMWithDraw();
        } else {// 没有关联账号
            rlHeadSelectLayout.setVisibility(View.GONE);
            selectAccountButton.setVisibility(View.GONE);
            refreshLayout.setMove(false);
            txtNoData.setVisibility(View.VISIBLE);
            txtNoData.setText(getResources().getString(R.string.boc_account_transdetail_no_account));
        }
    }

    @Override
    public void setListener() {
        selectParentLayout.setOnClickListener(this);// 筛选
        selectAccountButton.setOnClickListener(this);// 账户列表
        selectTimeRangeNew.setListener(new SelectTimeRangeNew.ClickListener() {// 日期选择
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
                slipDrawerLayout.toggle();
            }

            @Override
            public void rightClick(boolean haveSelected, String content) {
                if (haveSelected) {
                    if (content.contains("月")) {
                        tvTransferRecordRange.setText(content.replace("月", "个月") + "查询结果");
                    } else {
                        tvTransferRecordRange.setText(content + "查询结果");
                    }
                } else {
                    tvTransferRecordRange.setText("");
                }
                startLocalDate = LocalDate.parse(selectTimeRangeNew.getStartDate(), DateFormatters.dateFormatter1);
                endLocalDate = LocalDate.parse(selectTimeRangeNew.getEndDate(), DateFormatters.dateFormatter1);
                if (PublicUtils.judgeChoiceDateRange(startLocalDate, endLocalDate, 3, ATMWithDrawQueryFragment.this)) { // 判断日期是否合法
                    mList.clear();
                    currentIndex = 0;
                    isSelectData = true;
                    mViewModel = new ATMWithDrawQueryViewModel();
                    slipDrawerLayout.toggle();
                    showLoadingDialog();
                    queryATMWithDraw();
                }
            }
        });

        transactionView.setListener(new PinnedSectionListView.ClickListener() {
            @Override
            public void onItemClickListener(int position) {
                showLoadingDialog();
                getPresenter().queryATMWithDrawDetails(buildATMWithDrawDetailsViewModel(position));
            }
        });

        // 上拉加载
        refreshLayout.setOnLoadListener(new PullToRefreshLayout.OnLoadListener() {
            @Override
            public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
                startPresenter();
                if (mViewModel.getList() != null) {
                    if (mViewModel.getList().size() < mViewModel.getRecordNumber()) {
                        isPullToRefresh = true;
                        queryATMWithDraw();
                    } else {
                        refreshLayout.loadmoreCompleted(PullToRefreshLayout.NO_MORE_DATA);
                    }
                } else {
                    isPullToRefresh = true;
                    mList.clear();
                    currentIndex = 0;
                    queryATMWithDraw();
                }
            }
        });

    }

    @Override
    protected boolean isHaveTitleBarView() {
        return false;
    }

    @Override
    protected ATMWithDrawContract.Presenter initPresenter() {
        return new ATMWithDrawPresenter(this);
    }

    /**
     * 过滤账户类型
     */
    private void filterAccountType() {
        accountTypeList = new ArrayList<String>();
        // 借记卡 119
        accountTypeList.add(ApplicationConst.ACC_TYPE_BRO);
        // 普通活期 101
        accountTypeList.add(ApplicationConst.ACC_TYPE_ORD);
        // 活期一本通 188
        accountTypeList.add(ApplicationConst.ACC_TYPE_RAN);
    }

    /**
     * 初始化Title
     */
    private void initQueryTitleView() {
        queryTitleView = (TitleBarView) headView.findViewById(R.id.select_title_view);
        queryTitleView.setStyle(R.style.titlebar_common_white);
        queryTitleView.setTitle(R.string.boc_transfer_atm_draw_query_title);
        queryTitleView.setRightImgBtnVisible(false);

        queryTitleView.setLeftButtonOnClickLinster(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                titleLeftIconClick();
            }
        });
    }

    /**
     * 保存当前账户信息
     */
    private void saveCurAccountInfo(AccountBean accountBean) {
        curAccountBean.setAccountId(accountBean.getAccountId());
        curAccountBean.setAccountNumber(accountBean.getAccountNumber());
    }

    /**
     * 查询ATM无卡取款记录
     */
    private void queryATMWithDraw() {
        getPresenter().queryATMWithDraw(buildATMWithDrawQueryViewModel());
    }

    /**
     * 封装页面数据
     *
     * @return
     */
    private ATMWithDrawQueryViewModel buildATMWithDrawQueryViewModel() {
        mViewModel.setAccountId(curAccountBean.getAccountId());// 账户ID
        mViewModel.setFreeRemitTrsType(TRANSACTION_TYPE);// 交易类型
        mViewModel.setStartDate(startLocalDate.format(DateFormatters.dateFormatter1));// 开始日期
        mViewModel.setEndDate(endLocalDate.format(DateFormatters.dateFormatter1));// 结束日期
        mViewModel.setCurrentIndex(currentIndex);// 当前页起始索引
        mViewModel.setPageSize(ApplicationConst.PAGE_SIZE);// 页面显示记录条数
        String refresh = "true";
        if (currentIndex == 0) {
            refresh = "true";
        } else {
            refresh = "false";
        }
        mViewModel.set_refresh(refresh);
        return mViewModel;
    }

    /**
     * 封装请求详情的数据
     *
     * @param position
     * @return
     */
    private ATMWithDrawDetailsViewModel buildATMWithDrawDetailsViewModel(int position) {
        ATMWithDrawDetailsViewModel viewModel = new ATMWithDrawDetailsViewModel();
        ATMWithDrawQueryViewModel.ListBean item = mViewModel.getList().get(position);
        viewModel.setRemitNo(item.getRemitNumber());
        viewModel.setPayerName(item.getFromName());
        viewModel.setPayeeName(item.getToName());
        viewModel.setFreeRemitTrsType(TRANSACTION_TYPE_DETAILS);
        return viewModel;
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.leftIconIv) {// 返回
//            titleLeftIconClick();
        } else if (i == R.id.ll_transdetail_select) {// 筛选按钮
            selectTimeRangeNew.setClickSelectDefaultData();
            slipDrawerLayout.toggle();
            selectStatustChange();
        } else if (i == R.id.select_account_button) {// 账户列表
            startForResult(SelectAccoutFragment.newInstance(accountTypeList), SelectAccoutFragment.REQUEST_CODE_SELECT_ACCOUNT);
        }
    }

    @Override
    protected void onFragmentResult(int requestCode, int resultCode, Bundle data) {
        if (resultCode == SelectAccoutFragment.RESULT_CODE_SELECT_ACCOUNT) {
            if (requestCode == SelectAccoutFragment.REQUEST_CODE_SELECT_ACCOUNT) {
                AccountBean item = data.getParcelable(SelectAccoutFragment.ACCOUNT_SELECT);
                selectAccountButton.setData(item);
                curAccountBean.setAccountId(item.getAccountId());
                curAccountBean.setAccountNumber(item.getAccountNumber());
            }
            mList.clear();
            currentIndex = 0;
            isSelectData = false;
            mViewModel = new ATMWithDrawQueryViewModel();
            showLoadingDialog();
            queryATMWithDraw();
        }
    }

    @Override
    protected void titleLeftIconClick() {
        if (isStartFragment) {
            startAndPopOther(ATMWithDrawFragment.class);
        } else {
            super.titleLeftIconClick();
        }
    }

    @Override
    public boolean onBack() {
        if (isStartFragment) {
            startAndPopOther(ATMWithDrawFragment.class);
        }
        return true;
    }

    private void startAndPopOther(Class<? extends ATMWithDrawFragment> clazz) {
        popTo(clazz, false);
        findFragment(clazz).initData();
    }

    /**
     * 更新UI数据
     */
    private void updateData(ATMWithDrawQueryViewModel viewModel) {
        /*记录数为空*/
        if (viewModel.getRecordNumber() == 0) {
            PublicUtils.haveDataSelectText(false, txtNoData, isSelectData);
            return;
        }
        PublicUtils.haveDataSelectText(true, txtNoData, isSelectData);
        /*复制响应数据*/
        mViewModel.setRecordNumber(viewModel.getRecordNumber());
        List<ATMWithDrawQueryViewModel.ListBean> newList = viewModel.getList();// 新加载的数据
        List<ATMWithDrawQueryViewModel.ListBean> oldList = mViewModel.getList();
        if (oldList == null) {
            oldList = new ArrayList<ATMWithDrawQueryViewModel.ListBean>();
        }
        oldList.addAll(newList);
        mViewModel.setList(oldList);
        mList.clear();

        for (int i = 0; i < mViewModel.getList().size(); i++) {
            LocalDate localDate = mViewModel.getList().get(i).getPaymentDate();
            String formatTime = "";// 当前时间 MM月/yyyy
            String tempTime = "";// 上一次时间
            if (localDate != null) {
                formatTime = localDate.format(DateFormatters.monthFormatter1);
            }
            if (i > 0) {
                tempTime = mViewModel.getList().get(i - 1).getPaymentDate().format(DateFormatters.monthFormatter1);
            }
            if (tempTime.equals(formatTime)) {// child
                ShowListBean item = new ShowListBean();
                item.type = ShowListBean.CHILD;
                item.setTitleID(ShowListConst.TITLE_LEFT_2_RIGHT_1);
                item.setTime(localDate);
                item.setContentLeftAbove(NumberUtils.formatCardNumber(curAccountBean.getAccountNumber()));
                item.setContentLeftBelow(ResultStatusUtils.setResultStatus(mContext, mViewModel.getList().get(i).getStatus()));
                item.setContentRightAbove(MoneyUtils.transMoneyFormat(mViewModel.getList().get(i).
                        getPaymentAmount(), mViewModel.getList().get(i).getPaymentCode()));
                mList.add(item);
            } else {// group
                for (int j = 0; j < 2; j++) {
                    ShowListBean itemFirst = new ShowListBean();
                    if (j == 0) {
                        itemFirst.type = ShowListBean.GROUP;
                        itemFirst.setGroupName(formatTime);
                        itemFirst.setTime(localDate);
                    } else {
                        itemFirst.type = ShowListBean.CHILD;
                        itemFirst.setTitleID(ShowListConst.TITLE_LEFT_2_RIGHT_1);
                        itemFirst.setTime(localDate);
                        itemFirst.setContentLeftAbove(NumberUtils.formatCardNumber(curAccountBean.getAccountNumber()));
                        itemFirst.setContentLeftBelow(ResultStatusUtils.setResultStatus(mContext, mViewModel.getList().get(i).getStatus()));
                        itemFirst.setContentRightAbove(MoneyUtils.transMoneyFormat(mViewModel.getList().get(i).
                                getPaymentAmount(), mViewModel.getList().get(i).getPaymentCode()));
                    }
                    mList.add(itemFirst);
                }
            }
        }
        mAdapter.setData(mList);
    }

    public void setStartFragment(boolean startFragment) {
        isStartFragment = startFragment;
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
                if (PublicUtils.isCompareDateRange(choiceDate, ApplicationContext.getInstance().getCurrentSystemDate().toLocalDate(), 12)) {
                    showErrorDialog(getResources().getString(R.string.boc_account_transdetail_start_year_change, "一"));
                    return;
                }
                selectTimeRangeNew.setStartDate(strChoiceTime);
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
                if (PublicUtils.isCompareDateRange(choiceDate, ApplicationContext.getInstance().getCurrentSystemDate().toLocalDate(), 12)) {
                    showErrorDialog(getResources().getString(R.string.boc_account_transdetail_end_year_change, "一"));
                    return;
                }
                selectTimeRangeNew.setEndDate(strChoiceTime);
            }
        });
    }

    /**
     * 查询ATM无卡取款成功
     *
     * @param atmWithDrawQueryViewModel
     */
    @Override
    public void queryATMWithDrawSuccess(ATMWithDrawQueryViewModel atmWithDrawQueryViewModel) {
        closeProgressDialog();
        currentIndex += ApplicationConst.PAGE_SIZE;
        updateData(atmWithDrawQueryViewModel);
        if (isPullToRefresh) {
            refreshLayout.loadmoreCompleted(PullToRefreshLayout.SUCCEED);
            isPullToRefresh = false;
        }
    }

    /**
     * 查询ATM无卡取款失败
     *
     * @param biiResultErrorException
     */
    @Override
    public void queryATMWithDrawFail(BiiResultErrorException biiResultErrorException) {
        closeProgressDialog();
        isPullToRefresh = false;
        refreshLayout.loadmoreCompleted(PullToRefreshLayout.FAIL);
    }

    @Override
    public void queryATMWithDrawDetailsSuccess(ATMWithDrawDetailsViewModel atmWithDrawDetailsViewModel) {
        closeProgressDialog();
        // 跳转到详情页
        Bundle bundle = new Bundle();
        bundle.putParcelable("DetailsInfo", atmWithDrawDetailsViewModel);
        bundle.putString("AccountNum", curAccountBean.getAccountNumber());
        ATMWithDrawQueryDetailsFragment atmWithDrawQueryDetailsFragment = new ATMWithDrawQueryDetailsFragment();
        atmWithDrawQueryDetailsFragment.setArguments(bundle);
        startForResult(atmWithDrawQueryDetailsFragment, 2);
    }

    @Override
    public void queryATMWithDrawDetailsFail(BiiResultErrorException biiResultErrorException) {
        closeProgressDialog();
        showErrorDialog(biiResultErrorException.getErrorMessage());
    }

    /**
     * 筛选按钮的颜色改变
     */
    private void selectStatustChange() {
        selectTxt.setTextColor(getResources().getColor(R.color.boc_text_color_red));
        selectImg.setImageResource(R.drawable.boc_select_red);
    }
}
