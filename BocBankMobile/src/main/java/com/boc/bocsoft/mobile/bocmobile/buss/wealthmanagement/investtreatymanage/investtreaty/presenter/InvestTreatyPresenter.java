package com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.investtreatymanage.investtreaty.presenter;

import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PnsXpadInvestAgreementModifyVerify.PsnXpadInvestAgreementModifyVerifyResult;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadAgreementInfoQuery.PsnXpadAgreementInfoQueryParams;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadAgreementInfoQuery.PsnXpadAgreementInfoQueryResult;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadAgreementModifyResult.PsnXpadAgreementModifyResultParams;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadAgreementModifyResult.PsnXpadAgreementModifyResultResult;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadAutomaticAgreementMaintainResult.PsnXpadAutomaticAgreementMaintainResultParams;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadAutomaticAgreementMaintainResult.PsnXpadAutomaticAgreementMaintainResultResult;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadBenchmarkMaintainResult.PsnXpadBenchmarkMaintainResultParams;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadBenchmarkMaintainResult.PsnXpadBenchmarkMaintainResultResult;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadCapacityQuery.PsnXpadCapacityQueryResult;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadCapacityTransList.PsnXpadCapacityTransListParams;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadCapacityTransList.PsnXpadCapacityTransListResult;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadInvestAgreementCancel.PsnXpadInvestAgreementCancelResult;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadInvestAgreementModifyCommit.PsnXpadInvestAgreementModifyCommitParams;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadInvestAgreementModifyCommit.PsnXpadInvestAgreementModifyCommitResult;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadQueryRiskMatch.PsnXpadQueryRiskMatchParams;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadQueryRiskMatch.PsnXpadQueryRiskMatchResult;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.service.WealthManagementService;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.buss.account.base.BaseAccountPresenter;
import com.boc.bocsoft.mobile.bocmobile.buss.account.base.BaseAccountSubscriber;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.investtreatymanage.investtreaty.model.InvestTreatyConfirmModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.investtreatymanage.investtreaty.model.InvestTreatyInfoModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.investtreatymanage.investtreaty.model.InvestTreatyModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.investtreatymanage.util.InvestModelUtils;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.shareconversion.model.PsnXpadQueryRiskMatch.PsnXpadQueryRiskMatchReqModel;
import com.boc.bocsoft.mobile.framework.rx.SchedulersCompat;

import java.util.List;

import rx.Observable;
import rx.functions.Func1;

/**
 * 投资协议管理
 * Created by guokai on 2016/9/7.
 */
public class InvestTreatyPresenter extends BaseAccountPresenter implements InvestTreatyContract.Presenter {

    private WealthManagementService wealthManagementService;
    private InvestTreatyContract.InvestTreatyView investTreatyView;
    private InvestTreatyContract.InvestTreatyInfoView investTreatyInfoView;
    private InvestTreatyContract.InvestTreatyConfirmView investTreatyConfirmView;

    private InvestTreatyModel model;

    public InvestTreatyPresenter(InvestTreatyContract.InvestTreatyView investTreatyView) {
        this.investTreatyView = investTreatyView;
        wealthManagementService = new WealthManagementService();
        model = investTreatyView.getModel();
    }

    public InvestTreatyPresenter(InvestTreatyContract.InvestTreatyInfoView investTreatyInfoView) {
        this.investTreatyInfoView = investTreatyInfoView;
        wealthManagementService = new WealthManagementService();
    }

