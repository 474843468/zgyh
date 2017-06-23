package com.boc.bocsoft.mobile.bii.bus.coin.service;

import com.boc.bocsoft.mobile.bii.bus.coin.model.PsnCoinSellerSwitch.PsnCoinSellerSwitchParams;
import com.boc.bocsoft.mobile.bii.bus.coin.model.PsnCoinSellerSwitch.PsnCoinSellerSwitchResponse;
import com.boc.bocsoft.mobile.bii.bus.coin.model.PsnCoinSellerSwitch.PsnCoinSellerSwitchResult;
import com.boc.bocsoft.mobile.bii.common.client.BIIClient;
import rx.Observable;

/**
 * 纪念币service
 * Created by dingeryue on 2016年10月10.
 */

public class CoinService {
  /**
   * 4.1 001 PsnCoinSellerSwitch人行纪念币销售系统开关
   * @param params
   * @return
   */
  public Observable<PsnCoinSellerSwitchResult> psnCoinSellerSwitch(PsnCoinSellerSwitchParams params) {
    return BIIClient.instance.post("PsnCoinSellerSwitch", params, PsnCoinSellerSwitchResponse.class);
  }

  public Observable<PsnCoinSellerSwitchResult> psnCoinSellerSwitch(String url,PsnCoinSellerSwitchParams params) {
    return BIIClient.instance.post(url,"PsnCoinSellerSwitch", params, PsnCoinSellerSwitchResponse.class);
  }

}
