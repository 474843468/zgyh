package com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.accountstatementquery.ui;

import android.os.Bundle;
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
import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;
import com.boc.bocsoft.mobile.bocmobile.base.utils.PublicCodeUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.SlipMenu.SlipDrawerLayout;
import com.boc.bocsoft.mobile.bocmobile.base.widget.refreshliseview.PullToRefreshLayout;
import com.boc.bocsoft.mobile.bocmobile.base.widget.selectview.selecttimerangenew.SelectTimeRangeNew;
import com.boc.bocsoft.mobile.bocmobile.base.widget.showlistview.PinnedSectionListView;
import com.boc.bocsoft.mobile.bocmobile.base.widget.showlistview.ShowListConst;
import com.boc.bocsoft.mobile.bocmobile.base.widget.showlistview.adapter.ShowListAdapter;
import com.boc.bocsoft.mobile.bocmobile.base.widget.showlistview.bean.ShowListBean;
import com.boc.bocsoft.mobile.bocmobile.base.widget.singlemoreselect.Content;
import com.boc.bocsoft.mobile.bocmobile.base.widget.singlemoreselect.SelectGridView;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.accountstatementquery.model.AccountStatementQueryModel;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.accountstatementquery.presenter.AccountStatementQueryPresenter;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.transactiondetails.TransQueryUtils;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.transactiondetails.model.XpadPsnVFGGetRegCurrencyModel;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.utils.PublicUtils;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.transinquire.model.SelectParams;
import com.boc.bocsoft.mobile.common.utils.StringUtils;
import com.boc.bocsoft.mobile.common.utils.date.DateFormatters;
import com.boc.bocsoft.mobile.framework.widget.TitleBarView;

import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalDateTime;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 *双向宝--对账单查询
 * Created by wjk7118 on 2016/11/29.
 */
public class AccountStatementQueryFragment extends MvpBussFragment<AccountStatementQueryContract.Presenter> implements AccountStatementQueryContract.View {

    private View rootView;
    private AccountStatementQueryContract.Presenter mAccountStatementQueryPresenter;
    private TitleBarView select_title_view;//标题

    //侧滑
    private SlipDrawerLayout mDrawerLayout;
    // 选择时间范围组件（rightDrawer）
    private TitleBarView title_view; // 标题
    private SelectTimeRangeNew selectTimeRangeNew;
    private TextView tv_select; // “筛选”按钮
    private static final int NOTICE_TYPE_CONDITION_SELECT = 1; // 1表示“筛选条件选择”通知


    //筛选相关
    // 最大查询范围为3个月
    private final static int MAX_QUERY_RANGE = 3;
    // 最大查询起始日期为一年
    private final static int MAX_QUERY_DATE = 12;
    private SelectGridView sgv_money_type;
    // 筛选处额外添加的layout
    private LinearLayout llAddToLayout;
    // 筛选处额外添加的layout
    private LinearLayout llAddToTopLayout;
    // 币种筛选的数据
    private List<Content> selectMoneyTypeList;
    // 判断是否是筛选
    private boolean isSelectData;
    // 筛选出的当前 币种 信息
    private int currentMoneyType;
    // 起始时间
    private LocalDate startLocalDate;
    // 结束时间
    private LocalDate endLocalDate;
    private LinearLayout ll_money_type;
    private SelectParams mSelectParams;//页面筛选条件
    private int dateSelectedPosition = 2; // 日期选择位置（“一周”、“一个月”、“三个月”）
    private boolean isSelectButtonRed = false; // 筛选按钮是否是红色


    //列表
    private int num=0;
    private PullToRefreshLayout pullToRefreshLayout = null;//上拉刷新组件
    private PinnedSectionListView pinnedSectionListView;//listview
    private ShowListAdapter mAdapter;//listview
    private List<ShowListBean> mShowListBean;//listview详情

    private AccountStatementQueryModel accountStatementQueryModel=null;
    private List<AccountStatementQueryModel.AccountStatementQueryBean> mAccountStatementQueryBeen=
            new ArrayList<AccountStatementQueryModel.AccountStatementQueryBean>();//查询列表

