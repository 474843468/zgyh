package com.boc.bocsoft.mobile.bocmobile.buss.system.invest.presenter;

import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import com.boc.bocsoft.mobile.bii.bus.asset.model.PsnAssetBalanceQuery.PsnAssetBalanceQueryParams;
import com.boc.bocsoft.mobile.bii.bus.asset.model.PsnAssetBalanceQuery.PsnAssetBalanceQueryResult;
import com.boc.bocsoft.mobile.bii.bus.asset.service.AssetService;
import com.boc.bocsoft.mobile.bii.bus.financing.model.PsnXpadProductDetailQueryOutlay.PsnXpadProductDetailQueryOutlayParams;
import com.boc.bocsoft.mobile.bii.bus.financing.model.PsnXpadProductDetailQueryOutlay.PsnXpadProductDetailQueryOutlayResult;
import com.boc.bocsoft.mobile.bii.bus.financing.service.FinancingService;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundDetailQueryOutlay.PsnFundDetailQueryOutlayParams;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundDetailQueryOutlay.PsnFundDetailQueryOutlayResult;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnGetFundDetail.PsnGetFundDetailParams;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnGetFundDetail.PsnGetFundDetailResult;
import com.boc.bocsoft.mobile.bii.bus.fund.service.FundService;
import com.boc.bocsoft.mobile.bii.bus.global.model.PSNBmpsCreatConversation.PSNBmpsCreatConversationParams;
import com.boc.bocsoft.mobile.bii.bus.global.service.GlobalService;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadAccountQuery.PsnXpadAccountQueryParams;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadAccountQuery.PsnXpadAccountQueryResult;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadProductDetailQuery.PsnXpadProductDetailQueryParams;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadProductDetailQuery.PsnXpadProductDetailQueryResult;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.service.WealthManagementService;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.ApplicationContext;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.Exception.BIIBaseSubscriber;
import com.boc.bocsoft.mobile.bocmobile.base.utils.LogUtils;
import com.boc.bocsoft.mobile.bocmobile.buss.common.menu.Item;
import com.boc.bocsoft.mobile.bocmobile.buss.common.menu.ModuleCode;
import com.boc.bocsoft.mobile.bocmobile.buss.common.util.InvestUtils;
import com.boc.bocsoft.mobile.bocmobile.buss.system.common.dao.AppStateDao;
import com.boc.bocsoft.mobile.bocmobile.buss.system.common.data.InvestDataCenter;
import com.boc.bocsoft.mobile.bocmobile.buss.system.home.model.AdvertisementModel;
import com.boc.bocsoft.mobile.bocmobile.buss.system.invest.InvestTools;
import com.boc.bocsoft.mobile.bocmobile.buss.system.invest.dao.InvestMenuDao;
import com.boc.bocsoft.mobile.bocmobile.buss.system.invest.model.AssetDetailVo;
import com.boc.bocsoft.mobile.bocmobile.buss.system.invest.model.AssetVo;
import com.boc.bocsoft.mobile.bocmobile.buss.system.invest.model.InvestItemVo;
import com.boc.bocsoft.mobile.bocmobile.buss.system.invest.ui.InvestContract;
import com.boc.bocsoft.mobile.common.utils.StringUtils;
import com.boc.bocsoft.mobile.cr.bus.ad.model.CRgetPosterList.CRgetPosterListParams;
import com.boc.bocsoft.mobile.cr.bus.ad.model.CRgetPosterList.CRgetPosterListResult;
import com.boc.bocsoft.mobile.cr.bus.ad.service.CRAdService;
import com.boc.bocsoft.mobile.cr.bus.product.model.CRgetProductList.CRgetProductListParams;
import com.boc.bocsoft.mobile.cr.bus.product.model.CRgetProductList.CRgetProductListResult;
import com.boc.bocsoft.mobile.cr.bus.product.model.CRgetProductList.CRgetProductListResult.ProductBean;
import com.boc.bocsoft.mobile.cr.bus.product.service.CRProductService;
import com.boc.bocsoft.mobile.framework.rx.lifecycle.RxLifecycleManager;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Action2;
import rx.functions.Func0;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by dingeryue on 2016年08月17.
 */
public class InvestPresenter implements InvestContract.Presenter {

  private final String STATE = "invest_menu";
  public final static String[] ALL_MENU = new String[] {
      ModuleCode.MODULE_BOCINVT_0000,//理财
      ModuleCode.MODULE_AFINC_0000,//基金
      ModuleCode.MODULE_ISFOREXSTORAGECASH_0000,//双向宝

      ModuleCode.MODULE_BALANCE_0000,//余额理财
      ModuleCode.MODULE_GOLDACCOUNT_0000,//账户贵金属
      ModuleCode.MODULE_FOREX_STORAGE_CASH_0000,//外汇买卖
      ModuleCode.MODULE_GOLD_STORE_0000,//贵金属积存
  };

  private RxLifecycleManager lifecycleManager;
  private Handler mainHandler;
  private InvestContract.View investView;
  private CRAdService crAdService;
  private CRProductService productService;
  private AssetService assetService;

  private FinancingService financingService;//理财servie
  private FundService fundService;//基金service
  private  WealthManagementService wealthManagementService;

  private InvestMenuDao menuDao;
  private AssetVo userAssetVo = null;
  private List<AssetDetailVo> assetDetailVos;

  private boolean hasAdLoad = false;
  private boolean hasMenuLoad = false;
  private boolean hasProductsLoad = false;
  private boolean hasAccountLoadSuccess = false;//登记账户是否加载成功,BI请求成功,无账户也算成功

  public InvestPresenter(InvestContract.View investView) {
    this.investView = investView;
    lifecycleManager = new RxLifecycleManager();
    mainHandler = new Handler(Looper.getMainLooper());
    crAdService = new CRAdService();
    productService = new CRProductService();

    fundService = new FundService();
    financingService = new FinancingService();
    assetService = new AssetService();
    wealthManagementService = new WealthManagementService();

    menuDao = new InvestMenuDao();
  }

  @Override public boolean isLogin() {
    return ApplicationContext.getInstance().getUser() != null && ApplicationContext.getInstance()
        .getUser()
        .isLogin();
  }

  @Override public void uptateLoginState() {
    if (!hasAdLoad) {
      //加载广告
      loadAd();
    }

    if (!hasMenuLoad) {
      loadMenu();
    }

    if (!hasProductsLoad) {
      loadInvestList();
    } else {
      //TODO 理财根据登录状态刷新数据
      loadInvestList();
    }

    //登录前广告，登陆后投资金额
    if (isLogin()) {
      investView.changUILogin();
      loadMinAsset();
      //startLoadDataNoLogin();
    } else {
      userAssetVo = null;
      investView.changUINoLoin();
      //startLoadDataNoLogin();
    }
  }

