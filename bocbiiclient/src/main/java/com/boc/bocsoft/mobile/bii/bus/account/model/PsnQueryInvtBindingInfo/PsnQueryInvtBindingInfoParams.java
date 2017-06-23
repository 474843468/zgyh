package com.boc.bocsoft.mobile.bii.bus.account.model.PsnQueryInvtBindingInfo;

/**
 * 查询投资交易账号绑定信息请求参数
 * Created by lzc4524 on 2016/11/23.
 */
public class PsnQueryInvtBindingInfoParams {
    //投资交易类型, 外汇：10；黄金：11；基金：12；国债：13；XPAD（中银理财计划I）：23；贵金属代理：26
    private String invtType;
    public String getInvtType() {
        return invtType;
    }

    public void setInvtType(String invtType) {
        this.invtType = invtType;
    }
}
