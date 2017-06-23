package com.boc.bocsoft.mobile.bii.bus.account.model.PsnNcpayQuotaModifyPre;

import com.boc.bocsoft.mobile.bii.bus.login.model.PsnSvrRegisterDevicePre.FactorListBean;

import java.util.List;

/**
 * 修改交易限额预交易
 * Created by zhx on 2016/10/17
 */
public class PsnNcpayQuotaModifyPreResult {
    /**
     * 手机验证码有效时间
     */
    private String smcTrigerInterval;
    /**
     * CA加签数据XML报文
     */
    private String _plainData;
    /**
     * CA的DN值
     */
    private String _certDN;
    /**
     * field : {"name":"_signedData","type":"hidden"}
     */

    /**
     * 安全因子数组
     */
    private List<FactorListBean> factorList;

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

    public List<FactorListBean> getFactorList() {
        return factorList;
    }

    public void setFactorList(List<FactorListBean> factorList) {
        this.factorList = factorList;
    }
}
