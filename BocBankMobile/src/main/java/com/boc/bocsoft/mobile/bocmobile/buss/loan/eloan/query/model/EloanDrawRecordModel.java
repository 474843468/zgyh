package com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.query.model;


/**
 * Created by huixiaobo on 2016/9/27.
 * 贷款查询
 */
public class EloanDrawRecordModel {

    /**贷款用途*/
    private String merchant;
    /**交易渠道*/
    private String channel;

    public String getMerchant() {
        return merchant;
    }

    public void setMerchant(String merchant) {
        this.merchant = merchant;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    @Override
    public String toString() {
        return "EloanDrawRecordModel{" +
                "merchant='" + merchant + '\'' +
                ", channel='" + channel + '\'' +
                '}';
    }
}
