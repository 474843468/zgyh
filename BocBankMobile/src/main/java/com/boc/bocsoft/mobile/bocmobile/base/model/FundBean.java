package com.boc.bocsoft.mobile.bocmobile.base.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.math.BigDecimal;

/**
 * 基金基础bean
 * Created by lxw on 2016/8/3 0003.
 */
public class FundBean implements Parcelable {

    // 币种
    private String currency;
    // 基金代码
    private String fundCode;
    // 基金名称
    private String fundName;
    // 基金状态
    private String fundState;
    // 收费方式
    private String feeType;
    // 基金公司名称
    private String fundCompanyName;
    // 基金净值
    private BigDecimal netPrice;
    // 日增长率
    private BigDecimal dayIncomeRatio;
    // 净值截至日期
    private String endDate;
    // 年收益率/净值增长率(%)
    private BigDecimal fundIncomeRatio;
    // 每万份基金单位收益
    private BigDecimal fundIncomeUnit;
    // 首次认购下限
    private BigDecimal orderLowLimit;
    // 个人申购最低金额
    private BigDecimal applyLowLimit;

    // 七日年化收益率
    private String sevenDayYield;
    //
    private String chargeRate;
    // 是否可认购
    private String isBuy;
    // 是否可申购
    private String isInvt;
    // 0：加载中 1：正确返回 2：请求失败
    private String refreshState = "0";

    /**
     * 产品类型
     01：理财型基金
     02：QDII基金
     03：ETF基金
     04：保本型基金
     05：指数型基金
     06：货币型基金
     07：股票型基金
     08：债券型基金
     09：混合型基金
     10：其他基金
     */
    private String fntype;


    public FundBean(){

    }

    protected FundBean(Parcel in) {
        currency = in.readString();
        fundCode = in.readString();
        fundName = in.readString();
        fundState = in.readString();
        feeType = in.readString();
        fundCompanyName = in.readString();
        endDate = in.readString();
        sevenDayYield = in.readString();
        chargeRate = in.readString();
        isBuy = in.readString();
        isInvt = in.readString();
        refreshState = in.readString();
        fntype = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(currency);
        dest.writeString(fundCode);
        dest.writeString(fundName);
        dest.writeString(fundState);
        dest.writeString(feeType);
        dest.writeString(fundCompanyName);
        dest.writeString(endDate);
        dest.writeString(sevenDayYield);
        dest.writeString(chargeRate);
        dest.writeString(isBuy);
        dest.writeString(isInvt);
        dest.writeString(refreshState);
        dest.writeString(fntype);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<FundBean> CREATOR = new Creator<FundBean>() {
        @Override
        public FundBean createFromParcel(Parcel in) {
            return new FundBean(in);
        }

        @Override
        public FundBean[] newArray(int size) {
            return new FundBean[size];
        }
    };

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getFundCode() {
        return fundCode;
    }

    public void setFundCode(String fundCode) {
        this.fundCode = fundCode;
    }

    public String getFundName() {
        return fundName;
    }

    public void setFundName(String fundName) {
        this.fundName = fundName;
    }

    public String getFundState() {
        return fundState;
    }

    public void setFundState(String fundState) {
        this.fundState = fundState;
    }

    public String getFeeType() {
        return feeType;
    }

    public void setFeeType(String feeType) {
        this.feeType = feeType;
    }

    public String getFundCompanyName() {
        return fundCompanyName;
    }

    public void setFundCompanyName(String fundCompanyName) {
        this.fundCompanyName = fundCompanyName;
    }

    public BigDecimal getNetPrice() {
        return netPrice;
    }

    public void setNetPrice(BigDecimal netPrice) {
        this.netPrice = netPrice;
    }

    public BigDecimal getDayIncomeRatio() {
        return dayIncomeRatio;
    }

    public void setDayIncomeRatio(BigDecimal dayIncomeRatio) {
        this.dayIncomeRatio = dayIncomeRatio;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public BigDecimal getFundIncomeRatio() {
        return fundIncomeRatio;
    }

    public void setFundIncomeRatio(BigDecimal fundIncomeRatio) {
        this.fundIncomeRatio = fundIncomeRatio;
    }

    public BigDecimal getFundIncomeUnit() {
        return fundIncomeUnit;
    }

    public void setFundIncomeUnit(BigDecimal fundIncomeUnit) {
        this.fundIncomeUnit = fundIncomeUnit;
    }

    public String getFntype() {
        return fntype;
    }

    public void setFntype(String fntype) {
        this.fntype = fntype;
    }

    public BigDecimal getOrderLowLimit() {
        return orderLowLimit;
    }

    public void setOrderLowLimit(BigDecimal orderLowLimit) {
        this.orderLowLimit = orderLowLimit;
    }

    public String getRefreshState() {
        return refreshState;
    }

    public void setRefreshState(String refreshState) {
        this.refreshState = refreshState;
    }

    public String getChargeRate() {
        return chargeRate;
    }

    public void setChargeRate(String chargeRate) {
        this.chargeRate = chargeRate;
    }

    public BigDecimal getApplyLowLimit() {
        return applyLowLimit;
    }

    public void setApplyLowLimit(BigDecimal applyLowLimit) {
        this.applyLowLimit = applyLowLimit;
    }

    public String getIsBuy() {
        return isBuy;
    }

    public void setIsBuy(String isBuy) {
        this.isBuy = isBuy;
    }

    public String getIsInvt() {
        return isInvt;
    }

    public void setIsInvt(String isInvt) {
        this.isInvt = isInvt;
    }

    public String getSevenDayYield() {
        return sevenDayYield;
    }

    public void setSevenDayYield(String sevenDayYield) {
        this.sevenDayYield = sevenDayYield;
    }
}
