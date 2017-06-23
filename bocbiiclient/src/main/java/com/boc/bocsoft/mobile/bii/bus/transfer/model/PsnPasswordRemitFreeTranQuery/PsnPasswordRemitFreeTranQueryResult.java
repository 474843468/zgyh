package com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnPasswordRemitFreeTranQuery;

import org.threeten.bp.LocalDate;

import java.util.List;

/**
 * 密码汇款查询响应（ATM无卡取款查询）
 *
 * Created by liuweidong on 2016/6/24.
 */
public class PsnPasswordRemitFreeTranQueryResult {

    /**
     * 总记录数
     */
    private int recordNumber;

    private List<ListBean> list;

    public int getRecordNumber() {
        return recordNumber;
    }

    public void setRecordNumber(int recordNumber) {
        this.recordNumber = recordNumber;
    }

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean {
        /**
         * 附言
         */
        private String comment;
        /**
         * 汇款状态 OU-已汇款未收款(已预约未取现)
                    CR-已汇款未收款(已预约未取现)
                     OK-已成功收款（已成功取现）
                    CL-已取消汇款（已取消取现）
                    L3-密码错3次锁定
                    L6-永久锁定
         */
        private String status;
        /**
         * 机构ID
         */
        private String branchId;
        /**
         * 付款日期
         */
        private LocalDate paymentDate;
        /**
         * 卡号
         */
        private String cardNo;
        /**
         * 转出账户ID
         */
        private String fromActId;
        /**
         * 付款人姓名
         */
        private String fromName;
        /**
         * 付款人账户
         */
        private String fromActNumber;
        /**
         * 付款账户类型
         */
        private String fromActType;
        /**
         * 付款金额
         */
        private String paymentAmount;
        /**
         * 付款钞汇
         */
        private String paymentCashRemit;
        /**
         * 手机验证码
         */
        private String mobileVerifyCode;
        /**
         * 汇款编号
         */
        private String remitNumber;
        /**
         * 收款人姓名
         */
        private String toName;
        /**
         * 收款人手机号
         */
        private String toMobile;
        /**
         * 收款人支取方式
         */
        private String drawMode;
        /**
         * 支取密码
         */
        private String drawPassword;
        /**
         * 收款人账号
         */
        private String toActNumber;
        /**
         * 收款人账号类型
         */
        private String toActType;
        /**
         * 汇入汇出标识
         */
        private String inOutward;
        /**
         * 解付状态
         */
        private String receiptState;
        /**
         * 解付日期 必输，9~13位数字
         */
        private String receiptDate;
        /**
         * 收款账号ID
         */
        private String toActId;
        /**
         * 转出账户机构ID
         */
        private String fromBranchId;
        /**
         * 付款货币
         */
        private String paymentCode;
        /**
         * 到期日期 仅当交易状态为“OU-已汇款未收款/CR-解付冲正/L3-密码错3次锁定”时，显示到期日期
         */
        private String dueDate;

        public String getComment() {
            return comment;
        }

        public void setComment(String comment) {
            this.comment = comment;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getBranchId() {
            return branchId;
        }

        public void setBranchId(String branchId) {
            this.branchId = branchId;
        }

        public LocalDate getPaymentDate() {
            return paymentDate;
        }

        public void setPaymentDate(LocalDate paymentDate) {
            this.paymentDate = paymentDate;
        }

        public String getCardNo() {
            return cardNo;
        }

        public void setCardNo(String cardNo) {
            this.cardNo = cardNo;
        }

        public String getFromActId() {
            return fromActId;
        }

        public void setFromActId(String fromActId) {
            this.fromActId = fromActId;
        }

        public String getFromName() {
            return fromName;
        }

        public void setFromName(String fromName) {
            this.fromName = fromName;
        }

        public String getFromActNumber() {
            return fromActNumber;
        }

        public void setFromActNumber(String fromActNumber) {
            this.fromActNumber = fromActNumber;
        }

        public String getFromActType() {
            return fromActType;
        }

        public void setFromActType(String fromActType) {
            this.fromActType = fromActType;
        }

        public String getPaymentAmount() {
            return paymentAmount;
        }

        public void setPaymentAmount(String paymentAmount) {
            this.paymentAmount = paymentAmount;
        }

        public String getPaymentCashRemit() {
            return paymentCashRemit;
        }

        public void setPaymentCashRemit(String paymentCashRemit) {
            this.paymentCashRemit = paymentCashRemit;
        }

        public String getMobileVerifyCode() {
            return mobileVerifyCode;
        }

        public void setMobileVerifyCode(String mobileVerifyCode) {
            this.mobileVerifyCode = mobileVerifyCode;
        }

        public String getRemitNumber() {
            return remitNumber;
        }

        public void setRemitNumber(String remitNumber) {
            this.remitNumber = remitNumber;
        }

        public String getToName() {
            return toName;
        }

        public void setToName(String toName) {
            this.toName = toName;
        }

        public String getToMobile() {
            return toMobile;
        }

        public void setToMobile(String toMobile) {
            this.toMobile = toMobile;
        }

        public String getDrawMode() {
            return drawMode;
        }

        public void setDrawMode(String drawMode) {
            this.drawMode = drawMode;
        }

        public String getDrawPassword() {
            return drawPassword;
        }

        public void setDrawPassword(String drawPassword) {
            this.drawPassword = drawPassword;
        }

        public String getToActNumber() {
            return toActNumber;
        }

        public void setToActNumber(String toActNumber) {
            this.toActNumber = toActNumber;
        }

        public String getToActType() {
            return toActType;
        }

        public void setToActType(String toActType) {
            this.toActType = toActType;
        }

        public String getInOutward() {
            return inOutward;
        }

        public void setInOutward(String inOutward) {
            this.inOutward = inOutward;
        }

        public String getReceiptState() {
            return receiptState;
        }

        public void setReceiptState(String receiptState) {
            this.receiptState = receiptState;
        }

        public String getReceiptDate() {
            return receiptDate;
        }

        public void setReceiptDate(String receiptDate) {
            this.receiptDate = receiptDate;
        }

        public String getToActId() {
            return toActId;
        }

        public void setToActId(String toActId) {
            this.toActId = toActId;
        }

        public String getFromBranchId() {
            return fromBranchId;
        }

        public void setFromBranchId(String fromBranchId) {
            this.fromBranchId = fromBranchId;
        }

        public String getPaymentCode() {
            return paymentCode;
        }

        public void setPaymentCode(String paymentCode) {
            this.paymentCode = paymentCode;
        }

        public String getDueDate() {
            return dueDate;
        }

        public void setDueDate(String dueDate) {
            this.dueDate = dueDate;
        }
    }
}
