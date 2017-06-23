package com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.investtreatymanage.investtreaty.presenter;

import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.investtreatymanage.investtreaty.model.InvestTreatyConfirmModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.investtreatymanage.investtreaty.model.InvestTreatyInfoModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.investtreatymanage.investtreaty.model.InvestTreatyModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.shareconversion.model.PsnXpadQueryRiskMatch.PsnXpadQueryRiskMatchReqModel;
import com.boc.bocsoft.mobile.framework.ui.BasePresenter;
import com.boc.bocsoft.mobile.framework.ui.BaseView;

import java.util.List;

/**
 * 投资协议管理界面回调
 * Created by guokai on 2016/9/7.
 */
public class InvestTreatyContract {

    public interface InvestTreatyView extends BaseView<BasePresenter> {


        /**
         * 协议列表回调
         */
        void psnXpadCapacityQuery(InvestTreatyModel treatyModel);

        /**
         * 协议列表回调 失败
         */
        void psnXpadCapacityQueryFailed();

        InvestTreatyModel getModel();
    }

    public interface InvestTreatyInfoView extends BaseView<BasePresenter> {
        /**
         * 协议明细查询回调
         */
        void psnXpadAgreementInfoQuery(InvestTreatyInfoModel model);

        /**
         * 终止协议回调
         */
        void psnXpadInvestAgreementCancel(InvestTreatyInfoModel infoModel);

        /**
         * 投资协议交易明细查询回调
         */
        void psnXpadCapacityTransList(List<InvestTreatyInfoModel> tradeList);

        /**
         * 投资协议交易明细查询回调 失败
         */
        void psnXpadCapacityTransListFailed();

        /**
         * 查询客户风险等级与产品风险等级是否匹配
         */
        void psnXpadQueryRiskMatch(PsnXpadQueryRiskMatchReqModel reqModel);


        /**
         * 业绩基准周期滚续协议终止回调
         */
        void psnXpadBenchmarkMaintainResult(InvestTreatyConfirmModel infoModel);
    }

    public interface InvestTreatyConfirmView extends BaseView<BasePresenter> {

        /**
         * 投资协议修改预交易
         */
        void pnsXpadInvestAgreementModifyVerify();

        /**
         * 投资协议修改提交
         */
        void psnXpadInvestAgreementModifyCommit(InvestTreatyConfirmModel infoModel);

        /**
         * 协议修改结果
         */
        void psnXpadAgreementModifyResult(InvestTreatyConfirmModel infoModel);

        /**
         * 协议维护
         */
        void psnXpadAutomaticAgreementMaintainResult(InvestTreatyConfirmModel infoModel);

        /**
         * 业绩基准周期滚续协议
         */
        void psnXpadBenchmarkMaintainResult(InvestTreatyConfirmModel infoModel);
    }

    public interface Presenter extends BasePresenter {
//        /**
//         * 查询客户理财账户信息
//         */
//        void psnXpadAccountQuery();

        /**
         * 协议查询
         */
        void psnXpadCapacityQuery(InvestTreatyModel model);

        /**
         * 协议明细查询
         */
        void psnXpadAgreementInfoQuery(InvestTreatyModel.CapacityQueryBean model);

        /**
         * 终止协议
         *
         * @param model
         */
        void psnXpadInvestAgreementCancel(InvestTreatyInfoModel infoModel, InvestTreatyModel.CapacityQueryBean model);

        /**
         * 投资协议交易明细查询
         */
        void psnXpadCapacityTransList(InvestTreatyModel.CapacityQueryBean model);

        /**
         * 查询客户风险等级与产品风险等级是否匹配
         */
        void psnXpadQueryRiskMatch(InvestTreatyModel.CapacityQueryBean entity, InvestTreatyInfoModel infoModel);

        /**
         * 投资协议修改预交易
         */
        void pnsXpadInvestAgreementModifyVerify(InvestTreatyConfirmModel infoModel, InvestTreatyModel.CapacityQueryBean model);

        /**
         * 投资协议修改提交
         */
        void psnXpadInvestAgreementModifyCommit(InvestTreatyConfirmModel infoModel, InvestTreatyModel.CapacityQueryBean model);

        /**
         * 协议修改结果
         */
        void psnXpadAgreementModifyResult(InvestTreatyConfirmModel infoModel, InvestTreatyModel.CapacityQueryBean model);

        /**
         * 协议维护
         */
        void psnXpadAutomaticAgreementMaintainResult(InvestTreatyConfirmModel infoModel, InvestTreatyModel.CapacityQueryBean model);

        /**
         * 业绩基准周期滚续协议修改
         */
        void psnXpadBenchmarkMaintainResult(InvestTreatyConfirmModel infoModel, InvestTreatyModel.CapacityQueryBean model, String opt);
    }
}
