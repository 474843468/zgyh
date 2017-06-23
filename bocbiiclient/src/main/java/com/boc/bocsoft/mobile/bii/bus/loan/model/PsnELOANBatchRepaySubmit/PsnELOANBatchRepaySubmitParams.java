package com.boc.bocsoft.mobile.bii.bus.loan.model.PsnELOANBatchRepaySubmit;

import java.util.List;

/**
 * 中银E贷-批量提前还款上送参数
 * Created by lzc4524 on 2016/8/31.
 */
public class PsnELOANBatchRepaySubmitParams {
    private String quoteNo; //额度编号
    private String accountId; //账户标识,还款账户的ID
    private String payAccount; //还款账户
    private List<ListBean> List; //批量列表
    private String token; //防重
    private String conversationId; //会话ID

    public String getQuoteNo() {
        return quoteNo;
    }

    public void setQuoteNo(String quoteNo) {
        this.quoteNo = quoteNo;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getPayAccount() {
        return payAccount;
    }

    public void setPayAccount(String payAccount) {
        this.payAccount = payAccount;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public List<ListBean> getListBeen() {
        return List;
    }

    public void setListBeen(List<ListBean> listBeen) {
        this.List = listBeen;
    }

    public static class ListBean{
        private String loanType; //贷款品种
        private String loanAccount; //贷款账号
        private String currency; //币种
        private String advanceRepayInterest; //提前还款利息
        private String advanceRepayCapital; //提前还款本金
        private String repayAmount; //提前还款金额

        public String getLoanType() {
            return loanType;
        }

        public void setLoanType(String loanType) {
            this.loanType = loanType;
        }

        public String getLoanAccount() {
            return loanAccount;
        }

        public void setLoanAccount(String loanAccount) {
            this.loanAccount = loanAccount;
        }

        public String getCurrency() {
            return currency;
        }

        public void setCurrency(String currency) {
            this.currency = currency;
        }

        public String getAdvanceRepayInterest() {
            return advanceRepayInterest;
        }

        public void setAdvanceRepayInterest(String advanceRepayInterest) {
            this.advanceRepayInterest = advanceRepayInterest;
        }

        public String getAdvanceRepayCapital() {
            return advanceRepayCapital;
        }

        public void setAdvanceRepayCapital(String advanceRepayCapital) {
            this.advanceRepayCapital = advanceRepayCapital;
        }

        public String getRepayAmount() {
            return repayAmount;
        }

        public void setRepayAmount(String repayAmount) {
            this.repayAmount = repayAmount;
        }
    }
}
