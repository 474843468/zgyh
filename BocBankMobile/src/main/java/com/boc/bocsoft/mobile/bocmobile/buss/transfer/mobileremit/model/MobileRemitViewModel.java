package com.boc.bocsoft.mobile.bocmobile.buss.transfer.mobileremit.model;

import com.boc.bocsoft.mobile.bocmobile.base.model.FactorBean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * Created by liuweidong on 2016/7/26.
 */
public class MobileRemitViewModel implements Serializable {
    /**
     * 可用余额
     */
    private BigDecimal availableBalance;
    /**
     * 币种
     */
    private String currencyCode;
    /**
     * 汇款金额
     */
    private BigDecimal money;
    /**
     * 收款人姓名
     */
    private String payeeName;
    /**
     * 收款人手机号
     */
    private String payeeMobile;
    /**
     * 附言
     */
    private String remark;

    /**
     * 取款密码
     */
    private String withDrawPwd;
    private String withDrawPwd_RC;
    /**
     * 确认密码
     */
    private String withDrawPwdConf;
    private String withDrawPwdConf_RC;

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
     * 安全因子集合 name值"Smc"-需要输入手机验证码值为"Otp"-需要输入动态口令
     */
    private List<FactorBean> factorList;

    /**
     * 到期日期
     */
    private String dueDate;

    public BigDecimal getAvailableBalance() {
        return availableBalance;
    }

    public void setAvailableBalance(BigDecimal availableBalance) {
        this.availableBalance = availableBalance;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    public String getPayeeName() {
        return payeeName;
    }

    public void setPayeeName(String payeeName) {
        this.payeeName = payeeName;
    }

    public String getPayeeMobile() {
        return payeeMobile;
    }

    public void setPayeeMobile(String payeeMobile) {
        this.payeeMobile = payeeMobile;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getWithDrawPwd() {
        return withDrawPwd;
    }

    public void setWithDrawPwd(String withDrawPwd) {
        this.withDrawPwd = withDrawPwd;
    }

    public String getWithDrawPwd_RC() {
        return withDrawPwd_RC;
    }

    public void setWithDrawPwd_RC(String withDrawPwd_RC) {
        this.withDrawPwd_RC = withDrawPwd_RC;
    }

    public String getWithDrawPwdConf() {
        return withDrawPwdConf;
    }

    public void setWithDrawPwdConf(String withDrawPwdConf) {
        this.withDrawPwdConf = withDrawPwdConf;
    }

    public String getWithDrawPwdConf_RC() {
        return withDrawPwdConf_RC;
    }

    public void setWithDrawPwdConf_RC(String withDrawPwdConf_RC) {
        this.withDrawPwdConf_RC = withDrawPwdConf_RC;
    }

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

    public List<FactorBean> getFactorList() {
        return factorList;
    }

    public void setFactorList(List<FactorBean> factorList) {
        this.factorList = factorList;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }
}
