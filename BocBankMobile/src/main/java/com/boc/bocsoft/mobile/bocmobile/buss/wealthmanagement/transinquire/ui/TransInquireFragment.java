package com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.transinquire.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
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
import com.boc.bocsoft.mobile.bocmobile.base.widget.singlemoreselect.Content;
import com.boc.bocsoft.mobile.bocmobile.base.widget.singlemoreselect.SelectGridView;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.utils.PublicUtils;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.repealorder.ui.ChooseWealthAccountFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.transinquire.model.SelectParams;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.transinquire.model.XpadAccountQueryViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.transinquire.model.XpadRecentAccountQueryViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.transinquire.presenter.TransInquirePresenter;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.wealthproduct.ui.WealthProductFragment;
import com.boc.bocsoft.mobile.common.utils.NumberUtils;
import com.boc.bocsoft.mobile.common.utils.date.DateFormatters;
import com.boc.bocsoft.mobile.framework.widget.TitleBarView;

import org.threeten.bp.LocalDate;

import java.util.ArrayList;
import java.util.List;

/**
 * Fragment：中银理财-交易查询页面
 * Created by zhx on 2016/9/7
 */
public class TransInquireFragment extends BussFragment implements TransInquireContact.View {
    private static final int NOTICE_TYPE_PAGE_SELECT = 0; // 0表示“页面选中”通知
    private static final int NOTICE_TYPE_CHOOSE_ACCOUNT = 1; // 1表示“账户选择”通知
    private static final int NOTICE_TYPE_CONDITION_SELECT = 2; // 2表示“筛选条件选择”通知
    public static final String FROM_WHERE = "from_where"; // 页面从哪里来
    private static int mFromWhere = 0;
    private View rootView;
    protected TabPageIndicatorWidget tabIndicator; // ViewPager指示器组件
    protected ViewPager viewPager;
    String[] data = new String[]{"com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.transinquire.ui.WealthHistoryListFragment",
            "com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.transinquire.ui.EntrustTransFragment1",
            "com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.transinquire.ui.ExpireProductFragment",
    }; // MyFragmentAdapter需要的Fragment数组

    String[] title = new String[]{"历史交易", "委托交易", "已到期产品"}; // tab的标题
    // 侧滑
    private SlipDrawerLayout mDrawerLayout;
    // 选择时间范围组件（rightDrawer）
    private SelectTimeRangeNew selectTimeRangeNew;
    private TextView tv_select; // “筛选”按钮
    private TitleBarView select_title_view; // 标题
    private TransInquirePresenter transInquirePresenter;

    private TextView tv_choose_account;
    // ----------------
    public static final int REQUEST_CODE_CHOOSE_QUERY_ACCOUNT = 112; // 选择要查询的账户
    private XpadAccountQueryViewModel.XPadAccountEntity mCurrentSelectAccount; // 当前选择的账户
    private SelectParams mCurrentSelectQueryParams; // 当前选择的账户
    private SelectParams mSelectParams0; // 第1个页面的筛选条件
    private SelectParams mSelectParams1; // 第2个页面的筛选条件
    private SelectParams mSelectParams2; // 第3个页面的筛选条件
    private int dateSelectedPosition0 = 2; // 日期选择位置（“一周”、“一个月”、“三个月”）
    private int dateSelectedPosition1 = 2; // 日期选择位置（“一周”、“一个月”、“三个月”）

    private int dateSelectedPosition2 = 2; // 日期选择位置（“一周”、“一个月”、“三个月”）
    private boolean isSelectButton0Red = false; // 筛选按钮0是否是红色
    private boolean isSelectButton1Red = false; // 筛选按钮1是否是红色

    private boolean isSelectButton2Red = false; // 筛选按钮2是否是红色

