package com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.longshortforexhome.model.wfssQueryMultipleQuotation;

import java.util.List;

/**
 * response：涨跌幅／值：queryMultipleQuotation-请求model{查询外汇实盘、外汇虚盘、贵金属实盘、贵金属虚盘行情、详情基本信息。}
 * Created by yx on 2016年12月15日 11:05:03
 */ 
public class WFSSQueryMultipleQuotationResModel {

    /**
     * rcdcnt : 30
     * items : [{"currPercentDiff":"-2.563%","sellRate":"7.6443","sortPriority":2,"ccygrpNm":"欧元/人民币","currDiff":"-0.2175","tranCode":"T","sourceCurrencyCode":"038","targetCurrencyCode":"001","priceTime":"20210401093119","buyRate":"8.8900","referPrice":"8.2672"},{"currPercentDiff":"+15.251%","sellRate":"1.1202","sortPriority":11,"ccygrpNm":"美元/瑞士法郎","currDiff":"+0.1483","tranCode":"S","sourceCurrencyCode":"014","targetCurrencyCode":"015","priceTime":"20210401053000","buyRate":"1.1212","referPrice":"1.1207"},{"currPercentDiff":"+0.035%","sellRate":"85.48","sortPriority":12,"ccygrpNm":"瑞士法郎/日元","currDiff":"+0.03","tranCode":"T","sourceCurrencyCode":"015","targetCurrencyCode":"027","priceTime":"20210401093119","buyRate":"85.62","referPrice":"85.55"},{"currPercentDiff":"+0.022%","sellRate":"90.27","sortPriority":15,"ccygrpNm":"加拿大元/日元","currDiff":"+0.02","tranCode":"T","sourceCurrencyCode":"028","targetCurrencyCode":"027","priceTime":"20210401093119","buyRate":"90.59","referPrice":"90.43"},{"currPercentDiff":"-3.751%","sellRate":"0.7359","sortPriority":17,"ccygrpNm":"澳大利亚元/美元","currDiff":"-0.0287","tranCode":"S","sourceCurrencyCode":"029","targetCurrencyCode":"014","priceTime":"20200306000005","buyRate":"0.7371","referPrice":"0.7365"},{"currPercentDiff":"+0.079%","sellRate":"88.60","sortPriority":19,"ccygrpNm":"澳大利亚元/日元","currDiff":"+0.07","tranCode":"T","sourceCurrencyCode":"029","targetCurrencyCode":"027","priceTime":"20210401093119","buyRate":"88.98","referPrice":"88.79"},{"currPercentDiff":"-2.562%","sellRate":"1.3779","sortPriority":21,"ccygrpNm":"欧元/瑞士法郎","currDiff":"-0.0392","tranCode":"T","sourceCurrencyCode":"038","targetCurrencyCode":"015","priceTime":"20210401093119","buyRate":"1.6032","referPrice":"1.4906"},{"currPercentDiff":"-2.597%","sellRate":"1.3262","sortPriority":23,"ccygrpNm":"欧元/澳大利亚元","currDiff":"-0.0383","tranCode":"T","sourceCurrencyCode":"038","targetCurrencyCode":"029","priceTime":"20210401093119","buyRate":"1.5465","referPrice":"1.4364"},{"currPercentDiff":"+0.036%","sellRate":"137.90","sortPriority":26,"ccygrpNm":"英镑/日元","currDiff":"+0.05","tranCode":"T","sourceCurrencyCode":"012","targetCurrencyCode":"027","priceTime":"20210401093119","buyRate":"138.16","referPrice":"138.03"},{"currPercentDiff":"+0.042%","sellRate":"95.78","sortPriority":30,"ccygrpNm":"美元/日元","currDiff":"+0.04","tranCode":"T","sourceCurrencyCode":"014","targetCurrencyCode":"027","priceTime":"20210401093119","buyRate":"95.97","referPrice":"95.88"},{"currPercentDiff":"-10.272%","sellRate":"1.2006","sortPriority":31,"ccygrpNm":"美元/加拿大元","currDiff":"-0.1375","tranCode":"T","sourceCurrencyCode":"014","targetCurrencyCode":"028","priceTime":"20160530053000","buyRate":"1.2016","referPrice":"1.2011"},{"currPercentDiff":"+28.313%","sellRate":"1.5748","sortPriority":33,"ccygrpNm":"英镑/美元","currDiff":"+0.3476","tranCode":"T","sourceCurrencyCode":"012","targetCurrencyCode":"014","priceTime":"20160530053000","buyRate":"1.5758","referPrice":"1.5753"},{"currPercentDiff":"-26.718%","sellRate":"0.9998","sortPriority":34,"ccygrpNm":"欧元/美元","currDiff":"-0.3647","tranCode":"T","sourceCurrencyCode":"038","targetCurrencyCode":"014","priceTime":"20160530053000","buyRate":"1.0008","referPrice":"1.0003"},{"currPercentDiff":"-2.563%","sellRate":"0.8537","sortPriority":35,"ccygrpNm":"欧元/英镑","currDiff":"-0.0243","tranCode":"T","sourceCurrencyCode":"038","targetCurrencyCode":"012","priceTime":"20210401093119","buyRate":"0.9940","referPrice":"0.9239"},{"currPercentDiff":"-2.523%","sellRate":"117.83","sortPriority":36,"ccygrpNm":"欧元/日元","currDiff":"-3.30","tranCode":"T","sourceCurrencyCode":"038","targetCurrencyCode":"027","priceTime":"20210401093119","buyRate":"137.21","referPrice":"127.52"},{"currPercentDiff":"0.000%","sellRate":"2.0230","sortPriority":39,"ccygrpNm":"英镑/新加坡元","currDiff":"0.0000","tranCode":"T","sourceCurrencyCode":"012","targetCurrencyCode":"018","priceTime":"20210401053000","buyRate":"2.0300","referPrice":"2.0265"},{"currPercentDiff":"-1.969%","sellRate":"1.3583","sortPriority":40,"ccygrpNm":"美元/新加坡元","currDiff":"-0.0273","tranCode":"S","sourceCurrencyCode":"014","targetCurrencyCode":"018","priceTime":"20200306000005","buyRate":"1.3599","referPrice":"1.3591"},{"currPercentDiff":"0.000%","sellRate":"1.2543","sortPriority":41,"ccygrpNm":"瑞士法郎/新加坡元","currDiff":"0.0000","tranCode":"T","sourceCurrencyCode":"015","targetCurrencyCode":"018","priceTime":"20210401053000","buyRate":"1.2577","referPrice":"1.2560"},{"currPercentDiff":"-0.002%","sellRate":"5.0994","sortPriority":42,"ccygrpNm":"新加坡元/港币","currDiff":"-0.0001","tranCode":"T","sourceCurrencyCode":"018","targetCurrencyCode":"013","priceTime":"20210401093119","buyRate":"5.1306","referPrice":"5.1150"},{"currPercentDiff":"0.000%","sellRate":"1.2793","sortPriority":44,"ccygrpNm":"加拿大元/新加坡元","currDiff":"0.0000","tranCode":"T","sourceCurrencyCode":"028","targetCurrencyCode":"018","priceTime":"20200306035820","buyRate":"1.2843","referPrice":"1.2818"},{"currPercentDiff":"0.000%","sellRate":"0.9988","sortPriority":45,"ccygrpNm":"澳大利亚元/新加坡元","currDiff":"0.0000","tranCode":"T","sourceCurrencyCode":"029","targetCurrencyCode":"018","priceTime":"20200306000005","buyRate":"1.0031","referPrice":"1.0010"},{"currPercentDiff":"0.000%","sellRate":"1.8799","sortPriority":46,"ccygrpNm":"欧元/新加坡元","currDiff":"0.0000","tranCode":"T","sourceCurrencyCode":"038","targetCurrencyCode":"018","priceTime":"20200306000005","buyRate":"1.8927","referPrice":"1.8863"},{"currPercentDiff":"0.000%","sellRate":"5.5455","sortPriority":52,"ccygrpNm":"瑞士法郎/人民币","currDiff":"0.0000","tranCode":"T","sourceCurrencyCode":"015","targetCurrencyCode":"001","priceTime":"20210401053000","buyRate":"5.5474","referPrice":"5.5465"},{"currPercentDiff":"0.000%","sellRate":"4.4150","sortPriority":53,"ccygrpNm":"新加坡元/人民币","currDiff":"0.0000","tranCode":"T","sourceCurrencyCode":"018","targetCurrencyCode":"001","priceTime":"20210401053000","buyRate":"4.4169","referPrice":"4.4160"},{"currPercentDiff":"0.000%","sellRate":"0.0648","sortPriority":54,"ccygrpNm":"日元/人民币","currDiff":"0.0000","tranCode":"T","sourceCurrencyCode":"027","targetCurrencyCode":"001","priceTime":"20210401093119","buyRate":"0.0649","referPrice":"0.0649"},{"currPercentDiff":"-0.019%","sellRate":"5.8580","sortPriority":55,"ccygrpNm":"加拿大元/人民币","currDiff":"-0.0011","tranCode":"T","sourceCurrencyCode":"028","targetCurrencyCode":"001","priceTime":"20210401093112","buyRate":"5.8679","referPrice":"5.8630"},{"currPercentDiff":"+0.038%","sellRate":"5.7485","sortPriority":56,"ccygrpNm":"澳大利亚元/人民币","currDiff":"+0.0022","tranCode":"T","sourceCurrencyCode":"029","targetCurrencyCode":"001","priceTime":"20210401093112","buyRate":"5.7640","referPrice":"5.7563"},{"currPercentDiff":"-99.958%","sellRate":"6.2159","sortPriority":62,"ccygrpNm":"美元/人民币","currDiff":"-14711.3885","tranCode":"T","sourceCurrencyCode":"014","targetCurrencyCode":"001","priceTime":"20210401053000","buyRate":"6.2159","referPrice":"6.2159"},{"currPercentDiff":"0.000%","sellRate":"8.9467","sortPriority":64,"ccygrpNm":"英镑/人民币","currDiff":"0.0000","tranCode":"T","sourceCurrencyCode":"012","targetCurrencyCode":"001","priceTime":"20210401053000","buyRate":"8.9515","referPrice":"8.9491"},{"currPercentDiff":"0.000%","sellRate":"0.8633","sortPriority":66,"ccygrpNm":"港币/人民币","currDiff":"0.0000","tranCode":"T","sourceCurrencyCode":"013","targetCurrencyCode":"001","priceTime":"20210401093119","buyRate":"0.8634","referPrice":"0.8634"}]
     */

