package com.boc.bocsoft.mobile.bocmobile.buss.account.transdetail.presenter;

import com.boc.bocsoft.mobile.bii.bus.account.model.PsnAccountQueryAccountDetail.PsnAccountQueryAccountDetailParams;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnAccountQueryAccountDetail.PsnAccountQueryAccountDetailResult;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnAccountQueryTransferDetail.PsnAccountQueryTransferDetailParams;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnAccountQueryTransferDetail.PsnAccountQueryTransferDetailResult;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnFinanceICTransferDetail.PsnFinanceICTransferDetailParams;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnFinanceICTransferDetail.PsnFinanceICTransferDetailResult;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnInquiryRangeQuery.PsnInquiryRangeQueryParams;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnInquiryRangeQuery.PsnInquiryRangeQueryResult;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnMedicalInsurAcctTransferDetailQuery.PsnMedicalInsurAcctTransferDetailQueryParams;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnMedicalInsurAcctTransferDetailQuery.PsnMedicalInsurAcctTransferDetailQueryResult;
import com.boc.bocsoft.mobile.bii.bus.account.service.AccountService;
import com.boc.bocsoft.mobile.bii.bus.account.service.FinanceService;
import com.boc.bocsoft.mobile.bii.bus.global.model.PSNCreatConversation.PSNCreatConversationParams;
import com.boc.bocsoft.mobile.bii.bus.global.service.GlobalService;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.base.Exception.BIIBaseSubscriber;
import com.boc.bocsoft.mobile.bocmobile.buss.account.transdetail.model.AccountDetailViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.account.transdetail.DetailModelUtils;
import com.boc.bocsoft.mobile.bocmobile.buss.account.transdetail.model.FinanceICTransferViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.account.transdetail.model.MedicalTransferDetailQueryViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.account.transdetail.model.TransDetailViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.account.transdetail.ui.TransDetailContract;
import com.boc.bocsoft.mobile.framework.rx.SchedulersCompat;
import com.boc.bocsoft.mobile.framework.rx.lifecycle.RxLifecycleManager;

import rx.Observable;
import rx.functions.Func1;

/**
 * 交易明细BII通信逻辑处理
 * Created by wangf on 2016/6/8.
 */
public class TransDetailPresenter implements TransDetailContract.Presenter {

    private TransDetailContract.View mTransDetailView;
    private RxLifecycleManager mRxLifecycleManager;

    /**
     * 会话
     */
    private String conversation;
    /**
     * 公共service
     */
    private GlobalService globalService;
    /**
     * 查询交易明细service
     */
    private AccountService accountService;
    /**
     * 电子现金账户的service
     */
    private FinanceService financeService;


    public TransDetailPresenter(TransDetailContract.View transDetailView) {
        mTransDetailView = transDetailView;
        mRxLifecycleManager = new RxLifecycleManager();
        globalService = new GlobalService();
        accountService = new AccountService();
        financeService = new FinanceService();
    }

