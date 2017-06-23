package com.boc.bocsoft.mobile.bocmobile.buss.creditcard.myinstallment.model;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;
import com.boc.bocsoft.mobile.bocmobile.base.utils.PublicCodeUtils;
import com.boc.bocsoft.mobile.common.utils.BeanConvertor.anno.NotConvert;
import com.boc.bocsoft.mobile.common.utils.NumberUtils;

import java.math.BigDecimal;

/**
 * Created by yangle on 2016/11/21.
 * 分期历史记录model
 */
public class InstallmentRecordModel implements Parcelable{

    /**
     * 分期日期
     */
    private String instmtDate;
    /**
     * 分期交易描述
     */
    private String instmtDescription;
    /**
     * 分期币种
     */
    private String currency;
    /**
     * 账单分期实际金额
     */
    private BigDecimal amount;
    /**
     *分期期数
     */
    private Integer instmtCount;
    /**
     * 分期完成日期
     */
    private String accomplishDate;
    /**
     * 分期手续费
     */
    private BigDecimal instmtCharge;
    /**
     * 分期手续费收取方式
     * 1---手续费分期支付
     * 0---手续费一次性支付
     */
    private String chargeMode;
    /**
     * 分期后每期应还金额-首期
     */
    private BigDecimal firstInAmount;
    /**
     * 分期后每期应还金额-后每期
     */
    private BigDecimal restPerTimeInAmount;
    /**
     * 已入账期数
     */
    private Integer incomeTimeCount;
    /**
     * 已入账金额
     */
    private BigDecimal incomeAmount;
    /**
     * 本期账单剩余还款金额
     */
    private BigDecimal restAmount;
    /**
     * 下次入账日期
     */
    private String nextIncomeDate;
    /**
     * 分期计划
     */
    private String instalmentPlan;
    /**
     * 信用卡号
     */
    @NotConvert //接口返回是后四位,从首页传过来的accountBean获取
    private String creditCardNum;
    /**
     * 分期付款标识
     * EP01--消费分期
     * BI01--账单分期
     * XJ01--现金分期
     */
    private String instmtFlag;
    /**
     * 下期入账金额
     */
    private BigDecimal nextTimeAmount;
    /**
     * 剩余未入账期数
     */
    private Integer restTimeCount;

    // 账户id
    private String accountId;
    // 会话id
    private String conversationId;
    // 安全因子id
    private String _combinId;

    // 为使用beanConvert 添加 因上送字段是currencyCode
    private String currencyCode;