    /**
     * 货币对数
     */
    private int rcdcnt;
    /**
     * 循环体
     */
    private List<ItemsEntity> items;

    public void setRcdcnt(int rcdcnt) {
        this.rcdcnt = rcdcnt;
    }

    public void setItems(List<ItemsEntity> items) {
        this.items = items;
    }

    public int getRcdcnt() {
        return rcdcnt;
    }

    public List<ItemsEntity> getItems() {
        return items;
    }

    public static class ItemsEntity {
        /**
         * currPercentDiff : -2.563%
         * sellRate : 7.6443
         * sortPriority : 2
         * ccygrpNm : 欧元/人民币
         * currDiff : -0.2175
         * tranCode : T
         * sourceCurrencyCode : 038
         * targetCurrencyCode : 001
         * priceTime : 20210401093119
         * buyRate : 8.8900
         * referPrice : 8.2672
         */
        /**
         * 今日涨跌幅{小数点后有效位数为3位}
         */
        private String currPercentDiff;
        /**
         * 客户卖出价
         */
        private String sellRate;
        /**
         * 货币对优先级
         */
        private int sortPriority;
        /**
         * 货币对名称
         */
        private String ccygrpNm;
        /**
         * 今日涨跌值
         */
        private String currDiff;
        /**
         * 可交易标识	{T-可交易,S-停止交易}
         */
        private String tranCode;
        /**
         * 首货币代码
         */
        private String sourceCurrencyCode;
        /**
         * 尾货币代码
         */
        private String targetCurrencyCode;
        /**
         * 牌价有效时间		yyyyMMddHH24mmss
         */
        private String priceTime;
        /**
         * 客户买入价
         */
        private String buyRate;
        /**
         * 中间价		{小数位比买入价和卖出价多一位}
         */
        private String referPrice;

