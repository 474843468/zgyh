package com.boc.bocsoft.mobile.bii.bus.account.model.PsnCrcdVirtualCardOpenOnline;

import com.boc.bocsoft.mobile.bii.bus.account.model.PublicParams;

/**
 * @author wangyang
 *         16/7/26 17:27
 *         虚拟银行卡开通网上支付
 */
public class PsnCrcdVirtualCardOpenOnlineParams extends PublicParams{

    /** 实体卡Id */
    private String virCardAccountId;
    /** 限额 */
    private String quota;
}
