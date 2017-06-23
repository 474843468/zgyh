package com.boc.bocsoft.mobile.bii.bus.account.model.PsnCrcdVirtualCardSettledbillQuery;

/**
 * @author wangyang
 *         16/7/26 17:57
 *         虚拟银行卡已出账单查询
 */
public class PsnCrcdVirtualCardSettledbillQueryParams {

    /** 账户户名 */
    private String accountName;
    /** 虚拟卡卡号 */
    private String virtualCardNo;
    /** 已出账单月份--格式 yyyy/MM */
    private String statementMonth;

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getVirtualCardNo() {
        return virtualCardNo;
    }

    public void setVirtualCardNo(String virtualCardNo) {
        this.virtualCardNo = virtualCardNo;
    }

    public String getStatementMonth() {
        return statementMonth;
    }

    public void setStatementMonth(String statementMonth) {
        this.statementMonth = statementMonth;
    }
}
