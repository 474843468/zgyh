package com.boc.bocsoft.mobile.bocmobile.buss.creditcard.immediaterepayment.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.boc.bocsoft.mobile.bocmobile.ApplicationConst;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.MvpBussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;
import com.boc.bocsoft.mobile.bocmobile.base.utils.PublicCodeUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.confirmview.ConfirmInfoView;
import com.boc.bocsoft.mobile.bocmobile.base.widget.operation.ResultHead;
import com.boc.bocsoft.mobile.bocmobile.buss.creditcard.immediaterepayment.model.CrcdBillInfoBean;
import com.boc.bocsoft.mobile.bocmobile.buss.creditcard.immediaterepayment.model.RepaymentInfoBean;
import com.boc.bocsoft.mobile.bocmobile.buss.creditcard.immediaterepayment.model.TransCommissionChargeBean;
import com.boc.bocsoft.mobile.bocmobile.buss.creditcard.immediaterepayment.presenter.RepayPresenter;
import com.boc.bocsoft.mobile.bocmobile.buss.creditcard.immediaterepayment.presenter.RepaymentContract;
import com.boc.bocsoft.mobile.bocmobile.yun.BocCloudCenter;
import com.boc.bocsoft.mobile.bocmobile.yun.other.AccountType;
import com.boc.bocsoft.mobile.common.utils.NumberUtils;
import com.boc.bocsoft.mobile.framework.ui.BasePresenter;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * 作者：xwg on 16/11/23 10:43
 * 还款确认页面
 */
public class RepaymentConfirmFragment extends MvpBussFragment<RepayPresenter> implements RepaymentContract.RepaymentConfirmView {
    private View verifyView;
    private ConfirmInfoView confirmInfoView;
    public static final  String TRANS_COMMISSION_CHARGE_KEY ="TransCommissionChargeBean";
    public static final String REPAYMENT_INFO_KEY ="RepaymentInfoBean";
    private RepaymentInfoBean repaymentInfoBean;

    protected View onCreateView(LayoutInflater mInflater) {
        super.onCreateView(mInflater);
        verifyView = mInflater.inflate(R.layout.fragment_crcd_repayment_verify, null);
        return verifyView;
    }

    @Override
    public void initView() {
        super.initView();
        confirmInfoView = (ConfirmInfoView)verifyView.findViewById(R.id.confirm_view_title);
    }


    @Override
    public void initData() {
        super.initData();

        TransCommissionChargeBean chargeBean=getArguments().getParcelable(TRANS_COMMISSION_CHARGE_KEY);
        repaymentInfoBean=getArguments().getParcelable(REPAYMENT_INFO_KEY);

        confirmInfoView.setHeadValue(getResources().getString(R.string.boc_creditcard_repayment_amount)+"（"+ PublicCodeUtils.getCurrency(mContext,repaymentInfoBean.getCurrency())+"）",
                MoneyUtils.transMoneyFormat(repaymentInfoBean.getAmount(),repaymentInfoBean.getCurrency()),false);
        confirmInfoView.isShowSecurity(false);

        //nameAndVelue 是字段名和取值的hashmap
        LinkedHashMap<String,String> nameAndVelue=new LinkedHashMap<>();
        nameAndVelue.put(getResources().getString(R.string.boc_creditcard_acc_no), NumberUtils.formatCardNumber(repaymentInfoBean.getToAccountNo()));
        nameAndVelue.put(getResources().getString(R.string.boc_creditcard_payway),
                findFragment(RepaymentMainFragment.class).getPaywayString(repaymentInfoBean.getPayway(),repaymentInfoBean.getBillCurrency(),getCashRemit(repaymentInfoBean.getCashRemit()).trim()));
        //如果是人民币购汇
        if (!ApplicationConst.CURRENCY_CNY.equals(repaymentInfoBean.getBillCurrency())&&ApplicationConst.CURRENCY_CNY.equals(repaymentInfoBean.getCurrency())){
            nameAndVelue.put(getResources().getString(R.string.boc_creditcard_payway_rmbtoforeign_currency),
                    PublicCodeUtils.getCurrency(mContext,repaymentInfoBean.getBillCurrency())+getCashRemit(repaymentInfoBean.getCashRemit()));
            nameAndVelue.put(getResources().getString(R.string.boc_creditcard_payway_rmbtoforeign_rate),repaymentInfoBean.getExchangeRate().toString());
        }
        if ("1".equals(chargeBean.getGetChargeFlag())){
            if (chargeBean.getNeedCommissionCharge().doubleValue()>0.0)
                nameAndVelue.put(getResources().getString(R.string.boc_creditcard_standard_expense),MoneyUtils.formatMoney(String.valueOf(chargeBean.getNeedCommissionCharge())));

            nameAndVelue.put(getResources().getString(R.string.trans_commsion_fee),MoneyUtils.formatMoney(String.valueOf(chargeBean.getPreCommissionCharge()))+getResources().getString(R.string.trans_error_commision_fee_tip));
        }
        else
            nameAndVelue.put(getResources().getString(R.string.trans_commsion_fee),getResources().getString(R.string.trans_error_commision_fee_failed));
        nameAndVelue.put(getResources().getString(R.string.boc_creditcard_pay_acc),NumberUtils.formatCardNumber(repaymentInfoBean.getFromAccountNo()));
        confirmInfoView.addData(nameAndVelue,true);

    }

