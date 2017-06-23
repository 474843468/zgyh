package com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.accountmanagement.presenter;

import com.boc.bocsoft.mobile.bii.bus.longshortforex.model.PsnVFGFilterDebitCard.PsnVFGFilterDebitCardParams;
import com.boc.bocsoft.mobile.bii.bus.longshortforex.model.PsnVFGFilterDebitCard.PsnVFGFilterDebitCardResult;
import com.boc.bocsoft.mobile.bii.bus.longshortforex.service.LongShortForexService;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.base.Exception.BIIBaseSubscriber;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.accountmanagement.model.VFGFilterDebitCardViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.accountmanagement.ui.ChangeContractContact;
import com.boc.bocsoft.mobile.common.utils.BeanConvertor.BeanConvertor;
import com.boc.bocsoft.mobile.framework.rx.SchedulersCompat;
import com.boc.bocsoft.mobile.framework.rx.lifecycle.RxLifecycleManager;
import com.boc.bocsoft.mobile.framework.ui.RxPresenter;

import java.util.ArrayList;
import java.util.List;

/**
 * Presenter：双向宝-账户管理-新增保证金账户-过滤出符合条件的借记卡账户
 * Created by zhx on 2016/12/8
 */
public class ChangeContractPresenter extends RxPresenter implements ChangeContractContact.Presenter {
    private ChangeContractContact.View mView;
    private RxLifecycleManager mRxLifecycleManager;
    private LongShortForexService longShortForexService;

    public ChangeContractPresenter(ChangeContractContact.View view) {
        this.mView = view;
        mView.setPresenter(this);
        mRxLifecycleManager = new RxLifecycleManager();

        longShortForexService = new LongShortForexService();
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
