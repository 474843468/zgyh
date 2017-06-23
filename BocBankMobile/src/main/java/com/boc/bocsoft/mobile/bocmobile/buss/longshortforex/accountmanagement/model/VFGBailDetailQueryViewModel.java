package com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.accountmanagement.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.math.BigDecimal;

/**
 * ViewModel：保证金账户详情
 * Created by zhx on 2016/11/25
 */
public class VFGBailDetailQueryViewModel implements Parcelable {
    // 借记卡卡号
    private String accountNumber;
    // 结算货币(014美元 001人民币 038欧元 013港币 027日元 029澳元)
    private String settleCurrency;

    public void setSettleCurrency(String settleCurrency) {
        this.settleCurrency = settleCurrency;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getSettleCurrency() {
        return settleCurrency;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    //======================================//
    // 下面大致对应接口响应的字段
    //======================================//

    // 保证金账号
    private String marginAccountNo;
    // 交易所需保证金比例
    private BigDecimal needMarginRatio;
    // 报警比例
    private BigDecimal warnRatio;
    // 斩仓比例
    private BigDecimal liquidationRatio;
    // 开仓充足率
    private BigDecimal openRate;

    public void setMarginAccountNo(String marginAccountNo) {
        this.marginAccountNo = marginAccountNo;
    }

    public void setNeedMarginRatio(BigDecimal needMarginRatio) {
        this.needMarginRatio = needMarginRatio;
    }

    public void setOpenRate(BigDecimal openRate) {
        this.openRate = openRate;
    }

    public void setWarnRatio(BigDecimal warnRatio) {
        this.warnRatio = warnRatio;
    }

    public void setLiquidationRatio(BigDecimal liquidationRatio) {
        this.liquidationRatio = liquidationRatio;
    }

    public String getMarginAccountNo() {
        return marginAccountNo;
    }

    public BigDecimal getNeedMarginRatio() {
        return needMarginRatio;
    }

    public BigDecimal getOpenRate() {
        return openRate;
    }

    public BigDecimal getWarnRatio() {
        return warnRatio;
    }

    public BigDecimal getLiquidationRatio() {
        return liquidationRatio;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.accountNumber);
        dest.writeString(this.settleCurrency);
        dest.writeString(this.marginAccountNo);
        dest.writeSerializable(this.needMarginRatio);
        dest.writeSerializable(this.warnRatio);
        dest.writeSerializable(this.liquidationRatio);
        dest.writeSerializable(this.openRate);
    }

    public VFGBailDetailQueryViewModel() {
    }

    private VFGBailDetailQueryViewModel(Parcel in) {
        this.accountNumber = in.readString();
        this.settleCurrency = in.readString();
        this.marginAccountNo = in.readString();
        this.needMarginRatio = (BigDecimal) in.readSerializable();
        this.warnRatio = (BigDecimal) in.readSerializable();
        this.liquidationRatio = (BigDecimal) in.readSerializable();
        this.openRate = (BigDecimal) in.readSerializable();
    }

    public static final Parcelable.Creator<VFGBailDetailQueryViewModel> CREATOR = new Parcelable.Creator<VFGBailDetailQueryViewModel>() {
        public VFGBailDetailQueryViewModel createFromParcel(Parcel source) {
            return new VFGBailDetailQueryViewModel(source);
        }

        public VFGBailDetailQueryViewModel[] newArray(int size) {
            return new VFGBailDetailQueryViewModel[size];
        }
    };
}
