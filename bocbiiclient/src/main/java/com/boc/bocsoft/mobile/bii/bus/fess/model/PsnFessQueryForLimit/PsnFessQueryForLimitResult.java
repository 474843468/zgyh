package com.boc.bocsoft.mobile.bii.bus.fess.model.PsnFessQueryForLimit;

/**
 * 4.13 013PsnFessQueryForLimit查询个人结售汇额度
 * Created by gwluo on 2016/11/16.
 */

public class PsnFessQueryForLimitResult {
    private String annAmtUSD;//本年额度内已结/购汇金额折美元	String
    private String annRmeAmtUSD;//本年额度内剩余可/售结汇金额折美元	String
    private String custTypeCode;//交易主体类型代码	String	01境内个人 02境外个人
    private String typeStatus;//个人主体分类状态代码	String	01正常  02预关注  03关注名单
    private String signStatus;//	确认书签署状态	String	0未告知  1已告知
    private String pubDate;//	发布日期	String
    private String endDate;//	到期日期	String
    private String pubReason;//	发布原因	String
    private String custName;//	交易主体姓名	String	用于风险提示函与关注名单告知书提示使用

    public String getAnnAmtUSD() {
        return annAmtUSD;
    }

    public void setAnnAmtUSD(String annAmtUSD) {
        this.annAmtUSD = annAmtUSD;
    }

    public String getAnnRmeAmtUSD() {
        return annRmeAmtUSD;
    }

    public void setAnnRmeAmtUSD(String annRmeAmtUSD) {
        this.annRmeAmtUSD = annRmeAmtUSD;
    }

    public String getCustTypeCode() {
        return custTypeCode;
    }

    public void setCustTypeCode(String custTypeCode) {
        this.custTypeCode = custTypeCode;
    }

    public String getTypeStatus() {
        return typeStatus;
    }

    public void setTypeStatus(String typeStatus) {
        this.typeStatus = typeStatus;
    }

    public String getSignStatus() {
        return signStatus;
    }

    public void setSignStatus(String signStatus) {
        this.signStatus = signStatus;
    }

    public String getPubDate() {
        return pubDate;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getPubReason() {
        return pubReason;
    }

    public void setPubReason(String pubReason) {
        this.pubReason = pubReason;
    }

    public String getCustName() {
        return custName;
    }

    public void setCustName(String custName) {
        this.custName = custName;
    }
}
