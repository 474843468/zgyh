package com.boc.bocsoft.mobile.bocmobile.buss.fund.investmanagement.cancelorder.ui;

import android.view.LayoutInflater;
import android.view.View;

import com.boc.bocsoft.mobile.bii.bus.account.model.PsnQueryInvtBindingInfo.PsnQueryInvtBindingInfoResult;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.MvpBussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;
import com.boc.bocsoft.mobile.bocmobile.base.utils.ToastUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.confirmview.ConfirmInfoView;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.investmanagement.cancelorder.model.InvestRedeemParamsModel;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.investmanagement.cancelorder.model.RedeemModel;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.investmanagement.cancelorder.presenter.InvestCldPresenter;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.investmanagement.home.model.InvestBuyDetailModel;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.investmanagement.home.model.InvestSellDetailModel;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.investmanagement.home.ui.InvestConst;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.investmanagement.home.ui.InvestmanageFragment;

import java.math.BigDecimal;
import java.util.LinkedHashMap;

/**
 * Created by huixiaobo on 2016/11/30.
 * 定赎撤单
 */
public class RedeemInfoFragment extends MvpBussFragment<InvestCldPresenter>
        implements InvestCldContract.RedeemView {


    protected View rootView;
    protected ConfirmInfoView confirmView;
    /**定赎详情对象*/
    private InvestSellDetailModel mSellDetail;
    /**序号*/
    private String fundSeq;
    /**申请日期*/
    private String applyDate;
    /**定申修改标识*/
    private static final String REDEEM_AUT = "2";
    /**基金账户*/
    private PsnQueryInvtBindingInfoResult mInfoAccount;

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        rootView = mInflater.inflate(R.layout.boc_fund_redeemcld, null);
        return rootView;
    }

    @Override
    public void initView() {
        super.initView();
        confirmView = (ConfirmInfoView) rootView.findViewById(R.id.confirm_view);
    }

    @Override
    public void initData() {
        super.initData();
        //上页面传值定赎
        mSellDetail = (InvestSellDetailModel) getArguments().getSerializable(InvestConst.INVEST_SELL);
        //序号
        fundSeq = getArguments().getString(InvestConst.INVEST_FUNDSEQ);
        //申请日期
        applyDate = getArguments().getString(InvestConst.INVEST_FUNDDATE);

        showLoadingDialog();
        getPresenter().queryAccount(REDEEM_AUT);

    }


    @Override
    protected boolean isHaveTitleBarView() {
        return true;
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
    protected String getTitleValue() {
        return getString(R.string.boc_fundInvest_infotitle);
    }

    @Override
    public void setPresenter(InvestCldContract.Presenter presenter) {

    }

    @Override
    protected InvestCldPresenter initPresenter() {
        return new InvestCldPresenter(this);
    }

    /**
     * 040定赎撤单请求失败
     */
    @Override
    public void fundScheduleSellCancelFail(BiiResultErrorException biiResultErrorException) {
        closeProgressDialog();
    }

    /**
     * 040定赎撤单请求成功
     */
    @Override
    public void fundScheduleSellCancel(RedeemModel redeemModel) {
        closeProgressDialog();
        if (redeemModel != null) {
            popToAndReInit(InvestmanageFragment.class);
            ToastUtils.show("撤单成功");
        }
    }

    @Override
    public void fundAccountFail(BiiResultErrorException biiResultErrorException) {
        closeProgressDialog();
    }

    @Override
    public void fundAccountSuccess(PsnQueryInvtBindingInfoResult result) {
        closeProgressDialog();
        if (result != null) {
            mInfoAccount = result;
            setRedeemInfoData();
        }
    }

    /**
     * 赎回撤单页面显示数据
     */
    private void setRedeemInfoData() {
        if (mSellDetail == null || mInfoAccount == null) {
            return;
        }
        confirmView.setHeadValue(getString(R.string.boc_fundredeem_infoAmount),
                MoneyUtils.transMoneyFormat(mSellDetail.getSellCount(), mSellDetail.getCurrencyCode()));
        LinkedHashMap<String, String> datas = new LinkedHashMap<String, String>();
        datas.put(getString(R.string.boc_fundredeem_infoAccount), mInfoAccount.getInvestAccount());
        datas.put(getString(R.string.boc_fundredeem_infonumno), mInfoAccount.getAccount());
        datas.put(getString(R.string.boc_fundredeem_infoname),  mSellDetail.getFundName() + mSellDetail.getFundCode());
        datas.put(getString(R.string.boc_fundredeem_infoz),
                mSellDetail.getSellCycle());
        datas.put(getString(R.string.boc_fundredeem_infoDate), mSellDetail.getSellDate());
        if ("0".equals(mSellDetail.getSellFlag())) {
            datas.put(getString(R.string.boc_fundredeem_infotype), "不顺延赎回");
        } else if ("1".equals(mSellDetail.getSellFlag())) {
            datas.put(getString(R.string.boc_fundredeem_infotype), "顺延赎回");
        }

        if ("1".equals(mSellDetail.getEndFlag())) {
            datas.put(getString(R.string.boc_fundredeem_infoend), mSellDetail.getEndDate());
        } else if ("2".equals(mSellDetail.getEndFlag())) {
            datas.put(getString(R.string.boc_fundredeem_infonum),  mSellDetail.getEndSum());
        } else if ("3".equals(mSellDetail.getEndFlag())) {
            datas.put(getString(R.string.boc_fundredeem_infoAmt), mSellDetail.getEndCount());
        }
        confirmView.addData(datas);

    }

    /**
     * 定赎撤单网络请求
     */
    private void  investSell() {
        if (mSellDetail == null) {
            return;
        }
        InvestRedeemParamsModel paramsModel = new InvestRedeemParamsModel();
        paramsModel.setFundCode(mSellDetail.getFundCode());
        paramsModel.setEachAmount(new BigDecimal(mSellDetail.getSellCount()));
        paramsModel.setOldApplyDate(applyDate);
        paramsModel.setTransSeq(fundSeq);
        showLoadingDialog();
        getPresenter().queryScheduleSellCancel(paramsModel);
    }

    @Override
    public void setListener() {
        super.setListener();
        confirmView.setListener(new ConfirmInfoView.OnClickListener() {
            @Override
            public void onClickConfirm() {
                investSell();
            }

            @Override
            public void onClickChange() {

            }
        });
    }


}
