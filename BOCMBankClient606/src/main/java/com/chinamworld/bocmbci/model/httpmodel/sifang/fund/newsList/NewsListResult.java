package com.chinamworld.bocmbci.model.httpmodel.sifang.fund.newsList;

import java.util.List;

/**
 * Created by Administrator on 2016/10/27 0027.
 * 3.8 基金详情接口8（基金新闻列表，分页)返回字段
 */
public class NewsListResult {

    private List<NewsListItem> items;
    private String isNextPage;

    public List<NewsListItem> getItem() {
        return items;
    }

    public void setItem(List<NewsListItem> item) {
        this.items = item;
    }

    public String getIsNextPage() {
        return isNextPage;
    }

    public void setIsNextPage(String isNextPage) {
        this.isNextPage = isNextPage;
    }

    public class NewsListItem{
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
