package com.boc.bocsoft.mobile.bii.bus.activitymanagementpaltform.model.psnActivityInfoQuery;

/**
 * 3.36 036 PsnActivityInfoQuery 活动管理平台取票
 * <p>
 * Created by yx on 2016/12/19.
 */
public class PsnActivityInfoQueryResult {
    private String ticketInfo;//	票
    private String customerId;//	网银客户号
    private String cifNumber;//核心客户号
    private String ownerBankId;//	开户网银机构号
    private String ownerIbkNum;//	开户省行联行号
    private String actyUrl;//	活动管理平台url

    public String getActyUrl() {
        return actyUrl;
    }

    public void setActyUrl(String actyUrl) {
        this.actyUrl = actyUrl;
    }

    public String getCifNumber() {
        return cifNumber;
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getOwnerBankId() {
        return ownerBankId;
    }

    public String getTicketInfo() {
        return ticketInfo;
    }

    public String getOwnerIbkNum() {
        return ownerIbkNum;
    }


    public void setCifNumber(String cifNumber) {
        this.cifNumber = cifNumber;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public void setOwnerBankId(String ownerBankId) {
        this.ownerBankId = ownerBankId;
    }

    public void setTicketInfo(String ticketInfo) {
        this.ticketInfo = ticketInfo;
    }

    public void setOwnerIbkNum(String ownerIbkNum) {
        this.ownerIbkNum = ownerIbkNum;
    }

}

