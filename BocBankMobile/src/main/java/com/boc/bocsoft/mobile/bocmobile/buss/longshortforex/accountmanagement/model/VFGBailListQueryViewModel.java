package com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.accountmanagement.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * ViewModel：查询保证金账户列表
 * Created by zhx on 2016/11/21
 */
public class VFGBailListQueryViewModel {
    private List<BailItemEntity> entityList = new ArrayList<BailItemEntity>();

    public List<BailItemEntity> getEntityList() {
        return entityList;
    }

    public void setEntityList(List<BailItemEntity> entityList) {
        this.entityList = entityList;
    }

    public static class BailItemEntity implements Comparable<BailItemEntity>,Parcelable {
        // 借记卡卡号
        private String accountNumber;
        // 账户别名
        private String nickName;
        // 保证金账号
        private String marginAccountNo;
        // 结算货币
        private String settleCurrency;
        // 子账户类别
        private String subAcctType;
        // 签约时间
        private String signDate;
        private boolean isCurrentAccount = false; // 是否是当前签约账户下的保证金账户
        private boolean isChecked = false;

        ///////////
        // 保证金帐号
//        private String marginAccountNo;
        // 结算货币(014美元 001人民币 038欧元 013港币 027日元 029澳元)
        private SettleCurrencyEntity settleCurrency2;
        // 账户净值
        private BigDecimal marginNetBalance;
        // 账户余额
        private BigDecimal marginFund;
        // 暂计盈亏标志
        private String profitLossFlag;
        // 账户暂计盈亏
        private BigDecimal currentProfitLoss;
        // 开仓头寸
        private BigDecimal openPosition;
        // 已占用保证金
        private BigDecimal marginOccupied;
        // 可用保证金
        private BigDecimal marginAvailable;
        // 最大可交易额
        private BigDecimal maxTradeAmount;
        // 最大可提取额
        private BigDecimal maxDrawAmount;
        // 保证金充足率
        private BigDecimal marginRate;
        // 是否已经进入报警区
        private String alarmFlag;
        // 资金钞余额
        private BigDecimal cashBanlance;
        // 资金汇余额
        private BigDecimal remitBanlance;

        public String getAccountNumber() {
            return accountNumber;
        }

        public void setAccountNumber(String accountNumber) {
            this.accountNumber = accountNumber;
        }

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

        public String getMarginAccountNo() {
            return marginAccountNo;
        }

        public void setMarginAccountNo(String marginAccountNo) {
            this.marginAccountNo = marginAccountNo;
        }

        public String getSettleCurrency() {
            return settleCurrency;
        }

        public void setSettleCurrency(String settleCurrency) {
            this.settleCurrency = settleCurrency;
        }

        public String getSubAcctType() {
            return subAcctType;
        }

        public void setSubAcctType(String subAcctType) {
            this.subAcctType = subAcctType;
        }

        public String getSignDate() {
            return signDate;
        }

        public void setSignDate(String signDate) {
            this.signDate = signDate;
        }

        public boolean isCurrentAccount() {
            return isCurrentAccount;
        }

        public void setCurrentAccount(boolean currentAccount) {
            isCurrentAccount = currentAccount;
        }

        public SettleCurrencyEntity getSettleCurrency2() {
            return settleCurrency2;
        }

        public void setSettleCurrency2(SettleCurrencyEntity settleCurrency2) {
            this.settleCurrency2 = settleCurrency2;
        }

        public BigDecimal getMarginNetBalance() {
            return marginNetBalance;
        }

        public void setMarginNetBalance(BigDecimal marginNetBalance) {
            this.marginNetBalance = marginNetBalance;
        }

        public BigDecimal getMarginFund() {
            return marginFund;
        }

        public void setMarginFund(BigDecimal marginFund) {
            this.marginFund = marginFund;
        }

        public String getProfitLossFlag() {
            return profitLossFlag;
        }

        public void setProfitLossFlag(String profitLossFlag) {
            this.profitLossFlag = profitLossFlag;
        }

        public BigDecimal getCurrentProfitLoss() {
            return currentProfitLoss;
        }

        public void setCurrentProfitLoss(BigDecimal currentProfitLoss) {
            this.currentProfitLoss = currentProfitLoss;
        }

        public BigDecimal getOpenPosition() {
            return openPosition;
        }

        public void setOpenPosition(BigDecimal openPosition) {
            this.openPosition = openPosition;
        }

        public BigDecimal getMarginOccupied() {
            return marginOccupied;
        }

        public void setMarginOccupied(BigDecimal marginOccupied) {
            this.marginOccupied = marginOccupied;
        }

        public BigDecimal getMarginAvailable() {
            return marginAvailable;
        }

        public void setMarginAvailable(BigDecimal marginAvailable) {
            this.marginAvailable = marginAvailable;
        }

        public BigDecimal getMaxTradeAmount() {
            return maxTradeAmount;
        }

        public void setMaxTradeAmount(BigDecimal maxTradeAmount) {
            this.maxTradeAmount = maxTradeAmount;
        }

        public BigDecimal getMaxDrawAmount() {
            return maxDrawAmount;
        }

