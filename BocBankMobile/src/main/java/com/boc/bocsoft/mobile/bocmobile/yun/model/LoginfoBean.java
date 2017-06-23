package com.boc.bocsoft.mobile.bocmobile.yun.model;

/**
 * Created by dingeryue on 2016年10月27.
 */

public class LoginfoBean  {


  /**
   * 网银客户id
   */
  private String bocnetCustNo;

  /**
   * 核心系统客户号
   */
  private String bancsCustNo;

  /**
   * 客户注册手机号
   */
  private String mobilePh;

  /**
   * 登录时间
   */
  private String loginTime;

  /**
   * 正常退出时间
   */
  private String logoutTime;

  /**
   * 运行内存 GB
   */
  private String ram;
  /**
   * 存储空间 GB
   */
  private String rom;
  /**
   * 已使用的存储空间 GB
   */
  private String userdRom;
  /**
   * 分辨率
   */
  private String resolution;

  /**
   * 上传状态
   */
  private String uploadstate;

  /**
   * 记录id 自增
   */
  private int loginid;

  /*
   * 创建时间
   */
  private String createtime;

  public String getCreatetime() {
    return createtime;
  }

  public void setCreatetime(String createtime) {
    this.createtime = createtime;
  }

  public String getBocnetCustNo() {
    return bocnetCustNo;
  }

  public void setBocnetCustNo(String bocnetCustNo) {
    this.bocnetCustNo = bocnetCustNo;
  }

  public String getBancsCustNo() {
    return bancsCustNo;
  }

  public void setBancsCustNo(String bancsCustNo) {
    this.bancsCustNo = bancsCustNo;
  }

  public String getMobilePh() {
    return mobilePh;
  }

  public void setMobilePh(String mobilePh) {
    this.mobilePh = mobilePh;
  }

  public String getLoginTime() {
    return loginTime;
  }

  public void setLoginTime(String loginTime) {
    this.loginTime = loginTime;
  }

  public String getLogoutTime() {
    return logoutTime;
  }

  public void setLogoutTime(String logoutTime) {
    this.logoutTime = logoutTime;
  }

  public String getRam() {
    return ram;
  }

  public void setRam(String ram) {
    this.ram = ram;
  }

  public String getRom() {
    return rom;
  }

  public void setRom(String rom) {
    this.rom = rom;
  }

  public String getUserdRom() {
    return userdRom;
  }

  public void setUserdRom(String userdRom) {
    this.userdRom = userdRom;
  }

  public String getResolution() {
    return resolution;
  }

  public void setResolution(String resolution) {
    this.resolution = resolution;
  }

  public String getUploadstate() {
    return uploadstate;
  }

  public void setUploadstate(String uploadstate) {
    this.uploadstate = uploadstate;
  }

  public int getLoginid() {
    return loginid;
  }

  public void setLoginid(int loginid) {
    this.loginid = loginid;
  }
}
