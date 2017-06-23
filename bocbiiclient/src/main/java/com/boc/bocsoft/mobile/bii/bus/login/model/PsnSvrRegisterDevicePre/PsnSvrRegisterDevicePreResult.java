package com.boc.bocsoft.mobile.bii.bus.login.model.PsnSvrRegisterDevicePre;

import java.util.List;

/**
 * Created by feibin on 2016/6/20.
 * I20个人设定  4.75 075 PsnSvrRegisterDevicePre设备注册预交易
 * 结果参数model
 */
public class PsnSvrRegisterDevicePreResult {


    /**
     * _certDN : null
     * smcTrigerInterval : 60
     * _plainData : null
     * factorList : [{"field":{"name":"Smc","type":"password"}}]
     */

    /**
     * CA的DN值
     */
    private String _certDN;
    /**
     * 手机验证码有效时间
     */
    private String smcTrigerInterval;
    /**
     * CA加签数据XML报文
     */
    private String _plainData;
    /**
     * field : {"name":"Smc","type":"password"}
     */

    /**
     * 安全因子数组
     */
    private List<FactorListBean> factorList;

    public String get_certDN() {
        return _certDN;
    }

    public void set_certDN(String _certDN) {
        this._certDN = _certDN;
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

    public List<FactorListBean> getFactorList() {
        return factorList;
    }

    public void setFactorList(List<FactorListBean> factorList) {
        this.factorList = factorList;
    }
}