        public void setCurrPercentDiff(String currPercentDiff) {
            this.currPercentDiff = currPercentDiff;
        }

        public void setSellRate(String sellRate) {
            this.sellRate = sellRate;
        }

        public void setSortPriority(int sortPriority) {
            this.sortPriority = sortPriority;
        }

        public void setCcygrpNm(String ccygrpNm) {
            this.ccygrpNm = ccygrpNm;
        }

        public void setCurrDiff(String currDiff) {
            this.currDiff = currDiff;
        }

        public void setTranCode(String tranCode) {
            this.tranCode = tranCode;
        }

        public void setSourceCurrencyCode(String sourceCurrencyCode) {
            this.sourceCurrencyCode = sourceCurrencyCode;
        }

        public void setTargetCurrencyCode(String targetCurrencyCode) {
            this.targetCurrencyCode = targetCurrencyCode;
        }

        public void setPriceTime(String priceTime) {
            this.priceTime = priceTime;
        }

        public void setBuyRate(String buyRate) {
            this.buyRate = buyRate;
        }

        public void setReferPrice(String referPrice) {
            this.referPrice = referPrice;
        }

        public String getCurrPercentDiff() {
            return currPercentDiff;
        }

        public String getSellRate() {
            return sellRate;
        }

        public int getSortPriority() {
            return sortPriority;
        }

        public String getCcygrpNm() {
            return ccygrpNm;
        }

        public String getCurrDiff() {
            return currDiff;
        }

        public String getTranCode() {
            return tranCode;
        }

        public String getSourceCurrencyCode() {
            return sourceCurrencyCode;
        }

        public String getTargetCurrencyCode() {
            return targetCurrencyCode;
        }

        public String getPriceTime() {
            return priceTime;
        }

        public String getBuyRate() {
            return buyRate;
        }

        public String getReferPrice() {
            return referPrice;
        }
    }
}
