package com.boc.bocsoft.mobile.bocmobile.buss.creditcard.webplugin.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by dingeryue on 2016年12月28.
 */

public class CertifInfo3DBean implements Parcelable{

  /**
   * 开通标识
   * String 接口106
   * “1”：开通
   * “0”：未开通
   */
  private String openFlag;

  /**
   * 令牌厂牌
   */
  private String merchId ;
  /**
   * 令牌序号
   */
  private String tokenId ;

  protected CertifInfo3DBean(Parcel in) {
    openFlag = in.readString();
    merchId = in.readString();
    tokenId = in.readString();
  }

  public CertifInfo3DBean(String openFlag,String merchId,String tokenId){
    this.openFlag = openFlag;
    this.merchId = merchId;
    this.tokenId = tokenId;
  }


  public static final Creator<CertifInfo3DBean> CREATOR = new Creator<CertifInfo3DBean>() {
    @Override public CertifInfo3DBean createFromParcel(Parcel in) {
      return new CertifInfo3DBean(in);
    }

    @Override public CertifInfo3DBean[] newArray(int size) {
      return new CertifInfo3DBean[size];
    }
  };

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(openFlag);
    dest.writeString(merchId);
    dest.writeString(tokenId);
  }
}
