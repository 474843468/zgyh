package com.boc.bocsoft.mobile.bocyun.model.UBAS010001;

import java.util.List;

/**
 * Created by dingeryue on 2016年10月16.
 * 查询码表参数
 */

public class UBAS010001Result {

  private List<DictBean> list;

  public List<DictBean> getList() {
    return list;
  }

  public static class DictBean{
    private String dictTypeCode;
    private String dictKey;
    private String dictValue;

    public String getDictTypeCode() {
      return dictTypeCode;
    }

    public String getDictKey() {
      return dictKey;
    }

    public String getDictValue() {
      return dictValue;
    }

  }
}
