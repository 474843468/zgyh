package com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.accountmanagement.presenter;

import com.boc.bocsoft.mobile.bii.bus.global.model.PSNCreatConversation.PSNCreatConversationParams;
import com.boc.bocsoft.mobile.bii.bus.global.model.PSNGetTokenId.PSNGetTokenIdParams;
import com.boc.bocsoft.mobile.bii.bus.global.service.GlobalService;
import com.boc.bocsoft.mobile.bii.bus.longshortforex.model.PsnVFGBailListQuery.PsnVFGBailListQueryParams;
import com.boc.bocsoft.mobile.bii.bus.longshortforex.model.PsnVFGBailListQuery.PsnVFGBailListQueryResult;
import com.boc.bocsoft.mobile.bii.bus.longshortforex.model.PsnVFGFilterDebitCard.PsnVFGFilterDebitCardParams;
import com.boc.bocsoft.mobile.bii.bus.longshortforex.model.PsnVFGFilterDebitCard.PsnVFGFilterDebitCardResult;
import com.boc.bocsoft.mobile.bii.bus.longshortforex.model.PsnVFGGetBindAccount.PsnVFGGetBindAccountParams;
import com.boc.bocsoft.mobile.bii.bus.longshortforex.model.PsnVFGGetBindAccount.PsnVFGGetBindAccountResult;
import com.boc.bocsoft.mobile.bii.bus.longshortforex.model.PsnVFGSetTradeAccount.PsnVFGSetTradeAccountParams;
import com.boc.bocsoft.mobile.bii.bus.longshortforex.model.PsnVFGSetTradeAccount.PsnVFGSetTradeAccountResult;
import com.boc.bocsoft.mobile.bii.bus.longshortforex.service.LongShortForexService;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.base.Exception.BIIBaseSubscriber;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.accountmanagement.model.VFGBailListQueryViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.accountmanagement.model.VFGFilterDebitCardViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.accountmanagement.model.VFGGetBindAccountViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.accountmanagement.model.VFGSetTradeAccountViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.accountmanagement.ui.ChangeTradeAccountContact;
import com.boc.bocsoft.mobile.common.utils.BeanConvertor.BeanConvertor;
import com.boc.bocsoft.mobile.framework.rx.SchedulersCompat;
import com.boc.bocsoft.mobile.framework.rx.lifecycle.RxLifecycleManager;
import com.boc.bocsoft.mobile.framework.ui.RxPresenter;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.functions.Func1;

/**
 * Presenter：双向宝-账户管理-变更交易账户(从首页进)
 * Created by zhx on 2016/12/21
 */
public class ChangeTradeAccountPresenter extends RxPresenter implements ChangeTradeAccountContact.Presenter {
    private ChangeTradeAccountContact.View mView;
    private RxLifecycleManager mRxLifecycleManager;
    private GlobalService globalService;
    private LongShortForexService longShortForexService;
    private String conversationId;

    public ChangeTradeAccountPresenter(ChangeTradeAccountContact.View view) {
        this.mView = view;
        mView.setPresenter(this);
        mRxLifecycleManager = new RxLifecycleManager();

        longShortForexService = new LongShortForexService();
        globalService = new GlobalService();
    }

    @Override
    public void vFGSetTradeAccount(final VFGSetTradeAccountViewModel vfgSetTradeAccountViewModel) {
        PSNCreatConversationParams psnCreatConversationParams = new PSNCreatConversationParams();
        globalService.psnCreatConversation(psnCreatConversationParams)
                .compose(mRxLifecycleManager.<String>bindToLifecycle())
                .flatMap(new Func1<String, Observable<String>>() {
                    @Override
                    public Observable<String> call(String conversation) {
                        ChangeTradeAccountPresenter.this.conversationId = conversation;

                        PSNGetTokenIdParams psnGetTokenIdParams = new PSNGetTokenIdParams();
                        psnGetTokenIdParams.setConversationId(conversation);
                        return globalService.psnGetTokenId(psnGetTokenIdParams);
                    }
                })
                .flatMap(new Func1<String, Observable<PsnVFGSetTradeAccountResult>>() {
                    @Override
                    public Observable<PsnVFGSetTradeAccountResult> call(String token) {
                        PsnVFGSetTradeAccountParams params = new PsnVFGSetTradeAccountParams();
                        params.setConversationId(conversationId);
                        params.setToken(token);

                        BeanConvertor.toBean(vfgSetTradeAccountViewModel, params);
                        return longShortForexService.psnVFGSetTradeAccount(params);
                    }
                })
                .compose(SchedulersCompat.<PsnVFGSetTradeAccountResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnVFGSetTradeAccountResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        mView.vFGSetTradeAccountFail(biiResultErrorException);
                    }

                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onNext(PsnVFGSetTradeAccountResult psnVFGChangeContractResult) {
                        mView.vFGSetTradeAccountSuccess(null);
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
    public void vFGFilterDebitCard(final VFGFilterDebitCardViewModel viewModel) {
        PsnVFGFilterDebitCardParams params = new PsnVFGFilterDebitCardParams();
        longShortForexService.psnVFGFilterDebitCard(params)
                .compose(SchedulersCompat.<List<PsnVFGFilterDebitCardResult>>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<List<PsnVFGFilterDebitCardResult>>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        mView.vFGFilterDebitCardFail(biiResultErrorException);
                    }

                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onNext(List<PsnVFGFilterDebitCardResult> result) {
                        mView.vFGFilterDebitCardSuccess(generateVFGFilterDebitCardViewModel(viewModel, result));

                        System.out.print("");
                    }
                });
    }

    // 生成"过滤出符合条件的借记卡账户"ViewModel
    private VFGFilterDebitCardViewModel generateVFGFilterDebitCardViewModel(VFGFilterDebitCardViewModel viewModel, List<PsnVFGFilterDebitCardResult> result) {
        List<VFGFilterDebitCardViewModel.DebitCardEntity> cardList = new ArrayList<VFGFilterDebitCardViewModel.DebitCardEntity>();

        for (PsnVFGFilterDebitCardResult card : result) {
            VFGFilterDebitCardViewModel.DebitCardEntity debitCardEntity = new VFGFilterDebitCardViewModel.DebitCardEntity();
            BeanConvertor.toBean(card, debitCardEntity);

            cardList.add(debitCardEntity);
        }

        viewModel.setCardList(cardList);

        return viewModel;
    }
}
