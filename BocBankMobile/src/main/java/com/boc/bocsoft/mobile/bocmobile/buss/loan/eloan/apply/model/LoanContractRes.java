package com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.apply.model;

/**
 * 查询贷款合同模板，接口返回参数实体类
 * Created by xintong on 2016/6/13.
 */
public class LoanContractRes {
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
