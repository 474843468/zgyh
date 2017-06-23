package com.boc.bocsoft.mobile.bocyun.model.UBAS010001;

/**
 * Created by dingeryue on 2016年10月16.
 * 查询码表参数
 */

public class UBAS010001Param {

  /**
   * String
   * 1
   * 查询标识
   * 0 – 查询全部移动端参数
   * 1 – 查询指定的类型编码、参数编码
   * 必输
   */
  private String queryFlag;
  /**
   * String
   * 50
   * 类型编码
   */
  private String dictTypeCode;
  /**
   * Integer
   * 50
   * 参数编码
   * 取值见附录B
   * 为空时，接口返回该类型下所有参数
   */
  private String dictKey;

  public void setDictKey(String dictKey) {
    this.dictKey = dictKey;
  }

  public void setDictTypeCode(String dictTypeCode) {
    this.dictTypeCode = dictTypeCode;
  }

  public void setQueryFlag(String queryFlag) {
    this.queryFlag = queryFlag;
  }
}
