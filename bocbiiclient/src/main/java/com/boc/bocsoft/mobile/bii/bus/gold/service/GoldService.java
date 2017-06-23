package com.boc.bocsoft.mobile.bii.bus.gold.service;

import com.boc.bocsoft.mobile.bii.bus.gold.model.PsnGetAllExchangeRatesOutlay.PsnGetAllExchangeRatesOutlayParams;
import com.boc.bocsoft.mobile.bii.bus.gold.model.PsnGetAllExchangeRatesOutlay.PsnGetAllExchangeRatesOutlayResponse;
import com.boc.bocsoft.mobile.bii.bus.gold.model.PsnGetAllExchangeRatesOutlay.PsnGetAllExchangeRatesOutlayResult;
import com.boc.bocsoft.mobile.bii.bus.gold.model.PsnGoldTradeRateQuery.PsnGoldTradeRateQueryParams;
import com.boc.bocsoft.mobile.bii.bus.gold.model.PsnGoldTradeRateQuery.PsnGoldTradeRateQueryResponse;
import com.boc.bocsoft.mobile.bii.bus.gold.model.PsnGoldTradeRateQuery.PsnGoldTradeRateQueryResult;
import com.boc.bocsoft.mobile.bii.common.client.BIIClient;

import java.util.List;

import rx.Observable;

/**
 * 贵金属service
 * Created by lxw on 2016/8/16 0016.
 */
public class GoldService {

    /**
     * 黄金行情查询
     * added by xdy 20160603
     *
     * @param params 参数P
     */
    public Observable<List<PsnGoldTradeRateQueryResult>>
        psnGoldTradeRateQuery(PsnGoldTradeRateQueryParams params) {
            return BIIClient.instance.
                post("PsnGoldTradeRateQuery", params, PsnGoldTradeRateQueryResponse.class);
    }

    public Observable<List<PsnGetAllExchangeRatesOutlayResult>>
        psnGetAllExchangeRatesOutlay(PsnGetAllExchangeRatesOutlayParams params) {
            return BIIClient.instance.
                post("PsnGetAllExchangeRatesOutlay", params, PsnGetAllExchangeRatesOutlayResponse.class);
    }
}
