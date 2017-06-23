package com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLOANCycleLoanEApplyVerify;

import com.boc.bocsoft.mobile.bii.bus.login.model.PsnSvrRegisterDevicePre.FactorListBean;

import java.util.List;

/**
 * 贷款管理-中银E贷-提款-中银E贷用款预交易返回参数
 * Created by xintong on 2016/6/16.
 */
public class PsnLOANCycleLoanEApplyVerifyResult {


    /**
     * 安全因子数组
     * (name值"Smc"-需要输入手机验证码值为"Otp"-需要输入动态口令)
     */
    private List<FactorListBean> factorList;

    //手机验证码有效时间
    private String smcTrigerInterval;

    //CA加签数据XML报文
    private String _plainData;

    //CA的DN值
    private String _certDN;


    public List<FactorListBean> getFactorList() {
        return factorList;
    }

    public void setFactorList(List<FactorListBean> factorList) {
        this.factorList = factorList;
    }

    public String getSmcTrigerInterval() {
        return smcTrigerInterval;
    }

    public void setSmcTrigerInterval(String smcTrigerInterval) {
        this.smcTrigerInterval = smcTrigerInterval;
    }

    public String get_plainData() {
        return _plainData;
    }

    public void set_plainData(String _plainData) {
        this._plainData = _plainData;
    }

    public String get_certDN() {
        return _certDN;
    }

    public void set_certDN(String _certDN) {
        this._certDN = _certDN;
    }


    @Override
    public String toString() {
        return "PsnLOANCycleLoanEApplyVerifyResult{" +
                "factorList=" + factorList +
                ", smcTrigerInterval='" + smcTrigerInterval + '\'' +
                ", _plainData='" + _plainData + '\'' +
                ", _certDN='" + _certDN + '\'' +
                '}';
    }
}
