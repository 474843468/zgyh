package com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.portfoliopurchase.presenter;

import com.boc.bocsoft.mobile.bii.bus.global.model.PSNCreatConversation.PSNCreatConversationParams;
import com.boc.bocsoft.mobile.bii.bus.global.model.PSNGetTokenId.PSNGetTokenIdParams;
import com.boc.bocsoft.mobile.bii.bus.global.service.GlobalService;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadGuarantyBuyResult.PsnXpadGuarantyBuyResult;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadGuarantyBuyResult.PsnXpadGuarantyBuyResultParam;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadProductListQuery.PsnXpadProductListQueryParams;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadProductListQuery.PsnXpadProductListQueryResult;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadQueryGuarantyProductList.PsnXpadQueryGuarantyProductListParam;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadQueryGuarantyProductList.PsnXpadQueryGuarantyProductListResult;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadQueryRiskMatch.PsnXpadQueryRiskMatchParams;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadQueryRiskMatch.PsnXpadQueryRiskMatchResult;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadRecentAccountUpdate.PsnXpadRecentAccountUpdateParams;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadRecentAccountUpdate.PsnXpadRecentAccountUpdateResult;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.service.WealthManagementService;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.ApplicationConst;
import com.boc.bocsoft.mobile.bocmobile.base.Exception.BIIBaseSubscriber;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.portfoliopurchase.model.PortfolioPurchaseInputModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.portfoliopurchase.model.PortfolioPurchaseModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.portfoliopurchase.ui.PortfolioPurchaseContact;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.portfoliopurchase.ui.PortfolioPurchaseFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.purchase.ModelUtil;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.wealthproduct.WealthConst;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.wealthproduct.model.WealthListBean;
import com.boc.bocsoft.mobile.framework.rx.SchedulersCompat;
import com.boc.bocsoft.mobile.framework.ui.RxPresenter;
import java.util.List;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.exceptions.CompositeException;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by wangtong on 2016/9/13.
 */
