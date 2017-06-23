package com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadHisTradStatus;

import org.threeten.bp.LocalDate;

import java.util.List;

/**
 * 历史常规交易状况查询
 * Created by zhx on 2016/9/5
 */
public class PsnXpadHisTradStatusResult {

    /**
     * recordNumber : 92
     * list : [{"paymentDate":"2016/10/10","cashRemit":"2","currencyCode":"014","canBeCanceled":"0","status":"1","prodCode":"JD87458745","tranAtrr":"0","standardPro":"1","isReciveMoney":"1","productKind":"1","delegateDate":"2016/10/05","trfPrice":5.6,"trfAmount":100,"amount":560,"prodName":"中银理财001","tranSeq":"23423421","trfType":"01","channel":"02"},{"paymentDate":"2016/10/10","cashRemit":"2","currencyCode":"014","canBeCanceled":"0","status":"1","prodCode":"JD87458745","tranAtrr":"0","standardPro":"1","isReciveMoney":"1","productKind":"0","delegateDate":"2016/10/05","trfPrice":5.6,"trfAmount":100,"amount":560,"prodName":"中银理财001","tranSeq":"23423422","trfType":"09","channel":"02"},{"paymentDate":"2016/10/10","cashRemit":"2","currencyCode":"014","canBeCanceled":"0","status":"2","prodCode":"JD87458745","tranAtrr":"0","standardPro":"1","isReciveMoney":"1","productKind":"0","delegateDate":"2016/10/05","trfPrice":5.6,"trfAmount":100,"amount":560,"prodName":"中银理财001","tranSeq":"23423423","trfType":"01","channel":"02"},{"paymentDate":"2016/10/10","cashRemit":"2","currencyCode":"014","canBeCanceled":"0","status":"1","prodCode":"JD87458745","tranAtrr":"0","standardPro":"1","isReciveMoney":"1","productKind":"0","delegateDate":"2016/10/05","trfPrice":5.6,"trfAmount":100,"amount":560,"prodName":"中银理财001","tranSeq":"23423424","trfType":"01","channel":"02"},{"paymentDate":"2016/10/10","cashRemit":"2","currencyCode":"014","canBeCanceled":"0","status":"2","prodCode":"JD87458745","tranAtrr":"0","standardPro":"1","isReciveMoney":"1","productKind":"0","delegateDate":"2016/10/05","trfPrice":5.6,"trfAmount":100,"amount":560,"prodName":"中银理财021","tranSeq":"23423425","trfType":"01","channel":"02"}]
     */
    private int recordNumber;
    private List<XpadHisTradStatusResultEntity> list;

    public void setRecordNumber(int recordNumber) {
        this.recordNumber = recordNumber;
    }

    public void setList(List<XpadHisTradStatusResultEntity> list) {
        this.list = list;
    }

    public int getRecordNumber() {
        return recordNumber;
    }

    public List<XpadHisTradStatusResultEntity> getList() {
        return list;
    }

    public static class XpadHisTradStatusResultEntity {
        /**
         * paymentDate : 2016/10/10
         * cashRemit : 2
         * currencyCode : 014
         * canBeCanceled : 0
         * status : 1
         * prodCode : JD87458745
         * tranAtrr : 0
         * standardPro : 1
         * isReciveMoney : 1
         * productKind : 1
         * delegateDate : 2016/10/05
         * trfPrice : 5.6
         * trfAmount : 100
         * amount : 560
         * prodName : 中银理财001
         * tranSeq : 23423421
         * trfType : 01
         * channel : 02
         */

