package com.boc.bocsoft.mobile.bii.bus.account.model.PsnNcpayServiceChoose;

import java.math.BigDecimal;

/**
 * @author wangyang
 *         2016/10/11 11:15
 *         获取开通状态结果
 */
public class PsnNcpayServiceChooseResult {

    /** 服务开通状态 N: 未开通,Y: 开通 */
    private String status;
    /** 限额 */
    private BigDecimal quota;
    /** 卡品牌 V: VISA卡,M: Master Card卡,U: 银联卡 */
    private String cardBrand;

    private String acctNum;

    public String getAcctNum() {
        return acctNum;
    }

    public void setAcctNum(String acctNum) {
        this.acctNum = acctNum;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public BigDecimal getQuota() {
        return quota;
    }

    public void setQuota(BigDecimal quota) {
        this.quota = quota;
    }

    public String getCardBrand() {
        return cardBrand;
    }

    public void setCardBrand(String cardBrand) {
        this.cardBrand = cardBrand;
    }
}
