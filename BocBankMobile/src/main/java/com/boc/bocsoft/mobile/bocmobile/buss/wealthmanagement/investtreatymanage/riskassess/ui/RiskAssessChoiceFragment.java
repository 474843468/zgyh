package com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.investtreatymanage.riskassess.ui;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.activity.MvpBussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.widget.riskassessment.view.RiskEvaluationQuestionView;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.investtreatymanage.riskassess.presenter.RiskAssessContract;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.investtreatymanage.riskassess.presenter.RiskAssessPresenter;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.purchase.ui.PurchaseFragment;
import com.boc.bocsoft.mobile.framework.ui.BasePresenter;


/**
 * 风险评估答题界面
 * Created by guokai on 2016/9/18.
 */
@SuppressLint("ValidFragment")
public class RiskAssessChoiceFragment extends MvpBussFragment<RiskAssessPresenter> implements RiskAssessContract.RiskAssessChoiceView {


    private Class<? extends BussFragment> fromeClass;
    private RiskEvaluationQuestionView questionView;

    public static final int RISK_ASSESS_ACCOUNT = 1;//账户管理

    private int requestCode;

    public RiskAssessChoiceFragment(Class<? extends BussFragment> clazz, int requestCode) {
        this.fromeClass = clazz;
        this.requestCode = requestCode;
    }

    public RiskAssessChoiceFragment(Class<? extends BussFragment> clazz) {
        this.fromeClass = clazz;
    }


    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        return mInflater.inflate(R.layout.risk_assess_choice_fragment, null);
    }

    @Override
    protected String getTitleValue() {
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
        if (fromeClass.isInstance(PurchaseFragment.class)){
            popTo(fromeClass,false);
        }else {
            popToAndReInit(fromeClass);
        }
    }

    @Override
    public boolean onBack() {
        if (fromeClass.isInstance(PurchaseFragment.class)){
            popTo(fromeClass,false);
        }else {
            popToAndReInit(fromeClass);
        }
        return false;
    }

    @Override
    public void initView() {
        questionView = (RiskEvaluationQuestionView) mContentView.findViewById(R.id.risk_evaluation);
    }

    @Override
    public void initData() {
        questionView.initQuestions();
    }

    @Override
    public void setListener() {
        questionView.setFinishedQuestionsListener(new RiskEvaluationQuestionView.FinishedQuestionsListener() {
            @Override
            public void finshedQuestion(String score, String riskAnswer) {
                getPresenter().psnInvtEvaluationResult(score,riskAnswer);
            }
        });
    }


    /**
     * 风险评估完成的回调
     */
    @Override
    public void psnInvtEvaluationResult() {
        closeProgressDialog();
        if (requestCode == RISK_ASSESS_ACCOUNT) {
            startWithPop(new RiskAssessFragment(fromeClass, RiskAssessFragment.RISK_ASSESS_ACCOUNT));
        } else {
            startWithPop(new RiskAssessFragment(fromeClass, RiskAssessFragment.RISK_ASSESS_CHOICE));
        }
    }


    @Override
    public void setPresenter(BasePresenter presenter) {

    }

    @Override
    protected RiskAssessPresenter initPresenter() {
        return new RiskAssessPresenter(this);
    }


}
