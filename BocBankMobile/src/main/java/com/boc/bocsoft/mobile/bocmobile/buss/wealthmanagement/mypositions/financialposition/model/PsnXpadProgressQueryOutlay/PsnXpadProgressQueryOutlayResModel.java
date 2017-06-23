package com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.financialposition.model.PsnXpadProgressQueryOutlay;

import java.io.Serializable;
import java.util.List;

/**
 * 4.53 053 PsnXpadProgressQueryOutlay 登录前累进产品收益率查询  响应Model
 * Created by zn on 2016/11/23.
 */
public class PsnXpadProgressQueryOutlayResModel implements Serializable {

    /**
     * 总笔数
     */
    private String recordNumber;
    /**
     * 列表数据
     */
    private java.util.List<ListBean> List;

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
