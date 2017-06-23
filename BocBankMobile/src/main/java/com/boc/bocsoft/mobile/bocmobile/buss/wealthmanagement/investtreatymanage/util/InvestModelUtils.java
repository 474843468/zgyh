package com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.investtreatymanage.util;

import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PnsXpadInvestAgreementModifyVerify.PsnXpadInvestAgreementModifyVerifyParams;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PnsXpadInvestAgreementModifyVerify.PsnXpadInvestAgreementModifyVerifyResult;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnInvtEvaluationInit.PsnInvtEvaluationInitResult;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadAgreementInfoQuery.PsnXpadAgreementInfoQueryResult;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadCapacityQuery.PsnXpadCapacityQueryParams;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadCapacityQuery.PsnXpadCapacityQueryResult;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadCapacityTransList.PsnXpadCapacityTransListResult;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadInvestAgreementCancel.PsnXpadInvestAgreementCancelParams;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.investtreatymanage.investtreaty.model.InvestTreatyConfirmModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.investtreatymanage.investtreaty.model.InvestTreatyInfoModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.investtreatymanage.investtreaty.model.InvestTreatyModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.investtreatymanage.investtreaty.model.VerifyModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.investtreatymanage.riskassess.model.RiskAssessModel;
import com.boc.bocsoft.mobile.common.utils.BeanConvertor.BeanConvertor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by guokai on 2016/9/20.
 */
public class InvestModelUtils {
    /***************************** 投资协议管理 *****************************/

    /**
     * 生成投资协议的请求参数
     *
     * @return
     */
    public static PsnXpadCapacityQueryParams generateInvestTreatyParams(InvestTreatyModel model, String conversationId) {
        PsnXpadCapacityQueryParams params = new PsnXpadCapacityQueryParams();
        params.setAgrType(model.getAgrType());
        params.setAgrState(model.getAgrState());
        params.setPageSize(model.getPageSize());
        params.setCurrentIndex(model.getCurrentIndex());
        params.set_refresh(model.get_refresh());
        params.setConversationId(conversationId);
        return params;
    }

    /**
     * 生成InvestTreatyModel，设置参数
     *
     * @param result
     * @return
     */
    public static InvestTreatyModel generateInvestTreatyModel(PsnXpadCapacityQueryResult result) {
        List<PsnXpadCapacityQueryResult.CapacityQueryBean> list = result.getList();
        List<InvestTreatyModel.CapacityQueryBean> modelList = new ArrayList<InvestTreatyModel.CapacityQueryBean>();
        InvestTreatyModel treatyModel = new InvestTreatyModel();
        for (PsnXpadCapacityQueryResult.CapacityQueryBean bean : list) {
            InvestTreatyModel.CapacityQueryBean treatyBean = new InvestTreatyModel.CapacityQueryBean();
            BeanConvertor.toBean(bean, treatyBean);
            modelList.add(treatyBean);
        }
        treatyModel.setList(modelList);
        return treatyModel;
    }

    /**
     * 生成InvestTreatyInfoModel
     *
     * @param result
     * @return
     */
    public static InvestTreatyInfoModel generateInvestTreatyInfoModel(PsnXpadAgreementInfoQueryResult result) {
        InvestTreatyInfoModel model = new InvestTreatyInfoModel();
        model.setProId(result.getProId());
        model.setProName(result.getProName());
        model.setProCur(result.getProCur());
        model.setAgrType(result.getAgrType());
        model.setInstType(result.getInstType());
        model.setAgrCode(result.getAgrCode());
        model.setAgrName(result.getAgrName());
        model.setTradeCode(result.getTradeCode());
        model.setPeriodtotal(result.getPeriodtotal());
        model.setPeriod(result.getPeriod());
        model.setPeriodAge(result.getPeriodAge());
        model.setMininsperiod(result.getMininsperiod());
        model.setOneperiod(result.getOneperiod());
        model.setPeriodpur(result.getPeriodpur());
        model.setFirstdatepur(result.getFirstdatepur());
        model.setPeriodred(result.getPeriodred());
        model.setFirstdatered(result.getFirstdatered());
        model.setRate(result.getRate());
        model.setRatedetail(result.getRatedetail());
        model.setAgrPurstart(result.getAgrPurstart());
        model.setFirstBreakPromise(result.getFirstBreakPromise());
        model.setAmountType(result.getAmountType());
        model.setAmount(result.getAmount());
        model.setMinAmount(result.getMinAmount());
        model.setMaxAmount(result.getMaxAmount());
        model.setUnit(result.getUnit());
        model.setUnunitBalpur(result.getUnunitBalpur());
        model.setBuyPeriod(result.getBuyPeriod());
        model.setFinishperiod(result.getFinishperiod());
        model.setRemaindperiod(result.getRemaindperiod());
        model.setCanUpdate(result.getCanUpdate());
        model.setCanCancel(result.getCanCancel());
        model.setIsbenchmark(result.getIsbenchmark());
        model.setIsneedpur(result.getIsneedpur());
        model.setIsneedred(result.getIsneedred());
        model.setCashRemit(result.getCashRemit());
        model.setProductKind(result.getProductKind());
        model.setSerialCode(result.getSerialCode());
        return model;
    }

