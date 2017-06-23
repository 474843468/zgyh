package com.boc.bocsoft.mobile.wfss.buss.forexandnoblemetal.model.queryaveragetendency;

import java.util.List;

/**
 * 2.4 外汇、贵金属趋势图查询
 *
 * @author gwluo
 *         2016/10/25.
 */

public class WFSSQueryAverageTendencyResult {
    private List<Tendency> tendencyList;//	行情列表	body

    public List<Tendency> getTendencyList() {
        return tendencyList;
    }

    public void setTendencyList(List<Tendency> tendencyList) {
        this.tendencyList = tendencyList;
    }

    public class Tendency {
        /**
         * 时间 yyyy-MM-dd HH24:mm:ss
         */
        private String timestamp;
        //  参考价格
        private String averagePrice;

        public String getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(String timestamp) {
            this.timestamp = timestamp;
        }

        public String getAveragePrice() {
            return averagePrice;
        }

        public void setAveragePrice(String averagePrice) {
            this.averagePrice = averagePrice;
        }
    }


}
