package com.boc.bocsoft.mobile.bocmobile.buss.fund.investmanagement.cancelorder.ui;

import android.view.LayoutInflater;
import android.view.View;

import com.boc.bocsoft.mobile.bii.bus.account.model.PsnQueryInvtBindingInfo.PsnQueryInvtBindingInfoParams;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnQueryInvtBindingInfo.PsnQueryInvtBindingInfoResult;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.MvpBussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;
import com.boc.bocsoft.mobile.bocmobile.base.utils.ToastUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.confirmview.ConfirmInfoView;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.investmanagement.cancelorder.model.InvestCldParamsModel;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.investmanagement.cancelorder.model.InvestModel;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.investmanagement.cancelorder.presenter.InvestCldPresenter;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.investmanagement.home.model.InvestBuyDetailModel;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.investmanagement.home.ui.InvestConst;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.investmanagement.home.ui.InvestmanageFragment;

import java.math.BigDecimal;
import java.util.LinkedHashMap;

/**
 * Created by huixiao on 2016/11/30.
 * 定申撤单
 */
public class InvestInfoFragment extends MvpBussFragment<InvestCldPresenter>
        implements InvestCldContract.CancelorderView {

    protected View rootView;
    protected ConfirmInfoView confirmView;
    /**定投详情对象*/
    private InvestBuyDetailModel mBuyDetail;
    /**序号*/
    private String fundseq;
    /**申请日期*/
    private String applyDate;
    /**基金定申标识*/
    private static final String INVEST_AUT = "1";
    /**基金账号*/
    private PsnQueryInvtBindingInfoResult mInvestAccount;


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
        mBuyDetail = (InvestBuyDetailModel) getArguments().getSerializable(InvestConst.INVEST_BUY);
        //序号
        fundseq = getArguments().getString(InvestConst.INVEST_FUNDSEQ);
        //申请日期
        applyDate = getArguments().getString(InvestConst.INVEST_FUNDDATE);

        showLoadingDialog();
        getPresenter().queryAccount(INVEST_AUT);
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
        return getString(R.string.boc_fundInvest_infotitle);
    }

    /**040接口*/
    @Override
    public void fundDdAbortFail(BiiResultErrorException biiResultErrorException) {
        closeProgressDialog();
    }

    @Override
    public void fundDdAbortSuccess(InvestModel investModel) {
        closeProgressDialog();
        if (investModel != null) {
            popToAndReInit(InvestmanageFragment.class);
            ToastUtils.show("撤单成功");
        }
    }
    /**001接口基金账号请求失败*/
    @Override
    public void fundAccountFail(BiiResultErrorException biiResultErrorException) {
        closeProgressDialog();
    }

    /**001基金账号请求成功*/
    @Override
    public void fundAccountSuccess(PsnQueryInvtBindingInfoResult result) {
        closeProgressDialog();
        if (result != null) {
            mInvestAccount = result;
            setInvestInfoData();
        }

    }

    /**
     * 定投撤单页面赋值
     */
    private void setInvestInfoData() {
        // 确认页面组件赋值
        if (mBuyDetail == null || mInvestAccount == null) {
            return;
        }
        confirmView.setHeadValue(getString(R.string.boc_fundInvest_infoAmount),
                MoneyUtils.transMoneyFormat(mBuyDetail.getApplyAmount(), mBuyDetail.getCurrencyCode()));
        LinkedHashMap<String, String> datas = new LinkedHashMap<String, String>();
        datas.put(getString(R.string.boc_fundInvest_infoAccount), mInvestAccount.getInvestAccount());
        datas.put(getString(R.string.boc_fundInvest_infonumno), mInvestAccount.getAccount());
        datas.put(getString(R.string.boc_fundInvest_infoname), mBuyDetail.getFundName()+ mBuyDetail.getFundCode());
        datas.put(getString(R.string.boc_fundInvest_infoz), mBuyDetail.getApplyCycle());
        datas.put(getString(R.string.boc_fundInvest_infoDate),  mBuyDetail.getPaymentDate());
        datas.put(getString(R.string.boc_fundInvest_infotype),"定期定额申购");
        if ("1".equals(mBuyDetail.getEndFlag())) {
            //现接口没有返回值
            datas.put(getString(R.string.boc_fundInvest_infoend), "-");
        } else if ("2".equals(mBuyDetail.getEndFlag())){
            datas.put(getString(R.string.boc_fundInvest_infonum),mBuyDetail.getEndSum());
        } else if ("3".equals(mBuyDetail.getEndFlag())) {
            datas.put(getString(R.string.boc_fundInvest_infoAmt),mBuyDetail.getEndAmt());
        }
        confirmView.isShowSecurity(false);
        confirmView.addData(datas,false);
    }

    /**
     * 014定投撤单网络请求
     * */
    private void investBuy(){
        if (mBuyDetail == null) {
            return;
        }
        InvestCldParamsModel investCldParamsModel = new InvestCldParamsModel();
        investCldParamsModel.setFundCode(mBuyDetail.getFundCode());
        investCldParamsModel.setOldApplyDate(applyDate);
        investCldParamsModel.setTransSeq(fundseq);
        investCldParamsModel.setEachAmount(new BigDecimal(mBuyDetail.getApplyAmount()));
        showLoadingDialog();
        getPresenter().queryFundDdAbort(investCldParamsModel);
    }


    @Override
    public void setPresenter(InvestCldContract.Presenter presenter) {

    }

    @Override
    protected InvestCldPresenter initPresenter() {
        return new InvestCldPresenter(this);
    }

}
