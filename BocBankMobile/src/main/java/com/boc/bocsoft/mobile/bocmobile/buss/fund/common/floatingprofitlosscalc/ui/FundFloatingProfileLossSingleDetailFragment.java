package com.boc.bocsoft.mobile.bocmobile.buss.fund.common.floatingprofitlosscalc.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.MvpBussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.common.floatingprofitlosscalc.model.FundFloatingProfileLossModel;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.common.floatingprofitlosscalc.ui.adapter.FundFloatingProfileLossSingleAdapter;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.common.floatingprofitlosscalc.ui.bean.FundFloatingProfileLossSingleBean;
import com.boc.bocsoft.mobile.framework.ui.BasePresenter;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by taoyongzhen on 2016/12/2.
 * 总计详情页 单条详情页  复用同意fragment
 */

public class FundFloatingProfileLossSingleDetailFragment extends MvpBussFragment{

    private ListView listView;
    private View rootView;
    private TextView noDatetextView;
    private View headView;
    private TextView footView;

    private LinearLayout fundInfo;
    private TextView fundName;
    private TextView fundCode;

    private TextView totalHint;
    private TextView totalAccount;

    private TextView leftTop;
    private TextView leftBottom;
    private TextView rightTop;
    private TextView rightBottom;

    private View devideView;

    private FundFloatingProfileLossModel.ResultListBean resultBean;
    private FundFloatingProfileLossSingleAdapter adapter;

