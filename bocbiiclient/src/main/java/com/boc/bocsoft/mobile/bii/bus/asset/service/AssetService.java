package com.boc.bocsoft.mobile.bii.bus.asset.service;

import com.boc.bocsoft.mobile.bii.bus.asset.model.PsnAssetBalanceQuery.PsnAssetBalanceQueryParams;
import com.boc.bocsoft.mobile.bii.bus.asset.model.PsnAssetBalanceQuery.PsnAssetBalanceQueryResponse;
import com.boc.bocsoft.mobile.bii.bus.asset.model.PsnAssetBalanceQuery.PsnAssetBalanceQueryResult;
import com.boc.bocsoft.mobile.bii.common.client.BIIClient;
import rx.Observable;

/**
 * 资产
 */
public class AssetService {

  /**
   * 4.40 040 PsnAssetBalanceQuery客户资产负债信息查询
   * @param params
   * @return
   */
  public Observable<PsnAssetBalanceQueryResult> psnAssetBalanceQuery(PsnAssetBalanceQueryParams params) {
    return BIIClient.instance.post("PsnAssetBalanceQuery", params, PsnAssetBalanceQueryResponse.class);
  }

}