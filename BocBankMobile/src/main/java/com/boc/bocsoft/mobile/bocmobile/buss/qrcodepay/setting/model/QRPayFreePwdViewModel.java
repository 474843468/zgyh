package com.boc.bocsoft.mobile.bocmobile.buss.qrcodepay.setting.model;

import com.boc.bocsoft.mobile.bocmobile.base.model.FactorBean;

import java.util.List;

/**
 * 小额免密
 * Created by wangf on 2016/9/3.
 */
public class QRPayFreePwdViewModel {

    /**
     * 开通小额免密服务预交易
     * 上送数据
     */

    //客户选择的安全验证工具id
    private String _combinId;


    /**
     * 开通小额免密服务预交易
     * 响应数据
     */

    //CA的DN值
    private String _certDN;
    //CA加签数据XML报文
    private String _plainData;
    //手机验证码有效时间
    private String smcTrigerInterval;
    //安全因子数组
    private List<FactorBean> factorList;


    /**
     * 开通小额免密服务提交交易
     * 上送数据
     */
    //支付卡流水
    private String actSeq;
    //短信交易码加密串
    private String Smc;
    //短信交易码加密串
    private String Smc_RC;
    //动态口令加密串
    private String Otp;
    //动态口令加密串
    private String Otp_RC;
    //USB KEY或音频KEY加密串
    private String _signedData;
    //手机硬件绑定
    private String deviceInfo;
    private String deviceInfo_RC;

    private String activ;
    private String state;



    public String get_certDN() {
        return _certDN;
    }

    public void set_certDN(String _certDN) {
        this._certDN = _certDN;
    }

    public String get_plainData() {
        return _plainData;
    }

    public void set_plainData(String _plainData) {
        this._plainData = _plainData;
    }

    public String getSmcTrigerInterval() {
        return smcTrigerInterval;
    }

    public void setSmcTrigerInterval(String smcTrigerInterval) {
        this.smcTrigerInterval = smcTrigerInterval;
    }

    public List<FactorBean> getFactorList() {
        return factorList;
    }

    public void setFactorList(List<FactorBean> factorList) {
        this.factorList = factorList;
    }

    public String getSmc() {
        return Smc;
    }

    public void setSmc(String smc) {
        Smc = smc;
    }

    public String getSmc_RC() {
        return Smc_RC;
    }

    public void setSmc_RC(String smc_RC) {
        Smc_RC = smc_RC;
    }

    public String getOtp() {
        return Otp;
    }

    public void setOtp(String otp) {
        Otp = otp;
    }

    public String getOtp_RC() {
        return Otp_RC;
    }

    public void setOtp_RC(String otp_RC) {
        Otp_RC = otp_RC;
    }

    public String get_signedData() {
        return _signedData;
    }

    public void set_signedData(String _signedData) {
        this._signedData = _signedData;
    }

    public String get_combinId() {
        return _combinId;
    }

    public void set_combinId(String _combinId) {
        this._combinId = _combinId;
    }

    public String getActSeq() {
        return actSeq;
    }

    public void setActSeq(String actSeq) {
        this.actSeq = actSeq;
    }

    public String getDeviceInfo() {
        return deviceInfo;
    }

    public void setDeviceInfo(String deviceInfo) {
        this.deviceInfo = deviceInfo;
    }

    public String getDeviceInfo_RC() {
        return deviceInfo_RC;
    }

    public void setDeviceInfo_RC(String deviceInfo_RC) {
        this.deviceInfo_RC = deviceInfo_RC;
    }

    public String getActiv() {
        return activ;
    }

    public void setActiv(String activ) {
        this.activ = activ;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
