package com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.investtreatymanage.riskassess.presenter;

import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.accountmanagement.model.AccountModel;
import com.boc.bocsoft.mobile.framework.ui.BasePresenter;
import com.boc.bocsoft.mobile.framework.ui.BaseView;

/**
 * Created by guokai on 2016/9/17.
 */
public class RiskAssessContract {
    public interface RiskAssessView extends BaseView<BasePresenter> {
        /**
         * 风险评估查询
         */
        void psnInvtEvaluationInit(String riskLevel);
        /**
         * 风险评估提交回调
         */
        void psnInvtEvaluationResult(String riskLevel);

        AccountModel getModel();
    }

    public interface RiskAssessChoiceView extends BaseView<BasePresenter> {
        /**
         * 风险评估提交回调
         */
        void psnInvtEvaluationResult();


    }

    public interface Presenter extends BasePresenter {
        /**
         * 风险评估查询
         */
        void psnInvtEvaluationInit();

        /**
         * 风险评估提交
         */
        void psnInvtEvaluationResult();
        /**
         * 风险评估提交
         */
        void psnInvtEvaluationResult(String riskScore,String riskAnswer);
    }
}
