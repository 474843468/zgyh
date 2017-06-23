package com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdChargeOnRMBAccountQuery;

/**
 * 全球交易人民币记账功能查询
 * Created by wangf on 2016/11/22.
 */
public class PsnCrcdChargeOnRMBAccountQueryResult {

    //卡产品名称（描述）
    private String description;
    //别名
    private String alias;
    //开通标识 - true：开通   false：关闭
    private String openFlag;
    //账号（显示账号带星号）
    private String displayNum;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getOpenFlag() {
        return openFlag;
    }

    public void setOpenFlag(String openFlag) {
        this.openFlag = openFlag;
    }

    public String getDisplayNum() {
        return displayNum;
    }

    public void setDisplayNum(String displayNum) {
        this.displayNum = displayNum;
    }
}
