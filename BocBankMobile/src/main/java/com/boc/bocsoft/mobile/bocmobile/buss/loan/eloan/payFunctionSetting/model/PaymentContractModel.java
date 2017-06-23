package com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.payFunctionSetting.model;

/**
 * 作者：XieDu
 * 创建时间：2016/10/14 19:40
 * 描述：
 */
public class PaymentContractModel {

    /**
     * 合同内容
     */
    private String contract;
    /**
     * 借款人
     */
    private String custName;
    /**
     * 身份证件号码
     */
    private String custCerNo;
    /**
     * 额度号
     */
    private String limitNo;

    /**
     * 贷款账号
     */
    private String loanAccount;
    /**
     * 卡号
     */
    private String relDebitAccount;
    /**
     * 用款偏好
     */
    private String preference;
    /**
     * 贷款期限
     */
    private String timeLimit;
    /**
     * 还款方式
     */
    private String repayMode;
    /**
     * 最小放款金额
     */
    private String minLoanAmount;
    /**
     * 协议ID
     */
    private String contractNo;
    /**
     * 业务类型
     */
    private String dealType;

    public String getCustName() {
        return custName;
    }

    public void setCustName(String custName) {
        this.custName = custName;
    }

    public String getCustCerNo() {
        return custCerNo;
    }

    public void setCustCerNo(String custCerNo) {
        this.custCerNo = custCerNo;
    }

    public String getLimitNo() {
        return limitNo;
    }

    public void setLimitNo(String limitNo) {
        this.limitNo = limitNo;
    }

    public String getRelDebitAccount() {
        return relDebitAccount;
    }

    public void setRelDebitAccount(String relDebitAccount) {
        this.relDebitAccount = relDebitAccount;
    }

    public String getPreference() {
        return preference;
    }

    public void setPreference(String preference) {
        this.preference = preference;
    }

    public String getTimeLimit() {
        return timeLimit;
    }

    public void setTimeLimit(String timeLimit) {
        this.timeLimit = timeLimit;
    }

    public String getRepayMode() {
        return repayMode;
    }

    public void setRepayMode(String repayMode) {
        this.repayMode = repayMode;
    }

    public String getMinLoanAmount() {
        return minLoanAmount;
    }

    public void setMinLoanAmount(String minLoanAmount) {
        this.minLoanAmount = minLoanAmount;
    }

    public String getContractNo() {
        return contractNo;
    }

    public void setContractNo(String contractNo) {
        this.contractNo = contractNo;
    }

    public String getDealType() {
        return dealType;
    }

    public void setDealType(String dealType) {
        this.dealType = dealType;
    }

    public String getLoanAccount() {
        return loanAccount;
    }

    public void setLoanAccount(String loanAccount) {
        this.loanAccount = loanAccount;
    }

    public String getContract() {
        return contract;
    }

    public void setContract(String contract) {
        this.contract = contract;
    }
}
