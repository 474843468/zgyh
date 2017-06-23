package com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadCapacityTransList;

/**
 * 客户投资协议交易明细查询
 * Created by guokai on 2016/9/6.
 */
public class PsnXpadCapacityTransListResult {


    private String custAgrCode;//	客户协议编号
    private String  agrType;//	协议类型
    private String instType;//	投资方式
    private String tdsDate;//	交易日期
    private String tdsType;//	交易类型
    private String tdsAmt;//	交易金额
    private String tdsUnit;//	交易份额
    private String tdsState;//	交易状态
    private String memo;//	失败原因


    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getAgrType() {
        return agrType;
    }

    public void setAgrType(String agrType) {
        this.agrType = agrType;
    }

    public String getCustAgrCode() {
        return custAgrCode;
    }

    public void setCustAgrCode(String custAgrCode) {
        this.custAgrCode = custAgrCode;
    }

    public String getInstType() {
        return instType;
    }

    public void setInstType(String instType) {
        this.instType = instType;
    }

    public String getTdsAmt() {
        return tdsAmt;
    }

    public void setTdsAmt(String tdsAmt) {
        this.tdsAmt = tdsAmt;
    }

    public String getTdsDate() {
        return tdsDate;
    }

    public void setTdsDate(String tdsDate) {
        this.tdsDate = tdsDate;
    }

    public String getTdsState() {
        return tdsState;
    }

    public void setTdsState(String tdsState) {
        this.tdsState = tdsState;
    }

    public String getTdsType() {
        return tdsType;
    }

    public void setTdsType(String tdsType) {
        this.tdsType = tdsType;
    }

    public String getTdsUnit() {
        return tdsUnit;
    }

    public void setTdsUnit(String tdsUnit) {
        this.tdsUnit = tdsUnit;
    }
}
