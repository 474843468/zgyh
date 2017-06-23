package com.boc.bocsoft.mobile.wfss.buss.funds.model.ljyieldratetendency;

import java.util.List;

/**
 * 3.4 基金详情接口3（基金累计收益率--趋势图、历史累计收益率）
 * Created by gwluo on 2016/10/25.
 */

public class WFSSLjYieldRateTendencyResult {
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
        private String jzTime;//	净值时间
        private String ljYieldRate;//	累计收益率
        private String szzjYieldRate;//	上证综指收益率
        private String yjbjjzYieldRate;//	业绩比较基准收益率
        //	区间存款利率,理财型的基金显示区间存款利率
        private String sectionDepositRate;

        public String getJzTime() {
            return jzTime;
        }

        public void setJzTime(String jzTime) {
            this.jzTime = jzTime;
        }

        public String getLjYieldRate() {
            return ljYieldRate;
        }

        public void setLjYieldRate(String ljYieldRate) {
            this.ljYieldRate = ljYieldRate;
        }

        public String getSzzjYieldRate() {
            return szzjYieldRate;
        }

        public void setSzzjYieldRate(String szzjYieldRate) {
            this.szzjYieldRate = szzjYieldRate;
        }

        public String getYjbjjzYieldRate() {
            return yjbjjzYieldRate;
        }

        public void setYjbjjzYieldRate(String yjbjjzYieldRate) {
            this.yjbjjzYieldRate = yjbjjzYieldRate;
        }

        public String getSectionDepositRate() {
            return sectionDepositRate;
        }

        public void setSectionDepositRate(String sectionDepositRate) {
            this.sectionDepositRate = sectionDepositRate;
        }
    }
}
