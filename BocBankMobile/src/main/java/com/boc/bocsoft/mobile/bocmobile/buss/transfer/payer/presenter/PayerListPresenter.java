package com.boc.bocsoft.mobile.bocmobile.buss.transfer.payer.presenter;

import android.util.Log;

import com.boc.bocsoft.mobile.bii.bus.global.model.PSNCreatConversation.PSNCreatConversationParams;
import com.boc.bocsoft.mobile.bii.bus.global.model.PSNGetTokenId.PSNGetTokenIdParams;
import com.boc.bocsoft.mobile.bii.bus.global.service.GlobalService;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransActDeletePayer.PsnTransActDeletePayerParams;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransActDeletePayer.PsnTransActDeletePayerResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransActModifyPayerMobile.PsnTransActModifyPayerMobileParams;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransActModifyPayerMobile.PsnTransActModifyPayerMobileResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransActQueryPayerList.PsnTransActQueryPayerListParams;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransActQueryPayerList.PsnTransActQueryPayerListResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.service.TransferService;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.base.Exception.BIIBaseSubscriber;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.payer.model.PayerListModel;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.payer.ui.PayerListContract;
import com.boc.bocsoft.mobile.framework.rx.SchedulersCompat;
import com.boc.bocsoft.mobile.framework.rx.lifecycle.RxLifecycleManager;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by liuyang on 2016/7/21.
 */
public class PayerListPresenter implements PayerListContract.Presenter {

    private RxLifecycleManager mRxLifecycleManager;
    private PayerListContract.QueryList queryList;
    private PayerListContract.DetailedOperate detailedOperate;

    private GlobalService globalService;
    private TransferService transferService;
    private String conversationID , getConversationPre;

    public PayerListPresenter() {
        mRxLifecycleManager = new RxLifecycleManager();
        transferService = new TransferService();
        globalService = new GlobalService();

    }


    public PayerListPresenter(PayerListContract.QueryList query) {
        this();
        queryList = query;
//        queryList.setPresenter(this);
    }

    public PayerListPresenter(PayerListContract.DetailedOperate operate) {
        this();
        detailedOperate = operate;
//        detailedOperate.setPresenter(this);
    }


