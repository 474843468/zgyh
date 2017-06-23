package com.boc.bocsoft.mobile.bocmobile.buss.fund.fixedinvest.fixedpurchase.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.boc.bocop.sdk.util.StringUtil;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;
import com.boc.bocsoft.mobile.bocmobile.base.utils.PublicCodeUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.operation.BaseResultView;
import com.boc.bocsoft.mobile.bocmobile.base.widget.operation.ResultBottom;
import com.boc.bocsoft.mobile.bocmobile.base.widget.operation.ResultHead;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.common.fundguessyoulike.adapter.FundGuessYouLikeAdapter;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.common.fundguessyoulike.bean.FundListBean;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.fundsexchange.model.FundConversionConfirmModel;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.investmanagement.home.model.ValidinvestModel;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.investmanagement.home.ui.InvestConst;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.utils.DataUtils;
import com.boc.bocsoft.mobile.framework.widget.listview.OnItemClickListener;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by taoyongzhen on 2016/12/20.
 */

public class InvestResultFragment extends BussFragment implements OnItemClickListener,ResultBottom.OnClickListener,BaseResultView.HomeBackListener {

    private BaseResultView rootView;
    private FundConversionConfirmModel fragmentModel;
    private FundGuessYouLikeAdapter likeAdapter;

    /**有效定申传值对象*/
    private ValidinvestModel.ListBean mSellData;
    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        rootView = new BaseResultView(mContext);
        return rootView;
    }

    @Override
    public void initView() {
        super.initView();
    }

    @Override
    public void initData() {
        super.initData();
        super.initData();
        Bundle bundle = getArguments();
        if (bundle == null){
            return;
        }
        mSellData = bundle.getParcelable(InvestConst.VALID_DETAIL);
        if (mSellData == null) {
            return;
        }
        displayResultView();
    }

    private void displayResultView(){
        rootView.addStatus(ResultHead.Status.SUCCESS, getString(R.string.boc_fund_fixedinvest_buy_success_hint));
        //交易金额：需要格式化
        String sellAmount = MoneyUtils.transMoneyFormat(mSellData.getApplyAmount(),mSellData.getCurrency());
        String unit = DataUtils.getCurrencyAndCashFlagDes(mContext,mSellData.getCurrency(),mSellData.getCashFlag());
        rootView.addTitle(getString(R.string.boc_fund_fixedinvest_buy_tran_amount_unit,sellAmount,unit));

        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        //交易日期
        String sellDate = mSellData.getSubDate();
        map.put(getString(R.string.boc_fund_fixedinvest_buy_tran_data),sellDate);
        //基金交易账户
        String fundTranAccount = "";
        map.put(getString(R.string.boc_fund_fixedinvest_fund_tran_account),fundTranAccount);
        //资金账户
        String tranAccount = "";
        map.put(getString(R.string.boc_fund_fixedinvest_investaccount),tranAccount);
        //基金信息
        String fundCurrency = PublicCodeUtils.getCurrency(mContext,mSellData.getCurrency());
        String fundInfo = "";
        if (!StringUtil.isNullOrEmpty(fundCurrency)){
            fundCurrency = "[" +fundCurrency + "]";
        }
        fundInfo = fundCurrency + mSellData.getFundName() + " ("+mSellData.getFundCode() + ")";
        map.put(getString(R.string.boc_fund_fixedinvest_fund_info), fundInfo);
        //交易金额
        String tranAmount = MoneyUtils.transMoneyFormat(mSellData.getApplyAmount(),mSellData.getCurrency());;
        map.put(getString(R.string.boc_fund_fixedinvest_buy_tran_amount),tranAmount);
        //结束条件
        String endCondition = "";
        map.put(getString(R.string.boc_fund_fixedinvest_end_condition), endCondition);
        rootView.addDetail(map);

        rootView.addNeedItem(getString(R.string.boc_fund_conversion_to_position), 1);
        rootView.addNeedItem(getString(R.string.boc_fund_conversion_to_transaction_history), 2);
        rootView.addNeedItem(getString(R.string.boc_fund_conversion_to_cancel), 3);

        List<FundListBean> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            FundListBean bean = new FundListBean();
            bean.setFundName("产品" + i);
            bean.setAddUpNetVal((25 + i) + "%");
            bean.setAlwRdptDat((25 + i) + "25+i");
            list.add(bean);
        }

        likeAdapter = new FundGuessYouLikeAdapter(mContext, list);
        likeAdapter.setOnItemClickListener(this);
        rootView.addLikeView(likeAdapter);
    }

    @Override
    public void setListener() {
        super.setListener();
        likeAdapter.setOnItemClickListener(this);
        rootView.setNeedListener(this);
        rootView.setOnHomeBackClick(this);
    }

    @Override
    protected boolean isDisplayRightIcon() {
        return false;
    }

    @Override
    protected String getTitleValue() {
        return getString(R.string.boc_sure_result);
    }

    @Override
    protected boolean getTitleBarRed() {
        return false;
    }

    @Override
    public void onItemClick(View itemView, int position) {

    }

    @Override
    public void onClick(int id) {
        switch (id){
            case 1://持仓

                break;
            case 2://交易记录

                break;
            case 3://撤单

                break;
        }
    }

    @Override
    public void onHomeBack() {

    }
}
