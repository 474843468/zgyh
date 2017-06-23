package com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.accountstatementquery.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.threeten.bp.LocalDateTime;

import java.util.List;

/**
 * 双向宝-对账单查询页面
 * Created by wjk7118 on 2016/11/29.
 * I50——012 PsnVFGTradeInfoQuery 双向宝交易查询
 */
public class AccountStatementQueryModel {
    //请求报文

    private String queryType;
    //结算币种 人民币、美元、欧元、港币、日元、澳元
    private String currencyCode;
    //页面大小
    private String pageSize;
    //页面索引
    private String currentIndex;
    //是否刷新
    private String _refresh;
    //会话
    private String conversationId;
    //开始时间
    private String startDate;
    //结束时间
    private String endDate;

    public void setQueryType(String queryType) {
        this.queryType = queryType;
    }

    public String getQueryType() {
        return queryType;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setPageSize(String pageSize) {
        this.pageSize = pageSize;
    }

    public String getPageSize() {
        return pageSize;
    }

    public void set_refresh(String _refresh) {
        this._refresh = _refresh;
    }

    public String get_refresh() {
        return _refresh;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public String getConversationId() {
        return conversationId;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setCurrentIndex(String currentIndex) {
        this.currentIndex = currentIndex;
    }

    public String getCurrentIndex() {
        return currentIndex;
    }
    //下面对应响应字段

    //记录总数
    private int recordNumber;
    private List<AccountStatementQueryBean> list;

    public void setRecordNumber(int recordNumber) {
        this.recordNumber = recordNumber;
    }

    public void setList(List<AccountStatementQueryBean> list) {
        this.list = list;
    }

    public int getRecordNumber() {
        return recordNumber;
    }

    public List<AccountStatementQueryBean> getList() {
        return list;
    }

    public static class AccountStatementQueryBean implements Parcelable {
        //资金变动序号
        private String fundSeq;
        //资金余额
        private String balance;
        //钞汇标识
        private String noteCashFlag;
        //转账类型:TF=资金转账,PL=损益结转保证金,TD=交易利息结转保证金
        private String fundTransferType;
        //转账方向:I=转入,O=转出
        private String transferDir;
        //转账金额
        private String transferAmount;
        //资金货币
        private FundCurrencyEntity fundCurrency;
        //交易日期
        private LocalDateTime transferDate;

        public void setFundSeq(String fundSeq) {
            this.fundSeq = fundSeq;
        }

        public String getFundSeq() {
            return fundSeq;
        }

        public void setFundTransferType(String fundTransferType) {
            this.fundTransferType = fundTransferType;
        }

        public String getFundTransferType() {
            return fundTransferType;
        }

        public void setNoteCashFlag(String noteCashFlag) {
            this.noteCashFlag = noteCashFlag;
        }

        public String getNoteCashFlag() {
            return noteCashFlag;
        }

        public void setTransferAmount(String transferAmount) {
            this.transferAmount = transferAmount;
        }

        public String getTransferAmount() {
            return transferAmount;
        }

        public void setTransferDate(LocalDateTime transferDate) {
            this.transferDate = transferDate;
        }

        public LocalDateTime getTransferDate() {
            return transferDate;
        }

        public void setTransferDir(String transferDir) {
            this.transferDir = transferDir;
        }

        public String getTransferDir() {
            return transferDir;
        }

        public void setFundCurrency(FundCurrencyEntity fundCurrency) {
            this.fundCurrency = fundCurrency;
        }

        public FundCurrencyEntity getFundCurrency() {
            return fundCurrency;
        }

        public void setBalance(String balance) {
            this.balance = balance;
        }

        public String getBalance() {
            return balance;
        }

        public  AccountStatementQueryBean(){}

        public static class FundCurrencyEntity implements Parcelable {
            /**
             * code : 001
             * i18nId : CNY
             * fraction : 2
             */
            private String code;
            private String i18nId;
            private int fraction;

            public void setCode(String code) {
                this.code = code;
            }

            public void setI18nId(String i18nId) {
                this.i18nId = i18nId;
            }

            public void setFraction(int fraction) {
                this.fraction = fraction;
            }

            public String getCode() {
                return code;
            }

            public String getI18nId() {
                return i18nId;
            }

            public int getFraction() {
                return fraction;
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeString(this.code);
                dest.writeString(this.i18nId);
                dest.writeInt(this.fraction);
            }

            public FundCurrencyEntity() {
            }

            private FundCurrencyEntity(Parcel in) {
                this.code = in.readString();
                this.i18nId = in.readString();
                this.fraction = in.readInt();
            }

            public static final Creator<FundCurrencyEntity> CREATOR = new Creator<FundCurrencyEntity>() {
                public FundCurrencyEntity createFromParcel(Parcel source) {
                    return new FundCurrencyEntity(source);
                }

                public FundCurrencyEntity[] newArray(int size) {
                    return new FundCurrencyEntity[size];
                }
            };
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.fundSeq);
            dest.writeString(this.balance);
            dest.writeString(this.noteCashFlag);
            dest.writeString(this.fundTransferType);
            dest.writeString(this.transferDir);
            dest.writeString(this.transferAmount);
            dest.writeParcelable(this.fundCurrency, 0);
            dest.writeSerializable(this.transferDate);
        }

        private AccountStatementQueryBean(Parcel in) {
            this.fundSeq = in.readString();
            this.balance = in.readString();
            this.noteCashFlag = in.readString();
            this.fundTransferType = in.readString();
            this.transferDir = in.readString();
            this.transferAmount = in.readString();
            this.fundCurrency = in.readParcelable(FundCurrencyEntity.class.getClassLoader());
            this.transferDate = (LocalDateTime) in.readSerializable();
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public static final Parcelable.Creator<AccountStatementQueryBean> CREATOR = new Parcelable.Creator<AccountStatementQueryBean>() {
            public AccountStatementQueryBean createFromParcel(Parcel source) {
                return new AccountStatementQueryBean(source);
            }

            public AccountStatementQueryBean[] newArray(int size) {
                return new AccountStatementQueryBean[size];
            }
        };
    }
}
