package com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnMobileTransferPre;

import com.boc.bocsoft.mobile.bii.bus.login.model.PsnSvrRegisterDevicePre.FactorListBean;

import java.util.List;

/**
 * Created by wangtong on 2016/7/27.
 */
public class PsnMobileTransferPreResult {

    private String isHaveAcct;//该字段表示所输入手机号是否有对应账户
    private String payeeActno;
    private String payeeBankNum;
    private String smcTrigerInterval;
    private String toAccountType;
    private String needPassword;
    private String _plainData;

    public String get_plainData() {
        return _plainData;
    }

    public void set_plainData(String _plainData) {
        this._plainData = _plainData;
    }

    public String getNeedPassword() {
        return needPassword;
    }

    public void setNeedPassword(String needPassword) {
        this.needPassword = needPassword;
    }

    private List<FactorListBean> factorList;

    public String getIsHaveAcct() {
        return isHaveAcct;
    }

    public void setIsHaveAcct(String isHaveAcct) {
        this.isHaveAcct = isHaveAcct;
    }

    public String getPayeeActno() {
        return payeeActno;
    }

    public void setPayeeActno(String payeeActno) {
        this.payeeActno = payeeActno;
    }

    public String getPayeeBankNum() {
        return payeeBankNum;
    }

    public void setPayeeBankNum(String payeeBankNum) {
        this.payeeBankNum = payeeBankNum;
    }

    public String getSmcTrigerInterval() {
        return smcTrigerInterval;
    }

    public void setSmcTrigerInterval(String smcTrigerInterval) {
        this.smcTrigerInterval = smcTrigerInterval;
    }

    public String getToAccountType() {
        return toAccountType;
    }

    public void setToAccountType(String toAccountType) {
        this.toAccountType = toAccountType;
    }

    public List<FactorListBean> getFactorList() {
        return factorList;
    }

    public void setFactorList(List<FactorListBean> factorList) {
        this.factorList = factorList;
    }

}
