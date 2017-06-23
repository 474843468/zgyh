package com.boc.bocsoft.mobile.bii.bus.fess.model.PsnGetExchangeOutlay;

import org.threeten.bp.LocalDateTime;

import java.math.BigDecimal;

/**
 * 登录前查询结售汇行情列-返回报文
 * Created by lxw on 2016/8/24 0024.
 */
public class PsnGetExchangeOutlayResult {

    /**
     * updateDate : 2018/01/31 11:23:05
     * curCode : USD
     * buyRate : 621.35
     * sellRate : 623.85
     * buyNoteRate : 616.37
     * sellNoteRate : 623.85
     */

    // 牌价更新日期
    private String updateDate;
    // 产品币种
    private String curCode;
    // buyRate
    private BigDecimal buyRate;
    // 银行现汇卖出价
    private BigDecimal sellRate;
    // 银行现钞买入价
    private BigDecimal buyNoteRate;
    // 银行现钞卖出价
    private BigDecimal sellNoteRate;

    public BigDecimal getSellNoteRate() {
        return sellNoteRate;
    }

    public void setSellNoteRate(BigDecimal sellNoteRate) {
        this.sellNoteRate = sellNoteRate;
    }


    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    public String getCurCode() {
        return curCode;
    }

    public void setCurCode(String curCode) {
        this.curCode = curCode;
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

    public BigDecimal getBuyNoteRate() {
        return buyNoteRate;
    }

    public void setBuyNoteRate(BigDecimal buyNoteRate) {
        this.buyNoteRate = buyNoteRate;
    }
}
