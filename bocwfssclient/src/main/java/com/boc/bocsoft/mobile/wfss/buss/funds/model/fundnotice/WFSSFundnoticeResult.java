package com.boc.bocsoft.mobile.wfss.buss.funds.model.fundnotice;

import java.util.List;

/**
 * 3.7 基金详情接口7（基金公告列表，分页)
 * Created by gwluo on 2016/10/26.
 */

public class WFSSFundnoticeResult {
    private String isNextPage;//	是否含有下一页
    private List<FundList> items;//	列表

    public String getIsNextPage() {
        return isNextPage;
    }

    public void setIsNextPage(String isNextPage) {
        this.isNextPage = isNextPage;
    }

    public List<FundList> getItems() {
        return items;
    }

    public void setItems(List<FundList> items) {
        this.items = items;
    }

    public class FundList {
        private String ioriid;//	公告id
        private String strcaption;//	公告标题
        private String sdtpublish;//	公告时间
        private String reporturl;//	Pdf文件URL

        public String getIoriid() {
            return ioriid;
        }

        public void setIoriid(String ioriid) {
            this.ioriid = ioriid;
        }

        public String getStrcaption() {
            return strcaption;
        }

        public void setStrcaption(String strcaption) {
            this.strcaption = strcaption;
        }

        public String getSdtpublish() {
            return sdtpublish;
        }

        public void setSdtpublish(String sdtpublish) {
            this.sdtpublish = sdtpublish;
        }

        public String getReporturl() {
            return reporturl;
        }

        public void setReporturl(String reporturl) {
            this.reporturl = reporturl;
        }
    }


}
