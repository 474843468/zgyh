package com.boc.bocsoft.mobile.bii.bus.account.model.PsnSsmSignPre;

import com.boc.bocsoft.mobile.bii.bus.login.model.PsnSvrRegisterDevicePre.FactorListBean;

import java.util.List;


/**
 * Created by wangtong on 2016/8/16.
 */
public class PsnSsmSignPreResult {

    private Object _certDN;
    private String smcTrigerInterval;
    private String _plainData;

    private List<FactorListBean> factorList;

    public Object get_certDN() {
        return _certDN;
    }

    public void set_certDN(Object _certDN) {
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
