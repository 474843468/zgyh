package com.boc.bocsoft.mobile.bocmobile.buss.account.loss.model;

import com.boc.bocsoft.mobile.bocmobile.base.model.FactorBean;

import org.threeten.bp.LocalDateTime;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * 挂失的view层数据
 *
 * Created by liuweidong on 2016/7/6.
 */
public class LossViewModel implements Serializable {
    /**
     * 保存挂失的时间
     */
    private LocalDateTime time;

    /**
     * 挂失期限 1：5天；2：长期
     */
    private String lossDays;
    /**
     * 客户姓名
     */
    private String Name;

    /**
     * 是否同时冻结借记卡主账户 Y：是；N：否
     */
    private String accFlozenFlag;

    /**
     * 挂失类型：0-挂失；1-挂失及补卡
     */
    private String selectLossType;


    // 挂失手续费
    private BigDecimal lossFee;
    // 挂失手续费币种
    private String lossFeeCurrency;
    // 补卡手续费
    private BigDecimal reportFee;
    // 试费查询是否成功标识位
    private String getChargeFlag;
    // 补卡手续费币种
    private String reportFeeCurrency;
    // 邮寄地址
    private String mailAddress;
    // 邮寄地址类型
    private String mailAddressType;


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
     * 账户借方冻结是否成功
     */
    private boolean accFrozenStatus;
    /**
     * 借记卡临时挂失是否成功
     */
    private boolean reportLossStatus;

    /**
     * 手机交易码
     */
    private String Smc;

    /**
     * 动态口令
     */
    private String Otp;

    /**
     * CA认证生成的密文
     */
    private String _signedData;

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public String getLossDays() {
        return lossDays;
    }

    public void setLossDays(String lossDays) {
        this.lossDays = lossDays;
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public String getAccFlozenFlag() {
        return accFlozenFlag;
    }

    public void setAccFlozenFlag(String accFlozenFlag) {
        this.accFlozenFlag = accFlozenFlag;
    }

    public String getSelectLossType() {
        return selectLossType;
    }

    public void setSelectLossType(String selectLossType) {
        this.selectLossType = selectLossType;
    }

    public BigDecimal getLossFee() {
        return lossFee;
    }

    public void setLossFee(BigDecimal lossFee) {
        this.lossFee = lossFee;
    }

    public String getLossFeeCurrency() {
        return lossFeeCurrency;
    }

    public void setLossFeeCurrency(String lossFeeCurrency) {
        this.lossFeeCurrency = lossFeeCurrency;
    }

    public BigDecimal getReportFee() {
        return reportFee;
    }

    public void setReportFee(BigDecimal reportFee) {
        this.reportFee = reportFee;
    }

    public String getGetChargeFlag() {
        return getChargeFlag;
    }

    public void setGetChargeFlag(String getChargeFlag) {
        this.getChargeFlag = getChargeFlag;
    }

    public String getReportFeeCurrency() {
        return reportFeeCurrency;
    }

    public void setReportFeeCurrency(String reportFeeCurrency) {
        this.reportFeeCurrency = reportFeeCurrency;
    }

    public String getMailAddress() {
        return mailAddress;
    }

    public void setMailAddress(String mailAddress) {
        this.mailAddress = mailAddress;
    }

    public String getMailAddressType() {
        return mailAddressType;
    }

    public void setMailAddressType(String mailAddressType) {
        this.mailAddressType = mailAddressType;
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

    public boolean isAccFrozenStatus() {
        return accFrozenStatus;
    }

    public void setAccFrozenStatus(boolean accFrozenStatus) {
        this.accFrozenStatus = accFrozenStatus;
    }

    public boolean isReportLossStatus() {
        return reportLossStatus;
    }

    public void setReportLossStatus(boolean reportLossStatus) {
        this.reportLossStatus = reportLossStatus;
    }

    public String getSmc() {
        return Smc;
    }

    public void setSmc(String Smc) {
        this.Smc = Smc;
    }

    public String getOtp() {
        return Otp;
    }

    public void setOtp(String Otp) {
        this.Otp = Otp;
    }

    public String get_signedData() {
        return _signedData;
    }

    public void set_signedData(String _signedData) {
        this._signedData = _signedData;
    }

}
