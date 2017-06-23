package com.boc.bocsoft.mobile.bii.bus.activitymanagementpaltform.model.PsnQueryTransActivityStatus;

import java.util.ArrayList;
import java.util.List;

/**
 * 3.37 037 PsnQueryTransActivityStatus 查询交易可参与的活动并取票
 * <p>
 * Created by yx on 2016/12/19.
 */
public class PsnQueryTransActivityStatusResult {
    private String ticketInfo;//	票
    private String customerId;//	网银客户号	string
    private String cifNumber;//	核心客户号	string
    private String serviceId;//	服务码	string
    private String amount;//	金额	string	12.34
    private String firstSubmitDate;//	交易首次提交时间	string	Yyyy-MM-dd HH:mm:ss
    private String actType;//	付款账户类型	string
    private String ownerIbkNum;//	开户省行联行号
    private String actyUrl; //活动管理平台地址

    private String ownerBankId;//	开户网银机构号	string 废弃

    private List<ActyListBean> actyList = new ArrayList<ActyListBean>();//交易可参与的活动列表

    public String getTicketInfo() {
        return ticketInfo;
    }

    public List<ActyListBean> getActyList() {
        return actyList;
    }

    public String getFirstSubmitDate() {
        return firstSubmitDate;
    }

    public void setFirstSubmitDate(String firstSubmitDate) {
        this.firstSubmitDate = firstSubmitDate;
    }

    public String getOwnerBankId() {
        return ownerBankId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getCifNumber() {
        return cifNumber;
    }

    public String getActType() {
        return actType;
    }

    public String getActyUrl() {
        return actyUrl;
    }

    public void setActyUrl(String actyUrl) {
        this.actyUrl = actyUrl;
    }

    public String getAmount() {
        return amount;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setTicketInfo(String ticketInfo) {
        this.ticketInfo = ticketInfo;
    }

    public void setActyList(List<ActyListBean> actyList) {
        this.actyList = actyList;
    }

    public String getOwnerIbkNum() {
        return ownerIbkNum;
    }

    public void setOwnerIbkNum(String ownerIbkNum) {
        this.ownerIbkNum = ownerIbkNum;
    }

    public void setOwnerBankId(String ownerBankId) {
        this.ownerBankId = ownerBankId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public void setCifNumber(String cifNumber) {
        this.cifNumber = cifNumber;
    }

    public void setActType(String actType) {
        this.actType = actType;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    //交易可参与的活动列表
    public static class ActyListBean {
        private String actyId;//	活动Id	string	活动链接保存在前端，前端根据活动id对应活动链接
        private String actyName;//	活动名称	string
        private String actyPicUrl;//	活动图片url	string	若有则返回，否则为空列表结束

        public String getActyPicUrl() {
            return actyPicUrl;
        }

        public void setActyPicUrl(String actyPicUrl) {
            this.actyPicUrl = actyPicUrl;
        }

        public String getActyId() {
            return actyId;
        }

        public String getActyName() {
            return actyName;
        }

        //        ===========set=========

        public void setActyId(String actyId) {
            this.actyId = actyId;
        }

        public void setActyName(String actyName) {
            this.actyName = actyName;
        }


    }
}
