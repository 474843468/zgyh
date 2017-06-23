package com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLOANHistoryQuery;

import java.util.List;

/**
 * 其他类型贷款-还款计划-还款历史返回参数
 * Created by liuzc on 2016/8/11.
 */
public class PsnLOANHistoryQueryResult {
    /**
     * List : [{"transType":"PLAB","interestForfeit":100000,"loanActNum":"40000102811811890"," ervic":"123456","repayAmount":5462138,"repayCapital":5000000,"repayDate":"2011/03/18","repayId":"654321","repayInterest":200510,"repayForfeit":200010,"capitalForfeit":null,"compoundInterest":null}]
     * recordNumber : 1
     */

    private int recordNumber; //记录总数
    private List<ListBean> List;//历史还款信息列表

    public int getRecordNumber() {
        return recordNumber;
    }

    public void setRecordNumber(int recordNumber) {
        this.recordNumber = recordNumber;
    }

    public List<ListBean> getList() {
        return List;
    }

    public void setList(List<ListBean> list) {
        this.List = list;
    }

    public static class ListBean {
        /**
         * transType : PLAB
         * interestForfeit : 100000
         * loanActNum : 40000102811811890
         *  ervic : 123456
         * repayAmount : 5462138
         * repayCapital : 5000000
         * repayDate : 2011/03/18
         * repayId : 654321
         * repayInterest : 200510
         * repayForfeit : 200010
         * capitalForfeit : null
         * compoundInterest : null
         */

        private String transType; //交易类型
        private String interestForfeit; //利息罚息
        private String loanActNum; //贷款账号
        private String repayAmount; //还款金额
        private String repayCapital; //还款本金
        private String repayDate; //还款日期
        private String repayId;  //还款流水号
        private String repayInterest; //还款利息
        private String repayForfeit; //总罚息

        public String getTransType() {
            return transType;
        }

        public void setTransType(String transType) {
            this.transType = transType;
        }

        public String getInterestForfeit() {
            return interestForfeit;
        }

        public void setInterestForfeit(String interestForfeit) {
            this.interestForfeit = interestForfeit;
        }

        public String getLoanActNum() {
            return loanActNum;
        }

        public void setLoanActNum(String loanActNum) {
            this.loanActNum = loanActNum;
        }

        public String getRepayAmount() {
            return repayAmount;
        }

        public void setRepayAmount(String repayAmount) {
            this.repayAmount = repayAmount;
        }

        public String getRepayCapital() {
            return repayCapital;
        }

        public void setRepayCapital(String repayCapital) {
            this.repayCapital = repayCapital;
        }

        public String getRepayDate() {
            return repayDate;
        }

        public void setRepayDate(String repayDate) {
            this.repayDate = repayDate;
        }

        public String getRepayId() {
            return repayId;
        }

        public void setRepayId(String repayId) {
            this.repayId = repayId;
        }

        public String getRepayInterest() {
            return repayInterest;
        }

        public void setRepayInterest(String repayInterest) {
            this.repayInterest = repayInterest;
        }

        public String getRepayForfeit() {
            return repayForfeit;
        }

        public void setRepayForfeit(String repayForfeit) {
            this.repayForfeit = repayForfeit;
        }
    }
}
