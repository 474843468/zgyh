package com.boc.bocsoft.mobile.bocmobile.buss.system.life.presenter;

import com.boc.bocsoft.mobile.bii.bus.coin.model.PsnCoinSellerSwitch.PsnCoinSellerSwitchParams;
import com.boc.bocsoft.mobile.bii.bus.coin.model.PsnCoinSellerSwitch.PsnCoinSellerSwitchResult;
import com.boc.bocsoft.mobile.bii.bus.coin.service.CoinService;
import com.boc.bocsoft.mobile.bii.bus.global.model.PSNCreatConversation.PSNCreatConversationParams;
import com.boc.bocsoft.mobile.bii.bus.global.model.PSNGetTokenId.PSNGetTokenIdParams;
import com.boc.bocsoft.mobile.bii.bus.global.service.GlobalService;
import com.boc.bocsoft.mobile.bii.bus.plps.model.PsnPlpsDeleteCommonUsedPaymentList.PsnPlpsDeleteCommonUsedPaymentListParams;
import com.boc.bocsoft.mobile.bii.bus.plps.model.PsnPlpsDeleteCommonUsedPaymentList.PsnPlpsDeleteCommonUsedPaymentListParams.FlowFileBean;
import com.boc.bocsoft.mobile.bii.bus.plps.model.PsnPlpsDeleteCommonUsedPaymentList.PsnPlpsDeleteCommonUsedPaymentListResult;
import com.boc.bocsoft.mobile.bii.bus.plps.model.PsnPlpsGetCityListByPrvcShortName.PsnPlpsGetCityListByPrvcShortNameParams;
import com.boc.bocsoft.mobile.bii.bus.plps.model.PsnPlpsGetCityListByPrvcShortName.PsnPlpsGetCityListByPrvcShortNameResult;
import com.boc.bocsoft.mobile.bii.bus.plps.model.PsnPlpsQueryAllPaymentList.PsnPlpsQueryAllPaymentListParams;
import com.boc.bocsoft.mobile.bii.bus.plps.model.PsnPlpsQueryAllPaymentList.PsnPlpsQueryAllPaymentListResult;
import com.boc.bocsoft.mobile.bii.bus.plps.model.PsnPlpsQueryChildrenMenus.PsnPlpsQueryChildrenMenusParams;
import com.boc.bocsoft.mobile.bii.bus.plps.model.PsnPlpsQueryChildrenMenus.PsnPlpsQueryChildrenMenusResult.ChildrenMenuBean;
import com.boc.bocsoft.mobile.bii.bus.plps.model.PsnPlpsQueryCommonUsedPaymentList.PsnPlpsQueryCommonUsedPaymentListParams;
import com.boc.bocsoft.mobile.bii.bus.plps.model.PsnPlpsQueryCommonUsedPaymentList.PsnPlpsQueryCommonUsedPaymentListResult;
import com.boc.bocsoft.mobile.bii.bus.plps.service.PsnPlpsService;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.ApplicationConfig;
import com.boc.bocsoft.mobile.bocmobile.ApplicationContext;
import com.boc.bocsoft.mobile.bocmobile.base.Exception.BIIBaseSubscriber;
import com.boc.bocsoft.mobile.bocmobile.base.utils.LogUtils;
import com.boc.bocsoft.mobile.bocmobile.base.utils.SpUtils;
import com.boc.bocsoft.mobile.bocmobile.buss.system.home.model.AdvertisementModel;
import com.boc.bocsoft.mobile.bocmobile.buss.system.life.LifeDataManger;
import com.boc.bocsoft.mobile.bocmobile.buss.system.life.LifeTools;
import com.boc.bocsoft.mobile.bocmobile.buss.system.life.dao.LifeMenuDao;
import com.boc.bocsoft.mobile.bocmobile.buss.system.life.model.LifeMenuModel;
import com.boc.bocsoft.mobile.bocmobile.buss.system.life.model.LifeVo;
import com.boc.bocsoft.mobile.bocmobile.buss.system.life.ui.LifeContract;
import com.boc.bocsoft.mobile.common.utils.StringUtils;
import com.boc.bocsoft.mobile.common.utils.gson.GsonUtils;
import com.boc.bocsoft.mobile.cr.bus.ad.model.CRgetPosterList.CRgetPosterListParams;
import com.boc.bocsoft.mobile.cr.bus.ad.model.CRgetPosterList.CRgetPosterListResult;
import com.boc.bocsoft.mobile.cr.bus.ad.service.CRAdService;
import com.boc.bocsoft.mobile.framework.rx.SchedulersCompat;
import com.boc.bocsoft.mobile.framework.rx.lifecycle.RxLifecycleManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * 生活频道 Presenter
 * Created by eyding on 16/7/29.
 */
