package com.boc.bocsoft.mobile.bocmobile.buss.fund.purchase.presenter;

import android.graphics.AvoidXfermode;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.boc.bocsoft.mobile.bii.bus.account.model.PsnAccountQueryAccountDetail.PsnAccountQueryAccountDetailParams;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnAccountQueryAccountDetail.PsnAccountQueryAccountDetailResult;
import com.boc.bocsoft.mobile.bii.bus.account.service.AccountService;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundBuy.PsnFundBuyResult;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundCanDealDateQuery.PsnFundCanDealDateQueryParams;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundCanDealDateQuery.PsnFundCanDealDateQueryResult;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundCompanyInfoQuery.PsnFundCompanyInfoQueryParams;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundCompanyInfoQuery.PsnFundCompanyInfoQueryResult;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundNightBuy.PsnFundNightBuyResult;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundSignElectronicContract.PsnFundSignElectronicContractResult;
import com.boc.bocsoft.mobile.bii.bus.fund.service.FundService;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.base.Exception.BIIBaseSubscriber;
import com.boc.bocsoft.mobile.bocmobile.buss.account.base.BaseAccountSubscriber;
import com.boc.bocsoft.mobile.bocmobile.buss.account.base.BaseTransactionPresenter;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.purchase.ModelUtil;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.purchase.model.FundbuyModel;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.purchase.ui.FundPurchaseContract;
import com.boc.bocsoft.mobile.framework.rx.SchedulersCompat;

import rx.Observable;
import rx.functions.Func1;

public class FundPurchasePresenter extends BaseTransactionPresenter implements FundPurchaseContract.FundPurchasePresenter {

    /**
     * 基金service
     * */
    private FundService fundService;

    /**
     * 账户service
     * */
    private AccountService accountService;

    /**
     * 购买信息填写页面
     * */
    private FundPurchaseContract.FundPurchaseView mPurchaseView;

    /**
     * 购买确认页面
     * */
    private FundPurchaseContract.FundPurchaseConfirmView mConfirmView;
    /**
     * 购买结果页
     * */
    private FundPurchaseContract.FundPurchaseResultView mResultView;
    /**
     * 电子合同签约页
     * */
    private FundPurchaseContract.SignContractView mSignView;


    /**
     * 针对多种页面的Presenter构造器
     * */
    //基金购买信息填写页
    public FundPurchasePresenter(FundPurchaseContract.FundPurchaseView view) {
        super(null);//??
        fundService = new FundService();
        accountService = new AccountService();
        this.mPurchaseView = view;
    }

    //基金购买信息确认页
    public FundPurchasePresenter(FundPurchaseContract.FundPurchaseConfirmView view) {
        super(null);//??
        fundService = new FundService();
        this.mConfirmView = view;
    }

    //基金购买结果页
    public FundPurchasePresenter(FundPurchaseContract.FundPurchaseResultView view) {
        super(null);//??
        fundService = new FundService();
        this.mResultView = view;
    }

    //基金电子合同页
    public FundPurchasePresenter(FundPurchaseContract.SignContractView view){
        super(null);
        fundService = new FundService();
        this.mSignView = view;
    }
    /**
     * 查询基金公司详情 I41 061
     * */
    @Override
    public void queryFundCompanyDetail(final FundbuyModel model){
        PsnFundCompanyInfoQueryParams params = new PsnFundCompanyInfoQueryParams();
        if(!TextUtils.isEmpty(model.getFundCode())){
            params.setFundCode(model.getFundCode());
        }
        fundService.psnFundCompanyInfoQuery(params)
                .compose(this.<PsnFundCompanyInfoQueryResult>bindToLifecycle())
                .compose(SchedulersCompat.<PsnFundCompanyInfoQueryResult>applyComputationSchedulers())
                .subscribe(new BaseAccountSubscriber<PsnFundCompanyInfoQueryResult>() {
                    @Override
                    public void onNext(PsnFundCompanyInfoQueryResult psnFundCompanyInfoQueryResult) {
                        model.setCompanyPhone(psnFundCompanyInfoQueryResult.getCompanyPhone());
                        model.setCompanyName(psnFundCompanyInfoQueryResult.getCompanyName());
                        mPurchaseView.queryFundCompanyDetail(model);
                        Log.d("张辰雨1", "onNext: 成功。电话"+model.getCompanyPhone()+"名称"+model.getCompanyName());
                    }

                    @Override
                    public void onCompleted() {
                        Log.d("张辰雨", "onNext: 结束");
                    }

                    @Override
                    public void handleException(BiiResultErrorException error) {
                        Log.d("张辰雨", "onNext: 失败");
                    }
                });

    }

    /**
     * 查询用户风险等级评估
     *
    public void queryFundRiskEvaluation(final FundbuyModel model){
        PsnFundRiskEvaluationQueryParams params = new PsnFundRiskEvaluationQueryParams();
        fundService.psnFundRiskEvaluationQuery(params)
                .compose(this.<PsnFundRiskEvaluationQueryResult>bindToLifecycle())
                .compose(SchedulersCompat.<PsnFundRiskEvaluationQueryResult>applyComputationSchedulers())
                .subscribe(new BaseAccountSubscriber<PsnFundRiskEvaluationQueryResult>() {
                    @Override
                    public void onNext(PsnFundRiskEvaluationQueryResult psnFundRiskEvaluationQueryResult) {
                        model.setEvaluated(psnFundRiskEvaluationQueryResult.isEvaluated());
                        model.setUserRiskLevel(psnFundRiskEvaluationQueryResult.getRiskLevel());
                        mConfirmView.queryFundEvaluation(model);
                    }
                });
    }*/

