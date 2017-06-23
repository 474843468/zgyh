package com.boc.bocsoft.mobile.bii.bus.global.model.PsnCommonQueryAllChinaBankAccount;

import java.util.List;

/**
 * Created by XieDu on 2016/6/20.
 */
public class PsnCommonQueryAllChinaBankAccountParams {

    public List<String> getAccountType() {
        return accountType;
    }

    public void setAccountType(List<String> accountType) {
        this.accountType = accountType;
    }

    /**
     * 账户类型
     */
    private List<String> accountType;
}
