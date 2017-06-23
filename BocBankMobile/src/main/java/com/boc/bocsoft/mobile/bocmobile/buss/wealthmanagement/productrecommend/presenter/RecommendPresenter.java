package com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.productrecommend.presenter;

import com.boc.bocsoft.mobile.bii.bus.global.model.PSNCreatConversation.PSNCreatConversationParams;
import com.boc.bocsoft.mobile.bii.bus.global.model.PSNGetTokenId.PSNGetTokenIdParams;
import com.boc.bocsoft.mobile.bii.bus.global.service.GlobalService;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnOcrmProductQuery.PsnOcrmProductQueryParams;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnOcrmProductQuery.PsnOcrmProductQueryResult;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadAccountQuery.PsnXpadAccountQueryParams;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadAccountQuery.PsnXpadAccountQueryResult;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadProductBalanceQuery.PsnXpadProductBalanceQueryParams;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadProductBalanceQuery.PsnXpadProductBalanceQueryResult;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadProductDetailQuery.PsnXpadProductDetailQueryParams;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadProductDetailQuery.PsnXpadProductDetailQueryResult;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadSignInit.PsnXpadSignInitParam;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadSignInit.PsnXpadSignInitResult;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.service.WealthManagementService;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.base.Exception.BIIBaseSubscriber;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.base.model.WealthAccountBean;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.financialposition.model.psnxpadaccountquery.PsnXpadAccountQueryResModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.redeem.model.psnxpadproductdetailquery.PsnXpadProductDetailQueryResModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.productrecommend.model.RecommendModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.productrecommend.model.ProductModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.productrecommend.ui.CommendContract;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.protocolpurchase.model.PsnXpadSignInitBean;
import com.boc.bocsoft.mobile.common.utils.BeanConvertor.BeanConvertor;
import com.boc.bocsoft.mobile.common.utils.PublicUtils;
import com.boc.bocsoft.mobile.framework.rx.SchedulersCompat;
import com.boc.bocsoft.mobile.framework.rx.lifecycle.RxLifecycleManager;
import com.boc.bocsoft.mobile.framework.ui.RxPresenter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * 理财——商品推荐逻辑
 * Created by Wan mengxin on 2016/9/26.
 */
public class RecommendPresenter extends RxPresenter implements CommendContract.Presenter {

    public BussFragment bussFragment;
    private WealthManagementService service;
    private final GlobalService globalService;
    private RxLifecycleManager mRxLifecycleManager;
    private RecommendModel uiModel;
    private CommendContract.CommView commView;
    private PsnXpadProductDetailQueryResModel detailModel;
    private String prodBegin = "";
    private String prodEnd = "";

    private static final String XPADACCOUNTSATUS = "1";
    private static final String QUERYTYPE = "1";

    public RecommendPresenter(CommendContract.CommView commView) {
        this.commView = commView;
        bussFragment = (BussFragment) commView;
        service = new WealthManagementService();
        globalService = new GlobalService();
        mRxLifecycleManager = new RxLifecycleManager();
        uiModel = commView.getModel();
        detailModel = new PsnXpadProductDetailQueryResModel();
    }

