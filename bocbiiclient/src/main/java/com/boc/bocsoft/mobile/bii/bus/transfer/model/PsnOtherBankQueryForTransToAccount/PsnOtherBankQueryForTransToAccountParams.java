package com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnOtherBankQueryForTransToAccount;

/**
 * Params:查询银行列表
 * Created by zhx on 2016/7/20
 */
public class PsnOtherBankQueryForTransToAccountParams {
    /**
     * 查询类型(“HOT”：常用银行查询（查询返回14个常用热门银行）；“OTHER”：其他更多银行查询时上送（不含14个热门银行、不含中国银行总行）)
     */
    private String type;
    /**
     * 银行名称
     */
    private String bankName;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }
}
