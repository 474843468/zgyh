package com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.query.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bii.bus.account.model.PsnCommonQueryOprLoginInfo.PsnQueryOprLoginInfoResult;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLOANListEQuery.PsnLOANListEQueryResult;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.ApplicationContext;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.activity.MvpBussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.utils.LogUtils;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.common.loanmanagement.LoanCosnt;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.EloanConst;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.apply.ui.ApplyFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.query.model.EloanAccountListModel;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.query.model.EloanApplyModel;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.query.model.EloanCreditViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.query.model.EloanQuoteViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.query.presenter.EloanqueryPresenter;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.query.ui.view.ApplyView;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.query.ui.view.ExceptionView;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.query.ui.view.InProgressView;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.query.ui.view.RejectView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by huixiaobo on 2016/7/9.
 * 取消关闭 激活页面
 */
public class LoanActivatedFragment extends MvpBussFragment<EloanqueryPresenter> implements View.OnClickListener, EloanQueryContract.View {
    /**
     * view
     */
    protected View rootView;
    /**覆盖层*/
    protected FrameLayout rplContent;
    /** 申请激活view */
    private ApplyView mApplyView;
    /** 审批中view */
    private InProgressView mInProgressView;
    /** 审批拒绝view */
    private RejectView mRejectView;
    /**激活页面model*/
    private EloanApplyModel mEloanApplyModel;
   /***/
   private EloanAccountListModel mEaccountModel;
   /**测试错误提示码提示异常*/
   private static final String ELOAN_ERRORCODE = "BANCS.0188";
    /**逾期常量,默认不逾期*/
    private static final String LOAN_OVERDUE = "N";
    /**逾期信息*/
    private String mOverdue;


    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        rootView = mInflater.inflate(R.layout.fragment_eloan_status, null);
        return rootView;
    }

    /**
     * 接收上个页面传递的值
     * @param overdue 逾期信息
     */
    public static LoanActivatedFragment newInstance(String overdue) {
        LoanActivatedFragment ActivatedFragment = new LoanActivatedFragment();
        Bundle bundle = new Bundle();
        bundle.putString(LoanCosnt.LOAN_OVERDUE, overdue);
        ActivatedFragment.setArguments(bundle);
        return ActivatedFragment;
    }

    @Override
    public void initView() {
        super.initView();
        rplContent = (FrameLayout) rootView.findViewById(R.id.fl_content);
        mApplyView = new ApplyView(getContext());
        TextView activatedTv = (TextView) mApplyView.findViewById(R.id.activatedTv);
        activatedTv.setOnClickListener(this);
    }

    @Override
    public void initData() {
        super.initData();
        //逾期信息
        mOverdue = getArguments().getString(LoanCosnt.LOAN_OVERDUE);
        showLoadingDialog();
        //查询1046 或 1047 接口
        getPresenter().queryCycleLoan();

    }

    @Override
    public void reInit() {
        super.reInit();
        rplContent.removeAllViews();
        showLoadingDialog();
        getPresenter().queryCycleLoan();
        showLoadingDialog();
    }

    @Override
    protected boolean getTitleBarRed() {
        return false;
    }

    @Override
    protected boolean isDisplayRightIcon() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    protected String getTitleValue() {
        return getResources().getString(R.string.boc_eloan_middleTitle);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        //激活
        if (i == R.id.activatedTv) {
            //判断电话号码
            if (ApplicationContext.getInstance().getUser().getMobile() == null) {
                showErrorDialog(getString(R.string.boc_eloan_mobile));
                return;
            }
            //判断证件类型 逾期
            if (mEloanApplyModel != null) {
                if (ApplicationContext.getInstance().getUser().getIdentityType().equals("1")
                        || ApplicationContext.getInstance().getUser().getIdentityType().equals("01")
                        && ApplicationContext.getInstance().getUser().getIdentityNumber().length() >= 18) {
                    if (LOAN_OVERDUE.equals(mOverdue)) {
                        ApplyFragment applyFragment = new ApplyFragment();
                        Bundle bundle = new Bundle();
                        bundle.putSerializable(EloanConst.ELON_APPLY, mEloanApplyModel);
                        applyFragment.setArguments(bundle);
                        start(applyFragment);
                    } else {
                        showErrorDialog(getString(R.string.boc_eloan_accountover));
                    }

                } else {
                    showErrorDialog(getString(R.string.boc_apply_account));
                }
            }
        }
    }

    /**
     * 052查询个人循环贷款失败方法
     */
    @Override
    public void eCycleLoanFail(BiiResultErrorException biiResultErrorException) {
        if (biiResultErrorException.getErrorCode().equals(ELOAN_ERRORCODE)) {
            //没有个人循环贷款调用 046接口预授信额度
            getPresenter().queryCredit();

        } else {
            showErrorDialog(biiResultErrorException.getErrorMessage());
            closeProgressDialog();
        }

    }

    /**
     * 052查询个人循环贷款成功方法
     */
    @Override
    public void eCycleLoanSuccess(EloanAccountListModel cloanResult) {
        if (cloanResult == null) {
            return;
        }

        mEaccountModel = new EloanAccountListModel();
        mEaccountModel = cloanResult;
        if (cloanResult.getLoanList() != null && cloanResult.getLoanList().size() > 0) {
            List<EloanAccountListModel.PsnLOANListEQueryBean> cycleLoanList =
                    filterLoanList(cloanResult.getLoanList());
            if (cycleLoanList != null && cycleLoanList.size() > 0) {
                closeProgressDialog();
                showErrorDialog(getString(R.string.boc_eloan_project));
            } else {
                getPresenter().queryCredit();
            }

        } else {
            getPresenter().queryCredit();
        }
    }

    /**
     * 046预授信额度失败
     */
    @Override
    public void eCreditFail(BiiResultErrorException biiResultErrorException) {
        closeProgressDialog();
    }

    /**
     * 046预授信额度成功
     */
    @Override
    public void eCreditSuccess(EloanCreditViewModel ecreditResult) {
        if (ecreditResult == null) {
            return;
        }

        //个人信息保存页面
        mEloanApplyModel = new EloanApplyModel();
        mEloanApplyModel.setIdentityNumber(ApplicationContext.getInstance().getUser().getIdentityNumber());
        mEloanApplyModel.setIdentityType(ApplicationContext.getInstance().getUser().getIdentityType());
        mEloanApplyModel.setCustomerName(ApplicationContext.getInstance().getUser().getCustomerName());
        mEloanApplyModel.setMobile(ApplicationContext.getInstance().getUser().getMobile());
        mEloanApplyModel.setQuote(ecreditResult.getQuote());
        mEloanApplyModel.setCurrency("CNY");

        //未激活
        if (!TextUtils.isEmpty(ecreditResult.getQuoteState())) {
            if (ecreditResult.getQuoteState().equals("01")) {

                // 页面显示数据
                rplContent.removeAllViews();
                mApplyView.putApplyData(mEloanApplyModel);
                rplContent.addView(mApplyView);
                closeProgressDialog();

            }
            //审批中
            else if (ecreditResult.getQuoteState().equals("02")) {

                rplContent.removeAllViews();
                mInProgressView = new InProgressView(getContext());
                rplContent.addView(mInProgressView);
                closeProgressDialog();

            }
            //已激活
            else if (ecreditResult.getQuoteState().equals("03")) {
                closeProgressDialog();
                exceptionView(getString(R.string.boc_eloan_stutascanle));

            }
            //已拒绝
            else if (ecreditResult.getQuoteState().equals("04")) {
                rplContent.removeAllViews();
                mRejectView = new RejectView(getContext());
                rplContent.addView(mRejectView);
                closeProgressDialog();

            }
            //不存在状态
            else if (ecreditResult.getQuoteState().equals("05")) {
                closeProgressDialog();
                exceptionView(getString(R.string.boc_eloan_usercall));
            }
        } else {
            closeProgressDialog();
            exceptionView(getString(R.string.boc_eloan_credit));
        }


    }


    @Override
    public void setPresenter(EloanQueryContract.Presenter presenter) {

    }

    /**
     * 过滤个人循环贷款的 1047 和 1067 list
     */
    private List<EloanAccountListModel.PsnLOANListEQueryBean> filterLoanList
    (List<EloanAccountListModel.PsnLOANListEQueryBean> cycleList) {

        List<EloanAccountListModel.PsnLOANListEQueryBean> cycleLoanList = new ArrayList
                <EloanAccountListModel.PsnLOANListEQueryBean>();
        if (cycleList != null && cycleList.size() > 0) {
            for (EloanAccountListModel.PsnLOANListEQueryBean loanList : cycleList) {
                if (loanList.getLoanAccountStats().equals("5")
                        || loanList.getLoanAccountStats().equals("7")
                        || loanList.getLoanAccountStats().equals("12")
                        || loanList.getLoanAccountStats().equals("8")
                        || loanList.getLoanAccountStats().equals("50")
                        || !loanList.getLoanCycleBalance().equals("0")) {
                    if (loanList.getCycleFlag().equals("1")
                            || loanList.getCycleFlag().equals("01")) {

                        cycleLoanList.add(loanList);
                    }
                }
            }
        }
        return cycleLoanList;
    }


    /**
     * 错误信息页面
     */
    private void exceptionView(String exceptionMsg) {
        if (!TextUtils.isEmpty(exceptionMsg)) {
            rplContent.removeAllViews();
            ExceptionView mExceptionView = new ExceptionView(getContext());
            mExceptionView.putData(exceptionMsg);
            rplContent.addView(mExceptionView);
        }
    }

    @Override
    protected EloanqueryPresenter initPresenter() {
        return new EloanqueryPresenter(this);
    }
}
