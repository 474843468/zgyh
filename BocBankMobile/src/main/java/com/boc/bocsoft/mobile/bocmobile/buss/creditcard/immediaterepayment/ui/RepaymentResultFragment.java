package com.boc.bocsoft.mobile.bocmobile.buss.creditcard.immediaterepayment.ui;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;

import com.boc.bocsoft.mobile.bocmobile.ApplicationConst;
import com.boc.bocsoft.mobile.bocmobile.ApplicationContext;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.WebUrl;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;
import com.boc.bocsoft.mobile.bocmobile.base.utils.PublicCodeUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.operation.ResultBottom;
import com.boc.bocsoft.mobile.bocmobile.base.widget.operation.ResultHead;
import com.boc.bocsoft.mobile.bocmobile.buss.common.menu.ModuleActivityDispatcher;
import com.boc.bocsoft.mobile.bocmobile.buss.creditcard.CreditCordovaActivity;
import com.boc.bocsoft.mobile.bocmobile.buss.creditcard.creditcardhomepage.ui.CrcdHomeFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.creditcard.immediaterepayment.model.RepaymentInfoBean;
import com.boc.bocsoft.mobile.bocmobile.buss.creditcard.webplugin.model.AccountInfo;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.base.widget.BaseResultView;
import com.boc.bocsoft.mobile.common.utils.NumberUtils;

import java.util.LinkedHashMap;

/**
 * 作者：xwg on 16/11/23 13:44
 * 还款操作结果页面
 */
public class RepaymentResultFragment extends BussFragment implements ResultBottom.OnClickListener, com.boc.bocsoft.mobile.bocmobile.base.widget.operation.BaseResultView.HomeBackListener
{

