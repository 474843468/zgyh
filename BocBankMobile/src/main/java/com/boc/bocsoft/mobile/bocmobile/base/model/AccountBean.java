package com.boc.bocsoft.mobile.bocmobile.base.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.boc.bocsoft.mobile.bocmobile.base.utils.PinYinUtil;

/**
 * Created by feibin on 2016/6/29.
 * 账户bean 实现序列化
 */
public class AccountBean implements Parcelable {


    public AccountBean() {

    }

    /**
     * f
     * 网银账户标识
     */
    private String accountId;
    /**
     * 账户名称
     */
    private String accountName;
    /**
     * 是否开通电子现金账户功能
     * 1：有
     * 0：无
     */
    private String isECashAccount;
    /**
     * 是否开通医保账户
     * 1：有
     * 0：无
     */
    private String isMedicalAccount;
    /**
     * 账号
     */
    private String accountNumber;
    /**
     * 联行号（所属地区）
     */
    private String accountIbkNum;
    /**
     * 账户类型
     */
    private String accountType;
    /**
     * 所属银行机构标识
     */
    private String branchId;
    /**
     * 账户别名
     */
    private String nickName;
    /**
     * 账户状态
     */
    private String accountStatus;
    /**
     * 客户标识
     */
    private String customerId;
    /**
     * 币种
     * 001:人民币
     * 014:美元
     * 013:港币元
     * 012:英镑
     * 038:欧元
     * 027:日元
     * 028:加拿大元
     * 029:澳大利亚元
     * 015:瑞士法郎
     * 018:新加坡元
     * 081:澳门元
     */
    private String currencyCode;
    /**
     * 币种2
     * 014=美元
     * 027=日元
     */
    private String currencyCode2;
    /**
     * 所属网银机构名称
     */
    private String branchName;
    /**
     * 卡类型描述
     */
    private String cardDescription;
    /**
     * 是否有旧账号
     * 1：有旧账户
     * 0：无旧账户
     */
    private String hasOldAccountFlag;
    /**
     * 信用卡品种代码
     * 1	CCDZ	长城电子支付信用卡(CVV卡)
     * 2	PRMC	中银万事达白金信用卡精英版
     * 3	CBWG	长城北京外企联名卡银联金
     * 4	CBWC	长城北京外企联名卡银联普
     * 5	MBWG	长城北京外企联名卡万事达金
     * 6	MBWC	长城北京外企联名卡万事达普
     * 7	VMBG	中国银行公务卡
     * 8	CHBG	长城交管信用卡银联金卡
     * 9	CCDT	长城车贷通信用卡
     * 10	CXAC	长城缴费通信用卡普卡
     */
    private String cardDescriptionCode;
    /**
     * 验证因子HMAC加密串
     * 微信银行使用，加密数据源组成：userId+# accountName+#+accountNumber+#+accountType
     * 加密盐值：3%4d0R1h4￥1
     */
    private String verifyFactor;
    /**
     * 电子卡账户标识
     * 1：是
     * 0：否
     */
    private String ecard;
    /**
     * 账户类型
     * 1：I类账户
     * 2：II类账户
     * 3：III类账户
     * 空：此值返回不使用
     */
    private String accountCatalog;
    public String getAccountCatalog() {
        return accountCatalog;
    }

    public void setAccountCatalog(String accountCatalog) {
        this.accountCatalog = accountCatalog;
    }
    private String accountNamePinyin;

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getIsECashAccount() {
        return isECashAccount;
    }

    public void setIsECashAccount(String isECashAccount) {
        this.isECashAccount = isECashAccount;
    }

    public String getIsMedicalAccount() {
        return isMedicalAccount;
    }

