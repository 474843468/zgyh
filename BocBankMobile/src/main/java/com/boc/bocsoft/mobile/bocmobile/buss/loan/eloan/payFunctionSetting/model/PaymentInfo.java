package com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.payFunctionSetting.model;

import android.os.Parcel;
import android.os.Parcelable;
import java.math.BigDecimal;
import org.threeten.bp.LocalDate;

/**
 * 作者：XieDu
 * 创建时间：2016/9/5 16:11
 * 描述：
 */
public class PaymentInfo implements Parcelable {
    /**
     * 支付签约借记卡号
     * 如果该贷款账户未签约支付，则此字段为空，前端据此判断并做相应展现
     */
    private String signAccountNum;
    /**
     * 支付签约最小放款金额
     */
    private BigDecimal minLoanAmount;
    /**
     * 支付签约最小放款金额的币种
     * 国际化编码，例如人民币CNY
     */
    private String minLoanAmtCur;
    /**
     * 用款偏好
     * 贷款：BOR
     * 存款：DEP
     */
    private String usePref;
    /**
     * 期限
     */
    private String signPeriod;
    /**
     * 还款方式
     */
    private String payType;
    /**
     * 签约日期
     * yyyy/MM/dd
     */
    private LocalDate signDate;
    /**
     * 支付签约日期
     */
    private LocalDate paySignDate;
    /**
     * 支付解约日期
     */
    private LocalDate payUnsignDate;

    public String getSignAccountNum() {
        return signAccountNum;
    }

    public void setSignAccountNum(String signAccountNum) {
        this.signAccountNum = signAccountNum;
    }

    public BigDecimal getMinLoanAmount() {
        return minLoanAmount;
    }

    public void setMinLoanAmount(BigDecimal minLoanAmount) {
        this.minLoanAmount = minLoanAmount;
    }

    public String getMinLoanAmtCur() {
        return minLoanAmtCur;
    }

    public void setMinLoanAmtCur(String minLoanAmtCur) {
        this.minLoanAmtCur = minLoanAmtCur;
    }

    public String getUsePref() {
        return usePref;
    }

    public void setUsePref(String usePref) {
        this.usePref = usePref;
    }

    public String getSignPeriod() {
        return signPeriod;
    }

    public void setSignPeriod(String signPeriod) {
        this.signPeriod = signPeriod;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public LocalDate getSignDate() {
        return signDate;
    }

    public void setSignDate(LocalDate signDate) {
        this.signDate = signDate;
    }

    public LocalDate getPaySignDate() {
        return paySignDate;
    }

    public void setPaySignDate(LocalDate paySignDate) {
        this.paySignDate = paySignDate;
    }

    public LocalDate getPayUnsignDate() {
        return payUnsignDate;
    }

    public void setPayUnsignDate(LocalDate payUnsignDate) {
        this.payUnsignDate = payUnsignDate;
    }

    @Override
    public int describeContents() { return 0; }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.signAccountNum);
        dest.writeSerializable(this.minLoanAmount);
        dest.writeString(this.minLoanAmtCur);
        dest.writeString(this.usePref);
        dest.writeString(this.signPeriod);
        dest.writeString(this.payType);
        dest.writeSerializable(this.signDate);
        dest.writeSerializable(this.paySignDate);
        dest.writeSerializable(this.payUnsignDate);
    }

    public PaymentInfo() {}

    protected PaymentInfo(Parcel in) {
        this.signAccountNum = in.readString();
        this.minLoanAmount = (BigDecimal) in.readSerializable();
        this.minLoanAmtCur = in.readString();
        this.usePref = in.readString();
        this.signPeriod = in.readString();
        this.payType = in.readString();
        this.signDate = (LocalDate) in.readSerializable();
        this.paySignDate = (LocalDate) in.readSerializable();
        this.payUnsignDate = (LocalDate) in.readSerializable();
    }

    public static final Parcelable.Creator<PaymentInfo> CREATOR =
            new Parcelable.Creator<PaymentInfo>() {
                @Override
                public PaymentInfo createFromParcel(Parcel source) {return new PaymentInfo(source);}

                @Override
                public PaymentInfo[] newArray(int size) {return new PaymentInfo[size];}
            };
}
