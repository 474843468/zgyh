package com.boc.bocsoft.mobile.bocmobile.buss.transfer.makecollection.presenter;

import android.app.Activity;
import android.content.Context;

import com.boc.bocsoft.mobile.bii.bus.global.model.PSNCreatConversation.PSNCreatConversationParams;
import com.boc.bocsoft.mobile.bii.bus.global.model.PSNGetRandom.PSNGetRandomParams;
import com.boc.bocsoft.mobile.bii.bus.global.model.PSNGetTokenId.PSNGetTokenIdParams;
import com.boc.bocsoft.mobile.bii.bus.global.model.PsnGetSecurityFactor.CombinListBean;
import com.boc.bocsoft.mobile.bii.bus.global.model.PsnGetSecurityFactor.PsnGetSecurityFactorParams;
import com.boc.bocsoft.mobile.bii.bus.global.model.PsnGetSecurityFactor.PsnGetSecurityFactorResult;
import com.boc.bocsoft.mobile.bii.bus.global.service.GlobalService;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnBatchTransActCollectionSubmit.PsnBatchTransActCollectionSubmitParams;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnBatchTransActCollectionSubmit.PsnBatchTransActCollectionSubmitResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnBatchTransActCollectionVerify.PsnBatchTransActCollectionVerifyParams;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnBatchTransActCollectionVerify.PsnBatchTransActCollectionVerifyResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransActCollectionSubmit.PsnTransActCollectionSubmitParams;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransActCollectionSubmit.PsnTransActCollectionSubmitResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransActCollectionVerify.PsnTransActCollectionVerifyParams;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransActCollectionVerify.PsnTransActCollectionVerifyResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransActSavePayer.PsnTransActSavePayerParams;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransActSavePayer.PsnTransActSavePayerResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.service.TransferService;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.base.Exception.BIIBaseSubscriber;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.utils.DeviceInfoUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify.DeviceInfoModel;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify.SecurityFactorModel;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify.SecurityVerity;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.makecollection.model.CombinListEntity;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.makecollection.model.PsnBatchTransActCollectionSubmitViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.makecollection.model.PsnBatchTransActCollectionVerifyViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.makecollection.model.PsnTransActCollectionSubmitViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.makecollection.model.PsnTransActCollectionVerifyViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.makecollection.model.SavePayerViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.makecollection.ui.PayConfirmContact;
import com.boc.bocsoft.mobile.common.utils.BeanConvertor.BeanConvertor;
import com.boc.bocsoft.mobile.framework.rx.SchedulersCompat;
import com.boc.bocsoft.mobile.framework.rx.lifecycle.RxLifecycleManager;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.functions.Func1;

/**
 * Contract:主动收款确认（预交易和正式交易，单人和多人）
 * Created by zhx on 2016/8/11
 */
public class PayConfirmPresenter implements PayConfirmContact.Presenter {
    private PayConfirmContact.View mView;
    private RxLifecycleManager mRxLifecycleManager;

    private GlobalService globalService;
    private TransferService transferService;

    // 不知道该说啥
    private String _combinId = null;
    private String current_combinId = null;
    public String conversationId = null;
    private String token = null;
    public String random = null;

    public CombinListBean combinListBean;
    public PsnGetSecurityFactorResult psnGetSecurityFactorResult;

    public void setCurrent_combinId(String current_combinId) {
        this.current_combinId = current_combinId;
    }

    public PayConfirmPresenter(PayConfirmContact.View view) {
        this.mView = view;
        mView.setPresenter(this);
        mRxLifecycleManager = new RxLifecycleManager();

        globalService = new GlobalService();
        transferService = new TransferService();
    }


