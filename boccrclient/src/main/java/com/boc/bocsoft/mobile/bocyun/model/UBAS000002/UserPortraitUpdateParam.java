package com.boc.bocsoft.mobile.bocyun.model.UBAS000002;

/**
 * Created by dingeryue on 2016年10月16.
 * 用户头像更新请求参数
 */

public class UserPortraitUpdateParam {

  /**
   * String	16	网银客户ID		必输
   */
  private String bocnetCustNo;
  /**
   * String	16	核心客户号		必输
   */
  private String bancsCustNo;
  /**
   * String	16	手机号		必输
   */
  private String mobilePh;
  /**
   * String		图片字节流，使用BASE64编码。附件大小:最大为1M	需要上传的图片实体	必输
   */
  private String imageEntity;

  private String suffix;

  public void setBancsCustNo(String bancsCustNo) {
    this.bancsCustNo = bancsCustNo;
  }

  public void setBocnetCustNo(String bocnetCustNo) {
    this.bocnetCustNo = bocnetCustNo;
  }

  public void setImageEntity(String imageEntity) {
    this.imageEntity = imageEntity;
  }

  public void setMobilePhPh(String mobilePhPh) {
    this.mobilePh = mobilePhPh;
  }

  public void setSuffix(String suffix) {
    this.suffix = suffix;
  }
}
