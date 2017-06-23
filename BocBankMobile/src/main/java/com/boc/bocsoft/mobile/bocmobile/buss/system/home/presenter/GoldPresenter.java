package com.boc.bocsoft.mobile.bocmobile.buss.system.home.presenter;

import com.boc.bocsoft.mobile.bii.bus.global.model.PsnGetAllExchangeRatesOutlay.PsnGetAllExchangeRatesOutlayParams;
import com.boc.bocsoft.mobile.bii.bus.global.model.PsnGetAllExchangeRatesOutlay.PsnGetAllExchangeRatesOutlayResult;
import com.boc.bocsoft.mobile.bii.bus.global.service.GlobalService;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.base.Exception.BIIBaseSubscriber;
import com.boc.bocsoft.mobile.bocmobile.base.model.GoldBean;
import com.boc.bocsoft.mobile.bocmobile.buss.system.home.model.getallexchangeratesoutlay.GetAllExchangeRatesOutlayParams;
import com.boc.bocsoft.mobile.bocmobile.buss.system.home.ui.GoldContract;
import com.boc.bocsoft.mobile.framework.rx.SchedulersCompat;
import com.boc.bocsoft.mobile.framework.ui.RxPresenter;

import java.util.ArrayList;
import java.util.List;

/**贵金属
 * Created by gwluo on 2016/8/29.
 */
public class GoldPresenter extends RxPresenter implements GoldContract.Presenter {
    GoldContract.View mView;
    GlobalService mGoldServce;

    public GoldPresenter(GoldContract.View view) {
        mView = view;
        mGoldServce = new GlobalService();
    }

    @Override
    public void getGoldList(GetAllExchangeRatesOutlayParams params) {
        PsnGetAllExchangeRatesOutlayParams paramsCopy = new PsnGetAllExchangeRatesOutlayParams();
        paramsCopy.setIbknum(params.getIbknum());
        paramsCopy.setOfferType(params.getOfferType());
        paramsCopy.setParitiesType(params.getParitiesType());
        mGoldServce.psnGetAllExchangeRatesOutlay(paramsCopy).
                compose(this.<List<PsnGetAllExchangeRatesOutlayResult>>bindToLifecycle())
                .compose(SchedulersCompat.<List<PsnGetAllExchangeRatesOutlayResult>>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<List<PsnGetAllExchangeRatesOutlayResult>>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        mView.onFail();
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(List<PsnGetAllExchangeRatesOutlayResult> psnGetAllExchangeRatesOutlayResults) {
                        List<GoldBean> resultList = new ArrayList<>();
                        if (psnGetAllExchangeRatesOutlayResults != null) {
                            for (PsnGetAllExchangeRatesOutlayResult item : psnGetAllExchangeRatesOutlayResults) {
                                GoldBean bean = new GoldBean();
                                bean.setBuyRate(item.getBuyRate());
                                bean.setSellRate(item.getSellRate());
                                bean.setSourceCurrencyCode(item.getSourceCurrencyCode());
                                bean.setTargetCurrencyCode(item.getTargetCurrencyCode());
                                resultList.add(bean);
                            }
                        }
                        mView.updateGoldView(resultList);
                    }
                });
    }
}