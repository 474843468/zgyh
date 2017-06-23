package com.boc.bocsoft.mobile.bocmobile.buss.transfer.payment.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.ApplicationContext;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.MvpBussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.utils.DateTimePicker;
import com.boc.bocsoft.mobile.bocmobile.base.widget.SlipMenu.SlipDrawerLayout;
import com.boc.bocsoft.mobile.bocmobile.base.widget.refreshliseview.PullToRefreshLayout;
import com.boc.bocsoft.mobile.bocmobile.base.widget.selectview.selecttimerangenew.SelectTimeRangeNew;
import com.boc.bocsoft.mobile.bocmobile.base.widget.showlistview.PinnedSectionListView;
import com.boc.bocsoft.mobile.bocmobile.base.widget.showlistview.adapter.ShowListAdapter;
import com.boc.bocsoft.mobile.bocmobile.base.widget.showlistview.bean.ShowListBean;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.makecollection.ui.MeMakeCollectionFragment1;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.payment.model.OrderListModel;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.payment.presenter.OrderListPresenter;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.utils.PublicUtils;
import com.boc.bocsoft.mobile.common.utils.date.DateFormatters;

import org.threeten.bp.LocalDate;

import java.util.ArrayList;

/**
 * Created by wangtong on 2016/6/28.
 * 收付款指令管理主界面
 */
public class OrderListFragment extends MvpBussFragment<OrderListPresenter> implements OrderListContact.View {

    public static final String ORDER_LIST_FRAGMENT_REFRESH_ACTION = "order_list_fragment_refresh_action";
    public static final String VALUE_STATUS = "status";
    //数据列表
    protected PinnedSectionListView contentList;
    private ShowListAdapter adapter;
    //选择标签
    protected RadioGroup radioGroup;
    protected RadioButton btn_make;
    //刷新布局
    protected PullToRefreshLayout refreshLayout;
    //筛选按钮
    protected LinearLayout selectDate;
    //日期标签
    protected TextView dateLabel;
    protected ImageView btnBack;
    protected ImageView pullupIcon;
    protected ImageView loadingIcon;
    protected TextView loadstateTv;
    protected ImageView resultIcon;
    protected RelativeLayout loadmoreView;
    protected ImageView searchIcon;
    protected TextView emptyTip;
    protected TextView searchText;

    private View rootView;
    //数据模型
    private OrderListModel model;

    private SlipDrawerLayout drawerLayout;

