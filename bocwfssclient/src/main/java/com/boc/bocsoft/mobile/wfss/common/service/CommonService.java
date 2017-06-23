package com.boc.bocsoft.mobile.wfss.common.service;

import com.boc.bocsoft.mobile.wfss.buss.common.model.WFSSSearchAllProParams;
import com.boc.bocsoft.mobile.wfss.buss.common.model.WFSSSearchAllProResponse;
import com.boc.bocsoft.mobile.wfss.buss.common.model.WFSSSearchAllProResult;
import com.boc.bocsoft.mobile.wfss.common.client.WFSSClient;

import rx.Observable;

/**
 * Created by gwluo on 2016/11/5.
 */

public class CommonService {
    /**
     * 4.1 全局搜索
     *
     * @param params
     */
    public Observable<WFSSSearchAllProResult> searchAllPro(WFSSSearchAllProParams params) {
        return WFSSClient.instance.post(params.getPath(), params, WFSSSearchAllProResponse.class);
    }
}
