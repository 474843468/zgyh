package com.boc.bocsoft.mobile.bocmobile.buss.fund.trademanagement.ui;

import android.annotation.SuppressLint;

import com.boc.bocop.sdk.util.StringUtil;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;
import com.boc.bocsoft.mobile.bocmobile.base.utils.ToastUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.confirmview.ConfirmInfoView;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify.DeviceInfoModel;
import com.boc.bocsoft.mobile.bocmobile.buss.account.base.BaseTransactionFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.trademanagement.model.PsnFundAppointCancelModel;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.trademanagement.model.PsnFundConsignAbortModel;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.trademanagement.model.PsnFundQueryTransOntranModel;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.trademanagement.presenter.TransitTradeCancelConfirmPresenter;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.utils.DataUtils;
import com.boc.bocsoft.mobile.common.utils.date.DateFormatters;

import java.util.LinkedHashMap;

/**
 * Created by wy7105 on 2016/12/1.
 * 交易记录-撤单确认页面
 */
@SuppressLint("ValidFragment")
public class TransitTradeCancelConfirmFragment extends BaseTransactionFragment implements TransitTradeCancelConfirmContract.View {

    private PsnFundQueryTransOntranModel model;
    private PsnFundQueryTransOntranModel.ListBean listBean;
    private TransitTradeCancelConfirmContract.Presenter mTransitTradeCancelConfirmPresenter;

    public TransitTradeCancelConfirmFragment(PsnFundQueryTransOntranModel model) {
        this.model = model;
    }

    @Override
    public void initView() {
        confirmInfoView.isShowSecurity(false);
    }

    @Override
    protected TransitTradeCancelConfirmPresenter initPresenter() {
        return new TransitTradeCancelConfirmPresenter(this);
    }

    @Override
    public void initData() {
        mTransitTradeCancelConfirmPresenter = new TransitTradeCancelConfirmPresenter(this);
        listBean = (PsnFundQueryTransOntranModel.ListBean) getArguments().getSerializable("listBean"); //获取列表页传过来的值
        String paymentData = listBean.getPaymentDate().format(DateFormatters.dateFormatter1); //获取委托日期
        String headTitle = String.format(getString(R.string.boc_fund_amount_count),
                DataUtils.getCurrencyDescByLetter(mContext, "CNY"));
        String amount = MoneyUtils.transMoneyFormat(listBean.getTransAmount(), "CNY");
        String fundName = listBean.getFundName();
        String fundCode = listBean.getFundCode();
        String nameAndCode = mContext.getString(R.string.boc_fund_record_name_code, fundName, fundCode);
        String transType = DataUtils.getTransTypeDes(mContext, listBean.getFundTranType());
        confirmInfoView.setHeadValue(headTitle, amount);
        LinkedHashMap<String, String> datas = new LinkedHashMap<>();
        datas.put(getString(R.string.boc_fund_Seq), listBean.getOrignFundSeq());//基金交易流水号
        datas.put(getString(R.string.boc_fund_namecode), nameAndCode);//交易基金
        datas.put(getString(R.string.boc_wealth_entrust_date), paymentData);//委托日期
        datas.put(getString(R.string.boc_fundstate_applyType), transType);//交易类型
        confirmInfoView.addData(datas, false, false);
    }

    @Override
    public void onClickConfirm() {

    }

    @Override
    public void setListener() {
        confirmInfoView.setListener(new ConfirmInfoView.OnClickListener() {
            @Override
            public void onClickConfirm() {
                showLoadingDialog(false);
                listBean = (PsnFundQueryTransOntranModel.ListBean) getArguments().getSerializable("listBean");
                String specialTransFlag = listBean.getSpecialTransFlag(); //特殊交易标志

                //根据特殊交易标志判断请求021/036接口,如果特殊交易标志为“2”，则调用036指定日期撤单接口，其余调用021基金撤单接口
                if ("2".equals(specialTransFlag)) {
                    //生成指定日期撤单请求接口
                    PsnFundAppointCancelModel appointCancelModel = new PsnFundAppointCancelModel();
                    String appointData = listBean.getAppointDate().format(DateFormatters.dateFormatter1);
                    appointCancelModel.setFundCode(listBean.getFundCode()); //基金代码
                    appointCancelModel.setAssignedDate(appointData); //指定日期
                    appointCancelModel.setFundSeq(listBean.getOrignFundSeq()); //交易序号
                    appointCancelModel.setOriginalTransCode(listBean.getFundTranType()); //原始交易码
                    mTransitTradeCancelConfirmPresenter.psnFundAppointCancel(appointCancelModel);
                } else {
                    //生成一般撤单请求参数
                    PsnFundConsignAbortModel consignAbortModel = new PsnFundConsignAbortModel();
                    String paymentData = listBean.getPaymentDate().format(DateFormatters.dateFormatter1);
                    consignAbortModel.setFundCode(listBean.getFundCode()); //基金代码
                    consignAbortModel.setFundSeq(listBean.getOrignFundSeq()); //交易序号
                    consignAbortModel.setFundAmount(listBean.getTransAmount()); //交易份额
                    consignAbortModel.setOriginalTransCode(listBean.getFundTranType()); //原始交易码
                    consignAbortModel.setDate(paymentData); //交易日期(委托日期)
                    if ("1".equals(listBean.getSpecialTransFlag()))
                        consignAbortModel.setNightFlag("true");
                    else consignAbortModel.setNightFlag("false"); //夜间标志-根据是否挂单判断
                    mTransitTradeCancelConfirmPresenter.psnFundConsignAbort(consignAbortModel);
                }
            }

            @Override
            public void onClickChange() {
            }
        });
    }

    @Override
    public void submitTransactionWithSecurity(DeviceInfoModel deviceInfoModel, String factorId, String[] randomNums, String[] encryptPasswords) {

    }

    @Override
    public void psnFundConsignAbortSuccess(PsnFundConsignAbortModel viewModel) {
        closeProgressDialog();
        String fundSeq = viewModel.getFundSeq();
        String transactionId = viewModel.getTransactionId();
        if (!StringUtil.isNullOrEmpty(fundSeq) && !StringUtil.isNullOrEmpty(transactionId)) {
            ToastUtils.showLong(getResources().getString(R.string.boc_fund_tansfer_cancel_success).toString());
            popToAndReInit(TradeManagementFragment.class);
        }
    }

    @Override
    public void psnFundConsignAbortFail(BiiResultErrorException biiResultErrorException) {

    }

    @Override
    public void psnFundAppointCancelSuccess(PsnFundAppointCancelModel viewModel) {
        String fundSeq = viewModel.getFundSeq();
        String transactionId = viewModel.getTransactionId();
        if (!StringUtil.isNullOrEmpty(fundSeq) && !StringUtil.isNullOrEmpty(transactionId)) {
            ToastUtils.showLong(getResources().getString(R.string.boc_fund_tansfer_cancel_success).toString());
            popToAndReInit(TradeManagementFragment.class);
        }
    }

    @Override
    public void psnFundAppointCancelFail(BiiResultErrorException biiResultErrorException) {

    }

    @Override
    public void setPresenter(TransitTradeCancelConfirmPresenter transitTradeCancelConfirmPresenter) {

    }
}