package com.boc.bocsoft.mobile.bocmobile.buss.transfer.payee.presenter;

import com.boc.bocsoft.mobile.bii.bus.global.model.PSNCreatConversation.PSNCreatConversationParams;
import com.boc.bocsoft.mobile.bii.bus.global.model.PSNGetTokenId.PSNGetTokenIdParams;
import com.boc.bocsoft.mobile.bii.bus.global.service.GlobalService;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransManagePayeeDeletePayee.PsnTransManagePayeeDeletePayeeParams;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransManagePayeeDeletePayee.PsnTransManagePayeeDeletePayeeResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransManagePayeeModifyAlias.PsnTransManagePayeeModifyAliasParams;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransManagePayeeModifyAlias.PsnTransManagePayeeModifyAliasResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransManagePayeeModifyMobile.PsnTransManagePayeeModifyMobileParams;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransManagePayeeModifyMobile.PsnTransManagePayeeModifyMobileResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransManagePayeeQueryPayeeList.PsnTransManagePayeeQueryPayeeListParams;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransManagePayeeQueryPayeeList.PsnTransManagePayeeQueryPayeeListResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransPayeeListqueryForDim.PsnTransPayeeListqueryForDimParams;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransPayeeListqueryForDim.PsnTransPayeeListqueryForDimResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.service.TransferService;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.base.Exception.BIIBaseSubscriber;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.payee.model.PsnTransManagePayeeDeletePayeeViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.payee.model.PsnTransManagePayeeModifyAliasViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.payee.model.PsnTransManagePayeeModifyMobileViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.payee.model.PsnTransManagePayeeQueryPayeeListViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.payee.model.PsnTransPayeeListqueryForDimViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.payee.ui.PayeeDetailContact;
import com.boc.bocsoft.mobile.common.utils.BeanConvertor.BeanConvertor;
import com.boc.bocsoft.mobile.framework.rx.SchedulersCompat;
import com.boc.bocsoft.mobile.framework.rx.lifecycle.RxLifecycleManager;

import java.util.List;

import rx.Observable;
import rx.functions.Func1;

/**
 * Presenter:收款人详情
 * Created by zhx on 2016/7/26
 */
public class PayeeDetailPresenter implements PayeeDetailContact.Presenter {
    private PayeeDetailContact.View mView;
    private RxLifecycleManager mRxLifecycleManager;

    private TransferService transferService;
    private GlobalService globalService;

    private String conversationId;

    private PsnTransManagePayeeQueryPayeeListViewModel viewModel;
    private PsnTransPayeeListqueryForDimViewModel viewModel2;

    public PayeeDetailPresenter(PayeeDetailContact.View view) {
        mView = view;

        mRxLifecycleManager = new RxLifecycleManager();
        transferService = new TransferService();
        globalService = new GlobalService();

        mView.setPresenter(this);
    }