public class LifePresenter implements LifeContract.Presenter {
  public final static String TAG = LifePresenter.class.getSimpleName();
  private final static String STATE = "has_user_loginlife";//用户是否登录过生活页面,未登录过使用默认数据,登陆过使用保存数据

  private RxLifecycleManager rxLifecycleManager;

  private LifeContract.View lifeView;
  private LifeContract.LocationChooseView locationChooseView;
  private LifeContract.MoreView moreView;


  private PsnPlpsService psnPlpsService;
  private GlobalService globalService;
  private CRAdService crAdService;

  private LifeVo.CityVo currentLocation;

  private LifeTools lifeTools;
  private String conversitionId;

  private boolean hasAdLoad = false;//是否加载了广告
  private boolean hasCoinLoad = false;//是否加载了纪念币

  private LifeMenuDao lifeMenuDao;

  private static LifeDataManger dataManger = new LifeDataManger();

  public LifePresenter(LifeContract.View view){
    this.lifeView = view;

    lifeTools = new LifeTools();
    rxLifecycleManager = new RxLifecycleManager();

    psnPlpsService = new PsnPlpsService();
    globalService = new GlobalService();
    crAdService = new CRAdService();
    lifeMenuDao = new LifeMenuDao();
  }
  public void setLocationView(LifeContract.LocationChooseView locationView){
    this.locationChooseView = locationView;
  }

  public void setMoreView(LifeContract.MoreView moreView){
    this.moreView = moreView;
  }

  @Override public void initData() {

    LifeVo.CityVo cityVo = loadLocation();

    if(cityVo == null){
      //未选择地区则设置默认地区
      cityVo = lifeTools.getDefauleCity();
    }

    currentLocation = cityVo;

    lifeView.endLocation(cityVo);

    //清除菜单数据
    //lifeView.updateMenu(null);

    if(isLogin()){
      //登录则刷新数据
      loadDataByLocation(cityVo);
    }else{
      //未登录设置默认值
      loadDataNoLogin();
    }
  }



  //加载省
  public void loadProvinceList(){
    //从本地加载
    locationChooseView.updateProvinceData(lifeTools.getAllProvinceDatas());
    //locationChooseView.updateHotCities(lifeTools.getHotCities());
  }


  private Subscription citiesSubscription;
  //加载市
  @Override public void loadCitysByProvince(final LifeVo.ProvinceVo provinceVo, final boolean isFromHotCity) {
    if(provinceVo == null || StringUtils.isEmptyOrNull(provinceVo.getShortName())){
      return;
    }

    if(citiesSubscription != null && !citiesSubscription.isUnsubscribed()){
      citiesSubscription.unsubscribe();
    }

    locationChooseView.showLoading("");

    PsnPlpsGetCityListByPrvcShortNameParams params = new PsnPlpsGetCityListByPrvcShortNameParams();
    params.setPrvcShortName(provinceVo.getShortName());

    citiesSubscription = psnPlpsService.psnPlpsGetCityListByPrvcShortName(params)
        .compose(rxLifecycleManager.<PsnPlpsGetCityListByPrvcShortNameResult>bindToLifecycle())
        .compose(SchedulersCompat.<PsnPlpsGetCityListByPrvcShortNameResult>applyIoSchedulers())
        .subscribe(new BIIBaseSubscriber<PsnPlpsGetCityListByPrvcShortNameResult>() {
          @Override public void handleException(BiiResultErrorException biiResultErrorException) {
            locationChooseView.closeLoading();
          }

          @Override public void onCompleted() {
            locationChooseView.closeLoading();
          }

          @Override public void onNext(PsnPlpsGetCityListByPrvcShortNameResult result) {
            List<LifeVo.CityVo> cityVoList = lifeTools.buildCityListData(provinceVo, result);

            if (isFromHotCity && cityVoList != null && cityVoList.size() == 1) {
              locationChooseView.onCityChoose(cityVoList.get(0));
            } else {
              locationChooseView.updateCityData(lifeTools.buildCityListData(provinceVo, result));
            }
          }
        });
  }

