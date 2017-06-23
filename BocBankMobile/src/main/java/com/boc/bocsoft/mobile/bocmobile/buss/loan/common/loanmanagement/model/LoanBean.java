package com.boc.bocsoft.mobile.bocmobile.buss.loan.common.loanmanagement.model;

import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLOANListEQuery.PsnLOANListEQueryResult;

/**
 * Created by huixiaobo on 2016/9/4.
 * 贷款管理页面返回数据
 */
public class LoanBean {
    //中银E贷
    private LoanQuoteViewModel loanEQuote;
    //贷款产品列表
    private LoanAccountListModel.PsnLOANListEQueryBean loanQuery;

    public LoanQuoteViewModel getLoanEQuote() {
        return loanEQuote;
    }

    public void setLoanEQuote(LoanQuoteViewModel loanEQuote) {
        this.loanEQuote = loanEQuote;
    }

    public LoanAccountListModel.PsnLOANListEQueryBean getLoanQuery() {
        return loanQuery;
    }

    public void setLoanQuery(LoanAccountListModel.PsnLOANListEQueryBean loanQuery) {
        this.loanQuery = loanQuery;
    }

    @Override
    public String toString() {
        return "LoanBean{" +
                "loanEQuote=" + loanEQuote +
                ", loanQuery=" + loanQuery +
                '}';
    }
}
