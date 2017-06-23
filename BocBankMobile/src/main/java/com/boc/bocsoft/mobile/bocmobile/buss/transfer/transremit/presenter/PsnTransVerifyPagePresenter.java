package com.boc.bocsoft.mobile.bocmobile.buss.transfer.transremit.presenter;

import android.util.Log;

import com.boc.bocsoft.mobile.bii.bus.global.model.PSNCreatConversation.PSNCreatConversationParams;
import com.boc.bocsoft.mobile.bii.bus.global.model.PSNGetRandom.PSNGetRandomParams;
import com.boc.bocsoft.mobile.bii.bus.global.model.PSNGetTokenId.PSNGetTokenIdParams;
import com.boc.bocsoft.mobile.bii.bus.global.model.PsnGetSecurityFactor.CombinListBean;
import com.boc.bocsoft.mobile.bii.bus.global.model.PsnGetSecurityFactor.PsnGetSecurityFactorParams;
import com.boc.bocsoft.mobile.bii.bus.global.model.PsnGetSecurityFactor.PsnGetSecurityFactorResult;
import com.boc.bocsoft.mobile.bii.bus.global.service.GlobalService;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnDirTransBocNationalTransferVerify.PsnDirTransBocNationalTransferVerifyParams;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnDirTransBocNationalTransferVerify.PsnDirTransBocNationalTransferVerifyResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnDirTransBocTransferSubmit.PsnDirTransBocTransferSubmitParams;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnDirTransBocTransferSubmit.PsnDirTransBocTransferSubmitResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnDirTransBocTransferVerify.PsnDirTransBocTransferVerifyParams;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnDirTransBocTransferVerify.PsnDirTransBocTransferVerifyResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnDirTransCrossBankAddPayee.PsnDirTransCrossBankAddPayeeParams;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnDirTransCrossBankAddPayee.PsnDirTransCrossBankAddPayeeResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnDirTransCrossBankTransfer.PsnDirTransCrossBankTransferParams;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnDirTransCrossBankTransfer.PsnDirTransCrossBankTransferResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnDirTransCrossBankTransferSubmit.PsnDirTransCrossBankTransferSubmitParams;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnDirTransCrossBankTransferSubmit.PsnDirTransCrossBankTransferSubmitResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnDirTransNationalAddPayee.PsnDirTransNationalAddPayeeParams;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnDirTransNationalAddPayee.PsnDirTransNationalAddPayeeResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnDirTransNationalTransferSubmit.PsnDirTransNationalTransferSubmitParams;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnDirTransNationalTransferSubmit.PsnDirTransNationalTransferSubmitResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnEbpsRealTimePaymentConfirm.PsnEbpsRealTimePaymentConfirmParams;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnEbpsRealTimePaymentConfirm.PsnEbpsRealTimePaymentConfirmResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnEbpsRealTimePaymentSavePayee.PsnEbpsRealTimePaymentSavePayeeParams;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnEbpsRealTimePaymentSavePayee.PsnEbpsRealTimePaymentSavePayeeResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnEbpsRealTimePaymentTransfer.PsnEbpsRealTimePaymentTransferParams;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnEbpsRealTimePaymentTransfer.PsnEbpsRealTimePaymentTransferResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnOFAFinanceTransfer.PsnOFAFinanceTransferParams;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnOFAFinanceTransfer.PsnOFAFinanceTransferResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransBocAddPayee.PsnTransBocAddPayeeParams;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransBocAddPayee.PsnTransBocAddPayeeResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransBocNationalTransferVerify.PsnTransBocNationalTransferVerifyParams;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransBocNationalTransferVerify.PsnTransBocNationalTransferVerifyResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransBocTransferSubmit.PsnTransBocTransferSubmitParams;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransBocTransferSubmit.PsnTransBocTransferSubmitResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransBocTransferVerify.PsnTransBocTransferVerifyParams;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransBocTransferVerify.PsnTransBocTransferVerifyResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransLinkTransferSubmit.PsnTransLinkTransferSubmitParams;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransLinkTransferSubmit.PsnTransLinkTransferSubmitResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransNationalAddPayee.PsnTransNationalAddPayeeParams;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransNationalAddPayee.PsnTransNationalAddPayeeResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransNationalChangeBooking.PsnTransNationalChangeBookingParams;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransNationalChangeBooking.PsnTransNationalChangeBookingResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransNationalTransferSubmit.PsnTransNationalTransferSubmitParams;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransNationalTransferSubmit.PsnTransNationalTransferSubmitResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransPayeeListqueryForDim.PsnTransPayeeListqueryForDimParams;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransPayeeListqueryForDim.PsnTransPayeeListqueryForDimResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.service.TransferService;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.base.Exception.BIIBaseSubscriber;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.transremit.model.TransRemitVerifyInfoViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.transremit.ui.TransContract;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.transremit.ui.TransContract.TransPresenterVerifyPage;
import com.boc.bocsoft.mobile.framework.rx.SchedulersCompat;
//import com.boc.bocsoft.mobile.framework.rx.lifecycle.this;
import com.boc.bocsoft.mobile.framework.ui.RxPresenter;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
/**
 * Created by wangyuan on 2016/6/28.
 * 确认页面的Presenter
 */
public class PsnTransVerifyPagePresenter extends RxPresenter implements TransPresenterVerifyPage {

    /**
     * 转账汇款service
     */
    private TransferService transService;
//    private TransContract.TransViewBlankPage mTransBlankView;//转账填写页面
    private TransContract.TransViewVerifyPage mTransConfirmView;//转账确认页面

    /**
     * 公用service
     */
    private GlobalService globalService;

//    private this this;

    public PsnTransVerifyPagePresenter(TransContract.TransViewVerifyPage confirmView){
        mTransConfirmView=confirmView;
        mTransConfirmView.setPresenter(this);
        transService=new TransferService();
        globalService=new GlobalService();
//        this = new this();
    }

