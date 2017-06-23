package com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLoanContractQuery;

/**
 * 贷款合同接口返回参数
 * Created by xintong on 2016/6/13.
 */
public class PsnLoanContractQueryResult {
    //协议ID
    private String contractNo;
    //贷款合同内容
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
