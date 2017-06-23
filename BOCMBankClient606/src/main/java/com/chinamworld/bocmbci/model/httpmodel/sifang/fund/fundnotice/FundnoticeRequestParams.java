package com.chinamworld.bocmbci.model.httpmodel.sifang.fund.fundnotice;

import com.chinamworld.bocmbci.model.httpmodel.sifang.fund.BaseSFFundRequestModel;

/**
 * Created by Administrator on 2016/10/27 0027.
 * 3.7 基金详情接口7（基金公告列表，分页)上送字段
 */
public class FundnoticeRequestParams extends BaseSFFundRequestModel {
    @Override
    public Object getExtendParam() {
        return "fundnotice";
    }

    private String fundId;//	基金Id
    private String fundBakCode;
    private String pageNo;//	页数
    private String pageSize;//	每页最大条数


    public FundnoticeRequestParams(String fundBakCode, String pageNo, String pageSize){
        this.fundBakCode = fundBakCode;
        this.pageNo = pageNo;
        this.pageSize = pageSize;

    }

}
