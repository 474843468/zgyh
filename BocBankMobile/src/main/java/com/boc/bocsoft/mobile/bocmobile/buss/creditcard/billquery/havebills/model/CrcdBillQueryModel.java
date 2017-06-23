package com.boc.bocsoft.mobile.bocmobile.buss.creditcard.billquery.havebills.model;

/**
 * 4.7 007查询信用卡已出账单PsnCrcdQueryBilledTrans
 * 4.8 008查询信用卡已出账单交易明细PsnCrcdQueryBilledTransDetail
 * 接口请求参数
 *
 * 作者：xwg on 16/12/20 15:32
 * Modified by liuweidong on 2016/12/28.
 */
public class CrcdBillQueryModel {
    private String accountId;
    /**
     * 格式:yyyy/MM
     * 当日期为9999/99时为查询最近一期账单
     */
    private String statementMonth;

    /**
     * 信用卡账户标识	String	M	从PsnCrcdQueryBilledTrans接口中获取creditcardId
     */
    private String creditcardId;
    /**
     * 帐户类型 当为空时，不区分账户查询，将依次返回不同账户的交易。
     */
    private String accountType;
    private String pageNo;// 页码
    /**
     * 分页查询唯一号	String	O	第一页查询为空，后续页码查询不可为空，根据上一次查询返回的回应报文中获取，并用于后续查询。
     */
    private String primary;
    private String lineNum;// 每页返回行数

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getCreditcardId() {
        return creditcardId;
    }

    public void setCreditcardId(String creditcardId) {
        this.creditcardId = creditcardId;
    }

    public String getLineNum() {
        return lineNum;
    }

    public void setLineNum(String lineNum) {
        this.lineNum = lineNum;
    }

    public String getPageNo() {
        return pageNo;
    }

    public void setPageNo(String pageNo) {
        this.pageNo = pageNo;
    }

    public String getPrimary() {
        return primary;
    }

    public void setPrimary(String primary) {
        this.primary = primary;
    }

    public String getStatementMonth() {
        return statementMonth;
    }

    public void setStatementMonth(String statementMonth) {
        this.statementMonth = statementMonth;
    }
}
