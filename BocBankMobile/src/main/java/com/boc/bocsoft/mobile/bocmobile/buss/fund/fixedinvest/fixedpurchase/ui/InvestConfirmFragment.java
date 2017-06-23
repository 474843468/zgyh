package com.boc.bocsoft.mobile.bocmobile.buss.fund.fixedinvest.fixedpurchase.ui;

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
import com.boc.bocsoft.mobile.bocmobile.buss.fund.fixedinvest.fixedpurchase.model.PsnFundScheduleBuyModel;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.fixedinvest.fixedpurchase.presenter.InvestConfirmPresenter;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.investmanagement.home.model.ValidinvestModel;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.investmanagement.home.ui.InvestConst;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.investmanagement.update.model.FundCompanyModel;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.utils.DataUtils;

import java.util.LinkedHashMap;

/**
 * Created by pactera on 2016/12/20.
 */

public class InvestConfirmFragment extends MvpBussFragment<InvestConfirmPresenter> implements InvestConfirmContract.InvestConfirmView,ConfirmInfoView.OnClickListener{


    private HintMessageDialog hintMessageDialog;
    private ConfirmInfoView confirmInfoView;

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
        mFundcompany = (FundCompanyModel) bundle.getSerializable(InvestConst.FUND_COMPANYTYPE);
        mSellData = (ValidinvestModel.ListBean) bundle.getSerializable(InvestConst.VALID_DETAIL);
        initViewDate();
    }

    private void initViewDate(){
        //交易金额
        String headTile = getString(R.string.boc_fund_fixedinvest_buy_tran_amount);
        String headValue = mSellData.getApplyAmount();
        headValue = MoneyUtils.transMoneyFormat(headValue,mSellData.getCurrency());
        if (StringUtil.isNullOrEmpty(headValue)){
            headValue = "-";
        }
        confirmInfoView.setHeadValue(headTile,headValue);

        LinkedHashMap<String,String> detail = new LinkedHashMap<>();
        //交易日期
        String buyDate = mSellData.getSubDate();
        detail.put(getString(R.string.boc_fund_fixedinvest_buy_tran_data),buyDate);
        //基金
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
        //结束条件
        String endCondition = "";
        detail.put(getString(R.string.boc_fund_fixedinvest_end_condition),endCondition);
        confirmInfoView.addData(detail,false);
    }


    @Override
    public void setListener() {
        super.setListener();
        confirmInfoView.setListener(this);
    }

    @Override
    protected boolean getTitleBarRed() {
        return false;
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
    protected InvestConfirmPresenter initPresenter() {
        return new InvestConfirmPresenter(this);
    }

    @Override
    public void onClickConfirm() {
        fundScheduleBuy("N");
    }

    private void fundScheduleBuy(String affirmFlag){
        showLoadingDialog();
        PsnFundScheduleBuyModel model = new PsnFundScheduleBuyModel();
        model.setDtFlag(mSellData.getDtdsFlag());
        model.setFundPointendDate(mSellData.getEndDate());
        model.setDealCode("");
        model.setEachAmount(mSellData.getApplyAmount());
        model.setEndFlag(mSellData.getEndFlag());
        model.setEndSum(mSellData.getEndSum());
        model.setFundCode(mSellData.getFundCode());
        model.setFundPointendAmount(mSellData.getEndAmt());
        model.setFundSellFlag("");
        model.setAffirmFlag(affirmFlag);
        getPresenter().psnFundScheduleBuy(model);
    }

    @Override
    public void onClickChange() {

    }

    @Override
    public void psnFundScheduleBuySuccess(PsnFundScheduleBuyModel model) {
        closeProgressDialog();
        if (model == null){
            return;
        }
        if (DataUtils.ALTER_FLAG_SUCCESS.equals(model.getTranState())){
            InvestResultFragment fragment = new InvestResultFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable(InvestConst.VALID_DETAIL,mSellData);
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
        //其余情况如何处理？

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
                    fundScheduleBuy("Y");
                }
            });
        }
        if (hintMessageDialog.isShowing() == false){
            hintMessageDialog.showDialog(getString(R.string.boc_fund_fixedinvest_no_match_risk));
        }
    }



    @Override
    public void psnFundScheduleBuyFail(BiiResultErrorException e) {
        closeProgressDialog();
    }

    @Override
    public void setPresenter(InvestConfirmContract.Presenter presenter) {

    }
    //货币型基金特殊处理
    private void initHintInfo(){
        if (mFundcompany == null || StringUtil.isNullOrEmpty(mFundcompany.getCompanyName()) || StringUtil.isNullOrEmpty(mFundcompany.getCompanyPhone())){
            return;
        }
        confirmInfoView.setHint(getString(R.string.boc_fund_conversion_hint_info,mFundcompany.getCompanyName(),mFundcompany.getCompanyPhone()),R.color.boc_text_color_cinerous);
    }
}
