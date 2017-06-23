package com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadNetHistoryQuery;

import org.threeten.bp.LocalDate;

import java.util.List;

/**
 * 历史净值查询-响应
 * Created by liuweidong on 2016/10/22.
 */
public class PsnXpadNetHistoryQueryResult {

    private String productCode;// 产品代码
    private String minPrice;// 最小历史净值
    private String maxPrice;// 最大历史净值
    private String recordNumber;
    private List<ListBean> list;

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(String minPrice) {
        this.minPrice = minPrice;
    }

    public String getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(String maxPrice) {
        this.maxPrice = maxPrice;
    }

    public String getRecordNumber() {
        return recordNumber;
    }

    public void setRecordNumber(String recordNumber) {
        this.recordNumber = recordNumber;
    }

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean{
        private String netValue;// 净值
        private LocalDate valueDate;// 净值日期

        public String getNetValue() {
            return netValue;
        }

        public void setNetValue(String netValue) {
            this.netValue = netValue;
        }

        public LocalDate getValueDate() {
            return valueDate;
        }

        public void setValueDate(LocalDate valueDate) {
            this.valueDate = valueDate;
        }
    }
}
