package com.boc.bocsoft.mobile.bii.bus.longshortforex.model.PsnVFGPositionInfo;

import java.util.List;

/**
 * 双向宝持仓信息
 * Created by gengjunying on 2016/11/22.
 */
public class PsnVFGPositionInfoResult {


    /**
     * settleCurrency : {"code":"014","i18nId":null,"fraction":null}
     * details : [{"currency1":{"code":"001","i18nId":null,"fraction":null},"marketPrice":345.3,"currency2":{"code":"014","i18nId":null,"fraction":null},"balance":10,"profitLossFlag":"L","currentProfitLoss":46.5,"meanPrice":349.95,"direction":"B"}]
     * marginAccountName : null
     * marginAccountNo : 100680215637
     */
    //结算币种
    private SettleCurrencyEntity settleCurrency;
    //持仓详情
    private List<DetailsEntity> details;
    //保证金账户名称
    private String marginAccountName;
    //保证金账号
    private String marginAccountNo;

    public void setSettleCurrency(SettleCurrencyEntity settleCurrency) {
        this.settleCurrency = settleCurrency;
    }

    public void setDetails(List<DetailsEntity> details) {
        this.details = details;
    }

    public void setMarginAccountName(String marginAccountName) {
        this.marginAccountName = marginAccountName;
    }

    public void setMarginAccountNo(String marginAccountNo) {
        this.marginAccountNo = marginAccountNo;
    }

    public SettleCurrencyEntity getSettleCurrency() {
        return settleCurrency;
    }

    public List<DetailsEntity> getDetails() {
        return details;
    }

    public String getMarginAccountName() {
        return marginAccountName;
    }

    public String getMarginAccountNo() {
        return marginAccountNo;
    }

    public static class SettleCurrencyEntity {
        /**
         * code : 014
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

    public static class DetailsEntity {
        /**
         * currency1 : {"code":"001","i18nId":null,"fraction":null}
         * marketPrice : 345.3
         * currency2 : {"code":"014","i18nId":null,"fraction":null}
         * balance : 10
         * profitLossFlag : L
         * currentProfitLoss : 46.5
         * meanPrice : 349.95
         * direction : B
         */
        //第一货币
        private Currency1Entity currency1;
        //市场价格
        private double marketPrice;
        //第二货币
        private Currency2Entity currency2;
        //持仓余额
        private int balance;
        //暂计盈亏标识
        private String profitLossFlag;
        //账户暂计盈亏
        private double currentProfitLoss;
        //平均价格
        private double meanPrice;
        //买卖方向
        private String direction;

        public void setCurrency1(Currency1Entity currency1) {
            this.currency1 = currency1;
        }

        public void setMarketPrice(double marketPrice) {
            this.marketPrice = marketPrice;
        }

        public void setCurrency2(Currency2Entity currency2) {
            this.currency2 = currency2;
        }

        public void setBalance(int balance) {
            this.balance = balance;
        }

        public void setProfitLossFlag(String profitLossFlag) {
            this.profitLossFlag = profitLossFlag;
        }

        public void setCurrentProfitLoss(double currentProfitLoss) {
            this.currentProfitLoss = currentProfitLoss;
        }

        public void setMeanPrice(double meanPrice) {
            this.meanPrice = meanPrice;
        }

        public void setDirection(String direction) {
            this.direction = direction;
        }

        public Currency1Entity getCurrency1() {
            return currency1;
        }

        public double getMarketPrice() {
            return marketPrice;
        }

        public Currency2Entity getCurrency2() {
            return currency2;
        }

        public int getBalance() {
            return balance;
        }

        public String getProfitLossFlag() {
            return profitLossFlag;
        }

        public double getCurrentProfitLoss() {
            return currentProfitLoss;
        }

        public double getMeanPrice() {
            return meanPrice;
        }

        public String getDirection() {
            return direction;
        }

        public static class Currency1Entity {
            /**
             * code : 001
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
             * code : 014
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
    }
}
