package com.boc.bocsoft.mobile.wfss.buss.forexandnoblemetal.model.queryktendency;

import java.util.List;

/**
 * 2.3 外汇、贵金属K线图查询 queryKTendency
 * Created by gwluo on 2016/10/25.
 */

public class WFSSQueryKTendencyResult {

    private List<KList> kList;//K线图列表

    public List<KList> getkList() {
        return kList;
    }

    public class KList {
        private String timestamp;//	K线图时间小时：yyyyMMdd HH24:mi日周月：yyyyMMdd
        private String openPrice;//	开盘价格
        private String maxPrice;//	最高值
        private String minPrice;//	最低值
        private String closePrice;//	收盘价格
        /*时间区间	kList	小时：yyyyMMdd HH24日周：yyyyMMdd 月：yyyyMM*/
        private String timeZone;

        public String getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(String timestamp) {
            this.timestamp = timestamp;
        }

        public String getOpenPrice() {
            return openPrice;
        }

        public void setOpenPrice(String openPrice) {
            this.openPrice = openPrice;
        }

        public String getMaxPrice() {
            return maxPrice;
        }

        public void setMaxPrice(String maxPrice) {
            this.maxPrice = maxPrice;
        }

        public String getMinPrice() {
            return minPrice;
        }

        public void setMinPrice(String minPrice) {
            this.minPrice = minPrice;
        }

        public String getClosePrice() {
            return closePrice;
        }

        public void setClosePrice(String closePrice) {
            this.closePrice = closePrice;
        }

        public String getTimeZone() {
            return timeZone;
        }

        public void setTimeZone(String timeZone) {
            this.timeZone = timeZone;
        }
    }
}
