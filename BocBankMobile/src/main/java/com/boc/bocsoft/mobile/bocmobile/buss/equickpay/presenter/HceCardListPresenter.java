package com.boc.bocsoft.mobile.bocmobile.buss.equickpay.presenter;

import com.boc.bocsoft.mobile.bii.bus.equickpay.model.PsnHCEQuickPassCancel.PsnHCEQuickPassCancelParams;
import com.boc.bocsoft.mobile.bii.bus.equickpay.model.PsnHCEQuickPassCancel.PsnHCEQuickPassCancelResult;
import com.boc.bocsoft.mobile.bii.bus.equickpay.model.PsnHCEQuickPassListQuery.PsnHCEQuickPassListQueryParams;
import com.boc.bocsoft.mobile.bii.bus.equickpay.model.PsnHCEQuickPassListQuery.PsnHCEQuickPassListQueryResult;
import com.boc.bocsoft.mobile.bii.bus.equickpay.model.PsnHCEQuickPassListQueryOutlay.PsnHCEQuickPassListQueryOutlayParams;
import com.boc.bocsoft.mobile.bii.bus.equickpay.model.PsnHCEQuickPassListQueryOutlay.PsnHCEQuickPassListQueryOutlayResult;
import com.boc.bocsoft.mobile.bii.bus.equickpay.model.PsnHCEQuickPassLukLoad.PsnHCEQuickPassLukLoadParams;
import com.boc.bocsoft.mobile.bii.bus.equickpay.model.PsnHCEQuickPassLukLoad.PsnHCEQuickPassLukLoadResult;
import com.boc.bocsoft.mobile.bii.bus.equickpay.service.EquickpayService;
import com.boc.bocsoft.mobile.bii.bus.global.model.PSNCreatConversation.PSNCreatConversationParams;
import com.boc.bocsoft.mobile.bii.bus.global.service.GlobalService;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.base.Exception.BIIBaseSubscriber;
import com.boc.bocsoft.mobile.bocmobile.base.utils.LogUtils;
import com.boc.bocsoft.mobile.bocmobile.buss.equickpay.model.hcecardlist.HceCardListQueryViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.equickpay.ui.hcecardlist.HceCardListContact;
import com.boc.bocsoft.mobile.common.utils.BeanConvertor.BeanConvertor;
import com.boc.bocsoft.mobile.framework.rx.SchedulersCompat;
import com.boc.bocsoft.mobile.framework.ui.RxPresenter;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.functions.Func1;

/**
 * Presenter：Hce Card 列表
 * Created by gengjunying on 2016/11/29
 */
public class HceCardListPresenter extends RxPresenter implements HceCardListContact.Presenter {
    private HceCardListContact.View mView;
    private GlobalService globalService;
    private EquickpayService equickpayService;
    private String mConversationId;

    public HceCardListPresenter(HceCardListContact.View view) {
        this.mView = view;
        mView.setPresenter(this);
        equickpayService = new EquickpayService();
        globalService = new GlobalService();
    }


