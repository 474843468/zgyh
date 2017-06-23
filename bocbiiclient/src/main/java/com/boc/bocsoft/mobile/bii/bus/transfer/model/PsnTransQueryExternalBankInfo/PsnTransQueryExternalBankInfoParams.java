package com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransQueryExternalBankInfo;

/**
 * Created by WM on 2016/6/16.
 */
public class PsnTransQueryExternalBankInfoParams {

    /**
     * toBankCode : 103
     * bankName : 银行
     * currentIndex  : 1
     * pageSize : 10
     */

    private String toBankCode;
    private String bankName;
    private Integer currentIndex;
    private Integer pageSize;

    public String getToBankCode() {
        return toBankCode;
    }

    public void setToBankCode(String toBankCode) {
        this.toBankCode = toBankCode;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public Integer getCurrentIndex() {
        return currentIndex;
    }

    public void setCurrentIndex(Integer currentIndex) {
        this.currentIndex = currentIndex;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }
}
