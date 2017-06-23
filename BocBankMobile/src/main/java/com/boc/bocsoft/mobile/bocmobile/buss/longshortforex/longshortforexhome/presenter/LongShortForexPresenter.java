package com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.longshortforexhome.presenter;

import android.support.annotation.NonNull;

import com.boc.bocma.tools.LogUtil;
import com.boc.bocsoft.mobile.bii.bus.global.model.PSNCreatConversation.PSNCreatConversationParams;
import com.boc.bocsoft.mobile.bii.bus.global.service.GlobalService;
import com.boc.bocsoft.mobile.bii.bus.longshortforex.model.PsnGetAllExchangeRatesOutlay.PsnGetAllExchangeRatesOutlayParams;
import com.boc.bocsoft.mobile.bii.bus.longshortforex.model.PsnGetAllExchangeRatesOutlay.PsnGetAllExchangeRatesOutlayResult;
import com.boc.bocsoft.mobile.bii.bus.longshortforex.service.LongShortForexService;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadAccountQuery.PsnXpadAccountQueryParams;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadAccountQuery.PsnXpadAccountQueryResult;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadProductBalanceQuery.PsnXpadProductBalanceQueryParams;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadProductBalanceQuery.PsnXpadProductBalanceQueryResult;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadProductDetailQuery.PsnXpadProductDetailQueryParams;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadProductDetailQuery.PsnXpadProductDetailQueryResult;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.service.WealthManagementService;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.base.Exception.BIIBaseSubscriber;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.longshortforexhome.model.psnGetAllExchangeRatesOutlay.PsnGetAllExchangeRatesOutlayReqModel;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.longshortforexhome.model.wfssQueryMultipleQuotation.WFSSQueryMultipleQuotationReqModel;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.longshortforexhome.utils.LongShortForexHomeCodeModelUtil;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.transactiondetails.model.XpadVFGTradeInfoQueryModel;
import com.boc.bocsoft.mobile.bocmobile.buss.system.home.model.searchallpro.SearchAllProResult;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.financialposition.presenter.FinancialPositionContract;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.financialposition.util.FinancialPositionCodeModeUtil;
import com.boc.bocsoft.mobile.framework.rx.SchedulersCompat;
import com.boc.bocsoft.mobile.framework.ui.RxPresenter;
import com.boc.bocsoft.mobile.wfss.buss.common.model.WFSSSearchAllProResult;
import com.boc.bocsoft.mobile.wfss.buss.forexandnoblemetal.model.querymultiplequotation.WFSSQueryMultipleQuotationParams;
import com.boc.bocsoft.mobile.wfss.buss.forexandnoblemetal.model.querymultiplequotation.WFSSQueryMultipleQuotationResult;
import com.boc.bocsoft.mobile.wfss.buss.forexandnoblemetal.service.WFSSForexAndNobleMetalService;

import java.util.List;

/**
 * 双向宝-首页-Presenter
 * Created by yx on 2016/11/26.
 */
public class LongShortForexPresenter extends RxPresenter implements LongShortForexContract.LongShortForexHomePresenter {

    /**
     * 双向宝-service
     */
    private LongShortForexService mLongShortForexService;
    /**
     * 公共service
     */
    private GlobalService globalService;
    /**
     * WFSS -service
     */
    private WFSSForexAndNobleMetalService mWFSSForexAndNobleMetalService;
    /**
     * 双向宝-接口处理view
     */
    private LongShortForexContract.LongShortForexHomeView mLongShortForexHomeView;
    /***
     * 双向宝-model 转换工具类
     */
    private LongShortForexHomeCodeModelUtil mLongShortForexHomeCodeModelUtil;


    /**
     * 构造器，做初始化
     *
     * @param mLongShortForexHomeView
     */
    public LongShortForexPresenter(LongShortForexContract.LongShortForexHomeView mLongShortForexHomeView) {
        this.mLongShortForexHomeView = mLongShortForexHomeView;
        mLongShortForexService = new LongShortForexService();
        globalService = new GlobalService();
        mWFSSForexAndNobleMetalService = new WFSSForexAndNobleMetalService();
    }

    /**
     * 初始化
     */
    private void init() {

    }
    //----------------------------------登录前接口处理----------start------

