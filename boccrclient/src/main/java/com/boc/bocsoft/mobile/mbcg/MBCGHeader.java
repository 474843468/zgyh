package com.boc.bocsoft.mobile.mbcg;

/**
 * Created by dingeryue on 2016年10月26.
 */

public class MBCGHeader {

  private String method;

  private String custNo;
  private String ROM;
  private String RAM;
  private String resolution;
  private String versionNo;
  private String osVersion;
  private String firstUse;
  private String usedROM;
  private String productName;
  private String model;

  //"header":{"custNo":null,"ROM":"","RAM":"","resolution":"768*1024","versionNo":"1.0.1","osVersion":"7.0.6","firstUse":null,"usedROM":"","productName":"BOCSFSMEAP","model":"iPad 2 (A1395)"}

  public void setMethod(String method) {
    this.method = method;
  }

  public void setCustNo(String custNo) {
    this.custNo = custNo;
  }

  public void setROM(String ROM) {
    this.ROM = ROM;
  }

  public void setRAM(String RAM) {
    this.RAM = RAM;
  }

  public void setResolution(String resolution) {
    this.resolution = resolution;
  }

  public void setVersionNo(String versionNo) {
    this.versionNo = versionNo;
  }

  public void setOsVersion(String osVersion) {
    this.osVersion = osVersion;
  }

  public void setFirstUse(String firstUse) {
    this.firstUse = firstUse;
  }

  public void setUsedROM(String usedROM) {
    this.usedROM = usedROM;
  }

  public void setProductName(String productName) {
    this.productName = productName;
  }

  public void setModel(String model) {
    this.model = model;
  }
}