    @Override
    public void psnTransManagePayeeModifyMobile(final PsnTransManagePayeeModifyMobileViewModel viewModel) {
        // 显示修改中的对话框
        ((BussFragment) mView).showLoadingDialog("请稍后...", false);

        PSNCreatConversationParams psnCreatConversationParams = new PSNCreatConversationParams();
        globalService.psnCreatConversation(psnCreatConversationParams)
                .compose(mRxLifecycleManager.<String>bindToLifecycle())
                .flatMap(new Func1<String, Observable<String>>() {
                    @Override
                    public Observable<String> call(String conversation) {
                        PayeeDetailPresenter.this.conversationId = conversation;

                        PSNGetTokenIdParams psnGetTokenIdParams = new PSNGetTokenIdParams();
                        psnGetTokenIdParams.setConversationId(conversation);
                        return globalService.psnGetTokenId(psnGetTokenIdParams);
                    }
                })
                .flatMap(new Func1<String, Observable<PsnTransManagePayeeModifyMobileResult>>() {
                    @Override
                    public Observable<PsnTransManagePayeeModifyMobileResult> call(String token) {
                        PsnTransManagePayeeModifyMobileParams params = new PsnTransManagePayeeModifyMobileParams();

                        BeanConvertor.toBean(viewModel, params);

                        params.setToken(token);
                        params.setConversationId(PayeeDetailPresenter.this.conversationId);
                        return transferService.psnTransManagePayeeModifyMobile(params);
                    }
                })
                .compose(SchedulersCompat.<PsnTransManagePayeeModifyMobileResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnTransManagePayeeModifyMobileResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        ((BussFragment) mView).closeProgressDialog();

                        mView.psnTransManagePayeeModifyMobileFailed(biiResultErrorException);
                    }

                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onNext(PsnTransManagePayeeModifyMobileResult psnTransManagePayeeModifyMobileResult) {
                        ((BussFragment) mView).closeProgressDialog();

                        mView.psnTransManagePayeeModifyMobileSuccess(viewModel);
                    }
                });
    }

    @Override
    public void psnTransManagePayeeModifyAlias(final PsnTransManagePayeeModifyAliasViewModel viewModel) {
        // 显示修改中的对话框
        ((BussFragment) mView).showLoadingDialog("请稍后...", false);

        PSNCreatConversationParams psnCreatConversationParams = new PSNCreatConversationParams();
        globalService.psnCreatConversation(psnCreatConversationParams)
                .compose(mRxLifecycleManager.<String>bindToLifecycle())
                .flatMap(new Func1<String, Observable<String>>() {
                    @Override
                    public Observable<String> call(String conversation) {
                        PayeeDetailPresenter.this.conversationId = conversation;

                        PSNGetTokenIdParams psnGetTokenIdParams = new PSNGetTokenIdParams();
                        psnGetTokenIdParams.setConversationId(conversation);
                        return globalService.psnGetTokenId(psnGetTokenIdParams);
                    }
                })
                .flatMap(new Func1<String, Observable<PsnTransManagePayeeModifyAliasResult>>() {
                    @Override
                    public Observable<PsnTransManagePayeeModifyAliasResult> call(String token) {
                        PsnTransManagePayeeModifyAliasParams params = new PsnTransManagePayeeModifyAliasParams();

                        BeanConvertor.toBean(viewModel, params); // 由PsnTransManagePayeeModifyAliasViewModel生成对应的参数

                        params.setToken(token);
                        params.setConversationId(PayeeDetailPresenter.this.conversationId);
                        return transferService.psnTransManagePayeeModifyAlias(params);
                    }
                })
                .compose(SchedulersCompat.<PsnTransManagePayeeModifyAliasResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnTransManagePayeeModifyAliasResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        ((BussFragment) mView).closeProgressDialog();
                        mView.psnTransManagePayeeModifyAliasFailed(biiResultErrorException);
                    }

                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onNext(PsnTransManagePayeeModifyAliasResult psnTransManagePayeeModifyAliasResult) {
                        ((BussFragment) mView).closeProgressDialog();
                        mView.psnTransManagePayeeModifyAliasSuccess(viewModel);
                    }
                });
    }

    @Override
    public void psnTransManagePayeeDeletePayee(final PsnTransManagePayeeDeletePayeeViewModel viewModel) {
        // 显示修改中的对话框
        ((BussFragment) mView).showLoadingDialog("请稍后...", false);

        PSNCreatConversationParams psnCreatConversationParams = new PSNCreatConversationParams();
        globalService.psnCreatConversation(psnCreatConversationParams)
                .compose(mRxLifecycleManager.<String>bindToLifecycle())
                .flatMap(new Func1<String, Observable<String>>() {
                    @Override
                    public Observable<String> call(String conversation) {
                        PayeeDetailPresenter.this.conversationId = conversation;

                        PSNGetTokenIdParams psnGetTokenIdParams = new PSNGetTokenIdParams();
                        psnGetTokenIdParams.setConversationId(conversation);
                        return globalService.psnGetTokenId(psnGetTokenIdParams);
                    }
                })
                .flatMap(new Func1<String, Observable<PsnTransManagePayeeDeletePayeeResult>>() {
                    @Override
                    public Observable<PsnTransManagePayeeDeletePayeeResult> call(String token) {
                        PsnTransManagePayeeDeletePayeeParams params = new PsnTransManagePayeeDeletePayeeParams();

                        BeanConvertor.toBean(viewModel, params); // 由PsnTransManagePayeeModifyAliasViewModel生成对应的参数

                        params.setToken(token);
                        params.setConversationId(PayeeDetailPresenter.this.conversationId);
                        return transferService.psnTransManagePayeeDeletePayee(params);
                    }
                })
                .compose(SchedulersCompat.<PsnTransManagePayeeDeletePayeeResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnTransManagePayeeDeletePayeeResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        ((BussFragment) mView).closeProgressDialog();
                        mView.psnTransManagePayeeDeletePayeeFailed(biiResultErrorException);
                    }

                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onNext(PsnTransManagePayeeDeletePayeeResult psnTransManagePayeeDeletePayeeResult) {
                        ((BussFragment) mView).closeProgressDialog();
                        mView.psnTransManagePayeeDeletePayeeSuccess(null);
                    }
                });
    }

    @Override
    public void psnTransManagePayeeQueryPayeeList(final PsnTransManagePayeeQueryPayeeListViewModel viewModel) {
        this.viewModel = viewModel;
        PsnTransManagePayeeQueryPayeeListParams params = new PsnTransManagePayeeQueryPayeeListParams();
        params.setBocFlag(viewModel.getBocFlag());

        transferService.getPsnTransManagePayeeQueryPayeeList(params)
                .compose(mRxLifecycleManager.<List<PsnTransManagePayeeQueryPayeeListResult>>bindToLifecycle())
                .compose(SchedulersCompat.<List<PsnTransManagePayeeQueryPayeeListResult>>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<List<PsnTransManagePayeeQueryPayeeListResult>>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        mView.psnTransManagePayeeQueryPayeeListFailed(biiResultErrorException);
                    }

                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onNext(List<PsnTransManagePayeeQueryPayeeListResult> payeeListResults) {
                        transResult2ViewModel(payeeListResults);
                        mView.psnTransManagePayeeQueryPayeeListSuccess(viewModel);
                    }
                });
    }

    @Override
    public void psnTransPayeeListqueryForDim(PsnTransPayeeListqueryForDimViewModel viewModel) {
        // 显示修改中的对话框
        ((BussFragment) mView).showLoadingDialog("请稍后...");

        this.viewModel2 = viewModel;
        PsnTransPayeeListqueryForDimParams params = new PsnTransPayeeListqueryForDimParams();
        params.setBocFlag(viewModel.getBocFlag());
        params.setIsAppointed(viewModel.getIsAppointed()); // 是否定向收款人(0：非定向 1：定向 输入为空时（""）查全部)
        params.setPayeeName(viewModel.getPayeeName());
        params.setCurrentIndex(viewModel.getCurrentIndex());
        params.setPageSize("500");

        transferService.psnTransPayeeListqueryForDim(params)
                .compose(mRxLifecycleManager.<PsnTransPayeeListqueryForDimResult>bindToLifecycle())
                .compose(SchedulersCompat.<PsnTransPayeeListqueryForDimResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnTransPayeeListqueryForDimResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        ((BussFragment) mView).closeProgressDialog();
                        mView.psnTransPayeeListqueryForDimFailed(biiResultErrorException);
                    }

                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onNext(PsnTransPayeeListqueryForDimResult psnTransPayeeListqueryForDimResult) {

                        mView.psnTransPayeeListqueryForDimSuccess(transResult2ViewModel(psnTransPayeeListqueryForDimResult));
                        ((BussFragment) mView).closeProgressDialog();
                    }
                });
    }

    // 把请求结果转换为ViewModel
    private PsnTransPayeeListqueryForDimViewModel transResult2ViewModel(PsnTransPayeeListqueryForDimResult psnTransPayeeListqueryForDimResult) {
        viewModel2.getPayeeEntityList().clear();

        List<PsnTransPayeeListqueryForDimResult.PayeeAccountBean> accountBeanList = psnTransPayeeListqueryForDimResult.getList();

        for (PsnTransPayeeListqueryForDimResult.PayeeAccountBean payeeAccountBean : accountBeanList) {
            PsnTransPayeeListqueryForDimViewModel.PayeeEntity payeeEntity = new PsnTransPayeeListqueryForDimViewModel.PayeeEntity();

            BeanConvertor.toBean(payeeAccountBean, payeeEntity);
            payeeEntity.setPinyin("");

            viewModel2.getPayeeEntityList().add(payeeEntity);
        }

        return viewModel2;
    }

    // 把请求结果转换为ViewModel
    private PsnTransManagePayeeQueryPayeeListViewModel transResult2ViewModel(List<PsnTransManagePayeeQueryPayeeListResult> payeeListResults) {
        List<PsnTransManagePayeeQueryPayeeListViewModel.PayeeEntity> mPayeeEntityList = viewModel.getPayeeEntityList();
        mPayeeEntityList.clear();

        for (PsnTransManagePayeeQueryPayeeListResult result : payeeListResults) {
            PsnTransManagePayeeQueryPayeeListViewModel.PayeeEntity payeeEntity = new PsnTransManagePayeeQueryPayeeListViewModel.PayeeEntity();

            payeeEntity.setAddress(result.getAddress());
            payeeEntity.setType(result.getType());
            payeeEntity.setAccountNumber(result.getAccountNumber());
            payeeEntity.setPayeeCNName(result.getPayeeCNName());
            payeeEntity.setBankName(result.getBankName());
            payeeEntity.setPayeeAlias(result.getPayeeAlias());
            payeeEntity.setMobile(result.getMobile());
            payeeEntity.setBankCode(result.getBankCode());
            payeeEntity.setRegionCode(result.getRegionCode());
            payeeEntity.setPostcode(result.getPostcode());
            payeeEntity.setBankNum(result.getBankNum());
            payeeEntity.setCountryCode(result.getCountryCode());
            payeeEntity.setPayBankCode(result.getPayBankCode());
            payeeEntity.setPayBankName(result.getPayBankName());
            payeeEntity.setAccountName(result.getAccountName());
            payeeEntity.setAccountIbkNum(result.getAccountIbkNum());
            payeeEntity.setSwift(result.getSwift());
            payeeEntity.setPayeetId(result.getPayeetId());
            payeeEntity.setBocFlag(result.getBocFlag());
            payeeEntity.setCnapsCode(result.getCnapsCode());

            payeeEntity.setPinyin(""); // 设置拼音

            mPayeeEntityList.add(payeeEntity);
        }
        return viewModel;
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