    // ----------------
    /**
     * 查询范围相关
     */
    // 最大查询范围为3个月
    private final static int MAX_QUERY_RANGE = 3;
    // 最大查询起始日期为一年
    private final static int MAX_QUERY_DATE = 12;
    private SelectGridView sgv_money_type;
    private SelectGridView sgv_trans_type;
    private SelectGridView sgv_trans_type1;
    // 筛选处额外添加的layout
    private LinearLayout llAddToLayout;
    // 筛选处额外添加的layout
    private LinearLayout llAddToTopLayout;
    // 币种筛选的数据
    private List<Content> selectMoneyTypeList;
    // 交易类型筛选的数据
    private List<Content> selectTransTypeList;
    // 交易类型筛选的数据
    private List<Content> selectTransTypeList1;
    // 筛选出的当前 币种 信息
    private String currentMoneyType;
    // 筛选出的当前 交易类型 信息
    private String currentTransType;
    private String lastTransType;
    // 起始时间
    private LocalDate startLocalDate;
    // 结束时间
    private LocalDate endLocalDate;
    // 判断是否是筛选
    private boolean isSelectData;
    // 判断是否是上拉加载
    private boolean isPullToRefresh;
    private LinearLayout ll_trans_type;
    private LinearLayout ll_money_type;
    private int currentPagePosition = 0; // 当前页面位置
    private View view_top_divider;
    private MyFragmentAdapter fragmentAdapter;
    private XpadAccountQueryViewModel xpadAccountQueryViewModel;
    private List<XpadAccountQueryViewModel.XPadAccountEntity> mAccountList;
    private MyReceiver myReceiver;
    private LinearLayout ll_choose_select; // 中间包含“账户筛选”按钮和“条件筛选”按钮的线性布局

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        rootView = mInflater.inflate(R.layout.fragment_trans_inquire, null);
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

        // ------------
        mDrawerLayout = (SlipDrawerLayout) rootView.findViewById(R.id.drawer_layout_transfer_query);
        selectTimeRangeNew = (SelectTimeRangeNew) rootView.findViewById(R.id.right_drawer_transfer_query);
        tv_select = (TextView) rootView.findViewById(R.id.tv_select);
        select_title_view = (TitleBarView) rootView.findViewById(R.id.select_title_view);
        initTitleView();

        tv_choose_account = (TextView) rootView.findViewById(R.id.tv_choose_account);
        view_top_divider = (View) rootView.findViewById(R.id.view_top_divider);

        // ------------
        View selectTransLayout = View.inflate(mContext, R.layout.boc_view_xpad_trans_inquire_select, null); // 交易类型布局：中间的
        View selectTransLayoutTop = View.inflate(mContext, R.layout.boc_view_xpad_trans_inquire_select_top, null); // 交易类型布局：上边的
        sgv_money_type = (SelectGridView) selectTransLayout.findViewById(R.id.sgv_money_type);
        sgv_trans_type = (SelectGridView) selectTransLayout.findViewById(R.id.sgv_trans_type);
        ll_trans_type = (LinearLayout) selectTransLayout.findViewById(R.id.ll_trans_type);
        ll_money_type = (LinearLayout) selectTransLayout.findViewById(R.id.ll_money_type);
        sgv_trans_type1 = (SelectGridView) selectTransLayoutTop.findViewById(R.id.sgv_trans_type1);
        llAddToLayout = selectTimeRangeNew.getAddToLayout();
        llAddToTopLayout = selectTimeRangeNew.getAddToTopLayout();

        llAddToLayout.setVisibility(View.VISIBLE);
        llAddToLayout.addView(selectTransLayout);

