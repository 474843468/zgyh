package com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.query.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import com.boc.bocsoft.mobile.bocmobile.ApplicationContext;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.widget.edittext.EditChoiceWidget;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.EloanConst;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.batchprepay.ui.query.BatchPrepayQryFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.changeRepaymentAccount.ui.ChangeRepaymentAccountFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.payFunctionSetting.model.PaymentInfo;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.payFunctionSetting.model.PaymentInitBean;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.payFunctionSetting.ui.PayFunctionSettingFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.query.model.EloanStatusListModel;

/**
 * Created by huixiaobo on 2016/6/28.
 * 功能页面
 */
public class EloanMoreOptionFragment extends BussFragment implements View.OnClickListener{
    /**view*/
    private View rootView;
    /**额度详情model*/
    private EloanStatusListModel mEloanDrawModel;
    /**是否显示激活按钮*/
    private boolean isApply;
    /**逾期字段*/
    private String mOverState;


    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        rootView = mInflater.inflate(R.layout.boc_moreopitn_fragment, null);
        return rootView;
    }

    @Override
    public void initView() {
        super.initView();
        EditChoiceWidget eloanApplyLl = (EditChoiceWidget) rootView.findViewById(R.id.eloanApplyLl);
        EditChoiceWidget eloanQuoteLl = (EditChoiceWidget) rootView.findViewById(R.id.eloanQuoteLl);
        EditChoiceWidget eloanAccountLl = (EditChoiceWidget) rootView.findViewById(R.id.eloanAccountLl);
        EditChoiceWidget eloanRapayLl = (EditChoiceWidget) rootView.findViewById(R.id.eloanRapayLl);
        EditChoiceWidget eloanPay = (EditChoiceWidget) rootView.findViewById(R.id.eloanPay);
        eloanPay.setNameWidth();
        eloanQuoteLl.setBottomLineVisibility(true);
        eloanApplyLl.setBottomLineVisibility(true);
        eloanAccountLl.setBottomLineVisibility(true);
        eloanRapayLl.setBottomLineVisibility(true);
        eloanPay.setBottomLineVisibility(true);
        //判断是否显示激活按钮
        if (mEloanDrawModel != null
        		&& mEloanDrawModel.getQuoteState().equals("40")
        		&& isApply) {
            eloanApplyLl.setVisibility(View.VISIBLE);
        } else {
            eloanApplyLl.setVisibility(View.GONE);
        }
        // 判断是否显示提前还款
        if (mEloanDrawModel != null
                && mEloanDrawModel.getAccountListModel() != null
                && mEloanDrawModel.getAccountListModel().getLoanList() != null
                && mEloanDrawModel.getAccountListModel().getLoanList().size() > 0) {
            eloanRapayLl.setVisibility(View.VISIBLE);
        } else {
            eloanRapayLl.setVisibility(View.GONE);
        }
        // 判断支付功能设置是否显示
        if ("10".equals(mEloanDrawModel.getQuoteState())
                || "40".equals(mEloanDrawModel.getQuoteState())
                || "Y".equals(mOverState)) {
            eloanPay.setVisibility(View.GONE);
        } else {
            eloanPay.setVisibility(View.VISIBLE);
        }

        eloanApplyLl.setOnClickListener(this);
        eloanQuoteLl.setOnClickListener(this);
        eloanAccountLl.setOnClickListener(this);
        eloanRapayLl.setOnClickListener(this);
        eloanPay.setOnClickListener(this);
    }

    /**
     * 上页传递对象
     * @param eloanDrawModel 中银E贷对象
     * @param overState 逾期信息
     */
    public void setEloanDrawModel(EloanStatusListModel eloanDrawModel, String overState) {
        mEloanDrawModel = eloanDrawModel;
        mOverState = overState;
    }

    @Override
    public void initData() {
        super.initData();
    }

    @Override
    protected String getTitleValue() {
        return getResources().getString(R.string.boc_eloan_moreoption);
    }

    @Override
    protected void titleLeftIconClick() {
        super.titleLeftIconClick();
        popToAndReInit(EloanDrawFragment.class);
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
    public void onClick(View v) {
       int i = v.getId();
       if (i == R.id.eloanQuoteLl) {
    	   //额度详情
           if (mEloanDrawModel != null) {
               EloanAmountDetailFragment amountDetailFragment = new EloanAmountDetailFragment();
               Bundle bundle = new Bundle();
               bundle.putSerializable(EloanConst.ELOAN_QUOTA , mEloanDrawModel);
               amountDetailFragment.setArguments(bundle);
               start(amountDetailFragment);
           }
       } else if (i == R.id.eloanAccountLl) {
    	   //变更还款账户
    	   if(ApplicationContext.getInstance().getUser().getMobile() == null) {
    		   showErrorDialog(getString(R.string.boc_eloan_mobile));
       		return;
    	   }
           if (mEloanDrawModel != null) {
               ChangeRepaymentAccountFragment accountFragment = new ChangeRepaymentAccountFragment();
               Bundle bundle = new Bundle();
               bundle.putSerializable(EloanConst.ELON_ACCOUNT, mEloanDrawModel);
               accountFragment.setArguments(bundle);
               start(accountFragment);
           }
       } else if (i == R.id.eloanRapayLl) {
           if (mEloanDrawModel == null) {
               return;
           }
           //提前还款
           BatchPrepayQryFragment prepayQryFragment = new BatchPrepayQryFragment();
           Bundle bundle = new Bundle();

           bundle.putSerializable(EloanConst.ELON_QUOTE ,  mEloanDrawModel.getAccountListModel());
           //中银E贷签约类型
           bundle.putString(EloanConst.ELOAN_QUOTETYPE, mEloanDrawModel.getQuoteType());
           //额度编号
           bundle.putString(EloanConst.LOAN_QUOTENO, mEloanDrawModel.getQuoteNo());
           //当前时间
           bundle.putString(EloanConst.DATA_TIME, mEloanDrawModel.getAccountListModel().getEndDate());
           //还款账号
           bundle.putString(EloanConst.PEPAY_ACCOUNT, mEloanDrawModel.getPayAccount());

           prepayQryFragment.setArguments(bundle);
           start(prepayQryFragment);
       } else if (i == R.id.eloanPay) {
           if (mEloanDrawModel == null) {
               return;
           }
           //支付功能设置
           PaymentInitBean paymentBean = new PaymentInitBean();
           paymentBean.setQuoteNo(mEloanDrawModel.getQuoteNo());
           //贷款账号
           if (mEloanDrawModel.getAccountListModel() != null &&
                   mEloanDrawModel.getAccountListModel().getLoanList() != null &&
                   mEloanDrawModel.getAccountListModel().getLoanList().size() > 0) {

               paymentBean.setLoanNo(mEloanDrawModel.getAccountListModel().
                       getLoanList().get(0).getAccountNumber());
           }
           paymentBean.setPayAccount(mEloanDrawModel.getPayAccount());
           paymentBean.setQuoteType(mEloanDrawModel.getQuoteType());
           paymentBean.setRate(mEloanDrawModel.getRate());
           paymentBean.setQuoteFlag("F");
           PaymentInfo paymentInfo=new PaymentInfo();
           paymentInfo.setSignAccountNum(mEloanDrawModel.getQuoteViewModel().getSignAccountNum());
           paymentInfo.setUsePref(mEloanDrawModel.getQuoteViewModel().getUsePref());
           paymentInfo.setSignPeriod(mEloanDrawModel.getQuoteViewModel().getSignPeriod());
           paymentInfo.setPayType(mEloanDrawModel.getQuoteViewModel().getPayType());
           paymentBean.setPaymentInfo(paymentInfo);
           start(PayFunctionSettingFragment.newInstance(paymentBean));
       }
    }

}
