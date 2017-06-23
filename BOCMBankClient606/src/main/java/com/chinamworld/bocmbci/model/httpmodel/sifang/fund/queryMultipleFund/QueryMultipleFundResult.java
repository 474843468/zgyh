package com.chinamworld.bocmbci.model.httpmodel.sifang.fund.queryMultipleFund;

import java.util.List;

/**
 * Created by Administrator on 2016/10/27 0027.
 * 基金列表查询接口返回字段
 */
public class QueryMultipleFundResult {

    private List<QueryMultipleFundItem> items;
    private String isNextPage;

    public List<QueryMultipleFundItem> getItems() {
        return items;
    }

    public void setItems(List<QueryMultipleFundItem> items) {
        this.items = items;
    }

    public String getIsNextPage() {
        return isNextPage;
    }

    public void setIsNextPage(String isNextPage) {
        this.isNextPage = isNextPage;
    }

    public class QueryMultipleFundItem{
        private String fundId;
        private String fundBakCode;
        private String fundName;
        private String fundType;
        private String jzTime;
        private String fundStatus;
        private String transCurrency;
        private String levelOfRisk;
        private String fundCompany;
        private String dwjz;
        private String currPercentDiff;
        private String changeOfMonth;
        private String changeOfQuarter;
        private String changeOfHalfYear;
        private String changeOfYear;
        private String thisYearPriceChange;
        private String yieldOfWeek;
        private String yieldOfTenThousand;
        private String productType;
        private String changeOfWeek;

        public String getChangeOfWeek() {
            return changeOfWeek;
        }

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

        public String getFundId() {
            return fundId;
        }

        public void setFundId(String fundId) {
            this.fundId = fundId;
        }

        public String getFundBakCode() {
            return fundBakCode;
        }

        public void setFundBakCode(String fundBakCode) {
            this.fundBakCode = fundBakCode;
        }

        public String getFundName() {
            return fundName;
        }

        public void setFundName(String fundName) {
            this.fundName = fundName;
        }

        public String getCurrPercentDiff() {
            return currPercentDiff;
        }

        public void setCurrPercentDiff(String currPercentDiff) {
            this.currPercentDiff = currPercentDiff;
        }

        public String getChangeOfMonth() {
            return changeOfMonth;
        }

        public void setChangeOfMonth(String changeOfMonth) {
            this.changeOfMonth = changeOfMonth;
        }

        public String getChangeOfQuarter() {
            return changeOfQuarter;
        }

        public void setChangeOfQuarter(String changeOfQuarter) {
            this.changeOfQuarter = changeOfQuarter;
        }

        public String getChangeOfHalfYear() {
            return changeOfHalfYear;
        }

        public void setChangeOfHalfYear(String changeOfHalfYear) {
            this.changeOfHalfYear = changeOfHalfYear;
        }

        public String getChangeOfYear() {
            return changeOfYear;
        }

        public void setChangeOfYear(String changeOfYear) {
            this.changeOfYear = changeOfYear;
        }

        public String getThisYearPriceChange() {
            return thisYearPriceChange;
        }

        public void setThisYearPriceChange(String thisYearPriceChange) {
            this.thisYearPriceChange = thisYearPriceChange;
        }
    }
}
