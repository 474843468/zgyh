package com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransQueryTransferRecord;

import org.threeten.bp.LocalDate;

import java.util.List;

/**
 * 转账记录查询
 * Created by wangf on 2016/6/23.
 */
public class PsnTransQueryTransferRecordResult {


    /**
     * recordnumber : 2
     * list : [{"payeeAccountType":null,"payerAccountType":null,"transMode":null,"cashRemit":null,"transferType":"03","payeeAccountNumber":"6123125564997231","payerAccountNumber":"102450381920","payeeCountry":null,"feeCur":"001","amount":1000,"channel":"1","commissionCharge":null,"furInfo":null,"payeeBankName":null,"payeeIbk":null,"payerIbknum":null,"postage":null,"returnCode":null,"transactionId":1555691496,"payeeAccountName":"你好","payerAccountName":null,"batSeq":1082151061,"status":"I","paymentDate":"2025/04/25","firstSubmitDate":null,"feeCur2":null,"reexchangeStatus":"0","cashRemitExchange":null},{"payeeAccountType":null,"payerAccountType":null,"transMode":null,"cashRemit":null,"transferType":"01","payeeAccountNumber":"4563513600036928505","payerAccountNumber":"102450381920","payeeCountry":null,"feeCur":"001","amount":1000,"channel":"1","commissionCharge":null,"furInfo":null,"payeeBankName":null,"payeeIbk":null,"payerIbknum":null,"postage":null,"returnCode":null,"transactionId":1555691494,"payeeAccountName":"李五","payerAccountName":null,"batSeq":1082151059,"status":"A","paymentDate":"2025/04/25","firstSubmitDate":null,"feeCur2":null,"reexchangeStatus":"0","cashRemitExchange":null}]
     */

    private int recordnumber;
    /**
     * payeeAccountType : null
     * payerAccountType : null
     * transMode : null
     * cashRemit : null
     * transferType : 03
     * payeeAccountNumber : 6123125564997231
     * payerAccountNumber : 102450381920
     * payeeCountry : null
     * feeCur : 001
     * amount : 1000
     * channel : 1
     * commissionCharge : null
     * furInfo : null
     * payeeBankName : null
     * payeeIbk : null
     * payerIbknum : null
     * postage : null
     * returnCode : null
     * transactionId : 1555691496
     * payeeAccountName : 你好
     * payerAccountName : null
     * batSeq : 1082151061
     * status : I
     * paymentDate : 2025/04/25
     * firstSubmitDate : null
     * feeCur2 : null
     * reexchangeStatus : 0
     * cashRemitExchange : null
     */

    private List<ListBean> list;

    public int getRecordnumber() {
        return recordnumber;
    }

