package com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadAccountQuery;

/**
 * 查询客户理财账户信息
 * Created by zhx on 2016/9/3
 */
public class PsnXpadAccountQueryParams {

    /**
     * queryType : 0
     * xpadAccountSatus : 1
     */

    // 查询类型(0：查询所有已登记的理财账户 1、查询所有已登记并且关联到网银的理财账户)
    private String queryType;
    // 账户状态(0：停用 1：可用 不输代表查询全部)
    private String xpadAccountSatus;

    public void setQueryType(String queryType) {
        this.queryType = queryType;
    }

    public void setXpadAccountSatus(String xpadAccountSatus) {
        this.xpadAccountSatus = xpadAccountSatus;
    }

    public String getQueryType() {
        return queryType;
    }

    public String getXpadAccountSatus() {
        return xpadAccountSatus;
    }
}
