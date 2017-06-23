package com.chinamworld.bocmbci.model.httpmodel.sifang.fund.queryFundBasicDetail;

import com.chinamworld.bocmbci.model.httpmodel.sifang.fund.BaseSFFundRequestModel;

/**
 * Created by Administrator on 2016/10/27 0027.
 * 基金详情接口（基本信息）接口上送字段
 */
public class QueryFundBasicDetailRequestParams extends BaseSFFundRequestModel {
    @Override
    public Object getExtendParam() {
        return "queryFundBasicDetail";
    }

    private String fundId;//基金Id

    private String fundBakCode;//fundId 和fundBakCode 不能同时为空

    public String getFundBakCode() {
        return fundBakCode;
    }

    public void setFundBakCode(String fundBakCode) {
        this.fundBakCode = fundBakCode;
    }

    public String getFundId() {
        return fundId;
    }

    public void setFundId(String fundId) {
        this.fundId = fundId;
    }

    public QueryFundBasicDetailRequestParams(/*String fundId,*/String fundBakCode){
        //this.fundId = fundId;
        this.fundBakCode = fundBakCode;
    }

}