    public void setIsMedicalAccount(String isMedicalAccount) {
        this.isMedicalAccount = isMedicalAccount;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getAccountIbkNum() {
        return accountIbkNum;
    }

    public void setAccountIbkNum(String accountIbkNum) {
        this.accountIbkNum = accountIbkNum;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getBranchId() {
        return branchId;
    }

    public void setBranchId(String branchId) {
        this.branchId = branchId;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(String accountStatus) {
        this.accountStatus = accountStatus;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getCurrencyCode2() {
        return currencyCode2;
    }

    public void setCurrencyCode2(String currencyCode2) {
        this.currencyCode2 = currencyCode2;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public String getCardDescription() {
        return cardDescription;
    }

    public void setCardDescription(String cardDescription) {
        this.cardDescription = cardDescription;
    }

    public String getHasOldAccountFlag() {
        return hasOldAccountFlag;
    }

    public void setHasOldAccountFlag(String hasOldAccountFlag) {
        this.hasOldAccountFlag = hasOldAccountFlag;
    }

    public String getCardDescriptionCode() {
        return cardDescriptionCode;
    }

    public void setCardDescriptionCode(String cardDescriptionCode) {
        this.cardDescriptionCode = cardDescriptionCode;
    }

    public String getVerifyFactor() {
        return verifyFactor;
    }

    public void setVerifyFactor(String verifyFactor) {
        this.verifyFactor = verifyFactor;
    }

    public String getEcard() {
        return ecard;
    }

    public void setEcard(String ecard) {
        this.ecard = ecard;
    }

    public String getAccountNamePinyin() {
        return accountNamePinyin;
    }

    public void setAccountNamePinyin(String accountNamePinyin) {
        String pinYin = PinYinUtil.getPinYin(this.accountName);
        pinYin = pinYin.trim();
        this.accountNamePinyin = TextUtils.isEmpty(pinYin) ? "z" : pinYin.toUpperCase();
    }

    /**
     * 重写equals与hashCode 使用accountId作为区分AccountBean的唯一标识
     *
     * @param o
     * @return
     */
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null)
            return false;
        if(getClass() != o.getClass() && !getClass().isAssignableFrom(o.getClass()))
            return false;


        AccountBean that = (AccountBean) o;

        return accountId != null ? accountId.equals(that.accountId) : that.accountId == null;

    }

    @Override
    public int hashCode() {
        return accountId != null ? accountId.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "AccountBean{" +
                "accountId=" + accountId +
                ", accountName='" + accountName + '\'' +
                ", isECashAccount='" + isECashAccount + '\'' +
                ", isMedicalAccount='" + isMedicalAccount + '\'' +
                ", accountNumber='" + accountNumber + '\'' +
                ", accountIbkNum='" + accountIbkNum + '\'' +
                ", accountType='" + accountType + '\'' +
                ", branchId=" + branchId +
                ", nickName='" + nickName + '\'' +
                ", accountStatus='" + accountStatus + '\'' +
                ", customerId=" + customerId +
                ", currencyCode='" + currencyCode + '\'' +
                ", currencyCode2='" + currencyCode2 + '\'' +
                ", branchName='" + branchName + '\'' +
                ", cardDescription='" + cardDescription + '\'' +
                ", hasOldAccountFlag='" + hasOldAccountFlag + '\'' +
                ", cardDescriptionCode='" + cardDescriptionCode + '\'' +
                ", verifyFactor='" + verifyFactor + '\'' +
                ", ecard='" + ecard + '\'' +
                ", accountCatalog='" + accountCatalog + '\'' +
                '}';
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.accountId);
        dest.writeString(this.accountName);
        dest.writeString(this.isECashAccount);
        dest.writeString(this.isMedicalAccount);
        dest.writeString(this.accountNumber);
        dest.writeString(this.accountIbkNum);
        dest.writeString(this.accountType);
        dest.writeString(this.branchId);
        dest.writeString(this.nickName);
        dest.writeString(this.accountStatus);
        dest.writeString(this.customerId);
        dest.writeString(this.currencyCode);
        dest.writeString(this.currencyCode2);
        dest.writeString(this.branchName);
        dest.writeString(this.cardDescription);
        dest.writeString(this.hasOldAccountFlag);
        dest.writeString(this.cardDescriptionCode);
        dest.writeString(this.verifyFactor);
        dest.writeString(this.ecard);
        dest.writeString(this.accountCatalog);
        dest.writeString(this.accountNamePinyin);
    }

    private AccountBean(Parcel in) {
        this.accountId = in.readString();
        this.accountName = in.readString();
        this.isECashAccount = in.readString();
        this.isMedicalAccount = in.readString();
        this.accountNumber = in.readString();
        this.accountIbkNum = in.readString();
        this.accountType = in.readString();
        this.branchId = in.readString();
        this.nickName = in.readString();
        this.accountStatus = in.readString();
        this.customerId = in.readString();
        this.currencyCode = in.readString();
        this.currencyCode2 = in.readString();
        this.branchName = in.readString();
        this.cardDescription = in.readString();
        this.hasOldAccountFlag = in.readString();
        this.cardDescriptionCode = in.readString();
        this.verifyFactor = in.readString();
        this.ecard = in.readString();
        this.accountCatalog = in.readString();
        this.accountNamePinyin = in.readString();
    }

    public static final Parcelable.Creator<AccountBean> CREATOR = new Parcelable.Creator<AccountBean>() {
        public AccountBean createFromParcel(Parcel source) {
            return new AccountBean(source);
        }

        public AccountBean[] newArray(int size) {
            return new AccountBean[size];
        }
    };
}