    public InvestTreatyPresenter(InvestTreatyContract.InvestTreatyConfirmView investTreatyConfirmView) {
        this.investTreatyConfirmView = investTreatyConfirmView;
        wealthManagementService = new WealthManagementService();
    }


//    /**
//     * 查询客户理财账户信息
//     */
//    public void psnXpadAccountQuery() {
//        ((BussFragment) investTreatyView).showLoadingDialog();
//        PsnXpadAccountQueryParams params = new PsnXpadAccountQueryParams();
//        params.setQueryType("0");
//        params.setXpadAccountSatus("1");
//        wealthManagementService.psnXpadAccountQuery(params)
//                .compose(SchedulersCompat.<PsnXpadAccountQueryResult>applyIoSchedulers())
//                .subscribe(new BaseAccountSubscriber<PsnXpadAccountQueryResult>() {
//                    @Override
//                    public void onNext(PsnXpadAccountQueryResult result) {
//                        List<PsnXpadAccountQueryResult.XPadAccountEntity> list = result.getList();
//                        List<InvestTreatyModel.XPadAccountEntity> treatyList = new ArrayList<InvestTreatyModel.XPadAccountEntity>();
//                        for (PsnXpadAccountQueryResult.XPadAccountEntity entity : list) {
//                            InvestTreatyModel.XPadAccountEntity bean = new InvestTreatyModel.XPadAccountEntity();
//                            BeanConvertor.toBean(entity, bean);
//                            treatyList.add(bean);
//                        }
//                        model.setEntityList(treatyList);
//                        investTreatyView.psnXpadAccountQueryReturn();
//                        ((BussFragment) investTreatyView).closeProgressDialog();
//                    }
//                });
//    }

    /**
     * 063  客户投资智能协议查询 PsnXpadCapacityQuery
     */
    @Override
    public void psnXpadCapacityQuery(final InvestTreatyModel model) {
        //请求协议列表
        getConversation().compose(this.<String>bindToLifecycle())
                .flatMap(new Func1<String, Observable<PsnXpadCapacityQueryResult>>() {
                    @Override
                    public Observable<PsnXpadCapacityQueryResult> call(String conversationId) {
                        return wealthManagementService.psnXpadCapacityQuery(InvestModelUtils.generateInvestTreatyParams(model, conversationId));
                    }
                })
                .compose(SchedulersCompat.<PsnXpadCapacityQueryResult>applyIoSchedulers())
                .subscribe(new BaseAccountSubscriber<PsnXpadCapacityQueryResult>() {
                    @Override
                    public void onNext(PsnXpadCapacityQueryResult psnXpadCapacityQueryResult) {
                        InvestTreatyModel treatyModel = InvestModelUtils.generateInvestTreatyModel(psnXpadCapacityQueryResult);
                        treatyModel.setRecordNumber(psnXpadCapacityQueryResult.getRecordNumber());
                        investTreatyView.psnXpadCapacityQuery(treatyModel);
                    }

                    @Override
                    public void handleException(BiiResultErrorException error) {
                        investTreatyView.psnXpadCapacityQueryFailed();
                        super.handleException(error);
                    }
                });
    }

    /**
     * 064 客户投资协议详情 PsnXpadAgreementInfoQuery
     *
     * @param model
     */
    @Override
    public void psnXpadAgreementInfoQuery(InvestTreatyModel.CapacityQueryBean model) {
        PsnXpadAgreementInfoQueryParams params = new PsnXpadAgreementInfoQueryParams();
        params.setCustAgrCode(model.getCustAgrCode());
        params.setAgrType(model.getAgrType());

        wealthManagementService.psnXpadAgreementInfoQuery(params)
                .compose(SchedulersCompat.<PsnXpadAgreementInfoQueryResult>applyIoSchedulers())
                .subscribe(new BaseAccountSubscriber<PsnXpadAgreementInfoQueryResult>() {
                    @Override
                    public void onNext(PsnXpadAgreementInfoQueryResult psnXpadAgreementInfoQueryResult) {
                        clearConversation();
                        investTreatyInfoView.psnXpadAgreementInfoQuery(InvestModelUtils.generateInvestTreatyInfoModel(psnXpadAgreementInfoQueryResult));
                    }
                });
    }

    /**
     * 066  客户智能协议投资协议终止 PsnXpadInvestAgreementCancel
     *
     * @param model
     */
    public void psnXpadInvestAgreementCancel(final InvestTreatyInfoModel infoModel, final InvestTreatyModel.CapacityQueryBean model) {
        getToken().compose(this.<String>bindToLifecycle())
                .flatMap(new Func1<String, Observable<PsnXpadInvestAgreementCancelResult>>() {
                    @Override
                    public Observable<PsnXpadInvestAgreementCancelResult> call(String token) {
                        return wealthManagementService.psnXpadInvestAgreementCancel(InvestModelUtils.generateInvestTreatyCancelModel(infoModel, model, token, getConversationId()));
                    }
                })
                .compose(SchedulersCompat.<PsnXpadInvestAgreementCancelResult>applyIoSchedulers())
                .subscribe(new BaseAccountSubscriber<PsnXpadInvestAgreementCancelResult>() {
                    @Override
                    public void onNext(PsnXpadInvestAgreementCancelResult psnXpadInvestAgreementCancelResult) {
                        infoModel.setTransactionId(psnXpadInvestAgreementCancelResult.getTransactionId());
                        clearConversation();
                        investTreatyInfoView.psnXpadInvestAgreementCancel(infoModel);
                    }
                });
    }

