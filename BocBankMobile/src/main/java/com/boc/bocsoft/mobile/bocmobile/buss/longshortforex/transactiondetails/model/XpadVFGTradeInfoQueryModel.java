package com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.transactiondetails.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.threeten.bp.LocalDateTime;

import java.util.List;

/**
 * 双向宝交易查询
 * Created by zc7067 on 2016/11/17.
 * I50——012 PsnVFGTradeInfoQuery 双向宝交易查询
 */
public class XpadVFGTradeInfoQueryModel {

    //查询类型，当前有效委托 "1";历史挂单"2";历史成交 "3";斩仓交易查询"4";未平仓交易"5";对账单"6";
    private String queryType;
    //结算币种 人民币、美元、欧元、港币、日元、澳元
    private String currencyCode;
    //页面大小
    private String pageSize;
    //是否刷新
    private String _refresh;
    //页面索引
    private String currentIndex;
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

    public void setCurrentIndex(String currentIndex) {
        this.currentIndex = currentIndex;
    }

    public String getCurrentIndex() {
        return currentIndex;
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
    //下面对应响应字段

    //记录总数
    private int recordNumber;
    private List<XpadPsnVFGTradeInfoQueryResultEntity> List;

    public void setRecordNumber(int recordNumber) {
        this.recordNumber = recordNumber;
    }

    public void setList(List<XpadPsnVFGTradeInfoQueryResultEntity> List) {
        this.List = List;
    }

    public int getRecordNumber() {
        return recordNumber;
    }

    public List<XpadPsnVFGTradeInfoQueryResultEntity> getList() {
        return List;
    }

    public static class XpadPsnVFGTradeInfoQueryResultEntity implements Parcelable {
        /**
         * "amount": 123,
         * "paymentDate": "2012/06/26 00:00:00",
         * "cashRemit": null,
         * currency1 : {"code":"028","i18nId":null,"fraction":null}
         * currency2 : {"code":"027","i18nId":null,"fraction":null}
         * "direction": "B",
         * "exchangeTranType": "LI",
         * "exchangeRate": null,
         * "consignNumber": "123",
         * "liquidationNo": "1234",
         * "oldExchangeSeq": "123.00",
         * "liquidationDate": "2012/07/04 01:29:30",
         * "liquidationAmount": "1234",
         * "oldTradePrice": "1",
         * "profitLoss": "2",
         * "liquidationPrice": "1",
         * "beforeLiquidMarginNetValue": "1",
         * "beforeLiquidMarginNeeded": "1",
         * "beforeLiquidRatio": "1",
         * "afterLiquidMarginNetValue": "2",
         * "afterLiquidMarginNeeded": "3",
         * "afterLiquidRatio": "2",
         *　"dueDate": "2012/07/04 01:29:30",
         ＊　"firstType": "P",
         ＊　"secondType": null,
         ＊ "channelType": "N",
         *　"exchangeTransDate": null,
         ＊　"firstCustomerRate": 123,
         ＊　"secondCustomerRate": null,
         ＊　"thirdType": null,
         ＊　"thirdCustomerRate": null,
         ＊　"txnDate": null,
         ＊　"settleCurrecny": "014",
         * "openPositionFlag": "Y",
         * "profitLossFlag": null,
         * "closedPositionFlag": null,
         * "currentProfitLoss": null,
         * "unClosedBalance": 12,
         * "internalSeq": "2",
         * "orderStatus": "S"
         *
         */
        //交易金额
        private String amount;
        //委托时间 :YYYY/MM/DD HH:MM:SS 精确到秒
        private LocalDateTime paymentDate;
        //钞汇标识
        private String cashRemit;
        //货币对
        //货币对
        private Currency1Entity currency1;
        private Currency2Entity currency2;
        //买卖方向:B=买入,S=卖出
        private String direction;
        //委托类型:MI=市价即时,LI=限价即时,PO=获利委托,SO=止损委托,OO=二选一委托,IO=追加委托,TO=连环委托,MO=多选一委托,CO=委托撤单,FO=追击止损挂单,
        private String exchangeTranType;
        //成交汇率
        private String exchangeRate;
        //挂单序号/交易序号
        private String consignNumber;
        //斩仓序号
        private String liquidationNo;
        //被斩仓交易序号
        private String oldExchangeSeq;
        //斩仓时间
        private LocalDateTime liquidationDate;
        //斩仓金额
        private String liquidationAmount;
        //被斩仓成交价格
        private String oldTradePrice;
        //实现盈亏
        private String profitLoss;
        //斩仓价格
        private String liquidationPrice;
        //斩仓前保证金净值
        private String beforeLiquidMarginNetValue;
        //斩仓前所需保证金
        private String beforeLiquidMarginNeeded;
        //斩仓前充足率
        private String beforeLiquidRatio;
        //斩仓后保证金净值
        private String afterLiquidMarginNetValue;
        //斩仓后所需保证金
        private String afterLiquidMarginNeeded;
        //斩仓后充足率
        private String afterLiquidRatio;
        //挂单到期时间: YYYY/MM/DD HH:MM:SS 精确到秒
        private LocalDateTime dueDate;
        //第一成交类型 :P=获利,S=止损,追击止损挂单时为S
        private String firstType;
        //第二成交类型
        private String secondType;
        //交易渠道:C - 柜台,P - 电话,N - 网银,A - 客户端,O –中银开放平台,S - 系统斩仓
        private String channelType;
        //成交/撤单时间
        private LocalDateTime exchangeTransDate;
        //第一客户委托汇率,如果是追击止损挂单，则是挂单时的市场价格+追击点差
        private String firstCustomerRate;
        //第二客户委托汇率
        private String secondCustomerRate;
        //第三成交类型
        private String thirdType;
        //第三客户委托汇率
        private String thirdCustomerRate;
        //挂单时间
        private LocalDateTime txnDate;
        //结算货币:001=人民币,014=美元,038=欧元,013=港币,027=日元,029=澳元
        private String settleCurrecny;
        //建仓标识:Y=Y,N=N
        private String openPositionFlag;
        //盈亏标识
        private String profitLossFlag;
        //是否已被平仓 :Y=是,N=否
        private String closedPositionFlag;
        //盈亏金额
        private String currentProfitLoss;
        //未平仓金额
        private String unClosedBalance;
        //内部序号
        private String internalSeq;
        //交易状态:N=有效,U=未生效,S=成交,R=成交,C=撤销,E=过期,O=选择失效,X=失败
        private String orderStatus;
        //被斩仓交易的成交时间
        private LocalDateTime oldTradeDate;
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

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getAmount() {
            return amount;
        }

        public void setAfterLiquidMarginNeeded(String afterLiquidMarginNeeded) {
            this.afterLiquidMarginNeeded = afterLiquidMarginNeeded;
        }

        public String getAfterLiquidMarginNeeded() {
            return afterLiquidMarginNeeded;
        }

        public void setAfterLiquidMarginNetValue(String afterLiquidMarginNetValue) {
            this.afterLiquidMarginNetValue = afterLiquidMarginNetValue;
        }

        public String getAfterLiquidMarginNetValue() {
            return afterLiquidMarginNetValue;
        }

        public void setAfterLiquidRatio(String afterLiquidRatio) {
            this.afterLiquidRatio = afterLiquidRatio;
        }

        public String getAfterLiquidRatio() {
            return afterLiquidRatio;
        }

        public void setBeforeLiquidMarginNeeded(String beforeLiquidMarginNeeded) {
            this.beforeLiquidMarginNeeded = beforeLiquidMarginNeeded;
        }

        public String getBeforeLiquidMarginNeeded() {
            return beforeLiquidMarginNeeded;
        }

        public void setBeforeLiquidMarginNetValue(String beforeLiquidMarginNetValue) {
            this.beforeLiquidMarginNetValue = beforeLiquidMarginNetValue;
        }

        public String getBeforeLiquidMarginNetValue() {
            return beforeLiquidMarginNetValue;
        }

        public void setBeforeLiquidRatio(String beforeLiquidRatio) {
            this.beforeLiquidRatio = beforeLiquidRatio;
        }

        public String getBeforeLiquidRatio() {
            return beforeLiquidRatio;
        }

        public void setCashRemit(String cashRemit) {
            this.cashRemit = cashRemit;
        }

        public String getCashRemit() {
            return cashRemit;
        }

        public void setChannelType(String channelType) {
            this.channelType = channelType;
        }

        public String getChannelType() {
            return channelType;
        }

        public void setClosedPositionFlag(String closedPositionFlag) {
            this.closedPositionFlag = closedPositionFlag;
        }

        public String getClosedPositionFlag() {
            return closedPositionFlag;
        }

        public void setConsignNumber(String consignNumber) {
            this.consignNumber = consignNumber;
        }

        public String getConsignNumber() {
            return consignNumber;
        }

        public void setCurrency2(Currency2Entity currency2) {
            this.currency2 = currency2;
        }

        public void setCurrency1(Currency1Entity currency1) {
            this.currency1 = currency1;
        }

        public Currency1Entity getCurrency1() {
            return currency1;
        }

        public Currency2Entity getCurrency2() {
            return currency2;
        }

        public void setDirection(String direction) {
            this.direction = direction;
        }

        public String getDirection() {
            return direction;
        }

        public void setDueDate(LocalDateTime dueDate) {
            this.dueDate = dueDate;
        }

        public LocalDateTime getDueDate() {
            return dueDate;
        }

        public void setExchangeRate(String exchangeRate) {
            this.exchangeRate = exchangeRate;
        }

        public String getExchangeRate() {
            return exchangeRate;
        }

        public void setCurrentProfitLoss(String currentProfitLoss) {
            this.currentProfitLoss = currentProfitLoss;
        }

        public String getCurrentProfitLoss() {
            return currentProfitLoss;
        }

        public void setExchangeTransDate(LocalDateTime exchangeTransDate) {
            this.exchangeTransDate = exchangeTransDate;
        }

        public LocalDateTime getExchangeTransDate() {
            return exchangeTransDate;
        }

        public void setExchangeTranType(String exchangeTranType) {
            this.exchangeTranType = exchangeTranType;
        }

        public String getExchangeTranType() {
            return exchangeTranType;
        }

        public void setFirstCustomerRate(String firstCustomerRate) {
            this.firstCustomerRate = firstCustomerRate;
        }

        public String getFirstCustomerRate() {
            return firstCustomerRate;
        }

        public void setFirstType(String firstType) {
            this.firstType = firstType;
        }

        public String getFirstType() {
            return firstType;
        }

        public void setLiquidationAmount(String liquidationAmount) {
            this.liquidationAmount = liquidationAmount;
        }

        public String getLiquidationAmount() {
            return liquidationAmount;
        }

        public void setInternalSeq(String internalSeq) {
            this.internalSeq = internalSeq;
        }

        public String getInternalSeq() {
            return internalSeq;
        }

        public void setLiquidationDate(LocalDateTime liquidationDate) {
            this.liquidationDate = liquidationDate;
        }

        public LocalDateTime getLiquidationDate() {
            return liquidationDate;
        }

        public void setLiquidationNo(String liquidationNo) {
            this.liquidationNo = liquidationNo;
        }

        public String getLiquidationNo() {
            return liquidationNo;
        }

        public void setLiquidationPrice(String liquidationPrice) {
            this.liquidationPrice = liquidationPrice;
        }

        public String getLiquidationPrice() {
            return liquidationPrice;
        }

        public void setOldExchangeSeq(String oldExchangeSeq) {
            this.oldExchangeSeq = oldExchangeSeq;
        }

        public String getOldExchangeSeq() {
            return oldExchangeSeq;
        }

        public void setOldTradePrice(String oldTradePrice) {
            this.oldTradePrice = oldTradePrice;
        }

        public String getOldTradePrice() {
            return oldTradePrice;
        }

        public void setOpenPositionFlag(String openPositionFlag) {
            this.openPositionFlag = openPositionFlag;
        }

        public String getOpenPositionFlag() {
            return openPositionFlag;
        }

        public void setOrderStatus(String orderStatus) {
            this.orderStatus = orderStatus;
        }

        public String getOrderStatus() {
            return orderStatus;
        }

        public void setPaymentDate(LocalDateTime paymentDate) {
            this.paymentDate = paymentDate;
        }

        public LocalDateTime getPaymentDate() {
            return paymentDate;
        }

        public void setProfitLoss(String profitLoss) {
            this.profitLoss = profitLoss;
        }

        public String getProfitLoss() {
            return profitLoss;
        }

        public void setProfitLossFlag(String profitLossFlag) {
            this.profitLossFlag = profitLossFlag;
        }

        public String getProfitLossFlag() {
            return profitLossFlag;
        }

        public void setSecondCustomerRate(String secondCustomerRate) {
            this.secondCustomerRate = secondCustomerRate;
        }

        public String getSecondCustomerRate() {
            return secondCustomerRate;
        }

        public void setSecondType(String secondType) {
            this.secondType = secondType;
        }

        public String getSecondType() {
            return secondType;
        }

        public void setSettleCurrecny(String settleCurrecny) {
            this.settleCurrecny = settleCurrecny;
        }

        public String getSettleCurrecny() {
            return settleCurrecny;
        }

        public void setThirdCustomerRate(String thirdCustomerRate) {
            this.thirdCustomerRate = thirdCustomerRate;
        }

        public String getThirdCustomerRate() {
            return thirdCustomerRate;
        }

        public void setThirdType(String thirdType) {
            this.thirdType = thirdType;
        }

        public String getThirdType() {
            return thirdType;
        }

        public void setTxnDate(LocalDateTime txnDate) {
            this.txnDate = txnDate;
        }

        public LocalDateTime getTxnDate() {
            return txnDate;
        }

        public void setUnClosedBalance(String unClosedBalance) {
            this.unClosedBalance = unClosedBalance;
        }

        public String getUnClosedBalance() {
            return unClosedBalance;
        }

        public void setOldTradeDate(LocalDateTime oldTradeDate) {
            this.oldTradeDate = oldTradeDate;
        }

        public LocalDateTime getOldTradeDate() {
            return oldTradeDate;
        }

        public void setBalance(String balance) {
            this.balance = balance;
        }

        public String getBalance() {
            return balance;
        }

        public void setFundCurrency(FundCurrencyEntity fundCurrency) {
            this.fundCurrency = fundCurrency;
        }

        public FundCurrencyEntity getFundCurrency() {
            return fundCurrency;
        }
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

        public static class Currency1Entity implements Parcelable {
            /**
             * code : 028
             * i18nId : null
             * fraction : null
             */
            private String code;
            private String i18nId;
            private String fraction;

            public void setCode(String code) {
                this.code = code;
            }

            public void setI18nId(String i18nId) {
                this.i18nId = i18nId;
            }

            public void setFraction(String fraction) {
                this.fraction = fraction;
            }

            public String getCode() {
                return code;
            }

            public String getI18nId() {
                return i18nId;
            }

            public String getFraction() {
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
                dest.writeString(this.fraction);
            }

            public Currency1Entity() {
            }

            private Currency1Entity(Parcel in) {
                this.code = in.readString();
                this.i18nId = in.readString();
                this.fraction = in.readString();
            }

            public static final Creator<Currency1Entity> CREATOR = new Creator<Currency1Entity>() {
                public Currency1Entity createFromParcel(Parcel source) {
                    return new Currency1Entity(source);
                }

                public Currency1Entity[] newArray(int size) {
                    return new Currency1Entity[size];
                }
            };
        }

        public static class Currency2Entity implements Parcelable{
            /**
             * code : 027
             * i18nId : null
             * fraction : null
             */
            private String code;
            private String i18nId;
            private String fraction;

            public void setCode(String code) {
                this.code = code;
            }

            public void setI18nId(String i18nId) {
                this.i18nId = i18nId;
            }

            public void setFraction(String fraction) {
                this.fraction = fraction;
            }

            public String getCode() {
                return code;
            }

            public String getI18nId() {
                return i18nId;
            }

            public String getFraction() {
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
                dest.writeString(this.fraction);
            }

            public Currency2Entity() {
            }

            private Currency2Entity(Parcel in) {
                this.code = in.readString();
                this.i18nId = in.readString();
                this.fraction = in.readString();
            }

            public static final Creator<Currency2Entity> CREATOR = new Creator<Currency2Entity>() {
                public Currency2Entity createFromParcel(Parcel source) {
                    return new Currency2Entity(source);
                }

                public Currency2Entity[] newArray(int size) {
                    return new Currency2Entity[size];
                }
            };
        }

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

        public XpadPsnVFGTradeInfoQueryResultEntity() {
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.amount);
            dest.writeSerializable(this.paymentDate);
            dest.writeString(this.cashRemit);
            dest.writeParcelable(this.currency1, 0);
            dest.writeParcelable(this.currency2, 0);
            dest.writeString(this.direction);
            dest.writeString(this.exchangeTranType);
            dest.writeString(this.exchangeRate);
            dest.writeString(this.consignNumber);
            dest.writeString(this.liquidationNo);
            dest.writeString(this.oldExchangeSeq);
            dest.writeSerializable(this.liquidationDate);
            dest.writeString(this.liquidationAmount);
            dest.writeString(this.oldTradePrice);
            dest.writeString(this.profitLoss);
            dest.writeString(this.liquidationPrice);
            dest.writeString(this.beforeLiquidMarginNetValue);
            dest.writeString(this.beforeLiquidMarginNeeded);
            dest.writeString(this.beforeLiquidRatio);
            dest.writeString(this.afterLiquidMarginNetValue);
            dest.writeString(this.afterLiquidMarginNeeded);
            dest.writeString(this.afterLiquidRatio);
            dest.writeSerializable(this.dueDate);
            dest.writeString(this.firstType);
            dest.writeString(this.secondType);
            dest.writeString(this.channelType);
            dest.writeSerializable(this.exchangeTransDate);
            dest.writeString(this.firstCustomerRate);
            dest.writeString(this.secondCustomerRate);
            dest.writeString(this.thirdType);
            dest.writeString(this.thirdCustomerRate);
            dest.writeSerializable(this.txnDate);
            dest.writeString(this.settleCurrecny);
            dest.writeString(this.openPositionFlag);
            dest.writeString(this.profitLossFlag);
            dest.writeString(this.closedPositionFlag);
            dest.writeString(this.currentProfitLoss);
            dest.writeString(this.unClosedBalance);
            dest.writeString(this.internalSeq);
            dest.writeString(this.orderStatus);
            dest.writeSerializable(this.oldTradeDate);
            dest.writeString(this.fundSeq);
            dest.writeString(this.balance);
            dest.writeString(this.noteCashFlag);
            dest.writeString(this.fundTransferType);
            dest.writeString(this.transferDir);
            dest.writeString(this.transferAmount);
            dest.writeParcelable(this.fundCurrency, 0);
            dest.writeSerializable(this.transferDate);
        }

        private XpadPsnVFGTradeInfoQueryResultEntity(Parcel in) {
            this.amount = in.readString();
            this.paymentDate = (LocalDateTime) in.readSerializable();
            this.cashRemit = in.readString();
            this.currency1 = in.readParcelable(Currency1Entity.class.getClassLoader());
            this.currency2 = in.readParcelable(Currency2Entity.class.getClassLoader());
            this.direction = in.readString();
            this.exchangeTranType = in.readString();
            this.exchangeRate = in.readString();
            this.consignNumber = in.readString();
            this.liquidationNo = in.readString();
            this.oldExchangeSeq = in.readString();
            this.liquidationDate = (LocalDateTime) in.readSerializable();
            this.liquidationAmount = in.readString();
            this.oldTradePrice = in.readString();
            this.profitLoss = in.readString();
            this.liquidationPrice = in.readString();
            this.beforeLiquidMarginNetValue = in.readString();
            this.beforeLiquidMarginNeeded = in.readString();
            this.beforeLiquidRatio = in.readString();
            this.afterLiquidMarginNetValue = in.readString();
            this.afterLiquidMarginNeeded = in.readString();
            this.afterLiquidRatio = in.readString();
            this.dueDate = (LocalDateTime) in.readSerializable();
            this.firstType = in.readString();
            this.secondType = in.readString();
            this.channelType = in.readString();
            this.exchangeTransDate = (LocalDateTime) in.readSerializable();
            this.firstCustomerRate = in.readString();
            this.secondCustomerRate = in.readString();
            this.thirdType = in.readString();
            this.thirdCustomerRate = in.readString();
            this.txnDate = (LocalDateTime) in.readSerializable();
            this.settleCurrecny = in.readString();
            this.openPositionFlag = in.readString();
            this.profitLossFlag = in.readString();
            this.closedPositionFlag = in.readString();
            this.currentProfitLoss = in.readString();
            this.unClosedBalance = in.readString();
            this.internalSeq = in.readString();
            this.orderStatus = in.readString();
            this.oldTradeDate = (LocalDateTime) in.readSerializable();
            this.fundSeq = in.readString();
            this.balance = in.readString();
            this.noteCashFlag = in.readString();
            this.fundTransferType = in.readString();
            this.transferDir = in.readString();
            this.transferAmount = in.readString();
            this.fundCurrency = in.readParcelable(FundCurrencyEntity.class.getClassLoader());
            this.transferDate = (LocalDateTime) in.readSerializable();
        }

        public static final Creator<XpadPsnVFGTradeInfoQueryResultEntity> CREATOR = new Creator<XpadPsnVFGTradeInfoQueryResultEntity>() {
            public XpadPsnVFGTradeInfoQueryResultEntity createFromParcel(Parcel source) {
                return new XpadPsnVFGTradeInfoQueryResultEntity(source);
            }

            public XpadPsnVFGTradeInfoQueryResultEntity[] newArray(int size) {
                return new XpadPsnVFGTradeInfoQueryResultEntity[size];
            }
        };
    }

}
