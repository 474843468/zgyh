package com.boc.bocsoft.mobile.wfss.buss.funds.model.jztendency;

import java.util.List;

/**
 * 3.3 基金详情接口2（净值走势-趋势图、历史净值）
 * Created by gwluo on 2016/10/25.
 */

public class WFSSJzTendencyResult {
    private String fundId;//	基金代码
    private List<FundList> items;//	列表

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
        private String jzTime;//	净值时间
        private String dwjz;//	单位净值
        private String ljjz;//	累计净值
        private String changeOfDay;//	日涨跌幅

        public String getJzTime() {
            return jzTime;
        }

        public void setJzTime(String jzTime) {
            this.jzTime = jzTime;
        }

        public String getDwjz() {
            return dwjz;
        }

        public void setDwjz(String dwjz) {
            this.dwjz = dwjz;
        }

        public String getLjjz() {
            return ljjz;
        }

        public void setLjjz(String ljjz) {
            this.ljjz = ljjz;
        }

        public String getChangeOfDay() {
            return changeOfDay;
        }

        public void setChangeOfDay(String changeOfDay) {
            this.changeOfDay = changeOfDay;
        }
    }
}
