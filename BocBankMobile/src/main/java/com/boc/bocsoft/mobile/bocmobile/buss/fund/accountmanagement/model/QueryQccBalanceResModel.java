package com.boc.bocsoft.mobile.bocmobile.buss.fund.accountmanagement.model;

/**
 * 由080接口 PsnFincQueryQccBalance 查询资金账户余额
 * 上送参数：空
 * 返回参数：
 * Created by lyf7084 on 2016/12/13.
 */
public class QueryQccBalanceResModel {

    private QccBalanceAEntity qccBalanceA;
    private QccBalanceBEntity qccBalanceB;

    public void setQccBalanceA(QccBalanceAEntity qccBalanceA) {
        this.qccBalanceA = qccBalanceA;
    }

    public void setQccBalanceB(QccBalanceBEntity qccBalanceB) {
        this.qccBalanceB = qccBalanceB;
    }

    public QccBalanceAEntity getQccBalanceA() {
        return qccBalanceA;
    }

    public QccBalanceBEntity getQccBalanceB() {
        return qccBalanceB;
    }

    public static class QccBalanceAEntity {
        /**
         * currentBalance : 10000
         * currency : {"code":"001","i18nId":"CNY","fraction":2}
         */
        private int currentBalance;
        private CurrencyEntity currency;

        public void setCurrentBalance(int currentBalance) {
            this.currentBalance = currentBalance;
        }

        public void setCurrency(CurrencyEntity currency) {
            this.currency = currency;
        }

        public int getCurrentBalance() {
            return currentBalance;
        }

        public CurrencyEntity getCurrency() {
            return currency;
        }

        public static class CurrencyEntity {
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

    public static class QccBalanceBEntity {
        /**
         * currentBalance : 10000
         * currency : {"code":"001","i18nId":"CNY","fraction":2}
         */
        private int currentBalance;
        private CurrencyEntity currency;

        public void setCurrentBalance(int currentBalance) {
            this.currentBalance = currentBalance;
        }

        public void setCurrency(CurrencyEntity currency) {
            this.currency = currency;
        }

        public int getCurrentBalance() {
            return currentBalance;
        }

        public CurrencyEntity getCurrency() {
            return currency;
        }

        public static class CurrencyEntity {
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
