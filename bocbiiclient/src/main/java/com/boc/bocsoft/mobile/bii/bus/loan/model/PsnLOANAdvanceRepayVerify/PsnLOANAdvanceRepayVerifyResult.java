package com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLOANAdvanceRepayVerify;

import com.boc.bocsoft.mobile.bii.bus.login.model.PsnSvrRegisterDevicePre.FactorListBean;

import java.math.BigDecimal;
import java.util.List;

/**
 * PsnLOANAdvanceRepayVerify提前还款预交易，返回参数
 * Created by xintong on 2016/6/23.
 */
public class PsnLOANAdvanceRepayVerifyResult {

    /**
     * 安全因子数组
     * name值”Smc”-需要输入手机验证码值为”Otp”-需要输入动态口令
     */
    private List<FactorListBean> factorList;
    //手机验证码有效时间
    private String smcTrigerInterval;
    //CA加签数据XML报文
    private String _plainData;
    //CA的DN值
    private String  _certDN;
    //实体类loanRepayCount
    private LoanRepayCount loanRepayCount;


    //实体类loanRepayCount
    public class LoanRepayCount {
        //提前还款本金（必输）
        private BigDecimal advanceRepayCapital;
        //提前还款利息（必输）
        private BigDecimal advanceRepayInterest;
        //提前还款后剩余金额
        private BigDecimal repayAmountInAdvance;
        //提前还款后每期还款额
        private BigDecimal everyTermAmount;
        //手续费（必输）
        private BigDecimal charges;
        //提前还款后剩余期数
        private String remainIssueforAdvance;

        public BigDecimal getAdvanceRepayCapital() {
            return advanceRepayCapital;
        }

        public void setAdvanceRepayCapital(BigDecimal advanceRepayCapital) {
            this.advanceRepayCapital = advanceRepayCapital;
        }

        public BigDecimal getAdvanceRepayInterest() {
            return advanceRepayInterest;
        }

        public void setAdvanceRepayInterest(BigDecimal advanceRepayInterest) {
            this.advanceRepayInterest = advanceRepayInterest;
        }

        public BigDecimal getRepayAmountInAdvance() {
            return repayAmountInAdvance;
        }

        public void setRepayAmountInAdvance(BigDecimal repayAmountInAdvance) {
            this.repayAmountInAdvance = repayAmountInAdvance;
        }

        public BigDecimal getEveryTermAmount() {
            return everyTermAmount;
        }

        public void setEveryTermAmount(BigDecimal everyTermAmount) {
            this.everyTermAmount = everyTermAmount;
        }

        public BigDecimal getCharges() {
            return charges;
        }

        public void setCharges(BigDecimal charges) {
            this.charges = charges;
        }

        public String getRemainIssueforAdvance() {
            return remainIssueforAdvance;
        }

        public void setRemainIssueforAdvance(String remainIssueforAdvance) {
            this.remainIssueforAdvance = remainIssueforAdvance;
        }
    }


    public List<FactorListBean> getFactorList() {
        return factorList;
    }

    public void setFactorList(List<FactorListBean> factorList) {
        this.factorList = factorList;
    }

    public String getSmcTrigerInterval() {
        return smcTrigerInterval;
    }

    public void setSmcTrigerInterval(String smcTrigerInterval) {
        this.smcTrigerInterval = smcTrigerInterval;
    }

    public String get_plainData() {
        return _plainData;
    }

    public void set_plainData(String _plainData) {
        this._plainData = _plainData;
    }

    public String get_certDN() {
        return _certDN;
    }

    public void set_certDN(String _certDN) {
        this._certDN = _certDN;
    }

    public LoanRepayCount getLoanRepayCount() {
        return loanRepayCount;
    }

    public void setLoanRepayCount(LoanRepayCount loanRepayCount) {
        this.loanRepayCount = loanRepayCount;
    }
}
