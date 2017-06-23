package com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadShareTransitionCommit;

/**
 * Created by zn on 2016/9/12.
 * 4.71 071  PsnXpadShareTransitionCommit锁定期份额转换成交交易  结果Model
 */
public class PsnXpadShareTransitionCommitResult {

//    "trsDate": "2016/01/01",
//            "stDate": "2016/01/01",
//            "eDate": "2016/01/01",
//            "tranUnit": "1000.000",
//            "proCur": "014",
//            "makNo": "1",
//            "proid": "123123",
//            "transactionId  ": "12345678",
//            "proNam": "aaaa"
    /***
     * 交易日期	String	YYYY/MM/DD
     */
    private String trsDate;
    /**
     * 转换成立日	String	YYYY/MM/DD
     */
    private String stDate;


    /***
     * 份额到期日	String	YYYY/MM/DD
     */
    private String eDate;
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
     * 柜员营销代码	String	输入人力部员工号，目前预留位数够了
     */
    private String makNo;
    /**
     * 产品代码	String
     */
    private String proid;
    /***
     * 网银交易序号	String
     */
    private String transactionId;
    /**
     * 产品名称	String
     */
    private String proNam;
    /**
     * 钞汇标识	String	1：钞
     2：汇
     0：人民币钞汇

     */
    private String charCode;

    public String getCharCode() {
        return charCode;
    }

    public void setCharCode(String charCode) {
        this.charCode = charCode;
    }

    public String getTrsDate() {
        return trsDate;
    }

    public void setTrsDate(String trsDate) {
        this.trsDate = trsDate;
    }

    public String getStDate() {
        return stDate;
    }

    public void setStDate(String stDate) {
        this.stDate = stDate;
    }

    public String geteDate() {
        return eDate;
    }

    public void seteDate(String eDate) {
        this.eDate = eDate;
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

    public String getMakNo() {
        return makNo;
    }

    public void setMakNo(String makNo) {
        this.makNo = makNo;
    }

    public String getProid() {
        return proid;
    }

    public void setProid(String proid) {
        this.proid = proid;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getProNam() {
        return proNam;
    }

    public void setProNam(String proNam) {
        this.proNam = proNam;
    }

}