    public String getCurrencyCode() {
        return currency;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String get_combinId() {
        return _combinId;
    }

    public void set_combinId(String _combinId) {
        this._combinId = _combinId;
    }

    public String getInstmtDate() {
        return instmtDate;
    }

    public void setInstmtDate(String instmtDate) {
        this.instmtDate = instmtDate;
    }

    public String getInstmtDescription() {
        return instmtDescription;
    }

    public void setInstmtDescription(String instmtDescription) {
        this.instmtDescription = instmtDescription;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Integer getInstmtCount() {
        return instmtCount;
    }

    public void setInstmtCount(Integer instmtCount) {
        this.instmtCount = instmtCount;
    }

    public String getAccomplishDate() {
        return accomplishDate;
    }

    public void setAccomplishDate(String accomplishDate) {
        this.accomplishDate = accomplishDate;
    }

    public BigDecimal getInstmtCharge() {
        return instmtCharge;
    }

    public void setInstmtCharge(BigDecimal instmtCharge) {
        this.instmtCharge = instmtCharge;
    }

    public String getChargeMode() {
        return chargeMode;
    }

    public void setChargeMode(String chargeMode) {
        this.chargeMode = chargeMode;
    }

    public BigDecimal getFirstInAmount() {
        return firstInAmount;
    }

    public void setFirstInAmount(BigDecimal firstInAmount) {
        this.firstInAmount = firstInAmount;
    }

    public BigDecimal getRestPerTimeInAmount() {
        return restPerTimeInAmount;
    }

    public void setRestPerTimeInAmount(BigDecimal restPerTimeInAmount) {
        this.restPerTimeInAmount = restPerTimeInAmount;
    }

    public Integer getIncomeTimeCount() {
        return incomeTimeCount;
    }

    public void setIncomeTimeCount(Integer incomeTimeCount) {
        this.incomeTimeCount = incomeTimeCount;
    }

    public BigDecimal getIncomeAmount() {
        return incomeAmount;
    }

    public void setIncomeAmount(BigDecimal incomeAmount) {
        this.incomeAmount = incomeAmount;
    }

    public BigDecimal getRestAmount() {
        return restAmount;
    }

    public void setRestAmount(BigDecimal restAmount) {
        this.restAmount = restAmount;
    }

    public String getNextIncomeDate() {
        return nextIncomeDate;
    }

    public void setNextIncomeDate(String nextIncomeDate) {
        this.nextIncomeDate = nextIncomeDate;
    }

    public String getInstalmentPlan() {
        return instalmentPlan;
    }

    public void setInstalmentPlan(String instalmentPlan) {
        this.instalmentPlan = instalmentPlan;
    }

    public String getCreditCardNum() {
        return creditCardNum;
    }

    public void setCreditCardNum(String creditCardNum) {
        this.creditCardNum = creditCardNum;
    }

    public String getInstmtFlag() {
        return instmtFlag;
    }

    public void setInstmtFlag(String instmtFlag) {
        this.instmtFlag = instmtFlag;
    }

    public BigDecimal getNextTimeAmount() {
        return nextTimeAmount;
    }

    public void setNextTimeAmount(BigDecimal nextTimeAmount) {
        this.nextTimeAmount = nextTimeAmount;
    }

    public Integer getRestTimeCount() {
        return restTimeCount;
    }

    public void setRestTimeCount(Integer restTimeCount) {
        this.restTimeCount = restTimeCount;
    }

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    /***********************辅助转换方法*********************************/
    // 分期类型(由flag区分)
    public String getInstmtCategory(){
        switch (instmtFlag) {
            case "EP01":
                return "消费分期";
            case "BI01":
                return "账单分期";
            case "XJ01":
                return "现金分期";
            default:
                return "其它记录";
        }
    }

    public boolean isAccomplished() {
        return !TextUtils.isEmpty(accomplishDate);
    }
    // 是否提前结清
    public boolean isAdvanceAccomplished() {
        return isAccomplished() && incomeTimeCount != instmtCount;
    }

    public String getChargeModeName() {
        if ("0".equals(chargeMode)) {
            return "一次性支付";
        } else if ("1".equals(chargeMode)) {
            return "分期支付";
        } else {
            return "unKnown change mode ";
        }
    }

    public String getInstmtCountStr() {
        return String.valueOf(instmtCount);
    }

    public String getFirstInAmountMoneyFormat() {
        return MoneyUtils.transMoneyFormat(firstInAmount, currency);
    }

    public String getAmountMoneyFormat() {
        return MoneyUtils.transMoneyFormat(amount, currency);
    }

    public String getIncomeAmountStr() {
        return String.valueOf(incomeAmount);
    }

    public String getCreditCardNumStr() {
        return NumberUtils.formatCardNumberStrong(creditCardNum);
    }

    public String getNextTimeAmountMoneyFormat() {
        return MoneyUtils.transMoneyFormat(nextTimeAmount, currency);
    }

    public String getIncomeTimeCountStr() {
        return String.valueOf(incomeTimeCount);
    }

    public String getIncomeAmountMoneyFormat() {
        return MoneyUtils.transMoneyFormat(incomeAmount, currency);
    }

    public String getRestTimeCountStr() {
        return String.valueOf(restTimeCount);
    }

    public String getRestAmountMoneyFormat() {
        return MoneyUtils.transMoneyFormat(restAmount, currency);
    }

    public String getRestAmountStr() {
        return String.valueOf(restAmount);
    }

    public String getCurrencyName(Context context) {
        return PublicCodeUtils.getCurrency(context, currency);
    }

    /*******************************************************/

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.instmtDate);
        dest.writeString(this.instmtDescription);
        dest.writeString(this.currency);
        dest.writeSerializable(this.amount);
        dest.writeValue(this.instmtCount);
        dest.writeString(this.accomplishDate);
        dest.writeSerializable(this.instmtCharge);
        dest.writeString(this.chargeMode);
        dest.writeSerializable(this.firstInAmount);
        dest.writeSerializable(this.restPerTimeInAmount);
        dest.writeValue(this.incomeTimeCount);
        dest.writeSerializable(this.incomeAmount);
        dest.writeSerializable(this.restAmount);
        dest.writeString(this.nextIncomeDate);
        dest.writeString(this.instalmentPlan);
        dest.writeString(this.creditCardNum);
        dest.writeString(this.instmtFlag);
        dest.writeSerializable(this.nextTimeAmount);
        dest.writeValue(this.restTimeCount);
        dest.writeString(this.accountId);
        dest.writeString(this.conversationId);
        dest.writeString(this._combinId);
    }

    public InstallmentRecordModel() {
    }

    protected InstallmentRecordModel(Parcel in) {
        this.instmtDate = in.readString();
        this.instmtDescription = in.readString();
        this.currency = in.readString();
        this.amount = (BigDecimal) in.readSerializable();
        this.instmtCount = (Integer) in.readValue(Integer.class.getClassLoader());
        this.accomplishDate = in.readString();
        this.instmtCharge = (BigDecimal) in.readSerializable();
        this.chargeMode = in.readString();
        this.firstInAmount = (BigDecimal) in.readSerializable();
        this.restPerTimeInAmount = (BigDecimal) in.readSerializable();
        this.incomeTimeCount = (Integer) in.readValue(Integer.class.getClassLoader());
        this.incomeAmount = (BigDecimal) in.readSerializable();
        this.restAmount = (BigDecimal) in.readSerializable();
        this.nextIncomeDate = in.readString();
        this.instalmentPlan = in.readString();
        this.creditCardNum = in.readString();
        this.instmtFlag = in.readString();
        this.nextTimeAmount = (BigDecimal) in.readSerializable();
        this.restTimeCount = (Integer) in.readValue(Integer.class.getClassLoader());
        this.accountId = in.readString();
        this.conversationId = in.readString();
        this._combinId = in.readString();
    }

    public static final Creator<InstallmentRecordModel> CREATOR = new Creator<InstallmentRecordModel>() {
        @Override
        public InstallmentRecordModel createFromParcel(Parcel source) {
            return new InstallmentRecordModel(source);
        }

        @Override
        public InstallmentRecordModel[] newArray(int size) {
            return new InstallmentRecordModel[size];
        }
    };

}
