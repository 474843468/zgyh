package com.boc.bocsoft.mobile.bii.bus.coin.model.PsnCoinSellerSwitch;

import com.boc.bocsoft.mobile.common.utils.LoggerUtils;
import com.google.gson.annotations.SerializedName;

/**
 * 人行纪念币销售系统开关 结果
 */
public class PsnCoinSellerSwitchResult {

  /**
   * 开关
   * TRUE：开
   *FALSE：关
   */
  @SerializedName("switch")
  private boolean  isSwitch;

  /**
   *
   *   撤销及修改开关，该字段为TRUE，页面显示撤销和修改按键；
   *   为FALSE页面不显示撤销和修改按键
   */
  private boolean smsLock;

  /**
   *   短信通知开关，该字段为TRUE，预约和修改完成之后发送短信通知；该字段为FALSE，预约和修改完成之后不发送短信通知。
   */
  private boolean msgSwitch;

  public boolean isSwitch() {
    LoggerUtils.Log("is ssssssssss:"+isSwitch);
    return isSwitch;
  }

  public boolean isMsgSwitch() {
    return msgSwitch;
  }

  public boolean isSmsLock() {
    return smsLock;
  }
}