    //获取登录后的hce卡列表数据
    @Override
    public void HCEQuickPassListQuery(final String deviceNo) {
        globalService.psnCreatConversation(new PSNCreatConversationParams())
                .flatMap(new Func1<String, Observable<PsnHCEQuickPassListQueryResult>>() {
                    @Override
                    public Observable<PsnHCEQuickPassListQueryResult> call(String conversationId) {
                        PsnHCEQuickPassListQueryParams psnHCEQuickPassListQueryParams = new PsnHCEQuickPassListQueryParams();
                        psnHCEQuickPassListQueryParams.setDeviceNo(deviceNo);
                        psnHCEQuickPassListQueryParams.setConversationId(conversationId);


                        mConversationId = conversationId;

                        return equickpayService.PsnHCEQuickPassListQuery(psnHCEQuickPassListQueryParams);
                    }
                })
                .compose(this.<PsnHCEQuickPassListQueryResult>bindToLifecycle())
                .compose(SchedulersCompat.<PsnHCEQuickPassListQueryResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnHCEQuickPassListQueryResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        LogUtils.d("error", biiResultErrorException.getErrorMessage());
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(PsnHCEQuickPassListQueryResult psnHCEQuickPassListQueryResults) {
                        mView.HCEQuickPassListQuerySuccess(buildHceCardListQueryViewModel(psnHCEQuickPassListQueryResults));
                    }
                });
    }


    //获取未登录的hce卡列表数据
    @Override
    public void HCEQuickPassListNoLoginQuery(final String deviceNo, final String identifyType, final String identityNumber) {

        PsnHCEQuickPassListQueryOutlayParams psnHCEQuickPassListQueryOutlayParams = new PsnHCEQuickPassListQueryOutlayParams();
        psnHCEQuickPassListQueryOutlayParams.setDeviceNo(deviceNo);
        psnHCEQuickPassListQueryOutlayParams.setIdentifyType(identifyType);
        psnHCEQuickPassListQueryOutlayParams.setIdentityNumber(identityNumber);

        equickpayService.PsnHCEQuickPassListQueryOutlay(psnHCEQuickPassListQueryOutlayParams)
                .compose(this.<PsnHCEQuickPassListQueryOutlayResult>bindToLifecycle())
                .compose(SchedulersCompat.<PsnHCEQuickPassListQueryOutlayResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnHCEQuickPassListQueryOutlayResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        LogUtils.d("error", biiResultErrorException.getErrorMessage());
                    }

                    @Override
                    public void onCompleted() {
                        LogUtils.d("onCompleted", "onCompleted");
                    }

                    @Override
                    public void onNext(PsnHCEQuickPassListQueryOutlayResult psnHCEQuickPassListQueryResults) {
                        mView.HCEQuickPassListQuerySuccess(buildHceCardListQueryNoLoginViewModel(psnHCEQuickPassListQueryResults));
                    }
                });
    }


    //注销
    @Override
    public void PsnHCEQuickPassCancel(String conversationId,  String masterCardNo, String slaveCardNo) {

        PsnHCEQuickPassCancelParams psnHCEQuickPassCancelParams = new PsnHCEQuickPassCancelParams();
        psnHCEQuickPassCancelParams.setMasterCardNo(masterCardNo);
        psnHCEQuickPassCancelParams.setSlaveCardNo(slaveCardNo);
        psnHCEQuickPassCancelParams.setConversationId(conversationId);


        equickpayService.PsnHCEQuickPassCancel(psnHCEQuickPassCancelParams)
                .compose(this.<PsnHCEQuickPassCancelResult>bindToLifecycle())
                .compose(SchedulersCompat.<PsnHCEQuickPassCancelResult>applyIoSchedulers())
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
                        mView.PsnHCEQuickPassCancelSuccess();
                    }
                });
    }


    //获取luk 数据
    @Override
    public void PsnHCEQuickPassLukLoad(String deviceNo, String slaveCardNo, String cardSeq, String keyNum) {
        PsnHCEQuickPassLukLoadParams psnHCEQuickPassLukLoadParams = new PsnHCEQuickPassLukLoadParams();
        psnHCEQuickPassLukLoadParams.setDeviceNo("1111");
        psnHCEQuickPassLukLoadParams.setCardSeq("11");
        psnHCEQuickPassLukLoadParams.setKeyNum("11");
        psnHCEQuickPassLukLoadParams.setSlaveCardNo("5324586557002565");

        equickpayService.PsnHCEQuickPassLukLoad(psnHCEQuickPassLukLoadParams)
                .compose(this.<PsnHCEQuickPassLukLoadResult>bindToLifecycle())
                .compose(SchedulersCompat.<PsnHCEQuickPassLukLoadResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnHCEQuickPassLukLoadResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        LogUtils.d("error", biiResultErrorException.getErrorMessage());
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(PsnHCEQuickPassLukLoadResult psnHCEQuickPassLukLoadResult) {
                        mView.PsnHCEQuickPassLukLoadSuccess(psnHCEQuickPassLukLoadResult.getKeyInfoList());
                    }
                });





    }


    //已登录 构建ViewModel
    private List<HceCardListQueryViewModel> buildHceCardListQueryViewModel(PsnHCEQuickPassListQueryResult psnHCEQuickPassListQueryResult) {

        List<HceCardListQueryViewModel> list = new ArrayList<>();

        for (int i = 0; i < psnHCEQuickPassListQueryResult.getQuickPassList().size(); i++) {
            list.add(BeanConvertor.fromBean(psnHCEQuickPassListQueryResult.getQuickPassList().get(i), new HceCardListQueryViewModel()));
        }
        return list;
    }


    //未登录 构建ViewModel
    private List<HceCardListQueryViewModel> buildHceCardListQueryNoLoginViewModel(PsnHCEQuickPassListQueryOutlayResult psnHCEQuickPassListQueryOutlayResult) {

        List<HceCardListQueryViewModel> list = new ArrayList<>();

        for (int i = 0; i < psnHCEQuickPassListQueryOutlayResult.getQuickPassList().size(); i++) {
            list.add(BeanConvertor.fromBean(psnHCEQuickPassListQueryOutlayResult.getQuickPassList().get(i), new HceCardListQueryViewModel()));
        }
        return list;
    }

    @Override
    public String getConversationId() {
        return mConversationId;
    }
}
