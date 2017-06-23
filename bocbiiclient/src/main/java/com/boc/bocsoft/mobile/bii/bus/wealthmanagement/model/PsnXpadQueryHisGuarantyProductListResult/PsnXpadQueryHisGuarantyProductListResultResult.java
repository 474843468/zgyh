package com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadQueryHisGuarantyProductListResult;

import org.threeten.bp.LocalDate;

import java.util.List;

/**
 * 组合购买历史交易状况查询
 * Created by zhx on 2016/9/5
 */
public class PsnXpadQueryHisGuarantyProductListResultResult {

    /**
     * recordNumber : 1
     * List : [{"cashRemit":"02","status":"0","prodCode":"DE094353453","cancelPrice":4.12,"accountNo":"3242******7776","returnDate":"2015/10/10","buyAmt":5600,"currency":"014","trfPrice":56,"amount":5000,"prodName":"中银力度理财","accountKey":"59843708","cancelDate":"2015/10/10","channel":"02","tranSeq":"5409865445"},{"cashRemit":"02","status":"0","prodCode":"DE094353453","cancelPrice":4.12,"accountNo":"3242******7776","returnDate":"2015/10/10","buyAmt":5600,"currency":"014","trfPrice":56,"amount":5000,"prodName":"中银力度理财","accountKey":"59843708","cancelDate":"2015/10/10","channel":"02","tranSeq":"5409865415"},{"cashRemit":"02","status":"0","prodCode":"DE094353453","cancelPrice":4.12,"accountNo":"3242******7776","returnDate":"2015/10/10","buyAmt":5600,"currency":"014","trfPrice":56,"amount":5000,"prodName":"中银力度理财","accountKey":"59843708","cancelDate":"2015/10/10","channel":"02","tranSeq":"5409865425"}]
     */
    private int recordNumber;
    private List<ListEntity> list;

    public void setRecordNumber(int recordNumber) {
        this.recordNumber = recordNumber;
    }

    public void setList(List<ListEntity> List) {
        this.list = List;
    }

    public int getRecordNumber() {
        return recordNumber;
    }

    public List<ListEntity> getList() {
        return list;
    }

    public static class ListEntity {
        /**
         * cashRemit : 02
         * status : 0
         * prodCode : DE094353453
         * cancelPrice : 4.12
         * accountNo : 3242******7776
         * returnDate : 2015/10/10
         * buyAmt : 5600
         * currency : 014
         * trfPrice : 56
         * amount : 5000
         * prodName : 中银力度理财
         * accountKey : 59843708
         * cancelDate : 2015/10/10
         * channel : 02
         * tranSeq : 5409865445
         */

        // 组合交易流水号
        private String tranSeq;
        // 银行账号（加星显示）
        private String accountNo;
        // 账号缓存标识
        private String accountKey;
        // 产品代码
        private String prodCode;
        // 产品名称
        private String prodName;
        // 币种（001：人民币元 014：美元 012：英镑 013：港币 028: 加拿大元 029：澳元 038：欧元 027：日元）
        private String currency;
        // 钞汇标识（01：钞 02：汇 00：人民币）
        private String cashRemit;
        // 购买金额
        private String buyAmt;
        // 购买价格
        private String trfPrice;
        // 成交金额
        private String amount;
        // 解质价格
        private String cancelPrice;
        // 状态（0：正常 1：解除）
        private String status;
        // 交易日期（即质押日期）
        private LocalDate returnDate;
        // 解质日期
        private LocalDate cancelDate;
        // 渠道（交易渠道 0：理财系统交易 1：（核心系统 OFP）柜面 2：网银 3：电话银行自助 4：电话银行人工 5:手机银行 6:家居银行 7:微信银行 8:自助终端 9：OCRM ）
        private String channel;

        public void setCashRemit(String cashRemit) {
            this.cashRemit = cashRemit;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public void setProdCode(String prodCode) {
            this.prodCode = prodCode;
        }

        public void setCancelPrice(String cancelPrice) {
            this.cancelPrice = cancelPrice;
        }

        public void setAccountNo(String accountNo) {
            this.accountNo = accountNo;
        }

        public void setReturnDate(LocalDate returnDate) {
            this.returnDate = returnDate;
        }

        public void setBuyAmt(String buyAmt) {
            this.buyAmt = buyAmt;
        }

        public void setCurrency(String currency) {
            this.currency = currency;
        }

        public void setTrfPrice(String trfPrice) {
            this.trfPrice = trfPrice;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public void setProdName(String prodName) {
            this.prodName = prodName;
        }

        public void setAccountKey(String accountKey) {
            this.accountKey = accountKey;
        }

        public void setCancelDate(LocalDate cancelDate) {
            this.cancelDate = cancelDate;
        }

        public void setChannel(String channel) {
            this.channel = channel;
        }

        public void setTranSeq(String tranSeq) {
            this.tranSeq = tranSeq;
        }

        public String getCashRemit() {
            return cashRemit;
        }

        public String getStatus() {
            return status;
        }

        public String getProdCode() {
            return prodCode;
        }

        public String getCancelPrice() {
            return cancelPrice;
        }

        public String getAccountNo() {
            return accountNo;
        }

        public LocalDate getReturnDate() {
            return returnDate;
        }

        public String getBuyAmt() {
            return buyAmt;
        }

        public String getCurrency() {
            return currency;
        }

        public String getTrfPrice() {
            return trfPrice;
        }

        public String getAmount() {
            return amount;
        }

        public String getProdName() {
            return prodName;
        }

        public String getAccountKey() {
            return accountKey;
        }

        public LocalDate getCancelDate() {
            return cancelDate;
        }

        public String getChannel() {
            return channel;
        }

        public String getTranSeq() {
            return tranSeq;
        }
    }
}
