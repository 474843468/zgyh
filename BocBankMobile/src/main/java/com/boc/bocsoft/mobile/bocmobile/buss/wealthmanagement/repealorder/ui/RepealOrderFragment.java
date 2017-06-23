package com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.repealorder.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.ApplicationContext;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussActivity;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.utils.DateTimePicker;
import com.boc.bocsoft.mobile.bocmobile.base.widget.SlipMenu.SlipDrawerLayout;
import com.boc.bocsoft.mobile.bocmobile.base.widget.TabPageIndicator.MyFragmentAdapter;
import com.boc.bocsoft.mobile.bocmobile.base.widget.TabPageIndicator.TabPageIndicatorWidget;
import com.boc.bocsoft.mobile.bocmobile.base.widget.selectview.selecttimerangenew.SelectTimeRangeNew;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.utils.PublicUtils;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.repealorder.presenter.RepealOrderPresenter;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.transinquire.model.SelectParams;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.transinquire.model.XpadAccountQueryViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.transinquire.model.XpadRecentAccountQueryViewModel;
import com.boc.bocsoft.mobile.common.utils.NumberUtils;
import com.boc.bocsoft.mobile.common.utils.date.DateFormatters;
import com.boc.bocsoft.mobile.framework.widget.TitleBarView;

import org.threeten.bp.LocalDate;

import java.util.List;

/**
 * Fragment：中银理财-撤单页面
 * Created by zhx on 2016/9/23
 */
public class RepealOrderFragment extends BussFragment implements RepealOrderContact.View {
    private static final int NOTICE_TYPE_PAGE_SELECT = 0; // 0表示“页面选中”通知
    private static final int NOTICE_TYPE_CHOOSE_ACCOUNT = 1; // 1表示“账户选择”通知
    private static final int NOTICE_TYPE_CONDITION_SELECT = 2; // 2表示“筛选条件选择”通知
    private View rootView;
    protected TabPageIndicatorWidget tabIndicator; // ViewPager指示器组件
    protected ViewPager viewPager;
    String[] data = new String[]{"com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.repealorder.ui.TransNormalFragment1",
            "com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.repealorder.ui.TransComFragment1",
    }; // MyFragmentAdapter需要的Fragment数组
    String[] title = new String[]{"常规交易", "组合购买"}; // tab的标题


    // 起始时间
    private LocalDate startLocalDate;
    // 结束时间
    private LocalDate endLocalDate;
    /**
     * 查询范围相关
     */
    // 最大查询范围为3个月
    private final static int MAX_QUERY_RANGE = 3;
    // 最大查询起始日期为一年
    private final static int MAX_QUERY_DATE = 12;
    // ---------------
    // 侧滑
    private SlipDrawerLayout mDrawerLayout;
    // 选择时间范围组件（rightDrawer）
    private SelectTimeRangeNew selectTimeRangeNew;
    private TextView tv_select; // “筛选”按钮
    private TitleBarView select_title_view; // 标题
    private MyFragmentAdapter fragmentAdapter;

