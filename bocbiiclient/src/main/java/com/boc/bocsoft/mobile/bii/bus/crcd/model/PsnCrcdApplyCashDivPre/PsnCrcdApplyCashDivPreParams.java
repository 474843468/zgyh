package com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdApplyCashDivPre;


/**
 * Created by cry7096 on 2016/11/22.
 */
public class PsnCrcdApplyCashDivPreParams {

    /**
     * fromAccountId : 转出账户ID
     * toAccountId : 存入账户ID
     * toCardNo : 存入卡号
     * currency : 币种
     * divAmount : 分期金额
     * divPeriod : 分期期数
     * chargeMode : 手续费收取方式
     * divCharge : 分期手续费
     * divRate : 分期手续费率
     * firstPayAmount : 分期后每期应还金额-首期
     * perPayAmount : 分期后每期应还金额-后每期
     * _combinId : 安全因子组合id安全因子组合id
     * conversationId : 回话ID
     */

    private String fromAccountId;
    private String toAccountId;
    private String toCardNo;
    private String currency;
    private String divAmount;
    private String divPeriod;
    private String chargeMode;
    private String divRate;
    private String divCharge;
    private String firstPayAmount;
    private String perPayAmount;
    private String _combinId;
    private String conversationId;



    public String getFromAccountId() {
        return fromAccountId;
    }

    public void setFromAccountId(String fromAccountId) {
        this.fromAccountId = fromAccountId;
    }

    public String getToAccountId() {
        return toAccountId;
    }

    public void setToAccountId(String toAccountId) {
        this.toAccountId = toAccountId;
    }

    public String getToCardNo() {
        return toCardNo;
    }

    public void setToCardNo(String toCardNo) {
        this.toCardNo = toCardNo;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getDivAmount() {
        return divAmount;
    }

    public void setDivAmount(String divAmount) {
        this.divAmount = divAmount;
    }

    public String getDivPeriod() {
        return divPeriod;
    }

    public void setDivPeriod(String divPeriod) {
        this.divPeriod = divPeriod;
    }

    public String getChargeMode() {
        return chargeMode;
    }

    public void setChargeMode(String chargeMode) {
        this.chargeMode = chargeMode;
    }

    public String getDivCharge() {
        return divCharge;
    }

    public void setDivCharge(String divCharge) {
        this.divCharge = divCharge;
    }

    public String getDivRate() {
        return divRate;
    }

    public void setDivRate(String divRate) {
        this.divRate = divRate;
    }

    public String getFirstPayAmount() {
        return firstPayAmount;
    }

    public void setFirstPayAmount(String firstPayAmount) {
        this.firstPayAmount = firstPayAmount;
    }

    public String getPerPayAmount() {
        return perPayAmount;
    }

    public void setPerPayAmount(String perPayAmount) {
        this.perPayAmount = perPayAmount;
    }

    public String get_combinId() {
        return _combinId;
    }

    public void set_combinId(String _combinId) {
        this._combinId = _combinId;
    }

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }
}
