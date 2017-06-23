package com.boc.bocsoft.mobile.bii.bus.loan.model.PsnOnLineLoanAppliedQry;

import java.math.BigDecimal;
import java.util.List;

/**
 * 查询贷款记录列表返回结果
 * Created by liuzc on 2016/8/16.
 */
public class PsnOnLineLoanAppliedQryResult {
    /**
     * List : [{"transType":"PLAB","interestForfeit":100000,"loanActNum":"40000102811811890"," ervic":"123456","repayAmount":5462138,"repayCapital":5000000,"repayDate":"2011/03/18","repayId":"654321","repayInterest":200510,"repayForfeit":200010,"capitalForfeit":null,"compoundInterest":null}]
     * recordNumber : 1
     */

    private int recordNumber; //记录总数
    private List<ListBean> list;//历史还款信息列表

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
        private String loanNumber; //贷款编号
        private String productCode; //产品编码
        private String productName; //产品名称
        private String name;	//姓名/企业名称
        private BigDecimal loanAmount;	//贷款金额
        private int loanTerm;	//贷款期限
        private String applyDate;	//申请日期
        private String currency;	//币种
        private String loanStatus;	//贷款状态
        private String refuseReason;	//拒绝原因

        public String getLoanNumber() {
            return loanNumber;
        }

        public void setLoanNumber(String loanNumber) {
            this.loanNumber = loanNumber;
        }

        public String getProductCode() {
            return productCode;
        }

        public void setProductCode(String productCode) {
            this.productCode = productCode;
        }

        public String getProductName() {
            return productName;
        }

        public void setProductName(String productName) {
            this.productName = productName;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public BigDecimal getLoanAmount() {
            return loanAmount;
        }

        public void setLoanAmount(BigDecimal loanAmount) {
            this.loanAmount = loanAmount;
        }

        public int getLoanTerm() {
            return loanTerm;
        }

        public void setLoanTerm(int loanTerm) {
            this.loanTerm = loanTerm;
        }

        public String getApplyDate() {
            return applyDate;
        }

        public void setApplyDate(String applyDate) {
            this.applyDate = applyDate;
        }

        public String getCurrency() {
            return currency;
        }

        public void setCurrency(String currency) {
            this.currency = currency;
        }

        public String getLoanStatus() {
            return loanStatus;
        }

        public void setLoanStatus(String loanStatus) {
            this.loanStatus = loanStatus;
        }

        public String getRefuseReason() {
            return refuseReason;
        }

        public void setRefuseReason(String refuseReason) {
            this.refuseReason = refuseReason;
        }
    }
}
