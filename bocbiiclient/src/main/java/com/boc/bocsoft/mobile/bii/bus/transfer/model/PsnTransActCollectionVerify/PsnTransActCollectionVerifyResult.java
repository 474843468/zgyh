package com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransActCollectionVerify;

import com.boc.bocsoft.mobile.bii.bus.login.model.PsnSvrRegisterDevicePre.FactorListBean;

import java.util.List;

/**
 * 主动收款预交易(与32 主动收款提交相对应)
 * Created by zhx on 2016/6/29.
 */
public class PsnTransActCollectionVerifyResult {
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
