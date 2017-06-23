package com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.purchase.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.math.BigDecimal;

/**
 * Created by wangtong on 2016/9/30.
 */
public class PurchaseInputMode implements Parcelable {

    //风险评估使用
    public String accountKey;
    //付费账户
    public String payAccountNum ;
    //付费账户id
    public String payAccountId;
    //付费账户余额
    public String payAccountAmount;
    //付费账户类型
    public String payAccountType;
    //付费账户开户行Id
    public String payAccountBancID;
    //付费账户状态
    public String payAccountStatus;
    //产品品质(净值、非净值)
    public String productKind ;
    //产品代码
    public String prodCode ;
    //产品名称
    public String prodName ;
    //币种
    public String curCode ;
    //认购/申购撤单设置
    public String isCanCancle ;
    //购买类型 - 认购申购
    public String transTypeCode;
    //认购手续费（净值型）
    public String subscribeFee ;
    //申购手续费（净值型）
    public String purchFee;
    //认申购起点金额
    public String subAmount ;
    //追加认申购起点金额
    public String addAmount ;
    //购买基数
    public String baseAmount ;
    //购买金额
    public String buyAmount;
    //是否允许指定日期赎回
    public String appdatered;
    //赎回开始日期
    public String redEmptionStartDate ;
    //赎回结束日期
    public String redEmptionEndDate ;
    //是否为业绩基准产品
    public String isLockPeriod;
    //产品期限（业绩基准）
    public String prodTimeLimit;
    //是否为周期性产品
    public boolean periodical ;
    //周期性产品单价
    public String periodPrice ;
    public String priceDate;
    //赎回方式
    public String sellType;
    //赎回周期频率
    public String redEmperiodfReq;
    //赎回周期开始
    public String redEmperiodStart;
    //赎回周期结束
    public String redEmperiodEnd;
    //最大投资期数
    public int maxPeriodNumber;
    //指令上送
    public String dealCode;
    //额度
    public BigDecimal creditBalance;
    // 产品风险类别	String	1：保本固定收益、 2：保本浮动收益、3：非保本浮动收益
    public String prodRiskType;
    //产品类型
    public String productType;
    // 产品期限特性
    public String productTermType;
    //产品起息日
    public String productBegin;
    //产品到期日
    public String productEnd;
    // 付息频率
    public String couponpayFreq;
    // 收益到帐日
    public String interestDate;
    public String yearlyRR;// 预计年收益率（%）
    public String rateDetail;// 预计年收益率（%）(最大值)
    public String redEmptionHoliday;// 允许节假日赎回


    public BigDecimal getCreditBalance() {
        return creditBalance;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.payAccountNum);
        dest.writeString(this.payAccountId);
        dest.writeString(this.payAccountType);
        dest.writeString(this.payAccountBancID);
        dest.writeString(this.payAccountStatus);
        dest.writeString(this.payAccountAmount);
        dest.writeString(this.productKind);
        dest.writeString(this.prodCode);
        dest.writeString(this.prodName);
        dest.writeString(this.curCode);
        dest.writeString(this.isCanCancle);
        dest.writeString(this.transTypeCode);
        dest.writeString(this.subscribeFee);
        dest.writeString(this.purchFee);
        dest.writeString(this.subAmount);
        dest.writeString(this.addAmount);
        dest.writeString(this.baseAmount);
        dest.writeString(this.appdatered);
        dest.writeString(this.redEmptionStartDate);
        dest.writeString(this.redEmptionEndDate);
        dest.writeString(this.redEmptionHoliday);
        dest.writeString(this.isLockPeriod);
        dest.writeString(this.prodTimeLimit);
        dest.writeString(this.dealCode);
        dest.writeString(this.productType);
        dest.writeString(this.productTermType);
        dest.writeString(this.productBegin);
        dest.writeString(this.productEnd);
        dest.writeString(this.couponpayFreq);
        dest.writeString(this.interestDate);

        dest.writeString(this.yearlyRR);
        dest.writeString(this.rateDetail);
        dest.writeString(this.priceDate);

        dest.writeByte(periodical ? (byte) 1 : (byte) 0);
        dest.writeString(this.periodPrice);
        dest.writeString(this.sellType);
        dest.writeString(this.accountKey);
        dest.writeInt(maxPeriodNumber);
        dest.writeSerializable(creditBalance);
    }

    public PurchaseInputMode() {
    }

    protected PurchaseInputMode(Parcel in) {
        this.payAccountNum = in.readString();
        this.payAccountId = in.readString();
        this.payAccountType = in.readString();
        this.payAccountBancID = in.readString();
        this.payAccountStatus = in.readString();
        this.payAccountAmount = in.readString();
        this.productKind = in.readString();
        this.prodCode = in.readString();
        this.prodName = in.readString();
        this.curCode = in.readString();
        this.isCanCancle = in.readString();
        this.transTypeCode = in.readString();
        this.subscribeFee = in.readString();
        this.purchFee = in.readString();
        this.subAmount = in.readString();
        this.addAmount = in.readString();
        this.baseAmount = in.readString();
        this.appdatered = in.readString();
        this.redEmptionStartDate = in.readString();
        this.redEmptionEndDate = in.readString();
        this.redEmptionHoliday = in.readString();
        this.isLockPeriod = in.readString();
        this.prodTimeLimit = in.readString();
        this.periodical = in.readByte() != 0;
        this.periodPrice = in.readString();
        this.sellType = in.readString();
        this.dealCode = in.readString();
        this.accountKey = in.readString();
        this.maxPeriodNumber = in.readInt();
        this.creditBalance = (BigDecimal) in.readSerializable();
        this.productType = in.readString();
        this.productTermType = in.readString();
        this.productBegin = in.readString();
        this.productEnd = in.readString();
        this.couponpayFreq = in.readString();
        this.interestDate = in.readString();
        this.yearlyRR = in.readString();
        this.rateDetail = in.readString();
        this.priceDate = in.readString();
    }

    public static final Parcelable.Creator<PurchaseInputMode> CREATOR = new Parcelable.Creator<PurchaseInputMode>() {
        @Override
        public PurchaseInputMode createFromParcel(Parcel source) {
            return new PurchaseInputMode(source);
        }

        @Override
        public PurchaseInputMode[] newArray(int size) {
            return new PurchaseInputMode[size];
        }
    };
}