    public void setRecordnumber(int recordnumber) {
        this.recordnumber = recordnumber;
    }

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean {
        //转入帐户类型
        private String payeeAccountType;
        //转出帐户类型
        private String payerAccountType;
        //转账方式
        private String transMode;
        //钞汇标志
        private String cashRemit;
        //转账类型
        private String transferType;
        //转入账户
        private String payeeAccountNumber;
        //转出账户
        private String payerAccountNumber;
        //收款人常驻国家（地区）
        private String payeeCountry;
        //币种
        private String feeCur;
        //转账金额
        private String amount;
        //交易渠道
        private String channel;
        //手续费
        private String commissionCharge;
        //备注
        private String furInfo;
        //转入账户开户行
        private String payeeBankName;
        //转入账户地区
        private String payeeIbk;
        //转出账号地区
        private String payerIbknum;
        //电汇费
        private String postage;
        //返回码
        private String returnCode;
        //交易流水号
        private String transactionId;
        //收款人姓名
        private String payeeAccountName;
        //转出账户姓名
        private String payerAccountName;
        //转账批次号
        private String batSeq;
        //转账状态
        private String status;
        //转账日期
        private LocalDate paymentDate;
        private String firstSubmitDate;
        private String feeCur2;
        //退汇交易状态
        private String reexchangeStatus;
        private String cashRemitExchange;

        public String getPayeeAccountType() {
            return payeeAccountType;
        }

        public void setPayeeAccountType(String payeeAccountType) {
            this.payeeAccountType = payeeAccountType;
        }

        public String getPayerAccountType() {
            return payerAccountType;
        }

        public void setPayerAccountType(String payerAccountType) {
            this.payerAccountType = payerAccountType;
        }

        public String getTransMode() {
            return transMode;
        }

        public void setTransMode(String transMode) {
            this.transMode = transMode;
        }

        public String getCashRemit() {
            return cashRemit;
        }

        public void setCashRemit(String cashRemit) {
            this.cashRemit = cashRemit;
        }

        public String getTransferType() {
            return transferType;
        }

        public void setTransferType(String transferType) {
            this.transferType = transferType;
        }

        public String getPayeeAccountNumber() {
            return payeeAccountNumber;
        }

        public void setPayeeAccountNumber(String payeeAccountNumber) {
            this.payeeAccountNumber = payeeAccountNumber;
        }

        public String getPayerAccountNumber() {
            return payerAccountNumber;
        }

        public void setPayerAccountNumber(String payerAccountNumber) {
            this.payerAccountNumber = payerAccountNumber;
        }

        public String getPayeeCountry() {
            return payeeCountry;
        }

        public void setPayeeCountry(String payeeCountry) {
            this.payeeCountry = payeeCountry;
        }

        public String getFeeCur() {
            return feeCur;
        }

        public void setFeeCur(String feeCur) {
            this.feeCur = feeCur;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getChannel() {
            return channel;
        }

        public void setChannel(String channel) {
            this.channel = channel;
        }

        public String getCommissionCharge() {
            return commissionCharge;
        }

        public void setCommissionCharge(String commissionCharge) {
            this.commissionCharge = commissionCharge;
        }

        public String getFurInfo() {
            return furInfo;
        }

        public void setFurInfo(String furInfo) {
            this.furInfo = furInfo;
        }

        public String getPayeeBankName() {
            return payeeBankName;
        }

        public void setPayeeBankName(String payeeBankName) {
            this.payeeBankName = payeeBankName;
        }

        public String getPayeeIbk() {
            return payeeIbk;
        }

        public void setPayeeIbk(String payeeIbk) {
            this.payeeIbk = payeeIbk;
        }

        public String getPayerIbknum() {
            return payerIbknum;
        }

        public void setPayerIbknum(String payerIbknum) {
            this.payerIbknum = payerIbknum;
        }

        public String getPostage() {
            return postage;
        }

        public void setPostage(String postage) {
            this.postage = postage;
        }

        public String getReturnCode() {
            return returnCode;
        }

        public void setReturnCode(String returnCode) {
            this.returnCode = returnCode;
        }

        public String getTransactionId() {
            return transactionId;
        }

        public void setTransactionId(String transactionId) {
            this.transactionId = transactionId;
        }

        public String getPayeeAccountName() {
            return payeeAccountName;
        }

        public void setPayeeAccountName(String payeeAccountName) {
            this.payeeAccountName = payeeAccountName;
        }

        public String getPayerAccountName() {
            return payerAccountName;
        }

        public void setPayerAccountName(String payerAccountName) {
            this.payerAccountName = payerAccountName;
        }

        public String getBatSeq() {
            return batSeq;
        }

        public void setBatSeq(String batSeq) {
            this.batSeq = batSeq;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public LocalDate getPaymentDate() {
            return paymentDate;
        }

        public void setPaymentDate(LocalDate paymentDate) {
            this.paymentDate = paymentDate;
        }

        public String getFirstSubmitDate() {
            return firstSubmitDate;
        }

        public void setFirstSubmitDate(String firstSubmitDate) {
            this.firstSubmitDate = firstSubmitDate;
        }

        public String getFeeCur2() {
            return feeCur2;
        }

        public void setFeeCur2(String feeCur2) {
            this.feeCur2 = feeCur2;
        }

        public String getReexchangeStatus() {
            return reexchangeStatus;
        }

        public void setReexchangeStatus(String reexchangeStatus) {
            this.reexchangeStatus = reexchangeStatus;
        }

        public String getCashRemitExchange() {
            return cashRemitExchange;
        }

        public void setCashRemitExchange(String cashRemitExchange) {
            this.cashRemitExchange = cashRemitExchange;
        }
    }
}
