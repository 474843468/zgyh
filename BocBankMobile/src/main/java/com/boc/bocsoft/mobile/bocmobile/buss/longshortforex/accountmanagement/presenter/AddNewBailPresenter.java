package com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.accountmanagement.presenter;

import android.app.Activity;

import com.boc.bocsoft.mobile.bii.bus.global.model.PSNCreatConversation.PSNCreatConversationParams;
import com.boc.bocsoft.mobile.bii.bus.global.model.PsnGetSecurityFactor.CombinListBean;
import com.boc.bocsoft.mobile.bii.bus.global.model.PsnGetSecurityFactor.PsnGetSecurityFactorParams;
import com.boc.bocsoft.mobile.bii.bus.global.model.PsnGetSecurityFactor.PsnGetSecurityFactorResult;
import com.boc.bocsoft.mobile.bii.bus.global.service.GlobalService;
import com.boc.bocsoft.mobile.bii.bus.longshortforex.model.PsnVFGBailListQuery.PsnVFGBailListQueryParams;
import com.boc.bocsoft.mobile.bii.bus.longshortforex.model.PsnVFGBailListQuery.PsnVFGBailListQueryResult;
import com.boc.bocsoft.mobile.bii.bus.longshortforex.model.PsnVFGBailProductQuery.PsnVFGBailProductQueryParams;
import com.boc.bocsoft.mobile.bii.bus.longshortforex.model.PsnVFGBailProductQuery.PsnVFGBailProductQueryResult;
import com.boc.bocsoft.mobile.bii.bus.longshortforex.model.PsnVFGGetBindAccount.PsnVFGGetBindAccountParams;
import com.boc.bocsoft.mobile.bii.bus.longshortforex.model.PsnVFGGetBindAccount.PsnVFGGetBindAccountResult;
import com.boc.bocsoft.mobile.bii.bus.longshortforex.model.PsnVFGSignPre.PsnVFGSignPreParams;
import com.boc.bocsoft.mobile.bii.bus.longshortforex.model.PsnVFGSignPre.PsnVFGSignPreResult;
import com.boc.bocsoft.mobile.bii.bus.longshortforex.service.LongShortForexService;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.base.Exception.BIIBaseSubscriber;
import com.boc.bocsoft.mobile.bocmobile.base.model.VerifyBean;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify.SecurityFactorModel;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify.SecurityVerity;
import com.boc.bocsoft.mobile.bocmobile.buss.account.utils.ModelUtil;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.accountmanagement.model.VFGBailListQueryViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.accountmanagement.model.VFGBailProductQueryViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.accountmanagement.model.VFGGetBindAccountViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.accountmanagement.model.VFGSignPreViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.accountmanagement.ui.AddNewBailContact;
import com.boc.bocsoft.mobile.common.utils.BeanConvertor.BeanConvertor;
import com.boc.bocsoft.mobile.framework.rx.SchedulersCompat;
import com.boc.bocsoft.mobile.framework.rx.lifecycle.RxLifecycleManager;
import com.boc.bocsoft.mobile.framework.ui.RxPresenter;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.functions.Func1;

/**
 * Contact：双向宝-账户管理-新增保证金账户
 * Created by zhx on 2016/12/8
 */
public class AddNewBailPresenter extends RxPresenter implements AddNewBailContact.Presenter {
    private AddNewBailContact.View mView;
    private RxLifecycleManager mRxLifecycleManager;
    private GlobalService globalService;
    private LongShortForexService longShortForexService;
    private String conversationId;
    private Activity mActivity;

    public void setActivity(Activity activity) {
        this.mActivity = activity;
    }

    public AddNewBailPresenter(AddNewBailContact.View view) {
        this.mView = view;
        mView.setPresenter(this);
        mRxLifecycleManager = new RxLifecycleManager();

        longShortForexService = new LongShortForexService();
        globalService = new GlobalService();
    }