    /**
     * 终止协议
     *
     * @param model
     * @return
     */
    public static PsnXpadInvestAgreementCancelParams generateInvestTreatyCancelModel(InvestTreatyInfoModel infoModel, InvestTreatyModel.CapacityQueryBean model, String token, String conversationId) {
        PsnXpadInvestAgreementCancelParams params = new PsnXpadInvestAgreementCancelParams();
        params.setAccountKey(model.getAccountKey());
        params.setCustAgrCode(model.getCustAgrCode());
        params.setAgrType(infoModel.getAgrType());
        params.setProductName(infoModel.getProName());
        params.setTotalPeriod(infoModel.getPeriodtotal());
        params.setAmountType(infoModel.getAmountType());
        params.setToken(token);
        params.setConversationId(conversationId);
        return params;
    }

    /**
     * 风险评估参数设置
     *
     * @param result
     * @return
     */
    public static RiskAssessModel generateRiskAssessModel(PsnInvtEvaluationInitResult result) {
        RiskAssessModel model = new RiskAssessModel();
        model.setRiskLevel(result.getRiskLevel());
        return model;
    }

    /**
     * 生成InvestTreatyModel，设置参数
     *
     * @param results
     * @return
     */
    public static List<InvestTreatyInfoModel> generateTradeInfoModel(List<PsnXpadCapacityTransListResult> results) {
        List<InvestTreatyInfoModel> tradeList = new ArrayList<>();
        for (PsnXpadCapacityTransListResult result : results) {
            InvestTreatyInfoModel tradeModel = new InvestTreatyInfoModel();
            tradeModel.setCustAgrCode(result.getCustAgrCode());
            tradeModel.setAgrType(result.getAgrType());
            tradeModel.setInstType(result.getInstType());
            tradeModel.setTdsDate(result.getTdsDate());
            tradeModel.setMemo(result.getMemo());
            tradeModel.setTdsType(result.getTdsType());
            tradeModel.setTdsAmt(result.getTdsAmt());
            tradeModel.setTdsUnit(result.getTdsUnit());
            tradeModel.setTdsState(result.getTdsState());
            tradeList.add(tradeModel);
        }
        return tradeList;
    }


    /**
     * 投资协议修改预交易
     *
     * @return
     */
    public static PsnXpadInvestAgreementModifyVerifyParams generatePnsXpadInvestAgreementModifyVerifyParams(InvestTreatyConfirmModel infoModel, InvestTreatyModel.CapacityQueryBean model, String token, String conversationId) {
        PsnXpadInvestAgreementModifyVerifyParams params = new PsnXpadInvestAgreementModifyVerifyParams();
        params.setAccountKey(model.getAccountKey());
        params.setAgrCode(infoModel.getAgrCode());
        params.setCustAgrCode(model.getCustAgrCode());
        params.setAmountType(infoModel.getAmountType());
        params.setAmount(infoModel.getAmount());
        params.setMinAmount(infoModel.getMinAmount());
        params.setMaxAmount(infoModel.getMaxAmount());
        params.setUnit(infoModel.getUnit());
        params.setIsControl("0");
        params.setTotalPeriod(infoModel.getBuyPeriod());
        params.setCharCode(infoModel.getCashRemit());
        params.setToken(token);
        params.setConversationId(conversationId);
        return params;
    }
    /**
     * 投资协议修改预交易成功
     *
     * @return
     */
    public static void generateParamsToInvestInfoModel(PsnXpadInvestAgreementModifyVerifyResult result) {
        VerifyModel infoModel = new VerifyModel();
        infoModel.setAccNo(result.getAccNo());
        infoModel.setAgrCode(result.getAgrCode());
        infoModel.setAgrName(result.getAgrName());
        infoModel.setPeriodPur(result.getPeriodPur());
        infoModel.setFirstDatePur(result.getFirstDatePur());
        infoModel.setPeriodRed(result.getPeriodRed());
        infoModel.setFirstDateRed(result.getFirstDateRed());
        infoModel.setAmount(result.getAmount());
        infoModel.setMinAmount(result.getMinAmount());
        infoModel.setMaxAmount(result.getMaxAmount());
        infoModel.setPeriodAgr(result.getPeriodAgr());
        infoModel.setIsControl(result.getIsControl());
        infoModel.setTotalPeriod(result.getTotalPeriod());
        infoModel.setEndDate(result.getEndDate());
        infoModel.setIsNeedRed(result.getIsNeedRed());
        infoModel.setIsNeedPur(result.getIsNeedPur());
    }

}
