package com.boc.bocsoft.mobile.bocyun.model.UBAS000005;

public  class AccountBean{
    private String serviceNo;//	String	16	交易类型编码
    private String lastAccId;//  String	16	账户ID

    public String getLastAccId() {
      return lastAccId;
    }

  public String getServiceNo() {
    return serviceNo;
  }

  public void setLastAccId(String lastAccId) {
    this.lastAccId = lastAccId;
  }

  public void setServiceNo(String serviceNo) {
    this.serviceNo = serviceNo;
  }

  @Override public String toString() {
    return "AccountBean{" +
        "serviceNo='" + serviceNo + '\'' +
        ", lastAccId='" + lastAccId + '\'' +
        '}';
  }
}