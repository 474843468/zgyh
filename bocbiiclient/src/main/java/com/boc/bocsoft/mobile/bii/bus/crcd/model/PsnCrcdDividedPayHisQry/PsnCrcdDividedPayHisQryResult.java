package com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdDividedPayHisQry;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by yangle on 2016/11/22.
 */
public class PsnCrcdDividedPayHisQryResult {


    /**
     * list : [{"accomplishDate":"2014/10/12","amount":7000,"chargeMode":"0","creditCardNum":"1111","currency":"001","firstInAmount":2000,"incomeAmount":5800,"incomeTimeCount":3,"instalmentPlan":"1234567890111111","instmtCount":3,"instmtDate":"2011/01/25","instmtDescription":"分期付款计划描述1","instmtFlag":"ABCD","nextIncomeDate":"2013/12/12","nextTimeAmount":2000,"restAmount":1000,"restTimeCount":4},{"accomplishDate":"2014/10/12","amount":7000,"chargeMode":"0","creditCardNum":"1112","currency":"001","firstInAmount":2000,"incomeAmount":5800,"incomeTimeCount":3,"instalmentPlan":"1234567890111111","instmtCount":3,"instmtDate":"2011/01/25","instmtDescription":"分期付款计划描述1","instmtFlag":"ABCD","nextIncomeDate":"2013/12/12","nextTimeAmount":2000,"restAmount":1000,"restTimeCount":4}]
     * recordNumber : 15
     */

    private int recordNumber;
    /**
     * accomplishDate : 2014/10/12
     * amount : 7000
     * chargeMode : 0
     * creditCardNum : 1111
     * currency : 001
     * firstInAmount : 2000
     * incomeAmount : 5800
     * incomeTimeCount : 3
     * instalmentPlan : 1234567890111111
     * instmtCount : 3
     * instmtDate : 2011/01/25
     * instmtDescription : 分期付款计划描述1
     * instmtFlag : ABCD
     * nextIncomeDate : 2013/12/12
     * nextTimeAmount : 2000
     * restAmount : 1000
     * restTimeCount : 4
     */

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
         * 分期日期
         */
        private String instmtDate;
        /**
         * 分期交易描述
         */
        private String instmtDescription;
        /**
         * 分期币种
         */
        private String currency;
        /**
         * 账单分期实际金额
         */
        private BigDecimal amount;
        /**
         *分期期数
         */
        private Integer instmtCount;
        /**
         * 分期完成日期
         */
        private String accomplishDate;
        /**
         * 分期手续费
         */
        private BigDecimal instmtCharge;
        /**
         * 分期手续费收取方式
         * 1---手续费分期支付
         * 0---手续费一次性支付
         */
        private String chargeMode;
        /**
         * 分期后每期应还金额-首期
         */
        private BigDecimal firstInAmount;
        /**
         * 分期后每期应还金额-后每期
         */
        private BigDecimal restPerTimeInAmount;
        /**
         * 已入账期数
         */
        private Integer incomeTimeCount;
        /**
         * 已入账金额
         */
        private BigDecimal incomeAmount;
        /**
         * 本期账单剩余还款金额
         */
        private BigDecimal restAmount;
        /**
         * 下次入账日期
         */
        private String nextIncomeDate;
        /**
         * 分期计划
         */
        private String instalmentPlan;
        /**
         * 信用卡号
         */
        private String creditCardNum;
        /**
         * 分期付款标识
         * EP01--消费分期
         * BI01--账单分期
         * XJ01--现金分期
         */
        private String instmtFlag;
        /**
         * 下期入账金额
         */
        private BigDecimal nextTimeAmount;
        /**
         * 剩余未入账期数
         */
        private Integer restTimeCount;

        public String getInstmtDate() {
            return instmtDate;
        }

        public void setInstmtDate(String instmtDate) {
            this.instmtDate = instmtDate;
        }

        public String getInstmtDescription() {
            return instmtDescription;
        }

        public void setInstmtDescription(String instmtDescription) {
            this.instmtDescription = instmtDescription;
        }

        public String getCurrency() {
            return currency;
        }

        public void setCurrency(String currency) {
            this.currency = currency;
        }

        public BigDecimal getAmount() {
            return amount;
        }

        public void setAmount(BigDecimal amount) {
            this.amount = amount;
        }

        public Integer getInstmtCount() {
            return instmtCount;
        }

        public void setInstmtCount(Integer instmtCount) {
            this.instmtCount = instmtCount;
        }

        public String getAccomplishDate() {
            return accomplishDate;
        }

        public void setAccomplishDate(String accomplishDate) {
            this.accomplishDate = accomplishDate;
        }

        public BigDecimal getInstmtCharge() {
            return instmtCharge;
        }

        public void setInstmtCharge(BigDecimal instmtCharge) {
            this.instmtCharge = instmtCharge;
        }

        public String getChargeMode() {
            return chargeMode;
        }

        public void setChargeMode(String chargeMode) {
            this.chargeMode = chargeMode;
        }

        public BigDecimal getFirstInAmount() {
            return firstInAmount;
        }

        public void setFirstInAmount(BigDecimal firstInAmount) {
            this.firstInAmount = firstInAmount;
        }

        public BigDecimal getRestPerTimeInAmount() {
            return restPerTimeInAmount;
        }

        public void setRestPerTimeInAmount(BigDecimal restPerTimeInAmount) {
            this.restPerTimeInAmount = restPerTimeInAmount;
        }

        public Integer getIncomeTimeCount() {
            return incomeTimeCount;
        }

        public void setIncomeTimeCount(Integer incomeTimeCount) {
            this.incomeTimeCount = incomeTimeCount;
        }

        public BigDecimal getIncomeAmount() {
            return incomeAmount;
        }

        public void setIncomeAmount(BigDecimal incomeAmount) {
            this.incomeAmount = incomeAmount;
        }

        public BigDecimal getRestAmount() {
            return restAmount;
        }

        public void setRestAmount(BigDecimal restAmount) {
            this.restAmount = restAmount;
        }

        public String getNextIncomeDate() {
            return nextIncomeDate;
        }

        public void setNextIncomeDate(String nextIncomeDate) {
            this.nextIncomeDate = nextIncomeDate;
        }

        public String getInstalmentPlan() {
            return instalmentPlan;
        }

        public void setInstalmentPlan(String instalmentPlan) {
            this.instalmentPlan = instalmentPlan;
        }

        public String getCreditCardNum() {
            return creditCardNum;
        }

        public void setCreditCardNum(String creditCardNum) {
            this.creditCardNum = creditCardNum;
        }

        public String getInstmtFlag() {
            return instmtFlag;
        }

        public void setInstmtFlag(String instmtFlag) {
            this.instmtFlag = instmtFlag;
        }

        public BigDecimal getNextTimeAmount() {
            return nextTimeAmount;
        }

        public void setNextTimeAmount(BigDecimal nextTimeAmount) {
            this.nextTimeAmount = nextTimeAmount;
        }

        public Integer getRestTimeCount() {
            return restTimeCount;
        }

        public void setRestTimeCount(Integer restTimeCount) {
            this.restTimeCount = restTimeCount;
        }
    }
}
