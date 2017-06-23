package com.boc.bocsoft.mobile.bocmobile.buss.fund.fixedinvest.fixedredeem.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.boc.bocop.sdk.util.StringUtil;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.MvpBussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;
import com.boc.bocsoft.mobile.bocmobile.base.utils.PublicCodeUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.confirmview.ConfirmInfoView;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialog.HintMessageDialog;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.fixedinvest.fixedpurchase.ui.InvestResultFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.fixedinvest.fixedredeem.model.PsnFundScheduledSellModel;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.fixedinvest.fixedredeem.presenter.RedeemConfirmPresenter;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.investmanagement.home.model.ValidinvestModel;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.investmanagement.home.ui.InvestConst;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.investmanagement.update.model.FundCompanyModel;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.utils.DataUtils;

import java.util.LinkedHashMap;

/**
 * Created by taoyongzhen on 2016/12/19.
 * 订赎确认页
 */

public class RedeemConfirmFragment extends MvpBussFragment<RedeemConfirmContract.Presenter> implements RedeemConfirmContract.RedeemConfirmView,ConfirmInfoView.OnClickListener{

    private HintMessageDialog hintMessageDialog;
    private ConfirmInfoView confirmInfoView;
    private String investAccount = "-";//基金交易账户
    private String account = "-";//资金账户
    private String fundcode;
    /**有效定申传值对象*/
    private ValidinvestModel.ListBean mSellData;
    /**基金公司信息*/
    private FundCompanyModel mFundcompany;
    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        confirmInfoView = new ConfirmInfoView(mContext);
        return confirmInfoView;
    }

    @Override
    public void initData() {
        super.initData();
        Bundle bundle = getArguments();
        if (bundle == null){
            return;
        }
        mFundcompany = (FundCompanyModel) bundle.getSerializable(InvestConst.FUND_COMPANYTYPE);
        mSellData = (ValidinvestModel.ListBean) bundle.getSerializable(InvestConst.VALID_DETAIL);
        initViewDate();
        initHintInfo();
    }

    @Override
    public void setListener() {
        super.setListener();
        confirmInfoView.setListener(this);
    }


    @Override
    protected RedeemConfirmPresenter initPresenter() {
        return new RedeemConfirmPresenter(this);
    }

    @Override
    public void psnFundScheduledSellSuccess(PsnFundScheduledSellModel model) {
        closeProgressDialog();
        if (model == null){
            return;
        }
        if (DataUtils.ALTER_FLAG_SUCCESS.equals(model.getTranState())){
            InvestResultFragment fragment = new InvestResultFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable(InvestConst.VALID_DETAIL, mSellData);
            fragment.setArguments(bundle);
            start(fragment);
            return;
        }
        //未进行风险评估
        if (DataUtils.NO_RISK_EVALUATION.equals(model.getTranState())){
            return;
        }
        //风险评估不匹配
        if (DataUtils.NO_MATCH_RISK_EVALUATION.equals(model.getTranState())){
            dealNoMatchRisk();
            return;
        }
    }

    @Override
    public void psnFundScheduledSellFail(BiiResultErrorException e) {
        closeProgressDialog();
    }

    private void initViewDate(){
        if (mSellData == null){
            return;
        }
        String headTile = getString(R.string.boc_fund_fixedinvest_sell_tran_amount);
        String headValue = MoneyUtils.transMoneyFormat(mSellData.getApplyAmount(),mSellData.getCurrency());
        if (StringUtil.isNullOrEmpty(headValue)){
            headValue = "-";
        }
        confirmInfoView.setHeadValue(headTile,headValue);
        LinkedHashMap<String,String> detail = new LinkedHashMap<>();
        //订赎日期
        String sellDate = mSellData.getSubDate();
        detail.put(getString(R.string.boc_fund_fixedinvest_sell_tran_data),sellDate);
        //基金信息
        String fundCurrency = PublicCodeUtils.getCurrency(mContext,mSellData.getCurrency());
        if (StringUtil.isNullOrEmpty(fundCurrency) == false){
            fundCurrency = "[" + fundCurrency + "]";
        }
        String findInfo = fundCurrency + mSellData.getFundName() + " "+ mSellData.getFundCode();
        detail.put(getString(R.string.boc_fund_fixedinvest_fund_info),findInfo);
        //基金交易账号
        String fundTranAccount = "";
        detail.put(getString(R.string.boc_fund_fixedinvest_fund_tran_account),fundTranAccount);
        //资金账号
        String tranAccount = "";
        detail.put(getString(R.string.boc_fund_fixedinvest_investaccount),tranAccount);
        //赎回方式

        String[] sellType = getResources().getStringArray(R.array.boc_fund_sell_other_flag);
        //
        String indexStr = mSellData.getSellFlag();
        String sellFlag = "";
        try {
            int index = Integer.valueOf(indexStr);
            if (index >= 0 && index < sellType.length){
                sellFlag = sellType[index];
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        detail.put(getString(R.string.boc_fund_conversion_sell_format_hint),sellFlag);
        //结束条件
        String endCondition = mSellData.getEndFlag();
        detail.put(getString(R.string.boc_fund_fixedinvest_end_condition),endCondition);
        confirmInfoView.addData(detail,false);

    }

    private void initHintInfo(){
        if (mFundcompany == null || StringUtil.isNullOrEmpty(mFundcompany.getCompanyName()) || StringUtil.isNullOrEmpty(mFundcompany.getCompanyPhone())){
            return;
        }
        confirmInfoView.setHint(getString(R.string.boc_fund_conversion_hint_info,mFundcompany.getCompanyName(),mFundcompany.getCompanyPhone()),R.color.boc_text_color_cinerous);
    }

    @Override
    public void setPresenter(RedeemConfirmContract.Presenter presenter) {

    }

    @Override
    protected String getTitleValue() {
        return getString(R.string.boc_fund_fixedinvest_sure);
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
    public void onClickConfirm() {
        submitRedeemResult("N");
    }

    private void submitRedeemResult(String affirmFlag) {
        showLoadingDialog();
        PsnFundScheduledSellModel model = new PsnFundScheduledSellModel();
        model.setDtFlag(mSellData.getDtdsFlag());
        model.setFundPointendDate(mSellData.getEndDate());
        model.setEndSum(mSellData.getEndSum());
        model.setFundPointendAmount(mSellData.getEndAmt());
        //赎回份额
        model.setEachAmount(mSellData.getApplyAmount());
        model.setFeetype("");
        model.setFundSellFlag(mSellData.getSellFlag());
        model.setDealCode("");
        model.setFundCode(mSellData.getFundCode());
        model.setAffirmFlag(affirmFlag);
        getPresenter().psnFundScheduledSell(model);
    }




    @Override
    public void onClickChange() {

    }

    private void dealNoMatchRisk(){
        if (hintMessageDialog == null){
            hintMessageDialog = new HintMessageDialog(mContext);
            hintMessageDialog.setLeftButtonClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    hintMessageDialog.dismiss();
                }
            });
            hintMessageDialog.setRightButtonClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                submitRedeemResult("Y");
                }
            });
        }
        if (hintMessageDialog.isShowing() == false){
            hintMessageDialog.showDialog(getString(R.string.boc_fund_fixedinvest_no_match_risk));
        }
    }
}
