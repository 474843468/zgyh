package com.boc.bocsoft.mobile.bocmobile.base.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.math.BigDecimal;

/**
 * 贵金属基础bean
 * Created by lxw on 2016/8/4 0004.
 */
public class GoldBean implements Parcelable {

    // 省行联行号
    private String ibknum;

    // 货币对状态 T可交易；S停止交易
    private String state;
    // X-FUND涨跌标志 0未变,1涨,2跌
    private String flag;

    // X-FUND牌价类型
    private String type;
    // 兑换币别
    private String sourceCurrencyCode;
    // 目标货币
    private String targetCurrencyCode;
    // 银行现汇买入价
    private BigDecimal buyRate;
    // 银行现汇卖出价
    private BigDecimal sellRate;


    // 画面显示用
    private String name;

    // 更新时间
    private String updateDate;

    // 0：加载中 1：正确返回 2：请求失败
    private String refreshState = "0";

    public GoldBean(){

    }
    /**
     * 币种
     */
    public class CurrencyCode{
        // 货币码
        private String code;
        // 辅币位数
        private String fraction;
        // 国际化标识
        private String i18nId;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getFraction() {
            return fraction;
        }

        public void setFraction(String fraction) {
            this.fraction = fraction;
        }

        public String getI18nId() {
            return i18nId;
        }

        public void setI18nId(String i18nId) {
            this.i18nId = i18nId;
        }
    }


    protected GoldBean(Parcel in) {
        ibknum = in.readString();
        state = in.readString();
        flag = in.readString();
        type = in.readString();
        sourceCurrencyCode = in.readString();
        targetCurrencyCode = in.readString();
        name = in.readString();
        updateDate = in.readString();
        refreshState = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(ibknum);
        dest.writeString(state);
        dest.writeString(flag);
        dest.writeString(type);
        dest.writeString(sourceCurrencyCode);
        dest.writeString(targetCurrencyCode);
        dest.writeString(name);
        dest.writeString(updateDate);
        dest.writeString(refreshState);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<GoldBean> CREATOR = new Creator<GoldBean>() {
        @Override
        public GoldBean createFromParcel(Parcel in) {
            return new GoldBean(in);
        }

        @Override
        public GoldBean[] newArray(int size) {
            return new GoldBean[size];
        }
    };

    public String getIbknum() {
        return ibknum;
    }

    public void setIbknum(String ibknum) {
        this.ibknum = ibknum;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSourceCurrencyCode() {
        return sourceCurrencyCode;
    }

    public void setSourceCurrencyCode(String sourceCurrencyCode) {
        this.sourceCurrencyCode = sourceCurrencyCode;
    }

    public String getTargetCurrencyCode() {
        return targetCurrencyCode;
    }

    public void setTargetCurrencyCode(String targetCurrencyCode) {
        this.targetCurrencyCode = targetCurrencyCode;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public BigDecimal getSellRate() {
        return sellRate;
    }

    public void setSellRate(BigDecimal sellRate) {
        this.sellRate = sellRate;
    }

    public BigDecimal getBuyRate() {
        return buyRate;
    }

    public void setBuyRate(BigDecimal buyRate) {
        this.buyRate = buyRate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRefreshState() {
        return refreshState;
    }

    public void setRefreshState(String refreshState) {
        this.refreshState = refreshState;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

}
