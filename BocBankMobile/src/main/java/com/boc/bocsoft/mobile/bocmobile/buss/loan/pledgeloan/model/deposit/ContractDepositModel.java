package com.boc.bocsoft.mobile.bocmobile.buss.loan.pledgeloan.model.deposit;

import java.util.List;

/**
 * 作者：XieDu
 * 创建时间：2016/9/13 16:25
 * 描述：质押贷款合同所需数据
 */
public class ContractDepositModel {

    private String floatingrate;
    private String floatingValue;
    private String repayment;
    private String cdcard;
    private String cdtype;
    private String loanperiod;
    private String reciveraccount;
    private String repaymentaccount;
    private String loanamount;
    private String borrower;
    private String reciver;

    private List<PrimaryaccountlistEntity> primaryaccountlist;

    public String getFloatingrate() { return floatingrate;}

    public void setFloatingrate(String floatingrate) { this.floatingrate = floatingrate;}

    public String getRepayment() { return repayment;}

    public void setRepayment(String repayment) { this.repayment = repayment;}

    public String getCdcard() { return cdcard;}

    public void setCdcard(String cdcard) { this.cdcard = cdcard;}

    public String getCdtype() { return cdtype;}

    public void setCdtype(String cdtype) { this.cdtype = cdtype;}

    public String getLoanperiod() { return loanperiod;}

    public void setLoanperiod(String loanperiod) { this.loanperiod = loanperiod;}

    public String getReciveraccount() { return reciveraccount;}

    public void setReciveraccount(String reciveraccount) { this.reciveraccount = reciveraccount;}

    public String getRepaymentaccount() { return repaymentaccount;}

    public void setRepaymentaccount(String repaymentaccount) {
        this.repaymentaccount = repaymentaccount;
    }

    public String getLoanamount() { return loanamount;}

    public void setLoanamount(String loanamount) { this.loanamount = loanamount;}

    public String getBorrower() { return borrower;}

    public void setBorrower(String borrower) { this.borrower = borrower;}

    public String getReciver() { return reciver;}

    public void setReciver(String reciver) { this.reciver = reciver;}

    public List<PrimaryaccountlistEntity> getPrimaryaccountlist() { return primaryaccountlist;}

    public void setPrimaryaccountlist(List<PrimaryaccountlistEntity> primaryaccountlist) {
        this.primaryaccountlist = primaryaccountlist;
    }

    public String getFloatingValue() {
        return floatingValue;
    }

    public void setFloatingValue(String floatingValue) {
        this.floatingValue = floatingValue;
    }

    public static class PrimaryaccountlistEntity {
        private String rrimaryaccount;
        private String amountticket;
        private String volumenumber;
        private String amount;
        private String accountmark;
        private String curency;
        private String accounttype;

        public String getRrimaryaccount() { return rrimaryaccount;}

        public void setRrimaryaccount(String rrimaryaccount) {
            this.rrimaryaccount = rrimaryaccount;
        }

        public String getAmountticket() { return amountticket;}

        public void setAmountticket(String amountticket) { this.amountticket = amountticket;}

        public String getVolumenumber() { return volumenumber;}

        public void setVolumenumber(String volumenumber) { this.volumenumber = volumenumber;}

        public String getAmount() { return amount;}

        public void setAmount(String amount) { this.amount = amount;}

        public String getAccountmark() { return accountmark;}

        public void setAccountmark(String accountmark) { this.accountmark = accountmark;}

        public String getCurency() { return curency;}

        public void setCurency(String curency) { this.curency = curency;}

        public String getAccounttype() { return accounttype;}

        public void setAccounttype(String accounttype) { this.accounttype = accounttype;}
    }
}
