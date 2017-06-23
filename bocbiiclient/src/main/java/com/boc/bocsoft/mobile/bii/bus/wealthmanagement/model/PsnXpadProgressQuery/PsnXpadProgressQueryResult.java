package com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadProgressQuery;

import java.util.ArrayList;
import java.util.List;

/**
 * 4.34 034累进产品收益率查询PsnXpadProgressQuery
 * Created by cff on 2016/10/19.
 */
public class PsnXpadProgressQueryResult {

    /**
     * 总笔数
     */
    private String recordNumber;
    /**
     * 列表数据
     */
    private List<ListBean> List = new ArrayList<ListBean>();

    public String getRecordNumber() {
        return recordNumber;
    }

    public void setRecordNumber(String recordNumber) {
        this.recordNumber = recordNumber;
    }

    public List<ListBean> getList() {
        return List;
    }

    public void setList(List<ListBean> list) {
        this.List = list;
    }

    public static class ListBean {
        /**
         *产品代码
         */
        private String prodCode;
        /**
         *开始天数
         */
        private String minDays;
        /**
         *结束天数
         */
        private String maxDays;
        /**
         *预计年收益率（%）
         */
        private String yearlyRR;
        /**
         *发布收益率日期
         */
        private String pubrateDate;
        /**
         *生效日期
         */
        private String excuteDate;

        public String getProdCode() {
            return prodCode;
        }

        public void setProdCode(String prodCode) {
            this.prodCode = prodCode;
        }

        public String getMinDays() {
            return minDays;
        }

        public void setMinDays(String minDays) {
            this.minDays = minDays;
        }

        public String getMaxDays() {
            return maxDays;
        }

        public void setMaxDays(String maxDays) {
            this.maxDays = maxDays;
        }

        public String getYearlyRR() {
            return yearlyRR;
        }

        public void setYearlyRR(String yearlyRR) {
            this.yearlyRR = yearlyRR;
        }

        public String getPubrateDate() {
            return pubrateDate;
        }

        public void setPubrateDate(String pubrateDate) {
            this.pubrateDate = pubrateDate;
        }

        public String getExcuteDate() {
            return excuteDate;
        }

        public void setExcuteDate(String excuteDate) {
            this.excuteDate = excuteDate;
        }
    }
}
