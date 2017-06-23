package com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundCanDealDateQuery;

import java.util.List;

/**
 * Created by zcy7065 on 2016/11/23.
 */
public class PsnFundCanDealDateQueryResult {

    /**
     * 可交易日期列表
     * */
    private List<String> dealDatelist;

    public List<String> getDealDatelist() {
        return dealDatelist;
    }

    public void setDealDatelist(List<String> list) {
        this.dealDatelist = list;
    }
}
