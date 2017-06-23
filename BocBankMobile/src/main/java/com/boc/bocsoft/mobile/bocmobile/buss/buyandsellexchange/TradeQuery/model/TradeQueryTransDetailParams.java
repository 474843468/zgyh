package com.boc.bocsoft.mobile.bocmobile.buss.buyandsellexchange.TradeQuery.model;

/**4.19 019PsnFessQueryHibsExchangeTransDetail查询全渠道结购汇交易详情
 * Created by wzn7074 on 2016/11/18.
 * 只有当交易状态为成功时才允许调用此查询详细情接口
 */

public class TradeQueryTransDetailParams {
    private String bankSelfNum;//	交易流水号
    private String paymentDate;//	交易日期
    private String refNum;//	业务参号

    public String getBankSelfNum() {
        return bankSelfNum;
    }

    public void setBankSelfNum(String bankSelfNum) {
        this.bankSelfNum = bankSelfNum;
    }

    public String getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(String paymentDate) {
        this.paymentDate = paymentDate;
    }

    public String getRefNum() {
        return refNum;
    }

    public void setRefNum(String refNum) {
        this.refNum = refNum;
    }
}