    private int detailType = 1;
    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        rootView = mInflater.inflate(R.layout.boc_fragment_fund_floatprofileloss_single,null);
        return rootView;
    }

    @Override
    public void initView() {
        super.initView();
        listView = (ListView) rootView.findViewById(R.id.list_view);
        noDatetextView = (TextView) rootView.findViewById(R.id.tvNoData);
    }

    @Override
    public void initData() {
        super.initData();
        Bundle bundle = getArguments();
        boolean isNoDate = (bundle == null || !bundle.containsKey(FundFloatingProfileLossConstant.FUND_PROFILE_LOSS_BEAN_KEY)
                ||  bundle.getParcelable(FundFloatingProfileLossConstant.FUND_PROFILE_LOSS_BEAN_KEY) == null);
        if (isNoDate == true){
            listView.setVisibility(View.GONE);
            noDatetextView.setText(getString(R.string.boc_fund_profileloss_nodate));
            noDatetextView.setVisibility(View.VISIBLE);
            return;
        }else {
            resultBean = bundle.getParcelable(FundFloatingProfileLossConstant.FUND_PROFILE_LOSS_BEAN_KEY);
        }
        if (adapter == null){
            adapter = new FundFloatingProfileLossSingleAdapter(mContext);
            listView.setAdapter(adapter);
        }
        addHeadView();
        addFootView();
        displayView();

    }

    @Override
    protected boolean getTitleBarRed() {
        return false;
    }

    @Override
    protected String getTitleValue() {
        return getString(R.string.boc_fund_profileloss_detail_title);
    }

    @Override
    protected boolean isDisplayRightIcon() {
        return false;
    }

    @Override
    protected boolean isHaveTitleBarView() {
        if (detailType == FundFloatingProfileLossConstant.FUND_PROFILE_LOSS_DETAIL_TYPE_SINGLE_TOTAL){
            return true;
        }
        if (detailType == FundFloatingProfileLossConstant.FUND_PROFILE_LOSS_DETAIL_TYPE_TOTAL){
            return false;
        }

        return super.isHaveTitleBarView();
    }

    private void displayView(){
        //期初市场 买入总金额 卖出净金额 卖出金额 交易费用 总盈亏 期末市值
        List<FundFloatingProfileLossSingleBean> allBeans = new ArrayList<>();
        FundFloatingProfileLossSingleBean startBean = new FundFloatingProfileLossSingleBean();
        startBean.setType(1);
        startBean.setTitleContent("期初市场");
        startBean.setValueContent(MoneyUtils.transMoneyFormat(resultBean.getStartcost(),resultBean.getCurceny()));
        allBeans.add(startBean);

        FundFloatingProfileLossSingleBean buyAllbean = new FundFloatingProfileLossSingleBean();
        buyAllbean.setType(1);
        buyAllbean.setTitleContent("买入总金额");
        buyAllbean.setValueContent(MoneyUtils.transMoneyFormat(resultBean.getTrAmount(),resultBean.getCurceny()));
        allBeans.add(buyAllbean);

        FundFloatingProfileLossSingleBean sellAllbean = new FundFloatingProfileLossSingleBean();
        sellAllbean.setType(1);
        sellAllbean.setTitleContent("卖出净金额");
        sellAllbean.setValueContent(MoneyUtils.transMoneyFormat(resultBean.getTrAmount(),resultBean.getCurceny()));
        allBeans.add(sellAllbean);

        FundFloatingProfileLossSingleBean sellbean = new FundFloatingProfileLossSingleBean();
        sellbean.setType(2);
        sellbean.setTitleContent("卖出金额");
        sellbean.setValueContent(MoneyUtils.transMoneyFormat(resultBean.getTrAmount(),resultBean.getCurceny()));
        allBeans.add(sellbean);

        FundFloatingProfileLossSingleBean tranbean = new FundFloatingProfileLossSingleBean();
        tranbean.setType(2);
        tranbean.setTitleContent("交易费用");
        tranbean.setValueContent(MoneyUtils.transMoneyFormat(resultBean.getJyAmount(),resultBean.getCurceny()));
        allBeans.add(tranbean);

        FundFloatingProfileLossSingleBean allProfileLossbean = new FundFloatingProfileLossSingleBean();
        allProfileLossbean.setType(2);
        allProfileLossbean.setTitleContent("总盈亏");
        allProfileLossbean.setValueContent(MoneyUtils.transMoneyFormat(resultBean.getEndFloat(),resultBean.getCurceny()));
        allBeans.add(allProfileLossbean);

        FundFloatingProfileLossSingleBean allEndbean = new FundFloatingProfileLossSingleBean();
        allEndbean.setType(1);
        allEndbean.setTitleContent("期末市值");
        allEndbean.setValueContent(MoneyUtils.transMoneyFormat(resultBean.getEndCost(),resultBean.getCurceny()));
        allBeans.add(allEndbean);
        adapter.setBeans(allBeans);

    }

    private void addHeadView(){
        if (headView == null){
            headView = View.inflate(mContext,R.layout.boc_fragment_floatingprofilelosssingletotal_head,null);
            fundInfo = (LinearLayout) headView.findViewById(R.id.fund_info);
            fundName = (TextView) headView.findViewById(R.id.fund_name);
            fundCode = (TextView) headView.findViewById(R.id.fund_code);
            totalHint = (TextView) headView.findViewById(R.id.total_head);
            totalAccount = (TextView)headView.findViewById(R.id.total_account);
            rightTop = (TextView)headView.findViewById(R.id.right_top);
            leftTop = (TextView) headView.findViewById(R.id.left_top);
            rightBottom = (TextView)headView.findViewById(R.id.right_bottom);
            leftBottom = (TextView)headView.findViewById(R.id.left_bottom);
            devideView = headView.findViewById(R.id.divide_line);
        }
        if (listView.getHeaderViewsCount() <= 0){
            listView.addHeaderView(headView);
        }
        if (detailType == FundFloatingProfileLossConstant.FUND_PROFILE_LOSS_DETAIL_TYPE_SINGLE_TOTAL){
            devideView.setVisibility(View.GONE);
            fundName.setText(resultBean.getFundName());
            fundCode.setText(" (" + resultBean.getFundCode() + ")");
        }
        if (detailType == FundFloatingProfileLossConstant.FUND_PROFILE_LOSS_DETAIL_TYPE_TOTAL){
            devideView.setVisibility(View.VISIBLE);
            fundInfo.setVisibility(View.GONE);
        }

        String totolProfileLossHint = getString(R.string.boc_fund_profileloss_total_profileloss_hint) + " ";
        totalHint.setText(totolProfileLossHint);

        String totalAccountContent = MoneyUtils.transMoneyFormat(resultBean.getEndFloat(),resultBean.getCurceny());
        totalAccount.setText(totalAccountContent);

        leftBottom.setText("已实现盈亏");
        rightBottom.setText("持仓盈亏");

    }

    private void addFootView(){
        if (footView == null) {
            footView = new TextView(mContext);
            footView.setPadding(mContext.getResources().getDimensionPixelSize(R.dimen.boc_space_between_30px),
                    mContext.getResources().getDimensionPixelSize(R.dimen.boc_space_between_20px),
                    mContext.getResources().getDimensionPixelSize(R.dimen.boc_space_between_30px),
                    mContext.getResources().getDimensionPixelSize(R.dimen.boc_space_between_20px));
            footView.setTextAppearance(mContext, R.style.tv_small);
            footView.setText(getString(R.string.boc_fund_profileloss_rule));
        }
        if (listView.getFooterViewsCount() <= 0){
            listView.addFooterView(footView);
        }
    }

    public void setDetailType(int detailType) {
        this.detailType = detailType;
    }
    @Override
    public void setListener() {
        super.setListener();
    }

    @Override
    protected BasePresenter initPresenter() {
        return null;
    }
}
