package com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.shareconversion.model.PsnXpadQueryRiskMatch;

import java.io.Serializable;

/**
 * Created by zn on 2016/9/10.
 * 查询客户风险等级与产品风险等级是否匹配  PsnXpadQueryRiskMatch  请求Model
 */
public class PsnXpadQueryRiskMatchReqModel implements Serializable {

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

    /**
     * 客户与产品风险匹配状况	String	0：匹配
     * 1：不匹配但允许交易
     * （即客户风险低于产品风险，但系统不强制风险匹配，可提示客户确认风险和交易）
     * 2：不匹配且拒绝交易
     * （即客户风险低于产品风险，且系统强制风险匹配，拒绝交易）
     */
    private String riskMatch;

    public String getRiskMatch() {
        return riskMatch;
    }

    public void setRiskMatch(String riskMatch) {
        this.riskMatch = riskMatch;
    }

    public String getSerialCode() {
        return serialCode;
    }

    public void setSerialCode(String serialCode) {
        this.serialCode = serialCode;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getDigitalCode() {
        return digitalCode;
    }

    public void setDigitalCode(String digitalCode) {
        this.digitalCode = digitalCode;
    }

    public String getAccountKey() {
        return accountKey;
    }

    public void setAccountKey(String accountKey) {
        this.accountKey = accountKey;
    }
}
