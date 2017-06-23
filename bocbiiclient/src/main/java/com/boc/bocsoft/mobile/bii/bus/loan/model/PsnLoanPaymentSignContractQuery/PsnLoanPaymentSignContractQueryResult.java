package com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLoanPaymentSignContractQuery;

/**
 * 作者：XieDu
 * 创建时间：2016/10/12 8:42
 * 描述：
 */
public class PsnLoanPaymentSignContractQueryResult {
    /**
     * 协议Id
     */
    private String contractNo;
    /**
     * 贷款合同内容
     * html格式，协议内容包含待替换的要素项：（根据需求文档编写，可能会变化）
     * 【1、PLCF、WLCF】
     * 1、客户姓名 {custName}
     * 2、客户身份证号码 {custCerNo}
     * 3、关联借记卡号 {relDebitAccount}
     * 4、额度号 {limitNo}
     * 5、最小放款金额 {minLoanAmount}
     * 6、用款偏好 {preference}
     * 7、用款期限 {timeLimit}
     * 8、还款方式 {repayMode}
     * 【2、PLFB】
     * 1、客户姓名 {custName}
     * 2、客户身份证号码 {custCerNo}
     * 3、关联借记卡号 {relDebitAccount}
     * 4、贷款账号 {loanAccount}
     * 5、最小放款金额 {minLoanAmount}
     * 6、用款偏好 {preference}
     */
    private String loanContract;

    public String getContractNo() {
        return contractNo;
    }

    public void setContractNo(String contractNo) {
        this.contractNo = contractNo;
    }

    public String getLoanContract() {
        return loanContract;
    }

    public void setLoanContract(String loanContract) {
        this.loanContract = loanContract;
    }
}