    //添加中行收款人
    public void saveBocPayee(final PsnTransBocAddPayeeParams params){
        PSNCreatConversationParams psnCreatConversationParams = new PSNCreatConversationParams();
        globalService.psnCreatConversation(psnCreatConversationParams)
                .compose(this.<String>bindToLifecycle())
                .flatMap(new Func1<String, Observable<String>>() {
                    @Override
                    public Observable<String> call(String conversationId) {
                        PSNGetTokenIdParams psnGetTokenIdParams = new PSNGetTokenIdParams();
                        params.setConversationId(conversationId);
                        psnGetTokenIdParams.setConversationId(conversationId);
                        return globalService.psnGetTokenId(psnGetTokenIdParams);
                    }
                })
                .flatMap(new Func1<String, Observable<PsnTransBocAddPayeeResult>>() {
                    @Override
                    public Observable<PsnTransBocAddPayeeResult> call(String tokenId) {
                        params.setToken(tokenId);
                        return transService.psnTransBocAddPayee(params);
                    }
                })
                .compose(SchedulersCompat.<PsnTransBocAddPayeeResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnTransBocAddPayeeResult>() {
                    @Override
                    public void commonHandleException(BiiResultErrorException biiResultErrorException) {

                    }
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        mTransConfirmView.addBocPayeeFailed(biiResultErrorException);
                    }
                    @Override
                    public void onCompleted() {
                    }
                    @Override
                    public void onNext(PsnTransBocAddPayeeResult psnTransBocAddPayeeResult) {
                        mTransConfirmView.addBocPayeeSuccess(psnTransBocAddPayeeResult);
                    }
                });
    }

    //添加跨行收款人
    public void saveNationalPayee(final PsnTransNationalAddPayeeParams params){
        PSNCreatConversationParams psnCreatConversationParams = new PSNCreatConversationParams();
        globalService.psnCreatConversation(psnCreatConversationParams)
                .compose(this.<String>bindToLifecycle())
                .flatMap(new Func1<String, Observable<String>>() {
                    @Override
                    public Observable<String> call(String conversationId) {
                        PSNGetTokenIdParams psnGetTokenIdParams = new PSNGetTokenIdParams();
                        params.setConversationId(conversationId);
                        psnGetTokenIdParams.setConversationId(conversationId);
                        return globalService.psnGetTokenId(psnGetTokenIdParams);
                    }
                })
                .flatMap(new Func1<String, Observable<PsnTransNationalAddPayeeResult>>() {
                    @Override
                    public Observable<PsnTransNationalAddPayeeResult> call(String tokenId) {
                        params.setToken(tokenId);
                        return transService.psnTransNationalAddPayee(params);
                    }
                })
                .compose(SchedulersCompat.<PsnTransNationalAddPayeeResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnTransNationalAddPayeeResult>() {
                    @Override
                    public void commonHandleException(BiiResultErrorException biiResultErrorException) {

                    }

                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        mTransConfirmView.addNationalPayeeFailed(biiResultErrorException);
                    }
                    @Override
                    public void onCompleted() {
                    }
                    @Override
                    public void onNext(PsnTransNationalAddPayeeResult psnTransNationalAddPayeeResult) {
                        mTransConfirmView.addNationalPayeeSuccess(psnTransNationalAddPayeeResult);
                    }
                });
    }
    //添加跨行收款人
    public void saveNationalRealtimePayee(final PsnEbpsRealTimePaymentSavePayeeParams params){
        PSNCreatConversationParams psnCreatConversationParams = new PSNCreatConversationParams();
        globalService.psnCreatConversation(psnCreatConversationParams)
                .compose(this.<String>bindToLifecycle())
                .flatMap(new Func1<String, Observable<String>>() {
                    @Override
                    public Observable<String> call(String conversationId) {
                        PSNGetTokenIdParams psnGetTokenIdParams = new PSNGetTokenIdParams();
                        params.setConversationId(conversationId);
                        psnGetTokenIdParams.setConversationId(conversationId);
                        return globalService.psnGetTokenId(psnGetTokenIdParams);
                    }
                })
                .flatMap(new Func1<String, Observable<PsnEbpsRealTimePaymentSavePayeeResult>>() {
                    @Override
                    public Observable<PsnEbpsRealTimePaymentSavePayeeResult> call(String tokenId) {
                        params.setToken(tokenId);
                        return transService.psnEbpsRealTimePaymentSavePayee(params);
                    }
                })
                .compose(SchedulersCompat.<PsnEbpsRealTimePaymentSavePayeeResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnEbpsRealTimePaymentSavePayeeResult>() {
                    @Override
                    public void commonHandleException(BiiResultErrorException biiResultErrorException) {
                    }
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        mTransConfirmView.addNationalPayeeFailed(biiResultErrorException);
                    }
                    @Override
                    public void onCompleted() {
                    }
                    @Override
                    public void onNext(PsnEbpsRealTimePaymentSavePayeeResult psnTransNationalAddPayeeResult) {
                        mTransConfirmView.addNationalRealtimePayeeSuccess(psnTransNationalAddPayeeResult);
                    }
                });
    }
    //添加跨行定向收款人
    public void saveDirNationalPayee(final PsnDirTransNationalAddPayeeParams params){
        PSNCreatConversationParams psnCreatConversationParams = new PSNCreatConversationParams();
        globalService.psnCreatConversation(psnCreatConversationParams)
                .compose(this.<String>bindToLifecycle())
                .flatMap(new Func1<String, Observable<String>>() {
                    @Override
                    public Observable<String> call(String conversationId) {
                        PSNGetTokenIdParams psnGetTokenIdParams = new PSNGetTokenIdParams();
                        params.setConversationId(conversationId);
                        psnGetTokenIdParams.setConversationId(conversationId);
                        return globalService.psnGetTokenId(psnGetTokenIdParams);
                    }
                })
                .flatMap(new Func1<String, Observable<PsnDirTransNationalAddPayeeResult>>() {
                    @Override
                    public Observable<PsnDirTransNationalAddPayeeResult> call(String tokenId) {
                        params.setToken(tokenId);
                        return transService.psnDirTransNationalAddPayee(params);
                    }
                })
                .compose(SchedulersCompat.<PsnDirTransNationalAddPayeeResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnDirTransNationalAddPayeeResult>() {
                    @Override
                    public void commonHandleException(BiiResultErrorException biiResultErrorException) {
                    }
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        mTransConfirmView.addNationalPayeeFailed(biiResultErrorException);
                    }
                    @Override
                    public void onCompleted() {
                    }
                    @Override
                    public void onNext(PsnDirTransNationalAddPayeeResult psnDirTransNationalAddPayeeResult) {
                        mTransConfirmView.addDirNationalPayeeSuccess(psnDirTransNationalAddPayeeResult);
                    }
                });
    }

    //添加跨行定向收款人
    public void saveDirRealtimeNationalPayee(final PsnDirTransCrossBankAddPayeeParams params){
        PSNCreatConversationParams psnCreatConversationParams = new PSNCreatConversationParams();
        globalService.psnCreatConversation(psnCreatConversationParams)
                .compose(this.<String>bindToLifecycle())
                .flatMap(new Func1<String, Observable<String>>() {
                    @Override
                    public Observable<String> call(String conversationId) {
                        PSNGetTokenIdParams psnGetTokenIdParams = new PSNGetTokenIdParams();
                        params.setConversationId(conversationId);
                        psnGetTokenIdParams.setConversationId(conversationId);
                        return globalService.psnGetTokenId(psnGetTokenIdParams);
                    }
                })
                .flatMap(new Func1<String, Observable<PsnDirTransCrossBankAddPayeeResult>>() {
                    @Override
                    public Observable<PsnDirTransCrossBankAddPayeeResult> call(String tokenId) {
                        params.setToken(tokenId);
                        return transService.psnDirTransCrossBankAddPayee(params);
                    }
                })
                .compose(SchedulersCompat.<PsnDirTransCrossBankAddPayeeResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnDirTransCrossBankAddPayeeResult>() {
                    @Override
                    public void commonHandleException(BiiResultErrorException biiResultErrorException) {
                    }
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        mTransConfirmView.addNationalPayeeFailed(biiResultErrorException);
                    }
                    @Override
                    public void onCompleted() {
                    }
                    @Override
                    public void onNext(PsnDirTransCrossBankAddPayeeResult psnDirTransCrossBankAddPayeeResult) {
                        mTransConfirmView.addDirNationalRealtimePayeeSuccess(psnDirTransCrossBankAddPayeeResult);
                    }
                });
    }

    /**
     * 关联账户转账提交
     */

    @Override
    public void transLinkTransferSubmit(final TransRemitVerifyInfoViewModel dataModel) {
        final PsnTransLinkTransferSubmitParams linkSubmitParams=new PsnTransLinkTransferSubmitParams();
        linkSubmitParams.setAmount(dataModel.getAmount());
        linkSubmitParams.setCurrency(dataModel.getCurrency());
        linkSubmitParams.setPayeeId(dataModel.getPayeeId());
        linkSubmitParams.setCashRemit(dataModel.getCashRemit());
        linkSubmitParams.setFromAccountId(dataModel.getFromAccountId());
        linkSubmitParams.setPayeeName(dataModel.getPayeeName());
        linkSubmitParams.setToAccountId(dataModel.getToAccountId());
        linkSubmitParams.setPayeeActno(dataModel.getPayeeActno());
//        linkSubmitParams.setExecuteType("0");
        linkSubmitParams.setRemark(dataModel.getRemark());
//        linkSubmitParams.setToAccountId("183159779");
        linkSubmitParams.setToAccountId(dataModel.getToAccountId());
        linkSubmitParams.setExecuteType(dataModel.getExecuteType());
//        linkSubmitParams.setDevicePrint(dataModel.getDevicePrint());
//        if ("1".equals(dataModel.getExecuteType())){
//            linkSubmitParams.setDueDate(dataModel.getExecuteDate());
//            linkSubmitParams.setExecuteDate(dataModel.getExecuteDate());
//        }

        PSNCreatConversationParams psnCreatConversationParams = new PSNCreatConversationParams();
//        .compose(mthis.<String>bindToLifecycle())
        globalService.psnCreatConversation(psnCreatConversationParams)
                .compose(this.<String>bindToLifecycle())
                .flatMap(new Func1<String, Observable<String>>() {
                    @Override
                    public Observable<String> call(String conversationId) {
                        PSNGetTokenIdParams psnGetTokenIdParams = new PSNGetTokenIdParams();
                        linkSubmitParams.setConversationId(conversationId);
                        psnGetTokenIdParams.setConversationId(conversationId);
                        return globalService.psnGetTokenId(psnGetTokenIdParams);
                    }
                })
                .flatMap(new Func1<String, Observable<PsnTransLinkTransferSubmitResult>>() {
                    @Override
                    public Observable<PsnTransLinkTransferSubmitResult> call(String tokenId) {
                        linkSubmitParams.setToken(tokenId);
                        return transService.psnTransLinkTransferSubmit(linkSubmitParams);
                    }
                })
                .compose(SchedulersCompat.<PsnTransLinkTransferSubmitResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnTransLinkTransferSubmitResult>() {
            @Override
            public void handleException(BiiResultErrorException biiResultErrorException) {
                mTransConfirmView.transLinkTransferSubmitFailed(biiResultErrorException);
            }
            @Override
            public void onCompleted() {
            }
            @Override
            public void onNext(PsnTransLinkTransferSubmitResult psnTransLinkTransferSubmitResult) {
                mTransConfirmView.transLinkTransferSubmitSuccess(psnTransLinkTransferSubmitResult);
            }
        });
    }
    /**
     * 资金划转提交
     */

    @Override
    public void transPsnOFAFinanceSubmit(final TransRemitVerifyInfoViewModel dataModel) {
        final PsnOFAFinanceTransferParams ofaSubmitParams=new PsnOFAFinanceTransferParams();
        ofaSubmitParams.setAmount(dataModel.getAmount());
        ofaSubmitParams.setCashRemit(dataModel.getCashRemit());
        ofaSubmitParams.setCurrency(dataModel.getCurrency());
        ofaSubmitParams.setFinancialAccountId(dataModel.getFromAccountId());
        ofaSubmitParams.setBankAccountId(dataModel.getToAccountId());
        PSNGetTokenIdParams mTokenIdParams = new PSNGetTokenIdParams();
        mTokenIdParams.setConversationId(dataModel.getConversationId());
        PSNCreatConversationParams psnCreatConversationParams = new PSNCreatConversationParams();
        globalService.psnCreatConversation(psnCreatConversationParams)
                .compose(this.<String>bindToLifecycle())
                .flatMap(new Func1<String, Observable<String>>() {
                    @Override
                    public Observable<String> call(String conversationId) {
                        PSNGetTokenIdParams psnGetTokenIdParams = new PSNGetTokenIdParams();
                        psnGetTokenIdParams.setConversationId(conversationId);
                        ofaSubmitParams.setConversationId(conversationId);
                        return globalService.psnGetTokenId(psnGetTokenIdParams);
                    }
                })
                .flatMap(new Func1<String, Observable<PsnOFAFinanceTransferResult>>() {
                    @Override
                    public Observable<PsnOFAFinanceTransferResult> call(String tokenId) {
                        ofaSubmitParams.setToken(tokenId);
                        return transService.psnOFAFinanceTransfer(ofaSubmitParams);
                    }
                })
                .compose(SchedulersCompat.<PsnOFAFinanceTransferResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnOFAFinanceTransferResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        mTransConfirmView.transPsnOFAFinanceSubmitFailed(biiResultErrorException);
                    }
                    @Override
                    public void onCompleted() {
                    }
                    @Override
                    public void onNext(PsnOFAFinanceTransferResult result) {
                        mTransConfirmView.transPsnOFAFinanceSubmitSuccess(result);
                    }
                });
    }


    /**
     * 行内转账提交交易
     */

    @Override
    public void transBocSubmit( TransRemitVerifyInfoViewModel dataModel) {
        final PsnTransBocTransferSubmitParams bocSubmitParams=new PsnTransBocTransferSubmitParams();
        bocSubmitParams.setAmount(dataModel.getAmount());
        bocSubmitParams.setCurrency(dataModel.getCurrency());
        bocSubmitParams.setRemark(dataModel.getRemark());
        bocSubmitParams.setFromAccountId(dataModel.getFromAccountId());
        bocSubmitParams.setOtp(dataModel.getOtp());
        bocSubmitParams.setOtp_RC(dataModel.getOtp_RC());
        bocSubmitParams.setSmc(dataModel.getSmc());
        bocSubmitParams.setSmc_RC(dataModel.getSmc_RC());
        bocSubmitParams.setPayeeActno(dataModel.getPayeeActno());
        bocSubmitParams.setPayeeBankNum(dataModel.getPayeeBankNum());
        bocSubmitParams.setPayeeName(dataModel.getPayeeName());
        bocSubmitParams.setPayeeId(null==dataModel.getPayeeId()?"":dataModel.getPayeeId());
        bocSubmitParams.setPayeeMobile(dataModel.getPayeeMobile());
        bocSubmitParams.setToAccountType(dataModel.getToAccountType());
        bocSubmitParams.setAtmPassword(dataModel.getAtmPassword());
        bocSubmitParams.setExecuteType(dataModel.getExecuteType());
        bocSubmitParams.setPhoneBankPassword(dataModel.getPhoneBankPassword());
        bocSubmitParams.setConversationId(dataModel.getConversationId());
        bocSubmitParams.setDevicePrint(dataModel.getDevicePrint());
        bocSubmitParams.setActiv(dataModel.getActiv());
        bocSubmitParams.setState(dataModel.getState());
        if ("96".equals(dataModel.get_combinId())){
            bocSubmitParams.setDeviceInfo(dataModel.getDeviceInfo());
            bocSubmitParams.setDeviceInfo_RC(dataModel.getDeviceInfo_RC());
        }
        bocSubmitParams.set_signedData(dataModel.get_signedData());
        if ("1".equals(dataModel.getExecuteType())){
            bocSubmitParams.setDueDate(dataModel.getExecuteDate());
            bocSubmitParams.setExecuteDate(dataModel.getExecuteDate());
        }else{
            bocSubmitParams.setDueDate("");
            bocSubmitParams.setExecuteDate(null);
        }
        if ("1".equals(dataModel.getNeedPassword())){
            bocSubmitParams.setAtmPassword(dataModel.getAtmPassword());
            bocSubmitParams.setAtmPassword_RC(dataModel.getAtmPassword_RC());
        }
        if ("2".equals(dataModel.getNeedPassword())){
            bocSubmitParams.setPhoneBankPassword(dataModel.getPhoneBankPassword());
            bocSubmitParams.setPhoneBankPassword_RC(dataModel.getPhoneBankPassword_RC());
        }
        if ("3".equals(dataModel.getNeedPassword())){
            bocSubmitParams.setPassbookPassword(dataModel.getPassbookPassword());
            bocSubmitParams.setPassbookPassword_RC(dataModel.getPassbookPassword_RC());
        }
        PSNGetTokenIdParams mTokenIdParams = new PSNGetTokenIdParams();
        mTokenIdParams.setConversationId(dataModel.getConversationId());
        globalService.psnGetTokenId(mTokenIdParams)
                .compose(this.<String>bindToLifecycle())
                .flatMap(new Func1<String, Observable<PsnTransBocTransferSubmitResult>>() {
                    @Override
                    public Observable<PsnTransBocTransferSubmitResult> call(String tokenId) {
                        bocSubmitParams.setToken(tokenId);
                        return transService.psnTransBocTransferSubmit(bocSubmitParams);
                    }
                })
                .compose(SchedulersCompat.<PsnTransBocTransferSubmitResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnTransBocTransferSubmitResult>() {
            @Override
            public void handleException(BiiResultErrorException biiResultErrorException) {
                mTransConfirmView.transBocSubmitFailed(biiResultErrorException);
//                this.onDestroy();//必须注释掉
            }

            @Override
            public void onCompleted() {
//                this.onDestroy();//必须注释掉
            }
            @Override
            public void onNext(PsnTransBocTransferSubmitResult psnTransBocTransferSubmitResult) {
                mTransConfirmView.transBocSubmitSuccess(psnTransBocTransferSubmitResult);
            }
        });
    }
    @Override
    public void transNationalRealTimeSubmit(final TransRemitVerifyInfoViewModel dataModel) {
        final PsnEbpsRealTimePaymentTransferParams nationalRealtimeSubmitParams=new PsnEbpsRealTimePaymentTransferParams();
        nationalRealtimeSubmitParams.setPayeeActno(dataModel.getPayeeActno());
        nationalRealtimeSubmitParams.setPayeeName(dataModel.getPayeeName());
        nationalRealtimeSubmitParams.setAmount(dataModel.getAmount());
        nationalRealtimeSubmitParams.setCurrency(dataModel.getCurrency());
        nationalRealtimeSubmitParams.setExecuteType(dataModel.getExecuteType());
        nationalRealtimeSubmitParams.setFromAccountId(dataModel.getFromAccountId());
        nationalRealtimeSubmitParams.setMemo(dataModel.getMemo());
        nationalRealtimeSubmitParams.setPayeeBankName(dataModel.getPayeeBankName());
        nationalRealtimeSubmitParams.setPayeeCnaps(dataModel.getPayeeCnaps());//不是cnapscode字段
        nationalRealtimeSubmitParams.setPayeeMobile(dataModel.getPayeeMobile());
        nationalRealtimeSubmitParams.setSendMsgFlag(dataModel.getSendMsgFlag());
        nationalRealtimeSubmitParams.setPayeeOrgName(dataModel.getPayeeBankName());
        nationalRealtimeSubmitParams.setConversationId(dataModel.getConversationId());
        nationalRealtimeSubmitParams.setSmc(dataModel.getSmc());
        nationalRealtimeSubmitParams.setSmc_RC(dataModel.getSmc_RC());
        nationalRealtimeSubmitParams.setOtp_RC(dataModel.getOtp_RC());
        nationalRealtimeSubmitParams.setOtp(dataModel.getOtp());
        nationalRealtimeSubmitParams.setDevicePrint(dataModel.getDevicePrint());
        if ("96".equals(dataModel.get_combinId())){
            nationalRealtimeSubmitParams.setDeviceInfo(dataModel.getDeviceInfo());
            nationalRealtimeSubmitParams.setDeviceInfo_RC(dataModel.getDeviceInfo_RC());
        }
        nationalRealtimeSubmitParams.setState(dataModel.getState());
        nationalRealtimeSubmitParams.setActiv(dataModel.getActiv());
        nationalRealtimeSubmitParams.set_signedData(dataModel.get_signedData());
        if ("1".equals(dataModel.getExecuteType())){
            nationalRealtimeSubmitParams.setDueDate(dataModel.getExecuteDate());
            nationalRealtimeSubmitParams.setExecuteDate(dataModel.getExecuteDate());
        }else{
            nationalRealtimeSubmitParams.setDueDate("");
            nationalRealtimeSubmitParams.setExecuteDate(null);
        }

        if ("1".equals(dataModel.getNeedPassword())){
            nationalRealtimeSubmitParams.setAtmPassword(dataModel.getAtmPassword());
            nationalRealtimeSubmitParams.setAtmPassword_RC(dataModel.getAtmPassword_RC());
        }
        if ("2".equals(dataModel.getNeedPassword())){
            nationalRealtimeSubmitParams.setPhoneBankPassword(dataModel.getPhoneBankPassword());
            nationalRealtimeSubmitParams.setPhoneBankPassword_RC(dataModel.getPhoneBankPassword_RC());
        }
        if ("3".equals(dataModel.getNeedPassword())){
            nationalRealtimeSubmitParams.setPassbookPassword(dataModel.getPassbookPassword());
            nationalRealtimeSubmitParams.setPassbookPassword_RC(dataModel.getPassbookPassword_RC());
        }
        PSNGetTokenIdParams mTokenIdParams = new PSNGetTokenIdParams();
        mTokenIdParams.setConversationId(dataModel.getConversationId());
        globalService.psnGetTokenId(mTokenIdParams)
                .compose(this.<String>bindToLifecycle())
                .flatMap(new Func1<String, Observable<PsnEbpsRealTimePaymentTransferResult>>() {
                    @Override
                    public Observable<PsnEbpsRealTimePaymentTransferResult> call(String tokenId) {
                        nationalRealtimeSubmitParams.setToken(tokenId);
                        return transService.psnTransNationalRealtimeSubmit(nationalRealtimeSubmitParams);
                    }
                })
                .compose(SchedulersCompat.<PsnEbpsRealTimePaymentTransferResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnEbpsRealTimePaymentTransferResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        mTransConfirmView.transBocSubmitFailed(biiResultErrorException);
//                        this.onDestroy();
                    }

                    @Override
                    public void onCompleted() {
//                        this.onDestroy();
                    }
                    @Override
                    public void onNext(PsnEbpsRealTimePaymentTransferResult psnTransBocTransferSubmitResult) {
                        mTransConfirmView.transNationalRealTimeSubmitSuccess(psnTransBocTransferSubmitResult);
                    }
                });
    }

    /**
     * 根据提交交易返回结果，如需，转账加强认证交易
     */
