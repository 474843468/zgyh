package com.boc.bocsoft.mobile.bocmobile.buss.fund.fundproduct.home.presenter;

import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundCompanyQueryOutlay.PsnFundCompanyQueryOutlayParams;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundCompanyQueryOutlay.PsnFundCompanyQueryOutlayResult;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundQueryFundBalance.PsnFundQueryFundBalanceParams;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundQueryFundBalance.PsnFundQueryFundBalanceResult;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundQueryOutlay.PsnFundQueryOutlayParams;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundQueryOutlay.PsnFundQueryOutlayResult;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnGetFundCompanyList.PsnGetFundCompanyListParams;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnGetFundCompanyList.PsnGetFundCompanyListResultBean;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnQueryFundDetail.PsnQueryFundDetailParams;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnQueryFundDetail.PsnQueryFundDetailResult;
import com.boc.bocsoft.mobile.bii.bus.fund.service.FundService;
import com.boc.bocsoft.mobile.bii.bus.global.model.PSNCreatConversation.PSNCreatConversationParams;
import com.boc.bocsoft.mobile.bii.bus.global.service.GlobalService;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnOcrmProductQuery.PsnOcrmProductQueryParams;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnOcrmProductQuery.PsnOcrmProductQueryResult;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.service.WealthManagementService;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.base.Exception.BIIBaseSubscriber;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.fundproduct.home.model.FundCompanyListViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.fundproduct.home.ui.FundProductHomeContract;
import com.boc.bocsoft.mobile.common.utils.StringUtils;
import com.boc.bocsoft.mobile.framework.rx.SchedulersCompat;
import com.boc.bocsoft.mobile.wfss.buss.funds.model.querymultiplefund.WFSSQueryMultipleFundParams;
import com.boc.bocsoft.mobile.wfss.buss.funds.model.querymultiplefund.WFSSQueryMultipleFundResult;
import com.boc.bocsoft.mobile.wfss.buss.funds.service.WFSSFundsService;

import java.util.LinkedList;
import java.util.List;

import rx.Observable;
import rx.functions.Func1;

/**
 * 基金请求（首页与详情）
 * Created by liuzc on 2016/12/1.
 */
public class FundProductHomePresenter extends FundUserPresenter implements FundProductHomeContract.Presenter{
    private WFSSFundsService wfssFundService = null; //WFSS基金service
    private FundService fundService = null; //基金service
    private GlobalService globalService; //公共service
    private WealthManagementService wealthService;// 理财Service

    private FundProductHomeContract.HomeView homeViewContract = null;

    /**
     * 会话ID
     */
    private String conversationID = null;

    private String recommendConversationID = null; //请求推荐列表的会话ID

    public FundProductHomePresenter(FundProductHomeContract.HomeView view){
        super(view);
        homeViewContract = view;
        homeViewContract.setPresenter(this);

        wfssFundService = new WFSSFundsService();
        fundService = new FundService();
        globalService = new GlobalService();
        wealthService = new WealthManagementService();
    }

    //登录前从FUDS查询基金产品
    @Override
    public void queryFundsProductListNLog(PsnFundQueryOutlayParams params) {
        fundService.psnFundQueryOutlay(params)
            .compose(this.<PsnFundQueryOutlayResult>bindToLifecycle())
            .compose(SchedulersCompat.<PsnFundQueryOutlayResult>applyIoSchedulers())
            .subscribe(new BIIBaseSubscriber<PsnFundQueryOutlayResult>() {
                @Override
                public void handleException(BiiResultErrorException biiResultErrorException) {
                    homeViewContract.queryFundsProductListNLogFail(biiResultErrorException);
                }

                @Override
                public void commonHandleException(BiiResultErrorException biiResultErrorException) {

                }

                @Override
                public void onCompleted() {

                }

                @Override
                public void onNext(PsnFundQueryOutlayResult result) {
                    homeViewContract.queryFundsProductListNLogSuccess(result);
                }
            });
    }

