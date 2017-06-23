package com.boc.bocsoft.mobile.bocmobile.buss.equickpay.presenter;

import com.boc.bocsoft.mobile.bii.bus.equickpay.model.PsnHCEQuickPassCancel.PsnHCEQuickPassCancelParams;
import com.boc.bocsoft.mobile.bii.bus.equickpay.model.PsnHCEQuickPassCancel.PsnHCEQuickPassCancelResult;
import com.boc.bocsoft.mobile.bii.bus.equickpay.model.PsnHCEQuickPassListQuery.PsnHCEQuickPassListQueryResult;
import com.boc.bocsoft.mobile.bii.bus.equickpay.service.EquickpayService;
import com.boc.bocsoft.mobile.bii.bus.global.model.PSNCreatConversation.PSNCreatConversationParams;
import com.boc.bocsoft.mobile.bii.bus.global.service.GlobalService;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.base.Exception.BIIBaseSubscriber;
import com.boc.bocsoft.mobile.bocmobile.base.utils.LogUtils;
import com.boc.bocsoft.mobile.bocmobile.buss.equickpay.model.hcecardlist.HceCardListQueryViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.equickpay.ui.hcecardlist.HceCardListContact;
import com.boc.bocsoft.mobile.bocmobile.buss.equickpay.ui.hcelogoff.HceCardCancelContact;
import com.boc.bocsoft.mobile.common.utils.BeanConvertor.BeanConvertor;
import com.boc.bocsoft.mobile.framework.rx.SchedulersCompat;
import com.boc.bocsoft.mobile.framework.ui.RxPresenter;

import java.util.List;

import rx.Observable;
import rx.functions.Func1;

/**
 * Presenter：Hce Card 注销
 * Created by gengjunying on 2016/11/29
 */
public class HceCardCancelPresenter extends RxPresenter implements HceCardCancelContact.Presenter {
    private HceCardCancelContact.View mView;
    private GlobalService globalService;
    private EquickpayService equickpayService;

    public HceCardCancelPresenter(HceCardCancelContact.View view) {
        this.mView = view;
        mView.setPresenter(this);
        equickpayService = new EquickpayService();
        globalService = new GlobalService();
    }

    @Override
    public void HCEQuickPassCancel(final String masterCardNo, final String slaveCardNo) {
        globalService.psnCreatConversation(new PSNCreatConversationParams())
                .flatMap(new Func1<String, Observable<PsnHCEQuickPassCancelResult>>() {
                    @Override
                    public Observable<PsnHCEQuickPassCancelResult> call(String conversationId) {
                        PsnHCEQuickPassCancelParams psnHCEQuickPassCancelParams = new PsnHCEQuickPassCancelParams();
                        psnHCEQuickPassCancelParams.setMasterCardNo(masterCardNo);
                        psnHCEQuickPassCancelParams.setSlaveCardNo(slaveCardNo);
                        psnHCEQuickPassCancelParams.setConversationId(conversationId);

                        return equickpayService.PsnHCEQuickPassCancel(psnHCEQuickPassCancelParams);
                    }
                })
                .compose(SchedulersCompat.<PsnHCEQuickPassCancelResult>applyIoSchedulers())
                .compose(this.<PsnHCEQuickPassCancelResult>bindToLifecycle())
                .subscribe(new BIIBaseSubscriber<PsnHCEQuickPassCancelResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        LogUtils.d("error", biiResultErrorException.getErrorMessage());
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(PsnHCEQuickPassCancelResult psnHCEQuickPassCancelResult) {
                        mView.HCEQuickPassCancelSuccess(psnHCEQuickPassCancelResult.toString());
                    }
                });
    }

}
