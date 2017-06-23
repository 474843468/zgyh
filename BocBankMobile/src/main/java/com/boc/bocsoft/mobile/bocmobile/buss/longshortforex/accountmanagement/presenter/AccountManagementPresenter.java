package com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.accountmanagement.presenter;

import com.boc.bocsoft.mobile.bii.bus.global.service.GlobalService;
import com.boc.bocsoft.mobile.bii.bus.longshortforex.model.PsnVFGBailAccountInfoListQuery.PsnVFGBailAccountInfoListQueryParams;
import com.boc.bocsoft.mobile.bii.bus.longshortforex.model.PsnVFGBailAccountInfoListQuery.PsnVFGBailAccountInfoListQueryResult;
import com.boc.bocsoft.mobile.bii.bus.longshortforex.model.PsnVFGBailListQuery.PsnVFGBailListQueryParams;
import com.boc.bocsoft.mobile.bii.bus.longshortforex.model.PsnVFGBailListQuery.PsnVFGBailListQueryResult;
import com.boc.bocsoft.mobile.bii.bus.longshortforex.model.PsnVFGFilterDebitCard.PsnVFGFilterDebitCardParams;
import com.boc.bocsoft.mobile.bii.bus.longshortforex.model.PsnVFGFilterDebitCard.PsnVFGFilterDebitCardResult;
import com.boc.bocsoft.mobile.bii.bus.longshortforex.model.PsnVFGGetBindAccount.PsnVFGGetBindAccountParams;
import com.boc.bocsoft.mobile.bii.bus.longshortforex.model.PsnVFGGetBindAccount.PsnVFGGetBindAccountResult;
import com.boc.bocsoft.mobile.bii.bus.longshortforex.service.LongShortForexService;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.base.Exception.BIIBaseSubscriber;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.accountmanagement.model.VFGBailAccountInfoListQueryViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.accountmanagement.model.VFGBailListQueryViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.accountmanagement.model.VFGFilterDebitCardViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.accountmanagement.model.VFGGetBindAccountViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.accountmanagement.ui.AccountManagementContact;
import com.boc.bocsoft.mobile.common.utils.BeanConvertor.BeanConvertor;
import com.boc.bocsoft.mobile.framework.rx.SchedulersCompat;
import com.boc.bocsoft.mobile.framework.rx.lifecycle.RxLifecycleManager;
import com.boc.bocsoft.mobile.framework.ui.RxPresenter;

import java.util.ArrayList;
import java.util.List;

/**
 * Presenter：双向宝-账户管理
 * Created by zhx on 2016/11/22
 */
public class AccountManagementPresenter extends RxPresenter implements AccountManagementContact.Presenter {
    private AccountManagementContact.View mView;
    private RxLifecycleManager mRxLifecycleManager;

    private GlobalService globalService;
    private LongShortForexService longShortForexService;

    public AccountManagementPresenter(AccountManagementContact.View view) {
        this.mView = view;
        mView.setPresenter(this);
        mRxLifecycleManager = new RxLifecycleManager();

        globalService = new GlobalService();
        longShortForexService = new LongShortForexService();
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
    public void vFGBailAccountInfoListQuery(final VFGBailAccountInfoListQueryViewModel viewModel) {
        PsnVFGBailAccountInfoListQueryParams params = new PsnVFGBailAccountInfoListQueryParams();

        longShortForexService.psnVFGBailAccountInfoListQuery(params)
                .compose(SchedulersCompat.<PsnVFGBailAccountInfoListQueryResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnVFGBailAccountInfoListQueryResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        mView.vFGBailAccountInfoListQueryFail(biiResultErrorException);
                    }

                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onNext(PsnVFGBailAccountInfoListQueryResult psnVFGGetBindAccountResult) {
                        mView.vFGBailAccountInfoListQuerySuccess(buildVfgBailAccountInfoListQueryViewModel(psnVFGGetBindAccountResult, viewModel));
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

    // 构建ViewModel(双向宝保证金账户基本信息多笔查询)
    private VFGBailAccountInfoListQueryViewModel buildVfgBailAccountInfoListQueryViewModel(PsnVFGBailAccountInfoListQueryResult result, VFGBailAccountInfoListQueryViewModel viewModel) {
        List<VFGBailAccountInfoListQueryViewModel.VFGBailAccountInfo> list = new ArrayList<VFGBailAccountInfoListQueryViewModel.VFGBailAccountInfo>();

        for (PsnVFGBailAccountInfoListQueryResult.VFGBailAccountInfo accountInfo : result.getList()) {
            VFGBailAccountInfoListQueryViewModel.VFGBailAccountInfo vfgBailAccountInfo = new VFGBailAccountInfoListQueryViewModel.VFGBailAccountInfo();
            BeanConvertor.toBean(accountInfo, vfgBailAccountInfo);

            // 对结算货币赋值
            VFGBailAccountInfoListQueryViewModel.VFGBailAccountInfo.SettleCurrencyEntity currencyEntity = new VFGBailAccountInfoListQueryViewModel.VFGBailAccountInfo.SettleCurrencyEntity();
            BeanConvertor.toBean(accountInfo.getSettleCurrency(), currencyEntity);

            vfgBailAccountInfo.setSettleCurrency2(currencyEntity);

            list.add(vfgBailAccountInfo);
        }

        viewModel.setList(list);

        return viewModel;
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
}
