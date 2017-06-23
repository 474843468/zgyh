package com.boc.bocsoft.mobile.bii.bus.login.model.PsnAccBocnetQueryGeneralInfo;

import java.util.List;

/**
 * Created by feib on 16/8/2.
 */
public class PsnAccBocnetQueryGeneralInfoResult {

    /**
     * waiveMemFeeStartDate : 2023-07-10
     * waiveMemFeeEndDate : 2024-07-09
     * nextMemFeeDate : 2024-12-10
     * acctNum : 4096688381456341
     * acctName : 李小小
     * productName : VISA金卡
     * acctBank : 中国银行陕西省分行
     * startDate : 2023-07-10
     * carFlag : 1
     * billDate : 10
     * carAvaiDate : 2028-07-31
     * dueDate : 2024-10-30
     * carStatus :
     * annualFee : 1
     * actList : [{"currentBalance":51485.66,"currency":"001","realTimeBalance":51485.66,"billAmout":51485.66,"haveNotRepayAmout":51485.66,"billLimitAmout":26635,"dividedPayLimit":30000,"dividedPayAvaiBalance":0,"cashLimit":9000,"cashBalance":0,"totalLimt":30000,"toltalBalance":0,"rtBalanceFlag":"0","currentBalanceFlag":"1"},{"currentBalance":8088.56,"currency":"014","realTimeBalance":4500.45,"billAmout":0,"haveNotRepayAmout":0,"billLimitAmout":0,"dividedPayLimit":5010,"dividedPayAvaiBalance":4500.45,"cashLimit":1500,"cashBalance":4500.45,"totalLimt":5010,"toltalBalance":4500.45,"rtBalanceFlag":"1","currentBalanceFlag":"0"}]
     */

    private String waiveMemFeeStartDate;
    private String waiveMemFeeEndDate;
    private String nextMemFeeDate;
    private String acctNum;
    private String acctName;
    private String productName;
    private String acctBank;
    private String startDate;
    private String carFlag;
    private String billDate;
    private String carAvaiDate;
    private String dueDate;
    private String carStatus;
    private String annualFee;
    /**
     * currentBalance : 51485.66
     * currency : 001
     * realTimeBalance : 51485.66
     * billAmout : 51485.66
     * haveNotRepayAmout : 51485.66
     * billLimitAmout : 26635.0
     * dividedPayLimit : 30000.0
     * dividedPayAvaiBalance : 0.0
     * cashLimit : 9000.0
     * cashBalance : 0.0
     * totalLimt : 30000.0
     * toltalBalance : 0.0
     * rtBalanceFlag : 0
     * currentBalanceFlag : 1
     */

    private List<ActListBean> actList;

    public String getWaiveMemFeeStartDate() {
        return waiveMemFeeStartDate;
    }

    public void setWaiveMemFeeStartDate(String waiveMemFeeStartDate) {
        this.waiveMemFeeStartDate = waiveMemFeeStartDate;
    }

    public String getWaiveMemFeeEndDate() {
        return waiveMemFeeEndDate;
    }

    public void setWaiveMemFeeEndDate(String waiveMemFeeEndDate) {
        this.waiveMemFeeEndDate = waiveMemFeeEndDate;
    }

    public String getNextMemFeeDate() {
        return nextMemFeeDate;
    }

    public void setNextMemFeeDate(String nextMemFeeDate) {
        this.nextMemFeeDate = nextMemFeeDate;
    }

    public String getAcctNum() {
        return acctNum;
    }

    public void setAcctNum(String acctNum) {
        this.acctNum = acctNum;
    }

    public String getAcctName() {
        return acctName;
    }

