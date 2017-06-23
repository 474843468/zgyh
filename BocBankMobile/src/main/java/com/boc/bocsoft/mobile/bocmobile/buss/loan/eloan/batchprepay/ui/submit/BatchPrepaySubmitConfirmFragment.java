package com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.batchprepay.ui.submit;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnELOANBatchRepaySubmit.PsnELOANBatchRepaySubmitParams;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnELOANBatchRepaySubmit.PsnELOANBatchRepaySubmitResultBean;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLOANListEQuery.PsnLOANListEQueryResult;
import com.boc.bocsoft.mobile.bocmobile.ApplicationConst;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.confirmview.ConfirmInfoView;
import com.boc.bocsoft.mobile.bocmobile.base.widget.operation.OperationResultHead;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.EloanConst;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.batchprepay.model.BatchPrepayQryModel;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.batchprepay.model.BatchPrepaySubmitConfirmModel;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.batchprepay.presenter.BatchPrepaySubmitConfirmPresenter;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.changeRepaymentAccount.model.ErrorException;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.utils.DataUtils;
import com.boc.bocsoft.mobile.common.utils.NumberUtils;
import com.boc.bocsoft.mobile.common.utils.StringUtils;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * 贷款管理-中银E贷-批量提前还款确认信息Fragment
 * Created by liuzc on 2016/9/8.
 */
public class BatchPrepaySubmitConfirmFragment extends BussFragment implements BatchPrepaySubmitConfirmContract.View {
    private static final String TAG = "BPrepaySubmitConfFrag";
    /**
     * view区
     */
    //确认页面
    protected ConfirmInfoView confirmInfoView;

    private BatchPrepaySubmitConfirmPresenter mPresenter = null;

    //上层页面传递过来的数据
    private BatchPrepayQryModel mQryModel = null; //详情信息
    private BatchPrepaySubmitTotalBean mTotalInfoBean = null; //金额、利息的汇总信息

    private String currencyCode = ApplicationConst.CURRENCY_CNY; //币种
    private String quoteNo = null; //额度编号

