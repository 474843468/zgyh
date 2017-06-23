package com.boc.bocsoft.mobile.bii.bus.account.model.PsnNcpayOpenPre;


import com.boc.bocsoft.mobile.bii.bus.login.model.PsnSvrRegisterDevicePre.FactorListBean;

import java.util.List;

/**
 * Fragment:开通跨行订购
 * 安全因子数组与CA结果
 * Created by zhx on 2016/10/19
 */
public class PsnNcpayOpenPreResult {
    /** name- 值‘Smc’需要输入手机验证码,值'Otp'需要输入动态口令,值'signedData'需要CA认证 */
    private List<FactorListBean> factorList;
    /** 手机验证码有效时间 */
    private String smcTrigerInterval;
    /** CA加签数据XML报文 */
    private String _plainData;
    /** CA的DN值 */
    private String certDN;

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

    public String getCertDN() {
        return certDN;
    }

    public void setCertDN(String certDN) {
        this.certDN = certDN;
    }
}