    //查询WFSS产品列表
    @Override
    public void queryWFSSProductList(WFSSQueryMultipleFundParams params) {
        wfssFundService.queryMultipleFund(params)
            .compose(this.<WFSSQueryMultipleFundResult>bindToLifecycle())
            .compose(SchedulersCompat.<WFSSQueryMultipleFundResult>applyIoSchedulers())
            .subscribe(new BIIBaseSubscriber<WFSSQueryMultipleFundResult>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onNext(WFSSQueryMultipleFundResult psnLOANRemainResult) {
                    homeViewContract.queryWFSSProductListSuccess(psnLOANRemainResult);
                }

                @Override
                public void handleException(BiiResultErrorException biiResultErrorException) {
                    homeViewContract.queryWFSSProductListFail(biiResultErrorException);
                }
            });
    }

    //查询基金持仓信息
    @Override
    public void queryFundBalance(PsnFundQueryFundBalanceParams params) {
        fundService.psnFundQueryFundBalance(params)
                .compose(this.<PsnFundQueryFundBalanceResult>bindToLifecycle())
                .compose(SchedulersCompat.<PsnFundQueryFundBalanceResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnFundQueryFundBalanceResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        homeViewContract.queryFundBalanceFail(biiResultErrorException);
                    }

                    @Override
                    public void commonHandleException(BiiResultErrorException biiResultErrorException) {

                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(PsnFundQueryFundBalanceResult result) {
                        homeViewContract.queryFundBalanceSuccess(result);
                    }
                });
    }

    @Override
    public void getFundCompanyListNLog(PsnFundCompanyQueryOutlayParams params) {
        fundService.psnFundCompanyQueryOutlay(params)
                .compose(this.<List<PsnFundCompanyQueryOutlayResult>>bindToLifecycle())
                .compose(SchedulersCompat.<List<PsnFundCompanyQueryOutlayResult>>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<List<PsnFundCompanyQueryOutlayResult>>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        homeViewContract.getFundCompanyListNLogFail(biiResultErrorException);
                    }

                    @Override
                    public void commonHandleException(BiiResultErrorException biiResultErrorException) {

                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(List<PsnFundCompanyQueryOutlayResult> result) {
                        FundCompanyListViewModel viewModel = new FundCompanyListViewModel();

                        List<FundCompanyListViewModel.ListBean> list = new LinkedList<FundCompanyListViewModel.ListBean>();
                        for(int i = 0; i < result.size(); i ++){
                            FundCompanyListViewModel.ListBean bean = viewModel.new ListBean();
                            bean.setFundCompanyCode(result.get(i).getFundCompanyCode());
                            bean.setFundCompanyName(result.get(i).getFundCompanyName());

                            list.add(bean);
                        }
                        viewModel.setList(list);
                        homeViewContract.getFundCompanyListNLogSuccess(viewModel);
                    }
                });
    }

    //查询基金公司
    @Override
    public void getFundCompanyList(final PsnGetFundCompanyListParams params) {
        globalService.psnCreatConversation(new PSNCreatConversationParams())
                .compose(this.<String>bindToLifecycle())
                .flatMap(new Func1<String, Observable<List<PsnGetFundCompanyListResultBean>>>() {
                    @Override
                    public Observable<List<PsnGetFundCompanyListResultBean>> call(
                            String conversationId) {
                        conversationID = conversationId;
                        params.setConversationId(conversationId);
                        return fundService.psnGetFundCompanyList(params);
                    }
                })
                .compose(SchedulersCompat.<List<PsnGetFundCompanyListResultBean>>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<List<PsnGetFundCompanyListResultBean>>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        homeViewContract.getFundCompanyListFail(biiResultErrorException);
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(List<PsnGetFundCompanyListResultBean> result) {
                        //fragment要用到的viewmodel
                        FundCompanyListViewModel viewModel = new FundCompanyListViewModel();

                        //为viewModel中的list赋值
                        List<FundCompanyListViewModel.ListBean> list = new LinkedList<FundCompanyListViewModel.ListBean>();
                        //循环读取result列表中的每一项，赋值到list中
                        for(int i = 0; i < result.size(); i ++){
                            FundCompanyListViewModel.ListBean bean = viewModel.new ListBean();
                            bean.setFundCompanyCode(result.get(i).getFundCompanyCode());
                            bean.setFundCompanyName(result.get(i).getFundCompanyName());

                            list.add(bean);
                        }
                        //viewModel的list赋值
                        viewModel.setList(list);
                        //页面回调方法，viewModel是把服务端返回的数据解析到view层
                        homeViewContract.getFundCompanyListSuccess(viewModel);
                    }
                });
    }

    @Override
    public void queryFundsProductListLog(final PsnQueryFundDetailParams params) {
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
                    .subscribe(new BIIBaseSubscriber<PsnQueryFundDetailResult>() {
                        @Override
                        public void handleException(BiiResultErrorException biiResultErrorException) {
                            homeViewContract.queryFundsProductListLogFail(biiResultErrorException);
                        }

                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onNext(PsnQueryFundDetailResult result) {
                            homeViewContract.queryFundsProductListLogSuccess(result);
                        }
                    });
        }
        else{
            params.setConversationId(conversationID);
            fundService.psnQueryFundDetail(params)
                    .compose(this.<PsnQueryFundDetailResult>bindToLifecycle())
                    .compose(SchedulersCompat.<PsnQueryFundDetailResult>applyIoSchedulers())
                    .subscribe(new BIIBaseSubscriber<PsnQueryFundDetailResult>() {
                        @Override
                        public void handleException(BiiResultErrorException biiResultErrorException) {
                            homeViewContract.queryFundsProductListLogFail(biiResultErrorException);
                        }

                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onNext(PsnQueryFundDetailResult result) {
                            homeViewContract.queryFundsProductListLogSuccess(result);
                        }
                    });
        }
    }

    //请求基金推荐列表
    @Override
    public void queryFundsRecommend(final PsnOcrmProductQueryParams params) {
        if(StringUtils.isEmptyOrNull(recommendConversationID)){
            queryRecListWithoutConverstaion(params);
        }
        else{
            queryRecListWithConverstaion(params, recommendConversationID);
        }
    }

    //不带会话ID请求基金推荐
    private void queryRecListWithoutConverstaion(final PsnOcrmProductQueryParams params){
        globalService.psnCreatConversation(new PSNCreatConversationParams())
            .compose(this.<String>bindToLifecycle())
            .flatMap(new Func1<String, Observable<PsnOcrmProductQueryResult>>() {
                @Override
                public Observable<PsnOcrmProductQueryResult> call(
                        String conversationId) {
                    recommendConversationID = conversationId;
                    params.setConversationId(recommendConversationID);
                    params.set_refresh("true");
                    return wealthService.psnOcrmProductQuery(params);
                }
            })
            .compose(SchedulersCompat.<PsnOcrmProductQueryResult>applyIoSchedulers())
            .subscribe(getRecListSubscriber());
    }

    //带会话ID请求基金推荐
    private void queryRecListWithConverstaion(PsnOcrmProductQueryParams params, String converSationID){
        params.setConversationId(converSationID);
        params.set_refresh("false");
        wealthService.psnOcrmProductQuery(params)
                .compose(this.<PsnOcrmProductQueryResult>bindToLifecycle())
                .compose(SchedulersCompat.<PsnOcrmProductQueryResult>applyIoSchedulers())
                .subscribe(getRecListSubscriber());
    }

    //构造基金推荐请求的Subscriber
    private BIIBaseSubscriber getRecListSubscriber(){
        return new BIIBaseSubscriber<PsnOcrmProductQueryResult>() {
            @Override
            public void handleException(BiiResultErrorException biiResultErrorException) {
                homeViewContract.queryFundsRecommendFail(biiResultErrorException);
            }

            @Override
            public void commonHandleException(BiiResultErrorException biiResultErrorException) {

            }

            @Override
            public void onCompleted() {

            }

            @Override
            public void onNext(PsnOcrmProductQueryResult result) {
                homeViewContract.queryFundsRecommendSuccess(result);
            }
        };
    }
}
