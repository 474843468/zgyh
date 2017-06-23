package com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.productrecommend.model;

import java.io.Serializable;

/**
 * 投资协议申请页面数据
 * Created by Wan mengxin on 2016/11/11.
 */
public class ProductModel implements Serializable{
    //产品代码
    private String productCode;
    //产品名称
    private String productName;
    //获取币种
    private String curCode;
    //最大可购买期数
    private String remainCycleCount;
    //账户标识
    private String accountId;

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getCurCode() {
        return curCode;
    }

    public void setCurCode(String curCode) {
        this.curCode = curCode;
    }

    public String getRemainCycleCount() {
        return remainCycleCount;
    }

    public void setRemainCycleCount(String remainCycleCount) {
        this.remainCycleCount = remainCycleCount;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }
}
