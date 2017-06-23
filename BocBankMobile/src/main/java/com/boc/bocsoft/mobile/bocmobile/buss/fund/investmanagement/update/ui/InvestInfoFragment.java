package com.boc.bocsoft.mobile.bocmobile.buss.fund.investmanagement.update.ui;

import android.view.LayoutInflater;
import android.view.View;

import com.boc.bocsoft.mobile.bii.bus.account.model.PsnQueryInvtBindingInfo.PsnQueryInvtBindingInfoResult;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.MvpBussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;
import com.boc.bocsoft.mobile.bocmobile.base.utils.ToastUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.confirmview.ConfirmInfoView;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.investmanagement.home.model.InvestBuyDetailModel;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.investmanagement.home.model.ValidinvestModel;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.investmanagement.home.ui.InvestConst;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.investmanagement.home.ui.InvestmanageFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.investmanagement.home.ui.ValidDetailFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.investmanagement.update.model.FundCompanyModel;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.investmanagement.update.model.FundScheduelBuyModel;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.investmanagement.update.model.FundScheduledBuyPms;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.investmanagement.update.model.FundScheduledSellModel;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.investmanagement.update.presenter.FundUpdatePresenter;

import java.util.LinkedHashMap;

/**
 * Created by huixiaobo on 2016/12/14.
 * 定投修改确认页
 */
public class InvestInfoFragment extends MvpBussFragment<FundUpdatePresenter>
        implements FundUpdateContract.UpdateInfoView {

    protected View rootView;
    protected ConfirmInfoView confirmView;
    /**查询基金账号*/
    private PsnQueryInvtBindingInfoResult fundAccount;
    /**有效定申传值对象*/
    private ValidinvestModel.ListBean mBuyData;
    /**定申详情参数*/
    private InvestBuyDetailModel mBuyDetail;
    /**序号*/
    private String fundseq;
    /**基金公司名称*/
    private FundCompanyModel fundCompany;
    /**修改确认请求返回成功对象*/
    private FundScheduelBuyModel mfundBuyResult;
    /**申请日期*/
    private String applyDate;


    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        rootView = mInflater.inflate(R.layout.boc_fund_investcld, null);
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
       // mBuyDetail = (InvestBuyDetailModel) getArguments().getSerializable(InvestConst.INVEST_BUY);
        mBuyData = (ValidinvestModel.ListBean) getArguments().getSerializable(InvestConst.VALID_DETAIL);
        /**基金公司信息*/
        fundCompany = (FundCompanyModel) getArguments().getSerializable(InvestConst.FUND_COMPANYTYPE);
        //序号
        fundseq = getArguments().getString(InvestConst.INVEST_FUNDSEQ);
        //申请日期
        applyDate = getArguments().getString(InvestConst.INVEST_FUNDDATE);
        showLoadingDialog();
        getPresenter().queryAccount();
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
    protected boolean isDisplayRightIcon() {
        return false;
    }

    @Override
    protected String getTitleValue() {
        return "确认信息";
    }

    @Override
    protected FundUpdatePresenter initPresenter() {
        return new FundUpdatePresenter(this);
    }

    @Override
    public void setListener() {
        super.setListener();
        confirmView.setListener(new ConfirmInfoView.OnClickListener() {
            @Override
            public void onClickConfirm() {
                investBuy();
            }

            @Override
            public void onClickChange() {

            }
        });
    }
    /**定申修改确认请求上送对象*/
    private void investBuy() {
        if (mBuyData == null) {
            return;
        }
        FundScheduledBuyPms pms = new FundScheduledBuyPms();
        pms.setApplyAmount(mBuyData.getApplyAmount());
        pms.setEndFlag(mBuyData.getEndFlag());
        pms.setFundCode(mBuyData.getFundCode());
        pms.setSubDate(mBuyData.getSubDate());
        pms.setOldFundPaymentDate(mBuyData.getApplyDate());
        pms.setTransCycle(mBuyData.getDtdsFlag());
        pms.setTransSeq(mBuyData.getFundSeq());
        if ("1".equals(mBuyData.getEndFlag())){
            pms.setFundPointendDate(mBuyData.getEndDate());
        } else if ("2".equals(mBuyData.getSellFlag())) {
            pms.setEndSum(mBuyData.getEndSum());
        } else if ("3".equals(mBuyData.getSellFlag())) {
            pms.setFundPointendAmount(mBuyData.getEndAmt());
        }
        showLoadingDialog();
        getPresenter().queryScheduledBuyModify(pms);

    }

    private void getInvestInfoData() {
        if (mBuyData == null || fundAccount == null) {
            return;
        }
        confirmView.setHeadValue(getString(R.string.boc_fundInvest_infoAmount),
                MoneyUtils.transMoneyFormat(mBuyData.getApplyAmount(), mBuyData.getCurrency()));
        LinkedHashMap<String, String> datas = new LinkedHashMap<String, String>();
        datas.put(getString(R.string.boc_fundInvest_infoAccount), fundAccount.getInvestAccount());
        datas.put(getString(R.string.boc_fundInvest_infonumno), fundAccount.getAccount());
        datas.put(getString(R.string.boc_fundInvest_infoname), mBuyData.getFundName()+ mBuyData.getFundCode());
        datas.put(getString(R.string.boc_fundInvest_infoz), mBuyData.getDtdsFlag());
        datas.put(getString(R.string.boc_fundInvest_infoDate),  mBuyData.getSubDate());
        datas.put(getString(R.string.boc_fundInvest_infotype), "定期定额申购");
        if ("1".equals(mBuyData.getEndFlag())) {
            //现接口没有返回值
            datas.put(getString(R.string.boc_fundInvest_infoend), mBuyData.getEndDate());
        } else if ("2".equals(mBuyData.getEndFlag())){
            datas.put(getString(R.string.boc_fundInvest_infonum),mBuyData.getEndSum());
        } else if ("3".equals(mBuyData.getEndFlag())) {
            datas.put(getString(R.string.boc_fundInvest_infoAmt),mBuyData.getEndAmt());
        }
        confirmView.addData(datas,false);
        confirmView.setHint(getString(R.string.boc_fund_conversion_hint_info, fundCompany.getCompanyName(),
                fundCompany.getCompanyPhone()),R.color.boc_text_color_cinerous);
    }

    @Override
    public void fundAccountFail(BiiResultErrorException biiResultErrorException) {
        closeProgressDialog();
    }

    @Override
    public void fundAccountSuccess(PsnQueryInvtBindingInfoResult result) {
        closeProgressDialog();
        if (result == null) {
            return;
        }
        fundAccount = result;

        getInvestInfoData();
    }

    @Override
    public void scheduledBuyModifyFail(BiiResultErrorException biiResultErrorException) {
        closeProgressDialog();
    }

    @Override
    public void scheduledBuyModifySuccess(FundScheduelBuyModel fundBuyResult) {
        closeProgressDialog();
        if (fundBuyResult != null) {
            mfundBuyResult = fundBuyResult;
            popToAndReInit(ValidDetailFragment.class);
            ToastUtils.show("修改成功");
        }
    }

    @Override
    public void scheduledSellModifyFail(BiiResultErrorException biiResultErrorException) {
        closeProgressDialog();
    }

    @Override
    public void scheduledSellModifySuccess(FundScheduledSellModel fundSellResult) {
        closeProgressDialog();
        if (fundSellResult == null) {
            return;
        }

    }

    @Override
    public void setPresenter(FundUpdateContract.Presenter presenter) {

    }

}
