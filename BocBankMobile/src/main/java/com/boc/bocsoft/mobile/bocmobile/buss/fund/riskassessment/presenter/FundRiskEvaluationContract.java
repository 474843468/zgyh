package com.boc.bocsoft.mobile.bocmobile.buss.fund.riskassessment.presenter;

import com.boc.bocsoft.mobile.bocmobile.buss.fund.riskassessment.model.PsnFundRiskEvaluationQueryModel;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.riskassessment.model.PsnFundRiskEvaluationSubmitModel;
import com.boc.bocsoft.mobile.framework.ui.BasePresenter;
import com.boc.bocsoft.mobile.framework.ui.BaseView;

/**
 * Created by taoyongzhen on 2016/11/19.
 */

public class FundRiskEvaluationContract {
    public interface RiskEvaluationResultView extends BaseView<BasePresenter>{
        /**
         * 风险等级查询成功后回调
         * @param model
         */
        void fundRiskEvaluationInit(PsnFundRiskEvaluationQueryModel model);

        /**
         *依据默认分数"0"上报，获取等级回调
         * @param model
         */
        void fundRiskEvaluationResult(PsnFundRiskEvaluationSubmitModel model);


    }

    public interface RiskEvaluationSubmitView extends BaseView<BasePresenter>{
        /**
         * 依据分数提交后，获取等级回调
         */
        void fundRiskEvaluationSubmitResult();
    }


    public interface Presenter extends BasePresenter{
        /**
         *查询风险等级
         *
         */
        void fundRiskEvaluationQuery();

        /**
         * 使用"0"分，提交
         */
        void fundRiskEvaluationSumbit();

        /**
         * 答题完成后，提交答题分数，选项
         * @param model
         */
        void fundRiskEvaluationSumbit(PsnFundRiskEvaluationSubmitModel model);


    }
}
