package com.boc.bocsoft.mobile.bii.bus.longshortforex.model.PsnVFGTrade;

/**
 * @author wangyang
 *         2016/12/2 10:50
 *         买卖交易结果
 */
public class PsnVFGTradeResult {

    /** 网银交易序号 */
    private String transactionId;
    /** 成交汇率（市价即时和限价即时） */
    private String TxRate;
    /** 市场汇率（市价即时） */
    private String MacketRate;
    /** 成交时间（市价即时和限价即时） */
    private String TxTime;
    /** 委托序号（所有委托类交易） */
    private String EntrustNo;
    /** 委托时间（所有委托类交易） */
    private String EntrustTime;
    /** 优惠/惩罚类型 P 优惠,R 惩罚 */
    private String gprType;
    /** 优惠/惩罚点差 */
    private String offSet;

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getTxRate() {
        return TxRate;
    }

    public void setTxRate(String txRate) {
        TxRate = txRate;
    }

    public String getMacketRate() {
        return MacketRate;
    }

    public void setMacketRate(String macketRate) {
        MacketRate = macketRate;
    }

    public String getTxTime() {
        return TxTime;
    }

    public void setTxTime(String txTime) {
        TxTime = txTime;
    }

    public String getEntrustNo() {
        return EntrustNo;
    }

    public void setEntrustNo(String entrustNo) {
        EntrustNo = entrustNo;
    }

    public String getEntrustTime() {
        return EntrustTime;
    }

    public void setEntrustTime(String entrustTime) {
        EntrustTime = entrustTime;
    }

    public String getGprType() {
        return gprType;
    }

    public void setGprType(String gprType) {
        this.gprType = gprType;
    }

    public String getOffSet() {
        return offSet;
    }

    public void setOffSet(String offSet) {
        this.offSet = offSet;
    }
}
