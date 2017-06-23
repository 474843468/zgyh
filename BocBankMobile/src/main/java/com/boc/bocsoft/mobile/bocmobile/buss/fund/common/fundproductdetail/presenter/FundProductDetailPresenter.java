package com.boc.bocsoft.mobile.bocmobile.buss.fund.common.fundproductdetail.presenter;

import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundAttentionAdd.PsnFundAttentionAddParams;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundAttentionAdd.PsnFundAttentionAddResponse;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundAttentionAdd.PsnFundAttentionAddResult;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundAttentionCancel.PsnFundAttentionCancelParams;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundAttentionCancel.PsnFundAttentionCancelResult;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundAttentionQueryList.PsnFundAttentionQueryListParams;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundAttentionQueryList.PsnFundAttentionQueryListResult;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundChangeCard.PsnFundChangeCardResult;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundDetailQueryOutlay.PsnFundDetailQueryOutlayParams;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundDetailQueryOutlay.PsnFundDetailQueryOutlayResult;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnGetFundDetail.PsnGetFundDetailParams;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnGetFundDetail.PsnGetFundDetailResult;
import com.boc.bocsoft.mobile.bii.bus.fund.service.FundService;
import com.boc.bocsoft.mobile.bii.bus.global.model.PSNCreatConversation.PSNCreatConversationParams;
import com.boc.bocsoft.mobile.bii.bus.global.model.PSNGetTokenId.PSNGetTokenIdParams;
import com.boc.bocsoft.mobile.bii.bus.global.service.GlobalService;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.base.Exception.BIIBaseSubscriber;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.common.fundproductdetail.model.BIFundDetailParamsViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.common.fundproductdetail.model.BIFundDetailResultViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.common.fundproductdetail.model.FundNewsListViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.common.fundproductdetail.model.FundNoticeViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.common.fundproductdetail.model.JzTendencyViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.common.fundproductdetail.model.RankTendencyViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.common.fundproductdetail.model.WFSSFundBasicDetailParamsViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.common.fundproductdetail.model.WFSSFundBasicDetailResultViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.common.fundproductdetail.model.YieldOfWanFenTendencyViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.common.fundproductdetail.model.YieldOfWeekTendencyViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.common.fundproductdetail.model.YieldRateTendencyViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.common.fundproductdetail.ui.FundProductDetailContract;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.fundproduct.home.presenter.FundUserPresenter;
import com.boc.bocsoft.mobile.common.utils.BeanConvertor.BeanConvertor;
import com.boc.bocsoft.mobile.common.utils.StringUtils;
import com.boc.bocsoft.mobile.framework.rx.SchedulersCompat;
import com.boc.bocsoft.mobile.framework.ui.RxPresenter;
import com.boc.bocsoft.mobile.wfss.buss.funds.model.fundnotice.WFSSFundnoticeParams;
import com.boc.bocsoft.mobile.wfss.buss.funds.model.fundnotice.WFSSFundnoticeResult;
import com.boc.bocsoft.mobile.wfss.buss.funds.model.jztendency.WFSSJzTendencyParams;
import com.boc.bocsoft.mobile.wfss.buss.funds.model.jztendency.WFSSJzTendencyResult;
import com.boc.bocsoft.mobile.wfss.buss.funds.model.ljyieldratetendency.WFSSLjYieldRateTendencyParams;
import com.boc.bocsoft.mobile.wfss.buss.funds.model.ljyieldratetendency.WFSSLjYieldRateTendencyResult;
import com.boc.bocsoft.mobile.wfss.buss.funds.model.newslist.WFSSNewsListParams;
import com.boc.bocsoft.mobile.wfss.buss.funds.model.newslist.WFSSNewsListResult;
import com.boc.bocsoft.mobile.wfss.buss.funds.model.queryfundbasicdetail.WFSSQueryFundBasicDetailParams;
import com.boc.bocsoft.mobile.wfss.buss.funds.model.queryfundbasicdetail.WFSSQueryFundBasicDetailResult;
import com.boc.bocsoft.mobile.wfss.buss.funds.model.ranktendency.WFSSRankTendencyParams;
import com.boc.bocsoft.mobile.wfss.buss.funds.model.ranktendency.WFSSRankTendencyResult;
import com.boc.bocsoft.mobile.wfss.buss.funds.service.WFSSFundsService;

