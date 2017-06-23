package com.boc.bocsoft.mobile.bocmobile.buss.loan.reloan.query.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLOANListEQuery.PsnLOANListEQueryResult;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.common.loanmanagement.LoanCosnt;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.common.prepay.ui.PrepayFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.common.repayplan.ui.RepayPlanFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.EloanConst;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.payFunctionSetting.model.PaymentInitBean;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.payFunctionSetting.ui.PayFunctionSettingFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.query.model.EloanDrawDetailModel;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.reloan.query.ReloanQryConst;
import com.boc.bocsoft.mobile.common.utils.StringUtils;
import com.boc.bocsoft.mobile.common.utils.date.DateFormatters;

import org.threeten.bp.LocalDate;


/**
 * Created by liuzc on 2016/8/20.
 * 个人循环贷款-功能页面
 */
public class ReloanMoreOptionFragment extends BussFragment implements View.OnClickListener {
    /**
     * view
     */
    private View rootView;

    protected LinearLayout llyUseRecords;  //用款记录
    protected LinearLayout llyPreRepay;    //提前还款
    protected LinearLayout llyRepayPlan;   //还款计划
    protected LinearLayout llyPaySetting;   //支付功能设置

    private PsnLOANListEQueryResult.PsnLOANListEQueryBean mQryResultBean = null;
    private String currentDate = null;
    private String cardNumber = null;

    //客户是否逾期，从074接口获取； 052接口的逾期为该笔记录是否逾期
    private String customerOverdueState = null;

