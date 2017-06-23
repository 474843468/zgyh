package com.boc.bocsoft.mobile.bii.bus.qrcodepay.model.QRPayGetTransRecord;

import org.threeten.bp.LocalDateTime;

import java.util.List;

/**
 * 交易记录查询
 * Created by wangf on 2016/8/30.
 */
public class QRPayGetTransRecordResult {

    //记录条数
    private int recordNumber;


    public int getRecordNumber() {
        return recordNumber;
    }

    public void setRecordNumber(int recordNumber) {
        this.recordNumber = recordNumber;
    }

    private java.util.List<ListBean> List;

    public List<ListBean> getList() {
        return List;
    }

    public void setList(List<ListBean> List) {
        this.List = List;
    }

    public static class ListBean {
        //交易类型  01：支付 02：转账
        private String type;
        //转账类型
        private String transferType;
        //订单流水号
        private String tranSeq;
        //交易时间
        private LocalDateTime tranTime;
        //交易结果
        private String tranStatus;
        //交易金额
        private String tranAmount;
        //商户号
        private String merchantNo;
        //商户名称
        private String merchantName;
        //商户类型
        private String merchantCatNo;
        //交易说明 1：已退货 2：已撤销
        private String tranRemark;
        //付款方卡号 交易类型为02：转账时有意义
        private String payerAccNo;
        //付款方姓名 交易类型为02：转账时有意义
        private String payerName;
        //收款方附言 交易类型为02：转账时有意义
        private String payerComments;
        //付款凭证号 交易成功时有值
        private String voucherNum;
        //收款方卡号
        private String payeeAccNo;
        //收款方姓名
        private String payeeName;
        // 收款方附言
        private String payeeComments;



        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getTransferType() {
            return transferType;
        }

        public void setTransferType(String transferType) {
            this.transferType = transferType;
        }

        public String getTranSeq() {
            return tranSeq;
        }

        public void setTranSeq(String tranSeq) {
            this.tranSeq = tranSeq;
        }

        public LocalDateTime getTranTime() {
            return tranTime;
        }

        public void setTranTime(LocalDateTime tranTime) {
            this.tranTime = tranTime;
        }

        public String getTranStatus() {
            return tranStatus;
        }

        public void setTranStatus(String tranStatus) {
            this.tranStatus = tranStatus;
        }

        public String getTranAmount() {
            return tranAmount;
        }

        public void setTranAmount(String tranAmount) {
            this.tranAmount = tranAmount;
        }

        public String getMerchantNo() {
            return merchantNo;
        }

        public void setMerchantNo(String merchantNo) {
            this.merchantNo = merchantNo;
        }

        public String getMerchantName() {
            return merchantName;
        }

        public void setMerchantName(String merchantName) {
            this.merchantName = merchantName;
        }

        public String getMerchantCatNo() {
            return merchantCatNo;
        }

        public void setMerchantCatNo(String merchantCatNo) {
            this.merchantCatNo = merchantCatNo;
        }

        public String getTranRemark() {
            return tranRemark;
        }

        public void setTranRemark(String tranRemark) {
            this.tranRemark = tranRemark;
        }

        public String getPayerAccNo() {
            return payerAccNo;
        }

        public void setPayerAccNo(String payerAccNo) {
            this.payerAccNo = payerAccNo;
        }

        public String getPayerName() {
            return payerName;
        }

        public void setPayerName(String payerName) {
            this.payerName = payerName;
        }

        public String getPayerComments() {
            return payerComments;
        }

        public void setPayerComments(String payerComments) {
            this.payerComments = payerComments;
        }

        public String getVoucherNum() {
            return voucherNum;
        }

        public void setVoucherNum(String voucherNum) {
            this.voucherNum = voucherNum;
        }

        public String getPayeeAccNo() {
            return payeeAccNo;
        }

        public void setPayeeAccNo(String payeeAccNo) {
            this.payeeAccNo = payeeAccNo;
        }

        public String getPayeeName() {
            return payeeName;
        }

        public void setPayeeName(String payeeName) {
            this.payeeName = payeeName;
        }

        public String getPayeeComments() {
            return payeeComments;
        }

        public void setPayeeComments(String payeeComments) {
            this.payeeComments = payeeComments;
        }
    }

}