    private String getCashRemit(String cashRemit) {
        if ("00".equals(cashRemit))
            return "";
        return  "01".equals(cashRemit)?getString(R.string.boc_creditcard_unit_cash):getString(R.string.boc_creditcard_unit_remit);
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
        return getResources().getString(R.string.boc_creditcard_repayment_confirm);
    }

    @Override
    public void setListener() {
        super.setListener();
        confirmInfoView.setListener(new ConfirmInfoView.OnClickListener(){
            @Override
            public void onClickConfirm() {
                showLoadingDialog();
                if (repaymentInfoBean.getPayway()==RepaymentMainFragment.PAY_WAY_FOREIGN_RMB_FULL||
                        repaymentInfoBean.getPayway()==RepaymentMainFragment.PAY_WAY_FOREIGN_CUST_CASHREMIT)
                    getPresenter().crcdForeignPayOff(repaymentInfoBean);
                else
                    getPresenter().transferPayoffResult(repaymentInfoBean);
            }

            @Override
            public void onClickChange() {

            }
        });
    }

    @Override
    protected RepayPresenter initPresenter() {
        return new RepayPresenter(this);
    }

    @Override
    public void getTransPayoffResult(RepaymentInfoBean repaymentInfoBean) {
        this.repaymentInfoBean=repaymentInfoBean;
        getPresenter().queryCrcdRTBill(repaymentInfoBean.getToAccountId());

    }

    @Override
    public void queryCrcdRTBillSuccess(List<CrcdBillInfoBean> beanList) {
        closeProgressDialog();

        for (CrcdBillInfoBean bean:beanList){
            if (ApplicationConst.CURRENCY_CNY.equals(bean.getCurrency())){
                if (bean.getHaveNotRepayAmout().doubleValue()<=0)
                    repaymentInfoBean.setLocalBillClear(true);
                else
                    repaymentInfoBean.setLocalBillClear(false);
                if (repaymentInfoBean.isPayLocalBill())
                repaymentInfoBean.setHaveNotRepay(bean.getHaveNotRepayAmout().doubleValue());
            } else {
                if (bean.getHaveNotRepayAmout().doubleValue()<=0)
                    repaymentInfoBean.setForeignBillClear(true);
                else
                    repaymentInfoBean.setForeignBillClear(false);
                if (!repaymentInfoBean.isPayLocalBill())
                    repaymentInfoBean.setHaveNotRepay(bean.getHaveNotRepayAmout().doubleValue());
            }
        }

        RepaymentResultFragment resultFragment=new RepaymentResultFragment();
        Bundle bundle=new Bundle();
        bundle.putParcelable(REPAYMENT_INFO_KEY,repaymentInfoBean);
        resultFragment.setArguments(bundle);

        if (repaymentInfoBean==null){
            resultFragment.setStatus(ResultHead.Status.FAIL);
        }
        resultFragment.setStatus(ResultHead.Status.SUCCESS);
        saveRepayAccount();
        start(resultFragment);
    }

    /**
     *  存储还款账户信息
     */
    private  void saveRepayAccount(){
        if (repaymentInfoBean.isPayLocalBill()){
            //如果本次转账使用的付款账户与本地存储的不一样，则更新本地缓存//
            String accIdFromBackup= BocCloudCenter.getInstance().getUserDict(AccountType.ACC_TYPE_CRCD_LOCAL_REPAY);;//上次还款账户id
            if (!BocCloudCenter.getInstance().getSha256String(repaymentInfoBean.getFromAccountId()).equals(accIdFromBackup)){
                BocCloudCenter.getInstance().updateUserSecretDict
                        (AccountType.ACC_TYPE_CRCD_LOCAL_REPAY,repaymentInfoBean.getFromAccountId());
            }
        }else {
            //如果本次转账使用的付款账户与本地存储的不一样，则更新本地缓存//
            String accIdFromBackup= BocCloudCenter.getInstance().getUserDict(AccountType.ACC_TYPE_CRCD_FOREIGN_REPAY);
            if (!BocCloudCenter.getInstance().getSha256String(repaymentInfoBean.getFromAccountId()).equals(accIdFromBackup)){
                BocCloudCenter.getInstance().updateUserSecretDict
                        (AccountType.ACC_TYPE_CRCD_FOREIGN_REPAY,repaymentInfoBean.getFromAccountId());
            }
        }
    }
    @Override
    public void setPresenter(BasePresenter presenter) {
    }

}

