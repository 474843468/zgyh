package com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnQueryCrcdBillIsExist;

/**
 * 信用卡当月是否已出账单查询
 * Created by wangf on 2016/11/22.
 */
public class PsnQueryCrcdBillIsExistResult {

    //是否已出账单 - 0：没有出账单  1：已出
    private String isBillExist;

    public String getIsBillExist() {
        return isBillExist;
    }

    public void setIsBillExist(String isBillExist) {
        this.isBillExist = isBillExist;
    }
}
