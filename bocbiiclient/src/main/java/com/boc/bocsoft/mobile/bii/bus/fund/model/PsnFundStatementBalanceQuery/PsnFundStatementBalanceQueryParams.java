package com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundStatementBalanceQuery;

import java.util.List;

/**
 * Created by huixiaobo on 2016/11/19.
 * 053基金对账单持仓查询—上送参数
 */
public class PsnFundStatementBalanceQueryParams {

    /**年月*/
    private String fundStatementTime;

    public String getFundStatementTime() {
        return fundStatementTime;
    }

    public void setFundStatementTime(String fundStatementTime) {
        this.fundStatementTime = fundStatementTime;
    }

}
