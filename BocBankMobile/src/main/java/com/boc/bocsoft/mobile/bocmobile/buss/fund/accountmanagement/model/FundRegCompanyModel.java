package com.boc.bocsoft.mobile.bocmobile.buss.fund.accountmanagement.model;

import java.io.Serializable;
import java.util.List;

/**
 * 由033接口 PsnGetFundRegCompanyList 基金公司查询（注册登记机构查询）
 * 上送参数：空
 * 返回参数：List<RegCompanyBean>
 * Created by lyf7084 on 2016/12/13.
 */
public class FundRegCompanyModel implements Serializable {
    private List<RegCompanyBean> list;

    public List<RegCompanyBean> getList() {
        return list;
    }

    public void setList(List<RegCompanyBean> list) {
        this.list = list;
    }

    public class RegCompanyBean implements Comparable<RegCompanyBean>, Serializable {
        /**
         * 登记基金公司名称
         */
        private String fundRegName;

        /**
         * 登记基金公司代码
         */
        private String fundRegCode;

        /**
         * 基金公司字母标题
         */
        private String fundRegLetterTitle;

        /**
         * 基金公司名称拼音
         */
        private String fundRegNamePinyin;


        public String getFundRegName() {
            return fundRegName;
        }

        public void setFundRegName(String fundRegName) {
            this.fundRegName = fundRegName;
        }

        public String getFundRegCode() {
            return fundRegCode;
        }

        public void setFundRegCode(String fundRegCode) {
            this.fundRegCode = fundRegCode;
        }

        public String getFundRegLetterTitle() {
            return fundRegLetterTitle;
        }

        public void setFundRegLetterTitle(String fundRegLetterTitle) {
            this.fundRegLetterTitle = fundRegLetterTitle;
        }

        public String getFundRegNamePinyin() {
            return this.fundRegNamePinyin;
        }

        public void setFundRegNamePinyin(String fundRegNamePinyin) {
            this.fundRegNamePinyin = fundRegNamePinyin;
        }

        @Override
        public int compareTo(RegCompanyBean another) {
            return this.getFundRegNamePinyin().compareTo(another.getFundRegNamePinyin());
        }
    }

}