    /**
     * 查询最大跨度和最长时间范围
     */
    @Override
    public void queryInquiryRange() {
        PsnInquiryRangeQueryParams params = new PsnInquiryRangeQueryParams();
        accountService.psnInquiryRangeQuery(params)
                .compose(mRxLifecycleManager.<PsnInquiryRangeQueryResult>bindToLifecycle())
                .compose(SchedulersCompat.<PsnInquiryRangeQueryResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnInquiryRangeQueryResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        mTransDetailView.queryInquiryRangeFail(biiResultErrorException);
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(PsnInquiryRangeQueryResult inquiryRangeQueryResult) {
                        mTransDetailView.queryInquiryRangeSuccess(DetailModelUtils.copyInquiryRangeResult2UIModel(inquiryRangeQueryResult));
                    }
                });

    }


    /**
     * 查询账户详情
     *
     * @param accountId
     */
    @Override
    public void queryAccountQueryAccountDetail(String accountId) {
        PsnAccountQueryAccountDetailParams params = new PsnAccountQueryAccountDetailParams();
        params.setAccountId(accountId);
        accountService.psnAccountQueryAccountDetail(params)
                .compose(mRxLifecycleManager.<PsnAccountQueryAccountDetailResult>bindToLifecycle())
                .compose(SchedulersCompat.<PsnAccountQueryAccountDetailResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnAccountQueryAccountDetailResult>() {
                    @Override
                    public void handleException(
                            BiiResultErrorException biiResultErrorException) {
                        mTransDetailView.queryAccountQueryAccountDetailFail(biiResultErrorException);
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(PsnAccountQueryAccountDetailResult psnAccountQueryAccountDetailResult) {
                        AccountDetailViewModel model = DetailModelUtils.copyAccountDetail2ViewModel(
                                psnAccountQueryAccountDetailResult);
                        mTransDetailView.queryAccountQueryAccountDetailSuccess(model);
                    }

                    @Override
                    public void commonHandleException(BiiResultErrorException biiResultErrorException) {
                        //此处重写该方法，在错误处理时使底层不在弹窗
                    }
                });
    }


    /**
     * 查询交易明细列表 -- 活期
     *
     * @param transDetailViewModel
     */
    @Override
    public void queryTransDetailList(final TransDetailViewModel transDetailViewModel) {

        //查询之前请求会话
        PSNCreatConversationParams psnCreatConversationParams = new PSNCreatConversationParams();
        globalService.psnCreatConversation(psnCreatConversationParams)
                .compose(mRxLifecycleManager.<String>bindToLifecycle())
                .flatMap(new Func1<String, Observable<PsnAccountQueryTransferDetailResult>>() {
                    @Override
                    public Observable<PsnAccountQueryTransferDetailResult> call(String conversation) {
                        PsnAccountQueryTransferDetailParams detailParams = DetailModelUtils.buildTransDetailParams(transDetailViewModel);
                        detailParams.setConversationId(conversation);
                        return accountService.psnAccountQueryTransferDetail(detailParams);
                    }
                })
                .compose(SchedulersCompat.<PsnAccountQueryTransferDetailResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnAccountQueryTransferDetailResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        mTransDetailView.queryTransDetailListFail(biiResultErrorException);
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(PsnAccountQueryTransferDetailResult psnAccountQueryTransferDetailResult) {
                        mTransDetailView.queryTransDetailListSuccess(DetailModelUtils.copyDetailResult2UIModel(psnAccountQueryTransferDetailResult));
                    }

                    @Override
                    public void commonHandleException(BiiResultErrorException biiResultErrorException) {
                        //此处重写该方法，在错误处理时使底层不在弹窗
                    }
                });

    }

    /**
     * 查询交易明细列表 -- 医保账户
     *
     * @param medicalTransferViewModel
     */
    @Override
    public void queryMedicalTransferList(final MedicalTransferDetailQueryViewModel medicalTransferViewModel) {
        //查询之前请求会话
        PSNCreatConversationParams psnCreatConversationParams = new PSNCreatConversationParams();
        globalService.psnCreatConversation(psnCreatConversationParams)
                .compose(mRxLifecycleManager.<String>bindToLifecycle())
                .flatMap(new Func1<String, Observable<PsnMedicalInsurAcctTransferDetailQueryResult>>() {
                    @Override
                    public Observable<PsnMedicalInsurAcctTransferDetailQueryResult> call(String conversation) {
                        PsnMedicalInsurAcctTransferDetailQueryParams detailParams = DetailModelUtils.buildMedicalTransferParams(medicalTransferViewModel);
                        detailParams.setConversationId(conversation);
                        return accountService.psnMedicalInsurAcctTransferDetailQuery(detailParams);
                    }
                })
                .compose(SchedulersCompat.<PsnMedicalInsurAcctTransferDetailQueryResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnMedicalInsurAcctTransferDetailQueryResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        mTransDetailView.queryMedicalInsurAcctTransferListFail(biiResultErrorException);
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(PsnMedicalInsurAcctTransferDetailQueryResult insurAcctTransferDetailQueryResult) {
                        mTransDetailView.queryMedicalInsurAcctTransferlListSuccess(DetailModelUtils.copyMedicalTransferResult2UIModel(insurAcctTransferDetailQueryResult));
                    }
                });
    }

    /**
     * 查询交易明细列表 -- 电子现金账户
     *
     * @param financeICTransferViewModel
     */
    @Override
    public void queryFinanceICTransferList(FinanceICTransferViewModel financeICTransferViewModel) {
        PsnFinanceICTransferDetailParams params = DetailModelUtils.buildFinanceICTransferParams(financeICTransferViewModel);
        financeService.psnFinanceICTransferDetail(params)
                .compose(mRxLifecycleManager.<PsnFinanceICTransferDetailResult>bindToLifecycle())
                .compose(SchedulersCompat.<PsnFinanceICTransferDetailResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnFinanceICTransferDetailResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        mTransDetailView.queryFinanceICTransferListFail(biiResultErrorException);
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(PsnFinanceICTransferDetailResult financeICTransferDetailResult) {
                        mTransDetailView.queryFinanceICTransferListSuccess(DetailModelUtils.copyFinanceICTransferResult2UIModel(financeICTransferDetailResult));
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

}