    private SelectTimeRangeNew rightDrawer;
    //最大查询范围（月）
    private static int MAX_QUERY_RANGE = 3;
    //最长查询时间（月）
    private static int MAX_QUERY_DATE = 12;
    private String paySelectDate;
    private String reminderSelectDate;
    private ShowListBean selectedItem;
    private boolean paySearched = false;//付款是否筛选过
    private boolean remindSearched = false;//收款是否筛选过
    private String selectPayStartDate;// 付款筛选的起始时间
    private String selectPayEndDate;//付款筛选的结束时间
    private String selectRemindStartDate;// 收款筛选的起始时间
    private String selectRemindEndDate;//收款筛选的结束时间
    private int selectPayDatePos = 2;//付款日期选中位置
    private int selectRemindDatePos = 2;//收款日期选中位置
    public final String RERUAN_CODE = "returnCode";//返回码
    public final String IS_REINIT = "returnClassName";//要返回到的类名字符串
    private boolean isReInit = false;//返回的类是否要reinit
    private int returnCode;//返回码

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Bundle arguments = getArguments();
        returnCode = arguments.getInt(RERUAN_CODE);
        isReInit = arguments.getBoolean(IS_REINIT, false);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        rootView = mInflater.inflate(R.layout.boc_fragment_payment, null);
        return rootView;
    }

    @Override
    public void initView() {
        contentList = (PinnedSectionListView) rootView.findViewById(R.id.content_list);
        radioGroup = (RadioGroup) rootView.findViewById(R.id.radio_group);
        btn_make = (RadioButton) rootView.findViewById(R.id.btn_make);
        refreshLayout = (PullToRefreshLayout) rootView.findViewById(R.id.refresh_layout);
        selectDate = (LinearLayout) rootView.findViewById(R.id.btn_search);
        drawerLayout = (SlipDrawerLayout) rootView.findViewById(R.id.drawer_layout_transfer_query);
        rightDrawer = (SelectTimeRangeNew) rootView.findViewById(R.id.right_drawer_transfer_query);
        dateLabel = (TextView) rootView.findViewById(R.id.date_label);
        btnBack = (ImageView) rootView.findViewById(R.id.btn_back);
        pullupIcon = (ImageView) rootView.findViewById(R.id.pullup_icon);
        loadingIcon = (ImageView) rootView.findViewById(R.id.loading_icon);
        loadstateTv = (TextView) rootView.findViewById(R.id.loadstate_tv);
        resultIcon = (ImageView) rootView.findViewById(R.id.result_icon);
        loadmoreView = (RelativeLayout) rootView.findViewById(R.id.loadmore_view);
        searchIcon = (ImageView) rootView.findViewById(R.id.search_icon);
        emptyTip = (TextView) rootView.findViewById(R.id.empty_tip);
        searchText = (TextView) rootView.findViewById(R.id.search_text);

        refreshLayout.setVisibility(View.GONE);
    }

    @Override
    public void reInit() {
        initView();
        initData();
        setListener();
    }

    @Override
    public void initData() {
        Bundle bundle = getArguments();
        model = bundle.getParcelable("model");
        if (model == null) {
            model = new OrderListModel();
        }
        LocalDate current = ApplicationContext.getInstance().getCurrentSystemDate().toLocalDate();
        String currentString = current.format(DateFormatters.dateFormatter1);
        LocalDate threeMonthAgo = current.plusMonths(-3).plusDays(1);
        String threeMonthAgoString = threeMonthAgo.format(DateFormatters.dateFormatter1);
        paySelectDate = "近3个月";
        reminderSelectDate = "近3个月";
        model.setPayDateRange(threeMonthAgoString, currentString);
        model.setReminderDateRange(threeMonthAgoString, currentString);
        rightDrawer.setStartDate(threeMonthAgoString);
        rightDrawer.setEndDate(currentString);
        rightDrawer.setDefaultDate(threeMonthAgoString, currentString);
        selectPayStartDate = threeMonthAgoString;// 付款筛选的起始时间
        selectPayEndDate = currentString;//付款筛选的结束时间
        selectRemindStartDate = threeMonthAgoString;// 收款筛选的起始时间
        selectRemindEndDate = currentString;
        adapter = new ShowListAdapter(mContext, -1);
        adapter.setPositionType(true);
        contentList.saveAdapter(adapter);
        contentList.setShadowVisible(false);
        contentList.setAdapter(adapter);

        // 防止转账成功重新进入这个类model被重新new，getPresenter()不为空，presenter存放的是第一次initPresenter中的model
        getPresenter().setUiModel(model);

        getPresenter().psnTransActQueryPaymentOrderList();
    }

    @Override
    public void setListener() {
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.btn_pay) {
                    getPayData();
                    setSearchTextColor(paySearched);
                } else {
                    getReminderData();
                    setSearchTextColor(remindSearched);
                }
            }


        });

        contentList.setListener(new PinnedSectionListView.ClickListener() {
            @Override
            public void onItemClickListener(int position) {
                // 付款
                if (radioGroup.getCheckedRadioButtonId() == R.id.btn_pay) {
                    selectedItem = model.getPayList().get(position);
                    PaymentDetailFragment fragment = new PaymentDetailFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("notifyId", selectedItem.getContentLeftBelowAgain());
                    fragment.setArguments(bundle);
                    start(fragment);
                } else {
                    //收款
                    selectedItem = model.getReminderList().get(position);
                    ReminderDetailFragment fragment = new ReminderDetailFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("notifyId", selectedItem.getContentLeftBelowAgain());
                    fragment.setArguments(bundle);
                    start(fragment);
                }
            }
        });

        refreshLayout.setOnLoadListener(new PullToRefreshLayout.OnLoadListener() {
            @Override
            public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
                if (radioGroup.getCheckedRadioButtonId() == R.id.btn_pay) {
                    if (model.getPayListRecords() <= model.getCurrentPayIndex()) {
                        refreshLayout.loadmoreCompleted(PullToRefreshLayout.NO_MORE_DATA);
                    } else {
                        getPresenter().psnTransActQueryPaymentOrderList();
                    }

                } else if (radioGroup.getCheckedRadioButtonId() == R.id.btn_make) {
                    if (model.getReminderListRecords() <= model.getCurrentReminderIndex()) {
                        refreshLayout.loadmoreCompleted(PullToRefreshLayout.NO_MORE_DATA);
                    } else {
                        getPresenter().psnTransActQueryReminderOrderList();
                    }
                }
            }
        });

        selectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (radioGroup.getCheckedRadioButtonId() == R.id.btn_pay) {
                    rightDrawer.setDefaultDate(selectPayDatePos, selectPayStartDate, selectPayEndDate);
                } else {
                    rightDrawer.setDefaultDate(selectRemindDatePos, selectRemindStartDate, selectRemindEndDate);
                }
