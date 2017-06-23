package com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadShareTransitionVerify;

import java.io.Serializable;

/**
 *  * Created by zn on 2016/9/12.
 * 4.70 070  PsnXpadShareTransitionVerify    锁定期份额转换预交易    结果Model
 */
public class PsnXpadShareTransitionVerifyResult implements Serializable {
    /**
     * 预计年收益率	String
     */
    private String exyield;
    /***
     * 产品代码	String
     */
    private String proId;
    /**
     * 钞汇类型	String	01：钞
     * 02：汇
     * 00：人民币钞汇
     * ：若产品属性为不允许部分赎回，钞汇属性无意义，必须同步赎回
     */
    private String charCode;

    /**
     * 帐号	String
     */
    private String accNo;
    /**
     * 转换份额	BigDecimal
     */
    private String tranUnit;
    /**
     * 产品币种	String	001：人民币元
     * 014：美元
     * 012：英镑
     * 013：港币
     * 028: 加拿大元
     * 029：澳元
     * 038：欧元
     * 027：日元
     */
    private String proCur;
    /**
     * 交易日期	String YYYY/MM/DD
     */
    private String tranDate;

    /**
     * 交易号	String 转换成交交易使用
     */
    private String trsseq;
    /**
     * 产品名称	String
     */
    private String proNam;

    public String getExyield() {
        return exyield;
    }

    public void setExyield(String exyield) {
        this.exyield = exyield;
    }

    public String getProId() {
        return proId;
    }

    public void setProId(String proId) {
        this.proId = proId;
    }

    public String getCharCode() {
        return charCode;
    }

    public void setCharCode(String charCode) {
        this.charCode = charCode;
    }

    public String getAccNo() {
        return accNo;
    }

    public void setAccNo(String accNo) {
        this.accNo = accNo;
    }

    public String getTranUnit() {
        return tranUnit;
    }

    public void setTranUnit(String tranUnit) {
        this.tranUnit = tranUnit;
    }

    public String getProCur() {
        return proCur;
    }

    public void setProCur(String proCur) {
        this.proCur = proCur;
    }

    public String getTrsseq() {
        return trsseq;
    }

    public void setTrsseq(String trsseq) {
        this.trsseq = trsseq;
    }

    public String getTranDate() {
        return tranDate;
    }

    public void setTranDate(String tranDate) {
        this.tranDate = tranDate;
    }

    public String getProNam() {
        return proNam;
    }

    public void setProNam(String proNam) {
        this.proNam = proNam;
    }
}
