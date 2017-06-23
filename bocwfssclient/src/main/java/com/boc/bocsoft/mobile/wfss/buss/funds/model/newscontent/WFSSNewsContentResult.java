package com.boc.bocsoft.mobile.wfss.buss.funds.model.newscontent;

/**
 * 3.9 基金详情接口9（基金新闻内容)
 * Created by gwluo on 2016/10/26.
 */

public class WFSSNewsContentResult {
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getEditor() {
        return editor;
    }

    public void setEditor(String editor) {
        this.editor = editor;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