    /**
     * 069 客户投资协议交易明细查询  PsnXpadCapacityTransList
     */
    @Override
    public void psnXpadCapacityTransList(InvestTreatyModel.CapacityQueryBean model) {
        PsnXpadCapacityTransListParams params = new PsnXpadCapacityTransListParams();
        params.setCustAgrCode(model.getCustAgrCode());
        params.setAgrType(model.getAgrType());

        wealthManagementService.psnXpadCapacityTransList(params)
                .compose(SchedulersCompat.<List<PsnXpadCapacityTransListResult>>applyIoSchedulers())
                .subscribe(new BaseAccountSubscriber<List<PsnXpadCapacityTransListResult>>() {
                    @Override
                    public void onNext(List<PsnXpadCapacityTransListResult> psnXpadCapacityTransListResults) {
                        clearConversation();
                        investTreatyInfoView.psnXpadCapacityTransList(InvestModelUtils.generateTradeInfoModel(psnXpadCapacityTransListResults));
                    }
                });
    }

    /**
     * 024 查询客户风险等级与产品风险等级是否匹配 PsnXpadQueryRiskMatch
     */
    @Override
    public void psnXpadQueryRiskMatch(InvestTreatyModel.CapacityQueryBean entity, InvestTreatyInfoModel infoModel) {
        PsnXpadQueryRiskMatchParams params = new PsnXpadQueryRiskMatchParams();
        params.setAccountKey(entity.getAccountKey());
        params.setSerialCode("");
        params.setDigitalCode("");
        params.setProductCode(infoModel.getProId());
        wealthManagementService.psnXpadQueryRiskMatch(params)
                .compose(this.<PsnXpadQueryRiskMatchResult>bindToLifecycle())
                .compose(SchedulersCompat.<PsnXpadQueryRiskMatchResult>applyIoSchedulers())
                .subscribe(new BaseAccountSubscriber<PsnXpadQueryRiskMatchResult>() {
                    @Override
                    public void onNext(PsnXpadQueryRiskMatchResult psnXpadQueryRiskMatchResult) {
                        PsnXpadQueryRiskMatchReqModel reqModel = new PsnXpadQueryRiskMatchReqModel();
                        reqModel.setRiskMatch(psnXpadQueryRiskMatchResult.getRiskMatch());
                        clearConversation();
                        investTreatyInfoView.psnXpadQueryRiskMatch(reqModel);
                    }
                });
    }

    /**
     * 065 智能投资协议修改预交易  PsnXpadInvestAgreementModifyVerify
     */
    @Override
    public void pnsXpadInvestAgreementModifyVerify(final InvestTreatyConfirmModel infoModel, final InvestTreatyModel.CapacityQueryBean model) {
        getToken().compose(this.<String>bindToLifecycle())
                .flatMap(new Func1<String, Observable<PsnXpadInvestAgreementModifyVerifyResult>>() {
                    @Override
                    public Observable<PsnXpadInvestAgreementModifyVerifyResult> call(String token) {
                        return wealthManagementService.psnXpadInvestAgreementModifyVerify(InvestModelUtils.generatePnsXpadInvestAgreementModifyVerifyParams(infoModel, model, token, getConversationId()));
                    }
                })
                .compose(SchedulersCompat.<PsnXpadInvestAgreementModifyVerifyResult>applyIoSchedulers())
                .subscribe(new BaseAccountSubscriber<PsnXpadInvestAgreementModifyVerifyResult>() {
                    @Override
                    public void onNext(PsnXpadInvestAgreementModifyVerifyResult psnXpadInvestAgreementModifyVerifyResult) {
                        InvestModelUtils.generateParamsToInvestInfoModel(psnXpadInvestAgreementModifyVerifyResult);
                        investTreatyConfirmView.pnsXpadInvestAgreementModifyVerify();
                    }
                });
    }

