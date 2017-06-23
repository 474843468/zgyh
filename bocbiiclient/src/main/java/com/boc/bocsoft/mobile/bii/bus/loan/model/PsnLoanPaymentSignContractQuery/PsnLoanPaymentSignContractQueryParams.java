package com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLoanPaymentSignContractQuery;

/**
 * 作者：XieDu
 * 创建时间：2016/10/12 8:39
 * 描述：
 */
public class PsnLoanPaymentSignContractQueryParams {
    /**
     * 协议Id
     * 如果id为空则返回最新的协议
     */
    private String contractNo;
    /**
     * 语言
     * CHN-中文ENG-英文
     */
    private String eLanguage;
    /**
     * 业务类型
     * PLFB—随借随还、
     * PLCF—网络消费贷款（线下审批、线上用款）、
     * WLCF—网络消费贷款（线上审批、线上用款）
     */
    private String dealType;

    public String getContractNo() {
        return contractNo;
    }

    public void setContractNo(String contractNo) {
        this.contractNo = contractNo;
    }

    public String geteLanguage() {
        return eLanguage;
    }

    public void seteLanguage(String eLanguage) {
        this.eLanguage = eLanguage;
    }

    public String getDealType() {
        return dealType;
    }

    public void setDealType(String dealType) {
        this.dealType = dealType;
    }
}
