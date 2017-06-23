package com.boc.bocsoft.mobile.bocmobile.buss.creditcard.webplugin.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by dingeryue on 2016年12月27.
 */

public class AccountInfo implements Parcelable {

  /**
   * 账户ID
   */
  private String accountId;

  /**
   * 信用卡卡号
   */
  private String creditCardNum;

  /**
   * 用户姓名
   */
  private String userName;

  public AccountInfo(){

  }

  public AccountInfo(String accountId,String  creditCardNum,String userName){
    this.accountId = accountId;
    this.creditCardNum = creditCardNum;
    this.userName = userName;
  }

  protected AccountInfo(Parcel in) {
    accountId = in.readString();
    creditCardNum = in.readString();
    userName = in.readString();
  }

  public static final Creator<AccountInfo> CREATOR = new Creator<AccountInfo>() {
    @Override public AccountInfo createFromParcel(Parcel in) {
      return new AccountInfo(in);
    }

    @Override public AccountInfo[] newArray(int size) {
      return new AccountInfo[size];
    }
  };

  public void setAccountId(String accountId) {
    this.accountId = accountId;
  }

  public void setCreditCardNum(String creditCardNum) {
    this.creditCardNum = creditCardNum;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(accountId);
    dest.writeString(creditCardNum);
    dest.writeString(userName);
  }
}