  @Override public ArrayList<Item> getAllMenuModels() {
    //return ApplicationContext.getInstance().getMenu().filterMenuItem(ALL_MENU);
    return  ApplicationContext.getInstance().getMenu().getItemArrayByCategoryId("financeSupermarket");
  }

  @Override public AssetVo userAsset() {
    return userAssetVo;
  }

  @Override public List<AssetDetailVo> userAssetDetailList() {
    return assetDetailVos;
  }

  @Override public void saveMenuSort(List<Item> chooseDatas) {
    if (chooseDatas == null) {
      chooseDatas = new ArrayList<>();
    }

    Observable.from(chooseDatas).filter(new Func1<Item, Boolean>() {
      @Override public Boolean call(Item item) {
        return !Item.NO_ID.equals(item.getModuleId());
      }
    }).collect(new Func0<List<Item>>() {
      @Override public List<Item> call() {
        return new ArrayList<>();
      }
    }, new Action2<List<Item>, Item>() {
      @Override public void call(List<Item> items, Item item) {
        items.add(item);
      }
    }).subscribe(new Subscriber<List<Item>>() {
      @Override public void onCompleted() {

      }

      @Override public void onError(Throwable throwable) {

      }

      @Override public void onNext(List<Item> items) {
        try {
          menuDao.updateMenuList(items);
          new AppStateDao().updateAppState(STATE);
        } catch (Exception e) {
        }
      }
    });
  }

  private void loadMenu() {
    //从数据库加载
    AppStateDao appStateDao = new AppStateDao();

    boolean invest_menu = appStateDao.hasValue(STATE);

    ArrayList<Item> allMenuList =
        ApplicationContext.getInstance().getMenu().filterMenuItem(ALL_MENU);

    if (!invest_menu) {
      investView.updateMenus(
          allMenuList.subList(0, allMenuList.size() >= 7 ? 7 : allMenuList.size()));
      return;
    }
    List<Item> items = menuDao.queryInvestSaveMenuList();
    investView.updateMenus(items);
  }

  /**
   * 登录状态加载数据
   */
  private void startLoadDataNoLogin() {
    loadAd();
    loadProductList2();
  }

  /**
   * 登录状态加载数据
   */
  private void startLoadDataLogin() {
    //loadAd();
    //TODO load 登录后推荐产品
    //TODO load 个人资产信息
    loadMinAsset();
  }

