package com.boc.bocsoft.mobile.wfss.buss.funds.model.yieldofweektendency;

import java.util.List;

/**
 * 3.6 基金详情接口5（七日年化收益率--趋势图、历史)
 * Created by gwluo on 2016/10/25.
 */

public class WFSSYieldOfWeekTendencyResult {
    private String fundId;//	基金代码
    private List<FundList> items;//列表

    public String getFundId() {
        return fundId;
    }

    public void setFundId(String fundId) {
        this.fundId = fundId;
    }

    public List<FundList> getItems() {
        return items;
    }

    public void setItems(List<FundList> items) {
        this.items = items;
    }

    public class FundList {
        private String date;//	日期
        private String yieldOfWeek;//	七日年化收益率

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getYieldOfWeek() {
            return yieldOfWeek;
        }

        public void setYieldOfWeek(String yieldOfWeek) {
            this.yieldOfWeek = yieldOfWeek;
        }
    }

}
