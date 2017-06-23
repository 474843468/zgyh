package com.boc.bocsoft.mobile.bocyun.model.UBAS000006;

import com.boc.bocsoft.mobile.bocyun.model.UBAS000005.AccountBean;
import java.util.List;

/**
 * Created by dingeryue on 2016年10月16.
 * 用户头像请求参数
 */

public class UserLastAccountIdUpdateParam {

  private String bocnetCustNo;//	String	16	网银客户Id
  private String bancsCustNo	;//	String	16	核心客户号
  private String mobilePh		;//	String	16	手机号

  private List<AccountBean> list;

  public void setBancsCustNo(String bancsCustNo) {
    this.bancsCustNo = bancsCustNo;
  }

  public void setBocnetCustNo(String bocnetCustNo) {
    this.bocnetCustNo = bocnetCustNo;
  }

  public void setMobilePh(String mobilePh) {
    this.mobilePh = mobilePh;
  }

  public void setList(List<AccountBean> list) {
    this.list = list;
  }
}