package com.boc.bocsoft.mobile.bocyun.model.UBAS020001;

/**
 * Created by dingeryue on 2016年10月16.
 * 信息采集-客户登录信息
 */

public class UserLoginInfoUpdateParam {

  private String bocnetCustNo	;//String	16	网银客户Id		必输
  private String bancsCustNo		;//String	16	核心系统客户号		必输
  private String mobilePh			;//String	16	客户注册手机号		必输
  private String loginTime			;//String	8	登录时间		格式：YYYYMMDD
  private String logoutTime		;//String	8	正常退出时间		格式：YYYYMMDD
  private String ram			;//20	运行内存	单位：GB
  private String rom			;//20	存储空间	单位：GB
  private String usedRom				;//Number	20	已使用存储空间	单位：GB
  private String resolution		;//String	50	分辨率

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

  public String getUsedRom() {
    return usedRom;
  }

  public void setUsedRom(String usedRom) {
    this.usedRom = usedRom;
  }

  public String getResolution() {
    return resolution;
  }

  public void setResolution(String resolution) {
    this.resolution = resolution;
  }
}
