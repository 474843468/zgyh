package com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnOtherBankQueryForTransToAccount;

import java.util.List;

/**
 * Result:查询银行列表
 * Created by zhx on 2016/7/20
 */
public class PsnOtherBankQueryForTransToAccountResult {

    /**
     * accountOfBankList : [{"bankAlias":"中国工商银行","bankBtp":"102","bankCcpc":"1000","bankType":"CL00","bankStatus":"1","bankName":"中国工商银行","bankBpm":"中国工商银行","bankClr":"102100099996","bankCode":"102100099996"}]
     */
    private List<AccountOfBankListEntity> accountOfBankList;

    public void setAccountOfBankList(List<AccountOfBankListEntity> accountOfBankList) {
        this.accountOfBankList = accountOfBankList;
    }

    public List<AccountOfBankListEntity> getAccountOfBankList() {
        return accountOfBankList;
    }

    public static class AccountOfBankListEntity {
        /**
         * bankAlias : 中国工商银行
         * bankBtp : 102
         * bankCcpc : 1000
         * bankType : CL00
         * bankStatus : 1
         * bankName : 中国工商银行
         * bankBpm : 中国工商银行
         * bankClr : 102100099996
         * bankCode : 102100099996
         */
        /**
         * 银行名称
         */
        private String bankName;
        /**
         * 银行行号
         */
        private String bankCode;
        /**
         * 银行别名
         */
        private String bankAlias;
        /**
         * 银行类型
         */
        private String bankType;
        /**
         * 银行行别
         */
        private String bankBtp;
        /**
         * 银行行别名称
         */
        private String bankBpm;
        /**
         * 银行所属CCPC
         */
        private String bankCcpc;
        /**
         * 银行清算行号
         */
        private String bankClr;
        /**
         * 银行状态
         */
        private String bankStatus;

        public void setBankAlias(String bankAlias) {
            this.bankAlias = bankAlias;
        }

        public void setBankBtp(String bankBtp) {
            this.bankBtp = bankBtp;
        }

        public void setBankCcpc(String bankCcpc) {
            this.bankCcpc = bankCcpc;
        }

        public void setBankType(String bankType) {
            this.bankType = bankType;
        }

        public void setBankStatus(String bankStatus) {
            this.bankStatus = bankStatus;
        }

        public void setBankName(String bankName) {
            this.bankName = bankName;
        }

        public void setBankBpm(String bankBpm) {
            this.bankBpm = bankBpm;
        }

        public void setBankClr(String bankClr) {
            this.bankClr = bankClr;
        }

        public void setBankCode(String bankCode) {
            this.bankCode = bankCode;
        }

        public String getBankAlias() {
            return bankAlias;
        }

        public String getBankBtp() {
            return bankBtp;
        }

        public String getBankCcpc() {
            return bankCcpc;
        }

        public String getBankType() {
            return bankType;
        }

        public String getBankStatus() {
            return bankStatus;
        }

        public String getBankName() {
            return bankName;
        }

        public String getBankBpm() {
            return bankBpm;
        }

        public String getBankClr() {
            return bankClr;
        }

        public String getBankCode() {
            return bankCode;
        }
    }
}
