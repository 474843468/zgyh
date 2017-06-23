package com.boc.bocsoft.mobile.bocmobile.buss.loan.common.draw.model;

import com.boc.bocsoft.mobile.bii.bus.login.model.PsnSvrRegisterDevicePre.FactorListBean;

import java.util.List;

/**
 * 贷款管理-循环类贷款-提款-用款预交易
 * Created by liuzc on 2016/8/24.
 */
public class LoanDrawApplyVerifyRes {


    //安全因子数组
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
}
