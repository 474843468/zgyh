package com.boc.bocsoft.mobile.bocmobile.buss.fund.fundproduct.home.ui;

import com.boc.bocsoft.mobile.bii.bus.account.model.PsnQueryInvtBindingInfo.PsnQueryInvtBindingInfoParams;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundRiskEvaluationQuery.PsnFundRiskEvaluationQueryParams;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundRiskEvaluationQuery.PsnFundRiskEvaluationQueryResult;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnInvestmentManageIsOpen.PsnInvestmentManageIsOpenParams;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.fundproduct.home.model.InvstBindingInfoViewModel;
import com.boc.bocsoft.mobile.framework.ui.BasePresenter;
import com.boc.bocsoft.mobile.framework.ui.BaseView;

/**
 * 基金，账户相关接口：是否开通投资服务、绑定资金账户、进行风险测评等
 * Created by liuzc on 2016/12/27.
 */
public class FundUserContract {

    public interface View extends BaseView<Presenter>{
        //判断是否开通投资服务成功
        void queryInvestmentManageIsOpenSuccess(Boolean result);
        //判断是否开通投资服务失败
        void queryInvestmentManageIsOpenFail(BiiResultErrorException biiResultErrorException);

        //查询投资交易账号绑定信息成功
        void queryInvtBindingInfoSuccess(InvstBindingInfoViewModel result);
        //查询投资交易账号绑定信息失败
        void queryInvtBindingInfoFail(BiiResultErrorException biiResultErrorException);

        //查询风险评估等级成功
        void queryFundRiskEvaluationSuccess(PsnFundRiskEvaluationQueryResult result);
        //查询风险评估等级失败
        void queryFundRiskEvaluationFail(BiiResultErrorException biiResultErrorException);
    }

    public interface Presenter extends BasePresenter{
        /**
         * 判断是否开通投资服务
         * @param params
         */
        void  queryInvestmentManageIsOpen(PsnInvestmentManageIsOpenParams params);

        /**
         *查询投资交易账号绑定信息
         * @param params
         */
        void queryInvtBindingInfo(PsnQueryInvtBindingInfoParams params);

        /**
         * 查询风险评估等级
         * @param params
         */
        void queryFundRiskEvaluation(PsnFundRiskEvaluationQueryParams params);
    }
}
