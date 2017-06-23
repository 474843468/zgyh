package com.boc.bocsoft.mobile.bocmobile.buss.transfer.phonetransferquery.presenter;

import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnMobileTransferQueryUnSubmitTrans.PsnMobileTransferQueryUnSubmitTransParams;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnMobileTransferQueryUnSubmitTrans.PsnMobileTransferQueryUnSubmitTransResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.service.TransferService;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.base.Exception.BIIBaseSubscriber;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.phonetransferquery.model.PhoneTransferQueryViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.phonetransferquery.ui.PhoneTransferQueryContract;
import com.boc.bocsoft.mobile.framework.rx.SchedulersCompat;
import com.boc.bocsoft.mobile.framework.rx.lifecycle.RxLifecycleManager;

/**
 * 手机号转账查询Presenter
 *
 * Created by liuweidong on 2016/6/23.
 */
public class PhoneTransferQueryPresenter implements PhoneTransferQueryContract.Presenter {
    private RxLifecycleManager mRxLifecycleManager;
    private PhoneTransferQueryContract.View mView;
    // 手机号转账查询
    private TransferService psnPhoneTransferService;

    public PhoneTransferQueryPresenter(PhoneTransferQueryContract.View view){
        mRxLifecycleManager = new RxLifecycleManager();
        mView = view;
        mView.setPresenter(this);
        psnPhoneTransferService = new TransferService();
    }

    @Override
    public void queryMobileTransfer(PhoneTransferQueryViewModel phoneTransferQueryViewModel) {
        PsnMobileTransferQueryUnSubmitTransParams params = buildPsnMobileTransferQueryUnSubmitTransParams(phoneTransferQueryViewModel);

        psnPhoneTransferService.psnMobileTransferQueryUnSubmitTrans(params)
                .compose(mRxLifecycleManager.<PsnMobileTransferQueryUnSubmitTransResult>bindToLifecycle())
                .compose(SchedulersCompat.<PsnMobileTransferQueryUnSubmitTransResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnMobileTransferQueryUnSubmitTransResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        mView.queryMobileTransferFail(biiResultErrorException);
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(PsnMobileTransferQueryUnSubmitTransResult psnMobileTransferQueryUnSubmitTransResult) {
                        mView.queryMobileTransferSuccess(psnMobileTransferQueryUnSubmitTransResult);
                    }
                });
    }

    /**
     * 封装手机号转账请求参数
     *
     * @param phoneTransferQueryViewModel
     * @return
     */
    private PsnMobileTransferQueryUnSubmitTransParams buildPsnMobileTransferQueryUnSubmitTransParams(PhoneTransferQueryViewModel phoneTransferQueryViewModel){
        PsnMobileTransferQueryUnSubmitTransParams params = new PsnMobileTransferQueryUnSubmitTransParams();
        params.setServiceId(phoneTransferQueryViewModel.getServiceId());
//        params.setBeiginDate(phoneTransferQueryViewModel.getBeiginDate().format(DateFormatters.dateFormatter1));
//        params.setEndDate(phoneTransferQueryViewModel.getEndDate().format(DateFormatters.dateFormatter1));
        params.setBeiginDate("2025/04/18");
        params.setEndDate("2025/04/24");
        params.setStatus("");
        params.setCurrentIndex(phoneTransferQueryViewModel.getCurrentIndex());
        params.setPageSize(phoneTransferQueryViewModel.getPageSize());
        return params;
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unsubscribe() {
        mRxLifecycleManager.onDestroy();
    }
}