    /**
     * 035指令交易产品查询PsnOcrmProductQuery
     * 返回推荐界面数据
     */
    @Override
    public void psnOcrmProductQuery(final RecommendModel model) {
        PSNCreatConversationParams Params = new PSNCreatConversationParams();
        globalService.psnCreatConversation(Params)
                .compose(mRxLifecycleManager.<String>bindToLifecycle())
                .flatMap(new Func1<String, Observable<String>>() {
                    @Override
                    public Observable<String> call(String s) {
                        uiModel.setConversationId(s);

                        PSNGetTokenIdParams mParams = new PSNGetTokenIdParams();
                        mParams.setConversationId(uiModel.getConversationId());
                        return globalService.psnGetTokenId(mParams);
                    }
                })
                .flatMap(new Func1<String, Observable<PsnOcrmProductQueryResult>>() {
                    @Override
                    public Observable<PsnOcrmProductQueryResult> call(String s) {
                        uiModel.setTokenId(s);

                        PsnOcrmProductQueryParams params = new PsnOcrmProductQueryParams();
                        params.setProtpye(model.getProtpye());
                        params.setTradeType(model.getTradeType());
                        params.setPageSize(model.getPageSize());
                        params.setCurrentIndex(model.getCurrentIndex());
                        params.set_refresh(model.get_refresh());
                        params.setConversationId(uiModel.getConversationId());
                        return service.psnOcrmProductQuery(params);
                    }
                })
                .compose(SchedulersCompat.<PsnOcrmProductQueryResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnOcrmProductQueryResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        commView.psnOcrmProductQueryFailed();
                    }

                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onNext(PsnOcrmProductQueryResult result) {
                        if (result != null && !PublicUtils.isEmpty(result.getResultList())) {
                            ArrayList<RecommendModel.OcrmDetail> resultList = new ArrayList<>();
                            uiModel.setRecordNumber(result.getRecordNumber());
                            for (PsnOcrmProductQueryResult.OcrmDetail item : result.getResultList()) {
                                RecommendModel.OcrmDetail detial = new RecommendModel.OcrmDetail();
                                BeanConvertor.toBean(item, detial);
                                resultList.add(detial);
                            }
                            uiModel.setResultList(resultList);
                        }
                        commView.psnOcrmProductQuerySuccess();
                    }
                });
    }

    /**
     * 036查询客户持仓信息  PsnXpadProductBalanceQuery
     * 返回用户持仓详情
     */
    @Override
    public void psnXpadProductBalanceQuery(final String code) {
        PsnXpadProductBalanceQueryParams params = new PsnXpadProductBalanceQueryParams();
        service.psnXpadProductBalanceQuery(params)
                .compose(mRxLifecycleManager.<List<PsnXpadProductBalanceQueryResult>>bindToLifecycle())
                .compose(SchedulersCompat.<List<PsnXpadProductBalanceQueryResult>>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<List<PsnXpadProductBalanceQueryResult>>() {
                               @Override
                               public void handleException(BiiResultErrorException biiResultErrorException) {
                                   commView.psnXpadProductBalanceQueryFailed();
                               }

                               @Override
                               public void onCompleted() {
                               }

                               @Override
                               public void onNext(List<PsnXpadProductBalanceQueryResult> result) {
                                   uiModel.setBalanceQueryResult(true);
                                   List<RecommendModel.balanceMsg> mlist = new ArrayList();
                                   for (PsnXpadProductBalanceQueryResult item : result) {
                                       if (code.equals(item.getProdCode())) {
                                           RecommendModel.balanceMsg msg = new RecommendModel.balanceMsg();
                                           BeanConvertor.toBean(item, msg);
                                           mlist.add(msg);
                                       }
                                   }
                                   uiModel.setBalanceList(mlist);
                                   commView.psnXpadProductBalanceQuerySuccess();
                               }
                           }
                );
    }

    /**
     * 010周期性产品续约协议签约/签约初始化  PsnXpadSignInit
     * 返回封装对象
     */
    @Override
    public void psnXpadSignInit(ProductModel model) {
        PsnXpadSignInitParam params = new PsnXpadSignInitParam();
        params.setAccountId(model.getAccountId());
        params.setCurCode(model.getCurCode());
        params.setProductCode(model.getProductCode());
        params.setProductName(model.getProductName());
        params.setRemainCycleCount(model.getRemainCycleCount());
        service.psnXpadSignInit(params)
                .compose(mRxLifecycleManager.<PsnXpadSignInitResult>bindToLifecycle())
                .compose(SchedulersCompat.<PsnXpadSignInitResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnXpadSignInitResult>() {
                               @Override
                               public void handleException(BiiResultErrorException biiResultErrorException) {
                                   commView.psnXpadSignInitFailed();
                               }

                               @Override
                               public void onCompleted() {
                               }

                               @Override
                               public void onNext(PsnXpadSignInitResult result) {
                                   PsnXpadSignInitBean initBean = new PsnXpadSignInitBean();
                                   BeanConvertor.toBean(result, initBean);
                                   commView.psnXpadSignInitSuccess(initBean);
                               }
                           }
                );
    }

    /**
     * 040产品详情查询  PsnXpadProductDetailQuery 返回指定商品详情（即用户所点击条目的商品）
     * 037查询客户理财账户信息 PsnXpadAccountQuery 返回当前账户名下理财账号信息
     */
    @Override
    public void psnXpadProductDetailQuery(final String code) {
        PsnXpadProductBalanceQueryParams params = new PsnXpadProductBalanceQueryParams();
        service.psnXpadProductBalanceQuery(params)
                .compose(mRxLifecycleManager.<List<PsnXpadProductBalanceQueryResult>>bindToLifecycle())
                .flatMap(new Func1<List<PsnXpadProductBalanceQueryResult>, Observable<PsnXpadProductDetailQueryResult>>() {
                    @Override
                    public Observable<PsnXpadProductDetailQueryResult> call(List<PsnXpadProductBalanceQueryResult> result) {
                        for (PsnXpadProductBalanceQueryResult item : result) {
                            if (code.equals(item.getProdCode())) {
                                prodBegin = item.getProdBegin();
                                prodEnd = item.getProdEnd();
                                break;
                            }
                        }

                        PsnXpadProductDetailQueryParams params = new PsnXpadProductDetailQueryParams();
                        params.setProductCode(code);
                        return service.psnXpadProductDetailQuery(params);
                    }
                })
                .observeOn(Schedulers.io())
                .flatMap(new Func1<PsnXpadProductDetailQueryResult, Observable<PsnXpadAccountQueryResult>>() {
                    @Override
                    public Observable<PsnXpadAccountQueryResult> call(PsnXpadProductDetailQueryResult result) {
                        BeanConvertor.toBean(result, detailModel);
                        detailModel.setProdBegin(prodBegin);
                        detailModel.setProdEnd(prodEnd);
                        PsnXpadAccountQueryParams mParams = new PsnXpadAccountQueryParams();
                        mParams.setXpadAccountSatus(XPADACCOUNTSATUS);
                        mParams.setQueryType(QUERYTYPE);
                        return service.psnXpadAccountQuery(mParams);
                    }
                })
                .compose(SchedulersCompat.<PsnXpadAccountQueryResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnXpadAccountQueryResult>() {
                               @Override
                               public void handleException(BiiResultErrorException biiResultErrorException) {
                                   commView.psnXpadProductDetailQueryFailed();
                               }

                               @Override
                               public void onCompleted() {
                               }

                               @Override
                               public void onNext(PsnXpadAccountQueryResult result) {

                                   /**
                                    * 客户理财账户信息查询结果model created by yx
                                    */
                                   PsnXpadAccountQueryResModel resModel = new PsnXpadAccountQueryResModel();

                                   /**
                                    * 客户理财账户信息查询结果集合 created by yx
                                    */
                                   List<PsnXpadAccountQueryResModel.XPadAccountEntity> resList = new ArrayList<>();

                                   /**
                                    * 客户理财账户信息查询结果集合 created by liu weidong
                                    */
                                   ArrayList<WealthAccountBean> accountList = new ArrayList<>();//账户详情列表

                                   /**
                                    * 循环遍历账户列表查询结果，分别装入账户列表model  以及账户列表集合
                                    */
                                   for (PsnXpadAccountQueryResult.XPadAccountEntity entity : result.getList()) {
                                       PsnXpadAccountQueryResModel.XPadAccountEntity bean = new PsnXpadAccountQueryResModel.XPadAccountEntity();

                                       //账户列表封装对象
                                       BeanConvertor.toBean(entity, bean);
                                       resList.add(bean);

                                       //账户列表封装对象 集合
                                       WealthAccountBean accountBean = new WealthAccountBean();
                                       accountBean.setAccountId(entity.getAccountId());
                                       accountBean.setAccountKey(entity.getAccountKey());
                                       accountBean.setAccountType(entity.getAccountType());
                                       accountBean.setAccountNo(entity.getAccountNo());
                                       accountBean.setBancID(entity.getBancID());
                                       accountBean.setXpadAccount(entity.getXpadAccount());
                                       accountBean.setXpadAccountSatus(entity.getXpadAccountSatus());
                                       accountList.add(accountBean);
                                   }
                                   resModel.setList(resList);
                                   commView.psnXpadProductDetailQuerySuccess(detailModel, resModel, accountList);
                               }
                           }
                );
    }
}