    @Override
    public void getRandom() {
        ((BussFragment) mView).showLoadingDialog("请稍候...", false);

        PSNGetRandomParams psnGetRandomParams = new PSNGetRandomParams();
        psnGetRandomParams.setConversationId(this.conversationId);
        globalService.psnGetRandom(psnGetRandomParams)
                .compose(mRxLifecycleManager.<String>bindToLifecycle())
                .compose(SchedulersCompat.<String>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<String>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        ((BussFragment) mView).closeProgressDialog();
                        mView.getRandomFailed(biiResultErrorException);
                    }

                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onNext(String random) {
                        PayConfirmPresenter.this.random = random;
                        ((BussFragment) mView).closeProgressDialog();

                        mView.getRandomSuccess(random);
                    }
                });
    }

    @Override
    public void collectionVerify(final PsnTransActCollectionVerifyViewModel viewModel) {
        ((BussFragment) mView).showLoadingDialog("请稍候...", false);
        final PSNCreatConversationParams psnCreatConversationParams = new PSNCreatConversationParams();
        globalService.psnCreatConversation(psnCreatConversationParams)
                .compose(mRxLifecycleManager.<String>bindToLifecycle())
                .flatMap(new Func1<String, Observable<PsnGetSecurityFactorResult>>() {
                    @Override
                    public Observable<PsnGetSecurityFactorResult> call(String conversation) {
                        PayConfirmPresenter.this.conversationId = conversation;

                        PsnGetSecurityFactorParams psnGetSecurityFactorParams = new PsnGetSecurityFactorParams();
                        psnGetSecurityFactorParams.setConversationId(conversation);
                        psnGetSecurityFactorParams.setServiceId("PB037"); // 转账汇款->主动收款->主动收款预交易:PB037
                        return globalService.psnGetSecurityFactor(psnGetSecurityFactorParams);
                    }
                })
                .flatMap(new Func1<PsnGetSecurityFactorResult, Observable<PsnTransActCollectionVerifyResult>>() {
                    @Override
                    public Observable<PsnTransActCollectionVerifyResult> call(PsnGetSecurityFactorResult psnGetSecurityFactorResult) {
                        PayConfirmPresenter.this.psnGetSecurityFactorResult = psnGetSecurityFactorResult; // 保存psnGetSecurityFactorResult

                        PsnTransActCollectionVerifyParams psnTransActCollectionVerifyParams = new PsnTransActCollectionVerifyParams();

                        BeanConvertor.toBean(viewModel, psnTransActCollectionVerifyParams);
                        psnTransActCollectionVerifyParams.setConversationId(conversationId); // 如果conversationId没有赋值，就会提示回话失效


                        PayConfirmPresenter.this.combinListBean = generateCombinId(psnGetSecurityFactorResult);
                        if(current_combinId != null) {
                            psnTransActCollectionVerifyParams.set_combinId(current_combinId);
                        } else {
                            psnTransActCollectionVerifyParams.set_combinId(combinListBean.getId());
                        }

                        viewModel.set_combinId(combinListBean.getId());
                        viewModel.set_combinName(combinListBean.getName());


                        CombinListEntity combinListEntity = new CombinListEntity();
                        combinListEntity.setId(combinListBean.getId());

                        return transferService.psnTransActCollectionVerify(psnTransActCollectionVerifyParams);
                    }
                })
                .compose(SchedulersCompat.<PsnTransActCollectionVerifyResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnTransActCollectionVerifyResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        ((BussFragment) mView).closeProgressDialog();
                        mView.collectionVerifyFailed(biiResultErrorException);
                    }

                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onNext(PsnTransActCollectionVerifyResult psnTransActCollectionVerifyResult) {
                        BeanConvertor.toBean(psnTransActCollectionVerifyResult, viewModel);
                        viewModel.setFactorList(psnTransActCollectionVerifyResult.getFactorList());
                        mView.collectionVerifySuccess(viewModel);
                    }
                });

    }

    public PsnGetSecurityFactorResult getPsnGetSecurityFactorResult() {
        return psnGetSecurityFactorResult;
    }

    @Override
    public void collectionSubmit(final PsnTransActCollectionSubmitViewModel viewModel, final String[] randomNums,
                                 final String[] encryptPasswords, final int curCombinID, final Context context) {
        ((BussFragment) mView).showLoadingDialog("请稍候...", false);
        PSNGetTokenIdParams psnGetTokenIdParams = new PSNGetTokenIdParams();
        psnGetTokenIdParams.setConversationId(this.conversationId);
        globalService.psnGetTokenId(psnGetTokenIdParams)
                .compose(mRxLifecycleManager.<String>bindToLifecycle())
                .flatMap(new Func1<String, Observable<PsnTransActCollectionSubmitResult>>() {
                    @Override
                    public Observable<PsnTransActCollectionSubmitResult> call(String tokenId) {

                        // 生成请求的参数
                        PsnTransActCollectionSubmitParams params = generateCollectionSubmitParams(viewModel, randomNums, encryptPasswords, curCombinID, context, tokenId);

                        return transferService.psnTransActCollectionSubmit(params);
                    }
                })
                .compose(SchedulersCompat.<PsnTransActCollectionSubmitResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnTransActCollectionSubmitResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        ((BussFragment) mView).closeProgressDialog();
                        mView.collectionSubmitFailed(biiResultErrorException);
                    }

                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onNext(PsnTransActCollectionSubmitResult psnTransActCollectionSubmitResult) {
                        ((BussFragment) mView).closeProgressDialog();
                        viewModel.setNotifyId(psnTransActCollectionSubmitResult.getNotifyId());
                        mView.collectionSubmitSuccess(viewModel);
                    }
                });
    }

    @Override
    public void psnBatchTransActCollectionVerify(final PsnBatchTransActCollectionVerifyViewModel viewModel) {
        ((BussFragment) mView).showLoadingDialog("请稍候...");
        final PSNCreatConversationParams psnCreatConversationParams = new PSNCreatConversationParams();
        globalService.psnCreatConversation(psnCreatConversationParams)
                .compose(mRxLifecycleManager.<String>bindToLifecycle())
                .flatMap(new Func1<String, Observable<PsnGetSecurityFactorResult>>() {
                    @Override
                    public Observable<PsnGetSecurityFactorResult> call(String conversation) {
                        PayConfirmPresenter.this.conversationId = conversation;

                        PsnGetSecurityFactorParams psnGetSecurityFactorParams = new PsnGetSecurityFactorParams();
                        psnGetSecurityFactorParams.setConversationId(conversation);
                        psnGetSecurityFactorParams.setServiceId("PB037"); // 转账汇款->主动收款->主动收款预交易:PB037
                        return globalService.psnGetSecurityFactor(psnGetSecurityFactorParams);
                    }
                })
                .flatMap(new Func1<PsnGetSecurityFactorResult, Observable<PsnBatchTransActCollectionVerifyResult>>() {
                    @Override
                    public Observable<PsnBatchTransActCollectionVerifyResult> call(PsnGetSecurityFactorResult psnGetSecurityFactorResult) {
                        PayConfirmPresenter.this.psnGetSecurityFactorResult = psnGetSecurityFactorResult; // 保存psnGetSecurityFactorResult

                        // 生成“批量主动收款预交易”请求参数
                        PsnBatchTransActCollectionVerifyParams psnBatchTransActCollectionVerifyParams = generateBatchCollectionVerifyParams(viewModel);

                        return transferService.psnBatchTransActCollectionVerify(psnBatchTransActCollectionVerifyParams);
                    }
                })
                .compose(SchedulersCompat.<PsnBatchTransActCollectionVerifyResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnBatchTransActCollectionVerifyResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        ((BussFragment) mView).closeProgressDialog();
                        mView.collectionVerifyFailed(biiResultErrorException);
                    }

                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onNext(PsnBatchTransActCollectionVerifyResult psnBatchTransActCollectionVerifyResult) {
                        ((BussFragment) mView).closeProgressDialog();
                        BeanConvertor.toBean(psnBatchTransActCollectionVerifyResult, viewModel);
                        viewModel.setFactorList(psnBatchTransActCollectionVerifyResult.getFactorList());
                        mView.psnBatchTransActCollectionVerifySuccess(viewModel);
                    }
                });

    }

    // 生成“批量主动收款预交易”的参数
    private PsnBatchTransActCollectionVerifyParams generateBatchCollectionVerifyParams(PsnBatchTransActCollectionVerifyViewModel viewModel) {
        PsnBatchTransActCollectionVerifyParams psnBatchTransActCollectionVerifyParams = new PsnBatchTransActCollectionVerifyParams();

        BeanConvertor.toBean(viewModel, psnBatchTransActCollectionVerifyParams);

        // 给list手动赋值
        List<PsnBatchTransActCollectionVerifyParams.PayerEntity> list = new ArrayList<PsnBatchTransActCollectionVerifyParams.PayerEntity>();
        List<PsnBatchTransActCollectionVerifyViewModel.PayerEntity> payerEntityList = viewModel.getPayerList();
        for (PsnBatchTransActCollectionVerifyViewModel.PayerEntity payerEntity : payerEntityList) {
            PsnBatchTransActCollectionVerifyParams.PayerEntity entity = new PsnBatchTransActCollectionVerifyParams.PayerEntity();
            BeanConvertor.toBean(payerEntity, entity);
            list.add(entity);
        }
        psnBatchTransActCollectionVerifyParams.setPayerList(list);

        psnBatchTransActCollectionVerifyParams.setConversationId(conversationId); // 如果conversationId没有赋值，就会提示回话失效


        PayConfirmPresenter.this.combinListBean = generateCombinId(psnGetSecurityFactorResult);
        psnBatchTransActCollectionVerifyParams.set_combinId(combinListBean.getId());

        viewModel.set_combinId(combinListBean.getId());
        viewModel.set_combinName(combinListBean.getName());


        CombinListEntity combinListEntity = new CombinListEntity();
        combinListEntity.setId(combinListBean.getId());

        return psnBatchTransActCollectionVerifyParams;
    }

    @Override
    public void psnBatchTransActCollectionSubmit(final PsnBatchTransActCollectionSubmitViewModel viewModel, final String[] randomNums, final String[] encryptPasswords, final int curCombinID, final Context context) {
        ((BussFragment) mView).showLoadingDialog("请稍候...");
        PSNGetTokenIdParams psnGetTokenIdParams = new PSNGetTokenIdParams();
        psnGetTokenIdParams.setConversationId(this.conversationId);
        globalService.psnGetTokenId(psnGetTokenIdParams)
                .compose(mRxLifecycleManager.<String>bindToLifecycle())
                .flatMap(new Func1<String, Observable<PsnBatchTransActCollectionSubmitResult>>() {
                    @Override
                    public Observable<PsnBatchTransActCollectionSubmitResult> call(String tokenId) {
                        PsnBatchTransActCollectionSubmitParams params = generateBatchCollectionSubmitParams(viewModel, randomNums, encryptPasswords, curCombinID, context, tokenId);

                        return transferService.psnBatchTransActCollectionSubmit(params);
                    }
                })
                .compose(SchedulersCompat.<PsnBatchTransActCollectionSubmitResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnBatchTransActCollectionSubmitResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        ((BussFragment) mView).closeProgressDialog();
                        mView.psnBatchTransActCollectionSubmitFailed(biiResultErrorException);
                    }

                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onNext(PsnBatchTransActCollectionSubmitResult psnBatchTransActCollectionSubmitResult) {
                        ((BussFragment) mView).closeProgressDialog();
                        mView.psnBatchTransActCollectionSubmitSuccess(viewModel);
                    }
                });
    }

    @Override
    public void savePayer(final SavePayerViewModel savePayerViewModel) {
        ((BussFragment) mView).showLoadingDialog("请稍候...", false);
        PSNCreatConversationParams psnCreatConversationParams = new PSNCreatConversationParams();
        globalService.psnCreatConversation(psnCreatConversationParams)
                .compose(mRxLifecycleManager.<String>bindToLifecycle())
                .flatMap(new Func1<String, Observable<String>>() {
                    @Override
                    public Observable<String> call(String conversation) {
                        PSNGetTokenIdParams psnGetTokenIdParams = new PSNGetTokenIdParams();
                        psnGetTokenIdParams.setConversationId(conversation);
                        savePayerViewModel.setConversationId(conversation);
                        return globalService.psnGetTokenId(psnGetTokenIdParams);
                    }
                })
                .flatMap(new Func1<String, Observable<PsnTransActSavePayerResult>>() {
                    @Override
                    public Observable<PsnTransActSavePayerResult> call(String token) {
                        savePayerViewModel.setToken(token);
                        return transferService.psnTransActSavePayer(buildSavePayerMobileParams(savePayerViewModel));
                    }
                })
                .compose(SchedulersCompat.<PsnTransActSavePayerResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnTransActSavePayerResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        ((BussFragment) mView).closeProgressDialog();
                        mView.savePayerFail(biiResultErrorException);
                    }

                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onNext(PsnTransActSavePayerResult psnTransActSavePayerResult) {
                        ((BussFragment) mView).closeProgressDialog();
                        mView.savePayerSuccess(null);
                    }
                });
    }

    /**
     * 封装请求参数：
     * 主动收款保存常用付款人
     *
     * @param savePayerViewModel
     * @return PsnTransActSavePayerParams
     */
    private PsnTransActSavePayerParams buildSavePayerMobileParams(SavePayerViewModel savePayerViewModel) {
        PsnTransActSavePayerParams psnTransActSavePayerParams = new PsnTransActSavePayerParams();
        psnTransActSavePayerParams.setToken(savePayerViewModel.getToken());
        psnTransActSavePayerParams.setPayerChannel("2"); // 收款人类型：1：WEB渠道、2：手机渠道
        psnTransActSavePayerParams.setPayerCustId(savePayerViewModel.getPayerCustId()); // TODO 付款人客户号，怎么获取？
        psnTransActSavePayerParams.setPayerMobile(savePayerViewModel.getPayerMobile());
        psnTransActSavePayerParams.setPayerName(savePayerViewModel.getPayerName());
        psnTransActSavePayerParams.setConversationId(savePayerViewModel.getConversationId());
        return psnTransActSavePayerParams;
    }

    /**
     * 生成主动收款提交的参数(单人)
     *
     * @param viewModel
     * @param randomNums
     * @param encryptPasswords
     * @param curCombinID
     * @param context
     * @param tokenId
     * @return
     */
    private PsnTransActCollectionSubmitParams generateCollectionSubmitParams(final PsnTransActCollectionSubmitViewModel viewModel, final String[] randomNums,
                                                                             final String[] encryptPasswords, final int curCombinID, final Context context, String tokenId) {
        PsnTransActCollectionSubmitParams params = new PsnTransActCollectionSubmitParams();
        BeanConvertor.toBean(viewModel, params);
        params.setToken(tokenId);
        params.setConversationId(PayConfirmPresenter.this.conversationId);
        params.setState(SecurityVerity.SECURITY_VERIFY_STATE); // 状态
        params.setDevicePrint(DeviceInfoUtils.getDevicePrint((Activity) context)); // 设备指纹
        //        params.set_signedData(); // TODO 先空着，签名数据。中银E盾会用到

        params.setState(SecurityVerity.SECURITY_VERIFY_STATE);
        params.setActiv(SecurityVerity.getInstance().getCfcaVersion());

        switch (curCombinID) {
            case SecurityVerity.SECURITY_VERIFY_TOKEN:// 动态口令
                params.setOtp(encryptPasswords[0]);
                params.setOtp_RC(randomNums[0]);
                break;
            case SecurityVerity.SECURITY_VERIFY_SMS:// 短信
                params.setSmc(encryptPasswords[0]);
                params.setSmc_RC(randomNums[0]);
                break;
            case SecurityVerity.SECURITY_VERIFY_SMS_AND_TOKEN:// 动态口令+短信
                params.setOtp(encryptPasswords[0]);
                params.setOtp_RC(randomNums[0]);
                params.setSmc(encryptPasswords[1]);
                params.setSmc_RC(randomNums[1]);
                break;
            case SecurityVerity.SECURITY_VERIFY_DEVICE:// 手机交易码+硬件绑定
                params.setSmc(encryptPasswords[0]);
                params.setSmc_RC(randomNums[0]);
                DeviceInfoModel deviceInfoModel = DeviceInfoUtils.getDeviceInfo(context, random);
                params.setDeviceInfo(deviceInfoModel.getDeviceInfo());
                params.setDeviceInfo_RC(deviceInfoModel.getDeviceInfo_RC());
                break;
            case SecurityVerity.SECURITY_VERIFY_E_TOKEN:// 中银e盾
                params.set_signedData(randomNums[0]);
                break;
            default:
                break;
        }

        return params;
    }

    /**
     * 生成主动收款提交的参数(多人)
     *
     * @param viewModel
     * @param randomNums
     * @param encryptPasswords
     * @param curCombinID
     * @param context
     * @param tokenId
     * @return
     */
    private PsnBatchTransActCollectionSubmitParams generateBatchCollectionSubmitParams(final PsnBatchTransActCollectionSubmitViewModel viewModel, final String[] randomNums,
                                                                                       final String[] encryptPasswords, final int curCombinID, final Context context, String tokenId) {
        PsnBatchTransActCollectionSubmitParams params = new PsnBatchTransActCollectionSubmitParams();
        BeanConvertor.toBean(viewModel, params);
        params.setToken(tokenId);
        params.setConversationId(PayConfirmPresenter.this.conversationId);
        params.setState(SecurityVerity.SECURITY_VERIFY_STATE); // 状态
        params.setDevicePrint(DeviceInfoUtils.getDevicePrint((Activity) context)); // 设备指纹

        params.setState(SecurityVerity.SECURITY_VERIFY_STATE);
        params.setToAccountId(Integer.parseInt(viewModel.getToAccountId()));
        params.setActiv(SecurityVerity.getInstance().getCfcaVersion());

        switch (curCombinID) {
            case SecurityVerity.SECURITY_VERIFY_TOKEN:// 动态口令
                params.setOtp(encryptPasswords[0]);
                params.setOtp_RC(randomNums[0]);
                break;
            case SecurityVerity.SECURITY_VERIFY_SMS:// 短信
                params.setSmc(encryptPasswords[0]);
                params.setSmc_RC(randomNums[0]);
                break;
            case SecurityVerity.SECURITY_VERIFY_SMS_AND_TOKEN:// 动态口令+短信
                params.setOtp(encryptPasswords[0]);
                params.setOtp_RC(randomNums[0]);
                params.setSmc(encryptPasswords[1]);
                params.setSmc_RC(randomNums[1]);
                break;
            case SecurityVerity.SECURITY_VERIFY_DEVICE:// 手机交易码+硬件绑定
                params.setSmc(encryptPasswords[0]);
                params.setSmc_RC(randomNums[0]);
                DeviceInfoModel deviceInfoModel = DeviceInfoUtils.getDeviceInfo(context, random);
                params.setDeviceInfo(deviceInfoModel.getDeviceInfo());
                params.setDeviceInfo_RC(deviceInfoModel.getDeviceInfo_RC());
                break;
            case SecurityVerity.SECURITY_VERIFY_E_TOKEN:// 中银e盾
                params.set_signedData(randomNums[0]);
                break;
            default:
                break;
        }

        return params;
    }

    /**
     * 生成需要的CombinId
     *
     * @param psnGetSecurityFactorResult
     * @return
     */
    private CombinListBean generateCombinId(PsnGetSecurityFactorResult psnGetSecurityFactorResult) {
        SecurityFactorModel model = new SecurityFactorModel();

        model.setCombinList(psnGetSecurityFactorResult.get_combinList());
        model.setDefaultCombin(psnGetSecurityFactorResult.get_defaultCombin());

        CombinListBean combinListBean = SecurityVerity.getInstance().getDefaultSecurityFactorId(model);
        return combinListBean;
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
