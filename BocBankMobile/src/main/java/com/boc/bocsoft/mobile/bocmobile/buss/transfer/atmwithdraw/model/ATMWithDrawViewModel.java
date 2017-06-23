package com.boc.bocsoft.mobile.bocmobile.buss.transfer.atmwithdraw.model;

import com.boc.bocsoft.mobile.bocmobile.base.model.FactorBean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * ATM无卡取款数据
 *
 * Created by liuweidong on 2016/7/19.
 */
public class ATMWithDrawViewModel implements Serializable {
    /*账户详情*/
    // 可用余额
    private BigDecimal availableBalance;
    // 币种
    private String currencyCode;

    /*页面信息*/
    // 取款金额
    private BigDecimal money;
    // 附言
    private String furInf;
    // 预留密码
    private String obligatePassword;
    // 加密后的预留密码
    private String obligatePassword_RC;
    // 确认预留密码
    private String reObligatePassword;
    // 确认加密后的预留密码
    private String reObligatePassword_RC;

    /*预交易响应数据*/
    // CA的DN值
    private String _certDN;
    // 手机验证码有效时间
    private String smcTrigerInterval;
    // CA加签数据XML报文
    private String _plainData;
    // 安全因子集合 name值"Smc"-需要输入手机验证码值为"Otp"-需要输入动态口令
    private List<FactorBean> factorList;

    /*交易响应数据*/
    // 到期日期
    private String dueDate;
    // 交易状态
    private String status;
    // 汇款编号
    private String remitNo;

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

    public String getFurInf() {
        return furInf;
    }

    public void setFurInf(String furInf) {
        this.furInf = furInf;
    }

    public String getObligatePassword() {
        return obligatePassword;
    }

    public void setObligatePassword(String obligatePassword) {
        this.obligatePassword = obligatePassword;
    }

    public String getObligatePassword_RC() {
        return obligatePassword_RC;
    }

    public void setObligatePassword_RC(String obligatePassword_RC) {
        this.obligatePassword_RC = obligatePassword_RC;
    }

    public String getReObligatePassword() {
        return reObligatePassword;
    }

    public void setReObligatePassword(String reObligatePassword) {
        this.reObligatePassword = reObligatePassword;
    }

    public String getReObligatePassword_RC() {
        return reObligatePassword_RC;
    }

    public void setReObligatePassword_RC(String reObligatePassword_RC) {
        this.reObligatePassword_RC = reObligatePassword_RC;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRemitNo() {
        return remitNo;
    }

    public void setRemitNo(String remitNo) {
        this.remitNo = remitNo;
    }

}
