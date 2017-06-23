package com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundAttentionQueryList;

import java.util.List;

/**
 * Created by liuzc on 2017/1/2.
 */
public class PsnFundAttentionQueryListResult {
    private List<ListItem> attentionList; //我关注的基金attentionList实体

    public List<ListItem> getAttentionList() {
        return attentionList;
    }

    public void setAttentionList(List<ListItem> attentionList) {
        this.attentionList = attentionList;
    }

    public static class ListItem{
        private String fundCode; //基金代码
        private String fundShortName; //基金简称
        private String fundCompanyName; //基金公司
        /**
         * 货币码
         * 001-人民币,012-英镑,013-港币,014-美元,027-日元,038-欧元
         */
        private String currency;
        /**
         * 钞汇标志
         * CAS代表钞,TRN代表汇
         */
        private String cashFlag;
        private String netprice; //单位净值
        private String netValEndDate;//净值截至日期
        private String dayincomeratio; //单位净值增长率
        private String fundInfo; //基金信息

        public String getFundCode() {
            return fundCode;
        }

        public void setFundCode(String fundCode) {
            this.fundCode = fundCode;
        }

        public String getFundShortName() {
            return fundShortName;
        }

        public void setFundShortName(String fundShortName) {
            this.fundShortName = fundShortName;
        }

        public String getFundCompanyName() {
            return fundCompanyName;
        }

        public void setFundCompanyName(String fundCompanyName) {
            this.fundCompanyName = fundCompanyName;
        }

        public String getCurrency() {
            return currency;
        }

        public void setCurrency(String currency) {
            this.currency = currency;
        }

        public String getCashFlag() {
            return cashFlag;
        }

        public void setCashFlag(String cashFlag) {
            this.cashFlag = cashFlag;
        }

        public String getNetprice() {
            return netprice;
        }

        public void setNetprice(String netprice) {
            this.netprice = netprice;
        }

        public String getNetValEndDate() {
            return netValEndDate;
        }

        public void setNetValEndDate(String netValEndDate) {
            this.netValEndDate = netValEndDate;
        }

        public String getDayincomeratio() {
            return dayincomeratio;
        }

        public void setDayincomeratio(String dayincomeratio) {
            this.dayincomeratio = dayincomeratio;
        }

        public String getFundInfo() {
            return fundInfo;
        }

        public void setFundInfo(String fundInfo) {
            this.fundInfo = fundInfo;
        }
    }
}
