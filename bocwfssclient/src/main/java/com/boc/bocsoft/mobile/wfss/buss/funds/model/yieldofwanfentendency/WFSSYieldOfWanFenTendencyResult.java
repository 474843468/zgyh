package com.boc.bocsoft.mobile.wfss.buss.funds.model.yieldofwanfentendency;

import java.util.List;

/**
 * 3.10 基金详情接口10（万份收益率--趋势图、历史)
 * Created by gwluo on 2016/10/26.
 */

public class WFSSYieldOfWanFenTendencyResult {
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
        private String date;//	日期
        private String yieldOfTenThousand;//	万份收益

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getYieldOfTenThousand() {
            return yieldOfTenThousand;
        }

        public void setYieldOfTenThousand(String yieldOfTenThousand) {
            this.yieldOfTenThousand = yieldOfTenThousand;
        }
    }
}
