package com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.transactiondetails.ui;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.util.Log;
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
import com.boc.bocsoft.mobile.bocmobile.base.utils.PublicCodeUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.SlipMenu.SlipDrawerLayout;
import com.boc.bocsoft.mobile.bocmobile.base.widget.TabPageIndicator.MyFragmentAdapter;
import com.boc.bocsoft.mobile.bocmobile.base.widget.TabPageIndicator.TabPageIndicatorWidget;
import com.boc.bocsoft.mobile.bocmobile.base.widget.selectview.selecttimerangenew.SelectTimeRangeNew;
import com.boc.bocsoft.mobile.bocmobile.base.widget.singlemoreselect.Content;
import com.boc.bocsoft.mobile.bocmobile.base.widget.singlemoreselect.SelectGridView;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.transactiondetails.model.XpadPsnVFGGetRegCurrencyModel;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.transactiondetails.presenter.TransQueryPresenter;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.utils.PublicUtils;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.transinquire.model.SelectParams;
import com.boc.bocsoft.mobile.common.utils.date.DateFormatters;
import com.boc.bocsoft.mobile.framework.widget.TitleBarView;

import org.threeten.bp.LocalDate;

import java.util.ArrayList;
import java.util.List;

/**
 * 双向宝——交易查询主界面
 * Created by zc on 2016/11/17
 */
public class TransQueryFragment extends BussFragment implements TransQueryContract.View {

    private static String mFromWhere = "";//判断跳转源
    private View rootView;
//    private MyReceiver myReceiver;
    private TransQueryContract.Presenter mTransQueryPresenter;
    protected TabPageIndicatorWidget tabIndicator; // ViewPager指示器组件
    protected ViewPager viewPager;
    String[] data = new String[]{"com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.transactiondetails.ui.SuccessConditionFragment",
            "com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.transactiondetails.ui.OpenTradingFragment",
            "com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.transactiondetails.ui.EffectiveEntrustFragment",
            "com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.transactiondetails.ui.HistoryEntrustFragment",
            "com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.transactiondetails.ui.TakeTradeFragment"
    }; // MyFragmentAdapter需要的Fragment数组
    String[] title = new String[]{"成交状况", "未平仓交易", "有效委托","历史委托","斩仓交易"}; // tab的标题

    // 侧滑
    private SlipDrawerLayout mDrawerLayout;
    // 选择时间范围组件（rightDrawer）
    private TitleBarView select_title_view; // 标题
    private SelectTimeRangeNew selectTimeRangeNew;
    private TextView tv_select; // “筛选”按钮

    /**
     * 查询范围相关
     */
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
    // 筛选出的当前 币种 信息
    private String currentMoneyType;
    private String currentMoneyName;

    // 起始时间
    private LocalDate startLocalDate;
    // 结束时间
    private LocalDate endLocalDate;
    private LinearLayout ll_money_type;
    private int currentPagePosition = 0; // 当前页面位置
    private MyFragmentAdapter fragmentAdapter;
    private static String settleCurrency;


    private static final int NOTICE_TYPE_PAGE_SELECT = 0; // 0表示“页面选中”通知
    private static final int NOTICE_TYPE_CONDITION_SELECT = 1; // 1表示“筛选条件选择”通知
    private static final int NOTICE_TYPE_CONDITION_CANCLE = 2; // 2表示“撤单成功”通知
    private SelectParams mSelectParams0; // 第1个页面的筛选条件
    private SelectParams mSelectParams1; // 第2个页面的筛选条件
    private SelectParams mSelectParams2; // 第3个页面的筛选条件
    private SelectParams mSelectParams3; // 第4个页面的筛选条件
    private SelectParams mSelectParams4; // 第5个页面的筛选条件
    private int dateSelectedPosition0 = 2; // 日期选择位置（“一周”、“一个月”、“三个月”）
    private int dateSelectedPosition1 = 2; // 日期选择位置（“一周”、“一个月”、“三个月”）
    private int dateSelectedPosition2 = 2; // 日期选择位置（“一周”、“一个月”、“三个月”）
    private int dateSelectedPosition3 = 2; // 日期选择位置（“一周”、“一个月”、“三个月”）
    private int dateSelectedPosition4 = 2; // 日期选择位置（“一周”、“一个月”、“三个月”）
    private boolean isSelectButton0Red = false; // 筛选按钮0是否是红色
    private boolean isSelectButton1Red = false; // 筛选按钮1是否是红色
    private boolean isSelectButton2Red = false; // 筛选按钮2是否是红色
    private boolean isSelectButton3Red = false; // 筛选按钮2是否是红色
    private boolean isSelectButton4Red = false; // 筛选按钮2是否是红色

