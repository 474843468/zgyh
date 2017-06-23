package com.boc.bocsoft.mobile.bocmobile.buss.buyandsellexchange.home;

import com.boc.bocsoft.mobile.bii.bus.fess.model.PsnFessQueryQuotePrice.PsnFessQueryQuotePriceParams;
import com.boc.bocsoft.mobile.bii.bus.fess.model.PsnFessQueryQuotePrice.PsnFessQueryQuotePriceResult;
import com.boc.bocsoft.mobile.bii.bus.fess.model.PsnGetExchangeOutlay.PsnGetExchangeOutlayParams;
import com.boc.bocsoft.mobile.bii.bus.fess.model.PsnGetExchangeOutlay.PsnGetExchangeOutlayResult;
import com.boc.bocsoft.mobile.bii.bus.fess.service.FessService;
import com.boc.bocsoft.mobile.bii.bus.global.service.GlobalService;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.base.Exception.BIIBaseSubscriber;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.model.FessBean;
import com.boc.bocsoft.mobile.bocmobile.buss.buyandsellexchange.BuyExchange.model.BuyAndSellExcHomeModel;
import com.boc.bocsoft.mobile.bocmobile.buss.buyandsellexchange.home.model.PriceModel;
import com.boc.bocsoft.mobile.framework.rx.SchedulersCompat;
import com.boc.bocsoft.mobile.framework.ui.RxPresenter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 购汇首页presenter
 * Created by gwluo on 2016/11/29.
 */

public class BuyExchangeHomePresenter extends RxPresenter implements BuyExchangeHomeContract.Presenter {
    private GlobalService globalService;
    private FessService fessService;
    private BuyExchangeHomeContract.View mView;
    private BuyAndSellExcHomeModel model;

    public BuyExchangeHomePresenter(BuyExchangeHomeContract.View view) {
        globalService = new GlobalService();
        fessService = new FessService();
        mView = view;
        model = new BuyAndSellExcHomeModel();
    }

    /**
     * 登陆后牌价列表
     */
    public void psnFessQueryQuotePrice() {
        showLoadDialog();
        PsnFessQueryQuotePriceParams params = new PsnFessQueryQuotePriceParams();
        fessService.psnFessQueryQuotePrice(params)
                .compose(this.<PsnFessQueryQuotePriceResult>bindToLifecycle())
                .compose(SchedulersCompat.<PsnFessQueryQuotePriceResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnFessQueryQuotePriceResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        closeLoadDialog();
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(PsnFessQueryQuotePriceResult result) {
                        closeLoadDialog();
                        mView.getDataSucc(transPriceLogined(result.getQuotePriceList()));
                    }
                });
    }

    /**
     * 登录前牌价列表
     */
    public void psnGetExchangeOutlayParams() {
        showLoadDialog();
        PsnGetExchangeOutlayParams params = new PsnGetExchangeOutlayParams();
        fessService.psnGetExchangeOutlay(params)
                .compose(this.<List<PsnGetExchangeOutlayResult>>bindToLifecycle())
                .compose(SchedulersCompat.<List<PsnGetExchangeOutlayResult>>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<List<PsnGetExchangeOutlayResult>>() {

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(List<PsnGetExchangeOutlayResult> result) {
                        closeLoadDialog();
                        mView.getDataSucc(transPriceUnLogin(result));
                    }

                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        closeLoadDialog();
                    }

                });
    }

    private List<PriceModel> transPriceLogined(List<PsnFessQueryQuotePriceResult.QuotePrice> list) {
        List<PriceModel> returnList = new ArrayList<>();
        for (PsnFessQueryQuotePriceResult.QuotePrice item : list) {
            PriceModel model = new PriceModel();
            model.setCurrency(item.getCurrency());
            model.setBuyNoteRate(item.getBuyNoteRate());
            model.setBuyRate(item.getBuyRate());
            model.setSellNoteRate(item.getSellNoteRate());
            model.setSellRate(item.getSellRate());
            returnList.add(model);
        }
        return returnList;
    }

    private List<PriceModel> transPriceUnLogin(List<PsnGetExchangeOutlayResult> list) {
        List<PriceModel> returnList = new ArrayList<>();
        for (PsnGetExchangeOutlayResult item : list) {
            PriceModel model = new PriceModel();
            model.setCurrency(item.getCurCode());
            model.setBuyNoteRate(item.getBuyNoteRate().toPlainString());
            model.setBuyRate(item.getBuyRate().toPlainString());
            model.setSellNoteRate(item.getSellNoteRate().toPlainString());
            model.setSellRate(item.getSellRate().toPlainString());
            returnList.add(model);
        }
        return returnList;
    }

    private void showLoadDialog() {
        ((BussFragment) mView).showLoadingDialog();
    }

    private void closeLoadDialog() {
        ((BussFragment) mView).closeProgressDialog();
    }
}
