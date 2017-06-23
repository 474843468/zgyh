package com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadReferProfitQuery;

/**
 * PsnXpadReferProfitQuery  参考收益汇总查询  请求Model
 * Created by zn on 2016/9/20.
 */
public class PsnXpadReferProfitQueryParams {

    /**
     "accountKey": "97483dc7-885f-4f45-a2ad-e60f38e87573",
     "productCode": "AMRJYL01",
     "kind": "0",
     "tranSeq": "",
     "charCode": "00"
     */

    /**
     * 账号缓存标识	String	M
     */
    private String accountKey;
    /**
     * 产品代码	String	M
     */
    private String productCode;
    /**
     * 产品性质	String	M 1-类基金理财产品 0-结构性理财产品
     */
    private String kind;
    /**
     * 份额流水号	String	O	业绩基准产品必输（PsnXpadProductBalanceQuery返回项standPro不为0的产品），取自PsnXpadQuantityDetail接口返回项tranSeq
     */
    private String tranSeq;
    /**
     * 钞汇标识	String	M	必输
     * 01：钞
     * 02：汇
     * 00：人民币
     */
    private String charCode;

    public String getAccountKey() {
        return accountKey;
    }

    public void setAccountKey(String accountKey) {
        this.accountKey = accountKey;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getTranSeq() {
        return tranSeq;
    }

    public void setTranSeq(String tranSeq) {
        this.tranSeq = tranSeq;
    }

    public String getCharCode() {
        return charCode;
    }

    public void setCharCode(String charCode) {
        this.charCode = charCode;
    }
}
