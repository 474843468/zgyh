package com.boc.bocsoft.mobile.bocmobile.buss.system.home.presenter;

import com.boc.bocsoft.mobile.bii.bus.fess.model.PsnGetExchangeOutlay.PsnGetExchangeOutlayParams;
import com.boc.bocsoft.mobile.bii.bus.fess.model.PsnGetExchangeOutlay.PsnGetExchangeOutlayResult;
import com.boc.bocsoft.mobile.bii.bus.fess.service.FessService;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundDetailQueryOutlay.PsnFundDetailQueryOutlayParams;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundDetailQueryOutlay.PsnFundDetailQueryOutlayResult;
import com.boc.bocsoft.mobile.bii.bus.fund.service.FundService;
import com.boc.bocsoft.mobile.bii.bus.global.model.PsnGetAllExchangeRatesOutlay.PsnGetAllExchangeRatesOutlayParams;
import com.boc.bocsoft.mobile.bii.bus.global.model.PsnGetAllExchangeRatesOutlay.PsnGetAllExchangeRatesOutlayResult;
import com.boc.bocsoft.mobile.bii.bus.global.service.GlobalService;
import com.boc.bocsoft.mobile.bii.bus.gold.service.GoldService;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.ApplicationContext;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.Exception.BIIBaseSubscriber;
import com.boc.bocsoft.mobile.bocmobile.base.model.FessBean;
import com.boc.bocsoft.mobile.bocmobile.base.model.FundBean;
import com.boc.bocsoft.mobile.bocmobile.base.model.GoldBean;
import com.boc.bocsoft.mobile.bocmobile.base.utils.LogUtils;
import com.boc.bocsoft.mobile.bocmobile.buss.common.menu.Item;
import com.boc.bocsoft.mobile.bocmobile.buss.common.menu.Menu;
import com.boc.bocsoft.mobile.bocmobile.buss.common.util.InvestUtils;
import com.boc.bocsoft.mobile.bocmobile.buss.system.common.dao.AppStateDao;
import com.boc.bocsoft.mobile.bocmobile.buss.system.home.dao.HomeInvestDao;
import com.boc.bocsoft.mobile.bocmobile.buss.system.home.dao.HomeMenuDao;
import com.boc.bocsoft.mobile.bocmobile.buss.system.home.dao.InvestItemBO;
import com.boc.bocsoft.mobile.bocmobile.buss.system.home.model.AdvertisementModel;
import com.boc.bocsoft.mobile.bocmobile.buss.system.home.ui.HomeContract;
import com.boc.bocsoft.mobile.common.utils.StringUtils;
import com.boc.bocsoft.mobile.cr.bus.ad.model.CRgetPosterList.CRgetPosterListParams;
import com.boc.bocsoft.mobile.cr.bus.ad.model.CRgetPosterList.CRgetPosterListResult;
import com.boc.bocsoft.mobile.cr.bus.ad.service.CRAdService;
import com.boc.bocsoft.mobile.framework.rx.SchedulersCompat;
import com.boc.bocsoft.mobile.framework.rx.lifecycle.RxLifecycleManager;
import com.boc.bocsoft.mobile.wfss.buss.funds.model.queryfundbasicdetail.WFSSQueryFundBasicDetailParams;
import com.boc.bocsoft.mobile.wfss.buss.funds.model.queryfundbasicdetail.WFSSQueryFundBasicDetailResult;
import com.boc.bocsoft.mobile.wfss.buss.funds.service.WFSSFundsService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * 首页presenter
 * Created by lxw on 2016/5/23.
 */
public class HomePresenter implements HomeContract.Presenter{

    private final static String LOG_TAG = HomePresenter.class.getSimpleName();
    private final static String APP_KEY_HOME_MENU = "home_menu";
    private final static String APP_KEY_HOME_INVEST = "home_invest";

    HomeMenuDao homeMenuDao;
    HomeContract.View mView;
    FundService mFundService;
    GoldService mGoldService;
    GlobalService mGlobalService;
    FessService mFessService;
    HomeInvestDao homeInvestDao;
    AppStateDao appStateDao;
    //WFSSFundsService wfssFundService;
    private CRAdService crAdService;



    private  boolean isGoldProcess = false;

    private ArrayList<FundBean> fundBeans = new ArrayList<>();
    private ArrayList<GoldBean> goldBeans = new ArrayList<>();
    private ArrayList<FessBean> fessBeans = new ArrayList<>();

