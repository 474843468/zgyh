package com.boc.bocsoft.mobile.bii.bus.fess.model.PsnFessQueryHibsExchangeTrans;

import java.util.List;

/**4.18 018PsnFessQueryHibsExchangeTrans查询全渠道结购汇交易列表
 * Created by gwluo on 2016/11/18.
 */

public class PsnFessQueryHibsExchangeTransResult {
    private List<FessExchangeTrans>fessExchangeTransList;

    public List<FessExchangeTrans> getFessExchangeTransList() {
        return fessExchangeTransList;
    }

    public void setFessExchangeTransList(List<FessExchangeTrans> fessExchangeTransList) {
        this.fessExchangeTransList = fessExchangeTransList;
    }

    public class FessExchangeTrans{
        private String channel;//	交易渠道	String	参见附录“渠道标识”
        private String trsChannelNum;//	渠道流水号	String
        private String refNum;//	业务参号	String
        private String bankSelfNum;//	银行自身交易流水号	String
        private String transType;//	交易类型	String	01结汇 11购汇
        private String paymentDate;//	交易日期	String
        private String paymentTime;//	交易时间	String
        private String amount;//	外币交易金额	String
        private String cashRemit;//	钞汇	String	01现钞 02现汇
        private String currencyCode;//	币种	String
        private String status;//	交易状态	String	00-	成功 01-	失败 02-	未明（只有当交易状态为成功时才允许调用查询详细信息接口，失败和未明的详情页面展示取用此列表查询接口返回的数据）
        private String accountNumber;//	交易账号
        private String fundTypeCode;//	结汇/购汇资金属性代码
        private String tranRetCode;//	交易结果返回码
        private String handleStatus;//	手工处理状态	9-未处理0-手工处理成功 1-原交易已删除
        private String totalCount;//	记录总数	String

        public String getChannel() {
            return channel;
        }

        public void setChannel(String channel) {
            this.channel = channel;
        }

        public String getTrsChannelNum() {
            return trsChannelNum;
        }

        public void setTrsChannelNum(String trsChannelNum) {
            this.trsChannelNum = trsChannelNum;
        }

        public String getRefNum() {
            return refNum;
        }

        public void setRefNum(String refNum) {
            this.refNum = refNum;
        }

        public String getBankSelfNum() {
            return bankSelfNum;
        }

        public void setBankSelfNum(String bankSelfNum) {
            this.bankSelfNum = bankSelfNum;
        }

        public String getTransType() {
            return transType;
        }

        public void setTransType(String transType) {
            this.transType = transType;
        }

        public String getPaymentDate() {
            return paymentDate;
        }

        public void setPaymentDate(String paymentDate) {
            this.paymentDate = paymentDate;
        }

        public String getPaymentTime() {
            return paymentTime;
        }

        public void setPaymentTime(String paymentTime) {
            this.paymentTime = paymentTime;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getCashRemit() {
            return cashRemit;
        }

        public void setCashRemit(String cashRemit) {
            this.cashRemit = cashRemit;
        }

        public String getCurrencyCode() {
            return currencyCode;
        }

        public void setCurrencyCode(String currencyCode) {
            this.currencyCode = currencyCode;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getAccountNumber() {
            return accountNumber;
        }

        public void setAccountNumber(String accountNumber) {
            this.accountNumber = accountNumber;
        }

        public String getFundTypeCode() {
            return fundTypeCode;
        }

        public void setFundTypeCode(String fundTypeCode) {
            this.fundTypeCode = fundTypeCode;
        }

        public String getTranRetCode() {
            return tranRetCode;
        }

        public void setTranRetCode(String tranRetCode) {
            this.tranRetCode = tranRetCode;
        }

        public String getHandleStatus() {
            return handleStatus;
        }

        public void setHandleStatus(String handleStatus) {
            this.handleStatus = handleStatus;
        }

        public String getTotalCount() {
            return totalCount;
        }

        public void setTotalCount(String totalCount) {
            this.totalCount = totalCount;
        }
    }
}
