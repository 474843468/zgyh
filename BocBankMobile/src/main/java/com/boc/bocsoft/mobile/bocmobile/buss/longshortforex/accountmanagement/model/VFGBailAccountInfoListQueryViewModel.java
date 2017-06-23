package com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.accountmanagement.model;

import java.math.BigDecimal;
import java.util.List;

/**
 * Result：双向宝保证金账户基本信息多笔查询
 * Created by zhx on 2016/11/22
 */
public class VFGBailAccountInfoListQueryViewModel {
    private List<VFGBailAccountInfo> list;

    public void setList(List<VFGBailAccountInfo> list) {
        this.list = list;
    }

    public List<VFGBailAccountInfo> getList() {
        return list;
    }

    public static class VFGBailAccountInfo {
        /**
         * maxDrawAmount : 0
         * openPosition : 0
         * remitBanlance : 0
         * marginAvailable : 0
         * profitLossFlag : P
         * marginOccupied : 0
         * maxTradeAmount : 0
         * alarmFlag : N
         * marginFund : 0
         * currentProfitLoss : 0
         * cashBanlance : 0
         * marginAccountNo : 00000102058415234
         * settleCurrency2 : {"fraction":null,"code":"014","i18nId":null}
         * marginNetBalance : 0
         * marginRate : 99999.99999
         */

        // 保证金帐号
        private String marginAccountNo;
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

        public void setMaxDrawAmount(BigDecimal maxDrawAmount) {
            this.maxDrawAmount = maxDrawAmount;
        }

        public void setOpenPosition(BigDecimal openPosition) {
            this.openPosition = openPosition;
        }

        public void setRemitBanlance(BigDecimal remitBanlance) {
            this.remitBanlance = remitBanlance;
        }

        public void setMarginAvailable(BigDecimal marginAvailable) {
            this.marginAvailable = marginAvailable;
        }

        public void setProfitLossFlag(String profitLossFlag) {
            this.profitLossFlag = profitLossFlag;
        }

        public void setMarginOccupied(BigDecimal marginOccupied) {
            this.marginOccupied = marginOccupied;
        }

        public void setMaxTradeAmount(BigDecimal maxTradeAmount) {
            this.maxTradeAmount = maxTradeAmount;
        }

        public void setAlarmFlag(String alarmFlag) {
            this.alarmFlag = alarmFlag;
        }

        public void setMarginFund(BigDecimal marginFund) {
            this.marginFund = marginFund;
        }

        public void setCurrentProfitLoss(BigDecimal currentProfitLoss) {
            this.currentProfitLoss = currentProfitLoss;
        }

        public void setCashBanlance(BigDecimal cashBanlance) {
            this.cashBanlance = cashBanlance;
        }

        public void setMarginAccountNo(String marginAccountNo) {
            this.marginAccountNo = marginAccountNo;
        }

        public void setSettleCurrency2(SettleCurrencyEntity settleCurrency2) {
            this.settleCurrency2 = settleCurrency2;
        }

        public void setMarginNetBalance(BigDecimal marginNetBalance) {
            this.marginNetBalance = marginNetBalance;
        }

        public void setMarginRate(BigDecimal marginRate) {
            this.marginRate = marginRate;
        }

        public BigDecimal getMaxDrawAmount() {
            return maxDrawAmount;
        }

        public BigDecimal getOpenPosition() {
            return openPosition;
        }

        public BigDecimal getRemitBanlance() {
            return remitBanlance;
        }

        public BigDecimal getMarginAvailable() {
            return marginAvailable;
        }

        public String getProfitLossFlag() {
            return profitLossFlag;
        }

        public BigDecimal getMarginOccupied() {
            return marginOccupied;
        }

        public BigDecimal getMaxTradeAmount() {
            return maxTradeAmount;
        }

        public String getAlarmFlag() {
            return alarmFlag;
        }

        public BigDecimal getMarginFund() {
            return marginFund;
        }

        public BigDecimal getCurrentProfitLoss() {
            return currentProfitLoss;
        }

        public BigDecimal getCashBanlance() {
            return cashBanlance;
        }

        public String getMarginAccountNo() {
            return marginAccountNo;
        }

        public SettleCurrencyEntity getSettleCurrency2() {
            return settleCurrency2;
        }

        public BigDecimal getMarginNetBalance() {
            return marginNetBalance;
        }

        public BigDecimal getMarginRate() {
            return marginRate;
        }

        public static class SettleCurrencyEntity {
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
        }
    }
}
