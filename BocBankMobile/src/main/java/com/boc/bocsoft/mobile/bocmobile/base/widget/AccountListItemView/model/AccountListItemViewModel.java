package com.boc.bocsoft.mobile.bocmobile.base.widget.AccountListItemView.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.boc.bocsoft.mobile.bocmobile.base.model.AccountBean;
import com.boc.bocsoft.mobile.common.utils.StringUtils;

import java.util.List;

/**
 * Created by Administrator on 2016/5/24.
 */
public class AccountListItemViewModel implements Parcelable {

    /**
     * 账户信息
     */
    private AccountBean accountBean;

    /**
     * 余额标题
     */
    private String amountTitle;

    /**
     * 开户行
     */
    private String accOpenBank;
    /**
     * 开户时间
     */
    private String accOpenDate;
    /**
     * 账户列表数据下标，（账户概览界面用到）
     */
    private int position;
    /**
     * 卡片额度列表
     */
    private List<CardAmountViewModel> cardAmountViewModelList;

    public AccountListItemViewModel() {
    }

    protected AccountListItemViewModel(Parcel in) {
        accountBean = in.readParcelable(AccountBean.class.getClassLoader());
        amountTitle = in.readString();
        accOpenBank = in.readString();
        accOpenDate = in.readString();
        position = in.readInt();
        cardAmountViewModelList = in.createTypedArrayList(CardAmountViewModel.CREATOR);
    }

    public static final Creator<AccountListItemViewModel> CREATOR =
            new Creator<AccountListItemViewModel>() {
                @Override
                public AccountListItemViewModel createFromParcel(Parcel in) {
                    return new AccountListItemViewModel(in);
                }

                @Override
                public AccountListItemViewModel[] newArray(int size) {
                    return new AccountListItemViewModel[size];
                }
            };

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        AccountListItemViewModel that = (AccountListItemViewModel) o;

        return accountBean != null ? accountBean.equals(that.accountBean)
                : that.accountBean == null;
    }

    @Override
    public int hashCode() {
        return accountBean != null ? accountBean.hashCode() : 0;
    }

    public AccountBean getAccountBean() {
        return accountBean;
    }

    public void setAccountBean(AccountBean accountBean) {
        this.accountBean = accountBean;
    }

    public String getAmountTitle() {
        return amountTitle;
    }

    public void setAmountTitle(String amountTitle) {
        this.amountTitle = amountTitle;
    }

    public int getPosition() {
        return position;
    }

    public AccountListItemViewModel setPosition(int position) {
        this.position = position;
        return this;
    }

    public List<CardAmountViewModel> getCardAmountViewModelList() {
        return cardAmountViewModelList;
    }

    public void setCardAmountViewModelList(List<CardAmountViewModel> cardAmountViewModelList) {
        this.cardAmountViewModelList = cardAmountViewModelList;
    }

    public String getAccOpenBank() {
        return accOpenBank;
    }

    public void setAccOpenBank(String accOpenBank) {
        this.accOpenBank = accOpenBank;
    }

    public String getAccOpenDate() {
        return accOpenDate;
    }

    public void setAccOpenDate(String accOpenDate) {
        this.accOpenDate = accOpenDate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(accountBean, flags);
        dest.writeString(amountTitle);
        dest.writeString(accOpenBank);
        dest.writeString(accOpenDate);
        dest.writeInt(position);
        dest.writeTypedList(cardAmountViewModelList);
    }

    /**
     * 卡片额度信息
     */
    public static class CardAmountViewModel implements Parcelable, Comparable<CardAmountViewModel> {

        /**
         * 币种
         */
        private String currencyCode;

        /**
         * 钞汇类型，01=现钞，02=现汇
         */
        private String cashRemit;
        /**
         * 可用余额
         */
        private String amount;
        /**
         * 当前余额
         */
        private String bookBalance;

        /**
         * 实时余额标志位
         * “0”-欠款
         * “1”-存款
         * “2”-余额0
         */
        private String loanBalanceLimitFlag;

        /**
         * 账户类型
         */
        private String accountType;

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

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getBookBalance() {
            return bookBalance;
        }

        public void setBookBalance(String bookBalance) {
            this.bookBalance = bookBalance;
        }

        public String getAccountType() {
            return accountType;
        }

        public void setAccountType(String accountType) {
            this.accountType = accountType;
        }

        public String getLoanBalanceLimitFlag() {
            return loanBalanceLimitFlag;
        }

        public void setLoanBalanceLimitFlag(String loanBalanceLimitFlag) {
            this.loanBalanceLimitFlag = loanBalanceLimitFlag;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.currencyCode);
            dest.writeString(this.cashRemit);
            dest.writeString(this.amount);
            dest.writeString(this.bookBalance);
            dest.writeString(this.loanBalanceLimitFlag);
            dest.writeString(this.accountType);
        }

        public CardAmountViewModel() {
        }

        protected CardAmountViewModel(Parcel in) {
            this.currencyCode = in.readString();
            this.cashRemit = in.readString();
            this.amount = in.readString();
            this.bookBalance = in.readString();
            this.loanBalanceLimitFlag = in.readString();
            this.accountType = in.readString();
        }

        public static final Creator<CardAmountViewModel> CREATOR =
                new Creator<CardAmountViewModel>() {
                    @Override
                    public CardAmountViewModel createFromParcel(
                            Parcel source) {
                        return new CardAmountViewModel(source);
                    }

                    @Override
                    public CardAmountViewModel[] newArray(
                            int size) {
                        return new CardAmountViewModel[size];
                    }
                };

        @Override
        public int compareTo(CardAmountViewModel another) {
            if (StringUtils.isEmptyOrNull(getBookBalance()))
                return 1;

            if (StringUtils.isEmptyOrNull(another.getBookBalance()))
                return -1;

            return (int) (Double.parseDouble(another.getBookBalance())-Double.parseDouble(this.getBookBalance()));
        }
    }
}
