package com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.mypurchase.presenter;

import com.boc.bocsoft.mobile.bii.bus.global.service.GlobalService;
import com.boc.bocsoft.mobile.bii.bus.longshortforex.model.PsnVFGGetBindAccount.PsnVFGGetBindAccountParams;
import com.boc.bocsoft.mobile.bii.bus.longshortforex.model.PsnVFGGetBindAccount.PsnVFGGetBindAccountResult;
import com.boc.bocsoft.mobile.bii.bus.longshortforex.model.PsnVFGPositionInfo.PsnVFGPositionInfoParams;
import com.boc.bocsoft.mobile.bii.bus.longshortforex.model.PsnVFGPositionInfo.PsnVFGPositionInfoResult;
import com.boc.bocsoft.mobile.bii.bus.longshortforex.service.LongShortForexService;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.base.Exception.BIIBaseSubscriber;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.accountmanagement.model.VFGGetBindAccountViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.mypurchase.model.XpadPsnVFGPositionInfoModel;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.mypurchase.ui.MyPurchaseProductContract;
import com.boc.bocsoft.mobile.common.utils.BeanConvertor.BeanConvertor;
import com.boc.bocsoft.mobile.framework.rx.SchedulersCompat;
import com.boc.bocsoft.mobile.framework.rx.lifecycle.RxLifecycleManager;
import com.boc.bocsoft.mobile.framework.ui.RxPresenter;

import java.util.List;

/**
 * 双向宝--我的持仓主页面
 * Created by zc on 2016/12/14
 */
public class MyPurchaseProductPresenter extends RxPresenter implements MyPurchaseProductContract.Presenter {

    private MyPurchaseProductContract.View mMyPurchaseProductView;
    private RxLifecycleManager mRxLifecycleManager;
    private GlobalService globalService;
    private LongShortForexService mLongShortForexService;

    public MyPurchaseProductPresenter(MyPurchaseProductContract.View view) {
        this.mMyPurchaseProductView = view;
        mMyPurchaseProductView.setPresenter(this);
        mRxLifecycleManager = new RxLifecycleManager();
        globalService = new GlobalService();
        mLongShortForexService = new LongShortForexService();
    }

    //查询交易账户
    @Override
    public void psnVfgGetBindAccount(final VFGGetBindAccountViewModel model) {
        PsnVFGGetBindAccountParams psnVFGGetBindAccountParams = new PsnVFGGetBindAccountParams();

        mLongShortForexService.psnVFGGetBindAccount(psnVFGGetBindAccountParams)
                .compose(SchedulersCompat.<PsnVFGGetBindAccountResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnVFGGetBindAccountResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        mMyPurchaseProductView.psnXpadVfgGetBindAccountFail(biiResultErrorException);
                    }

                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onNext(PsnVFGGetBindAccountResult psnVFGGetRegCurrencyResults) {
                        BeanConvertor.toBean(psnVFGGetRegCurrencyResults, model);
                        mMyPurchaseProductView.psnXpadVfgGetBindAccountSuccess(model);
                    }
                });
    }
    //查询持仓列表信息
    @Override
    public void psnVFGPositionInfo(final XpadPsnVFGPositionInfoModel vfgPositionInfoModel) {

        PsnVFGPositionInfoParams positionInfoParams = new PsnVFGPositionInfoParams();

        mLongShortForexService.PsnVFGPositionInfo(positionInfoParams)
                .compose(SchedulersCompat.<List<PsnVFGPositionInfoResult>>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<List<PsnVFGPositionInfoResult>>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        mMyPurchaseProductView.psnXpadVfgGetBindAccountFail(biiResultErrorException);
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(List<PsnVFGPositionInfoResult> positionInfoResults) {
                        System.out.println("zhangcheng 持仓信息成功");
                        BeanConvertor.toBean(positionInfoResults,vfgPositionInfoModel);
//                        XpadPsnVFGPositionInfoModel aa = new XpadPsnVFGPositionInfoModel();
//                        aa.setDetails(XpadPsnVFGPositionInfoModel);
                        mMyPurchaseProductView.psnXpadVFGPositionInfoSuccess(vfgPositionInfoModel);
                    }
                });
    }

}