    /**
     * 查询可交易日期 I41 063
     * */
    public void queryFundCanDealDateQuery(final FundbuyModel fundCanDealDateQueryModel){
        PsnFundCanDealDateQueryParams params = new PsnFundCanDealDateQueryParams();
        if(!TextUtils.isEmpty(fundCanDealDateQueryModel.getFundCode())){
            params.setFundCode(fundCanDealDateQueryModel.getFundCode());
        }
        if(!TextUtils.isEmpty((fundCanDealDateQueryModel.getAppointFlag()))){
            params.setAppointFlag(fundCanDealDateQueryModel.getAppointFlag());
        }
        fundService.psnFundCanDealDateQuery(params)
                .compose(this.<PsnFundCanDealDateQueryResult>bindToLifecycle())
                .compose(SchedulersCompat.<PsnFundCanDealDateQueryResult>applyComputationSchedulers())
                .subscribe(new BaseAccountSubscriber<PsnFundCanDealDateQueryResult>() {
                    @Override
                    public void onNext(PsnFundCanDealDateQueryResult psnFundCanDealDateQueryResult) {
                        fundCanDealDateQueryModel.setDealDatelist(psnFundCanDealDateQueryResult.getDealDatelist());
                        mPurchaseView.queryFundCanDealDateQuery(fundCanDealDateQueryModel);

                    }
                });
    }

    //基金买入
    public void fundBuySubmit(final FundbuyModel fundbuyModel){
        getToken().flatMap(new Func1<String, Observable<PsnFundBuyResult>>(){
            @Override
            public  Observable<PsnFundBuyResult> call(String token){
                return fundService.psnFundBuy(ModelUtil.generateFundBuyParams(fundbuyModel,token,getConversationId()));
            }
        }).compose(this.<PsnFundBuyResult>bindToLifecycle())
        .compose(SchedulersCompat.<PsnFundBuyResult>applyComputationSchedulers())
        .subscribe(new BaseAccountSubscriber<PsnFundBuyResult>() {
            @Override
            public void onNext(PsnFundBuyResult result) {
                mConfirmView.fundBuySubmit(ModelUtil.generateFundBuyModel(fundbuyModel,result));
            }
        });
    }

    //基金挂单买入
    public void fundNightBuySubmit(final FundbuyModel fundNightBuyModel){
        getToken().flatMap(new Func1<String, Observable<PsnFundNightBuyResult>>() {
            @Override
            public Observable<PsnFundNightBuyResult> call(String token) {
                return fundService.psnFundNightBuy(ModelUtil.generateFundNightBuyParams(fundNightBuyModel,token,getConversationId()));
            }
        }).compose(this.<PsnFundNightBuyResult>bindToLifecycle())
                .compose(SchedulersCompat.<PsnFundNightBuyResult>applyComputationSchedulers())
                .subscribe(new BaseAccountSubscriber<PsnFundNightBuyResult>() {
                    @Override
                    public void onNext(PsnFundNightBuyResult result) {
                        mConfirmView.fundNightBuySubmit(ModelUtil.generateFundNightBuyModel(fundNightBuyModel,result));
                    }
                });

    }

    //资金账户详情查询
    public void queryAccountDetail(final FundbuyModel accountQueryAccountDetailModel){
        PsnAccountQueryAccountDetailParams params = new PsnAccountQueryAccountDetailParams();
        params.setAccountId(accountQueryAccountDetailModel.getAccountId());
        accountService.psnAccountQueryAccountDetail(params)
                .compose(this.<PsnAccountQueryAccountDetailResult>bindToLifecycle())
                .compose(SchedulersCompat.<PsnAccountQueryAccountDetailResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnAccountQueryAccountDetailResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {

                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(PsnAccountQueryAccountDetailResult psnAccountQueryAccountDetailResult) {
                        mPurchaseView.queryAccountDetail(ModelUtil.generateQueryAccountDetailModel(accountQueryAccountDetailModel,psnAccountQueryAccountDetailResult));
                    }
                });

    }

    //签署电子合同
    public void signContractSubmit(final FundbuyModel signContractModel){
        getToken().flatMap(new Func1<String, Observable<PsnFundSignElectronicContractResult>>(){
            @Override
            public  Observable<PsnFundSignElectronicContractResult> call(String token){
                return fundService.PsnFundSignElectronicContract(ModelUtil.generateSignContractParams(signContractModel,token,getConversationId()));
            }
        }).compose(this.<PsnFundSignElectronicContractResult>bindToLifecycle())
                .compose(SchedulersCompat.<PsnFundSignElectronicContractResult>applyComputationSchedulers())
                .subscribe(new BaseAccountSubscriber<PsnFundSignElectronicContractResult>() {
                    @Override
                    public void onNext(PsnFundSignElectronicContractResult result) {
                        mSignView.signContractSubmit(ModelUtil.generateSignContractModel(signContractModel,result));
                    }
                });

    }
}