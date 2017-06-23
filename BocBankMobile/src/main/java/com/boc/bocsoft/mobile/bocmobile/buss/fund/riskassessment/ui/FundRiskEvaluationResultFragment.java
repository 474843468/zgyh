package com.boc.bocsoft.mobile.bocmobile.buss.fund.riskassessment.ui;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.riskassessment.model.PsnFundRiskEvaluationQueryModel;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.riskassessment.model.PsnFundRiskEvaluationSubmitModel;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.riskassessment.presenter.FundRiskEvaluationContract;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.riskassessment.presenter.FundRiskEvaluationPresenter;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.riskassessment.view.RiskEvaluationResultView;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.utils.DataUtils;
import com.boc.bocsoft.mobile.framework.ui.BasePresenter;

/**
 * Created by taoyongzhen on 2016/11/19.
 */

@SuppressLint("ValidFragment")
public class FundRiskEvaluationResultFragment extends BussFragment implements FundRiskEvaluationContract.RiskEvaluationResultView{

    private View rootView;
    private RiskEvaluationResultView displayView;
    private int requestCode = 0;


    public FundRiskEvaluationResultFragment(Class<? extends BussFragment> fromeClass) {
        this.fromeClass = fromeClass;
    }

    private Class<? extends BussFragment> fromeClass;

    private FundRiskEvaluationPresenter presenter;

    public FundRiskEvaluationResultFragment() {

    }

    public FundRiskEvaluationResultFragment(int requestCode, Class<? extends BussFragment> fromeClass) {
        this.requestCode = requestCode;
        this.fromeClass = fromeClass;
    }


    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        rootView = mInflater.inflate(R.layout.boc_fragment_riskvaluation_result,null);
        return rootView;
    }

    @Override
    public void initView() {
        displayView = (RiskEvaluationResultView) rootView.findViewById(R.id.result_view);
    }

    @Override
    public void initData() {
        displayView.setRequestCode(requestCode);
        presenter = new FundRiskEvaluationPresenter(this);
        showLoadingDialog();
        presenter.fundRiskEvaluationQuery();

    }

    @Override
    protected String getTitleValue() {
        if (requestCode == DataUtils.RISK_ASSESS_ACCOUNT) {
            return getString(R.string.boc_risk_assess_question);
        } else if (requestCode == DataUtils.RISK_ASSESS_CHOICE) {
            return getString(R.string.boc_risk_assess_question_result);
        }
        return getString(R.string.boc_risk_assess_question);
    }

    /**
     * 是否显示右侧标题按钮
     */
    protected boolean isDisplayRightIcon() {
        return false;
    }

    /**
     * 是否显示左侧标题按钮
     */
    protected boolean isDisplayLeftIcon() {
        return true;
    }

    /**
     * 红色主题titleBar：true ；
     * 白色主题titleBar：false ；
     */
    protected boolean getTitleBarRed() {
        return false;
    }

    @Override
    protected void titleLeftIconClick() {
        if (fromeClass != null){
            popToAndReInit(fromeClass);
        }else{
            super.titleLeftIconClick();
        }
    }

    @Override
    public boolean onBack() {
        if (fromeClass != null){
            popToAndReInit(fromeClass);
            return false;
        }
        return super.onBack();
    }

    @Override
    public void onDestroy() {
        presenter.unsubscribe();
        super.onDestroy();
    }

    @Override
    public void setPresenter(BasePresenter presenter) {

    }

    @Override
    public void fundRiskEvaluationInit(PsnFundRiskEvaluationQueryModel model) {
        closeProgressDialog();
        String level = "";
        if (model != null) {
            level = model.getRiskLevel() + "";
        }
        displayView.initViewDate(level);

    }

    @Override
    public void fundRiskEvaluationResult(PsnFundRiskEvaluationSubmitModel model) {
        closeProgressDialog();
        String level = "";
        if (model != null) {
            level = model.getRiskLevel() + "";
        }
        displayView.initViewDate(level);

    }

    @Override
    public void setListener() {
        super.setListener();
        displayView.setRiskEvaluationListener(new RiskEvaluationResultView.RiskEvaluationListener() {
            @Override
            public void riskEvaluationSubmit() {
                showLoadingDialog();
                presenter.fundRiskEvaluationSumbit();
            }

            @Override
            public void riskEvaluationConfirm() {
                if (requestCode == DataUtils.RISK_ASSESS_ACCOUNT) {
                    startWithPop(new FundRiskEvaluationQuestionFragment(DataUtils.RISK_ASSESS_ACCOUNT,fromeClass));
                } else if (requestCode == DataUtils.RISK_ASSESS_CHOICE) {
                    popToAndReInit(fromeClass);
                } else {
                    startWithPop(new FundRiskEvaluationQuestionFragment(fromeClass));
                }
            }
        });
    }
}
