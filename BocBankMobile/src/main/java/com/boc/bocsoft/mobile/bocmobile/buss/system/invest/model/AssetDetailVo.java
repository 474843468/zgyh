package com.boc.bocsoft.mobile.bocmobile.buss.system.invest.model;

import android.os.Parcel;
import android.os.Parcelable;
import java.math.BigDecimal;

/**
 * Created by dingeryue on 2016年09月10.
 * 资产明细数据
 */
public class AssetDetailVo implements Parcelable{

  private String name;
  private String type;
  private String money;
  private float moneyNumber;
  private BigDecimal moneyBigDecimal;
  private String percent;//百分比

  public AssetDetailVo(){}
  protected AssetDetailVo(Parcel in) {
    name = in.readString();
    type = in.readString();
    money = in.readString();
    moneyNumber = in.readFloat();
    percent = in.readString();
    try {
      moneyBigDecimal = new BigDecimal(in.readString());
    }catch (Exception e){
      moneyBigDecimal = BigDecimal.ZERO;
    }
  }

  public static final Creator<AssetDetailVo> CREATOR = new Creator<AssetDetailVo>() {
    @Override public AssetDetailVo createFromParcel(Parcel in) {
      return new AssetDetailVo(in);
    }

    @Override public AssetDetailVo[] newArray(int size) {
      return new AssetDetailVo[size];
    }
  };

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getMoney() {
    return money;
  }

  public void setMoney(String money) {
    this.money = money;
  }

  public float getMoneyNumber() {
    return moneyNumber;
  }

  public void setMoneyNumber(float moneyNumber) {
    this.moneyNumber = moneyNumber;
  }

  public BigDecimal getMoneyBigDecimal() {
    return moneyBigDecimal;
  }

  public void setMoneyBigDecimal(BigDecimal moneyBigDecimal) {
    this.moneyBigDecimal = moneyBigDecimal;
  }

  public String getPercent() {
    return percent;
  }

  public void setPercent(String percent) {
    this.percent = percent;
  }

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(name);
    dest.writeString(type);
    dest.writeString(money);
    dest.writeFloat(moneyNumber);
    dest.writeString(percent);
    dest.writeString(moneyBigDecimal==null?"":moneyBigDecimal.toString());
  }
}