    private int pageCurrentIndex=0;//列表项
    private static final int PAGE_INDEX = 0; // 页面索引
//    private final static int pageSize = ApplicationConst.PAGE_SIZE;
    private final static int pageSize =5;
    private boolean isQueryByPullToRefresh = false; //是否通过上拉刷新进行的查询请求
//    private MyReceiver myReceiver;
    private boolean isFisrtTimeEnter = true; // 是否首次进入当前页面的标志
    private String settleCurrency;


    private LinearLayout ll_no_data_query;
    private TextView tv_no_data;//无数据显示


    //设置保证金结算币种
    String[] selectMoneyTypeData;
    String[] selectMoneyTypeIdData;

    /**
     * 初始化布局
     */
    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        rootView = mInflater.inflate(R.layout.boc_fragment_account_statement_query, null);
        return rootView;
    }

    @Override
    public void initView() {
        //页面布局初始化
        mDrawerLayout = (SlipDrawerLayout) rootView.findViewById(R.id.drawer_layout_st_query);
        select_title_view = (TitleBarView) rootView.findViewById(R.id.st_title_view);
        selectTimeRangeNew = (SelectTimeRangeNew) rootView.findViewById(R.id.st_right_drawer_transfer_query);//时间选择控件初始化
        tv_select = (TextView) rootView.findViewById(R.id.tv_st_select);//筛选按钮初始化
        initTitleView();//标题初始化

        //币种选择控件初始化
        View selectTransLayout = View.inflate(mContext, R.layout.boc_view_long_short_select, null);

        sgv_money_type = (SelectGridView) selectTransLayout.findViewById(R.id.sgv_money_type);
        ll_money_type = (LinearLayout) selectTransLayout.findViewById(R.id.ll_money_type);
        llAddToLayout = selectTimeRangeNew.getAddToLayout();
        llAddToTopLayout = selectTimeRangeNew.getAddToTopLayout();

        llAddToLayout.setVisibility(View.VISIBLE);

        llAddToTopLayout.setVisibility(View.GONE);
        llAddToLayout.addView(selectTransLayout);

        //listview初始化
        pullToRefreshLayout = (PullToRefreshLayout) rootView.findViewById(R.id.refresh_query);
        pinnedSectionListView=(PinnedSectionListView)rootView.findViewById(R.id.lv_history_query);
        pinnedSectionListView.setShadowVisible(false);
        ll_no_data_query = (LinearLayout) rootView.findViewById(R.id.no_data_query);
        tv_no_data = (TextView) rootView.findViewById(R.id.no_data);
    }

    @Override
    public void initData() {
        initSelectParams();
        mAccountStatementQueryPresenter=new AccountStatementQueryPresenter(this);
        accountStatementQueryModel=new AccountStatementQueryModel();
        initQueryDate();
        mShowListBean = new ArrayList<ShowListBean>();
        mAdapter = new ShowListAdapter(mContext, -1);
        pinnedSectionListView.saveAdapter(mAdapter);
        pinnedSectionListView.setAdapter(mAdapter);

        pageCurrentIndex=0;

        XpadPsnVFGGetRegCurrencyModel viewModel = new XpadPsnVFGGetRegCurrencyModel();
        mAccountStatementQueryPresenter.psnXpadGetRegCurrency(viewModel);
    }


    private void initTitleView() {
        select_title_view.setStyle(R.style.titlebar_common_white);
        select_title_view.setTitle("对账单");
        select_title_view.setRightImgBtnVisible(false);

        select_title_view.setLeftButtonOnClickLinster(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                titleLeftIconClick();
            }
        });
    }

    // 初始化 币种 筛选数据
    private void  initMoneyTypeSelectView() {

        selectMoneyTypeList = new ArrayList<Content>();

        for (int i = 0; i < selectMoneyTypeData.length; i++) {
            Content item = new Content();
            item.setName(selectMoneyTypeData[i]);
            item.setContentNameID(selectMoneyTypeIdData[i]);
            if (i == 0) {
                currentMoneyType = Integer.valueOf(selectMoneyTypeIdData[i]);
                item.setSelected(true);
            }
            selectMoneyTypeList.add(item);
        }
        sgv_money_type.setData(selectMoneyTypeList);
    }

    /**
     * 初始化选择的参数
     */
    private void initSelectParams() {
        //成交状况
        mSelectParams = new SelectParams();
        mSelectParams.setCurrency("0");
        mSelectParams.setStartDate(ApplicationContext.getInstance().getCurrentSystemDate().toLocalDate().minusMonths(3).plusDays(1).format(DateFormatters.dateFormatter1));
        mSelectParams.setEndDate(ApplicationContext.getInstance().getCurrentSystemDate().toLocalDate().format(DateFormatters.dateFormatter1));
    }
    /**
     * 初始化查询的时间
     */
    private void initQueryDate() {
        startLocalDate = ApplicationContext.getInstance().getCurrentSystemDate().toLocalDate().plusMonths(-MAX_QUERY_RANGE).plusDays(1);
        endLocalDate = ApplicationContext.getInstance().getCurrentSystemDate().toLocalDate();
        selectTimeRangeNew.setDefaultDate(startLocalDate.format(DateFormatters.dateFormatter1), endLocalDate.format(DateFormatters.dateFormatter1));
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
                if (PublicUtils.isCompareDateRange(choiceDate, ApplicationContext.getInstance().getCurrentSystemDate().toLocalDate(), MAX_QUERY_DATE)) {
                    showErrorDialog(getResources().getString(R.string.boc_account_transdetail_end_year_change, PublicUtils.changeNumberToUpper(MAX_QUERY_DATE / 12)));
                    return;
                }
                selectTimeRangeNew.setEndDate(strChoiceTime);
            }
        });
    }
    @Override
    protected AccountStatementQueryContract.Presenter initPresenter () {
        return new AccountStatementQueryPresenter(this);
    }

    @Override
    public void setListener () {
        //筛选按钮
        tv_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.toggle();
                resetPageSelectConditionShow();
            }
        });
        // 筛选重置按钮的监听
        selectTimeRangeNew.setResetListener(new SelectTimeRangeNew.ResetClickListener() {
            @Override
            public void resetClick() {
                resetMoneyTypeSelected();
            }
        });

        // 币种筛选
        sgv_money_type.setListener(new SelectGridView.ClickListener() {
            @Override
            public void setItemClick(AdapterView<?> parent, View view, int position, long id) {
                Content selectMoneyType = (Content) parent.getAdapter().getItem(position);
                currentMoneyType = Integer.valueOf(selectMoneyType.getContentNameID());
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

            @Override
            public void rightClick(boolean haveSelected, String content) {

                String startTime = selectTimeRangeNew.getStartDate();
                String endTime = selectTimeRangeNew.getEndDate();
                LocalDate start = LocalDate.parse(startTime, DateFormatters.dateFormatter1);
                LocalDate end = LocalDate.parse(endTime, DateFormatters.dateFormatter1);
                if (PublicUtils.judgeChoiceDateRange(start, end, MAX_QUERY_RANGE,AccountStatementQueryFragment.this)) {
                    mDrawerLayout.toggle();

                    isSelectData = true;
                    isQueryByPullToRefresh = false;

                    startLocalDate = start;
                    endLocalDate = end;

                    mSelectParams.setStartDate(startTime);
                    mSelectParams.setEndDate(endTime);
                    mSelectParams.setCurrency(String.valueOf(currentMoneyType));
                    // 保存日期的位置
                    dateSelectedPosition = selectTimeRangeNew.getCurPosition();
                    isSelectButtonRed = true;
                }

                // 此处填充页面查询操作的请求参数
                SelectParams selectParams = new SelectParams();
                selectParams.setStartDate(startTime);
                selectParams.setEndDate(endTime);
                settleCurrency = getCurrencyName(String.valueOf(selectMoneyTypeData[currentMoneyType]));
                pageCurrentIndex=0;
                querySuccessCondition();
                tv_select.setTextColor(getResources().getColor(R.color.boc_text_color_red));
                tv_select.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.boc_select_red), null);
            }
        });

        //点击跳转详情页面
        pinnedSectionListView.setListener(new PinnedSectionListView.ClickListener() {
            @Override
            public void onItemClickListener(int position) {
                    AccountStatementQueryModel.AccountStatementQueryBean accountStatementQueryBean =mAccountStatementQueryBeen.get(position);
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("accountStatementQueryBean",accountStatementQueryBean);
//                    bundle.putString("conversationId",conversationId);
                    bundle.putInt("selectPagePosition", PAGE_INDEX);
                    bundle.putString("settleCurrency",settleCurrency);
                    AccountStatementDetailsFragment accountStatementDetailsFragment = new AccountStatementDetailsFragment();
                    accountStatementDetailsFragment.setArguments(bundle);
                    start(accountStatementDetailsFragment);
            }
        });

        //加载更多
        pullToRefreshLayout.setOnLoadListener(new  PullToRefreshLayout.OnLoadListener() {
            @Override
            public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
                //当已经加载过数据，并且已加载数据数目大于等于总数时，不在请求数据
                boolean bCanLoadMore =accountStatementQueryModel.getList() != null
                        &&accountStatementQueryModel.getList().size()>=accountStatementQueryModel.getRecordNumber();
                System.out.println("accountStatementQueryMode   " + accountStatementQueryModel.getList().size());
                System.out.println("getRecordNumber()   " + accountStatementQueryModel.getRecordNumber());
                System.out.println("bCanLoadMore   " + bCanLoadMore);
                if(!bCanLoadMore){
                    System.out.println("进入if" );
                    isQueryByPullToRefresh = true;
                    querySuccessCondition();
                } else {
                        pullToRefreshLayout.loadmoreCompleted(PullToRefreshLayout.NO_MORE_DATA);
                    }

            }
        });
    }

    // 重置页面选择条件的显示
    private void resetPageSelectConditionShow() {
        llAddToTopLayout.setVisibility(View.VISIBLE);
        selectTimeRangeNew.getTimeLayout().setVisibility(View.VISIBLE);
        ll_money_type.setVisibility(View.VISIBLE);

        // 控制币种的显示
        resetPageSelectMoneyTypeSelected();

        // 控制时间一栏的显示
        selectTimeRangeNew.setDefaultDate(dateSelectedPosition, mSelectParams.getStartDate(), mSelectParams.getEndDate());

        // 控制筛选按钮是否红色
        setButtonRed(isSelectButtonRed);
    }

    /**
     *  控制筛选按钮是否红色
     *  @param isButtonRed
     */
    public void setButtonRed(boolean isButtonRed){
        if (isButtonRed) {
            tv_select.setTextColor(getResources().getColor(R.color.boc_text_color_red));
            tv_select.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.boc_select_red), null);
        } else {
            tv_select.setTextColor(getResources().getColor(R.color.boc_text_color_common_gray));
            tv_select.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.boc_select_gray), null);
        }
    }
    /**
     * 重置币种的选中
     */
    private void resetMoneyTypeSelected() {
        for (int i = 0; i < selectMoneyTypeList.size(); i++) {
            Content item = selectMoneyTypeList.get(i);
            if (item.getContentNameID().equals("0")) { // 重置币种为“全部”
                item.setSelected(true);
            } else {
                item.setSelected(false);
            }
        }
        sgv_money_type.getAdapter().notifyDataSetChanged();
        currentMoneyType = 0;
    }

    /**
     * （页面选中时）重置币种的选中
     */
    private void resetPageSelectMoneyTypeSelected() {
        SelectParams selectParams=null;
        selectParams = mSelectParams;

        for (int i = 0; i < selectMoneyTypeList.size(); i++) {
            Content item = selectMoneyTypeList.get(i);

            if (item.getContentNameID().equals(selectParams.getCurrency())) { // 重置当前页面的币种
                item.setSelected(true);
            } else {
                item.setSelected(false);
            }
        }
        sgv_money_type.getAdapter().notifyDataSetChanged();

        currentMoneyType = Integer.valueOf(selectParams.getCurrency());
    }




    // 根据typeName获取货币类型
    private String getCurrencyName(String typeName) {
        String currencyType = null;
        // 001-人民币元、 014-美元、 013：港币  029：澳元 038：欧元 027：日元）
        if ("人民币元".equals(typeName)) {
            currencyType = ApplicationConst.CURRENCY_CNY;
        } else if ("美元".equals(typeName)) {
            currencyType = ApplicationConst.CURRENCY_USD;
        } else if ("欧元".equals(typeName)) {
            currencyType =ApplicationConst.CURRENCY_EUR;
        } else if ("港币".equals(typeName)) {
            currencyType = ApplicationConst.CURRENCY_HKD;
        } else if ("日元".equals(typeName)) {
            currencyType = ApplicationConst.CURRENCY_JPY;
        } else if ("澳大利亚元".equals(typeName)) {
            currencyType = ApplicationConst.CURRENCY_AUD;
        }
        return currencyType;
    }

    public void querySuccessCondition(){

        if(!isQueryByPullToRefresh){
            showLoadingDialog();
        }

        AccountStatementQueryModel viewModel = new AccountStatementQueryModel();
        viewModel.setPageSize(String.valueOf(pageSize));
        viewModel.setCurrentIndex(String.valueOf(pageCurrentIndex*pageSize));
        viewModel.set_refresh("true");
        viewModel.setQueryType("6");
        viewModel.setCurrencyCode(settleCurrency);
        viewModel.setStartDate(mSelectParams.getStartDate());
        viewModel.setEndDate(mSelectParams.getEndDate());
        mAccountStatementQueryPresenter.queryAccountStatementList(viewModel);
    }

    @Override
    public void psnXpadGetRegCurrencySuccess(List<String> results) {
        settleCurrency = results.get(0);
        selectMoneyTypeData = new String[results.size()];
        selectMoneyTypeIdData = new String[results.size()];

        for (int i = 0;i<results.size();i++){
            selectMoneyTypeData [i] = PublicCodeUtils.getCurrency(mActivity,results.get(i));
            selectMoneyTypeIdData [i] = String.valueOf(i);
        }
        initMoneyTypeSelectView(); // 初始化币种选择
        querySuccessCondition();
    }

    @Override
    public void psnXpadGetRegCurrencyFail(BiiResultErrorException biiResultErrorException) {

    }
    //请求网络成功
    @Override
    public void queryAccountStatementListSuccess (AccountStatementQueryModel viewModel) {

        if(!isQueryByPullToRefresh){
            closeProgressDialog();
        }
        if (pageCurrentIndex < 0) {
            pageCurrentIndex = 0;
        }


                accountStatementQueryModel.setRecordNumber(viewModel.getRecordNumber());
                List<AccountStatementQueryModel.AccountStatementQueryBean> viewList = viewModel.getList();
                List<ShowListBean> transactionList = new ArrayList<ShowListBean>();
                if (!isQueryByPullToRefresh) {
                    mAccountStatementQueryBeen.clear();
                }
                isQueryByPullToRefresh=false;
                mAccountStatementQueryBeen.addAll(viewList);
                if(mAccountStatementQueryBeen!=null) {
                    pageCurrentIndex++;
                    if (isQueryByPullToRefresh) {
                        pullToRefreshLayout.loadmoreCompleted(PullToRefreshLayout.SUCCEED);
                    }
                    for (int i = 0; i < mAccountStatementQueryBeen.size(); i++) {
                        if(accountStatementQueryModel.getList() ==  null){
                            accountStatementQueryModel.setList(new LinkedList<AccountStatementQueryModel.AccountStatementQueryBean>());
                        }
                        accountStatementQueryModel.getList().add(mAccountStatementQueryBeen.get(i));
                        System.out.println("accountStatementQueryMode3   " + accountStatementQueryModel.getList().size());
                        System.out.println("accountStatementQueryMode2   " + mAccountStatementQueryBeen.size());
                        AccountStatementQueryModel.AccountStatementQueryBean viewBean = mAccountStatementQueryBeen.get(i);
                        ShowListBean transactionBean = new ShowListBean();
                        transactionBean.setChangeColor(true);

                        TransQueryUtils queryUtils = new TransQueryUtils();
                        LocalDateTime localDate = viewBean.getTransferDate();
                        LocalDate date = LocalDate.of(localDate.getYear(), localDate.getMonth(), localDate.getDayOfMonth());

                        String formatTime = "";// 当前时间 MM月/yyyy
                        String tempTime = "";// 上一次时间
                        if (localDate != null) {
                            formatTime = localDate.format(DateFormatters.monthFormatter1);
                        }
                        if (i > 0) {
                            tempTime = mAccountStatementQueryBeen.get(i - 1).getTransferDate().format(DateFormatters.monthFormatter1);
                        }

                        if (tempTime.equals(formatTime)) {// child
                            ShowListBean item = new ShowListBean();
                            item.type = ShowListBean.CHILD;

                            item.setTitleID(ShowListConst.TITLE_WEALTH);
                            item.setTime(date);

                            String fundTransferType = queryUtils.getFundTransferType(viewBean.getFundTransferType());
                            item.setContentLeftAbove(fundTransferType);
                            item.setContentLeftBelow(viewBean.getTransferDate().format(DateFormatters.timeFormatter));

                            String transferDir = queryUtils.gettransferDir(viewBean.getTransferDir());
                            String amount = MoneyUtils.transMoneyFormat(viewBean.getTransferAmount(), settleCurrency);
                            String currency = PublicCodeUtils.getCurrency(mActivity, settleCurrency);
                            item.setContentRightBelow(transferDir + " " + amount + currency);

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

                                    String fundTransferType = queryUtils.getFundTransferType(viewBean.getFundTransferType());
                                    itemFirst.setContentLeftAbove(fundTransferType);
                                    itemFirst.setContentLeftBelow(viewBean.getTransferDate().format(DateFormatters.timeFormatter));

                                    String transferDir = queryUtils.gettransferDir(viewBean.getTransferDir());
                                    String amount = MoneyUtils.transMoneyFormat(viewBean.getTransferAmount(), settleCurrency);
                                    String currency = PublicCodeUtils.getCurrency(mActivity, settleCurrency);
                                    itemFirst.setContentRightBelow(transferDir + " " + amount + currency);
                                }
                                transactionList.add(itemFirst);
                            }
                        }
                    }
                }
                mShowListBean.clear();
                mShowListBean.addAll(transactionList);
                mAdapter.setData(mShowListBean);
                showNoData();
        }
    //请求网络失败
    @Override
    public void queryAccountStatementListFail(BiiResultErrorException biiResultErrorException) {
        closeProgressDialog();

        if(biiResultErrorException == null || StringUtils.isEmptyOrNull(biiResultErrorException.getErrorCode())){
            showNoData();
            return;
        }
        //已经返回过数据，本次是通过上拉刷新方式加载的数据
        if (biiResultErrorException.getErrorCode().equals("validation.no.relating.acc")) {
            if(isQueryByPullToRefresh){
                pullToRefreshLayout.loadmoreCompleted(PullToRefreshLayout.NO_MORE_DATA);
            }
            else{
                mShowListBean.clear();
                mAdapter.setData(mShowListBean);
                showNoData();
            }
        } else {
            showErrorDialog(biiResultErrorException.getMessage());
        }
    }

    @Override
    public void setPresenter(AccountStatementQueryContract.Presenter presenter) {
    }


    // 处理无数据时的情况
    private void showNoData(){
        if(mShowListBean.size() > 0) {
            ll_no_data_query.setVisibility(View.GONE);
        } else {
            ll_no_data_query.setVisibility(View.VISIBLE);
            tv_no_data.setText(isFisrtTimeEnter ? "查询结果为空，请点击“筛选”变更查询条件" : "筛选后结果为空，请重新筛选");
        }
        isFisrtTimeEnter = false;
    }


    @Override
    protected String getTitleValue () {
        return "";
    }

    @Override
    protected boolean getTitleBarRed () {
        return false;
    }

    @Override
    protected boolean isDisplayRightIcon () {
        return false;
    }
    @Override
    protected boolean isHaveTitleBarView() {
        return false;
    }
//    @Override
//    public void reInit() {
//       initData();
//    }
    @Override
    public void onDestroy() {
        mAccountStatementQueryPresenter.unsubscribe();
        super.onDestroy();
    }
}