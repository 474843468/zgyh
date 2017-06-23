package com.boc.bocsoft.mobile.bii.bus.longshortforex.model.PsnVFGTradeDetailQuery;

import org.threeten.bp.LocalDateTime;

/**
 * Created by zc7067 on 2016/11/17.
 *
 * 双向宝交易查询详情   026  PsnVFGTradeDetailQuery保证金单笔交易明细查询
 */
public class PsnVFGTradeDetailQueryResult {


    /**
     * offSet : 12
     * openPositionFlag : Y
     * foSet : 123213
     * orderStatus : N
     * channelType : 电话
     * currencyPairCode : 001014
     * settleCurrecny : 001
     * vfgTransactionId : 12321312321
     * unClosedBalance : 23
     * internalSeq : 2321313123
     * tradeType : FO
     * closedPositionFlag : Y
     * direction : B
     * exchangeTransDate : 2015/05/27
     * amount : 12311
     * firstType : P
     * gprType : P
     * tradeBackground : L
     * transactionId : 1232132131
     * pageDate : 2015/05/19
     * currency1 : {"code":"014"}
     * currency2 : {"code":"001"}
     * customerRate : 0.01
     * txRate : 0.012
     * txnDate : 2015/05/18
     */
    //优惠/惩罚点差
    private String offSet;
    //建仓标识
    private String openPositionFlag;
    //追击点差
    private int foSet;
    //交易状态
    private String orderStatus;
    //交易渠道
    private String channelType;
    //货币对
    private String currencyPairCode;
    //结算货币
    private String settleCurrecny;
    //交易序号
    private String vfgTransactionId;
    //未平仓金额
    private String unClosedBalance;
    //内部序号
    private String internalSeq;
    //交易类型
    private String tradeType;
    //是否已被平仓
    private String closedPositionFlag;
    //买卖方向
    private String direction;
    //成交/撤单时间
    private LocalDateTime exchangeTransDate;
    //交易金额
    private String amount;
    //成交类型：P=获利，S=止损
    private String firstType;
    //优惠/惩罚类型：P=优惠，R=惩罚
    private String gprType;
    //交易背景：O –建仓，N –优先平仓（根据业务需求，此字段前端展示为先开先平），C –指定平仓，L–司法强制处理，S –系统斩仓，(备注：业务要求1.5、手机pad展示建仓标识的地方取此字段)
    private String tradeBackground;
    //网银交易序号
    private String transactionId;
    //失效时间
    private LocalDateTime pageDate;
    //货币对
    private Currency1Entity currency1;
    //货币对
    private Currency2Entity currency2;
    //客户汇率
    private String customerRate;
    //成交汇率（市价即时和限价即时）
    private String txRate;
    //挂单时间
    private LocalDateTime txnDate;

    public void setOffSet(String offSet) {
        this.offSet = offSet;
    }

    public void setOpenPositionFlag(String openPositionFlag) {
        this.openPositionFlag = openPositionFlag;
    }

    public void setFoSet(int foSet) {
        this.foSet = foSet;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public void setChannelType(String channelType) {
        this.channelType = channelType;
    }

    public void setCurrencyPairCode(String currencyPairCode) {
        this.currencyPairCode = currencyPairCode;
    }

    public void setSettleCurrecny(String settleCurrecny) {
        this.settleCurrecny = settleCurrecny;
    }

    public void setVfgTransactionId(String vfgTransactionId) {
        this.vfgTransactionId = vfgTransactionId;
    }

    public void setUnClosedBalance(String unClosedBalance) {
        this.unClosedBalance = unClosedBalance;
    }

    public void setInternalSeq(String internalSeq) {
        this.internalSeq = internalSeq;
    }

    public void setTradeType(String tradeType) {
        this.tradeType = tradeType;
    }

    public void setClosedPositionFlag(String closedPositionFlag) {
        this.closedPositionFlag = closedPositionFlag;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public void setExchangeTransDate(LocalDateTime exchangeTransDate) {
        this.exchangeTransDate = exchangeTransDate;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public void setFirstType(String firstType) {
        this.firstType = firstType;
    }

    public void setGprType(String gprType) {
        this.gprType = gprType;
    }

    public void setTradeBackground(String tradeBackground) {
        this.tradeBackground = tradeBackground;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public void setPageDate(LocalDateTime pageDate) {
        this.pageDate = pageDate;
    }

    public void setCurrency1(Currency1Entity currency1) {
        this.currency1 = currency1;
    }

    public void setCurrency2(Currency2Entity currency2) {
        this.currency2 = currency2;
    }

    public void setCustomerRate(String customerRate) {
        this.customerRate = customerRate;
    }

    public void setTxRate(String txRate) {
        this.txRate = txRate;
    }

    public void setTxnDate(LocalDateTime txnDate) {
        this.txnDate = txnDate;
    }

    public String getOffSet() {
        return offSet;
    }

    public String getOpenPositionFlag() {
        return openPositionFlag;
    }

    public int getFoSet() {
        return foSet;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public String getChannelType() {
        return channelType;
    }

    public String getCurrencyPairCode() {
        return currencyPairCode;
    }

    public String getSettleCurrecny() {
        return settleCurrecny;
    }

    public String getVfgTransactionId() {
        return vfgTransactionId;
    }

    public String getUnClosedBalance() {
        return unClosedBalance;
    }

    public String getInternalSeq() {
        return internalSeq;
    }

    public String getTradeType() {
        return tradeType;
    }

    public String getClosedPositionFlag() {
        return closedPositionFlag;
    }

    public String getDirection() {
        return direction;
    }

    public LocalDateTime getExchangeTransDate() {
        return exchangeTransDate;
    }

    public String getAmount() {
        return amount;
    }

    public String getFirstType() {
        return firstType;
    }

    public String getGprType() {
        return gprType;
    }

    public String getTradeBackground() {
        return tradeBackground;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public LocalDateTime getPageDate() {
        return pageDate;
    }

    public Currency1Entity getCurrency1() {
        return currency1;
    }

    public Currency2Entity getCurrency2() {
        return currency2;
    }

    public String getCustomerRate() {
        return customerRate;
    }

    public String getTxRate() {
        return txRate;
    }

    public LocalDateTime getTxnDate() {
        return txnDate;
    }

    public static class Currency1Entity {
        /**
         * code : 014
         */
        private String code;

        public void setCode(String code) {
            this.code = code;
        }

        public String getCode() {
            return code;
        }
    }

    public static class Currency2Entity {
        /**
         * code : 001
         */
        private String code;

        public void setCode(String code) {
            this.code = code;
        }

        public String getCode() {
            return code;
        }
    }
}
