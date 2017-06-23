package com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnQueryCrcdRTBill;

import java.math.BigDecimal;
import java.util.List;

/**
 * 作者：xwg on 16/11/21 15:50
 * 账户详情信息列表
 */
public class PsnQueryCrcdRTBillResult {

    private List<BillInfo> crcdRTBillList;

    public List<BillInfo> getCrcdRTBillList() {
        return crcdRTBillList;
    }

    public void setCrcdRTBillList(List<BillInfo> crcdRTBillList) {
        this.crcdRTBillList = crcdRTBillList;
    }

    public static class BillInfo {

        /**
         * 币种 001=人民币元
         * 014=美元
         * 027=日元
         * 038=欧元
         * 012=英镑
         * 013=港币
         * 028=加拿大元
         * 029=澳大利亚元
         * 038=欧元
         */
        private String currency;

        /**
         * 本期账单金额
         */
        private BigDecimal billAmount;
        /**
         * 本期应还最小金额
         */
        private BigDecimal haveNotRepayAmout;
        /**
         * 本期未还金额
         */
        private BigDecimal billLimitAmout;


        public BigDecimal getBillAmount() {
            return billAmount;
        }

        public void setBillAmount(BigDecimal billAmount) {
            this.billAmount = billAmount;
        }

        public BigDecimal getBillLimitAmout() {
            return billLimitAmout;
        }

        public void setBillLimitAmout(BigDecimal billLimitAmout) {
            this.billLimitAmout = billLimitAmout;
        }

        public String getCurrency() {
            return currency;
        }

        public void setCurrency(String currency) {
            this.currency = currency;
        }

        public BigDecimal getHaveNotRepayAmout() {
            return haveNotRepayAmout;
        }

        public void setHaveNotRepayAmout(BigDecimal haveNotRepayAmout) {
            this.haveNotRepayAmout = haveNotRepayAmout;
        }

    }
}
