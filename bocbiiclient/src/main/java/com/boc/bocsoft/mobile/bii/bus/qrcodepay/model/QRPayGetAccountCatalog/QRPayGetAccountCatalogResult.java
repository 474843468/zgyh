package com.boc.bocsoft.mobile.bii.bus.qrcodepay.model.QRPayGetAccountCatalog;

/**
 * Created by fanbin on 16/10/14.
 */
public class QRPayGetAccountCatalogResult {
    //账户类别  一类账户：1  二类账户：2    三类账户：3
    private String accountCatalog;

    public String getAccountCatalog() {
        return accountCatalog;
    }

    public void setAccountCatalog(String accountCatalog) {
        this.accountCatalog = accountCatalog;
    }
}
