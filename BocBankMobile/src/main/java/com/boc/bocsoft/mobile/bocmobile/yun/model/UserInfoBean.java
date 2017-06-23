package com.boc.bocsoft.mobile.bocmobile.yun.model;

/**
 * Created by dingeryue on 2016年10月26.
 */

public class UserInfoBean {

  // (mobilePh text, custno text ,custnetno text, photoUrl text, yunBase64 text,localBase64 text, uploadstate text)" ;

  private String mobilePh;
  private String custno;
  private String custnetno;
  private String photoUrl;
  private String yunBase64;
  private String localBase64;
  private String uploadstate;

  public String getMobilePh() {
    return mobilePh;
  }

  public void setMobilePh(String mobilePh) {
    this.mobilePh = mobilePh;
  }

  public String getCustno() {
    return custno;
  }

  public void setCustno(String custno) {
    this.custno = custno;
  }

  public String getCustnetno() {
    return custnetno;
  }

  public void setCustnetno(String custnetno) {
    this.custnetno = custnetno;
  }

  public String getPhotoUrl() {
    return photoUrl;
  }

  public void setPhotoUrl(String photoUrl) {
    this.photoUrl = photoUrl;
  }

  public String getYunBase64() {
    return yunBase64;
  }

  public void setYunBase64(String yunBase64) {
    this.yunBase64 = yunBase64;
  }

  public String getLocalBase64() {
    return localBase64;
  }

  public void setLocalBase64(String localBase64) {
    this.localBase64 = localBase64;
  }

  public String getUploadstate() {
    return uploadstate;
  }

  public void setUploadstate(String uploadstate) {
    this.uploadstate = uploadstate;
  }
}
