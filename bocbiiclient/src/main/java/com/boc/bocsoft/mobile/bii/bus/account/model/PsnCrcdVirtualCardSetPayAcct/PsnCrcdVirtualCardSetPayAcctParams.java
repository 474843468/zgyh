package com.boc.bocsoft.mobile.bii.bus.account.model.PsnCrcdVirtualCardSetPayAcct;

import com.boc.bocsoft.mobile.bii.bus.account.model.PublicParams;

/**
 * @author wangyang
 *         16/7/26 17:31
 *         虚拟银行卡设置支付账户
 */
public class PsnCrcdVirtualCardSetPayAcctParams extends PublicParams{

    /** 设置网上支付账户ID */
    private String virCardAccountId;

    public String getVirCardAccountId() {
        return virCardAccountId;
    }

    public void setVirCardAccountId(String virCardAccountId) {
        this.virCardAccountId = virCardAccountId;
    }
}
