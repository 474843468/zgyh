package com.boc.bocsoft.mobile.cr.bus.product.service;

import com.boc.bocsoft.mobile.cr.bus.product.model.CRgetProductList.CRgetProductListParams;
import com.boc.bocsoft.mobile.cr.bus.product.model.CRgetProductList.CRgetProductListResponse;
import com.boc.bocsoft.mobile.cr.bus.product.model.CRgetProductList.CRgetProductListResult;
import com.boc.bocsoft.mobile.cr.common.client.CRClient;
import com.boc.bocsoft.mobile.cr.common.client.CRClientConfig;
import rx.Observable;

/**
 * 产品推荐
 */
public class CRProductService {
    /**
     * 获取推荐列表
     * @param params
     * @return
     */
    public Observable<CRgetProductListResult> cRgetProductList (CRgetProductListParams params) {
        return CRClient.instance.post(CRClientConfig.getBiiUrl(),"CSgetProductList", params,  CRgetProductListResponse.class);
    }
}
