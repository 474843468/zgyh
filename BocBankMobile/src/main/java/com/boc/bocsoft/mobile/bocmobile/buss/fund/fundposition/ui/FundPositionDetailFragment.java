package com.boc.bocsoft.mobile.bocmobile.buss.fund.fundposition.ui;

import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.AbsoluteSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.boc.bocop.sdk.util.StringUtil;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.MvpBussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;
import com.boc.bocsoft.mobile.bocmobile.base.utils.ToastUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.details.DetailContentView;
import com.boc.bocsoft.mobile.bocmobile.base.widget.details.DetailTabRowTextButtonWithHint;
import com.boc.bocsoft.mobile.bocmobile.base.widget.details.DetailThreeTableRowTextButton;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialog.HintMessageDialog;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.common.floatingprofitlosscalc.model.FundFloatingProfileLossModel;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.common.floatingprofitlosscalc.ui.FundFloatProfileLossSingleDetailFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.fundposition.model.FundPositionDetailModel;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.fundposition.model.FundPositionModel;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.fundposition.presenter.FundPositionDetailPresenter;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.fundposition.widget.AlterBonusTypeDialog;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.fundsexchange.ui.FundConversionFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.utils.DataUtils;
import com.boc.bocsoft.mobile.common.utils.LoggerUtils;

import java.math.BigDecimal;

/**
 * Created by taoyongzhen on 2016/12/8.
 */

public class FundPositionDetailFragment extends MvpBussFragment<FundPositionDetailContract.Presenter> implements FundPositionDetailContract.FundBonusDetailView, View.OnClickListener {

    private View rootView;
    private TextView tvCurrentValueHint;
    private TextView tvCurrentValue;
    private TextView tvProfileLossValue;
    private TextView tvProfileLossHint;

    private DetailContentView detailContentView;
    private AlterBonusTypeDialog alterBonusTypeDialog;
    private HintMessageDialog riskHintMessageDialog;
    private HintMessageDialog nightHintMessageDialog;

    private TextView scheduledBuy;
    private TextView scheduledSell;
    private TextView reBuy;

