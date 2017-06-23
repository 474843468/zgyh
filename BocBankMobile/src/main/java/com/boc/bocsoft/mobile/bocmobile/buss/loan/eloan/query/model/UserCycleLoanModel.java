package com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.query.model;

import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnELOANCycleLoanAccountListQuery.PsnCycleLoanAccountEQueryResult;

import java.io.Serializable;
import java.util.List;

/**
 * Created by huixiaobo on 2016/6/22.
 * 个人循环贷款
 */
public class UserCycleLoanModel implements Serializable {

        /**贷款账户状态*/
        private String loanAccountStats;
        /**贷款余额*/
        private String loanCycleBalance;
        /**贷款账号*/
        private String accountNumber;

        public void setAccountNumber(String accountNumber) {
            this.accountNumber = accountNumber;
        }

        public String getAccountNumber() {
            return accountNumber;
        }

        public String getLoanAccountStats() {
            return loanAccountStats;
        }

        public void setLoanAccountStats(String loanAccountStats) {
            this.loanAccountStats = loanAccountStats;
        }

        public String getLoanCycleBalance() {
            return loanCycleBalance;
        }

        public void setLoanCycleBalance(String loanCycleBalance) {
            this.loanCycleBalance = loanCycleBalance;
        }

        @Override
        public String toString() {
            return "PsnCycleLoanAccountEQueryBean{" +
                    "loanAccountStats='" + loanAccountStats + '\'' +
                    ", loanCycleBalance='" + loanCycleBalance + '\'' +
                    ", accountNumber='" + accountNumber + '\'' +
                    '}';
        }
}
