package com.boc.bocsoft.mobile.bocmobile.buss.fund.riskassessment.ui;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.widget.riskassessment.view.RiskEvaluationQuestionView;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.riskassessment.model.PsnFundRiskEvaluationSubmitModel;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.riskassessment.presenter.FundRiskEvaluationContract;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.riskassessment.presenter.FundRiskEvaluationPresenter;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.utils.DataUtils;
import com.boc.bocsoft.mobile.framework.ui.BasePresenter;

/**
 * Created by taoyongzhen on 2016/11/19.
 *
 */
@SuppressLint("ValidFragment")
public class FundRiskEvaluationQuestionFragment extends BussFragment implements FundRiskEvaluationContract.RiskEvaluationSubmitView{

    private View rootView;
    private RiskEvaluationQuestionView questionView;
    private int requestCode = 0;

    private Class<? extends BussFragment> fromeClass;
    private FundRiskEvaluationPresenter presenter;


    public FundRiskEvaluationQuestionFragment() {
    }

    public FundRiskEvaluationQuestionFragment(Class<? extends BussFragment> fromeClass) {
        this.fromeClass = fromeClass;
    }

    public FundRiskEvaluationQuestionFragment(int requestCode, Class<? extends BussFragment> fromeClass) {
        this.requestCode = requestCode;
        this.fromeClass = fromeClass;
    }
    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        rootView = mInflater.inflate(R.layout.boc_fragment_riskevaluation_question,null);
        return rootView;

    }

    @Override
    public void initView() {
        super.initView();
        questionView = (RiskEvaluationQuestionView) rootView.findViewById(R.id.risk_evaluation);

    }

    @Override
    public void initData() {
        super.initData();
        presenter = new FundRiskEvaluationPresenter(this);
        //获取题目内容
        questionView.initQuestions();

    }


    @Override
    public void setListener() {
        super.setListener();
        questionView.setFinishedQuestionsListener(new RiskEvaluationQuestionView.FinishedQuestionsListener() {
            @Override
            public void finshedQuestion(String score, String riskAnswer) {
                showLoadingDialog();
                PsnFundRiskEvaluationSubmitModel model = new PsnFundRiskEvaluationSubmitModel();
                model.setRiskScore(score);
                model.setRiskAnswer(riskAnswer);
                presenter.fundRiskEvaluationSumbit(model);
            }
        });
    }

    @Override
    protected String getTitleValue() {
        return getString(R.string.boc_risk_assess_question);
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
    public void fundRiskEvaluationSubmitResult() {
        closeProgressDialog();
        if (requestCode == DataUtils.RISK_ASSESS_ACCOUNT) {
            startWithPop(new FundRiskEvaluationResultFragment(DataUtils.RISK_ASSESS_ACCOUNT,fromeClass));
        } else {
            startWithPop(new FundRiskEvaluationResultFragment(DataUtils.RISK_ASSESS_CHOICE,fromeClass));
        }
    }

    @Override
    public void setPresenter(BasePresenter presenter) {

    }

    @Override
    public void pop() {
        super.pop();
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
    public void onDestroy() {
        presenter.unsubscribe();
        super.onDestroy();
    }
}
