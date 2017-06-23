package com.boc.bocsoft.mobile.bocmobile.buss.fund.fundproduct.search.presenter;

;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundQueryOutlay.PsnFundQueryOutlayParams;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundQueryOutlay.PsnFundQueryOutlayResult;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnQueryFundDetail.PsnQueryFundDetailParams;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnQueryFundDetail.PsnQueryFundDetailResult;
import com.boc.bocsoft.mobile.bii.bus.fund.service.FundService;
import com.boc.bocsoft.mobile.bii.bus.global.model.PSNCreatConversation.PSNCreatConversationParams;
import com.boc.bocsoft.mobile.bii.bus.global.service.GlobalService;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.ApplicationConst;
import com.boc.bocsoft.mobile.bocmobile.base.Exception.BIIBaseSubscriber;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.fundproduct.search.ui.FundProductSearchContract;
import com.boc.bocsoft.mobile.bocmobile.buss.system.home.model.searchallpro.SearchAllProParams;
import com.boc.bocsoft.mobile.bocmobile.buss.system.home.model.searchallpro.SearchAllProResult;
import com.boc.bocsoft.mobile.cr.bus.product.model.CRgetProductList.CRgetProductListParams;
import com.boc.bocsoft.mobile.cr.bus.product.model.CRgetProductList.CRgetProductListResult;
import com.boc.bocsoft.mobile.cr.bus.product.service.CRProductService;
import com.boc.bocsoft.mobile.framework.rx.SchedulersCompat;
import com.boc.bocsoft.mobile.framework.ui.RxPresenter;
import java.util.List;

import rx.Observable;
import rx.functions.Func1;

/**
 * 基金产品搜索
 * Created by liuzc on 2016/12/31.
 */

public class FundProductSearchPresenter extends RxPresenter implements FundProductSearchContract.Presenter {
    private FundProductSearchContract.View mContractView;
    private CRProductService productService;
    private FundService fundService; //基金service
    private GlobalService globalService; //公共service

    /**
     * 会话ID
     */
    private String conversationID = null;

    public FundProductSearchPresenter(FundProductSearchContract.View view) {
        mContractView = view;
        mContractView.setPresenter(this);
        productService = new CRProductService();
        fundService = new FundService();
        globalService = new GlobalService();
    }

    public void cRgetProductList() {
        CRgetProductListParams params = new CRgetProductListParams();
        productService.cRgetProductList(params).
                compose(this.<CRgetProductListResult>bindToLifecycle()).
                compose(SchedulersCompat.<CRgetProductListResult>applyIoSchedulers()).
                subscribe(new BIIBaseSubscriber<CRgetProductListResult>() {

                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        mContractView.getProductListFail(biiResultErrorException);
                    }

                    @Override
                    public void onNext(CRgetProductListResult result) {
                        mContractView.getProductListSucc(result);
                    }
                });
    }

    @Override
    public void searchAllPro(SearchAllProParams proParams, boolean isLoginAndBinding) {
        //已经登陆且绑定资金账户,查询登陆后基金列表
        if(isLoginAndBinding){
            PsnQueryFundDetailParams params = new PsnQueryFundDetailParams();

            params.setFundType("00");
            params.setCurrentIndex("1");
            params.setPageSize(String.valueOf(ApplicationConst.FUND_PAGE_SIZE));
            params.setCurrencyCode("");
            params.setFundState("");
            params.setRiskGrade("");
            params.setCompany("");
            params.setFundInfo(proParams.getKey());
            params.setFundKind("00");

            queryFundsProductListLog(params);
        }
        else{ //未登陆,查询登陆前基金列表
            PsnFundQueryOutlayParams params = new PsnFundQueryOutlayParams();
            params.setFundType("00");
            params.setCurrentIndex("1");
            params.setPageSize(String.valueOf(ApplicationConst.FUND_PAGE_SIZE));
            params.setCurrencyCode("");
            params.setFundState("");
            params.setRiskGrade("");
            params.setCompany("");
            params.setFundInfo(proParams.getKey());
            params.setFundKind("00");

            queryFundsProductListNLog(params);
        }
    }

    //登录前从FUDS查询基金产品
    private void queryFundsProductListNLog(PsnFundQueryOutlayParams params) {
        fundService.psnFundQueryOutlay(params)
                .compose(this.<PsnFundQueryOutlayResult>bindToLifecycle())
                .compose(SchedulersCompat.<PsnFundQueryOutlayResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnFundQueryOutlayResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        mContractView.searchProductFail(biiResultErrorException);
                    }

                    @Override
                    public void commonHandleException(BiiResultErrorException biiResultErrorException) {

                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(PsnFundQueryOutlayResult result) {
                        SearchAllProResult srchResult = new SearchAllProResult();
                        List<PsnFundQueryOutlayResult.ListBean> fundList = result.getList();
                        List<SearchAllProResult.Fund> fundListCopy = srchResult.getFundList();
                        for (PsnFundQueryOutlayResult.ListBean fund : fundList) {
                            SearchAllProResult.Fund item = new SearchAllProResult().new Fund();
                            item.setFundId(fund.getFundCode());
                            item.setFundName(fund.getFundName());
                            fundListCopy.add(item);
                        }
                        mContractView.searchProductSucc(srchResult);
                    }
                });
    }

    //登录后从FUDS查询基金产品
    private void queryFundsProductListLog(final PsnQueryFundDetailParams params) {
        //不存在会话ID，则先请求会话ID
        if(conversationID == null){
            globalService.psnCreatConversation(new PSNCreatConversationParams())
                    .compose(this.<String>bindToLifecycle())
                    .flatMap(new Func1<String, Observable<PsnQueryFundDetailResult>>(){
                        @Override
                        public Observable<PsnQueryFundDetailResult> call(
                                String conversationId) {
                            conversationID = conversationId;
                            params.setConversationId(conversationId);
                            return fundService.psnQueryFundDetail(params);
                        }
                    })
                    .compose(SchedulersCompat.<PsnQueryFundDetailResult>applyIoSchedulers())
                    .subscribe(getLoginSubscriber());
        }
        else{
            params.setConversationId(conversationID);
            fundService.psnQueryFundDetail(params)
                    .compose(this.<PsnQueryFundDetailResult>bindToLifecycle())
                    .compose(SchedulersCompat.<PsnQueryFundDetailResult>applyIoSchedulers())
                    .subscribe(getLoginSubscriber());
        }
    }

    private BIIBaseSubscriber getLoginSubscriber(){
        return new BIIBaseSubscriber<PsnQueryFundDetailResult>() {
            @Override
            public void handleException(BiiResultErrorException biiResultErrorException) {
                mContractView.searchProductFail(biiResultErrorException);
            }

            @Override
            public void onCompleted() {

            }

            @Override
            public void onNext(PsnQueryFundDetailResult result) {
                SearchAllProResult srchResult = new SearchAllProResult();
                List<PsnQueryFundDetailResult.ListBean> fundList = result.getList();
                List<SearchAllProResult.Fund> fundListCopy = srchResult.getFundList();
                for (PsnQueryFundDetailResult.ListBean fund : fundList) {
                    SearchAllProResult.Fund item = new SearchAllProResult().new Fund();
                    item.setFundId(fund.getFundCode());
                    item.setFundName(fund.getFundName());
                    fundListCopy.add(item);
                }
                mContractView.searchProductSucc(srchResult);
            }
        };
    }
}
