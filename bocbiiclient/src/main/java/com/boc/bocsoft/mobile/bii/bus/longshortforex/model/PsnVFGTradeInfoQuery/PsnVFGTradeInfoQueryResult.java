package com.boc.bocsoft.mobile.bii.bus.longshortforex.model.PsnVFGTradeInfoQuery;

import org.threeten.bp.LocalDateTime;

import java.util.List;

/**
 * Created by zc7067 on 2016/11/17.
 *
 * @des 双向宝交易查询  012 PsnVFGTradeInfoQuery
 */
public class PsnVFGTradeInfoQueryResult {
    /**
     * recordNumber : 33
     * List : [{"liquidationNo":"123","firstCustomerRate":123,"thirdCustomerRate":null,"afterLiquidMarginNeeded":"3","dueDate":"2012/07/04 01:29:30","openPositionFlag":"N2","liquidationPrice":"1","orderStatus":"N","channelType":"N","secondType":null,"liquidationDate":"2012/07/04 01:29:30","beforeLiquidMarginNetValue":"1","settleCurrecny":"001","exchangeRate":null,"afterLiquidRatio":"2","unClosedBalance":123,"profitLossFlag":null,"currentProfitLoss":null,"internalSeq":"2","thirdType":null,"closedPositionFlag":null,"direction":"B","amount":123,"consignNumber":"123","exchangeTransDate":null,"exchangeTranType":"FO","firstType":"P","afterLiquidMarginNetValue":"2","profitLoss":"2","oldTradePrice":"1","beforeLiquidRatio":"1","oldExchangeSeq":"123.00","beforeLiquidMarginNeeded":"1","cashRemit":null,"currency1":{"code":"028","i18nId":null,"fraction":null},"currency2":{"code":"027","i18nId":null,"fraction":null},"liquidationAmount":"1234","secondCustomerRate":null,"paymentDate":"2012/06/26 00:00:00","txnDate":null},{"liquidationNo":"1234","firstCustomerRate":123,"thirdCustomerRate":null,"afterLiquidMarginNeeded":"3","dueDate":"2012/07/04 01:29:30","openPositionFlag":"Y","liquidationPrice":"1","orderStatus":"S","channelType":"N","secondType":null,"liquidationDate":"2012/07/04 01:29:30","beforeLiquidMarginNetValue":"1","settleCurrecny":"014","exchangeRate":null,"afterLiquidRatio":"2","unClosedBalance":12,"profitLossFlag":null,"currentProfitLoss":null,"internalSeq":"2","thirdType":null,"closedPositionFlag":null,"direction":"B","amount":123,"consignNumber":"123","exchangeTransDate":null,"exchangeTranType":"LI","firstType":"P","afterLiquidMarginNetValue":"2","profitLoss":"2","oldTradePrice":"1","beforeLiquidRatio":"1","oldExchangeSeq":"123.00","beforeLiquidMarginNeeded":"1","cashRemit":null,"currency1":{"code":"014","i18nId":null,"fraction":null},"currency2":{"code":"013","i18nId":null,"fraction":null},"liquidationAmount":"1234","secondCustomerRate":null,"paymentDate":"2012/06/26 00:00:00","txnDate":null}]
     */
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

    public static class XpadPsnVFGTradeInfoQueryResultEntity {

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

        public void setCurrency1(Currency1Entity currency1) {
            this.currency1 = currency1;
        }

        public Currency1Entity getCurrency1() {
            return currency1;
        }

        public void setCurrency2(Currency2Entity currency2) {
            this.currency2 = currency2;
        }

        public Currency2Entity getCurrency2() {
            return currency2;
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
        public static class Currency1Entity {
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
        }

        public static class Currency2Entity {
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
        }
        public static class FundCurrencyEntity {
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
        }
    }

}
