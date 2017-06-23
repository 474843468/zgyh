package com.boc.bocsoft.mobile.bocmobile.buss.loan.pledgeloan.model;

import com.boc.bocsoft.mobile.common.utils.BeanConvertor.anno.NotConvert;

/**
 * 作者：XieDu
 * 创建时间：2016/8/20 10:59
 * 描述：
 */
public class PledgeLoanSubmitBean<T extends IPledgeLoanInfoFillViewModel> {
    @NotConvert
    T pledgeLoanInfoFillViewModel;
    private String Smc;
    private String Otp;
    private String Smc_RC;
    private String Otp_RC;
    private String state;
    private String activ;
    private String token;
    private String _signedData;
    /** 设备信息-加密 */
    private String deviceInfo;
    private String deviceInfo_RC;
    private String devicePrint;

    public T getPledgeLoanInfoFillViewModel() {
        return pledgeLoanInfoFillViewModel;
    }

    public void setPledgeLoanInfoFillViewModel(T pledgeLoanInfoFillViewModel) {
        this.pledgeLoanInfoFillViewModel = pledgeLoanInfoFillViewModel;
    }

    public String getSmc_RC() {
        return Smc_RC;
    }

    public void setSmc_RC(String smc_RC) {
        Smc_RC = smc_RC;
    }

    public String getOtp_RC() {
        return Otp_RC;
    }

    public void setOtp_RC(String otp_RC) {
        Otp_RC = otp_RC;
    }

    public String getSmc() {
        return Smc;
    }

    public void setSmc(String smc) {
        Smc = smc;
    }

    public String getOtp() {
        return Otp;
    }

    public void setOtp(String otp) {
        Otp = otp;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String get_signedData() {
        return _signedData;
    }

    public void set_signedData(String _signedData) {
        this._signedData = _signedData;
    }

    public String getDeviceInfo() {
        return deviceInfo;
    }

    public void setDeviceInfo(String deviceInfo) {
        this.deviceInfo = deviceInfo;
    }

    public String getDevicePrint() {
        return devicePrint;
    }

    public void setDevicePrint(String devicePrint) {
        this.devicePrint = devicePrint;
    }

    public String getDeviceInfo_RC() {
        return deviceInfo_RC;
    }

    public void setDeviceInfo_RC(String deviceInfo_RC) {
        this.deviceInfo_RC = deviceInfo_RC;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getActiv() {
        return activ;
    }

    public void setActiv(String activ) {
        this.activ = activ;
    }
}
