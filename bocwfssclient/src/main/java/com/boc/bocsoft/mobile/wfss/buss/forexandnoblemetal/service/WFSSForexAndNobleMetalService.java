package com.boc.bocsoft.mobile.wfss.buss.forexandnoblemetal.service;

import com.boc.bocsoft.mobile.wfss.buss.forexandnoblemetal.model.queryaveragetendency.WFSSQueryAverageTendencyParams;
import com.boc.bocsoft.mobile.wfss.buss.forexandnoblemetal.model.queryaveragetendency.WFSSQueryAverageTendencyResponse;
import com.boc.bocsoft.mobile.wfss.buss.forexandnoblemetal.model.queryaveragetendency.WFSSQueryAverageTendencyResult;
import com.boc.bocsoft.mobile.wfss.buss.forexandnoblemetal.model.queryktendency.WFSSQueryKTendencyParams;
import com.boc.bocsoft.mobile.wfss.buss.forexandnoblemetal.model.queryktendency.WFSSQueryKTendencyResponse;
import com.boc.bocsoft.mobile.wfss.buss.forexandnoblemetal.model.queryktendency.WFSSQueryKTendencyResult;
import com.boc.bocsoft.mobile.wfss.buss.forexandnoblemetal.model.querymultiplequotation.WFSSQueryMultipleQuotationParams;
import com.boc.bocsoft.mobile.wfss.buss.forexandnoblemetal.model.querymultiplequotation.WFSSQueryMultipleQuotationResponse;
import com.boc.bocsoft.mobile.wfss.buss.forexandnoblemetal.model.querymultiplequotation.WFSSQueryMultipleQuotationResult;
import com.boc.bocsoft.mobile.wfss.buss.forexandnoblemetal.model.querysingelquotation.WFSSQuerySingelQuotationParams;
import com.boc.bocsoft.mobile.wfss.buss.forexandnoblemetal.model.querysingelquotation.WFSSQuerySingelQuotationResponse;
import com.boc.bocsoft.mobile.wfss.buss.forexandnoblemetal.model.querysingelquotation.WFSSQuerySingelQuotationResult;
import com.boc.bocsoft.mobile.wfss.common.client.WFSSClient;

import rx.Observable;

/**
 * 外汇和贵金属service
 * Created by gwluo on 2016/10/24.
 */
public class WFSSForexAndNobleMetalService {
    /**
     * 2.1外汇、贵金属多笔行情查询 queryMultipleQuotation
     */
    public Observable<WFSSQueryMultipleQuotationResult> queryMultipleQuotation(WFSSQueryMultipleQuotationParams params) {
        return WFSSClient.instance.post(params.getPath(), params, WFSSQueryMultipleQuotationResponse.class);
    }

    /**
     * 2.2 外汇、贵金属单笔行情查询
     */
    public Observable<WFSSQuerySingelQuotationResult> querySingelQuotation(WFSSQuerySingelQuotationParams params) {
        return WFSSClient.instance.post(params.getPath(), params, WFSSQuerySingelQuotationResponse.class);
    }

    /**
     * 2.3 外汇、贵金属K线图查询 queryKTendency
     */
    public Observable<WFSSQueryKTendencyResult> queryKTendency(WFSSQueryKTendencyParams params) {
        return WFSSClient.instance.post(params.getPath(), params, WFSSQueryKTendencyResponse.class);
    }

    /**
     * 2.4 外汇、贵金属趋势图查询
     */
    public Observable<WFSSQueryAverageTendencyResult> queryAverageTendency(WFSSQueryAverageTendencyParams params) {
        return WFSSClient.instance.post(params.getPath(), params, WFSSQueryAverageTendencyResponse.class);
    }
}