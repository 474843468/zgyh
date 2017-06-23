package com.chinamworld.bocmbci.model.httpmodel.sifang.fund.newsContent;

import com.chinamworld.bocmbci.model.httpmodel.sifang.fund.BaseSFFundRequestModel;

/**
 * Created by Administrator on 2016/10/27 0027.
 * 3.9 基金详情接口9（基金新闻内容)上送字段
 */
public class NewsContentRequestParams extends BaseSFFundRequestModel {
    @Override
    public Object getExtendParam() {
        return "newsContent";
    }

    private String contentId;//	新闻Id

    public NewsContentRequestParams(String contentId){
        this.contentId = contentId;

    }

}
