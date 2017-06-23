package com.boc.bocsoft.mobile.bocmobile.buss.fund.common.fundproductdetail.model;

import com.boc.bocsoft.mobile.common.utils.BeanConvertor.anno.ListItemType;

import java.util.List;

/**
 * 基金公告内容view层model
 * Created by lzc4524 on 2016/12/20.
 */
public class FundNoticeViewModel {
    /**
     * 上送参数
     */
    private String fundId;//基金Id	20	Y
    private String pageNo;//页数，上送页数，默认为1
    private String pageSize; //每页最大条数

    public String getFundId() {
        return fundId;
    }

    public String getPageNo() {
        return pageNo;
    }

    public String getPageSize() {
        return pageSize;
    }

    public void setPageSize(String pageSize) {
        this.pageSize = pageSize;
    }

    public void setFundId(String fundId) {
        this.fundId = fundId;
    }

    public void setPageNo(String pageNo) {
        this.pageNo = pageNo;
    }

    /**
     * 返回结果
     */
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

    public static class FundList {
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