//    @Override
//    public void transBocSubmitReinforce() {
//
//    }
    /**
     * 定向转账提交交易
     */
    @Override
    public void transDirBocSubmit(final TransRemitVerifyInfoViewModel dataModel) {
        final PsnDirTransBocTransferSubmitParams dirBocParams=new PsnDirTransBocTransferSubmitParams();
        dirBocParams.setAmount(dataModel.getAmount());
        dirBocParams.setCurrency(dataModel.getCurrency());
        dirBocParams.setExecuteType(dataModel.getExecuteType());
        dirBocParams.setFromAccountId(dataModel.getFromAccountId());
        dirBocParams.setPayeeActno(dataModel.getPayeeActno());
        dirBocParams.setPayeeName(dataModel.getPayeeName());
        dirBocParams.setPayeeMobile(dataModel.getPayeeMobile());
        dirBocParams.setPayeeBankNum(dataModel.getPayeeBankNum());
        dirBocParams.setPayeeId(null==dataModel.getPayeeId()?"":dataModel.getPayeeId());
        dirBocParams.setRemark(dataModel.getRemark());
        dirBocParams.setToAccountType(dataModel.getToAccountType());
        dirBocParams.setSmc(dataModel.getSmc());
        dirBocParams.setSmc_RC(dataModel.getSmc_RC());
        dirBocParams.setOtp_RC(dataModel.getOtp_RC());
        dirBocParams.setOtp(dataModel.getOtp());
        dirBocParams.setAtmPassword(dataModel.getAtmPassword());
        dirBocParams.setAtmPassword_RC(dataModel.getAtmPassword_RC());
        dirBocParams.setPhoneBankPassword(dataModel.getPhoneBankPassword());
        dirBocParams.setPhoneBankPassword_RC(dataModel.getPhoneBankPassword_RC());
        dirBocParams.setDevicePrint(dataModel.getDevicePrint());
        dirBocParams.setConversationId(dataModel.getConversationId());

        if ("96".equals(dataModel.get_combinId())){
            dirBocParams.setDeviceInfo(dataModel.getDeviceInfo());
            dirBocParams.setDeviceInfo_RC(dataModel.getDeviceInfo_RC());
        }
        dirBocParams.setActiv(dataModel.getActiv());
        dirBocParams.setState(dataModel.getState());
        dirBocParams.set_signedData(dataModel.get_signedData());
        if ("1".equals(dataModel.getExecuteType())){
            dirBocParams.setDueDate(dataModel.getExecuteDate());
            dirBocParams.setExecuteDate(dataModel.getExecuteDate());
        }else{
            dirBocParams.setDueDate("");
            dirBocParams.setExecuteDate(null);
        }

        PSNGetTokenIdParams mTokenIdParams = new PSNGetTokenIdParams();

        mTokenIdParams.setConversationId(dataModel.getConversationId());
        globalService.psnGetTokenId(mTokenIdParams)
                .compose(this.<String>bindToLifecycle())
                .flatMap(new Func1<String, Observable<PsnDirTransBocTransferSubmitResult>>() {
                    @Override
                    public Observable<PsnDirTransBocTransferSubmitResult> call(String tokenId) {
                        // TODO: 2016/7/9  是否需要上送atm密码，和电话银行密码要看实际情况
                        dirBocParams.setToken(tokenId);
                        return transService.psndirTransBocTransferSubmit(dirBocParams);
                    }
                })
               .compose(SchedulersCompat.<PsnDirTransBocTransferSubmitResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnDirTransBocTransferSubmitResult>() {
            @Override
            public void handleException(BiiResultErrorException biiResultErrorException) {
                mTransConfirmView.transBocSubmitFailed(biiResultErrorException);
            }
            @Override
            public void onCompleted() {
            }
            @Override
            public void onNext(PsnDirTransBocTransferSubmitResult psnTransBocTransferSubmitResult) {
                mTransConfirmView.transBocDirSubmitSuccess(psnTransBocTransferSubmitResult);
            }
        });
}
    /**
     * 根据提交交易返回结果，如需，定向转账加强认证交易
     */
    @Override
    public void transDirBocSumbmitReinfore() {

    }

    @Override
    public void updatePayeeList() {
        PsnTransPayeeListqueryForDimParams params = new PsnTransPayeeListqueryForDimParams();
        String[] bocFlag = {"0", "1", "3"};
        params.setBocFlag(bocFlag);
        params.setIsAppointed(""); // 是否定向收款人(0：非定向 1：定向 输入为空时（""）查全部)
        params.setPayeeName("");
        params.setCurrentIndex("0");
        params.setPageSize("500");

        transService.psnTransPayeeListqueryForDim(params)
                .compose(this.<PsnTransPayeeListqueryForDimResult>bindToLifecycle())
                .compose(SchedulersCompat.<PsnTransPayeeListqueryForDimResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnTransPayeeListqueryForDimResult>() {
                    @Override
                    public void commonHandleException(BiiResultErrorException biiResultErrorException) {
                    }
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        mTransConfirmView.updatePayeeListFailed(biiResultErrorException);
                    }
                    @Override
                    public void onCompleted() {
                    }
                    @Override
                    public void onNext(PsnTransPayeeListqueryForDimResult result) {
                        mTransConfirmView.updatePayeeListSuccess(result);
                    }
                });
    }

    @Override
    public void transNationalChangeBooking(String conversationId,String transType) {
        final PsnTransNationalChangeBookingParams changeBookingParams=new
                PsnTransNationalChangeBookingParams();
        changeBookingParams.setConversationId(conversationId);
        changeBookingParams.setTransType(transType);
        PSNGetTokenIdParams mTokenIdParams = new PSNGetTokenIdParams();
        mTokenIdParams.setConversationId(conversationId);
        globalService.psnGetTokenId(mTokenIdParams)
                .compose(this.<String>bindToLifecycle())
                .flatMap(new Func1<String, Observable<PsnTransNationalChangeBookingResult>>() {
                    @Override
                    public Observable<PsnTransNationalChangeBookingResult> call(String tokenId) {
                        changeBookingParams.setToken(tokenId);
                        return transService.psnTransChangeBooking(changeBookingParams);
                    }
                })
                .compose(SchedulersCompat.<PsnTransNationalChangeBookingResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnTransNationalChangeBookingResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        mTransConfirmView.transBocSubmitFailed(biiResultErrorException);
                    }
                    @Override
                    public void onCompleted() {
                    }
                    @Override
                    public void onNext(PsnTransNationalChangeBookingResult result) {
                        mTransConfirmView.transNationalChangeBookingSuccess(result);
                    }
                });
    }

    @Override
    public void transDirNationalSubmit(TransRemitVerifyInfoViewModel dataModel) {
        final PsnDirTransNationalTransferSubmitParams  dirNationalSubmitParams=
                new PsnDirTransNationalTransferSubmitParams();
        dirNationalSubmitParams.setAmount(dataModel.getAmount());
        dirNationalSubmitParams.setAccountNumber(dataModel.getAccountNumber());
        dirNationalSubmitParams.setAccountType(dataModel.getAccountType());
        dirNationalSubmitParams.setAccountIbkNum(dataModel.getAccountIbkNum());
        dirNationalSubmitParams.setNickName(dataModel.getNickName());
        dirNationalSubmitParams.setBankName(dataModel.getBankName());
        dirNationalSubmitParams.setCurrency(dataModel.getCurrency());
        dirNationalSubmitParams.setCnapsCode(dataModel.getCnapsCode());
        dirNationalSubmitParams.setExecuteType(dataModel.getExecuteType());
        dirNationalSubmitParams.setFromAccountId(dataModel.getFromAccountId());
        dirNationalSubmitParams.setAccountIbkNum(dataModel.getAccountIbkNum());
        dirNationalSubmitParams.setOtp(dataModel.getOtp());
        dirNationalSubmitParams.setOtp_RC(dataModel.getOtp_RC());
        dirNationalSubmitParams.setOpenChangeBooking("O");
        dirNationalSubmitParams.setSmc(dataModel.getSmc());
        dirNationalSubmitParams.setSmc_RC(dataModel.getSmc_RC());
        dirNationalSubmitParams.setPayeeName(dataModel.getPayeeName());
        dirNationalSubmitParams.setPayeeMobile(dataModel.getPayeeMobile());
        dirNationalSubmitParams.setPayeeId(null==dataModel.getPayeeId()?"":dataModel.getPayeeId());
        dirNationalSubmitParams.setPayeeActno(dataModel.getPayeeActno());
        dirNationalSubmitParams.setPayeeNickName(dataModel.getPayeeNickName());
        dirNationalSubmitParams.setPayeeType("-");
        dirNationalSubmitParams.setRemark(dataModel.getRemark());
        dirNationalSubmitParams.setDevicePrint(dataModel.getDevicePrint());
        dirNationalSubmitParams.setSaveAsPayeeYn(dataModel.isSaveAsPayeeYn());
        dirNationalSubmitParams.setSendmessageYn(dataModel.isSendmessageYn());
        dirNationalSubmitParams.setToOrgName(dataModel.getToOrgName());
        dirNationalSubmitParams.setConversationId(dataModel.getConversationId());
        dirNationalSubmitParams.set_combinId(dataModel.get_combinId());

        if ("96".equals(dataModel.get_combinId())){
            dirNationalSubmitParams.setDeviceInfo(dataModel.getDeviceInfo());
            dirNationalSubmitParams.setDeviceInfo_RC(dataModel.getDeviceInfo_RC());
        }
        dirNationalSubmitParams.setActiv(dataModel.getActiv());
        dirNationalSubmitParams.setState(dataModel.getState());
        dirNationalSubmitParams.set_signedData(dataModel.get_signedData());
        if ("1".equals(dataModel.getExecuteType())){
            dirNationalSubmitParams.setDueDate(dataModel.getExecuteDate());
            dirNationalSubmitParams.setExecuteDate(dataModel.getExecuteDate());
        }else{
            dirNationalSubmitParams.setDueDate("");
            dirNationalSubmitParams.setExecuteDate(null);
        }

//        dirNationalSubmitParams.set_combinId();
        PSNGetTokenIdParams mTokenIdParams = new PSNGetTokenIdParams();
        mTokenIdParams.setConversationId(dataModel.getConversationId());
        globalService.psnGetTokenId(mTokenIdParams)
                .compose(this.<String>bindToLifecycle())
                .flatMap(new Func1<String, Observable<PsnDirTransNationalTransferSubmitResult>>() {
                    @Override
                    public Observable<PsnDirTransNationalTransferSubmitResult> call(String tokenId) {
                        // TODO: 2016/7/9  是否需要上送atm密码，和电话银行密码要看实际情况
                        dirNationalSubmitParams.setToken(tokenId);
                        return transService.psnDirTransNationalSubmit(dirNationalSubmitParams);
                    }
                })
                .compose(SchedulersCompat.<PsnDirTransNationalTransferSubmitResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnDirTransNationalTransferSubmitResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        mTransConfirmView.transBocSubmitFailed(biiResultErrorException);
                    }
                    @Override
                    public void onCompleted() {
                    }
                    @Override
                    public void onNext(PsnDirTransNationalTransferSubmitResult psnDirTransBocTransferSubmitResult) {
                        mTransConfirmView.transDirNationalSubmitSuccess(psnDirTransBocTransferSubmitResult);
                    }
                });
    }
    @Override
    public void transDirNationalSubmitReinfore() {

    }

    @Override
    public void transNationalSubmit(TransRemitVerifyInfoViewModel dataModel) {
        final PsnTransNationalTransferSubmitParams nationalTransferSubmitParams
                =new PsnTransNationalTransferSubmitParams();
        nationalTransferSubmitParams.setAmount(dataModel.getAmount());
        nationalTransferSubmitParams.setBankName(dataModel.getBankName());
        nationalTransferSubmitParams.setCurrency(dataModel.getCurrency());
        nationalTransferSubmitParams.setCnapsCode(dataModel.getCnapsCode());
        nationalTransferSubmitParams.setExecuteType(dataModel.getExecuteType());
        nationalTransferSubmitParams.setFromAccountId(dataModel.getFromAccountId());
        nationalTransferSubmitParams.setOtp(dataModel.getOtp());
        nationalTransferSubmitParams.setOtp_RC(dataModel.getOtp_RC());
        nationalTransferSubmitParams.setOpenChangeBooking("O");
        nationalTransferSubmitParams.setSmc(dataModel.getSmc());
        nationalTransferSubmitParams.setSmc_RC(dataModel.getSmc_RC());
        nationalTransferSubmitParams.setPayeeName(dataModel.getPayeeName());
        nationalTransferSubmitParams.setPayeeMobile(dataModel.getPayeeMobile());
        nationalTransferSubmitParams.setPayeeId(null==dataModel.getPayeeId()?"":dataModel.getPayeeId());
        nationalTransferSubmitParams.setPayeeActno(dataModel.getPayeeActno());
        nationalTransferSubmitParams.setPayeeNickName(dataModel.getPayeeNickName());
        nationalTransferSubmitParams.setPayeeNickName(dataModel.getPayeeNickName());
        nationalTransferSubmitParams.setPayeeType(dataModel.getPayeeType());
        nationalTransferSubmitParams.setRemark(dataModel.getRemark());
        nationalTransferSubmitParams.setSaveAsPayeeYn(dataModel.isSaveAsPayeeYn());
        nationalTransferSubmitParams.setSendmessageYn(dataModel.isSendmessageYn());
        nationalTransferSubmitParams.setToOrgName(dataModel.getToOrgName());
        nationalTransferSubmitParams.setDevicePrint(dataModel.getDevicePrint());
        nationalTransferSubmitParams.setConversationId(dataModel.getConversationId());
        nationalTransferSubmitParams.setActiv(dataModel.getActiv());
        if ("96".equals(dataModel.get_combinId())){
            nationalTransferSubmitParams.setDeviceInfo(dataModel.getDeviceInfo());
            nationalTransferSubmitParams.setDeviceInfo_RC(dataModel.getDeviceInfo_RC());
        }
        nationalTransferSubmitParams.setState(dataModel.getState());
        nationalTransferSubmitParams.set_combinId(dataModel.get_combinId());
        nationalTransferSubmitParams.set_signedData(dataModel.get_signedData());
        if ("1".equals(dataModel.getExecuteType())){
            nationalTransferSubmitParams.setDueDate(dataModel.getExecuteDate());
            nationalTransferSubmitParams.setExecuteDate(dataModel.getExecuteDate());
        }else{
            nationalTransferSubmitParams.setDueDate("");
            nationalTransferSubmitParams.setExecuteDate(null);
        }

        if ("1".equals(dataModel.getNeedPassword())){
            nationalTransferSubmitParams.setAtmPassword(dataModel.getAtmPassword());
            nationalTransferSubmitParams.setAtmPassword_RC(dataModel.getAtmPassword_RC());
        }
        if ("2".equals(dataModel.getNeedPassword())){
            nationalTransferSubmitParams.setPhoneBankPassword(dataModel.getPhoneBankPassword());
            nationalTransferSubmitParams.setPhoneBankPassword_RC(dataModel.getPhoneBankPassword_RC());
        }
        if ("3".equals(dataModel.getNeedPassword())){
            nationalTransferSubmitParams.setPassbookPassword(dataModel.getPassbookPassword());
            nationalTransferSubmitParams.setPassbookPassword_RC(dataModel.getPassbookPassword_RC());
        }
//        nationalTransferSubmitParams.set_combinId();
        PSNGetTokenIdParams mTokenIdParams = new PSNGetTokenIdParams();
        mTokenIdParams.setConversationId(dataModel.getConversationId());
        globalService.psnGetTokenId(mTokenIdParams)
                .compose(this.<String>bindToLifecycle())
                .flatMap(new Func1<String, Observable<PsnTransNationalTransferSubmitResult>>() {
                    @Override
                    public Observable<PsnTransNationalTransferSubmitResult> call(String tokenId) {
                        // TODO: 2016/7/9  是否需要上送atm密码，和电话银行密码要看实际情况
                        nationalTransferSubmitParams.setToken(tokenId);
                        return transService.psnTransNationalSubmit(nationalTransferSubmitParams);
                    }
                })
                .compose(SchedulersCompat.<PsnTransNationalTransferSubmitResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnTransNationalTransferSubmitResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        mTransConfirmView.transBocSubmitFailed(biiResultErrorException);
                    }
                    @Override
                    public void onCompleted() {
                    }
                    @Override
                    public void onNext(PsnTransNationalTransferSubmitResult psnTransBocTransferSubmitResult) {
                        mTransConfirmView.transNationalSubmitSuccess(psnTransBocTransferSubmitResult);
                    }
                });

    }

