package com.boc.bocsoft.mobile.bocmobile.buss.fund.investmanagement.update.ui;

import android.view.LayoutInflater;
import android.view.View;

import com.boc.bocsoft.mobile.bii.bus.account.model.PsnQueryInvtBindingInfo.PsnQueryInvtBindingInfoResult;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundScheduledSellModify.PsnFundScheduledSellModifyParams;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.MvpBussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;
import com.boc.bocsoft.mobile.bocmobile.base.utils.ToastUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.confirmview.ConfirmInfoView;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.investmanagement.cancelorder.model.InvestRedeemParamsModel;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.investmanagement.home.model.InvestSellDetailModel;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.investmanagement.home.model.ValidinvestModel;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.investmanagement.home.ui.InvestConst;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.investmanagement.home.ui.ValidDetailFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.investmanagement.update.model.FundCompanyModel;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.investmanagement.update.model.FundScheduelBuyModel;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.investmanagement.update.model.FundScheduledSellModel;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.investmanagement.update.model.FundScheduledSellPms;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.investmanagement.update.presenter.FundUpdatePresenter;

import java.math.BigDecimal;
import java.util.LinkedHashMap;

/**
 * Created by huixiaobo on 2016/12/14.
 * 修改确认页
 */
public class RedeemInfoFragment extends MvpBussFragment<FundUpdatePresenter>
        implements FundUpdateContract.UpdateInfoView {


    protected View rootView;
    protected ConfirmInfoView confirmView;
    /**基金账号返回对象*/
    private PsnQueryInvtBindingInfoResult accountResult;
    /**基金定赎修改返回对象*/
    private ValidinvestModel.ListBean mSellData;
    /**基金定赎修改返回对象*/
    private InvestSellDetailModel mSellDetail;
    /**基金公司名称*/
    private FundCompanyModel mCompanyResult;
    /**定赎序号*/
    private String fundSeq;
    /**定赎申请时间*/
    private String applyDate;
    /**定赎修改成功后返回对象*/
    FundScheduledSellModel mFundSellModel;

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
    protected boolean isHaveTitleBarView() {
        return true;
    }

    @Override
    protected boolean getTitleBarRed() {
        return false;
    }

    @Override
    protected String getTitleValue() {
        return "确认信息";
    }


    @Override
    protected boolean isDisplayRightIcon() {
        return false;
    }

    @Override
    protected FundUpdatePresenter initPresenter() {
        return new FundUpdatePresenter(this);
    }

    @Override
    public void initData() {
        super.initData();
        //上页面传值定赎
       // mSellDetail = (InvestSellDetailModel) getArguments().getSerializable(InvestConst.INVEST_SELL);
        mSellData = (ValidinvestModel.ListBean) getArguments().getSerializable(InvestConst.VALID_DETAIL);
        //基金公司信息
        mCompanyResult = (FundCompanyModel) getArguments().getSerializable(InvestConst.FUND_COMPANYTYPE);
        //序号
        fundSeq = getArguments().getString(InvestConst.INVEST_FUNDSEQ);
        //申请日期
        applyDate = getArguments().getString(InvestConst.INVEST_FUNDDATE);
        showLoadingDialog();
        //请求基金账号
        getPresenter().queryAccount();
    }

    /**
     * 赎回撤单页面显示数据
     */
    private void setRedeemInfoData() {
        if (mSellData == null || accountResult == null) {
            return;
        }
        confirmView.setHeadValue(getString(R.string.boc_fundredeem_infoAmount),
                MoneyUtils.transMoneyFormat(mSellDetail.getSellCount(), mSellDetail.getCurrencyCode()));
        LinkedHashMap<String, String> datas = new LinkedHashMap<String, String>();
        datas.put(getString(R.string.boc_fundredeem_infoAccount), accountResult.getInvestAccount());
        datas.put(getString(R.string.boc_fundredeem_infonumno), accountResult.getAccount());
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
        confirmView.setHint(getString(R.string.boc_fund_conversion_hint_info, mCompanyResult.getCompanyName(),
                mCompanyResult.getCompanyPhone()),R.color.boc_text_color_cinerous);
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

    /**
     * 定赎撤单网络请求
     */
    private void  investSell() {
        if (mSellData == null) {
            return;
        }
        FundScheduledSellPms params = new FundScheduledSellPms();
        params.setFundCode(mSellData.getFundCode());
        params.setOldFundPaymentDate(mSellData.getApplyDate());
        params.setDsFlag(mSellData.getCashFlag());
        params.setEndFlag(mSellData.getEndFlag());
        params.setFundSellAmount(mSellData.getApplyAmount());
        params.setFundSellFlag(mSellData.getSellFlag());
        params.setFundSeq(mSellData.getFundSeq());
        params.setSubDate(mSellData.getSubDate());
        params.setEndFlag(mSellData.getEndFlag());
        if ("1".equals(mSellData.getEndFlag())){
            params.setFundPointendDate(mSellData.getEndDate());
        } else if ("2".equals(mSellData.getSellFlag())) {
            params.setEndSum(mSellData.getEndSum());
        } else if ("3".equals(mSellData.getSellFlag())) {
            params.setFundPointendAmount(mSellData.getEndAmt());
        }
         showLoadingDialog();
        getPresenter().queryScheduledSellModify(params);
    }

    @Override
    public void fundAccountFail(BiiResultErrorException biiResultErrorException) {
        closeProgressDialog();
    }

    @Override
    public void fundAccountSuccess(PsnQueryInvtBindingInfoResult result) {
        closeProgressDialog();
        if (result != null) {
            accountResult = result;
            setRedeemInfoData();
        }
    }


    @Override
    public void scheduledBuyModifyFail(BiiResultErrorException biiResultErrorException) {
        closeProgressDialog();
    }

    @Override
    public void scheduledBuyModifySuccess(FundScheduelBuyModel fundBuyResult) {

    }

    @Override
    public void scheduledSellModifyFail(BiiResultErrorException biiResultErrorException) {

    }

    @Override
    public void scheduledSellModifySuccess(FundScheduledSellModel fundSellResult) {
        if(fundSellResult != null) {
            mFundSellModel = fundSellResult;
            popToAndReInit(ValidDetailFragment.class);
            ToastUtils.show("修改成功");
        }
    }

    @Override
    public void setPresenter(FundUpdateContract.Presenter presenter) {

    }
}