        llAddToTopLayout.setVisibility(View.GONE);
        llAddToTopLayout.addView(selectTransLayoutTop);
        initTransTypeSelectView(); // 初始化交易类型选择
        initTransTypeSelectView1(); // 初始化交易类型选择
        initMoneyTypeSelectView(); // 初始化币种选择
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
    public void initData() {
        initSelectParams();

        XpadAccountQueryViewModel viewModel = new XpadAccountQueryViewModel();
        viewModel.setQueryType("0");
        viewModel.setXpadAccountSatus("1");
        transInquirePresenter = new TransInquirePresenter(this);
        transInquirePresenter.psnXpadAccountQuery(viewModel);

        initQueryDate();
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
                start(toFragment);
            }
        });

        tv_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.toggle();

                resetPageSelectConditionShow(currentPagePosition);
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
                intent.putExtra("noticeType", NOTICE_TYPE_PAGE_SELECT); // 0表示“页面选中”通知（共4种通知类型，页面选中通知，筛选条件选择通知，账户选择通知，撤单成功通知。这4种都有可能进行重新查询操作）
                intent.putExtra("selectPagePosition", currentPagePosition);
                intent.putExtra("BroadcastSource", "TransInquireFragment");
                mActivity.sendBroadcast(intent);

                resetPageSelectConditionShow(currentPagePosition);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        // 币种筛选
        sgv_money_type.setListener(new SelectGridView.ClickListener() {
            @Override
            public void setItemClick(AdapterView<?> parent, View view, int position, long id) {
                Content selectMoneyType = (Content) parent.getAdapter().getItem(position);
                currentMoneyType = selectMoneyType.getContentNameID();
            }
        });

        // 交易类型筛选
        sgv_trans_type.setListener(new SelectGridView.ClickListener() {
            @Override
            public void setItemClick(AdapterView<?> parent, View view, int position, long id) {
                Content selectTransType = (Content) parent.getAdapter().getItem(position);
                currentTransType = selectTransType.getContentNameID();

                //                // TODO 在此处控制交易日期的显示
                //                if ("0".equals(currentTransType)) {
                //                    selectTimeRangeNew.getTimeLayout().setVisibility(View.GONE);
                //                } else {
                //                    selectTimeRangeNew.getTimeLayout().setVisibility(View.VISIBLE);
                //                }
            }
        });

        // 交易类型筛选（顶部的）
        sgv_trans_type1.setListener(new SelectGridView.ClickListener() {
            @Override
            public void setItemClick(AdapterView<?> parent, View view, int position, long id) {
                Content selectTransType = (Content) parent.getAdapter().getItem(position);
                currentTransType = selectTransType.getContentNameID();

                // TODO 在此处控制交易日期的显示
                if ("0".equals(currentTransType)) {
                    selectTimeRangeNew.getTimeLayout().setVisibility(View.GONE);
                } else {
                    selectTimeRangeNew.getTimeLayout().setVisibility(View.VISIBLE);
                }
            }
        });

        // 筛选重置按钮的监听
        selectTimeRangeNew.setResetListener(new SelectTimeRangeNew.ResetClickListener() {
            @Override
            public void resetClick() {
                // 控制时间一栏的显示
                LocalDate currentDate = ApplicationContext.getInstance().getCurrentSystemDate().toLocalDate();
                String startStr = currentDate.plusMonths(-MAX_QUERY_RANGE).plusDays(1).format(DateFormatters.dateFormatter1);
                String endStr = currentDate.format(DateFormatters.dateFormatter1);
                selectTimeRangeNew.setDefaultDate(2, startStr, endStr);
                resetTransTypeSelected();
                resetTransTypeSelected1();
                resetMoneyTypeSelected();
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
                if (PublicUtils.judgeChoiceDateRange(start, end, MAX_QUERY_RANGE, TransInquireFragment.this)) {
                    mDrawerLayout.toggle();

                    isSelectData = true;
                    isPullToRefresh = false;

                    startLocalDate = start;
                    endLocalDate = end;

                    // 此处保存当前页面的筛选条件
                    switch (currentPagePosition) {
                        case 0:
                            mSelectParams0.setStartDate(startTime);
                            mSelectParams0.setEndDate(endTime);
                            mSelectParams0.setTransType(currentTransType);
                            mSelectParams0.setCurrency(currentMoneyType);

                            // 保存日期的位置
                            dateSelectedPosition0 = selectTimeRangeNew.getCurPosition();

                            isSelectButton0Red = true;
                            break;
                        case 1:
                            mSelectParams1.setStartDate(startTime);
                            mSelectParams1.setEndDate(endTime);
                            mSelectParams1.setTransType(currentTransType);
                            mSelectParams1.setCurrency(currentMoneyType);

                            // 保存日期的位置
                            dateSelectedPosition1 = selectTimeRangeNew.getCurPosition();

                            isSelectButton1Red = true;
                            break;
                        case 2:
                            mSelectParams2.setStartDate(startTime);
                            mSelectParams2.setEndDate(endTime);
                            mSelectParams2.setTransType(currentTransType);
                            mSelectParams2.setCurrency(currentMoneyType);

                            // 保存日期的位置
                            dateSelectedPosition2 = selectTimeRangeNew.getCurPosition();

                            isSelectButton2Red = true;
                            break;
                    }

                    // 此处填充页面查询操作的请求参数
                    SelectParams selectParams = new SelectParams();
                    selectParams.setStartDate(startTime);
                    selectParams.setEndDate(endTime);
                    selectParams.setTransType(currentTransType);
                    selectParams.setCurrency(getCurrencyType(currentMoneyType));

                    // 发出“开始查询的广播”广播
                    Intent intent = new Intent();
                    intent.setAction("startQuery");
                    intent.putExtra("selectParams", selectParams);
                    intent.putExtra("noticeType", NOTICE_TYPE_CONDITION_SELECT); // 2表示“筛选条件选择”通知
                    intent.putExtra("selectPagePosition", currentPagePosition);
                    intent.putExtra("BroadcastSource", "TransInquireFragment");
                    mActivity.sendBroadcast(intent);

                    // 设置筛选按钮的颜色为红色
                    tv_select.setTextColor(getResources().getColor(R.color.boc_text_color_red));
                    tv_select.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.boc_select_red), null);
                }
            }
        });
    }

    // 重置页面选择条件的显示
    private void resetPageSelectConditionShow(int position) {
        switch (position) {
            case 0: // 历史交易
                llAddToTopLayout.setVisibility(View.GONE);
                selectTimeRangeNew.getTimeLayout().setVisibility(View.VISIBLE);
                ll_trans_type.setVisibility(View.VISIBLE);
                ll_money_type.setVisibility(View.VISIBLE);

                // 控制交易类型和币种的显示
                resetPageSelectTransTypeSelected(position);
                resetPageSelectTransTypeSelected1(position);
                resetPageSelectMoneyTypeSelected(position);

                // 控制时间一栏的显示
                selectTimeRangeNew.setDefaultDate(dateSelectedPosition0, mSelectParams0.getStartDate(), mSelectParams0.getEndDate());

                // 控制选择帐号的隐藏和显示
                tv_choose_account.setVisibility(View.VISIBLE);
                tv_choose_account.setClickable(true);
                view_top_divider.setVisibility(View.VISIBLE);

                // 控制筛选按钮是否红色
                if (isSelectButton0Red) {
                    tv_select.setTextColor(getResources().getColor(R.color.boc_text_color_red));
                    tv_select.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.boc_select_red), null);
                } else {
                    tv_select.setTextColor(getResources().getColor(R.color.boc_text_color_common_gray));
                    tv_select.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.boc_select_gray), null);
                }
                break;
            case 1: // 委托交易
                llAddToTopLayout.setVisibility(View.VISIBLE);

                if ("0".equals(currentTransType)) {
                    selectTimeRangeNew.getTimeLayout().setVisibility(View.GONE);
                } else {
                    selectTimeRangeNew.getTimeLayout().setVisibility(View.VISIBLE);
                }

                ll_trans_type.setVisibility(View.GONE);
                ll_money_type.setVisibility(View.GONE);

                // 控制交易类型和币种的显示
                resetPageSelectTransTypeSelected(position);
                resetPageSelectTransTypeSelected1(position);
                resetPageSelectMoneyTypeSelected(position);

                // 控制时间一栏的显示
                selectTimeRangeNew.setDefaultDate(dateSelectedPosition1, mSelectParams1.getStartDate(), mSelectParams1.getEndDate());

                // 控制选择帐号的隐藏和显示
                tv_choose_account.setVisibility(View.VISIBLE);
                tv_choose_account.setClickable(true);
                view_top_divider.setVisibility(View.VISIBLE);

                // 控制筛选按钮是否红色
                if (isSelectButton1Red) {
                    tv_select.setTextColor(getResources().getColor(R.color.boc_text_color_red));
                    tv_select.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.boc_select_red), null);
                } else {
                    tv_select.setTextColor(getResources().getColor(R.color.boc_text_color_common_gray));
                    tv_select.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.boc_select_gray), null);
                }
                break;
            case 2: // 已到期产品
                llAddToTopLayout.setVisibility(View.GONE);

                selectTimeRangeNew.getTimeLayout().setVisibility(View.VISIBLE);

                ll_trans_type.setVisibility(View.GONE);
                ll_money_type.setVisibility(View.GONE);

                // 控制交易类型和币种的显示
                resetPageSelectTransTypeSelected(position);
                resetPageSelectTransTypeSelected1(position);
                resetPageSelectMoneyTypeSelected(position);

                // 控制时间一栏的显示
                selectTimeRangeNew.setDefaultDate(dateSelectedPosition2, mSelectParams2.getStartDate(), mSelectParams2.getEndDate());

                // 控制选择帐号的隐藏和显示
                tv_choose_account.setVisibility(View.INVISIBLE);
                tv_choose_account.setClickable(false);
                view_top_divider.setVisibility(View.INVISIBLE);

                // 控制筛选按钮是否红色
                if (isSelectButton2Red) {
                    tv_select.setTextColor(getResources().getColor(R.color.boc_text_color_red));
                    tv_select.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.boc_select_red), null);
                } else {
                    tv_select.setTextColor(getResources().getColor(R.color.boc_text_color_common_gray));
                    tv_select.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.boc_select_gray), null);
                }
                break;
        }
    }

    /**
     * 调用此方法跳转到当前页面
     * @param bussActivity
     * @param fromWhere
     */
    public static TransInquireFragment newinstance(BussActivity bussActivity, int fromWhere) {
        mFromWhere = fromWhere;
        TransInquireFragment fragment = bussActivity.findFragment(TransInquireFragment.class);
        if (fragment == null) {
            fragment = new TransInquireFragment();
            if (fromWhere != 0) {
                bussActivity.start(fragment);
            }
        } else {
            bussActivity.popTo(TransInquireFragment.class, false);

            fragment.reQuery();
            // 发出“开始查询的广播”广播
            Intent intent = new Intent();
            intent.setAction("startQuery");
            intent.putExtra("noticeType", 5); // 5表示“刷新”通知
            intent.putExtra("selectPagePosition", 0);
            intent.putExtra("BroadcastSource", "TransInquireFragment");
            bussActivity.sendBroadcast(intent);
        }
        return fragment;
    }

    // 重新查询
    public void reQuery() {
        XpadAccountQueryViewModel viewModel = new XpadAccountQueryViewModel();
        viewModel.setQueryType("0");
        viewModel.setXpadAccountSatus("1");
        transInquirePresenter = new TransInquirePresenter(this);
        transInquirePresenter.psnXpadAccountQuery(viewModel);
    }

    // 初始化选择的参数
    private void initSelectParams() {
        mSelectParams0 = new SelectParams();
        mSelectParams0.setTransType("0");
        mSelectParams0.setCurrency("0");
        mSelectParams0.setStartDate(ApplicationContext.getInstance().getCurrentSystemDate().toLocalDate().minusMonths(3).plusDays(1).format(
                DateFormatters.dateFormatter1));
        mSelectParams0.setEndDate(ApplicationContext.getInstance().getCurrentSystemDate().toLocalDate().format(DateFormatters.dateFormatter1));

        mSelectParams1 = new SelectParams();
        mSelectParams1.setTransType("0");
        mSelectParams1.setCurrency("0");
        mSelectParams1.setStartDate(ApplicationContext.getInstance().getCurrentSystemDate().toLocalDate().minusMonths(3).plusDays(1).format(
                DateFormatters.dateFormatter1));
        mSelectParams1.setEndDate(ApplicationContext.getInstance().getCurrentSystemDate().toLocalDate().format(DateFormatters.dateFormatter1));

        mSelectParams2 = new SelectParams();
        mSelectParams2.setTransType("0");
        mSelectParams2.setCurrency("0");
        mSelectParams2.setStartDate(ApplicationContext.getInstance().getCurrentSystemDate().toLocalDate().minusMonths(3).plusDays(1).format(
                DateFormatters.dateFormatter1));
        mSelectParams2.setEndDate(ApplicationContext.getInstance().getCurrentSystemDate().toLocalDate().format(DateFormatters.dateFormatter1));
    }

    // 根据typeId获取货币类型
    private String getCurrencyType(String typeId) {
        String currencyType = null;
        // 000-全部、001-人民币元、 014-美元、012-英镑、 013：港币 028: 加拿大元 029：澳元 038：欧元 027：日元）
        if ("0".equals(typeId)) {
            currencyType = "000";
        } else if ("1".equals(typeId)) {
            currencyType = "001";
        } else if ("2".equals(typeId)) {
            currencyType = "014";
        } else if ("3".equals(typeId)) {
            currencyType = "012";
        } else if ("4".equals(typeId)) {
            currencyType = "013";
        } else if ("5".equals(typeId)) {
            currencyType = "028";
        } else if ("6".equals(typeId)) {
            currencyType = "029";
        } else if ("7".equals(typeId)) {
            currencyType = "038";
        } else if ("8".equals(typeId)) {
            currencyType = "027";
        }

        return currencyType;
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
            queryIntent.putExtra("BroadcastSource", "TransInquireFragment");
            mActivity.sendBroadcast(queryIntent); // 这里queryIntent写成了intent，以后千万别犯这种错误
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mActivity.unregisterReceiver(myReceiver);
    }

    // 初始化 币种 筛选数据
    private void initMoneyTypeSelectView() {
        String[] selectMoneyTypeData = {"全部", "人民币元", "美元", "英镑", "港币", "加拿大元", "澳元", "欧元", "日元"};
        String[] selectMoneyTypeIdData = {"0", "1", "2", "3", "4", "5", "6", "7", "8"};

        selectMoneyTypeList = new ArrayList<Content>();

        for (int i = 0; i < selectMoneyTypeData.length; i++) {
            Content item = new Content();
            item.setName(selectMoneyTypeData[i]);
            item.setContentNameID(selectMoneyTypeIdData[i]);
            if (i == 0) {
                currentMoneyType = selectMoneyTypeIdData[i];
                item.setSelected(true);
            }
            selectMoneyTypeList.add(item);
        }
        sgv_money_type.setData(selectMoneyTypeList);
    }

    // 初始化 交易类型 筛选数据
    private void initTransTypeSelectView() {
        String[] selectTransTypeData = {"常规交易", "组合购买"};
        String[] selectTransTypeIdData = {"0", "1"};

        selectTransTypeList = new ArrayList<Content>();

        for (int i = 0; i < selectTransTypeData.length; i++) {
            Content item = new Content();
            item.setName(selectTransTypeData[i]);
            item.setContentNameID(selectTransTypeIdData[i]);
            if (i == 0) {
                currentTransType = selectTransTypeIdData[i];
                lastTransType = currentTransType;
                item.setSelected(true);

                // TODO 在此处控制交易日期的显示
                if (currentPagePosition == 1) {
                    if ("0".equals(item.getContentNameID())) {
                        selectTimeRangeNew.getTimeLayout().setVisibility(View.GONE);
                    } else {
                        selectTimeRangeNew.getTimeLayout().setVisibility(View.VISIBLE);
                    }
                } else {
                    selectTimeRangeNew.getTimeLayout().setVisibility(View.VISIBLE);
                }
            }
            selectTransTypeList.add(item);
        }
        sgv_trans_type.setData(selectTransTypeList);
    }

    // 初始化 交易类型 筛选数据
    private void initTransTypeSelectView1() {
        String[] selectTransTypeData = {"常规交易", "组合购买"};
        String[] selectTransTypeIdData = {"0", "1"};

        selectTransTypeList1 = new ArrayList<Content>();

        for (int i = 0; i < selectTransTypeData.length; i++) {
            Content item = new Content();
            item.setName(selectTransTypeData[i]);
            item.setContentNameID(selectTransTypeIdData[i]);
            if (i == 0) {
                currentTransType = selectTransTypeIdData[i];
                lastTransType = currentTransType;
                item.setSelected(true);

                // TODO 在此处控制交易日期的显示
                if (currentPagePosition == 1) {
                    if ("0".equals(item.getContentNameID())) {
                        selectTimeRangeNew.getTimeLayout().setVisibility(View.GONE);
                    } else {
                        selectTimeRangeNew.getTimeLayout().setVisibility(View.VISIBLE);
                    }
                } else {
                    selectTimeRangeNew.getTimeLayout().setVisibility(View.VISIBLE);
                }
            }
            selectTransTypeList1.add(item);
        }
        sgv_trans_type1.setData(selectTransTypeList1);
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

        currentMoneyType = "0";
    }

    /**
     * （页面选中时）重置币种的选中
     */
    private void resetPageSelectMoneyTypeSelected(int position) {
        SelectParams selectParams = null;
        switch (position) {
            case 0:
                selectParams = mSelectParams0;
                break;
            case 1:
                selectParams = mSelectParams1;
                break;
            case 2:
                selectParams = mSelectParams2;
                break;
        }

        for (int i = 0; i < selectMoneyTypeList.size(); i++) {
            Content item = selectMoneyTypeList.get(i);

            if (item.getContentNameID().equals(selectParams.getCurrency())) { // 重置当前页面的币种
                item.setSelected(true);
            } else {
                item.setSelected(false);
            }
        }
        sgv_money_type.getAdapter().notifyDataSetChanged();

        currentMoneyType = selectParams.getCurrency();
    }

    /**
     * 重置交易类型的选中
     */
    private void resetTransTypeSelected() {
        for (int i = 0; i < selectTransTypeList.size(); i++) {
            Content item = selectTransTypeList.get(i);
            if (item.getContentNameID().equals("0")) { // 重置为"常规"
                item.setSelected(true);

                // TODO 在此处控制交易日期的显示
                if (currentPagePosition == 1) {
                    if ("0".equals(item.getContentNameID())) {
                        selectTimeRangeNew.getTimeLayout().setVisibility(View.GONE);
                    } else {
                        selectTimeRangeNew.getTimeLayout().setVisibility(View.VISIBLE);
                    }
                } else {
                    selectTimeRangeNew.getTimeLayout().setVisibility(View.VISIBLE);
                }
            } else {
                item.setSelected(false);
            }
        }
        sgv_trans_type.getAdapter().notifyDataSetChanged();

        currentTransType = "0";
    }

    /**
     * 重置（页面选中）交易类型的选中
     */
    private void resetPageSelectTransTypeSelected(int position) {
        SelectParams selectParams = null;
        switch (position) {
            case 0:
                selectParams = mSelectParams0;
                break;
            case 1:
                selectParams = mSelectParams1;
                break;
            case 2:
                selectParams = mSelectParams2;
                break;
        }

        for (int i = 0; i < selectTransTypeList.size(); i++) {
            Content item = selectTransTypeList.get(i);

            if (item.getContentNameID().equals(selectParams.getTransType())) { // 重置为当前页面的交易类型
                item.setSelected(true);

                // TODO 在此处控制交易日期的显示
                if (currentPagePosition == 1) {
                    if ("0".equals(item.getContentNameID())) {
                        selectTimeRangeNew.getTimeLayout().setVisibility(View.GONE);
                    } else {
                        selectTimeRangeNew.getTimeLayout().setVisibility(View.VISIBLE);
                    }
                } else {
                    selectTimeRangeNew.getTimeLayout().setVisibility(View.VISIBLE);
                }
            } else {
                item.setSelected(false);
            }
        }
        sgv_trans_type.getAdapter().notifyDataSetChanged();

        currentTransType = selectParams.getTransType();
    }

    /**
     * 重置交易类型的选中
     */
    private void resetTransTypeSelected1() {
        for (int i = 0; i < selectTransTypeList1.size(); i++) {
            Content item = selectTransTypeList1.get(i);

            if (item.getContentNameID().equals("0")) { // 重置为全部
                item.setSelected(true);

                // 在此处控制交易日期的显示
                if (currentPagePosition == 1) {
                    if ("0".equals(item.getContentNameID())) {
                        selectTimeRangeNew.getTimeLayout().setVisibility(View.GONE);
                    } else {
                        selectTimeRangeNew.getTimeLayout().setVisibility(View.VISIBLE);
                    }
                } else {
                    selectTimeRangeNew.getTimeLayout().setVisibility(View.VISIBLE);
                }
            } else {
                item.setSelected(false);
            }
        }
        sgv_trans_type1.getAdapter().notifyDataSetChanged();

        currentTransType = "0";
    }

    /**
     * 重置(页面选中)交易类型的选中
     */
    private void resetPageSelectTransTypeSelected1(int position) {
        SelectParams selectParams = null;
        switch (position) {
            case 0:
                selectParams = mSelectParams0;
                break;
            case 1:
                selectParams = mSelectParams1;
                break;
            case 2:
                selectParams = mSelectParams2;
                break;
        }

        for (int i = 0; i < selectTransTypeList1.size(); i++) {
            Content item = selectTransTypeList1.get(i);

            if (item.getContentNameID().equals(selectParams.getTransType())) { // 重置当前页面的交易类型
                item.setSelected(true);

                // 在此处控制交易日期的显示
                if (currentPagePosition == 1) {
                    if ("0".equals(item.getContentNameID())) {
                        selectTimeRangeNew.getTimeLayout().setVisibility(View.GONE);
                    } else {
                        selectTimeRangeNew.getTimeLayout().setVisibility(View.VISIBLE);
                    }
                } else {
                    selectTimeRangeNew.getTimeLayout().setVisibility(View.VISIBLE);
                }
            } else {
                item.setSelected(false);
            }
        }
        sgv_trans_type1.getAdapter().notifyDataSetChanged();

        currentTransType = selectParams.getTransType();
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

    /**
     * 初始化Title
     */
    private void initTitleView() {
        select_title_view.setStyle(R.style.titlebar_common_white);
        select_title_view.setTitle("交易查询");
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
    protected boolean getTitleBarRed() {
        return false;
    }

    @Override
    protected boolean isDisplayRightIcon() {
        return false;
    }

    // 查询客户理财账户信息
    @Override
    public void psnXpadAccountQuerySuccess(XpadAccountQueryViewModel xpadAccountQueryViewModel) {

        this.xpadAccountQueryViewModel = xpadAccountQueryViewModel;
        mAccountList = xpadAccountQueryViewModel.getList();

        // 查询最近账户
        XpadRecentAccountQueryViewModel xpadRecentAccountQueryViewModel = new XpadRecentAccountQueryViewModel();
        transInquirePresenter.psnXpadRecentAccountQuery(xpadRecentAccountQueryViewModel);
    }

    @Override
    public void psnXpadAccountQueryFail(BiiResultErrorException biiResultErrorException) {
    }

    @Override
    public void psnXpadRecentAccountQuerySuccess(XpadRecentAccountQueryViewModel xpadRecentAccountQueryViewModel) {
        for (XpadAccountQueryViewModel.XPadAccountEntity accountEntity : xpadAccountQueryViewModel.getList()) {
            if (accountEntity.getAccountNo().equals(xpadRecentAccountQueryViewModel.getAccountNo())) {
                mCurrentSelectAccount = accountEntity;
            }
        }

        // 临时代码
        if (mCurrentSelectAccount == null) {
            mCurrentSelectAccount = xpadAccountQueryViewModel.getList().get(0);
        }

        tv_choose_account.setText(NumberUtils.formatCardNumberStrong(mCurrentSelectAccount.getAccountNo()));
        ll_choose_select.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onBack() {
        if (mDrawerLayout.isOpen()) {
            mDrawerLayout.toggle();
            return false; // 返回false，表示当前方法处理回退事件
        } else {
            if(1 == mFromWhere) {
                WealthProductFragment fragment = mActivity.findFragment(WealthProductFragment.class);
                if (fragment != null) {
                    popToAndReInit(WealthProductFragment.class);
                } else {
                    mActivity.finish();
                }
                return false;
            } else {
                return true; // 返回true，表示当前方法没有处理回退事件
            }
        }
    }

    @Override
    public void psnXpadRecentAccountQueryFail(BiiResultErrorException biiResultErrorException) {
    }

    // 获取当前选择的账户
    public XpadAccountQueryViewModel.XPadAccountEntity getCurrentSelectAccount() {
        return mCurrentSelectAccount;
    }

    @Override
    public void setPresenter(TransInquireContact.Presenter presenter) {
    }
}