    private String conversationID = null; //会话ID

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        confirmInfoView = new ConfirmInfoView(mContext);
        return confirmInfoView;
    }

    @Override
    protected void titleLeftIconClick() {
        super.titleLeftIconClick();
    }

    @Override
    protected void titleRightIconClick() {
        super.titleRightIconClick();
    }

    public void setConversationID(String value){
        conversationID = value;
    }

    @Override
    protected String getTitleValue() {
        return getString(R.string.boc_eloan_prepay_confirm_title);
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
    public void initView() {
        //初始化组件
        confirmInfoView.isShowSecurity(false); //隐藏安全组件
    }


    @Override
    public void initData() {
        mPresenter = new BatchPrepaySubmitConfirmPresenter(this);
        mPresenter.setConversationId(conversationID);

        Bundle bundle = getArguments();
        mQryModel = (BatchPrepayQryModel)bundle.getSerializable(EloanConst.ELON_PREPAY_COMMIT);
        mTotalInfoBean = (BatchPrepaySubmitTotalBean)bundle.getSerializable(EloanConst.ELOAN_REPAY_TOTAL);
        quoteNo = getArguments().getString(EloanConst.LOAN_QUOTENO);

        if(StringUtils.isEmptyOrNull(currencyCode)){
            currencyCode = ApplicationConst.CURRENCY_CNY;
        }
        //头部信息
        confirmInfoView.setHeadValue(String.format("%s (%s)", getResources().getString(R.string.boc_eloan_prepay_total_desc2),
                DataUtils.getCurrencyDescByLetter(mContext, currencyCode)),
                mTotalInfoBean.getTotalAmount());

        //绘制汇总信息
        addSumInfoView(NumberUtils.formatCardNumber(mTotalInfoBean.getPayAccount()), mTotalInfoBean.getTotalCapital(),
                mTotalInfoBean.getTotalCharges(), mTotalInfoBean.getTotalInterest());

        //绘制详情信息
        List<PsnLOANListEQueryResult.PsnLOANListEQueryBean> prepayList = mQryModel.getLoanList();
        for(int i = 0; i < prepayList.size(); i ++){
            PsnLOANListEQueryResult.PsnLOANListEQueryBean bean = prepayList.get(i);
            String date = bean.getLoanDate(); //用款日
            currencyCode = bean.getCurrencyCode();
            String sCapital = bean.getRemainCapital();
            String sInterest = bean.getThisIssueRepayInterest();
            String sCharges = mQryModel.getChargeList().get(i);
            if(StringUtils.isEmptyOrNull(sCharges)){
                sCharges = "0";
            }

            BigDecimal bigdCapital = new BigDecimal(sCapital);
            BigDecimal bigdInterest = new BigDecimal(sInterest);
            BigDecimal bigCharges = new BigDecimal(sCharges);
            BigDecimal bigdAmount = bigdCapital.add(bigdInterest).add(bigCharges);

            boolean bShowMargin = true;
            addDetailInfoView(date, MoneyUtils.transMoneyFormat(sCapital, currencyCode),
                    MoneyUtils.transMoneyFormat(sInterest, currencyCode),
                    MoneyUtils.transMoneyFormat(sCharges, currencyCode),
                    MoneyUtils.transMoneyFormat(bigdAmount, currencyCode), bShowMargin);
        }
    }
    /**
     * 添加汇总信息
     *  @param payAccount 还款账户
     * @param capital 本金
     * @param interest 利息
     * @param charges 手续费
     * @return
     */
    private void addSumInfoView(String payAccount, String capital, String charges, String interest){
        LinkedHashMap<String, String> datas = new LinkedHashMap<String, String>();
        datas.put(getString(R.string.boc_eloan_prepay_capital), capital);
        datas.put(getString(R.string.boc_eloan_prepay_interest_desc2), interest);
        datas.put(getString(R.string.boc_eloan_fee), DataUtils.getFormatCharges(charges));
        datas.put(getString(R.string.boc_pledge_info_payer_account), payAccount);
        datas.put(getString(R.string.boc_facility_loan_type), getString(R.string.boc_eloan_middleTitle));

        confirmInfoView.addData(datas, false, false);
    }

    /**
     * 添加详情信息
     * *@param date 用款日期
     * @param capital 本金
     * @param interest 利息
     * @param charge 手续费
     * @param amount 还款总额
     * @return
     */
    private void addDetailInfoView(String date, String capital,String interest, String charge, String amount, boolean showMargin){
        LinkedHashMap<String, String> datas = new LinkedHashMap<String, String>();
        datas.put(getString(R.string.boc_eloan_use_money_date), date);
        datas.put(getString(R.string.boc_eloan_prepay_capital), capital);
        datas.put(getString(R.string.boc_eloan_prepay_interest_desc2), interest);
        datas.put(getString(R.string.boc_eloan_fee), DataUtils.getFormatCharges(charge));
        datas.put(getString(R.string.boc_eloan_prepay_total_desc2), amount);

        confirmInfoView.addData(datas, showMargin);
    }



    @Override
    public void setListener() {
        confirmInfoView.setListener(new ConfirmInfoView.OnClickListener(){
            @Override
            public void onClickConfirm() {
                showLoadingDialog(false);
                PsnELOANBatchRepaySubmitParams parmas = buildSubmitParams();
                mPresenter.batchPrepaySubmit(parmas);
            }

            @Override
            public void onClickChange() {

            }
        });
    }

    private PsnELOANBatchRepaySubmitParams buildSubmitParams(){
        String accountID;

        PsnELOANBatchRepaySubmitParams parmas = new PsnELOANBatchRepaySubmitParams();

        List<PsnELOANBatchRepaySubmitParams.ListBean> paramList =
                new LinkedList<PsnELOANBatchRepaySubmitParams.ListBean>();

        List<BatchPrepayQryModel.PsnLOANListEQueryBean> modelList = mQryModel.getLoanList();
        for(int i = 0; i < modelList.size(); i ++){
            PsnELOANBatchRepaySubmitParams.ListBean paramBean =
                    new PsnELOANBatchRepaySubmitParams.ListBean();
            BatchPrepayQryModel.PsnLOANListEQueryBean modelBean = modelList.get(i);
            paramBean.setLoanAccount(modelBean.getAccountNumber());
//            paramBean.setCurrency(modelBean.getCurrencyCode());
            paramBean.setCurrency(ApplicationConst.CURRENCY_CNY);
            paramBean.setLoanType(modelBean.getLoanType());

            String capital = modelBean.getRemainCapital();
            String interest = modelBean.getThisIssueRepayInterest();

            String repayAmount = (new BigDecimal(capital)).add(new BigDecimal(interest)).toString();
            paramBean.setAdvanceRepayInterest(getStrWith2Decimal(interest));
            paramBean.setAdvanceRepayCapital(capital);
            paramBean.setRepayAmount(getStrWith2Decimal(repayAmount));

            paramList.add(paramBean);
        }

        parmas.setListBeen(paramList);
        parmas.setAccountId(mTotalInfoBean.getPayAccountID());
        parmas.setPayAccount(mTotalInfoBean.getPayAccount());
        parmas.setQuoteNo(quoteNo);
        return parmas;
    }

    /**
     * 数值四舍五入得到两位小数
     * @param value
     * @return
     */
    private String getStrWith2Decimal(String value){
        if(StringUtils.isEmptyOrNull(value)){
            return "";
        }

        BigDecimal bdValue = new BigDecimal(value);
        return bdValue.setScale(2, BigDecimal.ROUND_HALF_UP).toString();
    }

    @Override
    public void onDestroyView() {
        mPresenter.unsubscribe();
        super.onDestroyView();
    }

    @Override
    public void setPresenter(BatchPrepaySubmitConfirmContract.Presenter presenter) {
    }

    /**
     * 判断是否有一笔或多笔提前还款交易都成功
     * @return
     */
    private boolean isTransactionSuccess(BatchPrepaySubmitConfirmModel model){
        if(model == null || model.getBatchRepayList() == null){
            return false;
        }

        for(int i = 0; i < model.getBatchRepayList().size(); i ++){
            PsnELOANBatchRepaySubmitResultBean bean = model.getBatchRepayList().get(i);
            if(bean.getStatus() != null && bean.getStatus().equals("A")){
                return true;
            }
        }
        return false;
    }

    @Override
    public void batchPrepaySubmitSuccess(List<PsnELOANBatchRepaySubmitResultBean> result) {
        Log.i(TAG, "提前还款交易成功！");
        closeProgressDialog();
        BatchPrepaySubmitResultFragment resultsFragment = new BatchPrepaySubmitResultFragment();

        BatchPrepaySubmitConfirmModel model = new BatchPrepaySubmitConfirmModel();
        model.setBatchRepayList(result);

        boolean bSuccess = isTransactionSuccess(model);
        if(bSuccess){
            resultsFragment.setStatus(OperationResultHead.Status.SUCCESS);
        }
        else{
            resultsFragment.setStatus(OperationResultHead.Status.FAIL);
        }

        Bundle bundle = new Bundle();
        bundle.putSerializable(EloanConst.LOAN_PREPY_RESULT, model);
        bundle.putSerializable(EloanConst.ELOAN_REPAY_TOTAL, mTotalInfoBean);
        bundle.putSerializable(EloanConst.ELON_PREPAY_COMMIT, mQryModel);

        resultsFragment.setArguments(bundle);
        start(resultsFragment);
    }

    @Override
    public void batchPrepaySubmitFail(ErrorException e) {
        Log.i(TAG, "提前还款交易失败！");
        closeProgressDialog();
//        BatchPrepaySubmitResultFragment resultsFragment = new BatchPrepaySubmitResultFragment();
//        resultsFragment.setStatus(OperationResultHead.Status.FAIL);
//        start(resultsFragment);
    }
}
