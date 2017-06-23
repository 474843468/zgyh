package com.boc.bocsoft.mobile.bocmobile.buss.system.invest.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 资产vo
 * Created by dingeryue on 2016年09月06.
 */
public class AssetVo implements Parcelable{


  private String fundAmt			;//全行基金				基金
  private String tpccAmt			;//全行第三方存管	  第三方存管
  private String xpadAmt			;//全行理财产品		理财

  private String bondAmt			;//全行国债				债券
  private String ibasAmt			;//全行保险				保险
  private String actGoldAmt	;//全行账户贵金属		账户贵金属

  private String metalAmt		;//全行贵金属				实物贵金属
  private String forexAmt		;//双向宝资产				双向宝
  private String jxjAmt			;//吉祥金						贵金属积存
  private String autd				;//黄金T+D					T+D

  private String total;

  public AssetVo(){}

  protected AssetVo(Parcel in) {
    fundAmt = in.readString();
    tpccAmt = in.readString();
    xpadAmt = in.readString();
    bondAmt = in.readString();
    ibasAmt = in.readString();
    actGoldAmt = in.readString();
    metalAmt = in.readString();
    forexAmt = in.readString();
    jxjAmt = in.readString();
    autd = in.readString();
    total = in.readString();
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(fundAmt);
    dest.writeString(tpccAmt);
    dest.writeString(xpadAmt);
    dest.writeString(bondAmt);
    dest.writeString(ibasAmt);
    dest.writeString(actGoldAmt);
    dest.writeString(metalAmt);
    dest.writeString(forexAmt);
    dest.writeString(jxjAmt);
    dest.writeString(autd);
    dest.writeString(total);
  }

  @Override public int describeContents() {
    return 0;
  }

  public static final Creator<AssetVo> CREATOR = new Creator<AssetVo>() {
    @Override public AssetVo createFromParcel(Parcel in) {
      return new AssetVo(in);
    }

    @Override public AssetVo[] newArray(int size) {
      return new AssetVo[size];
    }
  };

  public String getFundAmt() {
    return fundAmt;
  }

  public void setFundAmt(String fundAmt) {
    this.fundAmt = fundAmt;
  }

  public String getTpccAmt() {
    return tpccAmt;
  }

  public void setTpccAmt(String tpccAmt) {
    this.tpccAmt = tpccAmt;
  }

  public String getXpadAmt() {
    return xpadAmt;
  }

  public void setXpadAmt(String xpadAmt) {
    this.xpadAmt = xpadAmt;
  }

  public String getBondAmt() {
    return bondAmt;
  }

  public void setBondAmt(String bondAmt) {
    this.bondAmt = bondAmt;
  }

  public String getIbasAmt() {
    return ibasAmt;
  }

  public void setIbasAmt(String ibasAmt) {
    this.ibasAmt = ibasAmt;
  }

  public String getActGoldAmt() {
    return actGoldAmt;
  }

  public void setActGoldAmt(String actGoldAmt) {
    this.actGoldAmt = actGoldAmt;
  }

  public String getMetalAmt() {
    return metalAmt;
  }

  public void setMetalAmt(String metalAmt) {
    this.metalAmt = metalAmt;
  }

  public String getForexAmt() {
    return forexAmt;
  }

  public void setForexAmt(String forexAmt) {
    this.forexAmt = forexAmt;
  }

  public String getJxjAmt() {
    return jxjAmt;
  }

  public void setJxjAmt(String jxjAmt) {
    this.jxjAmt = jxjAmt;
  }

  public String getAutd() {
    return autd;
  }

  public void setAutd(String autd) {
    this.autd = autd;
  }

  public String getTotal() {
    return total;
  }

  public void setTotal(String total) {
    this.total = total;
  }
}
