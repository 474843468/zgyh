package com.chinamworld.bocmbci.model.httpmodel.sifang.fund.fundnotice;

import java.util.List;

/**
 * Created by Administrator on 2016/10/27 0027.
 * 3.7 基金详情接口7（基金公告列表，分页)返回字段
 */
public class FundnoticeResult {

    private List<FundnoticeItem> items;
    private String isNextPage;

    public List<FundnoticeItem> getItem() {
        return items;
    }

    public void setItem(List<FundnoticeItem> item) {
        this.items = item;
    }

    public String getIsNextPage() {
        return isNextPage;
    }

    public void setIsNextPage(String isNextPage) {
        this.isNextPage = isNextPage;
    }

    public class FundnoticeItem{
        private String ioriid;//	公告id
        private String strcaption;//	公告标题
        private String sdtpublish;//	公告时间
        private String reporturl;//	Pdf文件URL

        public String getSdtpublish() {
            return sdtpublish;
        }

        public void setSdtpublish(String sdtpublish) {
            this.sdtpublish = sdtpublish;
        }

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
    }
}
