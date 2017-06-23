package com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdQueryAppertainAndMess;

import java.math.BigDecimal;

/**
 * Name: liukai
 * Time：2016/12/2 11:26.
 * Created by lk7066 on 2016/12/2.
 * It's used to
 */

public class PsnCrcdQueryAppertainAndMessResult {

    /**
     * 短信对象发送标识
     * */
    private String smeSendFlag;

    /**
     * 交易流量
     * */
    private BigDecimal tradeFlowAmount;

    public String getSmeSendFlag() {
        return smeSendFlag;
    }

    public void setSmeSendFlag(String smeSendFlag) {
        this.smeSendFlag = smeSendFlag;
    }

    public BigDecimal getTradeFlowAmount() {
        return tradeFlowAmount;
    }

    public void setTradeFlowAmount(BigDecimal tradeFlowAmount) {
        this.tradeFlowAmount = tradeFlowAmount;
    }
}
