package com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadProductQueryAndBuyInit;

/**
 * 产品查询与购买初始化(产品种类查询)
 * Created by liuweidong on 2016/9/26.
 */
public class PsnXpadProductQueryAndBuyInitResult {
    private String name;// 产品种类名称
    private String brandId;// 产品种类编号

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBrandId() {
        return brandId;
    }

    public void setBrandId(String brandId) {
        this.brandId = brandId;
    }
}
