package com.boc.bocsoft.mobile.bocmobile.buss.fund.common.floatingprofitlosscalc.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.ApplicationContext;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.MvpBussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.utils.DateTimePicker;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.SlipMenu.SlipDrawerLayout;
import com.boc.bocsoft.mobile.bocmobile.base.widget.selectview.selecttimerangenew.SelectTimeRangeNew;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.common.floatingprofitlosscalc.model.FundFloatingProfileLossModel;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.common.floatingprofitlosscalc.presenter.FundFloatingProfileLossPresenter;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.common.floatingprofitlosscalc.ui.adapter.FundFloatingProfileLossSingleAdapter;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.common.floatingprofitlosscalc.ui.bean.FundFloatingProfileLossBean;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.common.floatingprofitlosscalc.ui.bean.FundFloatingProfileLossSingleBean;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.utils.DataUtils;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.utils.PublicUtils;
import com.boc.bocsoft.mobile.common.utils.LoggerUtils;
import com.boc.bocsoft.mobile.common.utils.date.DateFormatters;
import com.boc.bocsoft.mobile.framework.ui.BasePresenter;
import com.boc.bocsoft.mobile.framework.widget.TitleBarView;

import org.threeten.bp.LocalDate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

/**
 * Created by taoyongzhen on 2016/12/23.
 * 从列表页进入浮动盈亏单条详情，不支持支持侧滑；
 * 从持仓进入浮动盈亏单条详情，支持侧滑
 */

