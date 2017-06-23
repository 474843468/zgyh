package com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadQueryRiskMatch;

/**
 * 查询客户风险等级与产品风险等级是否匹配
 * Created by guokai on 2016/9/6.
 */
public class PsnXpadQueryRiskMatchParams {

    public PsnXpadQueryRiskMatchParams() {
    }

    public PsnXpadQueryRiskMatchParams(String productCode) {
        this.productCode = productCode;
    }

    /**
     * 周期性产品系列编号{选输周期性产品输入此项，产品代码与产品数字代码可不输}
     */
    private String serialCode;
    /**
     * 产品代码{选输
     * 周期性产品系列编号与产品数字代码均未输入，则此项必输
     * }
     */
    private String productCode;
    /**
     * 产品数字代码{全输0表示此项未输入
     * 电话银行渠道可输入此项，产品代码与周期性产品系列编号可不输
     * }
     */
    private String digitalCode;
    /**
     * 账号缓存标识{必输	36位长度}
     */
    private String accountKey;

    public PsnXpadQueryRiskMatchParams(String accountKey, String prodCode) {
        this. accountKey = accountKey;
        this. productCode = prodCode;
    }

    public String getAccountKey() {
        return accountKey;
    }

    public void setAccountKey(String accountKey) {
        this.accountKey = accountKey;
    }

    public String getDigitalCode() {
        return digitalCode;
    }

    public void setDigitalCode(String digitalCode) {
        this.digitalCode = digitalCode;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getSerialCode() {
        return serialCode;
    }

    public void setSerialCode(String serialCode) {
        this.serialCode = serialCode;
    }
}