import java.util.LinkedList;
import java.util.List;

import rx.Observable;
import rx.functions.Func1;

/**
 * 基金详情网络请求
 * Created by liuzc on 2016/11/25.
 */
public class FundProductDetailPresenter extends FundUserPresenter implements FundProductDetailContract.Presenter{
    private WFSSFundsService wfssFundService = null; //WFSS基金service
    private FundService fundService = null; //基金service
    private FundProductDetailContract.View mContractView = null;
    private GlobalService mGlobalService = null;

    private String conversationId = null;

    public FundProductDetailPresenter(FundProductDetailContract.View view){
        super(view);
        mContractView = view;
        mContractView.setPresenter(this);

        wfssFundService = new WFSSFundsService();
        fundService = new FundService();
        mGlobalService = new GlobalService();
    }

    //从WFSS获取基金详情数据
    @Override
    public void queryWFSSFundDetail(WFSSFundBasicDetailParamsViewModel model) {
        WFSSQueryFundBasicDetailParams params = new WFSSQueryFundBasicDetailParams();
        params.setFundId(model.getFundId());
        wfssFundService.queryFundBasicDetail(params)
                .compose(this.<WFSSQueryFundBasicDetailResult>bindToLifecycle())
                .compose(SchedulersCompat.<WFSSQueryFundBasicDetailResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<WFSSQueryFundBasicDetailResult>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void commonHandleException(BiiResultErrorException biiResultErrorException) {

                    }

                    @Override
                    public void onNext(WFSSQueryFundBasicDetailResult result) {
                        WFSSFundBasicDetailResultViewModel resultModel = BeanConvertor.toBean(result,
                                new WFSSFundBasicDetailResultViewModel());
                        mContractView.queryWFSSFundDetailSuccess(resultModel);
                    }

                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        mContractView.queryWFSSFundDetailFail(biiResultErrorException);
                    }
                });
    }

    //登陆前基金详情查询，从BI获取数据
    @Override
    public void queryBiFundDetailNLog(PsnFundDetailQueryOutlayParams params) {
        fundService.psnFundDetailQueryOutlay(params)
                .compose(this.<PsnFundDetailQueryOutlayResult>bindToLifecycle())
                .compose(SchedulersCompat.<PsnFundDetailQueryOutlayResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnFundDetailQueryOutlayResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        mContractView.queryBiFundDetailNLogFail(biiResultErrorException);
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void commonHandleException(BiiResultErrorException biiResultErrorException) {

                    }

                    @Override
                    public void onNext(PsnFundDetailQueryOutlayResult psnFundDetailQueryOutlayResult) {
                        mContractView.queryBiFundDetailNLogSuccess(psnFundDetailQueryOutlayResult);
                    }
                });
    }

    //登陆后基金详情查询，从BI获取数据
    @Override
    public void getBiFundDetailLogin(BIFundDetailParamsViewModel model) {
        PsnGetFundDetailParams params = new PsnGetFundDetailParams();
        params.setFundCode(model.getFundCode());

        fundService.psnGetFundDetail(params)
                .compose(this.<PsnGetFundDetailResult>bindToLifecycle())
                .compose(SchedulersCompat.<PsnGetFundDetailResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnGetFundDetailResult>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(PsnGetFundDetailResult result) {
                        BIFundDetailResultViewModel resultModel = BeanConvertor.toBean(result,
                                new BIFundDetailResultViewModel());
                        mContractView.getBiFundDetailLoginSuccess(resultModel);
                    }

                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        mContractView.getBiFundDetailLoginFail(biiResultErrorException);
                    }
                });
    }

    //趋势图
    @Override
    public void queryJzTendency(JzTendencyViewModel params) {
        WFSSJzTendencyParams wfssParams = new WFSSJzTendencyParams();
        wfssParams.setFundId(params.getFundId());
        wfssParams.setFundCycle(params.getFundCycle());

        wfssFundService.jzTendency(wfssParams)
                .compose(this.<WFSSJzTendencyResult>bindToLifecycle())
                .compose(SchedulersCompat.<WFSSJzTendencyResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<WFSSJzTendencyResult>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(WFSSJzTendencyResult result) {
                        JzTendencyViewModel resultModel = BeanConvertor.toBean(result,
                                new JzTendencyViewModel());
                        List<JzTendencyViewModel.FundList> items = new LinkedList<JzTendencyViewModel.FundList>();
                        for(int i = 0; i < result.getItems().size(); i ++){
                            JzTendencyViewModel viewModel = new JzTendencyViewModel();
                            JzTendencyViewModel.FundList curItem = viewModel.new FundList();
                            WFSSJzTendencyResult.FundList resultItem = result.getItems().get(i);
                            curItem.setDwjz(resultItem.getDwjz());
                            curItem.setChangeOfDay(resultItem.getChangeOfDay());
                            curItem.setJzTime(resultItem.getJzTime());
                            curItem.setLjjz(resultItem.getLjjz());
                            items.add(curItem);
                        }

                        resultModel.setItems(items);
                        mContractView.queryJzTendencySuccss(resultModel);
                    }

                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        mContractView.queryWFSSFundDetailFail(biiResultErrorException);
                    }
                });
    }

    //累计收益
    @Override
    public void queryYieldRateTendency(YieldRateTendencyViewModel params) {
        WFSSLjYieldRateTendencyParams wfssParams = new WFSSLjYieldRateTendencyParams();
        wfssParams.setFundId(params.getFundId());
        wfssParams.setFundCycle(params.getFundCycle());

        wfssFundService.ljYieldRateTendency(wfssParams)
                .compose(this.<WFSSLjYieldRateTendencyResult>bindToLifecycle())
                .compose(SchedulersCompat.<WFSSLjYieldRateTendencyResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<WFSSLjYieldRateTendencyResult>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(WFSSLjYieldRateTendencyResult result) {
                        YieldRateTendencyViewModel resultModel = BeanConvertor.toBean(result,
                                new YieldRateTendencyViewModel());
                        List<YieldRateTendencyViewModel.FundList> items = new LinkedList<YieldRateTendencyViewModel.FundList>();
                        for(int i = 0; i < result.getItems().size(); i ++){
                            YieldRateTendencyViewModel viewModel = new YieldRateTendencyViewModel();
                            YieldRateTendencyViewModel.FundList curItem = viewModel.new FundList();
                            WFSSLjYieldRateTendencyResult.FundList resultItem = result.getItems().get(i);
                            curItem.setJzTime(resultItem.getJzTime());
                            curItem.setLjYieldRate(resultItem.getLjYieldRate());
                            curItem.setSectionDepositRate(resultItem.getSectionDepositRate());
                            curItem.setSzzjYieldRate(resultItem.getSzzjYieldRate());
                            curItem.setYjbjjzYieldRate(resultItem.getYjbjjzYieldRate());
                            items.add(curItem);
                        }

                        resultModel.setItems(items);
                        mContractView.queryYieldRateTendencySuccess(resultModel);
                    }

                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        mContractView.queryYieldRateTendencyFail(biiResultErrorException);
                    }
                });
    }

    //排名变化
    @Override
    public void queryRrankTendency(RankTendencyViewModel params) {
        WFSSRankTendencyParams wfssParams = new WFSSRankTendencyParams();
        wfssParams.setFundId(params.getFundId());
        wfssParams.setFundCycle(params.getFundCycle());

        wfssFundService.rankTendency(wfssParams)
                .compose(this.<WFSSRankTendencyResult>bindToLifecycle())
                .compose(SchedulersCompat.<WFSSRankTendencyResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<WFSSRankTendencyResult>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(WFSSRankTendencyResult result) {
                        RankTendencyViewModel resultModel = BeanConvertor.toBean(result,
                                new RankTendencyViewModel());
                        List<RankTendencyViewModel.FundList> items = new LinkedList<RankTendencyViewModel.FundList>();
                        for(int i = 0; i < result.getItems().size(); i ++){
                            RankTendencyViewModel viewModel = new RankTendencyViewModel();
                            RankTendencyViewModel.FundList curItem = viewModel.new FundList();
                            WFSSRankTendencyResult.FundList resultItem = result.getItems().get(i);
                            curItem.setJzTime(resultItem.getJzTime());
                            curItem.setRank(resultItem.getRank());
                            curItem.setRankScore(resultItem.getRankScore());
                            curItem.setSimilarCount(resultItem.getSimilarCount());
                            items.add(curItem);
                        }

                        resultModel.setItems(items);
                        mContractView.queryRrankTendencySuccess(resultModel);
                    }

                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        mContractView.queryRrankTendencyFail(biiResultErrorException);
                    }
                });
    }

    //七日年化收益率
    @Override
    public void queryYieldOfWeekTendency(YieldOfWeekTendencyViewModel params) {

    }

    //万份收益率
    @Override
    public void queryYieldOfWanFenTendency(YieldOfWanFenTendencyViewModel params) {

    }

    //查询基金公告内容
    @Override
    public void queryFundNotices(FundNoticeViewModel params) {
        WFSSFundnoticeParams wfssParams = new WFSSFundnoticeParams();
        wfssParams.setFundId(params.getFundId());
        wfssParams.setPageNo(params.getPageNo());
        wfssParams.setPageSize(params.getPageSize());

        wfssFundService.fundnotice(wfssParams)
                .compose(this.<WFSSFundnoticeResult>bindToLifecycle())
                .compose(SchedulersCompat.<WFSSFundnoticeResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<WFSSFundnoticeResult>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(WFSSFundnoticeResult result) {
                        FundNoticeViewModel model = BeanConvertor.toBean(result, new FundNoticeViewModel());
                        List<FundNoticeViewModel.FundList> modelList = new LinkedList<FundNoticeViewModel.FundList>();
                        List<WFSSFundnoticeResult.FundList> resList = result.getItems();
                        for(int i = 0; i < resList.size(); i ++){
                            FundNoticeViewModel.FundList modelItem = new FundNoticeViewModel.FundList();
                            WFSSFundnoticeResult.FundList curResItem = resList.get(i);
                            modelItem.setStrcaption(curResItem.getStrcaption());
                            modelItem.setIoriid(curResItem.getIoriid());
                            modelItem.setReporturl(curResItem.getReporturl());
                            modelItem.setSdtpublish(curResItem.getSdtpublish());
                            modelList.add(modelItem);
                        }
                        model.setItems(modelList);
                        mContractView.queryFundNoticesSuccess(model);
                    }

                    @Override
                    public void commonHandleException(BiiResultErrorException biiResultErrorException) {

                    }

                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        mContractView.queryFundNoticesFail(biiResultErrorException);
                    }
                });
    }

    @Override
    public void queryFundNewsList(FundNewsListViewModel params) {
        WFSSNewsListParams wfssParams = new WFSSNewsListParams();
        wfssParams.setFundBakCode(params.getFundBakCode());
        wfssParams.setPageNo(params.getPageNo());
        wfssParams.setPageSize(params.getPageSize());

        wfssFundService.newsList(wfssParams)
                .compose(this.<WFSSNewsListResult>bindToLifecycle())
                .compose(SchedulersCompat.<WFSSNewsListResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<WFSSNewsListResult>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(WFSSNewsListResult result) {
                        FundNewsListViewModel model = BeanConvertor.toBean(result, new FundNewsListViewModel());
                        List<FundNewsListViewModel.FundList> modelList = new LinkedList<FundNewsListViewModel.FundList>();
                        List<WFSSNewsListResult.FundList> resList = result.getItems();
                        for(WFSSNewsListResult.FundList curResItem: resList){
                            FundNewsListViewModel.FundList modelItem = new FundNewsListViewModel.FundList();
                            modelItem.setTitle(curResItem.getTitle());
                            modelItem.setContentId(curResItem.getContentId());
                            modelItem.setCreated(curResItem.getCreated());
                            modelList.add(modelItem);
                        }
                        model.setItems(modelList);
                        mContractView.queryNewsListSuccess(model);
                    }

                    @Override
                    public void commonHandleException(BiiResultErrorException biiResultErrorException) {

                    }

                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        mContractView.queryNewsListFail(biiResultErrorException);
                    }
                });
    }

    @Override
    public void queryFundAttentionList(PsnFundAttentionQueryListParams params) {
        fundService.psnFundAttentionQueryList(params)
                .compose(this.<PsnFundAttentionQueryListResult>bindToLifecycle())
                .compose(SchedulersCompat.<PsnFundAttentionQueryListResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnFundAttentionQueryListResult>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(PsnFundAttentionQueryListResult result) {
                        mContractView.queryFundAttentionListSuccess(result);
                    }

                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        mContractView.queryFundAttentionListFail(biiResultErrorException);
                    }
                });
    }

    /**
     * 获取会话
     * @return
     */
    public Observable<String> getConversation() {
        //生成ConversationId
        PSNCreatConversationParams psnCreatConversationParams = new PSNCreatConversationParams();
        return mGlobalService.psnCreatConversation(psnCreatConversationParams);
    }

    /**
     * 获取Token
     * @return
     */
    public Observable<String> getToken() {
        //根据ConversationId生成Token
        return getConversation().flatMap(new Func1<String, Observable<String>>() {
            @Override
            public Observable<String> call(String conversationResult) {
                conversationId = conversationResult;
                PSNGetTokenIdParams params = new PSNGetTokenIdParams();
                params.setConversationId(conversationResult);
                return mGlobalService.psnGetTokenId(params);
            }
        });
    }

    @Override
    public void addFundAttention(final PsnFundAttentionAddParams params) {
        getToken().flatMap(new Func1<String, Observable<PsnFundAttentionAddResult>>() {
                    @Override
                    public Observable<PsnFundAttentionAddResult> call(String token) {
                        params.setConversationId(conversationId);
                        params.setToken(token);
                        return fundService.psnFundAttentionAdd(params);
                    }
                })
                .compose(this.<PsnFundAttentionAddResult>bindToLifecycle())
                .compose(SchedulersCompat.<PsnFundAttentionAddResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnFundAttentionAddResult>() {

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(PsnFundAttentionAddResult result) {
                        //成功调用
                        mContractView.addFundAttentionSuccess(result);
                    }

                    @Override
                    public void handleException(BiiResultErrorException e) {
                        mContractView.addFundAttentionFail(e);
                    }
                });
    }

    @Override
    public void cancelFundAttention(final PsnFundAttentionCancelParams params) {
        getToken().flatMap(new Func1<String, Observable<PsnFundAttentionCancelResult>>() {
                    @Override
                    public Observable<PsnFundAttentionCancelResult> call(String token) {
                        params.setConversationId(conversationId);
                        params.setToken(token);
                        return fundService.psnFundAttentionCancel(params);
                    }
                })
                .compose(this.<PsnFundAttentionCancelResult>bindToLifecycle())
                .compose(SchedulersCompat.<PsnFundAttentionCancelResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnFundAttentionCancelResult>() {

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(PsnFundAttentionCancelResult result) {
                        //成功调用
                        mContractView.cancelFundAttentionSuccess(result);
                    }

                    @Override
                    public void handleException(BiiResultErrorException e) {
                        mContractView.cancelFundAttentionFail(e);
                    }
                });
    }
}