    // -----------
    // 判断是否是筛选
    private boolean isSelectData;
    // 判断是否是上拉加载
    private boolean isPullToRefresh;
    private TextView tv_choose_account;
    public static final int REQUEST_CODE_CHOOSE_QUERY_ACCOUNT = 112; // 选择要查询的账户
    private int currentPagePosition = 0; // 当前页面位置
    private XpadAccountQueryViewModel.XPadAccountEntity mCurrentSelectAccount; // 当前选择的账户
    private RepealOrderPresenter repealOrderPresenter;
    private XpadAccountQueryViewModel xpadAccountQueryViewModel;
    private MyReceiver myReceiver;
    private List<XpadAccountQueryViewModel.XPadAccountEntity> mAccountList;
    private LinearLayout ll_choose_select;

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        rootView = mInflater.inflate(R.layout.fragment_repeal_order, null);
        return rootView;
    }

    @Override
    public void initView() {
        ll_choose_select = (LinearLayout) rootView.findViewById(R.id.ll_choose_select);
        viewPager = (ViewPager) rootView.findViewById(R.id.view_pager);
        fragmentAdapter = new MyFragmentAdapter((BussActivity) getActivity(), data, title);
        viewPager.setAdapter(fragmentAdapter);
        tabIndicator = (TabPageIndicatorWidget) rootView.findViewById(R.id.tab_indicator);
        tabIndicator.addViewPager(viewPager);
        try {
            tabIndicator.setIndicatorColor(R.color.boc_text_color_red);
            tabIndicator.setIndicatorHeight((int) getResources().getDimension(R.dimen.boc_space_between_5px));
            tabIndicator.setSelectedTextColor(R.color.boc_text_color_red,R.color.boc_text_color_common_gray);

        } catch (Exception e) {
            e.printStackTrace();
        }

        // -------------
        mDrawerLayout = (SlipDrawerLayout) rootView.findViewById(R.id.drawer_layout_transfer_query);
        selectTimeRangeNew = (SelectTimeRangeNew) rootView.findViewById(R.id.right_drawer_transfer_query);
        tv_select = (TextView) rootView.findViewById(R.id.tv_select);
        select_title_view = (TitleBarView) rootView.findViewById(R.id.select_title_view);
        tv_choose_account = (TextView) rootView.findViewById(R.id.tv_choose_account);
        initTitleView();
    }

    @Override
    public void initData() {
        XpadAccountQueryViewModel viewModel = new XpadAccountQueryViewModel();
        viewModel.setQueryType("0");
        viewModel.setXpadAccountSatus("1");
        repealOrderPresenter = new RepealOrderPresenter(this);
        repealOrderPresenter.psnXpadAccountQuery(viewModel);

        initQueryDate();
    }

    @Override
    public void psnXpadAccountQuerySuccess(XpadAccountQueryViewModel xpadAccountQueryViewModel) {


        this.xpadAccountQueryViewModel = xpadAccountQueryViewModel;

        mAccountList = xpadAccountQueryViewModel.getList();

        // 查询最近账户
        XpadRecentAccountQueryViewModel xpadRecentAccountQueryViewModel = new XpadRecentAccountQueryViewModel();
        repealOrderPresenter.psnXpadRecentAccountQuery(xpadRecentAccountQueryViewModel);
    }

    @Override
    public void psnXpadAccountQueryFail(BiiResultErrorException biiResultErrorException) {
    }

    @Override
    public void psnXpadRecentAccountQuerySuccess(XpadRecentAccountQueryViewModel xpadRecentAccountQueryViewModel) {
        for (XpadAccountQueryViewModel.XPadAccountEntity accountEntity : mAccountList) {
            if (accountEntity.getAccountNo().equals(xpadRecentAccountQueryViewModel.getAccountNo())) {
                mCurrentSelectAccount = accountEntity;
            }
        }

        // 临时代码
        if (mCurrentSelectAccount == null) {
            mCurrentSelectAccount = mAccountList.get(0);
        }

        tv_choose_account.setText(NumberUtils.formatCardNumberStrong(mCurrentSelectAccount.getAccountNo()));
        ll_choose_select.setVisibility(View.VISIBLE);
    }

    @Override
    public void psnXpadRecentAccountQueryFail(BiiResultErrorException biiResultErrorException) {
    }

    @Override
    public void setPresenter(RepealOrderContact.Presenter presenter) {
    }

    // 广播接收者，接收ChooseWealthAccountFragment界面发送的广播
    class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            XpadAccountQueryViewModel.XPadAccountEntity account = intent.getParcelableExtra("account");
            mCurrentSelectAccount = account;
            tv_choose_account.setText(NumberUtils.formatCardNumberStrong(mCurrentSelectAccount.getAccountNo()));


            // 发出“开始查询的广播”广播
            Intent queryIntent = new Intent();
            queryIntent.setAction("startQuery");
            queryIntent.putExtra("noticeType", NOTICE_TYPE_CHOOSE_ACCOUNT); // 1表示“账户选择”通知
            queryIntent.putExtra("selectPagePosition", currentPagePosition);
            queryIntent.putExtra("account", mCurrentSelectAccount);
            queryIntent.putExtra("BroadcastSource", "RepealOrderFragment");
            mActivity.sendBroadcast(queryIntent); // 这里queryIntent写成了intent，以后千万别犯这种错误
        }
    }

    @Override
    public void setListener() {
        IntentFilter intentFilter = new IntentFilter("chooseAccount");
        myReceiver = new MyReceiver();
        mActivity.registerReceiver(myReceiver, intentFilter);

        // 选择要查询的账户
        tv_choose_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChooseWealthAccountFragment toFragment = new ChooseWealthAccountFragment();
                startForResult(toFragment, REQUEST_CODE_CHOOSE_QUERY_ACCOUNT);
            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                currentPagePosition = position;

                Intent intent = new Intent();
                intent.setAction("startQuery");
                intent.putExtra("noticeType", NOTICE_TYPE_PAGE_SELECT); // 0表示“页面选中”通知（共3种通知类型，页面选中通知，筛选条件选择通知，账户选择通知。这3种都有可能进行重新查询操作）
                intent.putExtra("selectPagePosition", currentPagePosition);
                intent.putExtra("BroadcastSource", "RepealOrderFragment");
                mActivity.sendBroadcast(intent);
                currentPagePosition = position;

                // 处理筛选按钮隐藏显示的逻辑
                if (position == 0) {
                    tv_select.setVisibility(View.GONE);
                    tv_select.setClickable(false);
                } else {
                    tv_select.setVisibility(View.VISIBLE);
                    tv_select.setClickable(true);
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        tv_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.toggle();
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

                if (PublicUtils.judgeChoiceDateRange(start, end, MAX_QUERY_RANGE, RepealOrderFragment.this)) {
                    mDrawerLayout.toggle();

                    // TODO
                    isSelectData = true;
                    isPullToRefresh = false;

                    startLocalDate = start;
                    endLocalDate = end;

                // 此处填充页面查询操作的请求参数
                SelectParams selectParams = new SelectParams();
                selectParams.setStartDate(startTime);
                selectParams.setEndDate(endTime);

                // 发出“开始查询的广播”广播
                Intent intent = new Intent();
                intent.setAction("startQuery");
                intent.putExtra("noticeType", NOTICE_TYPE_CONDITION_SELECT);
                intent.putExtra("selectParams", selectParams);
                intent.putExtra("selectPagePosition", currentPagePosition);
                intent.putExtra("BroadcastSource", "RepealOrderFragment");
                mActivity.sendBroadcast(intent);
                tv_select.setTextColor(getResources().getColor(R.color.boc_text_color_red));
                tv_select.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.boc_select_red), null);

                }
            }

        });
    }

    @Override
    protected boolean isHaveTitleBarView() {
        return false;
    }

    // 初始化TitleView
    private void initTitleView() {
        select_title_view.setStyle(R.style.titlebar_common_white);
        select_title_view.setTitle("撤单");
        select_title_view.setRightImgBtnVisible(false);

        select_title_view.setLeftButtonOnClickLinster(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                titleLeftIconClick();
            }
        });
    }

    /**
     * 初始化查询的时间
     */
    private void initQueryDate() {
        startLocalDate = ApplicationContext.getInstance().getCurrentSystemDate().toLocalDate().plusMonths(-MAX_QUERY_RANGE).plusDays(1);
        endLocalDate = ApplicationContext.getInstance().getCurrentSystemDate().toLocalDate();
        selectTimeRangeNew.setDefaultDate(startLocalDate.format(DateFormatters.dateFormatter1), endLocalDate.format(DateFormatters.dateFormatter1));
    }

    @Override
    public boolean onBack() {
        if (mDrawerLayout.isOpen()) {
            mDrawerLayout.toggle();
            return false;
        } else {
            return true;
        }
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
    public void onPause() {
        super.onPause();
        Log.e("ljljlj", "RepealOrderFragment onPause()");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        Log.e("ljljlj", "RepealOrderFragment onDestroy()");
        mActivity.unregisterReceiver(myReceiver);
    }
}
