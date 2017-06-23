package com.boc.bocsoft.mobile.bii.bus.longshortforex.model.PsnVFGTrade;

import com.boc.bocsoft.mobile.bii.bus.account.model.PublicParams;

import java.math.BigDecimal;

/**
 * @author wangyang
 *         2016/12/2 10:38
 *         买卖交易参数
 */
public class PsnVFGTradeParams extends PublicParams{

    /** 货币对 */
    private String currencyPairCode;
    /** 结算币种 */
    private String currencyCode;
    /** 买卖方向 */
    private String direction;
    /** 建仓标识 当成交类型为追击止损挂单时需为N */
    private String openPositionFlag;
    /** 交易金额 */
    private String amount;
    /** 交易类型 MI 市价即时,LI 即时即价,PO 获利委托,SO 止损委托,OO 二选一委托,IO 追加委托,TO 连环委托,FO 追击止损挂单 */
    private String tradeType;
    /** 限价汇率 追击止损挂单时的客户指定的追击点差 */
    private BigDecimal rate1;
    /** 委托汇率 */
    private BigDecimal rate2;
    /** 获利汇率 */
    private BigDecimal rate3;
    /** 成交类型1 追加委托时使用P获利,S止损,追击止损挂单时为S */
    private String profitStopType1;
    /** 成交类型2 追加委托时使用P获利，S止损 */
    private String profitStopType2;
    /** 成交类型3 连环委托时使用P获利，S止损 */
    private String profitStopType3;
    /** 失效时间 时间需要满足晚于目前系统时间30分钟以上，早于当前系统时间+7天的时间 0-23点 */
    private String pageDate;

    public String getCurrencyPairCode() {
        return currencyPairCode;
    }

    public void setCurrencyPairCode(String currencyPairCode) {
        this.currencyPairCode = currencyPairCode;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getOpenPositionFlag() {
        return openPositionFlag;
    }

    public void setOpenPositionFlag(String openPositionFlag) {
        this.openPositionFlag = openPositionFlag;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getTradeType() {
        return tradeType;
    }

    public void setTradeType(String tradeType) {
        this.tradeType = tradeType;
    }

    public BigDecimal getRate1() {
        return rate1;
    }

    public void setRate1(BigDecimal rate1) {
        this.rate1 = rate1;
    }

    public BigDecimal getRate2() {
        return rate2;
    }

    public void setRate2(BigDecimal rate2) {
        this.rate2 = rate2;
    }

    public BigDecimal getRate3() {
        return rate3;
    }

    public void setRate3(BigDecimal rate3) {
        this.rate3 = rate3;
    }

    public String getProfitStopType1() {
        return profitStopType1;
    }

    public void setProfitStopType1(String profitStopType1) {
        this.profitStopType1 = profitStopType1;
    }

    public String getProfitStopType2() {
        return profitStopType2;
    }

    public void setProfitStopType2(String profitStopType2) {
        this.profitStopType2 = profitStopType2;
    }

    public String getProfitStopType3() {
        return profitStopType3;
    }

    public void setProfitStopType3(String profitStopType3) {
        this.profitStopType3 = profitStopType3;
    }

    public String getPageDate() {
        return pageDate;
    }

    public void setPageDate(String pageDate) {
        this.pageDate = pageDate;
    }
}