    /**
     * 查询付款人列表
     *
     * @param
     */
    @Override
    public void queryPayerList() {
        PsnTransActQueryPayerListParams params = new PsnTransActQueryPayerListParams();
        transferService.psnTransActQueryPayerList(params)
                .compose(mRxLifecycleManager.<List<PsnTransActQueryPayerListResult>>bindToLifecycle())
                .compose(SchedulersCompat.<List<PsnTransActQueryPayerListResult>>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<List<PsnTransActQueryPayerListResult>>() {

                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onNext(List<PsnTransActQueryPayerListResult> onNext) {
                        queryList.queryPayerListSuccess(buildPayerList2UIModel(onNext));
                        Log.i("ly", "-----------onNext-付款人列表------------");
                    }

                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        queryList.queryPayerListFail(biiResultErrorException);
                        Log.i("ly", "-----------ErrorException-付款人列表------------");
                    }
                });
    }

    /**
     * 修改手机号
     * 接口调用顺序：PSNCreatConversation、PSNGetTokenId、psnTransActModifyPayerMobile
     *
     * @param payerViewModel
     */
    @Override
    public void revisePayerList(final PayerListModel payerViewModel) {
        PSNCreatConversationParams psnCreatConversationParams = new PSNCreatConversationParams();
        globalService.psnCreatConversation(psnCreatConversationParams)
                .compose(mRxLifecycleManager.<String>bindToLifecycle())
                .flatMap(new Func1<String, Observable<String>>() {
                    @Override
                    public Observable<String> call(String conversation) {
                        PSNGetTokenIdParams psnGetTokenIdParams = new PSNGetTokenIdParams();
                        conversationID = conversation;
                        psnGetTokenIdParams.setConversationId(conversationID);
                        return globalService.psnGetTokenId(psnGetTokenIdParams);
                    }
                })
                .flatMap(new Func1<String, Observable<PsnTransActModifyPayerMobileResult>>() {
                    @Override
                    public Observable<PsnTransActModifyPayerMobileResult> call(String token) {
                        payerViewModel.setToken(token);
//                        payerViewModel.setConversationId(conversation);

                        return transferService.psnTransActModifyPayerMobile(buildRevisePayerParams(payerViewModel));
                    }
                })
                .compose(SchedulersCompat.<PsnTransActModifyPayerMobileResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnTransActModifyPayerMobileResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        detailedOperate.revisePayerFail(biiResultErrorException);
                        Log.i("ly", "-----------ErrorException-修改手机号------------");
                    }

                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onNext(PsnTransActModifyPayerMobileResult psnTransActModifyPayerMobileResult) {
                        detailedOperate.revisePayerSuccess(psnTransActModifyPayerMobileResult);
                        Log.i("ly", "-----------onNext-修改手机号------------");
                    }


                });
    }


    /**
     * 删除付款人
     * 接口调用顺序：PSNCreatConversation、PSNGetTokenId、PsnTransActDeletePayer
     *
     * @param deletePayerViewModel
     */
    @Override
    public void deletePayerList(final PayerListModel deletePayerViewModel) {
        PSNCreatConversationParams psnCreatConversationParams = new PSNCreatConversationParams();
        globalService.psnCreatConversation(psnCreatConversationParams)
                .compose(mRxLifecycleManager.<String>bindToLifecycle())
                .flatMap(new Func1<String, Observable<String>>() {
                    @Override
                    public Observable<String> call(String conversation) {
                        PSNGetTokenIdParams psnGetTokenIdParams = new PSNGetTokenIdParams();
                        getConversationPre = conversation;
                        psnGetTokenIdParams.setConversationId(getConversationPre);
                        return globalService.psnGetTokenId(psnGetTokenIdParams);
                    }
                })
                .flatMap(new Func1<String, Observable<PsnTransActDeletePayerResult>>() {
                    @Override
                    public Observable<PsnTransActDeletePayerResult> call(String token) {
                        deletePayerViewModel.setToken(token);
                        return transferService.psnTransActDeletePayer(buildDeletePayerParams(deletePayerViewModel));
                    }
                })
                .compose(SchedulersCompat.<PsnTransActDeletePayerResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnTransActDeletePayerResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        detailedOperate.deletePayerFail(biiResultErrorException);
                        Log.i("ly", "-----------ErrorException-删除付款人------------");
                    }

                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onNext(PsnTransActDeletePayerResult psnMobileWithdrawalQueryResult) {
                        detailedOperate.deletePayerSuccess(psnMobileWithdrawalQueryResult);
                        Log.i("ly", "-----------onNext-删除付款人------------");
                    }
                });
    }


    /**
     * 付款人列表数据封装
     *
     * @param result
     * @return
     */
    private PayerListModel buildPayerList2UIModel(List<PsnTransActQueryPayerListResult> result) {
        PayerListModel listModel = new PayerListModel();
        if (result == null) {
            return listModel;
        }
        List<PayerListModel.ResultBean> viewResultBeanList = new ArrayList<PayerListModel.ResultBean>();
        for (int i = 0; i < result.size(); i++) {
            PayerListModel.ResultBean listBean = new PayerListModel.ResultBean();
            PsnTransActQueryPayerListResult resultItem = result.get(i);
            listBean.setIdentifyType(resultItem.getIdentifyType());
            listBean.setPayerCustomerId(resultItem.getPayerCustomerId());
            listBean.setPayerId(resultItem.getPayerId());
            listBean.setPayerMobile(resultItem.getPayerMobile());
            listBean.setPayerName(resultItem.getPayerName());
            viewResultBeanList.add(listBean);
        }

        listModel.setResult(viewResultBeanList);
        return listModel;
    }


    /**
     * 修改手机号
     * 接口调用顺序：PSNCreatConversation、PSNGetTokenId、psnTransActModifyPayerMobile
     *
     * @param payerViewModel
     * @return
     */
    private PsnTransActModifyPayerMobileParams buildRevisePayerParams(PayerListModel payerViewModel) {
        PsnTransActModifyPayerMobileParams psnTransActModifyPayerMobileParams = new PsnTransActModifyPayerMobileParams();
       psnTransActModifyPayerMobileParams.setConversationId(conversationID);
        psnTransActModifyPayerMobileParams.setPayerId(String.valueOf(payerViewModel.getPayerId()));
        psnTransActModifyPayerMobileParams.setPayerMobile(payerViewModel.getPayerMobile());
        psnTransActModifyPayerMobileParams.setToken(payerViewModel.getToken());

        return psnTransActModifyPayerMobileParams;
    }

    /**
     * 封装请求参数：
     * 删除付款人
     *
     * @param payerListModel
     * @return PsnTransActDeletePayerParams
     */
    private PsnTransActDeletePayerParams buildDeletePayerParams(PayerListModel payerListModel) {
        PsnTransActDeletePayerParams psnTransActDeletePayerParams = new PsnTransActDeletePayerParams();
        psnTransActDeletePayerParams.setConversationId(getConversationPre);
        psnTransActDeletePayerParams.setToken(payerListModel.getToken());
        psnTransActDeletePayerParams.setPayerId(String.valueOf(payerListModel.getPayerId()));
        return psnTransActDeletePayerParams;
    }


    @Override
    public void subscribe() {

    }

    @Override
    public void unsubscribe() {

    }
}
