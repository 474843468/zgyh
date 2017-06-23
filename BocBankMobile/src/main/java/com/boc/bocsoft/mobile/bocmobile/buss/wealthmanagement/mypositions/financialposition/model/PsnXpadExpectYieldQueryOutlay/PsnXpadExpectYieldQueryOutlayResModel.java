package com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.financialposition.model.PsnXpadExpectYieldQueryOutlay;

import java.util.List;

/**
 * 4.73 073登录前业绩基准产品预计年收益率查询 PsnXpadExpectYieldQueryOutlay
 * Created by zn on 2016/11/23.
 */
public class PsnXpadExpectYieldQueryOutlayResModel {

    /**
     * 产品代码	String
     */
    private String  prodID;
    /**
     * 产品名称	String
     */
    private String  prodName;
    /**
     * 上一生效日期	String
     */
    private String lastDate;
    /**
     * 下一生效日期	String
     */
    private String  nextDate;
    /**
     * 是否允许转低收益产品	String
     */
    private String  isLowProfit;
    /**
     * 普通份额预期收益率	BigDecimal
     */
    private String  exYield;
    /**
     * 收益率报价日期	String
     */
    private String postDate;

    /**
     * 列表数据
     */
    private List<ListEntity> list;

    public String getExYield() {
        return exYield;
    }

    public void setExYield(String exYield) {
        this.exYield = exYield;
    }

    public String getIsLowProfit() {
        return isLowProfit;
    }

    public void setIsLowProfit(String isLowProfit) {
        this.isLowProfit = isLowProfit;
    }

    public String getLastDate() {
        return lastDate;
    }

    public void setLastDate(String lastDate) {
        this.lastDate = lastDate;
    }

    public String getNextDate() {
        return nextDate;
    }

    public void setNextDate(String nextDate) {
        this.nextDate = nextDate;
    }

    public String getPostDate() {
        return postDate;
    }

    public List<ListEntity> getList() {
        return list;
    }

    public void setList(List<ListEntity> list) {
        this.list = list;
    }

    public void setPostDate(String postDate) {
        this.postDate = postDate;
    }

    public String getProdID() {
        return prodID;
    }

    public void setProdID(String prodID) {
        this.prodID = prodID;
    }

    public String getProdName() {
        return prodName;
    }

    public void setProdName(String prodName) {
        this.prodName = prodName;
    }

    public static class ListEntity {
        /**
         * 客户类型	String	0：普通客户
         1：中银理财
         2：中银财富管理
         3：中银私人银行

         */
        private String  custLevel;
        /**
         * 购买金额开始	String
         */
        private String  minAmt;
        /**
         * 购买金额结束	String	-1表示无穷大
         */
        private String  maxAmt;
        /**
         * 预计年收益率	BigDecimal
         */
        private String  rates;

        public String getCustLevel() {
            return custLevel;
        }

        public void setCustLevel(String custLevel) {
            this.custLevel = custLevel;
        }

        public String getMaxAmt() {
            return maxAmt;
        }

        public void setMaxAmt(String maxAmt) {
            this.maxAmt = maxAmt;
        }

        public String getMinAmt() {
            return minAmt;
        }

        public void setMinAmt(String minAmt) {
            this.minAmt = minAmt;
        }

        public String getRates() {
            return rates;
        }

        public void setRates(String rates) {
            this.rates = rates;
        }
    }

}
