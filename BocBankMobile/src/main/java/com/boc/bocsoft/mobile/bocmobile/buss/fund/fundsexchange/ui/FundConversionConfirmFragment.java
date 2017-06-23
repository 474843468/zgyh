package com.boc.bocsoft.mobile.bocmobile.buss.fund.fundsexchange.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.boc.bocop.sdk.util.StringUtil;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundCompanyInfoQuery.PsnFundCompanyInfoQueryResult;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.MvpBussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.confirmview.ConfirmInfoView;
import com.boc.bocsoft.mobile.bocmobile.base.widget.details.BaseDetailView;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialog.HintMessageDialog;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.fundsexchange.model.FundConversionConfirmModel;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.fundsexchange.presenter.FundConversionConfirmPresenter;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.utils.DataUtils;

import java.util.LinkedHashMap;

/**
 * Created by taoyongzhen on 2016/12/14.
 */

public class FundConversionConfirmFragment extends MvpBussFragment<FundConversionConfirmContract.Presenter> implements FundConversionConfirmContract.ConfirmConversionView,ConfirmInfoView.OnClickListener{

    private View rootView;
    private Button confirmButton;
    private BaseDetailView detailView;
    private FundConversionConfirmModel fragmentModel;
    private TextView tvfundHint;
    private ConfirmInfoView confirmInfoView;
    private HintMessageDialog riskHintMessageDialog;
    private HintMessageDialog nightHintMessageDialog;


    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        rootView = (View) mInflater.inflate(R.layout.boc_fragment_fund_conversion_confirm,null);
        return rootView;
    }

    @Override
    public void initView() {
        super.initView();
//        confirmButton = (Button) rootView.findViewById(R.id.btn_sure);
//        detailView = (BaseDetailView) rootView.findViewById(R.id.details_view);
//        tvfundHint = (TextView)rootView.findViewById(R.id.tv_fund_hint);
        confirmInfoView = (ConfirmInfoView) rootView.findViewById(R.id.confirm_view);
    }

    @Override
    public void initData() {
        super.initData();
        Bundle bundle = getArguments();
        if (bundle == null){
            return;
        }
        fragmentModel = bundle.getParcelable(DataUtils.FUND_CONVERSION_RESULT_KEY);
        initDatailViewDate();
        queryCompanyInfo(fragmentModel.getFromFundCode());
    }

    private void initDatailViewDate(){
        String headTile = getString(R.string.boc_fund_from_amount) + "（" +getString(R.string.boc_fund_bonus_unit) + "）";
        String headValue = MoneyUtils.transMoneyFormat(fragmentModel.getAmount(),fragmentModel.getFromFundCurrency());
        if (StringUtil.isNullOrEmpty(headValue)){
            headValue = "-";
        }
        confirmInfoView.setHeadValue(headTile,headValue);
        LinkedHashMap<String,String> detail = new LinkedHashMap<>();
        //
        String fromFund = fragmentModel.getFromFundName() +" "+ fragmentModel.getFromFundCode();
        detail.put(getString(R.string.boc_fund_conversion_from_title),fromFund);

        String toFund = fragmentModel.getToFundName() +" "+ fragmentModel.getToFundCode();
        detail.put(getString(R.string.boc_fund_conversion_to_title),toFund);

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
        detail.put(getString(R.string.boc_fund_conversion_sell_format_hint),sellFlag);
        confirmInfoView.addData(detail,false);
    }

    @Override
    public void setListener() {
        super.setListener();
        confirmInfoView.setListener(this);
    }


    @Override
    protected String getTitleValue() {
        return getString(R.string.boc_fund_conversion_sure_title);
    }

    @Override
    protected boolean isDisplayRightIcon() {
        return false;
    }

    @Override
    protected boolean getTitleBarRed() {
        return false;
    }