  @Override public void loadDataByLocation(final LifeVo.CityVo cityVo) {
    LogUtils.d("dding","---开始加载数据:"+cityVo);
    if(cityVo == null){
      return;
    }
    currentLocation = cityVo;
    //保存当前选中位置
    saveLocation(cityVo);

    if(!hasAdLoad){
      loadAd(currentLocation);
    }

    //加载缴费菜单
    loadNoLoginMenus();
    loadPayment2(currentLocation);
  }

  public void loadDataNoLogin(){

    loadNoLoginMenus();

    if(!hasAdLoad){
      loadAd(currentLocation);
    }

  }


  private void loadNoLoginMenus(){

    //load 保存的前登录账户
    String user = SpUtils.getLNhoneSpString(ApplicationContext.getAppContext(), SpUtils.SPKeys.KEY_LOGINNAME,"");
    boolean hasAOrder = lifeMenuDao.hsInit(user);

    if(hasAOrder){

      List<LifeMenuModel> list =
          lifeMenuDao.getMenus(user, currentLocation.getCode());

      if(list == null){
        list = new ArrayList<>();
      }
      list.addAll(lifeTools.getCountryMenus());

      dataManger.setAllListMenuModels(list);

      //list.add(LifeTools.getMoreMenu());
      lifeView.updateMenu(lifeTools.getHomeMenus(dataManger.getAllListMenuModels()));

    }else{
      //未初始化过
      dataManger.setAllListMenuModels(lifeTools.getDefaultLifeMenus());
    }

    lifeView.updateMenu(lifeTools.getHomeMenus(dataManger.getAllListMenuModels()));
  }

  @Override public void delCommonUse(final LifeMenuModel menuModel) {

    if(menuModel == null || StringUtils.isEmptyOrNull(menuModel.getFlowFileId())){
      return;
    }
    moreView.showLoading("");
    //
    final PsnPlpsDeleteCommonUsedPaymentListParams params = new PsnPlpsDeleteCommonUsedPaymentListParams();
    List<FlowFileBean> list = new ArrayList<>();
    FlowFileBean bean = new FlowFileBean();
    bean.setFlowFileId(menuModel.getFlowFileId());
    list.add(bean);
    params.setFlowFileIdList(list);

    globalService.psnCreatConversation(new PSNCreatConversationParams())
        .flatMap(new Func1<String, Observable<String>>() {
          @Override public Observable<String> call(String s) {
            PSNGetTokenIdParams tokenIdParams = new PSNGetTokenIdParams();
            tokenIdParams.setConversationId(s);
            params.setConversationId(s);

            return globalService.psnGetTokenId(tokenIdParams);
          }
        }).flatMap(new Func1<String, Observable<PsnPlpsDeleteCommonUsedPaymentListResult>>() {
      @Override public Observable<PsnPlpsDeleteCommonUsedPaymentListResult> call(String s) {
        params.setToken(s);
        return psnPlpsService.deleteCommonUsedPaymentList(params);
      }
    }).compose(rxLifecycleManager.<PsnPlpsDeleteCommonUsedPaymentListResult>bindToLifecycle())
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .delaySubscription(200, TimeUnit.MILLISECONDS)
        .subscribe(new BIIBaseSubscriber<PsnPlpsDeleteCommonUsedPaymentListResult>() {
          @Override public void handleException(BiiResultErrorException biiResultErrorException) {
            moreView.endLoading();
          }

          @Override public void onCompleted() {

          }

          @Override public void onNext(PsnPlpsDeleteCommonUsedPaymentListResult result) {
            moreView.endLoading();

            //删除成功 更新内存数据
            dataManger.removeUseCommon(menuModel);

            moreView.onItemDelSuccess(menuModel);
          }
        });

  }

  @Override public List<Map<String, Object>> getMapParamsCommonUse() {
    return lifeTools.buildCommListmap(dataManger.getUseCommonList());
  }

  @Override public List<Map<String, Object>> getMapParamsCity() {
    return lifeTools.buildAllListmap(dataManger.getAllPaymentList());
  }

  /**
   * 更具menuID 获取菜单名称
   * @param id
   * @return
   */
  public String getMenuNameById(String id){
    List<ChildrenMenuBean> childrenMenuBeanList = dataManger.getChildrenMenuBeanList();

    if(childrenMenuBeanList == null || childrenMenuBeanList.size()==0 || id == null)return "";

    for(ChildrenMenuBean bean:childrenMenuBeanList){
      if(id.equals(bean.getMenusId()))return bean.getMenusName();
    }
    return "";
  }

