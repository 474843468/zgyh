package com.boc.bocsoft.mobile.bocmobile.buss.equickpay.presenter;

import com.boc.bocma.tools.LogUtil;
import com.boc.bocsoft.mobile.bii.bus.equickpay.model.PsnCardBrandQuery.PsnCardBrandQueryParams;
import com.boc.bocsoft.mobile.bii.bus.equickpay.model.PsnHCEQuickPassApplication.PsnHCEQuickPassApplicationParams;
import com.boc.bocsoft.mobile.bii.bus.equickpay.model.PsnHCEQuickPassApplication.PsnHCEQuickPassApplicationResult;
import com.boc.bocsoft.mobile.bii.bus.equickpay.service.EquickpayService;
import com.boc.bocsoft.mobile.bii.bus.global.model.PSNCreatConversation.PSNCreatConversationParams;
import com.boc.bocsoft.mobile.bii.bus.global.service.GlobalService;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.base.Exception.BIIBaseSubscriber;
import com.boc.bocsoft.mobile.bocmobile.base.model.AccountBean;
import com.boc.bocsoft.mobile.bocmobile.buss.equickpay.model.AddNewCardViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.equickpay.model.ApplicationModel;
import com.boc.bocsoft.mobile.bocmobile.buss.equickpay.model.HceTransactionViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.equickpay.ui.hceaddcard.HceAddNewCardContract;
import com.boc.bocsoft.mobile.bocmobile.buss.equickpay.util.HceConstants;
import com.boc.bocsoft.mobile.bocmobile.buss.equickpay.util.HceUtil;
import com.boc.bocsoft.mobile.framework.rx.SchedulersCompat;
import com.boc.bocsoft.mobile.framework.ui.RxPresenter;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by yangle on 2016/12/15.
 * 描述:申请新卡presenter
 */
public class HceAddNewCardPresent extends RxPresenter implements HceAddNewCardContract.Present{

    private final HceAddNewCardContract.View mView;
    private final EquickpayService mEquickpayService;
    private final GlobalService mGlobalService;

    public HceAddNewCardPresent(HceAddNewCardContract.View view) {
        this.mView = view;
        mEquickpayService = new EquickpayService();
        mGlobalService = new GlobalService();
    }

    private PsnCardBrandQueryParams generateQueryBrandParams(AccountBean account, String conversationId) {
        PsnCardBrandQueryParams params = new PsnCardBrandQueryParams();
        params.setAccountId(account.getAccountId());
        params.setCardNo(account.getAccountNumber());
        params.setConversationId(conversationId);
        return null;
    }

