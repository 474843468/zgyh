package com.boc.bocsoft.mobile.bocmobile.buss.fund.fundproduct.recommend.presenter;

import com.boc.bocsoft.mobile.bii.bus.global.model.PSNCreatConversation.PSNCreatConversationParams;
import com.boc.bocsoft.mobile.bii.bus.global.service.GlobalService;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnOcrmProductQuery.PsnOcrmProductQueryParams;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnOcrmProductQuery.PsnOcrmProductQueryResult;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.service.WealthManagementService;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.base.Exception.BIIBaseSubscriber;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.fundproduct.recommend.ui.FundRecommendContract;
import com.boc.bocsoft.mobile.common.utils.StringUtils;
import com.boc.bocsoft.mobile.framework.rx.SchedulersCompat;
import com.boc.bocsoft.mobile.framework.ui.RxPresenter;

import java.util.LinkedList;
import java.util.List;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by lzc4524 on 2016/12/26.
 */
public class FundRecommendPresenter extends RxPresenter implements FundRecommendContract.Presenter{
    private FundRecommendContract.View mViewContract;
    private WealthManagementService wealthService;// 理财Service
    /**
     * 公共service
     */
    private GlobalService globalService;

    private String conversationID = null;

    public FundRecommendPresenter(FundRecommendContract.View contract){
        mViewContract = contract;
        mViewContract.setPresenter(this);
        globalService = new GlobalService();
        wealthService = new WealthManagementService();
    }

    @Override
    public void queryFundsRecommend(PsnOcrmProductQueryParams params) {
        if(StringUtils.isEmptyOrNull(conversationID)){
            queryWithoutConverstaion(params);
        }
        else{
            queryWithConverstaion(params, conversationID);
        }
    }

    private void queryWithoutConverstaion(final PsnOcrmProductQueryParams params){
        PsnOcrmProductQueryResult result = new PsnOcrmProductQueryResult();
        result.setRecordNumber("23");
        List<PsnOcrmProductQueryResult.OcrmDetail> list = new LinkedList<PsnOcrmProductQueryResult.OcrmDetail>();

        for(int i = 0; i < 10; i ++){
            PsnOcrmProductQueryResult.OcrmDetail item = new PsnOcrmProductQueryResult.OcrmDetail();
            list.add(item);
        }

        result.setResultList(list);
        mViewContract.queryFundsRecommendSuccess(result);

//        globalService.psnCreatConversation(new PSNCreatConversationParams())
//            .compose(this.<String>bindToLifecycle())
//            .flatMap(new Func1<String, Observable<PsnOcrmProductQueryResult>>() {
//                @Override
//                public Observable<PsnOcrmProductQueryResult> call(
//                        String conversationId) {
//                    conversationID = conversationId;
//                    params.setConversationId(conversationID);
//                    params.set_refresh("true");
//                    return wealthService.psnOcrmProductQuery(params);
//                }
//            })
//            .compose(SchedulersCompat.<PsnOcrmProductQueryResult>applyIoSchedulers())
//            .subscribe(getSubscriber());

    }

    private void queryWithConverstaion(PsnOcrmProductQueryParams params, String converSationID){
        params.setConversationId(converSationID);
        params.set_refresh("false");
        wealthService.psnOcrmProductQuery(params)
                .compose(this.<PsnOcrmProductQueryResult>bindToLifecycle())
                .compose(SchedulersCompat.<PsnOcrmProductQueryResult>applyIoSchedulers())
                .subscribe(getSubscriber());
    }

    private BIIBaseSubscriber getSubscriber(){
        return new BIIBaseSubscriber<PsnOcrmProductQueryResult>() {
            @Override
            public void handleException(BiiResultErrorException biiResultErrorException) {
                mViewContract.queryFundsRecommendFail(biiResultErrorException);
            }

            @Override
            public void commonHandleException(BiiResultErrorException biiResultErrorException) {

            }

            @Override
            public void onCompleted() {

            }

            @Override
            public void onNext(PsnOcrmProductQueryResult result) {
                mViewContract.queryFundsRecommendSuccess(result);
            }
        };
    }

}
