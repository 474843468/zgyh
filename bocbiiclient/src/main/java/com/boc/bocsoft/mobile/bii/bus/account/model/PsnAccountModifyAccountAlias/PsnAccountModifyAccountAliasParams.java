package com.boc.bocsoft.mobile.bii.bus.account.model.PsnAccountModifyAccountAlias;

import com.boc.bocsoft.mobile.bii.bus.account.model.PublicParams;

/**
 * Created by niuguobin on 2016/7/22.
 */
public class PsnAccountModifyAccountAliasParams extends PublicParams{
    private String accountId;
    private String accountNickName;


    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getAccountNickName() {
        return accountNickName;
    }

    public void setAccountNickName(String accountNickName) {
        this.accountNickName = accountNickName;
    }
}
