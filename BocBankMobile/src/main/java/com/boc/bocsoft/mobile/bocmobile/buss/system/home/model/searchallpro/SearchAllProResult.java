package com.boc.bocsoft.mobile.bocmobile.buss.system.home.model.searchallpro;

import java.util.ArrayList;
import java.util.List;

/**
 * 4.1 全局搜索
 * Created by gwluo on 2016/11/5.
 */

public class SearchAllProResult {
    private List<Forex> forexList = new ArrayList<>();//牌价列表
    private List<Fund> fundList = new ArrayList<>();//基金列表
    private List<Lc> lcList = new ArrayList<>();//	理财列表


    public class Forex {
        private String sourceCurrencyCode;//	首货币代码	forexList
        private String targetCurrencyCode;//	尾货币代码	forexList
        private String ccygrpNm;//	货币对名称	forexList
        /**
         * 牌价类型	forexList	F-外汇牌价
         * G-贵金属牌价
         */
        private String cardType;
        /**
         * 牌价种类	forexList	R-实盘(外汇)
         * M-虚盘(双向宝)
         */
        private String cardClass;

        public String getSourceCurrencyCode() {
            return sourceCurrencyCode;
        }

        public void setSourceCurrencyCode(String sourceCurrencyCode) {
            this.sourceCurrencyCode = sourceCurrencyCode;
        }

        public String getTargetCurrencyCode() {
            return targetCurrencyCode;
        }

        public void setTargetCurrencyCode(String targetCurrencyCode) {
            this.targetCurrencyCode = targetCurrencyCode;
        }

        public String getCcygrpNm() {
            return ccygrpNm;
        }

        public void setCcygrpNm(String ccygrpNm) {
            this.ccygrpNm = ccygrpNm;
        }

        public String getCardType() {
            return cardType;
        }

        public void setCardType(String cardType) {
            this.cardType = cardType;
        }

        public String getCardClass() {
            return cardClass;
        }

        public void setCardClass(String cardClass) {
            this.cardClass = cardClass;
        }
    }

    public class Fund {
        //基金内部代码，用于系统之间对接使用，可唯一代表一只基金，基金详情查询时需要上送此字段
        private String fundId;
        //基金公共代码	用于前端展示使用
        private String fundBakCode;
        //基金名称
        private String fundName;

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
    }

    public class Lc {
        private String proId;//	理财产品代码
        private String proName;//	理财产品名称
        private String kind;//	产品性质,0结构性理财，1类基金理财

        public String getKind() {
            return kind;
        }

        public void setKind(String kind) {
            this.kind = kind;
        }

        public String getProId() {
            return proId;
        }

        public void setProId(String proId) {
            this.proId = proId;
        }

        public String getProName() {
            return proName;
        }

        public void setProName(String proName) {
            this.proName = proName;
        }
    }

    public List<Forex> getForexList() {
        return forexList;
    }

    public void setForexList(List<Forex> forexList) {
        this.forexList = forexList;
    }

    public List<Fund> getFundList() {
        return fundList;
    }

    public void setFundList(List<Fund> fundList) {
        this.fundList = fundList;
    }

    public List<Lc> getLcList() {
        return lcList;
    }

    public void setLcList(List<Lc> lcList) {
        this.lcList = lcList;
    }
}
