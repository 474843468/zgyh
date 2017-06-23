package com.boc.bocsoft.mobile.bocmobile.buss.loan.pledgeloan.model.finance;

import android.os.Parcel;
import android.os.Parcelable;
import org.threeten.bp.LocalDate;

/**
 * Created by XieDu on 2016/8/2.
 */
public class PledgeProductBean implements Parcelable {
    /**
     * 资金账号		加星显示
     */
    private String bancAccount;
    /**
     * 产品代码
     */
    private String prodCode;
    /**
     * 产品名称
     */
    private String prodName;
    /**
     * 产品币种
     */
    private String curCode;
    /**
     * 产品起息日
     */
    private LocalDate prodBegin;
    /**
     * 产品到期日
     * 无限开放式产品返回“-1”表示“无限期”
     */
    private LocalDate prodEnd;
    /**
     * 持有份额
     */
    private String holdingQuantity;
    /**
     * 可用份额
     */
    private String availableQuantity;
    /**
     * 可贷款金额
     */
    private String availableLoanAmt;
    /**
     * 产品风险级别
     * 0：低风险产品
     * 1：中低风险产品
     * 2：中等风险产品
     * 3：中高风险产品
     * 4：高风险产品
     */
    private String prodRisk;
    /**
     * 质押率
     */
    private String pledgeRate;

    public String getBancAccount() {
        return bancAccount;
    }

    public void setBancAccount(String bancAccount) {
        this.bancAccount = bancAccount;
    }

    public String getProdCode() {
        return prodCode;
    }

    public void setProdCode(String prodCode) {
        this.prodCode = prodCode;
    }

    public String getProdName() {
        return prodName;
    }

    public void setProdName(String prodName) {
        this.prodName = prodName;
    }

    public String getCurCode() {
        return curCode;
    }

    public void setCurCode(String curCode) {
        this.curCode = curCode;
    }

    public LocalDate getProdBegin() {
        return prodBegin;
    }

    public void setProdBegin(LocalDate prodBegin) {
        this.prodBegin = prodBegin;
    }

    public LocalDate getProdEnd() {
        return prodEnd;
    }

    public void setProdEnd(LocalDate prodEnd) {
        this.prodEnd = prodEnd;
    }

    public String getHoldingQuantity() {
        return holdingQuantity;
    }

    public void setHoldingQuantity(String holdingQuantity) {
        this.holdingQuantity = holdingQuantity;
    }

    public String getAvailableQuantity() {
        return availableQuantity;
    }

    public void setAvailableQuantity(String availableQuantity) {
        this.availableQuantity = availableQuantity;
    }

    public String getAvailableLoanAmt() {
        return availableLoanAmt;
    }

    public void setAvailableLoanAmt(String availableLoanAmt) {
        this.availableLoanAmt = availableLoanAmt;
    }

    public String getProdRisk() {
        return prodRisk;
    }

    public void setProdRisk(String prodRisk) {
        this.prodRisk = prodRisk;
    }

    public String getPledgeRate() {
        return pledgeRate;
    }

    public void setPledgeRate(String pledgeRate) {
        this.pledgeRate = pledgeRate;
    }

    @Override
    public int describeContents() { return 0; }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.bancAccount);
        dest.writeString(this.prodCode);
        dest.writeString(this.prodName);
        dest.writeString(this.curCode);
        dest.writeSerializable(this.prodBegin);
        dest.writeSerializable(this.prodEnd);
        dest.writeString(this.holdingQuantity);
        dest.writeString(this.availableQuantity);
        dest.writeString(this.availableLoanAmt);
        dest.writeString(this.prodRisk);
        dest.writeString(this.pledgeRate);
    }

    public PledgeProductBean() {}

    protected PledgeProductBean(Parcel in) {
        this.bancAccount = in.readString();
        this.prodCode = in.readString();
        this.prodName = in.readString();
        this.curCode = in.readString();
        this.prodBegin = (LocalDate) in.readSerializable();
        this.prodEnd = (LocalDate) in.readSerializable();
        this.holdingQuantity = in.readString();
        this.availableQuantity = in.readString();
        this.availableLoanAmt = in.readString();
        this.prodRisk = in.readString();
        this.pledgeRate = in.readString();
    }

    public static final Creator<PledgeProductBean> CREATOR = new Creator<PledgeProductBean>() {
        @Override
        public PledgeProductBean createFromParcel(Parcel source) {
            return new PledgeProductBean(source);
        }

        @Override
        public PledgeProductBean[] newArray(int size) {return new PledgeProductBean[size];}
    };
}
