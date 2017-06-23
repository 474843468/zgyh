package com.boc.bocsoft.mobile.bocyun.common.model;

import java.io.Serializable;

/**
 * YunHeader
 *
 * @author Administrator
 */
public class YunHeader implements Serializable {

  private String msgType;//String	1	报文类型	必输；0－请求包 1－回复包
  private String trDt;//String	8	交易日期	必输。格式:YYYYMMDD
  private String trTime;//String	6	交易时间	必输。格式:YYYYMMDD
  private String reqTraceNo;//String	32	请求交易流水号	必输。可唯一标识一笔交易
  private String resTraceNo;//String	32	返回交易流水号	返回包必输。可唯一标识一笔交易
  private String channelId;//String	2	渠道分类	01 – 手机端（包含安卓和IOS）
  private String product;
      //String	100	应用标识	按原后管平台定义的产品名称填写。例如： 国内对私IOS:BOCMBC_V01.1P 国内对私Android:BOCMBC_V01.1A
  private String version;//String	10	终端应用版本	按实际填写。例如：BOCMBC_V01.1A对应的版本号
  private String device;//String	50	设备信息	格式：“厂商名,产品名,型号”中间用半角逗号分隔 例子："Apple,New Pad,MD368ZP"
  private String platform;//String	50	设备操作系统信息	格式：“厂商名,产品名,版本号” 中间用半角逗号分隔例子："Apple,iOS,5.1.1"
  private String deviceNo;//String	100	设备唯一标识	唯一标识一个设备
  private String validFlag;//String	200	校验码	请求包必输。使用AES加密以下信息：“product_version_YYYYMMDD”

  public String getMsgType() {
    return msgType;
  }

  public void setMsgType(String msgType) {
    this.msgType = msgType;
  }

  public String getTrDt() {
    return trDt;
  }

  public void setTrDt(String trDt) {
    this.trDt = trDt;
  }

  public String getTrTime() {
    return trTime;
  }

  public void setTrTime(String trTime) {
    this.trTime = trTime;
  }

  public String getReqTraceNo() {
    return reqTraceNo;
  }

  public void setReqTraceNo(String reqTraceNo) {
    this.reqTraceNo = reqTraceNo;
  }

  public String getResTraceNo() {
    return resTraceNo;
  }

  public void setResTraceNo(String resTraceNo) {
    this.resTraceNo = resTraceNo;
  }

  public String getChannelId() {
    return channelId;
  }

  public void setChannelId(String channelId) {
    this.channelId = channelId;
  }

  public String getProduct() {
    return product;
  }

  public void setProduct(String product) {
    this.product = product;
  }

  public String getVersion() {
    return version;
  }

  public void setVersion(String version) {
    this.version = version;
  }

  public String getDevice() {
    return device;
  }

  public void setDevice(String device) {
    this.device = device;
  }

  public String getPlatform() {
    return platform;
  }

  public void setPlatform(String platform) {
    this.platform = platform;
  }

  public String getDeviceNo() {
    return deviceNo;
  }

  public void setDeviceNo(String deviceNo) {
    this.deviceNo = deviceNo;
  }

  public String getValidFlag() {
    return validFlag;
  }

  public void setValidFlag(String validFlag) {
    this.validFlag = validFlag;
  }
}
