package com.boc.bocsoft.mobile.bocmobile.buss.creditcard.immediaterepayment.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.math.BigDecimal;

/**
 * 作者：xwg on 16/11/25 17:38
 */
public class TransCommissionChargeBean implements Parcelable{
    /**
     * needCommissionCharge : 20  应收费用 基准费用
     * preCommissionCharge : 20 拟收费用  优惠后
     * remitSetMealFlag :   是否签约汇款笔数套餐标识位  “1”--已签约  “0”--未签约
     * getChargeFlag :  试费查询是否成功  “1”-成功，“0”-失败
     */

    private BigDecimal needCommissionCharge;
    private BigDecimal preCommissionCharge;
    private String remitSetMealFlag;
    private String getChargeFlag;

    public TransCommissionChargeBean() {
    }

    protected TransCommissionChargeBean(Parcel in) {
        remitSetMealFlag = in.readString();
        getChargeFlag = in.readString();
    }

    public static final Creator<TransCommissionChargeBean> CREATOR = new Creator<TransCommissionChargeBean>() {
        @Override
        public TransCommissionChargeBean createFromParcel(Parcel in) {
            return new TransCommissionChargeBean(in);
        }

        @Override
        public TransCommissionChargeBean[] newArray(int size) {
            return new TransCommissionChargeBean[size];
        }
    };

    public BigDecimal getNeedCommissionCharge() {
        return needCommissionCharge;
    }

    public void setNeedCommissionCharge(BigDecimal needCommissionCharge) {
        this.needCommissionCharge = needCommissionCharge;
    }

    public BigDecimal getPreCommissionCharge() {
        return preCommissionCharge;
    }

    public void setPreCommissionCharge(BigDecimal preCommissionCharge) {
        this.preCommissionCharge = preCommissionCharge;
    }

    public String getRemitSetMealFlag() {
        return remitSetMealFlag;
    }

    public void setRemitSetMealFlag(String remitSetMealFlag) {
        this.remitSetMealFlag = remitSetMealFlag;
    }

    public String getGetChargeFlag() {
        return getChargeFlag;
    }

    public void setGetChargeFlag(String getChargeFlag) {
        this.getChargeFlag = getChargeFlag;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(remitSetMealFlag);
        dest.writeString(getChargeFlag);
    }
}