    @Override
    public void applyHce() {
        final AddNewCardViewModel model = mView.getModel();

        mView.showLoading();
        mGlobalService.psnCreatConversation(new PSNCreatConversationParams())
                .flatMap(new Func1<String, Observable<PsnHCEQuickPassApplicationResult>>() {
                    @Override
                    public Observable<PsnHCEQuickPassApplicationResult> call(String conversationId) {
                        model.setConversationId(conversationId);
                        return mEquickpayService.psnHCEQuickPassApplication(generateApplicationParams(model));
                    }
                })
                .map(new Func1<PsnHCEQuickPassApplicationResult, ApplicationModel>() {
                    @Override
                    public ApplicationModel call(PsnHCEQuickPassApplicationResult applicationResult) {
                        return convertToViewModel(applicationResult);
                    }
                })
                .compose(SchedulersCompat.<ApplicationModel>applyIoSchedulers())
                .compose(this.<ApplicationModel>bindToLifecycle())
                .subscribe(new BIIBaseSubscriber<ApplicationModel>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        mView.closeLoading();
                        LogUtil.e("============applyHce================="+biiResultErrorException.getErrorMessage());
                    }

                    @Override
                    public void onCompleted() {
                        mView.closeLoading();
                    }

                    @Override
                    public void onNext(ApplicationModel applicationModel) {
//                            mView.applyHceSuccess(viewModel);
                        LogUtil.e("============onNext================="+applicationModel.getCardData());
                       // processApplicationResult(applicationModel);
                        mView.applyHceSuccess(toTransactionModel(mView.getModel(),applicationModel));
                    }
                });
    }

    private ApplicationModel convertToViewModel(PsnHCEQuickPassApplicationResult result) {
        if (result == null) {
            throw new NullPointerException("PsnHCEQuickPassApplicationResult == null");
        }
        ApplicationModel applicationModel = new ApplicationModel();
        applicationModel.setMasterCardNo(result.getMasterCardNo());
        applicationModel.setSlaveCardNo(result.getSlaveCardNo());
        applicationModel.setRecordNumber(result.getRecordNumber());
        applicationModel.setCardData(result.getCardData());
        applicationModel.setKeys(getKeys(result.getKeyList()));
        return applicationModel;
    }

    private void processApplicationResult(ApplicationModel applicationModel) {
        if (applicationModel == null) {
            throw new NullPointerException("PsnHCEQuickPassApplicationResult == null");
        }
        // TODO: 2016/12/26 初始化hce环境数据,成功去激活,失败提示申请失败返回首页卡列表
        // 应用类型值
        int appType = mView.getModel().getCardBrandModel().getAppTypeValue();
        try {
            String initResult = HceUtil.initHceEnv(mView.getContext(), applicationModel, appType);
            LogUtil.d("initResult == " + initResult);
            if (HceConstants.HCE_SUCCEED.equals(initResult)) {
                // 本地化成功则申请成功
                mView.applyHceSuccess(toTransactionModel(mView.getModel(),applicationModel));
            } else {
                // 本地化失败则申请失败
                mView.applyHceFailure();
            }
        } catch (Exception e) {
            LogUtil.e(e.getMessage());
            e.printStackTrace();
        }
    }

    private List<String> getKeys(List<PsnHCEQuickPassApplicationResult.HceKey> keyList) {
        List<String> keys = new ArrayList<>();
        for (PsnHCEQuickPassApplicationResult.HceKey hceKey : keyList) {
            keys.add(hceKey.getKeyInfo());
        }
        return keys;
    }

    // 转化到交易model(用于确认界面与结果界面)
    private HceTransactionViewModel toTransactionModel(AddNewCardViewModel addModel,ApplicationModel applicationModell) {
        HceTransactionViewModel transactionModel = new HceTransactionViewModel();
        transactionModel.setDeviceNo(addModel.getDeviceNo());
        transactionModel.setSingleQuota(addModel.getSingleQuota());
        transactionModel.setPerDayQuota(addModel.getPerDayQuota());
        transactionModel.setCardBrandModel(addModel.getCardBrandModel());
        transactionModel.setConversationId(addModel.getConversationId());

        transactionModel.setMasterCardNo(applicationModell.getMasterCardNo());
        transactionModel.setSlaveCardNo(applicationModell.getSlaveCardNo());
        transactionModel.setConversationId(addModel.getConversationId());
        transactionModel.setCardBrandModel(addModel.getCardBrandModel());
        transactionModel.setFrom(HceTransactionViewModel.From.APPLY);
        return transactionModel;
    }

    // 构造请求参数
    private PsnHCEQuickPassApplicationParams generateApplicationParams(AddNewCardViewModel model) {
        PsnHCEQuickPassApplicationParams applicationParams = new PsnHCEQuickPassApplicationParams();
        applicationParams.setAccountId(model.getAccountId());
        applicationParams.setMasterCardNo(model.getMasterCardNo());
        applicationParams.setCardBrand(model.getCardBrandModel().getCardBrandHceValue());
        applicationParams.setDeviceNo(model.getDeviceNo());
        applicationParams.setSingleQuota(model.getSingleQuota());
        applicationParams.setKeyNum(model.getKeyNum());
        applicationParams.setConversationId(model.getConversationId());
        return applicationParams;
    }



}
