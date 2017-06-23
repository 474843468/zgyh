package com.boc.bocsoft.mobile.wfss.buss.funds.model.ranktendency;

import java.util.List;

/**
 * 3.5 基金详情接口4（排名变化--趋势图、历史排名变化)
 * Created by gwluo on 2016/10/25.
 */

public class WFSSRankTendencyResult {
    private String fundId;//基金代码

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
        private String jzTime;// 净值时间
        private String rankScore;// 排名分位数
        private String rank;// 排名
        private String similarCount;// 同类总量

        public String getSimilarCount() {
            return similarCount;
        }

        public void setSimilarCount(String similarCount) {
            this.similarCount = similarCount;
        }

        public String getJzTime() {
            return jzTime;
        }

        public void setJzTime(String jzTime) {
            this.jzTime = jzTime;
        }

        public String getRankScore() {
            return rankScore;
        }

        public void setRankScore(String rankScore) {
            this.rankScore = rankScore;
        }

        public String getRank() {
            return rank;
        }

        public void setRank(String rank) {
            this.rank = rank;
        }

    }
}
