package com.boc.bocsoft.mobile.bocmobile.buss.transfer.payee.presenter;

import com.boc.bocsoft.mobile.bii.bus.global.model.PSNCreatConversation.PSNCreatConversationParams;
import com.boc.bocsoft.mobile.bii.bus.global.model.PSNGetTokenId.PSNGetTokenIdParams;
import com.boc.bocsoft.mobile.bii.bus.global.service.GlobalService;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnEbpsRealTimePaymentSavePayee.PsnEbpsRealTimePaymentSavePayeeParams;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnEbpsRealTimePaymentSavePayee.PsnEbpsRealTimePaymentSavePayeeResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnQueryActTypeByActNum.PsnQueryActTypeByActNumParams;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnQueryActTypeByActNum.PsnQueryActTypeByActNumResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransBocAddPayee.PsnTransBocAddPayeeParams;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransBocAddPayee.PsnTransBocAddPayeeResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransIsSamePayee.PsnTransIsSamePayeeParams;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransIsSamePayee.PsnTransIsSamePayeeResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransNationalAddPayee.PsnTransNationalAddPayeeParams;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransNationalAddPayee.PsnTransNationalAddPayeeResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.service.TransferService;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.base.Exception.BIIBaseSubscriber;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.payee.model.PsnEbpsRealTimePaymentSavePayeeViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.payee.model.PsnQueryActTypeByActNumViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.payee.model.PsnTransBocAddPayeeViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.payee.model.PsnTransIsSamePayeeViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.payee.model.PsnTransNationalAddPayeeViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.payee.ui.AddPayeeContact;
import com.boc.bocsoft.mobile.common.utils.BeanConvertor.BeanConvertor;
import com.boc.bocsoft.mobile.framework.rx.SchedulersCompat;
import com.boc.bocsoft.mobile.framework.rx.lifecycle.RxLifecycleManager;

import rx.Observable;
import rx.functions.Func1;

/**
 * Presenter:添加收款人
 * Created by zhx on 2016/7/22
 */
public class AddPayeePresenter implements AddPayeeContact.Presenter {
    private AddPayeeContact.View mView;
    private RxLifecycleManager mRxLifecycleManager;

    private TransferService transferService;
    private GlobalService globalService;

    private String conversationId;

    public AddPayeePresenter(AddPayeeContact.View view) {
        this.mView = view;
        mRxLifecycleManager = new RxLifecycleManager();
        transferService = new TransferService();
        globalService = new GlobalService();

        mView.setPresenter(this);
    }

