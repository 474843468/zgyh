package com.boc.bocsoft.mobile.wfss.buss.common.sercice;

import com.boc.bocsoft.mobile.wfss.buss.common.model.WFSSSearchAllProParams;
import com.boc.bocsoft.mobile.wfss.buss.common.model.WFSSSearchAllProResponse;
import com.boc.bocsoft.mobile.wfss.buss.common.model.WFSSSearchAllProResult;
import com.boc.bocsoft.mobile.wfss.common.client.WFSSClient;

import rx.Observable;

/**
 * 通用接口
 * Created by lxw on 2016/11/5 0005.
 */
public class CommonService {

    // 全局查询
    public static final String URL_COMM_SEARCH_ALL_PRO = "/comm/searchAllPro";


    /**
     * 4.1 全局查询
     */
    public Observable<WFSSSearchAllProResult> searchAllPro(WFSSSearchAllProParams params) {
        return WFSSClient.instance.post(URL_COMM_SEARCH_ALL_PRO, params, WFSSSearchAllProResponse.class);
    }
}
