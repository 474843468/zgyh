package com.boc.bocsoft.mobile.bocmobile.buss.fund.investmanagement.update.model;

import java.io.Serializable;

/**
 * Created by huixiaobo on 2016/12/15.
 * 061接口返回view参数
 */
public class FundCompanyModel implements Serializable{
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