        // 交易日期
        private LocalDate paymentDate;
        // 产品代码
        private String prodCode;
        // 产品名称
        private String prodName;
        // 交易类型（00：认购 01：申购 02：赎回 03：红利再投 04：红利发放 05：（经过）利息返还 06：本金返还 07：起息前赎回 08：利息折份额 09:赎回亏损 10:赎回盈利 11:产品转让 12:份额转换）
        private String trfType;
        // 交易币种（001：人民币元 014：美元 012：英镑 013：港币 028: 加拿大元 029：澳元 038：欧元 027：日元）
        private String currencyCode;
        // 钞汇标识（01：钞 02：汇 00：人民币钞汇）
        private String cashRemit;
        // 交易价格
        private String trfPrice;
        // 交易份额
        private String trfAmount;
        // 交易金额
        private String amount;
        // 渠道（00：理财系统交易 01：（核心系统 OFP）柜面 02：网银 03：电话银行自助 04：电话银行人工 05：手机银行 06：家居银行 07：微信银行 08：自助终端 09：OCRM 10：中银易商平台 11：开放平台移动客户端）
        private String channel;
        // 状态（0：委托待处理 1：成功 2：失败 3：已撤销 4：已冲正 5：已赎回）
        private String status;
        // 00-常规交易 01-自动续约交易 02-预约交易 03-组合购买 04-自动投资交易 05-委托交易 06-短信委托 07-产品转入 08-产品转出 09-组合购买 10-委托交易 11-产品转让 12-周期投资 13-本金摊还
        private String tranAtrr;
        // 是否可撤单（0：是 1：否）
        private String canBeCanceled;
        // 是否到账（0：否 1：是）
        private String isReciveMoney;
        // 交易流水号
        private String tranSeq;
        // 产品种类(0：结构理财产品 1：类基金理财产品)
        private String productKind;
        // 是否为业绩基准产品（0：否 1：是）
        private String standardPro;
        private LocalDate delegateDate;

        public void setPaymentDate(LocalDate paymentDate) {
            this.paymentDate = paymentDate;
        }

        public void setCashRemit(String cashRemit) {
            this.cashRemit = cashRemit;
        }

        public void setCurrencyCode(String currencyCode) {
            this.currencyCode = currencyCode;
        }

        public void setCanBeCanceled(String canBeCanceled) {
            this.canBeCanceled = canBeCanceled;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public void setProdCode(String prodCode) {
            this.prodCode = prodCode;
        }

        public void setTranAtrr(String tranAtrr) {
            this.tranAtrr = tranAtrr;
        }

        public void setStandardPro(String standardPro) {
            this.standardPro = standardPro;
        }

        public void setIsReciveMoney(String isReciveMoney) {
            this.isReciveMoney = isReciveMoney;
        }

        public void setProductKind(String productKind) {
            this.productKind = productKind;
        }

        public void setDelegateDate(LocalDate delegateDate) {
            this.delegateDate = delegateDate;
        }

        public void setTrfPrice(String trfPrice) {
            this.trfPrice = trfPrice;
        }

        public void setTrfAmount(String trfAmount) {
            this.trfAmount = trfAmount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public void setProdName(String prodName) {
            this.prodName = prodName;
        }

        public void setTranSeq(String tranSeq) {
            this.tranSeq = tranSeq;
        }

        public void setTrfType(String trfType) {
            this.trfType = trfType;
        }

        public void setChannel(String channel) {
            this.channel = channel;
        }

        public LocalDate getPaymentDate() {
            return paymentDate;
        }

        public String getCashRemit() {
            return cashRemit;
        }

        public String getCurrencyCode() {
            return currencyCode;
        }

        public String getCanBeCanceled() {
            return canBeCanceled;
        }

        public String getStatus() {
            return status;
        }

        public String getProdCode() {
            return prodCode;
        }

        public String getTranAtrr() {
            return tranAtrr;
        }

        public String getStandardPro() {
            return standardPro;
        }

        public String getIsReciveMoney() {
            return isReciveMoney;
        }

        public String getProductKind() {
            return productKind;
        }

        public LocalDate getDelegateDate() {
            return delegateDate;
        }

        public String getTrfPrice() {
            return trfPrice;
        }

        public String getTrfAmount() {
            return trfAmount;
        }

        public String getAmount() {
            return amount;
        }

        public String getProdName() {
            return prodName;
        }

        public String getTranSeq() {
            return tranSeq;
        }

        public String getTrfType() {
            return trfType;
        }

        public String getChannel() {
            return channel;
        }
    }
}
