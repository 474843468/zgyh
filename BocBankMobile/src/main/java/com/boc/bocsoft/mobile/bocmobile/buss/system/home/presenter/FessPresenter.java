package com.boc.bocsoft.mobile.bocmobile.buss.system.home.presenter;

import com.boc.bocsoft.mobile.bii.bus.fess.model.PsnGetExchangeOutlay.PsnGetExchangeOutlayParams;
import com.boc.bocsoft.mobile.bii.bus.fess.model.PsnGetExchangeOutlay.PsnGetExchangeOutlayResult;
import com.boc.bocsoft.mobile.bii.bus.fess.service.FessService;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.base.Exception.BIIBaseSubscriber;
import com.boc.bocsoft.mobile.bocmobile.base.model.FessBean;
import com.boc.bocsoft.mobile.bocmobile.buss.system.home.model.getexchangeoutlay.GetExchangeOutlayParams;
import com.boc.bocsoft.mobile.bocmobile.buss.system.home.ui.FessContract;
import com.boc.bocsoft.mobile.framework.rx.SchedulersCompat;
import com.boc.bocsoft.mobile.framework.ui.RxPresenter;

import java.util.ArrayList;
import java.util.List;

/**结汇购汇
 * Created by gwluo on 2016/8/29.
 */
public class FessPresenter extends RxPresenter implements FessContract.Presenter {
    FessContract.View mView;
    FessService mFessService;


    public FessPresenter(FessContract.View view) {
        mView = view;
        mFessService = new FessService();
    }

    @Override
    public void getFessList(final GetExchangeOutlayParams params) {
        PsnGetExchangeOutlayParams paramsCopy = new PsnGetExchangeOutlayParams();
        mFessService.psnGetExchangeOutlay(paramsCopy).
                compose(this.<List<PsnGetExchangeOutlayResult>>bindToLifecycle()).
                compose(SchedulersCompat.<List<PsnGetExchangeOutlayResult>>applyIoSchedulers()).
                subscribe(new BIIBaseSubscriber<List<PsnGetExchangeOutlayResult>>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        mView.updateFail();
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(List<PsnGetExchangeOutlayResult> psnGetExchangeOutlayResults) {
                        List<FessBean> resultList = new ArrayList<FessBean>();
                        for (PsnGetExchangeOutlayResult item : psnGetExchangeOutlayResults) {
                            FessBean result = new FessBean();
                            result.setBuyNoteRate(item.getBuyNoteRate());
                            result.setSellNoteRate(item.getSellNoteRate());
                            result.setBuyRate(item.getBuyRate());
                            result.setCurCode(item.getCurCode());
                            result.setSellRate(item.getSellRate());
                            result.setUpdateDate(item.getUpdateDate());
                            resultList.add(result);

                        }
                        mView.updateFessView(resultList);
                    }
                });
    }
}