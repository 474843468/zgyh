package com.boc.bocsoft.mobile.wfss.buss.common.model;

import java.util.List;

/**
 * 4.1 全局搜索
 * Created by gwluo on 2016/11/5.
 */

public class WFSSSearchAllProResult {
    private List<Fund> fundList;//基金列表
    private List<Lc> lcList;//	理财列表


    public class Fund {
        //基金内部代码，用于系统之间对接使用，可唯一代表一只基金，基金详情查询时需要上送此字段
        private String fundId;
        //基金公共代码	用于前端展示使用
        private String fundBakCode;
        //基金名称
        private String fundName;
        private String fundType;//基金类型

        public String getFundType() {
            return fundType;
        }

        public void setFundType(String fundType) {
            this.fundType = fundType;
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
    }

    public class Lc {
        private String proId;//	理财产品代码
        private String proName;//	理财产品名称
        private String kind;//	产品性质，0结构性理财，1类基金产品

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
