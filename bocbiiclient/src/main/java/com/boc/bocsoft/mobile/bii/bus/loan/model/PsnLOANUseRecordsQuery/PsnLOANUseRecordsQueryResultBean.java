package com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLOANUseRecordsQuery;

/**
 * Created by huixiabo on 2016/6/16.
 * 用款查询返回参数 List
 */
public class PsnLOANUseRecordsQueryResultBean {
    /**用款日期*/
    private String loanApplyDate;
    /**用款金额*/
    private String loanApplyAmount;
    /**用款流水号*/
    private String loanApplyId;
    /**总笔数*/
    private Integer totnumq;
    /**返回记录数*/
    private Integer retnum;
    /**交易渠道*/
    private String channel;
    /**贷款用途*/
    private String merchant;

    public String getMerchant() {
        return merchant;
    }

    public void setMerchant(String merchant) {
        this.merchant = merchant;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getLoanApplyDate() {
        return loanApplyDate;
    }

    public String getLoanApplyAmount() {
        return loanApplyAmount;
    }

    public String getLoanApplyId() {
        return loanApplyId;
    }

    public Integer getTotnumq() {
        return totnumq;
    }

    public Integer getRetnum() {
        return retnum;
    }

    public void setLoanApplyDate(String loanApplyDate) {
        this.loanApplyDate = loanApplyDate;
    }

    public void setLoanApplyAmount(String loanApplyAmount) {
        this.loanApplyAmount = loanApplyAmount;
    }

    public void setLoanApplyId(String loanApplyId) {
        this.loanApplyId = loanApplyId;
    }

    public void setTotnumq(Integer totnumq) {
        this.totnumq = totnumq;
    }

    public void setRetnum(Integer retnum) {
        this.retnum = retnum;
    }

    @Override
    public String toString() {
        return "PsnLOANUseRecordsQueryBean{" +
                "loanApplyDate='" + loanApplyDate + '\'' +
                ", loanApplyAmount='" + loanApplyAmount + '\'' +
                ", loanApplyId='" + loanApplyId + '\'' +
                ", totnumq=" + totnumq +
                ", retnum=" + retnum +
                ", channel='" + channel + '\'' +
                ", merchant='" + merchant + '\'' +
                '}';
    }
}