public class FundFloatProfileLossSingleDetailFragment extends MvpBussFragment<FundFloatingProfileLossConst.Prrsenter> implements FundFloatingProfileLossConst.queryFloatingProfitLossView, View.OnClickListener
        , SelectTimeRangeNew.ClickListener {

    private View rootView;
    private SlipDrawerLayout mDrawerLayout;//侧滑菜单
    private LinearLayout llyHeader; //头部布局
    private SelectTimeRangeNew rightDrawer;//侧滑菜单
    private TitleBarView queryTitleView;
    //右侧提示语
    private TextView titleRightText;

    //筛选处的范围
    private TextView tvTransferRecordRange;

    //筛选
    private TextView tvTransferRecordSelect;
    //筛选的imageView
    private ImageView ivTransferRecordSelect;

    //起始时间
    private LocalDate startLocalDate;
    //结束时间
    private LocalDate endLocalDate;
    //最大查询范围为3个月
    private final static int MAX_QUERY_RANGE = 12;
    //最大查询起始日期为一年
    private final static int MAX_QUERY_DATE = 12*3;
    private String timeRangeHint;

    private ListView listView;

    private FundFloatingProfileLossModel.ResultListBean resultBean;
    private FundFloatingProfileLossSingleAdapter adapter;

    private LinearLayout llNoDate;
    private TextView tvNodata; //无数据提示
    //
    private View footView;
    // haed view
    private View llHead;
    private TextView timeView;
    private TextView unitView;
    private TextView amountView;

    private String fundCode;
    private boolean isCanTimeSelected = true;


    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        rootView = mInflater.inflate(R.layout.boc_fragment_fund_floatprofileloss_single_detail, null);
        return rootView;
    }

    @Override
    public void initView() {
        super.initView();
        mDrawerLayout = (SlipDrawerLayout) rootView.findViewById(R.id.layoutSlipDrawer);
        llyHeader = (LinearLayout) rootView.findViewById(R.id.llyHeader);
        llNoDate = (LinearLayout) rootView.findViewById(R.id.ll_no_date);
        tvNodata = (TextView) rootView.findViewById(R.id.tv_nodate);
        rightDrawer = (SelectTimeRangeNew) rootView.findViewById(R.id.viewSelectTime);
        rightDrawer.setSingleSelect(false);
        listView = (ListView) rootView.findViewById(R.id.query_view);
        initTitleView();

    }

    /**
     * 初始化Title
     * 近一年显示准则：只有在查询近一年无数据、失败时不显示；其余情况都予以显示
     */
    private void initTitleView() {
        queryTitleView = new TitleBarView(llyHeader.getContext());
        queryTitleView.setStyle(R.style.titlebar_common_white);
        queryTitleView.setTitle(getResources().getString(R.string.boc_fund_profileloss_detail_more_title));
        queryTitleView.setRightImgBtnVisible(false);
        queryTitleView.setLeftButtonOnClickLinster(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                titleLeftIconClick();
            }
        });
        llyHeader.addView(queryTitleView);

    }

    @Override
    public void initData() {
        super.initData();
        Bundle bundle = getArguments();
        boolean isNoDate = (bundle == null || !bundle.containsKey(DataUtils.FUND_FLOAT_PROFILELESS_BEAN_KEY)
                || bundle.getParcelable(DataUtils.FUND_FLOAT_PROFILELESS_BEAN_KEY) == null);
        if (isNoDate == true) {
            listView.setVisibility(View.GONE);
            llNoDate.setVisibility(View.VISIBLE);
            return;
        } else {
            resultBean = bundle.getParcelable(DataUtils.FUND_FLOAT_PROFILELESS_BEAN_KEY);
        }
        if (adapter == null) {
            adapter = new FundFloatingProfileLossSingleAdapter(mContext);
            listView.setAdapter(adapter);
        }
        timeRangeHint = bundle.getString(DataUtils.FUND_PROFILE_LOSS_TIME_HINT,getString(R.string.boc_fund_profile_one_year));
        isCanTimeSelected = bundle.getBoolean(DataUtils.FUND_PROFILE_LOSS_TIME_SELECT_KEY,false);
        startLocalDate = ApplicationContext.getInstance().getCurrentSystemDate().toLocalDate().plusMonths(-MAX_QUERY_RANGE).plusDays(1);
        endLocalDate = ApplicationContext.getInstance().getCurrentSystemDate().toLocalDate();
        rightDrawer.setDefaultDate(startLocalDate.format(DateFormatters.dateFormatter1),
                endLocalDate.format(DateFormatters.dateFormatter1));
        fundCode = resultBean.getFundCode();

        showHeadView();
        showFootView();
        displayViewDate();
    }

    @Override
    public void setListener() {
        super.setListener();
        rightDrawer.setListener(this);
    }

    @Override
    protected boolean isHaveTitleBarView() {
        return false;
    }

    //第一次进入无数据时，无显示头部，搜索后即使无数据时，需要显示头部
    private void showHeadView() {
        if (llHead == null) {
            LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(LAYOUT_INFLATER_SERVICE);
            llHead = inflater.inflate(R.layout.boc_view_fund_floatprofileloss_comm_head, listView, false);
            timeView = (TextView) llHead.findViewById(R.id.tv_time);
            timeView.setText(timeRangeHint);
            timeView.setOnClickListener(this);
            timeView.setClickable(isCanTimeSelected);
            if (isCanTimeSelected){
                timeView.setTextColor(mContext.getResources().getColor(R.color.boc_main_button_color));
            }
            unitView = (TextView) llHead.findViewById(R.id.tv_hint);
            unitView.setText(getString(R.string.boc_fund_profileloss_total_profileloss_unit_hint
                    , DataUtils.getCurrencyAndCashFlagDes(mContext, resultBean.getCurceny(), resultBean.getCashFlag())));
            amountView = (TextView) llHead.findViewById(R.id.tv_amount);
        }
        //依据查询范围，动态更新总盈亏（已实现盈亏+持仓盈亏），无盈亏数据时，
        String totoalAmount = calcaulateToatlProfileLoss();
        amountView.setText(totoalAmount);
        if (listView.getHeaderViewsCount() <= 0){
            listView.addHeaderView(llHead);
        }
        llHead.setVisibility(View.VISIBLE);
    }

    private String calcaulateToatlProfileLoss() {
        if (resultBean == null) {
            return MoneyUtils.transMoneyFormat("0", resultBean.getCurceny());
        }
        try {
            BigDecimal result = new BigDecimal(resultBean.getResultFloat());
            result = result.add(new BigDecimal(resultBean.getEndFloat()));
            return MoneyUtils.transMoneyFormat(result, resultBean.getCurceny());

        } catch (Exception e) {
            LoggerUtils.Error(e.getLocalizedMessage());
        }
        return MoneyUtils.transMoneyFormat("0", resultBean.getCurceny());
    }

    private void showFootView() {
        if (footView == null) {
            footView = View.inflate(mContext, R.layout.boc_view_fund_floatprofileloss_comm_foot, null);
        }
        if (listView.getFooterViewsCount() <= 0) {
            listView.addFooterView(footView);
        }
    }

    /**
     * 数据展示规则：
     * 1. 总盈亏：已实现盈亏(resultFloat)+持仓盈亏(endFloat)
     * 2. 持仓盈亏:endFloat
     * 3. 已实现盈亏:resultFloat
     * 4. 期末市值:endCost
     * 5. 期初市值:startCost
     * 6. 卖出净金额:卖出金额(hsAmount)-手续费用(jyAmount)
     * 7. 买入总金额:买入金额（trAmount）
     */
    private void displayViewDate() {
        List<FundFloatingProfileLossSingleBean> allBeans = new ArrayList<>();
        String currency = resultBean.getCurceny();
        FundFloatingProfileLossSingleBean endFloat = new FundFloatingProfileLossSingleBean();
        endFloat.setTitleContent(getString(R.string.boc_fund_profile_loss_hint_endfloat));
        endFloat.setValueContent(MoneyUtils.transMoneyFormat(resultBean.getEndFloat(), currency));
        allBeans.add(endFloat);

        FundFloatingProfileLossSingleBean resultFloat = new FundFloatingProfileLossSingleBean();
        resultFloat.setTitleContent(getString(R.string.boc_fund_profile_loss_hint_resultfloat));
        resultFloat.setValueContent(MoneyUtils.transMoneyFormat(resultBean.getResultFloat(), currency));
        resultFloat.setShowDownAxis(false);
        allBeans.add(resultFloat);

        FundFloatingProfileLossSingleBean endCost = new FundFloatingProfileLossSingleBean();
        endCost.setTitleContent(getString(R.string.boc_fund_profile_loss_hint_endcost));
        endCost.setValueContent(MoneyUtils.transMoneyFormat(resultBean.getEndCost(), currency));
        endCost.setShowUpAxis(false);
        allBeans.add(endCost);

        FundFloatingProfileLossSingleBean startCost = new FundFloatingProfileLossSingleBean();
        startCost.setTitleContent(getString(R.string.boc_fund_profile_loss_hint_starcost));
        startCost.setValueContent(MoneyUtils.transMoneyFormat(resultBean.getStartcost(), currency));
        allBeans.add(startCost);

        FundFloatingProfileLossSingleBean sellAmount = new FundFloatingProfileLossSingleBean();
        sellAmount.setTitleContent(getString(R.string.boc_fund_profile_loss_hint_hsamount));
        sellAmount.setValueContent(calcaulateSellAmount());
        String hsAmount = MoneyUtils.transMoneyFormat(resultBean.getHsAmount(), currency);
        String jyAmount = MoneyUtils.transMoneyFormat(resultBean.getJyAmount(), currency);
        sellAmount.setDespContent(getString(R.string.boc_fund_profile_loss_hint_des, hsAmount, jyAmount));
        allBeans.add(sellAmount);

        FundFloatingProfileLossSingleBean trAmount = new FundFloatingProfileLossSingleBean();
        trAmount.setTitleContent(getString(R.string.boc_fund_profile_loss_hint_tramount));
        trAmount.setValueContent(MoneyUtils.transMoneyFormat(resultBean.getTrAmount(), currency));
        allBeans.add(trAmount);
        adapter.setBeans(allBeans);
    }


    private String calcaulateSellAmount() {
        if (resultBean == null) {
            return "";
        }
        try {
            BigDecimal result = new BigDecimal(resultBean.getHsAmount());
            result = result.subtract(new BigDecimal(resultBean.getJyAmount()));
            return MoneyUtils.transMoneyFormat(result, resultBean.getCurceny());

        } catch (Exception e) {
            LoggerUtils.Error(e.getLocalizedMessage());
        }
        return "";
    }


    @Override
    protected FundFloatingProfileLossConst.Prrsenter initPresenter() {
        return new FundFloatingProfileLossPresenter(this);
    }

    @Override
    public void queryFloatingProfitLossFail(BiiResultErrorException exception) {
        closeProgressDialog();
        showNodateView();

    }

    @Override
    public void queryFloatingProfitLossSuccess(FundFloatingProfileLossModel model) {
        closeProgressDialog();
        boolean noDate = model == null || model.getResultList() == null
                || model.getResultList().size() <= 0;
        if (noDate) {
            showNodateView();
        } else {
            List<FundFloatingProfileLossBean> beans = new ArrayList<>();
            for (FundFloatingProfileLossModel.ResultListBean modelBean : model.getResultList()) {
                if (fundCode.equals(modelBean.getFundCode())) {
                    resultBean = modelBean;
                    break;
                }
            }

            if (resultBean == null) {
                showNodateView();
                return;
            }
            showHeadView();
            showFootView();
            displayViewDate();

        }
    }

    @Override
    public void setPresenter(BasePresenter presenter) {

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tv_time) {
            mDrawerLayout.toggle();
            return;
        }
    }

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

    }

    @Override
    public void rightClick(boolean haveSelected, String content) {
        String startTime = rightDrawer.getStartDate();
        String endTime = rightDrawer.getEndDate();
        timeView.setText(startTime + "-" +endTime.substring(0,1)+"...");
        startLocalDate = LocalDate.parse(startTime, DateFormatters.dateFormatter1);
        endLocalDate = LocalDate.parse(endTime, DateFormatters.dateFormatter1);
        if (PublicUtils.judgeChoiceDateRange(startLocalDate, endLocalDate, MAX_QUERY_RANGE, FundFloatProfileLossSingleDetailFragment.this)) {
            mDrawerLayout.toggle();
            queryList();
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

    private void queryList() {
        showLoadingDialog();
        FundFloatingProfileLossModel model = new FundFloatingProfileLossModel();
        model.setFundCode(fundCode);
        model.setStartDate(startLocalDate.format(DateFormatters.dateFormatter1));
        model.setEndDate(endLocalDate.format(DateFormatters.dateFormatter1));
        getPresenter().queryFloatingProfitLoss(model);
    }

    //listview 内容为空，显示头部，移除尾部；显示无数据
    private void showNodateView() {
        resultBean = null;
        showHeadView();
        adapter.getBeans().clear();
        if (listView.getFooterViewsCount() > 0 && footView != null) {
            listView.removeFooterView(footView);
        }
        adapter.notifyDataSetChanged();
        llNoDate.setVisibility(View.VISIBLE);

    }


}
