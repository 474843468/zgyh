package com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundCompanyInfoQuery;

/**
 * Created by huixiaobo on 2016/11/18.
 * 061基金公司信息查询—返回参数
 */
public class PsnFundCompanyInfoQueryResult {
    /**基金管理公司名称*/
    private String companyName;
    /**客户服务电话*/
    private String companyPhone;

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyPhone() {
        return companyPhone;
    }

    public void setCompanyPhone(String companyPhone) {
        this.companyPhone = companyPhone;
    }

    @Override
    public String toString() {
        return "PsnFundCompanyInfoQueryResult{" +
                "companyName='" + companyName + '\'' +
                ", companyPhone='" + companyPhone + '\'' +
                '}';
    }
}
