package com.boc.bocsoft.mobile.bii.bus.longshortforex.model.PsnVFGTradeConfirm;

import com.boc.bocsoft.mobile.bii.bus.account.model.PublicParams;

/**
 * @author wangyang
 *         2016/12/2 10:26
 *         买卖交易确认
 */
public class PsnVFGTradeConfirmParams extends PublicParams{

    /** 结算币种 */
    private String CurrencyCode;
    /** 买卖方向 */
    private String Direction;
    /** 货币对 */
    private String CurrencyPairCode;
    /** 交易类型 MI 市价即时,LI 即时即价,PO 获利委托,SO 止损委托,OO 二选一委托,IO 追加委托,TO 连环委托,FO 追击止损挂单 */
    private String TradeType;

    public String getCurrencyCode() {
        return CurrencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        CurrencyCode = currencyCode;
    }

    public String getDirection() {
        return Direction;
    }

    public void setDirection(String direction) {
        Direction = direction;
    }

    public String getCurrencyPairCode() {
        return CurrencyPairCode;
    }

    public void setCurrencyPairCode(String currencyPairCode) {
        CurrencyPairCode = currencyPairCode;
    }

    public String getTradeType() {
        return TradeType;
    }

    public void setTradeType(String tradeType) {
        TradeType = tradeType;
    }
}
