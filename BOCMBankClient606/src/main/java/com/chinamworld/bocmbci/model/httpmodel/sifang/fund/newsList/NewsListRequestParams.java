package com.chinamworld.bocmbci.model.httpmodel.sifang.fund.newsList;

import com.chinamworld.bocmbci.model.httpmodel.sifang.fund.BaseSFFundRequestModel;

/**
 * Created by Administrator on 2016/10/27 0027.
 * 3.8 基金详情接口8（基金新闻列表，分页)上送字段
 */
public class NewsListRequestParams extends BaseSFFundRequestModel {
    @Override
    public Object getExtendParam() {
        return "newsList";
    }

    private String fundBakCode;//	标签、关键字后修改为基金公共代码
    private String pageNo;//	页数
    private String pageSize;//	每页最大条数


    public NewsListRequestParams(String fundBakCode, String pageNo, String pageSize){
        this.fundBakCode = fundBakCode;
        this.pageNo = pageNo;
        this.pageSize = pageSize;

    }

}