    /**
     * 079 智能投资协议修改提交  PsnXpadInvestAgreementModifyCommit
     */
    @Override
    public void psnXpadInvestAgreementModifyCommit(final InvestTreatyConfirmModel infoModel, final InvestTreatyModel.CapacityQueryBean model) {
        getToken().compose(this.<String>bindToLifecycle())
                .flatMap(new Func1<String, Observable<PsnXpadInvestAgreementModifyCommitResult>>() {
                    @Override
                    public Observable<PsnXpadInvestAgreementModifyCommitResult> call(String token) {
                        PsnXpadInvestAgreementModifyCommitParams params = new PsnXpadInvestAgreementModifyCommitParams();
                        params.setAccountKey(model.getAccountKey());
                        params.setAgrCode(infoModel.getAgrCode());
                        params.setToken(token);
                        params.setConversationId(getConversationId());
                        return wealthManagementService.psnXpadInvestAgreementModifyCommit(params);
                    }
                })
                .compose(SchedulersCompat.<PsnXpadInvestAgreementModifyCommitResult>applyIoSchedulers())
                .subscribe(new BaseAccountSubscriber<PsnXpadInvestAgreementModifyCommitResult>() {
                    @Override
                    public void onNext(PsnXpadInvestAgreementModifyCommitResult result) {
                        infoModel.setTransactionId(result.getTransactionId());
                        clearConversation();
                        investTreatyConfirmView.psnXpadInvestAgreementModifyCommit(infoModel);
                    }
                });
    }

    /**
     * 018 协议修改结果 PsnXpadAgreementModifyResult
     */
    @Override
    public void psnXpadAgreementModifyResult(final InvestTreatyConfirmModel infoModel, final InvestTreatyModel.CapacityQueryBean model) {
        getToken().compose(this.<String>bindToLifecycle())
                .flatMap(new Func1<String, Observable<PsnXpadAgreementModifyResultResult>>() {
                    @Override
                    public Observable<PsnXpadAgreementModifyResultResult> call(String token) {
                        PsnXpadAgreementModifyResultParams params = new PsnXpadAgreementModifyResultParams();
                        params.setAccountKey(model.getAccountKey());
                        params.setTotalPeriod(infoModel.getBuyPeriod());
                        params.setContractSeq(model.getCustAgrCode());
                        params.setAmountTypeCode(infoModel.getAmountType());
                        params.setSerialName(infoModel.getProName());
                        params.setCurrencyCode(infoModel.getProCur());
                        params.setXpadCashRemit(infoModel.getCashRemit());
                        params.setAddAmount("0");
                        params.setContAmtMode("0");
                        if ("1".equals(infoModel.getAmountType())) {
                            params.setBaseAmount("0");
                            params.setMinAmount(infoModel.getMinAmount());
                            params.setMaxAmount(infoModel.getMaxAmount());
                        } else if ("0".equals(infoModel.getAmountType())) {
                            params.setBaseAmount(infoModel.getAmount());
                            params.setMinAmount("0.00");
                            params.setMaxAmount("0.00");
                        }
                        params.setToken(token);
                        params.setConversationId(getConversationId());
                        return wealthManagementService.psnXpadAgreementModifyResult(params);
                    }
                })
                .compose(SchedulersCompat.<PsnXpadAgreementModifyResultResult>applyIoSchedulers())
                .subscribe(new BaseAccountSubscriber<PsnXpadAgreementModifyResultResult>() {
                    @Override
                    public void onNext(PsnXpadAgreementModifyResultResult result) {
                        infoModel.setTransactionId(result.getTransactionId());
                        clearConversation();
                        investTreatyConfirmView.psnXpadAgreementModifyResult(infoModel);
                    }
                });
    }


