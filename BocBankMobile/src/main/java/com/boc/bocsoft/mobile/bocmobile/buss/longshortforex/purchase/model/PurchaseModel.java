package com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.purchase.model;

import org.threeten.bp.LocalDateTime;

import java.math.BigDecimal;

/**
 * @author wangyang
 *         2016/12/19 11:06
 *         双向宝交易Model
 */
public class PurchaseModel {

    public static final int QUERY_RATE_PERIOD = 3;

    public static final String TRANS_TYPE_DELETE = "5";

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
    /**
     * 首货币
     */
    private String sourceCurrency;
    /**
     * 尾货币
     */
    private String targetCurrency;
    /**
     * 结算币种
     */
    private String currency;
    /**
     * 买入价
     */
    private BigDecimal buyRate;
    /**
     * 卖出价
     */
    private BigDecimal sellRate;
    /**
     * 牌价类型 F-外币牌价,G-贵金属牌价
     */
    private String cardType;
    /**
     * tab类型
     */
    private TabType tabType;
    /**
     * 交易类型
     */
    private TransType transType;

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public String getSourceCurrency() {
        return sourceCurrency;
    }

    public void setSourceCurrency(String sourceCurrency) {
        this.sourceCurrency = sourceCurrency;
    }

    public String getTargetCurrency() {
        return targetCurrency;
    }

    public void setTargetCurrency(String targetCurrency) {
        this.targetCurrency = targetCurrency;
    }

    public BigDecimal getBuyRate() {
        return buyRate;
    }

    public void setBuyRate(BigDecimal buyRate) {
        this.buyRate = buyRate;
    }

    public BigDecimal getSellRate() {
        return sellRate;
    }

    public void setSellRate(BigDecimal sellRate) {
        this.sellRate = sellRate;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }

    public boolean isHadQuotation() {
        return buyRate != null && sellRate != null;
    }

    public TabType getTabType() {
        return tabType;
    }

    public void setTabType(TabType tabType) {
        this.tabType = tabType;
    }

    public TransType getTransType() {
        return transType;
    }

    public void setTransType(TransType transType) {
        this.transType = transType;
    }

    public boolean isForeignCurrency() {
        return "F".equals(cardType);
    }

    public boolean isShowMaxTrade(){
        return tabType == TabType.CREATE || tabType== TabType.NONE;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}
