package com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.financialposition.model.PsnXpadQuantityDetail;

import java.io.Serializable;

/**
 * 4.68 068份额明细查询  PsnXpadQuantityDetail  请求model
 * Created by zn on 2016/9/27.
 */
public class PsnXpadQuantityDetailReqModel implements Serializable {

    /**
     * 产品代码	String	M	产品代码
     */
    private String productCode;
    /**
     * 钞汇标识	String	M	01：钞
     * 02：汇
     * 00：人民币
     */
    private String charCode;

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getCharCode() {
        return charCode;
    }

    public void setCharCode(String charCode) {
        this.charCode = charCode;
    }
}