    private FundPositionModel.FundBalanceBean fundBalanceBean;
    private String profileLossValue;
    private DetailTabRowTextButtonWithHint detailTabRowTextButtonWithHint;
    private DetailThreeTableRowTextButton detailThreeTableRowTextButton;
    private FundFloatingProfileLossModel.ResultListBean resultListBean;

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        rootView = mInflater.inflate(R.layout.boc_fragment_fund_position_detail, null);
        return rootView;
    }

    @Override
    public void initView() {
        super.initView();

        tvCurrentValueHint = (TextView) rootView.findViewById(R.id.current_value_hint);
        tvCurrentValue = (TextView) rootView.findViewById(R.id.current_value);
        tvProfileLossValue = (TextView) rootView.findViewById(R.id.float_profile_value);
        tvProfileLossHint = (TextView) rootView.findViewById(R.id.float_profile_hint);
        detailContentView = (DetailContentView) rootView.findViewById(R.id.cv_position);

        scheduledBuy = (TextView) rootView.findViewById(R.id.bt_scheduledbuy);
        scheduledSell = (TextView) rootView.findViewById(R.id.bt_scheduledsell);
        reBuy = (TextView) rootView.findViewById(R.id.bt_rebuy);
        reBuy.setSelected(true);

    }

    @Override
    public void initData() {
        super.initData();
        if (getArguments() == null){
            return;
        }
        fundBalanceBean = (FundPositionModel.FundBalanceBean) getArguments().getSerializable(DataUtils.FUND_POSITION_BEAN_KEY);
        if (fundBalanceBean == null) {
            return;
        }
        resultListBean = getArguments().getParcelable(DataUtils.FUND_FLOAT_PROFILELESS_BEAN_KEY);
        disPlayView();
        initScheduledSell();
    }

    @Override
    public void setListener() {
        super.setListener();
        scheduledBuy.setOnClickListener(this);
        scheduledSell.setOnClickListener(this);
        reBuy.setOnClickListener(this);
        tvCurrentValueHint.setOnClickListener(this);
        tvProfileLossHint.setOnClickListener(this);
    }

    private void initScheduledSell() {
        if (!DataUtils.despToBoolean(fundBalanceBean.getFundInfo().getIsQuickSale())) {
            return;
        }
        String content = "赎回" + "\n可快速赎回(T+0)";
        String nodateHintSub = "可快速赎回(T+0)";
        SpannableStringBuilder style = new SpannableStringBuilder(content);
        style.setSpan(new AbsoluteSizeSpan(13, true), content.length() - nodateHintSub.length(), content.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        scheduledSell.setText(style);
        scheduledSell.setMovementMethod(LinkMovementMethod.getInstance());
    }

    private void disPlayView() {
        //当前市值提示
        String unit = DataUtils.getCurrencyDescByLetter(mContext, fundBalanceBean.getFundInfo().getCurrency());
        tvCurrentValueHint.setText(getString(R.string.boc_fund_position_current_value, unit));
        //当前市值
        String strCurrentValue = MoneyUtils.transMoneyFormat(fundBalanceBean.getCurrentCapitalisation(), fundBalanceBean.getFundInfo().getCurrency());
        tvCurrentValue.setText(strCurrentValue);
        //浮点盈亏
        String stProfileLoss = calcaulateToatlProfileLoss();
        tvProfileLossValue.setText(stProfileLoss);
        //基金信息
        String title = getString(R.string.boc_fund_position_fund);
        String fundName = fundBalanceBean.getFundInfo().getFundName();
        String fundcode = "  (" + fundBalanceBean.getFundInfo().getFundCode() + ")";
        String button = getString(R.string.boc_fund_profileless_fundtran);
        detailThreeTableRowTextButton = detailContentView.addTextAndButtonContent(title, fundName, fundcode, button);
        if (DataUtils.despToBoolean(fundBalanceBean.getFundInfo().getConvertFlag())){
            detailThreeTableRowTextButton.setDetailTvListener(new DetailThreeTableRowTextButton.DetailTvOnClickListener() {
                @Override
                public void onClickTextView() {//基金转换
                    gotoFundConversion();
                }
            });

        }else{
            detailThreeTableRowTextButton.isShowTvButton(false);
        }
        detailThreeTableRowTextButton.setRightTvOnClickListener(new DetailThreeTableRowTextButton.DetailRightTvOnClickListener() {
            @Override
            public void onClickRightTextView() {//基金详情

            }
        });

        //可用份额
        String stFreeQuty = MoneyUtils.transMoneyFormat(fundBalanceBean.getTotalAvailableBalance(), fundBalanceBean.getFundInfo().getCurrency());
        detailContentView.addDetailRow(getString(R.string.boc_fund_position_freequty), stFreeQuty);
        //持有份额
        String stHold = MoneyUtils.transMoneyFormat(fundBalanceBean.getTotalAvailableBalance(), fundBalanceBean.getFundInfo().getCurrency());
        detailContentView.addDetailRow(getString(R.string.boc_fund_position_share), stHold);
        //分红方式
        detailTabRowTextButtonWithHint = detailContentView.addTextAndButtonContent(getString(R.string.boc_fund_position_bonus_type), getBonusType(fundBalanceBean.getBonusType()),
                getString(R.string.boc_fund_position_alter_hint),
                new DetailTabRowTextButtonWithHint.DetailTvButtonOnClickListener() {
                    @Override
                    public void onClickTextView() {
                        alterBonus();
                    }
                });
        detailTabRowTextButtonWithHint.setValueHint(getString(R.string.boc_fund_position_alter_bonus_hint));

    }


    @Override
    protected String getTitleValue() {
        return getString(R.string.boc_fund_position_detail_titel);
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
    public void alterFundBonusSuccess(FundPositionDetailModel model) {
        closeProgressDialog();
        if (model == null ) {
            return;
        }
        if (DataUtils.ALTER_FLAG_SUCCESS.equals(model.getTranState())){
            detailTabRowTextButtonWithHint.updateValueContent(getOtherBonusType(fundBalanceBean.getBonusType()));
            return;
        }
        if (DataUtils.NO_MATCH_RISK_EVALUATION.equals(model.getTranState())){
            showRiskHintDialog();
            return;
        }
        if (DataUtils.ALTER_FLAG_NIGHT.equals(model.getTranState())){//提示

            return;
        }
    }

    @Override
    public void alterFundNightBonusFail(BiiResultErrorException biiResultErrorException) {
        ToastUtils.show(getString(R.string.boc_fund_position_alterbonus_fail));
    }

    @Override
    public void alterFundNightBonusSuccess(FundPositionDetailModel model) {
        closeProgressDialog();
        if (model == null ) {
            return;
        }
        if (DataUtils.ALTER_FLAG_SUCCESS.equals(model.getTranState())){

            return;
        }
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
    public void alterFundBonusFail(BiiResultErrorException biiResultErrorException) {

    }

    @Override
    public void setPresenter(Object presenter) {

    }

    @Override
    protected FundPositionDetailContract.Presenter initPresenter() {
        return new FundPositionDetailPresenter(this);
    }

    @Override
    public void onClick(View v) {

        //赎回
        if (v.getId() == R.id.bt_scheduledsell) {
            return;
        }
        //定投
        if (v.getId() == R.id.bt_scheduledbuy) {
            return;
        }
        //继续购买
        if (v.getId() == R.id.bt_rebuy) {
            return;
        }
        // 单条浮动盈亏详情页，fundcode
        if (v.getId() == R.id.float_profile_hint) {
            FundFloatProfileLossSingleDetailFragment fragment = new FundFloatProfileLossSingleDetailFragment();
            Bundle bundle = new Bundle();
            bundle.putBoolean(DataUtils.FUND_PROFILE_LOSS_TIME_SELECT_KEY,true);
            bundle.putParcelable(DataUtils.FUND_FLOAT_PROFILELESS_BEAN_KEY,resultListBean);
            bundle.putString(DataUtils.FUND_PROFILE_LOSS_TIME_HINT,getString(R.string.boc_fund_profile_one_year));
            fragment.setArguments(bundle);
            start(fragment);
            return;
        }
    }


    private void alterBonus() {
        if (alterBonusTypeDialog == null) {
            alterBonusTypeDialog = new AlterBonusTypeDialog(mContext);
            alterBonusTypeDialog.setLeftButtonClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alterBonusTypeDialog.dismiss();
                }
            });
            alterBonusTypeDialog.setRightButtonClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    fundBalanceBean.setBonusType(getOtherCode(fundBalanceBean.getBonusType()));
                    alterBonusTypeDialog.dismiss();
                    submitBonusResult(getOtherIndex(fundBalanceBean.getBonusType()),"N");
                }
            });
        }
        String fromType = getBonusType(fundBalanceBean.getBonusType());
        String toType = getOtherBonusType(fundBalanceBean.getBonusType());
        alterBonusTypeDialog.showDialog(fromType, toType);
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
                    submitBonusResult(getOtherIndex(fundBalanceBean.getBonusType()),"Y");
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
                    submitBonusNightResult(getOtherIndex(fundBalanceBean.getBonusType()),"N");
                    nightHintMessageDialog.dismiss();
                }
            });
        }
        nightHintMessageDialog.showDialog(getString(R.string.boc_fund_fixedinvest_no_match_risk));
    }



    private String getOtherCode(String one) {
        if (DataUtils.BONUSTYPE_CASH.equals(one)) {
            return DataUtils.BONUSTYPE_BONUS;
        } else {
            return DataUtils.BONUSTYPE_CASH;
        }
    }

    private String getOtherIndex(String one){
        if (StringUtil.isNullOrEmpty(one) || DataUtils.BONUSTYPE_DEFAULT.equals(one)){
            return  getOtherCode(fundBalanceBean.getFundInfo().getDefaultBonus());
        }else {
            return getOtherCode(one);
        }
    }

    private String getBonusType(String index){
        if (StringUtil.isNullOrEmpty(index) || DataUtils.BONUSTYPE_DEFAULT.equals(index)){
            return  DataUtils.getBonusTypeDes(mContext,fundBalanceBean.getFundInfo().getDefaultBonus());
        }else {
            return DataUtils.getBonusTypeDes(mContext, index);
        }
    }

    private String getOtherBonusType(String index){
        if (StringUtil.isNullOrEmpty(index) || DataUtils.BONUSTYPE_DEFAULT.equals(index)){
            return  DataUtils.getBonusTypeDes(mContext,getOtherCode(fundBalanceBean.getFundInfo().getDefaultBonus()));
        }else {
            return DataUtils.getBonusTypeDes(mContext, getOtherCode(index));
        }
    }

    private void submitBonusResult(String bonusType,String affirmFlag){
        FundPositionDetailModel model = new FundPositionDetailModel();
        model.setFundCode(fundBalanceBean.getFundCode());
        model.setAffirmFlag(affirmFlag);
        model.setFundBonusType(bonusType);
        getPresenter().alterFundBonus(model);
    }

    private void submitBonusNightResult(String bonusType,String affirmFlag){
        FundPositionDetailModel model = new FundPositionDetailModel();
        model.setAffirmFlag(affirmFlag);
        model.setFundBonusType(bonusType);
        model.setFundCode(fundBalanceBean.getFundCode());
        getPresenter().alterFundNightBonus(model);
    }

    private String calcaulateToatlProfileLoss() {
        if (resultListBean == null) {
            return MoneyUtils.transMoneyFormat("0", resultListBean.getCurceny());
        }
        try {
            BigDecimal result = new BigDecimal(resultListBean.getResultFloat());
            result = result.add(new BigDecimal(resultListBean.getEndFloat()));
            return MoneyUtils.transMoneyFormat(result, resultListBean.getCurceny());

        } catch (Exception e) {
            LoggerUtils.Error(e.getLocalizedMessage());
        }

        return MoneyUtils.transMoneyFormat("0", resultListBean.getCurceny());
    }

    private void gotoFundConversion(){
        FundConversionFragment fragment = new FundConversionFragment();
        Bundle bundle  = new Bundle();
        bundle.putString(DataUtils.FUND_NAME_KEY, fundBalanceBean.getFundInfo().getFullName());
        bundle.putString(DataUtils.FUND_CODE_KEY, fundBalanceBean.getFundInfo().getFundCode());
        bundle.putString(DataUtils.FUND_COMPANY_KEY, fundBalanceBean.getFundInfo().getFundCompanyName());
        bundle.putString(DataUtils.FUND_CURRENCY_KEY,fundBalanceBean.getFundInfo().getCurrency());
        fragment.setArguments(bundle);
        start(fragment);
    }



}