    private String repayType = null; //还款方式

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        rootView = mInflater.inflate(R.layout.boc_fragment_loan_reloan_moreopt, null);
        return rootView;
    }

    @Override
    public void initView() {
        super.initView();
        llyUseRecords = (LinearLayout) rootView.findViewById(R.id.llyUseRecords);
        //业务定 提前还款不显示 2016.11.1
        llyPreRepay = (LinearLayout) rootView.findViewById(R.id.llyPreRepay);
        llyRepayPlan = (LinearLayout) rootView.findViewById(R.id.llyRepayPlan);
        llyPaySetting = (LinearLayout) rootView.findViewById(R.id.llyPaySetting);

        llyUseRecords.setOnClickListener(this);
        llyPreRepay.setOnClickListener(this);
        llyRepayPlan.setOnClickListener(this);
        llyPaySetting.setOnClickListener(this);
    }


    @Override
    public void initData() {
        super.initData();

        Bundle bundle  = getArguments();
        mQryResultBean = (PsnLOANListEQueryResult.PsnLOANListEQueryBean)bundle.getSerializable(
                ReloanQryConst.KEY_STATUS_QRY_RESULT_BEAN);
        currentDate = getArguments().getString(LoanCosnt.LOAN_ENDDATE);
        cardNumber = getArguments().getString(LoanCosnt.LOAN_REPYNUM);
        customerOverdueState = getArguments().getString(LoanCosnt.LOAN_OVERDUE);
        repayType = getArguments().getString(EloanConst.LOAN_REPAYTYPE);

        if(shouldShowPaySetting()){
            llyPaySetting.setVisibility(View.VISIBLE);
        }
        else{
            llyPaySetting.setVisibility(View.GONE);
        }
    }

    /**
     * 是否显示支付功能设置：1046、1047品种的贷款显示
     * @return
     */
    private boolean shouldShowPaySetting(){
        String loanType = mQryResultBean.getLoanType();
        //客户已经逾期，不能进行支付功能设置
        if (isCustomerOverdue()){
            return false;
        }
        if( loanType != null && (loanType.equals("1046") || loanType.equals("1047"))){
            return true;
        }
        return false;
    }

    /**
     * 是否有客户逾期
     * @return
     */
    private boolean isCustomerOverdue(){
        return (!StringUtils.isEmptyOrNull(customerOverdueState) && customerOverdueState.equals("Y"));
    }

    @Override
    protected String getTitleValue() {
        return getResources().getString(R.string.boc_loan_mored);
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
        if (i == R.id.llyUseRecords) { //用款记录
           Bundle bundle = new Bundle();
           bundle.putString(ReloanQryConst.KEY_ACCOUNT_NUMBER, mQryResultBean.getAccountNumber());
           bundle.putString(ReloanQryConst.KEY_CURRENCY, mQryResultBean.getCurrencyCode());
           ReloanUseRecordsFragment fragment = new ReloanUseRecordsFragment();
           fragment.setArguments(bundle);
           start(fragment);
        } else if (i == R.id.llyPreRepay) { //提前还款
           //逾期标识
           boolean bIsOverdue = (mQryResultBean.getOverDueStat() != null &&
                   mQryResultBean.getOverDueStat().equals(ReloanQryConst.OVERDUE_STATUS_OVER));
           //是否有提前还款操作权限
           boolean bCanPreRepay = (mQryResultBean.getTransFlag() != null &&
                   mQryResultBean.getTransFlag().equals(ReloanQryConst.TRANSFLAG_ADVANCE) && isCurDateBeforeMatDate());
           if(!bIsOverdue && bCanPreRepay){
               PrepayFragment prepayFragment = new PrepayFragment();
               Bundle bundle = new Bundle();
               bundle.putSerializable(EloanConst.ELON_PREPAY, mQryResultBean);
               bundle.putString(EloanConst.PEPAY_ACCOUNT, mQryResultBean.getAccountNumber());
               bundle.putString(LoanCosnt.LOAN_REPYNUM, cardNumber);
               prepayFragment.setArguments(bundle);
               start(prepayFragment);
           }
           else{
               showErrorDialog(getResources().getString(R.string.boc_loan_not_support_prepay));
           }
        } else if (i == R.id.llyRepayPlan) { //还款计划
            try{
                RepayPlanFragment fragment = new RepayPlanFragment();
                Bundle bundle = new Bundle();
                EloanDrawDetailModel mDrawDetail = new EloanDrawDetailModel();
                mDrawDetail.setLoanAccount(mQryResultBean.getAccountNumber());
                mDrawDetail.setRemainIssue(Integer.parseInt(mQryResultBean.getRemainIssue()));
                mDrawDetail.setInterestType(mQryResultBean.getInterestType());
                mDrawDetail.setCurrency(mQryResultBean.getCurrencyCode());
                mDrawDetail.setLoanRepayPeriod("");
                
                //贷款状态： 40：结清
                String  loanAccountStatus = mQryResultBean.getLoanAccountStats();

                String pageIndex = "0"; //默认显示还款计划的剩余还款页
                if(mQryResultBean.getOverDueStat() != null && mQryResultBean.getOverDueStat().equals("01")){
                    //已经逾期，进入逾期页
                    pageIndex = "2";
                }
                else{
                    if(loanAccountStatus.equals("40")){
                        //已经结清，进入历史还款页
                        pageIndex = "1";
                    }
                }
                bundle.putSerializable(EloanConst.LOAN_DRAWDETA, mDrawDetail);
                bundle.putSerializable(EloanConst.DATA_TIME, currentDate);
                bundle.putSerializable(EloanConst.DEFAULT_PAGE_INDEX, pageIndex);
                bundle.putString(EloanConst.LOAN_REPAYTYPE, repayType);

                fragment.setArguments(bundle);
                start(fragment);
            }catch (Exception e){
                e.printStackTrace();
            }
        } else if (i == R.id.llyPaySetting) { //支付功能设置
            PaymentInitBean bean = new PaymentInitBean();
            bean.setLoanNo(mQryResultBean.getAccountNumber());
            bean.setRate(mQryResultBean.getLoanCycleRate());
            bean.setPayAccount(cardNumber);
            //// TODO: 2016/10/8
            bean.setQuoteFlag("A");
            bean.setQuoteType("03");
            start(PayFunctionSettingFragment.newInstance(bean));
        }
    }

    /**
     * 判断当前日期是否早于到期日，是否可提前还款会用到
     * 到期日：052接口返回，loanCycleMatDate，如何理解需业务确认，开发人员尚不清楚
     * @return
     */
    private boolean isCurDateBeforeMatDate(){
        try{
            //当前日起
            LocalDate curDate = LocalDate.parse(currentDate, DateFormatters.dateFormatter1);
            //到期日
            LocalDate endDate = LocalDate.parse(mQryResultBean.getLoanCycleMatDate(), DateFormatters.dateFormatter1);
            //到期日早于当期日期
            if(endDate.isBefore(curDate)){
                return false;
            }
        }catch (Exception e){
            e.printStackTrace();
            return true;
        }
        return true;
    }

}
