package com.boc.bocsoft.mobile.bocmobile.buss.fund.common.fundproductdetail.model;

import com.boc.bocsoft.mobile.common.utils.BeanConvertor.anno.ListItemType;

import java.util.List;

/**
 * 基金新闻内容view层model
 * Created by lzc4524 on 2016/12/20.
 */
public class FundNewsListViewModel {
    /**
     * 上送参数
     */
    private String pageNo;//	页数		上送页数，默认为1
    private String pageSize;//	每页最大条数		上送每页最大条数，默认为10
    private String fundBakCode;//基金公共代码

    public String getPageNo() {
        return pageNo;
    }

    public String getPageSize() {
        return pageSize;
    }

    public String getFundBakCode() {
        return fundBakCode;
    }

    public void setFundBakCode(String fundBakCode) {
        this.fundBakCode = fundBakCode;
    }


    public void setPageNo(String pageNo) {
        this.pageNo = pageNo;
    }

    public void setPageSize(String pageSize) {
        this.pageSize = pageSize;
    }


    /**
     * 返回参数
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
        private String title;//	新闻标题
        private String created;//	日期时间
        private String contentId;//	新闻Id

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getCreated() {
            return created;
        }

        public void setCreated(String created) {
            this.created = created;
        }

        public String getContentId() {
            return contentId;
        }

        public void setContentId(String contentId) {
            this.contentId = contentId;
        }
    }
}