//                rightDrawer.setClickSelectDefaultData();
                drawerLayout.toggle();
            }
        });

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
                drawerLayout.toggle();
            }

            @Override
            public void rightClick(boolean haveSelected, String content) {
                String start = rightDrawer.getStartDate();
                String end = rightDrawer.getEndDate();
                LocalDate startDate = LocalDate.parse(start, DateFormatters.dateFormatter1);
                LocalDate endDate = LocalDate.parse(end, DateFormatters.dateFormatter1);

                if (startDate.isAfter(endDate)) {
                    showErrorDialog(getString(R.string.boc_account_transdetail_start_end));
                } else if (PublicUtils.isCompareDateRange(startDate, endDate, MAX_QUERY_RANGE)) {
                    showErrorDialog(getResources().getString(R.string.boc_account_transdetail_date_range_change,
                            PublicUtils.changeNumberToUpper(MAX_QUERY_RANGE)));
                } else {
                    //日期标题提示
                    String dateTitle = content == null ? rightDrawer.getStartDate() +
                            "~" + rightDrawer.getEndDate() : getPerformDate(content);
                    if (haveSelected) {
                        dateLabel.setText(getResources().getString(
                                R.string.boc_transfer_payment_query_label, dateTitle));
                    } else {
                        dateLabel.setText(getResources().getString(
                                R.string.boc_transfer_payment_query_label, dateTitle));
                    }

                    // 记录筛选字体颜色
                    if (radioGroup.getCheckedRadioButtonId() == R.id.btn_pay) {
                        paySearched = true;
                    } else {
                        remindSearched = true;
                    }
                    searchIcon.setImageResource(R.drawable.boc_select_red);
                    searchText.setTextColor(getResources().getColor(R.color.boc_text_color_red));
                    emptyTip.setText(getString(R.string.boc_transfer_select_empty));
                    if (radioGroup.getCheckedRadioButtonId() == R.id.btn_pay) {
                        selectPayDatePos = rightDrawer.getCurPosition();
                        selectPayStartDate = start;
                        selectPayEndDate = end;
                        paySelectDate = dateTitle;
                        model.setPayDateRange(start, end);
                        getPresenter().psnTransActQueryPaymentOrderList();
                    } else {
                        selectRemindDatePos = rightDrawer.getCurPosition();
                        selectRemindStartDate = start;
                        selectRemindEndDate = end;
                        reminderSelectDate = dateTitle;
                        model.setReminderDateRange(start, end);
                        getPresenter().psnTransActQueryReminderOrderList();
                    }
                    drawerLayout.toggle();
                }
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                titleLeftIconClick();
            }
        });
    }

    /**
     * 设置筛选字体颜色
     *
     * @param isSearched
     */
    private void setSearchTextColor(boolean isSearched) {
        if (isSearched) {
            searchIcon.setImageResource(R.drawable.boc_select_red);
            searchText.setTextColor(getResources().getColor(R.color.boc_text_color_red));
        } else {
            searchIcon.setImageResource(R.drawable.boc_select_gray);
            searchText.setTextColor(getResources().getColor(R.color.boc_text_color_dark_gray));
        }
    }

    /**
     * 获取付款数据
     */
    private void getPayData() {
        dateLabel.setText(getString(R.string.boc_transfer_payment_query_label, paySelectDate));
        if (model.getCurrentPayIndex() == 0) {
            getPresenter().psnTransActQueryPaymentOrderList();
        } else {
            adapter.setData(model.getPayList());
            emptyTip.setVisibility(View.GONE);
            refreshLayout.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 获取收款数据
     */
    private void getReminderData() {
        dateLabel.setText(getString(R.string.boc_transfer_payment_query_label, reminderSelectDate));
        if (model.getCurrentReminderIndex() == 0) {
            getPresenter().psnTransActQueryReminderOrderList();
        } else {
            adapter.setData(model.getReminderList());
            emptyTip.setVisibility(View.GONE);
            refreshLayout.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 根据日期回调str返回显示str
     *
     * @param callBackStr
     * @return
     */
    private String getPerformDate(String callBackStr) {
        String performDate = "";
        if (callBackStr.contains("1月")) {
            performDate = "近1个月";
        } else if (callBackStr.contains("3月")) {
            performDate = "近3个月";
        } else {
            performDate = callBackStr;
        }
        return performDate;
    }

    public void refreshResultData(String status) {
        selectedItem.setContentLeftBelowAgain(status);
        adapter.notifyDataSetChanged();
    }

    /**
     * 起始日期的选择
     */
    private void judgeStartTimeAndSet(LocalDate currentDate) {
        DateTimePicker.showDatePick(mContext, currentDate, DateFormatters.dateFormatter1, new DateTimePicker.DatePickCallBack() {
            @Override
            public void onChoiceDateSet(String strChoiceTime, LocalDate choiceDate) {
                LocalDate current = ApplicationContext.getInstance().getCurrentSystemDate().toLocalDate();
                if (current.isBefore(choiceDate)) {
                    showErrorDialog(getResources().getString(R.string.boc_account_transdetail_start_before));
                    return;
                }
                if (PublicUtils.isCompareDateRange(choiceDate, ApplicationContext.getInstance()
                        .getCurrentSystemDate().toLocalDate(), MAX_QUERY_DATE)) {
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
        DateTimePicker.showDatePick(mContext, currentDate, DateFormatters.dateFormatter1,
                new DateTimePicker.DatePickCallBack() {
                    @Override
                    public void onChoiceDateSet(String strChoiceTime, LocalDate choiceDate) {
                        LocalDate current = ApplicationContext.getInstance().getCurrentSystemDate().toLocalDate();
                        if (current.isBefore(choiceDate)) {
                            showErrorDialog(getResources().getString(R.string.boc_account_transdetail_end_before));
                            return;
                        } else {
                            rightDrawer.setEndDate(strChoiceTime);
                        }
                        if (PublicUtils.isCompareDateRange(choiceDate, ApplicationContext.getInstance()
                                .getCurrentSystemDate().toLocalDate(), MAX_QUERY_DATE)) {
                            showErrorDialog(getResources().getString(R.string.boc_account_transdetail_end_year_change, "" + (MAX_QUERY_DATE / 12)));
                            return;
                        }
                    }
                });
    }

    @Override
    public void requestFailed(BiiResultErrorException biiResultErrorException) {

    }

    @Override
    public OrderListModel getModel() {
        return model;
    }

    @Override
    public void transActQueryReminderOrderListSucceed() {
        adapter.setData(model.getReminderList());
        if (model.getReminderList().size() == 0) {
            emptyTip.setVisibility(View.VISIBLE);
            refreshLayout.setVisibility(View.GONE);
        } else {
            emptyTip.setVisibility(View.GONE);
            refreshLayout.setVisibility(View.VISIBLE);
        }
        if (refreshLayout.getLoadState() == PullToRefreshLayout.LOADING) {
            refreshLayout.loadmoreCompleted(PullToRefreshLayout.SUCCEED);
        }
    }

    @Override
    public void transActQueryPaymentOrderListSucceed() {
        adapter.setData(model.getPayList());
        if (model.getPayList().size() == 0) {
            emptyTip.setVisibility(View.VISIBLE);
            refreshLayout.setVisibility(View.GONE);
        } else {
            emptyTip.setVisibility(View.GONE);
            refreshLayout.setVisibility(View.VISIBLE);
        }

        if (refreshLayout.getLoadState() == PullToRefreshLayout.LOADING) {
            refreshLayout.loadmoreCompleted(PullToRefreshLayout.SUCCEED);
        }
    }

    @Override
    public void setPresenter(OrderListContact.Presenter presenter) {

    }

    @Override
    protected void titleLeftIconClick() {
        returnClass(returnCode);
    }

    @Override
    public boolean onBack() {
        returnClass(returnCode);
        return false;
    }

    private void returnClass(int returnCode) {
        if (returnCode == 1) {
            if (isReInit) {
                popToAndReInit(MeMakeCollectionFragment1.class);
            } else {
                popTo(MeMakeCollectionFragment1.class, false);
            }
        } else {
            pop();
        }
    }


    @Override
    protected boolean isHaveTitleBarView() {
        return false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected OrderListPresenter initPresenter() {
        return new OrderListPresenter(this);
    }

    /**
     * 清除数据并重新请求收款
     */
    public void cleanReQryReminder() {
        refreshLayout.setVisibility(View.GONE);
        //撤消后数据发生变化，清除付款数据并设置
        model.setReminderList(new ArrayList<ShowListBean>());
        adapter.setData(model.getReminderList());
        //为了重新请求数据，修改值
        model.setCurrentReminderIndex(0);
        btn_make.setChecked(true);
        getReminderData();
    }
}
