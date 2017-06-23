package com.boc.bocsoft.mobile.bocmobile.buss.transfer.withdraw.presenter;

import com.boc.bocsoft.mobile.bii.bus.global.model.PSNCreatConversation.PSNCreatConversationParams;
import com.boc.bocsoft.mobile.bii.bus.global.model.PSNGetRandom.PSNGetRandomParams;
import com.boc.bocsoft.mobile.bii.bus.global.model.PSNGetTokenId.PSNGetTokenIdParams;
import com.boc.bocsoft.mobile.bii.bus.global.service.GlobalService;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnMobileWithdrawal.PsnMobileWithdrawalParams;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnMobileWithdrawal.PsnMobileWithdrawalResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnMobileWithdrawalDetails.PsnMobileWithdrawalDetailsParams;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnMobileWithdrawalDetails.PsnMobileWithdrawalDetailsResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.service.TransferService;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.base.Exception.BIIBaseSubscriber;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify.SecurityVerity;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.withdraw.model.MobileWithdrawViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.withdraw.ui.MobileWithdrawContract;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.withdraw.ui.MobileWithdrawFragment;
import com.boc.bocsoft.mobile.framework.rx.SchedulersCompat;
import com.boc.bocsoft.mobile.framework.rx.lifecycle.RxLifecycleManager;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by liuweidong on 2016/7/12.
 */
public class MobileWithdrawPresenter implements MobileWithdrawContract.Presenter {
    private RxLifecycleManager mRxLifecycleManager;
    // 公共Service
    private GlobalService globalService;
    // 转账汇款Service
    private TransferService transferService;

    private MobileWithdrawContract.BeforeView mBeforeView;
    private MobileWithdrawContract.ResultView mResultView;

    /**
     * 会话ID
     */
    private static String conversationID = "";
    public static String randomID = "";

    public MobileWithdrawPresenter() {
        mRxLifecycleManager = new RxLifecycleManager();
        globalService = new GlobalService();
        transferService = new TransferService();
    }

    public MobileWithdrawPresenter(MobileWithdrawContract.BeforeView beforeView) {
        this();
        mBeforeView = beforeView;
    }

    public MobileWithdrawPresenter(MobileWithdrawContract.ResultView resultView) {
        this();
        mResultView = resultView;
    }

    @Override
    public void queryRandom() {
        // 创建会话
        PSNCreatConversationParams psnCreatConversationParams = new PSNCreatConversationParams();
        globalService.psnCreatConversation(psnCreatConversationParams)
                .compose(mRxLifecycleManager.<String>bindToLifecycle())
                .flatMap(new Func1<String, Observable<String>>() {
                    @Override
                    public Observable<String> call(String s) {
                        conversationID = s;
                        // 查询随机数
                        PSNGetRandomParams psnGetRandomParams = new PSNGetRandomParams();
                        psnGetRandomParams.setConversationId(conversationID);
                        return globalService.psnGetRandom(psnGetRandomParams);
                    }
                }).compose(SchedulersCompat.<String>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<String>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        mBeforeView.queryRandomFail(biiResultErrorException);
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(String result) {
                        randomID = result;
                        mBeforeView.queryRandomSuccess();
                    }
                });

    }

    /**
     * 汇款解付详细信息
     *
     * @param viewModel
     */
    @Override
    public void queryMobileWithdrawDetails(MobileWithdrawViewModel viewModel) {
        transferService.psnMobileWithdrawalDetails(buildPsnMobileWithdrawalDetailsParams(viewModel))
                .compose(mRxLifecycleManager.<PsnMobileWithdrawalDetailsResult>bindToLifecycle())
                .compose(SchedulersCompat.<PsnMobileWithdrawalDetailsResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnMobileWithdrawalDetailsResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        mBeforeView.queryMobileWithdrawDetailsFail(biiResultErrorException);
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(PsnMobileWithdrawalDetailsResult result) {
                        resultModelToViewModel(result);
                        mBeforeView.queryMobileWithdrawDetailsSuccess();
                    }
                });
    }

    /**
     * 汇款解付
     */
    @Override
    public void queryMobileWithdraw(final String withDrawPwd, final String withDrawPwdRC, final String cfcaVersion) {
        PSNGetTokenIdParams psnGetTokenIdParams = new PSNGetTokenIdParams();
        psnGetTokenIdParams.setConversationId(conversationID);
        globalService.psnGetTokenId(psnGetTokenIdParams)
                .compose(mRxLifecycleManager.<String>bindToLifecycle())
                .flatMap(new Func1<String, Observable<PsnMobileWithdrawalResult>>() {
                    @Override
                    public Observable<PsnMobileWithdrawalResult> call(String tokenID) {
                        PsnMobileWithdrawalParams params = buildPsnMobileWithdrawalParams(withDrawPwd, withDrawPwdRC, cfcaVersion);
                        params.setToken(tokenID);
                        return transferService.psnMobileWithdrawal(params);
                    }
                }).compose(SchedulersCompat.<PsnMobileWithdrawalResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnMobileWithdrawalResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        mResultView.queryMobileWithdrawFail(biiResultErrorException);
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(PsnMobileWithdrawalResult result) {
                        mResultView.queryMobileWithdrawSuccess();
                    }
                });
    }

    @Override
    public void subscribe() {
        mRxLifecycleManager.onStart();
    }

    @Override
    public void unsubscribe() {
        mRxLifecycleManager.onDestroy();
    }

    /**
     * 封装汇款解付详细信息请求
     *
     * @param viewModel
     * @return
     */
    private PsnMobileWithdrawalDetailsParams buildPsnMobileWithdrawalDetailsParams(MobileWithdrawViewModel viewModel) {
        PsnMobileWithdrawalDetailsParams params = new PsnMobileWithdrawalDetailsParams();
        params.setRemitNo(viewModel.getRemitNo());
        params.setPayeeMobile(viewModel.getPayeeMobile());
        params.setPayeeName(viewModel.getPayeeName());
        return params;
    }

    /**
     * @param withDrawPwd
     * @param withDrawPwdRC
     * @return
     */
    private PsnMobileWithdrawalParams buildPsnMobileWithdrawalParams(String withDrawPwd, String withDrawPwdRC, String cfcaVersion) {
        PsnMobileWithdrawalParams params = new PsnMobileWithdrawalParams();
        params.setConversationId(conversationID);
        params.setRemitNo(MobileWithdrawFragment.getViewModel().getRemitNo());
        params.setWithDrawPwd(withDrawPwd);
        params.setWithDrawPwd_RC(withDrawPwdRC);
        params.setState(SecurityVerity.SECURITY_VERIFY_STATE);
        params.setActiv(cfcaVersion);
        return params;
    }

    /**
     * 汇款解付详细信息响应数据
     *
     * @param result
     */
    private void resultModelToViewModel(PsnMobileWithdrawalDetailsResult result) {
        MobileWithdrawViewModel viewModel = MobileWithdrawFragment.getViewModel();
        viewModel.setRemitNo(result.getRemitNo());
        viewModel.setPayeeMobile(result.getPayeeMobile());
        viewModel.setPayeeName(result.getPayeeName());
        viewModel.setCurrencyCode(result.getCurrencyCode());
        viewModel.setRemitAmount(result.getRemitAmount());
        viewModel.setRemark(result.getRemark());
    }
}
