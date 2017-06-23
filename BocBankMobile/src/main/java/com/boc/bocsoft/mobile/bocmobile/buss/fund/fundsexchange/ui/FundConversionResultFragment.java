package com.boc.bocsoft.mobile.bocmobile.buss.fund.fundsexchange.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.boc.bocop.sdk.util.StringUtil;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.activity.MvpBussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.widget.buyprocedure.BuyProcedureWidget;
import com.boc.bocsoft.mobile.bocmobile.base.widget.operation.BaseResultView;
import com.boc.bocsoft.mobile.bocmobile.base.widget.operation.ResultBottom;
import com.boc.bocsoft.mobile.bocmobile.base.widget.operation.ResultHead;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.common.fundguessyoulike.adapter.FundGuessYouLikeAdapter;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.common.fundguessyoulike.bean.FundListBean;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.fundsexchange.model.FundConversionConfirmModel;

import com.boc.bocsoft.mobile.bocmobile.buss.fund.utils.DataUtils;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.base.adapter.LikeAdapter;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.wealthproduct.model.WealthListBean;
import com.boc.bocsoft.mobile.framework.widget.listview.OnItemClickListener;
import com.boc.device.key.WDBroadcaseReceiver;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by taoyongzhen on 2016/12/14.
 */

public class FundConversionResultFragment extends BussFragment implements OnItemClickListener,ResultBottom.OnClickListener,BaseResultView.HomeBackListener {
    private BaseResultView rootView;
    private FundConversionConfirmModel fragmentModel;
    private FundGuessYouLikeAdapter likeAdapter;

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
        Bundle bundle = getArguments();
        if (bundle == null){
            return;
        }
        fragmentModel = bundle.getParcelable(DataUtils.FUND_CONVERSION_RESULT_KEY);
        displayResultView();
    }

    private void displayResultView(){
        rootView.addStatus(ResultHead.Status.SUCCESS, getString(R.string.boc_fund_conversion_sucess_hint));
        //需要格式化
        String sellAmount = fragmentModel.getAmount();
        rootView.addTitle(getString(R.string.boc_fund_conversion_from_amount,sellAmount));


        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        String fromFundCurrency = DataUtils.getCurrencyDesWithoutRMB(mContext,fragmentModel.getFromFundCurrency());
        String fromFundInfo = "";
        if (!StringUtil.isNullOrEmpty(fromFundCurrency)){
            fromFundCurrency = "[" +fromFundCurrency + "]";
        }
        fromFundInfo = fromFundCurrency + fragmentModel.getFromFundName() + " ("+fragmentModel.getFromFundCode() + ")";
        map.put(getString(R.string.boc_fund_conversion_from_title), fromFundInfo);

        String toFundInfo = fragmentModel.getToFundName() + " ("+fragmentModel.getToFundCode() + ")";
        map.put(getString(R.string.boc_fund_conversion_to_title), toFundInfo);

        map.put(getString(R.string.boc_fund_conversion_sell_amount),sellAmount);

        String[] sellType = getResources().getStringArray(R.array.boc_fund_sell_other_flag);
        String indexStr = fragmentModel.getSellFlag();
        String sellFlag = "";
        try {
            int index = Integer.valueOf(indexStr);
            if (index >= 0 && index < sellType.length){
                sellFlag = sellType[index];
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        map.put(getString(R.string.boc_fund_conversion_sell_format_hint),sellFlag);
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
    protected String getTitleValue() {
        return getString(R.string.boc_sure_result);
    }

    @Override
    protected boolean isDisplayRightIcon() {
        return false;
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
