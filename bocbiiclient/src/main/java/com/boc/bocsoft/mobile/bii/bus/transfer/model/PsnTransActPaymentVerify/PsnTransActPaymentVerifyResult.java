package com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransActPaymentVerify;

import com.boc.bocsoft.mobile.bii.bus.login.model.PsnSvrRegisterDevicePre.FactorListBean;

import java.util.List;


/**
 * Created by wangtong on 2016/6/30.
 */
public class PsnTransActPaymentVerifyResult {
    private String _certDN;
    private String _plainData;
    private List<FactorListBean> factorList;
    private String needPassword;

    public String getNeedPassword() {
        return needPassword;
    }

    public void setNeedPassword(String needPassword) {
        this.needPassword = needPassword;
    }

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

    public List<FactorListBean> getFactorList() {
        return factorList;
    }

    public void setFactorList(List<FactorListBean> factorList) {
        this.factorList = factorList;
    }

}
