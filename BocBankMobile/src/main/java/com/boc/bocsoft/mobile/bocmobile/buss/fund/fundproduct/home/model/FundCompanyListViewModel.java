package com.boc.bocsoft.mobile.bocmobile.buss.fund.fundproduct.home.model;

import java.io.Serializable;
import java.util.List;

/**
 * 基金公司列表viewmodel（登陆前、登陆后两个接口共用）
 * Created by liuzc on 2016/11/29.
 */
public class FundCompanyListViewModel{
    private List<ListBean> list;

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public class ListBean{
        private String fundCompanyName; //基金公司名称
        private String fundCompanyCode; //基金公司代码
        private String fundCompanyLetterTitle; //基金公司字母标题

        public String getFundCompanyName() {
            return fundCompanyName;
        }

        public void setFundCompanyName(String fundCompanyName) {
            this.fundCompanyName = fundCompanyName;
        }

        public String getFundCompanyCode() {
            return fundCompanyCode;
        }

        public void setFundCompanyCode(String fundCompanyCode) {
            this.fundCompanyCode = fundCompanyCode;
        }

        public String getFundCompanyLetterTitle() {
            return fundCompanyLetterTitle;
        }

        public void setFundCompanyLetterTitle(String fundCompanyLetterTitle) {
            this.fundCompanyLetterTitle = fundCompanyLetterTitle;
        }
    }
}
