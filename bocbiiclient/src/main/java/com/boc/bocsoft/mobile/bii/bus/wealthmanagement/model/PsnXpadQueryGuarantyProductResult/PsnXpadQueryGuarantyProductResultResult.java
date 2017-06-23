package com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadQueryGuarantyProductResult;

/**
 * 组合购买已押押品查询（详情）
 * Created by zhx on 2016/9/5
 */
public class PsnXpadQueryGuarantyProductResultResult {

    /**
     * freezeUnit : 10.000000
     * cashRemit : 0
     * prodName : P305-WY-RJYL-LXF0007
     * impawnPermit : 0
     * prodEnd : 2020/07/20
     * curCode : 001
     * prodCode : P305-WY-RJYL-LXF0007
     * prodBegin : 2020/07/05
     */

    // 押品代码
    private String prodCode;
    // 押品名称
    private String prodName;
    // 币种（001：人民币元 014：美元 012：英镑 013：港币 028: 加拿大元 029：澳元 038：欧元 027：日元）
    private String curCode;
    // 钞汇标识（1：钞 2：汇 0：人民币）
    private String cashRemit;
    // 押品份额
    private String freezeUnit;
    // 质押日期
    private String prodBegin;
    // 押品到期日
    private String prodEnd;
    // 组合购买状态（0：未成交 1：成交成功 2：成交失败）
    private String impawnPermit;

    public void setFreezeUnit(String freezeUnit) {
        this.freezeUnit = freezeUnit;
    }

    public void setCashRemit(String cashRemit) {
        this.cashRemit = cashRemit;
    }

    public void setProdName(String prodName) {
        this.prodName = prodName;
    }

    public void setImpawnPermit(String impawnPermit) {
        this.impawnPermit = impawnPermit;
    }

    public void setProdEnd(String prodEnd) {
        this.prodEnd = prodEnd;
    }

    public void setCurCode(String curCode) {
        this.curCode = curCode;
    }

    public void setProdCode(String prodCode) {
        this.prodCode = prodCode;
    }

    public void setProdBegin(String prodBegin) {
        this.prodBegin = prodBegin;
    }

    public String getFreezeUnit() {
        return freezeUnit;
    }

    public String getCashRemit() {
        return cashRemit;
    }

    public String getProdName() {
        return prodName;
    }

    public String getImpawnPermit() {
        return impawnPermit;
    }

    public String getProdEnd() {
        return prodEnd;
    }

    public String getCurCode() {
        return curCode;
    }

    public String getProdCode() {
        return prodCode;
    }

    public String getProdBegin() {
        return prodBegin;
    }
}