    @Override
    public void vFGBailProductQuery(final VFGBailProductQueryViewModel viewModel) {
        PSNCreatConversationParams psnCreatConversationParams = new PSNCreatConversationParams();
        globalService.psnCreatConversation(psnCreatConversationParams)
                .compose(mRxLifecycleManager.<String>bindToLifecycle())
                .flatMap(new Func1<String, Observable<PsnVFGBailProductQueryResult>>() {
                    @Override
                    public Observable<PsnVFGBailProductQueryResult> call(String conversationId) {
                        PsnVFGBailProductQueryParams params = new PsnVFGBailProductQueryParams();
                        params.set_refresh(viewModel.get_refresh());
                        params.setPageSize(viewModel.getPageSize());
                        params.setCurrentIndex(viewModel.getCurrentIndex());
                        params.setConversationId(conversationId);
                        return longShortForexService.psnVFGBailProductQuery(params);
                    }
                })
                .compose(SchedulersCompat.<PsnVFGBailProductQueryResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnVFGBailProductQueryResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        mView.vFGBailProductQueryFail(biiResultErrorException);
                    }

                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onNext(PsnVFGBailProductQueryResult result) {
                        mView.vFGBailProductQuerySuccess(generateVFGBailProductQueryViewModel(viewModel, result));
                    }
                });
    }

    @Override
    public void vFGSignPre(final VFGSignPreViewModel viewModel) {
        final PSNCreatConversationParams psnCreatConversationParams = new PSNCreatConversationParams();
        globalService.psnCreatConversation(psnCreatConversationParams)
                .compose(mRxLifecycleManager.<String>bindToLifecycle())
                .flatMap(new Func1<String, Observable<PsnGetSecurityFactorResult>>() {
                    @Override
                    public Observable<PsnGetSecurityFactorResult> call(String conversation) {
                        AddNewBailPresenter.this.conversationId = conversation;
                        viewModel.setConversationId(conversationId);

                        PsnGetSecurityFactorParams psnGetSecurityFactorParams = new PsnGetSecurityFactorParams();
                        psnGetSecurityFactorParams.setConversationId(conversation);
                        psnGetSecurityFactorParams.setServiceId("PB081");
                        return globalService.psnGetSecurityFactor(psnGetSecurityFactorParams);
                    }
                })
                .flatMap(new Func1<PsnGetSecurityFactorResult, Observable<PsnVFGSignPreResult>>() {
                    @Override
                    public Observable<PsnVFGSignPreResult> call(PsnGetSecurityFactorResult psnGetSecurityFactorResult) {
                        SecurityFactorModel securityFactorModel = ModelUtil.generateSecurityFactorModel(psnGetSecurityFactorResult);
                        CombinListBean combinBean = SecurityVerity.getInstance(mActivity).
                                getDefaultSecurityFactorId(securityFactorModel);

                        viewModel.set_combinId(combinBean.getId());
                        viewModel.setCombinName(combinBean.getName());

                        PsnVFGSignPreParams params = new PsnVFGSignPreParams();
                        BeanConvertor.toBean(viewModel, params);
                        params.setConversationId(conversationId);
                        return longShortForexService.psnVFGSignPre(params);
                    }
                })
                .compose(SchedulersCompat.<PsnVFGSignPreResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnVFGSignPreResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        mView.vFGBailProductQueryFail(biiResultErrorException);
                    }

                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onNext(PsnVFGSignPreResult psnVFGSignPreResult) {
                        VerifyBean verifyBean = BeanConvertor.toBean(psnVFGSignPreResult, new VerifyBean());

                        mView.vFGSignPreSuccess(verifyBean);
                    }
                });
    }

    @Override
    public void vFGGetBindAccount(final VFGGetBindAccountViewModel vfgGetBindAccountViewModel) {
        PsnVFGGetBindAccountParams psnVFGGetBindAccountParams = new PsnVFGGetBindAccountParams();

        longShortForexService.psnVFGGetBindAccount(psnVFGGetBindAccountParams)
                .compose(SchedulersCompat.<PsnVFGGetBindAccountResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnVFGGetBindAccountResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        mView.vFGGetBindAccountFail(biiResultErrorException);
                    }

                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onNext(PsnVFGGetBindAccountResult psnVFGGetBindAccountResult) {
                        BeanConvertor.toBean(psnVFGGetBindAccountResult, vfgGetBindAccountViewModel);
                        mView.vFGGetBindAccountSuccess(vfgGetBindAccountViewModel);
                    }
                });
    }

    @Override
    public void vFGBailListQuery(final VFGBailListQueryViewModel vfgBailListQueryViewModel) {
        PsnVFGBailListQueryParams psnVFGBailListQueryParams = new PsnVFGBailListQueryParams();

        longShortForexService.psnVFGBailListQuery(psnVFGBailListQueryParams)
                .compose(SchedulersCompat.<List<PsnVFGBailListQueryResult>>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<List<PsnVFGBailListQueryResult>>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        mView.vFGBailListQueryFail(biiResultErrorException);
                    }

                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onNext(List<PsnVFGBailListQueryResult> psnVFGBailListQueryResults) {
                        mView.vFGBailListQuerySuccess(buildVfgBailListQueryViewModel(psnVFGBailListQueryResults, vfgBailListQueryViewModel));
                    }
                });
    }

    // 构建ViewModel(查询保证金账户列表)
    private VFGBailListQueryViewModel buildVfgBailListQueryViewModel(List<PsnVFGBailListQueryResult> resultList, VFGBailListQueryViewModel viewModel) {
        List<VFGBailListQueryViewModel.BailItemEntity> bailItemList = viewModel.getEntityList();

        for (PsnVFGBailListQueryResult result : resultList) {
            VFGBailListQueryViewModel.BailItemEntity bailItemEntity = new VFGBailListQueryViewModel.BailItemEntity();
            BeanConvertor.toBean(result, bailItemEntity);

            bailItemList.add(bailItemEntity);
        }

        return viewModel;
    }

    // 生成保证金产品ViewModel
    private VFGBailProductQueryViewModel generateVFGBailProductQueryViewModel(VFGBailProductQueryViewModel viewModel, PsnVFGBailProductQueryResult result) {
        List<PsnVFGBailProductQueryResult.VFGBailProduct> list = result.getList();

        List<VFGBailProductQueryViewModel.VFGBailProduct> viewList = new ArrayList<VFGBailProductQueryViewModel.VFGBailProduct>();

        for (PsnVFGBailProductQueryResult.VFGBailProduct vfgBailProduct : list) {
            VFGBailProductQueryViewModel.VFGBailProduct product = new VFGBailProductQueryViewModel.VFGBailProduct();
            BeanConvertor.toBean(vfgBailProduct, product);

            viewList.add(product);
        }

        viewModel.setList(viewList);
        return viewModel;
    }
}
