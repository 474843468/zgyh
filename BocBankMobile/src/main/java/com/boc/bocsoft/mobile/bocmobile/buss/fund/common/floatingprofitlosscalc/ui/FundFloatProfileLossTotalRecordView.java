package com.boc.bocsoft.mobile.bocmobile.buss.fund.common.floatingprofitlosscalc.ui;

import android.content.Context;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.container.BussContainer;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.common.floatingprofitlosscalc.model.FundFloatingProfileLossModel;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.common.floatingprofitlosscalc.ui.adapter.FundFloatingProfileLossSingleAdapter;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.common.floatingprofitlosscalc.ui.bean.FundFloatingProfileLossSingleBean;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.utils.DataUtils;
import com.boc.bocsoft.mobile.common.utils.LoggerUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by taoyongzhen on 2016/12/24.
 */

public class FundFloatProfileLossTotalRecordView extends BussContainer{
    private Context mContext;
    private View rootView;
    private ListView listView;

    //
    private View footView;
    // haed view
    private View llHead;
    private TextView timeView;
    private TextView unitView;
    private TextView amountView;
    private FundFloatingProfileLossSingleAdapter adapter;
    private boolean isShowedView = false;
    private String timeHint;

    private FundFloatingProfileLossModel.ResultListBean resultBean;
    public FundFloatProfileLossTotalRecordView(Context context) {
        super(context);
        this.mContext = context;
    }

    @Override
    protected View createContentView() {
        rootView = inflate(getContext(), R.layout.boc_fragment_fund_floatprofileloss_single, null);
        return rootView;
    }


    @Override
    protected void initView() {
        super.initView();
        listView = (ListView) rootView.findViewById(R.id.list_view);
        if (adapter == null){
            adapter = new FundFloatingProfileLossSingleAdapter(mContext);
            listView.setAdapter(adapter);
        }

    }

    @Override
    protected void initData() {
        super.initData();


    }

    @Override
    protected void setListener() {
        super.setListener();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser){
            showView();
        }
    }

    public FundFloatingProfileLossModel.ResultListBean getResultBean() {
        return resultBean;
    }

    public void setResultBean(FundFloatingProfileLossModel.ResultListBean resultBean) {
        this.resultBean = resultBean;
    }

    public String getTimeHint() {
        return timeHint;
    }

    public void setTimeHint(String timeHint) {
        this.timeHint = timeHint;
    }


    public void showView(){
        if (isShowedView == false){
            showHeadView();
            showFootView();
            displayViewDate();
            isShowedView = true;
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
        endFloat.setTitleContent(mContext.getString(R.string.boc_fund_profile_loss_hint_endfloat));
        endFloat.setValueContent(MoneyUtils.transMoneyFormat(resultBean.getEndFloat(), currency));
        allBeans.add(endFloat);

        FundFloatingProfileLossSingleBean resultFloat = new FundFloatingProfileLossSingleBean();
        resultFloat.setTitleContent(mContext.getString(R.string.boc_fund_profile_loss_hint_resultfloat));
        resultFloat.setValueContent(MoneyUtils.transMoneyFormat(resultBean.getResultFloat(), currency));
        resultFloat.setShowDownAxis(false);
        allBeans.add(resultFloat);

        FundFloatingProfileLossSingleBean endCost = new FundFloatingProfileLossSingleBean();
        endCost.setTitleContent(mContext.getString(R.string.boc_fund_profile_loss_hint_endcost));
        endCost.setValueContent(MoneyUtils.transMoneyFormat(resultBean.getEndCost(), currency));
        endCost.setShowUpAxis(false);
        allBeans.add(endCost);

        FundFloatingProfileLossSingleBean startCost = new FundFloatingProfileLossSingleBean();
        startCost.setTitleContent(mContext.getString(R.string.boc_fund_profile_loss_hint_starcost));
        startCost.setValueContent(MoneyUtils.transMoneyFormat(resultBean.getStartcost(), currency));
        allBeans.add(startCost);

        FundFloatingProfileLossSingleBean sellAmount = new FundFloatingProfileLossSingleBean();
        sellAmount.setTitleContent(mContext.getString(R.string.boc_fund_profile_loss_hint_hsamount));
        sellAmount.setValueContent(calcaulateSellAmount());
        String hsAmount = MoneyUtils.transMoneyFormat(resultBean.getHsAmount(), currency);
        String jyAmount = MoneyUtils.transMoneyFormat(resultBean.getJyAmount(), currency);
        sellAmount.setDespContent(mContext.getString(R.string.boc_fund_profile_loss_hint_des, hsAmount, jyAmount));
        allBeans.add(sellAmount);

        FundFloatingProfileLossSingleBean trAmount = new FundFloatingProfileLossSingleBean();
        trAmount.setTitleContent(mContext.getString(R.string.boc_fund_profile_loss_hint_tramount));
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

    //第一次进入无数据时，无显示头部，搜索后即使无数据时，需要显示头部
    private void showHeadView() {
        if (llHead == null) {
            llHead = View.inflate(mContext, R.layout.boc_view_fund_floatprofileloss_comm_head, null);
            timeView = (TextView) llHead.findViewById(R.id.tv_time);
            timeView.setText(timeHint);
            unitView = (TextView) llHead.findViewById(R.id.tv_hint);
            unitView.setText(mContext.getString(R.string.boc_fund_profileloss_total_profileloss_unit_hint
                    , DataUtils.getCurrencyAndCashFlagDes(mContext, resultBean.getCurceny(), resultBean.getCashFlag())));
            amountView = (TextView) llHead.findViewById(R.id.tv_amount);
        }
        //依据查询范围，动态更新总盈亏（已实现盈亏+持仓盈亏），无盈亏数据时，
        String totoalAmount = calcaulateToatlProfileLoss();
        amountView.setText(totoalAmount);
        if (listView.getHeaderViewsCount() <=0){
            listView.addHeaderView(llHead);
        }
        llHead.setVisibility(View.VISIBLE);
    }

    private String calcaulateToatlProfileLoss() {
        if (resultBean == null) {
            return "";
        }
        try {
            BigDecimal result = new BigDecimal(resultBean.getResultFloat());
            result = result.add(new BigDecimal(resultBean.getEndFloat()));
            return MoneyUtils.transMoneyFormat(result, resultBean.getCurceny());

        } catch (Exception e) {
            LoggerUtils.Error(e.getLocalizedMessage());
        }
        return "";
    }

    private void showFootView() {
        if (footView == null) {
            footView = View.inflate(mContext, R.layout.boc_view_fund_floatprofileloss_comm_foot, null);
        }
        if (listView.getFooterViewsCount() <= 0) {
            listView.addFooterView(footView);
        }
    }

}
