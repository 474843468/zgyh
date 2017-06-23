package com.boc.bocsoft.mobile.bii.bus.equickpay.model.PsnHCEQuickPassListQueryOutlay;

/**
 * Created by gengjunying on 2016/12/6.
 * 获取未登录的hce卡列表信息 请求
 */
public class PsnHCEQuickPassListQueryOutlayParams {

    //设备imei
    private String deviceNo;
    //证件号码
    private String identityNumber;
    //证件类型
    private String identifyType;

    public String getDeviceNo() {
        return deviceNo;
    }

    public void setDeviceNo(String deviceNo) {
        this.deviceNo = deviceNo;
    }

    public String getIdentityNumber() {
        return identityNumber;
    }

    public void setIdentityNumber(String identityNumber) {
        this.identityNumber = identityNumber;
    }

    public String getIdentifyType() {
        return identifyType;
    }

    public void setIdentifyType(String identifyType) {
        this.identifyType = identifyType;
    }
}
