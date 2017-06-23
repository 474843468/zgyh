package com.boc.bocsoft.mobile.wfss.buss.funds.model.newslist;

import java.util.List;

/**
 * 3.8 基金详情接口8（基金新闻列表，分页)
 * Created by gwluo on 2016/10/26.
 */

public class WFSSNewsListResult {
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
