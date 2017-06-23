package com.boc.bocsoft.mobile.bocmobile.buss.loan.pledgeloan.model.deposit;

import android.os.Parcel;
import android.os.Parcelable;
import java.math.BigDecimal;
import org.threeten.bp.LocalDate;

/**
 * Created by XieDu on 2016/8/2.
 */
public class PersonalTimeAccountBean implements Parcelable {
    /**
     * 币种
     * 001=人民币元014=美元027=日元
     */
    private String currencyCode;
    /**
     * 钞汇标识
     * 01=现钞02=现汇
     */
    private String cashRemit;
    /**
     * 当前余额
     */
    private BigDecimal bookBalance;
    /**
     * 可用余额
     */
    private BigDecimal availableBalance;
    /**
     * 存折册号
     */
    private String volumeNumber;
    /**
     * 存单类型
     * 140=存本取息
     * 110=整存整取
     * 150=零存整取
     * 160=定活两便
     * 166=通知存款
     * 152=教育储蓄
     * 912=零存整取
     * 935=教育储蓄
     * 913=存本取息
     */
    private String type;
    /**
     * 利率
     */
    private String interestRate;
    /**
     * 子账户状态
     */
    private String status;
    /**
     * 月存、支金额
     */
    private BigDecimal monthBalance;
    /**
     * 存单号
     */
    private String cdNumber;
    /**
     * 存期
     * 1-1天通知存款
     * 7-7天通知存款
     * 01- 1个月
     * 03- 3个月
     * 06- 6个月
     * 12- 1年
     * 24- 2年
     * 36- 3年
     * 60- 5年
     * 72- 6年
     * 00- 不固定期限
     */
    private String cdPeriod;
    /**
     * 开户日期
     */
    private LocalDate openDate;
    /**
     * 起息日
     */
    private LocalDate interestStartsDate;
    /**
     * 到息日
     */
    private LocalDate interestEndDate;
    /**
     * 结清日期
     */
    private LocalDate settlementDate;
    /**
     * 冻结金额
     */
    private BigDecimal holdAmount;
    /**
     * 自动转存标识
     * R=自动转存
     * N=非自动转存
     */
    private String convertType;

    /**
     * 凭证号码
     * 存款质押贷款功能，查询存单时使用该字段
     */
    private String pingNo;

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getCashRemit() {
        return cashRemit;
    }

    public void setCashRemit(String cashRemit) {
        this.cashRemit = cashRemit;
    }

    public BigDecimal getBookBalance() {
        return bookBalance;
    }

    public void setBookBalance(BigDecimal bookBalance) {
        this.bookBalance = bookBalance;
    }

    public BigDecimal getAvailableBalance() {
        return availableBalance;
    }

    public void setAvailableBalance(BigDecimal availableBalance) {
        this.availableBalance = availableBalance;
    }

    public String getVolumeNumber() {
        return volumeNumber;
    }

    public void setVolumeNumber(String volumeNumber) {
        this.volumeNumber = volumeNumber;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(String interestRate) {
        this.interestRate = interestRate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public BigDecimal getMonthBalance() {
        return monthBalance;
    }

    public void setMonthBalance(BigDecimal monthBalance) {
        this.monthBalance = monthBalance;
    }

    public String getCdNumber() {
        return cdNumber;
    }

    public void setCdNumber(String cdNumber) {
        this.cdNumber = cdNumber;
    }

    public String getCdPeriod() {
        return cdPeriod;
    }

    public void setCdPeriod(String cdPeriod) {
        this.cdPeriod = cdPeriod;
    }

    public LocalDate getOpenDate() {
        return openDate;
    }

    public void setOpenDate(LocalDate openDate) {
        this.openDate = openDate;
    }

    public LocalDate getInterestStartsDate() {
        return interestStartsDate;
    }

    public void setInterestStartsDate(LocalDate interestStartsDate) {
        this.interestStartsDate = interestStartsDate;
    }

    public LocalDate getInterestEndDate() {
        return interestEndDate;
    }

    public void setInterestEndDate(LocalDate interestEndDate) {
        this.interestEndDate = interestEndDate;
    }

    public LocalDate getSettlementDate() {
        return settlementDate;
    }

    public void setSettlementDate(LocalDate settlementDate) {
        this.settlementDate = settlementDate;
    }

    public BigDecimal getHoldAmount() {
        return holdAmount;
    }

    public void setHoldAmount(BigDecimal holdAmount) {
        this.holdAmount = holdAmount;
    }

    public String getConvertType() {
        return convertType;
    }

    public void setConvertType(String convertType) {
        this.convertType = convertType;
    }

    public String getPingNo() {
        return pingNo;
    }

    public void setPingNo(String pingNo) {
        this.pingNo = pingNo;
    }

    @Override
    public int describeContents() { return 0; }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.currencyCode);
        dest.writeString(this.cashRemit);
        dest.writeSerializable(this.bookBalance);
        dest.writeSerializable(this.availableBalance);
        dest.writeString(this.volumeNumber);
        dest.writeString(this.type);
        dest.writeString(this.interestRate);
        dest.writeString(this.status);
        dest.writeSerializable(this.monthBalance);
        dest.writeString(this.cdNumber);
        dest.writeString(this.cdPeriod);
        dest.writeSerializable(this.openDate);
        dest.writeSerializable(this.interestStartsDate);
        dest.writeSerializable(this.interestEndDate);
        dest.writeSerializable(this.settlementDate);
        dest.writeSerializable(this.holdAmount);
        dest.writeString(this.convertType);
        dest.writeString(this.pingNo);
    }

    public PersonalTimeAccountBean() {}

    protected PersonalTimeAccountBean(Parcel in) {
        this.currencyCode = in.readString();
        this.cashRemit = in.readString();
        this.bookBalance = (BigDecimal) in.readSerializable();
        this.availableBalance = (BigDecimal) in.readSerializable();
        this.volumeNumber = in.readString();
        this.type = in.readString();
        this.interestRate = in.readString();
        this.status = in.readString();
        this.monthBalance = (BigDecimal) in.readSerializable();
        this.cdNumber = in.readString();
        this.cdPeriod = in.readString();
        this.openDate = (LocalDate) in.readSerializable();
        this.interestStartsDate = (LocalDate) in.readSerializable();
        this.interestEndDate = (LocalDate) in.readSerializable();
        this.settlementDate = (LocalDate) in.readSerializable();
        this.holdAmount = (BigDecimal) in.readSerializable();
        this.convertType = in.readString();
        this.pingNo = in.readString();
    }

    public static final Creator<PersonalTimeAccountBean> CREATOR =
            new Creator<PersonalTimeAccountBean>() {
                @Override
                public PersonalTimeAccountBean createFromParcel(
                        Parcel source) {return new PersonalTimeAccountBean(source);}

                @Override
                public PersonalTimeAccountBean[] newArray(
                        int size) {return new PersonalTimeAccountBean[size];}
            };
}