    //设置保证金结算币种
    String[] selectMoneyTypeData;
    String[] selectMoneyTypeIdData;

    /**
     * 初始化布局
     */
    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        rootView = mInflater.inflate(R.layout.boc_fragment_trans_query, null);
        Log.d("TransQueryFragment  +","onCreat");
        return rootView;
    }

    @Override
    public void initView() {
        viewPager = (ViewPager) rootView.findViewById(R.id.view_pager);
        select_title_view = (TitleBarView) rootView.findViewById(R.id.select_title_view);
        fragmentAdapter = new MyFragmentAdapter((BussActivity) getActivity(), data, title);
        viewPager.setAdapter(fragmentAdapter);
        viewPager.setOffscreenPageLimit(6);
        tabIndicator = (TabPageIndicatorWidget) rootView.findViewById(R.id.tab_indicator);
        tabIndicator.addViewPager(viewPager);
        try {
            tabIndicator.setIndicatorColor(R.color.boc_text_color_red);
            tabIndicator.setIndicatorHeight((int) getResources().getDimension(R.dimen.boc_space_between_5px));
        } catch (Exception e) {
            e.printStackTrace();
        }

        // ------------
        mDrawerLayout = (SlipDrawerLayout) rootView.findViewById(R.id.drawer_layout_transfer_query);
        selectTimeRangeNew = (SelectTimeRangeNew) rootView.findViewById(R.id.right_drawer_transfer_query);
        tv_select = (TextView) rootView.findViewById(R.id.tv_select);
        initTitleView();
        // ------------
        View selectTransLayout = View.inflate(mContext, R.layout.boc_view_long_short_select, null);

        sgv_money_type = (SelectGridView) selectTransLayout.findViewById(R.id.sgv_money_type);
        ll_money_type = (LinearLayout) selectTransLayout.findViewById(R.id.ll_money_type);
        llAddToLayout = selectTimeRangeNew.getAddToLayout();
        llAddToTopLayout = selectTimeRangeNew.getAddToTopLayout();

        llAddToLayout.setVisibility(View.VISIBLE);
        llAddToLayout.addView(selectTransLayout);

        llAddToTopLayout.setVisibility(View.GONE);

    }

    @Override
    public void initData() {
        mTransQueryPresenter = new TransQueryPresenter(this);
        showLoadingDialog();
        initQueryDate();
        initSelectParams();
        XpadPsnVFGGetRegCurrencyModel viewModel = new XpadPsnVFGGetRegCurrencyModel();
        mTransQueryPresenter.psnXpadGetRegCurrency(viewModel);

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

    /**
     * 初始化查询的时间
     */
    private void initQueryDate() {
        startLocalDate = ApplicationContext.getInstance().getCurrentSystemDate().toLocalDate().plusMonths(-MAX_QUERY_RANGE).plusDays(1);
        endLocalDate = ApplicationContext.getInstance().getCurrentSystemDate().toLocalDate();
        selectTimeRangeNew.setDefaultDate(startLocalDate.format(DateFormatters.dateFormatter1), endLocalDate.format(DateFormatters.dateFormatter1));
    }
    @Override
    public void setListener() {
//        IntentFilter intentFilter = new IntentFilter("longshortQuery");
//        myReceiver = new MyReceiver();
//        mActivity.registerReceiver(myReceiver, intentFilter);
        //筛选按钮
        tv_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.toggle();

                resetPageSelectConditionShow(currentPagePosition);
            }
        });
        // 筛选重置按钮的监听
        selectTimeRangeNew.setResetListener(new SelectTimeRangeNew.ResetClickListener() {
            @Override
            public void resetClick() {
                resetMoneyTypeSelected();
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
                intent.setAction("longshortQuery");
                intent.putExtra("noticeType", NOTICE_TYPE_PAGE_SELECT); // 0表示“页面选中”通知（共3种通知类型，页面选中通知，筛选条件选择通知，撤单成功通知。这3种都有可能进行重新查询操作）
                intent.putExtra("selectPagePosition", currentPagePosition);
                intent.putExtra("settleCurrency",settleCurrency);
                intent.putExtra("BroadcastSource", "TransQueryFragment");
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
                currentMoneyName = selectMoneyType.getName();
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
                if (PublicUtils.judgeChoiceDateRange(start, end, MAX_QUERY_RANGE, TransQueryFragment.this)) {
                    mDrawerLayout.toggle();

                    startLocalDate = start;
                    endLocalDate = end;

                    if(0==currentPagePosition){//成交状况
                        mSelectParams0.setStartDate(startTime);
                        mSelectParams0.setEndDate(endTime);
                        mSelectParams0.setCurrency(currentMoneyType);
                        // 保存日期的位置
                        dateSelectedPosition0 = selectTimeRangeNew.getCurPosition();
                        isSelectButton0Red = true;
                    }else if(1==currentPagePosition){//未平仓交易
                        mSelectParams1.setCurrency(currentMoneyType);
                        // 保存日期的位置
                        dateSelectedPosition1 = selectTimeRangeNew.getCurPosition();
                        isSelectButton1Red = true;
                    }else if(2==currentPagePosition){//有效委托
                        mSelectParams2.setCurrency(currentMoneyType);
                        // 保存日期的位置
                        dateSelectedPosition2 = selectTimeRangeNew.getCurPosition();
                        isSelectButton2Red = true;
                    }else if(3==currentPagePosition){//历史委托
                        mSelectParams3.setStartDate(startTime);
                        mSelectParams3.setEndDate(endTime);
                        mSelectParams3.setCurrency(currentMoneyType);
                        // 保存日期的位置
                        dateSelectedPosition3 = selectTimeRangeNew.getCurPosition();
                        isSelectButton3Red = true;
                    }else if(4==currentPagePosition){//斩仓交易
                        mSelectParams4.setStartDate(startTime);
                        mSelectParams4.setEndDate(endTime);
                        mSelectParams4.setCurrency(currentMoneyType);
                        // 保存日期的位置
                        dateSelectedPosition4 = selectTimeRangeNew.getCurPosition();
                        isSelectButton4Red = true;
                    }

                    // 此处填充页面查询操作的请求参数
                    SelectParams selectParams = new SelectParams();
                    selectParams.setStartDate(startTime);
                    selectParams.setEndDate(endTime);
                    selectParams.setCurrency(getCurrencyType(currentMoneyType));
//                    selectParams.setCurrency(getCurrencyName(currentMoneyName));
//                    showToast("筛选成功");
                    settleCurrency = getCurrencyName(currentMoneyName);

                    // 发出“开始查询的广播”广播
                    Intent intent = new Intent();
                    intent.setAction("longshortQuery");
                    intent.putExtra("selectParams", selectParams);
                    intent.putExtra("settleCurrency", settleCurrency);
                    intent.putExtra("noticeType", NOTICE_TYPE_CONDITION_SELECT); // 1表示“筛选条件选择”通知
                    intent.putExtra("selectPagePosition", currentPagePosition);
                    intent.putExtra("BroadcastSource", "TransQueryFragment");
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
        if (0 == position){//成交状况
            llAddToTopLayout.setVisibility(View.GONE);
            selectTimeRangeNew.getTimeLayout().setVisibility(View.VISIBLE);
            ll_money_type.setVisibility(View.VISIBLE);

            // 控制币种的显示
            resetPageSelectMoneyTypeSelected(position);

            // 控制时间一栏的显示
            selectTimeRangeNew.setDefaultDate(dateSelectedPosition0, mSelectParams0.getStartDate(), mSelectParams0.getEndDate());

            // 控制筛选按钮是否红色
            setButtonRed(isSelectButton0Red);
        }else if (1 == position){//未平仓交易
            llAddToTopLayout.setVisibility(View.GONE);
            selectTimeRangeNew.getTimeLayout().setVisibility(View.GONE);
            ll_money_type.setVisibility(View.VISIBLE);

            // 控制币种的显示
            resetPageSelectMoneyTypeSelected(position);
            // 控制筛选按钮是否红色
            setButtonRed(isSelectButton1Red);
        }else if (2 == position) {//有效委托
            llAddToTopLayout.setVisibility(View.GONE);
            selectTimeRangeNew.getTimeLayout().setVisibility(View.GONE);
            ll_money_type.setVisibility(View.VISIBLE);

            // 控制币种的显示
            resetPageSelectMoneyTypeSelected(position);
            // 控制筛选按钮是否红色
            setButtonRed(isSelectButton2Red);
        }else if (3 == position) {//历史委托
            llAddToTopLayout.setVisibility(View.GONE);
            selectTimeRangeNew.getTimeLayout().setVisibility(View.VISIBLE);
            ll_money_type.setVisibility(View.VISIBLE);

            // 控制币种的显示
            resetPageSelectMoneyTypeSelected(position);

            // 控制时间一栏的显示
            selectTimeRangeNew.setDefaultDate(dateSelectedPosition3, mSelectParams3.getStartDate(), mSelectParams3.getEndDate());

            // 控制筛选按钮是否红色
            setButtonRed(isSelectButton3Red);
        }else if (4 == position) {//斩仓交易
            llAddToTopLayout.setVisibility(View.GONE);
            selectTimeRangeNew.getTimeLayout().setVisibility(View.VISIBLE);
            ll_money_type.setVisibility(View.VISIBLE);

            // 控制币种的显示
            resetPageSelectMoneyTypeSelected(position);

            // 控制时间一栏的显示
            selectTimeRangeNew.setDefaultDate(dateSelectedPosition4, mSelectParams4.getStartDate(), mSelectParams4.getEndDate());
            // 控制筛选按钮是否红色
            setButtonRed(isSelectButton4Red);
        }
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
        currentMoneyType = "0";
    }

    /**
     * （页面选中时）重置币种的选中
     */
    private void resetPageSelectMoneyTypeSelected(int position) {
        SelectParams selectParams = null;
        if (0 == position){//成交状况
            selectParams = mSelectParams0;
        }else if (1 == position){//未平仓交易
            selectParams = mSelectParams1;
        }else if (2 == position){//有效委托
            selectParams = mSelectParams2;
        }else if (3 == position){//历史委托
            selectParams = mSelectParams3;
        }else if (4 == position){//斩仓交易
            selectParams = mSelectParams4;
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
     * 初始化选择的参数
     */
    private void initSelectParams() {
        //成交状况
        mSelectParams0 = new SelectParams();
        mSelectParams0.setCurrency("0");
        mSelectParams0.setStartDate(ApplicationContext.getInstance().getCurrentSystemDate().toLocalDate().minusMonths(3).plusDays(1).format(DateFormatters.dateFormatter1));
        mSelectParams0.setEndDate(ApplicationContext.getInstance().getCurrentSystemDate().toLocalDate().format(DateFormatters.dateFormatter1));
        //未平仓交易
        mSelectParams1 = new SelectParams();
        mSelectParams1.setCurrency("0");
        //有效委托
        mSelectParams2 = new SelectParams();
        mSelectParams2.setCurrency("0");
        //历史委托
        mSelectParams3 = new SelectParams();
        mSelectParams3.setCurrency("0");
        mSelectParams3.setStartDate(ApplicationContext.getInstance().getCurrentSystemDate().toLocalDate().minusMonths(3).plusDays(1).format(DateFormatters.dateFormatter1));
        mSelectParams3.setEndDate(ApplicationContext.getInstance().getCurrentSystemDate().toLocalDate().format(DateFormatters.dateFormatter1));
        //斩仓交易
        mSelectParams4 = new SelectParams();
        mSelectParams4.setCurrency("0");
        mSelectParams4.setStartDate(ApplicationContext.getInstance().getCurrentSystemDate().toLocalDate().minusMonths(3).plusDays(1).format(DateFormatters.dateFormatter1));
        mSelectParams4.setEndDate(ApplicationContext.getInstance().getCurrentSystemDate().toLocalDate().format(DateFormatters.dateFormatter1));
    }

    // 初始化 币种 筛选数据
    private void initMoneyTypeSelectView() {

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

    // 根据typeId获取货币类型
    private String getCurrencyType(String typeId) {
        String currencyType = null;
        // 000-全部、001-人民币元、 014-美元、012-英镑、 013：港币 028: 加拿大元 029：澳元 038：欧元 027：日元）
        if ("0".equals(typeId)) {
            currencyType = "001";
        } else if ("1".equals(typeId)) {
            currencyType = "014";
        } else if ("2".equals(typeId)) {
            currencyType = "038";
        } else if ("3".equals(typeId)) {
            currencyType = "013";
        } else if ("4".equals(typeId)) {
            currencyType = "027";
        } else if ("5".equals(typeId)) {
            currencyType = "029";
        }

        return currencyType;
    }
    // 根据typeName获取货币类型
    private String getCurrencyName(String typeName) {
        String currencyType = null;
        // 001-人民币元、 014-美元、 013：港币  029：澳元 038：欧元 027：日元）
        if ("人民币元".equals(typeName)) {
            currencyType = "001";
        } else if ("美元".equals(typeName)) {
            currencyType = "014";
        } else if ("欧元".equals(typeName)) {
            currencyType = "038";
        } else if ("港币".equals(typeName)) {
            currencyType = "013";
        } else if ("日元".equals(typeName)) {
            currencyType = "027";
        } else if ("澳大利亚元".equals(typeName)) {
            currencyType = "029";
        }

        return currencyType;
    }
//    class MyReceiver extends BroadcastReceiver {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            int noticeType = intent.getIntExtra("noticeType", -1);
//            switch (noticeType) {
//                case 5: // 0表示“页面选中”通知
//                    viewPager.set;
//                    break;
//            }
//        }
//    }
//    // 处理“页面选中”通知
//    private void handlePageSelectedNotice(Intent intent) {
//        // 如果广播源不是来自TransInquireFragment，直接返回
//        String broadcastSource = intent.getStringExtra("BroadcastSource");
//        if (!"MenuFragment".equals(broadcastSource)) {
//            return;
//        }
//        int selectPagePosition = intent.getIntExtra("selectPagePosition", -1);
////        settleCurrency = intent.getStringExtra("settleCurrency");
//        viewPager.setCurrentItem(selectPagePosition);
//        viewPager.setse
//
//    }

//    /**
//     * 调用此方法跳转到当前页面
//     * @param bussActivity
//     * @param fromWhere
//     */
//    public static TransQueryFragment newinstance(BussActivity bussActivity, int fromWhere) {
//        mFromWhere = fromWhere;
//        TransQueryFragment fragment = bussActivity.findFragment(TransQueryFragment.class);
//        fragment = new TransQueryFragment();
//        fragment.reQuery();
////        if (fragment == null) {
////            fragment = new TransQueryFragment();
////            if (fromWhere != 0) {
////                bussActivity.start(fragment);
////            }
////        } else {
////            bussActivity.popTo(TransQueryFragment.class, false);
////
////            fragment.reQuery();
////            // 发出“开始查询的广播”广播
////            Intent intent = new Intent();
////            intent.setAction("longshortQuery");
////            intent.putExtra("noticeType", 1); // 5表示“刷新”通知
////            intent.putExtra("selectPagePosition", 3);
////            intent.putExtra("BroadcastSource", "TransInquireFragment");
////            bussActivity.sendBroadcast(intent);
////        }
//        if (mFromWhere == 1){
//            // 发出“开始查询的广播”广播
//            Intent intent = new Intent();
//            intent.setAction("longshortQuery");
//            intent.putExtra("noticeType", NOTICE_TYPE_PAGE_SELECT); // 0表示页面选中
//            intent.putExtra("selectPagePosition", 2);
//            intent.putExtra("BroadcastSource", "TransQueryFragment");
//            intent.putExtra("settleCurrency",settleCurrency);
//            intent.putExtra("whichStart",2);
//            bussActivity.sendBroadcast(intent);
//
//        }
//        return fragment;
//    }
    // 重新查询
    public void reQuery() {
        mTransQueryPresenter = new TransQueryPresenter(this);
        XpadPsnVFGGetRegCurrencyModel viewModel = new XpadPsnVFGGetRegCurrencyModel();
        mTransQueryPresenter.psnXpadGetRegCurrency(viewModel);
    }

    @Override
    protected String getTitleValue() {
        return "";
    }

    @Override
    protected boolean getTitleBarRed() {
        return false;
    }

    @Override
    protected boolean isDisplayRightIcon() {
        return false;
    }
    @Override
    protected boolean isHaveTitleBarView() {
        return false;
    }
    @Override
    public boolean onBack() {
        if (mDrawerLayout.isOpen()) {
            mDrawerLayout.toggle();
            return false; // 返回false，表示当前方法处理回退事件
        } else {
            return true; // 返回true，表示当前方法没有处理回退事件
        }
    }

    @Override
    public void onDestroy() {
        mTransQueryPresenter.unsubscribe();
        Log.d("TransQueryFragment  +","onDestory");
        super.onDestroy();

    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d("TransQueryFragment  +","onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("TransQueryFragment  +","onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("TransQueryFragment  +","onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d("TransQueryFragment  +","onStop");
    }

    //保证金账户查询成功
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

        //委托交易查询则跳转到
        mFromWhere = getArguments().getString("fromWhere");
        if ("menu".equals(mFromWhere)){
            viewPager.setCurrentItem(2);
        }

    }

    //保证金账户查询失败
    @Override
    public void psnXpadGetRegCurrencyFail(BiiResultErrorException biiResultErrorException) {

    }

    @Override
    public void setPresenter(TransQueryContract.Presenter presenter) {

    }
}
