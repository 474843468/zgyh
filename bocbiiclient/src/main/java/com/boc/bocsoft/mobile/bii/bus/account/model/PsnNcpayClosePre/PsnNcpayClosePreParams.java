package com.boc.bocsoft.mobile.bii.bus.account.model.PsnNcpayClosePre;

import com.boc.bocsoft.mobile.bii.bus.account.model.PublicParams;

/**
 * @author wangyang
 *         2016/10/11 16:43
 *         限额服务关闭交易
 */
public class PsnNcpayClosePreParams extends PublicParams{

    /** 账户Id */
    private String accountId;
    /** 服务类型 1：无卡在线支付,2：代扣交易,3: 订购交易,5: 境外磁条交易,6：小额免密免签消费,7：免密或凭签名消费 */
    private String serviceType;

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
