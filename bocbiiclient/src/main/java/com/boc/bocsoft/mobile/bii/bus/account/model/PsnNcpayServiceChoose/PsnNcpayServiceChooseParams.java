package com.boc.bocsoft.mobile.bii.bus.account.model.PsnNcpayServiceChoose;

/**
 * @author wangyang
 *         2016/10/11 11:12
 *         获取开通状态及客户信息
 */
public class PsnNcpayServiceChooseParams {

    /** 账户Id */
    private String accountId;
    /** 1 无卡在线支付,2 代扣交易,3 订购交易,4 小额/凭签名免密,5 境外磁条交易*/
    private String serviceType;

    public PsnNcpayServiceChooseParams() {
    }

    public PsnNcpayServiceChooseParams(String accountId, String serviceType) {
        this.accountId = accountId;
        this.serviceType = serviceType;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }
}