public class PortfolioPurchasePersenter extends RxPresenter
        implements PortfolioPurchaseContact.Presenter {

    private WealthManagementService service;
    private PortfolioPurchaseModel uiModel;
    private PortfolioPurchaseInputModel inputModel;
    private BussFragment bussFragment;
    private GlobalService globalService;
    private String mConversitionId;

    private PortfolioPurchaseContact.MainView mainView;
    public PortfolioPurchaseContact.BuyView buyView;
    public PortfolioPurchaseContact.ConfirmView confirmView;

    public PortfolioPurchasePersenter(PortfolioPurchaseContact.MainView view) {
        mainView = view;
        service = new WealthManagementService();
        globalService = new GlobalService();
        bussFragment = (BussFragment) view;
        uiModel = bussFragment.findFragment(PortfolioPurchaseFragment.class).model;
        inputModel = bussFragment.findFragment(PortfolioPurchaseFragment.class).inputModel;
    }

    @Override
    public void psnXpadQueryGuarantyProductList() {
        bussFragment.showLoadingDialog();
        PsnXpadQueryGuarantyProductListParam param = new PsnXpadQueryGuarantyProductListParam();
        param.setProductCode(uiModel.getProdCode());
        param.setAccountId(uiModel.getAccountId());
        service.psnXpadQueryGuarantyProductList(param)
               .compose(this.<PsnXpadQueryGuarantyProductListResult>bindToLifecycle())
               .subscribeOn(Schedulers.io())
               .observeOn(AndroidSchedulers.mainThread())
               .subscribe(new BIIBaseSubscriber<PsnXpadQueryGuarantyProductListResult>() {
                   @Override
                   public void handleException(BiiResultErrorException biiResultErrorException) {
                   }

                   @Override
                   public void onCompleted() {

                   }

                   @Override
                   public void onNext(PsnXpadQueryGuarantyProductListResult result) {
                       uiModel.setProduct(result);
                       bussFragment.closeProgressDialog();
                       mainView.psnXpadQueryGuarantyProductListReturned();
                   }
               });
    }

    @Override
    public void psnXpadQueryRiskMatch() {
        PsnXpadQueryRiskMatchParams params = new PsnXpadQueryRiskMatchParams();
        bussFragment.showLoadingDialog();
        params.setProductCode(uiModel.getProdCode());
        params.setAccountKey(uiModel.getAccountKey());
        service.psnXpadQueryRiskMatch(params)
               .compose(this.<PsnXpadQueryRiskMatchResult>bindToLifecycle())
               .subscribeOn(Schedulers.io())
               .observeOn(AndroidSchedulers.mainThread())
               .subscribe(new BIIBaseSubscriber<PsnXpadQueryRiskMatchResult>() {

                   @Override
                   public void onCompleted() {

                   }

                   @Override
                   public void onNext(PsnXpadQueryRiskMatchResult result) {
                       uiModel.setRiskMatch(result.getRiskMatch());
                       uiModel.setProductRisk(result.getProRisk());
                       uiModel.setCustomerRisk(result.getCustRisk());
                       uiModel.setRiskMessage(result.getRiskMsg());
                       bussFragment.closeProgressDialog();
                       buyView.psnXpadQueryRiskMatchReturned();
                   }

                   @Override
                   public void handleException(BiiResultErrorException biiResultErrorException) {
                   }
               });
    }

    @Override
    public void psnXpadGuarantyBuyResult() {
        PSNCreatConversationParams conversationPreParams = new PSNCreatConversationParams();
        globalService.psnCreatConversation(conversationPreParams)
                     .flatMap(new Func1<String, Observable<String>>() {
                         @Override
                         public Observable<String> call(String s) {
                             mConversitionId = s;
                             PSNGetTokenIdParams params = new PSNGetTokenIdParams();
                             params.setConversationId(s);
                             return globalService.psnGetTokenId(params);
                         }
                     })
                     .flatMap(new Func1<String, Observable<PsnXpadGuarantyBuyResult>>() {
                         @Override
                         public Observable<PsnXpadGuarantyBuyResult> call(String s) {
                             PsnXpadGuarantyBuyResultParam params =
                                     new PsnXpadGuarantyBuyResultParam();
                             params.setConversationId(mConversitionId);
                             params.setProductCode(uiModel.getProdCode());
                             params.setProductName(uiModel.getProdName());
                             params.setGuarantyBuyAmount(uiModel.getPayAmount() + "");
                             params.setBuyPrice(uiModel.getBuyPrice());
                             params.setAccountId(uiModel.getAccountId());
                             params.setProductCurrency(uiModel.getCurCode());
                             params.setXpadCashRemit(uiModel.getCashRemit());
                             params.setProductNum(uiModel.getPortfolioNum() + "");
                             params.setOverFlag("1");
                             params.setToken(s);
                             params.setProdCode(uiModel.getSelectProductListCode());
                             params.setFreezeUnit(uiModel.getSelectProductListAmount());
                             return service.psnXpadGuarantyBuyResult(params);
                         }
                     })
                     .compose(this.<PsnXpadGuarantyBuyResult>bindToLifecycle())
                     .compose(SchedulersCompat.<PsnXpadGuarantyBuyResult>applyIoSchedulers())
                     .subscribe(new BIIBaseSubscriber<PsnXpadGuarantyBuyResult>() {

                         @Override
                         public void onCompleted() {

                         }

                         @Override
                         public void onNext(PsnXpadGuarantyBuyResult result) {
                             //TODO 继续查询刷新数据和产品列表
                             updateRecentAccountAndQueryLikeList();
                         }

                         @Override
                         public void handleException(
                                 BiiResultErrorException biiResultErrorException) {
                         }
                     });
    }

    private void updateRecentAccountAndQueryLikeList() {
        Observable.mergeDelayError(updateRecentAccount(uiModel), queryProductList(uiModel))
                  .compose(bindToLifecycle())
                  .subscribe(new BIIBaseSubscriber<Object>() {

                      private List<WealthListBean> likeBeans;

                      @Override
                      public void onNext(Object o) {
                          if (o instanceof PsnXpadProductListQueryResult) {
                              likeBeans = ModelUtil.generateWealthListBeans(uiModel.getProdCode(),
                                      uiModel.getProductRisk(), (PsnXpadProductListQueryResult) o);
                          }
                      }

                      @Override
                      public void onError(Throwable e) {
                          if (e instanceof CompositeException) {
                              CompositeException compositeException = (CompositeException) e;
                              e = compositeException.getExceptions().get(0);
                          }
                          super.onError(e);
                          onCompleted();
                      }

                      @Override
                      public void commonHandleException(
                              BiiResultErrorException biiResultErrorException) {
                      }

                      @Override
                      public void handleException(BiiResultErrorException biiResultErrorException) {
                      }

                      @Override
                      public void onCompleted() {
                          confirmView.psnXpadGuarantyBuyResultReturned(likeBeans);
                      }
                  });
    }

    public Observable<PsnXpadRecentAccountUpdateResult> updateRecentAccount(
            final PortfolioPurchaseModel purchaseModel) {
        PSNGetTokenIdParams params = new PSNGetTokenIdParams();
        params.setConversationId(mConversitionId);
        return globalService.psnGetTokenId(params)
                            .flatMap(
                                    new Func1<String, Observable<PsnXpadRecentAccountUpdateResult>>() {
                                        @Override
                                        public Observable<PsnXpadRecentAccountUpdateResult> call(
                                                String token) {
                                            PsnXpadRecentAccountUpdateParams params =
                                                    new PsnXpadRecentAccountUpdateParams();
                                            params.setAccountStatus(
                                                    inputModel.getAccountBean().getXpadAccountSatus());
                                            params.setXpadAccount(
                                                    inputModel.getAccountBean().getXpadAccount());
                                            params.setAccountType(
                                                    inputModel.getAccountBean().getAccountType());
                                            params.setBancID(
                                                    inputModel.getAccountBean().getBancID());
                                            params.setCapitalActNoKey(
                                                    purchaseModel.getAccountKey());
                                            params.setConversationId(mConversitionId);
                                            params.setToken(token);
                                            return service.psnXpadRecentAccountUpdate(params);
                                        }
                                    })
                            .compose(
                                    SchedulersCompat.<PsnXpadRecentAccountUpdateResult>applyIoSchedulers());
    }

    public Observable<PsnXpadProductListQueryResult> queryProductList(
            final PortfolioPurchaseModel model) {
        PsnXpadProductListQueryParams params = new PsnXpadProductListQueryParams();
        params.setConversationId(mConversitionId);
        params.setProductRiskType("0");
        params.setProductKind("0");
        params.setProductCurCode(model.getCurCode());
        params.setPageSize(String.valueOf(ApplicationConst.WEALTH_PAGE_SIZE));
        params.setIsLockPeriod(WealthConst.YES_1);// 是否支持业绩基准产品查询（支持）
        params.setAccountId(model.getAccountId());// 账户ID
        params.setXpadStatus("1");
        params.setIssueType("0");// 产品类型
        params.setProdTimeLimit("0");// 产品期限（月）
        params.setDayTerm("0");// 产品期限（天）
        params.setProRisk("0");// 风险等级
        params.setSortFlag("1");// 排序方式
        params.setSortType("0");// 排序条件
        params.setCurrentIndex("0");
        params.set_refresh("true");
        return service.psnXpadProductListQuery(params)
                      .compose(SchedulersCompat.<PsnXpadProductListQueryResult>applyIoSchedulers());
    }

    @Override
    public void setMainView(PortfolioPurchaseContact.MainView mainView) {
        this.mainView = mainView;
    }

    @Override
    public void setBuyView(PortfolioPurchaseContact.BuyView buyView) {
        this.buyView = buyView;
    }

    @Override
    public void setConfirmView(PortfolioPurchaseContact.ConfirmView confirmView) {
        this.confirmView = confirmView;
    }
}