    public void setAcctName(String acctName) {
        this.acctName = acctName;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getAcctBank() {
        return acctBank;
    }

    public void setAcctBank(String acctBank) {
        this.acctBank = acctBank;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getCarFlag() {
        return carFlag;
    }

    public void setCarFlag(String carFlag) {
        this.carFlag = carFlag;
    }

    public String getBillDate() {
        return billDate;
    }

    public void setBillDate(String billDate) {
        this.billDate = billDate;
    }

    public String getCarAvaiDate() {
        return carAvaiDate;
    }

    public void setCarAvaiDate(String carAvaiDate) {
        this.carAvaiDate = carAvaiDate;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public String getCarStatus() {
        return carStatus;
    }

    public void setCarStatus(String carStatus) {
        this.carStatus = carStatus;
    }

    public String getAnnualFee() {
        return annualFee;
    }

    public void setAnnualFee(String annualFee) {
        this.annualFee = annualFee;
    }

    public List<ActListBean> getActList() {
        return actList;
    }

    public void setActList(List<ActListBean> actList) {
        this.actList = actList;
    }

    public static class ActListBean {
        private String currentBalance;
        private String currency;
        private String realTimeBalance;
        private String billAmout;
        private String haveNotRepayAmout;
        private String billLimitAmout;
        private String dividedPayLimit;
        private String dividedPayAvaiBalance;
        private String cashLimit;
        private String cashBalance;
        private String totalLimt;
        private String toltalBalance;
        private String rtBalanceFlag;
        private String currentBalanceFlag;

        public String getCurrentBalance() {
            return currentBalance;
        }

        public void setCurrentBalance(String currentBalance) {
            this.currentBalance = currentBalance;
        }

        public String getCurrency() {
            return currency;
        }

        public void setCurrency(String currency) {
            this.currency = currency;
        }

        public String getRealTimeBalance() {
            return realTimeBalance;
        }

        public void setRealTimeBalance(String realTimeBalance) {
            this.realTimeBalance = realTimeBalance;
        }

        public String getBillAmout() {
            return billAmout;
        }

        public void setBillAmout(String billAmout) {
            this.billAmout = billAmout;
        }

        public String getHaveNotRepayAmout() {
            return haveNotRepayAmout;
        }

        public void setHaveNotRepayAmout(String haveNotRepayAmout) {
            this.haveNotRepayAmout = haveNotRepayAmout;
        }

        public String getBillLimitAmout() {
            return billLimitAmout;
        }

        public void setBillLimitAmout(String billLimitAmout) {
            this.billLimitAmout = billLimitAmout;
        }

        public String getDividedPayLimit() {
            return dividedPayLimit;
        }

        public void setDividedPayLimit(String dividedPayLimit) {
            this.dividedPayLimit = dividedPayLimit;
        }

        public String getDividedPayAvaiBalance() {
            return dividedPayAvaiBalance;
        }

        public void setDividedPayAvaiBalance(String dividedPayAvaiBalance) {
            this.dividedPayAvaiBalance = dividedPayAvaiBalance;
        }

        public String getCashLimit() {
            return cashLimit;
        }

        public void setCashLimit(String cashLimit) {
            this.cashLimit = cashLimit;
        }

        public String getCashBalance() {
            return cashBalance;
        }

        public void setCashBalance(String cashBalance) {
            this.cashBalance = cashBalance;
        }

        public String getTotalLimt() {
            return totalLimt;
        }

        public void setTotalLimt(String totalLimt) {
            this.totalLimt = totalLimt;
        }

        public String getToltalBalance() {
            return toltalBalance;
        }

        public void setToltalBalance(String toltalBalance) {
            this.toltalBalance = toltalBalance;
        }

        public String getRtBalanceFlag() {
            return rtBalanceFlag;
        }

        public void setRtBalanceFlag(String rtBalanceFlag) {
            this.rtBalanceFlag = rtBalanceFlag;
        }

        public String getCurrentBalanceFlag() {
            return currentBalanceFlag;
        }

        public void setCurrentBalanceFlag(String currentBalanceFlag) {
            this.currentBalanceFlag = currentBalanceFlag;
        }
    }
}