    private RxLifecycleManager mRxLifecycleManager;
    public HomePresenter(HomeContract.View view){
        mView = view;
        homeMenuDao = new HomeMenuDao();
        homeInvestDao = new HomeInvestDao();
        appStateDao = new AppStateDao();
        mRxLifecycleManager = new RxLifecycleManager();
        mFundService = new FundService();
        mGoldService = new GoldService();
        mGlobalService = new GlobalService();
        mFessService = new FessService();
        crAdService = new CRAdService();
        //wfssFundService = new WFSSFundsService();
    }

    /**
     * 获取广告列表
     */
    @Override
    public void getAdsList() {

        Observable.just("")
                .map(new Func1<String, String>() {
                    @Override public String call(String s) {
                        List<AdvertisementModel> advertisementModelList = new ArrayList<AdvertisementModel>();
                        if (ApplicationContext.getInstance().getUser().isLogin()){
                            advertisementModelList.addAll( getDefaultAdList());
                        } else {
                            advertisementModelList.add(getLoginAds());
                        }
                        mView.updateAdsView(advertisementModelList);
                        return s;
                    }
                })
                .observeOn(Schedulers.io())
                .flatMap(new Func1<String, Observable<CRgetPosterListResult>>() {
                    @Override public Observable<CRgetPosterListResult> call(String s) {

                        return getOBSAd();
                    }
                })
                .compose(mRxLifecycleManager.<CRgetPosterListResult>bindToLifecycle())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<CRgetPosterListResult>() {
                    @Override public void onCompleted() {

                    }

                    @Override public void onError(Throwable throwable) {
                        //TODO 广告加载失败
                        List<AdvertisementModel> advertisementModels = new ArrayList<>();
                        if (ApplicationContext.getInstance().getUser().isLogin()){
                            advertisementModels = getDefaultAdList();
                        } else {
                            advertisementModels.add(getLoginAds());
                        }
                        mView.updateAdsView(advertisementModels);
                    }

                    @Override public void onNext(CRgetPosterListResult cRgetPosterListResult) {
                        //ToastUtils.show("------广告加载成功----");
                        List<AdvertisementModel> advertisementModels =
                                buildAdListData(cRgetPosterListResult.getArrayList());

                        /**
                         * 登录后
                         */
                        if (ApplicationContext.getInstance().getUser().isLogin()){
                            if (advertisementModels == null || advertisementModels.size() == 0){
                                advertisementModels = getDefaultAdList();
                            }

                        } else {

                            if (advertisementModels == null){
                                advertisementModels = new ArrayList<AdvertisementModel>();
                                advertisementModels.add(getLoginAds());

                            } else {
                                advertisementModels.add(0, getLoginAds());
                            }
                        }
                        mView.updateAdsView(advertisementModels);
                    }
                });
    }

    @Override
    public void getHomeModuleList() {
        LogUtils.i(LOG_TAG, ":getHomeModuleList 开始执行...");
        List<Item> items = null;
        if (appStateDao.hasValue(APP_KEY_HOME_MENU)){
            items = homeMenuDao.queryUserSaveMenuList();
            LogUtils.i(LOG_TAG, ":查询结果为：" + items);
            mView.updateMainNav(items);
            LogUtils.i(LOG_TAG, ":主页面菜单" + items);
        } else {
            Menu menu = ApplicationContext.getInstance().getMenu();
            items = menu.filterMenuItem(HomeDefaultConfig.DEFAULT_MAIN_MENU);

            mView.updateMainNav(items);
            LogUtils.i(LOG_TAG, ":主页面菜单" + items);

        }

        LogUtils.i(LOG_TAG, ":getHomeModuleList 执行结束...");
    }

    /**
     * 更新菜单列表
     */
    @Override
    public void updateMenuList(List<Item> items) {
        LogUtils.i(LOG_TAG, ":updateMenuList 开始执行...");
        try {
            homeMenuDao.updateMenuList(items);
        } catch (Exception ex){
            LogUtils.i(LOG_TAG, ":updateMenuList 发生异常，数据已回滚...");
        }
        LogUtils.i(LOG_TAG, ":updateMenuList 执行结束...");
    }

    /**
     * 获取基金列表
     */
    @Override
    public void getFunds(ArrayList<FundBean> funds) {
        LogUtils.i(LOG_TAG, ":getFunds 开始执行");
        LogUtils.i(LOG_TAG, "开始获取基金列表");

        final Map<String, FundBean> fundMap = new LinkedHashMap<>();

        Observable.from(funds)
                .compose(getconcurrentTransformer(new Func1<FundBean, Observable<PsnFundDetailQueryOutlayResult>>() {
                    @Override
                    public Observable<PsnFundDetailQueryOutlayResult> call(final FundBean fundBean) {
                        fundMap.put(fundBean.getFundCode(), fundBean);
                        final PsnFundDetailQueryOutlayParams params = new PsnFundDetailQueryOutlayParams();
                        params.setFundCode(fundBean.getFundCode());
                        return mFundService.psnFundDetailQueryOutlay(params).doOnNext(new Action1<PsnFundDetailQueryOutlayResult>() {
                            @Override public void call(PsnFundDetailQueryOutlayResult result) {
                                //   服务端有返回 但是所有字段都为null 此时程序会出错
                                String fundCode = result.getFundCode();
                                String beanCode = fundBean.getFundCode();
                                if(!beanCode.equals(fundCode)){

                                }
                                result.setFundCode(beanCode);
                            }
                        });
                    }
                })).compose(mRxLifecycleManager.<PsnFundDetailQueryOutlayResult>bindToLifecycle())
                .compose(SchedulersCompat.

                        <PsnFundDetailQueryOutlayResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnFundDetailQueryOutlayResult>(){

                    @Override
                    public void onCompleted() {

                        // 更新页面
                        mView.updateFundView(fundMap);
                    }

                    @Override
                    public void onNext(PsnFundDetailQueryOutlayResult psnFundDetailQueryOutlayResult) {
                        LogUtils.i(LOG_TAG, "请求的基金信息:" + psnFundDetailQueryOutlayResult);
                        FundBean fundBean = fundMap.get(psnFundDetailQueryOutlayResult.getFundCode());
                        if (psnFundDetailQueryOutlayResult != null){

                            // 基金代码
                            //fundBean.setFundCode(psnFundDetailQueryOutlayResult.getFundCode());
                            if (!StringUtils.isEmptyOrNull(psnFundDetailQueryOutlayResult.getFntype())) {
                                // 基金名称
                                fundBean.setFundName(psnFundDetailQueryOutlayResult.getFundName());
                                // 基金币种
                                fundBean.setCurrency(psnFundDetailQueryOutlayResult.getCurrency());
                                fundBean.setFntype(psnFundDetailQueryOutlayResult.getFntype());
                                fundBean.setFundIncomeRatio(psnFundDetailQueryOutlayResult.getFundIncomeRatio());
                                fundBean.setFundIncomeUnit(psnFundDetailQueryOutlayResult.getFundIncomeUnit());
                                fundBean.setOrderLowLimit(psnFundDetailQueryOutlayResult.getOrderLowLimit());
                                fundBean.setApplyLowLimit(psnFundDetailQueryOutlayResult.getApplyLowLimit());
                                fundBean.setDayIncomeRatio(psnFundDetailQueryOutlayResult.getDayIncomeRatio());
                                fundBean.setNetPrice(psnFundDetailQueryOutlayResult.getNetPrice());
                                fundBean.setChargeRate(psnFundDetailQueryOutlayResult.getChargeRate());
                                fundBean.setEndDate(psnFundDetailQueryOutlayResult.getEndDate());
                                fundBean.setIsBuy(psnFundDetailQueryOutlayResult.getIsBuy());
                                fundBean.setIsInvt(psnFundDetailQueryOutlayResult.getIsInvt());
                                fundBean.setSevenDayYield(psnFundDetailQueryOutlayResult.getSevenDayYield());
                                fundBean.setRefreshState("1");
                                //updateFundRate(fundBean.getFundCode());
                            } else {
                                fundBean.setRefreshState("2");
                            }

                        }

                    }

                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        LogUtils.i(LOG_TAG, ":getFunds 执行发生异常.");
                    }

                    @Override
                    public void commonHandleException(BiiResultErrorException biiResultErrorException){

                    }

                });

    }