//    @Override
//    public void transNationalSubmitReinforce() {
//
//    }

    @Override
    public void transDirNationalRealTimeSubmit(TransRemitVerifyInfoViewModel dataModel) {
        final PsnDirTransCrossBankTransferSubmitParams  dirNationalRealtimeSubmitParams
                =new PsnDirTransCrossBankTransferSubmitParams();
        dirNationalRealtimeSubmitParams.setAmount(dataModel.getAmount());
        dirNationalRealtimeSubmitParams.setBankName(dataModel.getBankName());
//        dirNationalRealtimeParams.set_combinId();
        dirNationalRealtimeSubmitParams.setCnapsCode(dataModel.getCnapsCode());
        dirNationalRealtimeSubmitParams.setCurrency(dataModel.getCurrency());
        dirNationalRealtimeSubmitParams.setExecuteType(dataModel.getExecuteType());
        dirNationalRealtimeSubmitParams.setFromAccountId(dataModel.getFromAccountId());
        dirNationalRealtimeSubmitParams.setPayeeId(Integer.valueOf(null==dataModel.getPayeeId()?"":dataModel.getPayeeId()));
        dirNationalRealtimeSubmitParams.setPayeeActno(dataModel.getPayeeActno());
        dirNationalRealtimeSubmitParams.setPayeeMobile(dataModel.getPayeeMobile());
        dirNationalRealtimeSubmitParams.setPayeeName(dataModel.getPayeeName());
        dirNationalRealtimeSubmitParams.setRemark(dataModel.getRemark());
        dirNationalRealtimeSubmitParams.setRemittanceInfo(dataModel.getRemittanceInfo());
        dirNationalRealtimeSubmitParams.setSendMsgFlag(dataModel.getSendMsgFlag());
        dirNationalRealtimeSubmitParams.setToOrgName(dataModel.getToOrgName());
        dirNationalRealtimeSubmitParams.setSmc(dataModel.getSmc());
        dirNationalRealtimeSubmitParams.setSmc_RC(dataModel.getSmc_RC());
        dirNationalRealtimeSubmitParams.setOtp(dataModel.getOtp());
        dirNationalRealtimeSubmitParams.setOtp_RC(dataModel.getOtp_RC());
        dirNationalRealtimeSubmitParams.setDevicePrint(dataModel.getDevicePrint());
        if ("96".equals(dataModel.get_combinId())){
            dirNationalRealtimeSubmitParams.setDeviceInfo(dataModel.getDeviceInfo());
            dirNationalRealtimeSubmitParams.setDeviceInfo_RC(dataModel.getDeviceInfo_RC());
        }
        dirNationalRealtimeSubmitParams.setState(dataModel.getState());
        dirNationalRealtimeSubmitParams.setActiv(dataModel.getActiv());
        dirNationalRealtimeSubmitParams.set_combinId(dataModel.get_combinId());
        dirNationalRealtimeSubmitParams.setConversationId(dataModel.getConversationId());
        dirNationalRealtimeSubmitParams.set_signedData(dataModel.get_signedData());
        if ("1".equals(dataModel.getExecuteType())){
            dirNationalRealtimeSubmitParams.setDueDate(dataModel.getExecuteDate());
            dirNationalRealtimeSubmitParams.setExecuteDate(dataModel.getExecuteDate());
        }else{
            dirNationalRealtimeSubmitParams.setDueDate("");
            dirNationalRealtimeSubmitParams.setExecuteDate(null);
        }

        PSNGetTokenIdParams mTokenIdParams = new PSNGetTokenIdParams();
        mTokenIdParams.setConversationId(dataModel.getConversationId());
        globalService.psnGetTokenId(mTokenIdParams)
                .compose(this.<String>bindToLifecycle())
                .flatMap(new Func1<String, Observable<PsnDirTransCrossBankTransferSubmitResult>>() {
                    @Override
                    public Observable<PsnDirTransCrossBankTransferSubmitResult> call(String tokenId) {
                        // TODO: 2016/7/9  是否需要上送atm密码，和电话银行密码要看实际情况
                        dirNationalRealtimeSubmitParams.setToken(tokenId);
                        return transService.psnDirTransNationalRealtimeSubmit(dirNationalRealtimeSubmitParams);
                    }
                })
                .compose(SchedulersCompat.<PsnDirTransCrossBankTransferSubmitResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnDirTransCrossBankTransferSubmitResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        mTransConfirmView.transBocSubmitFailed(biiResultErrorException);
                    }
                    @Override
                    public void onCompleted() {
                    }
                    @Override
                    public void onNext(PsnDirTransCrossBankTransferSubmitResult result) {
                        mTransConfirmView.transDirNationalRealTimeSubmitSuccess(result);
                    }
                });
    }

    @Override
    public void getRandomNum(String conversationId) {
        PSNGetRandomParams randomParams=new PSNGetRandomParams();
        randomParams.setConversationId(conversationId);
        globalService.psnGetRandom(randomParams)
                .compose(this.<String>bindToLifecycle())
                .compose(SchedulersCompat.<String>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<String>() {
                    @Override
                    public void commonHandleException(BiiResultErrorException biiResultErrorException) {
                    }
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        mTransConfirmView.getRandomNumFailed(biiResultErrorException);
                    }
                    @Override
                    public void onCompleted() {
                    }
                    @Override
                    public void onNext(String s) {
//                        closeProgressDialog();
                        mTransConfirmView.getRandomNumSuccess(s);
                    }
                });
    }

    @Override
    public void transBocVerify(PsnTransBocTransferVerifyParams params) {
        transService.psnTransBocTransferVerify(params)
                .compose(this.<PsnTransBocTransferVerifyResult>bindToLifecycle())
                .compose(SchedulersCompat.<PsnTransBocTransferVerifyResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnTransBocTransferVerifyResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                    }
                    @Override
                    public void onCompleted() {
                    }
                    @Override
                    public void onNext(PsnTransBocTransferVerifyResult psnTransBocTransferVerifyResult) {
                        mTransConfirmView.transBocVerifySuccess(psnTransBocTransferVerifyResult);
                    }
                });
    }

    @Override
    public void transDirBocVerify(PsnDirTransBocTransferVerifyParams params) {
        transService.psnDirTransBocTransferVerify(params)
                .compose(this.<PsnDirTransBocTransferVerifyResult>bindToLifecycle())
                .compose(SchedulersCompat.<PsnDirTransBocTransferVerifyResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnDirTransBocTransferVerifyResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        mTransConfirmView.transBocDirSubmitFailed(biiResultErrorException);
                    }
                    @Override
                    public void onCompleted() {

                    }
                    @Override
                    public void onNext(PsnDirTransBocTransferVerifyResult psnDirTransBocTransferVerifyResult) {
                        mTransConfirmView.transDirVerifySuccess(psnDirTransBocTransferVerifyResult);
                    }
                });
    }

    @Override
    public void transDirNationalRealTimeVerify(PsnDirTransCrossBankTransferParams params) {
        transService.psnDirTransNationalRealtimeVerify(params)
                .compose(this.<PsnDirTransCrossBankTransferResult>bindToLifecycle())
                .compose(SchedulersCompat.<PsnDirTransCrossBankTransferResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnDirTransCrossBankTransferResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        mTransConfirmView.transDirNationalRealTimeVerifyFailed(biiResultErrorException);
                    }
                    @Override
                    public void onCompleted() {
                    }
                    @Override
                    public void onNext(PsnDirTransCrossBankTransferResult psnDirTransCrossBankTransferResult) {
                        mTransConfirmView.transDirNationalRealTimeVerifySuccess(psnDirTransCrossBankTransferResult);
                    }
                });
    }

    @Override
    public void transDirNationalVerify(PsnDirTransBocNationalTransferVerifyParams params) {
        transService.psnDirTransNationalVerify(params)
                .compose(this.<PsnDirTransBocNationalTransferVerifyResult>bindToLifecycle())
                .compose(SchedulersCompat.<PsnDirTransBocNationalTransferVerifyResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnDirTransBocNationalTransferVerifyResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        mTransConfirmView.transDirNationalVerifyFailed(biiResultErrorException);
                    }
                    @Override
                    public void onCompleted() {
                    }
                    @Override
                    public void onNext(PsnDirTransBocNationalTransferVerifyResult result) {
                        mTransConfirmView.transDirNationalVerifySuccess(result);
                    }
                });
    }

    @Override
    public void transNationalVerify(PsnTransBocNationalTransferVerifyParams params) {
        transService.psnTransferNationalVerify(params)
                .compose(this.<PsnTransBocNationalTransferVerifyResult>bindToLifecycle())
                .compose(SchedulersCompat.<PsnTransBocNationalTransferVerifyResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnTransBocNationalTransferVerifyResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        mTransConfirmView.transNationalVerifyFailed(biiResultErrorException);
                    }
                    @Override
                    public void onCompleted() {
                    }
                    @Override
                    public void onNext(PsnTransBocNationalTransferVerifyResult psnTransBocNationalTransferVerifyResult) {

                        mTransConfirmView.transNationalVerifySuccess(psnTransBocNationalTransferVerifyResult);
                    }
                });

    }
    @Override
    public void transNationalRealTimeVerify(PsnEbpsRealTimePaymentConfirmParams params) {
        transService.psnTransNationalRealtimeVerify(params)
                .compose(this.<PsnEbpsRealTimePaymentConfirmResult>bindToLifecycle())
                .compose(SchedulersCompat.<PsnEbpsRealTimePaymentConfirmResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnEbpsRealTimePaymentConfirmResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        mTransConfirmView.transNationalRealTimeVerifyFailed(biiResultErrorException);
                    }
                    @Override
                    public void onCompleted() {
                    }
                    @Override
                    public void onNext(PsnEbpsRealTimePaymentConfirmResult psnEbpsRealTimePaymentConfirmResult) {
                        mTransConfirmView.transNationalRealTimeVerifySuccess(psnEbpsRealTimePaymentConfirmResult);
                    }
                });
    }

//    @Override
//    public void subscribe() {
//    }
//
//    @Override
//    public void unsubscribe() {
//        this.onDestroy();
//    }
}