    private BaseResultView resultView;
    private ResultHead.Status status;
    private RepaymentInfoBean repaymentInfoBean;

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        resultView = new com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.base.widget.BaseResultView(getContext());
        return resultView;
    }

    @Override
    protected String getTitleValue() {
        return getString(R.string.boc_account_loss_success_title);
    }

    @Override
    public void initView() {
    }

    @Override
    public void initData() {
        super.initData();
//        resultView.setTopDividerVisible(true);

        repaymentInfoBean = getArguments().getParcelable(RepaymentConfirmFragment.REPAYMENT_INFO_KEY);

        //设置头部信息
        if (status==ResultHead.Status.FAIL){
            resultView.addStatus(ResultHead.Status.FAIL, getString(R.string.boc_creditcard_pay_failed));
//            resultView.addTitle("未知原因。。。。。");
            return;
        }

        resultView.addStatus(ResultHead.Status.SUCCESS, getString(R.string.boc_creditcard_pay_success));

        addtitle(repaymentInfoBean.getPayway(),repaymentInfoBean.getAmount(),repaymentInfoBean.getBillCurrency());

        LinkedHashMap<String, String> map = new LinkedHashMap<>();
//        map.put("交易序列号",repaymentInfoBean.getTransactionId());
        map.put(getResources().getString(R.string.boc_creditcard_acc_no), NumberUtils.formatCardNumber(repaymentInfoBean.getToAccountNo()));
        map.put(getResources().getString(R.string.boc_creditcard_payway),
                findFragment(RepaymentMainFragment.class).getPaywayString(
                        repaymentInfoBean.getPayway(),repaymentInfoBean.getBillCurrency(),getCashRemit(repaymentInfoBean.getCashRemit()).trim()));
//        if (repaymentInfoBean.getNeedCommissionCharge().doubleValue()>0.0)
//            nameAndVelue.put(getResources().getString(R.string.boc_creditcard_standard_expense),MoneyUtils.formatMoney(String.valueOf(chargeBean.getNeedCommissionCharge())));
        map.put(getResources().getString(R.string.boc_creditcard_commsion_fee),repaymentInfoBean.getTranFee());
        if (repaymentInfoBean.getPayway()==RepaymentMainFragment.PAY_WAY_FOREIGN_CUST_CASHREMIT
                ||repaymentInfoBean.getPayway()==RepaymentMainFragment.PAY_WAY_FOREIGN_RMB_FULL)
        map.put(getResources().getString(R.string.boc_creditcard_exchenge_acc),NumberUtils.formatCardNumber(repaymentInfoBean.getFromAccountNo()));
        else
        map.put(getResources().getString(R.string.boc_creditcard_pay_acc),NumberUtils.formatCardNumber(repaymentInfoBean.getFromAccountNo()));
        //如果是人民币购汇
        if (!ApplicationConst.CURRENCY_CNY.equals(repaymentInfoBean.getBillCurrency())
                &&ApplicationConst.CURRENCY_CNY.equals(repaymentInfoBean.getCurrency())){
            map.put(getResources().getString(R.string.boc_creditcard_payway_rmbtoforeign_currency),
                    PublicCodeUtils.getCurrency(mContext,repaymentInfoBean.getBillCurrency())+getCashRemit(repaymentInfoBean.getCashRemit()));
            map.put(getResources().getString(R.string.boc_creditcard_payway_rmbtoforeign_rate),repaymentInfoBean.getExchangeRate().toString());
        }
        resultView.addDetail(map);

        //您可能需要

        //如果本币账单未还清，外币账单也未清
        if (!repaymentInfoBean.isLocalBillClear())
            resultView.addNeedItem(getResources().getString(R.string.boc_creditcard_continue_pay,PublicCodeUtils.getCurrency(mContext,ApplicationConst.CURRENCY_CNY)),continuePayLocalListener);

        if (findFragment(RepaymentMainFragment.class).getForeignCurrencyInfo()!=null){
            if (!repaymentInfoBean.isForeignBillClear())
                resultView.addNeedItem(getResources().getString(R.string.boc_creditcard_continue_pay,PublicCodeUtils.getCurrency(mContext,findFragment(RepaymentMainFragment.class).getForeignCurrencyInfo().getCurrency()))
                        ,continuePayForeignListener);
            if (!"ADTE".equals(repaymentInfoBean.getChargeFlag()))
                resultView.addNeedItem(getString(R.string.boc_creditcard_open_mrb_charge),openRMBAccountListener);
            if ("0".equals(repaymentInfoBean.getForeignPayOffStatus()))
                resultView.addNeedItem(getString(R.string.boc_creditcard_open_foreign_bill_autopay),openForeignBillAutoPayListener);
        }
    }



    private void addtitle(int payway, String amount,String currency) {

        switch (payway){
            case RepaymentMainFragment.PAY_WAY_LOCAL_FULL:
                resultView.addTitle("您已全额还款人民币元"+ MoneyUtils.transMoneyFormat(amount,ApplicationConst.CURRENCY_CNY)+"，本期人民币账单已还清");
                break;
            case RepaymentMainFragment.PAY_WAY_LOCAL_MINI_LIMIT:
//                if (repaymentInfoBean.isLocalBillClear())
                resultView.addTitle("您已还款人民币元"+MoneyUtils.transMoneyFormat(amount,ApplicationConst.CURRENCY_CNY));
//                else
//                    resultView.addTitle("您已部分还款人民币元"+amount+"，本期剩余应还金额"+ MoneyUtils.transMoneyFormat(repaymentInfoBean.getHaveNotRepay().toString(),ApplicationConst.CURRENCY_CNY));
                break;
            case RepaymentMainFragment.PAY_WAY_LOCAL_CUST:
                if (repaymentInfoBean.isLocalBillClear())
                    resultView.addTitle("您已还款人民币元"+MoneyUtils.transMoneyFormat(amount,ApplicationConst.CURRENCY_CNY)+"，本期人民币账单已还清");
                else
                    resultView.addTitle("您已还款人民币元"+MoneyUtils.transMoneyFormat(amount,ApplicationConst.CURRENCY_CNY));
                break;
            case RepaymentMainFragment.PAY_WAY_FOREIGN_RMB_FULL:
                resultView.addTitle("您已使用人民币购汇还款人民币元"+ MoneyUtils.transMoneyFormat(amount,ApplicationConst.CURRENCY_CNY)+"本期"+PublicCodeUtils.getCurrency(mContext,currency)+"账单已还清");

                break;
            case RepaymentMainFragment.PAY_WAY_FOREIGN_FULL:
                resultView.addTitle("您已还款"+ PublicCodeUtils.getCurrency(mContext,currency)+getCashRemit(repaymentInfoBean.getCashRemit()).trim()+MoneyUtils.transMoneyFormat(amount,currency)
                        +"本期"+PublicCodeUtils.getCurrency(mContext,currency)+"账单已还清");
                break;
            case RepaymentMainFragment.PAY_WAY_FOREIGN_MINI_LIMIT:
            case RepaymentMainFragment.PAY_WAY_FOREIGN_CUST:
                resultView.addTitle("您已还款"+ PublicCodeUtils.getCurrency(mContext,currency)+getCashRemit(repaymentInfoBean.getCashRemit()).trim()+MoneyUtils.transMoneyFormat(amount,currency));
                break;
            case RepaymentMainFragment.PAY_WAY_FOREIGN_CUST_CASHREMIT:
                resultView.addTitle("您已使用人民币购汇还款人民币元"+MoneyUtils.transMoneyFormat(amount,ApplicationConst.CURRENCY_CNY));
                break;

        }

    }

    public void setStatus(ResultHead.Status status) {
        this.status = status;
    }

    @Override
    public void setListener() {
        resultView.setNeedListener(this);
        resultView.setOnHomeBackClick(this);
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
    public void onHomeBack() {
        ModuleActivityDispatcher.popToHomePage();
    }

    @Override
    public boolean onBack() {
//        popTo(CreditCardHomeFragment.class,false);
        onClickBack();
        return false;
    }
    /**
     * 返回事件处理
     */
    private void onClickBack(){
        if(getFragmentManager().findFragmentByTag(CrcdHomeFragment.class.getName()) != null){
            popToAndReInit(CrcdHomeFragment.class);
        }
        else{
            getActivity().finish();
        }
    }

    private  String getCashRemit(String cashRemit){
        if ("00".equals(cashRemit))
            return "";
        return  "01".equals(cashRemit)?getString(R.string.boc_creditcard_unit_cash):getString(R.string.boc_creditcard_unit_remit);
    }


    private View.OnClickListener continuePayLocalListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            findFragment(RepaymentMainFragment.class).setCurrentIndex(0);
            popToAndReInit(RepaymentMainFragment.class);
        }
    };
    private View.OnClickListener continuePayForeignListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if ( findFragment(RepaymentMainFragment.class).getFragmentListSize()==1)
                findFragment(RepaymentMainFragment.class).setCurrentIndex(0);
            else
                findFragment(RepaymentMainFragment.class).setCurrentIndex(1);
            popToAndReInit(RepaymentMainFragment.class);
        }
    };
    private View.OnClickListener openRMBAccountListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