//    @Override
//    public void onClick(View v) {
//        if (v.getId() == R.id.btn_sure){
//            getPresenter().confirmConversion(fragmentModel);
//        }
//    }


    @Override
    protected FundConversionConfirmContract.Presenter initPresenter() {
        return new FundConversionConfirmPresenter(this);
    }

    @Override
    public void confirmConversionFail(BiiResultErrorException e) {
        closeProgressDialog();
    }

    @Override
    public void confirmConversionSuccess(FundConversionConfirmModel model) {
       dealConversionsuccess(model);
    }

    @Override
    public void confirmNightConversionFail(BiiResultErrorException e) {
        closeProgressDialog();
    }

    @Override
    public void confirmNightConversionSuccess(FundConversionConfirmModel model) {
        closeProgressDialog();
        if (model == null){
            return;
        }
        //提交成功，跳转结果
        if (DataUtils.ALTER_FLAG_SUCCESS.equals(model.getTranState())){
            FundConversionResultFragment fragment = new FundConversionResultFragment();
            Bundle bundle = new Bundle();
            bundle.putParcelable(DataUtils.FUND_CONVERSION_RESULT_KEY,fragmentModel);
            fragment.setArguments(bundle);
            start(fragment);
            return;
        }
        //未做风险评估
        if (DataUtils.NO_RISK_EVALUATION.equals(model.getTranState())){
            return;
        }
        //风险等级不匹配
        if (DataUtils.NO_MATCH_RISK_EVALUATION.equals(model.getTranState())){
            showRiskHintDialog();
            return;
        }

    }

    @Override
    public void queryFundCompanyInfoFail(BiiResultErrorException e) {
        closeProgressDialog();
//        tvfundHint.setVisibility(View.GONE);
    }

    @Override
    public void queryFundCompanyInfoSuccess(PsnFundCompanyInfoQueryResult result) {
        closeProgressDialog();
        if (result == null || StringUtil.isNullOrEmpty(result.getCompanyName()) || StringUtil.isNullOrEmpty(result.getCompanyPhone())){
            return;
        }
        confirmInfoView.setHint(getString(R.string.boc_fund_conversion_hint_info,result.getCompanyName(),result.getCompanyPhone()),R.color.boc_text_color_cinerous);
    }



    private void queryCompanyInfo(String fundCode){
        showLoadingDialog();
        if (StringUtil.isNullOrEmpty(fundCode)){
            return;
        }
        getPresenter().queryFundCompanyInfo(fundCode);

    }

    private void dealConversionsuccess(FundConversionConfirmModel model){
        closeProgressDialog();
        if (model == null){
            return;
        }
        //提交成功，跳转结果
        if (DataUtils.ALTER_FLAG_SUCCESS.equals(model.getTranState())){
            FundConversionResultFragment fragment = new FundConversionResultFragment();
            Bundle bundle = new Bundle();
            bundle.putParcelable(DataUtils.FUND_CONVERSION_RESULT_KEY,fragmentModel);
            fragment.setArguments(bundle);
            start(fragment);
            return;
        }
        //未做风险评估
        if (DataUtils.NO_RISK_EVALUATION.equals(model.getTranState())){
            return;
        }
        //风险等级不匹配
        if (DataUtils.NO_MATCH_RISK_EVALUATION.equals(model.getTranState())){
            showRiskHintDialog();
            return;
        }

        if (DataUtils.ALTER_FLAG_NIGHT.equals(model.getTranState())){
            showNightHintDialog();
            return;
        }

    }

    @Override
    public void onClickConfirm() {
        getPresenter().confirmConversion(fragmentModel);

//        FundConversionResultFragment fragment = new FundConversionResultFragment();
//        Bundle bundle = new Bundle();
//        bundle.putParcelable(DataUtils.FUND_CONVERSION_RESULT_KEY,fragmentModel);
//        fragment.setArguments(bundle);
//        start(fragment);
    }

    @Override
    public void onClickChange() {

    }

    @Override
    public void setPresenter(FundConversionConfirmContract.Presenter presenter) {

    }

    private void showRiskHintDialog(){
        if (riskHintMessageDialog == null){
            riskHintMessageDialog = new HintMessageDialog(mContext);
            riskHintMessageDialog.setLeftButtonClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    riskHintMessageDialog.dismiss();
                }
            });
            riskHintMessageDialog.setRightButtonClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    fragmentModel.setAffirmFlag("Y");
                    getPresenter().confirmConversion(fragmentModel);
                    riskHintMessageDialog.dismiss();
                }
            });
        }
        riskHintMessageDialog.showDialog(getString(R.string.boc_fund_fixedinvest_no_match_risk));
    }

    private void showNightHintDialog(){
        if (nightHintMessageDialog == null){
            nightHintMessageDialog = new HintMessageDialog(mContext);
            nightHintMessageDialog.setLeftButtonClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    nightHintMessageDialog.dismiss();
                }
            });
            nightHintMessageDialog.setRightButtonClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getPresenter().confirmNightConversion(fragmentModel);
                    nightHintMessageDialog.dismiss();
                }
            });
        }
        nightHintMessageDialog.showDialog(getString(R.string.boc_fund_fixedinvest_no_match_risk));
    }
}
