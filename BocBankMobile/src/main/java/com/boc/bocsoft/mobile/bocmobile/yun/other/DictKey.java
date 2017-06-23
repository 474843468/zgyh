package com.boc.bocsoft.mobile.bocmobile.yun.other;

/**
 * Created by dingeryue on 2016年10月25.
 * 云备份 - 码表key值定义
 */

public interface DictKey {

  /**
   * 查询余额的账户数目上限（单位：个）
   */
  String QUERYACCLIMIT = "queryAccLimit";
  /**
   * 50000	大小额限额（单位：元）
   */
  String BIGSMALLLIMIT = "bigSmallLimit";//
  /**
   * //	50000	实时及普通判断（单位：元）
   */
  String CHECKREALTIMEFLAG = "checkRealTimeFlag";
  /**
   * //	50000	安全工具限额（单位：元）
   */
  String SAFETOOLSLIMIT = "safeToolsLimit";

}