  /**
   * 加载广告
   */
  public void loadAd() {

    Observable.just("")
        .map(new Func1<String, String>() {
          @Override public String call(String s) {
            List<AdvertisementModel> advertisementModelList = defaultAdList();
            investView.updateAdData(advertisementModelList);
            return s;
          }
        })
        .observeOn(Schedulers.io())
        .flatMap(new Func1<String, Observable<CRgetPosterListResult>>() {
          @Override public Observable<CRgetPosterListResult> call(String s) {

            return getOBSAd();
          }
        })
        .compose(lifecycleManager.<CRgetPosterListResult>bindToLifecycle())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new BIIBaseSubscriber<CRgetPosterListResult>() {
          @Override public void onCompleted() {

          }
          @Override public void handleException(BiiResultErrorException biiResultErrorException) {

          }

          @Override
          public void commonHandleException(BiiResultErrorException biiResultErrorException) {
            //super.commonHandleException(biiResultErrorException);
          }

          @Override public void onNext(CRgetPosterListResult cRgetPosterListResult) {
            //ToastUtils.show("------广告加载成功----");
            List<AdvertisementModel> advertisementModels =
                buildAdListData(cRgetPosterListResult.getArrayList());
            if(advertisementModels != null && advertisementModels.size()>0){
              investView.updateAdData(advertisementModels);
            }
          }
        });
  }

  /**
   * 加载优选推荐
   */
  @Override public void loadInvestList() {
    loadProductList2();
  }

  /**
   * 刷新某一条详情
   */
  @Override public void loadProductDetail(final int pos, final InvestItemVo vo) {
    //重置
    conversation = null;
    ibkNumber = null;

    //判断 理财||基金
    if (vo == null) return;

    LogUtils.d("dding","加载产品详情:"+pos+"  "+vo.getProductName());

    if (InvestTools.isFinancing(vo)) {
      //理财
      Observable.just(vo)
          .subscribeOn(Schedulers.io())
          .flatMap(new Func1<InvestItemVo, Observable<InvestItemVo>>() {
            @Override public Observable<InvestItemVo> call(InvestItemVo investItemVo) {
              return getOBSFinancing(vo).subscribeOn(Schedulers.io());
            }
          }).compose(lifecycleManager.<InvestItemVo>bindToLifecycle())
          .delaySubscription(300, TimeUnit.MILLISECONDS)
          .observeOn(AndroidSchedulers.mainThread())
          .subscribe(new BIIBaseSubscriber<InvestItemVo>() {
            @Override public void onCompleted() {
            }

            @Override
            public void commonHandleException(BiiResultErrorException biiResultErrorException) {
              vo.setRefrensh(false);
              investView.updateItemProduct(vo, pos);
            }

            @Override public void handleException(BiiResultErrorException biiResultErrorException) {

            }

            @Override public void onNext(InvestItemVo vo) {
              vo.setRefrensh(false);
              investView.updateItemProduct(vo, pos);
            }
          });
    } else if (InvestTools.isFund(vo)) {
      //基金
      getOBSFund(vo).subscribeOn(Schedulers.io())
          .compose(lifecycleManager.<InvestItemVo>bindToLifecycle())
          .delaySubscription(300, TimeUnit.MILLISECONDS)
          .observeOn(AndroidSchedulers.mainThread())
          .subscribe(new BIIBaseSubscriber<InvestItemVo>() {
            @Override public void onCompleted() {

            }

            @Override
            public void commonHandleException(BiiResultErrorException biiResultErrorException) {
              vo.setRefrensh(false);
              investView.updateItemProduct(vo, pos);
            }

            @Override public void handleException(BiiResultErrorException biiResultErrorException) {

            }

            @Override public void onNext(InvestItemVo vo) {
              vo.setRefrensh(false);
              investView.updateItemProduct(vo, pos);
            }
          });
    }
  }

  /**
   * 加载个人资产
   */
  public void loadMinAsset() {
    LogUtils.d("dding", "---加载个人资产");
    assetService.psnAssetBalanceQuery(new PsnAssetBalanceQueryParams())
        .compose(lifecycleManager.<PsnAssetBalanceQueryResult>bindToLifecycle())
        .subscribeOn(Schedulers.io())
        .map(new Func1<PsnAssetBalanceQueryResult, AssetVo>() {
          @Override public AssetVo call(PsnAssetBalanceQueryResult result) {
            return buildAssetVo(result);
          }
        })
        .map(new Func1<AssetVo, List<AssetDetailVo>>() {
          @Override public List<AssetDetailVo> call(AssetVo assetVo) {
            userAssetVo = assetVo;
            //转换类型 ，计算总资产
            List<AssetDetailVo> detailVos = new InvestTools().buildDetailVo(assetVo);
            return detailVos;
          }
        })
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Subscriber<List<AssetDetailVo>>() {
          @Override public void onCompleted() {

          }

          @Override public void onError(Throwable throwable) {
            LogUtils.d("dding", "---加载个人资产 失败:" + throwable.getMessage());
            investView.onAssetLoadFail();
          }

          @Override public void onNext(List<AssetDetailVo> assetVo) {
            LogUtils.d("dding", "---加载个人资产 成功");
            assetDetailVos = assetVo;
            if (assetDetailVos != null && assetDetailVos.size() > 0) {
              investView.onAssetLoadSuccess();
            } else {
              // TODO 没有资产
            }
          }
        });
  }

  private List<InvestItemVo> listDatas = null;

  /**
   * 加载推荐的产品
   */
  private void loadProductList2() {
    LogUtils.d("dding", "加载优选:"+isLogin()+"  "+hasProductsLoad);
    //重置
    conversation = null;
    ibkNumber = null;

    Observable<List<InvestItemVo>> listOBS = null;

    //已经加载过推荐产品的直接进入下一步,否则加载推荐
    if (/*!hasProductsLoad || */listDatas == null) {
      investView.setOptimalListViewLoadingState(true,true);
      LogUtils.d("dding","优选投资未初始化,加载优选");
      listOBS = getOBSProductList().compose(lifecycleManager.<List<ProductBean>>bindToLifecycle())
          .subscribeOn(Schedulers.io())
          .flatMap(new Func1<List<ProductBean>, Observable<List<InvestItemVo>>>() {
            @Override public Observable<List<InvestItemVo>> call(List<ProductBean> productBeen) {
              InvestDataCenter.getInstance().updateInvestProducts(productBeen);
              return Observable.just(buildInvestItemsFromProducts(productBeen));
            }
          })
          .observeOn(AndroidSchedulers.mainThread())//切主线程初步更新数据
          .map(new Func1<List<InvestItemVo>, List<InvestItemVo>>() {
            @Override public List<InvestItemVo> call(List<InvestItemVo> investItemVos) {
              LogUtils.d("dding","推荐或则缓存数据 加载完毕 开始显示,页面进入局部loading状态");
              //显示
              listDatas = investItemVos;
              updateInvestItemRefrenshStatues(investItemVos, true);
              updateInvestItemSuccess(investItemVos,false);

              investView.updateProductData(investItemVos);

              return investItemVos;
            }
          })
          .subscribeOn(Schedulers.io());
    } else {
      LogUtils.d("dding","优选投资已初始化");
      listOBS = Observable.just(listDatas)
          .compose(lifecycleManager.<List<InvestItemVo>>bindToLifecycle())
          .subscribeOn(Schedulers.io())
          .flatMap(new Func1<List<InvestItemVo>, Observable<InvestItemVo>>() {
            @Override public Observable<InvestItemVo> call(List<InvestItemVo> investItemVos) {
              return Observable.from(investItemVos);
            }
          }).filter(new Func1<InvestItemVo, Boolean>() {
            @Override public Boolean call(InvestItemVo investItemVo) {
              //加载失败项 重新加载
              if(!investItemVo.isSuccess()){
                investItemVo.setRefrensh(true);
                return true;
              }
              //理财项 重新加载
              if(InvestTools.isFinancing(investItemVo)){
                investItemVo.setRefrensh(true);
                investItemVo.setSuccess(false);
                return true;
              }
              return false;
            }
          }).collect(new Func0<List<InvestItemVo>>() {
            @Override public List<InvestItemVo> call() {
              return new ArrayList<>();
            }
          }, new Action2<List<InvestItemVo>, InvestItemVo>() {
            @Override public void call(List<InvestItemVo> list, InvestItemVo investItemVo) {
              investItemVo.setRefrensh(true);
              investItemVo.setSuccess(false);
              list.add(investItemVo);
            }
          }).observeOn(AndroidSchedulers.mainThread())
          .map(new Func1<List<InvestItemVo>, List<InvestItemVo>>() {
            @Override public List<InvestItemVo> call(List<InvestItemVo> investItemVos) {
              investView.updateProductData(listDatas);
              return investItemVos;
            }
          }).observeOn(Schedulers.io());
    }

    LogUtils.d("dding","优选投资初始化完成,开始更新item数据");

    listOBS.flatMap(new Func1<List<InvestItemVo>, Observable<InvestItemVo>>() {
      @Override public Observable<InvestItemVo> call(List<InvestItemVo> investItemVos) {
        return Observable.from(investItemVos);
      }
    }).compose(getconcurrentTransformer(new Func1<InvestItemVo, Observable<InvestItemVo>>() {
      @Override public Observable<InvestItemVo> call(InvestItemVo investItemVo) {
        //分类型请求理财和基金接口
        if (InvestTools.isFinancing(investItemVo)) {
          //理财
          return getOBSFinancing(investItemVo);
        } else if (InvestTools.isFund(investItemVo)) {
          return getOBSFund(investItemVo);
        }
        return Observable.just(investItemVo);
      }
    })).observeOn(AndroidSchedulers.mainThread())//切主线程更新
        .subscribe(new Subscriber<InvestItemVo>() {
          @Override public void onCompleted() {
            investView.updateProductData(listDatas);
            LogUtils.d("dding", "-------->优选推荐请求结束：");
          }

          @Override public void onError(Throwable throwable) {
            LogUtils.d("dding","异常:"+throwable.getMessage());
            investView.setOptimalListViewLoadingState(true,false);
            investView.updateProductData(null);
          }

          @Override public void onNext(InvestItemVo investItemVo) {
            LogUtils.d("dding",
                "----->>>:" + (investItemVo == null ? "请求失败:" : investItemVo.getProductName()));
          }
        });
  }

  private String conversation;

  private void getConversation() {
    LogUtils.d("dding", "----基金会话id:" + conversation + "  " + Thread.currentThread());
    if (conversation != null) {
      return;
    }
    synchronized (this) {
      if (conversation != null) return;

      new GlobalService().psnBmpsCreatConversation(new PSNBmpsCreatConversationParams())
          .subscribe(new Subscriber<String>() {
            @Override public void onCompleted() {

            }
            @Override public void onError(Throwable throwable) {

            }
            @Override public void onNext(String s) {
              conversation = s;
              LogUtils.d("dding", "----基金会话id请求成功:" + conversation + "  " + Thread.currentThread());
            }
          });
    }
  }

  public Observable<CRgetPosterListResult> getOBSAd() {
    CRgetPosterListParams params = new CRgetPosterListParams();
    params.setReleasePosition("2");//0:首页、1:生活、2:投资、
    params.setRegion("00");//首页和投资默认送00，生活送真实地区编码，参见地区编码
    return crAdService.cRgetPosterList(params);
  }

  public Observable<List<ProductBean>> getOBSProductList() {

    CRgetProductListParams params = new CRgetProductListParams();
    return productService.cRgetProductList(params)
        .flatMap(new Func1<CRgetProductListResult, Observable<List<ProductBean>>>() {
          @Override
          public Observable<List<ProductBean>> call(CRgetProductListResult productListResult) {
            hasProductsLoad = true;
            LogUtils.d("dding","开始加载优选投资");
            return Observable.just(productListResult.getArrayList());
          }
        }).map(new Func1<List<ProductBean>, List<ProductBean>>() {
          @Override public List<ProductBean> call(List<ProductBean> productBeen) {
            //productBeen.clear();

            //TODO
            if(productBeen == null || productBeen.size()==0){
              hasProductsLoad = false;
              return defaultProductList();
            }
            return productBeen;
          }
        })
        .onErrorReturn(new Func1<Throwable, List<ProductBean>>() {
          @Override public List<ProductBean> call(Throwable throwable) {
            LogUtils.d("dding","加载优选投资失败,开始加载内置数据,优选投资数据未初始");
            hasProductsLoad = false;
            return defaultProductList();
          }
        }).delay(200,TimeUnit.MILLISECONDS);
  }

  /**
   * 理财信息（全国接口）
   */
  /*public Observable<InvestItemVo> getOBSFinancing(final InvestItemVo investItemVo) {
    getConversation();
    if(conversation == null)return Observable.just(investItemVo);

    final PsnXadProductQueryOutlayParams params = new PsnXadProductQueryOutlayParams();
    params.set_refresh("true");
    params.setProductType("0");
    params.setProductSta("1");//销售状态
    params.setSortFlag("1");
    params.setSortSite("0");
    params.setPageSize("10");
    params.setCurrentIndex("1");
    params.setProductKind("2");//1:结构性  2:类基金
    //TODO 设置产品code u
    //params.setProductCode(investItemVo.getProductCode());

 *//*   params.setProductRiskType("0");
    params.setProdTimeLimit("0");
    params.setProductCurCode("001");
    params.setProductKind("0");
    params.setIssueType("0");
    params.setProRisk("0");
    params.setIsLockPeriod("1");
    params.setDayTerm("0");*//*

    params.setConversationId(conversation);

    return financingService.psnXadProductQueryOutlay(params)
        .compose(lifecycleManager.<PsnXadProductQueryOutlayResult>bindToLifecycle())
        .map(new Func1<PsnXadProductQueryOutlayResult, InvestItemVo>() {
          @Override public InvestItemVo call(PsnXadProductQueryOutlayResult result) {
            if (result == null || result.getList() == null || result.getList().size() == 0) {
              return investItemVo;
            }
            //fillInvestItemByFinancing(result.getList().get(0), investItemVo);
            return investItemVo;
          }
        });
  }*/
  private Observable<InvestItemVo> getOBSFinancing(final InvestItemVo investItemVo) {
    return isLogin() ? getOBSFinancingDetailLogin(investItemVo) : getOBSFinancingDetail(investItemVo);
  }


  private Observable<InvestItemVo> getOBSFund(final InvestItemVo investItemVo) {
    return isLogin()?getOBSFundLogin(investItemVo):getOBSFundNoLogin(investItemVo);
  }


  /**
   * 请求理财详情OBS  生命周期绑定、数据转换为vo 在方法内完成，线程设定，结果接受由调用者进行
   */
  private Observable<InvestItemVo> getOBSFinancingDetail(final InvestItemVo investItemVo) {
    LogUtils.d("dding","加载登录前详情页面,conversation:"+conversation);
  /*  getConversation();
    if(conversation == null){
      //未登录接口调用失败, 若已经请求过登录接口则可以切换到未登录状态进行显示
      LogUtils.d("dding","加载登录前详情页面,conversation 获取失败"+conversation);
      investItemVo.setLoadLoginApi(false);
      investItemVo.setRefrensh(false);
      return Observable.just(investItemVo);
    }*/

    PsnXpadProductDetailQueryOutlayParams params = new PsnXpadProductDetailQueryOutlayParams();
    // 设置产品code
    params.setProductCode(investItemVo.getProductCode());
    //产品性质
    params.setProductKind(investItemVo.getProductNature());
    //params.setConversationId(conversation);

    LogUtils.d("dding","加载登录前详情页面,bii ,"+investItemVo.getProductName());

    return financingService.psnXpadProductDetailQueryOutlay(params)
        .compose(lifecycleManager.<PsnXpadProductDetailQueryOutlayResult>bindToLifecycle())
        .map(new Func1<PsnXpadProductDetailQueryOutlayResult, InvestItemVo>() {
          @Override public InvestItemVo call(PsnXpadProductDetailQueryOutlayResult result) {
            LogUtils.d("dding","加载登录前详情页面 成功 ,"+result.getProdName());
            if (result != null) {
              fillInvestItemByFinancingDetail(result, investItemVo);
              if(!isLogin()){
                investItemVo.setSuccess(true);
                investItemVo.setRefrensh(false);
              }else{
                investItemVo.setSuccess(true);
                investItemVo.setRefrensh(false);
              }
              investItemVo.setLoadNoLoginApi(true);
              investItemVo.setLoadLoginApi(false);
            }else{
              investItemVo.setSuccess(false);
              investItemVo.setRefrensh(false);
            }
            return investItemVo;
          }
        }).onErrorReturn(new Func1<Throwable, InvestItemVo>() {
          @Override public InvestItemVo call(Throwable throwable) {
            LogUtils.d("dding","加载登录前详情页面 发生异常 ,"+investItemVo.getProductName()+"  "+throwable.getMessage());
            investItemVo.setSuccess(false);
            investItemVo.setRefrensh(false);
            return investItemVo;
          }
        });
  }

  private String ibkNumber = null;

  private void getIbkNumber(){
    LogUtils.d("dding","获取登录后IBK:"+ibkNumber+"  "+Thread.currentThread());
    if(!StringUtils.isEmptyOrNull(ibkNumber)){
      return;
    }

    synchronized (this){
      if(!StringUtils.isEmptyOrNull(ibkNumber))return;
      PsnXpadAccountQueryParams paramsAccount = new PsnXpadAccountQueryParams();
      paramsAccount.setXpadAccountSatus("1");
      paramsAccount.setQueryType("0");
      LogUtils.d("dding","获取账户 bii 开始");
      wealthManagementService.psnXpadAccountQuery(paramsAccount).compose(lifecycleManager.<PsnXpadAccountQueryResult>bindToLifecycle())
          .map(new Func1<PsnXpadAccountQueryResult, String>() {
            @Override public String call(PsnXpadAccountQueryResult result) {
              hasAccountLoadSuccess = true;
              if (result == null || result.getList() == null || result.getList().size() == 0) {
                LogUtils.d("dding","获取账户 bii 结束  没有");
                //没有关联账户
                return null;
              }
              ibkNumber = result.getList().get(0).getIbkNumber();
              return result.getList().get(0).getIbkNumber();
            }
          })
          .onErrorReturn(new Func1<Throwable, String>() {
            @Override public String call(Throwable throwable) {
              commonErrorHandler(null,throwable);
              return null;
            }
          })
          .subscribe(new Action1<String>() {
            @Override public void call(String s) {
              LogUtils.d("dding","获取账户 bii 成功,ibk:"+s);
              ibkNumber = s;
            }
          });
    }
  }

  /**
   * 理财 - 登录后详情
   */
  private Observable<InvestItemVo> getOBSFinancingDetailLogin(final InvestItemVo investItemVo) {
    LogUtils.d("dding","加载理财,登陆后详情:"+investItemVo.getProductName()+" ibkNumber:"+ibkNumber);

    getIbkNumber();

    LogUtils.d("dding","登录后 ibk:>"+investItemVo.getProductName()+"   "+ibkNumber);

    //iBK 获取失败无法进入下一步 ,终止item loading
    if(StringUtils.isEmptyOrNull(ibkNumber)){
      return getOBSFinancingDetail(investItemVo);
    /*  if(hasAccountLoadSuccess){//账户请求成功但是无账户,显示登录前数据,不显示刷新按钮
        return getOBSFinancingDetail(investItemVo);
      }
      investItemVo.setSuccess(false);
      investItemVo.setRefrensh(false);
      return Observable.just(investItemVo);*/
    }
    return  Observable.just(ibkNumber).flatMap(new Func1<String, Observable<PsnXpadProductDetailQueryResult>>() {
      @Override public Observable<PsnXpadProductDetailQueryResult> call(String s) {

        LogUtils.d("dding","加载登录后理财BII,"+investItemVo.getProductName());

        PsnXpadProductDetailQueryParams params = new PsnXpadProductDetailQueryParams();
        params.setIbknum(s);
        params.setProductCode(investItemVo.getProductCode());
        params.setProductKind(investItemVo.getProductNature());
        //切换数据来源

        return wealthManagementService.psnXpadProductDetailQuery(params);
      }
    })
        .map(new Func1<PsnXpadProductDetailQueryResult, InvestItemVo>() {
          @Override public InvestItemVo call(PsnXpadProductDetailQueryResult result) {
            LogUtils.d("dding","加载登录后理财BII 成功,"+result.getProdName());
            if (result != null) {
              fillInvestItemByFinancingDetailLogin(result, investItemVo);
              investItemVo.setSuccess(true);
              investItemVo.setRefrensh(false);
            } else {
              investItemVo.setSuccess(false);
              investItemVo.setRefrensh(false);
            }
            investItemVo.setLoadLoginApi(true);
            investItemVo.setLoadNoLoginApi(false);

            return investItemVo;
          }
        }).doOnError(new Action1<Throwable>() {
          @Override public void call(Throwable throwable) {
            commonErrorHandler(investItemVo,throwable);
          }
        })
        .onErrorResumeNext(getOBSFinancingDetail(investItemVo));
     /*   .onErrorReturn(new Func1<Throwable, InvestItemVo>() {
          @Override public InvestItemVo call(Throwable throwable) {
            //数据加载失败
            investItemVo.setSuccess(investItemVo.isLoadNoLoginApi());
            investItemVo.setRefrensh(false);
            commonErrorHandler(investItemVo,throwable);
            return investItemVo;
          }
        });*/
  }

  /**
   * 请求基金信息
   */
  private Observable<InvestItemVo> getOBSFundNoLogin(final InvestItemVo investItemVo) {
    LogUtils.d("dding","");
    //获取基金信息
    PsnFundDetailQueryOutlayParams params = new PsnFundDetailQueryOutlayParams();
    params.setFundCode(investItemVo.getProductCode());
    return fundService.psnFundDetailQueryOutlay(params)
        .compose(lifecycleManager.<PsnFundDetailQueryOutlayResult>bindToLifecycle())
        .map(new Func1<PsnFundDetailQueryOutlayResult, InvestItemVo>() {
          @Override public InvestItemVo call(PsnFundDetailQueryOutlayResult result) {
            //填充数据
            fillInvestItemByFundResult(result, investItemVo);
            //请求成功，设置标记为true
            investItemVo.setSuccess(true);
            investItemVo.setRefrensh(false);
            return investItemVo;
          }
        }).onErrorReturn(new Func1<Throwable, InvestItemVo>() {
          @Override public InvestItemVo call(Throwable throwable) {
            //请求失败 设置标记为false
            investItemVo.setSuccess(false);
            investItemVo.setRefrensh(false);
            return investItemVo;
          }
        });
  }


  /**
   * 请求基金信息
   */
  private Observable<InvestItemVo> getOBSFundLogin(final InvestItemVo investItemVo) {
    LogUtils.d("dding","请求登录后基金性情:"+investItemVo.getProductName());
    //获取基金信息
    PsnGetFundDetailParams params = new PsnGetFundDetailParams();
    params.setFundCode(investItemVo.getProductCode());

    return fundService.psnGetFundDetail(params)
        .compose(lifecycleManager.<PsnGetFundDetailResult>bindToLifecycle())
        .map(new Func1<PsnGetFundDetailResult, InvestItemVo>() {
          @Override public InvestItemVo call(PsnGetFundDetailResult result) {
            LogUtils.d("dding","登录后基金详情请求成功:"+result.getFundName());
            //填充数据
            fillInvestItemByFundLoginResult(result, investItemVo);
            //请求成功，设置标记为true
            investItemVo.setSuccess(true);
            investItemVo.setLoadLoginApi(true);
            investItemVo.setRefrensh(false);

            return investItemVo;
          }
        }).onErrorReturn(new Func1<Throwable, InvestItemVo>() {
          @Override public InvestItemVo call(Throwable throwable) {
            LogUtils.d("dding","登陆后基金详情请求失败:"+investItemVo.getProductName()+" "+throwable.getMessage());
            //请求失败 设置标记为false
            investItemVo.setSuccess(false);
            investItemVo.setRefrensh(false);
            commonErrorHandler(investItemVo,throwable);
            return investItemVo;
          }
        });
  }

  @Override public void subscribe() {

  }

  @Override public void unsubscribe() {
    lifecycleManager.onDestroy();
    mainHandler.removeCallbacks(null);
  }


  //登录后接口,判断会话超时
  private void commonErrorHandler(final InvestItemVo itemVo,final Throwable throwable){
    //TODO 判断是否为必须退出的错误类型
   final BIIBaseSubscriber subscriber  = new BIIBaseSubscriber<Integer>(){
      @Override public void onCompleted() {

      }

      @Override public void onNext(Integer integer) {

      }

      @Override public void doOnTimeOut() {
        LogUtils.d("dding","会话超时"+(itemVo==null?"":itemVo.getProductName()));
        lifecycleManager.onDestroy();
      }

      @Override public void commonHandleException(BiiResultErrorException biiResultErrorException) {
      }

      @Override public void handleException(BiiResultErrorException biiResultErrorException) {

      }
    };
    mainHandler.post(new Runnable() {
      @Override public void run() {
        subscriber.onError(throwable);
      }
    });


  }

  //-------------  数据拼接 转换 区-------------------------------------

  private List<AdvertisementModel> buildAdListData(
      List<CRgetPosterListResult.PosterBean> posterBeanList) {
    List<AdvertisementModel> adBeanList = new ArrayList<>();

    if (posterBeanList == null || posterBeanList.size() == 0) {
      return adBeanList;
    }

    for (CRgetPosterListResult.PosterBean bean : posterBeanList) {
      if (bean == null) continue;
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

  private AssetVo buildAssetVo(PsnAssetBalanceQueryResult result) {
    if (result == null) return new AssetVo();
    AssetVo assetVo = new AssetVo();

    assetVo.setFundAmt(result.getFundAmt());
    assetVo.setTpccAmt(result.getTpccAmt());
    assetVo.setXpadAmt(result.getXpadAmt());
    assetVo.setBondAmt(result.getBondAmt());
    assetVo.setIbasAmt(result.getIbasAmt());

    assetVo.setActGoldAmt(result.getActGoldAmt());
    assetVo.setMetalAmt(result.getMetalAmt());

    assetVo.setForexAmt(result.getForexAmt());
    assetVo.setJxjAmt(result.getJxjAmt());
    assetVo.setAutd(result.getAutd());

    return assetVo;
  }

  private void updateInvestItemRefrenshStatues(InvestItemVo investItemVo, boolean isRefrensh) {
    if (investItemVo == null) return;
    investItemVo.setRefrensh(isRefrensh);
  }

  private void updateInvestItemRefrenshStatues(List<InvestItemVo> list, boolean isRefrensh) {
    if (list == null || list.size() == 0) return;
    for (InvestItemVo item : list) {
      item.setRefrensh(isRefrensh);
    }
  }

  private void updateInvestItemSuccess(List<InvestItemVo> list,boolean isSuccess){
    if (list == null || list.size() == 0) return;
    for (InvestItemVo item : list) {
      item.setSuccess(isSuccess);
    }
  }

  /**
   * 初始根据推荐接口返回数据填充vo
   * @param products
   * @return
   */
  private List<InvestItemVo> buildInvestItemsFromProducts(List<ProductBean> products) {
    LogUtils.d("dding","根据具优选推荐数据构造vo");

    List<InvestItemVo> investItemVos = new ArrayList<>();

    if (products == null || products.size() == 0) {
      return investItemVos;
    }


    for (ProductBean productBean : products) {
      InvestItemVo investItemVo = new InvestItemVo();
      investItemVo.setProductType(productBean.getType());
      investItemVo.setProductName(productBean.getName());
      investItemVo.setProductCode(productBean.getProductCode());
      investItemVo.setProductNature(productBean.getProductNature());

      investItemVo.setPrice("--");
      //investItemVo.setLowLimit(productBean.getMinMoney());
      investItemVo.setAvailamt("--");
      investItemVo.setDayIncomeRatio("--");
      investItemVo.setChargeRate("--");
      investItemVo.setSevenDayYield("--");
      investItemVo.setRateValue(InvestTools.getRateShow(productBean.getRateValue(),""));
      investItemVo.setPriceDate("");

      //investItemVo.setMinMoney(productBean.getMinMoney());

      //TODO
      //investItemVo.setLowLimit(InvestTools.getFuncingLowLimit(productBean.getMinMoney()));
      investItemVo.setLowLimit("--");


      String productLimit = productBean.getProductLimit();
      if(StringUtils.isEmptyOrNull(productLimit)){
        investItemVo.setTimeLimit("--");
      }else{
        investItemVo.setTimeLimit(productLimit+"天");
      }
      investItemVos.add(investItemVo);
    }

    LogUtils.d("dding","根据具优选推荐数据构造vo 结束");
    return investItemVos;
  }

  /**
   * 使用基金接口数据填充vo
   */
  private void fillInvestItemByFundResult(PsnFundDetailQueryOutlayResult result,
      InvestItemVo investItemVo) {
    investItemVo.setProductName(result.getFundName());
    investItemVo.setProductCode(result.getFundCode());

    //基金类型
    investItemVo.setFntype(result.getFntype());

    //七日年化收益率	String	若返回值为0.05，则前端显示为5%
    try {
      BigDecimal showValue = new BigDecimal(result.getSevenDayYield());
      String value = showValue.multiply(new BigDecimal("100")).setScale(2, BigDecimal.ROUND_HALF_UP) + "%";
      investItemVo.setSevenDayYield(value);
    }catch (Exception e){
      investItemVo.setSevenDayYield("--");
    }

    //万分收益
    investItemVo.setFundIncomeUnit(InvestTools.getFundFormatPrice(result.getFundIncomeUnit()));

    //日净值增长率(%)	String	若返回值为0.05，则前端显示为0.05% (bii 文档有错误,应该显示5%)
    //  FundItemView
    try {
      String value = result.getDayIncomeRatio().setScale(2, BigDecimal.ROUND_HALF_UP).toString() + "%";
      investItemVo.setDayIncomeRatio(value);
    }catch (Exception e){
      investItemVo.setDayIncomeRatio("--");
    }

    //净值
    investItemVo.setPrice(InvestTools.getFundFormatPrice(result.getNetPrice()));

    if ("y".equalsIgnoreCase(result.getIsBuy())) {
      //可以认购
      investItemVo.setLowLimit(InvestUtils.getLimitMoney(result.getOrderLowLimit()));
      //费率
      if(TextUtils.isEmpty(result.getChargeRate())){
        investItemVo.setChargeRate("--");
      }else{
        investItemVo.setChargeRate("认购"+InvestTools.addPercent(result.getChargeRate()));
      }
    } else if ("y".equalsIgnoreCase(result.getIsInvt())) {
      //可以申购
      investItemVo.setLowLimit(InvestUtils.getLimitMoney(result.getApplyLowLimit()));
      //费率
      if(TextUtils.isEmpty(result.getChargeRate())){
        investItemVo.setChargeRate("--");
      }else{
        investItemVo.setChargeRate("申购"+InvestTools.addPercent(result.getChargeRate()));
      }

    } else {
      investItemVo.setLowLimit("");
    }

    //净值截止日期
    investItemVo.setPriceDate(result.getEndDate());
  }

  /**
   * 使用基金接口数据填充vo
   */
  private void fillInvestItemByFundLoginResult(PsnGetFundDetailResult result,
      InvestItemVo investItemVo) {
    investItemVo.setProductName(result.getFundName());
    investItemVo.setProductCode(result.getFundCode());

    //基金类型
    investItemVo.setFntype(result.getFntype());

    //七日年化收益率	String	若返回值为0.05，则前端显示为5%
    //investItemVo.setSevenDayYield(InvestTools.getPercentValue(result.getSevenDayYield()));
    try {
      BigDecimal showValue = new BigDecimal(result.getSevenDayYield());
      String value = showValue.multiply(new BigDecimal("100")).setScale(2, BigDecimal.ROUND_HALF_UP) + "%";
      investItemVo.setSevenDayYield(value);
    }catch (Exception e){
      investItemVo.setSevenDayYield("--");
    }


    //万分收益
    investItemVo.setFundIncomeUnit(InvestTools.getFundFormatPrice(result.getFundIncomeUnit()));


    //日净值增长率(%)	String	若返回值为0.05，则前端显示为0.05%
    //investItemVo.setDayIncomeRatio(InvestTools.getDayIncomeRatio(result.getDayIncomeRatio()));
    try {
//      String value = result.getDayIncomeRatio().setScale(2, BigDecimal.ROUND_HALF_UP).toString() + "%";
//      investItemVo.setDayIncomeRatio(value);
    }catch (Exception e){
      investItemVo.setDayIncomeRatio("--");
    }

    //净值
    //investItemVo.setPrice(result.getNetPrice().toString());
    investItemVo.setPrice(InvestTools.getFundFormatPrice(result.getNetPrice()));

    //起购金额
    if ("y".equalsIgnoreCase(result.getIsBuy())) {
      //可以认购
     // investItemVo.setLowLimit(InvestUtils.getLimitMoney(result.getOrderLowLimit()));

      //费率
      if(TextUtils.isEmpty(result.getChargeRate())){
        investItemVo.setChargeRate("--");
      }else{
        investItemVo.setChargeRate("认购"+InvestTools.addPercent(result.getChargeRate()));
      }

    } else if ("y".equalsIgnoreCase(result.getIsInvt())) {
      //可以申购
     // investItemVo.setLowLimit(InvestUtils.getLimitMoney(result.getApplyLowLimit()));

      //费率
      if(TextUtils.isEmpty(result.getChargeRate())){
        investItemVo.setChargeRate("--");
      }else{
        investItemVo.setChargeRate("申购"+InvestTools.addPercent(result.getChargeRate()));
      }

    } else {
      //TODO
      investItemVo.setLowLimit("");
    }

    //净值截止日期
    investItemVo.setPriceDate(result.getEndDate());
  }

  /**
   * 更具登录前 详情查询接口数据填充vo
   */
  private void fillInvestItemByFinancingDetail(PsnXpadProductDetailQueryOutlayResult bean,
      InvestItemVo investItemVo) {
    //理财  - 风险等级  保本与否  预计年化收益率 单位净值 产品期限

    investItemVo.setProductName(bean.getProdName());
    investItemVo.setProductCode(bean.getProdCode());
    //0:结构性理财产品 1:类基金理财产品
    investItemVo.setProductNature(bean.getProductKind());

    //年化收益率
    investItemVo.setRateValue(InvestTools.getRateShow(bean.getYearlyRR(),bean.getRateDetail()));

    //净值
    investItemVo.setPrice(InvestTools.getFormatFuncingPrice(bean.getPrice()));

    //净值日期
    investItemVo.setPriceDate(bean.getPriceDate());

    //产品期限
    String financingTimeLimit = getFinancingTimeLimit(bean.getProductKind(),bean.getIsLockPeriod(),bean.getProductTermType(),bean.getProdTimeLimit());
    investItemVo.setTimeLimit(financingTimeLimit);

    //起购金额
    //InvestUtils.getLimitMoney(bean.getLowLimitAmount());
    investItemVo.setLowLimit(InvestTools.getFuncingLowLimit(bean.getSubAmount(),bean.getCurCode()));

    //剩余额度
    investItemVo.setAvailamt(InvestTools.getAvailamt(bean.getAvailamt()));


  }

  /**
   *
   * @return
   */
  private String getFinancingTimeLimit(String productKind,String isLockPeriod,String termType,String timeLimit) {

    /**
     *该字段以天数表示的产品到期日期限，等于产品到日期减去产品起息日。
     *当为业绩基准产品时该字段有效，且如果是业绩基准转低收益产品，该天数表示锁定期期限，因此建议展示为“最低持有X天”；
     *当为非业绩基准产品，且“产品期限特性”不是“无限开放式”时，该字段有效；
     *其余情况该字段无效，请统一展示无固定期限。
     */
   /* if (bean == null) {
      return "";
    }*/
    String showValue = InvestTools.getShowValue(timeLimit);

    //区分产品性质 理财型
    if ("0".equals(productKind)) {
      String code = isLockPeriod;

      if ("1".equals(code)) {
        //业绩基准转低收益  , 标示锁定期限
        return "最低持有" + showValue + "天";
      } else if ("2".equals(code) || "3".equals(code) || ("0".equals(code) && !"3".equals(
          termType))) {
        // 字段有效
        return showValue + "天";
      } else {
        return "无固定期限";
      }
    } else {
      return showValue + "天";
    }
  }

  /**
   * 根具登录后 详情查询接口数据填充vo
   */
  private void fillInvestItemByFinancingDetailLogin(PsnXpadProductDetailQueryResult bean,
      InvestItemVo investItemVo) {
    //理财  - 风险等级  保本与否  预计年化收益率 单位净值 产品期限

    investItemVo.setProductName(bean.getProdName());
    investItemVo.setProductCode(bean.getProdCode());
    //0:结构性理财产品 1:类基金理财产品
    investItemVo.setProductNature(bean.getProductKind());

    //年化收益率
    //investItemVo.setRateValue(InvestTools.addPerce
    // nt(bean.getRateDetail()));
    investItemVo.setRateValue(InvestTools.getRateShow(bean.getYearlyRR(),bean.getRateDetail()));

    //净值
    investItemVo.setPrice(InvestTools.getFormatFuncingPrice(bean.getPrice()));

    //净值日期
    investItemVo.setPriceDate(bean.getPriceDate());

    //产品期限
    String financingTimeLimit = getFinancingTimeLimit(bean.getProductKind(),bean.getIsLockPeriod(),bean.getProductTermType(),bean.getProdTimeLimit());
    investItemVo.setTimeLimit(financingTimeLimit);
    investItemVo.setIsLockPeriod(investItemVo.getIsLockPeriod());

    //起购金额
    //investItemVo.setLowLimit(InvestTools.getShowValue(bean.getLowLimitAmount()));
    investItemVo.setLowLimit(InvestTools.getFuncingLowLimit(bean.getSubAmount(),bean.getCurCode()));

    //剩余额度
    investItemVo.setAvailamt(InvestTools.getAvailamt(bean.getAvailamt()+""));
  }

  //-------------  数据拼接 转换 区  end-------------------------------------

  // ------ 并发 ----------------------

  /**
   * 并发请求，结果无序需要排除 ， 发生错误返回null需要处理
   */
  private <T, R> Observable.Transformer<T, R> getconcurrentTransformer(
      final Func1<T, Observable<R>> func1) {
    Observable.Transformer<T, R> transformer = new Observable.Transformer<T, R>() {
      @Override public Observable<R> call(Observable<T> tObservable) {
        return tObservable.flatMap(new Func1<T, Observable<R>>() {
          @Override public Observable<R> call(T t) {
            return Observable.just(t)
                .flatMap(func1)
                .subscribeOn(Schedulers.io())
                .onErrorReturn(new Func1<Throwable, R>() {
                  @Override public R call(Throwable throwable) {
                    return null;
                  }
                });
          }
        });
      }
    };
    return transformer;
  }

  //-------------  测试demo 信息 -------------------------------------

  private List<AdvertisementModel> defaultAdList() {
    List<AdvertisementModel> adList = new ArrayList<>();
    AdvertisementModel model = new AdvertisementModel();
    model.setPosterUrl("");
    model.setImageUrl("");
    model.setPosterType("0");
    model.setPlaceHolderImageRes(R.drawable.invest_ad_default);
    adList.add(model);
    return adList;
  }

  private List<ProductBean> defaultProductList() {

    List<ProductBean> productBeanList = new ArrayList<>();
/*
    ProductBean p1 = demoProduct(1, "中银稳健增长(R)", "830002");
    p1.setProductNature("1");
    productBeanList.add(p1);

    ProductBean p2 = demoProduct(1, "中银债市通理财计划", "830100");
    p2.setProductNature("1");
    productBeanList.add(p2);

    ProductBean p3 = demoProduct(1, "中银新兴市场（R)", "830018");
    p3.setProductNature("1");
    productBeanList.add(p3);

    ProductBean p4 = demoProduct(1, "(社保卡专属)基智通理财计划-流动性增强B16411", "AMZYJZT-LPB16411");
    p4.setProductNature("0");
    productBeanList.add(p4);*/

    // T1数据

/*    //净值型
    productBeanList.add(demoFuncing("代销","LJJ-1010-DX",1,"1"));
    productBeanList.add(demoFuncing("ljj-p606-cl-sg-y002e","ljj-p606-cl-sg-y002",1,"1"));

    //结构性
    productBeanList.add(demoFuncing("日积月累测试","rjyltest-01",1,"0"));
    productBeanList.add(demoFuncing("日积月累名称长度日积月累名称长度日积月累名称长度","rjyl-p606-nametest02",1,"0"));
    productBeanList.add(demoFuncing("p606-zm-rjyl-rmb-001","p606-zm-rjyl-rmb-001",1,"0"));

    productBeanList.add(demoFuncing("组合购买（人民币）BY1013","KYN-BY1013",1,"0"));



    productBeanList.add(demoFuncing("不存在的净值型产品","KYN-BY1013",1,"1"));
    productBeanList.add(demoFuncing("不存在的结构型产品","KYN-BY102313",1,"0"));


    //productBeanList.add(demoFuncing("中银新兴市场（R)","830018",1,"2"));

    productBeanList.add(demoProduct(0, "鹏华美国房地产", "206011"));
    productBeanList.add(demoProduct(0, "诺安货币", "320019"));*/
    productBeanList.add(getFuncingBean("中银日积月累-日计划","AMRJYL01",1,"0"));
    productBeanList.add(getFuncingBean("收益累进","AMSYLJ01",1,"0"));

    //productBeanList.add(getFuncingBean("320019","320019",0,"0"));



    return productBeanList;

  }

  private ProductBean getFuncingBean( String productName, String ProductCode,int type,String kind) {
    ProductBean bean = new ProductBean();
    bean.setType(type + "");
    bean.setName(productName);
    bean.setProductCode(ProductCode);
    bean.setProductNature(kind);
    return bean;
  }

  private ProductBean demoProduct(int type, String productName, String ProductCode) {
    ProductBean bean = new ProductBean();
    bean.setType(type + "");
    bean.setName(productName);
    bean.setProductCode(ProductCode);
    return bean;
  }

  //-------------  测试demo 信息  结束-------------------------------------
}