    /**
     * WFSS-2.1 外汇、贵金属多笔行情查询
     * 概述
     * 查询外汇实盘、外汇虚盘、贵金属实盘、贵金属虚盘行情、详情基本信息。
     * 上送的URL
     * http://[ip]:[port]/mobileplatform/forex/queryMultipleQuotation
     */
    @Override
    public void getWFSSQueryMultipleQuotation(WFSSQueryMultipleQuotationReqModel mParams) {
        WFSSQueryMultipleQuotationParams multipleQuotationParams = new WFSSQueryMultipleQuotationParams();
        multipleQuotationParams.setpSort(mParams.getPSort() + "");
        multipleQuotationParams.setCardType(mParams.getCardType() + "");
        multipleQuotationParams.setCardClass(mParams.getCardClass() + "");
        mWFSSForexAndNobleMetalService.queryMultipleQuotation(multipleQuotationParams)
                .compose(this.<WFSSQueryMultipleQuotationResult>bindToLifecycle())
                .compose(SchedulersCompat.<WFSSQueryMultipleQuotationResult>applyComputationSchedulers())
                .subscribe(new BIIBaseSubscriber<WFSSQueryMultipleQuotationResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        LogUtil.d("yx---------wfss--------handleException");
                        mLongShortForexHomeView.obtainWfssQueryMultipleQuotationFail(biiResultErrorException);
                    }

                    @Override
                    public void onCompleted() {
                        LogUtil.d("yx---------wfss--------onCompleted");

                    }

                    @Override
                    public void onNext(WFSSQueryMultipleQuotationResult wfssQueryMultipleQuotationResult) {
                        LogUtil.d("yx---------wfss--------onNext");
                        mLongShortForexHomeView.obtainWfssQueryMultipleQuotationSuccess(LongShortForexHomeCodeModelUtil.transverterWFSSQueryMultipleQuotationResModel(wfssQueryMultipleQuotationResult));
                    }

                    @Override
                    public void commonHandleException(BiiResultErrorException biiResultErrorException) {
                        LogUtil.d("yx---------wfss--------commonHandleException");
                        super.commonHandleException(biiResultErrorException);
                    }
                });
    }

    /**
     * I43-4.18 018 PsnGetAllExchangeRatesOutlay 登录前贵金属、外汇、双向宝行情查询
     */
    @Override
    public void getPsnGetAllExchangeRatesOutlay(PsnGetAllExchangeRatesOutlayReqModel mParams) {
        PsnGetAllExchangeRatesOutlayParams psnGetAllExchangeRatesOutlayParams = new PsnGetAllExchangeRatesOutlayParams();
        psnGetAllExchangeRatesOutlayParams.setIbknum(mParams.getIbknum() + "");
        psnGetAllExchangeRatesOutlayParams.setOfferType(mParams.getOfferType() + "");
        psnGetAllExchangeRatesOutlayParams.setParitiesType(mParams.getParitiesType() + "");
        mLongShortForexService.psnGetAllExchangeRatesOutlay(psnGetAllExchangeRatesOutlayParams)
                .compose(this.<List<PsnGetAllExchangeRatesOutlayResult>>bindToLifecycle())
                .compose(SchedulersCompat.<List<PsnGetAllExchangeRatesOutlayResult>>applyComputationSchedulers())
                .subscribe(new BIIBaseSubscriber<List<PsnGetAllExchangeRatesOutlayResult>>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        mLongShortForexHomeView.obtainPsnGetAllExchangeRatesOutlayFail(biiResultErrorException);
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(List<PsnGetAllExchangeRatesOutlayResult> psnGetAllExchangeRatesOutlayResult) {
                        mLongShortForexHomeView.obtainPsnGetAllExchangeRatesOutlaySuccess(LongShortForexHomeCodeModelUtil.transverterPsnGetAllExchangeRatesOutlayResModel(psnGetAllExchangeRatesOutlayResult));
                    }

                    @Override
                    public void commonHandleException(BiiResultErrorException biiResultErrorException) {
                        super.commonHandleException(biiResultErrorException);
                    }
                });
        //----------------------------------登录前接口处理----------end------
    }

}