    /**
     * 027 协议维护 PsnXpadAutomaticAgreementMaintainResult
     */
    @Override
    public void psnXpadAutomaticAgreementMaintainResult(final InvestTreatyConfirmModel infoModel, final InvestTreatyModel.CapacityQueryBean model) {
        getToken().compose(this.<String>bindToLifecycle())
                .flatMap(new Func1<String, Observable<PsnXpadAutomaticAgreementMaintainResultResult>>() {
                    @Override
                    public Observable<PsnXpadAutomaticAgreementMaintainResultResult> call(String token) {
                        PsnXpadAutomaticAgreementMaintainResultParams params = new PsnXpadAutomaticAgreementMaintainResultParams();
                        params.setAccountKey(model.getAccountKey());
                        params.setTotalPeriod(infoModel.getBuyPeriod());
                        params.setContractSeq(model.getCustAgrCode());
                        params.setSerialName(infoModel.getProName());
                        params.setSerialCode(infoModel.getProId());
                        params.setCurCode(infoModel.getProCur());
                        params.setCashRemit(infoModel.getCashRemit());
                        if ("5".equals(infoModel.getInstType())) {
                            params.setAgreementType("0");
                        }else{
                            params.setAgreementType("1");
                        }
                        params.setMinAmount(infoModel.getMinAmount());
                        params.setMaxAmount(infoModel.getMaxAmount());
                        params.setPeriodSeq(infoModel.getPeriodAge().substring(0,infoModel.getPeriodAge().length()-1));
                        params.setPeriodSeqType(infoModel.getPeriodAge().substring(infoModel.getPeriodAge().length() - 1));
                        params.setMaintainFlag("0");
                        if ("0".equals(infoModel.getTradeCode())) {
                            params.setPeriodType("1");
                            params.setLastDate(infoModel.getFirstdatered());
                        } else {
                            params.setPeriodType("0");
                            params.setLastDate(infoModel.getFirstdatepur());
                        }
                        params.setBaseAmount(infoModel.getAmount());
                        params.setToken(token);
                        params.setConversationId(getConversationId());
                        return wealthManagementService.psnXpadAutomaticAgreementMaintainResult(params);
                    }
                })
                .compose(SchedulersCompat.<PsnXpadAutomaticAgreementMaintainResultResult>applyIoSchedulers())
                .subscribe(new BaseAccountSubscriber<PsnXpadAutomaticAgreementMaintainResultResult>() {
                    @Override
                    public void onNext(PsnXpadAutomaticAgreementMaintainResultResult result) {
                        infoModel.setTransactionId(result.getTransactionId());
                        clearConversation();
                        investTreatyConfirmView.psnXpadAgreementModifyResult(infoModel);
                    }
                });
    }

    /**
     * 081 业绩基准周期滚续产品更新/终止  PsnXpadBenchmarkMaintainResult
     */
    @Override
        public void psnXpadBenchmarkMaintainResult(final InvestTreatyConfirmModel infoModel, final InvestTreatyModel.CapacityQueryBean model,final String opt) {
        getToken().compose(this.<String>bindToLifecycle())
                .flatMap(new Func1<String, Observable<PsnXpadBenchmarkMaintainResultResult>>() {
                    @Override
                    public Observable<PsnXpadBenchmarkMaintainResultResult> call(String token) {
                        PsnXpadBenchmarkMaintainResultParams params = new PsnXpadBenchmarkMaintainResultParams();
                        params.setAccountKey(model.getAccountKey());
                        params.setTotalPeriod(infoModel.getBuyPeriod());
                        params.setCustAgrCode(model.getCustAgrCode());
                        params.setProductName(infoModel.getProName());
                        params.setCurrencyCode(infoModel.getProCur());
                        params.setCashRemit(infoModel.getCashRemit());
                        params.setAmountType(infoModel.getAmountType());
                        params.setBaseAmount(infoModel.getAmount());
                        params.setOpt(opt);
                        params.setToken(token);
                        params.setConversationId(getConversationId());
                        return wealthManagementService.psnXpadBenchmarkMaintainResult(params);
                    }
                })
                .compose(SchedulersCompat.<PsnXpadBenchmarkMaintainResultResult>applyIoSchedulers())
                .subscribe(new BaseAccountSubscriber<PsnXpadBenchmarkMaintainResultResult>() {
                    @Override
                    public void onNext(PsnXpadBenchmarkMaintainResultResult result) {
                        infoModel.setTransactionId(result.getTransactionId());
                        if ("1".equals(opt)) {
                            investTreatyConfirmView.psnXpadBenchmarkMaintainResult(infoModel);
                        }else{
                           investTreatyInfoView.psnXpadBenchmarkMaintainResult(infoModel);
                        }
                    }
                });
    }

}
