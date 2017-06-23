package com.chinamworld.bocmbci.model.httpmodel.sifang.fund.newsContent;

import java.util.List;

/**
 * Created by Administrator on 2016/10/27 0027.
 * 3.9 基金详情接口9（基金新闻内容)返回字段
 */
public class NewsContentResult {

    private String content;//	新闻内容
    private String name;//	来源
    private String author;//	作者
    private String editor;//	编辑
    private String description;//	摘要

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
