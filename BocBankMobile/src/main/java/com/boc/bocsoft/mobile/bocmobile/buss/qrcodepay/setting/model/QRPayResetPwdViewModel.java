package com.boc.bocsoft.mobile.bocmobile.buss.qrcodepay.setting.model;

import com.boc.bocsoft.mobile.bocmobile.base.model.FactorBean;

import java.util.List;

/**
 * 重置支付密码
 * Created by wangf on 2016/9/3.
 */
public class QRPayResetPwdViewModel {

    /**
     * 重置支付密码预交易
     * 上送数据
     */

    //客户选择的安全验证工具id
    private String _combinId;


    /**
     * 重置支付密码预交易
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
     * 重置支付密码提交交易
     * 上送数据
     */
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
    //新支付密码
    private String password;
    //新支付密码
    private String password_RC;
    //再次输入的新支付密码
    private String passwordConform;
    //再次输入的新支付密码
    private String passwordConform_RC;
    //支付密码类型  简单密码（六位数字）：01 复杂密码（字母数字组合）：02
    private String passType;

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword_RC() {
        return password_RC;
    }

    public void setPassword_RC(String password_RC) {
        this.password_RC = password_RC;
    }

    public String getPasswordConform() {
        return passwordConform;
    }

    public void setPasswordConform(String passwordConform) {
        this.passwordConform = passwordConform;
    }

    public String getPasswordConform_RC() {
        return passwordConform_RC;
    }

    public void setPasswordConform_RC(String passwordConform_RC) {
        this.passwordConform_RC = passwordConform_RC;
    }

    public String getPassType() {
        return passType;
    }

    public void setPassType(String passType) {
        this.passType = passType;
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
