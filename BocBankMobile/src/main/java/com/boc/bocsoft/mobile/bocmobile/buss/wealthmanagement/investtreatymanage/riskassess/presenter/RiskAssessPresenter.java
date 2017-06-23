package com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.investtreatymanage.riskassess.presenter;


import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnInvtEvaluationInit.PsnInvtEvaluationInitParams;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnInvtEvaluationInit.PsnInvtEvaluationInitResult;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnInvtEvaluationResult.PsnInvtEvaluationResultParams;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnInvtEvaluationResult.PsnInvtEvaluationResultResult;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.service.WealthManagementService;
import com.boc.bocsoft.mobile.bocmobile.buss.account.base.BaseAccountPresenter;
import com.boc.bocsoft.mobile.bocmobile.buss.account.base.BaseAccountSubscriber;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.accountmanagement.model.AccountModel;
import com.boc.bocsoft.mobile.framework.rx.SchedulersCompat;

import rx.Observable;
import rx.functions.Func1;

/**
 * 风险评估逻辑
 * Created by guokai on 2016/9/17.
 */
public class RiskAssessPresenter extends BaseAccountPresenter implements RiskAssessContract.Presenter {

    private RiskAssessContract.RiskAssessView riskAssessView;
    private RiskAssessContract.RiskAssessChoiceView riskAssessChoiceView;

    private WealthManagementService service;
    private AccountModel model;

    public RiskAssessPresenter(RiskAssessContract.RiskAssessView riskAssessView) {
        this.riskAssessView = riskAssessView;
        service = new WealthManagementService();
        model = riskAssessView.getModel();
    }

    public RiskAssessPresenter(RiskAssessContract.RiskAssessChoiceView riskAssessChoiceView) {
        this.riskAssessChoiceView = riskAssessChoiceView;
        service = new WealthManagementService();
    }

    /**
     * 020风险评估查询（判断是否做过风险评估）PsnInvtEvaluationInit
     *
     */
    @Override
    public void psnInvtEvaluationInit() {
        service.psnInvtEvaluationInit(new PsnInvtEvaluationInitParams())
                .compose(SchedulersCompat.<PsnInvtEvaluationInitResult>applyComputationSchedulers())
                .subscribe(new BaseAccountSubscriber<PsnInvtEvaluationInitResult>() {
                    @Override
                    public void onNext(PsnInvtEvaluationInitResult result) {
                        String riskLevel = result.getRiskLevel();
                        riskAssessView.psnInvtEvaluationInit(riskLevel);
                    }
                });


    }

    /**
     * 021风险评估提交 PsnInvtEvaluationResult
     */
    @Override
    public void psnInvtEvaluationResult() {
        getToken().compose(this.<String>bindToLifecycle())
                .flatMap(new Func1<String, Observable<PsnInvtEvaluationResultResult>>() {
                    @Override
                    public Observable<PsnInvtEvaluationResultResult> call(String token) {
                        PsnInvtEvaluationResultParams params = new PsnInvtEvaluationResultParams();
                        params.setToken(token);
                        params.setConversationId(getConversationId());
                        params.setRiskScore("0");
                        return service.psnInvtEvaluationResult(params);
                    }
                })
                .compose(SchedulersCompat.<PsnInvtEvaluationResultResult>applyIoSchedulers())
                .subscribe(new BaseAccountSubscriber<PsnInvtEvaluationResultResult>() {
                    @Override
                    public void onNext(PsnInvtEvaluationResultResult result) {
                        String riskLevel = result.getRiskLevel();
                        riskAssessView.psnInvtEvaluationResult(riskLevel);
                    }
                });
    }

    /**
     * 021风险评估提交 PsnInvtEvaluationResult
     */
    @Override
    public void psnInvtEvaluationResult(final String score,final String riskAnswer) {
        getToken().compose(this.<String>bindToLifecycle())
                .flatMap(new Func1<String, Observable<PsnInvtEvaluationResultResult>>() {
                    @Override
                    public Observable<PsnInvtEvaluationResultResult> call(String token) {
                        PsnInvtEvaluationResultParams params = new PsnInvtEvaluationResultParams();
                        params.setToken(token);
                        params.setConversationId(getConversationId());
                        params.setRiskScore(score);
                        params.setRiskAnswer(riskAnswer);
                        return service.psnInvtEvaluationResult(params);
                    }
                })
                .compose(SchedulersCompat.<PsnInvtEvaluationResultResult>applyIoSchedulers())
                .subscribe(new BaseAccountSubscriber<PsnInvtEvaluationResultResult>() {
                    @Override
                    public void onNext(PsnInvtEvaluationResultResult result) {
                        riskAssessChoiceView.psnInvtEvaluationResult();
                    }
                });
    }
}

