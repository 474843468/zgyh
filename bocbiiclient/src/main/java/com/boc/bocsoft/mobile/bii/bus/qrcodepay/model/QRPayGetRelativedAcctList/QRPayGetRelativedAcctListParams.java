package com.boc.bocsoft.mobile.bii.bus.qrcodepay.model.QRPayGetRelativedAcctList;

/**
 * 查询关联账户中的银联账户列表
 * Created by wangf on 2016/11/2.
 */
public class QRPayGetRelativedAcctListParams {

    /**
     * 账户类型
     */
    private String accountType;
//    private List<String> accountType;

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

//    public List<String> getAccountType() {
//        return accountType;
//    }
//
//    public void setAccountType(List<String> accountType) {
//        this.accountType = accountType;
//    }
}
