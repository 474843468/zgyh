package com.boc.bocsoft.mobile.bii.bus.financing.service;

import com.boc.bocsoft.mobile.bii.bus.financing.model.PsnXadProductQueryOutlay.PsnXadProductQueryOutlayParams;
import com.boc.bocsoft.mobile.bii.bus.financing.model.PsnXadProductQueryOutlay.PsnXadProductQueryOutlayResponse;
import com.boc.bocsoft.mobile.bii.bus.financing.model.PsnXadProductQueryOutlay.PsnXadProductQueryOutlayResult;
import com.boc.bocsoft.mobile.bii.bus.financing.model.PsnXpadProductDetailQueryOutlay.PsnXpadProductDetailQueryOutlayParams;
import com.boc.bocsoft.mobile.bii.bus.financing.model.PsnXpadProductDetailQueryOutlay.PsnXpadProductDetailQueryOutlayResponse;
import com.boc.bocsoft.mobile.bii.bus.financing.model.PsnXpadProductDetailQueryOutlay.PsnXpadProductDetailQueryOutlayResult;
import com.boc.bocsoft.mobile.bii.common.client.BIIClient;
import com.boc.bocsoft.mobile.bii.common.client.BIIClientConfig;

import rx.Observable;

/**
 * 理财
 */
public class FinancingService {

  /**
   * 查询全国发行的理财产品列
   * @param params
   * @return
   */
  public Observable<PsnXadProductQueryOutlayResult> psnXadProductQueryOutlay(PsnXadProductQueryOutlayParams params) {
    return BIIClient.instance.post(BIIClientConfig.getBPMSUrl(),"PsnXadProductQueryOutlay", params, PsnXadProductQueryOutlayResponse.class);
  }

  /**
   * 4.52 052 PsnXpadProductDetailQueryOutlay登录前查询理财产品
   * @param params
   * @return
   */
  public Observable<PsnXpadProductDetailQueryOutlayResult> psnXpadProductDetailQueryOutlay(PsnXpadProductDetailQueryOutlayParams params) {
    return BIIClient.instance.post(BIIClientConfig.getBPMSUrl(),"PsnXpadProductDetailQueryOutlay", params, PsnXpadProductDetailQueryOutlayResponse.class);
  }

}