//            ToastUtils.show("开通全球交易人民币记账");
            startH5Activity(WebUrl.CRCD_RMB_RECORD);
        }
    };
    private View.OnClickListener openForeignBillAutoPayListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
//            ToastUtils.show("开通外币账单自动汇购");
            startH5Activity(WebUrl.CRCD_BILL_AUTO_PURCHASE);
        }
    };

    @Override
    public void onClick(int id) {

    }

    /**
     * H5页面跳转
     */
    private void startH5Activity(String url) {
        Intent intent = new Intent();
        intent.setClass(mActivity, CreditCordovaActivity.class);
        intent.putExtra(CreditCordovaActivity.PARAM_TARGETURL, url);
        intent.putExtra(CreditCordovaActivity.PARAM_OPENFLAG, "0");/**参数 - 全球交易人民币记账功能 开通标示**/
        intent.putExtra(CreditCordovaActivity.PARAM_FOREIGNPAYOFFSTATUS, "0");/**参数 - 外币账单自动购汇设置状态**/
        AccountInfo accountInfo = new AccountInfo();
        accountInfo.setAccountId(repaymentInfoBean.getToAccountId());
        accountInfo.setCreditCardNum(repaymentInfoBean.getToAccountNo());
        accountInfo.setUserName(ApplicationContext.getInstance().getUser().getCustomerName());
        intent.putExtra(CreditCordovaActivity.PARAM_ACCOUNT_INFO, accountInfo);// 传递账户信息
        startActivity(intent);
    }
}
