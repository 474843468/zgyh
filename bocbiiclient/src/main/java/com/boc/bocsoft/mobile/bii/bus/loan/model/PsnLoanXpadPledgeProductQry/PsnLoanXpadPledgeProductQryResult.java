package com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLoanXpadPledgeProductQry;

import org.threeten.bp.LocalDate;

/**
 * 查询可做理财质押贷款的理财产品列表
 * 是否需要申请Conversation	True
 * Created by XieDu on 2016/8/2.
 */
public class PsnLoanXpadPledgeProductQryResult {
    /**
     * 资金账号		加星显示
     */
    private String bancAccount;
    /**
     * 产品代码
     */
    private String prodCode;
    /**
     * 产品名称
     */
    private String prodName;
    /**
     * 产品币种
     */
    private String curCode;
    /**
     * 产品起息日
     */
    private LocalDate prodBegin;
    /**
     * 产品到期日
     * 无限开放式产品返回“-1”表示“无限期”
     */
    private LocalDate prodEnd;
    /**
     * 持有份额
     */
    private String holdingQuantity;
    /**
     * 可用份额
     */
    private String availableQuantity;
    /**
     * 可贷款金额
     */
    private String availableLoanAmt;
    /**
     * 产品风险级别
     * 0：低风险产品
     * 1：中低风险产品
     * 2：中等风险产品
     * 3：中高风险产品
     * 4：高风险产品
     */
    private String prodRisk;
    /**
     * 质押率
     */
    private String pledgeRate;

    public String getBancAccount() {
        return bancAccount;
    }

    public void setBancAccount(String bancAccount) {
        this.bancAccount = bancAccount;
    }

    public String getProdCode() {
        return prodCode;
    }

    public void setProdCode(String prodCode) {
        this.prodCode = prodCode;
    }

    public String getProdName() {
        return prodName;
    }

    public void setProdName(String prodName) {
        this.prodName = prodName;
    }

    public String getCurCode() {
        return curCode;
    }

    public void setCurCode(String curCode) {
        this.curCode = curCode;
    }

    public LocalDate getProdBegin() {
        return prodBegin;
    }

    public void setProdBegin(LocalDate prodBegin) {
        this.prodBegin = prodBegin;
    }

    public LocalDate getProdEnd() {
        return prodEnd;
    }

    public void setProdEnd(LocalDate prodEnd) {
        this.prodEnd = prodEnd;
    }

    public String getHoldingQuantity() {
        return holdingQuantity;
    }

    public void setHoldingQuantity(String holdingQuantity) {
        this.holdingQuantity = holdingQuantity;
    }

    public String getAvailableQuantity() {
        return availableQuantity;
    }

    public void setAvailableQuantity(String availableQuantity) {
        this.availableQuantity = availableQuantity;
    }

    public String getAvailableLoanAmt() {
        return availableLoanAmt;
    }

    public void setAvailableLoanAmt(String availableLoanAmt) {
        this.availableLoanAmt = availableLoanAmt;
    }

    public String getProdRisk() {
        return prodRisk;
    }

    public void setProdRisk(String prodRisk) {
        this.prodRisk = prodRisk;
    }

    public String getPledgeRate() {
        return pledgeRate;
    }

    public void setPledgeRate(String pledgeRate) {
        this.pledgeRate = pledgeRate;
    }
}