        public void setMaxDrawAmount(BigDecimal maxDrawAmount) {
            this.maxDrawAmount = maxDrawAmount;
        }

        public BigDecimal getMarginRate() {
            return marginRate;
        }

        public void setMarginRate(BigDecimal marginRate) {
            this.marginRate = marginRate;
        }

        public String getAlarmFlag() {
            return alarmFlag;
        }

        public void setAlarmFlag(String alarmFlag) {
            this.alarmFlag = alarmFlag;
        }

        public BigDecimal getCashBanlance() {
            return cashBanlance;
        }

        public void setCashBanlance(BigDecimal cashBanlance) {
            this.cashBanlance = cashBanlance;
        }

        public BigDecimal getRemitBanlance() {
            return remitBanlance;
        }

        public void setRemitBanlance(BigDecimal remitBanlance) {
            this.remitBanlance = remitBanlance;
        }

        public boolean isChecked() {
            return isChecked;
        }

        public void setChecked(boolean checked) {
            isChecked = checked;
        }

        @Override
        public int compareTo(BailItemEntity another) {
            return this.accountNumber.compareTo(another.accountNumber);
        }

        public static class SettleCurrencyEntity implements Parcelable {
            /**
             * fraction : null
             * code : 014
             * i18nId : null
             */
            private String fraction;
            private String code;
            private String i18nId;

            public void setFraction(String fraction) {
                this.fraction = fraction;
            }

            public void setCode(String code) {
                this.code = code;
            }

            public void setI18nId(String i18nId) {
                this.i18nId = i18nId;
            }

            public String getFraction() {
                return fraction;
            }

            public String getCode() {
                return code;
            }

            public String getI18nId() {
                return i18nId;
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeString(this.fraction);
                dest.writeString(this.code);
                dest.writeString(this.i18nId);
            }

            public SettleCurrencyEntity() {
            }

            private SettleCurrencyEntity(Parcel in) {
                this.fraction = in.readString();
                this.code = in.readString();
                this.i18nId = in.readString();
            }

            public static final Parcelable.Creator<SettleCurrencyEntity> CREATOR = new Parcelable.Creator<SettleCurrencyEntity>() {
                public SettleCurrencyEntity createFromParcel(Parcel source) {
                    return new SettleCurrencyEntity(source);
                }

                public SettleCurrencyEntity[] newArray(int size) {
                    return new SettleCurrencyEntity[size];
                }
            };
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.accountNumber);
            dest.writeString(this.nickName);
            dest.writeString(this.marginAccountNo);
            dest.writeString(this.settleCurrency);
            dest.writeString(this.subAcctType);
            dest.writeString(this.signDate);
            dest.writeByte(isCurrentAccount ? (byte) 1 : (byte) 0);
            dest.writeByte(isChecked ? (byte) 1 : (byte) 0);
            dest.writeParcelable(this.settleCurrency2, 0);
            dest.writeSerializable(this.marginNetBalance);
            dest.writeSerializable(this.marginFund);
            dest.writeString(this.profitLossFlag);
            dest.writeSerializable(this.currentProfitLoss);
            dest.writeSerializable(this.openPosition);
            dest.writeSerializable(this.marginOccupied);
            dest.writeSerializable(this.marginAvailable);
            dest.writeSerializable(this.maxTradeAmount);
            dest.writeSerializable(this.maxDrawAmount);
            dest.writeSerializable(this.marginRate);
            dest.writeString(this.alarmFlag);
            dest.writeSerializable(this.cashBanlance);
            dest.writeSerializable(this.remitBanlance);
        }

        public BailItemEntity() {
        }

        private BailItemEntity(Parcel in) {
            this.accountNumber = in.readString();
            this.nickName = in.readString();
            this.marginAccountNo = in.readString();
            this.settleCurrency = in.readString();
            this.subAcctType = in.readString();
            this.signDate = in.readString();
            this.isCurrentAccount = in.readByte() != 0;
            this.isChecked = in.readByte() != 0;
            this.settleCurrency2 = in.readParcelable(SettleCurrencyEntity.class.getClassLoader());
            this.marginNetBalance = (BigDecimal) in.readSerializable();
            this.marginFund = (BigDecimal) in.readSerializable();
            this.profitLossFlag = in.readString();
            this.currentProfitLoss = (BigDecimal) in.readSerializable();
            this.openPosition = (BigDecimal) in.readSerializable();
            this.marginOccupied = (BigDecimal) in.readSerializable();
            this.marginAvailable = (BigDecimal) in.readSerializable();
            this.maxTradeAmount = (BigDecimal) in.readSerializable();
            this.maxDrawAmount = (BigDecimal) in.readSerializable();
            this.marginRate = (BigDecimal) in.readSerializable();
            this.alarmFlag = in.readString();
            this.cashBanlance = (BigDecimal) in.readSerializable();
            this.remitBanlance = (BigDecimal) in.readSerializable();
        }

        public static final Parcelable.Creator<BailItemEntity> CREATOR = new Parcelable.Creator<BailItemEntity>() {
            public BailItemEntity createFromParcel(Parcel source) {
                return new BailItemEntity(source);
            }

            public BailItemEntity[] newArray(int size) {
                return new BailItemEntity[size];
            }
        };
    }
}
