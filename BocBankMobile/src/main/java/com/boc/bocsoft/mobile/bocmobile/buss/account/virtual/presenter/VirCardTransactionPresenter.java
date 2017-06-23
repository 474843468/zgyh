package com.boc.bocsoft.mobile.bocmobile.buss.account.virtual.presenter;

import com.boc.bocsoft.mobile.bii.bus.account.model.PsnCrcdVirtualCardApplyConfirm.PsnCrcdVirtualCardApplyConfirmResult;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnCrcdVirtualCardApplySubmit.PsnCrcdVirtualCardApplySubmitResult;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnCrcdVirtualCardFunctionSetConfirm.PsnCrcdVirtualCardFunctionSetConfirmResult;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnCrcdVirtualCardFunctionSetSubmit.PsnCrcdVirtualCardFunctionSetSubmitResult;
import com.boc.bocsoft.mobile.bii.bus.account.service.VirtualService;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify.DeviceInfoModel;
import com.boc.bocsoft.mobile.bocmobile.buss.account.base.BaseAccountSubscriber;
import com.boc.bocsoft.mobile.bocmobile.buss.account.base.BaseTransactionPresenter;
import com.boc.bocsoft.mobile.bocmobile.buss.account.utils.ModelUtil;
import com.boc.bocsoft.mobile.bocmobile.buss.account.virtual.model.VirtualCardModel;
import com.boc.bocsoft.mobile.framework.rx.SchedulersCompat;

import rx.Observable;
import rx.functions.Func1;

/**
 * @author wangyang
 *         16/9/2 01:09
 *         虚拟卡交易逻辑处理
 */
public class VirCardTransactionPresenter extends BaseTransactionPresenter implements VirtualCardContract.TransactionPresenter {

    private VirtualService virtualService;

    private VirtualCardContract.VirCardApplyTransactionView applyTransactionView;

    private VirtualCardContract.VirCardUpdateTransactionView updateTransactionView;

    public VirCardTransactionPresenter(VirtualCardContract.VirCardUpdateTransactionView updateTransactionView) {
        super(updateTransactionView);
        this.updateTransactionView = updateTransactionView;
        virtualService = new VirtualService();
    }

    public VirCardTransactionPresenter(VirtualCardContract.VirCardApplyTransactionView applyTransactionView) {
        super(applyTransactionView);
        virtualService = new VirtualService();
        this.applyTransactionView = applyTransactionView;
    }

    @Override
    public void psnCrcdVirtualCardApplyConfirm(final VirtualCardModel model, final String factorId) {
        getConversation().compose(this.<String>bindToLifecycle())
                .flatMap(new Func1<String, Observable<PsnCrcdVirtualCardApplyConfirmResult>>() {
                    @Override
                    public Observable<PsnCrcdVirtualCardApplyConfirmResult> call(String conversationId) {
                        return virtualService.psnCrcdVirtualCardApplyConfirm(ModelUtil.generateVirtualCardApplyConfirmParams(conversationId, factorId, model));
                    }
                }).compose(SchedulersCompat.<PsnCrcdVirtualCardApplyConfirmResult>applyIoSchedulers())
                .subscribe(new BaseAccountSubscriber<PsnCrcdVirtualCardApplyConfirmResult>() {
                    @Override
                    public void onNext(PsnCrcdVirtualCardApplyConfirmResult result) {
                        preTransactionSuccess(ModelUtil.generateSecurityModel(result));
                    }
                });
    }

    @Override
    public void psnCrcdVirtualCardApplySubmit(final VirtualCardModel model, final DeviceInfoModel deviceInfoModel, final String factorId, final String[] randomNums, final String[] encryptPasswords) {
        getToken().compose(this.<String>bindToLifecycle())
                .flatMap(new Func1<String, Observable<PsnCrcdVirtualCardApplySubmitResult>>() {
                    @Override
                    public Observable<PsnCrcdVirtualCardApplySubmitResult> call(String token) {
                        return virtualService.psnCrcdVirtualCardApplySubmit(ModelUtil.generateVirtualCardApplySubmitParams(model, getConversationId(), token, deviceInfoModel, factorId, randomNums, encryptPasswords));
                    }
                }).compose(SchedulersCompat.<PsnCrcdVirtualCardApplySubmitResult>applyIoSchedulers())
                .subscribe(new BaseAccountSubscriber<PsnCrcdVirtualCardApplySubmitResult>() {
                    @Override
                    public void onNext(PsnCrcdVirtualCardApplySubmitResult result) {
                        clearConversation();
                        applyTransactionView.psnCrcdVirtualCardApplySubmit(ModelUtil.generateVirtualModel(model, result));
                    }
                });
    }

    @Override
    public void psnCrcdVirtualCardFunctionSetConfirm(final VirtualCardModel model, final String factorId) {
        getConversation().compose(this.<String>bindToLifecycle())
                .flatMap(new Func1<String, Observable<PsnCrcdVirtualCardFunctionSetConfirmResult>>() {
                    @Override
                    public Observable<PsnCrcdVirtualCardFunctionSetConfirmResult> call(String conversationId) {
                        return virtualService.psnCrcdVirtualCardFunctionSetConfirm(ModelUtil.generateVirtualCardFunctionSetConfirmParams(conversationId, model, factorId));
                    }
                }).compose(SchedulersCompat.<PsnCrcdVirtualCardFunctionSetConfirmResult>applyIoSchedulers())
                .subscribe(new BaseAccountSubscriber<PsnCrcdVirtualCardFunctionSetConfirmResult>() {
                    @Override
                    public void onNext(PsnCrcdVirtualCardFunctionSetConfirmResult result) {
                        preTransactionSuccess(ModelUtil.generateSecurityModel(result));
                    }
                });
    }

    @Override
    public void psnCrcdVirtualCardFunctionSetSubmit(final VirtualCardModel model, final DeviceInfoModel deviceInfoModel, final String factorId, final String[] randomNums, final String[] encryptPasswords) {
        getToken().compose(this.<String>bindToLifecycle())
                .flatMap(new Func1<String, Observable<PsnCrcdVirtualCardFunctionSetSubmitResult>>() {
                    @Override
                    public Observable<PsnCrcdVirtualCardFunctionSetSubmitResult> call(String token) {
                        return virtualService.psnCrcdVirtualCardFunctionSetSubmit(ModelUtil.generateVirtualCardFunctionSetParams(getConversationId(), token, model, deviceInfoModel, factorId, randomNums, encryptPasswords));
                    }
                }).compose(SchedulersCompat.<PsnCrcdVirtualCardFunctionSetSubmitResult>applyIoSchedulers())
                .subscribe(new BaseAccountSubscriber<PsnCrcdVirtualCardFunctionSetSubmitResult>() {
                    @Override
                    public void onNext(PsnCrcdVirtualCardFunctionSetSubmitResult result) {
                        clearConversation();
                        updateTransactionView.psnCrcdVirtualCardFunctionSetSubmit(ModelUtil.generateVirtualCardModel(model, result));
                    }
                });
    }
}