//    /**
//     * 更新基金费率
//     * @param fundId
//     */
//    private void updateFundRate(String fundId){
//        WFSSQueryFundBasicDetailParams params = new WFSSQueryFundBasicDetailParams();
//        //params.setFundId(fundId);
//        params.setFundBakCode(fundId);
//        LogUtils.i(LOG_TAG, "基金编码：" + fundId + " 开始更新该基金费率...");
//        wfssFundService.queryFundBasicDetail(params)
//                .compose(mRxLifecycleManager.<WFSSQueryFundBasicDetailResult>bindToLifecycle())
//                .compose(SchedulersCompat.<WFSSQueryFundBasicDetailResult>applyIoSchedulers())
//                .subscribe(new BIIBaseSubscriber<WFSSQueryFundBasicDetailResult>() {
//                    @Override
//                    public void handleException(BiiResultErrorException biiResultErrorException) {
//
//                    }
//
//                    @Override
//                    public void onCompleted() {
//
//                    }
//
//                    @Override
//                    public void commonHandleException(BiiResultErrorException biiResultErrorException){
//
//                    }
//
//                    @Override
//                    public void onNext(WFSSQueryFundBasicDetailResult queryFundBasicDetailResult) {
//                        LogUtils.i(LOG_TAG, "请求后的基金明细" + queryFundBasicDetailResult);
//                    }
//
//                });
//    }


    @Override
    public void getGolds(final ArrayList<GoldBean> golds) {
        synchronized (this){
            if (isGoldProcess) {
                return;
            } else {
                isGoldProcess = true;
            }
        }
        PsnGetAllExchangeRatesOutlayParams ratesOutlayParams = new PsnGetAllExchangeRatesOutlayParams();
        ratesOutlayParams.setIbknum("40142");
        //ratesOutlayParams.setIbknum("43016");
        ratesOutlayParams.setOfferType("R");
        ratesOutlayParams.setParitiesType("G");
        mGlobalService.psnGetAllExchangeRatesOutlay(ratesOutlayParams)
                .compose(mRxLifecycleManager.<List<PsnGetAllExchangeRatesOutlayResult>>bindToLifecycle())
                .compose(SchedulersCompat.<List<PsnGetAllExchangeRatesOutlayResult>>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<List<PsnGetAllExchangeRatesOutlayResult>>(){

                    @Override
                    public void onCompleted() {


                    }

                    @Override
                    public void onNext(List<PsnGetAllExchangeRatesOutlayResult> result) {
                        Map<String, GoldBean> goldBeanMap = new HashMap<>();
                        if (result != null && result.size() > 0) {
                            for(GoldBean goldBean : golds) {
                                GoldBean item = filterGoldList(result, goldBean.getSourceCurrencyCode(), goldBean.getTargetCurrencyCode());
                                goldBeanMap.put(InvestUtils.getCurrencyPair(goldBean.getSourceCurrencyCode(), goldBean.getTargetCurrencyCode()), item);
                            }
                        } else {
                            for(GoldBean goldBean : golds) {
                                GoldBean item = filterGoldList(result, goldBean.getSourceCurrencyCode(), goldBean.getTargetCurrencyCode());
                                item.setRefreshState("2");
                                goldBeanMap.put(InvestUtils.getCurrencyPair(goldBean.getSourceCurrencyCode(), goldBean.getTargetCurrencyCode()), item);
                            }
                        }
                        mView.updateGoldView(goldBeanMap);
                        // 更新页面
                        isGoldProcess = false;

                    }
                    
                    @Override
                    public void commonHandleException(BiiResultErrorException biiResultErrorException){

                    }

                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        Map<String, GoldBean> goldBeanMap = new HashMap<>();
                        for(GoldBean goldBean : golds) {
                            goldBean.setRefreshState("2");
                            goldBeanMap.put(goldBean.getSourceCurrencyCode() + "-" + goldBean.getTargetCurrencyCode(), goldBean);
                        }
                        mView.updateGoldView(goldBeanMap);
                        // 更新页面
                        isGoldProcess = false;
                    }

                });
    }

    /**
     * 请求结购汇
     * @param fesses
     */
    @Override
    public void getFesses(final ArrayList<FessBean> fesses) {

        PsnGetExchangeOutlayParams params = new PsnGetExchangeOutlayParams();
        mFessService.psnGetExchangeOutlay(params)
                .compose(mRxLifecycleManager.<List<PsnGetExchangeOutlayResult>>bindToLifecycle())
                .compose(SchedulersCompat.<List<PsnGetExchangeOutlayResult>>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<List<PsnGetExchangeOutlayResult>>(){

                    @Override
                    public void onCompleted() {

                        // 更新页面

                    }

                    @Override
                    public void onNext(List<PsnGetExchangeOutlayResult> result) {
                        Map<String, FessBean> fessBeanMap = new HashMap<>();
                        for (FessBean fessBean : fesses) {
                            FessBean item = filterFessList(result, fessBean.getCurCode());
                            item.setRefreshState("1");
                            fessBeanMap.put(fessBean.getCurCode(), item);
                        }
                        mView.updateFessView(fessBeanMap);
                    }

                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {

                    }

                    @Override
                    public void commonHandleException(BiiResultErrorException biiResultErrorException){
                        Map<String, FessBean> fessBeanMap = new HashMap<>();
                        for(FessBean fessBean : fesses) {
                            fessBean.setRefreshState("2");
                            fessBeanMap.put(fessBean.getCurCode(), fessBean);
                        }
                        mView.updateFessView(fessBeanMap);
                    }

                });
    }

    @Override
    public void initInvestListView() {

        fundBeans.clear();
        goldBeans.clear();
        fessBeans.clear();
        if (!appStateDao.hasValue(APP_KEY_HOME_INVEST)) {

            for (Map.Entry<String, String> entry: HomeDefaultConfig.HOME_DEFAULT_FUND_MAP.entrySet()) {
                FundBean fundBean = new FundBean();
                fundBean.setFundCode(entry.getKey());
                fundBean.setFundName(entry.getValue());
                fundBeans.add(fundBean);
            }

            for (String code : HomeDefaultConfig.HOME_DEFAULT_GOLD_LIST) {

                String codeArr[] = code.split("-");
                GoldBean goldBean = new GoldBean();
                goldBean.setSourceCurrencyCode(codeArr[0]);
                goldBean.setTargetCurrencyCode(codeArr[1]);
                goldBeans.add(goldBean);
            }

            for (String fess: HomeDefaultConfig.HOME_DEFAULT_FESS_LIST) {
                FessBean fessBean = new FessBean();
                fessBean.setCurCode(fess);
                fessBeans.add(fessBean);
            }

        } else {
            List<InvestItemBO> investItemBOs = homeInvestDao.queryUserInvestList();
            for (InvestItemBO bo :investItemBOs) {
                switch (bo.getType()){
                    case fund:
                        FundBean fundBean = new FundBean();
                        fundBean.setFundCode(bo.getInvestId());
                        fundBean.setFundName(bo.getInvestName());
                        fundBeans.add(fundBean);
                        break;
                    case gold:
                        GoldBean goldBean = new GoldBean();
                        String[] code = bo.getInvestId().split("-");
                        if (code == null || code.length < 2){
                           return;
                        }
                        goldBean.setSourceCurrencyCode(code[0]);
                        goldBean.setTargetCurrencyCode(code[1]);
                        goldBean.setName(bo.getInvestName());
                        goldBeans.add(goldBean);
                        break;
                    case fess:
                        FessBean fessBean = new FessBean();
                        fessBean.setCurCode(bo.getInvestId());
                        fessBean.setName(fessBean.getName());
                        fessBeans.add(fessBean);
                        break;
                }
            }
        }
        mView.updateInvestListView(fundBeans, goldBeans, fessBeans);
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unsubscribe() {
        mRxLifecycleManager.onDestroy();
    }

    /**
     * 根据币种获取相应的货币对
     * @param golds
     * @param sourceCode
     * @param targetCode
     * @return
     */
    private GoldBean filterGoldList(List<PsnGetAllExchangeRatesOutlayResult> golds,String sourceCode, String targetCode){
        GoldBean gold = new GoldBean();
        gold.setSourceCurrencyCode(sourceCode);
        gold.setTargetCurrencyCode(targetCode);
        boolean hasItem = false;
        for (PsnGetAllExchangeRatesOutlayResult item : golds) {
            if (gold.getSourceCurrencyCode().equals(item.getSourceCurrencyCode())
                    && gold.getTargetCurrencyCode().equals(item.getTargetCurrencyCode())){

                gold.setIbknum(item.getIbkNum());
                gold.setState(item.getState());
                gold.setFlag(item.getFlag());
                gold.setType(item.getType());
                gold.setSellRate(item.getSellRate());
                gold.setBuyRate(item.getBuyRate());
                gold.setUpdateDate(item.getUpdateDate());
                hasItem = true;
                gold.setRefreshState("1");
                return gold;
            }
        }

        if (!hasItem) {
            gold.setRefreshState("2");
        }
        return gold;
    }

    /**
     * 根据币种获取相应的货币对
     * @param fesses
     * @param curCode
     * @return
     */
    private FessBean filterFessList(List<PsnGetExchangeOutlayResult> fesses, String curCode){
        FessBean fess = new FessBean();
        if (fesses != null) {
            for (PsnGetExchangeOutlayResult item : fesses) {
                if (curCode.equals(item.getCurCode())) {
                    fess.setCurCode(item.getCurCode());
                    fess.setUpdateDate(item.getUpdateDate());
                    fess.setBuyRate(item.getBuyRate());
                    fess.setSellRate(item.getSellRate());
                    fess.setBuyNoteRate(item.getBuyNoteRate());
                    fess.setSellNoteRate(item.getSellNoteRate());
                    fess.setUpdateDate(item.getUpdateDate());
                }
            }
        }
        return fess;
    }


    /**
     * 并发请求，结果无序需要排除 ， 发生错误返回null需要处理
     */
    private  Observable.Transformer<FundBean, PsnFundDetailQueryOutlayResult> getconcurrentTransformer(
            final Func1<FundBean, Observable<PsnFundDetailQueryOutlayResult>> func1) {
        Observable.Transformer<FundBean, PsnFundDetailQueryOutlayResult> transformer =
                new Observable.Transformer<FundBean, PsnFundDetailQueryOutlayResult >() {
            @Override public Observable<PsnFundDetailQueryOutlayResult> call(Observable<FundBean> tObservable) {
                return tObservable.flatMap(new Func1<FundBean, Observable<PsnFundDetailQueryOutlayResult>>() {
                    @Override public Observable<PsnFundDetailQueryOutlayResult> call(final FundBean fundBean) {
                        return Observable.just(fundBean)
                                .flatMap(func1)
                                .onErrorReturn(new Func1<Throwable, PsnFundDetailQueryOutlayResult>() {
                                    @Override public PsnFundDetailQueryOutlayResult call(Throwable throwable) {
                                        PsnFundDetailQueryOutlayResult result = new PsnFundDetailQueryOutlayResult();
                                        result.setFundCode(fundBean.getFundCode());
                                        return result;
                                    }
                                });
                    }
                });
            }
        };
        return transformer;
    }

    /*
     * 获取广告
     */
    public Observable<CRgetPosterListResult> getOBSAd() {
        CRgetPosterListParams params = new CRgetPosterListParams();
        params.setReleasePosition("0");//0:首页、1:生活、2:投资、
        params.setRegion("00");//首页和投资默认送00，生活送真实地区编码，参见地区编码
        return crAdService.cRgetPosterList(params);
    }

    /**
     * 获取默认的广告
     * @return
     */
    public List<AdvertisementModel> getDefaultAdList(){
        List<AdvertisementModel> adList = new ArrayList<>();
        AdvertisementModel model = new AdvertisementModel();
        model.setPosterUrl(HomeDefaultConfig.DEFAULT_HOME_ADS_PATH);
        //model.setImageUrl();
        model.setPosterType("0");
        model.setPlaceHolderImageRes(HomeDefaultConfig.DEFAULT_HOME_ADS);
        adList.add(model);
        return adList;
    }

    /**
     * 获取登录的广告
     * @return
     */
    public AdvertisementModel getLoginAds(){
        AdvertisementModel model = new AdvertisementModel();
        model.setPosterUrl(HomeDefaultConfig.LOGIN_HOME_ADS_PATH);
        model.setPosterType("0");
        model.setPlaceHolderImageRes(HomeDefaultConfig.LOGIN_HOME_ADS);
        return model;
    }

    private List<AdvertisementModel> buildAdListData(
            List<CRgetPosterListResult.PosterBean> posterBeanList) {
        List<AdvertisementModel> adBeanList = new ArrayList<>();

        if (posterBeanList == null || posterBeanList.size() == 0) {
            return adBeanList;
        }

        for (CRgetPosterListResult.PosterBean bean : posterBeanList) {
            if(bean == null)continue;
            AdvertisementModel model = new AdvertisementModel();

            model.setRegion(bean.getReleasePosition());
            model.setImageUrl(bean.getImageUrl());
            model.setPosterName(bean.getPosterName());
            model.setPosterOrder(bean.getPosterOrder());
            model.setPosterType(bean.getPosterType());
            model.setProductNature(bean.getProductNature());
            model.setProductCode(bean.getProductCode());
            model.setPosterUrl(bean.getPosterUrl());
            model.setPlaceHolderImageRes(R.drawable.boc_default_ad_bg);

            adBeanList.add(model);
        }
        return adBeanList;
    }

}