    // 国内跨行汇款：新增收款人
    @Override
    public void psnTransNationalAddPayee(final PsnTransNationalAddPayeeViewModel viewModel) {
        PSNCreatConversationParams psnCreatConversationParams = new PSNCreatConversationParams();
        globalService.psnCreatConversation(psnCreatConversationParams)
                .compose(mRxLifecycleManager.<String>bindToLifecycle())
                .flatMap(new Func1<String, Observable<String>>() {
                    @Override
                    public Observable<String> call(String conversation) {
                        AddPayeePresenter.this.conversationId = conversation;

                        PSNGetTokenIdParams psnGetTokenIdParams = new PSNGetTokenIdParams();
                        psnGetTokenIdParams.setConversationId(conversation);
                        return globalService.psnGetTokenId(psnGetTokenIdParams);
                    }
                })
                .flatMap(new Func1<String, Observable<PsnTransNationalAddPayeeResult>>() {
                    @Override
                    public Observable<PsnTransNationalAddPayeeResult> call(String token) {
                        PsnTransNationalAddPayeeParams params = buildNationalAddPayeeRequestParams(viewModel);
                        params.setToken(token);
                        params.setConversationId(AddPayeePresenter.this.conversationId);
                        return transferService.psnTransNationalAddPayee(params);
                    }
                })
                .compose(SchedulersCompat.<PsnTransNationalAddPayeeResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnTransNationalAddPayeeResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        mView.psnTransNationalAddPayeeFailed(biiResultErrorException);
                    }

                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onNext(PsnTransNationalAddPayeeResult psnTransNationalAddPayeeResult) {
                        mView.psnTransNationalAddPayeeSuccess(viewModel);
                    }
                });
    }

    // 实时付款保存收款人
    @Override
    public void psnEbpsRealTimePaymentSavePayee(final PsnEbpsRealTimePaymentSavePayeeViewModel viewModel) {

        PSNCreatConversationParams psnCreatConversationParams = new PSNCreatConversationParams();
        globalService.psnCreatConversation(psnCreatConversationParams)
                .compose(mRxLifecycleManager.<String>bindToLifecycle())
                .flatMap(new Func1<String, Observable<String>>() {
                    @Override
                    public Observable<String> call(String conversation) {
                        AddPayeePresenter.this.conversationId = conversation;

                        PSNGetTokenIdParams psnGetTokenIdParams = new PSNGetTokenIdParams();
                        psnGetTokenIdParams.setConversationId(conversation);
                        return globalService.psnGetTokenId(psnGetTokenIdParams);
                    }
                })
                .flatMap(new Func1<String, Observable<PsnEbpsRealTimePaymentSavePayeeResult>>() {
                    @Override
                    public Observable<PsnEbpsRealTimePaymentSavePayeeResult> call(String token) {
                        PsnEbpsRealTimePaymentSavePayeeParams params = new PsnEbpsRealTimePaymentSavePayeeParams();

                        BeanConvertor.toBean(viewModel, params);

                        params.setToken(token);
                        params.setConversationId(AddPayeePresenter.this.conversationId);
                        return transferService.psnEbpsRealTimePaymentSavePayee(params);
                    }
                })
                .compose(SchedulersCompat.<PsnEbpsRealTimePaymentSavePayeeResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnEbpsRealTimePaymentSavePayeeResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        mView.psnEbpsRealTimePaymentSavePayeeFailed(biiResultErrorException);
                    }

                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onNext(PsnEbpsRealTimePaymentSavePayeeResult psnEbpsRealTimePaymentSavePayeeResult) {
                        mView.psnEbpsRealTimePaymentSavePayeeSuccess(viewModel);
                    }
                });
    }

    // 根据账号查询账户类型
    @Override
    public void psnQueryActTypeByActNum(final PsnQueryActTypeByActNumViewModel viewModel) {
        // 显示修改中的对话框

        PsnQueryActTypeByActNumParams params = new PsnQueryActTypeByActNumParams();
        BeanConvertor.toBean(viewModel, params);

        transferService.psnQueryActTypeByActNum(params)
                .compose(mRxLifecycleManager.<PsnQueryActTypeByActNumResult>bindToLifecycle())
                .compose(SchedulersCompat.<PsnQueryActTypeByActNumResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnQueryActTypeByActNumResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        mView.psnQueryActTypeByActNumFailed(biiResultErrorException);
                    }

                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onNext(PsnQueryActTypeByActNumResult psnQueryActTypeByActNumResult) {
                        viewModel.setAccountType(psnQueryActTypeByActNumResult.getAccountType());
                        viewModel.setIbkNumber(psnQueryActTypeByActNumResult.getIbkNumber());
                        mView.psnQueryActTypeByActNumSuccess(viewModel);
                    }
                });
    }

    // 中行内汇款：新增收款人
    @Override
    public void psnTransBocAddPayee(final PsnTransBocAddPayeeViewModel viewModel) {

        PSNCreatConversationParams psnCreatConversationParams = new PSNCreatConversationParams();
        globalService.psnCreatConversation(psnCreatConversationParams)
                .compose(mRxLifecycleManager.<String>bindToLifecycle())
                .flatMap(new Func1<String, Observable<String>>() {
                    @Override
                    public Observable<String> call(String conversation) {
                        AddPayeePresenter.this.conversationId = conversation;

                        PSNGetTokenIdParams psnGetTokenIdParams = new PSNGetTokenIdParams();
                        psnGetTokenIdParams.setConversationId(conversation);
                        return globalService.psnGetTokenId(psnGetTokenIdParams);
                    }
                })
                .flatMap(new Func1<String, Observable<PsnTransBocAddPayeeResult>>() {
                    @Override
                    public Observable<PsnTransBocAddPayeeResult> call(String token) {
                        PsnTransBocAddPayeeParams params = new PsnTransBocAddPayeeParams();

                        BeanConvertor.toBean(viewModel, params);

                        params.setToken(token);
                        params.setConversationId(AddPayeePresenter.this.conversationId);
                        return transferService.psnTransBocAddPayee(params);
                    }
                })
                .compose(SchedulersCompat.<PsnTransBocAddPayeeResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnTransBocAddPayeeResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        mView.psnTransBocAddPayeeFailed(biiResultErrorException);
                    }

                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onNext(PsnTransBocAddPayeeResult psnTransBocAddPayeeResult) {
                        mView.psnTransBocAddPayeeSuccess(viewModel);
                    }
                });
    }

    private PsnTransNationalAddPayeeParams buildNationalAddPayeeRequestParams(PsnTransNationalAddPayeeViewModel viewModel) {
        PsnTransNationalAddPayeeParams params = new PsnTransNationalAddPayeeParams();
        params.setToAccountId(viewModel.getToAccountId());
        params.setPayeeName(viewModel.getPayeeName());
        params.setCnapsCode(viewModel.getCnapsCode());
        params.setBankName(viewModel.getBankName());
        params.setToOrgName(viewModel.getToOrgName());
        params.setPayeeMobile(viewModel.getPayeeMobile());
        return params;
    }

    // 判断是否存在相同收款人
    @Override
    public void psnTransIsSamePayee(final PsnTransIsSamePayeeViewModel viewModel) {
        PsnTransIsSamePayeeParams psnTransIsSamePayeeParams = buildRequestParams(viewModel);
        transferService.psnTransIsSamePayee(psnTransIsSamePayeeParams)
                .compose(mRxLifecycleManager.<PsnTransIsSamePayeeResult>bindToLifecycle())
                .compose(SchedulersCompat.<PsnTransIsSamePayeeResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnTransIsSamePayeeResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        mView.psnTransIsSamePayeeFailed(biiResultErrorException);
                    }

                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onNext(PsnTransIsSamePayeeResult psnTransIsSamePayeeResult) {
                        viewModel.setFlag(psnTransIsSamePayeeResult.getFlag());
                        mView.psnTransIsSamePayeeSuccess(viewModel);
                    }
                });
    }

    private PsnTransIsSamePayeeParams buildRequestParams(PsnTransIsSamePayeeViewModel viewModel) {
        PsnTransIsSamePayeeParams psnTransIsSamePayeeParams = new PsnTransIsSamePayeeParams();
        psnTransIsSamePayeeParams.setCnapsCode(viewModel.getCnapsCode());
        psnTransIsSamePayeeParams.setPayeeActno(viewModel.getPayeeActno());
        psnTransIsSamePayeeParams.setBocFlag(viewModel.getBocFlag());

        return psnTransIsSamePayeeParams;
    }

    @Override
    public void subscribe() {
        // TODO onResume时需要做的工作
    }

    @Override
    public void unsubscribe() {
        // TODO 防止外界已经销毁，而后台线程的任务还在执行
        mRxLifecycleManager.onDestroy();
    }
}
