package com.boc.bocsoft.mobile.bocmobile.buss.creditcard.attachedcardmanagement.presenter;

import android.util.Log;

import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdQueryMasterAndSupplInfo.PsnCrcdQueryMasterAndSupplInfoParams;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdQueryMasterAndSupplInfo.PsnCrcdQueryMasterAndSupplInfoResult;
import com.boc.bocsoft.mobile.bii.bus.crcd.service.CrcdService;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.base.Exception.BIIBaseSubscriber;
import com.boc.bocsoft.mobile.framework.rx.SchedulersCompat;
import com.boc.bocsoft.mobile.framework.rx.lifecycle.RxLifecycleManager;

import java.util.List;

/**
 * Name: liukai
 * Time：2016/12/5 9:56.
 * Created by lk7066 on 2016/12/5.
 * It's used to 附属卡信息查询和交易明细查询
 */

public class AttCardMainPresenter implements AttCardMainContract.AttCardMainPresenter{

    private RxLifecycleManager mRxLifecycleManager;

    /**
     * 信用卡service
     */
    private CrcdService crcdService;

    private AttCardMainContract.AttCardMainView attCardMainView;

    public AttCardMainPresenter(AttCardMainContract.AttCardMainView mainView){
        attCardMainView = mainView;
        attCardMainView.setPresenter(this);
        crcdService = new CrcdService();
        mRxLifecycleManager= new RxLifecycleManager();
    }

    /**
     * 主附卡信息查询，查询所有的附属卡信息
     * */
    @Override
    public void queryMasterAndSupplInfo(String accountId) {

        PsnCrcdQueryMasterAndSupplInfoParams masterAndSupplInfoParams = new PsnCrcdQueryMasterAndSupplInfoParams();
        masterAndSupplInfoParams.setAccountId(accountId);

        crcdService.psnCrcdQueryMasterAndSupplInfo(masterAndSupplInfoParams)
                .compose(mRxLifecycleManager.<List<PsnCrcdQueryMasterAndSupplInfoResult>>bindToLifecycle())
                .compose(SchedulersCompat.<List<PsnCrcdQueryMasterAndSupplInfoResult>>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<List<PsnCrcdQueryMasterAndSupplInfoResult>>(){

                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        Log.d("--liukai--主附卡信息查询", "失败");
                        attCardMainView.masterAndSupplInfoFailed(biiResultErrorException);
                    }

                    @Override
                    public void onCompleted() {
                        Log.d("--liukai--主附卡信息查询", "完成情况");
                    }

                    @Override
                    public void onNext(List<PsnCrcdQueryMasterAndSupplInfoResult> psnCrcdQueryMasterAndSupplInfoResults) {
                        Log.d("--liukai--主附卡信息查询", "成功");
                        attCardMainView.masterAndSupplInfoSuccess(psnCrcdQueryMasterAndSupplInfoResults);
                    }

                });

    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unsubscribe() {

    }

}
