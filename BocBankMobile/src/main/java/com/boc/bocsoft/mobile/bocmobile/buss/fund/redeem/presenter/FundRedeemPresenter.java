package com.boc.bocsoft.mobile.bocmobile.buss.fund.redeem.presenter;

import android.text.TextUtils;
import android.util.Log;

import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundCanDealDateQuery.PsnFundCanDealDateQueryParams;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundCanDealDateQuery.PsnFundCanDealDateQueryResult;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundCompanyInfoQuery.PsnFundCompanyInfoQueryParams;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundCompanyInfoQuery.PsnFundCompanyInfoQueryResult;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundNightSell.PsnFundNightSellResult;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundQuickSell.PsnFundQuickSellResult;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundSell.PsnFundSellResult;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnQuickSellQuotaQuery.PsnQuickSellQuotaQueryParams;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnQuickSellQuotaQuery.PsnQuickSellQuotaQueryResult;
import com.boc.bocsoft.mobile.bii.bus.fund.service.FundService;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.base.Exception.BIIBaseSubscriber;
import com.boc.bocsoft.mobile.bocmobile.buss.account.base.BaseAccountSubscriber;
import com.boc.bocsoft.mobile.bocmobile.buss.account.base.BaseTransactionPresenter;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.redeem.ModelUtil;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.redeem.model.FundRedeemModel;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.redeem.ui.FundRedeemContract;
import com.boc.bocsoft.mobile.framework.rx.SchedulersCompat;
import com.boc.bocsoft.mobile.framework.ui.RxPresenter;

import java.util.List;

import rx.Observable;
import rx.functions.Func1;

public class FundRedeemPresenter extends BaseTransactionPresenter implements FundRedeemContract.FundRedeemPresenter {

    /**
     * 基金service
     * */
    private FundService fundService;

    private FundRedeemContract.FundRedeemView mFundRedeemView;
    private FundRedeemContract.FundRedeemConfirmView mFundRedeemConfirmView;
    private  FundRedeemContract.FundRedeemResultView mFundRedeemResultView;

    /**
     * 针对多种赎回页面的Presenter构造器
     * */
    //赎回信息填写页
    public FundRedeemPresenter(FundRedeemContract.FundRedeemView view) {
        super(null);//??
        fundService = new FundService();
        mFundRedeemView = view;
    }

    //赎回确认信息页
    public FundRedeemPresenter(FundRedeemContract.FundRedeemConfirmView view) {
        super(null);//??
        fundService = new FundService();
        mFundRedeemConfirmView = view;
    }

    //赎回结果页
    public FundRedeemPresenter(FundRedeemContract.FundRedeemResultView view) {
        super(null);//??
        fundService = new FundService();
        mFundRedeemResultView = view;
    }

    /**
     * 查询基金公司详情 I41 061
     * */
    @Override
    public void queryFundCompanyDetail(final FundRedeemModel model){
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
                        if(mFundRedeemView != null){
                            mFundRedeemView.queryFundCompanyDetail(model);
                        }
                        else if(mFundRedeemConfirmView != null){
                            mFundRedeemConfirmView.queryFundCompanyDetail(model);
                        }

                    }

                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void handleException(BiiResultErrorException error) {
                    }
                });

    }

    /**
     * 查询可交易日期 I41 063
     * */
    public void queryFundCanDealDateQuery(final FundRedeemModel fundCanDealDateQueryModel){
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
                        mFundRedeemView.queryFundCanDealDateQuery(fundCanDealDateQueryModel);

                    }
                });
    }


    /**
     * 基金赎回
     * */
    public void fundSell(final FundRedeemModel fundsellModel){
        getToken().flatMap(new Func1<String, Observable<PsnFundSellResult>>() {
            @Override
            public Observable<PsnFundSellResult> call(String token) {
                return fundService.psnFundSell(ModelUtil.generateFundSellParams(fundsellModel,token,getConversationId()));
            }
        }).compose(this.<PsnFundSellResult>bindToLifecycle())
                .compose(SchedulersCompat.<PsnFundSellResult>applyComputationSchedulers())
                .subscribe(new BaseAccountSubscriber<PsnFundSellResult>() {
                    @Override
                    public void onNext(PsnFundSellResult psnFundSellResult) {
                        mFundRedeemConfirmView.fundSell(ModelUtil.generateFundSellModel(fundsellModel,psnFundSellResult));
                    }
                });
    }

    /**
     * 基金挂单赎回
     * */
    public void fundNightSell(final FundRedeemModel fundnightsellModel){
        getToken().flatMap(new Func1<String, Observable<PsnFundNightSellResult>>() {
            @Override
            public Observable<PsnFundNightSellResult> call(String token) {
                return fundService.psnFundNightSell(ModelUtil.generateFundNightSellParams(fundnightsellModel,token,getConversationId()));
            }
        }).compose(this.<PsnFundNightSellResult>bindToLifecycle())
                .compose(SchedulersCompat.<PsnFundNightSellResult>applyComputationSchedulers())
                .subscribe(new BaseAccountSubscriber<PsnFundNightSellResult>() {
                    @Override
                    public void onNext(PsnFundNightSellResult psnFundNightSellResult) {
                        mFundRedeemConfirmView.fundNightSell(ModelUtil.generateFundNightSellModel(fundnightsellModel,psnFundNightSellResult));
                    }
                });
    }

    /**
     * 基金快速赎回
     * */
    public void fundQuickSell(final FundRedeemModel fundRedeemModel){
        getToken().flatMap(new Func1<String, Observable<PsnFundQuickSellResult>>() {
            @Override
            public Observable<PsnFundQuickSellResult> call(String token) {
                return fundService.psnFundQuickSell(ModelUtil.generateFundQuickSellParams(fundRedeemModel,token,getConversationId()));
            }
        }).compose(this.<PsnFundQuickSellResult>bindToLifecycle())
                .compose(SchedulersCompat.<PsnFundQuickSellResult>applyComputationSchedulers())
                .subscribe(new BaseAccountSubscriber<PsnFundQuickSellResult>() {
                    @Override
                    public void onNext(PsnFundQuickSellResult psnFundQuickSellResult) {
                        mFundRedeemConfirmView.fundQuickSell(ModelUtil.generateFundQuickSellModel(fundRedeemModel,psnFundQuickSellResult));
                    }
                });
    }

    /**
     * 064 基金快速赎回额度查询
     * */
    public void quickSellQuotaQuery(final FundRedeemModel fundRedeemModel){
        PsnQuickSellQuotaQueryParams params = new PsnQuickSellQuotaQueryParams();
        if(!TextUtils.isEmpty(fundRedeemModel.getFundCode())){
            params.setFundCode(fundRedeemModel.getFundCode());
        }
        fundService.psnFundQuickSellQuotaQuery(params)
                .compose(this.<PsnQuickSellQuotaQueryResult>bindToLifecycle())
                .compose(SchedulersCompat.<PsnQuickSellQuotaQueryResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnQuickSellQuotaQueryResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {

                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(PsnQuickSellQuotaQueryResult psnQuickSellQuotaQueryResults) {

                        mFundRedeemView.quickSellQuotaQuery(ModelUtil.generateQuickSellQuotaQuery(fundRedeemModel,psnQuickSellQuotaQueryResults));
                    }
                });
    }

}