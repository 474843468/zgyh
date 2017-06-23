package com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.mypurchase.model;

import java.util.List;

/**
 * Created by zc7067 on 2016/12/20.
 *
 * 双向宝——我的持仓信息model
 */
public class XpadPsnVFGPositionInfoModel {
   /* *//**
     * details : [{"method":"PsnVFGPositionInfo","id":"12","status":"01","result":[{"details":[{"currency1":{"code":"001","fraction":null,"i18nId":null},"currency2":{"code":"014","fraction":null,"i18nId":null},"balance":10,"direction":"B","profitLossFlag":"L","currentProfitLoss":46.5,"meanPrice":349.95,"marketPrice":345.3}],"settleCurrency":{"code":"014","fraction":null,"i18nId":null},"marginAccountNo":"100680215637","marginAccountName":null},{"details":[{"currency1":{"code":"001","fraction":null,"i18nId":null},"currency2":{"code":"014","fraction":null,"i18nId":null},"balance":10,"direction":"B","profitLossFlag":"L","currentProfitLoss":46.5,"meanPrice":349.95,"marketPrice":345.3}],"settleCurrency":{"code":"014","fraction":null,"i18nId":null},"marginAccountNo":"100680215637","marginAccountName":null}],"error":null}]
     * header : {"local":"ZH_cn"}
     *//*

    private HeaderBean header;
    private List<DetailsEntity> details;

    public HeaderBean getHeader() {
        return header;
    }

    public void setHeader(HeaderBean header) {
        this.header = header;
    }

    public List<DetailsEntity> getResponse() {
        return details;
    }

    public void setResponse(List<DetailsEntity> details) {
        this.details = details;
    }

    public static class HeaderBean {
        *//**
         * local : ZH_cn
         *//*

        private String local;

        public String getLocal() {
            return local;
        }

        public void setLocal(String local) {
            this.local = local;
        }
    }

    public static class DetailsEntity {
        *//**
         * method : PsnVFGPositionInfo
         * id : 12
         * status : 01
         * result : [{"details":[{"currency1":{"code":"001","fraction":null,"i18nId":null},"currency2":{"code":"014","fraction":null,"i18nId":null},"balance":10,"direction":"B","profitLossFlag":"L","currentProfitLoss":46.5,"meanPrice":349.95,"marketPrice":345.3}],"settleCurrency":{"code":"014","fraction":null,"i18nId":null},"marginAccountNo":"100680215637","marginAccountName":null},{"details":[{"currency1":{"code":"001","fraction":null,"i18nId":null},"currency2":{"code":"014","fraction":null,"i18nId":null},"balance":10,"direction":"B","profitLossFlag":"L","currentProfitLoss":46.5,"meanPrice":349.95,"marketPrice":345.3}],"settleCurrency":{"code":"014","fraction":null,"i18nId":null},"marginAccountNo":"100680215637","marginAccountName":null}]
         * error : null
         *//*

        private String method;
        private String           id;
        private String           status;
        private Object           error;
        private List<ResultBean> result;

        public String getMethod() {
            return method;
        }

        public void setMethod(String method) {
            this.method = method;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public Object getError() {
            return error;
        }

        public void setError(Object error) {
            this.error = error;
        }

        public List<ResultBean> getResult() {
            return result;
        }

        public void setResult(List<ResultBean> result) {
            this.result = result;
        }

        public static class ResultBean {
            *//**
             * details : [{"currency1":{"code":"001","fraction":null,"i18nId":null},"currency2":{"code":"014","fraction":null,"i18nId":null},"balance":10,"direction":"B","profitLossFlag":"L","currentProfitLoss":46.5,"meanPrice":349.95,"marketPrice":345.3}]
             * settleCurrency : {"code":"014","fraction":null,"i18nId":null}
             * marginAccountNo : 100680215637
             * marginAccountName : null
             *//*
            //结算币种
            private SettleCurrencyBean settleCurrency;
            private String            marginAccountNo;
            private Object            marginAccountName;
            private List<DetailsBean> details;

            public SettleCurrencyBean getSettleCurrency() {
                return settleCurrency;
            }

            public void setSettleCurrency(SettleCurrencyBean settleCurrency) {
                this.settleCurrency = settleCurrency;
            }

            public String getMarginAccountNo() {
                return marginAccountNo;
            }

            public void setMarginAccountNo(String marginAccountNo) {
                this.marginAccountNo = marginAccountNo;
            }

            public Object getMarginAccountName() {
                return marginAccountName;
            }

            public void setMarginAccountName(Object marginAccountName) {
                this.marginAccountName = marginAccountName;
            }

            public List<DetailsBean> getDetails() {
                return details;
            }

            public void setDetails(List<DetailsBean> details) {
                this.details = details;
            }

            public static class SettleCurrencyBean {
                *//**
                 * code : 014
                 * fraction : null
                 * i18nId : null
                 *//*

                private String code;
                private Object fraction;
                private Object i18nId;

                public String getCode() {
                    return code;
                }

                public void setCode(String code) {
                    this.code = code;
                }

                public Object getFraction() {
                    return fraction;
                }

                public void setFraction(Object fraction) {
                    this.fraction = fraction;
                }

                public Object getI18nId() {
                    return i18nId;
                }

                public void setI18nId(Object i18nId) {
                    this.i18nId = i18nId;
                }
            }

            public static class DetailsBean {
                *//**
                 * currency1 : {"code":"001","fraction":null,"i18nId":null}
                 * currency2 : {"code":"014","fraction":null,"i18nId":null}
                 * balance : 10.0
                 * direction : B
                 * profitLossFlag : L
                 * currentProfitLoss : 46.5
                 * meanPrice : 349.95
                 * marketPrice : 345.3
                 *//*

                private Currency1Bean currency1;
                private Currency2Bean currency2;
                private double        balance;
                private String        direction;
                private String        profitLossFlag;
                private double        currentProfitLoss;
                private double        meanPrice;
                private double        marketPrice;

                public Currency1Bean getCurrency1() {
                    return currency1;
                }

                public void setCurrency1(Currency1Bean currency1) {
                    this.currency1 = currency1;
                }

                public Currency2Bean getCurrency2() {
                    return currency2;
                }

                public void setCurrency2(Currency2Bean currency2) {
                    this.currency2 = currency2;
                }

                public double getBalance() {
                    return balance;
                }

                public void setBalance(double balance) {
                    this.balance = balance;
                }

                public String getDirection() {
                    return direction;
                }

                public void setDirection(String direction) {
                    this.direction = direction;
                }

                public String getProfitLossFlag() {
                    return profitLossFlag;
                }

                public void setProfitLossFlag(String profitLossFlag) {
                    this.profitLossFlag = profitLossFlag;
                }

                public double getCurrentProfitLoss() {
                    return currentProfitLoss;
                }

                public void setCurrentProfitLoss(double currentProfitLoss) {
                    this.currentProfitLoss = currentProfitLoss;
                }

                public double getMeanPrice() {
                    return meanPrice;
                }

                public void setMeanPrice(double meanPrice) {
                    this.meanPrice = meanPrice;
                }

                public double getMarketPrice() {
                    return marketPrice;
                }

                public void setMarketPrice(double marketPrice) {
                    this.marketPrice = marketPrice;
                }

                public static class Currency1Bean {
                    *//**
                     * code : 001
                     * fraction : null
                     * i18nId : null
                     *//*

                    private String code;
                    private Object fraction;
                    private Object i18nId;

                    public String getCode() {
                        return code;
                    }

                    public void setCode(String code) {
                        this.code = code;
                    }

                    public Object getFraction() {
                        return fraction;
                    }

                    public void setFraction(Object fraction) {
                        this.fraction = fraction;
                    }

                    public Object getI18nId() {
                        return i18nId;
                    }

                    public void setI18nId(Object i18nId) {
                        this.i18nId = i18nId;
                    }
                }

                public static class Currency2Bean {
                    *//**
                     * code : 014
                     * fraction : null
                     * i18nId : null
                     *//*

                    private String code;
                    private Object fraction;
                    private Object i18nId;

                    public String getCode() {
                        return code;
                    }

                    public void setCode(String code) {
                        this.code = code;
                    }

                    public Object getFraction() {
                        return fraction;
                    }

                    public void setFraction(Object fraction) {
                        this.fraction = fraction;
                    }

                    public Object getI18nId() {
                        return i18nId;
                    }

                    public void setI18nId(Object i18nId) {
                        this.i18nId = i18nId;
                    }
                }
            }
        }
    }*/
    

 /**
     * settleCurrency : {"code":"014","i18nId":null,"fraction":null}
     * details : [{"currency1":{"code":"001","i18nId":null,"fraction":null},"marketPrice":345.3,"currency2":{"code":"014","i18nId":null,"fraction":null},"balance":10,"profitLossFlag":"L","currentProfitLoss":46.5,"meanPrice":349.95,"direction":"B"}]
     * marginAccountName : null
     * marginAccountNo : 100680215637
     */
    //结算币种
    private SettleCurrencyEntity settleCurrency;
    //持仓详情
    private List<DetailsEntity>  details;
    //保证金账户名称
    private String               marginAccountName;
    //保证金账号
    private String               marginAccountNo;



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
