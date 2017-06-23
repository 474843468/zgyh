package com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnMobileRemitQuery;

import org.threeten.bp.LocalDateTime;

import java.util.List;

/**
 * 手机取款 -- 汇出查询
 * Created by wangf on 2016/6/23.
 */
public class PsnMobileRemitQueryResult {


    /**
     * fromAcctNumber : 102450381920
     * list : [{"transactionId":"1555691489","payeeMobile":"18712344321","payeeName":"好的","tranDate":"2025/04/25 09:18:17","currencyCode":"001","cashRemit":null,"remitAmount":200,"remitNo":"023647757","remitStatus":"A","agentName":null,"agentNum":null,"branchId":"50978","fromActNumber":"102450381920","remark":"测试01","channel":"1"},{"transactionId":"1555691491","payeeMobile":"18713244561","payeeName":"王一二","tranDate":"2025/04/25 09:34:42","currencyCode":"001","cashRemit":null,"remitAmount":600,"remitNo":null,"remitStatus":"B","agentName":null,"agentNum":null,"branchId":"50978","fromActNumber":"102450381920","remark":"测试02","channel":"1"},{"transactionId":"1555691492","payeeMobile":"18713244561","payeeName":"王一二","tranDate":"2025/04/25 09:35:26","currencyCode":"001","cashRemit":null,"remitAmount":200,"remitNo":"023647768","remitStatus":"A","agentName":null,"agentNum":null,"branchId":"50978","fromActNumber":"102450381920","remark":"测试02","channel":"1"}]
     * nickName : 活期一本通
     * recordNumber : 3
     */

    //汇款账号
    private String fromAcctNumber;
    //别名
    private String nickName;
    //记录总数
    private int recordNumber;
    /**
     * transactionId : 1555691489
     * payeeMobile : 18712344321
     * payeeName : 好的
     * tranDate : 2025/04/25 09:18:17
     * currencyCode : 001
     * cashRemit : null
     * remitAmount : 200
     * remitNo : 023647757
     * remitStatus : A
     * agentName : null
     * agentNum : null
     * branchId : 50978
     * fromActNumber : 102450381920
     * remark : 测试01
     * channel : 1
     */

    private List<ListBean> list;

    public String getFromAcctNumber() {
        return fromAcctNumber;
    }

    public void setFromAcctNumber(String fromAcctNumber) {
        this.fromAcctNumber = fromAcctNumber;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

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
        //网银交易序号
        private String transactionId;
        //收款人手机号
        private String payeeMobile;
        //收款人姓名
        private String payeeName;
        //交易日期
        private LocalDateTime tranDate;
        //币种
        private String currencyCode;
        //钞汇
        private String cashRemit;
        //汇款金额
        private String remitAmount;
        //汇款编号
        private String remitNo;
        //汇款状态
        private String remitStatus;
        // 代理点名称
        private String agentName;
        //代理点编号
        private String agentNum;
        //
        private String branchId;
        //汇款账号
        private String fromActNumber;
        //附言
        private String remark;
        /**
         * 渠道标识
         * <p/>
         * 0-全渠道
         * 1-WEB渠道
         * 2-手机渠道
         * 4-家居银行渠道
         * 5-微信银行渠道
         * 6-银企对接渠道
         */
        private String channel;

        public String getTransactionId() {
            return transactionId;
        }

        public void setTransactionId(String transactionId) {
            this.transactionId = transactionId;
        }

        public String getPayeeMobile() {
            return payeeMobile;
        }

        public void setPayeeMobile(String payeeMobile) {
            this.payeeMobile = payeeMobile;
        }

        public String getPayeeName() {
            return payeeName;
        }

        public void setPayeeName(String payeeName) {
            this.payeeName = payeeName;
        }

        public LocalDateTime getTranDate() {
            return tranDate;
        }

        public void setTranDate(LocalDateTime tranDate) {
            this.tranDate = tranDate;
        }

        public String getCurrencyCode() {
            return currencyCode;
        }

        public void setCurrencyCode(String currencyCode) {
            this.currencyCode = currencyCode;
        }

        public String getCashRemit() {
            return cashRemit;
        }

        public void setCashRemit(String cashRemit) {
            this.cashRemit = cashRemit;
        }

        public String getRemitAmount() {
            return remitAmount;
        }

        public void setRemitAmount(String remitAmount) {
            this.remitAmount = remitAmount;
        }

        public String getRemitNo() {
            return remitNo;
        }

        public void setRemitNo(String remitNo) {
            this.remitNo = remitNo;
        }

        public String getRemitStatus() {
            return remitStatus;
        }

        public void setRemitStatus(String remitStatus) {
            this.remitStatus = remitStatus;
        }

        public String getAgentName() {
            return agentName;
        }

        public void setAgentName(String agentName) {
            this.agentName = agentName;
        }

        public String getAgentNum() {
            return agentNum;
        }

        public void setAgentNum(String agentNum) {
            this.agentNum = agentNum;
        }

        public String getBranchId() {
            return branchId;
        }

        public void setBranchId(String branchId) {
            this.branchId = branchId;
        }

        public String getFromActNumber() {
            return fromActNumber;
        }

        public void setFromActNumber(String fromActNumber) {
            this.fromActNumber = fromActNumber;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public String getChannel() {
            return channel;
        }

        public void setChannel(String channel) {
            this.channel = channel;
        }
    }
}