  private void loadAd(final LifeVo.CityVo cityVo) {
    //广告和用户无关和地区有关

    //首先加载缓存||默认显示的值  -->加载网络&本地缓存 --->设置值

    LogUtils.d("dding","---- 开始加载广告 --  >");

    Observable.just(cityVo)
        .compose(rxLifecycleManager.<LifeVo.CityVo>bindToLifecycle())
        .subscribeOn(Schedulers.io())
        .flatMap(new Func1<LifeVo.CityVo, Observable<List<AdvertisementModel>>>() {
          @Override public Observable<List<AdvertisementModel>> call(LifeVo.CityVo cityVo) {
            //TODO load 缓存
            LogUtils.d("dding","---- 开始加载广告 cache--  >");
            List<AdvertisementModel> list = new ArrayList<>();
            return Observable.just(list);
          }
        })
        .observeOn(AndroidSchedulers.mainThread())
        .map(new Func1<List<AdvertisementModel>, Object>() {
          @Override public Object call(List<AdvertisementModel> advertisementModelList) {
            // 设置默认的ad
            LogUtils.d("dding","---- 开始加载广告 cache success--  >");
            List<AdvertisementModel> defaultAds = lifeTools.getDefaultAds();
            lifeView.updateBanner(defaultAds);
            return Observable.just(defaultAds);
          }
        })
        .observeOn(Schedulers.io())
        .flatMap(new Func1<Object, Observable<CRgetPosterListResult>>() {
          @Override public Observable<CRgetPosterListResult> call(Object o) {

            final CRgetPosterListParams params = new CRgetPosterListParams();
            params.setReleasePosition("1");///发布位置 0:首页、1:生活、2:投资、
            //params.setRegion(cityVo.getCode());
            ////地区  首页和投资默认送00，生活送真实地区编码，参见地区编码
            params.setRegion("00");
            return crAdService.cRgetPosterList(params);
          }
        })
        .map(new Func1<CRgetPosterListResult, List<AdvertisementModel>>() {
          @Override
          public List<AdvertisementModel> call(CRgetPosterListResult cRgetPosterListResult) {
            LogUtils.d("dding","---- 广告 net  success--  >");
            List<CRgetPosterListResult.PosterBean> arrayList = cRgetPosterListResult.getArrayList();
            return lifeTools.buildAdList(arrayList);
          }
        })
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Subscriber<List<AdvertisementModel>>() {

          @Override public void onCompleted() {

          }

          @Override public void onError(Throwable throwable) {
            //do nothing
            throwable.printStackTrace();
            LogUtils.d("dding","---- 广告 error --- >"+throwable.getMessage());
          }

          @Override public void onNext(List<AdvertisementModel> advertisementModelList) {
            LogUtils.d("dding","---- 广告 success --- >");
            if(advertisementModelList==null||advertisementModelList.size()==0)return;
            lifeView.updateBanner(advertisementModelList);
          }
        });
  }

  @Override public void loadCommonUseMenus() {
    getCommonUseOBS().compose(rxLifecycleManager.<List<LifeMenuModel>>bindToLifecycle())
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new BIIBaseSubscriber<List<LifeMenuModel>>() {
          @Override public void handleException(BiiResultErrorException exception) {
            if(moreView != null){
              moreView.onLoadCommonUseEnd(false,null);
            }
          }

          @Override public void onCompleted() {

          }

          @Override public void onNext(List<LifeMenuModel> menuModels) {
            if(moreView != null){
              String loginName = "";
              if(ApplicationContext.getInstance().getUser()!=null){
                loginName = ApplicationContext.getInstance().getUser().getLoginName();
              }
              lifeMenuDao.saveUserCommonMenu(menuModels,loginName);

              moreView.onLoadCommonUseEnd(true,menuModels);
            }
          }
        });
  }

  private void loadPayment2(final LifeVo.CityVo cityVo) {
    lifeView.showLoading("正在加载...");

/*    LifeMenuModel recordMenu = LifeTools.getRecordMenu();
    final ArrayList<LifeMenuModel> list = new ArrayList<>();
    list.add(recordMenu);

    Observable<ArrayList<LifeMenuModel>> recordOBS = Observable.just(list);*/

    //获取menuOBS
    Observable<List<ChildrenMenuBean>> childMenuOBS =
        psnPlpsService.psnPlpsQueryChildrenMenus(new PsnPlpsQueryChildrenMenusParams())
            .compose(rxLifecycleManager.<List<ChildrenMenuBean>>bindToLifecycle())
            .map(new Func1<List<ChildrenMenuBean>, List<ChildrenMenuBean>>() {
              @Override
              public List<ChildrenMenuBean> call(List<ChildrenMenuBean> childrenMenuBeen) {
                //保存源数据
                dataManger.setChildrenMenuBeanList(childrenMenuBeen);
                return childrenMenuBeen;
              }
            });

    final PsnPlpsQueryAllPaymentListParams params = new PsnPlpsQueryAllPaymentListParams();
    params.setCityDispNo(cityVo.getCode());
    params.setPrvcShortName(cityVo.getProvinceVo().getShortName());

    Observable<List<LifeMenuModel>> allOBS = childMenuOBS.flatMap(
        new Func1<List<ChildrenMenuBean>, Observable<PsnPlpsQueryAllPaymentListResult>>() {
          @Override public Observable<PsnPlpsQueryAllPaymentListResult> call(
              List<ChildrenMenuBean> childrenMenuBeen) {
            return psnPlpsService.psnPlpsQueryAllPaymentList(params);
          }
        }).flatMap(new Func1<PsnPlpsQueryAllPaymentListResult, Observable<List<LifeMenuModel>>>() {
      @Override
      public Observable<List<LifeMenuModel>> call(PsnPlpsQueryAllPaymentListResult result) {

        dataManger.setAllPaymentList(result.getAllPaymentList());

        List<LifeMenuModel> menuModels = lifeTools.buildAllPaymentLifeMenus(dataManger.getAllPaymentList(), cityVo);

        SpUtils.saveBoolean(ApplicationContext.getAppContext(),
            SpUtils.SPKeys.KEY_LIFE_HASCHOOSE_LOCATION, true);
        //menuDao.saveCitys(menuModels,cityVo.getCode());
        return Observable.just(menuModels);
      }
    });

    //加载用户常用
    Observable<List<LifeMenuModel>> commonUsedOBS = getCommonUseOBS();

    //组合两组数据
    Observable<List<LifeMenuModel>> countryOBS = Observable.just(lifeTools.getCountryMenus());

    final ArrayList<LifeMenuModel> tmpList = new ArrayList<>();
    Observable.merge(commonUsedOBS,allOBS,countryOBS)
        .compose(rxLifecycleManager.<List<LifeMenuModel>>bindToLifecycle())
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new BIIBaseSubscriber<List<LifeMenuModel>>() {
          @Override public void onCompleted() {
            lifeView.endLoading();
            LogUtils.d("dding", " --- >> 请求结束:" + tmpList.size());

            //设置内存菜单数据
            dataManger.setAllListMenuModels(tmpList);

            //保存菜单,菜单可能有变动
            saveMenuSort(tmpList);


            List<LifeMenuModel> menuModels = lifeTools.getHomeMenus(tmpList);

            lifeView.updateMenu(menuModels);

            lifeView.endLoadMenus(true);
          }

          @Override public void handleException(BiiResultErrorException exp) {
            lifeView.endLoading();
            lifeView.updateMenu(null);
            lifeView.endLoadMenus(false);
            LogUtils.d("dding", " --- >> 请求结束: shibai" + exp.getMessage());
          }

          @Override public void onNext(List<LifeMenuModel> lifeMenuModels) {
            tmpList.addAll(lifeMenuModels);
          }
        });
  }


  private Observable<List<LifeMenuModel>> getCommonUseOBS(){
   return globalService.psnCreatConversation(new PSNCreatConversationParams())
        .flatMap(new Func1<String, Observable<PsnPlpsQueryCommonUsedPaymentListResult>>() {
          @Override public Observable<PsnPlpsQueryCommonUsedPaymentListResult> call(String s) {
            PsnPlpsQueryCommonUsedPaymentListParams params =
                new PsnPlpsQueryCommonUsedPaymentListParams();
            params.setConversationId(s);
            conversitionId = s;
            //开始加载常用
            return psnPlpsService.psnPlpsQueryCommonUsedPaymentList(params);
          }
        })
        .flatMap(
            new Func1<PsnPlpsQueryCommonUsedPaymentListResult, Observable<List<LifeMenuModel>>>() {
              @Override public Observable<List<LifeMenuModel>> call(
                  PsnPlpsQueryCommonUsedPaymentListResult result) {

                //常用获取成功后 更新内存数据
                dataManger.setUseCommonList(result.getPaymentList());

                List<LifeMenuModel> menuModels = lifeTools.buildUserCommonLifeMenus(result.getPaymentList());

                return Observable.just(menuModels);
              }
            });
  }

  private void loadCoinStatus(){
    LogUtils.d("dding","开始加载纪念币");
    new CoinService().psnCoinSellerSwitch(ApplicationConfig.COIN_URL,new PsnCoinSellerSwitchParams())
        .compose(rxLifecycleManager.<PsnCoinSellerSwitchResult>bindToLifecycle())
        .compose(SchedulersCompat.<PsnCoinSellerSwitchResult>applyIoSchedulers())
        .subscribe(new BIIBaseSubscriber<PsnCoinSellerSwitchResult>() {

          @Override
          public void commonHandleException(BiiResultErrorException biiResultErrorException) {
            LogUtils.d("dding","纪念币接口异常:"+biiResultErrorException.getErrorMessage());
            //TODO 调用super弹出错误?
            //super.commonHandleException(biiResultErrorException);
          }

          @Override public void handleException(BiiResultErrorException biiResultErrorException) {

          }

          @Override public void onCompleted() {

          }

          @Override public void onNext(PsnCoinSellerSwitchResult result) {
            hasCoinLoad = true;
            //更新纪念币副标题
            LogUtils.d("dding","纪念币qing请求结束");
            lifeView.updateCoinStatus("",result.isSwitch());
          }
        });
  }



  @Override public void saveMenuSort(final  List<LifeMenuModel> menuModels) {
    //未登录不保存
    if (!isLogin()) return;

    if (ApplicationContext.getInstance().getUser() == null
        || currentLocation() == null
        || menuModels == null) {
      return;
    }

    LogUtils.d("dding", "---保存菜单:" + menuModels.size());

    Observable.create(new Observable.OnSubscribe<String>() {
      @Override public void call(Subscriber<? super String> subscriber) {
        if (subscriber.isUnsubscribed()) {
          return;
        }
        //保存菜单
        //LifeTools.saveLocalMenus(menuModels,currentLocation);
        String loginName = ApplicationContext.getInstance().getUser().getLoginName();

        lifeMenuDao.saveMenuSort(menuModels,loginName,currentLocation.getCode());
      }
    })
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Subscriber<String>() {
          @Override public void onCompleted() {

          }

          @Override public void onError(Throwable throwable) {
            LogUtils.d("dding", "----菜单保存失败:" + throwable);
          }

          @Override public void onNext(String s) {

            LogUtils.d("dding", "----菜单保存成功");
          }
        });
  }


  @Override public boolean isLogin() {
    return ApplicationContext.getInstance().getUser()!=null && ApplicationContext.getInstance().getUser().isLogin();
  }

  @Override public LifeVo.CityVo currentLocation() {
    return currentLocation;
  }

  @Override public List<LifeMenuModel> getAllMenuModels() {
    return dataManger.getAllListMenuModels();
  }

  @Override public List<LifeMenuModel> getAllDisplayMenuModels() {
    List<LifeMenuModel> allMenuModels = getAllMenuModels();
    if(allMenuModels == null || allMenuModels.size() == 0)return new ArrayList<>();
    List<LifeMenuModel> result = new ArrayList<>();

    for(LifeMenuModel menuModel:allMenuModels){
      if(menuModel.isDisplay()){
        result.add(menuModel);
      }
    }
    return result;
  }

  @Override public String getConversitionId() {
    return conversitionId;
  }

  @Override public void subscribe() {

  }

  @Override public void unsubscribe() {
    rxLifecycleManager.onDestroy();
  }


  private void saveLocation(LifeVo.CityVo vo){
    String json = GsonUtils.getGson().toJson(vo);
    LogUtils.d("dding","----保存当前城市:"+json);
    ApplicationContext instance = ApplicationContext.getInstance();
    SpUtils.saveString(instance, "location", json);
  }

  /**
   * 加载保存的位置
   * @return
   */
  private LifeVo.CityVo loadLocation(){
    ApplicationContext instance = ApplicationContext.getInstance();
    String location = SpUtils.getSpString(instance, "location", "");
    if(StringUtils.isEmptyOrNull(location)){
      return  null;
    }
    try {
      return   GsonUtils.getGson().fromJson(location, LifeVo.CityVo.class);
    }catch (Exception e){
      return null;
    }
  }
}

