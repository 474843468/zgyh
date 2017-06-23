package com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnMobileWithdrawalQuery;

import org.threeten.bp.LocalDateTime;

import java.util.List;

/**
 * 手机取款 -- 取款查询
 * Created by wangf on 2016/6/23.
 */
public class PsnMobileWithdrawalQueryResult {


    /**
     * list : [{"channel":"1","currencyCode":"001","transactionId":"10000000281348","cashRemit":null,"payeeName":"zlp","payeeMobile":"13456321203","tranDate":"2013/07/12","remitStatus":"A","remitNo":"123456780","agentName":"alias1","remitAmount":20000,"agentNum":"7"}]
     * recordNumber : 1
     * nickName : 长城借记卡
     * agentAcctNumber : 4563515000000185687
     */

    /**
     * 记录总数
     */
    private int recordNumber;
    /**
     * 别名
     */
    private String nickName;
    /**
     * 代理点收款账号
     */
    private String agentAcctNumber;

    /**
     * channel : 1
     * currencyCode : 001
     * transactionId : 10000000281348
     * cashRemit : null
     * payeeName : zlp
     * payeeMobile : 13456321203
     * tranDate : 2013/07/12
     * remitStatus : A
     * remitNo : 123456780
     * agentName : alias1
     * remitAmount : 20000
     * agentNum : 7
     */

    private List<ListBean> list;

    public int getRecordNumber() {
        return recordNumber;
    }

    public void setRecordNumber(int recordNumber) {
        this.recordNumber = recordNumber;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getAgentAcctNumber() {
        return agentAcctNumber;
    }

    public void setAgentAcctNumber(String agentAcctNumber) {
        this.agentAcctNumber = agentAcctNumber;
    }

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean {
        /**
         * 渠道标识
         */
        private String channel;
        /**
         * 取款币种
         */
        private String currencyCode;
        /**
         * 网银交易序号
         */
        private String transactionId;
        /**
         * 钞汇
         */
        private String cashRemit;
        /**
         * 收款人姓名
         */
        private String payeeName;
        /**
         * 收款人手机号
         */
        private String payeeMobile;
        /**
         * 取款日期
         */
        private LocalDateTime tranDate;
        /**
         * 汇款状态
         */
        private String remitStatus;
        /**
         * 汇款编号
         */
        private String remitNo;
        /**
         * 代理点名称
         */
        private String agentName;
        /**
         * 取款金额
         */
        private String remitAmount;
        /**
         * 代理点编号
         */
        private String agentNum;

        //机构号
        private String branchId;
        //附言
        private String remark;
        //汇出账号
        private String fromActNumber;

        public String getBranchId() {
            return branchId;
        }

        public void setBranchId(String branchId) {
            this.branchId = branchId;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public String getFromActNumber() {
            return fromActNumber;
        }

        public void setFromActNumber(String fromActNumber) {
            this.fromActNumber = fromActNumber;
        }

        public String getChannel() {
            return channel;
        }

        public void setChannel(String channel) {
            this.channel = channel;
        }

        public String getCurrencyCode() {
            return currencyCode;
        }

        public void setCurrencyCode(String currencyCode) {
            this.currencyCode = currencyCode;
        }

        public String getTransactionId() {
            return transactionId;
        }

        public void setTransactionId(String transactionId) {
            this.transactionId = transactionId;
        }

        public String getCashRemit() {
            return cashRemit;
        }

        public void setCashRemit(String cashRemit) {
            this.cashRemit = cashRemit;
        }

        public String getPayeeName() {
            return payeeName;
        }

        public void setPayeeName(String payeeName) {
            this.payeeName = payeeName;
        }

        public String getPayeeMobile() {
            return payeeMobile;
        }

        public void setPayeeMobile(String payeeMobile) {
            this.payeeMobile = payeeMobile;
        }

        public LocalDateTime getTranDate() {
            return tranDate;
        }

        public void setTranDate(LocalDateTime tranDate) {
            this.tranDate = tranDate;
        }

        public String getRemitStatus() {
            return remitStatus;
        }

        public void setRemitStatus(String remitStatus) {
            this.remitStatus = remitStatus;
        }

        public String getRemitNo() {
            return remitNo;
        }

        public void setRemitNo(String remitNo) {
            this.remitNo = remitNo;
        }

        public String getAgentName() {
            return agentName;
        }

        public void setAgentName(String agentName) {
            this.agentName = agentName;
        }

        public String getRemitAmount() {
            return remitAmount;
        }

        public void setRemitAmount(String remitAmount) {
            this.remitAmount = remitAmount;
        }

        public String getAgentNum() {
            return agentNum;
        }

        public void setAgentNum(String agentNum) {
            this.agentNum = agentNum;
        }
    }
}
