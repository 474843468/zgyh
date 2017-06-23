package com.boc.bocsoft.mobile.bocmobile.buss.creditcard.myinstallment.presenter;

import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdDividedPayHisQry.PsnCrcdDividedPayHisQryParams;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdDividedPayHisQry.PsnCrcdDividedPayHisQryResult;
import com.boc.bocsoft.mobile.bii.bus.crcd.service.CrcdService;
import com.boc.bocsoft.mobile.bii.bus.global.model.PSNCreatConversation.PSNCreatConversationParams;
import com.boc.bocsoft.mobile.bii.bus.global.service.GlobalService;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.base.Exception.BIIBaseSubscriber;
import com.boc.bocsoft.mobile.bocmobile.buss.creditcard.myinstallment.model.InstallmentRecordModel;
import com.boc.bocsoft.mobile.bocmobile.buss.creditcard.myinstallment.ui.InstallmentHistoryContract;
import com.boc.bocsoft.mobile.common.utils.BeanConvertor.BeanConvertor;
import com.boc.bocsoft.mobile.common.utils.PublicUtils;
import com.boc.bocsoft.mobile.framework.rx.SchedulersCompat;
import com.boc.bocsoft.mobile.framework.ui.RxPresenter;

import java.util.List;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by yangle on 2016/11/18.
 */
public class InstallmentHistoryPresenter extends RxPresenter implements InstallmentHistoryContract.Presenter{

    private final InstallmentHistoryContract.View mView;
    private final GlobalService mGlobalService;
    private final CrcdService mCrcdService;
    private String mConversationId;

    public InstallmentHistoryPresenter(InstallmentHistoryContract.View view) {
        this.mView = view;
        mGlobalService = new GlobalService();
        mCrcdService = new CrcdService();
    }

    @Override
    public void loadDividedPayRecords() {
        mView.showLoading();
        mGlobalService.psnCreatConversation(new PSNCreatConversationParams())
                .flatMap(new Func1<String, Observable<PsnCrcdDividedPayHisQryResult>>() {
                    @Override
                    public Observable<PsnCrcdDividedPayHisQryResult> call(String conversationId) {
                        mConversationId = conversationId;
                        return mCrcdService.psnCrcdDividedPayHisQry(generateRequestParams());
                    }
                })
                .compose(this.<PsnCrcdDividedPayHisQryResult>bindToLifecycle())
                .compose(SchedulersCompat.<PsnCrcdDividedPayHisQryResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnCrcdDividedPayHisQryResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        mView.closeLoading();
                        mView.loadFailed();
                    }

                    @Override
                    public void onCompleted() {
                        mView.closeLoading();
                    }

                    @Override
                    public void onNext(PsnCrcdDividedPayHisQryResult psnCrcdDividedPayHisQryResult) {
                        processResult(psnCrcdDividedPayHisQryResult);
                    }
                });
    }

    private void processResult(PsnCrcdDividedPayHisQryResult result) {
        if (result == null) {
            mView.showNoRecord();
            return;
        }
        List<PsnCrcdDividedPayHisQryResult.ListBean> resultList = result.getList();
        if (!PublicUtils.isEmpty(resultList)) {
            mView.getViewModel().setRecordNumber(result.getRecordNumber());
            for (PsnCrcdDividedPayHisQryResult.ListBean listBean : resultList) {
                InstallmentRecordModel recordModel = BeanConvertor.toBean(listBean, new InstallmentRecordModel());
                mView.getViewModel().getList().add(recordModel);
            }
            //设置当前currentIndex
            mView.getViewModel().setCurrentIndex(mView.getViewModel().getList().size() - 1);
        }

        if (mView.getViewModel().isEmpty()) {
            mView.showNoRecord();
        } else {
            mView.showAndUpdateRecords();
            // 第一次是true重新查询之后,上拉加载数据时都应置为false,从缓存中查询,否则数据重复
            if (mView.getViewModel().isRefreshFlagTrue()) {
                mView.getViewModel().setRefreshFlag(false);
            }
        }
    }

    private PsnCrcdDividedPayHisQryParams generateRequestParams() {
        PsnCrcdDividedPayHisQryParams params = new PsnCrcdDividedPayHisQryParams();
        params.setConversationId(mConversationId);
        params.setAccountId(mView.getViewModel().getAccountId());
        params.set_refresh(mView.getViewModel().get_refresh());
        params.setCurrentIndex(mView.getViewModel().getCurrentIndexStr());
        params.setPageSize(mView.getViewModel().getPageSizeStr());
        return params;
    }
}
