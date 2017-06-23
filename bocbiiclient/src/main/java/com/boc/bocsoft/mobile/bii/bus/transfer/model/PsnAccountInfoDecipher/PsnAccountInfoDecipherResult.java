package com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnAccountInfoDecipher;

/**
 * Created by XieDu on 2016/6/21.
 */
public class PsnAccountInfoDecipherResult {

    /**
     * custName
     * 客户名称
     */
    private String custName;
    /**
     * 客户账号
     */
    private String custActNum;

    public String getCustName() {
        return custName;
    }

    public void setCustName(String custName) {
        this.custName = custName;
    }

    public String getCustActNum() {
        return custActNum;
    }

    public void setCustActNum(String custActNum) {
        this.custActNum = custActNum;
    }
}
